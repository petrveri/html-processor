package org.petrveri.util.htmlprocessor.parsing;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Petro Veriienko
 */
public class HtmlTag {
    private String beginWord;
    private String endWord;
    ArrayList<Pair<String, String>> filterAttributes = new ArrayList<>();

    public HtmlTag(String beginWord, String endWord) {
        this.setBeginWord(beginWord);
        this.setEndWord(endWord);
    }

    public HtmlTag(String beginWord, String endWord, String filterAttrName, String filterAttrValue) {
        this.setBeginWord(beginWord);
        this.setEndWord(endWord);
        this.filterAttributes.add(new Pair<> (filterAttrName, filterAttrValue));
    }

    public HtmlTag(String beginWord, String endWord, List<Pair<String, String>> filterAttributes) {
        this.setBeginWord(beginWord);
        this.setEndWord(endWord);
        this.filterAttributes.addAll(filterAttributes);
    }

    public String getBeginWord() {
        return beginWord;
    }

    public void setBeginWord(String beginWord) {
        this.beginWord = beginWord;
    }

    public String getEndWord() {
        return endWord;
    }

    public void setEndWord(String endWord) {
        this.endWord = endWord;
    }

    public ArrayList<Pair<String, String>> getFilterAttributes() {
        return filterAttributes;
    }
}
