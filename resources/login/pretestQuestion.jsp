<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 4/13/15
  Time: 3:42 PM
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

<%@ page import="edu.umass.ckc.wo.content.PrePostProblemDefn" %>
<style>
    .ui-progressbar {
        position: relative;
    }
    .progress-label {
        position: absolute;
        left: 5%;
        top: 4px;
        font-weight: bold;
        text-shadow: 1px 1px 0 #fff;
    }
</style>

<%--  JQuery is loaded by the containing page loginK12Outer.jsp--%>
<script type="text/javascript">
    var startTime;
    var elapsedTime = 0;
    var showWaitMessage = true;
    // Pop up an alert after a period of time so student does not sit in the question too long
    $(document).ready(function () {
        var warnTime = ${question.waitTimeSecs};
        var interval = warnTime * 1000;  // convert to ms
        var d = new Date();
        startTime = d.getTime();
        // only set up a warning cycle if the warnTime is set.
        if ( warnTime > 0)
            setInterval(function () { alertUser(${question.isMultiChoice()})}, interval );
        // hide the controls that allow selecting 'I don't know'
        setIDontKnowControls(true, 'none');
    });

    // calculate the time since this page was shown and set the hidden input to have the time.
    // If the form is valid, the elapsedTime input will be sent.
    function updateElapsedTime () {
        var d = new Date();
        now = d.getTime();
        elapsedTime = now - startTime;
        document.getElementById("elapsedTimeInput").value = elapsedTime;
    }

    // value will be either 'none' or 'inline'.
    // none hides a control and inline exposes it.
    function setIDontKnowControls (isMultiChoice, value) {
        if (isMultiChoice) {
            var radBut = document.getElementById('IDontKnowRadioButton');
            if (radBut)
                radBut.style.display = value;   // hides the button control
            var butText = document.getElementById('IDontKnowText');
            if (butText)
                butText.style.display = value;
        }
        else {
            var but = document.getElementById('IDontKnowButton');
            if (but)
                but.style.display = value;
        }
    }

    function alertUser (isMultiChoice) {
        if (!showWaitMessage)
                return;
        if (isMultiChoice) {
            setIDontKnowControls(true, 'inline');
            var msg1 = "<%= rb.getString("taking_a_while_added_choice")%>";
            alert(msg1);
        }
        else {
            // expose a button that allows the user to select 'I don't know'
            setIDontKnowControls(false, 'inline');
            var msg2 = "<%= rb.getString("taking_a_while_added_button")%>";
            alert(msg2);
        }
    }

    // when the user clicks the I don't know button, it autofills the input box and submits the form.
    function submitIDontKnow () {
        // put text in the input box so that it will be valid on submission
        var msg3 = "<%= rb.getString("i_dont_know")%>" + ".";
        document.getElementById("f").value = msg3;
        // now submit the form
        document.getElementById("pretestQuestion").submit();
    }

    function validateForm(isMultipleChoiceQuest) {
        var a,b,c,d,e,idontknow,v;
        console.log("validateForm"+ isMultipleChoiceQuest);
        if (isMultipleChoiceQuest) {
            a = document.getElementById("a")!=null && document.getElementById("a").checked == true;
            b = document.getElementById("b")!=null && document.getElementById("b").checked == true;
            c = document.getElementById("c")!=null && document.getElementById("c").checked == true;
            d = document.getElementById("d")!=null && document.getElementById("d").checked == true;
            e = document.getElementById("e")!=null && document.getElementById("e").checked == true;
            idontknow =  document.getElementById("IDontKnowRadioButton")!=null &&
                    document.getElementById("IDontKnowRadioButton").checked == true;
            v = a || b || c || d || e || idontknow;

            if (!v) {
            	var msg4 = "<%= rb.getString("please_select_one_answer")%>";
            	alert(msg4);
            }
        }
        else {
            v = true;
            var input = document.getElementById("f").value.trim();
            if(input == "")
            {
            	var msg5 = "<%= rb.getString("please_answer_question")%>";
            	alert(msg5);
                document.getElementById("f").focus();
                v = false
            }
        }
        // once the user submits a valid answer, stop popping up messages about time.
        if (v)
                showWaitMessage = false;
        return v;
    }

    function setVar () {
        if (huygui) {
            document.getElementById("varparam").value = "b";
        }
        else {
            document.getElementById("varparam").value = "a";
        }
    }


    $( function() {

        var progressbar = $( "#progressbar" ),
                progressLabel = $( ".progress-label" );

        progressbar.progressbar({
            max: ${numProbsInTest},
            value: ${numProbsCompleted+1},
        });
        progressbar.width(300);

    } );
</script>

<%--@elvariable id="question" type="edu.umass.ckc.wo.content.PrePostProblemDefn"--%>

<c:if test="${message != null}">
    <b>${message}</b> <br><br>
</c:if>



