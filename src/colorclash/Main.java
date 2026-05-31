package src.colorclash;
import javax.swing.JFrame;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JButton;


public class Main {
    public static void main (String[] args){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run(){
                JFrame jframe= new JFrame();
                jframe.setVisible(true);
                // ciao
            }
            
        });
        
    }
}
