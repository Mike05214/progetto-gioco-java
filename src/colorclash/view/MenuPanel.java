package src.colorclash.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import src.colorclash.main.MainFrame;

public class MenuPanel extends JPanel {

    private final int GRID_ROWS = 2;
    private final int GRID_COLS = 1;
    private final int GRID_HGAP = 0;
    private final int GRID_VGAP = 2;


    public MenuPanel(MainFrame frame) {
        this.setBackground(Color.DARK_GRAY);
        
        this.setLayout(new GridBagLayout()); //layout base del MenuPanel, serve a disporre al centro buttonGrid (consiglio di gemini)
        JButton playButton = new JButton("GAME");
        JButton hiButton = new JButton("HIGHSCORE");
        
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.changeFrame("GAME");
            }
        });

        JPanel buttonGrid = new JPanel(new GridLayout(GRID_ROWS, GRID_COLS, GRID_HGAP, GRID_VGAP));
        buttonGrid.setOpaque(false);
        buttonGrid.add(playButton);
        buttonGrid.add(hiButton);
        this.add(buttonGrid);
        
    }
}