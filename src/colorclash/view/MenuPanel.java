package src.colorclash.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font; 

import javax.swing.JButton;
import javax.swing.JLabel;

public class MenuPanel extends BaseMenuPanel {
    //variabili d'istanza
    private JLabel highScoreLabel; 
    private JButton resumeButton; 
    private JButton playButton;

    //costanti
    private final int HI_LABEL_SIZE = 18;
    private final int ROW_0 = 0;
    private final int ROW_1 = 1;
    private final int ROW_2 = 2;
    
    public MenuPanel(MainFrame frame) {
        super();
        setBackground(Color.DARK_GRAY);
        initButtons(frame); 
        initTitleLabel("COLOR CLASH", Color.YELLOW);
        addComponentToCenter(playButton, ROW_0, true);
        addComponentToCenter(resumeButton, ROW_1, true);
        addComponentToCenter(highScoreLabel, ROW_2, true);
    }//fine costruttore

    public void updateHighScoreDisplay(int newScore) {
        highScoreLabel.setText("High Score: " + newScore);
    }//fine updateHighScoreDisplay

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
        highScoreLabel.setForeground(Color.WHITE); 
        resumeButton = new JButton("RESUME");
        resumeButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        resumeButton.setFont(new Font("Arial", Font.BOLD, BUTTON_TEXT_SIZE));
        resumeButton.setEnabled(false);
    }//fine initButtons
}//fine classe MenuPanel