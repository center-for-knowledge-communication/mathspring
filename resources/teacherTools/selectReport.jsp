<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--
  Created by IntelliJ IDEA.
  User: marshall
  Date: Feb 5, 2010
  Time: 3:51:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<style type="text/css">
a:active {  font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #FFFFFF; text-decoration: underline}
a:hover {  font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #FFFF00; text-decoration: underline}
a:link {  font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #FFFFFF; text-decoration: underline}
a:visited {  font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #FFFFFF; text-decoration: underline}
</style>
<head>
<title>MathSpring: Teacher Tools: Reports</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>



<body bgcolor="#904a4a" text="#000000">
<p><b><font face="Arial, Helvetica, sans-serif" size="7" color="#FFFFFF">Wayang
  Outpost </font></b><i><font face="Arial, Helvetica, sans-serif" size="6" color="#FFFFFF">Teacher
  Tools</font></i></p>
<p>&nbsp;</p>
<p><font face="Arial, Helvetica, sans-serif" size="4" color="#FFFFFF">Select a
  report </font></p>

<table width="781" border="1">
  <tr>
    <td ><font color="#FFFFFF" face="Arial, Helvetica, sans-serif"> Category</font></td>
    <td ><font color="#FFFFFF" face="Arial, Helvetica, sans-serif"> Report</font></td>
    <td ><font color="#FFFFFF" face="Arial, Helvetica, sans-serif"> Explanation</font></td>
  </tr>








<%-- Pre/Post test Reports --%>
  <tr><td colspan="3"><font color="#FFFFFF" face="Arial, Helvetica, sans-serif">Pre/Post test Reports</font></td>
  </tr>
  <tr>
    <td></td><td bgcolor=#773430 ><font  face="Arial, Helvetica, sans-serif"/><a href="WoAdmin?action=AdminViewReport&teacherId=<c:out value="${teacherId}"/>&classId=<c:out value="${classId}"/>&reportId=10&state=showReport">Pre/Posttest scores per Student</a></td>
    <td><font color="#FFFFFF" face="Arial, Helvetica, sans-serif"/>The scores of each student on a test before and after getting tutoring</td>
  </tr>
  <tr>
    <td></td>
    <td bgcolor=#773430 ><font  face="Arial, Helvetica, sans-serif"/><a href="WoAdmin?action=AdminViewReport&teacherId=<c:out value="${teacherId}"/>&classId=<c:out value="${classId}"/>&reportId=15&state=showReport">Pre/Posttest problem summaries per class</a></td>
    <td><font color="#FFFFFF" face="Arial, Helvetica, sans-serif"/>Individual problems in the pre/posttest reported on the class</td>
  </tr>
  <tr>
    <td></td>
    <td bgcolor=#773430 ><font  face="Arial, Helvetica, sans-serif"/><a href="WoAdmin?action=AdminViewReport&teacherId=<c:out value="${teacherId}"/>&classId=<c:out value="${classId}"/>&reportId=18&state=showReport">Pre/Posttest problem detail per student</a></td>
    <td><font color="#FFFFFF" face="Arial, Helvetica, sans-serif"/>Shows individual pre/post test problem scoring data per student</td>
  </tr>
<%-- Learning Hut Reports --%>
  <tr><td colspan="3"><font color="#FFFFFF" face="Arial, Helvetica, sans-serif">Detailed Problem Solving Reports</font></td>
  </tr>
    <tr> <td></td>
      <td bgcolor=#773430 ><font  face="Arial, Helvetica, sans-serif"/><a href="WoAdmin?action=AdminViewReport&teacherId=<c:out value="${teacherId}"/>&classId=<c:out value="${classId}"/>&reportId=23&state=showReport">Class Summary Per Student Per Topic</a></td>
      <td><font color="#FFFFFF" face="Arial, Helvetica, sans-serif"/>How your students are mastering a variety of math topics</td>
    </tr>
  <tr>  <td></td>
    <td bgcolor=#773430 ><font  face="Arial, Helvetica, sans-serif"/><a href="WoAdmin?action=AdminViewReport&teacherId=<c:out value="${teacherId}"/>&classId=<c:out value="${classId}"/>&reportId=4&state=showReport">Class Summary Per Problem</a></td>
    <td><font color="#FFFFFF" face="Arial, Helvetica, sans-serif"/>Results for problems, for your whole class</td>
  </tr>
  <tr> <td></td>
    <td bgcolor=#773430 ><font  face="Arial, Helvetica, sans-serif"/><a href="WoAdmin?action=AdminViewReport&teacherId=<c:out value="${teacherId}"/>&classId=<c:out value="${classId}"/>&reportId=5&state=showReport">Class Summary Per Student</a></td>
    <td><font color="#FFFFFF" face="Arial, Helvetica, sans-serif"/>Number of problems and hints each student has seen</td>
  </tr>
    <tr> <td></td>
        <td bgcolor=#773430 ><font  face="Arial, Helvetica, sans-serif"/><a href="WoAdmin?action=AdminViewReport&teacherId=<c:out value="${teacherId}"/>&classId=<c:out value="${classId}"/>&reportId=2&state=showReport">Class Summary Per Common Core Cluster</a></td>
        <td><font color="#FFFFFF" face="Arial, Helvetica, sans-serif"/>Results per Common Core Cluster, for your whole class</td>
    </tr>

  <tr> <td></td>
    <td bgcolor=#773430 ><font  face="Arial, Helvetica, sans-serif"/><a href="WoAdmin?action=AdminViewReport&teacherId=<c:out value="${teacherId}"/>&classId=<c:out value="${classId}"/>&reportId=2&state=showReport">Class Summary Per Skill</a></td>
    <td><font color="#FFFFFF" face="Arial, Helvetica, sans-serif"/>Results in fine-grained level math skills, for your whole class</td>
  </tr>
<%-- For Researchers only Reports --%>
  <tr><td colspan="3"><font color="#FFFFFF" face="Arial, Helvetica, sans-serif">For Researchers only</font></td>
  </tr>
  <tr> <td></td>
    <td bgcolor=#773430 ><font  face="Arial, Helvetica, sans-serif"/><a href="WoAdmin?action=AdminViewReport&teacherId=<c:out value="${teacherId}"/>&classId=<c:out value="${classId}"/>&reportId=16&state=showReport">Class Emotion Summary Per Student Per Emotion </a></td>
    <td><font color="#FFFFFF" face="Arial, Helvetica, sans-serif"/>Emotion summary per student per emotion</td>
  </tr>
  <tr> <td></td>
    <td bgcolor=#773430 ><font  face="Arial, Helvetica, sans-serif"/><a href="WoAdmin?action=AdminViewReport&teacherId=<c:out value="${teacherId}"/>&classId=<c:out value="${classId}"/>&reportId=13&state=showReport">Detailed Activity of each student</a></td>
    <td><font color="#FFFFFF" face="Arial, Helvetica, sans-serif"/>What students have worked on at each time step</td>
  </tr>
    <tr> <td></td>
      <td bgcolor=#773430 ><font  face="Arial, Helvetica, sans-serif"/><a href="WoAdmin?action=AdminViewReport&teacherId=<c:out value="${teacherId}"/>&classId=<c:out value="${classId}"/>&reportId=27&state=showReport">Collaboration Activity in Simultaneous Problems</a></td>
      <td><font color="#FFFFFF" face="Arial, Helvetica, sans-serif"/>Students Who Have Collaborated in Simultaneous Problems</td>
    </tr>
  <tr>  <td></td>
    <td bgcolor=#773430 ><font  face="Arial, Helvetica, sans-serif"/><a href="WoAdmin?action=AdminViewReport&teacherId=<c:out value="${teacherId}"/>&classId=<c:out value="${classId}"/>&reportId=14&state=showReport">Start and Stop times of Students with associated sensors</a></td>
    <td><font color="#FFFFFF" face="Arial, Helvetica, sans-serif"/>What students have worked on at each time step</td>
  </tr>
  <tr>  <td></td>
    <td bgcolor=#773430 ><font  face="Arial, Helvetica, sans-serif"/><a href="WoAdmin?action=AdminViewReport&teacherId=<c:out value="${teacherId}"/>&classId=<c:out value="${classId}"/>&reportId=17&state=showReport">Impact of Interventions</a></td>
    <td><font color="#FFFFFF" face="Arial, Helvetica, sans-serif"/>What students have worked on at each time step</td>
  </tr>
  <tr> <td></td>
    <td bgcolor=#773430 ><font  face="Arial, Helvetica, sans-serif"/><a href="WoAdmin?action=AdminViewReport&teacherId=<c:out value="${teacherId}"/>&classId=<c:out value="${classId}"/>&reportId=19&state=showReport">Student activity in learning hut.</a></td>
    <td><font color="#FFFFFF" face="Arial, Helvetica, sans-serif"/>Detailed activity report of learning hut activity- per student</td>
  </tr>
<%-- On the Go Reports --%>
  <tr><td colspan="3"><font color="#FFFFFF" face="Arial, Helvetica, sans-serif">On-the-go Reports</font></td>
  </tr>
 <tr>
    <td></td>
    <td bgcolor=#773430 >
        <font  face="Arial, Helvetica, sans-serif"/>
        <a href="WoAdmin?action=AdminViewReport&teacherId=<c:out value="${teacherId}"/>&classId=<c:out value="${classId}"/>&reportId=12&state=showReport">User names and passwords</a>
    </td>
    <td>
        <font color="#FFFFFF" face="Arial, Helvetica, sans-serif"/> User names and passwords for this class
    </td>
  </tr>
</table>

<p>
<table><tr><td>
    <form name="form1" id="form1" method="post"
          action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminAlterClassEdit&teacherId=<c:out value="${teacherId}"/>&classId=<c:out value="${classId}"/>">
        <input type="submit" name="submit" value="Back to Class Editing" />
        <input type="hidden" name="teacherId"  value="<c:out value="${teacherId}"/>"/>
        <input type="hidden" name="classId"  value="<c:out value="${classId}"/>"/>
    </form>
</td>
    <td>
        <form name="form2" id="form2" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminUpdateClassId">
            <input type="submit" name="submit" value="Choose Another Activity" />
            <input type="hidden" name="teacherId"  value="<c:out value="${teacherId}"/>"/>
            <input type="hidden" name="classId"  value="<c:out value="${classId}"/>"/>
        </form>
    </td>
</tr></table>

<p>&nbsp;</p>
<p><a href="?action=AdminViewReport&teacherId=<c:out value="${teacherId}"/>&state=chooseClass">Return to Class Selection</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <a href="?action=AdminUpdateClassId&teacherId=<c:out value="${teacherId}"/>&classId="<c:out value="${classId}"/>>&nbsp;Return to Activity Selection
  to Activity Selection</a></p>
<p>&nbsp; </p>
</body>
</html>
