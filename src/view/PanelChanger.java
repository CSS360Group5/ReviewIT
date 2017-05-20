package view;

import java.awt.CardLayout;
import java.util.Objects;

import javax.swing.JPanel;

/**
 * A class to handle changing the panel of a CardLayout. It initializes and creates a JPanel for the CardLayout.
 *
 * @author Zachary Chandler
 */
public class PanelChanger {

    /** The layout of the panel. */
    private CardLayout cardLayout;
    
    /** The actual panel. */
    private JPanel parent;
    
    /**
     * Creates a panel changer without any panels in it.
     */
    public PanelChanger() {
        cardLayout = new CardLayout();
        parent = new JPanel(cardLayout);
    }
    
    /**
     * Add a card to the panel changer with the lookup string of PanelCard.getName().
     * @param card the card to add.
     * @throws NullPointerException if card is null.
     */
    public void addCard(PanelCard card) {
        Objects.requireNonNull(card);
        parent.add(card, card.getName());
    }
    
    /**
     * No content in the JPanel should be changed by the caller of this method.
     * 
     * @return The JPanel of this panel changer.
     */
    public JPanel getJPanel() {
        return parent;
    }
    
    /**
     * Changes the window to display the panel of panelName.
     * @param panelName
     */
    public void changeTo(String panelName) {
        cardLayout.show(parent, panelName);
    }
}
