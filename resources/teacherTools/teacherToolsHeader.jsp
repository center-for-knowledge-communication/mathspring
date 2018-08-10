<%--
  Created by IntelliJ IDEA.
  User: marshall
  Date: Jan 28, 2008
  Time: 4:07:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
  <link href="js/jquery-ui-1.10.4.custom/css/spring/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
  <script src="js/jquery-ui-1.10.4.custom/js/jquery-ui-1.10.4.custom.min.js"></script>

  <script src="js/jquery-ui-1.10.4.custom/js/jquery-ui-1.10.4.custom.min.js"></script>
  <style type="text/css">

<!--
.whArial {font-family: Arial, Helvetica, sans-serif; font-size: 18; color: #FFFFFF; }
-->

.a2 A:link {text-decoration: underline; color: black; font-family: Arial, Helvetica, sans-serif;}
.a2 A:visited {text-decoration: underline; color: black; font-family: Arial, Helvetica, sans-serif;}
.a2 A:active {text-decoration: none} 
.a2 A:hover {text-decoration: underline; color: blue;}
  </style>
<title>Mathspring: Teacher Tools</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="JavaScript">
<!--
function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);
// -->


</script>
<script language="JavaScript" src="slider.js"></script>
</head>


<c:choose>
<c:when test="${isAdmin}">
<body bgcolor="#CECEFF" text="#FFFFFF">
</c:when>
<c:otherwise>
<body bgcolor="#FFF0A3" text="#FFFFFF">
<img alt="none" src='<c:out value="${pageContext.request.contextPath}"/>/images/EDcover-design.png'/>
</c:otherwise>
</c:choose>




