package colorclash.model;

public interface IPlayer {
    
    double getX();
    double getY();
    int getWidth();
    int getHeight();
    int getColorId();
    IHitbox getHitbox();
    void setMovingUp(boolean movingUp);
    void setMovingDown(boolean movingDown);
    void setMovingLeft(boolean movingLeft);
    void setMovingRight(boolean movingRight);
    void colorCooldown(int availableColorsCount, boolean forward);
    void resetMovementFlags();

}// fine IPlayer