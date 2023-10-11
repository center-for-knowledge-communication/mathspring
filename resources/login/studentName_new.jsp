<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 4/14/15
  Time: 3:49 PM
  To change this template use File | Settings | File Templates.
  Frank	09-01-20	Issue #230 and IDs to form fields to allow initialization
--%>
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

        <div id="name">	       
        	<h3><%= rb.getString("enter_student_name")%>:</h3>
			<div class="row">	
				<div class="col-md-3">			    
          			<span style=""style="width: 40px;"><%= rb.getString("first_name")%></span>: &nbsp;
          			<input type="text" name="fname" id="fname" value="">
	       		</div>
				<div class="col-md-3">			    
          			<span style="style="width: 12px;"><%= rb.getString("last_initial")%></span>: &nbsp;
          			<input type="text" name="lini" id="lini" value="">
	       		</div>
			</div>
		</div>
		<br>
        <div id="languageDiv">	       
        	<h3><%= rb.getString("select_student_language")%>:</h3>
			<div class="row">	
				<div class="col-md-2">			    
	       			<label class="radio-inline">
	          			<input type="radio" name="optLanguage" id="optEnglish" value="English">
	          			<span style="display:block; text-align: center;"><%= rb.getString("english")%></span>
	         		</label>
	       		</div>
				<div class="col-md-2">			    
	       			<label class="radio-inline">
	          			<input type="radio" name="optLanguage" id="optSpanish" value="Spanish">
	          			<span style="display:block; text-align: center;"><%= rb.getString("spanish")%></span>
	         		</label>
	       		</div>
			</div>
		</div>
		<br>
        <div id="genderDiv">	       
        	<h3><%= rb.getString("select_student_gender")%>:</h3>
			<div class="row">	
				<div class="col-md-2">			    
	       			<label class="radio-inline">
	          			<input type="radio" name="optGender" id="optFemale" value="F">
	          			<span style="display:block; text-align: center;"><%= rb.getString("student_gender_female")%></span>
	         		</label>
	       		</div>
				<div class="col-md-2">			    
	       			<label class="radio-inline">
	          			<input type="radio" name="optGender" id="optFemale" value="M">
	          			<span style="display:block; text-align: center;"><%= rb.getString("student_gender_male")%></span>
	         		</label>
	       		</div>
				<div class="col-md-2">			    
	       			<label class="radio-inline">
	          			<input type="radio" name="optGender" id="optOther" value="O">
	          			<span style="display:block; text-align: center;"><%= rb.getString("student_gender_other")%></span>
	         		</label>
	       		</div>
			</div>
		</div>
		<br>

        <input class="btn mathspring-btn" type="submit" onclick="return checkValues()" value="<%= rb.getString("submit")%>" />
    </form>
</div>
     	
<script>

function checkValues() {
	
	var result1 = false;
	var result2 = false;
	
    var languages = document.getElementsByName("optLanguage");
    for (const language of languages) {
        if (language.checked) {
            result1 = true;
            break;
        }
    }
    var genders = document.getElementsByName("optGender");
    for (const gender of genders) {
        if (gender.checked) {
            result2 = true;
            break;
        }
    }
    	
    if ((result1 == true) && (result2 == true)) {
    	return true;
    }
    else {
    	alert("Please answer all questions");
    	return false;
    }
}
</script>
