package src.colorclash.model;

public class Avatar {
    
    // Coordinate e dimensioni pure (nessun rettangolo grafico, solo matematica)
    private int x;
    private int y;
    private int width;
    private int height;
    
    // Attributi di gioco
    private int speed;  // Lo speed rappresenta la lunghezza del passo quando si piagia il comando daje 
    private int colorId; // Usiamo un ID logico invece di java.awt.Color, nel game panel mettiamo un array di tipo color 
                               // dove ongni colore è associato ad un indice 
    private boolean isInvulnerable;
    
    // Costruttore
    public Avatar(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        
        // Valori base (in futuro li sposteremo nella classe Config per comodità)
        this.width = 50;
        this.height = 50;
        this.speed = 15;
        this.colorId = 0; // Parte con il primo colore (es. 0 = Rosso)
        this.isInvulnerable = false;
    }
    
    // --- METODI DI AZIONE (Chiamati dal GameModel quando l'utente preme i tasti) ---
    
    public void moveLeft() {
        this.x -= speed;
    }
    
    public void moveRight() {
        this.x += speed;
    }

    public void moveUp(){
        this.y -= speed;
    }

    public void moveDown(){
        this.y += speed;
    }
    
    public void switchColor() {
        // Passa al colore successivo. Supponiamo di avere 3 colori totali (0, 1, 2)
        this.colorId++;
        
        // Se supera il numero massimo di colori, torna a 0 (effetto circolare)
        if (this.colorId > 2) {
            this.colorId = 0;
        }
    }
    
    // --- METODI HELPER LOGICI ---
    
    // Questo metodo servirà al GameModel per evitare che l'avatar esca dallo schermo
    public void constrainX(int minX, int maxX) {
        if (this.x < minX) {
            this.x = minX;
        }
        
        if (this.x + this.width > maxX) {
            this.x = maxX - this.width;
        }
    }
    
    // --- GETTERS & SETTERS (Per far leggere i dati alla View e al GameModel) ---
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getColorId() { return colorId; }
    
    public boolean isInvulnerable() { return isInvulnerable; }
    public void setInvulnerable(boolean invulnerable) { this.isInvulnerable = invulnerable; }
}