package colab.common.event.document;

import colab.common.channel.document.diff.DocumentParagraphDiff;
import colab.common.identity.ParagraphIdentifier;

public interface EditParagraphListener {

    void onEdit(ParagraphIdentifier id, DocumentParagraphDiff difference);

}
