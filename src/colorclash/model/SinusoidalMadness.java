package src.colorclash.model;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class SinusoidalMadness extends Obstacle {
  
    public SinusoidalMadness(int startX, int startY, double speed, int colorId,int width,int height) {
        
        
        super(startX, startY, speed, colorId,width,height);
        
       
    }

    @Override
    public Shape getHitbox() {
        // Usiamo Ellipse2D.Double che accetta le coordinate e le dimensioni
        return new Ellipse2D.Double(x, y, width, height);
    }
}
