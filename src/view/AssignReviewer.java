package view;

public class AssignReviewer extends PanelCard {
    
    /** The name to lookup this panel in a panel changer. */
    public static final String PANEL_LOOKUP_NAME = "ASSIGN_REVIEWER";

    /** SVUID */
    private static final long serialVersionUID = 5949259200759242048L;

    public AssignReviewer(PanelChanger p, UserContext context) {
        super(p, context);
    }

    @Override
    public void updatePanel() {
        // TODO Auto-generated method stub

    }

    @Override
    public String getNameOfPanel() {
        return PANEL_LOOKUP_NAME;
    }

}
