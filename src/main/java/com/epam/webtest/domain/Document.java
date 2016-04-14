package com.epam.webtest.domain;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Document extends BaseEntity {
    private XWPFDocument xwpfDocument;
    private String name;
    Pack pack;

    public Document(String name, Pack pack) throws IOException {
        this.name = name;
        this.pack = pack;
        this.xwpfDocument = new XWPFDocument(new FileInputStream(pack.getLocation() + name));
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
