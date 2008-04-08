package colab.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import colab.common.naming.UserName;

/**
 * Parent class for all client-side channel panels.
 */
public abstract class ClientChannelPanel extends JPanel {

    private final UserName username;

    public UserName getUsername() {
        return username;
    }

    private ArrayList<ActionListener> listeners;

    public ClientChannelPanel(final UserName username) {

        this.username = username;

        listeners = new ArrayList<ActionListener>();
    }

    /**
     * Adds an ActionListener of a certain type to an element.
     *
     * @param listener the particular ActionListener
     */
    public final void addActionListener(final ActionListener listener) {
        listeners.add(listener);
    }

    /**
     * Fires when an ActionEvent occurs.
     *
     * @param event the ActionEvent that occurs
     */
    protected final void fireActionPerformed(final ActionEvent event) {

        for (final ActionListener listener : listeners) {
            listener.actionPerformed(event);
        }
    }
}
