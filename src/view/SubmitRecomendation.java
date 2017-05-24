package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 
 * @author Kevin Nguyen
 *
 */
public class SubmitRecomendation extends PanelCard {

    /** The name to lookup this panel in a panel changer. */
    public static final String PANEL_LOOKUP_NAME = "SUBMIT_RECOMENDATION";
	private final ButtonGroup buttonGroup = new ButtonGroup();

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
    private JTable table = new JTable();
    /** */
    private JTextField paperTitleTextField = new JTextField();
    /** */
    private JTextField authorsTextField = new JTextField("Enter single author name");

    /** */
    private boolean initialSignIn;
    
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
    	JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JLabel lblSubProgramChair = new JLabel("Sub Program Chair:");
		panel_1.add(lblSubProgramChair);
		
		JLabel lblPaper = new JLabel("Paper:");
		panel_1.add(lblPaper, BorderLayout.SOUTH);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.WEST);
		
		JPanel panel_4 = new JPanel();
		panel.add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_5 = new JPanel();
		panel_4.add(panel_5, BorderLayout.NORTH);
		
		JLabel lblUploadARecommendation = new JLabel("Upload a recommendation File for the Paper and select your recommendation.");
		lblUploadARecommendation.setAlignmentY(LEFT_ALIGNMENT);
		panel_5.add(lblUploadARecommendation);
		
		JPanel panel_6 = new JPanel();
		panel_4.add(panel_6, BorderLayout.CENTER);
		GridBagLayout gbl_panel_6 = new GridBagLayout();
		gbl_panel_6.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel_6.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panel_6.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_6.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_6.setLayout(gbl_panel_6);
		
		JLabel lblFile = new JLabel("File:");
		GridBagConstraints gbc_lblFile = new GridBagConstraints();
		gbc_lblFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblFile.gridx = 0;
		gbc_lblFile.gridy = 0;
		panel_6.add(lblFile, gbc_lblFile);
		
		JButton btnChooseFile = new JButton("Choose File");
		GridBagConstraints gbc_btnChooseFile = new GridBagConstraints();
		gbc_btnChooseFile.anchor = GridBagConstraints.WEST;
		gbc_btnChooseFile.insets = new Insets(0, 0, 5, 5);
		gbc_btnChooseFile.gridx = 1;
		gbc_btnChooseFile.gridy = 0;
		btnChooseFile.addActionListener(new SelectFileAction());
		panel_6.add(btnChooseFile, gbc_btnChooseFile);
		
		JLabel lblNewLabel = new JLabel("New label");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 0;
		panel_6.add(lblNewLabel, gbc_lblNewLabel);
		
		JRadioButton rdbtnRecommend = new JRadioButton("Recommend");
		buttonGroup.add(rdbtnRecommend);
		GridBagConstraints gbc_rdbtnRecommend = new GridBagConstraints();
		gbc_rdbtnRecommend.anchor = GridBagConstraints.WEST;
		gbc_rdbtnRecommend.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnRecommend.gridx = 1;
		gbc_rdbtnRecommend.gridy = 1;
		panel_6.add(rdbtnRecommend, gbc_rdbtnRecommend);
		
		JRadioButton rdbtnDontRecommend = new JRadioButton("Don't Recommend");
		buttonGroup.add(rdbtnDontRecommend);
		GridBagConstraints gbc_rdbtnDontRecommend = new GridBagConstraints();
		gbc_rdbtnDontRecommend.anchor = GridBagConstraints.WEST;
		gbc_rdbtnDontRecommend.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnDontRecommend.gridx = 1;
		gbc_rdbtnDontRecommend.gridy = 2;
		panel_6.add(rdbtnDontRecommend, gbc_rdbtnDontRecommend);
		
		JRadioButton rdbtnUnsure = new JRadioButton("Unsure");
		buttonGroup.add(rdbtnUnsure);
		GridBagConstraints gbc_rdbtnUnsure = new GridBagConstraints();
		gbc_rdbtnUnsure.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnUnsure.anchor = GridBagConstraints.WEST;
		gbc_rdbtnUnsure.gridx = 1;
		gbc_rdbtnUnsure.gridy = 3;
		panel_6.add(rdbtnUnsure, gbc_rdbtnUnsure);
		
		JButton btnSeeReviews = new JButton("See Reviews");
		GridBagConstraints gbc_btnSeeReviews = new GridBagConstraints();
		gbc_btnSeeReviews.insets = new Insets(0, 0, 5, 5);
		gbc_btnSeeReviews.anchor = GridBagConstraints.WEST;
		gbc_btnSeeReviews.gridx = 1;
		gbc_btnSeeReviews.gridy = 4;
		panel_6.add(btnSeeReviews, gbc_btnSeeReviews);
		
		JButton btnCancel = new JButton("Cancel");
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.insets = new Insets(0, 0, 0, 5);
		gbc_btnCancel.gridx = 0;
		gbc_btnCancel.gridy = 5;
		panel_6.add(btnCancel, gbc_btnCancel);
		
		JButton btnSubmitRecommendation = new JButton("Submit Recommendation");
		GridBagConstraints gbc_btnSubmitRecommendation = new GridBagConstraints();
		gbc_btnSubmitRecommendation.insets = new Insets(0, 0, 0, 5);
		gbc_btnSubmitRecommendation.gridx = 1;
		gbc_btnSubmitRecommendation.gridy = 5;
		panel_6.add(btnSubmitRecommendation, gbc_btnSubmitRecommendation);
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
	            //displayErrorMessage("There was an error loading the selected file!");
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
			
		}
    	
    }
}
