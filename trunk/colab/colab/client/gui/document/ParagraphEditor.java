package colab.client.gui.document;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Vector;

import javax.swing.JTextArea;

import colab.client.ClientDocumentChannel;
import colab.common.channel.document.DocumentParagraph;
import colab.common.channel.document.diff.Insert;
import colab.common.event.document.ParagraphListener;
import colab.common.naming.UserName;

class ParagraphEditor extends JTextArea {

    public enum ParagraphState {
        INSERTING,
        DELETE,
        NOTHING
    }

    private static final int FONT_STEP = 4;

    private final DocumentParagraph paragraph;

    private final ClientDocumentChannel channel;

    private final UserName user;

    private final Font defaultFont;

    private final Color defaultFG, defaultBG;

    private static final long serialVersionUID = 1;

    private final Vector<Insert> inserts;

    private ParagraphState state;

    /** The index at which we first clicked (presumably to being inserting
     * or deleting text)
     */
    private int startClickIndex;

    private int endIndex;

    public ParagraphEditor(final ClientDocumentChannel channel,
            final DocumentParagraph paragraph,
            final UserName user) {

        this.channel = channel;
        this.paragraph = paragraph;
        this.user = user;
        this.defaultFont = getFont();
        this.defaultFG = getForeground();
        this.defaultBG = getBackground();

        this.state = ParagraphState.NOTHING;
        this.inserts = new Vector<Insert>();
        this.startClickIndex = -1;
        this.endIndex = -1;


        setText(this.paragraph.getContents());
        showHeader(this.paragraph.getHeaderLevel());
        showLock(this.paragraph.getLockHolder());

        setLineWrap(true);
        setWrapStyleWord(true);

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


    public void sendPendingInserts() {
        // Combine all inserts into a single diff

        // TODO
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


    public int getStartClickIndex() {
        return startClickIndex;
    }


    public void setStartClickIndex(int startClickIndex) {
        this.startClickIndex = startClickIndex;
    }


    public int getEndIndex() {
        return endIndex;
    }


    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

}
