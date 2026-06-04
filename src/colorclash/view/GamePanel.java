package src.colorclash.view;
import src.colorclash.model.GameModel;
import src.colorclash.model.Obstacle;
import src.colorclash.model.Avatar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer; // Attenzione: importare quello di javax.swing!


public class GamePanel extends JPanel {

    private GameModel model;
    private Timer gameLoop;

    // Calcoliamo i millisecondi per avere 60 FPS (1000 ms / 60 = ~16 ms)
    private final int DELAY = 16;

    // Palete dei colori per associare ID(indice) a un colore specifico 
    private final Color[] colorPalette = {
        Color.RED,   // ID 0
        Color.GREEN, // ID 1
        Color.BLUE   // ID 2
    };

    public GamePanel(MainFrame frame) {  // al costruttore del pannello viene passata la nostra finestra principale 
        this.model = new GameModel();
        this.setBackground(Color.BLACK);
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.changeFrame("MENU");
            }
        });
        this.add(backButton);
        // --- CREAZIONE DEL GAME LOOP (IL MOTORE DEL TEMPO) ---
        this.gameLoop = new Timer(DELAY,new ActionListener(){        //Metodo mostratoci dal prof su dispensa per animazione 
            @Override
            public void actionPerformed(ActionEvent e){
                // 1. Il tempo scorre: facciamo muovere la logica
                model.update(getWidth(), getHeight()); //i metodi nei parametri sono nativi di Java Swing a quanto dice gemini

                // 2. Ridisegniamo lo schermo con le nuove posizioni
                repaint();
            }
        });
        this.gameLoop.start(); //senza sta riga gli update non avvengono, di conseguenza il gioco non parte
        //INIZIO PROTOTIPO MOVIMENTO PLAYER (magari da rivedere i metodi usati in base a quelli che ci sono nelle dispense del prof se questi non ci piacciono)
        this.addKeyListener(new java.awt.event.KeyAdapter() { //listener della tastiera nella view come da programma
        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
            int key = e.getKeyCode();
        
         // Quando premi il tasto, ACCENDI l'interruttore (true)
            if (key == java.awt.event.KeyEvent.VK_W || key == java.awt.event.KeyEvent.VK_UP) {
                model.getPlayer().setMovingUp(true);
            }
            if (key == java.awt.event.KeyEvent.VK_S || key == java.awt.event.KeyEvent.VK_DOWN) {
                model.getPlayer().setMovingDown(true);
            }
            if (key == java.awt.event.KeyEvent.VK_A || key == java.awt.event.KeyEvent.VK_LEFT) {
                model.getPlayer().setMovingLeft(true);
            }
            if (key == java.awt.event.KeyEvent.VK_D || key == java.awt.event.KeyEvent.VK_RIGHT) {
                model.getPlayer().setMovingRight(true);
            }
        }

        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            int key = e.getKeyCode();
        
        // Quando rilasci il tasto, SPEGNI l'interruttore (false)
            if (key == java.awt.event.KeyEvent.VK_W || key == java.awt.event.KeyEvent.VK_UP) {
                model.getPlayer().setMovingUp(false);
            }
            if (key == java.awt.event.KeyEvent.VK_S || key == java.awt.event.KeyEvent.VK_DOWN) {
                model.getPlayer().setMovingDown(false);
            }
            if (key == java.awt.event.KeyEvent.VK_A || key == java.awt.event.KeyEvent.VK_LEFT) {
                model.getPlayer().setMovingLeft(false);
            }
            if (key == java.awt.event.KeyEvent.VK_D || key == java.awt.event.KeyEvent.VK_RIGHT) {
                model.getPlayer().setMovingRight(false);
            }
        }
        //FINE PROTOTIPO MOVIMENTO PLAYER
});

        
    }// FINE COSTRUTTORE

    // --- IL METODO PER DISEGNARE ---
    @Override
    public void paintComponent(Graphics g){  // non si usa paint perchè noi vogliamo disegnare il pannello specifico
        //chiamare SEMPRE il super all'inizio per pulire lo schermo vecchio!
        super.paintComponent(g);
        // 1. Disegniamo l'Avatar
        Avatar player = this.model.getPlayer();
        g.setColor(this.colorPalette[player.getColorId()]);
        g.fillRect(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        // 2. Disegniamo TUTTI gli ostacoli presenti nella lista
        for (Obstacle obs : this.model.getEnemies()){
            g.setColor(this.colorPalette[obs.getColorId()]);
            g.fillRect(obs.getX(),obs.getY(),obs.getWidth(),obs.getHeight());
        }
    }
    





}
