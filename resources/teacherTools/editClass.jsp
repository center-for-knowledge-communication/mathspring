<%--
  Created by IntelliJ IDEA.
  User: marshall
  Date: Jan 30, 2008
  Time: 1:05:03 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="${sideMenu}" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="classInfo" scope="request" type="edu.umass.ckc.wo.beans.ClassInfo"/>
<jsp:useBean id="pool" scope="request" type="edu.umass.ckc.wo.beans.PretestPool"/>

<div class="mainPageMargin">
<div id="Layer1" align="center">
  <p align="center" class="a2"><font color="#000000"><b><font face="Arial, Helvetica, sans-serif">Editable Class information</font></b></font></p>
    <form name="form1" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminAlterClassSubmitInfo">
    <table width="334" border="0" height="98">
       <tr>
        <td width="102"><font color="#000000" face="Arial, Helvetica, sans-serif">ID:</font></td>
        <td width="222" class="whArial"><c:out value="${classInfo.classid}"/></td>
      </tr>
      <tr>
        <td width="102"><font color="#000000" face="Arial, Helvetica, sans-serif">Class Name:</font></td>
        <td width="222" class="whArial"><input name="className" type="text" value="<c:out value="${classInfo.name}"/>"/></td>
      </tr>
      <tr>
        <td width="102"><font color="#000000" face="Arial, Helvetica, sans-serif">Town:</font></td>
        <td width="222" class="whArial"><input name="town" type="text" value="<c:out value="${classInfo.town}"/>"/></td>
      </tr>
      <tr>
        <td width="102"><font color="#000000" face="Arial, Helvetica, sans-serif">School:</font></td>
        <td width="222" class="whArial"><input name="school" type="text" value="<c:out value="${classInfo.school}"/>"/></td>
      </tr>

      <tr>
        <td width="102"><font color="#000000" face="Arial, Helvetica, sans-serif">Year:</font></td>
        <td width="222" class="whArial"><input name="schoolYear" type="text" value="<c:out value="${classInfo.schoolYear}"/>"/></td>
      </tr>
      <tr>
        <td width="102"><font color="#000000" face="Arial, Helvetica, sans-serif">Section:</font></td>
        <td width="222" class="whArial"><input name="section" type="text" value="<c:out value="${classInfo.section}"/>"/></td>
      </tr>
        <tr>
            <td width="102"><font color="#000000" face="Arial, Helvetica, sans-serif">* Grade:</font></td>

            <td width="222">
                <select name="grade">
                    <option <c:if test="${classInfo.grade == '5'}">selected</c:if> value="5">5</option>
                    <option <c:if test="${classInfo.grade == '6'}">selected</c:if> value="6">6</option>
                    <option <c:if test="${classInfo.grade == '7'}">selected</c:if> value="7">7</option>
                    <option <c:if test="${classInfo.grade == '8'}">selected</c:if> value="8">8</option>
                    <option <c:if test="${classInfo.grade == '9'}">selected</c:if> value="9">9</option>
                    <option <c:if test="${classInfo.grade == '10'}">selected</c:if> value="10">10</option>
                    <option <c:if test="${classInfo.grade == 'adult'}">selected</c:if> value="adult">adult</option>
                </select>
            </td>
        </tr>
	  <%--<tr>--%>
        <%--<td width="102"><font color="#000000" face="Arial, Helvetica, sans-serif">Prop Group*:</font></td>--%>
        <%--<td width="222" class="whArial"><input name="propGroupId" type="text" value="<c:out value="${classInfo.propGroupId}"/>"/></td>--%>
      <%--</tr>--%>
        <%--<tr>--%>
        <%--<td width="102"><font color="#000000" face="Arial, Helvetica, sans-serif">Pretest Pool:</font></td>--%>
        <%--<td width="222" class="whArial"><c:out value="${pool.description}"/></td>--%>
      <%--</tr>--%>
    </table>
        <p class="whArial">
      * If you change the grade of your class, it will be necessary to use Simple Tutor Configuration to select content for it.
      <br /><br />
          <input type="submit" name="saveChanges" value="Save Changes" />
      <input type="hidden" name="classId"  value="<c:out value="${classInfo.classid}"/>"/>
        <input type="hidden" name="teacherId"  value="<c:out value="${classInfo.teachid}"/>"/>
  </form>
     <p/>

    <font color="#000000" face="Arial, Helvetica, sans-serif"><c:out value="${message}"/></font>
  </div>



<div id="Layer2" align="center" >
<p class="whArial">Pedagogies in use: </p>
<table width="90%"  border="0">
        <tr>
            <td width="17%" class="whArial">ID</td>
            <td width="83%" class="whArial">Name</td>
        </tr>
        <%--@elvariable id="pedagogies" type="java.util.List"--%>
        <%--@elvariable id="ped" type="edu.umass.ckc.wo.tutor.Pedagogy"--%>
        <c:forEach var="ped" items="${pedagogies}">
         <tr>
            <td class="whArial">
            <c:out value="${ped.id}"/></td>
            <td class="whArial"><c:out value="${ped.name}"/> </td>
        </tr>
        </c:forEach>
    </table>
    <p class="whArial">

</div>
</div>



</body>
</html>