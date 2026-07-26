package src.colorclash.model;

import java.util.Random;

public class Particle {
    private double x, y, dirX, dirY;
    private int life;
    private int maxLife;
    private int colorId;
    private int size;
    private Random rand;

    public Particle(double x, double y, int colorId) {
        this.x = x;
        this.y = y;
        this.colorId = colorId;
        this.rand = new Random();
        this.size = rand.nextInt(10, (20 + 1));
        this.dirX = (rand.nextDouble() - 0.5) * 10;
        this.dirY = (rand.nextDouble() - 0.5) * 10;
        this.maxLife = rand.nextInt(20, 40 + 1);
        this.life = maxLife;
    }

    public void update() {
        x += dirX;
        y += dirY;
        life--;
    }

    public boolean isDead() {
        if (life <= 0) {
            return true;
        } else {
            return false;
        }
    }

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
}
