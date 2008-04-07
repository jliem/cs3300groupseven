package colab.client.gui.document;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import colab.common.DebugManager;
import colab.common.channel.document.DocumentParagraph;

/**
 * Class to handle key events for a ParagraphEditor class.
 *
 */
public class ParagraphEditorKeyAdapter extends KeyAdapter {

    private final ParagraphEditor editor;

    public ParagraphEditorKeyAdapter(final ParagraphEditor editor) {
        this.editor = editor;
    }

    public void keyPressed(final KeyEvent arg0) {
        super.keyPressed(arg0);

        // Every time a key is pressed, log the index
        editor.setEndIndex(editor.getSelectionStart());

        DebugManager.debug("End index is " + editor.getEndIndex());

        switch (arg0.getKeyCode()) {

        case KeyEvent.VK_ENTER:

            if (arg0.isShiftDown()) {
                int position = editor.getCaretPosition();
                editor.insert("\n", position);
            } else {
                /* TODO- signal new paragraph creation
                 * to server, insert in gui, move
                 * cursor to it, et cetera */
            }
            arg0.consume();

            break;

        case KeyEvent.VK_TAB:

             if (arg0.isShiftDown()) {
                 editor.append("\t");
             } else {
                 // Focus shifting done in DocumentPanel
             }
             arg0.consume();

            break;

        case KeyEvent.VK_UP:

            if (arg0.isControlDown()) {
                DocumentParagraph p = editor.getParagraph();
                p.setHeaderLevel(p.getHeaderLevel()+1);
                /* TODO: - signal insert to server, still
                 * not sure how this will work- maybe some
                 * unified object that i can send all this
                 * too, will use timers to send updates? */
            }

            break;

        case KeyEvent.VK_DOWN:
            if (arg0.isControlDown()) {
                DocumentParagraph p = editor.getParagraph();
                p.setHeaderLevel(p.getHeaderLevel()-1);
                // TODO: signal server
            } else {

            }

            break;

        case KeyEvent.VK_BACK_SPACE:
        case KeyEvent.VK_DELETE:
            // Delete event

            break;

        default:
            // Nothing

        }
    }
}

