import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        // Crea la finestra del gioco
        JFrame finestra = new JFrame("Il Nostro Gioco Java");
        
        // Imposta le dimensioni (Larghezza, Altezza)
        finestra.setSize(800, 600);
        
        // Fa in modo che il programma si chiuda quando premi la X
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Centra la finestra sullo schermo
        finestra.setLocationRelativeTo(null);
        
        // Rende la finestra visibile
        finestra.setVisible(true);

        System.out.println("ciao");
    }
}