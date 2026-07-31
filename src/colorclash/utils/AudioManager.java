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

    private AudioManager() {}

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    private String getAudioFileFullPath(String fileName) {
        String path = AudioManager.class.getResource("/src/colorclash/sounds/" + fileName).toString(); 
        
        if (path.contains("//")) {
            path = path.substring("file:/".length()); // Versione Windows
        } else if (path.contains("/")) {
            path = path.substring("file:".length()); // Versione Linux
        }
        
        path = path.replaceAll("%20", " "); 
        return path;
    }

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

        } catch (FileNotFoundException fnfe) { 
            System.out.println("ERRORE: File audio non trovato -> " + fileName);
            fnfe.printStackTrace(); 
        } catch (IOException ioe) { 
            System.out.println("Errore di IO riproduzione effetto sonoro: " + fileName);
            ioe.printStackTrace(); 
        } catch (Exception e) {
            
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close(); 
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

        FileInputStream fis = null; 
        ByteArrayInputStream bais = null;
        AudioInputStream audioIn = null;

        try {
            fis = new FileInputStream(getAudioFileFullPath(fileName)); 

            byte[] audioData = fis.readAllBytes();
            bais = new ByteArrayInputStream(audioData);
            audioIn = AudioSystem.getAudioInputStream(bais);

            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioIn);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (FileNotFoundException fnfe) { 
            System.out.println("ERRORE: File audio non trovato -> " + fileName);
            fnfe.printStackTrace(); 
        } catch (IOException ioe) { 
            System.out.println("Errore di IO riproduzione musica di sottofondo.");
            ioe.printStackTrace(); 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close(); 
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