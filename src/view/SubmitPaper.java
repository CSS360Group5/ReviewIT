package view;

import java.awt.BorderLayout;
import java.awt.Component;
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

public class SubmitPaper extends PanelCard {

    /** The name to lookup this panel in a panel changer. */
    public static final String PANEL_LOOKUP_NAME = "SUBMIT_PAPER";
    
    /** SVUID */
    private static final long serialVersionUID = 8305415430621852696L;
    
    /** File chooser where start page is set to current directory */
    private final JFileChooser fileChooser = new JFileChooser(".");
    
    /** */
    private String currentFilePath = "No file has been selected.";
    
    /** */
    private String currentPaperTitle = "No title has been entered.";
    public SubmitPaper(PanelChanger p, UserContext context) {
        super(p, context);
        this.setLayout(new BorderLayout());
    }

    @Override
    public void updatePanel() {
    	this.removeAll();
    	
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

	@Override
    public String getNameOfPanel() {
        return PANEL_LOOKUP_NAME;
    }
    
    
    
    private class SelectFileAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			final int returnVal = fileChooser.showOpenDialog(null);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	File fileOfPaper = fileChooser.getSelectedFile();
	                      
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
    
    private void displayErrorMessage(final String theMessage) {
        JOptionPane.showMessageDialog(this, theMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private JPanel getInfoPanel() {
    	JPanel infoPanel = new JPanel();
    	JLabel title = new JLabel("Current paper information: ");
    	infoPanel.add(title);
    	return infoPanel;
    }
    
    private JPanel getSelectionPanel() {
    	JPanel selectionPanel = new JPanel();
    	JLabel title = new JLabel("Enter information about paper here");
    	selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
    	 //file chooser
        JButton fileChooserButton = new JButton("Select file to upload...");
        final FileNameExtensionFilter fileTypeFilter = new FileNameExtensionFilter(
                "docx, doc, and pdf", "docx", "doc", "pdf");
            fileChooser.setFileFilter(fileTypeFilter);
        fileChooserButton.addActionListener(new SelectFileAction());
        
        //text field
        JTextField paperTitleTextField = new JTextField("Enter the paper's title here...");

        
        selectionPanel.add(title);
        selectionPanel.add(fileChooserButton);
        selectionPanel.add(paperTitleTextField);
    	return selectionPanel;
    }

}
