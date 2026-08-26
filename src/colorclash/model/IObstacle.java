package colorclash.model;

public interface IObstacle {
    boolean isActive();
    String getType();
    double getX();
    double getY();
    double getFallSpeed();
    int getColorId();
    int getWidth();
    int getHeight();
    IHitbox getHitbox();
}