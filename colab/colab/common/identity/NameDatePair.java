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

    public boolean equals(NameDatePair pair) {
        return name.equals(pair.getName()) && date.equals(pair.getDate());
    }
    
    public int compareTo(NameDatePair pair) {
        int ret = date.compareTo(pair.getDate());
        
        if(ret == 0) {
            ret = name.compareTo(pair.getName());
        }
        
        return ret;
    }
}