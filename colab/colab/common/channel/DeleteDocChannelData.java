package colab.common.channel;

import java.util.Date;

import colab.common.Document;
import colab.common.exception.NotApplicableException;
import colab.common.identity.ParagraphIdentifier;
import colab.common.naming.UserName;

public final class DeleteDocChannelData extends DocumentChannelData {

    /** Serialization version number. */
    private static final long serialVersionUID = 1L;

    private ParagraphIdentifier paragraphID;

    public DeleteDocChannelData(final ParagraphIdentifier paragraphID,
            final UserName creator, final Date timestamp) {

        super(creator, timestamp, DocumentChannelDataType.DELETE);

        this.paragraphID = paragraphID;

    }

    @Override
    public void apply(final Document doc) throws NotApplicableException {
        doc.delete(paragraphID);
    }

    public ParagraphIdentifier getParagraphID() {
        return paragraphID;
    }

    public void setParagraphID(final ParagraphIdentifier paragraphID) {
        this.paragraphID = paragraphID;
    }
}
