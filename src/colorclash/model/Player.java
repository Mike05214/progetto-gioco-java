package src.colorclash.model;

import java.awt.Shape;
import java.awt.geom.Path2D;

import src.colorclash.utils.Config;

public class Player {

    // variabili di stato
    private double x;
    private double y;
    private long lastColorChange = 0;
    private long currentTime = 0;
    private int colorId;
    private boolean isInvulnerable;
    private boolean movingUp, movingDown, movingLeft, movingRight;

    //costanti
    private final double SCALE_FACTOR = 0.7071;// 1/sqtr(2)
    private final long COLOR_COOLDOWN = Config.getInstance().getIntProperty("color_cooldown");
    private final double PLAYER_SPEED = Config.getInstance().getDoubleProperty("player_speed"); 
    private final int PLAYER_WIDTH = Config.getInstance().getIntProperty("player_width");
    private final int PLAYER_HEIGHT = Config.getInstance().getIntProperty("player_height");
    

    public Player(double startX, double startY, int startColorId) {
        x = startX;
        y = startY;
        colorId = startColorId;
        isInvulnerable = false;
    }// fine costruttore

    public void update() {
        boolean diagonalMovement = (movingLeft ^ movingRight) && (movingUp ^ movingDown);
        double speedVector = 0;

        if (diagonalMovement) {
            speedVector = PLAYER_SPEED * SCALE_FACTOR; 
        } else {
            speedVector = PLAYER_SPEED;
        }

        if (movingUp && !movingDown) {
            y -= speedVector;
        }

        if (movingDown && !movingUp) {
            y += speedVector;
        }

        if (movingLeft && !movingRight) {
            x -= speedVector;
        }

        if (movingRight && !movingLeft) {
            x += speedVector;
        }
    }// fine update

    public void constrainX(int minX, int maxX) {
        if (x < minX) {
            x = minX;
        }

        if (x + PLAYER_WIDTH > maxX) {
            x = maxX - PLAYER_WIDTH;
        }
    }// fine constrainX

    public void constrainY(int minY, int maxY) {
        if (y < minY) {
            y = minY;
        }

        if (y + PLAYER_HEIGHT > maxY) {
            y = maxY - PLAYER_HEIGHT;
        }
    }// fine constrainY

    public void colorCooldown(int availableColorsCount, boolean forward) {
        currentTime = System.currentTimeMillis();//DOC: Returns the current time in milliseconds.
        if (currentTime - lastColorChange >= COLOR_COOLDOWN) {
            switchColor(availableColorsCount, forward);
        }
    }// fine colorCoolDown

    public void switchColor(int availableColorsCount, boolean forward) {
        if (forward) {
            colorId = (colorId + 1) % availableColorsCount; // se è in avanti aumenta verso destra 
        } else {
            colorId = (colorId - 1 + availableColorsCount) % availableColorsCount;
        }
        lastColorChange = currentTime;
    }// fine switchColor

    public void resetToInitialSettings(double startX, double startY, int startColorId) {
        x = startX;
        y = startY;
        colorId = startColorId;
    }// fine resetToInitialSettings

    public void resetMovementFlags() {
        movingUp = false;
        movingDown = false;
        movingLeft = false;
        movingRight = false;
    }// fine resetMovementFlags

    public Shape getHitbox() {
        Path2D.Double navicella = new Path2D.Double();
        navicella.moveTo( x + (PLAYER_WIDTH * 0.5), y);
        navicella.lineTo( x + (PLAYER_WIDTH * 0.6), y + (PLAYER_HEIGHT * 0.3));
        navicella.lineTo( x + PLAYER_WIDTH, y + (PLAYER_HEIGHT * 0.8));
        navicella.lineTo( x + (PLAYER_WIDTH * 0.7), y + (PLAYER_HEIGHT * 0.8));
        navicella.lineTo( x + (PLAYER_WIDTH * 0.7), y + PLAYER_HEIGHT);
        navicella.lineTo( x + (PLAYER_WIDTH * 0.5), y + (PLAYER_HEIGHT * 0.85));
        navicella.lineTo( x + (PLAYER_WIDTH * 0.3), y + PLAYER_HEIGHT);
        navicella.lineTo( x + (PLAYER_WIDTH * 0.3), y + (PLAYER_HEIGHT * 0.8));
        navicella.lineTo( x, y + (PLAYER_HEIGHT * 0.8));
        navicella.lineTo( x + (PLAYER_WIDTH * 0.4), y + (PLAYER_HEIGHT * 0.3));
        navicella.closePath();
        return navicella;
    }// fine getHitbox

    // metodi getters per l'avatar

    public double getX() {
        return  x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return PLAYER_WIDTH;
    }

    public int getHeight() {
        return PLAYER_HEIGHT;
    }

    public int getColorId() {
        return colorId;
    }

    public boolean isInvulnerable() {
        return isInvulnerable;
    }

    // metodi setters per l'avatar
    public void setInvulnerable(boolean invulnerable) {
        this.isInvulnerable = invulnerable;
    }

    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }

    public void setMovingDown(boolean movingDown) {
        this.movingDown = movingDown;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public void setColorId(int colorId){
        this.colorId = colorId;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y; 
    }
}// fine classe Player