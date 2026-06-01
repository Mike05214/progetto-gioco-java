package src.colorclash.model;

public class GameModel {
    
    // Variabili di stato del gioco
    private int score;
    private int lives;
    private boolean isGameOver;
    
    // Costruttore: viene chiamato quando l'utente preme "Gioca"
    public GameModel() {
        initGame();
    }
    
    // --- HELPER METHODS PRIVATI ---
    
    // Inizializza o resetta i parametri di partenza
    private void initGame() {
        this.score = 0;
        this.lives = 3; // In futuro questo "3" lo prenderemo dalla classe Config!
        this.isGameOver = false;
    }
    
    // --- METODI PUBBLICI (Richiamati dalla View) ---
    
    // Ogni volta che il Timer del pannello "suona" (circa 60 volte al secondo), 
    // il pannello chiama model.update() per far calcolare la fisica, e subito dopo chiama repaint() per disegnare la nuova scena.
    public void update() {
        // Se il gioco è finito, fermiamo i calcoli
        if (isGameOver) {
            return;
        }
        
        // Nei prossimi passaggi aggiungeremo qui altri helper methods come:
        // spawnObstacles();
        // moveEntities();
        // checkCollisions();
        
        // Per ora facciamo solo salire il punteggio per testare che il tempo scorre!
        score++; 
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
    
    // --- GETTERS (Servono alla View per sapere cosa disegnare) ---
    
    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
}