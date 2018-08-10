<%--
  Created by IntelliJ IDEA.
  User: marshall
  Date: Sept 8, 2011
  Time: 1:05:03 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--<jsp:include page="teacherToolsHeader.jsp" />     --%>
<jsp:include page="${sideMenu}" />


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="classInfo" scope="request" type="edu.umass.ckc.wo.beans.ClassInfo"/>
<jsp:useBean id="pool" scope="request" type="edu.umass.ckc.wo.beans.PretestPool"/>
<jsp:useBean id="bean" scope="request" type="edu.umass.ckc.wo.beans.Classes"/>


<div id="Layer1" align="center">
  <p class="a2"><b><font face="Arial, Helvetica, sans-serif">Class Cloning</font></b></p>
    <form name="form1" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminAlterClassCloneSubmitInfo">
    <font face="Arial, Helvetica, sans-serif">
        You are cloning class id:<c:out value="${classInfo.classid}"/> name: <c:out value="${classInfo.name}"/> section: <c:out value="${classInfo.section}"/>
    </font>
    <br>
    You must give the new class a different name and section
    <table width="334" border="0" height="98">

      <tr>
        <td width="102"><font  face="Arial, Helvetica, sans-serif">Class Name:</font></td>
        <td width="222" class="whArial"><input name="className" type="text"/></td>
      </tr>

      <tr>
        <td width="102"><font  face="Arial, Helvetica, sans-serif">Section:</font></td>
        <td width="222" class="whArial"><input name="section" type="text"/></td>
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
      <font face="Arial, Helvetica, sans-serif"></font>
    <p/>

    <font  face="Arial, Helvetica, sans-serif"><c:out value="${message}"/></font>
</div>

<jsp:include page="wayangTempTail.jsp" />