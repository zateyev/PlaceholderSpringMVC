package com.epam.webtest.controller.action;

import com.epam.webtest.dao.DocumentDao;
import com.epam.webtest.dao.JdbcDocumentDao;
import com.epam.webtest.dao.JdbcPackDao;
import com.epam.webtest.dao.PackDao;
import com.epam.webtest.domain.Document;
import com.epam.webtest.domain.Marker;
import com.epam.webtest.domain.Pack;
import com.epam.webtest.domain.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.List;
import java.util.Set;

public class FormGenerateAction implements Action {
    private HttpServletRequest request;
    @Autowired
    private DriverManagerDataSource dataSource;

    public FormGenerateAction(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public ModelAndView execute() {
        Long packID = Long.valueOf(request.getParameter("packid"));
        PackDao jdbcPackDao = new JdbcPackDao(dataSource);
        DocumentDao jdbcDocumentDao = new JdbcDocumentDao(dataSource);
        Pack pack = jdbcPackDao.findById(packID);
        List<Document> documents = jdbcDocumentDao.findByPack(pack);
        pack.setDocuments(documents);
        Marker marker = new Marker();
        Set<Tag> tags = marker.getTags(pack);
        ModelAndView model = new ModelAndView("generated-form");
        model.addObject("tags", tags);
        model.addObject("pack", pack);
        return model;
        }
}
