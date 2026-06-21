package src.colorclash.model;
import java.awt.Shape;
import java.awt.Rectangle;
import java.util.Random;


public class StandardObstacle extends Obstacle {
    
    // Costanti segrete, note SOLO a questa classe
    private static final int MIN_SIZE = 50;
    private static final int MAX_SIZE = 100;
    private static final Random random = new Random(); // Un solo generatore condiviso

    // 1. IL COSTRUTTORE NASCOSTO (protected o private)
    // Ora lo Spawner NON PUÒ più chiamare new StandardObstacle(...) direttamente!
    public StandardObstacle(int startX, int startY, double speed, int colorId, int width, int height) {
        super(startX, startY, speed, colorId, width, height);
    }

    // 2. IL METODO FACTORY STATICO
    // Questo è l'unico punto di accesso per creare un ostacolo standard
    public static StandardObstacle createStandardObstacle(int panelWidth, int startY, double speed, int colorId) {
        // La classe decide le SUE dimensioni
        int randomWidth = random.nextInt(MIN_SIZE, MAX_SIZE);
        int randomHeight = random.nextInt(MIN_SIZE, MAX_SIZE);
        
        // La classe calcola la SUA posizione X sicura
        int randomX = random.nextInt(panelWidth - randomWidth);
        
        // Costruisce se stessa e si restituisce al mittente
        return new StandardObstacle(randomX, startY, speed, colorId, randomWidth, randomHeight);
    }

    @Override
    public Shape getHitbox() {
        return new Rectangle((int)x, (int)y, width, height);
    }
}