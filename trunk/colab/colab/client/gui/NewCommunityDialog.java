package colab.client.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import colab.client.ColabClient;
import colab.common.DebugManager;
import colab.common.exception.CommunityAlreadyExistsException;
import colab.common.exception.NetworkException;
import colab.common.naming.CommunityName;
import colab.server.user.Password;

public class NewCommunityDialog extends JDialog {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private final JTextField commName;

    private final JPasswordField commPass;

    private final JPasswordField confirmCommPass;

    private final ColabClient client;

    private final ChooseCommunityPanel parentPanel;

    public NewCommunityDialog(final ChooseCommunityPanel parentPanel,
            final ColabClient client) {

        this.client = client;
        this.parentPanel = parentPanel;

        JButton createButton = new JButton("Create Community");
        commName = new JTextField("");
        commPass = new JPasswordField("");
        confirmCommPass = new JPasswordField("");
        JLabel nameLabel = new JLabel("Community name: ");
        JLabel passLabel = new JLabel("Community password: ");
        JLabel confirmPassLabel = new JLabel("Confirm community password: ");

        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {

                boolean createOK = false;

                try {
                    handleCreate(e);

                    createOK = true;

                } catch (NetworkException e1) {
                    DebugManager.network(e1);
                } catch (CommunityAlreadyExistsException ce) {
                    DebugManager.exception(ce);
                } catch (RemoteException re) {
                    DebugManager.remote(re);
                }

                if (!createOK) {

                    // TODO Handle this in parent
                    JOptionPane.showMessageDialog(NewCommunityDialog.this,
                            "The connection to the server was lost. Double-check that the server is running.",
                            "Connection to Server Lost",
                            JOptionPane.ERROR_MESSAGE);

                    client.exitProgram();
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                closeWindow();
            }
        });

        //this.setPreferredSize(new Dimension(800,400));
        setLayout(new GridLayout(4, 2));
        add(nameLabel);
        add(commName);
        add(passLabel);
        add(commPass);
        add(confirmPassLabel);
        add(confirmCommPass);
        add(createButton);
        add(cancelButton);

        pack();

        this.setModal(true);

    }

    private void showErrorBox(final String message, final String title) {
        JOptionPane.showMessageDialog(
                this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private void handleCreate(final ActionEvent e) throws NetworkException,
        RemoteException, CommunityAlreadyExistsException {

        if (commName.getText().compareTo("") == 0
                || commPass.getPassword().length == 0
                || confirmCommPass.getPassword().length == 0) {

            return;

        }

        if (!Arrays.equals(
                commPass.getPassword(),
                confirmCommPass.getPassword())) {

            showErrorBox(
                  "The password fields do not agree. Please enter the "
                + "desired password again. (The password is case sensitive!)",
                "Password Error");

            commPass.setText("");
            confirmCommPass.setText("");

            return;

        }

        boolean flagged = false;

        Collection<CommunityName> allCommunities;

        allCommunities = client.getAllCommunityNames();

        for (final CommunityName name : allCommunities) {
            if (name.toString().equalsIgnoreCase(commName.getText())) {
                showErrorBox("A community with that name already "
                        + "exists; please enter a new community name.",
                    "Community Name Error");
                commName.setText("");
                flagged = true;
            }
        }

        if (!flagged) {
            Password password = new Password(
                    commPass.getPassword());
            CommunityName name = new CommunityName(
                    commName.getText());


            client.createCommunity(name, password);


            // Show updated list
            parentPanel.refreshCommunityNames();

            // Select newly created community
            parentPanel.setSelectedCommunity(name);

            // Destroy this window
            this.closeWindow();
        }

    }

    /**
     * Closes this window.
     */
    private void closeWindow() {
        this.dispose();
    }

}
