package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.Paper;
/**
 * GUI screen that allows a logged in user to submit a paper to the current selected conference.
 * @author Ian Jury
 * @version 5/24/2017
 */
public class SubmitPaper extends PanelCard {

    /** The name to lookup this panel in a panel changer. */
    public static final String PANEL_LOOKUP_NAME = "SUBMIT_PAPER";
    
    /** SVUID */
    private static final long serialVersionUID = 8305415430621852696L;
    
    /** File chooser where start page is set to current directory */
    private final JFileChooser fileChooser = new JFileChooser(".");
    
    /** Panel for all other panels to be placed for layout purposes*/
    private JPanel centerPanel = new JPanel();

    /** Panel for file selection */
    private JPanel filePanel = new JPanel();
    
    /** Panel for title information input */
    private JPanel titlePanel = new JPanel();
    
    /** Panel for the addition of authors */
    private JPanel authorPanel = new JPanel();
    
    /** Panel to display current authors inputed by user*/
    private JPanel authorDisplayPanel = new JPanel();
      
    /** Dimension used to format text entry fields. */
    private Dimension preferredDimension = new Dimension(400, 20);
    
    /** Authors of paper to be submitted.*/
    private List<String> authorsOfPaper = new ArrayList<>();
    
    /** File path to display to user */
    private String currentFilePath = "No file has been selected.";
    
    /** Text field for user to enter title of paper. */
    private JTextField paperTitleTextField = new JTextField();
    
    /** Text field for user to enter author names */
    private JTextField authorToAdd = new JTextField();
    
    /** File object of paper to submit. */
    File fileOfPaper = new File("Placeholder");
    
    /** Boolean check to see if user has been signed in */
    private boolean initialSignIn;
    
    /**
     * 
     * @param p
     * @param context
     */
    public SubmitPaper(PanelChanger p, UserContext context) {
        super(p, context);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(Main.WINDOW_SIZE.height / 5 , 0 , Main.WINDOW_SIZE.height / 4, 0));

