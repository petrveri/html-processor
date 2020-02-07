package org.petrveri.util.htmlprocessor.parsing;

import javafx.util.Pair;
import org.petrveri.util.htmlprocessor.exceptions.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Petro Veriienko
 */
@Component
public class Processor {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final int wordLength = 5;

    public void process(StringBuilder document, List<HtmlTag> tags) {
        for (HtmlTag tag : tags) {
            process(document, tag);
        }
    }

    private void process(StringBuilder document, HtmlTag tag) {
        logger.info("Process tag [{}...]. Initial document length is {} character(s).",
                tag.getBeginWord().substring(0, Math.min(tag.getBeginWord().length(), wordLength)), document.length());
        int tagBegInd = document.indexOf(tag.getBeginWord());
        while (tagBegInd > -1) {
            int tagEndInd = document.indexOf(tag.getEndWord(), tagBegInd + tag.getBeginWord().length());
            if (tagEndInd == -1) {
                throw new ApplicationException(String.format("end tag not found: |[%s]|,\nline: |[%s]|",
                        tag.getEndWord(), document.toString()));
            }
            if (checkTagToPersist(document.substring(tagBegInd, tagEndInd + tag.getEndWord().length()), tag)) {
                tagBegInd++;
            } else {
                document.delete(tagBegInd, tagEndInd + tag.getEndWord().length());
            }
            tagBegInd = document.indexOf(tag.getBeginWord(), tagBegInd);
        }
        logger.info("Final document length is {} character(s).", document.length());
    }

    private boolean checkTagToPersist(String line, HtmlTag tag) {
        boolean found = false;
        for (Pair<String, String> attr : tag.getFilterAttributes()) {
            found |= checkTagAllowedAttributeNameValue(line, attr.getKey(), attr.getValue());
        }
        return found;
    }

    private boolean checkTagAllowedAttributeNameValue(String line, String attrName, String attrValue) {
        if (attrName != null && attrValue != null && line.contains(attrName)) {
            String attrNameExpr = String.format(" %s=\"", attrName);
            int attrNameExprBeg = line.indexOf(attrNameExpr);
            if (attrNameExprBeg > -1) {
                int attrValueBeg = attrNameExprBeg + attrNameExpr.length();
                int attrValueEnd = line.indexOf("\"", attrValueBeg);
                if (attrValueEnd > -1) {
                    String actualAttrValue = line.substring(attrValueBeg, attrValueEnd);
                    return attrValue.equals(actualAttrValue);
                }
            }
        }
        return false;
    }

}
