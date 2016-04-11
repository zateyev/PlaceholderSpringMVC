package com.epam.webtest.controller.action;

import com.epam.webtest.dao.*;
import com.epam.webtest.domain.Document;
import com.epam.webtest.domain.Pack;
import com.epam.webtest.domain.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class UploadAction implements Action {
    private HttpServletRequest request;

    @Autowired
    private DriverManagerDataSource dataSource;

    public UploadAction(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public ModelAndView execute() {
        PackDao jdbcPackDao = new JdbcPackDao(dataSource);
        UserDao jdbcUserDao = new JdbcUserDao(dataSource);
        DocumentDao jdbcDocumentDao = new JdbcDocumentDao(dataSource);
        String value = null;
        ResourceBundle resourceBundle = ResourceBundle.getBundle("app");
        String filepath = resourceBundle.getString("upload.location");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        String email = userDetail.getUsername();
        User user = jdbcUserDao.findByEmail(email);
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List items = null;
            try {
                items = upload.parseRequest(request);
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
            Iterator itr = items.iterator();
            Pack initPack = new Pack();
            initPack.setUser(user);
            initPack.setLocation(filepath);
            Pack pack = jdbcPackDao.insert(initPack);
            while (itr.hasNext()) {
                FileItem item = (FileItem) itr.next();
                if (item.isFormField()) {
                    value = item.getString();
                } else {
                    try {
                        String itemName = item.getName();
                        File uploadedFile = new File(filepath + itemName);
                        item.write(uploadedFile);
                        XWPFDocument docx = new XWPFDocument(new FileInputStream(filepath + itemName));
                        Document document = new Document(docx, itemName);
                        document.setPack(pack);
                        jdbcDocumentDao.insert(document);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            pack.setName(value);
            jdbcPackDao.update(pack);
        }
        List<Pack> packList = jdbcPackDao.findByUser(user);
        ModelAndView model = new ModelAndView("my-packs");
        model.addObject("packList", packList);
        return model;
    }
}
