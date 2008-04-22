package colab.client.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import colab.client.ClientChannel;
import colab.common.DebugManager;
import colab.common.naming.ChannelName;

public class ExportFrame extends JFrame {

    /** Serialization verson number. */
    public static final long serialVersionUID = 1L;

    private final ClientChannel channel;

    private final JFileChooser fileChooser;

    private final JButton exportButton;

    private final JButton browseButton;

    private final JTextField fromBox;

    private final JTextField toBox;

    private final JTextField fileBox;

    private final JLabel fromLabel;

    private final JLabel toLabel;

    private File exportFile;

    public ExportFrame(final ClientChannel channel) {

        this.channel = channel;

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setApproveButtonText("Export");
        fileChooser.setMultiSelectionEnabled(false);
        exportFile = null;

        fromBox = new JTextField(3);
        toBox = new JTextField(3);
        fromLabel = new JLabel("from ");
        toLabel = new JLabel(" to ");
        fileBox = new JTextField(13);
        browseButton = new JButton("Browse");
        exportButton = new JButton("Export");

        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (fileChooser.showSaveDialog(ExportFrame.this)
                        == JFileChooser.APPROVE_OPTION) {

                    // Sets the file path in the text box
                    fileBox.setText(fileChooser.getSelectedFile().getPath());
                }
            }
        });

        exportButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                exportAction();
            }
        });

        //defaults to disabled line options
        fromBox.setEnabled(false);
        fromLabel.setEnabled(false);
        toBox.setEnabled(false);
        toLabel.setEnabled(false);

        JPanel linesPanel = new JPanel();
        linesPanel.setLayout(new BoxLayout(linesPanel, BoxLayout.LINE_AXIS));

        /* Construct three constraints, one to be shared by the radio
         * buttons, and another for the lines stuff */
        GridBagConstraints linesC = new GridBagConstraints();

        linesC.gridy = 0;
        linesC.gridx = GridBagConstraints.RELATIVE;
        linesC.insets = new Insets(5, 5, 5, 5);

        // Add line stuff
        linesPanel.add(fromLabel);
        linesPanel.add(fromBox);
        linesPanel.add(toLabel);
        linesPanel.add(toBox);

        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.LINE_AXIS));
        filePanel.add(fileBox);
        filePanel.add(Box.createRigidArea(new Dimension(5, 0)));
        filePanel.add(browseButton);

        setLayout(new GridBagLayout());
        GridBagConstraints optionsC = new GridBagConstraints();
        GridBagConstraints filesC = new GridBagConstraints();
        GridBagConstraints exportC = new GridBagConstraints();

        optionsC.gridx = 0;
        optionsC.gridy = 0;
        optionsC.fill = GridBagConstraints.HORIZONTAL;
        optionsC.insets = new Insets(5, 5, 5, 5);
        filesC.gridx = 0;
        filesC.gridy = 1;
        filesC.insets = new Insets(5, 5, 5, 5);
        exportC.gridx = 0;
        exportC.gridy = 2;

        add(filePanel, filesC);
        add(exportButton, exportC);

        setSize(275, 200);
        setResizable(false);

        // We don't want closing the export window to close the whole app
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pack();

    }

    private void exportAction() {

        try {

            // Try to create/open a file from the path in the text box
            exportFile = new File(fileBox.getText());

            if (exportFile == null || (!exportFile.exists()
                    && !exportFile.createNewFile())
                    || !exportFile.canWrite()) {

                showErrorBox("Cannot write to selected file.",
                             "Export Error");
                return;

            }

            channel.export(exportFile);

        } catch (final IOException ex) {
            showErrorBox("Error writing to file.", "File Error");
            DebugManager.ioException(ex);

            return;
        }

        setVisible(false);
        dispose();

    }

    private void showErrorBox(final String message, final String title) {

        JOptionPane.showMessageDialog(
                this, message, title, JOptionPane.ERROR_MESSAGE);

    }

}