<form id="pretestQuestion" method="post" name="login" onsubmit="updateElapsedTime(); setVar(); return validateForm(${question.isMultiChoice()})" action="${pageContext.request.contextPath}/WoLoginServlet">
    <input type="hidden" name="action" value="LoginInterventionInput"/>
    <input type="hidden" name="sessionId" value="${sessionId}">
    <input type="hidden" name="skin" value="${skin}"/>
    <input id="varparam" type="hidden" name="var" value="a"/>
    <input type="hidden" name="interventionClass" value="${interventionClass}"/>
    <input type="hidden" name="probId" value="${question.id}"/>
    <input id="elapsedTimeInput" type="hidden" name="elapsedTime" value="0"/>


    <p>&nbsp;</p>
    <c:if test="${question.hasImage}">
        <%-- DM 1/17/18 image (BLOB) field found to be corrupt.  Use of new MSAdmin tool for authoring pretest questions
        is now storing images in the apache doc root rather than db.   The path to the resource is (e.g.)
        http://rose.cs.umass.edu/mathspring/mscontent/surveys/surveyq_234/filename.jpg  where 234 is the ID of the prepostproblem --%>
        <%-- Never used: <img src="${pageContext.request.contextPath}${question.url}"/>--%>
        <%--<img src="${pageContext.request.contextPath}/getImage?table=prePostProblem&column=image&id=${question.id}"/>--%>
        <img src="${surveyURI}${question.imageFilename}"/>
        <br/>
    </c:if>
    <p><b>${question.descr}</b></p>
    <c:choose>
        <c:when test="${question.isMultiChoice()}">
            <c:if test="${question.aAns != null}">
                <c:choose>
                    <c:when test="${not fn:containsIgnoreCase(question.aAns, '<img')}">
                        <input id="a" type="radio" name="answer" size="60" value="${question.aAns}"> ${question.aAns}</input>
                    </c:when>
                    <c:otherwise>
                        <input id="a" type="radio" name="answer" size="60" value="a"> ${question.aAns}</input>
                    </c:otherwise>
                </c:choose>
                <br>
            </c:if>
            <c:if test="${question.bAns != null}">
                <c:choose>
                    <c:when test="${not fn:containsIgnoreCase(question.bAns, '<img')}">
                        <input id="b" type="radio" name="answer" size="60" value="${question.bAns}"> ${question.bAns}</input>
                    </c:when>
                    <c:otherwise>
                        <input id="b" type="radio" name="answer" size="60" value="b"> ${question.bAns}</input>
                    </c:otherwise>
                </c:choose>
                <br>
            </c:if>
            <c:if test="${question.cAns != null}">
                <c:choose>
                    <c:when test="${not fn:containsIgnoreCase(question.cAns, '<img')}">
                        <input id="c" type="radio" name="answer" size="60" value="${question.cAns}"> ${question.cAns}</input>
                    </c:when>
                    <c:otherwise>
                        <input id="c" type="radio" name="answer" size="60" value="c"> ${question.cAns}</input>
                    </c:otherwise>
                </c:choose>
                <br>
            </c:if>
            <c:if test="${question.dAns != null}">
                <c:choose>
                    <c:when test="${not fn:containsIgnoreCase(question.dAns, '<img')}">
                        <input id="d" type="radio" name="answer" size="60" value="${question.dAns}"> ${question.dAns}</input>
                    </c:when>
                    <c:otherwise>
                        <input id="d" type="radio" name="answer" size="60" value="d"> ${question.dAns}</input>
                    </c:otherwise>
                </c:choose>
                <br>
            </c:if>
            <c:if test="${question.eAns != null}">
                <c:choose>
                    <c:when test="${not fn:containsIgnoreCase(question.eAns, '<img')}">
                        <input id="e" type="radio" name="answer" size="60" value="${question.eAns}"> ${question.eAns}</input>
                    </c:when>
                    <c:otherwise>
                        <input id="e" type="radio" name="answer" size="60" value="e"> ${question.eAns}</input>
                    </c:otherwise>
                </c:choose>
                <br>
            </c:if>
            <br/>
            <input id="IDontKnowRadioButton" type="radio" name="answer" value="<%= rb.getString("i_dont_know")%>">
                <span id="IDontKnowText"><%= rb.getString("i_dont_know")%></span>
            </input>

            <br>
        </c:when>
        <c:when test="${question.isLongAnswer()}">
            <textarea id="ta" rows="4" cols="50" name="answer"> </textarea>
            </br>
            <br>
        </c:when>
        <c:otherwise>
            <input id="f" type="text" name="answer"/>
            </br>
            <br>
        </c:otherwise>
    </c:choose>


    <br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input  type="submit"  value="<%= rb.getString("submit")%>" /> &nbsp;&nbsp;
    <input id="IDontKnowButton" onClick="submitIDontKnow()" type="submit" value="<%= rb.getString("i_dont_know")%>" style="display:none"/>
    <br><br>
    <div id="progressbar"> <div class="progress-label">${numProbsCompleted+1} <%= rb.getString("of")%> ${numProbsInTest} <%= rb.getString("questions")%></div></div>
    </p>
</form>
<br>





