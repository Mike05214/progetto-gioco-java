package src.colorclash.view;
import src.colorclash.model.GameModel;
import src.colorclash.model.Obstacle;
import src.colorclash.model.Avatar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer; // Attenzione: importare quello di javax.swing!
import javax.swing.border.Border;


public class GamePanel extends JPanel {

    private GameModel model;
    private Timer gameLoop;
    private  boolean spaceAlreadyPressed = false;
    private MainFrame frame; //reso frame variabilie d'istanza per poterla passare ai metodi helper del costruttore
    private JLabel scoreLabel;
    private GameSpace gameSpace;
    

    // Calcoliamo i millisecondi per avere 60 FPS (1000 ms / 60 = ~16 ms)
    private final int DELAY = 8;

    // Palete dei colori per associare ID(indice) a un colore specifico 
    private Color[] colorPalette = {
        Color.RED,   // ID 0
        Color.GREEN, // ID 1
        Color.CYAN   // ID 2
    };

    public GamePanel(MainFrame frame) {  // al costruttore del pannello viene passata la nostra finestra principale 
        this.model = new GameModel();
        this.frame = frame;
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        initGameLoop();
        initHudPanel(frame);
        initGameSpace();
        //INIZIO PROTOTIPO MOVIMENTO PLAYER (magari da rivedere i metodi usati in base a quelli che ci sono nelle dispense del prof se questi non ci piacciono)
        initSetupListeners();

        
    }// FINE COSTRUTTORE

   
    //INIZIALIZZATORI

    public void initGameLoop(){
        // --- CREAZIONE DEL GAME LOOP (IL MOTORE DEL TEMPO) ---
        this.gameLoop = new Timer(DELAY,new ActionListener(){        //Metodo mostratoci dal prof su dispensa per animazione 
            @Override
            public void actionPerformed(ActionEvent e){
                // 1. Il tempo scorre: facciamo muovere la logica
                model.update(getGameSpaceWidth(), getGameSpaceHeight()); //i metodi nei parametri sono nativi di Java Swing a quanto dice gemini
                scoreLabel.setText("SCORE: "+ model.getScore());
                // 2. Ridisegniamo lo schermo con le nuove posizioni
                repaint();
            }
        });
        //this.gameLoop.start(); //senza sta riga gli update non avvengono, di conseguenza il gioco non parte
    }

    private void initGameSpace(){
        this.gameSpace = new GameSpace();
        this.add(gameSpace, BorderLayout.CENTER);
    }

    //la toolbar non è una classe specifica, è un altro JPanel creato su misura per contenere bottone back e punteggio, per evitare interferenze con l'area di gioco
    public void initHudPanel(MainFrame frame){
        JPanel hudPanel = new JPanel(new BorderLayout());
        hudPanel.setBackground(Color.DARK_GRAY);
        JButton pauseButton = new JButton("PAUSE (ALT + Z)");
        pauseButton.setMnemonic(KeyEvent.VK_Z);
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.changeFrame("PAUSE");
            }
        });
        hudPanel.add(pauseButton, BorderLayout.WEST);

        scoreLabel = new JLabel("SCORE: ");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER); //centra il testo nella sua area, SwingConstants è una interface
        hudPanel.add(scoreLabel, BorderLayout.CENTER);
        this.add(hudPanel, BorderLayout.NORTH);

    }

    public void initSetupListeners(){
         this.addKeyListener(new KeyAdapter() { //listener della tastiera nella view come da programma
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
        
         // Quando premi il tasto, ACCENDI l'interruttore (true)
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
        
        // Quando rilasci il tasto, SPEGNI l'interruttore (false)
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
        //FINE PROTOTIPO MOVIMENTO PLAYER
        });

        
    }
    //FINE INIZIALIZZATORI
    
    public void spaceKeyLogic(){  //per farsì che il metodo colorcooldown non venga chiamato più volte se continui a tenr prenuto space, si serve a quello spaceAlreadyPressed
        if (!spaceAlreadyPressed){
            model.getPlayer().colorCooldown();
            spaceAlreadyPressed = true;
        }
        else{
            spaceAlreadyPressed = false;
        }

    }

    @Override
    public void setVisible(boolean visible){   // quando viene invocato changeFrame "Game " viene invocato il metodo stVisible("true") del gamepanel sovrascritto
        super.setVisible(visible);
        if(this.gameLoop != null){
            if(visible){
                this.gameLoop.start();
            }
            else{
                this.gameLoop.stop(); //così ogni volta che il gamePanel ha il setvisible impostato a false il gioco si ferma
                model.getPlayer().resetMovementFlags(); //quando il pannello cambia le movementFlags vanno sempre resettate per evitare bug di movimento
            }
        }
    }
    //CLASSE INTERNA CHE GESTISCE SEPARATAMENTE L'AREA DI GIOCO DALLA BARRA MENU
    private class GameSpace extends JPanel{
        public GameSpace(){
            setBackground(Color.BLACK);
        }
             // --- IL METODO PER DISEGNARE ---
        @Override
        public void paintComponent(Graphics g){  // non si usa paint perchè noi vogliamo disegnare il pannello specifico
            //chiamare SEMPRE il super all'inizio per pulire lo schermo vecchio!
            super.paintComponent(g);
            // 1. Disegniamo l'Avatar
            Avatar player = model.getPlayer();
            int PlayerColorId = model.getPlayer().getColorId();
            Color currentColor = colorPalette[PlayerColorId];
            g.setColor(currentColor);
            g.fillRect((int)player.getX(), (int)player.getY(), player.getWidth(), player.getHeight());

            // 2. Disegniamo TUTTI gli ostacoli presenti nella lista
            for (Obstacle obs : model.getEnemies()){
                g.setColor(colorPalette[obs.getColorId()]);
                g.fillRect(obs.getX(),obs.getY(),obs.getWidth(),obs.getHeight());
            }
        }
    }

    public GameModel getModel(){
        return this.model;
    }

    public int getGameSpaceWidth(){
        return this.gameSpace.getWidth();
    }
    public int getGameSpaceHeight(){
        return this.gameSpace.getHeight();
    }





}//FINE CLASSE PANNELLO
