package colab.common.channel.document.diff;

import java.io.Serializable;

import colab.common.channel.document.DocumentParagraph;
import colab.common.exception.NotApplicableException;
import colab.common.xml.XmlSerializable;

/**
 * The interface used to internally represent individual edits.
 */
interface Applicable extends Serializable, XmlSerializable {

    void apply(DocumentParagraph para) throws NotApplicableException;

}
