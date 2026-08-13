package colorclash.model; // Assicurati che sia nel package del model

public class LogicalRect {
    
    private double x;
    private double y;
    private double width;
    private double height;

    public LogicalRect(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Metodo fondamentale: i rettangoli devono muoversi in sincrono con l'ostacolo
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    // Metodo per spostare il rettangolo rispetto alla sua posizione attuale 
    // (utile per i componenti secondari di una hitbox complessa)
    public void translate(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    // Getters necessari per il CollisionChecker
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
}