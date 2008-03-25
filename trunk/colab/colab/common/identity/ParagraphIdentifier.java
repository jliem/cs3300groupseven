package colab.common.identity;

import java.util.Date;

import colab.common.naming.UserName;

/**
 * An Identifier user temporarily to identify paragraphs. The basis for the
 * identification, a pairing of a {@link UserName} (intended as the creator's
 * user name) and a Date, should be unique per document channel; however, this
 * form of identification is easily tampered with and needs to be reviewed.
 */
public final class ParagraphIdentifier extends Identifier<NameDatePair> {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    public ParagraphIdentifier(final UserName creator, final Date date) {
        super(new NameDatePair(creator, date));
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getValue().toString();
    }

    public UserName getCreator() {
        return getValue().getName();
    }

    public Date getDate() {
        return getValue().getDate();
    }

}
