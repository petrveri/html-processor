package org.petrveri.util.htmlprocessor.io;

import org.petrveri.util.htmlprocessor.exceptions.ApplicationException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author Petro Veriienko
 */
public class CharFileReader {
    private String filePath;
    private final int bufferSize = 256 * 1024;

    public CharFileReader(String filePath) {
        this.filePath = filePath;
    }

    public StringBuilder readContent() {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            throw new ApplicationException(e);
        }
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return readContent(reader);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    private StringBuilder readContent(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] chars = new char[bufferSize];
        int charsRead;
        do {
            charsRead = reader.read(chars, 0, bufferSize);
            if (charsRead > -1) {
                sb.append(chars, 0, charsRead);
            }
        } while (charsRead > -1);
        return sb;
    }
}
