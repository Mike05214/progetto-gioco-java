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

    //variabili d'istanza
    private GameModel model;
    private Timer gameLoop;
    private  boolean spaceAlreadyPressed = false;
    private MainFrame frame;
    private GameSpace gameSpace;
    private HudPanel hudPanel;

    //costanti
    private final int DELAY = 8;
    private final int RESUME_COOLDOWN_DELAY = 1000;
    private final int SECONDS_LEFT = 3;
    
    public GamePanel(MainFrame frame) {  
        this.model = new GameModel();
        this.frame = frame;
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        initGameLoop();
        initHudPanel(frame);
        initGameSpace();
        initSetupListeners();

        
    }//fine costruttore

    //metodi privati
    private void initGameLoop(){
        this.gameLoop = new Timer(DELAY,new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                model.update(getGameSpaceWidth(), getGameSpaceHeight());
                hudPanel.updateLivesView(model.getLives());
                hudPanel.updateScoreText(model.getScore());

                if(model.isGameOver()){
                    hudPanel.getPauseButton().setEnabled(false);
                    gameSpace.getRestarButton().setVisible(true);
                    gameLoop.stop();
                }
                repaint();
            }
        });
    }//fine initGameLoop

    private void initGameSpace(){
        this.gameSpace = new GameSpace(frame,model,hudPanel);
        this.add(gameSpace, BorderLayout.CENTER);
    }//fine initGameSpace

    private void initHudPanel(MainFrame frame){
        this.hudPanel = new HudPanel(frame);
        this.add(hudPanel, BorderLayout.NORTH);
        
    }//fine initHudPanel

    private void initSetupListeners(){
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
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

                if(key == KeyEvent.VK_SPACE){
                    spaceKeyLogic();
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
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

                if(key == KeyEvent.VK_SPACE){
                    spaceKeyLogic();
                }
            }
        });

        
    }//fine initSetupListeners

    
    private void spaceKeyLogic(){
        if (!spaceAlreadyPressed){
            model.getPlayer().colorCooldown(model.getAvailableColorsCount());
            spaceAlreadyPressed = true;
        }
        else{
            spaceAlreadyPressed = false;
        }

    }//fine spaceKeyLogic

    //metodi pubblici
    @Override
    public void setVisible(boolean visible){ 
        super.setVisible(visible);
        if(this.gameLoop != null){
            if(visible){
                this.gameLoop.start();
            }
            else{
                this.gameLoop.stop();
                model.getPlayer().resetMovementFlags();
            }
        }
    }//fine setVisible

    public void resumeCountdown(){
        gameLoop.stop();
        hudPanel.getPauseButton().setEnabled(false);
        hudPanel.showCountdown(SECONDS_LEFT);
        Timer countdown = new Timer(RESUME_COOLDOWN_DELAY,new ActionListener(){ //timer NON dura 1000 ms ma SCATTA ogni 1000 ms
            int count = 3;
            @Override
            public void actionPerformed(ActionEvent e){
                count--;
                if(count > 0){
                    hudPanel.showCountdown(count);
                }
                else{
                    ((Timer) e.getSource()).stop();// e è l'actionEvent, getSource restituisce sempre Object di default e col cast a Timer diventa Timer su cui può essere chiamato il metodo stop()
                    hudPanel.restoreScoreLabel(model.getScore());
                    hudPanel.getPauseButton().setEnabled(true);
                    gameLoop.start();
                }
            }
        });
        countdown.setInitialDelay(RESUME_COOLDOWN_DELAY);
        countdown.start();
    }//fine resumeCountdown
    
    

    
    
    //getters del GamePanel
    public GameModel getModel(){
        return this.model;
    }

    public int getGameSpaceWidth(){
        return this.gameSpace.getWidth();
    }
    public int getGameSpaceHeight(){
        return this.gameSpace.getHeight();
    }
}//fine classe GamePanel
