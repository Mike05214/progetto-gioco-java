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

    // Costruttore privato (Design Pattern Singleton)
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

    // METODI ISTANZA (Esempio per leggere un parametro)
    public String getBackgroundColor() {
        return this.properties.getProperty("backgroundColor");
    }

    public String getStringProperty(String key) {
        // Restituisce il valore associato alla chiave, oppure null se non esiste
        return properties.getProperty(key);
    }

    // (Opzionale) Metodo helper per prendere direttamente gli interi e non dover
    // fare
    // Integer.parseInt(...) ogni singola volta in giro per le altre classi
    public int getIntProperty(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            return Integer.parseInt(value.trim()); // trim() toglie eventuali spazi vuoti
        }
        return 0; // Valore di default se la chiave non esiste
    }

    public Double getDoubleProperty(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            return Double.parseDouble(value.trim()); // trim() toglie eventuali spazi vuoti
        }
        return 0.0; // Valore di default se la chiave non esiste
    }

    // METODI STATICI
    public static Config getInstance() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

} // end class