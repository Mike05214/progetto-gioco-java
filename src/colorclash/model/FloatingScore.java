package colorclash.model;

public class FloatingScore {

    // variabili d'istanza
    private double x, y;
    private String text;
    private int alpha = 255;

    public FloatingScore(double startX, double startY, int points) {
        x = startX;
        y = startY;
        text = "+" + points;
    }// fine costruttore

    //METODI PUBBLICI

    public void update() {
        y -= 1.5;
        alpha -= 3;

        if (alpha < 0) {
            alpha = 0;
        }
    }// fine update

    public boolean isDead() {
        if (alpha <= 0) {
            return true;
        } else {
            return false;
        }
    }// fine isDead
    

    // getters di FloatingScore

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getText() {
        return text;
    }

    public int getAlpha() {
        return alpha;
    }
    
}// fine classe FloatingScore