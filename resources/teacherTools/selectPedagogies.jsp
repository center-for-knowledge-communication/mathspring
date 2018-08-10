<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="${sideMenu}" />
<jsp:useBean id="bean" scope="request" type="edu.umass.ckc.wo.beans.Classes"/>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="mainPageMargin">
  <div id="Layer1" align="center">
    <p class="a2"><font color="#000000"><b><font face="Arial, Helvetica, sans-serif">Pedagogies Selection</font></b></font></p>
    <c:if test="${message != null}">
         <c:out value="${message}"/>
    </c:if>
      <p size=".5">Choose all the pedagogies that you would like to be available for the class.</p>
    <form name="form1" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=<c:out value="${formSubmissionEvent}"/>">

      <table width="334" border="0" height="98">

          <c:if test="${!useTutoringStrategies}">
            <tr><td width="40"><input type="checkbox" name="0"/><td width="305"><font color="#000000" face="Arial, Helvetica, sans-serif">Default</font></td></tr>
          </c:if>
          <%--@elvariable id="pedagogies" type="edu.umass.ckc.wo.beans.PedagogyBean[]"--%>
          <c:forEach var="pedagogy" items="${pedagogies}">
              <tr><td valign="top" width="40"><input type="checkbox" <c:if test="${pedagogy.selected}">checked="checked"</c:if> name="<c:out value="${pedagogy.id}"/>"/></td>
                  <td width="305"><font color="#000000" face="Arial, Helvetica, sans-serif"><c:out value="${pedagogy.description}"/></font></td></tr>

          </c:forEach>
      </table>
        <p class="a2"><font color="#000000"><b><font face="Arial, Helvetica, sans-serif">Tutoring Strategies</font></b></font></p>
        <input type="checkbox" <c:if test="${useTutoringStrategies}">checked="checked"</c:if> name="useTutoringStrategies"> Use Tutoring Strategies instead of the pedagogies above.
      <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="submit" name="Submit" value="Submit">
        <input type="hidden" name="classId" value="<c:out value="${classId}"/>">
        <input type="hidden" name="teacherId" value="<c:out value="${teacherId}"/>">
        <input type="hidden" name="isSimpleConfig" value="false">
    </form>

      <p style="color: #FFFFFF"><font face="Arial, Helvetica, sans-serif">Select as many pedagogies as you wish. &nbsp;Students in the class will be evenly divided into groups which use the selected pedagogies.</font> 


<p>
      </div>
</div>

<jsp:include page="wayangTempTail.jsp" />
