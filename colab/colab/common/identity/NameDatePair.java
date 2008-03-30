package colab.common.identity;

import java.util.Date;

import colab.common.naming.UserName;

/**
 * A simple pair data structure which holds a UserName and a Date.
 */
public final class NameDatePair implements Comparable<NameDatePair> {

    private final UserName name;

    private final Date date;

    /**
     * Constructs a new NameDatePair with a given name and date.
     *
     * @param name the name
     * @param date the date
     */
    public NameDatePair(final UserName name, final Date date) {
        this.name = name;
        this.date = date;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return the name
     */
    public UserName getName() {
        return name;
    }

    /** {@inheritDoc} */
    public int compareTo(final NameDatePair pair) {
        int ret = date.compareTo(pair.getDate());

        if(ret == 0) {
            ret = name.compareTo(pair.getName());
        }

        return ret;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {

        if (!(object instanceof NameDatePair)) {
            return false;
        }

        NameDatePair pair = (NameDatePair) object;

        return name.equals(pair.getName()) && date.equals(pair.getDate());

    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {

        return name.hashCode() + date.hashCode();

    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return name.toString() + " " + date.toString();
    }

}
