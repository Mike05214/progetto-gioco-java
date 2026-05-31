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
        
        JButton btnIndietro = new JButton("Torna al Menu (Test)");
        
        btnIndietro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.cambiaSchermata("MENU");
            }
        });
        
        add(btnIndietro);
    }
}
