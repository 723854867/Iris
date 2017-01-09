package com.spnetty.util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by djyin on 14-6-3.
 */
public class CtxUtils {


    /**
     * 利用jmx的接口获取进程id
     * @return
     */
    public static int pid() {
        try {
            java.lang.management.RuntimeMXBean runtime =
                    java.lang.management.ManagementFactory.getRuntimeMXBean();
            java.lang.reflect.Field jvm = runtime.getClass().getDeclaredField("jvm");
            jvm.setAccessible(true);
            sun.management.VMManagement mgmt =
                    (sun.management.VMManagement) jvm.get(runtime);
            java.lang.reflect.Method pid_method =
                    mgmt.getClass().getDeclaredMethod("getProcessId");
            pid_method.setAccessible(true);
            return (Integer) pid_method.invoke(mgmt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Pseudo URL prefix for loading from the class path: "classpath:", "classpath*:"
     */
    public static final String CLASSPATH_URL_PREFIX = "classpath:";
    /**
     * Pseudo URL prefix for loading from the class path: "classpath:", "classpath*:"
     */
    public static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";

    /**
     * URL prefix for loading from the file system: "file:"
     */
    public static final String FILE_URL_PREFIX = "file:";

    /**
     * Construct a file from the set of name elements.
     *
     * @param names the name elements
     * @return the file
     * @since 2.1
     */
    public static File getFile(String... names) {
        if (names == null) {
            throw new NullPointerException("names must not be null");
        }
        File file = null;
        for (String name : names) {
            if (file == null) {
                file = new File(name);
            } else {
                file = new File(file, name);
            }
        }
        return file;
    }



    /**
     * Load resource.
     *
     * @param location the location
     * @return the input stream
     */
    public static InputStream loadResource(String location) throws IOException {
        // classpath:
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            String path = location.substring(CLASSPATH_URL_PREFIX.length());
            return CtxUtils.class.getResourceAsStream(path);
        }
        // classpath*:
        if (location.startsWith(CLASSPATH_ALL_URL_PREFIX)) {
            String path = location.substring(CLASSPATH_ALL_URL_PREFIX.length());
            return CtxUtils.class.getResourceAsStream(path);
        }
        // file:
        if (location.startsWith(FILE_URL_PREFIX)) {
            String path = location.substring(FILE_URL_PREFIX.length());
            File f = getFile(path);
            InputStream fin = openInputStream(f);
            return fin;
        }
        //
        File f = getFile(location);
        InputStream fin = openInputStream(f);
        return fin;
    }

    /**
     * Opens a {@link java.io.FileInputStream} for the specified file, providing better
     * error messages than simply calling <code>new FileInputStream(file)</code>.
     * <p/>
     * At the end of the method either the stream will be successfully opened,
     * or an exception will have been thrown.
     * <p/>
     * An exception is thrown if the file does not exist.
     * An exception is thrown if the file object exists but is a directory.
     * An exception is thrown if the file exists but cannot be read.
     *
     * @param file the file to open for input, must not be {@code null}
     * @return a new {@link java.io.FileInputStream} for the specified file
     * @throws java.io.FileNotFoundException if the file does not exist
     * @throws java.io.IOException           if the file object is a directory
     * @throws java.io.IOException           if the file cannot be read
     * @since 1.3
     */
    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canRead() == false) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }

    /**
     * Get the contents of an <code>InputStream</code> as a list of Strings,
     * one entry per line, using the default character encoding of the platform.
     * <p/>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     *
     * @param input the <code>InputStream</code> to read from, not null
     * @return the list of Strings, never null
     * @throws NullPointerException if the input is null
     * @throws java.io.IOException          if an I/O error occurs
     * @since 1.1
     */
    public static List<String> readLines(InputStream input) throws IOException {
        return readLines(input, Charset.forName("utf8"));
    }

    /**
     * Get the contents of an <code>InputStream</code> as a list of Strings,
     * one entry per line, using the specified character encoding.
     * <p/>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     *
     * @param input    the <code>InputStream</code> to read from, not null
     * @param encoding the encoding to use, null means platform default
     * @return the list of Strings, never null
     * @throws NullPointerException if the input is null
     * @throws java.io.IOException          if an I/O error occurs
     * @since 2.3
     */
    public static List<String> readLines(InputStream input, Charset encoding) throws IOException {
        InputStreamReader reader = new InputStreamReader(input, encoding);
        return readLines(reader);
    }

    /**
     * Get the contents of a <code>Reader</code> as a list of Strings,
     * one entry per line.
     * <p/>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     *
     * @param input the <code>Reader</code> to read from, not null
     * @return the list of Strings, never null
     * @throws NullPointerException if the input is null
     * @throws java.io.IOException          if an I/O error occurs
     * @since 1.1
     */
    public static List<String> readLines(Reader input) throws IOException {
        BufferedReader reader = toBufferedReader(input);
        List<String> list = new ArrayList<String>();
        String line = reader.readLine();
        while (line != null) {
            list.add(line);
            line = reader.readLine();
        }
        return list;
    }

    /**
     * Returns the given reader if it is a {@link java.io.BufferedReader}, otherwise creates a toBufferedReader for the given
     * reader.
     *
     * @param reader the reader to wrap or return
     * @return the given reader or a new {@link java.io.BufferedReader} for the given reader
     * @since 2.2
     */
    public static BufferedReader toBufferedReader(Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

}
