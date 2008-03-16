package colab.common.util;

import junit.framework.TestCase;

public final class StringUtilsTester extends TestCase {

    public void testContainsOnlyCharacters1() {
        assertTrue(StringUtils.containsOnlyCharacters(
                "613461251634126123456121", "123456"));
    }

    public void testContainsOnlyCharacters2() {
        assertFalse(StringUtils.containsOnlyCharacters(
                "6134612516341" + "7" + "26123456121", "123456"));
    }

}
