package org.petrveri.util.htmlprocessor.parsing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.petrveri.util.htmlprocessor.config.AppConfig;
import org.petrveri.util.htmlprocessor.config.ConfigKeys;
import org.petrveri.util.htmlprocessor.fs.FileSystem;
import org.petrveri.util.htmlprocessor.io.CharFileReader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.inject.Inject;

/**
 * @author Petro Veriienko
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {FileProcessorTestConfig.class}, loader = AnnotationConfigContextLoader.class)
public class FileProcessorTest {

    @Inject
    private AppConfig appConfig;
    @Inject
    private FileProcessor fileProcessor;
    @Inject
    private FileSystem fileSystem;

    @Test
    void testFileWildCard() {
        Assertions.assertTrue(fileProcessor.isFileWildCard("*.html"));
    }

    @Test
    void testProcessFile() {
        String inputFilePath = "target/test-classes/org/petrveri/util/htmlprocessor/fs/html.html";
        String outputDirectoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/parsing/output/";
        String outputFilePath = fileSystem.joinPathParts(outputDirectoryPath, "html.html");
        fileSystem.createDirectory(outputDirectoryPath);
        fileProcessor.processFile(inputFilePath, outputFilePath);
        String expectedDirectoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/parsing/output_expected/";
        assertFilesEqual(fileSystem.joinPathParts(expectedDirectoryPath, "html_expected.html"),
                outputFilePath);
        fileSystem.delete(outputFilePath);
        fileSystem.delete(outputDirectoryPath);
    }

    @Test
    void testProcessFiles4() {
        String inputDirectoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/fs/";
        String inputFileName = "html.html";
        String outputDirectoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/parsing/output/";
        String outputFileName = "html.html";
        String outputFilePath = fileSystem.joinPathParts(outputDirectoryPath, outputFileName);
        fileSystem.createDirectory(outputDirectoryPath);
        fileProcessor.processFiles(inputFileName, outputFileName, inputDirectoryPath, outputDirectoryPath);
        String expectedDirectoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/parsing/output_expected/";
        assertFilesEqual(fileSystem.joinPathParts(expectedDirectoryPath, "html_expected.html"),
                outputFilePath);
        fileSystem.delete(outputFilePath);
        fileSystem.delete(outputDirectoryPath);
    }

    @Test
    void testProcessFiles2FilePath1FilePath2() {
        String inputDirectoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/fs/";
        String inputFileName = "html.html";
        String outputDirectoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/parsing/output/";
        String outputFileName = "html.html";
        String outputFilePath = fileSystem.joinPathParts(outputDirectoryPath, outputFileName);
        fileProcessor.processFiles(fileSystem.joinPathParts(inputDirectoryPath, inputFileName), outputFilePath);
        String expectedDirectoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/parsing/output_expected/";
        assertFilesEqual(fileSystem.joinPathParts(expectedDirectoryPath, "html_expected.html"),
                outputFilePath);
        fileSystem.delete(outputFilePath);
        fileSystem.delete(outputDirectoryPath);
    }

    @Test
    void testProcessFiles2FilePath1DirectoryPath2() {
        String inputDirectoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/fs/";
        String inputFileName = "html.html";
        String outputDirectoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/parsing/output/";
        fileProcessor.processFiles(fileSystem.joinPathParts(inputDirectoryPath, inputFileName), outputDirectoryPath);
        String outputFilePath = fileSystem.joinPathParts(outputDirectoryPath, inputFileName);
        String expectedDirectoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/parsing/output_expected/";
        assertFilesEqual(fileSystem.joinPathParts(expectedDirectoryPath, "html_expected.html"),
                outputFilePath);
        fileSystem.delete(fileSystem.joinPathParts(outputDirectoryPath, inputFileName));
        fileSystem.delete(outputDirectoryPath);
    }

    @Test
    void testProcessFiles2FilePath1Null() {
        String inputDirectoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/fs/";
        String inputFileName = "html.html";
        fileProcessor.processFiles(fileSystem.joinPathParts(inputDirectoryPath, inputFileName), null);
        String outputDirectoryPath = appConfig.getString(ConfigKeys.DEFAULT_OUTPUT_DIRECTORY_PATH);
        String outputFilePath = fileSystem.joinPathParts(outputDirectoryPath, inputFileName);
        String expectedDirectoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/parsing/output_expected/";
        assertFilesEqual(fileSystem.joinPathParts(expectedDirectoryPath, "html_expected.html"),
                outputFilePath);
        fileSystem.delete(outputFilePath);
        fileSystem.delete(outputDirectoryPath);
    }

    @Test
    void testProcessFiles2DirectoryPath1Null() {
        String inputDirectoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/fs/inputManyFiles/";
        fileProcessor.processFiles(inputDirectoryPath, null);
        String outputDirectoryPath = appConfig.getString(ConfigKeys.DEFAULT_OUTPUT_DIRECTORY_PATH);
        String expectedDirectoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/parsing/output_expected/";
        assertFilesEqual(fileSystem.joinPathParts(expectedDirectoryPath, "htmlMeta_expected.html"),
                fileSystem.joinPathParts(outputDirectoryPath, "htmlMeta.html"));
        assertFilesEqual(fileSystem.joinPathParts(expectedDirectoryPath, "htmlScript_expected.html"),
                fileSystem.joinPathParts(outputDirectoryPath, "htmlScript.html"));
        fileSystem.delete(fileSystem.joinPathParts(outputDirectoryPath, "htmlMeta.html"));
        fileSystem.delete(fileSystem.joinPathParts(outputDirectoryPath, "htmlScript.html"));
        fileSystem.delete(outputDirectoryPath);
    }

    @Test
    void testProcessFiles2FileName1Null() {
        String inputFileName = "htmlMeta.html";
        fileProcessor.processFiles(inputFileName, null);
        String outputDirectoryPath = appConfig.getString(ConfigKeys.DEFAULT_OUTPUT_DIRECTORY_PATH);
        String expectedDirectoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/parsing/output_expected/";
        assertFilesEqual(fileSystem.joinPathParts(expectedDirectoryPath, "htmlMeta_expected.html"),
                fileSystem.joinPathParts(outputDirectoryPath, "htmlMeta.html"));
        fileSystem.delete(fileSystem.joinPathParts(outputDirectoryPath, "htmlMeta.html"));
        fileSystem.delete(outputDirectoryPath);
    }

    @Test
    void testProcessFiles2DirectoryName1Null() {
        String inputFileName = "inputManyFiles2ndLevel";
        fileProcessor.processFiles(inputFileName, null);
        String outputDirectoryPath = appConfig.getString(ConfigKeys.DEFAULT_OUTPUT_DIRECTORY_PATH);
        String expectedDirectoryPath = "target/test-classes/org/petrveri/util/htmlprocessor/parsing/output_expected/";
        assertFilesEqual(fileSystem.joinPathParts(expectedDirectoryPath, "htmlLink_expected.html"),
                fileSystem.joinPathParts(outputDirectoryPath, "htmlLink.html"));
        assertFilesEqual(fileSystem.joinPathParts(expectedDirectoryPath, "htmlIframe_expected.html"),
                fileSystem.joinPathParts(outputDirectoryPath, "htmlIframe.html"));
        fileSystem.delete(fileSystem.joinPathParts(outputDirectoryPath, "htmlLink.html"));
        fileSystem.delete(fileSystem.joinPathParts(outputDirectoryPath, "htmlIframe.html"));
        fileSystem.delete(outputDirectoryPath);
    }

    private void assertFilesEqual(String expectedFilePath, String actualFilePath) {
        StringBuilder expectedContent = new CharFileReader(expectedFilePath).readContent();
        StringBuilder actualContent = new CharFileReader(actualFilePath).readContent();
        Assertions.assertEquals(expectedContent.toString(), actualContent.toString());
    }
}
