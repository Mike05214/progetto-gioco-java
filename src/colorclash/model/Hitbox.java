package colorclash.model;

import java.util.ArrayList;
import java.util.List;

public class Hitbox implements IHitbox {
    
    //variabili d'istanza
    private List<LogicalRect> rectangles;
    private List<Double> offsetX;
    private List<Double> offsetY;
    
    public Hitbox() {
        this.rectangles = new ArrayList<>();
        this.offsetX = new ArrayList<>();
        this.offsetY = new ArrayList<>();
    }// fine costruttore

    // METODI PUBBLICI DI LOGICA
    
    public void addRect(double offX, double offY, double width, double height) {
        rectangles.add(new LogicalRect(0, 0, width, height));
        offsetX.add(offX);
        offsetY.add(offY);
    }//fine addRect
    
    public void updatePosition(double baseX, double baseY) {
        for (int i = 0; i < rectangles.size(); i++) {
            rectangles.get(i).setPosition(baseX + offsetX.get(i), baseY + offsetY.get(i));
        }
    }// fine updatePosition

    // GETTER IMPLEMENTATO DALL'INTERFACCIA IHitbox
    
    @Override
    public List<LogicalRect> getRectangles() {
        return rectangles;
    }
    
    // ALTRI GETTERS (Usati internamente dal Model per la logica/aggiornamenti)
    
    public List<Double> getOffsetX() {
        return offsetX;
    }

    public List<Double> getOffsetY() {
        return offsetY;
    }
    
}// fine classe Hitbox