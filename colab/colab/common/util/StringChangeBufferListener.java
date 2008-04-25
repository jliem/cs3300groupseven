package colab.common.util;

/**
 * A listener which can be notified of events fired by a StringChangeBuffer.
 */
public interface StringChangeBufferListener {

    /**
     * Called when text is inserted.
     *
     * @param offset the offset of the insert
     * @param str the inserted text
     */
    void insert(int offset, String str);

    /**
     * Called when text is deleted.
     *
     * @param offset the starting position of the delete
     * @param length the number of deleted characters
     */
    void delete(int offset, int length);

}
