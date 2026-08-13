package colorclash.view;

import colorclash.model.GameModel;
import colorclash.utils.AudioManager;
import colorclash.utils.SaveManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Font;

import javax.swing.JButton;

public class PausePanel extends BaseMenuPanel {

    // variabili d'istanza
    private GameModel model;
    private JButton backToMenuButton;
    private JButton saveAndExitButton;
    private JButton resumeButton;

    // costanti
    private final int ROW_0 = 0;
    private final int ROW_1 = 1;
    private final int ROW_2 = 2;

    public PausePanel(MainFrame mainFrame) {
        super(mainFrame);
        model = GameModel.getInstance();
        setBackground(new Color(10, 10, 20));
        initTitleLabel("PAUSE");
        initButtons();
        addComponentToCenter(resumeButton, ROW_0, true);
        addComponentToCenter(backToMenuButton, ROW_1, true);
        addComponentToCenter(saveAndExitButton, ROW_2, false);
    }// fine costruttore

    //METODI PRIVATI

    private void initButtons(){
        initResumeButton();
        initBackToMenuButton();
        initSaveAndExitButton();
    }// fine initButtons

    private void initResumeButton() {
        resumeButton = new JButton("RESUME");
        resumeButton.setFont(new Font("Impact", Font.PLAIN, BUTTON_TEXT_SIZE));
        resumeButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        resumeButton.setMnemonic(KeyEvent.VK_X);
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AudioManager.getInstance().stopBackgroundMusic();
                frame.getGamePanel().resumeCountdown();
                frame.changeFrame("GAME");
            }
        });
        
    }// fine initResumeButton

    private void initBackToMenuButton() {
        backToMenuButton = new JButton("BACK TO MENU");
        backToMenuButton.setFont(new Font("Impact", Font.PLAIN, BUTTON_TEXT_SIZE));
        backToMenuButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        backToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.changeFrame("MENU");
                frame.getMenuPanel().updateHighScoreDisplay();
                model.resetGame();
                frame.getGamePanel().resetGamePanel();
            }
        });
    }// fine initBackToMenuButton

    private void initSaveAndExitButton() {
        saveAndExitButton = new JButton("SAVE AND EXIT");
        saveAndExitButton.setFont(new Font("Impact", Font.PLAIN, BUTTON_TEXT_SIZE));
        saveAndExitButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        saveAndExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SaveManager.getInstance().writeGameState(model.getScore(), model.getLives(), model.getPhase(),
                model.getCurrentSpeed(), model.getAvailableColorsCount(),
                model.getPlayer(), model.getEnemies());
                model.resetGame();
                frame.getMenuPanel().refreshSavingButtons();
                frame.changeFrame("MENU");

            }
        });

    }// fine initSaveAndExitButton

}// fine classe PausePanel
