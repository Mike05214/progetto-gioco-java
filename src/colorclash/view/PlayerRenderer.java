package colorclash.view;

import colorclash.model.Player;
import colorclash.utils.Config;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class PlayerRenderer {

    // 1. CACHE DELLA FORMA
    private static final Path2D.Double PLAYER_SHAPE = createPlayerShape();
    

    public static void render(Graphics2D g2d, Player player, Color playerColor) {
    if (player.isInvulnerable() && (System.currentTimeMillis() % 200 < 100)) {
        return; 
    }
    g2d.setColor(playerColor); // Usa il colore passato da GameSpace
    g2d.translate(player.getX(), player.getY());
    g2d.fill(PLAYER_SHAPE);
    g2d.translate(-player.getX(), -player.getY());
    }

    private static Path2D.Double createPlayerShape() {
        int width = Config.getInstance().getIntProperty("player_width");
        int height = Config.getInstance().getIntProperty("player_height");
        
        Path2D.Double navicella = new Path2D.Double();
        // Le coordinate sono state slegate da X e Y, partendo da 0
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
    }
}