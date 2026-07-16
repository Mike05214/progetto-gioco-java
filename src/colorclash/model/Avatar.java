package src.colorclash.model;

import java.awt.Shape;
import java.awt.geom.Path2D;

public class Avatar {

    // Coordinate e dimensioni
    private double x;
    private double y;
    private int width;
    private int height;
    private long lastColorChange = 0;
    private long currentTime = 0;
    private final double SCALE_FACTOR = 0.7071;
    private final long COLOR_COOLDOWN = 20;

    // Attributi di gioco dell'avatar
    private double speed;
    private int colorId;
    private boolean isInvulnerable;
    private boolean movingUp, movingDown, movingLeft, movingRight;

    public Avatar(int startX, int startY, int startColorId) {
        this.x = startX;
        this.y = startY;

        this.width = 70;
        this.height = 80;
        this.speed = 2.5;
        this.colorId = startColorId;
        this.isInvulnerable = false;
    }// fine costruttore

    public void move() {
        boolean diagonalMovement = (movingLeft ^ movingRight) && (movingUp ^ movingDown);
        double speedVector = 0;

        if (diagonalMovement) {
            speedVector = speed * SCALE_FACTOR;
        } else {
            speedVector = speed;
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

        if (this.x + this.width > maxX) {
            this.x = maxX - this.width;
        }
    }// fine constrainX

    public void constrainY(int minY, int maxY) {
        if (this.y < minY) {
            this.y = minY;
        }

        if (this.y + this.height > maxY) {
            this.y = maxY - this.height;
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
        navicella.moveTo(this.x + (width * 0.5), this.y);
        navicella.lineTo(this.x + (width * 0.6), this.y + (height * 0.3));
        navicella.lineTo(this.x + width, this.y + (height * 0.8));
        navicella.lineTo(this.x + (width * 0.7), this.y + (height * 0.8));
        navicella.lineTo(this.x + (width * 0.7), this.y + height);
        navicella.lineTo(this.x + (width * 0.5), this.y + (height * 0.85));
        navicella.lineTo(this.x + (width * 0.3), this.y + height);
        navicella.lineTo(this.x + (width * 0.3), this.y + (height * 0.8));
        navicella.lineTo(this.x, this.y + (height * 0.8));
        navicella.lineTo(this.x + (width * 0.4), this.y + (height * 0.3));
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
        return width;
    }

    public int getHeight() {
        return height;
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
}// fine classe Avatar