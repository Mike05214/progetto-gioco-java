package colorclash.model;

public class CollisionChecker {
    
    public static boolean checkCollision(Hitbox h1, Hitbox h2) {
        for (LogicalRect r1 : h1.getRectangles()) {
            for (LogicalRect r2 : h2.getRectangles()) {
                if (intersect(r1, r2)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Metodo helper matematico puro che verifica se due rettangoli hanno intersezione
    private static boolean intersect(LogicalRect r1, LogicalRect r2) {
        return r1.getX() < r2.getX() + r2.getWidth() &&
               r1.getX() + r1.getWidth() > r2.getX() &&
               r1.getY() < r2.getY() + r2.getHeight() &&
               r1.getY() + r1.getHeight() > r2.getY();
    }
}