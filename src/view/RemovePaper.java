package view;

public class RemovePaper extends PanelCard {
    
    /** The name to lookup this panel in a panel changer. */
    public static final String PANEL_LOOKUP_NAME = "REMOVE_PAPER";

    /** SVUID */
    private static final long serialVersionUID = 3199779118935711601L;

    public RemovePaper(PanelChanger p) {
        super(p);
    }

    @Override
    public void initializePanel() {
        // TODO Auto-generated method stub

    }

    @Override
    public String getNameOfPanel() {
        return PANEL_LOOKUP_NAME;
    }

}
