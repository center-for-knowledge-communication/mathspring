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

<form method="post" name="login" action="${pageContext.request.contextPath}/WoLoginServlet">
    <input type="hidden" name="var" value="b"/>
    <input type="hidden" name="action" value="LoginInterventionInput"/>
    <input type="hidden" name="sessionId" value="${sessionId}">
    <input type="hidden" name="skin" value="${skin}"/>
    <input type="hidden" name="interventionClass" value="${interventionClass}"/>

    <p>&nbsp;</p>

    <p><b><%= rb.getString("how_confident_question")%>:</b></p>
    <label class="radio-inline">
        <input type="radio" name="confidence" value="1"><%= rb.getString("not_at_all")%>
    </label>
    <label class="radio-inline">
        <input type="radio" name="confidence" value="2"><%= rb.getString("a_little")%>
    </label>
    <label class="radio-inline">
        <input type="radio" name="confidence" value="3"><%= rb.getString("somewhat")%>
    </label>
    <label class="radio-inline">
        <input type="radio" name="confidence" value="4"><%= rb.getString("quite_a_bit")%>
    </label>
    <label class="radio-inline">
        <input type="radio" name="confidence" value="5">C
    </label>
    <br>

    <br>
    <p><b><%= rb.getString("how_interested_question")%>:</b></p>
    <label class="radio-inline">
        <input type="radio" name="interest" value="1"><%= rb.getString("not_at_all")%>
    </label>
    <label class="radio-inline">
        <input type="radio" name="interest" value="2"><%= rb.getString("a_little")%>
    </label>
    <label class="radio-inline">
        <input type="radio" name="interest" value="3"><%= rb.getString("somewhat")%>
    </label>
    <label class="radio-inline">
        <input type="radio" name="interest" value="4"><%= rb.getString("quite_a_bit")%>
    </label>
    <label class="radio-inline">
        <input type="radio" name="interest" value="5"><%= rb.getString("<%= rb.getString("extremely")%>")%>
    </label>
    </br>
    <br>

    <p><b><%= rb.getString("how_excited_question")%>:</b></p>
    <label class="radio-inline">
        <input type="radio" name="excitement" value="1"><%= rb.getString("not_at_all")%>
    </label>
    <label class="radio-inline">
        <input type="radio" name="excitement" value="2"><%= rb.getString("a_little")%>
    </label>
    <label class="radio-inline">
        <input type="radio" name="excitement" value="3"><%= rb.getString("somewhat")%>
    </label>
    <label class="radio-inline">
        <input type="radio" name="excitement" value="4"><%= rb.getString("quite_a_bit")%>
    </label>
    <label class="radio-inline">
        <input type="radio" name="excitement" value="5"><%= rb.getString("extremely")%>
    </label>
    </br>


    <br>
    <p><b><%= rb.getString("how_frutstrated_question")%>:</b></p>
    <label class="radio-inline">
        <input type="radio" name="frustration" value="1"><%= rb.getString("not_at_all")%>
    </label>
    <label class="radio-inline">
        <input type="radio" name="frustration" value="2"><%= rb.getString("a_little")%>
    </label>
    <label class="radio-inline">
        <input type="radio" name="frustration" value="3"><%= rb.getString("somewhat")%>
    </label>
    <label class="radio-inline">
        <input type="radio" name="frustration" value="4"><%= rb.getString("quite_a_bit")%>
    </label>
    <label class="radio-inline">
        <input type="radio" name="frustration" value="5"><%= rb.getString("extremely")%>
    </label>
    </br>

    <input class="btn mathspring-btn" type="submit"  value="<%= rb.getString("submit")%>" />
    </p>
</form>
