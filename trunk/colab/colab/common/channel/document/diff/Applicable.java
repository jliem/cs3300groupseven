package colab.common.channel.document.diff;

import colab.common.channel.document.DocumentParagraph;
import colab.common.exception.NotApplicableException;

/**
 * The interface used to internally represent individual edits.
 */
interface Applicable {

    void apply(DocumentParagraph para) throws NotApplicableException;

}
