package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
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
    
    private JList<String> reviewerJList;

    /** SVUID */
    private static final long serialVersionUID = 5949259200759242048L;

    public AssignReviewer(PanelChanger p, UserContext context) {
        super(p, context);
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(Main.WINDOW_SIZE.height / 4 , 0 , Main.WINDOW_SIZE.height / 2, 0));
        this.setAlignmentX(CENTER_ALIGNMENT);
        
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setPreferredSize(new Dimension(400, 600));
    }

    @Override
    public void updatePanel() {
    	this.removeAll();
    	
    	JLabel selectReviewersLabel = new JLabel("Select Reviewer(s)");
    	JLabel currentReviewersLabel = new JLabel("Current Reviewer(s)");
    	
    	mainPanel.add(selectReviewersLabel);
    	mainPanel.add(getAvailableReviewersList());
    	mainPanel.add(getButtonPanel());
    	
    	mainPanel.add(currentReviewersLabel);
    	mainPanel.add(getCurrentReviewers());
    	
    	this.add(mainPanel);
    }
    
    private JList<String> getAvailableReviewersList() {

    	List<UserProfile> reviewerList = context.getCurrentConference().getInfo().getReviewers();	
    	List<UserProfile> currentReviewers = context.getCurrentConference().getInfo().getReviewers(); 	
    	List<String> authors = context.getPaper().getAuthors(); // removing reviewers if they are an author
    	
    	//getting rid of reviewers if they are an author
    	Iterator<UserProfile> refineByAuthor = reviewerList.iterator();
    	while(refineByAuthor.hasNext()) {
    		UserProfile nextReviewer = refineByAuthor.next();
    		for(String author : authors) {
    			if (nextReviewer.getName().equals(author)) {
    				refineByAuthor.remove();
    				break;
    			}
    		}
    	}	
    	
    	//getting rid of reviewers if they are already reviewing the paper
    	Iterator<UserProfile> refineByCurrentReviewer = reviewerList.iterator();
    	while(refineByCurrentReviewer.hasNext()) {
    		UserProfile nextReviewer = refineByCurrentReviewer.next();
    		for(UserProfile currentReviewer : currentReviewers) {
    			if (currentReviewer.getName().equals(nextReviewer.getName())) {
    				refineByCurrentReviewer.remove();
    				break;
    			}
    		}
    	}
    	
    	String[] nameArray = new String[reviewerList.size()];	
    	for(int i = 0; i < reviewerList.size(); i++) {
    		//nameArray[i] = "" + (i+1) + ". " + reviewerList.get(i).getName();
    		nameArray[i] = reviewerList.get(i).getName();
    	}
    	
    	reviewerJList = new JList<String>(nameArray);
    	
    	//Dimension panelSize = Main.WINDOW_SIZE;
    	
    	reviewerJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    	reviewerJList.setPreferredSize(new Dimension(200, 400));
    	reviewerJList.setBorder(new CompoundBorder(new LineBorder(this.getBackground(), 20 / 2), 
                              new CompoundBorder(new LineBorder(Color.BLACK),
                                                 new EmptyBorder(20, 20, 20, 20))));
    	
    	return reviewerJList;
    }
    
    private JList<String> getCurrentReviewers() {
    	List<UserProfile> reviewerList = context.getCurrentConference().getInfo().getReviewers();
    	
    	String[] nameArray = new String[reviewerList.size()]; // getting the name of reviewers to display
    	for(int i = 0; i < reviewerList.size(); i++) {
    		//nameArray[i] = "" + (i+1) + ". " + reviewerList.get(i).getName();
    		nameArray[i] = reviewerList.get(i).getName();
    	}
    	
    	JList<String> currentReviewers = new JList<String>(nameArray);
    	currentReviewers.setBorder(new CompoundBorder(new LineBorder(this.getBackground(), 20 / 2), 
                new CompoundBorder(new LineBorder(Color.BLACK),
                                   new EmptyBorder(20, 20, 20, 20))));
    	
    	return currentReviewers;
    }
    
    private JPanel getButtonPanel() {
    	JPanel buttonPanel = new JPanel();
    	
        JButton assignButton = new JButton("Assign Reviewer");
        JButton cancelButton = new JButton("Cancel");
        
        assignButton.addActionListener(new ActionListener() {
        	
        	@Override
            public void actionPerformed(ActionEvent arg) {	
        		context.getPaper().addReviewer(reviewerJList.getSelectedValue());
        	}
        	
        });
        
        cancelButton.addActionListener(new ActionListener() {
        	
        	@Override
            public void actionPerformed(ActionEvent arg) {	
        		panelChanger.changeTo(DashBoard.PANEL_LOOKUP_NAME);
        	}
        	
        });
        
    	buttonPanel.add(cancelButton);
        buttonPanel.add(assignButton);
		return buttonPanel;
	}

    @Override
    public String getNameOfPanel() {
        return PANEL_LOOKUP_NAME;
    }

}
