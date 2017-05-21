package view;

public class SubmitPaper extends PanelCard {

    /** The name to lookup this panel in a panel changer. */
    public static final String PANEL_LOOKUP_NAME = "SUBMIT_PAPER";
    
    /** SVUID */
    private static final long serialVersionUID = 8305415430621852696L;

    public SubmitPaper(PanelChanger p, UserContext context) {
        super(p, context);
    }

    @Override
    public void updatePanel() {
    	this.removeAll();

    }

    @Override
    public String getNameOfPanel() {
        return PANEL_LOOKUP_NAME;
    }

}
