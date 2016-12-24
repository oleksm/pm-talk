<%--
  Created by IntelliJ IDEA.
  User: alexm
  Date: 12/9/16
  Time: 7:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>PM Talk - Created!</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/main.css">
</head>
<body>
<div class="container">

    <div class="alert alert-success" role="alert">
        Thank you for submitting PM Talk request!
    </div>


    <div class="panel panel-default">
        <div class="panel-heading">Please share following links with candidate ${order.candidate}:</div>
        <div class="panel-body">
            <p>
            <h5>Hangouts Meeting:</h5> <a href="${order.meetingLink}">${order.meetingLink}</a></p>
            <p>
            <h5>Coding Doc:</h5> <a href="${order.codingLink}">${order.codingLink}</a></p>
        </div>
    </div>
</div>
</body>
<jsp:include page="analytics-script.jsp"/>
</html>
