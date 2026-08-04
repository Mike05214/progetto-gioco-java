package src.colorclash.model;

import src.colorclash.utils.SaveManager;
import src.colorclash.utils.AudioManager;
import src.colorclash.utils.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class GameModel {

    private Player player;
    private List<Obstacle> enemies;

    // Variabili di stato del gioco
    private int lives;
    private boolean isGameOver;
    private int score;
    private int stackedTime = 0;
    private boolean isInvulnerable = false;
    private int invulnTimer = 0;
    private Random random;
    private int frameCounter = 0;
    private int spawnInterval = Config.getInstance().getIntProperty("obstacle_spawn_rate_ms"); // 125 frame = 2 secondo
                                                                                               // (se il timer è a 8ms)
    private double currentFallSpeed;;
    private int currentPhase = 1;

    private List<Particle> particles = new ArrayList<>();
    private List<FloatingScore> floatingScores = new ArrayList<>();
    private List<Star> stars = new ArrayList<>();
    private int availableColorsCount = 2;
    private SaveManager saveManager;
    private static GameModel model = null;

    // costanti
    private final double START_X = Config.getInstance().getDoubleProperty("player_start_x");
    private final double START_Y = Config.getInstance().getDoubleProperty("player_start_y");
    private final int MIN_X = 0; // relative al constrainX e constrainY
    private final int MIN_Y = 0;
    private final int OBSTACLE_START_Y = -150;
    private final int START_COLOR_ID = 0;
    private final double BASE_SPEED =Config.getInstance().getDoubleProperty("obstacle_base_speed");
    private final int TICK_TIME = 8;
    private final int SCORE_DELAY = Config.getInstance().getIntProperty("score_delay");
    private final int SCORE_PHASE_2 = Config.getInstance().getIntProperty("phase_2_score_threshold");
    private final int SCORE_PHASE_3 = Config.getInstance().getIntProperty("phase_3_score_threshold");
    private final double SPEED_PHASE_MULTIPLIER = Config.getInstance().getDoubleProperty("speed_phase_multiplier");
    private final double SPEEDRACER_MULTIPLIER = Config.getInstance().getDoubleProperty("speed_racer_multiplier");
    private final int MAX_INVULN_FRAMES = 125; // 125 frame a 125fps = 1 secondi invulerabile
    private final int DEFAULT_GAIN = 100; // punti per il tempo di sopravvivenza
    private final int MAX_CHANCE = 100;
    private final int SPEEDRACER_CHANCE_PHASE_2 = 30;
    private final int SPEEDRACER_CHANCE_PHASE_3 = 40;
    private final int SINUSOIDALMADNESS_CHANCE = 15;
    private final int MIN_PARTICLES = 5; // numero di particelle minime che appaiono durante esplosione ostacolo
    private final int MAX_PARTICLES = 12;
    private final int EXPLOSION_OFFSET = 20; // per alzare il centro esplosione
    private final int MAX_LIVES = 3;
    private final int NUM_STARS = 100;

    // METODI STATICI

    public static GameModel getInstance() {
        if (model == null) {
            model = new GameModel();
        }
        return model;
    }

    // METODI PRIVATI

    public GameModel() {
        initGame();
    }// fine costruttore

    private void initGame() {
        player = new Player(START_X, START_Y, START_COLOR_ID);
        enemies = new ArrayList<>();
        lives = MAX_LIVES;
        isGameOver = false;
        currentFallSpeed = BASE_SPEED;
        random = new Random();
        saveManager = SaveManager.getInstance();
        initStars(Config.getInstance().getIntProperty("frame_width"),
                Config.getInstance().getIntProperty("frame_height"));
    }// fine initGame

    public void update(int panelWidth, int panelHeight) {

        if (isGameOver) {
            return;
        }
        invulnerabilityHandler();
        player.update();
        player.constrainX(MIN_X, panelWidth);
        player.constrainY(MIN_Y, panelHeight);
        spawningHandler(panelWidth);
        updateEnemies(panelHeight);
        updateParticles();
        updateFloatingScore();
        checkCollisions(); // qui il gameover potrebbe essere settato a true
        updateSurvivalScore();
        updateStars(panelWidth, panelHeight);
        if (isGameOver) {
            AudioManager.getInstance().stopBackgroundMusic();
            AudioManager.getInstance().playSoundEffect("game_over.wav");

            if (score > saveManager.getHighscore()) {
                saveManager.writeHighscore(score);
            }
        }
    }// fine update

    public void initStars(int panelWidth, int panelHeight) {
        for (int i = 0; i < NUM_STARS; i++) {
            stars.add(new Star(panelWidth, panelHeight));
        }
    }// fine initStars

    private void updateEnemies(int panelHeight) {

        for (Obstacle obs : enemies) {
            obs.update();
            obs.checkOffScreen(panelHeight);
        }
        enemies.removeIf(obs -> !obs.isActive());
    }// fine updateEnemies

    private void updateParticles() {
        for (Particle p : particles) {
            p.update();
        }
        particles.removeIf(p -> p.isDead());
    }// fine updateParticles

    private void updateFloatingScore() {
        for (FloatingScore fs : floatingScores) {
            fs.update();
        }
        floatingScores.removeIf(fs -> fs.isDead());
    }// fine updateFloatingScore

    public void updateStars(int panelWidth, int panelHeight) {
        if (stars != null) {
            for (Star s : stars) {
                s.update(panelWidth, panelHeight);
            }
        }
    }// fine updateStarts

    private void invulnerabilityHandler() {

        if (isInvulnerable) {
            invulnTimer++;

            if (invulnTimer >= MAX_INVULN_FRAMES) {
                isInvulnerable = false;
                invulnTimer = 0;
            }
        }
    }// fine invulnerabilityHandler

    private void spawningHandler(int panelWidth) {
        frameCounter++;

        if (frameCounter >= spawnInterval) {
            spawnRandomEnemy(panelWidth);
            frameCounter = 0;
        }
    }// fine handleSpawning

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
                    newEnemy = SpeedRacer.createSpeedRacerObstacle(panelWidth, startY,
                            currentFallSpeed * SPEEDRACER_MULTIPLIER, randomColorId);
                } else {
                    newEnemy = StandardObstacle.createStandardObstacle(panelWidth, startY, currentFallSpeed,
                            randomColorId);
                }
                break;

            case 3:
            default:

                if (chance < SINUSOIDALMADNESS_CHANCE) {
                    newEnemy = SinusoidalMadness.creatSinusoidalMadness(panelWidth, startY, currentFallSpeed,
                            randomColorId);
                } else if (chance < SPEEDRACER_CHANCE_PHASE_3) {
                    newEnemy = SpeedRacer.createSpeedRacerObstacle(panelWidth, startY,
                            currentFallSpeed * SPEEDRACER_MULTIPLIER, randomColorId);
                } else {
                    newEnemy = StandardObstacle.createStandardObstacle(panelWidth, startY, currentFallSpeed,
                            randomColorId);
                }
                break;
        }
        enemies.add(newEnemy);
    }// fine spawnRandomEnemy

    private void checkDifficultyProgression(int currentScore) {

        if (currentScore >= SCORE_PHASE_2 && currentPhase == 1) {
            currentPhase = 2;
            currentFallSpeed *= SPEED_PHASE_MULTIPLIER;
            availableColorsCount = 3;

        }

        else if (currentScore >= SCORE_PHASE_3 && currentPhase == 2) {
            currentPhase = 3;
            currentFallSpeed *= SPEED_PHASE_MULTIPLIER;
            availableColorsCount = 4;
        }
    }// fine checkDifficultyProgression

    private void updateSurvivalScore() {
        this.stackedTime += TICK_TIME;

        if (this.stackedTime >= SCORE_DELAY) {
            addScore(DEFAULT_GAIN);
            this.stackedTime -= SCORE_DELAY;
        }
    }// fine scoreHandler

    private void addScore(int points) {
        this.score += points;
        checkDifficultyProgression(this.score);
    }// fine addScore

    private void decreaseLives() {
        if (lives > 0) {
            lives--;
        }
        if (lives <= 0) {
            isGameOver = true;
        }
    }// fine decreaseLives

    private void createExplosion(double x, double y, int colorId) {
        int particlesNumber = random.nextInt(MIN_PARTICLES, MAX_PARTICLES + 1);
        for (int i = 0; i < particlesNumber; i++) {
            particles.add(new Particle(x, y, colorId));
        }
    }// fine createExplosion


    private void checkCollisions() {
        Shape playerShape = player.getHitbox();
        Rectangle2D playerBounds = playerShape.getBounds2D(); //DOC: Returns a high precision and more accurate bounding box of the Shape than the getBounds method.
                                                             //Returns an integer Rectangle that completely encloses the Shape.
        for (int i = 0; i < enemies.size(); i++) {
            Obstacle obs = enemies.get(i);
            Shape obsShape = obs.getHitbox();
            Rectangle2D obsBounds = obsShape.getBounds2D();

            if (!playerBounds.intersects(obsBounds)) {
                continue;
            }
            //DOC: An Area object stores and manipulates a resolution-independent description of an enclosed area of 2-dimensional space.
            Area playerArea = new Area(playerShape); //The Area class creates an area geometry from the specified Shape object.
            Area obsArea = new Area(obsShape);
            playerArea.intersect(obsArea); // Sets the shape of this Area to the intersection of its current shape and the shape of the specified Area.

            if (!playerArea.isEmpty()) {

                if (player.getColorId() == obs.getColorId()) {
                    AudioManager.getInstance().playSoundEffect("hit.wav");
                    createExplosion(obs.getX(), obs.getY() - EXPLOSION_OFFSET, obs.getColorId());
                    floatingScores.add(new FloatingScore(obs.getX(), obs.getY(), obs.getPoints()));
                    addScore(obs.getPoints());
                    enemies.remove(i);
                    i--;
                } else {

                    if (!isInvulnerable) {
                        decreaseLives();
                        if (!isGameOver) {
                            AudioManager.getInstance().playSoundEffect("hurt.wav");
                        }
                        isInvulnerable = true;
                    }
                }
            }
        }
    }// fine checkCollisions

    

    private void resetScore() {
        this.score = 0;
        this.stackedTime = 0;
    }// fine resetScore

    private void resetObstacles() {
        this.enemies.clear();
        this.particles.clear();
    }// fine resetObstacles

    private void resetDifficulty() {
        currentFallSpeed = BASE_SPEED;
        currentPhase = 1;
        availableColorsCount = 2;
    }// fine resetDifficulty

    private void resetGameover() {
        isGameOver = false;
    }// fine resetGameover

    private void resetLives() {
        lives = MAX_LIVES;
    }// fine resetLives

    private void resetInvulnerability() {
        isInvulnerable = false;

    }// fine resetInvulnerability

    // METODI PUBBLICI

    public void resetGame() {
        getPlayer().resetToInitialSettings(getStartX(), getStartY(), getStartColorId());
        resetScore();
        resetObstacles();
        resetGameover();
        resetLives();
        resetInvulnerability();
        resetDifficulty();
    }// fine resetGame

    public void autoSave() {
        // Salviamo lo stato solo se il giocatore è in partita e non ha già perso.
        if (!this.isGameOver) {
            saveManager.writeGameState(this.score, this.lives, this.currentPhase, this.currentFallSpeed,
                    this.availableColorsCount, this.player, this.enemies);
        }
    }// fine autoSave

    // getters del GameModel

    public boolean isInvulnerable() {
        return isInvulnerable;
    }// fine isInvulnerable

    public int getInvulnTimer() {
        return invulnTimer;
    }

    public Player getPlayer() {
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

    public double getStartX() {
        return this.START_X;
    }

    public double getStartY() {
        return this.START_Y;
    }

    public int getStartColorId() {
        return this.START_COLOR_ID;
    }

    public int getAvailableColorsCount() {
        return this.availableColorsCount;
    }

    public int getPhase() {
        return this.currentPhase;
    }

    public double getCurrentSpeed() {
        return this.currentFallSpeed;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public List<FloatingScore> getFloatingScores() {
        return this.floatingScores;
    }

    public List<Star> getStars() {
        return this.stars;
    }

    public int getHighscore() {
        return saveManager.getHighscore();
    }

    // setters gameModel

    public void setScore(int score) {
        this.score = score;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setPhase(int phase) {
        this.currentPhase = phase;
    }

    public void setCurrentSpeed(double currentSpeed) {
        this.currentFallSpeed = currentSpeed;
    }

    public void setAvaibleColors(int avaibleColors) {
        this.availableColorsCount = avaibleColors;
    }

}// fine classe GameModel