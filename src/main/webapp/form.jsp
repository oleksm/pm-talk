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
  <title>PM Talk</title>
  <link rel="stylesheet" href="css/bootstrap.min.css">
  <link rel="stylesheet" href="css/main.css">
</head>
<body>
<div class="container">

  <c:if test="${not empty order.errors}">
    <div class="alert alert-danger" role="alert">
      <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
      <span class="sr-only">Error:</span>
      ${order.errors}
    </div>
  </c:if>

  <div class="row">
    <div class="col-md-2"></div>
    <div class="col-md-8">
      <form class="form-horizontal" method="post" enctype="multipart/form-data" onsubmit="$('#pm-talk-submit').prop('disabled', true); $('#pm-talk-submit').addClass('disabled');">
        <fieldset>

          <!-- Form Name -->
          <legend>PM Talk Request</legend>

          <!-- Text input-->
          <div class="form-group form-group-lg">
            <label class="col-md-2 control-label" for="candidate">Candidate</label>
            <div class="col-md-8">
              <input id="candidate" name="candidate" type="text" placeholder="" class="form-control input-lg" required="" value="${order.candidate}">
              <span class="help-block">full name</span>
            </div>
          </div>

          <!-- Text input-->
          <div class="form-group form-group-lg">
            <label class="col-md-2 control-label" for="staffing">Position</label>
            <div class="col-md-8">
              <input id="staffing" name="staffing" type="text" placeholder="http://" class="form-control input-lg" required="" value="${order.staffingLink}">
              <span class="help-block">staffing desk link</span>
            </div>
          </div>

          <!-- File Button -->
          <div class="form-group form-group-lg">
            <label class="col-md-2 control-label" for="resume">Resume</label>
            <div class="col-md-6">
              <input id="resume" name="resume" class="input-file" type="file" value="${candidate.resume.submittedFileName}">
            </div>
          </div>

          <!-- Button -->
          <div class="form-group form-group-lg">
            <label class="col-md-2 control-label" for="pm-talk-submit">PM Talk</label>
            <div class="col-md-6">
              <button id="pm-talk-submit" name="pm-talk-submit" value="pm-talk-submit" class="btn btn-primary btn-lg">Submit</button>
            </div>
          </div>
        </fieldset>
      </form>
    </div>
  </div>
</div>
</body>
<script src="js/jquery.min-2.0.3.js"></script>
<jsp:include page="analytics-script.jsp"/>
</html>
