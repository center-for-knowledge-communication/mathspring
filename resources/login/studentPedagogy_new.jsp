<%--
  Author: kartik
  
  Frank 08-03-21 Issues 150 AND 487 class message and worksheet location 
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

		<label for="messageFromTeacher"><%= rb.getString("todays_messages")%>:</label>
		<br>
		<textarea readonly id="messageFromTeacher" name="messageFromTeacher" rows="4" cols="90">
 		</textarea>

    <form method="post" name="login"
          action="${pageContext.request.contextPath}/WoLoginServlet">
        <input type="hidden" name="var" value="b"/>
        <input type="hidden" name="skin" value="${skin}"/>
        <input type="hidden" name="sessionId" value="${sessionId}"/>
        <input type="hidden" name="action" value="LoginInterventionInput"/>
        <input type="hidden" name="interventionClass" value="${interventionClass}"/>
 
         <h4><%= rb.getString("want_to_work_with")%></h4>
		<c:forEach items="${lcprofile}" var="lcompanion" varStatus="loop">
			<c:if test="${(loop.index == 0 || loop.index%3  == 0)}">
				<c:set var="terminator" value="${loop.index + 4}"/>
			<div class="row">	
			</c:if>
			<div class="col-md-3">			    
       			<label class="radio-inline">
          			<input type="radio" name="optLC" id="LC${loop.index}" value="${lcompanion.key}" required ${lcompanion.value[2]}>
          			<c:if test="${lcompanion.value[1] != 'NoLC'}">
          				<img src="${url}/${lcompanion.value[1]}/character.png" width="120px" height="150px">
          			</c:if>
          			<span style="display:block; text-align: center;">${lcompanion.value[1]}</span>
         		</label>
       		</div>
		    <c:if test="${loop.index == terminator}">
		    </div>
		    </c:if> 
		    <c:if test="${(loop.last == 'true' && loop.index != terminator)}">
		    </div>
		    </c:if>
		</c:forEach>

        <div id="workSheetDiv">
 	       <hr>
        	<h4><%= rb.getString("where_i_keep_worksheet")%>:</h4>
			<div class="row">	
				<div class="col-md-2">			    
	       			<label class="radio-inline">
	          			<input type="radio" name="optLocation" id="optLeft" value="Left">
	          			<span style="display:block; text-align: center;"><%= rb.getString("to_the_left")%></span>
	         		</label>
	       		</div>
				<div class="col-md-2">			    
	       			<label class="radio-inline">
	          			<input type="radio" name="optLocation" id="optCenter" value="Center">
	          			<span style="display:block; text-align: center;"><%= rb.getString("in_the_center")%></span>
	         		</label>
	       		</div>
				<div class="col-md-2">			    
	       			<label class="radio-inline">
	          			<input type="radio" name="optLocation" id="optRight" value="Right">
	          			<span style="display:block; text-align: center;"><%= rb.getString("to_the_right")%></span>
	         		</label>
	       		</div>
			</div>
		</div>
        
		
		<div class="row">
			<input class="btn btn-success" type="submit" value="<%= rb.getString("lets_get_started")%>" style="
			    display: block;margin: auto;margin-top: 20px;">
		</div>
    </form>
</div>
     	
