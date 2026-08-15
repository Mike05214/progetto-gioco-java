package colorclash.view;

import colorclash.model.Obstacle;
import colorclash.model.SpeedRacer;
import colorclash.model.SinusoidalMadness;
import colorclash.model.StandardObstacle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Ellipse2D;
import java.awt.GradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.BasicStroke;

public class ObstacleRenderer {

    private static final Path2D.Double SPEED_RACER_SHAPE = createSpeedRacerShape();
    private static final Ellipse2D.Double SINUSOIDAL_SHAPE = new Ellipse2D.Double(0, 0, 70, 70); 

    public static void render(Graphics2D g2d, Obstacle obs, Color obsColor) {
        int w = obs.getWidth();
        int h = obs.getHeight();

        // Traslazione sulle coordinate dell'ostacolo
        g2d.translate(obs.getX(), obs.getY());

        // Colore scuro per il volume 3D
        Color darkerColor = obsColor.darker().darker();

        if (obs instanceof StandardObstacle) {
            // 1. Ombra
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(4, 4, w, h);
            
            // 2. Gradiente lineare per il finto 3D
            GradientPaint gp = new GradientPaint(0, 0, obsColor, w, h, darkerColor);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
            
            // 3. Contorno luminoso
            g2d.setColor(new Color(255, 255, 255, 180));
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawRect(0, 0, w, h);
            
        } 
        else if (obs instanceof SpeedRacer) {
            // 1. Ombra
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.translate(4, 4);
            g2d.fill(SPEED_RACER_SHAPE);
            g2d.translate(-4, -4);

            // 2. Gradiente lineare 
            GradientPaint gp = new GradientPaint(0, 0, obsColor, 0, h, darkerColor);
            g2d.setPaint(gp);
            g2d.fill(SPEED_RACER_SHAPE);
            
            // 3. Contorno luminoso
            g2d.setColor(new Color(255, 255, 255, 180));
            g2d.setStroke(new BasicStroke(2f));
            g2d.draw(SPEED_RACER_SHAPE);
            
        } 
        else if (obs instanceof SinusoidalMadness) {
            // 1. Ombra
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.translate(4, 4);
            g2d.fill(SINUSOIDAL_SHAPE);
            g2d.translate(-4, -4);

            // 2. Gradiente radiale (effetto sferico)
            RadialGradientPaint rgp = new RadialGradientPaint(
                w / 2f, h / 2f, w / 2f, 
                new float[]{0f, 1f}, 
                new Color[]{obsColor, darkerColor}
            );
            g2d.setPaint(rgp);
            g2d.fill(SINUSOIDAL_SHAPE);
            
            // 3. Contorno luminoso
            g2d.setColor(new Color(255, 255, 255, 180));
            g2d.setStroke(new BasicStroke(2f));
            g2d.draw(SINUSOIDAL_SHAPE);
        }

        // Ripristino origine
        g2d.translate(-obs.getX(), -obs.getY());
    }

    private static Path2D.Double createSpeedRacerShape() {
        Path2D.Double triangle = new Path2D.Double();
        triangle.moveTo(0, 0);
        triangle.lineTo(50, 0); 
        triangle.lineTo(50 / 2.0, 100); 
        return triangle;
    }
}