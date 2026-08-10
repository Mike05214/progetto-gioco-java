package src.colorclash.model;

import java.awt.geom.Path2D;
import java.awt.Shape;

import src.colorclash.utils.Config;

public class SpeedRacer extends Obstacle {

    // costanti statiche
    protected static final int WIDTH = 50;
    protected static final int HEIGHT = 100;

    // costanti
    private final int SPEEDRACER_POINTS = Config.getInstance().getIntProperty("speed_racer_points");

    public SpeedRacer() {
        super(0, -2000, 0, 0, WIDTH, HEIGHT);
        this.setActive(false);
    }// fine costruttore

    //METODI PUBBLICI

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
