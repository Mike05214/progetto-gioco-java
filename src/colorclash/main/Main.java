package src.colorclash.main;
import javax.swing.JFrame;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JButton;


public class Main {
    public static void main (String[] args){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run(){
                MainFrame mainFrame= new MainFrame();
                mainFrame.setVisible(true);
                
            }
            
        });
        
    }
}
