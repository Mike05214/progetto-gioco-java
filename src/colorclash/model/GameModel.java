package src.colorclash.model;
import java.util.ArrayList;
import java.util.List;

public class GameModel {

    // Gli attori del nostro gioco
    private Avatar player;
    private List<Obstacle> enemies;
    
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

        // Posizioniamo l'Avatar in basso al centro (es. per una finestra 800x600)
        this.player = new Avatar(375, 450); 
        this.enemies = new ArrayList<>(); // Lista vuota all'inizio
        this.enemies.add(new StandardObstacle(375, 0, 5, 1));

        this.score = 0;
        this.lives = 3; // In futuro questo "3" lo prenderemo dalla classe Config!
        this.isGameOver = false;
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
        // spawnObstacles();
        // moveEntities();
        // checkCollisions();
        
        // Per ora facciamo solo salire il punteggio per testare che il tempo scorre!
        score++;
    }
    //HELPER METODI:

    private void updateEnemies() {
        // 1. Facciamo cadere ogni sotacolo presente nella lista 
        for (Obstacle obs : enemies) { //in questo modo si ottimizza la sintassi al posto del for tradizionale con la size della lista(UTILE)
            obs.fall();
            obs.checkOffScreen(600); // Controlla se sono usciti dal fondo dello schermo
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
    public Avatar getPlayer() { 
        return player; 
    }
    public List<Obstacle> getEnemies() { 
        return enemies; 
    }
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