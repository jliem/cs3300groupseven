package colab.client.gui.document;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ParagraphEditorMouseListener extends MouseAdapter {

    private final ParagraphEditor editor;

    private int selectionStart;

    public ParagraphEditorMouseListener(final ParagraphEditor editor) {
        this.editor = editor;
    }

    /**
     * Whenever the mouse is clicked in a text box,
     * we are beginning an insert.
     *
     * {@inheritDoc}
     */
    public void mouseClicked(final MouseEvent me) {

        // Save the click position first--it will
        // be wiped out by any inserts
        selectionStart = editor.getSelectionStart();

        // Send any current inserts or deletes
        editor.sendPendingChange();

        // Restore the caret
        editor.setCaretPosition(selectionStart);
    }

}
