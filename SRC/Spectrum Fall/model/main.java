package model;

package jfall;

import jfall.model.GameModel;
import jfall.view.MainFrame;
import javax.swing.SwingUtilities;

public class Main {
    
    public static void main(String[] args) {
        // Regola fondamentale di Swing: l'interfaccia grafica va avviata 
        // sul thread dedicato chiamato Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 1. Inizializza lo stato del gioco (Model)
                GameModel model = new GameModel();
                
                // 2. Inizializza la finestra principale (View) passando il modello come parametro
                MainFrame frame = new MainFrame(model);
                
                // 3. Rendi la finestra visibile sullo schermo
                frame.setVisible(true);
            }
        });
    }
}public class main {
    
}
