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
    
    /**
     * Create a new card for the given panel changer.
     * @param p the panel changer.
     */
    public PanelCard(PanelChanger p) {
        panelChanger = p;
        initializePanel();
    }
    
    /**
     * This method will be called by the super constructor to initialize the panel.
     */
    public abstract void initializePanel();
    
    /**
     * @return the name of the panel for the panel changer.
     */
    public abstract String getNameOfPanel();
}
