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

Locale loc = request.getLocale();
String lang = loc.getDisplayLanguage();

ResourceBundle rb = null;
try {
	rb = ResourceBundle.getBundle("MathSpring",loc);
}
catch (Exception e) {
//	logger.error(e.getMessage());
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
        	var contextPath = "<%=ctx%>";
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
                <button id="submit_answer" type="button"><%= rb.getString("submit_answer") %></button>
                <div id="Grade_Check" class="short_answer_check"></div>
                <div id="Grade_X" class="short_answer_x"></div>
                <div id="Grade_Circle" class="short_answer_circle"></div>
                
            </div>
        </div>
    </div>
    </body>
</html>
