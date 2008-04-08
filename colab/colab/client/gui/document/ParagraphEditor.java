package colab.client.gui.document;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.rmi.RemoteException;

import javax.swing.JTextArea;

import colab.client.ClientDocumentChannel;
import colab.common.DebugManager;
import colab.common.channel.document.DocumentParagraph;
import colab.common.event.document.ParagraphListener;
import colab.common.naming.UserName;

class ParagraphEditor extends JTextArea {

    private static final int FONT_STEP = 4;

    private final DocumentParagraph paragraph;

    private final ClientDocumentChannel channel;

    private final UserName user;

    private final Font defaultFont;

    private final Color defaultFG, defaultBG;

    private static final long serialVersionUID = 1;

    private StringBuffer insertText;


    /** The index at which we first clicked (presumably to being inserting
     * or deleting text)
     */
    private int startIndex;

    private int deleteLength;
    private int deleteStart;

    public ParagraphEditor(final ClientDocumentChannel channel,
            final DocumentParagraph paragraph,
            final UserName user) {

        this.channel = channel;
        this.paragraph = paragraph;
        this.user = user;
        this.defaultFont = getFont();
        this.defaultFG = getForeground();
        this.defaultBG = getBackground();

        this.insertText = new StringBuffer();
        this.startIndex = -1;
        this.deleteLength = -1;
        this.deleteStart = -1;

        setText(this.paragraph.getContents());
        showHeader(this.paragraph.getHeaderLevel());
        showLock(this.paragraph.getLockHolder());

        setLineWrap(true);
        setWrapStyleWord(true);

        this.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent arg0) {
                // When this field gains focus, save the start
                // index
//                setStartIndex(getSelectionStart());
//
//                DebugManager.debug("Focus--Insert text beginning at " + getStartIndex());
            }

            public void focusLost(FocusEvent e) {
                sendPendingInsert();
            }
        });

        paragraph.addParagraphListener(new ParagraphListener() {

            public void onHeaderChange(final int headerLevel) {
                showHeader(headerLevel);
            }

            public void onDelete(final int offset, final int length) {
                setText(paragraph.getContents());
            }
            public void onInsert(final int offset, final String hunk) {
                setText(paragraph.getContents());
            }
            public void onLock(final UserName newOwner) {
                showLock(newOwner);
            }
            public void onUnlock() {
                showUnlock();
            }
        });
    }

    public void addInsertText(char c) {
        insertText.append(c);
    }

    public void sendPendingChange() {
        sendPendingDelete();
        sendPendingInsert();
    }

    public void sendPendingDelete() {

        if (deleteStart >= 0 && deleteLength >= 0) {
            // Compute starting offset from start and length
            int offset = deleteStart - deleteLength;

            DebugManager.debug("Deleting text from " + offset + ", length is " + deleteLength);

            try {
                channel.deleteText(offset, deleteLength, paragraph.getId());
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                DebugManager.remote(e);
            }

            deleteStart = -1;
            deleteLength = -1;
        }

    }

    public void sendPendingInsert() {

        // Check if there's any text to send
        if (startIndex >= 0 && insertText.length() > 0) {
            DebugManager.debug("Editor is sending text \"" + insertText.toString() +
                    "\" at index " + startIndex);

            try {
                channel.insertText(startIndex, insertText.toString(), paragraph.getId());
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                DebugManager.remote(e);
            }

            // Clear start index and text
            startIndex = -1;
            insertText = new StringBuffer();
        }
    }


    /**
     * Check whether the paragraph is locked by someone else.
     *
     * @return true if the paragraph is locked by someone else,
     * false if the paragraph is unlocked or is locked by me.
     */
    public boolean isLockedByOther() {
        UserName lockHolder = paragraph.getLockHolder();

        if (lockHolder == null || lockHolder.equals(user))
            return false;

        return true;
    }

    public void showHeader(final int headerLevel) {

        int newSize = defaultFont.getSize();
        int style = Font.PLAIN;

        if (headerLevel>0) {
            style = Font.BOLD;
        }

        if (headerLevel>1) {
            newSize += FONT_STEP * (headerLevel - 1);
        }

        setFont(new Font(defaultFont.getFontName(), style, newSize));

    }

    public void showLock(final UserName newOwner) {

        if (newOwner != null) {

            if (newOwner.equals(ParagraphEditor.this.user)) {

                setBackground(Color.BLUE.brighter());
                setForeground(Color.WHITE);
                setToolTipText("");
                requestFocus();

            } else {

                setBackground(Color.GREEN.brighter());
                setForeground(Color.BLACK);
                setToolTipText(newOwner.toString() + " is editing...");
                setEditable(false);

            }

        } else {

            showUnlock();

        }

    }

    public void showUnlock() {

        setForeground(defaultFG);
        setBackground(defaultBG);

        setEditable(true);

    }

    public DocumentParagraph getParagraph() {

        return paragraph;

    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(
                super.getMaximumSize().width,
                getMinimumSize().height);
    }


    public int getStartIndex() {
        return startIndex;
    }


    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getDeleteLength() {
        return deleteLength;
    }

    public void setDeleteLength(int deleteLength) {
        this.deleteLength = deleteLength;
    }

    public int getDeleteStart() {
        return deleteStart;
    }

    public void setDeleteStart(int deleteStart) {
        this.deleteStart = deleteStart;
    }

}
