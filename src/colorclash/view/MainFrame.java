package src.colorclash.view;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Container;
import java.awt.BorderLayout;

public class MainFrame extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private final int FRAME_WIDTH = 800;
    private final int FRAME_HEIGHT = 600;

    
    public MainFrame() {
        setTitle("Color Clash");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // Da RIVEDERE
        setLocationRelativeTo(null); // Centra la finestra DA RIVEDERE ANCHE QUESTO
        Container contPane = this.getContentPane();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        MenuPanel menuPanel = new MenuPanel(this);
        GamePanel gamePanel = new GamePanel(this);
        contPane.add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(gamePanel, "GAME");
        
        contPane.add(mainPanel);
        cardLayout.show(mainPanel, "MENU");
        
    }
    
    
    public void changeFrame(String panelName) {
        cardLayout.show(mainPanel, panelName);
        if(panelName.equals("GAME")) {
            mainPanel.getComponent(1).requestFocusInWindow(); // imposta il focus sulla schermata del gioco
        }
        
    }
}