package src.colorclash.model;
import java.awt.Shape;
import java.awt.Polygon;

public class SpeedRacer extends Obstacle {
    public SpeedRacer(int startX, int startY, double speed, int colorId, int width, int height) {
        
        
        super(startX, startY, speed, colorId, width, height);
       
    }

    
    @Override
    public Shape getHitbox() {
        Polygon triangle = new Polygon();
        
        // 1. Spigolo in alto a sinistra (inizio della base piatta)
        triangle.addPoint((int)x, (int)y); 
        
        // 2. Spigolo in alto a destra (fine della base piatta)
        triangle.addPoint((int)x + width, (int)y);    
        
        // 3. Punta in basso al centro (il vertice che "buca" l'aria)
        triangle.addPoint((int)x + width / 2, (int)y + height); 
        
        return triangle;
    }

}
