<%--@elvariable id="user" type="kz.zateyev.pdformation.entity.User"--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="i18n"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Form to fill template</title>
    <link rel="stylesheet" href="webjars/bootstrap/3.3.2/dist/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="static/jumbotron-narrow.css"/>
</head>
<body style="zoom: 1;">
<c:url value="/j_spring_security_logout" var="logoutUrl"/>
<form action="${logoutUrl}" method="post" id="logoutForm">
    <input type="hidden" name="${_csrf.parameterName}"
           value="${_csrf.token}"/>
</form>
<script>
    function formSubmit() {
        document.getElementById("logoutForm").submit();
    }
</script>
<div class="container">
    <div class="header">
        <ul class="nav nav-pills pull-right">
            <li class="active"><a href="my-packs.jsp"><span class="glyphicon glyphicon-home"></span> Главная</a></li>
            <li><a href="#">${pageContext.request.userPrincipal.name}</a></li>
            <li><a href="create-pack.jsp">Создать пакет</a></li>
            <li>
                <button class="btn btn-default navbar-btn"><a href="javascript:formSubmit()"> Выйти</a></button>
            </li>
        </ul>
        <h1 class="text-muted"><fmt:message key="project.name"/></h1>
    </div>
    <%@include file="form.jspf" %>
    <%@include file="footer.jspf" %>
</div>
</body>
</html>