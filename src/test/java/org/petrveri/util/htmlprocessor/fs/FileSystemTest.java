package org.petrveri.util.htmlprocessor.fs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.inject.Inject;

/**
 * @author Petro Veriienko
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {FileSystemTestConfig.class}, loader = AnnotationConfigContextLoader.class)
public class FileSystemTest {

    @Inject
    private FileSystem fileSystem = new FileSystem();

    @Test
    void testGetFileNameEmptyPath() {
        Assertions.assertEquals("", fileSystem.getFileName(null));
    }

    @Test
    void testGetFileNameFileExistFile() {
        String directoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/fs/";
        String fileName = "html.html";
        Assertions.assertEquals(fileName, fileSystem.getFileName(fileSystem.joinPathParts(directoryPath, fileName)));
    }

    @Test
    void testGetFileNameFileExistDirectory() {
        String directoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/fs/";
        Assertions.assertEquals("", fileSystem.getFileName(directoryPath));
    }

    @Test
    void testGetFileNameFileNotExistFile() {
        String directoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/fs/";
        String fileName = "html1.html";
        Assertions.assertEquals(fileName, fileSystem.getFileName(fileSystem.joinPathParts(directoryPath, fileName)));
    }

    @Test
    void testGetFileNameFileNotExistDirectory() {
        String filePath = "target/test-classes/org/petrveri/util/htmlprocessor/fs/";
        Assertions.assertEquals("", fileSystem.getFileName(filePath));
    }

    @Test
    void testGetFileNameFileNotExistSingle() {
        String filePath = "aaa";
        Assertions.assertEquals("aaa", fileSystem.getFileName(filePath));
    }

    @Test
    void testGetDirNameEmptyPath() {
        Assertions.assertEquals("", fileSystem.getDirectoryPath(null));
    }

    @Test
    void testGetDirNameFileExistFile() {
        String directoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/fs/";
        String fileName = "html.html";
        String directoryPathExp = directoryPath.substring(0, directoryPath.length());
        Assertions.assertEquals(directoryPathExp, fileSystem.getDirectoryPath(fileSystem.joinPathParts(directoryPath, fileName)));
    }

    @Test
    void testGetDirNameFileExistDirectory() {
        String directoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/fs/";
        Assertions.assertEquals(directoryPath, fileSystem.getDirectoryPath(directoryPath));
    }

    @Test
    void testGetDirNameFileNotExistFile() {
        String directoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/fs/";
        String fileName = "html1.html";
        Assertions.assertEquals(directoryPath, fileSystem.getDirectoryPath(fileSystem.joinPathParts(directoryPath, fileName)));
    }

    @Test
    void testGetDirNameFileNotExistDirectory() {
        String directoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/fsaaaa/";
        Assertions.assertEquals(directoryPath, fileSystem.getDirectoryPath(directoryPath));
    }

    @Test
    void testGetDirNameFileNotExistSingle() {
        String filePath = "aaa";
        Assertions.assertEquals("aaa", fileSystem.getDirectoryPath(filePath));
    }

    @Test
    void testDeleteDirectoryNotExist() {
        String directoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/fs/aaaa/";
        fileSystem.delete(directoryPath);;
    }







}
