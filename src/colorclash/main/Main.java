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
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // --- INIZIO IMPOSTAZIONI LOOK AND FEEL ---
        try {
            // Scegli il tema di Swing che vuoi testare cambiando questa stringa.
            // Puoi provare: "Nimbus", "Metal", oppure "CDE/Motif"
            String temaScelto = "Windows Classic";

            System.out.println("Temi disponibili su questo PC:");
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                System.out.println("- " + info.getName());
            }

            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (temaScelto.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

            // Personalizzazione globale (sovrascrive il tema scelto per questi elementi)
            UIManager.put("Button.background", new Color(50, 50, 50));
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.font", new Font("Impact", Font.PLAIN, 20));
            UIManager.put("Button.focus", new Color(0, 0, 0, 0));
            

        } catch (Exception e) {
            System.err.println("Errore durante l'impostazione del Look and Feel.");
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