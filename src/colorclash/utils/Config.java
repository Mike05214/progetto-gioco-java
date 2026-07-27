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
            // Caricamento del file tramite percorso calcolato a run-time
            buffRead = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(getConfigFileFullPath()), "ISO-8859-1"));

            this.properties = new Properties();
            this.properties.load(buffRead);
        } catch (FileNotFoundException fnfe) {
            JOptionPane.showMessageDialog(null,
                    "Configuration file not found, the program will be closed.",
                    "Serious ERROR",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null,
                    "Unable to read the configuration file, the program will be closed.",
                    "Serious ERROR",
                    JOptionPane.ERROR_MESSAGE);
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

        if (fileName.contains("//"))
            fileName = fileName.substring("file:/".length()); // Versione Windows
        else if (fileName.contains("/"))
            fileName = fileName.substring("file:".length()); // Versione Linux

        fileName = fileName.replaceAll("%20", " ");
        return fileName;
    }

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