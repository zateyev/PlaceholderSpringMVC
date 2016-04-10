package com.epam.webtest.domain;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;

public class Document extends BaseEntity {
    private XWPFDocument xwpfDocument;
    private String name;
    Pack pack;

    public Document() {
    }

    public Document(XWPFDocument xwpfDocument, String name) {
        this.xwpfDocument = xwpfDocument;
        this.name = name;
    }

    public XWPFDocument getXwpfDocument() {
        return xwpfDocument;
    }

    public void setXwpfDocument(XWPFDocument xwpfDocument) {
        this.xwpfDocument = xwpfDocument;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pack getPack() {
        return pack;
    }

    public void setPack(Pack pack) {
        this.pack = pack;
    }

    public boolean delete() {
        File file = new File(this.getPack().getLocation() + this.getName());
        return file.delete();
    }
}
