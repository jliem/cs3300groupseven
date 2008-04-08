package colab.client.gui.document;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.rmi.RemoteException;

import javax.swing.JTextArea;
import javax.swing.Timer;

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

    /** A timer that will fire periodically to send changes
     * when no keys are pressed for a specified time.
     */
    private Timer timer;

    /** Timer delay in ms */
    private final int TIMER_DELAY = 2000;


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

        this.timer = new Timer(TIMER_DELAY, new ParagraphChangeDispatcher());

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
                sendPendingChange();

                // If we had a lock, release it
                if (isLockedByMe()) {
                    DebugManager.debug("Releasing lock");
                    try {
                        channel.requestUnlock(paragraph.getId());
                    } catch (RemoteException re) {
                        DebugManager.remote(re);
                    }
                }

                // Stop the timer
                timer.stop();
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

                DebugManager.debug(newOwner + " has just gained a lock on paragraph " +
                        paragraph.getId());

                // If this paragraph was just locked and we didn't do it,
                // undo any changes that we might have made
                if (!newOwner.equals(user)) {
                    resetDelete();
                    resetInsert();
                    setText(paragraph.getContents());
                }

                // Update GUI
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

    /**
     * Deletes this paragraph.
     */
    public void delete() {
        try {
            channel.deleteParagraph(paragraph.getId());
        } catch (RemoteException re) {
            DebugManager.remote(re);
        }
    }

    public void restartTimer() {
        timer.restart();
    }

    public void requestLock() {
        try {
            channel.requestLock(user, paragraph.getId());
        } catch (RemoteException re) {
            DebugManager.remote(re);
        }
    }

    public boolean canRequestLock() {

        // Check if someone else has locked it
        if (this.isLockedByOther()) {
            return false;
        }

        // Check whether I have locked it
        UserName lockHolder = paragraph.getLockHolder();

        DebugManager.debug("Checking for lock: currently held by " + paragraph.getLockHolder());
        if (lockHolder != null && lockHolder.equals(user)) {
            return false;
        }

        return true;
    }

    public boolean canDisplay(int codePoint) {
        return defaultFont.canDisplay(codePoint);
    }

    public void sendPendingChange() {

        if (this.isLockedByOther())
            return;

        int selectionStart = this.getSelectionStart();

        // Send any current inserts or deletes
        sendPendingDelete();
        sendPendingInsert();


        // Restore the caret
        this.setCaretPosition(selectionStart);

    }

    private void sendPendingDelete() {

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

            resetDelete();
        }

    }

    private void resetDelete() {
        deleteStart = -1;
        deleteLength = -1;
    }

    private void resetInsert() {
        startIndex = -1;
        insertText = new StringBuffer();
    }

    private void sendPendingInsert() {

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
            resetInsert();
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

        DebugManager.debug("Lock holder is " + paragraph.getLockHolder());

        // If no lock or it's not me, return false
        if (lockHolder == null || lockHolder.equals(user)) {
            return false;
        }

        return true;
    }

    public boolean isLockedByMe() {
        UserName lockHolder = paragraph.getLockHolder();

        DebugManager.debug("Lock holder is " + paragraph.getLockHolder());

        return (lockHolder != null && lockHolder.equals(user));

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

    private void showLock(final UserName newOwner) {

        if (newOwner != null) {

            if (newOwner.equals(ParagraphEditor.this.user)) {

                //setBackground(Color.BLUE.brighter());
                //setForeground(Color.WHITE);
                setBackground(Color.CYAN);

                setToolTipText(null);
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

    private void showUnlock() {

        setForeground(defaultFG);
        setBackground(defaultBG);

        setToolTipText(null);
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

    private class ParagraphChangeDispatcher implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            DebugManager.debug("Timer is sending changes");
            sendPendingChange();
        }

    }

}
