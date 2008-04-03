package colab.client.gui.revision;

import javax.swing.JFrame;

public class RevisionFrame extends JFrame {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    public RevisionFrame(final RevisionPanel revisionPanel) {

        this.setTitle("Revision Mode");

        this.add(revisionPanel);
        this.pack();


    }
}
