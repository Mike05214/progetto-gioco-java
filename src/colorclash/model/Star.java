package src.colorclash.model;

import java.util.Random;

public class Star {
    private double x, y;
    private int size;
    private double speed;
    private int alpha;
    private Random rand;

    public Star(int screenWidth, int screenHeight) {
        this.rand = new Random();
        // Al primo avvio, distribuisce le stelle casualmente su tutto lo schermo
        reset(screenWidth, rand.nextInt(0, (screenHeight + 1)));
    }

    public void reset(int screenWidth, int startY) {
        // Posizione X casuale per tutta la larghezza dello schermo
        this.x = rand.nextInt(0, (screenWidth + 1));
        this.y = startY;
        
        // Dimensione casuale tra 1 e 4 (usando la tua esatta logica dei bound)
        this.size = rand.nextInt(1, (4 + 1));
        
        // La velocità di caduta rimane proporzionale alla dimensione
        this.speed = this.size * 0.8;
        
        // Opacità casuale tra 100 e 255 per l'effetto di profondità
        this.alpha = rand.nextInt(100, (255 + 1));
    }

    public void update(int screenWidth, int screenHeight) {
        this.y += speed;
        // Se la stella esce dallo schermo, viene riposizionata in cima
        if (this.y > screenHeight) {
            reset(screenWidth, 0); 
        }
    }

    // --- GETTER NECESSARI PER LA VIEW ---
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
}