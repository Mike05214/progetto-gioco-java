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

    // Il costruttore riceve il modello, il frame e il pulsante pausa originale
    public HudPanel( MainFrame frame) {
        System.out.println("La radice del Classpath è qui: " + getClass().getResource("/"));
        this.frame = frame;

        // Il vostro amato BorderLayout
        this.setLayout(new BorderLayout());
        this.setBackground(Color.DARK_GRAY);

        // --- 2. CENTRO (CENTER): Score + Bottone Riavvio a scomparsa ---
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        centerPanel.setOpaque(false);

        // Pulsante "PAUSE" (si affianca allo score solo quando perdi)
        pauseButton = new JButton("PAUSE (ALT+X)");
        pauseButton.setMnemonic(KeyEvent.VK_X);
        pauseButton.setFont(new Font("Arial", Font.BOLD, 14));
        pauseButton.addActionListener(e -> {
            frame.changeFrame("PAUSE");
        });
        centerPanel.add(pauseButton);
        JPanel scoreContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        scoreContainer.setOpaque(false);

        scoreLabel = new JLabel("SCORE: 0");
        // Crea un bordo vuoto: (Alto, Sinistra, Basso, Destra)
// Mettendo un valore a DESTRA (es. 50), crei un muro invisibile 
// che spinge la scritta dello Score verso SINISTRA!
        
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreContainer.add(scoreLabel);

        this.add(centerPanel, BorderLayout.WEST);
        this.add(scoreContainer, BorderLayout.CENTER);

        // --- 3. DESTRA (EAST): Il contenitore dei cuori pixelati ---
        try {
    // Ora che è dentro 'src', il percorso parte pulito dalla radice del Classpath
    java.net.URL imgURL = getClass().getResource("/assets/cuore.png");
    
    if (imgURL != null) {
        // Se VS Code ha fatto il suo dovere, entra qui e legge l'immagine
        this.heartIcon = new ImageIcon(javax.imageio.ImageIO.read(imgURL));
        System.out.println("[GODO] Cuore caricato correttamente dal Classpath!");
    } else {
        // Se restituisce ancora null, significa che VS Code sta ancora "dormendo"
        System.out.println("[ATTENZIONE] Il Classpath è giusto, ma VS Code non ha ancora aggiornato il bunker.");
        
        // PIANO B DI EMERGENZA (Locale temporaneo per non far crashare il gioco)
        java.io.File fileLocale = new java.io.File("src/assets/cuore.png");
        if (fileLocale.exists()) {
            this.heartIcon = new ImageIcon(javax.imageio.ImageIO.read(fileLocale));
            System.out.println("[FALLBACK] Caricato da file fisico locale per questa volta.");
        } else {
            this.heartIcon = new ImageIcon(); // Icona vuota salvavita
        }
    }
} catch (Exception e) {
    System.out.println("[ERRORE] Qualcosa è andato storto nel caricamento.");
    this.heartIcon = new ImageIcon(); // Giubbotto antiproiettile finale
}

        // Spinge i cuori contro il bordo destro
        livesContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0)); //allineamento laterale lasciando hgap
        livesContainer.setOpaque(false);
        this.add(livesContainer, BorderLayout.EAST);
        livesLabel = new JLabel("LIVES: ");
        livesLabel.setForeground(Color.WHITE);
        livesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        livesContainer.add(livesLabel);
        // Creiamo i 3 cuori e li mettiamo nell'Array in modo da non perderli di vista
    for (int i = 0; i < MAX_LIVES; i++) {
        heartLabels[i] = new JLabel("♥");
        heartLabels[i].setFont(new Font("Arial", Font.BOLD, 24));
        heartLabels[i].setForeground(Color.RED); // Tutti rossi all'inizio
    
    // Li aggiungiamo al pannello una volta per tutte
        livesContainer.add(heartLabels[i]); 
    }
        
}//FINE COSTRUTTORE

    public void updateLivesView(int currentLives) {
    // Scorriamo le 3 etichette che già esistono in memoria
        for (int i = 0; i < MAX_LIVES; i++) {
            if (i < currentLives) {
                heartLabels[i].setForeground(Color.RED);       // Vita attiva
            } else {
                heartLabels[i].setForeground(Color.DARK_GRAY); // Vita persa
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

    // Getter per dare il controllo del bottone al GamePanel
    public JButton getPauseButton() {
        return this.pauseButton;
    }
}