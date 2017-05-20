package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import model.ConferenceSystem;

public class Main {

    public static final Dimension WINDOW_SIZE = new Dimension(800, 600);
    
    public static void main(String[] args) {
        intializeSystem();
        initializeWindow();
        
        Runtime.getRuntime().addShutdownHook(new Thread(Main::saveSystem));
    }
    
    private static void intializeSystem() {
        ConferenceSystem.getInstance().deserializeData();
    }
    
    private static void saveSystem() {
        ConferenceSystem.getInstance().serializeModel();
    }
    
    private static void initializeWindow() {
        JFrame frame = new JFrame("MSEE Conferences");
        frame.setSize(WINDOW_SIZE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        
        UserContext context = new UserContext();
        
        PanelChanger cards = new PanelChanger();
        cards.addCard(new LoginScreen(cards, context));
        cards.addCard(new ConferenceSelection(cards, context));
        cards.addCard(new DashBoard(cards, context));
        cards.addCard(new SubmitPaper(cards, context));
        cards.addCard(new RemovePaper(cards, context));
        cards.addCard(new AssignReviewer(cards, context));
        cards.addCard(new SubmitRecomendation(cards, context));
        
        cards.changeTo(LoginScreen.PANEL_LOOKUP_NAME);
        
        frame.getContentPane().add(cards.getJPanel(), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
