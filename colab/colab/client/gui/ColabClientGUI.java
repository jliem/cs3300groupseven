package colab.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import colab.client.ColabClient;

public class ColabClientGUI extends JFrame {

    private ColabClient client;
	
	public ColabClientGUI(final ColabClient client) {
    	
		this.client = client;
    }

    public static void main(final String[] args)
    {
        ColabClient client = new ColabClient();
        final LoginPanel loginPanel = new LoginPanel(client);
        final ColabClientGUI gui = new ColabClientGUI(client);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setTitle("CoLab Login");
        gui.setVisible(true);
        gui.setSize(400, 100);
        gui.add(loginPanel);
        
        loginPanel.addActionListener(new ActionListener()
        {
           public void actionPerformed(ActionEvent e)
           {
               if(e.getActionCommand().equals("Login Succeeded!")){
                   ChooseCommunityPanel commPanel = new ChooseCommunityPanel();
                   gui.remove(loginPanel);
                   gui.add(commPanel);
                   gui.setTitle("Select Community");
                   commPanel.updateUI();
                   
               }
           }
        });
    }
    

}
