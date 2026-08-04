package src.colorclash.model;

import java.util.Random;

public class Particle {
    private double x, y, dirX, dirY;
    private int colorId;
    private int size;
    private Random rand;
    private int alpha = 255;

    private final int MIN_SIZE = 10;
    private final int MAX_SIZE = 20;

    public Particle(double startX, double startY, int colorId) {
        x = startX;
        y = startY;
        this.colorId = colorId;
        rand = new Random();
        size = rand.nextInt(MIN_SIZE, (MAX_SIZE + 1));
        dirX = (rand.nextDouble() - 0.5) * 10; // va da -0.5 a 0.5 determinandone direzione orizzontale
                                                    // moltiplicato per 10 per velocità effettiva
        dirY = (rand.nextDouble() - 0.5) * 10;
    }// fine costruttore

    public void update() {
        x += dirX;
        y += dirY;
        alpha -= 4;

        if (alpha < 0) {
            alpha = 0;
        }
    }// fine update

    public boolean isDead() {
        if (alpha <= 0) {
            return true;
        } else {
            return false;
        }
    }// fine idDead

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
}// fine classe Particle
