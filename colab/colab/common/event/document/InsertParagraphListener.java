package colab.common.event.document;

import colab.common.channel.document.DocumentParagraph;

public interface InsertParagraphListener {

    void onInsert(int offset, DocumentParagraph paragraph);

}
