package src.colorclash.model;

import java.awt.Shape;

public abstract class Obstacle {
    // variabili d'istanza ereditabili
    protected double x;
    protected double y;
    protected int width;
    protected int height;
    protected double fallSpeed;
    protected int colorId;
    protected boolean isActive;


    public Obstacle(double startX, double startY, double speed, int colorId, int width, int height) {
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
        this.fallSpeed = speed;
        this.colorId = colorId;
        this.isActive = true;
    }// fine costruttore

    public void update() {
        this.y += fallSpeed;
    }// fine fall

    public void checkOffScreen(int screenHeight) {

        if (this.y > screenHeight) {
            this.isActive = false;
        }
    }// fine checkOffScreen

    public void destroy() {
        this.isActive = false;
    }// fine destroy

    // setters di Obstacle
    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    // getters di Obstacle

    public abstract Shape getHitbox();

    public abstract String getType();
    
    abstract int getPoints();

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getColorId() {
        return colorId;
    }

    public boolean isActive() {
        return isActive;
    }

    public double getFallSpeed(){
        return fallSpeed;
    }

    
}// fine classe astratta Obstacle