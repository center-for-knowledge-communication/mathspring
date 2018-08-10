<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 4/14/15
  Time: 3:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div>
    <form method="post" name="login"
          action="${pageContext.request.contextPath}/WoLoginServlet">
        <input type="hidden" name="skin" value="${skin}"/>
        <input type="hidden" name="sessionId" value="${sessionId}"/>
        <input type="hidden" name="action" value="LoginInterventionInput"/>
        <input type="hidden" name="interventionClass" value="${interventionClass}"/>
        <p>&nbsp;</p>
        <p>First Name:
            <input type="text" name="fname" />
            Last Initial:
            <input type="text" name="lini" />
            <br>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input  type="submit"  value="Submit" />


        </p>
    </form>
</div>