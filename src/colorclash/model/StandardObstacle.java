package src.colorclash.model;
import java.awt.Shape;
import java.awt.Rectangle;


public class StandardObstacle extends Obstacle {

    
    public StandardObstacle(int startX, int startY, double speed, int colorId, int width, int height) {
        
        // Passa i valori alla classe astratta Obstacle
        super(startX, startY, speed, colorId,width,height);
        
        
        
       
    }

    public Shape getHitbox() {
    return new Rectangle((int)x, (int)y, width, height); 
}



}