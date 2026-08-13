package colorclash.model;

import java.util.ArrayList;
import java.util.List;

public class Hitbox {
    private List<LogicalRect> rectangles = new ArrayList<>();
    private List<Double> offsetX = new ArrayList<>();
    private List<Double> offsetY = new ArrayList<>();

    // Aggiunge un rettangolo specificando la sua distanza relativa dall'ostacolo
    public void addRect(double offX, double offY, double width, double height) {
        rectangles.add(new LogicalRect(0, 0, width, height));
        offsetX.add(offX);
        offsetY.add(offY);
    }

    // Aggiorna le coordinate fisiche di tutti i rettangoli quando l'ostacolo si muove
    public void updatePosition(double baseX, double baseY) {
        for (int i = 0; i < rectangles.size(); i++) {
            rectangles.get(i).setPosition(baseX + offsetX.get(i), baseY + offsetY.get(i));
        }
    }

    public List<LogicalRect> getRectangles() { 
        return rectangles; 
    }
}