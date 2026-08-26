package colorclash.model;

import colorclash.utils.SaveManager;
import colorclash.utils.AudioManager;
import colorclash.utils.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameModel implements IGameModel {

    // costanti
    private final double START_X = Config.getInstance().getDoubleProperty("player_start_x");
    private final double START_Y = Config.getInstance().getDoubleProperty("player_start_y");
    private final int MIN_X = 0; // relative al constrainX e constrainY
    private final int MIN_Y = 0;
    private final int OBSTACLE_START_Y = -150;
    private final int START_COLOR_ID = 0;
    private final double BASE_SPEED = Config.getInstance().getDoubleProperty("obstacle_base_speed");
    private final int TICK_TIME = 8;
    private final int DEATH_DELAY_MS =1500;
    private final int SCORE_DELAY_MS = Config.getInstance().getIntProperty("score_delay_ms");
    private final int SCORE_PHASE_2 = Config.getInstance().getIntProperty("phase_2_score_threshold");
    private final int SCORE_PHASE_3 = Config.getInstance().getIntProperty("phase_3_score_threshold");
    private final double SPEED_PHASE_MULTIPLIER = Config.getInstance().getDoubleProperty("speed_phase_multiplier");
    private final double SPEEDRACER_MULTIPLIER = Config.getInstance().getDoubleProperty("speed_racer_multiplier");
    private final int SPAWN_INTERVAL_MS = Config.getInstance().getIntProperty("obstacle_spawn_interval_ms");
    private final int MAX_INVULN_TIME_MS = 1000;
    private final int DEFAULT_SCORE_GAIN = 100;
    private final int MAX_CHANCE = 100;
    private final int SPEEDRACER_CHANCE_PHASE_2 = 30;
    private final int SPEEDRACER_CHANCE_PHASE_3 = 40;
    private final int SINUSOIDALMADNESS_CHANCE = 15;
    private final int MAX_PARTICLES = 200;
    private final int EXPLOSION_OFFSET = 20; // per alzare il centro esplosione
    private final int MAX_LIVES = 3;
    private final int NUM_STARS = 100;
    private final int POOL_SIZE_STANDARD = 15;
    private final int POOL_SIZE_SPECIAL = 10;
    private final int DEFAULT_POSITION = -2000;

    // Variabili di stato del gioco
    private Player player;
    private int lives;
    private boolean isGameOver;
    private boolean isInvulnerable = false;
    private boolean isPlayerDead = false;
    private int score;

    private int deathTimer = 0;
    private int scoreTimer = 0;
    private int invulnTimer = 0;
    private int spawnTimer = 0;
    private Random random;
    private double currentFallSpeed;
    private int currentPhase = 1;
    private int availableColorsCount = 2;
    private List<Obstacle> allEnemies;
    private List<Particle> allParticles = new ArrayList<>();
    private List<FloatingScore> floatingScores = new ArrayList<>();
    private List<Star> stars = new ArrayList<>();
    private StandardObstacle[] standardPool = new StandardObstacle[POOL_SIZE_STANDARD];
    private SpeedRacer[] speedRacerPool = new SpeedRacer[POOL_SIZE_SPECIAL];
    private SinusoidalMadness[] sinusoidalPool = new SinusoidalMadness[POOL_SIZE_SPECIAL];
    private SaveManager saveManager;

    // variabili statiche
    private static GameModel model = null;

    private GameModel() {
        initGame();
    }// fine costruttore

    // METODI STATICI

    public static GameModel getInstance() {
        if (model == null) {
            model = new GameModel();
        }
        return model;
    }

    // METODI PRIVATI

    private void initGame() {
        allEnemies = new ArrayList<>();
        initObstaclesPool();
        initParticlesPool();
        player = new Player(START_X, START_Y, START_COLOR_ID);
        lives = MAX_LIVES;
        isGameOver = false;
        currentFallSpeed = BASE_SPEED;
        random = new Random();
        saveManager = SaveManager.getInstance();
        initStars(Config.getInstance().getIntProperty("frame_width"),
                Config.getInstance().getIntProperty("frame_height"));
    }// fine initGame

    private void initObstaclesPool() {
        for (int i = 0; i < standardPool.length; i++) {
            standardPool[i] = new StandardObstacle();
            allEnemies.add(standardPool[i]);
        }
        for (int i = 0; i < speedRacerPool.length; i++) {
            speedRacerPool[i] = new SpeedRacer();
            allEnemies.add(speedRacerPool[i]);
        }
        for (int i = 0; i < sinusoidalPool.length; i++) {
            sinusoidalPool[i] = new SinusoidalMadness();
            allEnemies.add(sinusoidalPool[i]);
        }
    }// fine innitObstaclesPool

    private void initParticlesPool() {
        allParticles.clear();
        for (int i = 0; i < MAX_PARTICLES; i++) {
            Particle p = new Particle();
            p.setActive(false);
            allParticles.add(p);
        }
    }// fine initParticlesPool

    private void initStars(int panelWidth, int panelHeight) {
        for (int i = 0; i < NUM_STARS; i++) {
            stars.add(new Star(panelWidth, panelHeight));
        }
    }// fine initStars

    private void updateEnemies(int panelHeight) {
        for (Obstacle obs : allEnemies) {
            if (obs.isActive()) {
                obs.update();
                obs.checkOffScreen(panelHeight);
            }
        }
    }// fine updateEnemies

    private void updateParticles() {
        for (Particle p : allParticles) {
            if (p.isActive()) {
                p.update();
            }
        }
    }// fine updateParticles

    private void updateFloatingScore() {
        for (FloatingScore fs : floatingScores) {
            fs.update();
        }
        floatingScores.removeIf(fs -> fs.isDead());
    }// fine updateFloatingScore

    private void updateStars(int panelWidth, int panelHeight) {
        if (stars != null) {
            for (Star s : stars) {
                s.update(panelWidth, panelHeight);
            }
        }
    }// fine updateStarts

    private void invulnerabilityHandler() {
        if (isInvulnerable) {
            invulnTimer += TICK_TIME;

            if (invulnTimer >= MAX_INVULN_TIME_MS) {
                isInvulnerable = false;
                invulnTimer = 0;
            }
        }
    }// fine invulnerabilityHandler

    private void spawningHandler(int panelWidth) {
        spawnTimer += TICK_TIME;

        if (spawnTimer >= SPAWN_INTERVAL_MS) {
            spawnRandomEnemy(panelWidth);
            spawnTimer -= SPAWN_INTERVAL_MS;
        }
    }// fine handleSpawning

    private void spawnStandard(StandardObstacle obs, int panelWidth, double startY, double speed, int colorId) {
        int randomWidth = random.nextInt(StandardObstacle.MIN_SIZE, StandardObstacle.MAX_SIZE);
        int randomHeight = random.nextInt(StandardObstacle.MIN_SIZE, StandardObstacle.MAX_SIZE);
        double randomX = random.nextDouble(panelWidth - randomWidth);

        obs.setX(randomX);
        obs.setY(startY);
        obs.setFallSpeed(speed);
        obs.setColorId(colorId);
        obs.setWidth(randomWidth);
        obs.setHeight(randomHeight);
        obs.updateHitbox();

        obs.setActive(true);
    }// fine spawnStandard

    private void spawnSpeedRacer(SpeedRacer obs, int panelWidth, double startY, double speed, int colorId) {
        double randomX = random.nextDouble(panelWidth - SpeedRacer.WIDTH);

        obs.setX(randomX);
        obs.setY(startY);
        obs.setFallSpeed(speed);
        obs.setColorId(colorId);
        obs.setWidth(SpeedRacer.WIDTH);
        obs.setHeight(SpeedRacer.HEIGHT);

        obs.setActive(true);
    }// fine spawnSpeedRacer

    private void spawnSinusoidal(SinusoidalMadness obs, int panelWidth, double startY, double speed, int colorId) {
        double safeMinX = SinusoidalMadness.AMPLITUDE;
        double safeMaxX = panelWidth - SinusoidalMadness.WIDTH - SinusoidalMadness.AMPLITUDE;
        double randomX = random.nextDouble(safeMinX, safeMaxX);

        obs.setX(randomX);
        obs.setStartX(randomX);
        obs.setY(startY);
        obs.setFallSpeed(speed);
        obs.setColorId(colorId);
        obs.setWidth(SinusoidalMadness.WIDTH);
        obs.setHeight(SinusoidalMadness.HEIGHT);

        obs.setActive(true);
    }// fine spawnSinusoidal

    private void activateFromPool(Obstacle[] pool, int panelWidth, double startY, double speed, int colorId) {
        for (int i = 0; i < pool.length; i++) {
            if (!pool[i].isActive()) {
                if (pool[i] instanceof StandardObstacle) {
                    spawnStandard((StandardObstacle) pool[i], panelWidth, startY, speed, colorId);
                } else if (pool[i] instanceof SpeedRacer) {
                    spawnSpeedRacer((SpeedRacer) pool[i], panelWidth, startY, speed, colorId);
                } else if (pool[i] instanceof SinusoidalMadness) {
                    spawnSinusoidal((SinusoidalMadness) pool[i], panelWidth, startY, speed, colorId);
                }
                return;
            }
        }
    }// fine activateFromPool

    private void spawnRandomEnemy(int panelWidth) {
        int startY = OBSTACLE_START_Y;
        int randomColorId = random.nextInt(availableColorsCount);
        int chance = random.nextInt(MAX_CHANCE);

        switch (currentPhase) {
            case 1:
                activateFromPool(standardPool, panelWidth, startY, currentFallSpeed, randomColorId);
                break;

            case 2:
                if (chance < SPEEDRACER_CHANCE_PHASE_2) {
                    activateFromPool(speedRacerPool, panelWidth, startY, currentFallSpeed * SPEEDRACER_MULTIPLIER,
                            randomColorId);
                } else {
                    activateFromPool(standardPool, panelWidth, startY, currentFallSpeed, randomColorId);
                }
                break;

            case 3:
            default:
                if (chance < SINUSOIDALMADNESS_CHANCE) {
                    activateFromPool(sinusoidalPool, panelWidth, startY, currentFallSpeed, randomColorId);
                } else if (chance < SPEEDRACER_CHANCE_PHASE_3) {
                    activateFromPool(speedRacerPool, panelWidth, startY, currentFallSpeed * SPEEDRACER_MULTIPLIER,
                            randomColorId);
                } else {
                    activateFromPool(standardPool, panelWidth, startY, currentFallSpeed, randomColorId);
                }
                break;
        }
    }// fine spawnRandomEnemy

    private void checkDifficultyProgression(int currentScore) {
        if (currentScore >= SCORE_PHASE_2 && currentPhase == 1) {
            currentPhase = 2;
            currentFallSpeed *= SPEED_PHASE_MULTIPLIER;
            availableColorsCount = 3;
        } else if (currentScore >= SCORE_PHASE_3 && currentPhase == 2) {
            currentPhase = 3;
            currentFallSpeed *= SPEED_PHASE_MULTIPLIER;
            availableColorsCount = 4;
        }
    }// fine checkDifficultyProgression

    private void updateSurvivalScore() {
        scoreTimer += TICK_TIME;

        if (scoreTimer >= SCORE_DELAY_MS) {
            addScore(DEFAULT_SCORE_GAIN);
            scoreTimer -= SCORE_DELAY_MS;
        }
    }// fine scoreHandler

    private void addScore(int points) {
        this.score += points;
        checkDifficultyProgression(this.score);
    }// fine addScore

    private void createPlayerExplosion(double x, double y, int colorId) {
        int particlesToSpawn = 25;
        int spawned = 0;

        // Calcola il centro esatto della navicella da cui far partire l'esplosione
        double centerX = x + (player.getWidth() / 2.0);
        double centerY = y + (player.getHeight() / 2.0);

        for (Particle p : allParticles) {
            if (!p.isActive()) {
                p.spawn(centerX, centerY, colorId); // Ricicla la particella
                p.setTriangle(true); // La fa diventare un triangolo

                spawned++;
                if (spawned >= particlesToSpawn) {
                    break; // Ferma il ciclo quando abbiamo spawnato 40 particelle
                }
            }
        }
    }

    private void createObstaclesExplosion(double x, double y, int colorId) {
        int particlesToSpawn = 15;
        int spawned = 0;
        for (Particle p : allParticles) {
            if (!p.isActive()) {
                p.spawn(x, y, colorId);
                p.setTriangle(false);
                spawned++;
                if (spawned >= particlesToSpawn) {
                    break;
                }
            }
        }
    }// fine createExplosion

    private void checkCollisions() {
        Hitbox playerHitbox = player.getHitbox();

        for (Obstacle obs : allEnemies) {
            if (!obs.isActive() || !CollisionChecker.checkCollision(playerHitbox, obs.getHitbox())) {
                continue;
            }

            // 2. Logica piatta e leggibile
            if (player.getColorId() == obs.getColorId()) {
                handlePositiveCollision(obs);
            } else {
                handleNegativeCollision();
            }
        }
    }// fine checkCollisions

    private void handlePositiveCollision(Obstacle obs) {
        AudioManager.getInstance().playSoundEffect("hit.wav");
        createObstaclesExplosion(obs.getX(), obs.getY() - EXPLOSION_OFFSET, obs.getColorId());
        floatingScores.add(new FloatingScore(obs.getX(), obs.getY(), obs.getPoints()));
        addScore(obs.getPoints());
        obs.setActive(false);
        obs.setY(DEFAULT_POSITION);
    }// fine handlePositiveCollision

    private void handleNegativeCollision() {
        if (isInvulnerable || isPlayerDead) {
            return;
        }

        decreaseLives();

        if (isPlayerDead) {
            AudioManager.getInstance().playSoundEffect("hit.wav");
            createPlayerExplosion(player.getX(), player.getY(), player.getColorId());
            player.setY(DEFAULT_POSITION); // Sposta fuori schermo
        } else {
            AudioManager.getInstance().playSoundEffect("hurt.wav");
            isInvulnerable = true;
        }
    }

    private void resetScore() {
        score = 0;
        scoreTimer = 0;
    }// fine resetScore

    private void resetObstacles() {
        for (Obstacle obs : allEnemies) {
            obs.setActive(false);
            obs.setY(DEFAULT_POSITION);
        }
    }// fine resetObstacles

    private void resetParticles() {
        for (Particle p : allParticles) {
            p.setActive(false);
        }
    }// fine resetParticles

    private void resetFloatingScore() {
        floatingScores.clear();
    }// fine resetFloatingScore

    private void resetDifficulty() {
        currentFallSpeed = BASE_SPEED;
        currentPhase = 1;
        availableColorsCount = 2;
    }// fine resetDifficulty

    private void resetGameover() {
        isGameOver = false;
    }// fine resetGameover

    private void resetLives() {
        lives = MAX_LIVES;
    }// fine resetLives

    private void resetInvulnerability() {
        isInvulnerable = false;
    }// fine resetInvulnerability

    private void resetDeathDelay() {
        isPlayerDead = false;
        deathTimer = 0;
    }// fine resetDeathDelay

    // METODI PUBBLICI OVERRIDE DA IGameModel

    @Override
    public void decreaseLives() {
        lives--;
        if (lives <= 0) {
            isPlayerDead = true;
            AudioManager.getInstance().stopBackgroundMusic();
        }
    }

    @Override
    public void update(int panelWidth, int panelHeight) {
        
        if (isGameOver) {
            return;
        }

        if (isPlayerDead) {
            handleDeathSequence();
            return;
        }

        updateGameplayCore(panelWidth, panelHeight);

    }// fine update

    private void handleDeathSequence() {
        deathTimer+=TICK_TIME;

        for (Particle p : allParticles) {
            if (p.isActive()) {
                p.update();
            }
        }
        
        if (deathTimer >= DEATH_DELAY_MS) {
            triggerGameOver();
        }
    }// fine handleDeathSequence

    private void triggerGameOver() {
        isGameOver = true;
        AudioManager.getInstance().playSoundEffect("game_over.wav");

        if (score > saveManager.getHighscore()) {
            saveManager.writeHighscore(score);
        }
    }// fine triggerGameOver

    private void updateGameplayCore(int panelWidth, int panelHeight) {
        invulnerabilityHandler();

        player.update();
        player.constrainX(MIN_X, panelWidth);
        player.constrainY(MIN_Y, panelHeight);

        spawningHandler(panelWidth);
        updateEnemies(panelHeight);

        updateParticles();
        updateFloatingScore();
        updateStars(panelWidth, panelHeight);

        checkCollisions();
        updateSurvivalScore();
    }// fine updateGameplayCore

    @Override
    public void resetGame() {
        resetDeathDelay();
        this.player.resetToInitialSettings(getStartX(), getStartY(), getStartColorId());
        resetScore();
        resetObstacles();
        resetParticles();
        resetGameover();
        resetLives();
        resetInvulnerability();
        resetDifficulty();
        resetFloatingScore();
    }// fine resetGame

    @Override
    public void autoSave() {
        if (!this.isGameOver) {
            saveManager.writeGameState(this.score, this.lives, this.currentPhase, this.currentFallSpeed,
                    this.availableColorsCount, this.player, this.allEnemies);
        }
    }// fine autoSave

    // getters del GameModel OVERRIDE

    @Override
    public boolean isInvulnerable() {
        return isInvulnerable;
    }// fine isInvulnerable
    
    @Override
    public int getInvulnTimer() {
        return invulnTimer;
    }

    // MODIFICA DA QUI IN GIÙ: Restituiscono le interfacce invece delle classi concrete
    @Override
    public IPlayer getPlayer() {
        return player;
    }

    @Override
    public List<? extends IObstacle> getEnemies() {
        return allEnemies;
    }

    @Override
    public List<? extends IParticle> getParticles() {
        return allParticles;
    }

    @Override
    public List<? extends IFloatingScore> getFloatingScores() {
        return floatingScores;
    }

    @Override
    public List<? extends IStar> getStars() {
        return stars;
    }

    // ... Il resto rimane invariato ...

    @Override
    public int getScore() {
        return this.score;
    }

    @Override
    public int getLives() {
        return lives;
    }

    @Override
    public boolean isGameOver() {
        return isGameOver;
    }

    @Override
    public double getStartX() {
        return this.START_X;
    }

    @Override
    public double getStartY() {
        return this.START_Y;
    }

    @Override
    public int getStartColorId() {
        return this.START_COLOR_ID;
    }

    @Override
    public int getAvailableColorsCount() {
        return this.availableColorsCount;
    }

    @Override
    public int getPhase() {
        return this.currentPhase;
    }

    @Override
    public double getCurrentSpeed() {
        return this.currentFallSpeed;
    }

    @Override
    public int getHighscore() {
        return saveManager.getHighscore();
    }

    @Override
    public boolean isPlayerDead() {
        return isPlayerDead;
    }

    // setters del GameModel OVERRIDE

    @Override
    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public void setLives(int lives) {
        this.lives = lives;
    }

    @Override
    public void setPhase(int phase) {
        this.currentPhase = phase;
    }

    @Override
    public void setCurrentSpeed(double currentSpeed) {
        this.currentFallSpeed = currentSpeed;
    }

    @Override
    public void setAvaibleColors(int avaibleColors) {
        this.availableColorsCount = avaibleColors;
    }

}// fine classe GameModel