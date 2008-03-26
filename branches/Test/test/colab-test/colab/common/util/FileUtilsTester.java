package colab.common.util;

import java.io.File;
import java.util.Scanner;

import junit.framework.TestCase;

public final class FileUtilsTester extends TestCase {

    public void testAppendLine() throws Exception {

        File file = File.createTempFile("colabTestAppendLine", null);

        FileUtils.appendLine(file, "abc");
        FileUtils.appendLine(file, "def");

        Scanner scanner = new Scanner(file);
        assertEquals(scanner.nextLine(), "abc");
        assertEquals(scanner.nextLine(), "def");

    }

}
