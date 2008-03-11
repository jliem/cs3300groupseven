package colab.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A utility class containing methods for dealing with files.
 */
public final class FileUtils {

    /** The size of the char buffer used in getContentAsString. */
    private static final int GETCONTENT_BUFFER_SIZE = 1024;

    /** Hidden default constructor. */
    private FileUtils() {
    }

    /**
     * Ensures the existence of a directory at the given path.
     *
     * @param pathStr an absolute or relative file path
     * @return the given directory
     * @throws IOException if the directory cannot be created,
     *                     or if a file exists at the specified path
     */
    public static File getDirectory(final String pathStr)
            throws IOException {

        File file = new File(pathStr);

        if (!file.isAbsolute()) {

            // If a relative path was given, prepend the user path
            file = new File(System.getProperty("user.dir")
                    + File.separator + pathStr);

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

    /**
     * Ensures the existence of a directory at the given path.
     *
     * @param parent the parent of the desired directory
     *               (must already exist and be a directory)
     * @param subdirectoryName the name of the desired subdirectory
     * @return the given subdirectory
     * @throws IOException if the parent is not a directory,
     *                     if the subdirectory cannot be created,
     *                     or if a file exists at the specified path
     */
    public static File getDirectory(final File parent,
            final String subdirectoryName) throws IOException {

        if (!parent.isDirectory()) {
            throw new IOException("Parent is not a directory");
        }

        String parentPath = parent.getAbsolutePath();
        String path = parentPath + File.separator + subdirectoryName;
        return getDirectory(path);

    }

    /**
     * Ensures the existence of a file at the given path.
     *
     * @param pathStr an absolute or relative file path
     * @return the given file
     * @throws IOException if the file cannot be created,
     *                     or if a directory exists at the specified path
     */
    public static File getFile(final String pathStr)
            throws IOException {

        File file = new File(pathStr);

        if (!file.isAbsolute()) {

            // If a relative path was given, prepend the user path
            file = new File(System.getProperty("user.dir")
                    + File.separator + pathStr);

        }


        if (file.exists()) {

            // If the path exists but is not a file, throw an exception
            if (!file.isFile()) {
                throw new IOException(pathStr + " is not a file");
            }

        } else {

            // If the path does not exist, create it
            if (!file.createNewFile()) {

                // If unable to create the file, throw an exception
                throw new IOException("Unable to create file");

            }

        }

        return file;

    }

    /**
     * Ensures the existence of a file at the given path.
     *
     * @param parent the parent of the desired directory
     *               (must already exist and be a directory)
     * @param fileName the name of the desired file
     * @return the given file
     * @throws IOException if the parent is not a directory,
     *                     if the file cannot be created,
     *                     or if a file exists at the specified path
     */
    public static File getFile(final File parent,
            final String fileName) throws IOException {

        if (!parent.isDirectory()) {
            throw new IOException("Parent is not a directory");
        }

        String parentPath = parent.getAbsolutePath();
        String path = parentPath + File.separator + fileName;
        return getFile(path);

    }

    /**
     * Writes a string and a line break to the end of a file.
     * @param file the file to modify
     * @param line the string to append
     * @throws IOException if any problem occurs in writing to the file
     */
    public static void appendLine(final File file, final String line)
            throws IOException {

        FileWriter writer = new FileWriter(file, true);
        writer.write(line + "\n");
        writer.flush();

    }

    /**
     * Reads all of the content from a file and returns
     * it as a single string.
     *
     * This is not an efficient way to do file I/O, and is
     * provided only as a convenience for use in test cases.
     *
     * @param file the file to read
     * @return the content of the file, as a string
     * @throws IOException if any I/O problem occurs
     */
    public static String getContentAsString(final File file)
            throws IOException {

        StringBuilder str = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        char[] buffer = new char[GETCONTENT_BUFFER_SIZE];
        int charsRead;
        while ((charsRead = reader.read(buffer)) != -1) {
            str.append(buffer, 0, charsRead);
        }
        reader.read(buffer);
        return str.toString();

    }

}
