package org.petrveri.util.htmlprocessor.fs;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.petrveri.util.htmlprocessor.config.AppConfig;
import org.petrveri.util.htmlprocessor.config.ConfigKeys;
import org.petrveri.util.htmlprocessor.config.DefaultConfig;
import org.petrveri.util.htmlprocessor.exceptions.ApplicationException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Petro Veriienko
 */
@Service
public class FileSystem {

    @Inject
    private AppConfig appConfig;

    public List<String> getFilesList(String folderPath, String wildCard) {
        ArrayList<String> fileList = new ArrayList<>();
        File[] files = new File(folderPath).listFiles((FileFilter) new WildcardFileFilter(wildCard));
        if (files != null) {
            for (File file : files) {
                fileList.add(file.getName());
            }
        }
        return fileList;
    }

    public void createDirectory(String directoryName) {
        Path path = Paths.get(".", directoryName);
        if (!path.toFile().exists()) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                throw new ApplicationException(e);
            }
        }
    }

    public void delete(String fileOrDirectory) {
        Path path = Paths.get(".", fileOrDirectory);
        if (path.toFile().exists()) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new ApplicationException(e);
            }
        }
    }

    public String getFileName(String pathName) {
        // exclude directory path if provided
        if (StringUtils.isBlank(pathName)) {
            return "";
        }
        if (isExist(pathName) && isDirectory(pathName)) {
            return "";
        }
        int lastIndexOfDelimiter = getLastIndexOfDelimiter(pathName);
        if (lastIndexOfDelimiter > -1) {
            return pathName.substring(lastIndexOfDelimiter + 1);
        }
        return pathName;
    }

    public String getDirectoryPath(String pathName) {
        // exclude file name if provided
        String defaultInputDirectory = appConfig.getString(ConfigKeys.DEFAULT_INPUT_DIRECTORY_PATH,
                DefaultConfig.DEFAULT_INPUT_DIRECTORY_PATH);
        if (StringUtils.isBlank(pathName)) {
            return "";
        }
        if (isExist(pathName)) {
            if (isDirectory(pathName)) {
                return pathName;
            } else {
                int lastIndexOfDelimiter = getLastIndexOfDelimiter(pathName);
                if (lastIndexOfDelimiter > -1) {
                    return pathName.substring(0, lastIndexOfDelimiter + 1);
                } else {
                    return defaultInputDirectory;
                }
            }
        }
        int lastIndexOfDelimiter = getLastIndexOfDelimiter(pathName);
        if (lastIndexOfDelimiter > -1) {
            return pathName.substring(0, lastIndexOfDelimiter + 1);
        }
        return pathName;
    }

    private int getLastIndexOfDelimiter(String pathName) {
        int delimIndex = pathName.lastIndexOf('/');
        if (delimIndex == -1) {
            delimIndex = pathName.lastIndexOf('\\');
        }
        return delimIndex;
    }

    public boolean isExist(String pathName) {
        return new File(pathName).exists();
    }

    public boolean isDirectory(String pathName) {
        return new File(pathName).isDirectory();
    }

    public String joinPathParts(String leftPart, String rightPart) {
        char lastChar = leftPart.charAt(leftPart.length() - 1);
        String format;
        if (lastChar == '/' || lastChar == '\\') {
            format = "%s%s";
        } else {
            format = "%s/%s";
        }
        return String.format(format, leftPart, rightPart);
    }

    public String handleNewLines(String input) {
        String fileLineSeparator = appConfig.getString(ConfigKeys.FILE_LINE_SEPARATOR, System.lineSeparator());
        return input.replaceAll("\n", fileLineSeparator);
    }
}
