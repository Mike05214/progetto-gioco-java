package colorclash.view;

import colorclash.model.Obstacle;
import colorclash.model.SpeedRacer;
import colorclash.model.SinusoidalMadness;
import colorclash.model.StandardObstacle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Ellipse2D;

public class ObstacleRenderer {

    // 1. CACHE DELLE FORME (allocate una sola volta in memoria)
    private static final Path2D.Double SPEED_RACER_SHAPE = createSpeedRacerShape();
    private static final Ellipse2D.Double SINUSOIDAL_SHAPE = new Ellipse2D.Double(0, 0, 70, 70); // Usa i valori fissi

    public static void render(Graphics2D g2d, Obstacle obs, Color obsColor) {
        g2d.setColor(obsColor); // Usa il colore passato da GameSpace
        g2d.translate(obs.getX(), obs.getY());

        if (obs instanceof StandardObstacle) {
            g2d.fillRect(0, 0, obs.getWidth(), obs.getHeight());
        } else if (obs instanceof SpeedRacer) {
            g2d.fill(SPEED_RACER_SHAPE);
        } else if (obs instanceof SinusoidalMadness) {
            g2d.fill(SINUSOIDAL_SHAPE);
        }
        g2d.translate(-obs.getX(), -obs.getY());
    }

    private static Path2D.Double createSpeedRacerShape() {
        Path2D.Double triangle = new Path2D.Double();
        triangle.moveTo(0, 0);
        triangle.lineTo(50, 0); // WIDTH fissa
        triangle.lineTo(50 / 2.0, 100); // HEIGHT fissa
        return triangle;
    }
}