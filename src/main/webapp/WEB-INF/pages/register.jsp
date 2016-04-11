<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="i18n" />

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title><fmt:message key="register" /></title>
  <link rel="stylesheet" href="../../static/style.css">
</head>
<body>
<form action="${pageContext.request.contextPath}/register" method="post" class="form">
  <label><fmt:message key="register" /></label>
  <input name="firstName" type="text" placeholder="Name">
  <input name="email" type="text" placeholder="Your email">
  <input name="password" type="password" placeholder="Create a password">
  <button><fmt:message key="register" /></button>
</form>
</body>
</html>