package colorclash.model;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import colorclash.utils.Config;

public class StandardObstacle extends Obstacle {
    
    // costanti statiche
    protected static final int MIN_SIZE = 50;
    protected static final int MAX_SIZE = 100;

    // costanti
    private final int STANDARD_OBSTACLE_POINTS = Config.getInstance().getIntProperty("default_obstacle_points");

    public StandardObstacle() {
        super(0, -2000, 0, 0, MIN_SIZE, MIN_SIZE);
        this.setActive(false);
    }// fine costruttore

    //METODI PUBBLICI

    // getters di StandardObstacle
    @Override
    public Shape getHitbox() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    @Override
    public int getPoints() {
        return STANDARD_OBSTACLE_POINTS;
    }

    @Override
    public String getType() {
        return "StandardObstacle";
    }
}// fine classe StandardObstacle