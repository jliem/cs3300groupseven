package colab.common.channel.document.diff;

import colab.common.channel.document.DocumentParagraph;
import colab.common.naming.UserName;

class ChangeLock implements Applicable {

    private UserName newOwner;

    public ChangeLock(final UserName newOwner) {
        super();
        this.newOwner = newOwner;
    }

    public void apply(final DocumentParagraph para) {
        if (newOwner==null) {
            para.unlock();
        } else {
            para.lock(newOwner);
        }
    }

    public UserName getNewOwner() {
        return newOwner;
    }
}
