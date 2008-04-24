package colab.common.util;

import junit.framework.TestCase;

/**
 * Test cases for {@link StringChangeBuffer}.
 */
public class StringChangeBufferTester extends TestCase {

    private class TestStringChangeBufferListener
            implements StringChangeBufferListener {

        /** The number of times an insert was fired. */
        private int inserts = 0;

        /** The number of times a delete was fired. */
        private int deletes = 0;

        public void insert(final int offset, final String str) {
            fail();
        }

        public void delete(final int offset, final int length) {
            fail();
        }

        protected void insertIncrement() {
            inserts++;
        }

        protected void deleteIncrement() {
            deletes++;
        }

        public void assertInserts(final int expectedInserts) {
            TestCase.assertEquals(expectedInserts, inserts);
        }

        public void assertDeletes(final int expectedDeletes) {
            TestCase.assertEquals(expectedDeletes, deletes);
        }

    }

    /**
     * Performs a number of inserts which can all form one contiguous
     * insert, and checks that they are aggregated into a single insert.
     *
     * @throws Exception if any exception is thrown
     */
    public void testContiguousInsert() throws Exception {

        TestStringChangeBufferListener listener =
            new TestStringChangeBufferListener();

        StringChangeBuffer buffer = new StringChangeBuffer(listener);

        StringBuilder str = new StringBuilder();
        str.append("------");

        // Do a series of inserts

        buffer.insert(3, "bcf");
        str.insert(3, "bcf");
        assertEquals("---bcf---", str.toString());

        buffer.insert(3, "a");
        str.insert(3, "a");
        assertEquals("---abcf---", str.toString());

        buffer.insert(6, "de");
        str.insert(6, "de");
        assertEquals("---abcdef---", str.toString());

        // Do a manual update, which should cause the insert to fire

        listener = new TestStringChangeBufferListener() {
            public void insert(final int offset, final String str) {
                insertIncrement();
                assertEquals(str, "abcdef");
            }
        };
        buffer.setListener(listener);
        buffer.update();
        listener.assertInserts(1);

    }

    /**
     * Performs a simple delete.
     *
     * @throws Exception if any exception is thrown
     */
    public void testSingleDelete() throws Exception {

        TestStringChangeBufferListener listener =
                new TestStringChangeBufferListener();

        StringChangeBuffer buffer = new StringChangeBuffer(listener);

        StringBuilder str = new StringBuilder();
        str.append("12345");

        // Do a delete

        buffer.delete(2, 1);
        str.delete(2, 2 + 1);
        assertEquals("1245", str.toString());

        // Do a manual update, which should cause the delete to fire.

        listener = new TestStringChangeBufferListener() {
            public void delete(final int offset, final int length) {
                deleteIncrement();
                assertEquals(2, offset);
                assertEquals(1, length);
            }
        };
        buffer.setListener(listener);
        buffer.update();
        listener.assertDeletes(1);

    }

    /**
     * Performs an insert, then a delete.
     * Both updates should fire.
     *
     * @throws Exception if any exception is thrown
     */
    public void testInsertThenDelete() throws Exception {

        TestStringChangeBufferListener listener =
                new TestStringChangeBufferListener();

        StringChangeBuffer buffer = new StringChangeBuffer(listener);

        StringBuilder str = new StringBuilder();
        str.append("1245");

        // Do an insert

        buffer.insert(2, "3");
        str.insert(2, "3");
        assertEquals("12345", str.toString());

        // Do a delete, which should cause the previous insert to fire

        listener = new TestStringChangeBufferListener() {
            public void insert(final int offset, final String str) {
                insertIncrement();
                assertEquals(2, offset);
                assertEquals(str, "3");
            }
        };
        buffer.setListener(listener);

        buffer.delete(2, 1);
        str.delete(2, 2 + 1);
        assertEquals("1245", str.toString());

        listener.assertInserts(1);

        // Do a manual update, which should cause the delete to fire.

        listener = new TestStringChangeBufferListener() {
            public void delete(final int offset, final int length) {
                deleteIncrement();
                assertEquals(2, offset);
                assertEquals(1, length);
            }
        };
        buffer.setListener(listener);
        buffer.update();
        listener.assertDeletes(1);

    }

}
