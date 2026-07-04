package src.colorclash.view;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.BorderLayout;

public class MainFrame extends JFrame {
    //variabili d'istanza
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GamePanel gamePanel;
    private MenuPanel menuPanel;
    private PausePanel pausePanel;

    //costanti
    private final int FRAME_WIDTH = 700;
    private final int FRAME_HEIGHT = 900;

    
    public MainFrame() {
        initialSettings();
        frameStructureBuilder();
    }//fine costruttore
    
    public void changeFrame(String panelName) {
        cardLayout.show(mainPanel, panelName);
        if(panelName.equals("GAME")) {
            mainPanel.getComponent(1).requestFocusInWindow();
        }
        
    }//fine changeFrame

    private void initialSettings(){
        setTitle("Color Clash");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); 
        setLocationRelativeTo(null);
    }//fine initialSettings

    private void frameStructureBuilder(){
        Container contPane = this.getContentPane();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        this.menuPanel = new MenuPanel(this);
        this.gamePanel = new GamePanel(this);
        this.pausePanel = new PausePanel(this, gamePanel.getModel());
        contPane.add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(gamePanel, "GAME");
        mainPanel.add(pausePanel, "PAUSE");
        
        contPane.add(mainPanel);
        cardLayout.show(mainPanel, "MENU");
    }//fine frameStructureBuilder

    public GamePanel getGamePanel(){
        return this.gamePanel;
    }
}