package colab.common.event.document;

import colab.common.identity.ParagraphIdentifier;

public interface DeleteParagraphListener {

    void onDelete(ParagraphIdentifier id);

}
