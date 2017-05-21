package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Conference;
import model.ConferenceSystem;

/**
 * A conference selection screen.
 *
 * @author Zachary Chandler
 */
public class ConferenceSelection extends PanelCard {

    /** The name to lookup this panel in a panel changer. */
    public static final String PANEL_LOOKUP_NAME = "CONFERENCE_SELECTION";
    
    /** A padding value used to determine the desired padding of several elements in the panel. */
    private static final int PADDING = 20;
    
    /** SVUID */
    private static final long serialVersionUID = -3013328871722353114L;
    
    public ConferenceSelection(PanelChanger p, UserContext context) {
        super(p, context);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
    }

    @Override
    public void updatePanel() {
        this.removeAll();
        
        ConferenceSystem sys =  ConferenceSystem.getInstance();
        Dimension panelSize = Main.WINDOW_SIZE;
        
        JLabel prompt = new JLabel("Choose a Conference");
        prompt.setAlignmentX(CENTER_ALIGNMENT);
        
        JList<Conference> conferences = new JList<Conference>((sys.getConferences().toArray(new Conference[] {})));
        conferences.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        conferences.setPreferredSize(new Dimension(panelSize.width, panelSize.height));
        conferences.setBorder(new CompoundBorder(new LineBorder(this.getBackground(), PADDING / 2), 
                              new CompoundBorder(new LineBorder(Color.BLACK),
                                                 new EmptyBorder(PADDING, PADDING, PADDING, PADDING))));
        
        JButton selectButton = new JButton("Select");
        selectButton.setAlignmentX(CENTER_ALIGNMENT);
        selectButton.setEnabled(false);
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                  context.setCurrentConference(conferences.getSelectedValue());
                  
                  if (context.getCurrentConference() == null) {
                      throw new IllegalStateException();
                  }
                  
                  panelChanger.changeTo(DashBoard.PANEL_LOOKUP_NAME);
            }
        });
        
        conferences.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                Conference c = conferences.getSelectedValue();
                
                selectButton.setEnabled(DashBoard.shouldShowAuthorPane(context.getUser(), c)
                        || DashBoard.shouldShowSubProgramChairPane(context.getUser(), c));
            }
        });
        
        this.add(prompt);
        this.add(conferences);
        this.add(selectButton);
    }

    @Override
    public String getNameOfPanel() {
        return PANEL_LOOKUP_NAME;
    }
}
