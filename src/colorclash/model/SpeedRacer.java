package src.colorclash.model;

import java.awt.Polygon;
import java.awt.Shape;

import java.util.Random;
import src.colorclash.utils.Config;

public class SpeedRacer extends Obstacle {

    // costanti statiche
    private static final int WIDTH = 50;
    private static final int HEIGHT = 100;
    private static final Random random = new Random();

    // costanti
    private final int SPEEDRACER_POINTS = Config.getInstance().getIntProperty("speed_racer_points");

    public SpeedRacer(int startX, int startY, double speed, int colorId, int width, int height) {
        super(startX, startY, speed, colorId, width, height);
    }// fine costruttore

    public static SpeedRacer createSpeedRacerObstacle(int panelWidth, int startY, double speed, int colorId) {
        int randomX = random.nextInt(panelWidth - WIDTH);
        return new SpeedRacer(randomX, startY, speed, colorId, WIDTH, HEIGHT);
    }// fine createSpeedRacer

    // getters di SpeedRacer
    @Override
    public Shape getHitbox() {
        Polygon triangle = new Polygon();
        triangle.addPoint((int) x, (int) y);
        triangle.addPoint((int) x + width, (int) y);
        triangle.addPoint((int) x + width / 2, (int) y + height);

        return triangle;
    }

    @Override
    public int getPoints() {
        return SPEEDRACER_POINTS;
    }

}// fine classe SpeedRacer
