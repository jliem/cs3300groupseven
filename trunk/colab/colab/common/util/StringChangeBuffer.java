package colab.common.util;

/**
 * A string change buffer is used to consolidate multiple small
 * inserts/deletes on some text into larger inserts/deletes whenever
 * possible (when the edits are contiguous).
 */
public class StringChangeBuffer {

    private enum State { INSERT, DELETE, NIL };

    private State state = State.NIL;

    private int startPosition = 0;

    private StringBuilder insertString = new StringBuilder();

    private int deleteLength = 0;

    private StringChangeBufferListener listener;

    /**
     * Constructs a new StringChangeBuffer.
     *
     * @param listener the listener to use
     */
    public StringChangeBuffer(final StringChangeBufferListener listener) {
        setListener(listener);
    }

    /**
     * @param listener the listener to use
     */
    void setListener(final StringChangeBufferListener listener) {
        this.listener = listener;
    }

    /**
     * Adds some insertion to the buffer.
     *
     * @param offset the position of the insert
     * @param str the inserted text
     */
    public void insert(final int offset, final String str) {

        switch (state) {
        case INSERT:
            if (offset >= startPosition
                    && offset <= startPosition + insertString.length()) {
                insertString.insert(offset - startPosition, str);
            } else {
                update();
                insert(offset, str);
            }
            break;
        case DELETE:
            update();
            insert(offset, str);
            break;
        default:
            state = State.INSERT;
            startPosition = offset;
            insertString.append(str);
            break;
        }
    }

    /**
     * Adds some deletion to the buffer.
     *
     * @param offset the starting position of the delete
     * @param length the number of deleted characters
     */
    public void delete(final int offset, final int length) {

        switch (state) {
        case INSERT:
            update();
            delete(offset, length);
            break;
        case DELETE:
            if (offset <= startPosition
                    && offset + length >= startPosition) {
                deleteLength += length;
                startPosition = offset;
            } else {
                update();
                delete(offset, length);
            }
            break;
        default:
            state = State.DELETE;
            startPosition = offset;
            deleteLength = length;
            break;
        }

    }

    /**
     * Fires any pending event, and resets the state.
     */
    public void update() {

        switch (state) {
        case INSERT:
            if (insertString.length() != 0) {
                listener.insert(startPosition, insertString.toString());
                insertString.setLength(0);
            }
            break;
        case DELETE:
            if (deleteLength != 0) {
                listener.delete(startPosition, deleteLength);
                deleteLength = 0;
            }
            break;
        default:
            break;
        }

        this.state = State.NIL;

    }

}
