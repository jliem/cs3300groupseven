package colab.util;

import java.io.File;
import java.io.IOException;

/**
 * A utility class containing methods for dealing with files.
 */
public final class FileUtils {

    /** Hidden default constructor. */
    private FileUtils() {
    }

    public static File getOrCreateDirectory(final String pathStr)
            throws IOException {

        File file = new File(pathStr);

        if (!file.isAbsolute()) {

            // If a relative path was given, prepend the user path
            file = new File(System.getProperty("user.dir")
                    + File.pathSeparator + pathStr);

        }


        if (file.exists()) {

            // If the path exists but is not a directory, throw an exception
            if (!file.isDirectory()) {
                throw new IOException(pathStr + " is not a directory");
            }

        } else {

            // If the path does not exist, create it
            if (!file.mkdir()) {

                // If unable to create the directory, throw an exception
                throw new IOException("Unable to create directory");

            }

        }

        return file;

    }

}
