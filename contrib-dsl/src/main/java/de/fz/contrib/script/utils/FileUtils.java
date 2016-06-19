package de.fz.contrib.script.utils;


import java.io.*;

/**
 * @author felixz
 */
public final class FileUtils {

    /**
     * Prevent instantiating this class.
     */
    private FileUtils() {
    }

    /**
     * Default buffer size for reading files.
     */
    private static final int DEFAULT_BUFFER_SIZE = 256;

    /**
     * Returns an input stream if the given fileName is found on the classpath.
     *
     * @param fileName the file to open
     * @return an InputStream representing the input file
     */
    public static InputStream readInternal(final String fileName) {
        InputStream inputStream = FileUtils.class.getResourceAsStream("/" + fileName);
        if (inputStream == null) {
            throw new RuntimeException("File not found: " + fileName);
        }
        return inputStream;
    }

    /**
     * Returns a reader if the given fileName is found on the classpath.
     *
     * @param fileName the file to open
     * @return a Reader representing the input file
     */
    public static Reader readerInternal(final String fileName) {
        return new InputStreamReader(FileUtils.readInternal(fileName));
    }

    /**
     * Reads the entire file into a string.
     *
     * @param fileName the file to read
     * @return a string containing the entire file
     */
    public static String readInternalAsString(final String fileName) {
        InputStream inputStream = null;
        Reader reader = null;
        StringBuilder output = null;
        try {
            inputStream = FileUtils.readInternal(fileName);
            reader = FileUtils.readerInternal(fileName);
            output = new StringBuilder(inputStream.available());
            char[] buffer = new char[DEFAULT_BUFFER_SIZE];
            while (true) {
                int length = reader.read(buffer);
                if (length == -1) {
                    break;
                }
                output.append(buffer, 0, length);
            }
            return output.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(inputStream);
            closeQuietly(reader);
        }
        return "";
    }

    /**
     * Close and ignore all errors.
     *
     * @param closeable the closable to close
     */
    public static void closeQuietly(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignored) {
            }
        }
    }
}
