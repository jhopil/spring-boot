package com.hdbsoft.nio.file;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.util.List;

/**
 * JAVA 7 NIO File, Path Test
 */
public class FilePathTest {

    public void testPath() {
        Path path1 = Paths.get("data");
        File file1 = path1.resolve("data/test1.txt").toFile(); // data/test1.txt
        println(file1.exists());

        Path path2 = Paths.get(URI.create("file:///data/test2.txt"));
        File file2 = path2.toFile();
        println(path2);
        println(file2.exists());
    }

    public void testFileSystem() throws IOException {
        FileSystem fs = FileSystems.getDefault();

        for(Path path : fs.getRootDirectories()) {
            println("rootDirectory => " + path);
        }

        for(String view : fs.supportedFileAttributeViews()) {
            println("fileAttriburteView => " + view);
        }

        for(FileStore store : fs.getFileStores()) {
            boolean supported = store.supportsFileAttributeView(BasicFileAttributeView.class);
            println("fileStore => " + store + " is supported : " + supported);

            long totalSpace = store.getTotalSpace();
            println("Total Space: " + totalSpace);
        }
    }

    public void testFile() throws IOException {
        Path path = FileSystems.getDefault().getPath("./data/test3.txt");
        //Files.createDirectories(path);
        if(Files.exists(path) == false) {
            Files.createFile(path);
        }
        Files.write(path, "Hello World!!\n".getBytes(), StandardOpenOption.TRUNCATE_EXISTING);

        List<String> lines = Files.readAllLines(path);
        for(String line : lines) {
            println(line);
        }

    }

    private void println(Object object) {
        String called = "";
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        for(StackTraceElement st : sts) {
            String str = st.toString();
            if(str.contains(this.getClass().getName() + ".test")) {
                called = str;
                break;
            }
        }
        System.out.println(called + " ==> " + object);
    }

    public static void main(String[] args) throws IOException {

        FilePathTest fp = new FilePathTest();
        fp.testPath();
        fp.testFileSystem();
        fp.testFile();

    }

}
