package colorclash.model;

import java.util.Random;

public class Particle {
    //variabili d'istanza
    private double x, y, dirX, dirY;
    private int colorId;
    private int size;
    private Random rand;
    private int alpha = 255;
    private boolean isActive = false;

    //costanti
    private final int MIN_SIZE = 10;
    private final int MAX_SIZE = 20;

    public Particle() {
        this.x = 0;
        this.y = -2000; 
        this.colorId = 0;
        this.rand = new Random();
        this.isActive = false;

    }// fine costruttore

    //METODI PUBBLICI

    public void spawn(double startX, double startY, int colorId) {
        this.x = startX;
        this.y = startY;
        this.colorId = colorId;
        this.alpha = 255;
        this.size = rand.nextInt(MIN_SIZE, (MAX_SIZE + 1));
        this.dirX = (rand.nextDouble() - 0.5) * 10;
        this.dirY = (rand.nextDouble() - 0.5) * 10;
        this.isActive = true;
    }// fine spawn

    public void update() {
        x += dirX;
        y += dirY;
        alpha -= 4;

        if (alpha <= 0) {
            alpha = 0;
            this.isActive = false; // La particella si "autodistrugge" e torna disponibile nel pool
        }
    }// fine update

    
    // getters di Particle

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

    public int getColorId() {
        return colorId;
    }

    public boolean isActive() {
        return isActive;
    }

    // setters di Particle

    public void setActive(boolean active) {
        this.isActive = active;
    }
}// fine classe Particle
