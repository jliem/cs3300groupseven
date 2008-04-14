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
/*
    private final ParagraphEditor editor;

    public ParagraphEditorKeyAdapter(final ParagraphEditor editor) {
        this.editor = editor;
    }

    public void keyTyped(final KeyEvent ke) {

    }

    public void keyPressed(final KeyEvent ke) {

        switch (ke.getKeyCode()) {

        // DON'T REMOVE THIS!
        case KeyEvent.VK_ENTER:

            if (ke.isShiftDown()) {

            } else {
                // TODO- signal new paragraph creation
                // to server, insert in gui, move
                // cursor to it, et cetera
            }
            ke.consume();

            break;

        case KeyEvent.VK_TAB:

             if (ke.isShiftDown()) {

             } else {
                 // Focus shifting done in DocumentPanel
             }
             ke.consume();

            break;

        case KeyEvent.VK_UP:

            if (ke.isControlDown()) {
                DocumentParagraph p = editor.getParagraph();
                p.setHeaderLevel(p.getHeaderLevel()+1);
                // TODO: - signal insert to server, still
                // not sure how this will work- maybe some
                // unified object that i can send all this
                // too, will use timers to send updates?
            }

            break;

        case KeyEvent.VK_DOWN:
            if (ke.isControlDown()) {
                DocumentParagraph p = editor.getParagraph();
                p.setHeaderLevel(p.getHeaderLevel()-1);
                // TODO: signal server
            } else {
            }

            break;

        case KeyEvent.VK_INSERT:

            // Send any current inserts or deletes
            editor.sendPendingChange();

            break;

        case KeyEvent.VK_PAGE_UP:
        case KeyEvent.VK_PAGE_DOWN:
        case KeyEvent.VK_HOME:
        case KeyEvent.VK_END:
        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_RIGHT:

            // Pressing an arrow key means moving the cursor, so send
            // any inserts

            // Send any current inserts or deletes
            editor.sendPendingChange();

            break;


        case KeyEvent.VK_DELETE:
        case KeyEvent.VK_BACK_SPACE:
            // Delete event

            // If we don't have a lock, request it
            // if appropriate
            if (editor.isUnlocked()) {
                editor.requestLock();
            }

            if (editor.getText().length() == 0) {
                // There was no text and delete/backspace was pressed, so
                // delete this paragraph

                editor.delete();
            } else if (editor.getSelectionStart() > 0) {
                // Delete text

                // Check whether we're in the middle of deleting
                if (editor.getDeleteStart() >= 0) {

                    DebugManager.debug("Delete already in progress from "
                        + editor.getDeleteStart());

                    // We already have a delete in progress, so increment it one
                    editor.setDeleteLength(editor.getDeleteLength()+1);
                } else {
                    // No delete in progress

                    // Set starting index
                    editor.setDeleteStart(editor.getSelectionStart());

                    DebugManager.debug("No delete in progress, starting at "
                        + editor.getDeleteStart());

                    editor.setDeleteLength(1);
                }

                // If delete was pressed instead of backspace,
                // increment the delete start index
                if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
                    editor.setDeleteStart(editor.getDeleteStart()+1);
                }

            } else {
                DebugManager.debug("Not running, Selection start is "
                    + editor.getSelectionStart());
            }

            break;

        default:
            // Nothing
        }
    }*/
}
