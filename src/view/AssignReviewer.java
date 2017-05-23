package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import model.Conference;
import model.UserProfile;

public class AssignReviewer extends PanelCard {
    
    /** The name to lookup this panel in a panel changer. */
    public static final String PANEL_LOOKUP_NAME = "ASSIGN_REVIEWER";
    
    private JPanel mainPanel = new JPanel();

    /** SVUID */
    private static final long serialVersionUID = 5949259200759242048L;

    public AssignReviewer(PanelChanger p, UserContext context) {
        super(p, context);
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(Main.WINDOW_SIZE.height / 4 , 0 , Main.WINDOW_SIZE.height / 2, 0));
        this.setAlignmentX(CENTER_ALIGNMENT);
        
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    }

    @Override
    public void updatePanel() {
    	this.removeAll();
    	
    	JLabel test = new JLabel("Select Reviewer(s)");
    	mainPanel.add(test);
    	mainPanel.add(getReviewerList());
    	mainPanel.add(getButtonPanel());
    	
    	this.add(mainPanel);
    }
    
    private JList<String> getReviewerList() {

    	List<UserProfile> reviewerList = context.getCurrentConference().getInfo().getReviewers();	
    	String[] nameArray = new String[reviewerList.size()];
    	
    	for(int i = 0; i < reviewerList.size(); i++) {
    		nameArray[i] = reviewerList.get(i).getName();
    	}
    	
    	JList<String> reviewerJList = new JList<String>(nameArray);
    	
    	String[] data = {"one", "two", "three", "four"};
    	//JList<String> reviewerJList = new JList<String>(data);
    	
    	//Dimension panelSize = Main.WINDOW_SIZE;
    	
    	reviewerJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    	reviewerJList.setPreferredSize(new Dimension(200, 400));
    	reviewerJList.setBorder(new CompoundBorder(new LineBorder(this.getBackground(), 20 / 2), 
                              new CompoundBorder(new LineBorder(Color.BLACK),
                                                 new EmptyBorder(20, 20, 20, 20))));
    	
    	return reviewerJList;
    }
    
    private JPanel getCurrentReviewers() {
    	List<UserProfile> reviewerList = context.getCurrentConference().getInfo().getReviewers();
    	
    	return new JPanel();
    }
    
    private JPanel getCurrentReviews() {
    	List<File> reviewList = context.getPaper().getReviews();
    	
    	return new JPanel();
    }
    
    private JPanel getButtonPanel() {
    	JPanel buttonPanel = new JPanel();
    	
        JButton assignButton = new JButton("Assign Reviewer");
        JButton cancelButton = new JButton("Cancel");
        
    	buttonPanel.add(cancelButton);
        buttonPanel.add(assignButton);
		return buttonPanel;
	}

    @Override
    public String getNameOfPanel() {
        return PANEL_LOOKUP_NAME;
    }

}
