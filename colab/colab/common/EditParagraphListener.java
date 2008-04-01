package colab.common;

import colab.common.identity.ParagraphIdentifier;

public interface EditParagraphListener {
    public void onEdit(ParagraphIdentifier id, DocumentParagraphDiff difference);
}
