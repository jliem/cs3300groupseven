package colab.common.channel.document;

import java.util.Date;

import colab.common.exception.NotApplicableException;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.UserName;
import colab.common.util.StringUtils;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;

public final class InsertDocChannelData extends DocumentChannelData {

    /** Serialization version number. */
    private static final long serialVersionUID = 1;

    private ParagraphIdentifier previous;

    private DocumentParagraph paragraph;

    /**
     * Constructs an empty InsertDocChannelData.
     */
    public InsertDocChannelData() {
    }

    /**
     * Constructs a new InsertDocChannelData.
     *
     * @param previous the id of the preceding paragraph in the document,
     *                 to indicate the relative position of the new paragraph;
     *                 null if inserting at the beginning of the document
     * @param paragraph the paragraph inserted by this revision
     * @param creator the user who added the revision
     * @param timestamp the time at which the revision was made
     */
    public InsertDocChannelData(final ParagraphIdentifier previous,
            final DocumentParagraph paragraph,
            final UserName creator, final Date timestamp) {

        super(creator, timestamp, DocumentChannelDataType.INSERT);

        this.previous = previous;
        this.paragraph = paragraph;

    }

    @Override
    public void apply(final Document doc) throws NotApplicableException {

        doc.insert(previous, paragraph);

    }

    public ParagraphIdentifier getPrevious() {
        return previous;
    }

    public void setPrevious(final ParagraphIdentifier previous) {
        this.previous = previous;
    }

    public DocumentParagraph getParagraph() {
        return paragraph;
    }

    public void setParagraph(final DocumentParagraph paragraph) {
        this.paragraph = paragraph;
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Insert";
    }

    /** {@inheritDoc} */
    public XmlNode toXml() {

        XmlNode node = super.toXml();

        node.setAttribute("previous", StringUtils.emptyIfNull(
                this.previous.getValue().toString()));
        node.addChild(this.paragraph.toXml());

        return node;

    }

    /** {@inheritDoc} */
    public void fromXml(final XmlNode node) throws XmlParseException {

        super.fromXml(node);

        try {
            this.previous = new ParagraphIdentifier(
                    Integer.parseInt(node.getAttribute("previous")));
        } catch (final NumberFormatException e) {
            throw new XmlParseException(e);
        }

        this.paragraph = new DocumentParagraph();
        this.paragraph.fromXml(node.getChildren().get(0));

    }

}
