package src.colorclash.view;

import src.colorclash.model.Avatar;
import src.colorclash.model.GameModel;
import src.colorclash.model.Obstacle;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font; // Importante per la grandezza del testo!

public class PausePanel extends BaseMenuPanel{

    private GameModel model;
    private final int ROW_0 = 0;
    private final int ROW_1 = 1;


    public PausePanel(MainFrame frame, GameModel model){ // il model da resettare è quello del gamePanel che infatti gli viene passato come parametro
        super();
        this.model = model; // è quello del gamePanel
        this.setBackground(Color.DARK_GRAY);
        initTitleLabel("PAUSE", Color.RED);
        JButton resume = new JButton("RESUME");
        resume.setFont(new Font("Arial", Font.BOLD, BUTTON_TEXT_SIZE));
        resume.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        JButton backToMenu = new JButton("BACK TO MENU");
        backToMenu.setFont(new Font("Arial", Font.BOLD, BUTTON_TEXT_SIZE));
        backToMenu.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        resume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.changeFrame("GAME"); // il gioco esce dallo stato di pausa e ricomincia da dove interrotto grazie al metodo setVisibleTrue nel gamePanel
            }
        });

        backToMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.changeFrame("MENU");
                resetGame();
            }
        });
        //metodi ereditati dalla superclasse BaseMenuPanel che servono ad aggiungere i bottoni al PausePanel secondo la logica della BaseMenuPanel
        addComponentToCenter(resume, ROW_0, true);
        addComponentToCenter(backToMenu, ROW_1, true);
    }

    public void resetGame(){
        model.getPlayer().resetToInitialSettings(model.getStartX(), model.getStartY(),model.getStartColorId());
        model.resetScore();
        model.resetObstacles();
    }
}
