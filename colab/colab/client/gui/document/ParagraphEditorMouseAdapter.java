package colab.client.gui.document;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import colab.common.DebugManager;

public class ParagraphEditorMouseAdapter extends MouseAdapter {

    private final ParagraphEditor editor;

    private int selectionStart;

    public ParagraphEditorMouseAdapter(final ParagraphEditor editor) {
        this.editor = editor;
    }

    public void mouseClicked(MouseEvent me) {

        // Whenever the mouse is clicked in a text box, we are
        // beginning an insert

        // Save the click position first--it will
        // be wiped out by any inserts
        selectionStart = editor.getSelectionStart();

        // Send any current inserts or deletes
        editor.sendPendingChange();

        // Restore the caret
        editor.setCaretPosition(selectionStart);
    }

}