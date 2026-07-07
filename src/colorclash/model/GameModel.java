package src.colorclash.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;

public class GameModel {

    private Avatar player;
    private List<Obstacle> enemies;
    
    // Variabili di stato del gioco
    private int updateCounter;
    private int lives;
    private boolean isGameOver;
    private int score;
    private int stackedTime = 0;
    private boolean isInvulnerable = false;
    private int invulnTimer = 0;
    private Random random;
    private int frameCounter;
    private int spawnInterval; // Ogni quanti frame nasce un nemico
    private double currentFallSpeed = 3.0;
    private int currentPhase = 1;
    private Color[] avaibleColors;
    private List<Particle> particles = new ArrayList<>();
    private int availableColorsCount = 2;

    //costanti
    private final int START_X = 350;
    private final int START_Y = 550;
    private final int MIN_X = 0;
    private final int MIN_Y = 0;
    private final int OBSTACLE_START_Y = -150;
    private final int START_COLOR_ID = 0;
    private final int TICK_TIME = 8;
    private final int SCORE_DELAY = 1000; // in ms
    private final int SCORE_PHASE_2 = 2500; //points
    private final int SCORE_PHASE_3 = 5000; //points
    private final double SPEED_PHASE_MULTIPLIER = 1.20;
    private final double SPEEDRACER_MULTIPLIER = 1.5;
    private final int MAX_INVULN_FRAMES = 120; // 120 frame a 60fps = 2 secondi invulerabile
    private final int DEFAULT_GAIN = 100;
    private final int MAX_CHANCE = 100;
    private final int SPEEDRACER_CHANCE_PHASE_2 = 30;
    private final int SPEEDRACER_CHANCE_PHASE_3 = 40;
    private final int SINUSOIDALMADNESS_CHANCE = 15;

    //metodi privati
    
    private void initGame() {
        this.player = new Avatar(START_X, START_Y,START_COLOR_ID); 
        this.enemies = new ArrayList<>();
        this.avaibleColors = new Color[]{Color.RED, Color.GREEN};
        this.updateCounter = 0;
        this.lives = 3;
        this.isGameOver = false;
        this.random = new Random();
        this.frameCounter = 0;
        this.spawnInterval = 62; // 125 frame = 1 secondo (se il timer è a 8ms)
    }//fine initGame
    
    private void scoreHandler(){
        this.stackedTime += TICK_TIME;

        if (this.stackedTime >= SCORE_DELAY) {
            addScore(DEFAULT_GAIN);
            this.stackedTime -= SCORE_DELAY;
        }
    }//fine scoreHandler

    private void updateEnemies(int panelHeight ) {

        for (Obstacle obs : enemies) {
            obs.fall();
            obs.checkOffScreen(panelHeight);
        }
        enemies.removeIf(obs -> !obs.isActive());
    }//fine updateEnemies

    private void invulnerabilityHandler(){

        if (isInvulnerable) {
            invulnTimer++; 

            if (invulnTimer >= MAX_INVULN_FRAMES) {
                isInvulnerable = false; 
                invulnTimer = 0;     
            }
        }
    }//fine invulnerabilityHandler

    private void handleSpawning(int panelWidth) {
        frameCounter++;
    
        if (frameCounter >= spawnInterval) {
            spawnRandomEnemy(panelWidth);
            frameCounter = 0; 
        }
    }//fine handleSpawning

    private void spawnRandomEnemy(int panelWidth) {
        int startY = OBSTACLE_START_Y;
        int randomColorId = random.nextInt(availableColorsCount); 
        Obstacle newEnemy;
        int chance = random.nextInt(MAX_CHANCE);

        switch (currentPhase) {
            case 1: 
                newEnemy = StandardObstacle.createStandardObstacle(panelWidth, startY, currentFallSpeed, randomColorId);
                break;
            
            case 2:
                if (chance < SPEEDRACER_CHANCE_PHASE_2) {
                    newEnemy = SpeedRacer.createSpeedRacerObstacle(panelWidth, startY, currentFallSpeed * SPEEDRACER_MULTIPLIER, randomColorId);
                } else {
                    newEnemy = StandardObstacle.createStandardObstacle(panelWidth, startY, currentFallSpeed, randomColorId);
                }
                break;
            
            case 3:
            default:
                if (chance < SINUSOIDALMADNESS_CHANCE) {
                    newEnemy = SinusoidalMadness.creatSinusoidalMadness(panelWidth, startY, currentFallSpeed, randomColorId);
                } else if (chance < SPEEDRACER_CHANCE_PHASE_3) {
                    newEnemy = SpeedRacer.createSpeedRacerObstacle(panelWidth, startY, currentFallSpeed * SPEEDRACER_MULTIPLIER, randomColorId);
                } else {
                    newEnemy = StandardObstacle.createStandardObstacle(panelWidth, startY, currentFallSpeed, randomColorId);
                }
                break;
        }
        enemies.add(newEnemy);
    }//fine spawnRandomEnemy

