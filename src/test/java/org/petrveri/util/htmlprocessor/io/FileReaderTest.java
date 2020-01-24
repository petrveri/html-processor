package org.petrveri.util.htmlprocessor.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.petrveri.util.htmlprocessor.parsing.HtmlTag;

/**
 * @author Petro Veriienko
 */
public class FileReaderTest {

    HtmlTag scriptTag = new HtmlTag("<script", "</script>");

    @Test
    void testRead() {
        String fileFullPath = "target/test-classes/org/petrveri/util/htmlprocessor/fs/html.html";
        CharFileReader reader = new CharFileReader(fileFullPath);
        reader.readContent();
    }

    @Test
    void testReadScript() {
        String fileFullPath = "target/test-classes/org/petrveri/util/htmlprocessor/fs/html.html";
        CharFileReader reader = new CharFileReader(fileFullPath);
        StringBuilder content = reader.readContent();
        Assertions.assertTrue(content.indexOf(scriptTag.getBeginWord()) > -1 &&
                content.indexOf(scriptTag.getEndWord()) > -1);
    }

}
