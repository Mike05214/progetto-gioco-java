package colorclash.model;

import colorclash.utils.Config;

public class SpeedRacer extends Obstacle {

    // costanti statiche
    protected static final int WIDTH = 50;
    protected static final int HEIGHT = 100;

    // costanti
    private final int SPEEDRACER_POINTS = Config.getInstance().getIntProperty("speed_racer_points");

    public SpeedRacer() {
        super(0, -2000, 0, 0, WIDTH, HEIGHT);
        this.setActive(false);
        
        updateHitbox(); // Inizializza la hitbox in base alle dimensioni attuali
    }// fine costruttore

    //METODI PUBBLICI
    
    /**
     * Aggiorna o ricostruisce la hitbox per approssimare la forma a triangolo,
     * adattandola proporzionalmente alla larghezza e altezza correnti.
     */
    public void updateHitbox() {
        // Pulisce i rettangoli precedenti
        hitbox.getRectangles().clear();
        hitbox.getOffsetX().clear();
        hitbox.getOffsetY().clear();
        
        // Approssimazione della forma a triangolo (punta verso il basso) con 5 rettangoli logici
        hitbox.addRect(0, 0, width, height * 0.20); // Fascia alta (larga, base del triangolo)
        hitbox.addRect(width * 0.10, height * 0.20, width * 0.80, height * 0.20); // Fascia medio-alta
        hitbox.addRect(width * 0.20, height * 0.40, width * 0.60, height * 0.20); // Fascia centrale
        hitbox.addRect(width * 0.30, height * 0.60, width * 0.40, height * 0.20); // Fascia medio-bassa
        hitbox.addRect(width * 0.40, height * 0.80, width * 0.20, height * 0.20); // Fascia bassa (stretta, punta del triangolo)
    }

    // getters di SpeedRacer

    @Override
    public int getPoints() {
        return SPEEDRACER_POINTS;
    }

    @Override 
    public String getType() {
        return "SpeedRacer";
    }

}// fine classe SpeedRacer