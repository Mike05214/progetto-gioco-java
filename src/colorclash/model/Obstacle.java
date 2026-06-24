package src.colorclash.model;
import java.awt.Shape;

public abstract class Obstacle {
    
    // Variabili "protected" così le classi figlie(le sotto-classi) (es. SpeedRacer) potranno leggerle
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    
    protected double fallSpeed;
    protected int colorId;
    protected boolean isActive; // Ci serve per sapere quando eliminarlo dallo schermo
    
    // Costruttore: ogni ostacolo nasce con coordinate, velocità e colore specifici
    public Obstacle(int startX, int startY, double speed, int colorId,int width,int height) {
        this.x = startX;
        this.y = startY;
        this.width = width;  // Dimensione standard
        this.height = height; // Dimensione standard
        
        this.fallSpeed = speed;
        this.colorId = colorId;
        this.isActive = true; // Appena nasce, è ovviamente "vivo"
    }
    
    // --- METODI DI AZIONE ---
    
    // Il movimento base: cadere verso il basso
    public void fall() {
        this.y += fallSpeed;
    }
    
    // --- METODI HELPER LOGICI ---
    
    // Il GameModel chiamerà questo metodo per capire se l'ostacolo è uscito dallo schermo
    public void checkOffScreen(int screenHeight) {
        if (this.y > screenHeight) {
            this.isActive = false; // "Muore" di vecchiaia uscendo dallo schermo
        }
    }
    
    // Se viene distrutto dall'Avatar, chiamiamo questo metodo
    public void destroy() {
        this.isActive = false;
    }
    public void setColorId(int colorId) {
    this.colorId = colorId;
    }
    
    // --- GETTERS ---
    // Ogni ostacolo, non importa che forma abbia, DEVE saper restituire la sua Hitbox
    public abstract Shape getHitbox();
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getColorId() { return colorId; }
    public boolean isActive() { return isActive; }

    //METODO DI DEBUG DA TOGLIERE IN SEGUITO
    @Override
    public String toString() {
    return "Ostacolo [" +
           "X=" + this.x + 
           ", Y=" + this.y + 
           ", ColoreID=" + this.colorId + 
           ", Velocità=" + this.fallSpeed + 
           "]";

    
}

    public enum ObstacleShape{ //parole chiave per distinguere i vari tipi di ostacoli
        STANDARD,
        SINUSOIDAL,
        SPEED_RACER
    }

}