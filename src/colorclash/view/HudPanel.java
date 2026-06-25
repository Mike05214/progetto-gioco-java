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
        this.setPreferredSize(new Dimension(0, 40));
       
        JPanel westernPanel = new JPanel(new GridBagLayout());
        // Pulsante "PAUSE" (si affianca allo score solo quando perdi)
        pauseButton = new JButton("PAUSE (ALT+X)");
        
        pauseButton.setMnemonic(KeyEvent.VK_X);
        pauseButton.setFont(new Font("Arial", Font.BOLD, 14));
        pauseButton.addActionListener(e -> {
            frame.changeFrame("PAUSE");
        });
        pauseButton.setPreferredSize(new Dimension(150, 40));
        westernPanel.add(pauseButton);
  
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        scoreLabel = new JLabel("SCORE: 0");      
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        centerPanel.add(scoreLabel);


        this.add(centerPanel, BorderLayout.CENTER);
        this.add(westernPanel, BorderLayout.WEST);


       

        // Spinge i cuori contro il bordo destro
        livesContainer = new JPanel(new GridBagLayout()); //allineamento laterale lasciando hgap
        livesContainer.setOpaque(false);
        livesContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        livesContainer.setPreferredSize(new Dimension(150, 40));// serve a fissare le dimensioni del pannello in modo che la label dello score non si sposti a sinistra quando scompare un cuore
        this.add(livesContainer, BorderLayout.EAST);
        
        // Creiamo i 3 cuori e li mettiamo nell'Array in modo da non perderli di vista
        for (int i = 0; i < MAX_LIVES; i++) {
            heartLabels[i] = loadImage("src/colorclash/resources/cuore.png");
            heartLabels[i].setPreferredSize(heartLabels[i].getPreferredSize()); // questa riga salva e congela la dimensione delle label dei cuori impedendogli di settarsi a 0 quando il cuore scompare, migliorando l'effetto grafico
    // Li aggiungiamo al pannello una volta per tutte
            livesContainer.add(heartLabels[i]); 
        }
        // 3. Ci "rubiamo" l'icona dal primo cuore e la mettiamo in cassaforte per dopo
        this.iconaDiSalvataggio = heartLabels[0].getIcon();
        
    }//FINE COSTRUTTORE

    public void updateLivesView(int currentLives) {
        // Scorriamo le 3 etichette che già esistono in memoria
        if(currentLives == lastLives){
            return;
        }
        for (int i = 0; i < MAX_LIVES; i++) {
        heartLabels[i].setVisible(true);
    
            // Il trucco: la soglia si sposta da sinistra verso destra man mano che perdi vite
            if (i >= (MAX_LIVES - currentLives)) {
        
                // VITA ATTIVA: Rimettiamo l'icona dalla nostra cassaforte
                heartLabels[i].setIcon(this.iconaDiSalvataggio); 
                heartLabels[i].setForeground(Color.RED); // Nel caso in cui fosse un cuore testuale
        
            } 
            else{
        
                // VITA PERSA: Svuotiamo l'immagine. 
                // Grazie a heartLabels[i].setPreferredSize(heartLabels[i].getPreferredSize()) sopra la dimensione della label del cuore scomparso non si annulla mantenendo tutto in posizione
                heartLabels[i].setIcon(null); 
        
                // Rendiamo trasparente l'eventuale cuore testuale del "Piano B"
                    heartLabels[i].setForeground(new Color(0, 0, 0, 0));
        
            }
        }
        // Poiché non abbiamo rimosso o aggiunto oggetti grafici, 
        // NON serve più revalidate()! Basta un rapido repaint.
        livesContainer.repaint();
        lastLives = currentLives;
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