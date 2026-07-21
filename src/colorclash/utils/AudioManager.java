package src.colorclash.utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class AudioManager {

    private static AudioManager instance;
    private Clip backgroundMusic;
    private boolean isMuted = false;

    // Costruttore privato per Singleton
    private AudioManager() {
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    // ==========================================
    // 1. EFFETTI SONORI (Esplosioni, Danni)
    // ==========================================
    public void playSoundEffect(String fileName) {
        if(isMuted){
            return;
        }
        try {
            InputStream is = getClass().getResourceAsStream("/src/colorclash/sounds/" + fileName);
            if (is == null) {
                System.out.println("ERRORE: File audio non trovato -> /sounds/" + fileName);
                return;
            }

            // Legge tutto in RAM per evitare l'errore "Resetting to invalid mark"
            byte[] audioData = is.readAllBytes();
            ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(bais);

            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();

            // Chiude il canale audio a fine riproduzione (evita Memory Leak)
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });

        } catch (Exception e) {
            System.out.println("Eccezione riproduzione effetto sonoro: " + fileName);
            e.printStackTrace();
        }
    }

    // ==========================================
    // 2. MUSICA DI SOTTOFONDO IN LOOP
    // ==========================================
    public void playBackgroundMusic(String fileName) {
        if(isMuted){
            return;
        }
        try {
            InputStream is = getClass().getResourceAsStream("/src/colorclash/sounds/" + fileName);
            if (is == null) {
                System.out.println("ERRORE: File audio non trovato -> /sounds/" + fileName);
                return;
            }

            // Legge tutto in RAM per evitare l'errore "Resetting to invalid mark"
            byte[] audioData = is.readAllBytes();
            ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(bais);

            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioIn);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {
            System.out.println("Eccezione riproduzione musica di sottofondo.");
            e.printStackTrace();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    public void toggleSound() {
        isMuted = !isMuted;
        if (isMuted) {
            stopBackgroundMusic();
        }
    }

    public boolean isSoundEnabled() {
        return !isMuted;
    }
}