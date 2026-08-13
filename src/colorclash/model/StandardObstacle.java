package colorclash.model;

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
        
        // Il contorno rettangolare è descritto da un solo rettangolo logico
        hitbox.addRect(0, 0, width, height);
    }// fine costruttore

    //METODI PUBBLICI

    // getters di StandardObstacle
    
    @Override
    public int getPoints() {
        return STANDARD_OBSTACLE_POINTS;
    }

    @Override
    public String getType() {
        return "StandardObstacle";
    }
}// fine classe StandardObstacle