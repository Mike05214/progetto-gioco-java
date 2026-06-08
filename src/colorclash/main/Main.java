package src.colorclash.main;

import src.colorclash.view.MainFrame;

public class Main {
    public static void main (String[] args){
        // PREVIENE IL BUG DEL CLEARTYPE DI WINDOWS, "Usa il tuo anti-aliasing interno e non fare domande a Windows".
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run(){
                MainFrame mainFrame= new MainFrame();
                mainFrame.setVisible(true);
            } 
        });
 
    }
}
