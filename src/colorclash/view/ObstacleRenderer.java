package colorclash.view;

import colorclash.model.IObstacle; // Unico import del Model necessario

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Ellipse2D;
import java.awt.GradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.BasicStroke;

public class ObstacleRenderer {

    // costanti statiche
    private static final Path2D.Double SPEED_RACER_SHAPE = createSpeedRacerShape();
    private static final Ellipse2D.Double SINUSOIDAL_SHAPE = new Ellipse2D.Double(0, 0, 70, 70); 

    // METODI PUBBILICI

    public static void render(Graphics2D g2d, IObstacle obs, Color obsColor) {
        int w = obs.getWidth();
        int h = obs.getHeight();

        g2d.translate(obs.getX(), obs.getY());
        Color darkerColor = obsColor.darker().darker().darker();

        switch (obs.getType()) {
            case "StandardObstacle":
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.fillRect(4, 4, w, h);
                GradientPaint gpStand = new GradientPaint(0, 0, obsColor, w, h, darkerColor);
                g2d.setPaint(gpStand);
                g2d.fillRect(0, 0, w, h);
                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawRect(0, 0, w, h);
                break;

            case "SpeedRacer":
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.translate(4, 4);
                g2d.fill(SPEED_RACER_SHAPE);
                g2d.translate(-4, -4);
                GradientPaint gpSpeed = new GradientPaint(0, 0, obsColor, 0, h, darkerColor);
                g2d.setPaint(gpSpeed);
                g2d.fill(SPEED_RACER_SHAPE);
                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.setStroke(new BasicStroke(2f));
                g2d.draw(SPEED_RACER_SHAPE);
                break;

            case "SinusoidalMadness":
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.translate(4, 4);
                g2d.fill(SINUSOIDAL_SHAPE);
                g2d.translate(-4, -4);
                RadialGradientPaint rgp = new RadialGradientPaint(
                    w / 2f, h / 2f, w / 2f, 
                    new float[]{0f, 1f}, 
                    new Color[]{obsColor, darkerColor}
                );
                g2d.setPaint(rgp);
                g2d.fill(SINUSOIDAL_SHAPE);
                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.setStroke(new BasicStroke(2f));
                g2d.draw(SINUSOIDAL_SHAPE);
                break;
        }

        g2d.translate(-obs.getX(), -obs.getY());
    }// fine render

    // METODI PRIVATI

    private static Path2D.Double createSpeedRacerShape() {
        Path2D.Double triangle = new Path2D.Double();
        triangle.moveTo(0, 0);
        triangle.lineTo(50, 0); 
        triangle.lineTo(50 / 2.0, 100); 
        return triangle;
    }// fine createSpeedRacerShape

}// fine classe ObstacleRender