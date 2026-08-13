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
        
        // Approssimazione della forma a triangolo (punta verso il basso) con 5 rettangoli logici
        hitbox.addRect(0,            0,             WIDTH,        HEIGHT * 0.20); // Fascia alta (larga, base del triangolo)
        hitbox.addRect(WIDTH * 0.10, HEIGHT * 0.20, WIDTH * 0.80, HEIGHT * 0.20); // Fascia medio-alta
        hitbox.addRect(WIDTH * 0.20, HEIGHT * 0.40, WIDTH * 0.60, HEIGHT * 0.20); // Fascia centrale
        hitbox.addRect(WIDTH * 0.30, HEIGHT * 0.60, WIDTH * 0.40, HEIGHT * 0.20); // Fascia medio-bassa
        hitbox.addRect(WIDTH * 0.40, HEIGHT * 0.80, WIDTH * 0.20, HEIGHT * 0.20); // Fascia bassa (stretta, punta del triangolo)
    }// fine costruttore

    //METODI PUBBLICI

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