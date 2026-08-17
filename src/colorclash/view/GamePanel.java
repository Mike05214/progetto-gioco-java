package colorclash.view;

import colorclash.model.IGameModel;
import colorclash.utils.AudioManager;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel {

    // variabili d'istanza

    private Timer gameLoop;
    private MainFrame frame;
    private GameSpace gameSpace;
    private HudPanel hudPanel;
    private boolean isResuming = false;
    private boolean isScoreUpdateBlocked;
    private boolean colorSwitchLocked = false;
    private int lastPhase;
    private int currentPhase;
    private Timer alertTimer;
    private IGameModel model; // <-- USO L'INTERFACCIA

    // costanti
    private final int DELAY = 8;
    private final int RESUME_COOLDOWN_DELAY_ms = 800;
    private final int COUNT_LEFT = 3;
    private final int NEW_COLOR_LABEL_VISIBLE_DELAY = 500;

    public GamePanel(MainFrame mainframe, IGameModel injectedModel) {
        this.model = injectedModel; // <-- INIEZIONE
        frame = mainframe;
        lastPhase = model.getPhase();
        setLayout(new BorderLayout());
        initGameLoop();
        initHudPanel();
        initGameSpace();
        initListeners();

    }// fine costruttore

    // METODI PRIVATI
    
    private void initGameLoop() {
        gameLoop = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.update(getGameSpaceWidth(), getGameSpaceHeight());
                hudPanel.updateLivesView(model.getLives());
                if (!isScoreUpdateBlocked) {
                    hudPanel.updateScoreText(model.getScore());
                }

                currentPhase = model.getPhase();
                if (currentPhase > lastPhase) {
                    lastPhase = currentPhase;
                    newColorUnlockedCountdown();
                }

                if(model.isPlayerDead()){
                    resetGamePanel();
                }
                
                if (model.isGameOver()) {
                    hudPanel.getPauseButton().setEnabled(false);
                    hudPanel.getScoreLabel().setVisible(false);
                    gameSpace.getRestarButton().setVisible(true);
                    gameLoop.stop();
                }
                repaint();
            }
        });
    }// fine initGameLoop

    private void initGameSpace() {
        gameSpace = new GameSpace(model); // <-- PASSO IL MODEL AL GAMESPACE
        gameSpace.getRestarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.changeFrame("MENU");
                hudPanel.getScoreLabel().setVisible(true);
                frame.getMenuPanel().updateHighScoreDisplay();
                model.resetGame();
                resetGamePanel();
                gameSpace.getRestarButton().setVisible(false);
                hudPanel.getPauseButton().setEnabled(true);
            }
        });
        add(gameSpace, BorderLayout.CENTER);
    }// fine initGameSpace

    private void initHudPanel() {
        hudPanel = new HudPanel();
        hudPanel.getPauseButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.changeFrame("PAUSE");
                stopBlinking();
            }
        });
        add(hudPanel, BorderLayout.NORTH);
    }// fine initHudPanel

    private void initListeners() {
        addKeyListener(new KeyAdapter() { // DOC : An abstract adapter class for receiving keyboard events.
                                          // The methods in this class are empty.
                                          // This class exists as convenience for creating listener objects.
            public void keyPressed(KeyEvent e) {
                if (isResuming) {
                    return;
                }
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_W) {
                    model.getPlayer().setMovingUp(true);
                }

                if (key == KeyEvent.VK_S) {
                    model.getPlayer().setMovingDown(true);
                }

                if (key == KeyEvent.VK_A) {
                    model.getPlayer().setMovingLeft(true);
                }

                if (key == KeyEvent.VK_D) {
                    model.getPlayer().setMovingRight(true);
                }

                if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_RIGHT) {
                    ColorSwitchLogic(true);
                }
                if (key == KeyEvent.VK_LEFT) {
                    ColorSwitchLogic(false);
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (isResuming) {
                    return;
                }
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_W) {
                    model.getPlayer().setMovingUp(false);
                }

                if (key == KeyEvent.VK_S) {
                    model.getPlayer().setMovingDown(false);
                }

                if (key == KeyEvent.VK_A) {
                    model.getPlayer().setMovingLeft(false);
                }

                if (key == KeyEvent.VK_D) {
                    model.getPlayer().setMovingRight(false);
                }

                if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_LEFT) {
                    resetKeyLogic();
                }
            }
        });

    }// fine initSetupListeners

    private void ColorSwitchLogic(boolean forward) {
        if (!colorSwitchLocked) {
            model.getPlayer().colorCooldown(model.getAvailableColorsCount(), forward);
            colorSwitchLocked = true; 
        }
    }// fine spaceKeyLogic

    private void resetKeyLogic() {
        colorSwitchLocked = false; 
    }// fine resetKeyLogic

    // METODI PUBBLICI

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            if (!isResuming) {
                this.gameLoop.start();
            }
        } else {
            this.gameLoop.stop();
            model.getPlayer().resetMovementFlags();
        }
    }// fine setVisible

    public void resumeCountdown() {
        isResuming = true;
        hudPanel.getPauseButton().setEnabled(false);
        gameSpace.setForceDrawPlayer(true);
        hudPanel.showCountdown(COUNT_LEFT);
        Timer countdown = new Timer(RESUME_COOLDOWN_DELAY_ms, new ActionListener() {
            int count = COUNT_LEFT;
            @Override
            public void actionPerformed(ActionEvent e) {
                count--;
                if (count > 0) {
                    hudPanel.showCountdown(count);
                } else {
                    ((Timer) e.getSource()).stop();
                    
                    if (isScoreUpdateBlocked && alertTimer != null) {
                        alertTimer.start();
                    } else {
                        hudPanel.restoreScoreLabel(model.getScore());
                    }
                    AudioManager.getInstance().playBackgroundMusic("soundtrack.wav");
                    gameSpace.setForceDrawPlayer(false);
                    gameLoop.start();
                    hudPanel.getPauseButton().setEnabled(true);
                    isResuming = false;
                }
            }
        });
        AudioManager.getInstance().playSoundEffect("race_countdown.wav");
        countdown.start();

    }// fine resumeCountdown

    public void newColorUnlockedCountdown() {
        isScoreUpdateBlocked = true;
        alertTimer = new Timer(NEW_COLOR_LABEL_VISIBLE_DELAY, new ActionListener() {
            int tickCount = 0;
            boolean visible = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                tickCount++;
                visible = !visible;
                if (visible && !model.isGameOver()) {
                    hudPanel.showNewColorUnlocked();
                    AudioManager.getInstance().playSoundEffect("notification.wav");
                    gameSpace.createBorder(model.getPhase());
                } else {
                    hudPanel.hideNewColorUnlocked();
                    gameSpace.deleteBorder();
                }

                if (tickCount >= 8) {
                    ((Timer) e.getSource()).stop();
                    gameSpace.deleteBorder();
                    isScoreUpdateBlocked = false;
                    hudPanel.restoreScoreLabel(model.getScore());
                }

            }
        });
        alertTimer.setInitialDelay(0); 
        alertTimer.start();

    }// fine newColorUnlockedCountdown

    public void stopBlinking() {
        if (alertTimer != null && alertTimer.isRunning()) {
            alertTimer.stop();
        }
    }// fine stopBlinking

    public void resetGamePanel() {
        this.lastPhase = model.getPhase();
        this.isScoreUpdateBlocked = false;
        stopBlinking();
        hudPanel.restoreScoreLabel(model.getScore());
        gameSpace.deleteBorder();
    }// fine resetGamePanel

    // getters del GamePanel

    public int getGameSpaceWidth() {
        return gameSpace.getWidth();
    }

    public int getGameSpaceHeight() {
        return gameSpace.getHeight();
    }

    public Timer getNewColorTimer() {
        return alertTimer;
    }

    public HudPanel getHudPanel() {
        return hudPanel;
    }
}// fine classe GamePanel