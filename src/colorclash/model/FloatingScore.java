package src.colorclash.model;

public class FloatingScore {
    
    private double x, y;
    private String text;
    private int alpha = 255;

    public FloatingScore(double startX, double startY, int points) {
        this.x = startX;
        this.y = startY;
        this.text = "+" + points;
    }//fine costruttore

    public void update() {
        this.y -= 1.5;
        this.alpha -= 3;
        
        if (this.alpha < 0) {
            this.alpha = 0;
        }
    }//fine update

    public boolean isDead() {
        return this.alpha <= 0;
    }//fine isDead

    //getters di FloatingScore

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
}//fine classe FloatingScore