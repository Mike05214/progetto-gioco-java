package colorclash.model;

public class FloatingScore implements IFloatingScore {

    // variabili d'istanza
    private double x, y;
    private String text;
    private int alpha = 255;

    public FloatingScore(double startX, double startY, int points) {
        x = startX;
        y = startY;
        text = "+" + points;
    }// fine costruttore

    // METODI PUBBLICI DI LOGICA

    public void update() {
        y -= 1.5;
        alpha -= 3;

        if (alpha < 0) {
            alpha = 0;
        }
    }// fine update

    // GETTERS IMPLEMENTATI DALL'INTERFACCIA IFloatingScore

    @Override
    public boolean isDead() {
        return alpha <= 0; // Semplificato: ritorna direttamente il booleano senza if/else
    }// fine isDead
    
    @Override
    public double getX() { return x; }

    @Override
    public double getY() { return y; }

    @Override
    public String getText() { return text; }

    @Override
    public int getAlpha() { return alpha; }
    
}// fine classe FloatingScore