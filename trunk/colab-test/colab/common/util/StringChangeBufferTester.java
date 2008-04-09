package colab.common.util;

import junit.framework.TestCase;

/**
 * Test cases for {@link StringChangeBuffer}.
 */
public class StringChangeBufferTester extends TestCase {

    public void testContiguousInsert() throws Exception {

        StringChangeBufferListener listener =
                new StringChangeBufferListener() {
            public void insert(final int offset, final String str) {
                assertEquals(str, "abcdef");
            }
            public void delete(final int offset, final int length) {
                fail();
            }
        };

        StringChangeBuffer buffer = new StringChangeBuffer(listener);

        StringBuilder str = new StringBuilder();
        str.append("------");

        buffer.insert(3, "bcf");
        str.insert(3, "bcf");
        assert str.toString().equals("---bcf---");

        buffer.insert(3, "a");
        str.insert(3, "a");
        assert str.toString().equals("---abcf---");

        buffer.insert(6, "de");
        str.insert(6, "de");
        assert str.toString().equals("---abcdef---");

        buffer.update();

    }

    public void testSingleDelete() throws Exception {

        StringChangeBufferListener listener =
                new StringChangeBufferListener() {
            public void insert(final int offset, final String str) {
                fail();
            }
            public void delete(final int offset, final int length) {
                assertEquals(2, offset);
                assertEquals(1, length);
            }
        };

        StringChangeBuffer buffer = new StringChangeBuffer(listener);

        StringBuilder str = new StringBuilder();
        str.append("12345");

        buffer.delete(2, 1);
        str.delete(2, 2 + 1);
        assert str.toString().equals("1245");

        buffer.update();

    }

}
