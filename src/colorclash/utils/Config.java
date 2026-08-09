package src.colorclash.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class Config {

    // COSTANTI
    private final static boolean IS_DIST_VERSION = false; // Metti a true prima di fare il .jar per l'esame

    // CAMPI STATICI
    private static Config config = null;

    // CAMPI ISTANZA
    private Properties properties;
    private String charset = "UTF-8";

    // METODI STATICI
    public static Config getInstance() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    private Config() {
        BufferedReader buffRead = null;
        try {
            String configFile = getConfigFile();

            buffRead = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(configFile), charset));

            properties = new Properties();
            properties.load(buffRead);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            System.exit(-1);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            try {
                if (buffRead != null){
                    buffRead.close();
                }    
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    // METODI PRIVATI
    private String getConfigFile() throws URISyntaxException {
        String configFile = null;
        String relPath = "\\conf\\config.txt"; // il  \\ viene convertito in / da java 

        if (System.getProperty("os.name").startsWith("Linux")) {
            relPath = "/conf/config.txt";
        }

        if (IS_DIST_VERSION) {
            configFile = getHomeFolderForDistVersion() + relPath; //C:/percorso/del/gioco/conf/config.txt
        } else {
            configFile = getHomeFolderForDevVersion() + relPath;
        }
        return configFile;
    }

    private String getHomeFolderForDistVersion() throws URISyntaxException {

        String homeDir = null;
        String jarPath = Config.class.getResource("Config.class").toURI().toString();//jar:file:/C:/percorso/del/gioco/ColorClash.jar!/colorclash/utils/Config.class
        int indexOfExclamationMark = jarPath.indexOf("!");

        String prefix = "jar:file:/";
        if (System.getProperty("os.name").startsWith("Linux")) {
            prefix = "jar:file:";
        }

        homeDir = jarPath.substring(prefix.length(), indexOfExclamationMark);// si ottiene C:/percorso/del/gioco/ColorClash.jar
        int lastIndexOfSlash = homeDir.lastIndexOf("/");
        homeDir = homeDir.substring(0, lastIndexOfSlash);//resta è esattamente C:/percorso/del/gioco, ovvero la cartella esterna in cui l'utente ha posizionato il videogioco.
        return homeDir;
    }

    private String getHomeFolderForDevVersion() throws URISyntaxException {
        return System.getProperty("user.dir"); //restituisce automaticamente la cartella radice del progetto aperta nell'IDE.
    }

    // GETTERS
    public String getStringProperty(String key) {
        return properties.getProperty(key);
    }

    public int getIntProperty(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            return Integer.valueOf(value.trim());
        }
        return 0;
    }

    public Double getDoubleProperty(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            return Double.valueOf(value.trim());
        }
        return 0.0;
    }

}