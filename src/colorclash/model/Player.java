package src.colorclash.model;

import java.awt.Shape;
import java.awt.geom.Path2D;

import src.colorclash.utils.Config;

public class Player {

    // Coordinate e dimensioni
    private double x;
    private double y;
    private long lastColorChange = 0;
    private long currentTime = 0;
    private final double SCALE_FACTOR = 0.7071;
    private final long COLOR_COOLDOWN = Config.getInstance().getIntProperty("color_cooldown");
    private final double PLAYER_SPEED = Config.getInstance().getDoubleProperty("player_speed"); 
    private final int PLAYER_WIDTH = Config.getInstance().getIntProperty("player_width");
    private final int PLAYER_HEIGHT = Config.getInstance().getIntProperty("player_height");

    // Attributi di gioco dell'avatar
    private int colorId;
    private boolean isInvulnerable;
    private boolean movingUp, movingDown, movingLeft, movingRight;

    public Player(int startX, int startY, int startColorId) {
        this.x = startX;
        this.y = startY;
        this.colorId = startColorId;
        this.isInvulnerable = false;
    }// fine costruttore

    public void move() {
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
    }// fine move

    public void constrainX(int minX, int maxX) {
        if (this.x < minX) {
            this.x = minX;
        }

        if (this.x + PLAYER_WIDTH > maxX) {
            this.x = maxX - PLAYER_WIDTH;
        }
    }// fine constrainX

    public void constrainY(int minY, int maxY) {
        if (this.y < minY) {
            this.y = minY;
        }

        if (this.y + PLAYER_HEIGHT > maxY) {
            this.y = maxY - PLAYER_HEIGHT;
        }
    }// fine constrainY

    public void colorCooldown(int availableColorsCount, boolean forward) {
        this.currentTime = System.currentTimeMillis();
        if (currentTime - lastColorChange >= COLOR_COOLDOWN) {
            switchColor(availableColorsCount, forward);
        }
    }// fine colorCoolDown

    public void switchColor(int availableColorsCount, boolean forward) {
    if (forward) {
        this.colorId = (this.colorId + 1) % availableColorsCount;
    } else {
        this.colorId = (this.colorId - 1 + availableColorsCount) % availableColorsCount;
    }
    lastColorChange = currentTime;
} // fine switchColor

    public void resetToInitialSettings(int startX, int startY, int startColorId) {
        this.x = startX;
        this.y = startY;
        this.colorId = startColorId;
    }// fine resetToInitialSettings

    public void resetMovementFlags() {
        this.movingUp = false;
        this.movingDown = false;
        this.movingLeft = false;
        this.movingRight = false;
    }// fine resetMovementFlags

    public Shape getHitbox() {
        Path2D.Double navicella = new Path2D.Double();
        // costruzione della forma dell'avatar
        navicella.moveTo(this.x + (PLAYER_WIDTH * 0.5), this.y);
        navicella.lineTo(this.x + (PLAYER_WIDTH * 0.6), this.y + (PLAYER_HEIGHT * 0.3));
        navicella.lineTo(this.x + PLAYER_WIDTH, this.y + (PLAYER_HEIGHT * 0.8));
        navicella.lineTo(this.x + (PLAYER_WIDTH * 0.7), this.y + (PLAYER_HEIGHT * 0.8));
        navicella.lineTo(this.x + (PLAYER_WIDTH * 0.7), this.y + PLAYER_HEIGHT);
        navicella.lineTo(this.x + (PLAYER_WIDTH * 0.5), this.y + (PLAYER_HEIGHT * 0.85));
        navicella.lineTo(this.x + (PLAYER_WIDTH * 0.3), this.y + PLAYER_HEIGHT);
        navicella.lineTo(this.x + (PLAYER_WIDTH * 0.3), this.y + (PLAYER_HEIGHT * 0.8));
        navicella.lineTo(this.x, this.y + (PLAYER_HEIGHT * 0.8));
        navicella.lineTo(this.x + (PLAYER_WIDTH * 0.4), this.y + (PLAYER_HEIGHT * 0.3));
        navicella.closePath();
        // fine costruzione della forma dell'avatar
        return navicella;
    }// fine getHitbox

    // metodi getters per l'avatar

    public int getX() {
        return (int) x;
    } // cast esplicito a int, il metodo fillRect nella view si aspetta delle
      // coordinate intere non double

    public int getY() {
        return (int) y;
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

    public void setX(int x) {
        this.x = x ;
    }

    public void setY(int y) {
        this.y = y ; 
    }
}// fine classe Avatar