package org.petrveri.util.htmlprocessor.parsing;

import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.petrveri.util.htmlprocessor.fs.FileSystem;
import org.petrveri.util.htmlprocessor.io.CharFileReader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Petro Veriienko
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ProcessorTestConfig.class}, loader = AnnotationConfigContextLoader.class)
public class ProcessorTest {

    @Inject
    private FileSystem fileSystem;
    @Inject
    private Processor processor;

    HtmlTag scriptTag = new HtmlTag("<script", "</script>");
    HtmlTag metaTag = new HtmlTag("<meta", ">",
            Arrays.asList(new Pair<>("http-equiv", "Content-Type"), new Pair<>("http-equiv", "X-UA-Compatible")));

    @Test
    void testParseFileExcludeScriptTag() {
        String inputFilePath = "target/test-classes/org/petrveri/util/htmlprocessor/fs/html.html";
        String resultContent = processFile(inputFilePath, Collections.singletonList(scriptTag)).toString();
        Assertions.assertFalse(resultContent.contains("<script "));
        Assertions.assertFalse(resultContent.contains("</script>"));
    }

    @Test
    void testParseFileLeaveMetaAttributedTag() {
        String inputFilePath = "target/test-classes/org/petrveri/util/htmlprocessor/fs/html.html";
        String resultContent = processFile(inputFilePath, Collections.singletonList(metaTag)).toString();
        Assertions.assertTrue(resultContent.contains("<meta http-equiv=\"Content-Type\""));
        Assertions.assertTrue(resultContent.contains("meta http-equiv=\"X-UA-Compatible\""));
        Assertions.assertFalse(resultContent.contains("<meta name=\"viewport\""));
    }

    @Test
    void testParseFileCrossTag() {
        String inputFilePath = "target/test-classes/org/petrveri/util/htmlprocessor/fs/html.html";
        HtmlTag outerMediumDivBeginFragmentTag = new HtmlTag("<div class=\"outerDiv\">", "<div class=\"mediumDiv\">");
        String outerMediumDivEndFragmentTagBegin = fileSystem.handleNewLines("</div>\n    </div");
        HtmlTag outerMediumDivEndFragmentTag = new HtmlTag(outerMediumDivEndFragmentTagBegin, ">");
        String resultContent = processFile(inputFilePath,
                Arrays.asList(outerMediumDivBeginFragmentTag, outerMediumDivEndFragmentTag)).toString();
        Assertions.assertTrue(resultContent.contains("<div class=\"innerDiv\">"));
        Assertions.assertTrue(resultContent.contains(fileSystem.handleNewLines(
                "<span class=\"in_inner_div_span\" />\n        </div>")));
        Assertions.assertFalse(resultContent.contains("<div class=\"outerDiv\">"));
        Assertions.assertFalse(resultContent.contains("<div class=\"mediumDiv\">"));
        Assertions.assertFalse(resultContent.contains(outerMediumDivEndFragmentTagBegin));
    }

    @Test
    void testParseFileNoTag() {
        String inputFilePath = "target/test-classes/org/petrveri/util/htmlprocessor/fs/html.html";
        CharFileReader reader = new CharFileReader(inputFilePath);
        StringBuilder initialContent = reader.readContent();
        StringBuilder resultContent = processFile(inputFilePath, Collections.emptyList());
        Assertions.assertEquals(initialContent.toString(), resultContent.toString());
    }

    private StringBuilder processFile(String inputFilePath, List<HtmlTag> tags) {
        CharFileReader reader = new CharFileReader(inputFilePath);
        StringBuilder document = new StringBuilder(reader.readContent());
        processor.process(document, tags);
        return document;
    }
}
