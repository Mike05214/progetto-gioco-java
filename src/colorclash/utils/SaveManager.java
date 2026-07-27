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
    private String gameStatePath = "saves/gamestate.txt";
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
        File savesFolder = new File("saves");
        if (!savesFolder.exists()) {
            savesFolder.mkdirs();
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
                score = Integer.valueOf(line.trim());
            }
        } catch (FileNotFoundException fnfe) { 
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
        PrintWriter printWriter = null;

        try {
            printWriter = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(gameStatePath), charset)),
                    true);

            printWriter.print("SCORE:" + score + "\r\n");
            printWriter.print("LIVES:" + lives + "\r\n");
            printWriter.print("PHASE:" + phase + "\r\n");
            printWriter.print("CURRENT_SPEED:" + speed + "\r\n");
            printWriter.print("AVAIBLE_COLORS:" + avaibleColors + "\r\n");
            printWriter.print("PLAYER:" + player.getX() + ";" + player.getY() + ";" + player.getColorId() + "\r\n");

            for (Obstacle obs : enemies) {

                printWriter.print("OBSTACLE:" + obs.getType() + ";" +
                        obs.getX() + ";" +
                        obs.getY() + ";" +
                        obs.getFallSpeed() + ";" +
                        obs.getColorId() + ";" +
                        obs.getWidth() + ";" +
                        obs.getHeight() + "\r\n");
            }

            System.out.println("Game status frozen and saved");

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
                    String[] playerData = line.split(":")[1].split(";"); 
                    model.getPlayer().setX(Double.valueOf(playerData[0]));
                    model.getPlayer().setY(Double.valueOf(playerData[1]));
                    model.getPlayer().setColorId(Integer.valueOf(playerData[2]));

                } else if (line.startsWith("OBSTACLE:")) {
                    String[] obsData = line.split(":")[1].split(";"); 

                    String type = obsData[0];
                    double x = Double.valueOf(obsData[1]);
                    double y = Double.valueOf(obsData[2]);
                    double speed = Double.valueOf(obsData[3]); 
                    int colorId = Integer.valueOf(obsData[4]);
                    int width = Integer.valueOf(obsData[5]);
                    int height = Integer.valueOf(obsData[6]);

                    if (type.equals("StandardObstacle")) {
                        model.getEnemies().add(new StandardObstacle(x, y, speed, colorId, width, height));
                    } else if (type.equals("SpeedRacer")) {
                        model.getEnemies().add(new SpeedRacer(x, y, speed, colorId, width, height));
                    } else if (type.equals("SinusoidalMadness")) {
                        model.getEnemies().add(new SinusoidalMadness(x, y, speed, colorId, width, height));
                    }
                }
            }
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