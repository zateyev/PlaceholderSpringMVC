<%--@elvariable id="user" type="kz.zateyev.pdformation.entity.User"--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="i18n" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Formed documents</title>
    <link rel="stylesheet" href="webjars/bootstrap/3.3.2/dist/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="../../static/jumbotron-narrow.css"/>
</head>
<body style="zoom: 1;">
<div class="container">
    <div class="header">
        <ul class="nav nav-pills pull-right">
            <li><a href="my-packs.jsp"><span class="glyphicon glyphicon-home"></span> Главная</a></li>
            <li><a href="#">${user.firstName}</a></li>
            <li><a href="create-pack.jsp">Создать пакет</a></li>
            <li>
                <form method="get" action="${pageContext.request.contextPath}/logout">
                    <button class="btn btn-default navbar-btn">Выйти</button>
                </form>
            </li>
        </ul>
        <h1 class="text-muted"><fmt:message key="project.name" /></h1>
    </div>

    <h3>Сформированные документы</h3>
    <table class="table">
        <tr>
            <th>Документ</th>
            <th></th>
        </tr>
        <tbody>
        <jsp:useBean id="pack" scope="session" type="kz.zateyev.pdformation.entity.Pack"/>
        <c:forEach items="${pack.documents}" var="document">
            <tr>
                <td>${document.name}</td>
                <td><a href="${pageContext.request.contextPath}/download?filename=${document.name}">Скачать</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <%@include file="footer.jspf" %>
</div>
</body>
</html>