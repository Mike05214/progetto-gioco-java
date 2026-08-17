package colorclash.model;

import java.util.List;

public interface IGameModel {
    
    // Metodi di aggiornamento e gestione stato
    void update(int panelWidth, int panelHeight);
    void resetGame();
    void autoSave();
    void decreaseLives();

    // Getters di stato del gioco
    boolean isInvulnerable();
    int getInvulnTimer();
    boolean isGameOver();
    boolean isPlayerDead();
    int getScore();
    int getLives();
    int getPhase();
    double getCurrentSpeed();
    int getAvailableColorsCount();
    int getHighscore();

    // Getters per il setup iniziale
    double getStartX();
    double getStartY();
    int getStartColorId();

    // Getters degli oggetti di gioco
    Player getPlayer();
    List<Obstacle> getEnemies();
    List<Particle> getParticles();
    List<FloatingScore> getFloatingScores();
    List<Star> getStars();

    // Setters usati dal SaveManager per ripristinare la partita
    void setScore(int score);
    void setLives(int lives);
    void setPhase(int phase);
    void setCurrentSpeed(double currentSpeed);
    void setAvaibleColors(int avaibleColors);
}