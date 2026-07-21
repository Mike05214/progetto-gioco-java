package src.colorclash.utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AudioManager {

    private static AudioManager instance;
    private Clip backgroundMusic;
    private boolean isMuted = false;

    // Costruttore privato (Design Pattern Singleton)
    private AudioManager() {}

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    // Ricava a run-time il percorso completo del file per garantire la portabilità
    private String getAudioFileFullPath(String fileName) {
        String path = AudioManager.class.getResource("/src/colorclash/sounds/" + fileName).toString(); //
        
        if (path.contains("//")) {
            path = path.substring("file:/".length()); // Versione Windows[cite: 5]
        } else if (path.contains("/")) {
            path = path.substring("file:".length()); // Versione Linux[cite: 5]
        }
        
        path = path.replaceAll("%20", " "); //[cite: 5]
        return path;
    }

    // ==========================================
    // 1. EFFETTI SONORI (Esplosioni, Danni)
    // ==========================================
    public void playSoundEffect(String fileName) {
        if (isMuted) {
            return;
        }

        FileInputStream fis = null; //
        ByteArrayInputStream bais = null;
        AudioInputStream audioIn = null;

        try {
            fis = new FileInputStream(getAudioFileFullPath(fileName)); //
            
            byte[] audioData = fis.readAllBytes();
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

        } catch (FileNotFoundException fnfe) { //
            System.out.println("ERRORE: File audio non trovato -> " + fileName);
            fnfe.printStackTrace(); //[cite: 1, 3, 5]
        } catch (IOException ioe) { //[cite: 1, 3, 5]
            System.out.println("Errore di IO riproduzione effetto sonoro: " + fileName);
            ioe.printStackTrace(); //[cite: 1, 3, 5]
        } catch (Exception e) {
            // Catch generico mantenuto solo per le eccezioni specifiche di javax.sound (non coperte dal prof)
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close(); //[cite: 1, 3, 5]
                if (bais != null) bais.close();
                if (audioIn != null) audioIn.close();
            } catch (IOException ioe) { //[cite: 1, 3, 5]
                ioe.printStackTrace(); //[cite: 1, 3, 5]
            }
        } //[cite: 1, 3, 5]
    }

    // ==========================================
    // 2. MUSICA DI SOTTOFONDO IN LOOP
    // ==========================================
    public void playBackgroundMusic(String fileName) {
        if (isMuted) {
            return;
        }

        FileInputStream fis = null; //[cite: 1, 3, 5]
        ByteArrayInputStream bais = null;
        AudioInputStream audioIn = null;

        try {
            fis = new FileInputStream(getAudioFileFullPath(fileName)); //[cite: 1, 3, 5]

            byte[] audioData = fis.readAllBytes();
            bais = new ByteArrayInputStream(audioData);
            audioIn = AudioSystem.getAudioInputStream(bais);

            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioIn);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (FileNotFoundException fnfe) { //[cite: 1, 3, 5]
            System.out.println("ERRORE: File audio non trovato -> " + fileName);
            fnfe.printStackTrace(); //[cite: 1, 3, 5]
        } catch (IOException ioe) { //[cite: 1, 3, 5]
            System.out.println("Errore di IO riproduzione musica di sottofondo.");
            ioe.printStackTrace(); //[cite: 1, 3, 5]
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close(); //[cite: 1, 3, 5]
                if (bais != null) bais.close();
                if (audioIn != null) audioIn.close();
            } catch (IOException ioe) { //[cite: 1, 3, 5]
                ioe.printStackTrace(); //[cite: 1, 3, 5]
            }
        } //[cite: 1, 3, 5]
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