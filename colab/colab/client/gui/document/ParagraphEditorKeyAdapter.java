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

    private int selectionStart;

    public ParagraphEditorKeyAdapter(final ParagraphEditor editor) {
        this.editor = editor;
    }

    public void keyPressed(final KeyEvent arg0) {
        super.keyPressed(arg0);

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

        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_RIGHT:

            // Pressing an arrow key means moving the cursor, so send any inserts

            selectionStart = editor.getSelectionStart();

            // Send any current inserts
            editor.sendPendingInsert();

            // Restore the caret
            editor.setCaretPosition(selectionStart);

            break;

        case KeyEvent.VK_BACK_SPACE:
        case KeyEvent.VK_DELETE:
            // Delete event

            break;

        default:

            // Any other character gets added as insert text


            // If we weren't already tracking the index, record it now
            if (editor.getStartIndex() < 0) {
                editor.setStartIndex(editor.getSelectionStart());

                DebugManager.debug("Start index set to " + editor.getStartIndex());
            }
            editor.addInsertText(arg0.getKeyChar());
        }
    }
}

