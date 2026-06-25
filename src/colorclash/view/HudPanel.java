package src.colorclash.view;

import javax.swing.*;
import java.awt.*; //DA MODIFICARE METTENDOCI SOLO QUELLI UTILIZZATI
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import src.colorclash.model.GameModel;
import java.awt.event.KeyEvent;
import java.io.File;

public class HudPanel extends JPanel {
    private GameModel model;
    private MainFrame frame;
    private JLabel livesLabel; 
    
    private JButton pauseButton;
    private JLabel scoreLabel;
    private JPanel livesContainer; 
    private ImageIcon heartIcon;
    private int lastLives = -1;
    private int lastScore = -1;
    private final int MAX_LIVES = 3;
    private JLabel[] heartLabels = new JLabel[MAX_LIVES];
    private Icon iconaDiSalvataggio;

    // Il costruttore riceve il modello, il frame e il pulsante pausa originale
    public HudPanel( MainFrame frame) {
        System.out.println("La radice del Classpath è qui: " + getClass().getResource("/"));
        this.frame = frame;

        // Il vostro amato BorderLayout
        this.setLayout(new BorderLayout());
        this.setBackground(Color.DARK_GRAY);

        // --- 2. CENTRO (CENTER): Score + Bottone Riavvio a scomparsa ---
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        centerPanel.setOpaque(false);

        // Pulsante "PAUSE" (si affianca allo score solo quando perdi)
        pauseButton = new JButton("PAUSE (ALT+X)");
        
        pauseButton.setMnemonic(KeyEvent.VK_X);
        pauseButton.setFont(new Font("Arial", Font.BOLD, 14));
        pauseButton.addActionListener(e -> {
            frame.changeFrame("PAUSE");
        });
        centerPanel.add(pauseButton);
        JPanel scoreContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        scoreContainer.setOpaque(false);

        scoreLabel = new JLabel("SCORE: 0");      
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreContainer.add(scoreLabel);

        this.add(centerPanel, BorderLayout.WEST);
        this.add(scoreContainer, BorderLayout.CENTER);

       

        // Spinge i cuori contro il bordo destro
        livesContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); //allineamento laterale lasciando hgap
        livesContainer.setOpaque(false);
        this.add(livesContainer, BorderLayout.EAST);
        
        // Creiamo i 3 cuori e li mettiamo nell'Array in modo da non perderli di vista
        for (int i = 0; i < MAX_LIVES; i++) {
            heartLabels[i] = loadImage("src/colorclash/resources/cuore.png");
            heartLabels[i].setPreferredSize(new Dimension(50, 50));
    // Li aggiungiamo al pannello una volta per tutte
            livesContainer.add(heartLabels[i]); 
        }
        // 3. Ci "rubiamo" l'icona dal primo cuore e la mettiamo in cassaforte per dopo
        this.iconaDiSalvataggio = heartLabels[0].getIcon();
        
    }//FINE COSTRUTTORE

    public void updateLivesView(int currentLives) {
    // Scorriamo le 3 etichette che già esistono in memoria
        for (int i = 0; i < MAX_LIVES; i++) {
            heartLabels[i].setVisible(true);
            if (i < currentLives) {
                // VITA ATTIVA: Rimettiamo l'icona dalla nostra cassaforte
                    heartLabels[i].setIcon(this.iconaDiSalvataggio); 
                heartLabels[i].setForeground(Color.RED); // Nel caso in cui fosse un cuore testuale       // Vita attiva
            } else {
                // VITA PERSA: Svuotiamo l'immagine. 
            // Grazie al setPreferredSize che abbiamo messo su, la scatola non si restringe!
                heartLabels[i].setIcon(null); 
            
            // Rendiamo trasparente l'eventuale cuore testuale del "Piano B"
                heartLabels[i].setForeground(new Color(0, 0, 0, 0));
            }
        }
    
    // Poiché non abbiamo rimosso o aggiunto oggetti grafici, 
    // NON serve più revalidate()! Basta un rapido repaint.
        livesContainer.repaint();
    }

    // Metodo per aggiornare il punteggio al centro
    public void updateScoreText(int currentScore) {
        // SE IL PUNTEGGIO NON È CAMBIATO, ABORTISCI IMMEDIATAMENTE!
        // Evita calcoli e refresh grafici inutili ogni 8ms
        if (currentScore == lastScore) {
            return; 
        }

        // Se il punteggio è cambiato (es. hai preso un punto o resettato il gioco)
        if (scoreLabel != null) {
            scoreLabel.setText("SCORE: " + currentScore);
        
            // Salvi il nuovo punteggio nello storico
            this.lastScore = currentScore; 
        }
    }

    public static JLabel loadImage(String filename){ //metodo da utilizzare quando capiremo come cazzo far funzionare il percorso dell'immagine
        BufferedImage image;
        JLabel imageContainer;
        try{
            image = ImageIO.read(new File(filename));//attualmente se il file cuore.png viene spostato fuori da tutte le altre cartelle viene caricato correttamente, se sta nella cartella resources dentro a src esplode e si carica il cuore a emoji provvisorio
            imageContainer = new JLabel(new ImageIcon(image));
            return imageContainer;
        }
        catch(Exception e){
            System.out.println("Errore nel caricamento di " + filename + ": " + e);
            System.out.println("Come misura di emergenza carico il cuore emoji");
            // GIUBBOTTO ANTIPROIETTILE: Ritorna una Label con un cuore invece di null
            JLabel errorLabel = new JLabel("♥"); 
            errorLabel.setForeground(Color.RED);
            return errorLabel;
        }
    }


    // Getter per dare il controllo del bottone al GamePanel
    public JButton getPauseButton() {
        return this.pauseButton;
    }
}