package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.ConferenceSystem;

/**
 * A simple panel to login the user.
 *
 * @author Zachary Chandler
 */
public class LoginScreen extends PanelCard {

    /** The name to lookup this panel in a panel changer. */
    public static final String PANEL_LOOKUP_NAME = "LOGIN_SCREEN";
    
    private static final String PROMPT_TEXT = "Enter Your User Name: ";
    
    /** SVUID */
    private static final long serialVersionUID = -6726488320492216960L;

    public LoginScreen(PanelChanger p, UserContext context) {
        super(p, context);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(Main.BODY_SIZE.height / 3 , 0 , 0, 0));
    }

    @Override
    protected void updatePanel() {
        this.removeAll();
        
        JLabel prompt = new JLabel(PROMPT_TEXT);
        prompt.setAlignmentX(CENTER_ALIGNMENT);
        
        JTextField input = new JTextField(10);
        input.setAlignmentX(CENTER_ALIGNMENT);
        
        JPanel field = new JPanel();
        field.setAlignmentX(CENTER_ALIGNMENT);
        field.add(prompt);
        field.add(input);
        field.setMaximumSize(new Dimension(Main.BODY_SIZE.width, 35));
        
        JButton submit = new JButton("Login");
        submit.setAlignmentX(CENTER_ALIGNMENT);
        
        ActionListener login = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                context.setUser(ConferenceSystem.getInstance().getUserProfile(input.getText()));
                
                if (context.getUser() == null) {
                    JOptionPane.showMessageDialog(null,
                            "User not found. Please enter a valid user name.",
                            "User Not Found",
                            JOptionPane.PLAIN_MESSAGE);
                    input.grabFocus();
                } else {
                    panelChanger.changeTo(ConferenceSelection.PANEL_LOOKUP_NAME);
                }
            }
        };
        
        submit.addActionListener(login);
        input.addActionListener(login);
        
        this.add(field);
        this.add(submit);
    }

    @Override
    public String getNameOfPanel() {
        return PANEL_LOOKUP_NAME;
    }

}
