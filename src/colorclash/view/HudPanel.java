package colorclash.view;

import java.net.URL;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;

import javax.imageio.ImageIO;

public class HudPanel extends JPanel {

    // costanti
    private final int MAX_LIVES = 3;
    private final int HUD_WIDTH = 0;
    private final int HUD_HEIGHT = 35;
    private final int PAUSE_BUTTON_WIDTH = 150;
    private final int PAUSE_BUTTON_FONT_SIZE = 24;
    private final int SCORE_LABEL_FONT_SIZE = 20;
    private final int LIVES_CONTAINER_WIDTH = 150;
    private final int LC_BORDER_TOP = 2;
    private final int LC_BORDER_LEFT = 0;
    private final int LC_BORDER_BOTTOM = 0;
    private final int LC_BORDER_RIGHT = 5;
    private final int HGAP = 10;
    private final int VGAP = 0;

    // variabili d'istanza
    private JButton pauseButton;
    private JLabel titleScoreLabel;
    private JLabel valueScoreLabel;
    private JPanel livesContainer;
    private int lastLives = -1;
    private int lastScore = -1;
    private JLabel[] heartLabels = new JLabel[MAX_LIVES];
    private Icon saveIcon;

    public HudPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(HUD_WIDTH, HUD_HEIGHT));
        initWesternPanel();
        initCenterPanel();
        initLivesContainer();
    }// fine costruttore

    // METODI PRIVATI

    private void initWesternPanel() {
        JPanel westernPanel = new JPanel(new BorderLayout());
        pauseButton = new JButton("⏸️");
        pauseButton.setMnemonic(KeyEvent.VK_X);
        pauseButton.setFont(new Font("Dialog", Font.PLAIN, PAUSE_BUTTON_FONT_SIZE));
        pauseButton.setPreferredSize(new Dimension(PAUSE_BUTTON_WIDTH, HUD_HEIGHT));
        westernPanel.add(pauseButton);
        add(westernPanel, BorderLayout.WEST);
    }// fine initWesternPanel

    private void initCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        
        titleScoreLabel = new JLabel("SCORE: ");
        titleScoreLabel.setForeground(Color.BLACK);
        titleScoreLabel.setFont(new Font("Impact", Font.PLAIN, SCORE_LABEL_FONT_SIZE));
        
        valueScoreLabel = new JLabel("0");
        valueScoreLabel.setForeground(Color.BLACK);
        valueScoreLabel.setFont(new Font("Impact", Font.PLAIN, SCORE_LABEL_FONT_SIZE));
        valueScoreLabel.setPreferredSize(new Dimension(80, 25)); // Spazio riservato ai numeri
        valueScoreLabel.setHorizontalAlignment(SwingConstants.LEFT);

        centerPanel.add(titleScoreLabel);
        centerPanel.add(valueScoreLabel);
        add(centerPanel, BorderLayout.CENTER);
    }// fine initCenterPanel

    private void initLivesContainer() {
        livesContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, HGAP, VGAP));// DOC: A flow layout arranges
                                                // components in a directional flow,
                                                // much like lines of text in a
                                                // paragraph.
                                                // The flow direction is determined by
                                                // the container's
                                                // componentOrientation property
        livesContainer.setOpaque(true);
        livesContainer.setBackground(new Color(0, 100, 0));
        livesContainer.setBorder(
                BorderFactory.createEmptyBorder(LC_BORDER_TOP, LC_BORDER_LEFT, LC_BORDER_BOTTOM, LC_BORDER_RIGHT));
        livesContainer.setPreferredSize(new Dimension(LIVES_CONTAINER_WIDTH, HUD_HEIGHT));
        add(livesContainer, BorderLayout.EAST);

        generateVisualHearts();
    }// fine initLivesContainer

    private void generateVisualHearts() {
        for (int i = 0; i < MAX_LIVES; i++) {
            heartLabels[i] = loadImage();
            heartLabels[i].setPreferredSize(heartLabels[i].getPreferredSize());
            livesContainer.add(heartLabels[i]);
        }
        saveIcon = heartLabels[0].getIcon();// DOC: Returns the graphic image (glyph, icon) that the label displays.
    }// fine generateVisualHearts

    private JLabel loadImage() {
        BufferedImage image; // DOC: The BufferedImage subclass describes an Image with an accessible buffer
                             // of image data. extends Image
        JLabel imageContainer;

        try {// DOC: ImageIO containing static convenience methods for locating ImageReaders
             // and ImageWriters, and performing simple encoding and decoding.
            URL imgUrl = getClass().getResource("/colorclash/resources/heart.png");
            if (imgUrl == null) {
                imgUrl = getClass().getResource("/src/colorclash/resources/heart.png");
            }
            if (imgUrl == null) {
                throw new Exception("File heart.png not found in any directory");
            }

            image = ImageIO.read(imgUrl);// DOC: read(URL input) Returns a BufferedImage as the result of decoding a
                                         // supplied URL
                                         // with an ImageReader chosen automatically from among those currently
                                         // registered.

            imageContainer = new JLabel(new ImageIcon(image));// DOC: Creates an ImageIcon from an image object.
            return imageContainer;

        } catch (Exception e) {
            System.out.println("Error loading graphic hearts: " + e);
            System.out.println("As an emergency measure, loading the heart emoji");
            JLabel errorLabel = new JLabel("♥");
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 30));
            errorLabel.setForeground(Color.RED);
            return errorLabel;
        }
    }// fine loadImage

    // METODI PUBBLICI

    public void updateLivesView(int currentLives) {

        if (currentLives == lastLives) {
            return;
        }

        for (int i = 0; i < MAX_LIVES; i++) {

            if (i >= (MAX_LIVES - currentLives)) {
                heartLabels[i].setIcon(saveIcon);
                heartLabels[i].setForeground(Color.RED);
            } else {
                heartLabels[i].setIcon(null);
                heartLabels[i].setForeground(new Color(0, 0, 0, 0));
            }
        }
        livesContainer.repaint();
        lastLives = currentLives;
    }// fine updateLivesView

    public void updateScoreText(int currentScore) {
        if (currentScore == lastScore) {
            return;
        }
        titleScoreLabel.setText("SCORE: ");
        valueScoreLabel.setText(String.valueOf(currentScore));
        lastScore = currentScore;
    }// fine updateScoreText

    public void showCountdown(int secondsLeft) {
        titleScoreLabel.setText("GAME RESTARTS IN: " + secondsLeft);
        valueScoreLabel.setVisible(false);
    }// fine showCountdown

    public void showNewColorUnlocked() {
        titleScoreLabel.setText("NEW COLOR UNLOCKED!");
        valueScoreLabel.setVisible(false);
    }// fine showNewColorUnlocked

    public void restoreScoreLabel(int currentScore) {
        titleScoreLabel.setText("SCORE: ");
        valueScoreLabel.setText(String.valueOf(currentScore));
        valueScoreLabel.setVisible(true);
        lastScore = currentScore;
    }// fine restoreScoreLabel

    public void hideNewColorUnlocked() {
        titleScoreLabel.setText("");
        valueScoreLabel.setVisible(false);
    }// fine hideNewColorUnloccked

    public void setScoreVisible(boolean visible) {
        titleScoreLabel.setVisible(visible);
        valueScoreLabel.setVisible(visible);
    }

    // getters di HudPanel

    public JButton getPauseButton() {
        return pauseButton;
    }

    public int getHudPanelHeight() {
        return HUD_HEIGHT;
    }

}// fine classe HudPanel