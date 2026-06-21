package src.colorclash.model;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class SinusoidalMadness extends Obstacle {
    private static final int WIDTH = 70;
    private static final int HEIGHT = 70;
    private static final Random random = new Random();
  
    public SinusoidalMadness(int startX, int startY, double speed, int colorId,int width,int height) {
        
        
        super(startX, startY, speed, colorId,WIDTH,HEIGHT);
        
       
    }

    @Override
    public Shape getHitbox() {
        // Usiamo Ellipse2D.Double che accetta le coordinate e le dimensioni
        return new Ellipse2D.Double(x, y, width, height);
    }
}
