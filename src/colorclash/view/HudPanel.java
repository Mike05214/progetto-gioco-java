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
    private final int MAX_COLORS = 4;
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
    private JPanel legendContainer;
    private JPanel scorePanel;
    private JLabel[] colorLabels = new JLabel[MAX_COLORS];
    private int lastLives = -1;
    private int lastScore = -1;
    private JLabel[] heartLabels = new JLabel[MAX_LIVES];
    private Icon saveIcon;

    public HudPanel() {
        setLayout(new BorderLayout());

        setBackground(new Color(170, 40, 60));
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
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        centerPanel.setOpaque(false);

        initScorePanel();
        initLegendContainer();

        centerPanel.add(scorePanel);
        centerPanel.add(legendContainer);

        add(centerPanel, BorderLayout.CENTER);
    }// fine initCenterPanel

    private void initScorePanel() {
        scorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 3));
        scorePanel.setOpaque(true);
        scorePanel.setBackground(new Color(0, 100, 0));
        scorePanel.setPreferredSize(new Dimension(220, HUD_HEIGHT - 1));
        scorePanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 1));

        titleScoreLabel = new JLabel("SCORE: ");
        titleScoreLabel.setForeground(Color.WHITE);
        titleScoreLabel.setFont(new Font("Impact", Font.PLAIN, SCORE_LABEL_FONT_SIZE));

        valueScoreLabel = new JLabel("0");
        valueScoreLabel.setForeground(Color.WHITE);
        valueScoreLabel.setFont(new Font("Impact", Font.PLAIN, SCORE_LABEL_FONT_SIZE));
        valueScoreLabel.setPreferredSize(new Dimension(60, 25));
        valueScoreLabel.setHorizontalAlignment(SwingConstants.LEFT);

        scorePanel.add(titleScoreLabel);
        scorePanel.add(valueScoreLabel);
    }// fine initScorePanel

    private void initLegendContainer() {
        legendContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        legendContainer.setOpaque(true);
        legendContainer.setBackground(Color.DARK_GRAY);
        legendContainer.setPreferredSize(new Dimension(120, HUD_HEIGHT - 1));
        legendContainer.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 1));

        for (int i = 0; i < MAX_COLORS; i++) {
            colorLabels[i] = new JLabel();
            colorLabels[i].setOpaque(true);
            colorLabels[i].setPreferredSize(new Dimension(20, 20));
            colorLabels[i].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
            colorLabels[i].setVisible(false);
            legendContainer.add(colorLabels[i]);
        }
    }

    private void initLivesContainer() {

        JPanel easternPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        easternPanel.setOpaque(false);

        livesContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, HGAP, 0));
        livesContainer.setOpaque(true);
        livesContainer.setBackground(new Color(0, 100, 0));
        livesContainer.setPreferredSize(new Dimension(LIVES_CONTAINER_WIDTH, HUD_HEIGHT - 1));
        livesContainer.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        generateVisualHearts();

        easternPanel.add(livesContainer);

        add(easternPanel, BorderLayout.EAST);
    }// fine initLivesContainer

    private void generateVisualHearts() {
        for (int i = 0; i < MAX_LIVES; i++) {
            heartLabels[i] = loadImage();
            heartLabels[i].setPreferredSize(heartLabels[i].getPreferredSize());
            livesContainer.add(heartLabels[i]);
        }
        saveIcon = heartLabels[0].getIcon();
    }// fine generateVisualHearts

    private JLabel loadImage() {
        BufferedImage image;
        JLabel imageContainer;

        try {
            URL imgUrl = getClass().getResource("/colorclash/resources/heart.png");
            if (imgUrl == null) {
                imgUrl = getClass().getResource("/src/colorclash/resources/heart.png");
            }
            if (imgUrl == null) {
                throw new Exception("File heart.png not found in any directory");
            }

            image = ImageIO.read(imgUrl);
            imageContainer = new JLabel(new ImageIcon(image));
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

    public void updateLegendView(int availableColors, int currentColorId, Color[] palette) {
        for (int i = 0; i < MAX_COLORS; i++) {
            if (i < availableColors) {
                colorLabels[i].setVisible(true);
                colorLabels[i].setBackground(palette[i]);

                if (i == currentColorId) {
                    colorLabels[i].setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                } else {
                    colorLabels[i].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
                }
            } else {
                colorLabels[i].setVisible(false);
            }
        }
    }// fine updateLegendView

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
        legendContainer.setVisible(false);
    }// fine showCountdown

    public void showNewColorUnlocked() {
        titleScoreLabel.setText("NEW COLOR UNLOCKED!");
        valueScoreLabel.setVisible(false);
        legendContainer.setVisible(false);
    }// fine showNewColorUnlocked

    public void restoreScoreLabel(int currentScore) {
        titleScoreLabel.setText("SCORE: ");
        valueScoreLabel.setText(String.valueOf(currentScore));
        valueScoreLabel.setVisible(true);
        legendContainer.setVisible(true);
        lastScore = currentScore;
    }// fine restoreScoreLabel

    public void hideNewColorUnlocked() {
        titleScoreLabel.setText("");
        valueScoreLabel.setVisible(false);
    }// fine hideNewColorUnloccked

    public void setScoreVisible(boolean visible) {
        titleScoreLabel.setVisible(visible);
        valueScoreLabel.setVisible(visible);
        legendContainer.setVisible(visible);
    }// fine setScoreVisible

    // getters di HudPanel
    public JButton getPauseButton() {
        return pauseButton;
    }

    public int getHudPanelHeight() {
        return HUD_HEIGHT;
    }

}// fine classe HudPanel