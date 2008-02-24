package colab.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

import colab.client.ColabClient;
import colab.common.community.CommunityName;

public class ColabClientGUI extends JFrame {

    private final ColabClient client;

    private final LoginPanel loginPanel;
    private ChooseCommunityPanel commPanel;

    private JPanel activePanel;

    public ColabClientGUI(final ColabClient client) {
        super();

        this.client = client;

        this.loginPanel = new LoginPanel(client);
        this.commPanel = new ChooseCommunityPanel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        gotoUserLoginView();

        loginPanel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (e.getActionCommand().equals("Login Succeeded!")) {
                    ArrayList<String> commNames = new ArrayList<String>();
                    try {
                        for(CommunityName name: client.getAllCommunityNames())
                            commNames.add(name.getValue());
                    } catch (RemoteException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                        
                    }
                    
                    gotoCommunityLoginView(commNames.toArray());
                }
            }
        });

    }

    private void gotoUserLoginView() {
        setActivePanel(loginPanel);
        setTitle("CoLab Login");
        setResizable(false);
        setSize(400, 100);
        loginPanel.updateUI();
    }

    private void gotoCommunityLoginView(Object[] names){
       
        commPanel.setCommunityNames(names);
        setActivePanel(commPanel);
        setTitle("Select Community");
        setResizable(false);
        setSize(400, 100);
        commPanel.updateUI();
    }

    private void setActivePanel(final JPanel newActivePanel) {
        if (activePanel != null) {
            remove(activePanel);
        }
        add(activePanel = newActivePanel);
    }

    public ColabClientGUI() throws RemoteException {
        this(new ColabClient());
    }

    public static void main(final String[] args) throws RemoteException {

        // Assign a security manager, in the event
        // that dynamic classes are loaded.
        //if (System.getSecurityManager() == null) {
        //    System.setSecurityManager(new RMISecurityManager());
        //}

        new ColabClientGUI();
        

    }


}
