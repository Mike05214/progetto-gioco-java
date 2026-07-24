package src.colorclash.view;

import src.colorclash.model.GameModel;
import src.colorclash.model.Obstacle;
import src.colorclash.model.Particle;
import src.colorclash.model.Player;
import src.colorclash.model.FloatingScore;

import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.BasicStroke;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class GameSpace extends JPanel {

    // variabili d'istanza
    private JButton restartButton;
    private GameModel model;
    private Color[] colorPalette = {
            Color.RED, // ID 0
            Color.GREEN, // ID 1
            Color.CYAN, // ID 2
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

    public GameSpace() {
        setBackground(Color.BLACK);
        this.model = GameModel.getInstance();
        this.setLayout(new GridBagLayout());
        restartButton = new JButton("BACK TO MENU");
        restartButton.setFont(new Font("Impact", Font.PLAIN, RESTART_BUTTON_FONT_SIZE));
        restartButton.setPreferredSize(new Dimension(RESTART_BUTTON_WIDTH, RESTART_BUTTON_HEIGHT));
        restartButton.setVisible(false);
        this.add(restartButton);
    }// fine costruttore

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        showLegend(g2d);
        drawPlayer(g2d);
        drawObstacles(g2d);
        drawParticles(g2d);
        drawFloatingScore(g2d);
        if (model.isGameOver()) {
            showGameOver(g2d);
        }

    }// fine paintComponent

    private void drawParticles(Graphics2D g2d) {
        for (Particle p : model.getParticles()) {

            int colorId = p.getColorId();
            g2d.setColor(colorPalette[colorId]);

            // Disegna il frammento
            g2d.fillRect(p.getX(), p.getY(), p.getSize(), p.getSize());
        }
    }

    private void drawFloatingScore(Graphics2D g2d) {
        for (FloatingScore fs : model.getFloatingScores()) {
            // Imposta il colore con il livello di trasparenza attuale
            g2d.setColor(new Color(255, 255, 255, fs.getAlpha()));

            // Imposta stile e grandezza del testo
            g2d.setFont(new Font("Impact", Font.PLAIN, 20));

            // Disegna la stringa a schermo
            g2d.drawString(fs.getText(), (int) fs.getX(), (int) fs.getY());
        }
    }

    private void drawPlayer(Graphics2D g2d) {
        boolean drawPlayer = true;
        if (model.isInvulnerable()) {

            if (model.getInvulnTimer() % 20 < 10) {
                drawPlayer = false;
            }
        }

        if (drawPlayer) {
            Player player = model.getPlayer();
            g2d.setColor(colorPalette[player.getColorId()]);
            g2d.fill(player.getHitbox());
        }
    }// fine playerBlinkingHandler

    private void drawObstacles(Graphics2D g2d) {
        for (Obstacle obs : model.getEnemies()) {
            g2d.setColor(colorPalette[obs.getColorId()]);
            g2d.fill(obs.getHitbox());
        }
    }// fine drawObstacles

    private void showGameOver(Graphics2D g2d) {
        g2d.setColor(new Color(GAME_OVER_COLOR_R, GAME_OVER_COLOR_G, GAME_OVER_COLOR_B, GAME_OVER_OPACITY));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Impact", Font.PLAIN, GAME_OVER_FONT_SIZE));
        String gameOverText = "GAME OVER";
        g2d.drawString(gameOverText, getWidth() / 2 - 220, getHeight() / 2 - 80);
        g2d.setFont(new Font("Impact", Font.PLAIN, 25));
        g2d.drawString("SCORE: " + model.getScore(), getWidth() / 2 - 60, getHeight() / 2 - 35);
    }// fine showGameOver

    public void showLegend(Graphics2D g2d) {
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
                g2d.drawRect(x - LEGEND_STROKE_NEW_START, y - LEGEND_STROKE_NEW_START,
                        elementSize + LEGEND_STROKE_RESIZE, elementSize + LEGEND_STROKE_RESIZE); // ridimensiona il
                                                                                                 // rettangolo
                                                                                                 // evindenziato
            }
        }
    }// fine showLegend

    public void createBorder(int newColorId) {
        this.setBorder(BorderFactory.createLineBorder(colorPalette[newColorId], 5));
    }

    public void deleteBorder() {
        this.setBorder(null);
    }

    // getters di GameSpace
    public JButton getRestarButton() {
        return this.restartButton;
    }
}// fine classe GameSpace
