package src.colorclash.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import src.colorclash.main.MainFrame;

public class GamePanel extends JPanel {

    public GamePanel(MainFrame frame) {
        setBackground(Color.BLACK);
        
        JButton backButton = new JButton("Back to Menu");
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.changeFrame(TOOL_TIP_TEXT_KEY);
            }
        });
        
        add(backButton);
    }
}
