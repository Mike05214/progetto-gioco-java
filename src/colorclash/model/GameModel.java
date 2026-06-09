package src.colorclash.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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


    //costanti
    private final int START_X = 375;
    private final int START_Y = 450;
    private final int START_COLOR_ID = 0;
    private final int TICK_TIME = 8;
    private final int SCORE_DELAY = 1000; // in ms

    // --- VARIABILI PER LO SPAWNER ---
    private Random random;
    private int frameCounter;
    private int spawnInterval; // Ogni quanti frame nasce un nemico
    
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
        player.move();
        player.constrainX(0, panelWidth);
        player.constrainY(0, panelHeight);
        // Nei prossimi passaggi aggiungeremo qui altri helper methods come:
        // 2. Genera nuovi nemici se serve
        handleSpawning(panelWidth);
        // 3. Muovi i nemici e cancella quelli usciti dallo schermo
        updateEnemies(panelHeight); // Modifica il tuo metodo updateEnemies per fargli usare panelHeight!
        
        // moveEntities();
        // checkCollisions();
        scoreHandler();
    }
    
    
        
    
    //HELPER METODI: (quelli privati li può usare solo il Model)

    private void scoreHandler(){
        this.stackedTime += TICK_TIME;

        // Se l'accumulatore supera la soglia (es. 1 secondo), scatta il punto!
        if (this.stackedTime >= SCORE_DELAY) {
            this.score++;
            this.stackedTime -= SCORE_DELAY; // Scaliamo la soglia senza azzerare il resto, mantenendo la precisione
            System.out.println("Model: Secondo passato! Nuovo Score: " + this.score);
        }
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

    private void checkCollisions() {
        // Nel prossimo step, scriveremo qui la matematica per 
        // controllare se il rettangolo dell'Avatar tocca un Ostacolo
    }

    // Questo metodo lo useremo in futuro per generare nemici a caso
    public void spawnEnemy(Obstacle newObstacle) {
        enemies.add(newObstacle);
    }

    // Controlla se è il momento di far nascere un nemico
    private void handleSpawning(int panelWidth) {
        frameCounter++;
    
        if (frameCounter >= spawnInterval) {
            spawnRandomEnemy(panelWidth);
            frameCounter = 0; // Azzera il contatore per il prossimo nemico!
        }
    }

// Crea fisicamente il nemico
    private void spawnRandomEnemy(int panelWidth) {
        
        int randomWidth = random.nextInt(50, 100); 
        int randomHeigth = random.nextInt(50, 100);
    
    // 1. Genera una coordinata X casuale dentro i limiti dello schermo
    // Se lo schermo è 800, la X sarà tra 0 e 750 (per non uscire fuori col lato destro)
        int randomX = random.nextInt(panelWidth - randomWidth);
    
    // 2. Sceglie un colore a caso (0 = Rosso, 1 = Verde, 2 = Blu)
        int randomColorId = random.nextInt(3);
    
    // 3. Velocità di caduta (es. 5 pixel a frame)
        int fallSpeed = 5;
    
    // IL TRUCCO: La Y di partenza è -50! (Così nasce FUORI dallo schermo in alto e "scivola" dentro)
        int startY = -150;

        
    // Creiamo il nemico concreto e lo mettiamo nella lista
        Obstacle newEnemy = new StandardObstacle(randomX, startY, fallSpeed, randomColorId,randomWidth,randomHeigth);
        enemies.add(newEnemy);
    }




    
    // Metodo per gestire il danno
    public void decreaseLives() {
        if (lives > 0) {
            lives--;
        }
        
        // Se le vite arrivano a zero, scatta il Game Over
        if (lives <= 0) {
            isGameOver = true;
        }
    }
    

    
    // Metodo di reset che chiamerai nel bottone Back To Menu
    public void resetScore() {
        this.score = 0;
        this.stackedTime = 0;
    }
        
    
    // --- GETTERS (Servono alla View per sapere cosa disegnare) ---
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
}