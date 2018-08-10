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

<div class="mainPageMargin">
<div id="Layer1" align="center">
  <p align="center" class="a2"><font color="#000000"><b><font face="Arial, Helvetica, sans-serif">Simple Tutor Configuration</font></b></font></p>
    <form name="form1" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=${formSubmissionEvent}">
    <table width="700" border="0" height="98">
       <tr>
        <td width="300"><font color="#000000" face="Arial, Helvetica, sans-serif">Use Learning Companions?</font></td>
           <td width="250">
               <select name="learningCompanion">
                   <option <c:if test="${classInfo.simpleLC == 'none'}">selected</c:if> value="none">No</option>
                   <option <c:if test="${classInfo.simpleLC == 'male'}">selected</c:if> value="male">Male Companion</option>
                   <option <c:if test="${classInfo.simpleLC == 'female'}">selected</c:if> value="female">Female Companion</option>
                   <option <c:if test="${classInfo.simpleLC == 'both'}">selected</c:if> value="both">Both Companions</option>
               </select>
           </td>
      </tr>
      <tr>
        <td width="300"><font color="#000000" face="Arial, Helvetica, sans-serif">Student Collaboration?</font></td>
          <td width="250">
              <select name="collab">
                  <option <c:if test="${classInfo.simpleCollab == 'none'}">selected</c:if> value="none">None</option>
                  <option <c:if test="${classInfo.simpleCollab == 'some'}">selected</c:if> value="some">Some</option>
                  <option <c:if test="${classInfo.simpleCollab == 'alot'}">selected</c:if> value="alot">A Lot</option>
              </select>
          </td>
      </tr>
      <tr>
        <td width="300"><font color="#000000" face="Arial, Helvetica, sans-serif">Grade level of problems:</font></td>
          <td width="250">
              <select name="lowEndDiff">
                  <option <c:if test="${classInfo.simpleLowDiff == 'below3'}">selected</c:if> value="below3">Down to 3 grades below level</option>
                  <option <c:if test="${classInfo.simpleLowDiff == 'below2'}">selected</c:if> value="below2">Down to 2 grades below level</option>
                  <option <c:if test="${classInfo.simpleLowDiff == 'below1'}">selected</c:if> value="below1">Down to 1 grade below level</option>
                  <option <c:if test="${classInfo.simpleLowDiff == 'below0'}">selected</c:if> value="below0">nothing below grade level</option>
              </select>
          </td>
          <td width="10"></td>
          <td width="250">
              <select name="highEndDiff">
                  <option <c:if test="${classInfo.simpleHighDiff == 'above1'}">selected</c:if> value="above1">Up to 1 grade above level</option>
                  <option <c:if test="${classInfo.simpleHighDiff == 'above2'}">selected</c:if> value="above2">Up to 2 grades above level</option>
                  <option <c:if test="${classInfo.simpleHighDiff == 'above3'}">selected</c:if> value="above3">Up to 3 grades above level</option>
                  <option <c:if test="${classInfo.simpleHighDiff == 'above0'}">selected</c:if> value="above0">nothing above grade level</option>
              </select>
          </td>
      </tr>
      <tr>
        <td width="300"><font color="#000000" face="Arial, Helvetica, sans-serif">Rate at which problems get harder:</font></td>
          <td width="250">
              <select name="diffRate">
                  <option <c:if test="${classInfo.simpleDiffRate == 'gentle'}">selected</c:if> value="gentle">Gentle</option>
                  <option <c:if test="${classInfo.simpleDiffRate == 'normal'}">selected</c:if> value="normal">Normal</option>
                  <option <c:if test="${classInfo.simpleDiffRate == 'agressive'}">selected</c:if> value="agressive">Aggressive</option>
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

      <br /><br />
          <input type="submit" name="saveChanges" value="Save Changes" />
      <input type="hidden" name="classId"  value="<c:out value="${classInfo.classid}"/>"/>
      <input type="hidden" name="teacherId"  value="<c:out value="${classInfo.teachid}"/>"/>
      <c:if test="${createClassSeq}"> <input type="hidden" name="createClassSeq" value="true"/></c:if>

    </form>
     <p/>

    <font color="#000000" face="Arial, Helvetica, sans-serif"><c:out value="${message}"/></font>
  </div>




</div>



</body>
</html>