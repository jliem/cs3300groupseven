package colab.client.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import colab.client.ColabClient;
import colab.common.exception.CommunityAlreadyExistsException;
import colab.common.exception.NetworkException;
import colab.common.naming.CommunityName;
import colab.server.user.Password;

public class NewCommunityFrame extends JFrame {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private final JButton createButton;

    private final JTextField commName;

    private final JPasswordField commPass;

    private final JPasswordField confirmCommPass;

    private final JLabel nameLabel;

    private final JLabel passLabel;

    private final JLabel confirmPassLabel;

    private final ColabClient client;

    private final ChooseCommunityPanel parentPanel;

    public NewCommunityFrame(final ChooseCommunityPanel parentPanel,
            final ColabClient client) {

        this.client = client;
        this.parentPanel = parentPanel;

        createButton = new JButton("Create Community");
        commName = new JTextField("");
        commPass = new JPasswordField("");
        confirmCommPass = new JPasswordField("");
        nameLabel = new JLabel("Community name: ");
        passLabel = new JLabel("Community password: ");
        confirmPassLabel = new JLabel("Confirm community password: ");

        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                handleCreate(e);
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

    }

    private void showErrorBox(final String message, final String title) {
        JOptionPane.showMessageDialog(
                this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private void handleCreate(final ActionEvent e) {

        if (commName.getText().isEmpty()
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
        try {
            allCommunities = client.getAllCommunityNames();
        } catch (final NetworkException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return;
        }

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
                    commPass.getPassword().toString());
            CommunityName name = new CommunityName(
                    commName.getText());
            try {
                client.createCommunity(name, password);
            } catch (RemoteException re) {
                // TODO: Handle remote exception
                re.printStackTrace();
            } catch (NetworkException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (CommunityAlreadyExistsException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            JOptionPane.showMessageDialog(this,
                    "Created new community " + name.getValue(),
                    "New Community Created",
                    JOptionPane.INFORMATION_MESSAGE);


            // Show updated list
            parentPanel.refreshCommunityNames();

            // Destroy this window
            this.dispose();
        }

    }

}
