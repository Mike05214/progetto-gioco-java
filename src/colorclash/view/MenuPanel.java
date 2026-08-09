package src.colorclash.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.Border;

import src.colorclash.model.GameModel;
import src.colorclash.utils.AudioManager;
import src.colorclash.utils.SaveManager;

import java.io.File;//DOC: An abstract representation of file and directory pathnames.


public class MenuPanel extends BaseMenuPanel {
    // variabili d'istanza
    private JLabel highScoreLabel;
    private JButton resumeButton;
    private JButton playButton;
    private JButton deleteSaveButton;
    private JButton soundButton;
    private GameModel model;

    // costanti
    private final int TEXT_LABEL_SIZE = 18;
    private final int ROW_0 = 0;
    private final int ROW_1 = 1;
    private final int ROW_2 = 2;
    private final int ROW_3 = 3;
    private final int ROW_4 = 4;
    private final int HI_LABEL_TOP = 3;
    private final int HI_LABEL_LEFT = 25;
    private final int HI_LABEL_BOTTOM = 3;
    private final int HI_LABEL_RIGHT = 25;
    private final int HI_LABEL_LINE_THICKNESS = 2;
    private final int SOUND_BUTTON_FONT_SIZE = 25;

    public MenuPanel(MainFrame mainFrame) {
        super(mainFrame);
        model = GameModel.getInstance();
        setBackground(new Color(10, 10, 20));
        initButtons();
        initHighScoreLabel();
        initTitleLabel("COLOR CLASH");
        addComponentToCenter(playButton, ROW_0, true);
        addComponentToCenter(resumeButton, ROW_1, true);
        addComponentToCenter(deleteSaveButton, ROW_2, true);
        addComponentToCenter(highScoreLabel, ROW_3, true);
        addComponentToCenter(soundButton, ROW_4, false);
        
    }// fine costruttore

    //METODI PUBBLICI 
    
    public void updateHighScoreDisplay() {
        highScoreLabel.setText("High Score:   " + model.getHighscore());
    }// fine updateHighScoreDisplay

    public void initButtons() {
        initPlayButton();
        initResumeButton();
        initDeleteButton();
        initSoundButton();
        refreshSavingButtons();

    }// fine initButtons

    private void initPlayButton(){
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
    }// fine initPlayButton

    private void initResumeButton(){
        resumeButton = new JButton("RESUME");
        resumeButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        resumeButton.setFont(new Font("Impact", Font.PLAIN, BUTTON_TEXT_SIZE));
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SaveManager.getInstance().loadGameState(model)) {
                    AudioManager.getInstance().stopBackgroundMusic();
                    frame.getGamePanel().resumeCountdown();
                    frame.changeFrame("GAME");
                } else {
                    System.out.println("Error: Unable to load the save file.");
                }
            }
        });
    }// fine initResumeButton

    private void initDeleteButton(){
        deleteSaveButton = new JButton("DELETE SAVE");
        deleteSaveButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        deleteSaveButton.setFont(new Font("Impact", Font.PLAIN, BUTTON_TEXT_SIZE));
        deleteSaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SaveManager.getInstance().deleteGameState();
                deleteSaveButton.setEnabled(false);
                resumeButton.setEnabled(false);
            }
        });
    }// fine initDeleteButton

    private void initSoundButton(){
        soundButton = new JButton("🔊");
        soundButton.setFont(new Font("Dialog", Font.PLAIN, SOUND_BUTTON_FONT_SIZE));
        soundButton.setPreferredSize(new Dimension(SOUND_BUTTON_WIDTH, BUTTON_HEIGHT));
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
    }// fine initSoundButton
    
    private void initHighScoreLabel(){
        highScoreLabel = new JLabel("HIGH SCORE:   " + model.getHighscore());
        highScoreLabel.setFont(new Font("Impact", Font.PLAIN, TEXT_LABEL_SIZE));
        highScoreLabel.setOpaque(true);
        highScoreLabel.setBackground(new Color(50, 50, 50));
        Border line = BorderFactory.createLineBorder(Color.WHITE, HI_LABEL_LINE_THICKNESS);
        Border padding = BorderFactory.createEmptyBorder(HI_LABEL_TOP, HI_LABEL_LEFT, HI_LABEL_BOTTOM, HI_LABEL_RIGHT);
        highScoreLabel.setBorder(BorderFactory.createCompoundBorder(line, padding));
        highScoreLabel.setBackground(Color.LIGHT_GRAY);
        highScoreLabel.setForeground(Color.BLACK);
    }

    public void refreshSavingButtons() {
        File saveFile = new File("saves/gamestate.txt");
        resumeButton.setEnabled(saveFile.exists());
        deleteSaveButton.setEnabled(saveFile.exists());
    }

    

}// fine classe MenuPanel