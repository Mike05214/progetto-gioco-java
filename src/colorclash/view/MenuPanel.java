package src.colorclash.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import src.colorclash.main.MainFrame;

public class MenuPanel extends JPanel {

    public MenuPanel(MainFrame frame) {
        setBackground(Color.DARK_GRAY);
        
        JButton btnGioca = new JButton("Gioca");
        
        btnGioca.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.cambiaSchermata("GIOCO");
            }
        });
        
        add(btnGioca);
    }
}