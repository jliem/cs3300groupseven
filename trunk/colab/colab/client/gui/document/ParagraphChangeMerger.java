package colab.client.gui.document;

import java.util.Date;

import colab.common.channel.document.EditDocChannelData;
import colab.common.channel.document.diff.DocumentParagraphDiff;
import colab.common.event.document.ParagraphListener;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.UserName;

public class ParagraphChangeMerger implements ParagraphListener {

    private DocumentPanel panel;

    private UserName user;

    private ParagraphIdentifier id;

    public ParagraphChangeMerger(DocumentPanel panel,
            ParagraphIdentifier id) {

        if (panel == null) {
            throw new IllegalArgumentException("DocumentPanel cannot be null");
        }

        this.panel = panel;
        this.id = id;
        user = panel.getUsername();
    }
    public void onDelete(int offset, int length) {
//        DocumentParagraph paragraph = currentDocument.get(id);
//        if (paragraph != null) {
//            //paragraph.delete(offset, length);

        DocumentParagraphDiff diff = new DocumentParagraphDiff();
        diff.delete(offset, length);

        EditDocChannelData edit = new EditDocChannelData(id,
                diff,
                user,
                new Date());

        panel.fireOnMessageSent(edit);

//            add(edit);
//        }
    }


    public void onHeaderChange(int headerLevel) {
        // TODO Auto-generated method stub

    }


    public void onInsert(int offset, String hunk) {
        DocumentParagraphDiff diff = new DocumentParagraphDiff();
        diff.insert(offset, hunk);

        EditDocChannelData edit = new EditDocChannelData(id,
                diff,
                user,
                new Date());

        panel.fireOnMessageSent(edit);

    }


    public void onLock(UserName newOwner) {
        // TODO Auto-generated method stub

    }


    public void onUnlock() {
        // TODO Auto-generated method stub

    }

}
