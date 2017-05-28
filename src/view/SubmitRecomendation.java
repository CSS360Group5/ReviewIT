package view;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This class creates submits the recommendation for the paper to the program chair.
 * Preconditions:
 * 	The paper must exist
 * 	There must be atleast 3 reviews associated with the paper
 *  There must be a subprogram chair
 * Postconditions:
 * 	Submits the recommendation file and short name
 * @author Kevin Nguyen
 *
 */
public class SubmitRecomendation extends PanelCard {

    /** The name to lookup this mainPanel in a mainPanel changer. */
    public static final String PANEL_LOOKUP_NAME = "SUBMIT_RECOMENDATION";
    /** The button group to hold the radio buttons and make only 1 selectable */
	private final ButtonGroup buttonGroup = new ButtonGroup();

    /** SVUID */
    private static final long serialVersionUID = 8305415430621852696L;
    /** File chooser where start page is set to current directory */
    private final JFileChooser fileChooser = new JFileChooser(".");
    /** The submit button */
    private JButton submitRecommendationButton = new JButton("Submit Recommendation");
    /** The JLabel and panel that gets added/defined in other classes */
    private JLabel fileNameLabel;
    private JLabel submissionLabel;
    private JPanel gridLocation;
    /** These are the strings that change based on what the user submits */
    private String submissionMessage = "Recommendation Status: No recommendation has been given ";
    private String currentFilePath = "No file has been selected.";
    private String theRadioButtonRecommendationSelection;
    private JTable table = new JTable();
    
