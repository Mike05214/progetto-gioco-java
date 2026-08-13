package colorclash.model;

import java.awt.Shape;

public abstract class Obstacle {
    
    // variabili d'istanza 
    protected double x;
    protected double y;
    protected int width;
    protected int height;
    protected double fallSpeed;
    protected int colorId;
    protected boolean isActive;


    public Obstacle(double startX, double startY, double speed, int colorId, int width, int height) {
        x = startX;
        y = startY;
        this.width = width;
        this.height = height;
        fallSpeed = speed;
        this.colorId = colorId;
        isActive = true;
    }// fine costruttore

    //METODI PUBBLICI

    public void update() {
        y += fallSpeed;
    }// fine fall

    public void checkOffScreen(int screenHeight) {

        if (y > screenHeight) {
            isActive = false;
        }
    }// fine checkOffScreen

    // setters di Obstacle
    
    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    // getters di Obstacle

    public abstract Shape getHitbox();

    public abstract String getType();
    
    public abstract int getPoints();

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

    // setters di Obstacle

    public void setActive(boolean active){
        isActive= active;

    }

    public void setY(double CordY ){
        y=CordY;
    }

    public void setX(double CordX ){
        x=CordX;
    }

    public void setWidth(int width ){
        this.width=width;
    }

    public void setHeight(int height ){
        this.height=height;
    }

    public void setFallSpeed(double speed ){
        this.fallSpeed=speed;
    }

    
}// fine classe astratta Obstacle