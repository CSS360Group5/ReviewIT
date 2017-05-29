package view;

import java.awt.CardLayout;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.TreeMap;

import javax.swing.JPanel;

/**
 * A class to handle changing the panel of a CardLayout. It initializes and creates a JPanel for the CardLayout.
 *
 * @author Zachary Chandler
 */
public class PanelChanger extends Observable {

    /** The layout of the panel. */
    private CardLayout cardLayout;
    
    /** The actual panel. */
    private JPanel parent;
    
    private Map<String, PanelCard> cards;

    private LinkedList<String> panelNameHistory;
    
    /**
     * Creates a panel changer without any panels in it.
     */
    public PanelChanger() {
        cardLayout = new CardLayout();
        parent = new JPanel(cardLayout);
        cards = new TreeMap<>();
        panelNameHistory = new LinkedList<>();
    }
    
    /**
     * Add a card to the panel changer with the lookup string of PanelCard.getName().
     * @param card the card to add.
     * @throws NullPointerException if card is null.
     * @throws IllegalArgumentException if a card with that name has already been added.
     */
    public void addCard(PanelCard card) {
        Objects.requireNonNull(card);
        
        if (cards.containsKey(card.getNameOfPanel())) {
            throw new IllegalArgumentException();
        }
        
        parent.add(card, card.getNameOfPanel());
        cards.put(card.getNameOfPanel(), card);
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
     * @throws NullPointerExcetpion if panelName is null.
     * @throws IllegalArgumentException if panelName isn't a name of any current panels.
     */
    public void changeTo(String panelName) {
        Objects.requireNonNull(panelName);
        
        PanelCard card = cards.get(panelName);
        if (card == null) {
            throw new IllegalArgumentException();
        }
        
        cardLayout.show(parent, panelName);
        card.setVisible(false);
        card.updatePanel();
        card.setVisible(true);
        card.repaint();
        
        panelNameHistory.push(panelName);
        this.setChanged();
        this.notifyObservers();
    }

    public void back() {
        panelNameHistory.pop();
        changeTo(panelNameHistory.pop());
    }

    public String getCurrentPanelName() {
        return panelNameHistory.peek();
    }
    
}
