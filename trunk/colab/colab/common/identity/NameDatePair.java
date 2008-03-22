package colab.common.identity;

import java.util.Date;

import colab.common.naming.UserName;

public class NameDatePair implements Comparable<NameDatePair>{
    
    private final UserName name;

    private final Date date;

    public NameDatePair(final UserName name, final Date date) {
        super();
        this.name = name;
        this.date = date;
    }
    
    @Override
    public String toString() {
        return name.toString() + " " + date.toString();
    }

    public Date getDate() {
        return date;
    }

    public UserName getName() {
        return name;
    }

    public int compareTo(NameDatePair arg0) {
        return date.compareTo(arg0.getDate());
    }
}