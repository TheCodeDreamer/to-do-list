<%@ page import="java.util.Map" %>
<%@ page import="main.Task" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<c:forEach var="entry" items="${requestScope.lists}">
<tr><td>
<c:out value="${entry.key}"/>
</td><td><c:out value="${entry.value}"/></td></tr>
</c:forEach>

