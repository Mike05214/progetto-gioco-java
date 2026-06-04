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
    private boolean movingUp, movingDown, movingLeft, movingRight; //quattro "interruttori" per il movimento
    
    // Costruttore
    public Avatar(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        
        // Valori base (in futuro li sposteremo nella classe Config per comodità)
        this.width = 50;
        this.height = 50;
        this.speed = 5;
        this.colorId = 0; // Parte con il primo colore (es. 0 = Rosso)
        this.isInvulnerable = false;
    }
    
   // UNICO METODO MOVE PER UN CONTROLLO PIU FLUIDO DEL MOVIMENTO DELL'AVATAR
    
    public void move(){
        if(movingUp){
            y -= speed;
        }

        if(movingDown){
            y += speed;
        }

        if(movingLeft){
            x -= speed;
        }

        if(movingRight){
            x += speed;
        }
    }
    
    // Questo metodo servirà al GameModel per evitare che l'avatar esca dallo schermo
    public void constrainX(int minX, int maxX) {
        if (this.x < minX) {
            this.x = minX;
        }
        
        if (this.x + this.width > maxX) {
            this.x = maxX - this.width;
        }
    }

    public void constrainY(int minY, int maxY){
        if(this.y < minY){
            this.y = minY;
        }

        if(this.y + this.height > maxY){
            this.y = maxY - this.height;
        }
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
    
    
    
    // --- GETTERS & SETTERS (Per far leggere i dati alla View e al GameModel) ---
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getColorId() { return colorId; }
    
    public boolean isInvulnerable() { return isInvulnerable; }
    public void setInvulnerable(boolean invulnerable) { this.isInvulnerable = invulnerable; }
    // 3. I METODI "SET" PER ACCENDERE/SPEGNERE GLI INTERRUTTORI, permettono al metodo update di chiamare continuamente ad ogni tick il metodo move 
    // che a sua volta muove il quadratino in base a quale delle variabili moving è settata a true da questi metodi
    public void setMovingUp(boolean movingUp)       { this.movingUp = movingUp; }
    public void setMovingDown(boolean movingDown)   { this.movingDown = movingDown; }
    public void setMovingLeft(boolean movingLeft)   { this.movingLeft = movingLeft; }
    public void setMovingRight(boolean movingRight) { this.movingRight = movingRight; }
}