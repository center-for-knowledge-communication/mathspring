<%--
  Created by IntelliJ IDEA.
  User: marshall
  Date: Sept 8, 2011
  Time: 1:05:03 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--<jsp:include page="teacherToolsHeader.jsp" />  --%>
<jsp:include page="${sideMenu}" />
<jsp:useBean id="bean" scope="request" type="edu.umass.ckc.wo.beans.Classes"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="classInfo" scope="request" type="edu.umass.ckc.wo.beans.ClassInfo"/>


<div class="mainPageMargin">
<div id="Layer1" align="center">
  <p class="a2"><b><font face="Arial, Helvetica, sans-serif">Other Class Configurations</font></b></p>
    <form name="form1" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminAlterClassOtherConfigSubmitInfo">
    <font  face="Arial, Helvetica, sans-serif">
        Set how often you'd like emails sent.   If you do not wish to receive email, set the days between emails to 0.  If you do not wish emails sent to students set days between emails to 0.
    </font>
    <br>
    <table width="700" border="0" height="98">

      <tr>
        <td width="300"><font color="#000000" face="Arial, Helvetica, sans-serif">Days between emails to teachers:</font></td>
        <td width="50" class="whArial"><input name="teacherEmailInterval" value="<c:out value="${classInfo.emailInterval}"/>" type="text"/></td>
        <td width="300"><font color="#00000" face="Arial, Helvetica, sans-serif">Report Period (days):</font></td>
        <td width="50" class="whArial"><input name="teacherReportPeriod" value="<c:out value="${classInfo.statusReportPeriodDays}"/>" type="text"/></td>
      </tr>
        <tr>

        <td width="300"><font color="#00000" face="Arial, Helvetica, sans-serif">Days between emails to students:</font></td>
        <td width="50" class="whArial"><input name="studentEmailInterval" value="<c:out value="${classInfo.studentEmailIntervalDays}"/>" type="text"/></td>
        <td width="300"><font color="#00000" face="Arial, Helvetica, sans-serif">Report Period (days):</font></td>
        <td width="50" class="whArial"><input name="studentReportPeriod" value="<c:out value="${classInfo.studentEmailPeriodDays}"/>" type="text"/></td>

      </tr>



    </table>




      <!-- Fucked up XML  I've got a form closer in the first table cell below.  This closes the form
      with all the fields + the save changes button.  I've then got a form in cell 2 and a form
      in cell 3.  It shouldn't be valid XHTML to have a </form> inside a <td> unless there is also
      a <form in there but the browser (FF) executes this correctly.  Others might not.
      -->
      <input type="submit" name="saveChanges" value="Save Changes" />
      <input type="hidden" name="classId" value="<c:out value="${classInfo.classid}"/>"/>
      <input type="hidden" name="teacherId" value="<c:out value="${classInfo.teachid}"/>"/>
  </form>



  <p/>
      <font color="#00000" face="Arial, Helvetica, sans-serif"></font>
    <p/>

    <font color="#00000" face="Arial, Helvetica, sans-serif"><c:out value="${message}"/></font>


</div>
</div>

<jsp:include page="wayangTempTail.jsp" />