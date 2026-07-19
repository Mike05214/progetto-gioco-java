package src.colorclash.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;

import src.colorclash.model.GameModel;
import src.colorclash.utils.SaveManager;

import java.io.File;

public class MenuPanel extends BaseMenuPanel {
    // variabili d'istanza
    private JLabel highScoreLabel;
    private JButton resumeButton;
    private JButton playButton;
    private JButton deleteSaveButton;
    private GameModel model; 
   

    // costanti
    private final int HI_LABEL_SIZE = 18;
    private final int ROW_0 = 0;
    private final int ROW_1 = 1;
    private final int ROW_2 = 2;
    private final int ROW_3 = 3;

    public MenuPanel(MainFrame frame, GameModel model) {
        super();
        this.model = model;
        setBackground(Color.DARK_GRAY);
        initButtons(frame);
        initTitleLabel("COLOR CLASH", Color.YELLOW);
        addComponentToCenter(playButton, ROW_0, true);
        addComponentToCenter(resumeButton, ROW_1, true);
        addComponentToCenter(deleteSaveButton, ROW_2, true);
        addComponentToCenter(highScoreLabel, ROW_3, true);
    }// fine costruttore

    public void updateHighScoreDisplay() {
        highScoreLabel.setText("High Score: " + model.getHighscore());
    }// fine updateHighScoreDisplay

    public void initButtons(MainFrame frame) {
        playButton = new JButton("PLAY");
        playButton.setFont(new Font("Arial", Font.BOLD, BUTTON_TEXT_SIZE));
        playButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.changeFrame("GAME");
            }
        });

        highScoreLabel = new JLabel("High Score:" + model.getHighscore());
        highScoreLabel.setFont(new Font("Arial", Font.PLAIN, HI_LABEL_SIZE));
        highScoreLabel.setForeground(Color.WHITE);

        resumeButton = new JButton("RESUME");
        resumeButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        resumeButton.setFont(new Font("Arial", Font.BOLD, BUTTON_TEXT_SIZE));
       
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.getSaveManager().loadGameState(model);
                frame.changeFrame("GAME");
                frame.getGamePanel().resumeCountdown();
            }
        });

        deleteSaveButton = new JButton("DELETE SAVE");
        deleteSaveButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        deleteSaveButton.setFont(new Font("Arial", Font.BOLD, BUTTON_TEXT_SIZE));
        deleteSaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                File saveFile = new File("saves/gamestate.txt");
                saveFile.delete();
                deleteSaveButton.setEnabled(false);
                resumeButton.setEnabled(false);
            }
        }); 
        refreshResumeButton();
    }// fine initButtons

    public void refreshResumeButton(){
        File saveFile = new File("saves/gamestate.txt");
        resumeButton.setEnabled(saveFile.exists());
        deleteSaveButton.setEnabled(saveFile.exists());
    }

}// fine classe MenuPanel