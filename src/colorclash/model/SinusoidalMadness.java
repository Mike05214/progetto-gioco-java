package src.colorclash.model;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import java.util.Random;
import src.colorclash.utils.Config;

public class SinusoidalMadness extends Obstacle {

    // costanti statiche
    private static final int WIDTH = 70;
    private static final int HEIGHT = 70;
    private static final Random random = new Random();
    private static final int AMPLITUDE = Config.getInstance().getIntProperty("sinusoidal_madness_amplitude");
    private static final int COLOR_CHANGE_INTERVAL = Config.getInstance().getIntProperty("sinusoidal_madness_color_change_interval");
    private static final int CURRENT_MAX_COLORS = 4;

    // costanti
    private final double WAWE_SPEED = 0.05;
    private final int SINUSOIDALMADNESS_POINTS = Config.getInstance().getIntProperty("sinusoidal_madness_points");

    // variabili d'istanza
    private double startX;
    private double angle = 0;
    private int colorTimer = 0;

    public SinusoidalMadness(double x, double y, double fallSpeed, int colorId, int width, int height) {
        super(x, y, fallSpeed, colorId, width, height);
        this.startX = x;
    }// fine costruttore

    @Override
    public void fall() {
        this.y += this.fallSpeed;
        angle += WAWE_SPEED;
        this.x = this.startX + (AMPLITUDE * Math.sin(angle));
        colorTimer++;

        if (colorTimer >= COLOR_CHANGE_INTERVAL) {
            colorTimer = 0;
            int nextColorId = (this.getColorId() + 1) % CURRENT_MAX_COLORS;
            this.setColorId(nextColorId);
        }
    }// fine fall

    public static SinusoidalMadness creatSinusoidalMadness(int panelWidth, double startY, double fallSpeed, int colorId) {
        double safeMinX = AMPLITUDE;
        double safeMaxX = panelWidth - WIDTH - AMPLITUDE;
        double randomX = random.nextDouble(safeMinX, safeMaxX);
        return new SinusoidalMadness(randomX, startY, fallSpeed, colorId, WIDTH, HEIGHT);
    }// fine createSinusoidalMadness

    // getters ereditati dalla superclasse Obstacle
    @Override
    public Shape getHitbox() {
        return new Ellipse2D.Double(x, y, width, height);
    }

    

    @Override
    public int getPoints() {
        return SINUSOIDALMADNESS_POINTS;
    }
}// fine classe SinusoidalMadness
