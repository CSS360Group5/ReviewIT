package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class HeaderPanel extends JPanel implements Observer {

    /** SVUID */
    private static final long serialVersionUID = 8098693823655450146L;
    private UserContext context;
    private PanelChanger changer;
    private JLabel conferenceLabel;
    
    public HeaderPanel(UserContext context, PanelChanger changer) {
        super();
        
        this.context = context;
        this.changer = changer;
        
        
        this.setLayout(new BorderLayout());
        
        JPanel header = new JPanel();
        header.setLayout(new BorderLayout());
        header.setBorder(new LineBorder(Color.BLACK));
        
        JPanel westPanel = new JPanel();
        JPanel eastPanel = new JPanel();

        JButton home = new JButton("MSEE");
        home.addActionListener(new HomeButton());
        
//        Runnable run = () -> { System.out.println(header.getSize()); };
//        
//        home.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent arg0) {
//                run.run();
//            }
//            
//        });
        
        this.conferenceLabel = new JLabel("Conferences");
        
        JButton back = new JButton("Back");
        back.addActionListener(new BackButton());
        
        JButton logout = new JButton("Logout");
        logout.addActionListener(new LogoutButton());

        westPanel.add(home);
        westPanel.add(conferenceLabel);
        eastPanel.add(back);
        eastPanel.add(logout);

        header.add(westPanel, BorderLayout.WEST);
        header.add(eastPanel, BorderLayout.EAST);
        
        this.add(header, BorderLayout.NORTH);
    }
    
    
    public void addBody(JPanel body) {
        this.add(body, BorderLayout.CENTER);
    }
    
    private class HomeButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            if (!changer.getCurrentPanelName().equals(LoginScreen.PANEL_LOOKUP_NAME)) {
                changer.changeTo(ConferenceSelection.PANEL_LOOKUP_NAME);                
            }
        }
    }
    
    private class BackButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            if (!changer.getCurrentPanelName().equals(LoginScreen.PANEL_LOOKUP_NAME)) {
                changer.back();
            }
        }
    }
    
    private class LogoutButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            if (!changer.getCurrentPanelName().equals(LoginScreen.PANEL_LOOKUP_NAME)) {
                changer.changeTo(LoginScreen.PANEL_LOOKUP_NAME);
            }           
        }
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        
        if (changer.getCurrentPanelName().equals(LoginScreen.PANEL_LOOKUP_NAME) ||
                changer.getCurrentPanelName().equals(ConferenceSelection.PANEL_LOOKUP_NAME)) {
            conferenceLabel.setText("Conferences");
        } else {
            conferenceLabel.setText(context.getCurrentConference().getInfo().getName());
        }
        
    }
}
