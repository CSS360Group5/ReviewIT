package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
    private String currentFilePath = "No file has been selected.";
    
    private JPanel infoPanel = new JPanel();
    
    private JPanel selectionPanel = new JPanel();
    
    private Dimension preferredDimension = new Dimension(500, 20);
    
    /** */
    private String currentPaperTitle = "No title has been entered.";
    
    private JTextField paperTitleTextField = new JTextField("Enter the paper's title here...");
    
    public SubmitPaper(PanelChanger p, UserContext context) {
        super(p, context);
        this.setLayout(new BorderLayout());
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
    	
    	 Objects.requireNonNull(context.getCurrentConference());
         Objects.requireNonNull(context.getUser());

         
         this.add(getInfoPanel(), BorderLayout.WEST);
         this.add(getSelectionPanel(), BorderLayout.EAST);
         this.add(getConfirmationPanel(), BorderLayout.SOUTH);
         
         
    }

    private Component getConfirmationPanel() {
    	JPanel confirmationPanel = new JPanel();
    	
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");
        
        //set to see if paper is valid later
        submitButton.setEnabled(false);
        
        cancelButton.addActionListener(new CancelAction());
        
    	confirmationPanel.add(cancelButton);
        confirmationPanel.add(submitButton);
		return confirmationPanel;
	}
    
    /**
     * 
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
    	infoPanel.add(spacing);
    	infoPanel.add(paperFilePath);
    	infoPanel.add(paperTitleLabel);
    	infoPanel.add(paperAuthorsLabel);
    	return infoPanel;
    }
    
    /**
     * 
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
        
        
        JTextField authorsTextField = new JTextField("Enter authors (change this...)");
        JButton paperAuthorEnterButton = new JButton("Add author to list");
        authorsTextField.setMaximumSize(preferredDimension);
        
        selectionPanel.add(title);
        selectionPanel.add(spacing);
        selectionPanel.add(fileChooserButton);
        
        selectionPanel.add(paperTitleTextField);
        selectionPanel.add(paperTitleEnterButton);
        selectionPanel.add(spacing);
        
        selectionPanel.add(authorsTextField);
        selectionPanel.add(paperAuthorEnterButton);
    	return selectionPanel;
    }
    
    /**
     * Error message that displays if file selection process goes wrong.
     * @param theMessage
     */
    private void displayErrorMessage(final String theMessage) {
        JOptionPane.showMessageDialog(this, theMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
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
     * @author ijj
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
    
    private class CancelAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			panelChanger.changeTo(DashBoard.PANEL_LOOKUP_NAME);
			
		}
    	
    }

}
