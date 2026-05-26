package controller;
import javax.swing.JFrame;
import java.awt.Container;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.LayoutManager;

public class GUI extends JFrame {
    private JPanel jpanel = new JPanel(new GridLayout(0,1));

    public GUI(){
        super("Gioco");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = this.getContentPane();
        container.add(jpanel,BorderLayout.CENTER);
        jpanel.add(new JButton("Bottone"));
        jpanel.add(new JLabel("Etichetta"));
        this.pack();

    }

    public static void main (String args[]){

        SwingUtilities.invokeLater(new Runnable() {
            public void run(){
                new GUI().setVisible(true);
            }
            //helo
            
        });

    }
}