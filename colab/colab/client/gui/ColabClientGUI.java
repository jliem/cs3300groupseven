package colab.client.gui;

import javax.swing.JFrame;

import colab.client.ColabClient;

public class ColabClientGUI extends JFrame {

    public ColabClientGUI(final ColabClient client) {

    }

    public static void main(final String[] args) {

        ColabClient client = new ColabClient();

        ColabClientGUI gui = new ColabClientGUI(client);
        gui.setVisible(true);

    }

}
