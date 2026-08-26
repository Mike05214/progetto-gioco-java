package colorclash.model; 

public class LogicalRect implements ILogicalRect {
    
    // variabili d'istanza
    private double x;
    private double y;
    private double width;
    private double height;

    public LogicalRect(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }// fine costruttore

    // METODI PUBBLICI

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }// fine setPosition
    
    // GETTERS

    @Override
    public double getX() { return x; }

    @Override
    public double getY() { return y; }

    @Override
    public double getWidth() { return width; }

    @Override
    public double getHeight() { return height; }

}// fine classe LogicalRect