package src.colorclash.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font; // Importante per la grandezza del testo!

public class MenuPanel extends JPanel {

    private final int GRID_ROWS = 2;
    private final int GRID_COLS = 1;
    private final int GRID_HGAP = 0;
    private final int GRID_VGAP = 20;

    private JLabel highScoreLabel; // etichetta dell'Highscore 
    private JButton resumeButton; // Dichiariamo il bottone qui per poterlo abilitare/disabilitare dopo


    public MenuPanel(MainFrame frame) {
        this.setBackground(Color.DARK_GRAY);
        
        this.setLayout(new BorderLayout()); //layout base del MenuPanel, serve a disporre al centro buttonGrid (consiglio di gemini)
        JButton playButton = new JButton("GAME");
        playButton.setFont(new Font("Arial", Font.BOLD, 20));
        playButton.setPreferredSize(new Dimension(200,50));
        this.highScoreLabel = new JLabel("High Score:");
        this.highScoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        this.highScoreLabel.setForeground(Color.WHITE); // imposto colore etichetta
        this.resumeButton = new JButton("RESUME");
        this.resumeButton.setPreferredSize(new Dimension(200, 50));
        this.resumeButton.setFont(new Font("Arial", Font.BOLD, 20));
        this.resumeButton.setEnabled(false); // Appena apri il gioco, non puoi fare "Riprendi"

        JLabel titleLabel = new JLabel("COLOR CLASH",SwingConstants.CENTER); // etichetta del titolo gioco e forzo a stare al centro
        titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
        titleLabel.setForeground(Color.CYAN); 
        this.add(titleLabel,BorderLayout.NORTH);
    
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.changeFrame("GAME");
            }
        });

        //nuovo layout dei bottoni
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        // Posizioniamo gli elementi nel pannellino centrale
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Colonna unica centrale
        
        gbc.gridy = 0; // Riga 0 per il bottone
        gbc.insets = new Insets(0, 0, 30, 0); // 30 pixel di spazio vuoto SOTTO il bottone
        centerPanel.add(playButton, gbc);

        gbc.gridy=1;
        gbc.insets= new Insets(0, 0, 30, 0);
        centerPanel.add(resumeButton,gbc);

        gbc.gridy = 2; // Riga 1 per la label
        gbc.insets = new Insets(0, 0, 0, 0); // Nessuno spazio sotto
        centerPanel.add(highScoreLabel, gbc);
        
        // 3. INSERIMENTO AL CENTRO DELLA FINESTRA
        this.add(centerPanel, BorderLayout.CENTER);
        
    }

    // --- METODO HELPER PER IL FUTURO ---
    // Quando la partita finirà, chiameremo questo metodo per aggiornare il testo!
    public void updateHighScoreDisplay(int newScore) {
        highScoreLabel.setText("High Score: " + newScore);
    }
}