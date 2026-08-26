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
        createHitbox(); // Usa il metodo per inizializzarla
    }// fine costruttore

    // METODI PROTETTI

    @Override
    protected void createHitbox(){
        hitbox.addRect(0, 0, width, height);
    }

    // METODI PUBBLICI

    @Override
    public void updateHitbox() {
        hitbox.getRectangles().clear();
        hitbox.getOffsetX().clear();
        hitbox.getOffsetY().clear();
        
        // Aggiunge il rettangolo proporzionale alla dimensione corrente
        createHitbox(); 
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