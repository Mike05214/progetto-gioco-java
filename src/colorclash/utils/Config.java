package src.colorclash.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Properties;

import javax.swing.JOptionPane;

public class Config {

    // CAMPI STATICI
    private static Config config = null;

    // CAMPI ISTANZA
    private Properties properties;

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
            buffRead = new BufferedReader(
                        new InputStreamReader(
                        new FileInputStream(getConfigFileFullPath()), "UTF-8"));

            properties = new Properties();
            properties.load(buffRead);//DOC: Reads a property list (key and element pairs) from the input character stream in a simple line-oriented format.
        } catch (FileNotFoundException fnfe) {
            System.out.println("Configuration file not found, the program will be closed.");
            System.exit(-1);
        } catch (IOException ioe) {
            System.out.println("Unable to read the configuration file, the program will be closed.");
            System.exit(-1);
        } finally {
            try {
                if (buffRead != null)
                    buffRead.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } // end finally
    } // end constructor

    // METODI PRIVATI
    private String getConfigFileFullPath() {
        String fileName = Config.class.getResource("/conf/config.txt").toString();

        if (fileName.contains("//")){
            fileName = fileName.substring("file:/".length()); // Versione Windows
        }else if (fileName.contains("/")){
            fileName = fileName.substring("file:".length()); // Versione Linux
            fileName = fileName.replaceAll("%20", " ");
        }
        return fileName;
    }// fine getConfigFileFullPath

    //GETTERS

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

    

} // end class