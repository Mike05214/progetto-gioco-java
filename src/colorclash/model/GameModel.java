package src.colorclash.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;

public class GameModel {

    // Gli attori del nostro gioco
    private Avatar player;
    private List<Obstacle> enemies;
    
    // Variabili di stato del gioco
    private int updateCounter;
    private int lives;
    private boolean isGameOver;
    private int score;
    private int stackedTime = 0;

    //Variabili per invulnerabilità
    private boolean isInvulnerable = false;
    private int invulnTimer = 0;
    private static final int MAX_INVULN_FRAMES = 120; // 120 frame a 60fps = 2 secondi di pace

    //costanti
    private final int START_X = 350;
    private final int START_Y = 550;
    private final int START_COLOR_ID = 0;
    private final int TICK_TIME = 8;
    private final int SCORE_DELAY = 1000; // in ms
    private final int SCORE_PHASE_2 = 2500;
    private final int SCORE_PHASE_3 = 5000;
    private final double SPEED_PHASE_MULTIPLIER = 1.20;

    // --- VARIABILI PER LO SPAWNER ---
    private Random random;
    private int frameCounter;
    private int spawnInterval; // Ogni quanti frame nasce un nemico
    private double currentFallSpeed = 3.0;
    private int currentPhase = 1;
    private Color[] avaibleColors;
    private int availableColorsCount = 2;
    
    // Costruttore: viene chiamato quando l'utente preme "Gioca"
    public GameModel() {
        initGame();
    }
    
    // --- HELPER METHODS PRIVATI ---
    
    // Inizializza o resetta i parametri di partenza
    private void initGame() {

        // Posizioniamo l'Avatar in basso al centro (es. per una finestra 800x600)
        this.player = new Avatar(START_X, START_Y,START_COLOR_ID); 
        this.enemies = new ArrayList<>(); // Lista vuota all'inizio
        //attualmente questo array di colori e tutte le sue istanze nel gamepanel sono 
        //sostanzialmente inutili ai fini del semplice limitare la palette di colori nelle varie
        //fasi di difficoltà, per quello basterebbe semplicemente manipolare la variabile
        //avaibleColorsCount, però la bro gemini dice che ci servirà più avanti per gli effetti
        //grafici delle esplosioni degli ostacoli/player quindi ce lo lasciamo
        this.avaibleColors = new Color[]{Color.RED, Color.GREEN};
        

        this.updateCounter = 0;
        this.lives = 3; // In futuro questo "3" lo prenderemo dalla classe Config!
        this.isGameOver = false;

        this.random = new Random();
        this.frameCounter = 0;
        this.spawnInterval = 62; // 125 frame = 1 secondo (se il timer è a ~8ms)
    }
    
    // --- METODI PUBBLICI (Richiamati dalla View) ---
    
    // Ogni volta che il Timer del pannello "suona" (circa 60 volte al secondo), 
    // il pannello chiama model.update() per far calcolare la fisica, e subito dopo chiama repaint() per disegnare la nuova scena.
    public void update(int panelWidth, int panelHeight) {
        // Se il gioco è finito, fermiamo i calcoli
        if (isGameOver) {
            return;
        }
        invulnerabilityHandler();
        player.move();
        player.constrainX(0, panelWidth);
        player.constrainY(0, panelHeight);
        // Nei prossimi passaggi aggiungeremo qui altri helper methods come:
        // 2. Genera nuovi nemici se serve
        handleSpawning(panelWidth);
        // 3. Muovi i nemici e cancella quelli usciti dallo schermo
        updateEnemies(panelHeight); // Modifica il tuo metodo updateEnemies per fargli usare panelHeight!
        
        // moveEntities();
        
        checkCollisions();
        scoreHandler();
    }
    
    
        
    
    //HELPER METODI: (quelli privati li può usare solo il Model)

    private void scoreHandler(){
        this.stackedTime += TICK_TIME;

        // Se l'accumulatore supera la soglia (es. 1 secondo), scatta il punto!
        if (this.stackedTime >= SCORE_DELAY) {
            addScore(100);
            this.stackedTime -= SCORE_DELAY; // Scaliamo la soglia senza azzerare il resto, mantenendo la precisione
            System.out.println("Model: Secondo passato! Nuovo Score: " + this.score);
        }
    }

    public void addScore(int points){
        this.score += points;
        checkDifficultyProgression(this.score);
    }

