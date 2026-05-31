package src.colorclash.main;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import src.colorclash.view.GamePanel;
import src.colorclash.view.MenuPanel;

public class MainFrame extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel pannelloPrincipale;
    
    public MainFrame() {
        setTitle("Color Clash");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null); // Centra la finestra
        
        cardLayout = new CardLayout();
        pannelloPrincipale = new JPanel(cardLayout);
        
        MenuPanel menuPanel = new MenuPanel(this);
        GamePanel gamePanel = new GamePanel(this);
        
        pannelloPrincipale.add(menuPanel, "MENU");
        pannelloPrincipale.add(gamePanel, "GIOCO");
        
        add(pannelloPrincipale);
        cardLayout.show(pannelloPrincipale, "MENU");
    }
    
    public void cambiaSchermata(String nomeSchermata) {
        cardLayout.show(pannelloPrincipale, nomeSchermata);
        if(nomeSchermata.equals("GIOCO")) {
            pannelloPrincipale.getComponent(1).requestFocusInWindow(); 
        }
    }
}