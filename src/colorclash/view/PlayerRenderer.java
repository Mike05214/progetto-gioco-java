package colorclash.view;

import colorclash.model.IPlayer;

import colorclash.utils.Config;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.GradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.BasicStroke;

public class PlayerRenderer {

    // costanti statiche
    private static final Path2D.Double PLAYER_SHAPE = createPlayerShape();

    //METODI PUBBLICI

    public static void render(Graphics2D g2d, IPlayer player, Color playerColor, boolean isInvulnerable,
            boolean drawShipBody) {
        int w = player.getWidth();
        int h = player.getHeight();

        g2d.translate(player.getX(), player.getY());

        if (isInvulnerable) {
            int margin = 15;
            int shieldW = w + margin * 2;
            int shieldH = h + margin * 2;

            RadialGradientPaint shieldPaint = new RadialGradientPaint(
                    w / 2f, h / 2f, shieldW / 2f,
                    new float[] { 0.7f, 1f },
                    new Color[] { new Color(255, 255, 255, 0), new Color(0, 255, 255, 150) });
            g2d.setPaint(shieldPaint);
            g2d.fillOval(-margin, -margin, shieldW, shieldH);

            g2d.setColor(new Color(0, 255, 255, 200));
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawOval(-margin, -margin, shieldW, shieldH);
        }

        if (!drawShipBody) {
            g2d.translate(-player.getX(), -player.getY());
            return;
        }

        g2d.translate(5, 5);
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fill(PLAYER_SHAPE);
        g2d.translate(-5, -5);
        Color darkerColor = playerColor.darker().darker();
        GradientPaint gp = new GradientPaint(0, 0, playerColor, 0, h, darkerColor);
        g2d.setPaint(gp);
        g2d.fill(PLAYER_SHAPE);
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.setStroke(new BasicStroke(2f));
        g2d.draw(PLAYER_SHAPE);
        g2d.setColor(new Color(180, 230, 255, 220));
        g2d.fillOval((int) (w * 0.45), (int) (h * 0.35), (int) (w * 0.1), (int) (h * 0.2));
        g2d.translate(-player.getX(), -player.getY());
    }// fine render

    // METODI PRIVATO

    private static Path2D.Double createPlayerShape() {
        int width = Config.getInstance().getIntProperty("player_width");
        int height = Config.getInstance().getIntProperty("player_height");

        Path2D.Double navicella = new Path2D.Double();
        navicella.moveTo(width * 0.5, 0);
        navicella.lineTo(width * 0.6, height * 0.3);
        navicella.lineTo(width, height * 0.8);
        navicella.lineTo(width * 0.7, height * 0.8);
        navicella.lineTo(width * 0.7, height);
        navicella.lineTo(width * 0.5, height * 0.85);
        navicella.lineTo(width * 0.3, height);
        navicella.lineTo(width * 0.3, height * 0.8);
        navicella.lineTo(0, height * 0.8);
        navicella.lineTo(width * 0.4, height * 0.3);
        navicella.closePath();

        return navicella;
    }// fine createPlayerShape

}// fine classe PlayerRenderer