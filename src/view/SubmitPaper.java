package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
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
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 * 
 * @author Ian Jury
 *
 */
public class SubmitPaper extends PanelCard {

    /** The name to lookup this panel in a panel changer. */
    public static final String PANEL_LOOKUP_NAME = "SUBMIT_PAPER";
    
    /** SVUID */
    private static final long serialVersionUID = 8305415430621852696L;
    
    /** File chooser where start page is set to current directory */
    private final JFileChooser fileChooser = new JFileChooser(".");
    /** */
    private JPanel infoPanel = new JPanel();
    /** */
    private JPanel selectionPanel = new JPanel();
    
    private JPanel buttonPanel = new JPanel();
    
    /** Dimension used to format text entry fields. */
    private Dimension preferredDimension = new Dimension(500, 20);
    
    /** Title of the paper to be submitted */
    private String currentPaperTitle = "No title has been entered.";
    
    /** Authors of paper to be submitted.*/
    private List<String> authorsOfPaper = new ArrayList<>();
    
    /** */
    private String currentFilePath = "No file has been selected.";
    
    /** */
    private JTextField paperTitleTextField = new JTextField("Enter the paper's title here...");
    /** */
    private JTextField authorsTextField = new JTextField("Enter single author name");

    /** */
    private boolean initialSignIn;
    
    public SubmitPaper(PanelChanger p, UserContext context) {
        super(p, context);
        this.setLayout(new BorderLayout());
        initialSignIn = true; //because object is instantiated before user is signed in.
    }
    
	@Override
    public String getNameOfPanel() {
        return PANEL_LOOKUP_NAME;
    }

    @Override
    public void updatePanel() {
    	this.removeAll();
    	infoPanel.removeAll();
    	selectionPanel.removeAll();
    	buttonPanel.removeAll();
    	
    	 Objects.requireNonNull(context.getCurrentConference());
         Objects.requireNonNull(context.getUser());
         //adds the current user as an author of the paper, unless it has already been done.
         if (initialSignIn) {
        	 addCurrentUserAsAuthor();
         }

         this.add(getInfoPanel(), BorderLayout.WEST);
         this.add(getButtonPanel(), BorderLayout.EAST);
         this.add(getSelectionPanel(), BorderLayout.CENTER);
         this.add(getConfirmationPanel(), BorderLayout.SOUTH);
         
         
    }
    /**
     * Adds the currently signed in user to the paper being constructed.
     */
    private void addCurrentUserAsAuthor() {
    	initialSignIn = false;
    	authorsOfPaper.add(context.getUser().getName());
    }

    /**
     * Panel that allows user to either submit paper or exit current frame.
     * @return
     */
    private Component getConfirmationPanel() {
    	JPanel confirmationPanel = new JPanel();
    	
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");
        
        //set to see if paper is valid later
        submitButton.setEnabled(false);
        
        cancelButton.addActionListener(new CancelAction());
        submitButton.addActionListener(new submitAction());
        
    	confirmationPanel.add(cancelButton);
        confirmationPanel.add(submitButton);
		return confirmationPanel;
	}
    
    
    /**
     * Displays the info for the current state of the paper. Changes as paper is being 'built' before submission.
     * @return
     */
    private JPanel getInfoPanel() {
    	//JPanel infoPanel = new JPanel();
    	infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
    	
    	JLabel title = new JLabel("Current paper information: ");
    	JLabel spacing = new JLabel(" ");
    	JLabel paperFilePath = new JLabel("Current file path: " + currentFilePath);
    	JLabel paperTitleLabel = new JLabel("Current title: " + currentPaperTitle);
    	JLabel paperAuthorsLabel = new JLabel("Current authors: ");
    	
    	infoPanel.add(title);
    	//infoPanel.add(spacing);
    	infoPanel.add(paperFilePath);
    	infoPanel.add(paperTitleLabel);
    	infoPanel.add(paperAuthorsLabel);
    	
    	//prints out authors of paper
    	for (String authorName : authorsOfPaper) {
    		infoPanel.add(new JLabel(authorName));
    	}
    	return infoPanel;
    }
    
    private JPanel getButtonPanel() {
    	buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
    	JButton paperTitleEnterButton = new JButton("Confirm current title");
        paperTitleEnterButton.addActionListener(new titleEnterAction());
        
        JButton paperAuthorEnterButton = new JButton("Add author to list");
        paperAuthorEnterButton.addActionListener(new authorEnterAction());
        
        buttonPanel.add(paperTitleEnterButton);
        buttonPanel.add(paperAuthorEnterButton);
    	return buttonPanel;
    }
    
    
    /**
     * Adds all of the necessary elements to the panel that takes user input for paper submission.
     * @return
     */
    private JPanel getSelectionPanel() {
    	//JPanel selectionPanel = new JPanel();
    	JLabel title = new JLabel("Enter information about paper here");
    	selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
    	JLabel spacing = new JLabel(" ");
    	
    	
    	 //file chooser
        JButton fileChooserButton = new JButton("Select file to upload...");
        final FileNameExtensionFilter fileTypeFilter = new FileNameExtensionFilter(
                "docx, doc, and pdf", "docx", "doc", "pdf");
            fileChooser.setFileFilter(fileTypeFilter);
        fileChooserButton.addActionListener(new SelectFileAction());
        
        //text field       
        paperTitleTextField.setMaximumSize(preferredDimension);
        JButton paperTitleEnterButton = new JButton("Confirm current title");
        paperTitleEnterButton.addActionListener(new titleEnterAction());
        
        JButton paperAuthorEnterButton = new JButton("Add author to list");
        paperAuthorEnterButton.addActionListener(new authorEnterAction());
        authorsTextField.setMaximumSize(preferredDimension);
        
        selectionPanel.add(title);
        selectionPanel.add(fileChooserButton);       
        selectionPanel.add(paperTitleTextField);        
        selectionPanel.add(authorsTextField);

    	return selectionPanel;
    }
    
    /**
     * Resets all all of the information about the paper being constructed to submit.
     * Normally called when a user cancels or submits a paper.
     */
    private void resetPaperInformation() {
    	currentPaperTitle = "No title has been entered.";
    	currentFilePath = "No file has been selected.";
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
     * Action listener for adding the author in associated text field to list of authors for paper submission.
     * @author Ian Jury
     *
     */
    private class authorEnterAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			authorsOfPaper.add(authorsTextField.getText());
			//this is super weird-- need to change
        	panelChanger.changeTo(ConferenceSelection.PANEL_LOOKUP_NAME);
        	panelChanger.changeTo(SubmitPaper.PANEL_LOOKUP_NAME);
		} 	
    }
    
    /**
     * Action to make the author name in the associated text box the name of the paper to submit.
     * @author Ian Jury
     *
     */
    private class titleEnterAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			currentPaperTitle = paperTitleTextField.getText();
			//this is super weird-- need to change
        	panelChanger.changeTo(ConferenceSelection.PANEL_LOOKUP_NAME);
        	panelChanger.changeTo(SubmitPaper.PANEL_LOOKUP_NAME);			
		}
    }
       
    /**
     * 
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
	        	infoPanel.repaint();
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
     *
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
     */
    private class submitAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
    	
    }
}
