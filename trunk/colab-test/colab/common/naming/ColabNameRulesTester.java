package colab.common.naming;

import junit.framework.TestCase;

public class ColabNameRulesTester extends TestCase {

    public void testIsValidCommunityNameLength() {
        int maxLength = ColabNameRules.getCommunityMaxLength();

        StringBuilder s = new StringBuilder();

        // Empty string
        assertTrue(ColabNameRules.isValidCommunityName(s.toString()));

        s.append('a');

        // Single character
        assertTrue(ColabNameRules.isValidCommunityName(s.toString()));

        // Max valid length
        for (int i=0; i<maxLength-1; i++) {
            s.append('a');
        }

        System.out.println(s.toString().length());
        assertTrue(ColabNameRules.isValidCommunityName(s.toString()));

        // Valid length + 1
        s.append('a');

        assertFalse(ColabNameRules.isValidCommunityName(s.toString()));
    }

    public void testIsValidCommunityNameChars() {
        StringBuilder s = new StringBuilder();

        // Empty string
        assertTrue(ColabNameRules.isValidCommunityName(s.toString()));

        s.append('a');

        // Single character
        assertTrue(ColabNameRules.isValidCommunityName(s.toString()));

        s.append("$");

        assertFalse(ColabNameRules.isValidCommunityName(s.toString()));
    }
}
