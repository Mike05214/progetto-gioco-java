package src.colorclash.utils;

import javax.sound.sampled.AudioSystem; //DOC: The AudioSystem class acts as the entry point to the sampled-audio system resources.
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip; //DOC: The Clip interface represents a special kind of data line whose audio data can be loaded prior to playback, instead of being streamed in real time.
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.HashMap;

public class AudioManager {

    //variabili di stato
    private static AudioManager instance;
    private Clip backgroundMusic;
    private boolean isMuted = false;

    //HashMap per gestione audio
    private HashMap<String, Clip[]> soundPool = new HashMap<>();
    private HashMap<String, Integer> soundIndexes = new HashMap<>();

    // costruttore
    private AudioManager() {}

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }// fine getInstance

    public void preloadSoundEffect(String fileName, int numClips) {
        Clip[] clips = new Clip[numClips];
        
        for (int i = 0; i < numClips; i++) {
            InputStream is = null;
            AudioInputStream ais = null;
            
            try {
                is = AudioManager.class.getResourceAsStream("/colorclash/sounds/" + fileName);
                if (is == null) {
                    is = AudioManager.class.getResourceAsStream("/src/colorclash/sounds/" + fileName);
                }
                if (is == null) {
                    throw new FileNotFoundException("ERROR: Audio file not found -> " + fileName);
                }

                ais = AudioSystem.getAudioInputStream(is);
                
                // DOC: Obtains the audio format of the sound data in this audio input stream.
                AudioFormat af = ais.getFormat();
                
                // DOC: getFrameLength() obtains the length of the stream, expressed in sample frames rather than bytes. getFrameSize() obtains the frame size in bytes.
                int bufferSize = (int)ais.getFrameLength() * af.getFrameSize();
                
                // DOC: Constructs a data line's info object from the specified information, which includes a single audio format and a desired buffer size.
                DataLine.Info info = new DataLine.Info(Clip.class, af, bufferSize);
                
                // Gestione rigorosa del supporto linea come da dispensa
                if (!AudioSystem.isLineSupported(info)) {
                    throw new IOException("Error: the AudioSystem does not support the specified DataLine.Info object"); 
                }

                try {
                    // DOC: Obtains a line that matches the description in the specified Line.Info object.
                    clips[i] = (Clip)AudioSystem.getLine(info);
                    // DOC: Opens the clip with the format and audio data present in the provided audio input stream.
                    clips[i].open(ais);
                } catch (LineUnavailableException lue) {
                    throw new IOException("Error: a LineUnavailableException exception was thrown"); 
                }
                
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            } catch (UnsupportedAudioFileException uafe) {
                uafe.printStackTrace(); 
            } catch (IOException ioe) {
                ioe.printStackTrace(); 
            } finally {
                // DOC: Closes the line, indicating that any system resources in use by the line can be released.
                try {
                    if (ais != null) {
                        ais.close(); 
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        
        soundPool.put(fileName, clips);
        soundIndexes.put(fileName, 0);
    } // fine preloadSoundEffect

    public void playSoundEffect(String fileName) {
        if (isMuted) return;

        if (soundPool.containsKey(fileName)) {
            Clip[] clips = soundPool.get(fileName);
            int index = soundIndexes.get(fileName);
            
            if (clips[index] != null) {
                clips[index].stop();
                clips[index].setFramePosition(0); 
                // DOC: Allows a line to engage in data I/O.
                clips[index].start();
            }

            index = (index + 1) % clips.length;
            soundIndexes.put(fileName, index);
        } else {
            System.err.println("WARNING: Sound not preloaded in memory -> " + fileName);
        }
    }// fine playSoundEffect

    public void playBackgroundMusic(String fileName) {
        if (isMuted) return;

        InputStream is = null;
        AudioInputStream ais = null;

        try {
            is = AudioManager.class.getResourceAsStream("/colorclash/sounds/" + fileName);
            if (is == null) {
                is = AudioManager.class.getResourceAsStream("/src/colorclash/sounds/" + fileName);
            }
            if (is == null) {
                throw new FileNotFoundException("ERROR: Audio file not found -> " + fileName);
            }

            ais = AudioSystem.getAudioInputStream(is);
            AudioFormat af = ais.getFormat();
            int bufferSize = (int)ais.getFrameLength() * af.getFrameSize();
            DataLine.Info info = new DataLine.Info(Clip.class, af, bufferSize);
        
            if (!AudioSystem.isLineSupported(info)) {
                throw new IOException("Error: the AudioSystem does not support the specified DataLine.Info object"); 
            }

            try {
                backgroundMusic = (Clip)AudioSystem.getLine(info);
                backgroundMusic.open(ais);
            } catch (LineUnavailableException lue) {
                throw new IOException("Error: a LineUnavailableException exception was thrown");
            }
            
            // DOC: A value of LOOP_CONTINUOUSLY indicates that looping should continue until interrupted.
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); 

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (UnsupportedAudioFileException uafe) {
            uafe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (ais != null) {
                    ais.close(); 
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }//fine playBackgroundMusic

    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            // DOC: Stops the line. A stopped line should cease I/O activity.
            backgroundMusic.stop(); 
        }
    }// fine stopBackgroundMusic

    public void toggleSound() {
        isMuted = !isMuted;
        if (isMuted) stopBackgroundMusic();
    }// fine toggleSound

    public boolean isSoundEnabled() {
        return !isMuted;
    }// fine isSoundEnabled

}// fine classe AudioManager