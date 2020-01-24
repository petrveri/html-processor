package org.petrveri.util.htmlprocessor.config;

import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.petrveri.util.htmlprocessor.fs.FileSystem;
import org.petrveri.util.htmlprocessor.parsing.HtmlTag;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Petro Veriienko
 */
@Component
public class TagConfiguration {

    @Inject
    private AppConfig appConfig;
    @Inject
    private FileSystem fileSystem;

    public List<HtmlTag> getTags() {
        HtmlTag commentTag = new HtmlTag("<!--", "-->");
        HtmlTag scriptTag = new HtmlTag("<script", "</script>");
        HtmlTag scriptTagU = new HtmlTag("<SCRIPT", "</SCRIPT>");
        HtmlTag noscriptTag = new HtmlTag("<noscript", "</noscript>");
        HtmlTag noscriptTagU = new HtmlTag("<NOSCRIPT", "</NOSCRIPT>");
        HtmlTag iframeTag = new HtmlTag("<iframe", "</iframe>");
        HtmlTag iframeTagU = new HtmlTag("<IFRAME", "</IFRAME>");
        HtmlTag metaTag = new HtmlTag("<meta", ">",
                Arrays.asList(new Pair<>("http-equiv", "Content-Type"), new Pair<>("http-equiv", "X-UA-Compatible")));
        HtmlTag metaTagU = new HtmlTag("<META", ">", metaTag.getFilterAttributes());
        HtmlTag linkTag = new HtmlTag("<link", ">", "type", "text/css");
        HtmlTag linkTagU = new HtmlTag("<LINK", ">", linkTag.getFilterAttributes());
        ArrayList<HtmlTag> result = new ArrayList<>(Arrays.asList(commentTag, scriptTag, scriptTagU,
                noscriptTag, noscriptTagU, iframeTag, iframeTagU, metaTag, metaTagU, linkTag, linkTagU
        ));
        String customHtmlTags = appConfig.getString(ConfigKeys.CUSTOM_HTML_TAGS, "");
        if (StringUtils.isNotBlank(customHtmlTags)) {
            Arrays.stream(customHtmlTags.split(",")).forEach(s -> {
                String tagBegin = fileSystem.handleNewLines(
                        appConfig.getString(String.format("custom.html.tag.%s.begin", s)));
                String tagEnd = fileSystem.handleNewLines(
                        appConfig.getString(String.format("custom.html.tag.%s.end", s)));
                result.add(new HtmlTag(tagBegin, tagEnd));
            });
        }
        return result;
    }
}
