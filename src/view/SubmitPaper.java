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
    
    private JPanel centerPanel = new JPanel();
    /** */
    private JPanel infoPanel = new JPanel();
    
    private JPanel filePanel = new JPanel();
    
    private JPanel titlePanel = new JPanel();
    
    private JPanel authorPanel = new JPanel();
    
    private JPanel authorDisplayPanel = new JPanel();
      
    /** Dimension used to format text entry fields. */
    private Dimension preferredDimension = new Dimension(400, 20);
    
    /** Authors of paper to be submitted.*/
    private List<String> authorsOfPaper = new ArrayList<>();
    
    /** */
    private String currentFilePath = "No file has been selected.";
    
    /** */
    private JTextField paperTitleTextField = new JTextField();
    /** */
    private JTextField authorsTextField = new JTextField("Enter single author name");

    /** */
    private boolean initialSignIn;
    
    public SubmitPaper(PanelChanger p, UserContext context) {
        super(p, context);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(Main.WINDOW_SIZE.height / 4 , 0 , Main.WINDOW_SIZE.height / 2, 0));

        this.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        //centerPanel.setAlignmentX(LEFT_ALIGNMENT);
        initialSignIn = true; //because object is instantiated before user is signed in.
    }
    
	@Override
    public String getNameOfPanel() {
        return PANEL_LOOKUP_NAME;
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
               
         this.add(centerPanel);    
    }
    
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
	
	private JPanel getTitlePanel() {
		titlePanel.setAlignmentY(LEFT_ALIGNMENT);
		JLabel paperTitleLabel = new JLabel("Title: ");
        paperTitleTextField.setPreferredSize(preferredDimension);
		
		titlePanel.add(paperTitleLabel);
		titlePanel.add(paperTitleTextField);
		return titlePanel;
	}
	
    private JPanel getAuthorPanel() {
        JButton paperAuthorEnterButton = new JButton("Add author to paper");
        paperAuthorEnterButton.addActionListener(new authorEnterAction());
        
                 
        authorPanel.add(paperAuthorEnterButton);
        
        
		return authorPanel;
	}
    
    private JPanel getAuthorDisplayPanel() {
    	JLabel paperAuthorsLabel = new JLabel("Current authors: ");
    	authorDisplayPanel.add(paperAuthorsLabel);
    	for (String authorName : authorsOfPaper) {
    		authorDisplayPanel.add(new JLabel(authorName));
    	}
    	return authorDisplayPanel;
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
     * Resets all all of the information about the paper being constructed to submit.
     * Normally called when a user cancels or submits a paper.
     */
    private void resetPaperInformation() {
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
			//authorsOfPaper.add(authorsTextField.getText());
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
			//submit paper
			resetPaperInformation();
			
		}
    	
    }
}
