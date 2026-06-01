package src.colorclash.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.GridBagLayout;

public class MenuPanel extends JPanel {

    private final int GRID_ROWS = 2;
    private final int GRID_COLS = 1;
    private final int GRID_HGAP = 0;
    private final int GRID_VGAP = 20;

    private JLabel highScoreLabel; // etichetta dell'Highscore 


    public MenuPanel(MainFrame frame) {
        this.setBackground(Color.DARK_GRAY);
        
        this.setLayout(new GridBagLayout()); //layout base del MenuPanel, serve a disporre al centro buttonGrid (consiglio di gemini)
        JButton playButton = new JButton("GAME");
        this.highScoreLabel = new JLabel("High Score:");
        this.highScoreLabel.setForeground(Color.WHITE); // imposto colore etichetta
        
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.changeFrame("GAME");
            }
        });

        //nuovo layout dei bottoni
        JPanel buttonGrid = new JPanel(new GridLayout(GRID_ROWS, GRID_COLS, GRID_HGAP, GRID_VGAP));
        buttonGrid.setOpaque(false);
        buttonGrid.add(playButton);
        buttonGrid.add(highScoreLabel); // ho messo il label alla fine mi ha detto anche gemini che andava tolto il bottone 
        this.add(buttonGrid); //aggiunge il panello con la griglia bottoni al menu (ottimo fili)
        
    }

    // --- METODO HELPER PER IL FUTURO ---
    // Quando la partita finirà, chiameremo questo metodo per aggiornare il testo!
    public void updateHighScoreDisplay(int newScore) {
        highScoreLabel.setText("High Score: " + newScore);
    }
}