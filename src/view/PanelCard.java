package view;

import javax.swing.JPanel;

/**
 * A class to handle the initialization of a panel and to enforce inheriting classes to return a name for the card 
 * lookup in a PanelChanger.
 *
 * @author Zachary Chandler
 */
public abstract class PanelCard extends JPanel {

    /** SVUID */
    private static final long serialVersionUID = -7713021751090444772L;
    
    /** The panel changer for this card. */
    protected final PanelChanger panelChanger;

    /** The context of the panel. */
    protected final UserContext context;
    
    /**
     * Create a new card for the given panel changer.
     * @param p the panel changer.
     */
    public PanelCard(PanelChanger p, UserContext context) {
        this.panelChanger = p;
        this.context = context;
    }
    
    /**
     * Load the panel. Load the panel with up to date values. This method gets called right before the panel is shown.
     * This is not an initialize method, and the items in the panel will still be there when this method is called.
     */
    protected abstract void updatePanel();
    
    /**
     * @return the name of the panel for the panel changer.
     */
    public abstract String getNameOfPanel();
}
