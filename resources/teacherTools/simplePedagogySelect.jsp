<%--
  Created by IntelliJ IDEA.
  User: edo
  Date: 12/27/12
  Time: 3:42 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="${sideMenu}" />
<jsp:useBean id="bean" scope="request" type="edu.umass.ckc.wo.beans.Classes"/>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="mainPageMargin">
    <div id="Layer1" align="center">
        <p class="a2"><font color="#000000"><b><font face="Arial, Helvetica, sans-serif">Pedagogies Selection</font></b></font></p>

        <p size=".5">The pedagogies below control the behavior of the tutor.   Select the ones you want for members of your class or you may leave the ones selected by default. When you are done, click the submit button so that your selections are recorded</p>
        <form name="form1" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=<c:out value="${formSubmissionEvent}"/>">

            <table width="334" border="0" height="98">

                <%--@elvariable id="pedagogies" type="edu.umass.ckc.wo.beans.PedagogyBean[]"--%>
                <c:forEach var="pedagogy" items="${pedagogies}">
                    <tr><td valign="top" width="40"><input type="checkbox" <c:if test="${pedagogy.selected}">checked="checked"</c:if> name="<c:out value="${pedagogy.id}"/>"/></td>
                        <td width="305"><font color="#000000" face="Arial, Helvetica, sans-serif"><c:out value="${pedagogy.description}"/></font></td></tr>

                </c:forEach>
            </table>
            <c:if test="${usingTutoringStrategies}">
                This class has been configured to use tutoring strategies rather than pedagogies.  Tutoring strategies must be removed with the msadmin tool or by manually changing the database.  When all strategies are removed from the class, you can then set pedagogies.
            </c:if>
            <%-- only show the advanced pedagogy link to admins --%>
            <c:if test="${sideMenu == 'adminSideMenu.jsp'}">
                <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/WoAdmin?action=AdminAlterClassAdvancedPedagogySelection&classId=${classId}&teacherId=${teacherId}">Advanced Pedagogy Selection</a></p>
            </c:if>
            <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="submit" name="Submit" value="Submit">
                <input type="hidden" name="classId" value="<c:out value="${classId}"/>">
                <input type="hidden" name="teacherId" value="<c:out value="${teacherId}"/>">
                <input type="hidden" name="isSimpleConfig" value="true">
            <p>
            <c:if test="${message != null}">
                <c:out value="${message}"/>
            </c:if>
            </p>

        </form>

        <p style="color: #FFFFFF"><font face="Arial, Helvetica, sans-serif">Select as many pedagogies as you wish. &nbsp;Students in the class will be evenly divided into groups which use the selected pedagogies.</font>


        <p>
    </div>
</div>

<jsp:include page="wayangTempTail.jsp" />

</html>