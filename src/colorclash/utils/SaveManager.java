package colorclash.utils;

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

import java.net.URISyntaxException;

import java.util.List;

import colorclash.model.IPlayer;
import colorclash.model.IGameModel;
import colorclash.model.IObstacle;
import colorclash.model.Obstacle;
import colorclash.model.SinusoidalMadness;
import colorclash.model.SpeedRacer;
import colorclash.model.StandardObstacle;

public class SaveManager {
    // costanti
    private final static boolean IS_DIST_VERSION = false;

    // variabili di stato
    private String highscoreFilePath;
    private String gameStatePath;
    private String charset = "UTF-8";
    private static SaveManager saveManager = null;

    private SaveManager() {
        try {
            String savesDirPath = getSavesDirectory();

            File savesFolder = new File(savesDirPath);
            if (!savesFolder.exists()) {
                savesFolder.mkdirs();
            }

            boolean isLinux = System.getProperty("os.name").startsWith("Linux");
            highscoreFilePath = savesDirPath + (isLinux ? "/highscore.txt" : "\\highscore.txt");
            gameStatePath = savesDirPath + (isLinux ? "/gamestate.txt" : "\\gamestate.txt");

            File fileHighscore = new File(highscoreFilePath);
            if (!fileHighscore.exists()) {
                fileHighscore.createNewFile();
                writeHighscore(0);
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }// fine costruttore

    // METODI STATICO

    public static SaveManager getInstance() {
        if (saveManager == null) {
            saveManager = new SaveManager();
        }
        return saveManager;
    }// fine getInstance

    // METODI PUBBLICI

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
    }// fine getHighscore

    public void writeHighscore(int newScore) {
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(highscoreFilePath), charset)),
                    true);

            printWriter.print(String.valueOf(newScore));

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }// fine writeHighscore

    public void writeGameState(int score, int lives, int phase, double speed, int avaibleColors, IPlayer player,
            List<? extends IObstacle> enemies) {
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

            for (IObstacle obs : enemies) {
                if (obs.isActive()) {
                    printWriter.print("OBSTACLE:" + obs.getType() + ";" +
                            obs.getX() + ";" +
                            obs.getY() + ";" +
                            obs.getFallSpeed() + ";" +
                            obs.getColorId() + ";" +
                            obs.getWidth() + ";" +
                            obs.getHeight() + "\r\n");
                }
            }

            System.out.println("Game status frozen and saved in " + gameStatePath);

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }// fine writeGameState

    public void deleteGameState() {
        File file = new File(gameStatePath);
        if (file.exists()) {
            file.delete();
            System.out.println("Save file successfully deleted.");
        }
    }// fine deleteGameState

    public boolean loadGameState(IGameModel model) {
        File file = new File(gameStatePath);

        if (!file.exists()) {
            return false;
        }

        resetEnemyPool(model);

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(gameStatePath), charset));
            String line;

            while ((line = reader.readLine()) != null) {
                processSaveLine(line, model);
            }
            return true;

        } catch (FileNotFoundException fnfe) {
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
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // METODI PRIVATI

    private void resetEnemyPool(IGameModel model) {
        for (IObstacle iObs : model.getEnemies()) {
            Obstacle obs = (Obstacle) iObs;
            obs.setActive(false);
            obs.setY(-2000);
        }
    }

    private void processSaveLine(String line, IGameModel model) throws NumberFormatException {
        String[] parts = line.split(":");
        if (parts.length < 2)
            return;

        String key = parts[0];
        String value = parts[1];

        switch (key) {
            case "SCORE":
                model.setScore(Integer.valueOf(value));
                break;
            case "LIVES":
                model.setLives(Integer.valueOf(value));
                break;
            case "PHASE":
                model.setPhase(Integer.valueOf(value));
                break;
            case "CURRENT_SPEED":
                model.setCurrentSpeed(Double.valueOf(value));
                break;
            case "AVAIBLE_COLORS":
                model.setAvaibleColors(Integer.valueOf(value));
                break;
            case "PLAYER":
                loadPlayerData(value, model);
                break;
            case "OBSTACLE":
                loadObstacleData(value, model);
                break;
        }
    }

    private void loadPlayerData(String data, IGameModel model) throws NumberFormatException {
        String[] playerData = data.split(";");
        colorclash.model.Player p = (colorclash.model.Player) model.getPlayer();
        p.setX(Double.valueOf(playerData[0]));
        p.setY(Double.valueOf(playerData[1]));
        p.setColorId(Integer.valueOf(playerData[2]));
        p.getHitbox().updatePosition(p.getX(), p.getY());
    }

    private void loadObstacleData(String data, IGameModel model) throws NumberFormatException {
        String[] obsData = data.split(";");

        String type = obsData[0];
        double x = Double.valueOf(obsData[1]);
        double y = Double.valueOf(obsData[2]);
        double speed = Double.valueOf(obsData[3]);
        int colorId = Integer.valueOf(obsData[4]);
        int width = Integer.valueOf(obsData[5]);
        int height = Integer.valueOf(obsData[6]);

        for (IObstacle iObs : model.getEnemies()) {
            Obstacle obs = (Obstacle) iObs;
            
            if (!obs.isActive()) {
                boolean isMatchingType = (type.equals("StandardObstacle") && obs instanceof StandardObstacle) ||
                        (type.equals("SpeedRacer") && obs instanceof SpeedRacer) ||
                        (type.equals("SinusoidalMadness") && obs instanceof SinusoidalMadness);

                if (isMatchingType) {
                    obs.setX(x);
                    obs.setY(y);
                    obs.setFallSpeed(speed);
                    obs.setColorId(colorId);
                    obs.setWidth(width);
                    obs.setHeight(height);

                    obs.updateHitbox();

                    if (obs instanceof SinusoidalMadness) {
                        ((SinusoidalMadness) obs).setStartX(x);
                    }

                    obs.setActive(true);
                    break;
                }
            }
        }
    }

    private String getSavesDirectory() throws URISyntaxException {
        String savesDir = null;
        String relPath = "\\saves";

        if (System.getProperty("os.name").startsWith("Linux")) {
            relPath = "/saves";
        }

        if (IS_DIST_VERSION) {
            savesDir = getHomeFolderForDistVersion() + relPath;
        } else {
            savesDir = getHomeFolderForDevVersion() + relPath;
        }
        return savesDir;
    }// fine getSaveDirectory

    private String getHomeFolderForDistVersion() throws URISyntaxException {
        String homeDir = null;
        String jarPath = SaveManager.class.getResource("SaveManager.class").toURI().toString();
        int indexOfExclamationMark = jarPath.indexOf("!");

        String prefix = "jar:file:/";
        if (System.getProperty("os.name").startsWith("Linux")) {
            prefix = "jar:file:";
        }

        homeDir = jarPath.substring(prefix.length(), indexOfExclamationMark);
        int lastIndexOfSlash = homeDir.lastIndexOf("/");
        homeDir = homeDir.substring(0, lastIndexOfSlash);
        return homeDir;
    }// fine getHomeFolderForDistVersion

    private String getHomeFolderForDevVersion() throws URISyntaxException {
        return System.getProperty("user.dir");
    }// fine getHomeFolderForDevVersion

}// fine classe SaveManager