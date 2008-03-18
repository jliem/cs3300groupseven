package colab.common.util;

import junit.framework.TestCase;

public final class StringUtilsTester extends TestCase {

    public void testContainsOnlyCharactersTrue() {

        String validChars = "123456";
        String str = "613461251634126123456121";

        assertTrue(StringUtils.containsOnlyCharacters(str, validChars));

    }

    public void testContainsOnlyCharactersFalse() {

        String validChars = "123456";
        String str = "6134612516341" + "7" + "26123456121";

        assertFalse(StringUtils.containsOnlyCharacters(str, validChars));

    }

    public void testRepeatSingleCharacter() {

        String expected = "aaaaa";
        String actual = StringUtils.repeat("a", 5);

        assertEquals(expected, actual);

    }

}
