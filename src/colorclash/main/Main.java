package src.colorclash.main;

import src.colorclash.view.MainFrame;

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
