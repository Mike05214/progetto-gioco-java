package src.colorclash.model;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class SinusoidalMadness extends Obstacle {
    private static final int WIDTH = 70;
    private static final int HEIGHT = 70;
    private static final Random random = new Random();
    private static final int AMPLITUDE = 80;
    private static final int SPEED= 3;
    private int StartX;
    // Invece di frequency, la chiamiamo waveSpeed (velocità dell'onda)
    private double waveSpeed = 0.05;
    // Il nostro orologio interno indipendente!
    private double angle = 0;

  
    public SinusoidalMadness(int x, int y, double fallSpeed, int colorId,int width,int height) {
        
        
        super(x, y, fallSpeed, colorId,width,height);
        this.StartX=x;
        
    }

    @Override
    public void fall() {
        // 1. LA VELOCITÀ DI CADUTA: Rimane letteralmente invariata e indipendente
        this.y += this.fallSpeed;

        // 2. IL TEMPO CHE SCORRE: Aumentiamo l'angolo ad ogni frame
        angle += waveSpeed;

        // 3. IL MOVIMENTO ORIZZONTALE: Usiamo l'angolo interno, non la Y!
        this.x = this.StartX + (int) (AMPLITUDE * Math.sin(angle));
    }
    
    public static SinusoidalMadness creatSinusoidalMadness(int panelWidth, int startY, int colorId){
        // 1. CALCOLO DELLA ZONA SICURA (SAFE ZONE)
        // Il punto più a sinistra in cui può spawnare senza uscire dallo schermo
        int safeMinX = AMPLITUDE; 
        
        // Il punto più a destra in cui può spawnare
        int safeMaxX = panelWidth - WIDTH - AMPLITUDE;
        int randomX = random.nextInt(safeMinX,safeMaxX);
        return new SinusoidalMadness(randomX, startY, SPEED, colorId,WIDTH,HEIGHT);

    }


    @Override
    public Shape getHitbox() {
        // Usiamo Ellipse2D.Double che accetta le coordinate e le dimensioni
        return new Ellipse2D.Double(x, y, width, height);
    }
}
