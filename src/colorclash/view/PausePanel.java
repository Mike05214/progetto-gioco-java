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

public class PausePanel extends JPanel{

    private GameModel model;


    public PausePanel(MainFrame frame, GameModel model){ // il model da resettare è quello del gamePanel che infatti gli viene passato come parametro
        this.model = model; // è quello del gamePanel
        this.setLayout(new BorderLayout());
        this.setBackground(Color.DARK_GRAY);
        JButton resume = new JButton("RESUME");
        JButton backToMenu = new JButton("BACK TO MENU");
        this.add(resume, BorderLayout.NORTH);
        this.add(backToMenu, BorderLayout.SOUTH);

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
                model.getPlayer().resetToInitialSettings(model.getStartX(), model.getStartY(),model.getStartColorId());
                model.getPlayer().resetMovementFlags();
                model.resetScore();
                model.resetObstacles();
            }
        });
    }
}
