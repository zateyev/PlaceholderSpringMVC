package com.epam.webtest.controller;

import com.epam.webtest.controller.action.Action;
import com.epam.webtest.controller.action.Actionfactory;
import com.epam.webtest.dao.PackDao;
import com.epam.webtest.dao.UserDao;
import com.epam.webtest.domain.Pack;
import com.epam.webtest.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class HelloController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PackDao packDao;

    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public ModelAndView defaultPage() {
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/admin**", method = RequestMethod.GET)
    public ModelAndView adminPage() {

        ModelAndView model = new ModelAndView();
        model.addObject("title", "Spring Security Login Form - Database Authentication");
        model.addObject("message", "This page is for ROLE_ADMIN only!");
        model.setViewName("admin");
        return model;

    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Invalid username and password!");
        }

        if (logout != null) {
            model.addObject("msg", "You've been logged out successfully.");
        }
        model.setViewName("login");

        return model;

    }

    //for 403 access denied page
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ModelAndView accesssDenied() {

        ModelAndView model = new ModelAndView();

        //check if user is login
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            model.addObject("username", userDetail.getUsername());
        }

        model.setViewName("403");
        return model;

    }

    @RequestMapping(value = "/my-packs", method = RequestMethod.GET)
    public ModelAndView packList(HttpServletRequest request) {
        User user = userDao.findByEmail(request.getUserPrincipal().getName());
        List<Pack> packList = packDao.findByUser(user);
        ModelAndView model = new ModelAndView("my-packs");
        model.addObject("packList", packList);
        return model;
    }

    @RequestMapping(value = "/create-pack", method = RequestMethod.GET)
    public String packUploadPage() {
        return "create-pack";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ModelAndView createPack(HttpServletRequest request) {
        Action action = Actionfactory.getAction(request);
        return action.execute();
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public ModelAndView generateForm(HttpServletRequest request) {
        Action action = Actionfactory.getAction(request);
        return action.execute();
    }
}