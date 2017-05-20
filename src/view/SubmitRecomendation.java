package view;

public class SubmitRecomendation extends PanelCard {
    
    /** The name to lookup this panel in a panel changer. */
    public static final String PANEL_LOOKUP_NAME = "SUBMIT_RECOMENDATION";

    /** SVUID */
    private static final long serialVersionUID = 3548345257787147831L;

    public SubmitRecomendation(PanelChanger p, UserContext context) {
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
