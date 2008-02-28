package colab.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import colab.client.ClientChatChannel;
import colab.common.channel.ChatChannelData;
import colab.common.naming.ChannelName;

public class ExportChatFrame extends JFrame {
	private final JFileChooser fileChooser;
	private final JButton exportButton, browseButton;
	private final JTextField fromBox, toBox, fileBox;
	private final JLabel fromLabel, toLabel;
	private final JRadioButton local, lines, entire;
	private File exportFile;
	public ExportChatFrame(final ClientChatChannel channel) {

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
			public void actionPerformed(ActionEvent e) {
				if(fileChooser.showOpenDialog(ExportChatFrame.this) == JFileChooser.APPROVE_OPTION) {
					fileBox.setText((exportFile = fileChooser.getSelectedFile()).getPath());
				}
			}
		});
		
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO:add actual export action
				try{
					if(exportFile==null || (!exportFile.exists() && !exportFile.createNewFile()) || !exportFile.canWrite()){
						showErrorBox("Cannot write to selected file.", "Export Error");
						return;
					}
					
					if(local.isSelected()){
						export(exportFile, channel.getLocalMessages());
					}
					else if(lines.isSelected()){
						//TODO:export per line, requires some server-side fixes (retrieving old history)
					}
					else {
						//TODO:total export, requires some server-side fixes (retrieving old history)
					}
				}
				catch(IOException ex){
					showErrorBox("Error writing to file.", "File Error");
					ex.printStackTrace();
					return;
				}
				
				setVisible(false);
				dispose();
			}
		});
		
		local = new JRadioButton("Local chat");
		lines = new JRadioButton("Lines");
		entire = new JRadioButton("Entire chat");
		
		ButtonGroup group = new ButtonGroup();
		group.add(local);
		group.add(lines);
		group.add(entire);
		
		//selects local option, links line option enabled the radio button
		local.setSelected(true);
		lines.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				boolean isSel = lines.isSelected();
				fromBox.setEnabled(isSel);
				fromLabel.setEnabled(isSel);
				toBox.setEnabled(isSel);
				toLabel.setEnabled(isSel);
			}
		});
		
		//defaults to disabled line options
		fromBox.setEnabled(false);
		fromLabel.setEnabled(false);
		toBox.setEnabled(false);
		toLabel.setEnabled(false);
		
		JPanel optionsPanel = new JPanel();
		optionsPanel.setBorder(new LineBorder(Color.GRAY, 1, true));
		optionsPanel.setLayout(new GridBagLayout());
		JPanel linesPanel = new JPanel();
		linesPanel.setLayout(new BoxLayout(linesPanel, BoxLayout.LINE_AXIS));
		//constructs three constraints, one to be shared by the radio buttons, and
		//another for the lines stuff
		GridBagConstraints buttonsC = new GridBagConstraints(),
			linesC = new GridBagConstraints();
		
		buttonsC.gridx = 0;
		buttonsC.gridy = GridBagConstraints.RELATIVE;
		buttonsC.insets = new Insets(2, 2, 2, 2);
		
		linesC.gridy = 1;
		linesC.gridx = GridBagConstraints.RELATIVE;
		linesC.insets = new Insets(5, 5, 5, 5);
		
		//adds radio options
		optionsPanel.add(local, buttonsC);
		optionsPanel.add(lines, buttonsC);
		optionsPanel.add(entire, buttonsC);
		//adds line stuff
		linesPanel.add(fromLabel);
		linesPanel.add(fromBox);
		linesPanel.add(toLabel);
		linesPanel.add(toBox);
		optionsPanel.add(linesPanel, linesC);
		
		
		JPanel filePanel = new JPanel();
		filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.LINE_AXIS));
		filePanel.add(fileBox);
		filePanel.add(Box.createRigidArea(new Dimension(5, 0)));
		filePanel.add(browseButton);
		
		setLayout(new GridBagLayout());
		GridBagConstraints optionsC = new GridBagConstraints(),
			filesC = new GridBagConstraints(),
			exportC = new GridBagConstraints();
		
		optionsC.gridx = 0;
		optionsC.gridy = 0;
		optionsC.fill = GridBagConstraints.HORIZONTAL;
		optionsC.insets = new Insets(5, 5, 5, 5);
		filesC.gridx = 0;
		filesC.gridy = 1;
		filesC.insets = new Insets(5, 5, 5, 5);
		exportC.gridx = 0;
		exportC.gridy = 2;
		
		add(optionsPanel, optionsC);
		add(filePanel, filesC);
		add(exportButton, exportC);
		
		setSize(275, 200);
		setResizable(false);
		//we don't want closing the export window to close the whole app
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	private void export(File file, List <ChatChannelData> data) throws IOException {
		PrintWriter writer = new PrintWriter(new FileOutputStream(file));
		for(ChatChannelData ccd : data) {
			writer.println(ccd.getMessageString(true));
		}
		writer.close();
	}
	
	private void showErrorBox(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
	
	public static void main(String args[]) throws RemoteException {
		ExportChatFrame frame = new ExportChatFrame(new ClientChatChannel(new ChannelName("Test")));
		frame.setVisible(true);
	}
}
