package colab.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;


public class ColabLogin implements ActionListener {

    public ColabLogin(final String username, final char[] password,
            final String serverIP) {
        // TODO Auto-generated constructor stub
    }

    public void actionPerformed(final ActionEvent arg0) {
        // TODO Auto-generated method stub
    	JFrame f = new JFrame("Select Community");
        ChooseCommunityPanel cPanel = new ChooseCommunityPanel();

        f.add(cPanel);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(400, 100);
        f.setVisible(true);
    	
    }

}
