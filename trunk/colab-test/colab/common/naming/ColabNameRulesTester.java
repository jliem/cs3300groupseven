package colab.common.naming;

import junit.framework.TestCase;
import colab.common.util.StringUtils;

/**
 * Test cases for {@link ColabNameRules}.
 */
public final class ColabNameRulesTester extends TestCase {

    /**
     * Tests validating an empty-string community name.
     */
    public void testEmptyCommunityName() {

        String name = "";

        assertTrue(ColabNameRules.isValidCommunityName(name));

    }

    /**
     * Tests validating a single-character community name.
     */
    public void testSingleCharacterCommunityName() {

        String name = "a";

        assertTrue(ColabNameRules.isValidCommunityName(name));

    }

    /**
     * Tests validating a community name whose length is exactly
     * the maximum length.
     */
    public void testMaxLengthCommunityName() {

        int maxLength = ColabNameRules.getCommunityMaxLength();
        String name = StringUtils.repeat("a", maxLength);

        assertTrue(ColabNameRules.isValidCommunityName(name));

    }

    /**
     * Tests validating a community name whose length is one
     * greater than the maximum length.
     */
    public void testTooLongCommunityName() {

        int maxLength = ColabNameRules.getCommunityMaxLength();
        String name = StringUtils.repeat("a", maxLength + 1);

        assertFalse(ColabNameRules.isValidCommunityName(name));

    }

    /**
     * Tests validating a community name with an invalid
     * character in it.
     */
    public void testInvalidCharacterInCommunityName() {

        String name = "abcd$efgh";

        assertFalse(ColabNameRules.isValidCommunityName(name));

    }

}
