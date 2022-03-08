<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Access Denied...</title>
<link href="${path}/resources/assets/css/error/custom403.css" rel="stylesheet">
</head>
<body>

	<div class="lock"></div>
	<div class="message">
	  <h1>Access to this page is restricted</h1>
	  <p>Please check with the site admin if you believe this is a mistake.</p>
	  <div id="error_div"><a id="errorLink" href="/">Go Back</a></div>
	</div>

</body>
</html>