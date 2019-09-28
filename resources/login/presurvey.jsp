<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 4/14/15
  Time: 3:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>
<% 

Locale loc = request.getLocale();
String lang = loc.getDisplayLanguage();

ResourceBundle rb = null;
try {
	rb = ResourceBundle.getBundle("MathSpring",loc);
}
catch (Exception e) {
//	logger.error(e.getMessage());
}
%>


<div>
    <%--<form method="post" name="login"--%>
          <%--action="${pageContext.request.contextPath}/WoLoginServlet">--%>
        <%--<input type="hidden" name="skin" value="${skin}"/>--%>
        <%--<input type="hidden" name="sessionId" value="${sessionId}"/>--%>
        <%--<input type="hidden" name="action" value="LoginInterventionInput"/>--%>
        <%--<input type="hidden" name="interventionClass" value="${interventionClass}"/>--%>
        <p>&nbsp;<%= rb.getString("presurvey_instructions")%>
  <br><br>
        &nbsp;&nbsp;&nbsp;
            <input id="surveyButton" style="width: 10em; background-color: #51791e; font-size: 150%; font-weight: bold;" type="button" name="Start Survey" value="<%= rb.getString("start_survey")%>"/>
        </p>

    <%--</form>--%>
</div>