package src.colorclash.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import src.colorclash.model.Player;
import src.colorclash.model.GameModel;
import src.colorclash.model.Obstacle;
import src.colorclash.model.SinusoidalMadness;
import src.colorclash.model.SpeedRacer;
import src.colorclash.model.StandardObstacle;

public class SaveManager {

    private String highscoreFilePath = "saves/highscore.txt";
    private String charset = "UTF-8";
    private static SaveManager saveManager = null;

    public static SaveManager getInstance() {
        if (saveManager == null) {
            saveManager = new SaveManager();
        }
        return saveManager;
    }

    public SaveManager() {
        // MANTENUTO: Crea la cartella e il file vuoto al primo avvio
        File SavesFolder = new File("saves");
        if (!SavesFolder.exists()) {
            SavesFolder.mkdirs();
        }

        File fileHighscore = new File(highscoreFilePath);
        if (!fileHighscore.exists()) {
            try {
                fileHighscore.createNewFile();
                writeHighscore(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getHighscore() {
        int score = 0;
        BufferedReader buffRead = null;

        try {
            buffRead = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(highscoreFilePath), charset));

            String line = buffRead.readLine();
            if (line != null && !line.isEmpty()) {
                // MODIFICATO: Stile del prof (valueOf)
                score = Integer.valueOf(line.trim());
            }
        } catch (FileNotFoundException fnfe) { // MODIFICATO: catch separati come il prof
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (buffRead != null)
                    buffRead.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return score;
    }

    public void writeHighscore(int newScore) {
        PrintWriter printWriter = null;

        try {
            printWriter = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(highscoreFilePath), charset)),
                    true);

            printWriter.print(String.valueOf(newScore));

        } catch (FileNotFoundException fnfe) { // MODIFICATO: catch separati
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    public void writeGameState(int score, int lives, int phase, double speed, int avaibleColors, Player player, List<Obstacle> enemies) {
        String gameStatePath = "saves/gamestate.txt";
        PrintWriter printWriter = null;

        try {
            printWriter = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(gameStatePath), charset)),
                    true);

            // MODIFICATO: Uso di print() + \r\n come fa il professore per i ritorni a capo
            printWriter.print("SCORE:" + score + "\r\n");
            printWriter.print("LIVES:" + lives + "\r\n");
            printWriter.print("PHASE:" + phase + "\r\n");
            printWriter.print("CURRENT_SPEED:" + speed + "\r\n");
            printWriter.print("AVAIBLE_COLORS:" + avaibleColors + "\r\n");

            // MODIFICATO: Uso del punto e virgola ; come separatore di token
            printWriter.print("PLAYER:" + player.getX() + ";" + player.getY() + ";" + player.getColorId() + "\r\n");

            for (Obstacle obs : enemies) {
                String tipo = obs.getClass().getSimpleName();

                printWriter.print("OBSTACLE:" + tipo + ";" +
                        obs.getX() + ";" +
                        obs.getY() + ";" +
                        obs.getFallSpeed() + ";" +
                        obs.getColorId() + ";" +
                        obs.getWidth() + ";" +
                        obs.getHeight() + "\r\n");
            }

            System.out.println("Stato della partita congelato e salvato!");

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    public boolean loadGameState(GameModel model) {
        String gameStatePath = "saves/gamestate.txt";
        File file = new File(gameStatePath);

        // MANTENUTO
        if (!file.exists()) {
            return false;
        }

        BufferedReader buffRead = null;
        try {
            buffRead = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(gameStatePath), charset));

            // MODIFICATO: dichiarazione var a null prima del while (come il prof)
            String line = null; 
            model.getEnemies().clear(); 

            while ((line = buffRead.readLine()) != null) {

                if (line.startsWith("SCORE:")) {
                    // MODIFICATO: valueOf
                    int savedScore = Integer.valueOf(line.split(":")[1]); 
                    model.setScore(savedScore);

                } else if (line.startsWith("LIVES:")) {
                    int savedLives = Integer.valueOf(line.split(":")[1]);
                    model.setLives(savedLives);

                } else if (line.startsWith("PHASE:")) {
                    int savedPhase = Integer.valueOf(line.split(":")[1]);
                    model.setPhase(savedPhase);

                } else if(line.startsWith("CURRENT_SPEED:")){
                    double savedSpeed = Double.valueOf(line.split(":")[1]); 
                    model.setCurrentSpeed(savedSpeed);

                } else if (line.startsWith("AVAIBLE_COLORS:")){
                    int savedColors = Integer.valueOf(line.split(":")[1]);
                    model.setAvaibleColors(savedColors);
                    
                } else if (line.startsWith("PLAYER:")) {
                    // MODIFICATO: Recupero con il ;
                    String[] dati = line.split(":")[1].split(";"); 
                    model.getPlayer().setX(Double.valueOf(dati[0]));
                    model.getPlayer().setY(Double.valueOf(dati[1]));
                    model.getPlayer().setColorId(Integer.valueOf(dati[2]));

                } else if (line.startsWith("OBSTACLE:")) {
                    String[] dati = line.split(":")[1].split(";"); 

                    String tipo = dati[0];
                    double x = Double.valueOf(dati[1]);
                    double y = Double.valueOf(dati[2]);
                    double speed = Double.valueOf(dati[3]); 
                    int colorId = Integer.valueOf(dati[4]);
                    int width = Integer.valueOf(dati[5]);
                    int height = Integer.valueOf(dati[6]);

                    if (tipo.equals("StandardObstacle")) {
                        model.getEnemies().add(new StandardObstacle(x, y, speed, colorId, width, height));
                    } else if (tipo.equals("SpeedRacer")) {
                        model.getEnemies().add(new SpeedRacer(x, y, speed, colorId, width, height));
                    } else if (tipo.equals("SinusoidalMadness")) {
                        model.getEnemies().add(new SinusoidalMadness(x, y, speed, colorId, width, height));
                    }
                }
            }

            // MANTENUTO
            file.delete();
            return true;

        } catch (FileNotFoundException fnfe) { // MODIFICATO: Divisione precisa delle eccezioni
            fnfe.printStackTrace();
            return false;
        } catch (IOException ioe) { 
            ioe.printStackTrace();
            return false; 
        } catch (NumberFormatException nfe) { 
            nfe.printStackTrace();
            return false;
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