<%--
  Created by IntelliJ IDEA.
  User: marshall
  Date: 3/19/2018
  Time: 4:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Instructions</title>

</head>
<body>
<img src="${pageContext.request.contextPath}/login/instructionResources/MathspringInstructions.png">
<form method="post" name="login" action="${pageContext.request.contextPath}/WoLoginServlet">
    <input type="hidden" name="action" value="LoginInterventionInput"/>
    <input type="hidden" name="sessionId" value="${sessionId}"/>
    <input type="hidden" name="skin" value="${skin}"/>
    <input type="hidden" name="interventionClass" value="${interventionClass}"/>

    <br><br><input type="submit"/>
</form>
</body>
</html>
