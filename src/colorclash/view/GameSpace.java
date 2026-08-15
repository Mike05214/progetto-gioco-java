package colorclash.view;

import colorclash.model.GameModel;
import colorclash.model.Obstacle;
import colorclash.model.Particle;
import colorclash.model.Star;
import colorclash.model.FloatingScore;

import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.BasicStroke;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class GameSpace extends JPanel {

    // variabili d'istanza
    private JButton restartButton;
    private boolean forceDrawPlayer = false;
    private GameModel model;
    private Color[] colorPalette = {
            Color.RED,
            Color.GREEN,
            Color.CYAN,
            Color.ORANGE
    };

    // costanti
    private final int RESTART_BUTTON_FONT_SIZE = 20;
    private final int RESTART_BUTTON_WIDTH = 200;
    private final int RESTART_BUTTON_HEIGHT = 40;
    private final int GAME_OVER_COLOR_R = 0;
    private final int GAME_OVER_COLOR_G = 0;
    private final int GAME_OVER_COLOR_B = 0;
    private final int GAME_OVER_OPACITY = 150;
    private final int GAME_OVER_FONT_SIZE = 100;
    private final int LEGEND_STROKE_WIDTH = 3;
    private final int LEGEND_STROKE_NEW_START = 2;
    private final int LEGEND_STROKE_RESIZE = 4;
    private final int GAME_OVER_HEIGHT_OFFSET = 80;
    private final int GAME_OVER_SCORE_HEIGHT_OFFSET = 35;

    public GameSpace() {
        setBackground(new Color(10, 10, 20));
        model = GameModel.getInstance();
        setLayout(new GridBagLayout());
        restartButton = new JButton("BACK TO MENU");
        restartButton.setFont(new Font("Impact", Font.PLAIN, RESTART_BUTTON_FONT_SIZE));
        restartButton.setPreferredSize(new Dimension(RESTART_BUTTON_WIDTH, RESTART_BUTTON_HEIGHT));
        restartButton.setVisible(false);
        add(restartButton);
    }// fine costruttore

    // METODI PUBBLICI

    public void createBorder(int newColorId) {
        setBorder(BorderFactory.createLineBorder(colorPalette[newColorId], 5));
    }// fine createBorder

    public void deleteBorder() {
        setBorder(null);
    }// fine deleteBorder

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawStars(g2d);
        showLegend(g2d);
        drawPlayer(g2d);
        drawObstacles(g2d);
        drawParticles(g2d);
        drawFloatingScore(g2d);
        if (model.isGameOver()) {
            showGameOver(g2d);
        }

    }// fine paintComponent

    // METODI PRIVATI

    private void drawParticles(Graphics2D g2d) {
        Rectangle2D.Double particleRect = new Rectangle2D.Double();
        Path2D.Double triangle = new Path2D.Double();

        for (Particle p : model.getParticles()) {

            // Early continue: se la particella è inattiva, passa subito alla successiva
            if (!p.isActive()) {
                continue;
            }

            // 1. Impostazione Colore e Trasparenza
            int colorId = p.getColorId();
            Color baseColor = colorPalette[colorId];
            g2d.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), p.getAlpha()));

            // 2. Disegno della Forma (Triangolo o Rettangolo)
            if (p.isTriangle()) {
                double s = p.getSize();
                triangle.reset();
                triangle.moveTo(p.getX(), p.getY() - s);
                triangle.lineTo(p.getX() - s, p.getY() + s);
                triangle.lineTo(p.getX() + s, p.getY() + s);
                triangle.closePath();

                g2d.fill(triangle);
            } else {
                particleRect.setRect(p.getX(), p.getY(), p.getSize(), p.getSize());

                g2d.fill(particleRect);
            }
        }
    }// fine drawParticles

    private void drawFloatingScore(Graphics2D g2d) {
        for (FloatingScore fs : model.getFloatingScores()) {
            g2d.setColor(new Color(255, 255, 255, fs.getAlpha()));
            g2d.setFont(new Font("Impact", Font.PLAIN, 20));
            g2d.drawString(fs.getText(), (float) fs.getX(), (float) fs.getY());
        }
    }// fine drawFloatingScore

    private void drawPlayer(Graphics2D g2d) {

        if (model.isPlayerDead()) {
            return; // Nasconde la navicella e lo scudo
        }
        boolean drawShipBody = true;
        boolean isInvuln = model.isInvulnerable();

        if (isInvuln && !forceDrawPlayer) {
            if (model.getInvulnTimer() % 150 < 75) {
                drawShipBody = false;
            }
        }

        PlayerRenderer.render(g2d, model.getPlayer(), colorPalette[model.getPlayer().getColorId()], isInvuln,
                drawShipBody);
    }// fine drawPlayer

    private void drawObstacles(Graphics2D g2d) {
        for (Obstacle obs : model.getEnemies()) {
            if (obs.isActive()) {
                // Utilizzo del renderer per evitare allocazioni continue
                ObstacleRenderer.render(g2d, obs, colorPalette[obs.getColorId()]);
            }
        }
    }// fine drawObstacles

    private void drawStars(Graphics2D g2d) {
        Rectangle2D.Double starRect = new Rectangle2D.Double();

        for (Star s : model.getStars()) {
            g2d.setColor(new Color(255, 255, 255, s.getAlpha()));
            starRect.setRect(s.getX(), s.getY(), s.getSize(), s.getSize());
            g2d.fill(starRect);
        }
    }// fine drawStars

    private void showGameOver(Graphics2D g2d) {
        g2d.setColor(new Color(GAME_OVER_COLOR_R, GAME_OVER_COLOR_G, GAME_OVER_COLOR_B, GAME_OVER_OPACITY));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Impact", Font.PLAIN, GAME_OVER_FONT_SIZE));
        String gameOverText = "GAME OVER";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(gameOverText, (getWidth() - fm.stringWidth(gameOverText)) / 2,
                getHeight() / 2 - GAME_OVER_HEIGHT_OFFSET);
        g2d.setFont(new Font("Impact", Font.PLAIN, 25));
        fm = g2d.getFontMetrics();
        String score = "SCORE: " + model.getScore();
        g2d.drawString(score, (getWidth() - fm.stringWidth(score)) / 2,
                getHeight() / 2 - GAME_OVER_SCORE_HEIGHT_OFFSET);
    }// fine showGameOver

    private void showLegend(Graphics2D g2d) {
        int elementSize = 20;
        int elementDistance = 10;
        int marginX = 20;
        int marginY = 20;
        int currentPhaseColors = model.getAvailableColorsCount();
        int currentColorId = model.getPlayer().getColorId();
        int totalWidth = (currentPhaseColors * elementSize) + (elementDistance * (currentPhaseColors - 1));
        int startX = getWidth() - totalWidth - marginX;
        int startY = getHeight() - elementSize - marginY;

        for (int i = 0; i < currentPhaseColors; i++) {
            int x = startX + (i * (elementSize + elementDistance));
            int y = startY;
            g2d.setColor(colorPalette[i]);
            g2d.fillRect(x, y, elementSize, elementSize);

            if (i == currentColorId) {
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(LEGEND_STROKE_WIDTH));
                g2d.drawRect(x - LEGEND_STROKE_NEW_START,
                        y - LEGEND_STROKE_NEW_START,
                        elementSize + LEGEND_STROKE_RESIZE,
                        elementSize + LEGEND_STROKE_RESIZE);
            }
        }
    }// fine showLegend

    // getters di GameSpace

    public JButton getRestarButton() {
        return restartButton;
    }

    // setters di GameSpace

    public void setForceDrawPlayer(boolean force) {
        forceDrawPlayer = force;
    }

}// fine classe GameSpace