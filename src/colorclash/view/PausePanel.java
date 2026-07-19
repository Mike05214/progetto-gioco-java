package src.colorclash.view;

import src.colorclash.model.GameModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
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

    public PausePanel(MainFrame frame, GameModel model) { // il model da resettare è quello del gamePanel che infatti
                                                          // gli viene passato come parametro
        super();
        this.model = model; // è quello del gamePanel
        setBackground(Color.BLACK);
        initTitleLabel("PAUSE", Color.ORANGE);

        this.resumeButton = initResumeButton(frame);
        this.backToMenuButton = initBackToMenuButton(frame);
        this.saveAndExitButton = initSaveAndExitButton(frame);

        addComponentToCenter(resumeButton, ROW_0, true);
        addComponentToCenter(backToMenuButton, ROW_1, true);
        addComponentToCenter(saveAndExitButton, ROW_2, true);
    }// fine costruttore

    private JButton initResumeButton(MainFrame frame) {
        JButton resume = new JButton("RESUME");
        resume.setFont(new Font("Arial", Font.BOLD, BUTTON_TEXT_SIZE));
        resume.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        resume.setMnemonic(KeyEvent.VK_X);
        resume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.changeFrame("GAME");
                frame.getGamePanel().resumeCountdown();
            }
        });
        return resume;
    }// fine initResumeButton

    private JButton initBackToMenuButton(MainFrame frame) {
        JButton backToMenu = new JButton("BACK TO MENU");
        backToMenu.setFont(new Font("Arial", Font.BOLD, BUTTON_TEXT_SIZE));
        backToMenu.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        backToMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.changeFrame("MENU");
                frame.getMenuPanel().updateHighScoreDisplay();
                model.resetGame();
                frame.getGamePanel().resetGamePanel();
            }
        });
        return backToMenu;
    }// fine initBackToMenuButton

    private JButton initSaveAndExitButton(MainFrame frame) {
        JButton saveAndExit = new JButton("SAVE AND EXIT");
        saveAndExit.setFont(new Font("Arial", Font.BOLD, BUTTON_TEXT_SIZE));
        saveAndExit.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        saveAndExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.changeFrame("MENU");
                model.getSaveManager().writeGameState(model.getScore(), model.getLives(), model.getPhase(), model.getCurrentSpeed(), model.getAvailableColorsCount(),
                        model.getPlayer(), model.getEnemies());
                model.resetGame();
                frame.getMenuPanel().refreshResumeButton();
                
            }
        });
        return saveAndExit;
    }// fine initSaveAndExitButton

}// fine classe PausePanel
