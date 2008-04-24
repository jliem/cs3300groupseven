package colab.common.util;

import java.io.File;
import java.util.Scanner;

import junit.framework.TestCase;

/**
 * Test cases for {@link FileUtils}.
 */
public final class FileUtilsTester extends TestCase {

    /**
     * Creates a temporary file, appends two lines to it, then opens
     * a scanner on the file to ensure that the lines were added.
     *
     * @throws Exception if any exception is thrown.
     */
    public void testAppendLine() throws Exception {

        File file = File.createTempFile("colabTestAppendLine", null);

        FileUtils.appendLine(file, "abc");
        FileUtils.appendLine(file, "def");

        Scanner scanner = new Scanner(file);
        assertEquals(scanner.nextLine(), "abc");
        assertEquals(scanner.nextLine(), "def");

    }

}
