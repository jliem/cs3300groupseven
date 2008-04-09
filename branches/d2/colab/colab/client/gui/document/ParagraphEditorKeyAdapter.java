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

    public void keyTyped(final KeyEvent ke) {

        // For any key typed, restart timer
        editor.restartTimer();

        // Filter out invalid chars
        // getKeyCode() seems to return 0 in keyTyped,
        // so cast getKeyChar() instead

        // So far the only ones I've found are delete and backspace,
        // but filter them all for safety
        switch ((int)ke.getKeyChar()) {

        case KeyEvent.VK_TAB:
        case KeyEvent.VK_UP:
        case KeyEvent.VK_DOWN:
        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_RIGHT:
        case KeyEvent.VK_INSERT:
        case KeyEvent.VK_PAGE_UP:
        case KeyEvent.VK_PAGE_DOWN:
        case KeyEvent.VK_HOME:
        case KeyEvent.VK_END:
        case KeyEvent.VK_DELETE:
        case KeyEvent.VK_BACK_SPACE:

            // Nothing
            break;

        default:

            if ((int)ke.getKeyChar() == KeyEvent.VK_ENTER ||
                    (int)ke.getKeyChar() == KeyEvent.VK_TAB) {

                if (!ke.isShiftDown()) {
                    // Nothing if shift is not held down,
                    // will be taken care of by keyPressed
                    return;
                }
            }

        // Any typeable character gets inserted as text

        // If we don't have a lock, request it
        // if appropriate
        if (editor.isUnlocked()) {
            editor.requestLock();
        }

        // If we weren't already tracking the index, record it now
        if (editor.getStartIndex() < 0) {
            editor.setStartIndex(editor.getSelectionStart());
        }

        editor.addInsertText(ke.getKeyChar());

        }

    }

    public void keyPressed(final KeyEvent ke) {
        super.keyPressed(ke);

        // For any key typed, restart timer
        editor.restartTimer();

        switch (ke.getKeyCode()) {

        // DON'T REMOVE THIS!
        case KeyEvent.VK_ENTER:

            if (ke.isShiftDown()) {
                int position = editor.getCaretPosition();
                editor.insert("\n", position);
                editor.addInsertText('\n');
            } else {
                // New paragraph creation done in DocumentPanel
            }
            ke.consume();

            break;

        case KeyEvent.VK_TAB:

             if (ke.isShiftDown()) {
                 int position = editor.getCaretPosition();
                 editor.insert("\t", position);
                 editor.addInsertText('\t');
             } else {
                 // Focus shifting done in DocumentPanel
             }
             ke.consume();

            break;

        case KeyEvent.VK_UP:

            if (ke.isControlDown()) {

            	if (editor.isUnlocked()) {
            		editor.requestLock();
            	}

                DocumentParagraph p = editor.getParagraph();
                p.setHeaderLevel(p.getHeaderLevel()+1);

                editor.sendHeaderChange(p.getHeaderLevel());
            }

            break;

        case KeyEvent.VK_DOWN:
            if (ke.isControlDown()) {

            	if (editor.isUnlocked()) {
            		editor.requestLock();
            	}

                DocumentParagraph p = editor.getParagraph();
                p.setHeaderLevel(p.getHeaderLevel()-1);

                editor.sendHeaderChange(p.getHeaderLevel());

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

            // Pressing an arrow key means moving the cursor, so send any inserts

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

            	// Send pending changes first
            	editor.sendPendingChange();

                editor.delete();
            } else if (editor.getSelectionStart() > 0) {
                // Delete text

                // Check whether we're in the middle of deleting
                if (editor.getDeleteStart() >= 0) {

                    DebugManager.debug("Delete already in progress from " + editor.getDeleteStart());

                    // We already have a delete in progress, so increment it one
                    editor.setDeleteLength(editor.getDeleteLength()+1);
                } else {
                    // No delete in progress

                    // Set starting index
                    editor.setDeleteStart(editor.getSelectionStart());

                    DebugManager.debug("No delete in progress, starting at " + editor.getDeleteStart());

                    editor.setDeleteLength(1);
                }

                // If delete was pressed instead of backspace,
                // increment the delete start index
                if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
                    editor.setDeleteStart(editor.getDeleteStart()+1);
                }

            } else {
                DebugManager.debug("Not running, Selection start is " + editor.getSelectionStart());
            }

            break;

        default:
            // Nothing
        }
    }
}

