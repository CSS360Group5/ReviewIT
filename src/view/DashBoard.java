package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import model.Conference;
import model.Paper;
import model.Review;
import model.UserProfile;
import model.SubprogramUtilities.RecommendStatus;

/**
 * A class to display options to a user based on their role.
 *
 * @author Zachary Chandler
 * @author Brian Geving
 */
public class DashBoard extends PanelCard {

    /** The name to lookup this panel in a panel changer. */
    public static final String PANEL_LOOKUP_NAME = "DASHBOARD";
    
    /** SVUID */
    private static final long serialVersionUID = 8350355413908915713L;

    /** A padding value used to determine the desired padding of several elements in the panel. */
    private static final int PADDING = 20;
    
    /**
     * See PanelCard constructor for more details.
     * 
     * @param p the panel changer of this panel card.
     * @param context the context of this panel card.
     */
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
        int width = Main.BODY_SIZE.width - (PADDING * 2);
        
        JPanel result = new JPanel();
        result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));
        result.setAlignmentX(LEFT_ALIGNMENT);

        JLabel assignedLabel = new JLabel("Assigned Papers");
        assignedLabel.setAlignmentX(LEFT_ALIGNMENT);
        
        JTable assignedPapers = getAssignedPapersTable(actualPapers);
        
        JScrollPane scrollPane = new JScrollPane(assignedPapers);
        assignedPapers.setFillsViewportHeight(true);
        
        JPanel assignedPapersPanel = new JPanel();
        assignedPapersPanel.setLayout(new BorderLayout());
        assignedPapersPanel.setAlignmentX(LEFT_ALIGNMENT);
        assignedPapersPanel.add(assignedPapers.getTableHeader(), BorderLayout.PAGE_START);
        assignedPapersPanel.add(scrollPane, BorderLayout.CENTER);
        
        JButton assignReviewerButton = new JButton("Assign Reviewer");
        assignReviewerButton.setAlignmentY(TOP_ALIGNMENT);
        assignReviewerButton.addActionListener(new AssignReviewerAction(assignedPapers));
        assignReviewerButton.setEnabled(false);
        
        JButton submitRecomendationButton = new JButton("Submit Recommendation");
        submitRecomendationButton.setAlignmentY(TOP_ALIGNMENT);
        submitRecomendationButton.addActionListener(new RecomendPaperAction(assignedPapers));
        submitRecomendationButton.setEnabled(false);
        
        assignedPapers.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                Paper p = getSelectedPaper(assignedPapers);
                
                submitRecomendationButton.setEnabled(p.getReviews().size() > 2);
                assignReviewerButton.setEnabled(!context.getCurrentConference().getInfo().isSubmissionOpen(new Date()));
            }
        });
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.setAlignmentX(LEFT_ALIGNMENT);
        buttonsPanel.add(assignReviewerButton);
        buttonsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonsPanel.add(submitRecomendationButton);
        
        result.add(assignedLabel);
        result.add(assignedPapersPanel);
        result.add(buttonsPanel);
        
        result.setMaximumSize(new Dimension(width, Main.BODY_SIZE.height / 2 - PADDING * 2));
        
        return result;
    }

    /**
     * Get a JTable to display the information of a subprogram chair's assigned papers
     * @param actualPapers the papers assigned to the subprogram chair.
     * @return the JTable for the assigned papers.
     */
    private JTable getAssignedPapersTable(List<Paper> actualPapers) {
        String[] collumnNames = new String[] {"Papers", "R1", "R2", "R3", "Recommendation"};
        Object[][] papers = new Object[actualPapers.size()][collumnNames.length];
        
        for (int i = 0; i < actualPapers.size(); i++) {
            Paper p = actualPapers.get(i);
            Review[] reviews = p.getReviews().toArray(new Review[0]);
            int amountOfReviewers = context.getCurrentConference().getInfo().getReviewersForPaper(p).size();
            
            papers[i][0] = p;
            
            int j;
            
            for (j = 0; j < 3 && j < reviews.length; j++) {
                papers[i][j+1] = reviews[j].score;
            }
            
            for (; j < 3; j++) {
                papers[i][j+1] = j + 1 > amountOfReviewers ? "Not Assigned" : "Not Submitted";
            }
            
            Review recommendation = p.getMyRecommendation();
            
            if (recommendation == null) {
                papers[i][collumnNames.length - 1] = "";
            } else if (recommendation.score == RecommendStatus.YES.intRepresentation) {
                papers[i][collumnNames.length - 1] = "yes";
            } else if (recommendation.score == RecommendStatus.NO.intRepresentation) {
                papers[i][collumnNames.length - 1] = "no";
            } else if (recommendation.score == RecommendStatus.NOT_SURE.intRepresentation) {
                papers[i][collumnNames.length - 1] = "?";
            } else {
                throw new IllegalStateException();
            }
        }
    
        JTable assignedPapers = new JTable();
        assignedPapers.setAlignmentX(LEFT_ALIGNMENT);
        assignedPapers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
        DefaultTableModel tableModel = new DefaultTableModel(papers, collumnNames) {
            /** SVUID */
            private static final long serialVersionUID = 5457480627821664367L;
    
            @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
    
        assignedPapers.setModel(tableModel);
    
        TableColumn column = null;
        for (int i = 0; i < collumnNames.length; i++) {
            column = assignedPapers.getColumnModel().getColumn(i);
            
            if (i == 0) {
                column.setPreferredWidth(1000); //third column is bigger
            } else if (i == collumnNames.length - 1) {
                column.setMinWidth(120);
            } else {
                column.setMinWidth(30);
            }
        }
        
        return assignedPapers;
    }

    /**
     * Get the author panel.
     */
    private JPanel getAuthorPanel(List<Paper> actualPapers) {
        int width = Main.BODY_SIZE.width - (PADDING * 2);

        JPanel result = new JPanel();
        result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));
        result.setAlignmentX(LEFT_ALIGNMENT);

        JLabel submittedLabel = new JLabel("Submitted Papers");
        submittedLabel.setAlignmentX(LEFT_ALIGNMENT);
        
        
        JTable submitedPapers = getAuthoredPapersTable(actualPapers);
        
        JScrollPane scrollPane = new JScrollPane(submitedPapers);
        submitedPapers.setFillsViewportHeight(true);
        
        JPanel submittedPapersPanel = new JPanel();
        submittedPapersPanel.setLayout(new BorderLayout());
        submittedPapersPanel.setAlignmentX(LEFT_ALIGNMENT);
        submittedPapersPanel.add(submitedPapers.getTableHeader(), BorderLayout.PAGE_START);
        submittedPapersPanel.add(scrollPane, BorderLayout.CENTER);
        
        JButton submitPaperButton = new JButton("Submit New Paper...");
        submitPaperButton.setAlignmentY(TOP_ALIGNMENT);
        submitPaperButton.addActionListener(new SubmitPaperAction());

        //added by Ian to prevent author from being able to press button to submit >limit of papers 
        //to a conference. Follows heuristic of not allowing user to enter information.
        List<String> authors = new LinkedList<>();
        authors.add(context.getUser().getName());
        Paper thePaper = Paper.createPaper(new File(""), authors, "Test title", context.getUser());
        submitPaperButton.setEnabled(context.getCurrentConference().getInfo().isSubmissionOpen(new Date())
        		&& context.getCurrentConference().getInfo().isPaperInAuthorSubmissionLimit(thePaper));
        
        JButton removePaperButton = new JButton("Remove Paper");
        removePaperButton.setAlignmentY(TOP_ALIGNMENT);
        removePaperButton.addActionListener(new RemovePaperAction(submitedPapers));
        removePaperButton.setEnabled(false);
        
        submitedPapers.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                removePaperButton.setEnabled(context.getCurrentConference().getInfo().getReviewersForPaper(
                        getSelectedPaper(submitedPapers)).isEmpty());
            }
        });
        
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.setAlignmentX(LEFT_ALIGNMENT);
        buttonsPanel.add(removePaperButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        buttonsPanel.add(submitPaperButton);
        
        result.add(submittedLabel);
        result.add(submittedPapersPanel);
        result.add(buttonsPanel);
        
        result.setMaximumSize(new Dimension(width, Main.BODY_SIZE.height / 2 - PADDING * 2));
        
        return result;
    }

    /**
     * @param actualPapers the papers this author has authored.
     * @return the JTable of the papers to display the actualPapers
     */
    private JTable getAuthoredPapersTable(List<Paper> actualPapers) {
        String[] collumnNames = new String[] {"Paper", "Authors", "Date Submitted"};
        Object[][] values = new Object[actualPapers.size()][collumnNames.length];
        
        for (int i = 0; i < actualPapers.size(); i++) {
            Paper p = actualPapers.get(i); //this was .get(0), changed because it only displayed first paper
            values[i][0] = p;
            values[i][1] = stringListToString(p.getAuthors());
            values[i][2] = p.getSubmitDate();
        }
        
        
        JTable submitedPapers = new JTable();
        submitedPapers.setAlignmentX(LEFT_ALIGNMENT);
        submitedPapers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        DefaultTableModel tableModel = new DefaultTableModel(values, collumnNames) {
            /** SVUID */
            private static final long serialVersionUID = 5457480627821664367L;

            @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        
        submitedPapers.setModel(tableModel);
        return submitedPapers;
    }
    
    /**
     * @param strings a list of strings to turn into a single string
     * @return a string merge of all of the strings in the list separated by ", "
     */
    private static String stringListToString(List<String> strings) {
        StringBuilder result = new StringBuilder();
        
        Iterator<String> iter = strings.iterator();
        
        if (iter.hasNext()) {
            result.append(iter.next());
        }
        
        while(iter.hasNext()) {
            result.append(", ");
            result.append(iter.next());
        }
        
        return result.toString();
    }

    /**
     * Gets a selected paper from a valid table.
     * @param theTable a table in which the selected row's zeroth column holds a paper.
     * @return a selected paper from a JTable.
     */
    private static Paper getSelectedPaper(JTable theTable) {
        int row = theTable.getSelectedRow();
        return (Paper) theTable.getModel().getValueAt(row, 0);
    }
    
    @Override
    public String getNameOfPanel() {
        return PANEL_LOOKUP_NAME;
    }

    /**
     * An action to change to the assign reviewer panel and set the current paper of the context. The currently selected
     * paper in the assignedPapers JTable will be set to be the current paper in the context after this action is 
     * performed. 
     * 
     * @author Zachary Chandler
     */
    private class AssignReviewerAction implements ActionListener {

        private JTable papers;

        public AssignReviewerAction(JTable assignedPapers) {
            this.papers = assignedPapers;
        }
        
        @Override
        public void actionPerformed(ActionEvent arg0) {
            context.setPaper(getSelectedPaper(papers));
            
            if (context.getPaper() == null) {
                throw new IllegalStateException();
            }
            
            panelChanger.changeTo(AssignReviewer.PANEL_LOOKUP_NAME);
        }
    }

    /**
     * An action to change to the recommend panel and set the current paper of the context. The currently selected
     * paper in the assignedPapers JTable will be set to be the current paper in the context after this action is 
     * performed. 
     * 
     * @author Zachary Chandler
     */
    private class RecomendPaperAction implements ActionListener {

        private JTable papers;

        public RecomendPaperAction(JTable assignedPapers) {
            this.papers = assignedPapers;
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {
            context.setPaper(getSelectedPaper(papers));
            
            if (context.getPaper() == null) {
                throw new IllegalStateException();
            }
            
            panelChanger.changeTo(SubmitRecomendation.PANEL_LOOKUP_NAME);
        }
    }

    /**
     * An action to change to the submit paper panel and set the current paper of the context. The currently selected
     * paper in the assignedPapers JTable will be set to be the current paper in the context after this action is 
     * performed. 
     * 
     * @author Zachary Chandler
     */
    private class SubmitPaperAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            panelChanger.changeTo(SubmitPaper.PANEL_LOOKUP_NAME);
        }
    }
    
    /**
     * An action to remove the currently selected paper from the submitedPapers JTable.
     *
     * @author Brian Geving
     */
    private class RemovePaperAction implements ActionListener {

        private JTable papers;

        public RemovePaperAction(JTable submitedPapers) {
            this.papers = submitedPapers;
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {
            Paper p = getSelectedPaper(papers);
            
            context.setPaper(p);
            
            if (context.getPaper() == null) {
                throw new IllegalStateException();
            }
            
            // opens a confirmation dialog box with yes/no/cancel
            int result = JOptionPane.showConfirmDialog(null, 
            		"Are you sure you want to remove this paper?", "Confirm remove", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
            	try { //to test if paper can be removed (no reviewers assigned)
                	context.getCurrentConference().getUserRole().removePaper(context.getUser(), p);
    				panelChanger.changeTo(DashBoard.PANEL_LOOKUP_NAME);
//    				JOptionPane.showMessageDialog(null, "Paper has been successfully removed.");

            	} catch (IllegalArgumentException ex) {
            		
            	}
            }
        }
    }
}
