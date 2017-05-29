package view;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import model.ConferenceSystem;

public class Main {

    public static final Dimension BODY_SIZE = new Dimension(800, 600);
    
    public static final ImageIcon ICON = new ImageIcon("Icon.png", "a pretty but meaningless splat");
    
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
        frame.setSize(new Dimension(BODY_SIZE.width, BODY_SIZE.height + 38));
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(ICON.getImage());
        
        UserContext context = new UserContext();
        
        PanelChanger cards = new PanelChanger();
        cards.addCard(new LoginScreen(cards, context));
        cards.addCard(new ConferenceSelection(cards, context));
        cards.addCard(new DashBoard(cards, context));
        cards.addCard(new SubmitPaper(cards, context));
        cards.addCard(new AssignReviewer(cards, context));
        cards.addCard(new SubmitRecomendation(cards, context));
        
        HeaderPanel header = new HeaderPanel(context, cards);
        header.addBody(cards.getJPanel());
        
        cards.addObserver(header);
        cards.changeTo(LoginScreen.PANEL_LOOKUP_NAME);
        
        frame.getContentPane().add(header);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
