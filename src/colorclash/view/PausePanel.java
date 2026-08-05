package src.colorclash.view;

import src.colorclash.model.GameModel;
import src.colorclash.model.Star;
import src.colorclash.utils.AudioManager;
import src.colorclash.utils.SaveManager;
import src.colorclash.view.MainFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.RenderingHints;

import java.io.File;

import java.util.List;


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

    public PausePanel(MainFrame mainFrame) { // il model da resettare è quello del gamePanel che infatti
                                         // gli viene passato come parametro
        super(mainFrame);
        model = GameModel.getInstance(); // è quello del gamePanel
        setBackground(new Color(10, 10, 20));
        initTitleLabel("PAUSE");
        initButtons();
        addComponentToCenter(resumeButton, ROW_0, true);
        addComponentToCenter(backToMenuButton, ROW_1, true);
        addComponentToCenter(saveAndExitButton, ROW_2, true);
    }// fine costruttore

    //METODI PRIVATI

    private void initButtons(){
        initResumeButton();
        initBackToMenuButton();
        initSaveAndExitButton();
    }

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
                frame.changeFrame("MENU");
                SaveManager.getInstance().writeGameState(model.getScore(), model.getLives(), model.getPhase(),
                model.getCurrentSpeed(), model.getAvailableColorsCount(),
                model.getPlayer(), model.getEnemies());
                model.resetGame();
                frame.getMenuPanel().refreshSavingButtons();

            }
        });

    }// fine initSaveAndExitButton

    
}// fine classe PausePanel
