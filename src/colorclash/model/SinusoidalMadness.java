package src.colorclash.model;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import java.util.Random;
import src.colorclash.utils.Config;

public class SinusoidalMadness extends Obstacle {

    // costanti statiche
    protected static final int WIDTH = 70;
    protected static final int HEIGHT = 70;
    protected static final int AMPLITUDE = Config.getInstance().getIntProperty("sinusoidal_madness_amplitude");
    private static final int COLOR_CHANGE_INTERVAL = Config.getInstance()
            .getIntProperty("sinusoidal_madness_color_change_interval");
    private static final int CURRENT_MAX_COLORS = 4;

    // costanti
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
    }// fine costruttore

    @Override
    public void update() {
        y += this.fallSpeed;
        angle += WAWE_SPEED;
        x = this.startX + (AMPLITUDE * Math.sin(angle));
        colorTimer++;

        if (colorTimer >= COLOR_CHANGE_INTERVAL) {
            colorTimer = 0;
            int nextColorId = (this.getColorId() + 1) % CURRENT_MAX_COLORS;
            this.setColorId(nextColorId);
        }
    }// fine fall

    // getters ereditati dalla superclasse Obstacle
    @Override
    public Shape getHitbox() {
        return new Ellipse2D.Double(x, y, width, height);
    }

    @Override
    public int getPoints() {
        return SINUSOIDALMADNESS_POINTS;
    }

    @Override
    public String getType() {
        return "SinusoidalMadness";
    }

    public void setStartX(double startX){
        this.startX=startX;
    }
}// fine classe SinusoidalMadness
