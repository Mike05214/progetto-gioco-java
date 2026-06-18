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

public class MenuPanel extends BaseMenuPanel {

    //parametri dichiarati come costanti
    private final int HI_LABEL_SIZE = 18;
    private final int ROW_0 = 0;
    private final int ROW_1 = 1;
    private final int ROW_2 = 2;


    private JLabel highScoreLabel; // etichetta dell'Highscore 
    private JButton resumeButton; // Dichiariamo il bottone qui per poterlo abilitare/disabilitare dopo
    private JButton playButton;


    public MenuPanel(MainFrame frame) {
        super();
        setBackground(Color.DARK_GRAY);
        
        initButtons(frame); // i bottoni vengono comunque costruiti qui nel MenuPanel, dal BaseMenuPanel arriva solo il layout
        initTitleLabel("COLOR CLASH", Color.YELLOW);
        
        //metodi ereditati dalla superclasse BaseMenuPanel che servono ad aggiungere i bottoni al MenuPanel secondo la logica della BaseMenuPanel
        addComponentToCenter(playButton, ROW_0, true);
        addComponentToCenter(resumeButton, ROW_1, true);
        addComponentToCenter(highScoreLabel, ROW_2, true);
        
    }//FINE COSTRUTTORE

    // --- METODO HELPER PER IL FUTURO ---
    // Quando la partita finirà, chiameremo questo metodo per aggiornare il testo!
    public void updateHighScoreDisplay(int newScore) {
        highScoreLabel.setText("High Score: " + newScore);
    }

    public void initButtons(MainFrame frame){
        playButton = new JButton("PLAY");
        playButton.setFont(new Font("Arial", Font.BOLD, BUTTON_TEXT_SIZE));
        playButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.changeFrame("GAME");
            }
        });
        
        highScoreLabel = new JLabel("High Score:");
        highScoreLabel.setFont(new Font("Arial", Font.PLAIN, HI_LABEL_SIZE));
        highScoreLabel.setForeground(Color.WHITE); // imposto colore etichetta
        resumeButton = new JButton("RESUME");
        resumeButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        resumeButton.setFont(new Font("Arial", Font.BOLD, BUTTON_TEXT_SIZE));
        resumeButton.setEnabled(false); // Appena apri il gioco, non puoi fare "Riprendi"

        
    }
}