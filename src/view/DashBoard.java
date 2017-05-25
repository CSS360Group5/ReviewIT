package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Conference;
import model.Paper;
import model.UserProfile;

/**
 * A class to display options to a user based on their role.
 *
 * @author Zachary Chandler
 */
public class DashBoard extends PanelCard {

    /** The name to lookup this panel in a panel changer. */
    public static final String PANEL_LOOKUP_NAME = "DASHBOARD";
    
    /** SVUID */
    private static final long serialVersionUID = 8350355413908915713L;

    /** A padding value used to determine the desired padding of several elements in the panel. */
    private static final int PADDING = 20;
    
    public DashBoard(PanelChanger p, UserContext context) {
        super(p, context);
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(PADDING/2, PADDING, PADDING/2, PADDING));
    }

    @Override
    public void updatePanel() {
        this.removeAll();

        Objects.requireNonNull(context.getCurrentConference());
        Objects.requireNonNull(context.getUser());
        
        List<Paper> submittedPapers = context.getCurrentConference().getInfo()
                .getPapersSubmittedBy(context.getUser());
        
        List<Paper> assignedPapers = context.getCurrentConference().getInfo()
                .getPapersAssignedToSubProgramChair(context.getUser());
        
        if (shouldShowSubProgramChairPane(context.getUser(), context.getCurrentConference())) {
            this.add(getSubChairPanel(assignedPapers));
            this.add(Box.createRigidArea(new Dimension(0, PADDING)));
        }
        
        if (shouldShowAuthorPane(context.getUser(), context.getCurrentConference())) {
            this.add(getAuthorPanel(submittedPapers));            
        }
    }

    /**
     * Check if the subprogram chair panel will be shown for the given user at a given conference.
     */
    public static boolean shouldShowSubProgramChairPane(UserProfile user, Conference c) {
        List<Paper> assignedPapers = c.getInfo().getPapersAssignedToSubProgramChair(user);
        return assignedPapers != null && !assignedPapers.isEmpty();
    }

    /**
     * Check if the author chair panel will be shown for the given user at a given conference.
     */
    public static boolean shouldShowAuthorPane(UserProfile user, Conference c) {
        List<Paper> submittedPapers = c.getInfo().getPapersSubmittedBy(user);
        return !submittedPapers.isEmpty() || c.getInfo().getSubmissionDate().after(new Date());
    }
    
    /**
     * Get the subprogram chair panel.
     */
    private JPanel getSubChairPanel(List<Paper> actualPapers) {
        int width = Main.WINDOW_SIZE.width - (PADDING * 2);
        
        JPanel result = new JPanel();
        result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));
        result.setAlignmentX(LEFT_ALIGNMENT);

        JLabel assignedLabel = new JLabel("Assigned Papers");
        assignedLabel.setAlignmentX(LEFT_ALIGNMENT);
                
        JList<Paper> assignedPapers = new JList<Paper>(actualPapers.toArray(new Paper[0]));
        assignedPapers.setAlignmentX(LEFT_ALIGNMENT);
        assignedPapers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        assignedPapers.setPreferredSize(new Dimension(width , Main.WINDOW_SIZE.height / 3));
        assignedPapers.setBorder(new CompoundBorder(new LineBorder(this.getBackground(), 3), 
                              new CompoundBorder(new LineBorder(Color.BLACK),
                                                 new EmptyBorder(PADDING, PADDING, PADDING, PADDING))));
        
        JPanel assignedPapersPanel = new JPanel();
        assignedPapersPanel.setAlignmentX(LEFT_ALIGNMENT);
        assignedPapersPanel.add(assignedPapers);
        
        JButton assignReviewerButton = new JButton("Assign Reviewer");
        assignReviewerButton.setAlignmentY(TOP_ALIGNMENT);
        assignReviewerButton.addActionListener(new AssignReviewerAction(assignedPapers));
        assignReviewerButton.setEnabled(false);
        
        JButton submitRecomendationButton = new JButton("Submit Recommendation");
        submitRecomendationButton.setAlignmentY(TOP_ALIGNMENT);
        submitRecomendationButton.addActionListener(new RecomendPaperAction(assignedPapers));
        submitRecomendationButton.setEnabled(false);
        //Added this button to go back to conferenc selection.
        //Needs spacing and will change if we implement a logo with a hyperlink
        JButton goToConferences = new JButton("Go Back");
        goToConferences.setAlignmentY(TOP_ALIGNMENT);
        goToConferences.setEnabled(true);
        goToConferences.addActionListener(new cancelAction());
        assignedPapers.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
            	//Buisness Constraint
                submitRecomendationButton.setEnabled(true);
                assignReviewerButton.setEnabled(!context.getCurrentConference().getInfo().isSubmissionOpen(new Date()));
            }
        });
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.setAlignmentX(LEFT_ALIGNMENT);
        buttonsPanel.add(assignReviewerButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonsPanel.add(submitRecomendationButton);
        buttonsPanel.add(goToConferences);
        
        result.add(assignedLabel);
        result.add(assignedPapersPanel);
        result.add(buttonsPanel);
        
        result.setMaximumSize(new Dimension(width, Main.WINDOW_SIZE.height / 2 - PADDING * 2));
        
        return result;
    }

    /**
     * Get the author panel.
     */
    private JPanel getAuthorPanel(List<Paper> actualPapers) {
        int width = Main.WINDOW_SIZE.width - (PADDING * 2);

        JPanel result = new JPanel();
        result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));
        result.setAlignmentX(LEFT_ALIGNMENT);

        JLabel submittedLabel = new JLabel("Submitted Papers");
        submittedLabel.setAlignmentX(LEFT_ALIGNMENT);
        
        JList<Paper> submitedPapers = new JList<Paper>(actualPapers.toArray(new Paper[0]));
        submitedPapers.setAlignmentX(LEFT_ALIGNMENT);
        submitedPapers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        submitedPapers.setPreferredSize(new Dimension(width , Main.WINDOW_SIZE.height / 3));
        submitedPapers.setBorder(new CompoundBorder(new LineBorder(this.getBackground(), 3), 
                              new CompoundBorder(new LineBorder(Color.BLACK),
                                                 new EmptyBorder(PADDING, PADDING, PADDING, PADDING))));
        
        JPanel submittedPapersPanel = new JPanel();
        submittedPapersPanel.setAlignmentX(LEFT_ALIGNMENT);
        submittedPapersPanel.add(submitedPapers);
        
        JButton submitPaperButton = new JButton("Submit New Paper...");
        submitPaperButton.setAlignmentY(TOP_ALIGNMENT);
        submitPaperButton.addActionListener(new SubmitPaperAction());
        submitPaperButton.setEnabled(context.getCurrentConference().getInfo().isSubmissionOpen(new Date()));
        
        //added by Ian to prevent author from being able to press button to submit >limit of papers 
        //to a conference. Follows heuristic of not allowing user to enter information.
        List<String> authors = new LinkedList<>();
        authors.add(context.getUser().getName());
        Paper thePaper = Paper.createPaper(new File(""), authors, "Test title", context.getUser());
        submitPaperButton.setEnabled(context.getCurrentConference().getInfo().isPaperInAuthorSubmissionLimit(thePaper));
        
        JButton removePaperButton = new JButton("Remove Paper");
        removePaperButton.setAlignmentY(TOP_ALIGNMENT);
        removePaperButton.addActionListener(new RemovePaperAction(submitedPapers));
        removePaperButton.setEnabled(false);
        
        //Added this button to go back to conferenc selection.
        //Needs spacing and will change if we implement a logo with a hyperlink
        JButton goToConferencesButton = new JButton("Go Back");
        goToConferencesButton.setAlignmentY(TOP_ALIGNMENT);
        goToConferencesButton.setEnabled(true);
        goToConferencesButton.addActionListener(new cancelAction());
        
        submitedPapers.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                removePaperButton.setEnabled(context.getCurrentConference().getInfo().getReviewersForPaper(
                        submitedPapers.getSelectedValue()).isEmpty());
            }
        });
        
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.setAlignmentX(LEFT_ALIGNMENT);
        buttonsPanel.add(removePaperButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonsPanel.add(submitPaperButton);
        buttonsPanel.add(goToConferencesButton);
        
        result.add(submittedLabel);
        result.add(submittedPapersPanel);
        result.add(buttonsPanel);
        
        result.setMaximumSize(new Dimension(width, Main.WINDOW_SIZE.height / 2 - PADDING * 2));
        
        return result;
    }
    
    @Override
    public String getNameOfPanel() {
        return PANEL_LOOKUP_NAME;
    }

    private class AssignReviewerAction implements ActionListener {

        private JList<Paper> papers;

        public AssignReviewerAction(JList<Paper> assignedPapers) {
            this.papers = assignedPapers;
        }
        
        @Override
        public void actionPerformed(ActionEvent arg0) {
            context.setPaper(papers.getSelectedValue());
            
            if (context.getPaper() == null) {
                throw new IllegalStateException();
            }
            
            panelChanger.changeTo(AssignReviewer.PANEL_LOOKUP_NAME);
        }
    }
    
    private class RecomendPaperAction implements ActionListener {

        private JList<Paper> papers;

        public RecomendPaperAction(JList<Paper> assignedPapers) {
            this.papers = assignedPapers;
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {
            context.setPaper(papers.getSelectedValue());
            
            if (context.getPaper() == null) {
                throw new IllegalStateException();
            }
            
            panelChanger.changeTo(SubmitRecomendation.PANEL_LOOKUP_NAME);
        }
    }
    
    private class SubmitPaperAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            panelChanger.changeTo(SubmitPaper.PANEL_LOOKUP_NAME);
        }
    }
    
    private class RemovePaperAction implements ActionListener {

        private JList<Paper> papers;

        public RemovePaperAction(JList<Paper> submitedPapers) {
            this.papers = submitedPapers;
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {
            context.setPaper(papers.getSelectedValue());
            
            if (context.getPaper() == null) {
                throw new IllegalStateException();
            }
            
            // opens a confirmation dialog box with yes/no/cancel
            int result = JOptionPane.showConfirmDialog(null, 
            		"Are you sure you want to remove this paper?");
            if (result == JOptionPane.YES_OPTION) {
            	context.getCurrentConference().getUserRole().removePaper(context.getUser(), papers.getSelectedValue());
				JOptionPane.showMessageDialog(null, "Paper has been successfully removed.");
				
            }
        }
    }
    private class cancelAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			panelChanger.changeTo(ConferenceSelection.PANEL_LOOKUP_NAME);			
		}   	
    }
}
