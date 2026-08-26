package colorclash.model;

import java.util.List;

public interface IHitbox {
    // Il wildcard "? extends" è fondamentale per le liste con generici
    List<? extends ILogicalRect> getRectangles(); 
}