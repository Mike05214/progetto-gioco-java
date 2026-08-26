package colorclash.model;

import colorclash.utils.Config;

public class Player implements IPlayer {

    // variabili di stato
    private double x;
    private double y;
    private long lastColorChange = 0;
    private long currentTime = 0;
    private int colorId;
    private boolean movingUp, movingDown, movingLeft, movingRight;
    private Hitbox hitbox; 

    // costanti
    private final double SCALE_FACTOR = 0.7071;
    private final long COLOR_COOLDOWN = Config.getInstance().getIntProperty("color_cooldown");
    private final double PLAYER_SPEED = Config.getInstance().getDoubleProperty("player_speed"); 
    private final int PLAYER_WIDTH = Config.getInstance().getIntProperty("player_width");
    private final int PLAYER_HEIGHT = Config.getInstance().getIntProperty("player_height");
    
    public Player(double startX, double startY, int startColorId) {
        this.x = startX;
        this.y = startY;
        this.colorId = startColorId;
        createHitbox();
    }// fine costruttore

    private void createHitbox() {
        this.hitbox = new Hitbox();
        
        // Punta anteriore
        hitbox.addRect(PLAYER_WIDTH * 0.45, 0, PLAYER_WIDTH * 0.10, PLAYER_HEIGHT * 0.15);
        // Muso superiore
        hitbox.addRect(PLAYER_WIDTH * 0.40, PLAYER_HEIGHT * 0.15, PLAYER_WIDTH * 0.20, PLAYER_HEIGHT * 0.15);
        // Attacco ali alto
        hitbox.addRect(PLAYER_WIDTH * 0.32, PLAYER_HEIGHT * 0.30, PLAYER_WIDTH * 0.36, PLAYER_HEIGHT * 0.15);
        // Centro fusoliera / ali medie
        hitbox.addRect(PLAYER_WIDTH * 0.20, PLAYER_HEIGHT * 0.45, PLAYER_WIDTH * 0.60, PLAYER_HEIGHT * 0.15);
        // Ali basse
        hitbox.addRect(PLAYER_WIDTH * 0.10, PLAYER_HEIGHT * 0.60, PLAYER_WIDTH * 0.80, PLAYER_HEIGHT * 0.10);
        // Punte esterne delle ali
        hitbox.addRect(0, PLAYER_HEIGHT * 0.70, PLAYER_WIDTH, PLAYER_HEIGHT * 0.10);
        // Base motori posteriore
        hitbox.addRect(PLAYER_WIDTH * 0.30, PLAYER_HEIGHT * 0.80, PLAYER_WIDTH * 0.40, PLAYER_HEIGHT * 0.20);
        
        hitbox.updatePosition(this.x, this.y);
    }

    // METODI PUBBLICI

    public void update() {
        boolean diagonalMovement = (movingLeft ^ movingRight) && (movingUp ^ movingDown);
        double speedVector = diagonalMovement ? (PLAYER_SPEED * SCALE_FACTOR) : PLAYER_SPEED;

        if (movingUp && !movingDown) y -= speedVector;
        if (movingDown && !movingUp) y += speedVector;
        if (movingLeft && !movingRight) x -= speedVector;
        if (movingRight && !movingLeft) x += speedVector;
        
        hitbox.updatePosition(this.x, this.y);
    }// fine update

    public void constrainX(int minX, int maxX) {
        if (x < minX) x = minX;
        if (x + PLAYER_WIDTH > maxX) x = maxX - PLAYER_WIDTH;
    }// fine constrainX

    public void constrainY(int minY, int maxY) {
        if (y < minY) y = minY;
        if (y + PLAYER_HEIGHT > maxY) y = maxY - PLAYER_HEIGHT;
    }// fine constrainY

    public void colorCooldown(int availableColorsCount, boolean forward) {
        currentTime = System.currentTimeMillis();
        if (currentTime - lastColorChange >= COLOR_COOLDOWN) {
            switchColor(availableColorsCount, forward);
        }
    }// fine colorCoolDown

    public void switchColor(int availableColorsCount, boolean forward) {
        if (forward) {
            colorId = (colorId + 1) % availableColorsCount; 
        } else {
            colorId = (colorId - 1 + availableColorsCount) % availableColorsCount;
        }
        lastColorChange = currentTime;
    }// fine switchColor

    public void resetToInitialSettings(double startX, double startY, int startColorId) {
        x = startX;
        y = startY;
        colorId = startColorId;
        hitbox.updatePosition(this.x, this.y); 
    }// fine resetToInitialSettings

    public void resetMovementFlags() {
        movingUp = false;
        movingDown = false;
        movingLeft = false;
        movingRight = false;
    }// fine resetMovementFlags


    //GETTERS

    @Override
    public double getX() { return x; }

    @Override
    public double getY() { return y; }

    @Override
    public int getWidth() { return PLAYER_WIDTH; }

    @Override
    public int getHeight() { return PLAYER_HEIGHT; }

    @Override
    public int getColorId() { return colorId; }

    @Override
    public Hitbox getHitbox() { 
        return hitbox; 
    }


    //SETTERS

    public void setMovingUp(boolean movingUp) { this.movingUp = movingUp; }
    public void setMovingDown(boolean movingDown) { this.movingDown = movingDown; }
    public void setMovingLeft(boolean movingLeft) { this.movingLeft = movingLeft; }
    public void setMovingRight(boolean movingRight) { this.movingRight = movingRight; }
    public void setColorId(int colorId){ this.colorId = colorId; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}