package src.colorclash.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;

public abstract class BaseMenuPanel extends JPanel {
    // costanti protette
    protected final int BUTTON_WIDTH = 200;
    protected final int BUTTON_HEIGHT = 50;
    protected final int BUTTON_TEXT_SIZE = 20;
    protected final int TITLE_SIZE = 60;
    protected final int TOP = 0;
    protected final int LEFT = 0;
    protected final int RIGHT = 0;
    protected final int BETWEEN_SPACE = 30;
    protected final int ONE_COLUMN = 0;

    // variabili d'istanza
    protected JPanel centerPanel;

    // metodi pubblici
    public BaseMenuPanel() {
        this.setLayout(new BorderLayout());

        this.centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        this.add(centerPanel, BorderLayout.CENTER);
    }// fine costruttore

    // metodi protetti
    protected void initTitleLabel(String text, Color color) {
        JLabel titleLabel = new JLabel(text, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                // Disegna lo sfondo se opaco
                if (isOpaque()) {
                    g.setColor(getBackground());
                    g.fillRect(0, 0, getWidth(), getHeight());
                }

                Graphics2D g2 = (Graphics2D) g;

                // Forza l'anti-aliasing per la massima qualità del testo
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(getFont()); // Usa il font impostato fuori

                String text = getText();
                FontMetrics fm = g2.getFontMetrics();

                // 1. Definisci la tua sequenza di colori (puoi allungarla o accorciarla)
                Color[] colori = {
                        Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
                        Color.CYAN, Color.BLUE, Color.MAGENTA
                };

                // 2. Calcola le coordinate centrali
                // La coordinata Y è l'altezza della riga di base
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

                // La coordinata X iniziale per far sì che la parola intera sia centrata
                int xAttuale = (getWidth() - fm.stringWidth(text)) / 2;

                // 3. Itera su ogni carattere e disegnalo singolarmente
                for (int i = 0; i < text.length(); i++) {
                    char carattere = text.charAt(i);
                    String pattern = String.valueOf(carattere);

                    // Se è uno spazio, saltalo ma sposta la coordinata X
                    if (Character.isWhitespace(carattere)) {
                        xAttuale += fm.stringWidth(pattern);
                        continue;
                    }

                    // Sceglie il colore dall'array basandosi sull'indice,
                    // usando l'operatore modulo (%) per ricominciare se i caratteri sono più dei
                    // colori.
                    g2.setColor(colori[i % colori.length]);

                    // Disegna il singolo carattere
                    g2.drawString(pattern, xAttuale, y);

                    // Sposta la X attuale della larghezza esatta del carattere appena disegnato
                    xAttuale += fm.stringWidth(pattern);
                }

                g2.dispose();
            }
        };
        titleLabel.setFont(new Font("Impact", Font.PLAIN, TITLE_SIZE));
        titleLabel.setForeground(color);
        this.add(titleLabel, BorderLayout.NORTH);
    }// fine initTitleLabel

    protected void addComponentToCenter(Component comp, int row, boolean hasSpaceBelow) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = ONE_COLUMN; // una sola colonna verticale
        gbc.gridy = row;
        gbc.insets = new Insets(TOP, LEFT, hasSpaceBelow ? BETWEEN_SPACE : 0, RIGHT);
        centerPanel.add(comp, gbc);
    }// fine addComponentToCenter
}// fine classe astratta BaseMenuPanel