    public SubmitRecomendation(PanelChanger p, UserContext context) {
        super(p, context);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		table = new JTable();
		add(table);
    }
    /**
     * Gets the name and returns so that the panel changer method knows what
     * panel to change when the user clicks on the jButton
     */
	@Override
    public String getNameOfPanel() {
        return PANEL_LOOKUP_NAME;
    }
	/**
	 * This is the panel that contains the submit recommendation stuff.	
	 * @author Kevin Nguyen
	 */
    @Override
    public void updatePanel() {
    	this.removeAll();
    	JPanel mainPanel = new JPanel();
		add(mainPanel);
		//Gots this from zachs code trial and error need to test on other screens
		mainPanel.setLayout(new BorderLayout(Main.BODY_SIZE.height / 6 , Main.BODY_SIZE.height / 6));
		
		createUpperInfoPanel(mainPanel);
		//This is the jbuttons on the left side of the gui
		mainPanel.add(quickNavigationPanel(), BorderLayout.WEST);
		//This is the center panel with the grid stuff
		JPanel gridPanel = new JPanel();
		mainPanel.add(gridPanel, BorderLayout.CENTER);
		gridPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel instructionPanel = new JPanel();
		gridPanel.add(instructionPanel, BorderLayout.NORTH);
		instructionPanel.setLayout(new CardLayout(0, 0));
		//This is just a jlabel 
		JLabel lblUploadARecommendation = new JLabel("Upload a recommendation File for the Paper and select your recommendation.");
		lblUploadARecommendation.setAlignmentY(LEFT_ALIGNMENT);
		instructionPanel.add(lblUploadARecommendation);
		
		createGridBagGrid(gridPanel);
		//This is the location of the jlabel that has the name/directory of the recommendation file
		GridBagConstraints fileLabelLocation = new GridBagConstraints();
		fileLabelLocation.insets = new Insets(50, 0, 5, 5);
		fileLabelLocation.gridx = 0;
		fileLabelLocation.gridy = 0;
		gridLocation.add(fileChooserPanel(), fileLabelLocation);
		
		createRadioButtonGroupOnGrid(gridLocation);
		createSubmitButtonAndLocation(gridLocation);
		
    }  
    /**
     * This method creates the file chooser and its location 
     * @return the Panel that contains the file chooser
     * @author Ian Jury, Kevin Nguyen
     * 
     */
    private JPanel fileChooserPanel() {
		JPanel filePanel = new JPanel();
		JLabel info = new JLabel("Current file selected: ");
		//The jlabele gets changed after submission.
		fileNameLabel = new JLabel(currentFilePath);
		JButton fileChooserButton = new JButton("Upload File");
        final FileNameExtensionFilter fileTypeFilter = new FileNameExtensionFilter(
                "docx, doc, and pdf", "docx", "doc", "pdf");
            fileChooser.setFileFilter(fileTypeFilter);
        fileChooserButton.addActionListener(new SelectFileAction());
		
		filePanel.add(info);
		filePanel.add(fileChooserButton);
		filePanel.add(fileNameLabel);
		
		return filePanel;
    }
    /**
     * This makes a panel on the left side that contains buttons that navigates through the gui
     * For user convenience instead of going back everything.
     * @author Kevin Nguyen
     * @return the navigation panel with buttons
     */
    private JPanel quickNavigationPanel() {
    	//The buttons might have icons for them later.
		JPanel placeHolder = new JPanel();
		//Added spaces to make the button same size the hard code way.
		JButton conferenceBack = new JButton("Papers           ");
		conferenceBack.addActionListener(new cancelAction());
		JButton cancelBut = new JButton("Conferences");
		cancelBut.addActionListener(new conferenceAction());
		JButton reviewersPage = new JButton("Reviewers     ");
		reviewersPage.addActionListener(new reviewersPageAction());
		placeHolder.add(cancelBut);
		placeHolder.add(conferenceBack);
		placeHolder.add(reviewersPage);
		placeHolder.setLayout(new BoxLayout(placeHolder, BoxLayout.Y_AXIS));
		return placeHolder;
    }
    /**
     * This creates the grid on the panel
     * @author Kevin Nguyen
     * @param gridPanel
     */
    private void createGridBagGrid(JPanel gridPanel) {
		gridLocation = new JPanel();
		gridPanel.add(gridLocation, BorderLayout.CENTER);
		GridBagLayout theGridColandRows = new GridBagLayout();
		theGridColandRows.columnWidths = new int[]{0, 0, 0, 0};
		theGridColandRows.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		theGridColandRows.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		theGridColandRows.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridLocation.setLayout(theGridColandRows);
    }
    /**
     * Creates a jpanel that contains jlabels that has the name conference and submittion status.
     * pre:
     * 	The main panel must exists
     * post: 
     * 	the top panel containing the jlabels must show.
     * @param mainPanel The panel that contains the strings
     * @return a jpanel that contains information on the top left of the screen
     */
    private JPanel createUpperInfoPanel(JPanel mainPanel) {
		JPanel namePanel = new JPanel();
		mainPanel.add(namePanel, BorderLayout.NORTH);
		namePanel.setLayout(new BorderLayout(0, 0));
		
		JLabel SubProgramChairlabel = new JLabel("Sub Program Chair:  " + context.getUser().getName());
		namePanel.add(SubProgramChairlabel, BorderLayout.NORTH);
		submissionLabel = new JLabel(submissionMessage +
				"for the manuscript " + context.getPaper().getTitle());
		JLabel currentConference = new JLabel("Currently in the conference: " + context.getCurrentConference().toString());
		namePanel.add(currentConference,BorderLayout.CENTER);
		namePanel.add(submissionLabel, BorderLayout.SOUTH);
		return namePanel;
    }
    /**
     * Creates the jradio buttons that gets added into the button group.
     * pre: 
     * 	the grid panel must exist.
     * post:
     * 	adds in the buttons to the panel.
     * @param gridLocation the panel that has the grid bag layout
     */
    private void createRadioButtonGroupOnGrid(JPanel gridLocation) {
    	radioButtonAndLocation(gridLocation, 1, "Recommend");
    	radioButtonAndLocation(gridLocation, 2, "Don't Recommend");
    	radioButtonAndLocation(gridLocation, 3, "Unsure");

		JButton btnSeeReviews = new JButton("See Review Scores");
		btnSeeReviews.addActionListener(new seeReviewsAction());
		GridBagConstraints seeReviewsButtonLocation = new GridBagConstraints();

		seeReviewsButtonLocation.insets = new Insets(0, 0, 5, 5);
		seeReviewsButtonLocation.anchor = GridBagConstraints.WEST;
		seeReviewsButtonLocation.gridx = 0;
		seeReviewsButtonLocation.gridy = 4;
		gridLocation.add(btnSeeReviews, seeReviewsButtonLocation);
	}
    /**
     * Creates the actual buttons and where they exists on the grid layout
     * @param gridLocation the grid panel
     * @param gridY the y coordinate of the radio button
     * @param buttonName the name of the jbutton
     */
    private void radioButtonAndLocation(JPanel gridLocation, int gridY, String buttonName) {
		JRadioButton theRadioButton = new JRadioButton(buttonName);
		theRadioButton.setActionCommand(theRadioButton.getText());
		theRadioButton.addActionListener(new checkRequirementsForRadioButtons());
		buttonGroup.add(theRadioButton);
		GridBagConstraints theButtonLocation = new GridBagConstraints();
		theButtonLocation.anchor = GridBagConstraints.WEST;
		theButtonLocation.insets = new Insets(0, 0, 5, 5);
		theButtonLocation.gridx = 0;
		theButtonLocation.gridy = gridY;
		gridLocation.add(theRadioButton, theButtonLocation);
    }
    /**
     * Creates the submit button and the location.
     * pre:
     * 	The grid panel.
     * post:
     * 	creates the submit button and has the appropiate location.
     * @param gridLocation the grid panel
     */
    private void createSubmitButtonAndLocation(JPanel gridLocation) {
		JPanel theSubmitPanel = new JPanel();
		//theSubmitPanel.add(cancelButton);
		theSubmitPanel.add(submitRecommendationButton);
		submitRecommendationButton.setEnabled(false);
		//cancelButton.addActionListener(new cancelAction());
		submitRecommendationButton.addActionListener(new submitAction());
		GridBagConstraints theSubmitPanelLocation = new GridBagConstraints();
		theSubmitPanelLocation.insets = new Insets(50, 0, 0, 5);
		theSubmitPanelLocation.anchor = GridBagConstraints.WEST;
		theSubmitPanelLocation.gridx = 0;
		theSubmitPanelLocation.gridy = 5;
		gridLocation.add(theSubmitPanel, theSubmitPanelLocation);
    }
    /**
     * This is the action class for the paper submition.
     * Pre: 
     * 	The submit button must exist
     * 	The paper and recommendation file must exist
     * 	The user must select one of the radio buttons.
     * Post:
     * 	If the user selects okay then the recommendation gets added to the paper.
     * @author Kevin Nguyen
     * 
     */
    private class submitAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int reply = JOptionPane.showConfirmDialog(
				    null, "You Are About to submit the file: " + currentFilePath + 
				    "\nYou also chose " +  "\"" + theRadioButtonRecommendationSelection + "\"" + 
				    " for the manuscript: \n" + context.getPaper().getTitle() + " \nProceed?",
				    "Recommendation Submission" ,  JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				try {
					//System.out.println("Testing a submission");
					submissionMessage = "Recommendation Status: File Submitted! You decided " +
							theRadioButtonRecommendationSelection;
					submissionLabel.setText(submissionMessage +
					" for the manuscript " + context.getPaper().getTitle());
					//Should this be permanent?
					submitRecommendationButton.setEnabled(false);
				    
					//Changing the paper is currently not working. Due to business rule.
					//Changing to english
					context.getPaper().setRecommendationShort(theRadioButtonRecommendationSelection);
					//System.out.println("helloedfsdf");
					File theRecommendationFile = fileChooser.getSelectedFile();
					context.getPaper().setMyRecommendation(theRecommendationFile);
					//System.out.println(context.getPaper().getRecommendationShort());
					//panelChanger.changeTo(DashBoard.PANEL_LOOKUP_NAME);
				} catch (IllegalArgumentException ex) {
					//displayErrorMessage("Paper could not be submitted due to invalid input");
				}
			}
		}	
    }
    /**
     * Checks if both the radio button and file is selected/exists
     * So it can enable the submit recommendation button.
     */
    private class checkRequirementsForRadioButtons implements ActionListener {
    	@Override
		public void actionPerformed(ActionEvent e) {
    		theRadioButtonRecommendationSelection = buttonGroup.getSelection().getActionCommand();
    		//Converting statement to english.
	    	if(fileChooser.getSelectedFile() != null) {
	    		submitRecommendationButton.setEnabled(true);
	    	}
    	}
    }
    private void checkRequirementsRecommendationFile() {
    	if(fileChooser.getSelectedFile() != null && 
    			theRadioButtonRecommendationSelection != null) {
    		submitRecommendationButton.setEnabled(true);
    	}
    }
    /**
     * Error message that displays if file selection process goes wrong.
     * @param theMessage
     */
    private void displayErrorMessage(final String theMessage) {
        JOptionPane.showMessageDialog(this, theMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }    
    /**
     * The action that gets the Jfile chooser.
     * @author Ian Jury
     *
     */
    private class SelectFileAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			final int returnVal = fileChooser.showOpenDialog(null);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	File fileOfPaper = fileChooser.getSelectedFile();
	        	currentFilePath = fileOfPaper.getAbsolutePath();
	        	//The path stays when the user comes back
	        	//If done by ian's way it messes with my radio buttons
	        	fileNameLabel.setText(currentFilePath);
	        	checkRequirementsRecommendationFile();          
	        } else if (returnVal == JFileChooser.ERROR_OPTION) { 
	            displayErrorMessage("There was an error loading the selected file!");
	        }  			
		}  	
    }
    /**
     * This is the message dialog that shows the placeholder reviews.
     * @author K_Nguyen
     *
     */
    private class seeReviewsAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			//Hard Coded the scores since review scores are not implemented for this deliverable
			JOptionPane.showMessageDialog(
				    null, "Scale: 1-5 (5 being the best) \n \n"
				    		+ "Brian Geving: 5 \n Zachary Chandler: 3 \n Ian Jury: 1", 
				    "Review Scores", JOptionPane.PLAIN_MESSAGE);
		}   	
    }
    /**
     * These are the actions that changes the panel for the left side jbuttons of the panel
     * @author K_Nguyen
     *
     */
    private class conferenceAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			panelChanger.changeTo(ConferenceSelection.PANEL_LOOKUP_NAME);			
		}   	
    }
    private class reviewersPageAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			panelChanger.changeTo(AssignReviewer.PANEL_LOOKUP_NAME);			
		}   	
    }
    /**
     * Action for cancel button to change panel to previous.
     * @author Ian Jury
     *
     */
    private class cancelAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			panelChanger.changeTo(DashBoard.PANEL_LOOKUP_NAME);			
		}   	
    }

}
