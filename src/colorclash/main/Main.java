package src.colorclash.main;

import src.colorclash.view.MainFrame;
import src.colorclash.utils.AudioManager;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.Color;
import java.awt.Font;

public class Main {
    public static void main(String[] args) {
        // PREVIENE IL BUG DEL CLEARTYPE DI WINDOWS, "Usa il tuo anti-aliasing interno e
        // non fare domande a Windows".
        System.setProperty("awt.useSystemAAFontSettings", "on"); //
        System.setProperty("swing.aatext", "true"); // 

        // --- INIZIO IMPOSTAZIONI LOOK AND FEEL ---
        try {
            
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
            UIManager.put("Button.background", new Color(50, 50, 50));
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.font", new Font("Impact", Font.PLAIN, 20));
            UIManager.put("Button.focus", new Color(0, 0, 0, 0));
            

        } catch (Exception e) {  //Nel caso venga catturata un'eccezione il programma applica il look and fell di default
            System.err.println("Error setting the Look and Feel.");
            e.printStackTrace();
        }
       
        // --- FINE IMPOSTAZIONI LOOK AND FEEL ---

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });

        AudioManager.getInstance().playBackgroundMusic("menu.wav");

    }// fine main
}// fine classe Main