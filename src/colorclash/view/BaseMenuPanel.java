package src.colorclash.view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font; 
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;

public abstract class BaseMenuPanel extends JPanel {
    //costanti protette
    protected final int BUTTON_WIDTH = 200; 
    protected final int BUTTON_HEIGHT = 50;
    protected final int BUTTON_TEXT_SIZE = 20;
    protected final int TITLE_SIZE = 60;
    protected final int TOP = 0;
    protected final int LEFT = 0;
    protected final int RIGHT = 0;
    protected final int BETWEEN_SPACE = 30;
    protected final int ONE_COLUMN = 0;

    //variabili d'istanza
    protected JPanel centerPanel;

    //metodi pubblici
    public BaseMenuPanel(){
        this.setLayout(new BorderLayout());

        this.centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        this.add(centerPanel, BorderLayout.CENTER);
    }//fine costruttore

    //metodi protetti
    protected void initTitleLabel(String text, Color color){
        JLabel titleLabel = new JLabel(text, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, TITLE_SIZE));
        titleLabel.setForeground(color);
        this.add(titleLabel, BorderLayout.NORTH);
    }//fine initTitleLabel

    protected void addComponentToCenter(Component comp, int row, boolean hasSpaceBelow){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = ONE_COLUMN; //una sola colonna verticale
        gbc.gridy = row;
        gbc.insets = new Insets(TOP,LEFT, hasSpaceBelow ? BETWEEN_SPACE : 0, RIGHT);
        centerPanel.add(comp, gbc);
    }//fine addComponentToCenter
}//fine classe astratta BaseMenuPanel
