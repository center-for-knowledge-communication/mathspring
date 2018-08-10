<%--
  Created by IntelliJ IDEA.
  User: marshall
  Date: 3/15/18
  Time: 10:13 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>


<form method="post" name="login" action="${pageContext.request.contextPath}/WoLoginServlet"><span style='font-size:11.0pt'>
        <input type="hidden" name="action" value="LoginInterventionInput"/>
        <input type="hidden" name="sessionId" value="${sessionId}"/>
        <input type="hidden" name="skin" value="${skin}"/>
        <input type="hidden" name="interventionClass" value="${interventionClass}"/>


    <%--<input name="assent" value="yes" checked="true" type="radio">Yes I agree to participate in this study</input>--%>
    <%--<input name="assent" value="no" type="radio">No I do not agree to participate in this study</input>--%>
    <input type="submit"/>
</form>
</body>
</html>
