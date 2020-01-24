package org.petrveri.util.htmlprocessor.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.petrveri.util.htmlprocessor.exceptions.ApplicationException;
import org.petrveri.util.htmlprocessor.fs.FileSystem;

/**
 * @author Petro Veriienko
 */
public class FileWriterTest {

    @Test
    void testWriteFileAbsentDirectory() {
        String directoryPath =
                "target/test-classes/org/petrveri/util/htmlprocessor/fs/output/";
        String fileName = "html001.html";
        ApplicationException ex = Assertions.assertThrows(ApplicationException.class, () -> {
            new CharFileWriter(directoryPath + fileName).writeContent("*");
        });
        Assertions.assertEquals(
                "target\\test-classes\\org\\petrveri\\util\\htmlprocessor\\fs\\output\\html001.html" +
                        " (The system cannot find the path specified)",
                ex.getThrowable().getMessage());
    }

    @Test
    void testWriteFileCreatedDirectory() {
        String directoryPath =
                "target/test-classes/org/petrveri/util/htmlprocessor/fs/output/";
        String filePath = directoryPath + "html001.html";
        String testContent = "a test line";
        FileSystem fileSystem = new FileSystem();
        fileSystem.createDirectory(directoryPath);
        CharFileWriter writer = new CharFileWriter(filePath);
        writer.writeContent(testContent);
        CharFileReader reader = new CharFileReader(filePath);
        String content = reader.readContent().toString();
        Assertions.assertEquals(testContent, content);
        fileSystem.delete(filePath);
        fileSystem.delete(directoryPath);
    }
}
