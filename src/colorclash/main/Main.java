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

        System.setProperty("awt.useSystemAAFontSettings", "on"); // DOC: Controls whether the system desktop anti-aliasing font settings
                                                                            // should be used by the Java 2D text renderer.
        System.setProperty("swing.aatext", "true");// DOC: Globally enables anti-aliasing for text rendering across all Swing components.

        // --- INIZIO IMPOSTAZIONI LOOK AND FEEL ---
        try {
            String osName = System.getProperty("os.name").toLowerCase();

            if (osName.contains("linux")) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            } else {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
            }

            UIManager.put("Button.background", new Color(50, 50, 50));
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.font", new Font("Impact", Font.PLAIN, 20));
            UIManager.put("Button.focus", new Color(0, 0, 0, 0));

        } catch (Exception e) {
            System.err.println("Error setting the Look and Feel.");
            e.printStackTrace();
        }
        // --- FINE IMPOSTAZIONI LOOK AND FEEL ---


        // --- INIZIO INIZIALIZZAZIONE SUONO ---
        AudioManager.getInstance().preloadSoundEffect("game_over.wav", 1);
        AudioManager.getInstance().preloadSoundEffect("hit.wav", 5);
        AudioManager.getInstance().preloadSoundEffect("hurt.wav", 2);
        AudioManager.getInstance().preloadSoundEffect("notification.wav", 4);
        AudioManager.getInstance().preloadSoundEffect("race_countdown.wav", 1);
        AudioManager.getInstance().playBackgroundMusic("menu.wav");
        // --- FINE INIZIALIZZAZIONE SUONO ---

        
        // --- AVVIO FINESTRA GRAFICA ---

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });
       

    }// fine main
    
}// fine classe Main