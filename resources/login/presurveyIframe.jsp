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
        <p>&nbsp;
        Please fill out the survey below.   When you have completed the survey, click the blue submit button that is inside the survey.   After you have clicked
            that button you will see a message saying your response has been recorded.  At this point you should click the continue button on the very bottom <br><br>
        <p>
        <iframe width="600" height="600" marginheight="0" marginwidth="0" frameborder="0" src="${iframeURL}"></iframe>
        <br><br>
            &nbsp;&nbsp;&nbsp;<input type="submit" name="Continue" value="Continue"/>
        </p>

    </form>
</div>