package colorclash.model;

public class FloatingScore implements IFloatingScore {

    // variabili d'istanza
    private double x, y;
    private String text;
    private int alpha = 255;
    private boolean isActive = false;

    public FloatingScore() {
        this.x = 0;
        this.y = -2000; 
        this.text = "";
        this.isActive = false;
    }// fine costruttore

    // METODI PUBBLICI 

    public void spawn(double startX, double startY, int points) {
        this.x = startX;
        this.y = startY;
        this.text = "+" + points;
        this.alpha = 255;
        this.isActive = true;
    }// fine spawn

    public void update() {
        y -= 1.5;
        alpha -= 3;

        if (alpha <= 0) {
            alpha = 0;
            this.isActive = false;
        }
    }// fine update

    // GETTERS

    @Override
    public double getX() { return x; }

    @Override
    public double getY() { return y; }

    @Override
    public String getText() { return text; }

    @Override
    public int getAlpha() { return alpha; }

    @Override
    public boolean isActive() { return isActive; }

    // SETTERS

    public void setActive(boolean active) { this.isActive = active; }

}// fine classe FloatingScore