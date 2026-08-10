package src.colorclash.view;

import src.colorclash.model.GameModel;
import src.colorclash.model.Star;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;// DOC: The GridBagLayout class is a flexible layout manager that aligns components vertically, 
                              // horizontally or along their baseline without requiring that the components be of the same size. 
                              // Each GridBagLayout object maintains a dynamic, rectangular grid of cells,
                              // with each component occupying one or more cells, called its display area.
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
    protected GameModel model;
    protected MainFrame frame;

    public BaseMenuPanel(MainFrame mainFrame) {
        frame = mainFrame;
        model = GameModel.getInstance();
        setLayout(new BorderLayout());
        centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        add(centerPanel, BorderLayout.CENTER);
    }// fine costruttore

    // METODI PROTETTI

    protected void initTitleLabel(String text) {
        JLabel titleLabel = createRainbowLabel(text);
        titleLabel.setFont(new Font("Impact", Font.PLAIN, TITLE_SIZE));
        add(titleLabel, BorderLayout.NORTH); //aggiunto a nord del BaseMenuPanel
    }// fine initTitleLabel

    protected void addComponentToCenter(Component comp, int row, boolean hasSpaceBelow) {
        // DOC: The constraints object specifies where a component's display area should be located on the grid
        // and how the component should be positioned within its display area. 
        // In addition to its constraints object,
        // the GridBagLayout also considers each component's minimum and preferred sizes in order to determine a component's size.
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
        FontMetrics fm = g2d.getFontMetrics(); // DOC: The FontMetrics class defines a font metrics object, 
                                                //which encapsulates information about the rendering of a particular font on a particular screen.
        Color[] rainbowColors = {
                Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
                Color.CYAN, Color.BLUE, Color.MAGENTA
        };
        int y = (label.getHeight() - fm.getHeight()) / 2 + fm.getAscent();//Determines the font ascent of the Font described by this FontMetrics object.
        int currentX = (label.getWidth() - fm.stringWidth(text)) / 2; //Returns the total advance width for showing the specified String in this Font.

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

    //METODI PUBBLICI

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g2d);
        g2d.setColor(new Color(10, 10, 20));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        List<Star> stelle = model.getStars();
        if (stelle != null) {
            Rectangle2D.Double starRect = new Rectangle2D.Double();
            int offsetY = frame.getGamePanel().getHudPanel().getHudPanelHeight();

            for (Star s : stelle) {
                g2d.setColor(new Color(255, 255, 255, s.getAlpha()));
                starRect.setRect(s.getX(), s.getY() + offsetY, s.getSize(), s.getSize());
                g2d.fill(starRect);
            }
        }
    }// fine paintComponent

}// fine classe astratta BaseMenuPanel
