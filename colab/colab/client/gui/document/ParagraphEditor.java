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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import colab.client.ClientDocumentChannel;
import colab.common.DebugManager;
import colab.common.channel.document.Document;
import colab.common.channel.document.DocumentParagraph;
import colab.common.event.document.ParagraphListener;
import colab.common.naming.UserName;
import colab.common.util.StringChangeBuffer;
import colab.common.util.StringChangeBufferListener;

class ParagraphEditor extends JTextArea {

    /** Serialization verion number. */
    public static final long serialVersionUID = 1L;

    private static final int FONT_STEP = 4;

    private final DocumentParagraph paragraph;

    private final ClientDocumentChannel channel;

    private final UserName user;

    private final Font defaultFont;

    private final Color defaultFG, defaultBG;

    private final StringChangeBuffer changeBuffer;

    private Vector<ParagraphListener> paragraphListeners;

    private final DocumentPanel documentPanel;
    /**
     * A timer that will fire periodically to send changes
     * when no keys are pressed for a specified time.
     */
    private Timer timer;

    /** Timer delay in ms. */
    private final int TIMER_DELAY = 4000000;

    public ParagraphEditor(final ClientDocumentChannel channel,
            final DocumentPanel documentPanel,
            final DocumentParagraph paragraph,
            final UserName user) {

        this.channel = channel;
        this.documentPanel = documentPanel;
        this.paragraph = paragraph;
        this.user = user;
        this.defaultFont = getFont();
        this.defaultFG = getForeground();
        this.defaultBG = getBackground();

        this.changeBuffer = new StringChangeBuffer(
                new StringChangeBufferListener() {
            public void insert(final int offset, final String str) {
                for (ParagraphListener listener : paragraphListeners) {
                    listener.onInsert(offset, str);
                }
            }
            public void delete(final int offset, final int length) {
                for (ParagraphListener listener : paragraphListeners) {
                    listener.onDelete(offset, length);
                }
            }
        });

        this.paragraphListeners = new Vector<ParagraphListener>();

        this.timer = new Timer(TIMER_DELAY, new ParagraphChangeDispatcher());

        setText(this.paragraph.getContents());
        showHeader(this.paragraph.getHeaderLevel());
        showLock(this.paragraph.getLockHolder());

        setLineWrap(true);
        setWrapStyleWord(true);

        this.addFocusListener(new FocusListener() {

            public void focusGained(final FocusEvent arg0) {
            }

            public void focusLost(final FocusEvent e) {
                sendPendingChange();

                // Check whether this paragraph still exists
                Document doc = channel.getCurrentDocument();

                DocumentParagraph para = doc.get(paragraph.getId());

                if (para != null) {
                    // If we had a lock, release it
                    if (isLockedByMe()) {
                        DebugManager.debug("Releasing lock");
                        requestUnlock();
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
                final String contents = paragraph.getContents();
                if (!getText().equals(contents)) {
                    int caret = getCaretPosition();
                    setText(contents);
                    setCaretPosition(caret);
                }
            }
            public void onInsert(final int offset, final String hunk) {
                final String contents = paragraph.getContents();
                if (!getText().equals(contents)) {
                    int caret = getCaretPosition();
                    setText(contents);
                    setCaretPosition(caret);
                }
            }
            public void onLock(final UserName newOwner) {

                DebugManager.debug(newOwner
                        + " has just gained a lock on paragraph "
                        + paragraph.getId());

                // If this paragraph was just locked and we didn't do it,
                // undo any changes that we might have made
                if (!newOwner.equals(user)) {
                    changeBuffer.update();
                    setText(paragraph.getContents());
                }

                // Update GUI
                showLock(newOwner);


            }
            public void onUnlock() {
                showUnlock();
            }
        });

        this.addMouseListener(new ParagraphEditorMouseListener(this));

        this.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(final DocumentEvent e) {
                DebugManager.debug(e.toString()); // probably never happens
            }

            public void insertUpdate(final DocumentEvent e) {

                restartTimer();

                if (isUnlocked()) {
                    requestLock();
                }

                String str = ParagraphEditor.this.getText().substring(
                            e.getOffset(), e.getOffset() + e.getLength());
                if (e.getLength() == 1) {
                    changeBuffer.insert(e.getOffset(), str);
                } else {
                    // TODO: temporary safety measure, kill this
                    DebugManager.debug("#######~~~~~~######: " + str);
                }

            }

            public void removeUpdate(final DocumentEvent e) {

                restartTimer();

                if (isUnlocked()) {
                    requestLock();
                }

                DebugManager.debug("#######~~~~~~######: " + "deleting from "
                        + e.getOffset() + ", length=" + e.getLength());
                changeBuffer.delete(e.getOffset(), e.getLength());

            }

        });

    }

    /**
     * Deletes this paragraph.
     */
    public void delete() {
        documentPanel.deleteParagraph(paragraph.getId(), user);
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
        this.fireOnLock(user);
    }

    /**
     * Requests that this paragraph's lock be released.
     */
    public void requestUnlock() {
        this.fireOnUnlock();
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

    /**
     * Sends all pending changes (inserts or deletes)
     * to the channel.
     */
    public void sendPendingChange() {

        // Don't send if we don't have the lock
        if (!isLockedByMe()) {
            DebugManager.debug("ParagraphEditor could not send changes because "
                    + this.getLockHolder() + " has the lock!");
            return;
        }

        // Save the cursor position
        int selectionStart = this.getSelectionStart();

        // Send any current inserts or deletes
        changeBuffer.update();

        // Restore the caret
        this.setCaretPosition(selectionStart);

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

    private UserName getLockHolder() {
        UserName lockHolder = paragraph.getLockHolder();
        return lockHolder;
    }

    private class ParagraphChangeDispatcher implements ActionListener {

        public void actionPerformed(final ActionEvent e) {
            sendPendingChange();
        }

    }

}