        this.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(CENTER_ALIGNMENT);
        initialSignIn = true; //because object is instantiated before user is signed in.
    }

    @Override
    public void updatePanel() {
    	this.removeAll();
    	centerPanel.removeAll();
    	filePanel.removeAll();
    	titlePanel.removeAll();
    	authorPanel.removeAll();
    	authorDisplayPanel.removeAll();
    	
    	 Objects.requireNonNull(context.getCurrentConference());
         Objects.requireNonNull(context.getUser());
         
         //adds the current user as an author of the paper, unless it has already been done.
         if (initialSignIn) {
        	 addCurrentUserAsAuthor();
         }
         
         centerPanel.add(getFilePanel());
         centerPanel.add(getTitlePanel());
         centerPanel.add(getAuthorPanel());
         centerPanel.add(getAuthorDisplayPanel());
         centerPanel.add(getConfirmationPanel());
         
         filePanel.setAlignmentX(RIGHT_ALIGNMENT);
         titlePanel.setAlignmentX(RIGHT_ALIGNMENT);
         authorPanel.setAlignmentX(RIGHT_ALIGNMENT);
         authorDisplayPanel.setAlignmentX(RIGHT_ALIGNMENT);        
               
         this.add(centerPanel);    
    }
    /**
     * Fills the file panel with relevant content.
     * @return filled file panel
     */
	private JPanel getFilePanel() {
		titlePanel.setAlignmentY(LEFT_ALIGNMENT);
		JLabel info = new JLabel("Current file selected: " + currentFilePath);
		JButton fileChooserButton = new JButton("Select file to upload...");
        final FileNameExtensionFilter fileTypeFilter = new FileNameExtensionFilter(
                "docx, doc, and pdf", "docx", "doc", "pdf");
            fileChooser.setFileFilter(fileTypeFilter);
        fileChooserButton.addActionListener(new SelectFileAction());
		
		filePanel.add(info);
		filePanel.add(fileChooserButton);
		return filePanel;
	}
	/**
	 * Fills the title panel with the relevant content.
	 * @return the filled title panel
	 */
	private JPanel getTitlePanel() {
		titlePanel.setAlignmentY(LEFT_ALIGNMENT);
		JLabel paperTitleLabel = new JLabel("Title: ");
        paperTitleTextField.setPreferredSize(preferredDimension);
		
		titlePanel.add(paperTitleLabel);
		titlePanel.add(paperTitleTextField);
		return titlePanel;
	}
	/**
	 * Fills the author panel with the relevant content.
	 * @return the filled author panel
	 */
    private JPanel getAuthorPanel() {
    	
    	authorToAdd.setPreferredSize(new Dimension(200, 20));
        JButton paperAuthorEnterButton = new JButton("Add author to paper");
        paperAuthorEnterButton.addActionListener(new authorEnterAction());
        //JList<UserProfile> listOfAuthorsOfConference = new JList
              
        authorPanel.add(authorToAdd);
        authorPanel.add(paperAuthorEnterButton); 
        
		return authorPanel;
	}
    /**
     * Fills the author display panel with the relevant content.
     * @return the filled author display panel
     */
    private JPanel getAuthorDisplayPanel() {
    	JLabel paperAuthorsLabel = new JLabel("Current authors: ");
    	authorDisplayPanel.add(paperAuthorsLabel);
    	for (String authorName : authorsOfPaper) {
    		authorDisplayPanel.add(new JLabel(authorName));
    	}
    	return authorDisplayPanel;
    }

    /**
     * Panel that allows user to either submit paper or exit current frame.
     * @return
     */
    private Component getConfirmationPanel() {
    	JPanel confirmationPanel = new JPanel();
    	confirmationPanel.setAlignmentX(RIGHT_ALIGNMENT);
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");
                
        cancelButton.addActionListener(new CancelAction());
        submitButton.addActionListener(new submitAction());
        
    	confirmationPanel.add(cancelButton);
        confirmationPanel.add(submitButton);
		return confirmationPanel;
	}

	/**
     * Adds the currently signed in user to the paper being constructed.
     */
    private void addCurrentUserAsAuthor() {
    	initialSignIn = false;
    	authorsOfPaper.add(context.getUser().getName());
    }
            
    /**
     * Resets all all of the information about the paper being constructed to submit.
     * Normally called when a user cancels or submits a paper.
     */
    private void resetPaperInformation() {
    	currentFilePath = "No file has been selected.";
    	paperTitleTextField.setText("");
    	authorsOfPaper.clear();
    	initialSignIn = true; 
    }
    
    /**
     * Error message that displays if file selection process goes wrong.
     * @param theMessage
     */
    private void displayErrorMessage(final String theMessage) {
        JOptionPane.showMessageDialog(this, theMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Message that displays if a paper has been successfully submitted to a conference.
     * @param theMessage to be displayed
     */
    private void displaySuccessMessage(final String theMessage) {
        JOptionPane.showMessageDialog(this, theMessage, "Success", JOptionPane.DEFAULT_OPTION);
    }
    
	@Override
    public String getNameOfPanel() {
        return PANEL_LOOKUP_NAME;
    }
    
    //private classes for actions
    
    /**
     * Action listener for adding the author in associated text field to list of authors for paper submission.
     * @author Ian Jury
     * @version 5/24/2017
     */
    private class authorEnterAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			authorsOfPaper.add(authorToAdd.getText());
			authorToAdd.setText("");

			//this is super weird-- need to change
        	panelChanger.changeTo(ConferenceSelection.PANEL_LOOKUP_NAME);
        	panelChanger.changeTo(SubmitPaper.PANEL_LOOKUP_NAME);
		} 	
    }
       
    /**
     * 
     * @author Ian Jury
     * @version 5/24/2017
     */
    private class SelectFileAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			final int returnVal = fileChooser.showOpenDialog(null);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	fileOfPaper = fileChooser.getSelectedFile();
	        	currentFilePath = fileOfPaper.getAbsolutePath();
	        	//this is super weird-- need to change
	        	panelChanger.changeTo(ConferenceSelection.PANEL_LOOKUP_NAME);
	        	panelChanger.changeTo(SubmitPaper.PANEL_LOOKUP_NAME);
	                      
	        } else if (returnVal == JFileChooser.ERROR_OPTION) { 
	            displayErrorMessage("There was an error loading the selected file!");
	        }  			
		}  	
    }
    
    /**
     * Action for cancel button to change panel to previous.
     * @author Ian Jury
     * @version 5/24/2017
     */
    private class CancelAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			resetPaperInformation();
			panelChanger.changeTo(DashBoard.PANEL_LOOKUP_NAME);			
		}   	
    }
    
    /**
     * 
     * @author Ian Jury
     * @version 5/24/2017     
     */
    private class submitAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//submit paper
			String titleOfPaper = paperTitleTextField.getText();
			try {
				Paper paperToSubmit = 
						Paper.createPaper(fileOfPaper, authorsOfPaper, titleOfPaper, context.getUser());
				//submits paper to conference
				context.getCurrentConference().getUserRole().addPaper(context.getUser(), paperToSubmit);
				displaySuccessMessage("Paper has been submitted to \"" 
						+ context.getCurrentConference().getInfo().getName() + "\".");
				resetPaperInformation();	
				panelChanger.changeTo(DashBoard.PANEL_LOOKUP_NAME);
			} catch (IllegalArgumentException ex) {
				displayErrorMessage("Paper could not be submitted due to invalid input");
			}
		}	
    }
}
