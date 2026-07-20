package src.colorclash.view;
import src.colorclash.utils.AudioManager;
import src.colorclash.utils.Config;
import src.colorclash.model.GameModel;
import src.colorclash.utils.AudioManager;

import javax.swing.JFrame;
import javax.swing.JPanel;


import java.awt.CardLayout;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class MainFrame extends JFrame {
    // variabili d'istanza
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GamePanel gamePanel;
    private MenuPanel menuPanel;
    private PausePanel pausePanel;
    private GameModel model;

    // costanti
    private final int FRAME_WIDTH = Config.getInstance().getIntProperty("frame_width");
    private final int FRAME_HEIGHT = Config.getInstance().getIntProperty("frame_height");

    public MainFrame() {
        this.model = GameModel.getInstance();
        initialSettings();
        frameStructureBuilder();
    }// fine costruttore

    public void changeFrame(String panelName) {
        // Se stiamo per mostrare il menu, aggiorniamo il bottone

        cardLayout.show(mainPanel, panelName);

        if (panelName.equals("GAME")) {
            mainPanel.getComponent(1).requestFocusInWindow();
            AudioManager.getInstance().playBackgroundMusic("sample-15s.wav");
        }
        if (panelName.equals("MENU")) {
            AudioManager.getInstance().stopBackgroundMusic();
        }
    }

    private void initialSettings() {
        setTitle("Color Clash");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        // Ascoltatore che si attiva nel momento esatto in cui premi la "X"
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                // Salva lo stato SOLO se la finestra attiva è il gioco o il menu di pausa
                if (gamePanel.isVisible()) {
                    if (model != null) {
                        model.autoSave();
                    }
                }
                // Chiude la finestra e spegne la JVM in modo pulito
                dispose();
                System.exit(0);
            }
        });
    }// fine initialSettings

    private void frameStructureBuilder() {
        Container contPane = this.getContentPane();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        this.menuPanel = new MenuPanel(this);
        this.gamePanel = new GamePanel(this);
        this.pausePanel = new PausePanel(this);
        contPane.add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(gamePanel, "GAME");
        mainPanel.add(pausePanel, "PAUSE");

        contPane.add(mainPanel);
        cardLayout.show(mainPanel, "MENU");
    }// fine frameStructureBuilder

    // GETTERS
    public GamePanel getGamePanel() {
        return this.gamePanel;
    }

    public MenuPanel getMenuPanel() {
        return this.menuPanel;
    }
}