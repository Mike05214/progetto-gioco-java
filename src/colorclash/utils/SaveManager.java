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
import java.util.List;

import src.colorclash.model.Avatar;
import src.colorclash.model.GameModel;
import src.colorclash.model.Obstacle;
import src.colorclash.model.SinusoidalMadness;
import src.colorclash.model.SpeedRacer;
import src.colorclash.model.StandardObstacle;

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
        int score = 0;
        BufferedReader buffRead = null;

        try {
            // Incapsulamento degli stream per la lettura dei caratteri
            buffRead = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(highscoreFilePath), charset)); // [cite: 7]

            String line = buffRead.readLine(); // [cite: 7]
            if (line != null && !line.isEmpty()) { // [cite: 7]
                score = Integer.parseInt(line.trim());
            }
        } catch (IOException ioe) { // [cite: 7]
            ioe.printStackTrace(); // [cite: 7]
        } finally {
            // Chiusura sicura dello stream
            try {
                if (buffRead != null)
                    buffRead.close(); // [cite: 7]
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
                                    new FileOutputStream(highscoreFilePath), charset)),
                    true); // [cite: 7]

            // Scriviamo il nuovo punteggio nel file
            printWriter.print(String.valueOf(newScore)); // [cite: 7]

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            // Chiusura sicura dello stream
            if (printWriter != null) {
                printWriter.close(); // [cite: 7]
            }
        }
    }

    public void writeGameState(int score, int lives, int phase, double speed, int avaibleColors, Avatar player, List<Obstacle> enemies) {
        String gameStatePath = "saves/gamestate.txt";
        PrintWriter printWriter = null;

        try {
            // Usiamo la stessa catena di flussi insegnata dal prof
            printWriter = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(gameStatePath), charset)),
                    true);

            // 1. Salviamo le variabili di base
            printWriter.println("SCORE:" + score);
            printWriter.println("LIVES:" + lives);
            printWriter.println("PHASE:" + phase);
            printWriter.println("CURRENT_SPEED:" + speed);
            printWriter.println("AVAIBLE_COLORS:" + avaibleColors);

            // 2. Salviamo il Player (X, Y, ID Colore)
            printWriter.println("PLAYER:" + player.getX() + "," + player.getY() + "," + player.getColorId());

            // Salviamo tutti i nemici presenti a schermo
            for (Obstacle obs : enemies) {
                String tipo = obs.getClass().getSimpleName();

                // Struttura: TIPO, X, Y, SPEED, COLOR_ID, WIDTH, HEIGHT
                printWriter.println("OBSTACLE:" + tipo + "," +
                        obs.getX() + "," +
                        obs.getY() + "," +
                        obs.getFallSpeed() + "," +
                        obs.getColorId() + "," +
                        obs.getWidth() + "," +
                        obs.getHeight());
            }

            System.out.println("Stato della partita congelato e salvato!");

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    public boolean loadGameState(GameModel model) {
        File file = new File("saves/gamestate.txt");

        // Se il file non esiste, significa che non c'è una partita da riprendere
        if (!file.exists()) {
            return false;
        }

        BufferedReader buffRead = null;
        try {
            // La catena di lettura del prof
            buffRead = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file), charset));

            String line;
            model.getEnemies().clear(); // Svuotiamo i nemici attuali prima di caricare i vecchi

            while ((line = buffRead.readLine()) != null) {

                // Analizziamo riga per riga in base alla "targhetta" che abbiamo messo
                if (line.startsWith("SCORE:")) {
                    int savedScore = Integer.parseInt(line.split(":")[1]); // legge i valori trasformando le stringhe in
                                                                           // int
                    model.setScore(savedScore); // Devi creare questo setter nel GameModel

                } else if (line.startsWith("LIVES:")) {
                    int savedLives = Integer.parseInt(line.split(":")[1]);// legge i valori trasformando le stringhe in
                                                                          // int
                    model.setLives(savedLives); // Devi creare questo setter nel GameModel

                } else if (line.startsWith("PHASE:")) {
                    int savedPhase = Integer.parseInt(line.split(":")[1]);// legge i valori trasformando le stringhe in
                                                                          // int
                    model.setPhase(savedPhase); // Devi creare questo setter nel GameModel

                } else if(line.startsWith("CURRENT_SPEED:")){
                    
                    double savedSpeed = Double.parseDouble(line.split(":")[1]);
                    model.setCurrentSpeed(savedSpeed);

                } else if (line.startsWith("AVAIBLE_COLORS:")){
                    int savedColors = Integer.parseInt(line.split(":")[1]);
                    model.setAvaibleColors(savedColors);
                    
                } else if (line.startsWith("PLAYER:")) {
                    String[] dati = line.split(":")[1].split(",");
                    model.getPlayer().setX(Integer.parseInt(dati[0]));
                    model.getPlayer().setY(Integer.parseInt(dati[1]));
                    model.getPlayer().setColorId(Integer.parseInt(dati[2]));

                } else if (line.startsWith("OBSTACLE:")) {
                    String[] dati = line.split(":")[1].split(",");

                    // Spacchettiamo l'array esattamente nell'ordine in cui l'abbiamo salvato
                    String tipo = dati[0];
                    int x = Integer.parseInt(dati[1]);
                    int y = Integer.parseInt(dati[2]);
                    double speed = Double.parseDouble(dati[3]); // <-- Attenzione: parseDouble per la velocità!
                    int colorId = Integer.parseInt(dati[4]);
                    int width = Integer.parseInt(dati[5]);
                    int height = Integer.parseInt(dati[6]);

                    // Ricreiamo l'ostacolo passando tutti i 6 parametri al costruttore
                    if (tipo.equals("StandardObstacle")) {
                        model.getEnemies().add(new StandardObstacle(x, y, speed, colorId, width, height));
                    } else if (tipo.equals("SpeedRacer")) {
                        model.getEnemies().add(new SpeedRacer(x, y, speed, colorId, width, height));
                    } else if (tipo.equals("SinusoidalMadness")) {
                        model.getEnemies().add(new SinusoidalMadness(x, y, speed, colorId, width, height));
                    }
                }
            }

            // Una volta caricato, eliminiamo il file per evitare che il giocatore
            // possa morire, chiudere forzatamente e ricaricare il vecchio salvataggio
            file.delete();
            return true;

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return false; // Errore nella lettura
        } finally {
            try {
                if (buffRead != null)
                    buffRead.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}