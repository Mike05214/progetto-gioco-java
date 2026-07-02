package src.colorclash.model;
import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.geom.Path2D;

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
    // Dimensioni totali dell'ingombro
    
    
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
        this.width = 70;
        this.height = 80;
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

    public void colorCooldown(int availableColorsCount) {
        this.currentTime = System.currentTimeMillis(); 
    
        if (currentTime - lastColorChange >= COLOR_COOLDOWN) { 
            // Passiamo il limite al metodo che fa il cambio effettivo
            switchColor(availableColorsCount);
        } else {
            System.out.println("Cooldown attivo!");
        }
    }

    public void switchColor(int availableColorsCount){
        // Il Modulo fa il ciclo perfetto: 
        // Se hai 2 colori: 0 -> 1 -> 0
        // Se hai 4 colori: 0 -> 1 -> 2 -> 3 -> 0
        this.colorId = (this.colorId + 1) % availableColorsCount;
    
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
    public Shape getHitbox() {
        Path2D.Double navicella = new Path2D.Double();

    // 1. LA PUNTA (Centro, in alto)
        navicella.moveTo(this.x + (width * 0.5), this.y);

    // 2. CORPO DESTRO (Si allarga leggermente verso il basso)
        navicella.lineTo(this.x + (width * 0.6), this.y + (height * 0.3));

    // 3. PUNTA DELL'ALA DESTRA (Estrema destra, verso il basso)
        navicella.lineTo(this.x + width, this.y + (height * 0.8));

    // 4. ATTACCO DELL'ALA DESTRA (Rientra verso il centro)
        navicella.lineTo(this.x + (width * 0.7), this.y + (height * 0.8));

    // 5. PROPULSORE DESTRO (Va dritto fino al fondo)
        navicella.lineTo(this.x + (width * 0.7), this.y + height);

    // 6. RIENTRANZA CENTRALE TRA I MOTORI (Lo scarico)
        navicella.lineTo(this.x + (width * 0.5), this.y + (height * 0.85));

    // 7. PROPULSORE SINISTRO (Fondo)
        navicella.lineTo(this.x + (width * 0.3), this.y + height);

    // 8. ATTACCO DELL'ALA SINISTRA
        navicella.lineTo(this.x + (width * 0.3), this.y + (height * 0.8));

    // 9. PUNTA DELL'ALA SINISTRA (Estrema sinistra)
        navicella.lineTo(this.x, this.y + (height * 0.8));

    // 10. CORPO SINISTRO (Torna verso la punta)
        navicella.lineTo(this.x + (width * 0.4), this.y + (height * 0.3));

    // Chiude il tracciato ricollegandosi alla PUNTA!
        navicella.closePath();

        return navicella;
    }
     

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