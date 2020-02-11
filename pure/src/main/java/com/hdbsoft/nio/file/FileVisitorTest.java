package com.hdbsoft.nio.file;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FileVisitorTest {
    private static SimpleFileVisitor<Path> sfv = new SimpleFileVisitor() {
        @Override
        public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
            System.out.println("visited directory => " + dir.toString());
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Object file, IOException exc) throws IOException {
            System.out.println(exc);
            return FileVisitResult.CONTINUE;
        }
    };

    public static void main(String[] args) throws IOException {

        Path path = Paths.get(".");
        Files.walkFileTree(path, sfv);
    }
}
