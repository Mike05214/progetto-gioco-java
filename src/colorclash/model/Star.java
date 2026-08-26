package colorclash.model;

import java.util.Random;

public class Star implements IStar {

    // variabili di stato
    private double x, y;
    private int size;
    private double speed;
    private int alpha;
    private Random rand;

    // costanti
    private final int MIN_SIZE = 1;
    private final int MIN_OPACITY = 100;
    private final int MAX_SIZE = 4;
    private final int MAX_OPACITY = 255;
    private final double FALL_SPEED_MULTIPLIER = 0.8;

    public Star(int screenWidth, int screenHeight) {
        rand = new Random();
        reset(screenWidth, rand.nextInt(0, (screenHeight + 1)));
    }// fine costruttore

    // METODI PUBBLICI DI LOGICA

    public void reset(int screenWidth, int startY) {
        size = rand.nextInt(MIN_SIZE, (MAX_SIZE + 1));
        x = rand.nextInt(0, (screenWidth-size) + 1);
        y = startY;
        speed = size * FALL_SPEED_MULTIPLIER; 
        alpha = rand.nextInt(MIN_OPACITY, (MAX_OPACITY + 1)); 
    }// fine reset

    public void update(int screenWidth, int screenHeight) {
        y += speed;
        if (y > screenHeight) {
            reset(screenWidth, 0); 
        }
    }// fine update

    // GETTERS IMPLEMENTATI DALL'INTERFACCIA IStar
    
    @Override
    public double getX() { return x; }
    
    @Override
    public double getY() { return y; }
    
    @Override
    public int getSize() { return size; }
    
    @Override
    public int getAlpha() { return alpha; }
    
}// fine classe Star