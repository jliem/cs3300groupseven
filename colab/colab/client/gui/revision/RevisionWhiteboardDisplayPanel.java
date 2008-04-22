package colab.client.gui.revision;

import java.awt.Graphics;

import javax.swing.JPanel;

import colab.common.channel.whiteboard.Whiteboard;

public class RevisionWhiteboardDisplayPanel extends JPanel {

    private Whiteboard board;

    public void setBoard(Whiteboard board) {
        this.board = board;
    }

    public void paintComponent(Graphics g) {
        if (board != null) {
            board.draw(g);
        }
    }

}
