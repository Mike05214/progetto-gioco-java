package src.colorclash.view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font; // Importante per la grandezza del testo!
import java.awt.Component;

public abstract class BaseMenuPanel extends JPanel {
    protected final int BUTTON_WIDTH = 200;
    protected final int BUTTON_HEIGHT = 50;
    protected final int BUTTON_TEXT_SIZE = 20;
    protected final int TITLE_SIZE = 60;
    private final int BETWEEN_SPACE = 30;

    protected JPanel centerPanel;

    public BaseMenuPanel(){
        this.setBackground(Color.DARK_GRAY);
        this.setLayout(new BorderLayout());

        this.centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        this.add(centerPanel, BorderLayout.CENTER);
    }

    protected void initTitleLabel(String text, Color color){
        JLabel titleLabel = new JLabel(text, SwingConstants.CENTER); //la label è così al centro del suo stesso spazio
        titleLabel.setFont(new Font("Arial", Font.BOLD, TITLE_SIZE));
        titleLabel.setForeground(color);
        this.add(titleLabel, BorderLayout.NORTH);
    }

    protected void addComponentToCenter(Component comp, int row, boolean hasSpaceBelow){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; //una sola colonna verticale
        gbc.gridy = row;
        gbc.insets = new Insets(0,0, hasSpaceBelow ? BETWEEN_SPACE : 0, 0); // quel ? è l'operatore ternario: condizione ? valore_se_vero : valore_se_falsa, è un if compatto
        // i quattro parametri del metodo insest sono: top, left, bottom, right, il bottom è l'unico gestito dinamicamente perchè è lo spazio che separa i bottoni
        centerPanel.add(comp, gbc);

    }
}
