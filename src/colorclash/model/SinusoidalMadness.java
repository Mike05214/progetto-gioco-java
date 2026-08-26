package colorclash.model;

import colorclash.utils.Config;

public class SinusoidalMadness extends Obstacle {

    // costanti statiche
    protected static final int WIDTH = 70;
    protected static final int HEIGHT = 70;
    protected static final int AMPLITUDE = Config.getInstance().getIntProperty("sinusoidal_madness_amplitude");
    
    // costanti
    private final int COLOR_CHANGE_INTERVAL_MS = Config.getInstance().getIntProperty("sinusoidal_madness_color_change_interval_ms");
    private final int CURRENT_MAX_COLORS = 4;
    private final double WAWE_SPEED = 0.05;
    private final int SINUSOIDALMADNESS_POINTS = Config.getInstance().getIntProperty("sinusoidal_madness_points");

    // variabili d'istanza
    private double startX;
    private double angle;
    private int colorTimer;

    public SinusoidalMadness() {
        super(0, -2000, 0, 0, WIDTH, HEIGHT);
        this.startX = 0;
        this.angle = 0;
        this.colorTimer = 0;
        this.setActive(false);
        
        createHitbox(); // Inizializza la hitbox dinamica
    }// fine costruttore

    //METODI PROTETTI

    @Override
    protected void createHitbox() {
        
        // Approssimazione della forma circolare con 5 rettangoli logici 
        hitbox.addRect(width * 0.20, 0, width * 0.60, height * 0.20); // Fascia alta
        hitbox.addRect(width * 0.05, height * 0.20, width * 0.90, height * 0.20); // Fascia medio-alta
        hitbox.addRect(0, height * 0.40, width, height * 0.20); // Fascia centrale (massima)
        hitbox.addRect(width * 0.05, height * 0.60, width * 0.90, height * 0.20); // Fascia medio-bassa
        hitbox.addRect(width * 0.20, height * 0.80, width * 0.60, height * 0.20); // Fascia bassa
    }

    //METODI PUBBLICI

    @Override
    public void update() {
        y += this.fallSpeed;
        angle += WAWE_SPEED;
        x = this.startX + (AMPLITUDE * Math.sin(angle));
        colorTimer += 8;

        if (colorTimer >= COLOR_CHANGE_INTERVAL_MS) {
            colorTimer = 0;
            int nextColorId = (this.getColorId() + 1) % CURRENT_MAX_COLORS;
            this.setColorId(nextColorId);
        }
        
        // Aggiorna la posizione dei rettangoli della hitbox
        hitbox.updatePosition(this.x, this.y);
    }// fine update

    // getters di SinusoidalMadness
    
    @Override
    public int getPoints() {
        return SINUSOIDALMADNESS_POINTS;
    }

    @Override
    public String getType() {
        return "SinusoidalMadness";
    }

    // setters di SinusoidalMadness
    public void setStartX(double startX){
        this.startX = startX;
    }
}// fine classe SinusoidalMadness