    private void updateEnemies(int panelHeight ) {
        // 1. Facciamo cadere ogni sotacolo presente nella lista 
        for (Obstacle obs : enemies) { //in questo modo si ottimizza la sintassi al posto del for tradizionale con la size della lista(UTILE)
            obs.fall();
            obs.checkOffScreen(panelHeight); // Controlla se sono usciti dal fondo dello schermo
        }
        
        // 2. RIMOZIONE INTELLIGENTE
        // removeIf controlla la lista e cancella in automatico gli ostacoli disattivati.
        enemies.removeIf(obs -> !obs.isActive()); //Metodo proprio di ArrayList trovato sulla documentazione, in input va messo un predicato
                                                  //"regola" che prende un oggetto e risponde VERO o FALSO.
    }

    public void checkCollisions() {
    // Otteniamo la forma del giocatore e la sua "scatola grezza" di delimitazione
        Shape playerShape = player.getHitbox();
        Rectangle playerBounds = playerShape.getBounds(); 

    // Scorriamo tutta la lista dei nemici attuali a schermo
        for (int i = 0; i < enemies.size(); i++) {
            Obstacle obs = enemies.get(i);
            Shape obsShape = obs.getHitbox();
            Rectangle obsBounds = obsShape.getBounds(); 
        
        // ==========================================
        // FASE 1: BROAD PHASE (Controllo Velocissimo)
        // ==========================================
        // Controlliamo prima i rettangoli esterni. Se non si sfiorano nemmeno,
        // saltiamo questo ostacolo e passiamo al successivo senza sprecare calcoli!
            if (!playerBounds.intersects(obsBounds)) {
                continue; 
            }
        
        // ==========================================
        // FASE 2: NARROW PHASE (Controllo Millimetrico)
        // ==========================================
        // Arriviamo qui SOLO se le scatole si toccano. 
        // Ora usiamo l'Area per vedere se i pixel geometrici si sovrappongono davvero.
            Area playerArea = new Area(playerShape);
            Area obsArea = new Area(obsShape);
        
        // Calcola l'intersezione matematica tra le due forme
            playerArea.intersect(obsArea); //Non è più l'area della shape originale, ma viene "sovrascritta" con il risultato geometrico dello scontro.

        
        // Se l'area intersecata NON è vuota, si sono schiantati!
            if (!playerArea.isEmpty()) {
            
            // --- GESTIONE DELLA LOGICA DI GIOCO ---
            
                if(player.getColorId()==obs.getColorId()){
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
    }

    public void checkDifficultyProgression(int currentScore)
    {
       if(currentScore >= SCORE_PHASE_2 && currentPhase == 1){
            currentPhase = 2;
            currentFallSpeed *= SPEED_PHASE_MULTIPLIER;
            avaibleColors = new Color[]{Color.CYAN, Color.GREEN, Color.RED};
            availableColorsCount = 3;
            System.out.println("FASE 2 COMINCIATA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
       }
       else if(currentScore >= SCORE_PHASE_3 && currentPhase == 2){
            currentPhase = 3;
            currentFallSpeed *= SPEED_PHASE_MULTIPLIER; // La velocità generale aumenta ancora
        
            // Sblocchiamo il 4° colore
            avaibleColors = new Color[]{Color.CYAN, Color.GREEN, Color.RED, Color.ORANGE};
            availableColorsCount = 4;
            System.out.println("FASE 3 COMINCIATA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
       }
    }

    private void invulnerabilityHandler(){
        if (isInvulnerable) {
            invulnTimer++; // Il tempo passa
            if (invulnTimer >= MAX_INVULN_FRAMES) {
                isInvulnerable = false; // Torna vulnerabile
                invulnTimer = 0;        // Resetta il timer
            }
        }
    }


     // Metodo per gestire il danno
    public void decreaseLives() {
        if (lives > 0) {
            lives--;
        }
        
        // Se le vite arrivano a zero, scatta il Game Over
        if (lives <= 0) {
            isGameOver = true;
            System.out.println("GAME OVER!");
        }
    }
    
    // Controlla se è il momento di far nascere un nemico
    private void handleSpawning(int panelWidth) {
        frameCounter++;
    
        if (frameCounter >= spawnInterval) {
            spawnRandomEnemy(panelWidth);
            frameCounter = 0; // Azzera il contatore per il prossimo nemico!
            //debugEnemiesTemp();
        }
    }

// Crea fisicamente il nemico
    private void spawnRandomEnemy(int panelWidth) {
        // 1. IL TRUCCO: La Y di partenza è -150!
    int startY = -150;

    // 2. Colore Dinamico: Invece di un fisso "3", usiamo la variabile che cresce col livello.
    // Fase 1: random.nextInt(2) -> colori 0, 1
    // Fase 2: random.nextInt(3) -> colori 0, 1, 2
    // Fase 3: random.nextInt(4) -> colori 0, 1, 2, 3
    int randomColorId = random.nextInt(availableColorsCount); 

    // 3. Velocità Dinamica: Prende la base impostata dal checkDifficultyProgression
    double fallSpeed = currentFallSpeed; 

    // Dichiariamo il nemico generico (Padre)
    Obstacle newEnemy;

    // Generiamo una probabilità da 0 a 99 per scegliere il tipo di nemico
    int chance = random.nextInt(100);

    // Lo spawner decide in base alla FASE in cui si trova ColorClash
    switch (currentPhase) {
        case 1:
            // FASE 1: Punteggio basso. 100% Ostacoli Standard. Niente scherzi.
            newEnemy = StandardObstacle.createStandardObstacle(panelWidth, startY, fallSpeed, randomColorId);
            break;
            
        case 2:
            // FASE 2: Punteggio medio. Si sblocca lo Speed Racer.
            // 30% di probabilità che sia uno Speed Racer, 70% Standard.
            if (chance < 30) {
                // Notare il fallSpeed * 1.5 che avevi impostato tu
                newEnemy = SpeedRacer.createSpeedRacerObstacle(panelWidth, startY, fallSpeed * 1.5, randomColorId);
            } else {
                newEnemy = StandardObstacle.createStandardObstacle(panelWidth, startY, fallSpeed, randomColorId);
            }
            break;
            
        case 3:
        default:
            // FASE 3: Punteggio alto. L'inferno. Tutti sbloccati.
            // 15% Sinusoidal, 25% Speed Racer, 60% Standard.
            if (chance < 15) {
                newEnemy = SinusoidalMadness.creatSinusoidalMadness(panelWidth, startY, fallSpeed, randomColorId);
            } else if (chance < 40) { // 15 + 25 = 40
                newEnemy = SpeedRacer.createSpeedRacerObstacle(panelWidth, startY, fallSpeed * 1.5, randomColorId);
            } else {
                newEnemy = StandardObstacle.createStandardObstacle(panelWidth, startY, fallSpeed, randomColorId);
            }
            break;
    }

    // Aggiungiamo il nemico nato alla lista del Model
    enemies.add(newEnemy);
    debugEnemiesTemp();
    }




    
   

    
    // Metodo di reset che chiamerai nel bottone Back To Menu
    public void resetScore() {
        this.score = 0;
        this.stackedTime = 0;
        System.out.println("score resettato con successo");
    }

    public void resetObstacles(){
        System.out.println("lista prima del reset: ");
        debugEnemiesTemp();
        this.enemies.clear();
        System.out.println("lista dopo il reset: ");
        debugEnemiesTemp();
    }

    public void resetDifficulty(){
        currentFallSpeed = 3.0;
        currentPhase = 1;
        avaibleColors = new Color[]{Color.RED, Color.GREEN}; //necessario per allineare lo stato reale allo stato logico
        availableColorsCount = 2;
    }

    public void resetGameover(){
        isGameOver=false;
    }
    
    public void resetLives(){
            lives=3;
    }

    public void resetInvulnerability(){
        isInvulnerable=false;

    }
    
    public void resetGame(){
        getPlayer().resetToInitialSettings(getStartX(), getStartY(),getStartColorId());
        resetScore();
        resetObstacles();
        resetGameover();
        resetLives();
        resetInvulnerability();
        resetDifficulty();
    }

    
    // --- GETTERS (Servono alla View per sapere cosa disegnare) ---
    public boolean isInvulnerable() {
        return isInvulnerable;
    }

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

    public void debugEnemiesTemp(){
        if(enemies.isEmpty()){
            System.out.println("LISTA VUOTA");
        }
        else{
            for(Obstacle obs : enemies){
                System.out.println(obs);
            }
        }
    }
}