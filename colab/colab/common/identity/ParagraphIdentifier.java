package colab.common.identity;

import java.util.Date;

import colab.common.naming.UserName;

/**
 * @author matt
 * A subclass of {@link Identifier<T>}, used temporarily to identify
 * paragraphs.  The basis for the identification, a pairing of a {@link UserName}
 * (intended as the creator's user name) and a Date, should be unique per
 * document channel; however, this form of identification is easily
 * tampered with and needs to be reviewed. 
 */
public class ParagraphIdentifier extends Identifier<NameDatePair> {
    public static final long serialVersionUID = 1;

    public ParagraphIdentifier(UserName creator, Date date) {
        super(new NameDatePair(creator, date));
    }

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