<%--
  Frank	09-01-20	Issue #230 and IDs to form fields to allow initialization
  Frank	08-03-21	Added suppport for worksheet location and class message
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>
<% 

/**
 * Frank 12-18-20 Issue #336 added cache-busting for selected .js and .css files
 */

ResourceBundle versions = null; 
try {
	 versions = ResourceBundle.getBundle("Versions");
}
catch (Exception e) {
	 System.out.println("versions bundle ERROR");	 
//	logger.error(e.getMessage());	
}

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

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>MathSpring | <%= rb.getString("thank_you_for_using_ms")%></title>
    
    <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png">
    <link rel="icon" type="image/png" href="/favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="/favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="/manifest.json">
    <meta name="theme-color" content="#ffffff">
  <link rel="stylesheet" href="css/bootstrap.min.css">
        <link href="css/common_new.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="sass_compiled/loginouter.css?ver=<%=versions.getString("css_version")%>">
    <link href="js/jquery-ui-1.10.4.custom/css/spring/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">

    <script src="js/jquery-1.10.2.js"></script>
    <script src="js/jquery-ui-1.10.4.custom/js/jquery-ui-1.10.4.custom.min.js"></script>
    <script type="text/javascript" src="js/simple-slider.js"></script>
    <script type="text/javascript" src="js/login_new.js"></script>
    <script type="text/javascript">

    
        var huygui=true;
        // Unfortunately the back button will run this function too which means that it can generate a BeginExternalActivity
        $(document).ready(function() {
            surveyButton('${servletContext}', '${servletName}', '${URL}', '${skin}', ${sessionId}, '${interventionClass}');

        });

        </script>
<style>
    textarea:read-only {
      background-color: lightblue;
    }
</style>
</head>
<body>

<div class="additional-form">
    <div class="additional-form__wrapper">
        <h1 class="additional-form__header"><%= rb.getString("thank_you_for_using_ms")%></h1>
        <p class="additional-form__paragraph"><%= rb.getString("please_answer_some_questions")%></p>
        <hr>
        <jsp:include page="${innerjsp}" />
    </div>
</div>

    <script type="text/javascript">
        $(document).ready(function() {
        	
        	var inner = "${innerjsp}";
        	if (inner.indexOf('studentName_new.jsp?') >= 0) {
        		var urlSplitter = inner.split("?");
        		if (urlSplitter[1].indexOf("&") >= 0) {
	        		var paramListArr = urlSplitter[1].split("&");
        			var fnameArr = paramListArr[0].split("=");
		           	if (!(fnameArr[1] == "none")) {
    		       		document.getElementById("fname").value = fnameArr[1];
        		   	}
        			var lnameArr = paramListArr[1].split("=");
		           	if (!(lnameArr[1] == "none")) {
    		       		document.getElementById("lini").value = lnameArr[1];
        		   	}
        		}
        	}
        	
        	if (inner.indexOf('studentPedagogy_new.jsp?') >= 0) {
        		var urlSplitter = inner.split("?");
        		if (urlSplitter[1].indexOf("&") >= 0) {
	        		var paramListArr = urlSplitter[1].split("&");
					for (var i=0; i < paramListArr.length; i = i + 1) {
		        		var paramArr = paramListArr[i].split("=");
						if (paramArr[0] === "resource") {
			       			var worksheet = document.getElementById("workSheetDiv");
				           	if (paramArr[1] === "gazeOn") {
				           		worksheet.style.visibility = 'visible';  		     	
				           	}
					        else {
				           		worksheet.style.visibility = 'hidden';
					        }
        				}
						if (paramArr[0] === "location") {
			       			var optLoc = document.getElementById(paramArr[1]);
				           	optLoc.checked = 'checked';
        				}
						if (paramArr[0] === "messageFromTeacher") {
							document.getElementById("messageFromTeacher").value = paramArr[1];
        				}
					}
        		}
        	}

        
        });
    </script>        	

<%--<footer class="bottom-sticky-footer">--%>
    <%--&copy; 2016 University of Massachusetts Amherst and Worcester Polytechnic Institute ~ All Rights Reserved.--%>
<%--</footer>--%>
</body>
</html>
