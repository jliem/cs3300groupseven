package colab.client.gui.document;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.JTextArea;
import javax.swing.Timer;

import colab.client.ClientDocumentChannel;
import colab.client.gui.ChannelPanelListener;
import colab.common.DebugManager;
import colab.common.channel.document.Document;
import colab.common.channel.document.DocumentChannelData;
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

    private Vector<ParagraphListener> paragraphListeners;

    /** A timer that will fire periodically to send changes
     * when no keys are pressed for a specified time.
     */
    private Timer timer;

    /** Timer delay in ms */
    private final int TIMER_DELAY = 2000;


    /** The index at which we first began inserting text. */
    private int startIndex;

    /** Tracks the length of text to be deleted. */
    private int deleteLength;

    /** Tracks the index at which we first began deleting text. */
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

        this.paragraphListeners = new Vector<ParagraphListener>();

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

                // Check whether this paragraph still exists
                Document doc = channel.getCurrentDocument();

                DocumentParagraph para = doc.get(paragraph.getId());

                if (para != null) {
                    // If we had a lock, release it
                    if (isLockedByMe()) {
                        DebugManager.debug("Releasing lock");
                        try {
                            channel.requestUnlock(paragraph.getId());
                        } catch (RemoteException re) {
                            DebugManager.remote(re);
                        }
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

    /**
     * Adds text to the local insert buffer to be sent the next time
     * the channel is notified.
     *
     * @param c the character being inserted into the paragraph
     */
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

    /**
     * Updates GUI to display header.
     *
     * @param headerLevel the header level
     */
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

    /**
     * Restarts the timer which sends changes.
     */
    public void restartTimer() {
        timer.restart();
    }

    /**
     * Requests a lock on this paragraph.
     */
    public void requestLock() {
        try {
            channel.requestLock(user, paragraph.getId());
        } catch (RemoteException re) {
            DebugManager.remote(re);
        }
    }

    /**
     * Checks whether the user is allowed to request a lock
     * right now.
     *
     * @return true if the paragraph is unlocked, false otherwise
     */
    public boolean isUnlocked() {
        return (getLockHolder() == null);
    }

    /**
     * Check whether the paragraph is locked by someone else.
     *
     * @return true if the paragraph is locked by someone else,
     * false if the paragraph is unlocked or is locked by me.
     */
    public boolean isLockedByOther() {
        UserName lockHolder = this.getLockHolder();

        // If no lock or it's not me, return false
        if (lockHolder == null || lockHolder.equals(user)) {
            return false;
        }

        return true;
    }

    /**
     * Checks whether the current user has locked this paragraph.
     *
     * @return true if the current user owns the paragraph, false if the
     * paragraph is unlocked or another user owns it
     */
    public boolean isLockedByMe() {
        UserName lockHolder = this.getLockHolder();

        return (lockHolder != null && lockHolder.equals(user));

    }

    public void addParagraphListener(
            final ParagraphListener listener) {
        paragraphListeners.add(listener);
    }

    public void removeParagraphListener(
            final ParagraphListener listener) {
        paragraphListeners.remove(listener);
    }

    private void fireOnLock(final UserName newOwner) {
        for (ParagraphListener listener : paragraphListeners) {
            listener.onLock(newOwner);
        }
    }

    private void fireOnUnlock() {
        for (ParagraphListener listener : paragraphListeners) {
            listener.onUnlock();
        }
    }

    private void fireHeaderChange(final int headerLevel) {
        for (ParagraphListener listener : paragraphListeners) {
            listener.onHeaderChange(headerLevel);
        }
    }

    private void fireOnInsert(final int offset, final String hunk) {
        for (ParagraphListener listener : paragraphListeners) {
            listener.onInsert(offset, hunk);
        }
    }

    private void fireOnDelete(final int offset, final int length) {
        for (ParagraphListener listener : paragraphListeners) {
            listener.onDelete(offset, length);
        }
    }

    /**
     * Sends all pending changes (inserts or deletes)
     * to the channel.
     */
    public void sendPendingChange() {

        // Don't send if we don't have the lock
        if (!isLockedByMe()) {
            DebugManager.debug("ParagraphEditor could not send changes because this user " +
                    "doesn't have the lock!");
            return;
        }

        // Save the cursor position
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

            this.fireOnDelete(offset, deleteLength);
//            try {
//                channel.deleteText(offset, deleteLength,
//                        paragraph.getId(), user);
//            } catch (RemoteException e) {
//                // TODO Auto-generated catch block
//                DebugManager.remote(e);
//            }

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

            this.fireOnInsert(startIndex, insertText.toString());

//            try {
//                channel.insertText(startIndex, insertText.toString(),
//                        paragraph.getId(), user);
//            } catch (RemoteException e) {
//                // TODO Auto-generated catch block
//                DebugManager.remote(e);
//            }

            // Clear start index and text
            resetInsert();
        }
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

    private UserName getLockHolder() {
        UserName lockHolder = paragraph.getLockHolder();
        return lockHolder;
    }

    private class ParagraphChangeDispatcher implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            sendPendingChange();
        }

    }

}
