package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Main {

    public static final Dimension MIN_WINDOW_SIZE = new Dimension(800, 600);
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("MSEE Conferences");
        frame.setMinimumSize(MIN_WINDOW_SIZE);
        frame.setLocationRelativeTo(null);

        PanelChanger cards = new PanelChanger();
        cards.addCard(new ConferenceSelection(cards));
        cards.addCard(new DashBoard(cards));
        cards.addCard(new SubmitPaper(cards));
        cards.addCard(new RemovePaper(cards));
        cards.addCard(new AssignReviewer(cards));
        cards.addCard(new SubmitRecomendation(cards));
        
        frame.getContentPane().add(cards.getJPanel(), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
