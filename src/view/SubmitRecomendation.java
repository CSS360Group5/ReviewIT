package view;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.Paper;

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
    private JButton submitRecommendationButton = new JButton("Submit Recommendation");
    private JLabel fileNameLabel;
    /** */
    private String currentFilePath = "No file has been selected.";
    private String theRadioButtonRecommendationSelection;
    private JTable table = new JTable();
    
    public SubmitRecomendation(PanelChanger p, UserContext context) {
        super(p, context);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
//        Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
//        this.setBackground(Color.RED);
//        this.setBorder(raisedetched);
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
    	JPanel mainPanel = new JPanel();
		add(mainPanel);
		mainPanel.setLayout(new BorderLayout(Main.WINDOW_SIZE.height / 3 , Main.WINDOW_SIZE.height / 6));
		
		JPanel namePanel = new JPanel();
		mainPanel.add(namePanel, BorderLayout.NORTH);
		namePanel.setLayout(new BorderLayout(0, 0));
		
		JLabel SubProgramChairlabel = new JLabel("Sub Program Chair:  " + context.getUser().getName());
		namePanel.add(SubProgramChairlabel);
		
		JLabel labelPaper = new JLabel("Paper:		"+ context.getPaper().getTitle());
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
		fileLabelLocation.insets = new Insets(50, 0, 5, 5);
		fileLabelLocation.gridx = 0;
		fileLabelLocation.gridy = 0;
        final FileNameExtensionFilter fileTypeFilter = new FileNameExtensionFilter(
                "docx, doc, and pdf", "docx", "doc", "pdf");
        fileChooser.setFileFilter(fileTypeFilter);
		gridLocation.add(fileLabel, fileLabelLocation);
		
		JButton btnChooseFile = new JButton("Choose File");
		GridBagConstraints gbc_btnChooseFile = new GridBagConstraints();
		gbc_btnChooseFile.anchor = GridBagConstraints.WEST;
		gbc_btnChooseFile.insets = new Insets(50, 0, 5, 5);
		gbc_btnChooseFile.gridx = 1;
		gbc_btnChooseFile.gridy = 0;
		btnChooseFile.addActionListener(new SelectFileAction());
		gridLocation.add(btnChooseFile, gbc_btnChooseFile);
		
		fileNameLabel = new JLabel(currentFilePath);
		GridBagConstraints fileNameLabelLocation = new GridBagConstraints();
		fileNameLabelLocation.insets = new Insets(50, 0, 5, 5);
		fileNameLabelLocation.gridx = 2;
		fileNameLabelLocation.gridy = 0;
		gridLocation.add(fileNameLabel, fileNameLabelLocation);
		
		JRadioButton recommendRadioButton= new JRadioButton("Recommend");
		recommendRadioButton.setActionCommand(recommendRadioButton.getText());
		recommendRadioButton.addActionListener(new checkRequirementsForRadioButtons());
		buttonGroup.add(recommendRadioButton);
		GridBagConstraints recommendRadioButtonLocation= new GridBagConstraints();
		recommendRadioButtonLocation.anchor = GridBagConstraints.WEST;
		recommendRadioButtonLocation.insets = new Insets(0, 0, 5, 5);
		recommendRadioButtonLocation.gridx = 1;
		recommendRadioButtonLocation.gridy = 1;
		gridLocation.add(recommendRadioButton, recommendRadioButtonLocation);
		
		JRadioButton denyRadioButton = new JRadioButton("Don't Recommend");
		denyRadioButton.setActionCommand(denyRadioButton.getText());
		denyRadioButton.addActionListener(new checkRequirementsForRadioButtons());
		buttonGroup.add(denyRadioButton);
		GridBagConstraints denyRadioButtonLocation = new GridBagConstraints();
		denyRadioButtonLocation.anchor = GridBagConstraints.WEST;
		denyRadioButtonLocation.insets = new Insets(0, 0, 5, 5);
		denyRadioButtonLocation.gridx = 1;
		denyRadioButtonLocation.gridy = 2;
		gridLocation.add(denyRadioButton, denyRadioButtonLocation);
		
		JRadioButton unsureRadioButton = new JRadioButton("Unsure");
		unsureRadioButton.setActionCommand(unsureRadioButton.getText());
		unsureRadioButton.addActionListener(new checkRequirementsForRadioButtons());
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
		
		JButton cancelButton = new JButton("Go Back");
		GridBagConstraints cancelButtonLocation = new GridBagConstraints();
		cancelButtonLocation.insets = new Insets(50, 0, 0, 5);
		cancelButtonLocation.gridx = 0;
		cancelButtonLocation.gridy = 5;
		cancelButton.addActionListener(new cancelAction());
		gridLocation.add(cancelButton, cancelButtonLocation);
		
		submitRecommendationButton.setEnabled(false);
		GridBagConstraints submitRecommendationButtonLocation = new GridBagConstraints();
		submitRecommendationButtonLocation.insets = new Insets(50, 0, 0, 5);
		submitRecommendationButtonLocation.gridx = 1;
		submitRecommendationButtonLocation.gridy = 5;
		submitRecommendationButton.addActionListener(new submitAction());
		gridLocation.add(submitRecommendationButton, submitRecommendationButtonLocation); 
    }  
    private class submitAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//submit paper
			//theRadioButtonRecommendationSelection = buttonGroup.getSelection().getActionCommand();
			//Debug print statement
			System.out.println(theRadioButtonRecommendationSelection);

			try {
				context.getPaper().setRecommendationShort(theRadioButtonRecommendationSelection);
				File theRecommendationFile = fileChooser.getSelectedFile();
				context.getPaper().setMyRecommendation(theRecommendationFile);	
				panelChanger.changeTo(DashBoard.PANEL_LOOKUP_NAME);
			} catch (IllegalArgumentException ex) {
				//displayErrorMessage("Paper could not be submitted due to invalid input");
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
	        	fileNameLabel.setText(currentFilePath);
	        	checkRequirementsRecommendationFile();          
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
			panelChanger.changeTo(DashBoard.PANEL_LOOKUP_NAME);			
		}   	
    }
}
