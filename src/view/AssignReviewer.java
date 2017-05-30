package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.UserProfile;


/**
 * GUI class that creates the Panel where the user can assign a Reviewer to a Paper
 *
 * @version 05/25/2017
 * @author Dmitriy Bliznyuk
 *
 */
public class AssignReviewer extends PanelCard {

    /** The name to lookup this panel in a panel changer. */
    public static final String PANEL_LOOKUP_NAME = "ASSIGN_REVIEWER";

    /**
     * Padding inside the panels.
     */
    private static final int INSIDE_PADDING = 20;

    /**
     * Space between the panels and the side of the window.
     */
    private static final int SIDE_PADDING = 100;

    /**
     * Space in between the panels.
     */
    private static final int BETWEEN_PADDING = 10;
    
    /**
     * Maximum number of reviews a reviewer is allowed to be assigned.
     */
    public static final int MAX_REVIEWS = 8;

    private static JButton assignButton;
    
    private static JLabel successLabel;

    /** SVUID */
    private static final long serialVersionUID = 5949259200759242048L;

    /**
     * Constructor that creates the AssignReviewer Panel.
     *
     * @param p PanelChanger that allows to switch between the different panels in the program
     * @param context UserContext which contains information for this Panel
     */
    public AssignReviewer(PanelChanger p, UserContext context) {
        super(p, context);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(INSIDE_PADDING, INSIDE_PADDING, INSIDE_PADDING, INSIDE_PADDING));
    }

    /**
     * Method to refresh the AssignReviewer Panel.
     *
     * Postconditions:
     * 	-Removes all components on in the JFrame and adds them back
     *
     */
    @Override
    public void updatePanel() {
    	this.removeAll();
    	
    	successLabel = new JLabel();
    	successLabel.setForeground(new Color(0, 153, 0)); // green color
    	successLabel.setHorizontalAlignment(BoxLayout.LINE_AXIS);
    	
    	JPanel successPanel = new JPanel(new BorderLayout());
    	successPanel.add(successLabel, BorderLayout.CENTER);
    	successPanel.setBorder(new EmptyBorder(0, 0, BETWEEN_PADDING, 0));
    	successLabel.setVisible(false);
    	successLabel.setHorizontalAlignment(JLabel.CENTER);
    	this.add(successPanel);
    	
    	this.add(getInfoPanel());
    	this.add(new JPanel());  	//adding a border between panels
    	this.add(getCurrentReviewersPanel());
    	this.add(new JPanel());		//adding a border between panels
    	this.add(getAvailableReviewersPanel());
    }

    /**
     * Method to receive a panel which contains all available Reviewers in the Conference.
     *
     * @return JPanel which contains all available Reviewers.
     */
    private JPanel getAvailableReviewersPanel() {
    	JPanel bottomPanel = new JPanel(new BorderLayout());
    	bottomPanel.setBorder(new CompoundBorder(new EmptyBorder(0, SIDE_PADDING, 0, SIDE_PADDING), BorderFactory.createTitledBorder(" Assign Another Reviewer")));
    	JPanel bottomLabelPanel = new JPanel(new BorderLayout());
    	bottomPanel.add(bottomLabelPanel, BorderLayout.NORTH);
    	List<UserProfile> reviewerList = context.getCurrentConference().getInfo().getReviewers();

    	reviewerList = refineByAuthors(reviewerList);
    	reviewerList = refineByCurrentReviewers(reviewerList);
    	reviewerList = refineByMaxReviews(reviewerList);
    	reviewerList = refineBySubchair(reviewerList);
    	final List<UserProfile> finalReviewerList = reviewerList;
    	String[] nameArray = new String[reviewerList.size()];
    	for(int i = 0; i < reviewerList.size(); i++) {	// what will be actually displayed to the user
    		nameArray[i] = reviewerList.get(i).getName();
    	}
    	JList<String> reviewerJList = new JList<String>(nameArray);
    	Dimension panelSize = Main.BODY_SIZE;
    	reviewerJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	reviewerJList.setPreferredSize(new Dimension(panelSize.width / 2, panelSize.height/3));
    	reviewerJList.setMaximumSize(new Dimension(panelSize.width / 2, panelSize.height/2));
    	reviewerJList.setBorder(new CompoundBorder(new LineBorder(this.getBackground(), INSIDE_PADDING / 2),
                              new CompoundBorder(new LineBorder(Color.BLACK),
                                                 new EmptyBorder(INSIDE_PADDING, INSIDE_PADDING, INSIDE_PADDING, INSIDE_PADDING))));
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
    	JPanel buttonPanel = new JPanel();
    	bottomPanel.add(reviewerJList, BorderLayout.CENTER); 	
        assignButton = new JButton("Assign Reviewer");
        assignButton.setEnabled(false);
        assignButton.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent arg) {
        		context.getCurrentConference().getSubprogramRole().assignReviewer(finalReviewerList.get((reviewerJList.getSelectedIndex())), context.getPaper());
        		panelChanger.changeTo(PANEL_LOOKUP_NAME);
        		successLabel.setText("Successfully assigned \"" + finalReviewerList.get((reviewerJList.getSelectedIndex())).getName() 
        				+ "\" to review Paper \"" + context.getPaper().getTitle() +"\"");
        		successLabel.setVisible(true);
        	}
        });
        buttonPanel.add(assignButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
    	return bottomPanel;
    }

    /**
     * Method to receive a panel which contains all current Reviewers of the paper.
     *
     * @return JPanel which contains all current Reviewers of the paper.
     */
    private JPanel getCurrentReviewersPanel() {
    	JPanel topPanel = new JPanel(new BorderLayout());
    	topPanel.setBorder(new CompoundBorder(new EmptyBorder(0, SIDE_PADDING, 0, SIDE_PADDING), BorderFactory.createTitledBorder(" Current Reviewer(s)")));
    	JPanel topLabelPanel = new JPanel(new BorderLayout());
    	topPanel.add(topLabelPanel, BorderLayout.PAGE_START);
    	topPanel.add(getCurrentReviewers(), BorderLayout.CENTER);

    	return topPanel;
    }

    /**
     * Method to receive a panel which contains information about the paper.
     *
     * @return JPanel which contains information about the paper.
     */
    private JPanel getInfoPanel() {
    	JPanel infoPanel = new JPanel(new BorderLayout());
    	JPanel insidePanel = new JPanel(new BorderLayout());
    	String paperName = context.getPaper().getTitle();
    	List<String> authors = context.getPaper().getAuthors();

    	StringBuilder authorBuffer = new StringBuilder(authors.get(0));
    	if (authors.size() > 1) {
	    	for(int i = 1; i < authors.size(); i++) {
	    		authorBuffer.append(", " + authors.get(i));
	    	}
    	}

    	infoPanel.setBorder(new CompoundBorder(new EmptyBorder(0, SIDE_PADDING, 0, SIDE_PADDING), BorderFactory.createTitledBorder(" Paper Information")));
    	insidePanel.setBorder(new EmptyBorder(INSIDE_PADDING/2, INSIDE_PADDING, INSIDE_PADDING/2, INSIDE_PADDING));

    	JLabel titleLabel = new JLabel("Title: " + paperName);
    	JLabel authorsLabel;
    	if (authors.size() > 1) {
    		authorsLabel = new JLabel("Authors: " + authorBuffer.toString());
    	} else {
    		authorsLabel = new JLabel("Author: " + authors.get(0));
    	}
    	insidePanel.add(titleLabel, BorderLayout.NORTH);
    	insidePanel.add(authorsLabel, BorderLayout.CENTER);
    	infoPanel.add(insidePanel);

    	return infoPanel;
    }

    /**
     * Method that refines the list of Reviewers, getting rid of Reviewers if they are
     * one of the authors of the paper.
     *
     * @param reviewerList List of UserProfiles of potential reviewers
     * @return refined List of UserProfiles of reviewers
     */
    private List<UserProfile> refineByAuthors(List<UserProfile> reviewerList) {
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

    	return reviewerList;
    }
    
    /**
     * Method that refines the list of Reviewers, getting rid of Reviewers if they are
     * a Subprogram chair of the paper.
     *
     * @param reviewerList List of UserProfiles of potential reviewers
     * @return refined List of UserProfiles of reviewers
     */
    private List<UserProfile> refineBySubchair(List<UserProfile> reviewerList) { 
    	
    	Iterator<UserProfile> refineByAuthor = reviewerList.iterator();
    	while(refineByAuthor.hasNext()) {
    		UserProfile nextReviewer = refineByAuthor.next();
    		if(context.getUser().getName().equals(nextReviewer.getName())) {
    			refineByAuthor.remove();
    		}
    	}
    	return reviewerList;
    }

    /**
     * Method that refines the list of Reviewers, getting rid of Reviewers if they are
     * already reviewing the paper.
     *
     * @param reviewerList List of UserProfiles of potential reviewers
     * @return refined List of UserProfiles of reviewers
     */
    private List<UserProfile> refineByCurrentReviewers(List<UserProfile> reviewerList) {
    	List<UserProfile> currentReviewers = context.getCurrentConference().getInfo().getReviewersForPaper(context.getPaper());


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

    	return reviewerList;
    }

    /**
     * Method that refines the list of Reviewers, getting rid of Reviewers if they are
     * already reviewing MAX_REVIEWS papers.
     *
     * @param reviewerList List of UserProfiles of potential reviewers
     * @return refined List of UserProfiles of reviewers
     */
    private List<UserProfile> refineByMaxReviews(List<UserProfile> reviewerList) {

    	Iterator<UserProfile> refineByMaxReviews = reviewerList.iterator();
    	while(refineByMaxReviews.hasNext()) {
    		UserProfile nextReviewer = refineByMaxReviews.next();
    		if(context.getCurrentConference().getInfo().getPapersAssignedToReviewer(nextReviewer).size() >= MAX_REVIEWS) {
    			refineByMaxReviews.remove();
    		}
    	}
    	return reviewerList;
    }

    /**
     * Method to receive a list component of all current Reviewers of the paper.
     *
     * @return JList of Strings of all current Reviewers of the paper
     */
    private JList<String> getCurrentReviewers() {
    	List<UserProfile> currentReviewerList = context.getCurrentConference().getInfo().getReviewersForPaper(context.getPaper());
      	
    	String[] nameArray = new String[currentReviewerList.size()]; // getting the name of reviewers to display
    	for(int i = 0; i < currentReviewerList.size(); i++) {
    		nameArray[i] = "" + (i+1) + ". " + currentReviewerList.get(i).getName();
    		//nameArray[i] = reviewerList.get(i).getName();
    	}

    	JList<String> currentReviewers = new JList<String>(nameArray);
    	Dimension panelSize = Main.BODY_SIZE;
    	currentReviewers.setPreferredSize(new Dimension(panelSize.width / 2, panelSize.height/3));
    	currentReviewers.setMaximumSize(new Dimension(panelSize.width / 2, panelSize.height/2));
    	currentReviewers.setBorder(new CompoundBorder(new LineBorder(this.getBackground(), INSIDE_PADDING / 2),
                new CompoundBorder(new LineBorder(Color.BLACK),
                                   new EmptyBorder(INSIDE_PADDING, INSIDE_PADDING, INSIDE_PADDING, INSIDE_PADDING))));

    	return currentReviewers;
    }

    @Override
    public String getNameOfPanel() {
        return PANEL_LOOKUP_NAME;
    }

}
