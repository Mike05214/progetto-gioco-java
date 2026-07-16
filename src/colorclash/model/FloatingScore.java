package src.colorclash.model;

public class FloatingScore {
    private double x, y;
    private String text;
    private int alpha = 255; // 255 = visibile, 0 = invisibile

    public FloatingScore(double startX, double startY, int points) {
        this.x = startX;
        this.y = startY;
        this.text = "+" + points; // Aggiunge il "+" davanti al numero
    }

    public void update() {
        this.y -= 1.5; // Il testo fluttua lentamente verso l'alto
        this.alpha -= 3; // Il testo svanisce gradualmente
        
        if (this.alpha < 0) {
            this.alpha = 0;
        }
    }

    public boolean isDead() {
        return this.alpha <= 0;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public String getText() {
        return this.text;
    }

    public int getAlpha() {
        return this.alpha;
    }
}