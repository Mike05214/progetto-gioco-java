package src.colorclash.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import src.colorclash.main.MainFrame;

public class MenuPanel extends JPanel {

    public MenuPanel(MainFrame frame) {
        this.setBackground(Color.DARK_GRAY);
        
        JButton playButton = new JButton("GAME");
        
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.changeFrame("GAME");
            }
        });
        
        this.add(playButton);
    }
}