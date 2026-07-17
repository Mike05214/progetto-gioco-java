package src.colorclash.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class SaveManager {

    private String highscoreFilePath = "saves/highscore.txt";
    private String charset = "UTF-8";

    public SaveManager() {
        // 1. Crea la cartella e il file vuoto al primo avvio
        File cartellaSalvataggi = new File("saves");
        if (!cartellaSalvataggi.exists()) {
            cartellaSalvataggi.mkdirs();
        }

        File fileHighscore = new File(highscoreFilePath);
        if (!fileHighscore.exists()) {
            try {
                fileHighscore.createNewFile();
                // Scriviamo uno "0" iniziale se il file è appena stato creato
                writeHighscore(0); 
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // LETTURA DEL FILE (basato sull'Esercizio 1a del prof)
    public int getHighscore() {
        int score=0;
        BufferedReader buffRead = null;
        
        try {
            // Incapsulamento degli stream per la lettura dei caratteri
            buffRead = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(highscoreFilePath), charset)); //[cite: 7]
            
            String line = buffRead.readLine(); //[cite: 7]
            if (line != null && !line.isEmpty()) { //[cite: 7]
                score = Integer.parseInt(line.trim());
            }
        } 
        catch (IOException ioe) { //[cite: 7]
            ioe.printStackTrace(); //[cite: 7]
        } 
        finally {
            // Chiusura sicura dello stream
            try {
                if (buffRead != null) buffRead.close(); //[cite: 7]
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return score;
    }

    // SCRITTURA DEL FILE (basato sull'Esercizio 1c del prof)
    public void writeHighscore(int newScore) {
        PrintWriter printWriter = null;
        
        try {
            // Incapsulamento degli stream per la scrittura dei caratteri
            printWriter = new PrintWriter(
                new BufferedWriter(
                    new OutputStreamWriter(
                        new FileOutputStream(highscoreFilePath), charset)), true); //[cite: 7]
            
            // Scriviamo il nuovo punteggio nel file
            printWriter.print(String.valueOf(newScore)); //[cite: 7]
            
        } 
        catch (IOException ioe) {
            ioe.printStackTrace();
        } 
        finally {
            // Chiusura sicura dello stream
            if (printWriter != null) {
                printWriter.close(); //[cite: 7]
            }
        }
    }
}