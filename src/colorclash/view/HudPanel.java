package src.colorclash.view;

import src.colorclash.utils.AudioManager;
import src.colorclash.utils.Config;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;

import java.io.File;

public class HudPanel extends JPanel {

    // variabili d'istanza
    private JButton pauseButton;
    private JLabel scoreLabel;
    private JPanel livesContainer;
    private int lastLives = -1;
    private int lastScore = -1;
    private final int MAX_LIVES = 3; 
    private JLabel[] heartLabels = new JLabel[MAX_LIVES];
    private Icon iconaDiSalvataggio;

    // costanti
    private final int HUD_WIDTH = 0;
    private final int HUD_HEIGHT = 35;
    private final int PAUSE_BUTTON_WIDTH = 150;
    private final int PAUSE_BUTTON_FONT_SIZE = 14;
    private final int SCORE_LABEL_FONT_SIZE = 20;
    private final int LIVES_CONTAINER_WIDTH = 150;
    private final int LC_BORDER_TOP = 1;
    private final int LC_BORDER_LEFT = 0;
    private final int LC_BORDER_BOTTOM = 0;
    private final int LC_BORDER_RIGHT = 5;
    private final int HGAP = 0;
    private final int VGAP = 0;
    private final int H_LABELS_BORDER_TOP = 0;
    private final int H_LABELS_BORDER_LEFT = 8;
    private final int H_LABELS_BORDER_BOTTOM = 0;
    private final int H_LABELS_BORDER_RIGHT = 0;

    public HudPanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.DARK_GRAY);
        this.setPreferredSize(new Dimension(HUD_WIDTH, HUD_HEIGHT));

        initWesternPanel();
        initCenterPanel();
        initLivesContainer();
    }// fine costruttore

    private void initWesternPanel() {
        JPanel westernPanel = new JPanel(new GridBagLayout());
        pauseButton = new JButton("PAUSE (ALT+X)");

        pauseButton.setMnemonic(KeyEvent.VK_X);
        pauseButton.setFont(new Font("Impact", Font.PLAIN, PAUSE_BUTTON_FONT_SIZE));
        pauseButton.setPreferredSize(new Dimension(PAUSE_BUTTON_WIDTH, HUD_HEIGHT));
        westernPanel.add(pauseButton);
        this.add(westernPanel, BorderLayout.WEST);
    }// fine initWesternPanel

    private void initCenterPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        scoreLabel = new JLabel("SCORE: 0");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Impact", Font.PLAIN, SCORE_LABEL_FONT_SIZE));
        centerPanel.add(scoreLabel);
        this.add(centerPanel, BorderLayout.CENTER);
    }// fine initCenterPanel

    private void initLivesContainer() {
        livesContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, HGAP, VGAP));
        livesContainer.setOpaque(false);
        livesContainer.setBorder(
                BorderFactory.createEmptyBorder(LC_BORDER_TOP, LC_BORDER_LEFT, LC_BORDER_BOTTOM, LC_BORDER_RIGHT));
        livesContainer.setPreferredSize(new Dimension(LIVES_CONTAINER_WIDTH, HUD_HEIGHT));
        this.add(livesContainer, BorderLayout.EAST);

        generateVisualHearts();
    }// fine initLivesContainer

    private void generateVisualHearts() {
        for (int i = 0; i < MAX_LIVES; i++) {
            heartLabels[i] = loadImage("src/colorclash/resources/cuore.png");
            heartLabels[i].setBorder(BorderFactory.createEmptyBorder(H_LABELS_BORDER_TOP, H_LABELS_BORDER_LEFT,
                    H_LABELS_BORDER_BOTTOM, H_LABELS_BORDER_RIGHT));
            heartLabels[i].setPreferredSize(heartLabels[i].getPreferredSize());
            livesContainer.add(heartLabels[i]);
        }
        this.iconaDiSalvataggio = heartLabels[0].getIcon();
    }// fine generateVisualHearts

    public void updateLivesView(int currentLives) {

        if (currentLives == lastLives) {
            return;
        }

        for (int i = 0; i < MAX_LIVES; i++) {
            heartLabels[i].setVisible(true);

            if (i >= (MAX_LIVES - currentLives)) {
                heartLabels[i].setIcon(this.iconaDiSalvataggio);
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

        if (scoreLabel != null) {
            scoreLabel.setText("SCORE: " + currentScore);
            this.lastScore = currentScore;
        }
    }// fine updateScoreText

    public static JLabel loadImage(String filename) {
        BufferedImage image;
        JLabel imageContainer;
        try {
            image = ImageIO.read(new File(filename));
            imageContainer = new JLabel(new ImageIcon(image));
            return imageContainer;
        } catch (Exception e) {
            System.out.println("Errore nel caricamento di " + filename + ": " + e);
            System.out.println("Come misura di emergenza carico il cuore emoji");
            JLabel errorLabel = new JLabel("♥");
            errorLabel.setForeground(Color.RED);
            return errorLabel;
        }
    }// fine loadImage

    public void showCountdown(int secondsLeft) {
        this.scoreLabel.setText("GAME RESTARTS IN: " + secondsLeft);
    }// fine showCountdown

    public void showNewColorUnlocked() {
        this.scoreLabel.setText("NEW COLOR UNLOCKED!");
    }

    public void restoreScoreLabel(int currentScore) {
        scoreLabel.setText("SCORE: " + currentScore);
        
    }// fine restoreScoreLabel

    public void hideNewColorUnlocked() {
        this.scoreLabel.setText("");
    }

    // getters di HudPanel
    public JButton getPauseButton() {
        return this.pauseButton;
    }
}// fine classe HudPanel