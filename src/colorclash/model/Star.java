package src.colorclash.model;

import java.util.Random;

public class Star {

    //variabili di stato
    private double x, y;
    private int size;
    private double speed;
    private int alpha;
    private Random rand;

    //costanti
    private final int MIN_SIZE = 1;
    private final int MIN_OPACITY = 100;
    private final int MAX_SIZE = 4;
    private final int MAX_OPACITY = 255;
    private final double FALL_SPEED_MULTIPLIER = 0.8;

    public Star(int screenWidth, int screenHeight) {
        this.rand = new Random();
        // Al primo avvio, distribuisce le stelle casualmente su tutto lo schermo
        reset(screenWidth, rand.nextInt(0, (screenHeight + 1)));
    }// fine costruttore

    public void reset(int screenWidth, int startY) {
        // Posizione X casuale per tutta la larghezza dello schermo
        this.x = rand.nextInt(0, (screenWidth + 1));
        this.y = startY;
        this.size = rand.nextInt(MIN_SIZE, (MAX_SIZE + 1));
        // La velocità di caduta rimane proporzionale alla dimensione
        this.speed = this.size * FALL_SPEED_MULTIPLIER;
        
        // Opacità casuale tra 100 e 255 per l'effetto di profondità
        this.alpha = rand.nextInt(MIN_OPACITY, (MAX_OPACITY + 1));
    }// fine reset

    public void update(int screenWidth, int screenHeight) {
        this.y += speed;
        // Se la stella esce dallo schermo, viene riposizionata in cima
        if (this.y > screenHeight) {
            reset(screenWidth, 0); 
        }
    }// fine update

    // getters di Star
    public double getX() { 
        return x; 
    }
    
    public double getY() { 
        return y; 
    }
    
    public int getSize() { 
        return size; 
    }
    
    public int getAlpha() { 
        return alpha; 
    }
}// fine classe Star