package src.colorclash.model;

import java.awt.geom.Path2D;
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

    public SpeedRacer(double startX, double startY, double speed, int colorId, int width, int height) {
        super(startX, startY, speed, colorId, width, height);
    }// fine costruttore

    public static SpeedRacer createSpeedRacerObstacle(int panelWidth, double startY, double speed, int colorId) {
        double randomX = random.nextDouble(panelWidth - WIDTH);
        return new SpeedRacer(randomX, startY, speed, colorId, WIDTH, HEIGHT);
    }// fine createSpeedRacer

    // getters di SpeedRacer
    @Override
    public Shape getHitbox() {
        Path2D.Double triangle = new Path2D.Double();
        triangle.moveTo(x, y);
        triangle.lineTo(x + width, y);
        triangle.lineTo(x + width / 2.0, y + height);

        return triangle;
    }

    @Override
    public int getPoints() {
        return SPEEDRACER_POINTS;
    }

    @Override 
    public String getType() {
        return "SpeedRacer";
    }

}// fine classe SpeedRacer
