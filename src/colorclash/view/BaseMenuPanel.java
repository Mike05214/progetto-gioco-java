package src.colorclash.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public abstract class BaseMenuPanel extends JPanel {
    // costanti protette
    protected final int BUTTON_WIDTH = 200;
    protected final int BUTTON_HEIGHT = 50;
    protected final int BUTTON_TEXT_SIZE = 20;
    protected final int TITLE_SIZE = 60;
    protected final int TOP = 0;
    protected final int LEFT = 0;
    protected final int RIGHT = 0;
    protected final int BETWEEN_SPACE = 30;
    protected final int ONE_COLUMN = 0;

    // variabili d'istanza
    protected JPanel centerPanel;

    // metodi pubblici
    public BaseMenuPanel() {
        this.setLayout(new BorderLayout());

        this.centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        this.add(centerPanel, BorderLayout.CENTER);
    }// fine costruttore

    // metodi protetti
    protected void initTitleLabel(String text, Color color) {
        JLabel titleLabel = createRainbowLabel(text);
        titleLabel.setFont(new Font("Impact", Font.PLAIN, TITLE_SIZE));
        titleLabel.setForeground(color);
        this.add(titleLabel, BorderLayout.NORTH);
    }// fine initTitleLabel

    private JLabel createRainbowLabel(String text) {
        return new JLabel(text, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                paintLabelBackground(g, this);
                drawRainbowText((Graphics2D) g, this);
            }
        };
    }// fine createRainBowLabel

    private void paintLabelBackground(Graphics g, JLabel label) {
        if (label.isOpaque()) {
            g.setColor(label.getBackground());
            g.fillRect(0, 0, label.getWidth(), label.getHeight());
        }
    }// fine paintLabelBackground

    private void drawRainbowText(Graphics2D g2, JLabel label) {
        //g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(label.getFont());
        String text = label.getText();
        FontMetrics fm = g2.getFontMetrics();
        Color[] rainbowColors = {
                Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
                Color.CYAN, Color.BLUE, Color.MAGENTA
        };
        int y = (label.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        int xAttuale = (label.getWidth() - fm.stringWidth(text)) / 2;

        for (int i = 0; i < text.length(); i++) {
            char carattere = text.charAt(i);
            String pattern = String.valueOf(carattere);

            if (Character.isWhitespace(carattere)) {
                xAttuale += fm.stringWidth(pattern);
                continue;
            }

            g2.setColor(rainbowColors[i % rainbowColors.length]);
            g2.drawString(pattern, xAttuale, y);
            xAttuale += fm.stringWidth(pattern);
        }
    }// fine drawRainbowText

    protected void addComponentToCenter(Component comp, int row, boolean hasSpaceBelow) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = ONE_COLUMN;
        gbc.gridy = row;
        gbc.insets = new Insets(TOP, LEFT, hasSpaceBelow ? BETWEEN_SPACE : 0, RIGHT);
        centerPanel.add(comp, gbc);
    }// fine addComponentToCenter

}// fine classe astratta BaseMenuPanel
