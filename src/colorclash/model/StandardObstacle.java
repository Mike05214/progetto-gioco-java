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
        updateHitbox(); // Usa il metodo per inizializzarla
    }// fine costruttore

    //METODI PUBBLICI

    /**
     * Aggiorna o ricostruisce la hitbox in base alle dimensioni attuali dell'ostacolo.
     */
    public void updateHitbox() {
        // Pulisce i rettangoli esistenti (supponendo che tu aggiunga un metodo clear() in Hitbox, 
        // oppure puoi ricreare l'oggetto Hitbox se preferisci: this.hitbox = new Hitbox();)
        hitbox.getRectangles().clear();
        hitbox.getOffsetX().clear();
        hitbox.getOffsetY().clear();
        
        // Aggiunge il rettangolo proporzionale alla dimensione corrente
        hitbox.addRect(0, 0, width, height);
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