package colab.common;

import colab.common.identity.ParagraphIdentifier;

public interface EditParagraphListener {

    void onEdit(ParagraphIdentifier id, DocumentParagraphDiff difference);

}
