<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="teacherSideMenu.jsp" />


<jsp:useBean id="bean" scope="request" type="edu.umass.ckc.wo.beans.Classes"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="mainPageMargin">

    <p align="center"><font color="#000000" size="5" face="Arial, Helvetica, sans-serif">Welcome <c:out value= "${bean.classes[1].teacherName}"/>!</font></p>

   <p><font size="3">You are logged in as a teacher to the Mathspring class management tool.  You can create a new class, change some things about your existing classes, or see some reports about your students.

   </font></p>
                </p>
    <p>By clicking Create Class you will easily create a new class that automatically follows the steps below or you may visit these pages using the menu.
        <ul>
            <li>
                Use the Create Class page to enter the information about the class.
            </li>
            <li>
                Use the Content Settings page to choose how math content will be given.
            </li>
            <li>
                Use the Student Roster page to create users for your students (or you may let students self-register when they first login).
            </li>
        </ul>
    </p></td>

    Existing classes can be modified using the menu items under the Alter Existing Classes menu on the left.
    You may change the class you want to see with the pull-down bar above.
    You are currently looking at class <c:out value="${classId}"/>.

    
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