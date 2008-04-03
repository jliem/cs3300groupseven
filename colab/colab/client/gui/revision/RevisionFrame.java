package colab.client.gui.revision;

import java.rmi.RemoteException;

import javax.swing.JFrame;

import colab.client.ClientChannel;
import colab.client.ColabClient;
import colab.common.DebugManager;
import colab.common.naming.UserName;

public class RevisionFrame extends JFrame {

    public RevisionFrame(final RevisionPanel revisionPanel) {

        this.setTitle("Revision Mode");

        this.add(revisionPanel);
        this.pack();


    }
}
