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

    //parametri dichiarati come costanti
    private final int BUTTON_WIDTH = 200;
    private final int BUTTON_HEIGHT = 50;
    private final int BUTTON_TEXT_SIZE = 20;
    private final int TITLE_SIZE = 60;
    private final int HI_LABEL_SIZE = 18;

    private JLabel highScoreLabel; // etichetta dell'Highscore 
    private JButton resumeButton; // Dichiariamo il bottone qui per poterlo abilitare/disabilitare dopo


    public MenuPanel(MainFrame frame) {
        this.setBackground(Color.DARK_GRAY);
        
        this.setLayout(new BorderLayout()); //layout base del MenuPanel, serve a disporre al centro buttonGrid (consiglio di gemini)
        JButton playButton = new JButton("PLAY");
        playButton.setFont(new Font("Arial", Font.BOLD, BUTTON_TEXT_SIZE));
        playButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        this.highScoreLabel = new JLabel("High Score:");
        this.highScoreLabel.setFont(new Font("Arial", Font.PLAIN, HI_LABEL_SIZE));
        this.highScoreLabel.setForeground(Color.WHITE); // imposto colore etichetta
        this.resumeButton = new JButton("RESUME");
        this.resumeButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        this.resumeButton.setFont(new Font("Arial", Font.BOLD, BUTTON_TEXT_SIZE));
        this.resumeButton.setEnabled(false); // Appena apri il gioco, non puoi fare "Riprendi"

        JLabel titleLabel = new JLabel("COLOR CLASH",SwingConstants.CENTER); // etichetta del titolo gioco e forzo a stare al centro
        titleLabel.setFont(new Font("Arial", Font.BOLD, TITLE_SIZE));
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