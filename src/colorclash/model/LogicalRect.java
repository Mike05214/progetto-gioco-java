package colorclash.model; 

public class LogicalRect {
    
    //variabili d'istanza
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

    //METODI PUBBLICI

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }// fine setPosition
    

    //getters di LogicalRect

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

}// fine classe LogicalRect