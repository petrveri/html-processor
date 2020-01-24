package org.petrveri.util.htmlprocessor.io;

import org.petrveri.util.htmlprocessor.exceptions.ApplicationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author Petro Veriienko
 */
public class CharFileWriter {

    private String filePath;

    public CharFileWriter(String filePath) {
        this.filePath = filePath;
    }

    public void writeContent(String line) {
        FileOutputStream fileStream;
        try {
            fileStream = new FileOutputStream(new File(filePath));
        } catch (FileNotFoundException e) {
            throw new ApplicationException(e);
        }
        try (OutputStreamWriter writer = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8)) {
            writer.write(line);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }
}
