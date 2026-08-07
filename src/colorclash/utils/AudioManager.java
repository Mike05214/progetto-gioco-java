package src.colorclash.utils;


import javax.sound.sampled.AudioSystem;//DOC: The AudioSystem class acts as the entry point to the sampled-audio system resources. 
                                       // This class lets you query and access the mixers that are installed on the system.
import javax.sound.sampled.AudioInputStream;                                       
import javax.sound.sampled.Clip;//The Clip interface represents a special kind of data line whose audio data can be loaded prior to playback,
                                // instead of being streamed in real time; Because the data is pre-loaded and has a known length,
                                // you can set a clip to start playing at any position in its audio data.
import javax.sound.sampled.LineListener;//Instances of classes that implement the LineListener interface can register to receive events when a line's status changes.
import javax.sound.sampled.LineEvent;

import java.io.ByteArrayInputStream;//A ByteArrayInputStream contains an internal buffer that contains bytes that may be read from the stream.
                                    // An internal counter keeps track of the next byte to be supplied by the read method.
import java.io.InputStream;
import java.io.IOException;

public class AudioManager {

    private static AudioManager instance;
    private Clip backgroundMusic;
    private boolean isMuted = false;

    // costruttore
    private AudioManager() {
    }

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
        AudioInputStream audioIn = null;

        try {
            // Carica il file audio direttamente come flusso di byte 

            // 1. Prova il percorso standard (funzionerà nel .jar)
            is = AudioManager.class.getResourceAsStream("/colorclash/sounds/" + fileName);

            // 2. Se fallisce (come su VSCode), prova il percorso fisico DEV
            if (is == null) {
                is = AudioManager.class.getResourceAsStream("/src/colorclash/sounds/" + fileName);
            }

            if (is == null) {
                System.out.println("ERROR: Audio file not found -> " + fileName);
                return;
            }

            byte[] audioData = is.readAllBytes();
            audioIn = AudioSystem.getAudioInputStream(new ByteArrayInputStream(audioData));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);//DOC: Opens the clip with the format and audio data present in the provided audio input stream.
            clip.start();

            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                }
            });

        } catch (Exception e) {
            System.out.println("Error playing sound effect: " + fileName);
            e.printStackTrace();
        } finally {
            try {
                if (is != null){
                    is.close();
                }  
                if (audioIn != null){
                    audioIn.close();
                } 
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
        AudioInputStream audioIn = null;

        try {
            // Carica il file audio direttamente come flusso di byte
            is = AudioManager.class.getResourceAsStream("/colorclash/sounds/" + fileName);
            if (is == null) {
                is = AudioManager.class.getResourceAsStream("/src/colorclash/sounds/" + fileName);
            }
            if (is == null) {
                System.out.println("ERROR: Audio file not found -> " + fileName);
                return;
            }

            byte[] audioData = is.readAllBytes();
            audioIn = AudioSystem.getAudioInputStream(new ByteArrayInputStream(audioData));

            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioIn);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);//DOC:LOOP_CONTINUOUSLY to indicate that looping should continue until interrupted

        } catch (Exception e) {
            System.out.println("Error playing background music: " + fileName);
            e.printStackTrace();
        } finally {
            try {
                if (is != null){
                    is.close();
                }
                if (audioIn != null){
                    audioIn.close();
                }      
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