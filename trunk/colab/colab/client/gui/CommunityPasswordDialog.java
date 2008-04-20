package colab.client.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import colab.client.ColabClient;
import colab.common.DebugManager;
import colab.common.exception.CommunityAlreadyExistsException;
import colab.common.exception.CommunityDoesNotExistException;
import colab.common.exception.NetworkException;
import colab.common.naming.CommunityName;
import colab.server.user.Password;

public class CommunityPasswordDialog extends JDialog {

    private final ColabClient client;

    private final CommunityName communityName;

    private JPasswordField commPass;
    private JPasswordField confirmCommPass;

    public CommunityPasswordDialog(final ColabClient client,
            CommunityName communityName) {


        this.client = client;
        this.communityName = communityName;

        commPass = new JPasswordField("");
        confirmCommPass = new JPasswordField("");
        JLabel passLabel = new JLabel("Community password: ");
        JLabel confirmPassLabel = new JLabel("Confirm community password: ");

        JButton changeButton = new JButton("Change password");
        changeButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {

                boolean changeOK = false;

                try {
                    handleChange(e);

                    changeOK = true;

                } catch (NetworkException e1) {
                    DebugManager.network(e1);
                } catch (CommunityAlreadyExistsException ce) {
                    DebugManager.exception(ce);
                } catch (RemoteException re) {
                    DebugManager.remote(re);
                } catch (CommunityDoesNotExistException cdne) {
                    DebugManager.exception(cdne);
                }

                if (!changeOK) {

                    // TODO Handle this in parent
                    JOptionPane.showMessageDialog(CommunityPasswordDialog.this,
                            "The connection to the server was lost. "
                            + "Double-check that the server is running.",
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

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));
        panel.add(passLabel);
        panel.add(commPass);
        panel.add(confirmPassLabel);
        panel.add(confirmCommPass);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(changeButton);
        buttonPanel.add(cancelButton);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.setLayout(new BorderLayout());
        this.add(mainPanel, BorderLayout.CENTER);

        pack();

        this.setTitle("Change Community Password");

        this.setModal(true);

    }

    private void handleChange(final ActionEvent e) throws NetworkException,
    RemoteException, CommunityAlreadyExistsException, CommunityDoesNotExistException {

        if (commPass.getPassword().length == 0
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

        Password password = new Password(commPass.getPassword());

        client.changePassword(communityName, password);

        // Destroy this window
        this.closeWindow();

    }

    private void showErrorBox(final String message, final String title) {
        JOptionPane.showMessageDialog(
                this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Closes this window.
     */
    private void closeWindow() {
        this.dispose();
    }

}
