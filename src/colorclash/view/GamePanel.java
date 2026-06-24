package src.colorclash.view;
import src.colorclash.model.GameModel;
import src.colorclash.model.Obstacle;
import src.colorclash.model.Avatar;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
    private MainFrame frame; //reso frame variabilie d'istanza per poterla passare ai metodi helper del costruttoreth
    private GameSpace gameSpace;
    private JButton pauseButton;
    private HudPanel hudPanel;
   
    

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
                hudPanel.updateLivesView(model.getLives());
                hudPanel.updateScoreText(model.getScore());
                if(model.isGameOver()){
                    hudPanel.getPauseButton().setEnabled(false);
                    gameSpace.restartButton.setVisible(true);

                    // FONDAMENTALE: Spegniamo il motore del tempo!
                    gameLoop.stop();
                }
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

    /* private void initHudPanel(){
        this.hudPanel = new HudPanel(frame);
        this.add(hudPanel, BorderLayout.NORTH);
    } */

     //la toolbar non è una classe specifica, è un altro JPanel creato su misura per contenere bottone back e punteggio, per evitare interferenze con l'area di gioco
    public void initHudPanel(MainFrame frame){
        this.hudPanel = new HudPanel(frame);
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

        private JButton restartButton;

        public GameSpace(){
            setBackground(Color.BLACK);
            
            // Usiamo il GridBagLayout solo per centrare il bottone automaticamente nello spazio
                this.setLayout(new GridBagLayout()); 

        // 2. Configuriamo il bottone una volta sola qui nel costruttore
            restartButton = new JButton("BACK TO MENU");
            restartButton.setFont(new Font("Arial", Font.BOLD, 20));
            restartButton.setPreferredSize(new Dimension(200, 40));
        
        // Di base il gioco è attivo, quindi il bottone deve essere INVISIBILE
            restartButton.setVisible(false); 

        // Gestiamo il click del bottone
            this.restartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                // Qui chiamerete il metodo del vostro controller/model per resettare la partita
                    frame.changeFrame("MENU");
                    model.resetGame();
                    restartButton.setVisible(false);
                    hudPanel.getPauseButton().setEnabled(true);
                }
            });

        // 3. Lo aggiungiamo FISICAMENTE al pannello
            this.add(restartButton); // di t
        }// FINE COSTRUTTORE
    
             // --- IL METODO PER DISEGNARE ---
        @Override
        public void paintComponent(Graphics g){  // non si usa paint perchè noi vogliamo disegnare il pannello specifico
            //chiamare SEMPRE il super all'inizio per pulire lo schermo vecchio!
            super.paintComponent(g);
            // 1. Facciamo il casting per sbloccare i superpoteri
            Graphics2D g2d = (Graphics2D) g;
            // 1. Decidiamo se l'Avatar deve essere disegnato in QUESTO specifico fotogramma
            boolean drawAvatar = true;

            if (model.isInvulnerable()) {
            // Se siamo invulnerabili, creiamo un ciclo di 20 frame.
            // Per i primi 10 frame scompare, per gli altri 10 riappare.
            // Più abbassate questo numero (es. % 10 < 5), più lampeggerà velocemente!
                if (model.getInvulnTimer() % 20 < 10) {
                drawAvatar = false;
                }
            }

            // 2. Disegniamo l'Avatar SOLO se drawAvatar è rimasto true
            if (drawAvatar) {
                Avatar player = model.getPlayer();
                g2d.setColor(colorPalette[player.getColorId()]);
                g2d.fill(player.getHitbox()); 
    
    // (Opzionale) Se l'avatar ha un contorno o degli occhi, disegnateli dentro questo "if",
    // altrimenti gli occhi rimarranno a fluttuare da soli mentre il corpo lampeggia!
            }
            // 2. Disegniamo TUTTI gli ostacoli presenti nella lista
            for (Obstacle obs : model.getEnemies()){
                g2d.setColor(colorPalette[obs.getColorId()]);
                // Non ci interessa che forma sia. g2d.fill() accetta qualsiasi Shape!
                g2d.fill(obs.getHitbox());
            }
            
            if (model.isGameOver()) {
        
        // A. Creiamo un "Velo" nero semi-trasparente
        // I parametri di Color sono (Rosso, Verde, Blu, Trasparenza/Alpha)
        // L'Alpha va da 0 (invisibile) a 255 (tinta unita). 150 crea un bell'effetto vetro affumicato.
                g2d.setColor(new Color(0, 0, 0, 150)); 
                g2d.fillRect(0, 0, getWidth(), getHeight()); // Copre tutto il pannello
        
        // B. Scriviamo il testo in grande al centro
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Monospaced", Font.BOLD, 100)); // Scegli il font che preferisci
        
        // C. Disegniamo la stringa (le coordinate X e Y andranno centrate in base al tuo schermo)
                String gameOverText = "GAME OVER";
                g2d.drawString(gameOverText, getWidth()/2 - 265, getHeight()/2 - 80);
        
        // Opzionale: Mostriamo il punteggio finale sotto
                g2d.setFont(new Font("Monospaced", Font.PLAIN, 25));
                g2d.drawString("SCORE: " + model.getScore(), getWidth()/2 - 100, getHeight()/2 - 35);
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
