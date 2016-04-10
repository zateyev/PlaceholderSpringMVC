package com.epam.webtest.domain;

import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class Replacer {
    private Map<String, String> map;

    public Replacer(Map<String, String> map) {
        this.map = map;
    }

    public void insertReplacers(Document document) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("app");
        String outpath = resourceBundle.getString("formed.document.location");
        for (XWPFParagraph p : document.getXwpfDocument().getParagraphs()) {
            replace(p, map);
        }
        for (XWPFTable tbl : document.getXwpfDocument().getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        replace(p, map);
                    }
                    for (XWPFTable insTbl : cell.getTables()) {
                        for (XWPFTableRow insRow : insTbl.getRows()) {
                            for (XWPFTableCell insCell : insRow.getTableCells()) {
                                for (XWPFParagraph p : insCell.getParagraphs()) {
                                    replace(p, map);
                                }
                            }
                        }
                    }
                }
            }
        }
        try {
            document.getXwpfDocument().write(new FileOutputStream(outpath + document.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void replace0(XWPFParagraph p, Map<String, String> map) {
        int numberOfRuns = p.getRuns().size();
        // Collate text of all runs
        StringBuilder sb = new StringBuilder();
        for (XWPFRun r : p.getRuns()) {
            int pos = r.getTextPosition();
            if (r.getText(pos) != null) {
                sb.append(r.getText(pos));
            }
        }
        // Continue if there is text and contains "test"
        if (sb.length() > 0) {
            // Remove all existing runs
            for (int i = numberOfRuns; i >= 0; i--) {
                p.removeRun(i);
            }
            String text = sb.toString();
            Set<String> markers = map.keySet();
            for (String marker : markers) {
                text = text.replace(marker, map.get(marker));
            }
            // Add new run with updated text
            XWPFRun run = p.createRun();
            run.setText(text);
            p.addRun(run);
        }
    }

    private void replace(XWPFParagraph p, Map<String, String> replacements) {
        List<XWPFRun> runs = p.getRuns();
        for (Map.Entry<String, String> replPair : replacements.entrySet()) {
            String find = replPair.getKey();
            String repl = replPair.getValue();
            TextSegement found = p.searchText(find, new PositionInParagraph());
            if (found != null) {
                if (found.getBeginRun() == found.getEndRun()) {
                    // whole search string is in one Run
                    XWPFRun run = runs.get(found.getBeginRun());
                    String runText = run.getText(run.getTextPosition());
                    String replaced = runText.replace(find, repl);
                    run.setText(replaced, 0);
                } else {
                    // The search string spans over more than one Run
                    // Put the Strings together
                    StringBuilder b = new StringBuilder();
                    for (int runPos = found.getBeginRun(); runPos <= found.getEndRun(); runPos++) {
                        XWPFRun run = runs.get(runPos);
                        b.append(run.getText(run.getTextPosition()));
                    }
                    String connectedRuns = b.toString();
                    String replaced = connectedRuns.replace(find, repl);
                    // The first Run receives the replaced String of all connected Runs
                    XWPFRun partOne = runs.get(found.getBeginRun());
                    partOne.setText(replaced, 0);
                    // Removing the text in the other Runs.
                    for (int runPos = found.getBeginRun() + 1; runPos <= found.getEndRun(); runPos++) {
                        XWPFRun partNext = runs.get(runPos);
                        partNext.setText("", 0);
                    }
                }
            }
        }
    }

    public void insertReplacers(Pack pack) {
        for (Document document : pack.getDocuments()) {
            insertReplacers(document);
        }
    }
}
