package src.colorclash.model;
import java.awt.Shape;
import java.awt.Rectangle;
import java.util.Random;

public class StandardObstacle extends Obstacle {
    //costanti statiche
    private static final int MIN_SIZE = 50;
    private static final int MAX_SIZE = 100;
    private static final Random random = new Random();

    //costanti
    private final int STANDARD_OBSTACLE_POINTS = 100;

    public StandardObstacle(int startX, int startY, double speed, int colorId, int width, int height) {
        super(startX, startY, speed, colorId, width, height);
    }//fine costruttore

    public static StandardObstacle createStandardObstacle(int panelWidth, int startY, double speed, int colorId) {
        int randomWidth = random.nextInt(MIN_SIZE, MAX_SIZE);
        int randomHeight = random.nextInt(MIN_SIZE, MAX_SIZE);
        int randomX = random.nextInt(panelWidth - randomWidth);
        return new StandardObstacle(randomX, startY, speed, colorId, randomWidth, randomHeight);
    }//fine createStandardObstacle

    //getters di StandardObstacle
    @Override
    public Shape getHitbox() {
        return new Rectangle((int)x, (int)y, width, height);
    }
    @Override
    public int getPoints(){
        return STANDARD_OBSTACLE_POINTS;
    }
}//fine classe StandardObstacle