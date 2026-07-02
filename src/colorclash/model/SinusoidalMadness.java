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

    // Contatore per sapere da quanti frame non cambiamo colore
    private int colorTimer = 0; 

// Ogni quanti frame cambia colore? (es. 20 frame = cambia 3 volte al secondo a 60FPS)
    private static final int COLOR_CHANGE_INTERVAL = 60; 

// Il numero di colori attuali (0, 1, 2). In futuro questo diventerà 4!
    private static final int CURRENT_MAX_COLORS = 3;

  
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

        // 2. IL CAMBIO COLORE CONTINUO (La nuova meccanica!)
        colorTimer++; // Il tempo scorre...
    
    // Se è passato l'intervallo di tempo prestabilito...
        if (colorTimer >= COLOR_CHANGE_INTERVAL) {
        
        // A. Resettiamo il cronometro a zero
            colorTimer = 0;
        
        // B. Calcoliamo il prossimo colore
        // L'operatore modulo (%) fa la magia: se il colore è 2, (2+1)%3 fa 0. 
        // Il colore torna all'inizio creando un loop infinito: 0 -> 1 -> 2 -> 0 -> 1...
            int nextColorId = (this.getColorId() + 1) % CURRENT_MAX_COLORS;
        
        // C. Aggiorniamo il colore effettivo dell'ostacolo
            this.setColorId(nextColorId); 
    }
    }
    
    public static SinusoidalMadness creatSinusoidalMadness(int panelWidth, int startY, double fallSpeed, int colorId){
        // 1. CALCOLO DELLA ZONA SICURA (SAFE ZONE)
        // Il punto più a sinistra in cui può spawnare senza uscire dallo schermo
        int safeMinX = AMPLITUDE; 
        
        // Il punto più a destra in cui può spawnare
        int safeMaxX = panelWidth - WIDTH - AMPLITUDE;
        int randomX = random.nextInt(safeMinX,safeMaxX);

        return new SinusoidalMadness(randomX, startY, fallSpeed, colorId,WIDTH,HEIGHT);
        

    }


    @Override
    public Shape getHitbox() {
        // Usiamo Ellipse2D.Double che accetta le coordinate e le dimensioni
        return new Ellipse2D.Double(x, y, width, height);
    }
    @Override
    public int getPoints(){
        return 300;
    }
}
