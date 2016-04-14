package com.epam.webtest.domain;

import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Marker {

    public Set<Tag> getTags(Document document) {
        Set<Tag> tags = new HashSet<Tag>();
        String regex = "(\\{\\w*?\\})";
        StringBuilder sb = new StringBuilder();
        for (XWPFParagraph p : document.getXwpfDocument().getParagraphs()) {
            sb.append(getText(p));
        }
        for (XWPFTable tbl : document.getXwpfDocument().getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {

                    for (XWPFParagraph p : cell.getParagraphs()) {
                        sb.append(getText(p));
                    }

                    for (XWPFTable insTbl : cell.getTables()) {
                        for (XWPFTableRow insRow : insTbl.getRows()) {
                            for (XWPFTableCell insCell : insRow.getTableCells()) {
                                for (XWPFParagraph p : insCell.getParagraphs()) {
                                    sb.append(getText(p));
                                }
                            }
                        }
                    }
                }
            }
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sb.toString());
        while (matcher.find()) {
            Tag tag = new Tag(matcher.group().substring(1, matcher.group().length() - 1));
            tags.add(tag);
        }
        return tags;
    }

    private String getText(XWPFParagraph p) {
        StringBuilder sb = new StringBuilder();
        for (XWPFRun r : p.getRuns()) {
            int pos = r.getTextPosition();
            if (r.getText(pos) != null) {
                sb.append(r.getText(pos));
            }
        }
        return sb.toString();
    }

    public Set<Tag> getTags(Pack pack) {
        Set<Tag> tags = new HashSet<Tag>();
        pack.getDocuments().forEach(document -> tags.addAll(getTags(document)));
        /*for (Document document : pack.getDocuments()) {
            tags.addAll(getTags(document));
        }*/
        return tags;
    }
}
