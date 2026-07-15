package src.colorclash.view;

import src.colorclash.model.GameModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel {

    // variabili d'istanza
    private GameModel model;
    private Timer gameLoop;
    private MainFrame frame;
    private GameSpace gameSpace;
    private HudPanel hudPanel;
    private boolean isResuming;
    private boolean isScoreUpdateBlocked;
    private boolean spaceAlreadyPressed = false;
    private int lastPhase;
    private int currentPhase;
    private Timer alertTimer;

    // costanti
    private final int DELAY = 8;
    private final int RESUME_COOLDOWN_DELAY = 1000;
    private final int SECONDS_LEFT = 3;
    private final int NEW_COLOR_LABEL_VISIBLE_DELAY = 500;

    public GamePanel(MainFrame frame) {
        this.model = new GameModel();
        this.frame = frame;
        this.lastPhase = model.getPhase();
        setLayout(new BorderLayout());
        initGameLoop();
        initHudPanel(frame);
        initGameSpace();
        initSetupListeners();

    }// fine costruttore

    // metodi privati
    private void initGameLoop() {
        this.gameLoop = new Timer(DELAY, new ActionListener() {
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

                if (model.isGameOver()) {
                    hudPanel.getPauseButton().setEnabled(false);
                    gameSpace.getRestarButton().setVisible(true);
                    gameLoop.stop();
                }
                repaint();
            }
        });
    }// fine initGameLoop

    private void initGameSpace() {
        this.gameSpace = new GameSpace(frame, model);
        gameSpace.getRestarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.changeFrame("MENU");
                model.resetGame();
                resetGamePanel();
                gameSpace.getRestarButton().setVisible(false);
                hudPanel.getPauseButton().setEnabled(true);
            }
        });
        this.add(gameSpace, BorderLayout.CENTER);
    }// fine initGameSpace

    private void initHudPanel(MainFrame frame) {
        this.hudPanel = new HudPanel(frame);
        hudPanel.getPauseButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.changeFrame("PAUSE");
                stopBlinking();
            }
        });
        this.add(hudPanel, BorderLayout.NORTH);
    }// fine initHudPanel

    private void initSetupListeners() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isResuming) {
                    return;
                }
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
                    model.getPlayer().setMovingUp(true);
                }

                if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
                    model.getPlayer().setMovingDown(true);
                }

                if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
                    model.getPlayer().setMovingLeft(true);
                }

                if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
                    model.getPlayer().setMovingRight(true);
                }

                if (key == KeyEvent.VK_SPACE) {
                    spaceKeyLogic();

                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (isResuming) {
                    return;
                }
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
                    model.getPlayer().setMovingUp(false);
                }

                if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
                    model.getPlayer().setMovingDown(false);
                }

                if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
                    model.getPlayer().setMovingLeft(false);
                }

                if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
                    model.getPlayer().setMovingRight(false);
                }

                if (key == KeyEvent.VK_SPACE) {
                    resetKeyLogic();
                }

            }
        });

    }// fine initSetupListeners

    private void spaceKeyLogic() {
        if (!spaceAlreadyPressed) {
            model.getPlayer().colorCooldown(model.getAvailableColorsCount());
            spaceAlreadyPressed = true;
        }

    }// fine spaceKeyLogic

    private void resetKeyLogic() {
        spaceAlreadyPressed = false;
    }// fine resetKeyLogic

    // metodi pubblici
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (this.gameLoop != null) {
            if (visible) {
                this.gameLoop.start();
            } else {
                this.gameLoop.stop();
                model.getPlayer().resetMovementFlags();
            }
        }
    }// fine setVisible

    public void resumeCountdown() {
        isResuming = true;
        gameLoop.stop(); // per colpa del setVisible
        hudPanel.getPauseButton().setEnabled(false);
        hudPanel.showCountdown(SECONDS_LEFT);
        Timer countdown = new Timer(RESUME_COOLDOWN_DELAY, new ActionListener() { // timer NON dura 1000 ms ma SCATTA
                                                                                  // ogni 1000 ms
            int count = 3;

            @Override
            public void actionPerformed(ActionEvent e) {
                count--;
                if (count > 0) {
                    hudPanel.showCountdown(count);
                } else {
                    ((Timer) e.getSource()).stop();// e è l'actionEvent, getSource restituisce sempre Object di default
                                                   // e col cast a Timer diventa Timer su cui può essere chiamato il
                                                   // metodo stop()
                    if (isScoreUpdateBlocked && alertTimer != null) {
                        alertTimer.start();
                    } else {
                        hudPanel.restoreScoreLabel(model.getScore());
                    }
                    gameLoop.start();
                    hudPanel.getPauseButton().setEnabled(true);
                    isResuming = false;
                }
            }
        });
        countdown.setInitialDelay(RESUME_COOLDOWN_DELAY);
        countdown.start();
    }// fine resumeCountdown

    public void newColorUnlockedCountdown() {
        isScoreUpdateBlocked = true;
        this.alertTimer = new Timer(NEW_COLOR_LABEL_VISIBLE_DELAY, new ActionListener() {
            int tickCount = 0;
            boolean visible = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                tickCount++;
                visible = !visible;
                if (visible) {
                    hudPanel.showNewColorUnlocked();
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
        alertTimer.start();
    }

    public void resetGamePanel() {
        this.lastPhase = model.getPhase();
        this.isScoreUpdateBlocked = false;
        stopBlinking();
        hudPanel.restoreScoreLabel(model.getScore());
        gameSpace.deleteBorder();
    }

    public void stopBlinking() {
        if (alertTimer != null && alertTimer.isRunning()) {
            alertTimer.stop();
        }
    }

    // getters del GamePanel
    public GameModel getModel() {
        return this.model;
    }

    public int getGameSpaceWidth() {
        return this.gameSpace.getWidth();
    }

    public int getGameSpaceHeight() {
        return this.gameSpace.getHeight();
    }

    public Timer getNewColorTimer() {
        return this.alertTimer;
    }
}// fine classe GamePanel