    //metodi pubblici

    public GameModel() {
        initGame();
    }//fine costruttore

    public void update(int panelWidth, int panelHeight) {

        if (isGameOver) {
            return;
        }
        invulnerabilityHandler();
        player.move();
        player.constrainX(MIN_X, panelWidth);
        player.constrainY(MIN_Y, panelHeight);
        handleSpawning(panelWidth);
        updateEnemies(panelHeight);
        checkCollisions();
        scoreHandler();
    }//fine update

    public void addScore(int points){
        this.score += points;
        checkDifficultyProgression(this.score);
    }//fine addScore

    public void checkCollisions() {
        Shape playerShape = player.getHitbox();
        Rectangle playerBounds = playerShape.getBounds(); 

        for (int i = 0; i < enemies.size(); i++) {

            Obstacle obs = enemies.get(i);
            Shape obsShape = obs.getHitbox();
            Rectangle obsBounds = obsShape.getBounds();

            if (!playerBounds.intersects(obsBounds)) {
                continue; 
            }
            Area playerArea = new Area(playerShape);
            Area obsArea = new Area(obsShape);
            playerArea.intersect(obsArea); 

            if (!playerArea.isEmpty()) {

                if(player.getColorId()==obs.getColorId()){
                    createExplosion(obs.getX(), obs.getY() - 20, obs.getColorId());
                    addScore(obs.getPoints());
                    enemies.remove(i);
                    i--; 
                }else{

                    if(!isInvulnerable){
                        decreaseLives();
                        i--; 
                        isInvulnerable=true;
                    }
                }
            }
        }
    }//fine checkCollisions

    public void checkDifficultyProgression(int currentScore)
    {

       if(currentScore >= SCORE_PHASE_2 && currentPhase == 1){
            currentPhase = 2;
            currentFallSpeed *= SPEED_PHASE_MULTIPLIER;
            avaibleColors = new Color[]{Color.CYAN, Color.GREEN, Color.RED};
            availableColorsCount = 3;
            
       }

       else if(currentScore >= SCORE_PHASE_3 && currentPhase == 2){
            currentPhase = 3;
            currentFallSpeed *= SPEED_PHASE_MULTIPLIER;
            avaibleColors = new Color[]{Color.CYAN, Color.GREEN, Color.RED, Color.ORANGE};
            availableColorsCount = 4;
       }
    }//fine checkDifficultyProgression

    public void createExplosion(int x, int y, int colorId){
        int particlesNumber = random.nextInt(5, 12+1);
        for(int i = 0; i < particlesNumber; i++){
            particles.add(new Particle(x, y, colorId));
        }
    }

    public void updateParticles(){
        for(Particle p : particles){
            p.update();
        }
        particles.removeIf(Particle::isDead);
    }

    public void decreaseLives() {

        if (lives > 0) {
            lives--;
        }
        
        if (lives <= 0) {
            isGameOver = true;
            System.out.println("GAME OVER!");
        }
    }//fine decreaseLives

    public void resetScore() {
        this.score = 0;
        this.stackedTime = 0;
        System.out.println("score resettato con successo");
    }//fine resetScore

    public void resetObstacles(){
        System.out.println("lista prima del reset: ");
        this.enemies.clear();
        this.particles.clear();
        System.out.println("lista dopo il reset: ");

    }//fine resetObstacles

    public void resetDifficulty(){
        currentFallSpeed = 3.0;
        currentPhase = 1;
        avaibleColors = new Color[]{Color.RED, Color.GREEN}; 
        availableColorsCount = 2;
    }//fine resetDifficulty

    public void resetGameover(){
        isGameOver=false;
    }//fine resetGameover
    
    public void resetLives(){
            lives=3;
    }//fine resetLives

    public void resetInvulnerability(){
        isInvulnerable=false;

    }//fine resetInvulnerability
    
    public void resetGame(){
        getPlayer().resetToInitialSettings(getStartX(), getStartY(),getStartColorId());
        resetScore();
        resetObstacles();
        resetGameover();
        resetLives();
        resetInvulnerability();
        resetDifficulty();
    }// fine resetGame

    public boolean isInvulnerable() {
        return isInvulnerable;
    }//fine isInvulnerable

    //getters del GameModel
    public int getInvulnTimer() {
        return invulnTimer;
    }
    public Avatar getPlayer() { 
        return player; 
    }
    public List<Obstacle> getEnemies() { 
        return enemies; 
    }
    public int getScore() {
        return this.score;
    }
    public int getLives() {
        return lives;
    }
    public boolean isGameOver() {
        return isGameOver;
    }
    public int getStartX(){
        return this.START_X;
    }
    public int getStartY(){
        return this.START_Y;
    }
    public int getStartColorId(){
        return this.START_COLOR_ID;
    }
    public int getAvailableColorsCount(){
        return this.availableColorsCount;
    }
    public int getPhase(){
        return this.currentPhase;
    }
    public List<Particle> getParticles(){
        return particles;
    }
}//fine classe GameModel