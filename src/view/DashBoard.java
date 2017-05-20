package view;

import javax.swing.JLabel;

public class DashBoard extends PanelCard {

    /** The name to lookup this panel in a panel changer. */
    public static final String PANEL_LOOKUP_NAME = "DASHBOARD";

    /** SVUID */
    private static final long serialVersionUID = 8350355413908915713L;
    
    public DashBoard(PanelChanger p) {
        super(p);
    }

    @Override
    public void initializePanel() {
        JLabel text = new JLabel(PANEL_LOOKUP_NAME);
        text.setHorizontalAlignment(JLabel.CENTER);
        this.add(text);
    }

    @Override
    public String getNameOfPanel() {
        return PANEL_LOOKUP_NAME;
    }
}
