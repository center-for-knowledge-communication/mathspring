<%--
  User: kartik
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div>
    <form method="post" name="login"
          action="${pageContext.request.contextPath}/WoLoginServlet">
        <input type="hidden" name="var" value="b"/>
        <input type="hidden" name="skin" value="${skin}"/>
        <input type="hidden" name="sessionId" value="${sessionId}"/>
        <input type="hidden" name="action" value="LoginInterventionInput"/>
        <input type="hidden" name="interventionClass" value="${interventionClass}"/>
        <p><%= rb.getString("want_to_work_with")%></p>
		<c:forEach items="${lcprofile}" var="lcompanion" varStatus="loop">
			<c:if test="${(loop.index == 0 || loop.index%3  == 0)}">
				<c:set var="terminator" value="${loop.index + 2}"/>
			<div class="row">	
			</c:if>
			<div class="col-md-4">
			    
       			<label class="radio-inline">
          			<input type="radio" name="optLC" id="LC" value="${lcompanion.key}">
          			<img src="https://lcharacter.s3.amazonaws.com/${lcompanion.value}.png" width="180px" height="226px">
          			<span style="display:block; text-align: center;">${lcompanion.value}</span>
         		</label>
       		</div>
		    <c:if test="${loop.index == terminator}">
		    </div>
		    </c:if> 
		    <c:if test="${(loop.last == 'true' && loop.index != terminator)}">
		    </div>
		    </c:if>
		</c:forEach>
		
		<div class="row">
			<input class="btn btn-success" type="submit" value="Submit" style="
			    display: block;margin: auto;margin-top: 20px;">
		</div>
    </form>
</div>
     	
