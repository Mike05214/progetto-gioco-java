package src.colorclash.view;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Container;
import java.awt.BorderLayout;

public class MainFrame extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private final int FRAME_WIDTH = 700;
    private final int FRAME_HEIGHT = 900;

    
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
        PausePanel pausePanel = new PausePanel(this, gamePanel.getModel());
        contPane.add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(gamePanel, "GAME");
        mainPanel.add(pausePanel, "PAUSE");
        
        contPane.add(mainPanel);
        cardLayout.show(mainPanel, "MENU"); // questo metodo per ogni pannello nel cardLayout chiama il metodo setVisible e lo imposta a true per il pannello nel secondo argomento, per gli altri li mette a false
        
    }
    
    
    public void changeFrame(String panelName) {
        cardLayout.show(mainPanel, panelName);
        if(panelName.equals("GAME")) {
            mainPanel.getComponent(1).requestFocusInWindow(); // imposta il focus sulla schermata del gioco
        }
        
    }
}