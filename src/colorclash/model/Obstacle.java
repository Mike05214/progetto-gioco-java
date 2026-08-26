package colorclash.model;

public abstract class Obstacle implements IObstacle {
    
    //variabili d'istanza 
    protected double x;
    protected double y;
    protected int width;
    protected int height;
    protected double fallSpeed;
    protected int colorId;
    protected boolean isActive;
    protected Hitbox hitbox;

    public Obstacle(double startX, double startY, double speed, int colorId, int width, int height) {
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
        this.fallSpeed = speed;
        this.colorId = colorId;
        this.isActive = true;
        this.hitbox = new Hitbox(); 
    }// fine costruttore

    //METODI PUBBLICI

    public void update() {
        y += fallSpeed;
        hitbox.updatePosition(this.x, this.y);
    }// fine update

    public void checkOffScreen(int screenHeight) {
        if (y > screenHeight) {
            isActive = false;
        }
    }// fine checkOffScreen
    
    public abstract void updateHitbox();

    //METODI PROTETTI
    protected abstract void createHitbox();

    //GETTERS
    
    @Override
    public double getX() { return x; }

    @Override
    public double getY() { return y; }

    @Override
    public int getWidth() { return width; }

    @Override
    public int getHeight() { return height; }

    @Override
    public int getColorId() { return colorId; }

    @Override
    public boolean isActive() { return isActive; }

    @Override
    public Hitbox getHitbox() { return hitbox;  }
    public abstract String getType();
    public abstract int getPoints();
    public double getFallSpeed() { return fallSpeed; }

    // SETTERS

    public void setActive(boolean active) { this.isActive = active; }
    public void setY(double CordY) { this.y = CordY; }
    public void setX(double CordX) { this.x = CordX; }
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public void setFallSpeed(double speed) { this.fallSpeed = speed; }
    public void setColorId(int colorId) { this.colorId = colorId; }

}// fine classe astratta Obstacle