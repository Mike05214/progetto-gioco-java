package src.colorclash.model;
import java.awt.Shape;
import java.util.Random;
import java.awt.Polygon;

public class SpeedRacer extends Obstacle {
    // Costanti segrete, note SOLO a questa classe
    private static final int WIDTH = 50;
    private static final int HEIGHT = 100;
    private static final Random random = new Random();
    public SpeedRacer(int startX, int startY, double speed, int colorId, int width, int height) {
        
        
        super(startX, startY, speed, colorId, width, height);
       
    }

    // 2. IL METODO FACTORY STATICO
    // Questo è l'unico punto di accesso per creare un ostacolo standard
    public static SpeedRacer createSpeedRacerObstacle(int panelWidth, int startY, double speed, int colorId) {
        // La classe decide le SUE dimensioni;
        // La classe calcola la SUA posizione X sicura
        int randomX = random.nextInt(panelWidth - WIDTH);
        
        // Costruisce se stessa e si restituisce al mittente
        return new SpeedRacer(randomX, startY, speed, colorId, WIDTH, HEIGHT);
        
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
    @Override
    public int getPoints(){
        return 200;
    }

}
