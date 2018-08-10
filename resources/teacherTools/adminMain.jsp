<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="${sideMenu}" />


<jsp:useBean id="bean" scope="request" type="edu.umass.ckc.wo.beans.Classes"/>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="mainPageMargin">

    <p align="center"><font color="#000000" size="5" face="Arial, Helvetica, sans-serif">Welcome ${adminName}!</font></p>

   <p><font size="3">You are logged in as an administrator</font></p>
                </p>
    <p>You can select a teacher if you want to work on a particular teacher's classes or you can access all classes.</p>

                </td>


    Choose from the list of activities in the menu panel on the left. 
    You may change the class you want to see with the pull down bar above. 
    You are currently looking at class <c:out value="${classId}"/>. <br><br> You may choose a teacher from the menu below<br> <br>
    <form name="form1" id="f" method="post"
          action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminSetTeacher">
    <select name="teacherId">
        <c:forEach var="teacher" items="${teachers}">
            <c:choose>
                <c:when test="${teacher.id == teacherId}"> <option selected value="${teacher.id}">${teacher.name}</option> </c:when>
                <c:otherwise> <option value="${teacher.id}">${teacher.name}</option> </c:otherwise>
            </c:choose>
        </c:forEach>
    </select>
        <input type="submit" name="submit" value="Set Teacher" />
    </form>
   <br><br>

    
    If you need help at anytime, feel free to consult the teacher tools handbook by clicking on the help icon <img src="images/lighbulb.png" width="100" height="100" alt="help">, in the top right corner of each page.

            <div style="background-color:#8BB42D; opacity:0.6" align="center">
                <p height="50">&nbsp</p>
                <p><font size="3">Any questions or concerns, message us at <a href="mailto:CKCHelp@cs.umass.edu">CKCHelp@cs.umass.edu</a></font></p>
                <p height="50">&nbsp</p>
            </div>
    <p>&nbsp</p>
                <div style="background-color:#3D9AD1; opacity:0.6" align="center">
                    <p height="50">&nbsp</p>
                    <p><font size="3" color=#FFFFFF weight="bold">Stay updated with Wayang and CKC related news through <a href="http://www.facebook.com/WayangMathTutor">Facebook</a> and our <a href="http://wayangoutpost.com"> official website.</a></font></p>
                <p height="50">&nbsp</p>
            </div>





</div>

<jsp:include page="wayangTempTail.jsp" />