package org.petrveri.util.htmlprocessor.parsing;

import org.apache.commons.lang3.StringUtils;
import org.petrveri.util.htmlprocessor.config.AppConfig;
import org.petrveri.util.htmlprocessor.config.ConfigKeys;
import org.petrveri.util.htmlprocessor.config.DefaultConfig;
import org.petrveri.util.htmlprocessor.config.TagConfiguration;
import org.petrveri.util.htmlprocessor.exceptions.ApplicationException;
import org.petrveri.util.htmlprocessor.fs.FileSystem;
import org.petrveri.util.htmlprocessor.io.CharFileReader;
import org.petrveri.util.htmlprocessor.io.CharFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Petro Veriienko
 */
@Component
public class FileProcessor {

    final public static String WILD_CARD = "*.html";

    @Inject
    private AppConfig appConfig;
    @Inject
    private TagConfiguration tagConfiguration;
    @Inject
    private FileSystem fileSystem;
    @Inject
    private Processor processor;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Processes files specified by inputPath and outputPath parameters.
     * Possible options:
     * inputPath: null, outputPath: null -> Process all masked files in the default input directory.
     *  Save results to the default output directory.
     * inputPath: directory path, outputPath: null -> Process all masked files in the directory specified by the path.
     *   Save results to the default output directory.
     * inputPath: directory name, outputPath: null -> Process all masked files in the directory specified by the name,
     *   that is a subdirectory of the default input directory. Save results to the default output directory.
     * inputPath: file path, outputPath: null -> Process the file specified by the path.
     *   Save the result to the default output directory.
     * inputPath: file name, outputPath: null -> Process the file specified by the name,
     *   that is a file in the default input directory. Save results to the default output directory.
     * inputPath: input file path, outputPath: output file path -> Process the file specified by the input path.
     *   Save the result to the file specified by the output path.
     * inputPath: input file name, outputPath: output file name -> Process the file specified by the name,
     *   that is a file in the default input directory.
     *   Save the result with the specified name to the default output directory.
     *
     * @param inputPath inputPath
     * @param outputPath outputPath
     */
    public void processFiles(String inputPath, String outputPath) {
        String inputDirPath = appConfig.getString(ConfigKeys.DEFAULT_INPUT_DIRECTORY_PATH,
                DefaultConfig.DEFAULT_INPUT_DIRECTORY_PATH);
        String inputFileName = FileProcessor.WILD_CARD;
        if (inputPath != null) {
            String inputPathExt = inputPath;
            if (!fileSystem.isExist(inputPathExt)) {
                inputPathExt = fileSystem.joinPathParts(inputDirPath, inputPath);
                if (!fileSystem.isExist(inputPathExt)) {
                    throw new ApplicationException(String.format("Input directory does not exist: %s", inputPath));
                }
            }
            if (fileSystem.isDirectory(inputPathExt)) {
                inputFileName = WILD_CARD;
                inputDirPath = inputPathExt;
                logger.info("Process all files by mask [{}] in the input directory [{}].", WILD_CARD, inputDirPath);
            } else {
                inputFileName = fileSystem.getFileName(inputPath);
                inputDirPath = fileSystem.getDirectoryPath(inputPathExt);
                logger.info("Process the file [{}] in the input directory [{}].", inputFileName, inputDirPath);
            }
        }
        String outputDirPath = appConfig.getString(ConfigKeys.DEFAULT_OUTPUT_DIRECTORY_PATH,
                DefaultConfig.DEFAULT_OUTPUT_DIRECTORY_PATH);
        String outputFileName = inputFileName;
        if (outputPath != null) {
            outputFileName = fileSystem.getFileName(outputPath);
            outputDirPath = fileSystem.getDirectoryPath(outputPath);
        }
        fileSystem.createDirectory(outputDirPath);
        logger.info("Output arguments: file [{}] in the directory [{}].", outputFileName, outputDirPath);
        processFiles(inputFileName, outputFileName, inputDirPath, outputDirPath);
        logger.info("Processing completed.");
    }

    public void processFiles(String inputFileName, String outputFileName, String inputDirPath, String outputDirPath) {
        if (isFileWildCard(inputFileName)) {
            List<String> fileList = fileSystem.getFilesList(inputDirPath, inputFileName);
            if (fileList.size() > 0) {
                logger.info("Found {} file(s) by mask [{}] in the input directory [{}].", fileList.size(), WILD_CARD, inputDirPath);
                for(String fileName : fileList) {
                    processFile(fileSystem.joinPathParts(inputDirPath, fileName),
                            fileSystem.joinPathParts(outputDirPath, fileName));
                }
            }
        } else {
            if (outputFileName.isEmpty()) {
                logger.info("Output file name is not specified, use [{}].", inputFileName);
                outputFileName = inputFileName;
            }
            processFile(fileSystem.joinPathParts(inputDirPath, inputFileName),
                    fileSystem.joinPathParts(outputDirPath, outputFileName));
        }
    }

    public void processFile(String inputFilePath, String outputFilePath) {
        logger.info("Process input file [{}] to output file [{}].", inputFilePath, outputFilePath);
        List<HtmlTag> tags = tagConfiguration.getTags();
        logger.info("Applying a set of {} tags.", tags.size());
        CharFileReader reader = new CharFileReader(inputFilePath);
        StringBuilder document = reader.readContent();
        processor.process(document, tags);
        CharFileWriter writer = new CharFileWriter(outputFilePath);
        writer.writeContent(document.toString());
    }

    public boolean isFileWildCard(String name) {
        return WILD_CARD.equals(name);
    }
}
