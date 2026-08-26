package colorclash.model;

public interface IParticle {
    double getX();
    double getY();
    int getSize();
    int getColorId();
    boolean isActive();
    boolean isTriangle();
    public int getAlpha() ;
    // Aggiungi qui getAlpha() o altri getter se li usi per il rendering
}