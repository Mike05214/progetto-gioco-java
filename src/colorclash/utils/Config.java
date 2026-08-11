package src.colorclash.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class Config {

    // costanti statiche
    private final static boolean IS_DIST_VERSION = false;

    // variabili statiche
    private static Config config = null;

    // variabili d'istanza
    private Properties properties;
    private String charset = "UTF-8";

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
                if (buffRead != null) {
                    buffRead.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }// fine costruttore

    // METODI STATICI

    public static Config getInstance() {
        if (config == null) {
            config = new Config();
        }
        return config;
        
    }// fine getInstance

    // METODI PRIVATI

    private String getConfigFile() throws URISyntaxException {
        String configFile = null;
        String relPath = "\\conf\\config.txt";

        if (System.getProperty("os.name").startsWith("Linux")) {
            relPath = "/conf/config.txt";
        }

        if (IS_DIST_VERSION) {
            configFile = getHomeFolderForDistVersion() + relPath;
        } else {
            configFile = getHomeFolderForDevVersion() + relPath;
        }
        return configFile;
    }// fine getConfigFile

    private String getHomeFolderForDistVersion() throws URISyntaxException {

        String homeDir = null;
        String jarPath = Config.class.getResource("Config.class").toURI().toString();
        int indexOfExclamationMark = jarPath.indexOf("!");

        String prefix = "jar:file:/";
        if (System.getProperty("os.name").startsWith("Linux")) {
            prefix = "jar:file:";
        }

        homeDir = jarPath.substring(prefix.length(), indexOfExclamationMark);
        int lastIndexOfSlash = homeDir.lastIndexOf("/");
        homeDir = homeDir.substring(0, lastIndexOfSlash);
        return homeDir;
    }//fine getHomeFolderForDistVersion

    private String getHomeFolderForDevVersion() throws URISyntaxException {
        return System.getProperty("user.dir"); 
    }

    // getters di Config

    public String getStringProperty(String key) {
        return properties.getProperty(key);
    }// fine getStringProperty

    public int getIntProperty(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            return Integer.valueOf(value.trim());
        }
        return 0;
    }// getIntProperty

    public Double getDoubleProperty(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            return Double.valueOf(value.trim());
        }
        return 0.0;
    }// fine getDoubleProperty

}// fine classe Config