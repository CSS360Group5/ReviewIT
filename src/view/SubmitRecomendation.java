package view;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
 * 
 * @author Kevin Nguyen
 *
 */
public class SubmitRecomendation extends PanelCard {

    /** The name to lookup this mainPanel in a mainPanel changer. */
    public static final String PANEL_LOOKUP_NAME = "SUBMIT_RECOMENDATION";
	private final ButtonGroup buttonGroup = new ButtonGroup();

    /** SVUID */
    private static final long serialVersionUID = 8305415430621852696L;
    
    /** File chooser where start page is set to current directory */
    private final JFileChooser fileChooser = new JFileChooser(".");
    /** */
    private JPanel infoPanel = new JPanel();
    /** Authors of paper to be submitted.*/
    private List<String> authorsOfPaper = new ArrayList<>();
    
    /** */
    private String currentFilePath = "No file has been selected.";
    private JTable table = new JTable();
    
    public SubmitRecomendation(PanelChanger p, UserContext context) {
        super(p, context);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		table = new JTable();
		add(table);
    }
    
	@Override
    public String getNameOfPanel() {
        return PANEL_LOOKUP_NAME;
    }

    @Override
    public void updatePanel() {
    	this.removeAll();
    	this.removeAll();
    	JPanel mainPanel = new JPanel();
		add(mainPanel);
		mainPanel.setLayout(new BorderLayout(Main.WINDOW_SIZE.height / 3 , Main.WINDOW_SIZE.height / 4));
		
		JPanel namePanel = new JPanel();
		mainPanel.add(namePanel, BorderLayout.NORTH);
		namePanel.setLayout(new BorderLayout(0, 0));
		
		JLabel SubProgramChairlabel = new JLabel("Sub Program Chair:  " + context.getUser().getName());
		namePanel.add(SubProgramChairlabel);
		
		JLabel labelPaper = new JLabel("Paper:	"); //+ context.getPaper().getTitle());
		namePanel.add(labelPaper, BorderLayout.SOUTH);
		
		JPanel placeHolder = new JPanel();
		mainPanel.add(placeHolder, BorderLayout.WEST);
		
		JPanel gridPanel = new JPanel();
		mainPanel.add(gridPanel, BorderLayout.CENTER);
		gridPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel instructionPanel = new JPanel();
		gridPanel.add(instructionPanel, BorderLayout.NORTH);
		instructionPanel.setLayout(new CardLayout(0, 0));
		
		JLabel lblUploadARecommendation = new JLabel("Upload a recommendation File for the Paper and select your recommendation.");
		lblUploadARecommendation.setAlignmentY(LEFT_ALIGNMENT);
		instructionPanel.add(lblUploadARecommendation);
		
		JPanel gridLocation = new JPanel();
		gridPanel.add(gridLocation, BorderLayout.CENTER);
		GridBagLayout theGridColandRows = new GridBagLayout();
		theGridColandRows.columnWidths = new int[]{0, 0, 0, 0};
		theGridColandRows.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		theGridColandRows.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		theGridColandRows.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridLocation.setLayout(theGridColandRows);
		
		JLabel fileLabel = new JLabel("File:");
		GridBagConstraints fileLabelLocation = new GridBagConstraints();
		fileLabelLocation.insets = new Insets(0, 0, 5, 5);
		fileLabelLocation.gridx = 0;
		fileLabelLocation.gridy = 0;
        final FileNameExtensionFilter fileTypeFilter = new FileNameExtensionFilter(
                "docx, doc, and pdf", "docx", "doc", "pdf");
        fileChooser.setFileFilter(fileTypeFilter);
		gridLocation.add(fileLabel, fileLabelLocation);
		
		JButton btnChooseFile = new JButton("Choose File");
		GridBagConstraints gbc_btnChooseFile = new GridBagConstraints();
		gbc_btnChooseFile.anchor = GridBagConstraints.WEST;
		gbc_btnChooseFile.insets = new Insets(0, 0, 5, 5);
		gbc_btnChooseFile.gridx = 1;
		gbc_btnChooseFile.gridy = 0;
		btnChooseFile.addActionListener(new SelectFileAction());
		gridLocation.add(btnChooseFile, gbc_btnChooseFile);
		
		JLabel fileNameLabel = new JLabel(currentFilePath);
		GridBagConstraints fileNameLabelLocation = new GridBagConstraints();
		fileNameLabelLocation.insets = new Insets(0, 0, 3, 0);
		fileNameLabelLocation.gridx = 2;
		fileNameLabelLocation.gridy = 0;
		gridLocation.add(fileNameLabel, fileNameLabelLocation);
		
		JRadioButton recommendRadioButton= new JRadioButton("Recommend");
		buttonGroup.add(recommendRadioButton);
		GridBagConstraints recommendRadioButtonLocation= new GridBagConstraints();
		recommendRadioButtonLocation.anchor = GridBagConstraints.WEST;
		recommendRadioButtonLocation.insets = new Insets(0, 0, 5, 5);
		recommendRadioButtonLocation.gridx = 1;
		recommendRadioButtonLocation.gridy = 1;
		gridLocation.add(recommendRadioButton, recommendRadioButtonLocation);
		
		JRadioButton denyRadioButton = new JRadioButton("Don't Recommend");
		buttonGroup.add(denyRadioButton);
		GridBagConstraints denyRadioButtonLocation = new GridBagConstraints();
		denyRadioButtonLocation.anchor = GridBagConstraints.WEST;
		denyRadioButtonLocation.insets = new Insets(0, 0, 5, 5);
		denyRadioButtonLocation.gridx = 1;
		denyRadioButtonLocation.gridy = 2;
		gridLocation.add(denyRadioButton, denyRadioButtonLocation);
		
		JRadioButton unsureRadioButton = new JRadioButton("Unsure");
		buttonGroup.add(unsureRadioButton);
		GridBagConstraints unsureRadioButtonLocation = new GridBagConstraints();
		unsureRadioButtonLocation.insets = new Insets(0, 0, 5, 5);
		unsureRadioButtonLocation.anchor = GridBagConstraints.WEST;
		unsureRadioButtonLocation.gridx = 1;
		unsureRadioButtonLocation.gridy = 3;
		gridLocation.add(unsureRadioButton, unsureRadioButtonLocation);
		
		JButton btnSeeReviews = new JButton("See Reviews");
		GridBagConstraints seeReviewsButtonLocation = new GridBagConstraints();
		seeReviewsButtonLocation.insets = new Insets(0, 0, 5, 5);
		seeReviewsButtonLocation.anchor = GridBagConstraints.WEST;
		seeReviewsButtonLocation.gridx = 1;
		seeReviewsButtonLocation.gridy = 4;
		gridLocation.add(btnSeeReviews, seeReviewsButtonLocation);
		
		JButton cancelButton = new JButton("Cancel");
		GridBagConstraints cancelButtonLocation = new GridBagConstraints();
		cancelButtonLocation.insets = new Insets(0, 0, 0, 5);
		cancelButtonLocation.gridx = 0;
		cancelButtonLocation.gridy = 5;
		cancelButton.addActionListener(new cancelAction());
		gridLocation.add(cancelButton, cancelButtonLocation);
		
		JButton submitRecommendationButton = new JButton("Submit Recommendation");
		GridBagConstraints submitRecommendationButtonLocation = new GridBagConstraints();
		submitRecommendationButtonLocation.insets = new Insets(0, 0, 0, 5);
		submitRecommendationButtonLocation.gridx = 1;
		submitRecommendationButtonLocation.gridy = 5;
		gridLocation.add(submitRecommendationButton, submitRecommendationButtonLocation); 
    }  
    /**
     * Resets all all of the information about the paper being constructed to submit.
     * Normally called when a user cancels or submits a paper.
     */
    private void resetPaperInformation() {
    	currentFilePath = "No file has been selected.";
    	authorsOfPaper.clear();
    }
    
    /**
     * Error message that displays if file selection process goes wrong.
     * @param theMessage
     */
    private void displayErrorMessage(final String theMessage) {
        JOptionPane.showMessageDialog(this, theMessage, "Error", JOptionPane.ERROR_MESSAGE);
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
	        	panelChanger.changeTo(SubmitRecomendation.PANEL_LOOKUP_NAME);
	                      
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
    private class cancelAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			resetPaperInformation();
			panelChanger.changeTo(SubmitRecomendation.PANEL_LOOKUP_NAME);			
		}   	
    }
}
