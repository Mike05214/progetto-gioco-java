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
import java.io.File;
import java.util.List;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.RenderingHints;

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

    public PausePanel(MainFrame mainFrame) { // il model da resettare è quello del gamePanel che infatti
                                         // gli viene passato come parametro
        super();
        model = GameModel.getInstance(); // è quello del gamePanel
        frame = mainFrame;
        setBackground(Color.BLACK);
        initTitleLabel("PAUSE");

        resumeButton = initResumeButton();
        backToMenuButton = initBackToMenuButton();
        saveAndExitButton = initSaveAndExitButton();

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
                frame.getGamePanel().resumeCountdown();
                frame.changeFrame("GAME");
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
                SaveManager.getInstance().writeGameState(model.getScore(), model.getLives(), model.getPhase(),
                        model.getCurrentSpeed(), model.getAvailableColorsCount(),
                        model.getPlayer(), model.getEnemies());
                model.resetGame();
                frame.getMenuPanel().refreshResumeButton();

            }
        });
        return saveAndExit;
    }// fine initSaveAndExitButton

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g2d);
        g.setColor(new Color(10, 10, 20));
        g.fillRect(0, 0, getWidth(), getHeight());

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

}// fine classe PausePanel
