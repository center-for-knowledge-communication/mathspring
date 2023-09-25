<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>
<% 
/**
* Frank 12-18-20 Issue #336 added cache-busting for selected .js and .css files
* Frank 01-16-21 Issue #378 added correct answers element
* Frank 01-16-21 Issue #378R2 move correct answers element to top
* Frank 02-17-21 Issue #383R2 get context for buildProblems.js
* Boming 08-30-21 Issue #421 Circle answer before submission
*/
ResourceBundle versions = null; 
try {
	 versions = ResourceBundle.getBundle("Versions");
}
catch (Exception e) {
	 System.out.println("teacherToolsMain ERROR");	 
//	logger.error(e.getMessage());	
}

int pageLangIndex = 0;

try {
	pageLangIndex = (int) request.getAttribute("pageLangIndex");
}
catch (Exception e) {
	 System.out.println("pageLangIndex " + e.getMessage());
	 pageLangIndex = 0;
}


String pageLang = (String) request.getAttribute("pageLang");


Locale loc = request.getLocale();


try {
	if (pageLang.equals("es")) {
		loc = new Locale("es","US");	
	}
	else {
		loc = new Locale("en","US");	
	}	
}
catch (Exception e) {
	 System.out.println("pageLang r " + e.getMessage());
	 loc = new Locale("en","US");
}

ResourceBundle rb = null;

try {
	rb = ResourceBundle.getBundle("MathSpring",loc);
}
catch (Exception e) {
	System.out.println(e.getMessage());
}


String ctx = request.getContextPath();

%>

<jsp:useBean id="problem" scope="request" class="edu.umass.ckc.wo.content.Problem"/>
<!DOCTYPE html>
<html>
    <head>

        <link rel="stylesheet" type="text/css" href="css/quickAuthProblem.css?ver=<%=versions.getString("css_version")%>"/>
        <script src="js/jquery-1.10.2.js"></script>
        <script>
	    	var submitText =        "undef";
    		var stepText =          "undef";
    		var correctAnswerText = "undef";
    		var feedback =          "undef";
    		var hintText =          "undef";
    		var playhintText =      "undef";
	    	var step_by_step_solution =        "undef";
    		
	        $(document).ready(function () {
	        	contextPath = "<%=ctx%>";
	        	submitText =        "<%= rb.getString("submit") %>";
	        	stepText =          "<%= rb.getString("step") %>";
	        	correctAnswerText = "<%= rb.getString("correct_answer") %>";
	        	feedback =          "<%= rb.getString("feedback") %>";
	        	hintText =          "<%= rb.getString("hint") %>";
	        	playhintText =      "<%= rb.getString("play_hint") %>";
	        	step_by_step_solution = "<%= rb.getString("step_by_step_solution") %>";
	        });

        </script>
        <script src="js/quickAuth/format2json.js?ver=<%=versions.getString("js_version")%>"></script>
        <script src="js/quickAuth/formatBuilder.js?ver=<%=versions.getString("js_version")%>"></script>
        <script src="js/quickAuth/problemUtils.js?ver=<%=versions.getString("js_version")%>"></script>
        <script src="js/quickAuth/buildProblem.js?ver=<%=versions.getString("js_version")%>"></script>
        <script src="js/quickAuth/loadProblem.js?ver=<%=versions.getString("js_version")%>"></script>
        <script type="text/x-mathjax-config">
            MathJax.Hub.Config({
                showMathMenu: false,
                messageStyle: "none",
                tex2jax: {inlineMath: [['$$', '$$'], ['\\(', '\\)']], displayMath: [['\\[', '\\]']]},
                TeX: {extensions: ["color.js"]}
            });
    	
			$(document).ready(function() {
				console.log("ready");
				console.log('window.parent.probLang: ' + window.parent.probLang);
				console.log('heading: ' + window.parent.step_by_step_solution);
				console.log('button: ' + window.parent.submit_answer);
				console.log('altheading: ' + window.parent.alt_step_by_step_solution);
				console.log('altbutton: ' + window.parent.alt_submit_answer);
				if (window.parent.probLang == 'en') {
					document.getElementById("HintHeader").innerHTML =  window.parent.step_by_step_solution;
					document.getElementById("submit_answer").innerHTML =  window.parent.submit_answer;


				}
				else {
					document.getElementById("HintHeader").innerHTML =  window.parent.alt_step_by_step_solution;
					document.getElementById("submit_answer").innerHTML =  window.parent.alt_submit_answer;
				}
	        });

        </script>
        <script type="text/javascript" async src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.1/MathJax.js?config=TeX-MML-AM_CHTML"></script>
        <script type="text/javascript">
            var theProblem;  // A global variable which is the problem object.  Gets set in buildProblem.  Gets used in problemUtils
            const QUICKAUTH_PATH = "/html5Probs/qa/";
        </script>
    </head>

    <body onload="quickAuthLoadProblem.requestProblemData(${problem.id}, ${sessionId}, ${elapsedTime}, ${eventCounter}, '${servletContext}', '${servletName}', ${previewMode}, ${teacherId})">
    <div id="ProblemContainer"
         <c:choose>
             <c:when test="${not empty zoom}">style="background-color:white; transform:scale(${zoom})"</c:when>
             <c:otherwise>style="background-color:white;"</c:otherwise>
         </c:choose>>

        <div id="HintContainer" style="display: none">
            <div id="HintHeader">
            </div>
            <div id="HintThumbs">
            </div>
            <div class="clear"></div>
            <div id="HintContent">
            </div>
        </div>
        <div id="CorrectAnswers" class="answer_text" style="display: none">
   	    </div>
         <div id="ProblemStatement"></div>
        <div id="ProblemFigure"></div>
        <div id="Answers">
            <div id="ShortAnswerBox" style="display: none">
                <input id="answer_field" type="text"/>
                <span id="Units"></span>
            </div>
            <div id="MultipleChoiceAnswers" style="display: none">
            </div>
            <div id="SubmitAnswerBox" style="display:none">
                <button id="submit_answer" type="button"></button>
                <div id="Grade_Check" class="short_answer_check"></div>
                <div id="Grade_X" class="short_answer_x"></div>
                <div id="Grade_Circle" class="short_answer_circle"></div>
                
            </div>
        </div>
    </div>
    </body>
</html>
