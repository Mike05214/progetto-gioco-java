package colorclash.view;

import colorclash.model.IGameModel;
import colorclash.model.IStar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public abstract class BaseMenuPanel extends JPanel {

    // costanti protette
    protected final int SOUND_BUTTON_WIDTH = 100;
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
    protected IGameModel model; 
    protected MainFrame frame;

    public BaseMenuPanel(MainFrame mainFrame, IGameModel injectedModel) {
        frame = mainFrame;
        model = injectedModel; 
        setLayout(new BorderLayout());
        centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        add(centerPanel, BorderLayout.CENTER);
    }// fine costruttore

    // METODI PROTETTI

    protected void initTitleLabel(String text) {
        JLabel titleLabel = createRainbowLabel(text);
        titleLabel.setFont(new Font("Impact", Font.PLAIN, TITLE_SIZE));
        add(titleLabel, BorderLayout.NORTH); 
    }// fine initTitleLabel

    protected void addComponentToCenter(Component comp, int row, boolean hasSpaceBelow) {
        GridBagConstraints gbc = new GridBagConstraints(); 
        gbc.gridx = ONE_COLUMN;
        gbc.gridy = row;
        gbc.insets = new Insets(TOP, LEFT, hasSpaceBelow ? BETWEEN_SPACE : 0, RIGHT); 
        centerPanel.add(comp, gbc);
    }// fine addComponentToCenter

    // METODI PRIVATI

    private JLabel createRainbowLabel(String text) {
        return new JLabel(text, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) { 
                Graphics2D g2d = (Graphics2D) g;
                drawRainbowText(g2d, this);
            }
        };
    }// fine createRainBowLabel

    private void drawRainbowText(Graphics2D g2d, JLabel label) {
        g2d.setFont(label.getFont());
        String text = label.getText();
        FontMetrics fm = g2d.getFontMetrics(); 
        Color[] rainbowColors = {
                Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
                Color.CYAN, Color.BLUE, Color.MAGENTA
        };
        int y = (label.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        int currentX = (label.getWidth() - fm.stringWidth(text)) / 2; 

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

    // METODI PUBBLICI

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g2d);
        g2d.setColor(new Color(10, 10, 20));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        List<? extends IStar> stelle = model.getStars();
        if (stelle != null) {
            Rectangle2D.Double starRect = new Rectangle2D.Double();
            int offsetY = frame.getGamePanel().getHudPanel().getHudPanelHeight();

            for (IStar s : stelle) {
                g2d.setColor(new Color(255, 255, 255, s.getAlpha()));
                starRect.setRect(s.getX(), s.getY() + offsetY, s.getSize(), s.getSize());
                g2d.fill(starRect);
            }
        }
    }// fine paintComponent

}// fine classe astratta BaseMenuPanel