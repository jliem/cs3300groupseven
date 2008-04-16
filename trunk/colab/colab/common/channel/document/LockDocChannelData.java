package colab.common.channel.document;

import java.util.Date;

import colab.common.exception.NotApplicableException;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.InvalidUserNameException;
import colab.common.naming.UserName;
import colab.common.xml.XmlNode;
import colab.common.xml.XmlParseException;

public class LockDocChannelData extends DocumentChannelData {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private UserName lockHolder;

    private ParagraphIdentifier id;

    public LockDocChannelData(final UserName lockHolder,
            final ParagraphIdentifier id) {

        super(lockHolder, new Date(), DocumentChannelDataType.LOCK);

        this.id = id;
        this.lockHolder = lockHolder;
    }

    @Override
    public void apply(final Document doc) throws NotApplicableException {
        DocumentParagraph para = doc.get(id);

        if(para == null) {
            throw new NotApplicableException(
                    "The document has no matching paragraph.");
        }

        if(para.isLocked() && lockHolder != null) {
            throw new NotApplicableException(
                    "A locked paragraph must be unlocked before the lock "
                    + "owner may be changed.");
        }

        if(lockHolder == null) {
            para.unlock();
        } else {
            para.lock(lockHolder);
        }
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Lock";
    }

    /** {@inheritDoc} */
    @Override
    public XmlNode toXml() {

        XmlNode node = new XmlNode(xmlNodeName());
        node.setContent(this.lockHolder.getValue());
        return node;

    }

    /** {@inheritDoc} */
    @Override
    public void fromXml(final XmlNode node) throws XmlParseException {

        try {
            this.lockHolder = new UserName(node.getBody());
        } catch (final InvalidUserNameException e) {
            throw new XmlParseException(e);
        }

    }

}
