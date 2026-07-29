package src.colorclash.model;

import java.util.Random;

public class Particle {
    private double x, y, dirX, dirY;
    private int lifeSpan;
    private int colorId;
    private int size;
    private Random rand;

    private final int MIN_SIZE = 10;
    private final int MIN_LIFE_SPAN = 20;
    private final int MAX_SIZE = 20;
    private final int MAX_LIFE_SPAN = 40;

    public Particle(double x, double y, int colorId) {
        this.x = x;
        this.y = y;
        this.colorId = colorId;
        this.rand = new Random();
        this.size = rand.nextInt(MIN_SIZE, (MAX_SIZE + 1));
        this.dirX = (rand.nextDouble() - 0.5) * 10;
        this.dirY = (rand.nextDouble() - 0.5) * 10;
        this.lifeSpan = rand.nextInt(20, 40 + 1);
    }//fine costruttore

    public void update() {
        x += dirX;
        y += dirY;
        lifeSpan--;
    }//fine update

    public boolean isDead() {
        if (lifeSpan <= 0) {
            return true;
        } else {
            return false;
        }
    }//fine idDead

    //getters di Particle

    public double getX() {
        return  x;
    }

    public double getY() {
        return  y;
    }

    public int getSize() {
        return size;
    }

    public int getColorId() {
        return colorId;
    }
}//fine classe Particle
