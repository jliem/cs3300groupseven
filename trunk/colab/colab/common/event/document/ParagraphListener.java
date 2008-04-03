package colab.common.event.document;

import colab.common.naming.UserName;

public interface ParagraphListener {

    void onLock(UserName newOwner);

    void onUnlock();

    void onHeaderChange(int headerLevel);

    void onInsert(int offset, String hunk);

    void onDelete(int offset, int length);

}
