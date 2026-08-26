package colorclash.model;

import java.util.Random;

public class Particle implements IParticle {
    
    // variabili d'istanza
    private double x, y, dirX, dirY;
    private int colorId;
    private int size;
    private Random rand;
    private int alpha = 255;
    private boolean isActive = false;
    private boolean isTriangle = false;

    // costanti
    private final int MIN_SIZE = 10;
    private final int MAX_SIZE = 20;

    public Particle() {
        this.x = 0;
        this.y = -2000;
        this.colorId = 0;
        this.rand = new Random();
        this.isActive = false;
    }// fine costruttore

    // METODI PUBBLICI

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
            this.isActive = false;
        }
    }// fine update

    // GETTERS

    @Override
    public double getX() { return x; }

    @Override
    public double getY() { return y; }

    @Override
    public int getSize() { return size; }

    @Override
    public int getAlpha() { return alpha; }

    @Override
    public int getColorId() { return colorId; }

    @Override
    public boolean isActive() { return isActive; }

    @Override
    public boolean isTriangle() { return isTriangle; }

    //SETTERS

    public void setActive(boolean active) { this.isActive = active; }
    
    public void setTriangle(boolean isTriangle) { this.isTriangle = isTriangle; }
    
}// fine classe Particle