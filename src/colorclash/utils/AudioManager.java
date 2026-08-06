package src.colorclash.utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;

public class AudioManager {

    private static AudioManager instance;
    private Clip backgroundMusic;
    private boolean isMuted = false;

    // costruttore
    private AudioManager() {}

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void playSoundEffect(String fileName) {
        if (isMuted) {
            return;
        }

        InputStream is = null;
        ByteArrayInputStream bais = null;
        AudioInputStream audioIn = null;

        try {
            // Carica il file audio direttamente come flusso di byte (funziona sia su IDE che nel .jar)
            is = AudioManager.class.getResourceAsStream("/src/colorclash/sounds/" + fileName);
            
            if (is == null) {
                System.out.println("ERRORE: File audio non trovato -> " + fileName);
                return;
            }
            
            byte[] audioData = is.readAllBytes();
            bais = new ByteArrayInputStream(audioData);
            audioIn = AudioSystem.getAudioInputStream(bais);

            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });

        } catch (Exception e) {
            System.out.println("Errore riproduzione effetto sonoro: " + fileName);
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close(); 
                if (bais != null) bais.close();
                if (audioIn != null) audioIn.close();
            } catch (IOException ioe) { 
                ioe.printStackTrace();
            }
        } 
    }

    public void playBackgroundMusic(String fileName) {
        if (isMuted) {
            return;
        }

        InputStream is = null; 
        ByteArrayInputStream bais = null;
        AudioInputStream audioIn = null;

        try {
            // Carica il file audio direttamente come flusso di byte
            is = AudioManager.class.getResourceAsStream("/src/colorclash/sounds/" + fileName);
            
            if (is == null) {
                System.out.println("ERRORE: File audio non trovato -> " + fileName);
                return;
            }

            byte[] audioData = is.readAllBytes();
            bais = new ByteArrayInputStream(audioData);
            audioIn = AudioSystem.getAudioInputStream(bais);

            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioIn);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {
            System.out.println("Errore riproduzione musica di sottofondo: " + fileName);
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close(); 
                if (bais != null) bais.close();
                if (audioIn != null) audioIn.close();
            } catch (IOException ioe) { 
                ioe.printStackTrace(); 
            }
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