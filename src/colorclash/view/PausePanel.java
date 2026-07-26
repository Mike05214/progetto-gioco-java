package src.colorclash.view;

import src.colorclash.model.GameModel;
import src.colorclash.model.Star;
import src.colorclash.utils.AudioManager;
import src.colorclash.view.MainFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JButton;

public class PausePanel extends BaseMenuPanel {

    // variabili d'istanza
    private GameModel model;
    private JButton backToMenuButton;
    private JButton saveAndExitButton;
    private JButton resumeButton;
    private MainFrame frame;

    // costanti
    private final int ROW_0 = 0;
    private final int ROW_1 = 1;
    private final int ROW_2 = 2;

    public PausePanel(MainFrame frame) { // il model da resettare è quello del gamePanel che infatti
                                                          // gli viene passato come parametro
        super();
        this.model = GameModel.getInstance(); // è quello del gamePanel
        this.frame = frame;
        setBackground(Color.BLACK);
        initTitleLabel("PAUSE", Color.RED);

        this.resumeButton = initResumeButton();
        this.backToMenuButton = initBackToMenuButton();
        this.saveAndExitButton = initSaveAndExitButton();

        addComponentToCenter(resumeButton, ROW_0, true);
        addComponentToCenter(backToMenuButton, ROW_1, true);
        addComponentToCenter(saveAndExitButton, ROW_2, true);
    }// fine costruttore

    private JButton initResumeButton() {
        JButton resume = new JButton("RESUME");
        resume.setFont(new Font("Impact", Font.PLAIN, BUTTON_TEXT_SIZE));
        resume.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        resume.setMnemonic(KeyEvent.VK_X);
        resume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AudioManager.getInstance().stopBackgroundMusic();
                frame.changeFrame("GAME");
                frame.getGamePanel().resumeCountdown();
            }
        });
        return resume;
    }// fine initResumeButton

    private JButton initBackToMenuButton() {
        JButton backToMenu = new JButton("BACK TO MENU");
        backToMenu.setFont(new Font("Impact", Font.PLAIN, BUTTON_TEXT_SIZE));
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

    private JButton initSaveAndExitButton() {
        JButton saveAndExit = new JButton("SAVE AND EXIT");
        saveAndExit.setFont(new Font("Impact", Font.PLAIN, BUTTON_TEXT_SIZE));
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // 1. Dipingi il nero dello spazio
        g.setColor(new Color(10, 10, 20));
        g.fillRect(0, 0, getWidth(), getHeight());

        // 2. Recupera le stelle e disegnale esattamente dov'erano 
        // (essendo in pausa, leggerà le stesse identiche coordinate dell'ultimo frame)
        List<Star> stelle = model.getStars();
        if (stelle != null) {
            for (Star s : stelle) {
                g.setColor(new Color(255, 255, 255, s.getAlpha()));
                g.fillRect((int)s.getX(), (int)s.getY(), s.getSize(), s.getSize());
            }
        }
        
        // 3. Qui disegnate i vostri bottoni "Riprendi", "Esci", ecc.
    }

}// fine classe PausePanel
