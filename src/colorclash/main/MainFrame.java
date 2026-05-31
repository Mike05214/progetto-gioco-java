package src.colorclash.main;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import src.colorclash.view.GamePanel;
import src.colorclash.view.MenuPanel;

public class MainFrame extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel pannelloPrincipale;
    private final int FRAME_WIDTH = 800;
    private final int FRAME_HEIGHT = 600;

    
    public MainFrame() {
        setTitle("Color Clash");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // Da RIVEDERE
        setLocationRelativeTo(null); // Centra la finestra DA RIVEDERE ANCHE QUESTO
        
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