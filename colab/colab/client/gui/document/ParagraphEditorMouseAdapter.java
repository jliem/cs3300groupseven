package colab.client.gui.document;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import colab.client.gui.document.ParagraphEditor.ParagraphState;
import colab.common.DebugManager;

public class ParagraphEditorMouseAdapter extends MouseAdapter {

    private final ParagraphEditor editor;

    public ParagraphEditorMouseAdapter(final ParagraphEditor editor) {
        this.editor = editor;
    }

    public void mouseClicked(MouseEvent me) {

        // Whenever the mouse is clicked in a text box, we are
        // beginning an insert

        // Check whether an insert was already in progress


        // For every click, log the click position
        editor.setStartClickIndex(editor.getSelectionStart());

        DebugManager.debug("Start index is " + editor.getStartClickIndex());
    }

}
