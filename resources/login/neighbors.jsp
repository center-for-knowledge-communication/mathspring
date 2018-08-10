<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 4/13/15
  Time: 3:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="p7ccm04-1col-column1-cnt p7ccm04-content">
    <p>If you are working in a classroom or a lab the MathSpring Mathematics Tutor can better
        help you if you let us know who is sitting next to you.</p>

    <p>&nbsp;</p>

    <div class="set">
        <form method="post" name="login"
              action="${pageContext.request.contextPath}/WoLoginServlet">
        <input type="hidden" name="action" value="LoginInterventionInput"/>
        <input type="hidden" name="sessionId" value="${sessionId}"/>
        <input type="hidden" name="skin" value="${skin}"/>
        <input type="hidden" name="var" value="b"/>
        <input type="hidden" name="interventionClass" value="${interventionClass}"/>

        <c:out value="${message}"/>
        <p>Who is on your left?
            <select name="left">

                <option value="-1">No One</option>
                <c:forEach var="u" items="${students}">
                    <%--<c:out value="${u.id}"/>--%>
                     <%--<br>--%>
                    <option value="${u.id}">${u.uname} : ${u.fname} ${u.lname}</option>
                </c:forEach>
            </select>
            <img src="login/images/Devices-computer-icon.png" width="60" height="32"
                 alt="computer"/>Who is on your right?
            <select name="right">
                <option value="-1">No One</option>
                <c:forEach var="u" items="${students}">
                    <option value="${u.id}">${u.uname} : ${u.fname} ${u.lname}</option>
                </c:forEach>
            </select>
        </p>
        <p>&nbsp;</p>

        <label>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="submit" name="button" id="button" value="Submit"/>
        </label>
        </form>
        <p>&nbsp;              </p>
    </div>
    <p>&nbsp;</p>

    <p>&nbsp;</p>
</div>