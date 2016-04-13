package com.epam.webtest.controller.action;

import javax.servlet.http.HttpServletRequest;

public class Actionfactory {
    public static Action getAction(HttpServletRequest request) {
        String method = request.getMethod();
        String action = request.getRequestURI().substring(request.getContextPath().length());
        switch (method + action) {
            case "GET/form":
                return new FormGenerateAction(request);
            default:
                throw new ActionException("There isn't required action");
        }
    }
}
