package colorclash.model;

public interface IParticle {
    
    double getX();
    double getY();
    int getSize();
    int getColorId();
    boolean isActive();
    boolean isTriangle();
    int getAlpha();

}// fine IParticle