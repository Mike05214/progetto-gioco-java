package src.colorclash.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

import src.colorclash.model.GameModel;
import src.colorclash.model.Star;
import src.colorclash.utils.AudioManager;
import src.colorclash.utils.SaveManager;
import src.colorclash.utils.AudioManager;

import java.io.File;
import java.util.List;

public class MenuPanel extends BaseMenuPanel {
    // variabili d'istanza
    private JLabel highScoreLabel;
    private JButton resumeButton;
    private JButton playButton;
    private JButton deleteSaveButton;
    private JButton soundButton;
    private GameModel model;
    private MainFrame frame;

    // costanti
    private final int TEXT_LABEL_SIZE = 18;
    private final int ROW_0 = 0;
    private final int ROW_1 = 1;
    private final int ROW_2 = 2;
    private final int ROW_3 = 3;
    private final int ROW_4 = 4;

    public MenuPanel(MainFrame frame) {
        super();
        this.model = GameModel.getInstance();
        this.frame = frame;
        setBackground(new Color(10, 10, 10));
        initButtons();
        initTitleLabel("COLOR CLASH", Color.YELLOW);
        addComponentToCenter(playButton, ROW_0, true);
        addComponentToCenter(resumeButton, ROW_1, true);
        addComponentToCenter(deleteSaveButton, ROW_2, true);
        addComponentToCenter(soundButton, ROW_3, true);
        addComponentToCenter(highScoreLabel, ROW_4, true);
    }// fine costruttore

    public void updateHighScoreDisplay() {
        highScoreLabel.setText("High Score:   " + model.getHighscore());
    }// fine updateHighScoreDisplay

    public void initButtons() {
        playButton = new JButton("PLAY");
        playButton.setFont(new Font("Impact", Font.PLAIN, BUTTON_TEXT_SIZE));
        playButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AudioManager.getInstance().stopBackgroundMusic();
                AudioManager.getInstance().playBackgroundMusic("soundtrack.wav");
                frame.changeFrame("GAME");
                model.resetGame();
                frame.getGamePanel().resetGamePanel();
            }
        });

        highScoreLabel = new JLabel("HIGH SCORE:   " + model.getHighscore());
        highScoreLabel.setFont(new Font("Impact", Font.PLAIN, TEXT_LABEL_SIZE));
        highScoreLabel.setOpaque(true);
        highScoreLabel.setBackground(new Color(50, 50, 50)); // Stesso grigio scuro dei bottoni
        highScoreLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1), // Linea esterna visibile (colore, spessore)
                BorderFactory.createEmptyBorder(10, 20, 10, 20) // Spazio vuoto interno (alto, sinistra, basso, destra)
        ));
        highScoreLabel.setBackground(Color.LIGHT_GRAY);
        highScoreLabel.setForeground(Color.BLACK);

        resumeButton = new JButton("RESUME");
        resumeButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        resumeButton.setFont(new Font("Impact", Font.PLAIN, BUTTON_TEXT_SIZE));
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getSaveManager().loadGameState(model)) {
                    AudioManager.getInstance().stopBackgroundMusic();
                    frame.getGamePanel().resumeCountdown();
                    frame.changeFrame("GAME");
                } else {
                    // Opzionale: mostra un messaggio di errore all'utente
                    System.out.println("Error: Unable to load the save file.");
                }
            }
        });

        deleteSaveButton = new JButton("DELETE SAVE");
        deleteSaveButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        deleteSaveButton.setFont(new Font("Impact", Font.PLAIN, BUTTON_TEXT_SIZE));
        deleteSaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File saveFile = new File("saves/gamestate.txt");
                saveFile.delete();
                deleteSaveButton.setEnabled(false);
                resumeButton.setEnabled(false);
            }
        });
        refreshResumeButton();

        soundButton = new JButton("🔊");
        soundButton.setFont(new Font("Dialog", Font.PLAIN, 25));
        soundButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        soundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AudioManager.getInstance().toggleSound();
                if (AudioManager.getInstance().isSoundEnabled()) {
                    soundButton.setText("🔊");
                    AudioManager.getInstance().playBackgroundMusic("menu.wav");
                } else {
                    soundButton.setText("🔇");
                }
            }
        });

    }// fine initButtons

    public void refreshResumeButton() {
        File saveFile = new File("saves/gamestate.txt");
        resumeButton.setEnabled(saveFile.exists());
        deleteSaveButton.setEnabled(saveFile.exists());
    }

    @Override
    protected void paintComponent(Graphics g) {
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
    }

}// fine classe MenuPanel