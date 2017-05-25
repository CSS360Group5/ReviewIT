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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Conference;
import model.UserProfile;

//A Reviewer can be assigned to review a maximum of 8 manuscripts for any conference.
//A Reviewer cannot be assigned until after the author submission deadline. --probably should be done on before panel

public class AssignReviewer extends PanelCard {
    
    /** The name to lookup this panel in a panel changer. */
    public static final String PANEL_LOOKUP_NAME = "ASSIGN_REVIEWER";
    
    private static final int PADDING = 20;
    
    public static final int MAX_REVIEWS = 8;
    
    private JButton assignButton;
    
    private JList<String> reviewerJList;

    /** SVUID */
    private static final long serialVersionUID = 5949259200759242048L;

    public AssignReviewer(PanelChanger p, UserContext context) {
        super(p, context);
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
    }

    @Override
    public void updatePanel() {
    	this.removeAll();
    	
    	JLabel selectReviewersLabel = new JLabel(" Assign Another Reviewer");
    	JLabel currentReviewersLabel = new JLabel(" Current Reviewer(s)");
    	
    	JPanel topPanel = new JPanel(new BorderLayout());
    	topPanel.setBorder(new CompoundBorder(new EmptyBorder(0, 100, 0, 100), BorderFactory.createTitledBorder(" Current Reviewer(s)")));
    	//topPanel.setBorder(new EmptyBorder(0, 80, 0, 80));
    	JPanel topLabelPanel = new JPanel(new BorderLayout());
    	topLabelPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
    	//topLabelPanel.add(currentReviewersLabel, BorderLayout.WEST);
    	topPanel.add(topLabelPanel, BorderLayout.PAGE_START);
    	topPanel.add(getCurrentReviewers(), BorderLayout.CENTER);
    	 	
    	JPanel bottomPanel = new JPanel(new BorderLayout());
    	//bottomPanel.setBorder(new EmptyBorder(0, 80, 0, 80));
    	bottomPanel.setBorder(new CompoundBorder(new EmptyBorder(0, 100, 0, 100), BorderFactory.createTitledBorder(" Assign Another Reviewer")));
    	JPanel bottomLabelPanel = new JPanel(new BorderLayout());
    	bottomLabelPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
    	//bottomLabelPanel.add(selectReviewersLabel);
    	bottomPanel.add(bottomLabelPanel, BorderLayout.NORTH);
    	bottomPanel.add(getAvailableReviewersList(), BorderLayout.CENTER);
    	bottomPanel.add(getButtonPanel(), BorderLayout.SOUTH);
    	
    	JPanel borderPanel = new JPanel();
    	//borderPanel.setBackground(Color.YELLOW);
    	
    	this.add(getInfoPanel());
    	this.add(topPanel);
    	this.add(borderPanel);
    	this.add(bottomPanel);
    }
    
    private JPanel getInfoPanel() {
    	JPanel infoPanel = new JPanel(new BorderLayout());
    	
    	return infoPanel;
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
    	
    	//gotta filter by max reviews allowed
    	//context.getCurrentConference().getInfo().getPapersAssignedToReviewer(UserProfile)
    	
    	
    	
    	//getting rid of reviewers if they are already reviewing the paper
//    	Iterator<UserProfile> refineByCurrentReviewer = reviewerList.iterator();
//    	while(refineByCurrentReviewer.hasNext()) {
//    		UserProfile nextReviewer = refineByCurrentReviewer.next();
//    		for(UserProfile currentReviewer : currentReviewers) {
//    			if (currentReviewer.getName().equals(nextReviewer.getName())) {
//    				refineByCurrentReviewer.remove();
//    				break;
//    			}
//    		}
//    	}
    	
    	String[] nameArray = new String[reviewerList.size()];	
    	for(int i = 0; i < reviewerList.size(); i++) {
    		//nameArray[i] = "" + (i+1) + ". " + reviewerList.get(i).getName();
    		nameArray[i] = reviewerList.get(i).getName();
    	}
    	
    	reviewerJList = new JList<String>(nameArray);
    	
    	Dimension panelSize = Main.WINDOW_SIZE;
    	reviewerJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
   
    	reviewerJList.setPreferredSize(new Dimension(panelSize.width / 2, panelSize.height/3));
    	reviewerJList.setMaximumSize(new Dimension(panelSize.width / 2, panelSize.height/2));
    	reviewerJList.setBorder(new CompoundBorder(new LineBorder(this.getBackground(), 20 / 2), 
                              new CompoundBorder(new LineBorder(Color.BLACK),
                                                 new EmptyBorder(20, 20, 20, 20))));
    	
    	
    	reviewerJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                String name = reviewerJList.getSelectedValue();
                
                if (name != null && !name.equals("")) {
                	assignButton.setEnabled(true);
                } else {
                	assignButton.setEnabled(false);
                }
            }
        });
    	
    	
    	return reviewerJList;
    }
    
    private JList<String> getCurrentReviewers() {
    	List<UserProfile> reviewerList = context.getCurrentConference().getInfo().getReviewers();
    	
    	String[] nameArray = new String[reviewerList.size()]; // getting the name of reviewers to display
    	for(int i = 0; i < reviewerList.size(); i++) {
    		nameArray[i] = "" + (i+1) + ". " + reviewerList.get(i).getName();
    		//nameArray[i] = reviewerList.get(i).getName();
    	}
    	
    	JList<String> currentReviewers = new JList<String>(nameArray);
    	Dimension panelSize = Main.WINDOW_SIZE;
    	currentReviewers.setPreferredSize(new Dimension(panelSize.width / 2, panelSize.height/3));
    	currentReviewers.setMaximumSize(new Dimension(panelSize.width / 2, panelSize.height/2));
    	currentReviewers.setBorder(new CompoundBorder(new LineBorder(this.getBackground(), 20 / 2), 
                new CompoundBorder(new LineBorder(Color.BLACK),
                                   new EmptyBorder(20, 20, 20, 20))));
    	
    	return currentReviewers;
    }
    
    private JPanel getButtonPanel() {
    	JPanel buttonPanel = new JPanel();
    	
        assignButton = new JButton("Assign Reviewer");
        assignButton.setEnabled(false);
        JButton cancelButton = new JButton("Cancel");
        
        assignButton.addActionListener(new ActionListener() {
        	
        	@Override
            public void actionPerformed(ActionEvent arg) {	
        		context.getPaper().addReviewer(reviewerJList.getSelectedValue());
        		updatePanel();
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
