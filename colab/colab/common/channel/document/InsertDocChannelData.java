package colab.common.channel.document;

import java.util.Date;

import colab.common.channel.ChannelDataIdentifier;
import colab.common.channel.whiteboard.InsertLayer;
import colab.common.channel.whiteboard.layer.LayerIdentifier;
import colab.common.exception.NotApplicableException;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.UserName;
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

    public InsertDocChannelData(final ParagraphIdentifier previous,
            final UserName creator) {
        this(previous, null, creator, new Date());
        this.paragraph = new DocumentParagraph();
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

    public void setId(final ChannelDataIdentifier id) {

        super.setId(id);
        ParagraphIdentifier paragraphId;
        if (id != null) {
            paragraphId = new ParagraphIdentifier(id);
        } else {
            paragraphId = null;
        }
        this.paragraph.setId(paragraphId);

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
    @Override
    public XmlNode toXml() {

        XmlNode node = super.toXml();

        String prevStr = "";
        if (this.previous != null) {
            prevStr = this.previous.getValue().toString();
        }
        node.setAttribute("previous", prevStr);
        node.addChild(this.paragraph.toXml());

        return node;

    }

    /** {@inheritDoc} */
    @Override
    public void fromXml(final XmlNode node) throws XmlParseException {

        super.fromXml(node);

        String prevStr = node.getAttribute("previous");
        if (!prevStr.equals("")) {
            try {
                this.previous = new ParagraphIdentifier(
                        Integer.parseInt(prevStr));
            } catch (final NumberFormatException e) {
                throw new XmlParseException(e);
            }
        }

        this.paragraph = new DocumentParagraph();
        this.paragraph.fromXml(node.getChildren().get(0));

    }

    public InsertDocChannelData copy() {

        UserName username = null;
        if (super.getCreator() != null) {
            username = new UserName(super.getCreator().getValue());
        }

        ParagraphIdentifier li = null;
        if (this.getId() != null) {
            li = new ParagraphIdentifier(this.getId().getValue());
        }


        InsertDocChannelData copy =
            new InsertDocChannelData(li, username);

        if (paragraph != null) {
            copy.setParagraph(paragraph.copy());
        }

        // Set channel id
        copy.setId(this.getId());

        // Set previous
        if (previous != null) {
            copy.previous = new ParagraphIdentifier(previous);
        } else {
            copy.previous = null;
        }

        return copy;
    }

}
