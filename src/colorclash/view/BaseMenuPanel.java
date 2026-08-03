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

    // METODI PUBBLICI

    public BaseMenuPanel() {
        setLayout(new BorderLayout());
        centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false); // per evitare che si sovrapponga ai paintings
        add(centerPanel, BorderLayout.CENTER);
    }// fine costruttore

    // METODI PROTETTI

    protected void initTitleLabel(String text) {
        JLabel titleLabel = createRainbowLabel(text);
        titleLabel.setFont(new Font("Impact", Font.PLAIN, TITLE_SIZE));
        add(titleLabel, BorderLayout.NORTH); //aggiunto a nord del BaseMenuPanel
    }// fine initTitleLabel

    protected void addComponentToCenter(Component comp, int row, boolean hasSpaceBelow) {
        GridBagConstraints gbc = new GridBagConstraints(); //classe per settare i vincoli del layout
        gbc.gridx = ONE_COLUMN;
        gbc.gridy = row;
        gbc.insets = new Insets(TOP, LEFT, hasSpaceBelow ? BETWEEN_SPACE : 0, RIGHT); //haSpaceBelow true = BETWEEN_SPACE, hasSpaceBelow false = 0
        centerPanel.add(comp, gbc);
    }// fine addComponentToCenter

    // METODI PRIVATI

    private JLabel createRainbowLabel(String text) {
        return new JLabel(text, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) { //testo originale non viene paintato perchè abbiamo omesso super.paintcomponent
                Graphics2D g2d = (Graphics2D) g;
                drawRainbowText(g2d, this);
            }
        };
    }// fine createRainBowLabel

    private void drawRainbowText(Graphics2D g2d, JLabel label) {
        g2d.setFont(label.getFont());
        String text = label.getText();
        FontMetrics fm = g2d.getFontMetrics(); //calcola le dimensioni esatte in pixel di un testo,
        //  basandosi sul font che abbiamo impostato in precedenza su g2d E Sulle impostazioni grafiche
        Color[] rainbowColors = {
                Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
                Color.CYAN, Color.BLUE, Color.MAGENTA
        };
        int y = (label.getHeight() - fm.getHeight()) / 2 + fm.getAscent(); //ragioniamo con l'altezza standard del font in questione 
        int currentX = (label.getWidth() - fm.stringWidth(text)) / 2; //centratura della scritta

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            String pattern = String.valueOf(c);

            if (Character.isWhitespace(c)) {
                currentX += fm.stringWidth(pattern);
                continue;
            }

            g2d.setColor(rainbowColors[i % rainbowColors.length]);
            g2d.drawString(pattern, currentX, y);
            currentX += fm.stringWidth(pattern);
        }
    }// fine drawRainbowText

}// fine classe astratta BaseMenuPanel
