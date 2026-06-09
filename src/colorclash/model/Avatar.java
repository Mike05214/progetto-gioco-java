package src.colorclash.model;

public class Avatar {
    
    // Coordinate e dimensioni pure (nessun rettangolo grafico, solo matematica)
    private double x;
    private double y;
    private int width;
    private int height;
    private long lastColorChange = 0;
    private long currentTime = 0;
    private final double SCALE_FACTOR = 0.7071; //lunghezza del vettore velocità (teorema di pitagora)
    private final long COLOR_COOLDOWN = 500;
    
    // Attributi di gioco
    private double speed;  // Lo speed rappresenta la lunghezza del passo quando si piagia il comando daje 
    private int colorId; // Usiamo un ID logico invece di java.awt.Color, nel game panel mettiamo un array di tipo color 
                               // dove ongni colore è associato ad un indice 
    private boolean isInvulnerable;
    private boolean movingUp, movingDown, movingLeft, movingRight; //quattro "interruttori" per il movimento
    
    // Costruttore
    public Avatar(int startX, int startY,int startColorId) {
        this.x = startX;
        this.y = startY;
        
        // Valori base (in futuro li sposteremo nella classe Config per comodità)
        this.width = 50;
        this.height = 50;
        this.speed = 2.5;
        this.colorId = startColorId; // Parte con il primo colore (es. 0 = Rosso)
        this.isInvulnerable = false;
    }
    
   // UNICO METODO MOVE PER UN CONTROLLO PIU FLUIDO DEL MOVIMENTO DELL'AVATAR
    
    public void move(){
       // Si muove in diagonale solo se c'è un movimento orizzontale REALE e uno verticale REALE
        boolean diagonalMovement = (movingLeft ^ movingRight) && (movingUp ^ movingDown); // ^ è l'operatore logico XOR, diagonal movement è true solo se si stanno premento un tasto laterale e uno verticale allo stesso tempo
        double speedVector = 0;
        if(diagonalMovement){
            speedVector = speed*SCALE_FACTOR; //si divide il vettore velocità diagonale per la sua lunghezza, normalizzandolo
        }
        else{
            speedVector = speed;
        }

        if(movingUp && !movingDown) {
            y -= speedVector;
        }   
        if(movingDown && !movingUp){
            y += speedVector;
        }    
        if(movingLeft && !movingRight){
            x -= speedVector;
        }   
        if(movingRight && !movingLeft){
            x += speedVector;
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

    public void colorCooldown() {
        this.currentTime = System.currentTimeMillis(); //metodo che restituisce il tempo del sistema operativo in millisecondi
        if(currentTime - lastColorChange >= COLOR_COOLDOWN){ //se il tempo passato dall'ultimo cambio colore è maggiore o uguale al cooldown che è una costante, sarà di nuovo possibile cambiare colore
            switchColor();
        }
        else{
            System.out.println("Cooldown attivo!");
        }
    }

    public void switchColor(){
        this.colorId++;
            if(this.colorId > 2){
                colorId = 0;
            }
            lastColorChange = currentTime;
    }

    public void resetToInitialSettings(int startX, int startY, int startColorId){
        this.x = startX;
        this.y = startY;
        this.colorId = startColorId;
    }

    public void resetMovementFlags() {
        this.movingUp = false;
        this.movingDown = false;
        this.movingLeft = false;
        this.movingRight = false;
    }
    
    // --- METODI HELPER LOGICI ---
    
    
    
    // --- GETTERS & SETTERS (Per far leggere i dati alla View e al GameModel) ---
    
    public int getX() { return (int)x; } //cast esplicito a int, il metodo fillRect nella view si aspetta delle coordinate intere non double
    public int getY() { return (int)y; }
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