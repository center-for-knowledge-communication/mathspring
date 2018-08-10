<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="teacherToolsHeader.jsp" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="ttlogo.jsp" />
<!-- ADD A BAR FOR WELCOME TEACHER -->

  <!--
   <style type="text/css">
  a:active {  font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #FFFFFF; text-decoration: underline}
  a:hover {  font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #FFFF00; text-decoration: underline}
  a:link {  font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #FFFFFF; text-decoration: underline}
  a:visited {  font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #FFFFFF; text-decoration: underline}
.style1 {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 18px;
}
  -->

  <div id="Layer1" style="position:absolute; width:400px; height:333px; z-index:1; left: 350px; top: 5px">
    <p align="left" class="a2"><span class="whArial"><font color="#FFFFFF">What would you like to do?</font></span><br>

      <br>

      <span class="a2"><a href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminCreateNewClass&teacherId=<c:out value="${teacherId}"/>">Create a new class</a></span></p>
    <p align="left"><span class="a2"><a href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminAlterClass1&teacherId=<c:out value="${teacherId}"/>">Alter an existing class</a></span> <br>
      <br>
      <span class="a2"><a href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminViewReport&state=chooseClass&teacherId=<c:out value="${teacherId}"/>">View reports about my classes</a></span></p>
    <p>&nbsp;</p>
  </div>



  </body>
</html>