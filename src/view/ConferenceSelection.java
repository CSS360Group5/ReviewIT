package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import model.Conference;
import model.ConferenceSystem;

/**
 * A conference selection screen.
 *
 * @author Zachary Chandler
 */
public class ConferenceSelection extends PanelCard {

    /** The name to lookup this panel in a panel changer. */
    public static final String PANEL_LOOKUP_NAME = "CONFERENCE_SELECTION";
    
    /** A padding value used to determine the desired padding of several elements in the panel. */
    private static final int PADDING = 20;
    
    /** SVUID */
    private static final long serialVersionUID = -3013328871722353114L;
    
    public ConferenceSelection(PanelChanger p, UserContext context) {
        super(p, context);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
    }

    @Override
    public void updatePanel() {
        this.removeAll();
        
        ConferenceSystem sys =  ConferenceSystem.getInstance();
        Dimension panelSize = Main.BODY_SIZE;
        
        JLabel prompt = new JLabel("Choose a Conference");
        prompt.setAlignmentX(CENTER_ALIGNMENT);
        
        Conference[] actualConferences = sys.getConferences().toArray(new Conference[] {});
        
        Object[] tableHeader = new Object[] {"Conference", "Submission Deadline"};
        Object[][] values = new Object[actualConferences.length][tableHeader.length];
        
        for (int i = 0; i < actualConferences.length; i++) {
            values[i][0] = actualConferences[i];
            

            if (actualConferences[i].getInfo().isSubmissionOpen(new Date())) {
                values[i][1] = actualConferences[i].getInfo().getSubmissionDate();
            } else {
                values[i][1] = "CLOSED";
            }
        }
        
        JTable conferences = new JTable();
        conferences.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        DefaultTableModel tableModel = new DefaultTableModel(values, tableHeader) {
            /** SVUID */
            private static final long serialVersionUID = 5457480627821664367L;

            @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        
        conferences.setModel(tableModel);
        conferences.getColumnModel().getColumn(0).setPreferredWidth(panelSize.width);
        conferences.getColumnModel().getColumn(1).setMinWidth(190);
        
        JScrollPane scrollPane = new JScrollPane(conferences);
        conferences.setFillsViewportHeight(true);
        
        JPanel conferencesPanel = new JPanel();
        conferencesPanel.setLayout(new BorderLayout());
        conferencesPanel.setAlignmentX(CENTER_ALIGNMENT);
        conferencesPanel.add(conferences.getTableHeader(), BorderLayout.PAGE_START);
        conferencesPanel.add(scrollPane, BorderLayout.CENTER);
        
        JButton selectButton = new JButton("Select");
        selectButton.setAlignmentX(CENTER_ALIGNMENT);
        selectButton.setEnabled(false);
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                  context.setCurrentConference(getSelectedConference(conferences));
                  
                  if (context.getCurrentConference() == null) {
                      throw new IllegalStateException();
                  }
                  
                  panelChanger.changeTo(DashBoard.PANEL_LOOKUP_NAME);
            }
        });
        
        conferences.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                Conference c = getSelectedConference(conferences);
                
                selectButton.setEnabled(DashBoard.shouldShowAuthorPane(context.getUser(), c)
                        || DashBoard.shouldShowSubProgramChairPane(context.getUser(), c));
            }
        });
        
        this.add(prompt);
        this.add(conferencesPanel);
        this.add(selectButton);
    }

    @Override
    public String getNameOfPanel() {
        return PANEL_LOOKUP_NAME;
    }
    
    private static Conference getSelectedConference(JTable theTable) {
        int row = theTable.getSelectedRow();
        return (Conference) theTable.getModel().getValueAt(row, 0);
    }
}
