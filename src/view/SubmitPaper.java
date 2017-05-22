package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JFileChooser;

public class SubmitPaper extends PanelCard {

    /** The name to lookup this panel in a panel changer. */
    public static final String PANEL_LOOKUP_NAME = "SUBMIT_PAPER";
    
    /** SVUID */
    private static final long serialVersionUID = 8305415430621852696L;
    
    /** File chooser where start page is set to current directory */
    private final JFileChooser fileChooser = new JFileChooser(".");
    

    public SubmitPaper(PanelChanger p, UserContext context) {
        super(p, context);
    }

    @Override
    public void updatePanel() {
    	this.removeAll();
    	
    	 Objects.requireNonNull(context.getCurrentConference());
         Objects.requireNonNull(context.getUser());
         JButton fileChooserButton = new JButton("Select file to upload...");
         fileChooserButton.addActionListener(new SelectFileAction());
         this.add(fileChooserButton);

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
	        	File filePath = fileChooser.getSelectedFile();
	                      
	        } else if (returnVal == JFileChooser.ERROR_OPTION) { 
	            //displayErrorMessage("There was an error loading the selected file!");
	        }  
			
		}
    	
    }

}
