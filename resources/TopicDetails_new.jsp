<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>
<% 
/**
 * Frank 05-18-2020 issue #117 restrict thumbnail sixe to 500px x 500px
 * Frank 08-14-2020 issue #194 removed link to Practice Area
*/
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

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>MathSpring | <%= rb.getString("topic_details") %></title>
    <link rel="apple-touch-icon" sizes="180x180" href="apple-touch-icon.png">
    <link rel="icon" type="image/png" href="favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="manifest.json">
    <meta name="theme-color" content="#ffffff">
    <link rel="stylesheet" href="sass_compiled/details.css">
    <link href="css/graph_new.css" rel="stylesheet" type="text/css"/>

    <script src="js/jquery-1.10.2.js"></script>
    <script src="js/jchart.js"></script>
    <script type="text/javascript" src="js/tutorutils.js"></script>
<!-- <script src="js/bootstrap/js/language_es.js"></script> -->

    <script language="javascript" type="text/javascript">
        var problemList=new Array();
        var currentProblem="";
        var currentTopic=${topicId};
        var currentProblemId=0;
        var currentEffort="";
        var formalityId="";
        var isFormality=false;
        var problemImagePath="";
        var effortFeedback="";
        var elapsedTime=0;
        var probElapsedTime=0;
        var startClockTime = 0;
        var startElapsedTime=0;
        var useHybridTutor=${useHybridTutor};
        var selectedCard = null;
        var problem_imageURL = '${webContentpath}'+'problemSnapshots/prob_';
   
        var globals = {
            mouseSaveInterval: ${mouseSaveInterval},
            mouseHistory: [],
            sessionId: ${sessionId}
        }

        var sysGlobals = {
            gritServletContext: '${gritServletContext}',
            wayangServletContext: '${wayangServletContext}',
            gritServletName: '${gritServletName}'
        }


        function initiateElapsedTime() {
            startElapsedTime= ${elapsedTime} ;
            var d = new Date();
            startClockTime = d.getTime();
        }

        function updateElapsedTime() {
            var d = new Date();
            var now = d.getTime();
            probElapsedTime += now-startClockTime;
            elapsedTime = startElapsedTime + probElapsedTime;
            return elapsedTime;
        }

        function initChart() {
            var chart = Chart;
            chart.init();
            chart.renderMastery("masteryChartDiv",${mastery} ,${problemsDone} );
            var i=0;

            <c:forEach var="pd" items="${problemDetailsList}">
                problemList[i]=new Array(8);
                problemList[i][0]="${pd.problemId}";
                problemList[i][1]="${pd.problemName}";
                problemList[i][2]="${pd.effort}";
                problemList[i][3]=${pd.numAttemptsToSolve};
                problemList[i][4]=${pd.numHints};
                problemList[i][6]="${pd.ccstds}";
                problemList[i][7]="${pd.snapshot}";
                i++;
            </c:forEach>

            chart.renderCharts(problemList,i,wrapperList);
        }

        $(document).ready(function(){
            // Set up mouse tracking if the mouseSaveInterval is positive (our indicator that mouse tracking is desired)
            if (globals.mouseSaveInterval > 0)
                setInterval(sendMouseData, 1000 * globals.mouseSaveInterval); // send mouse data to server every # of seconds
            // Only set up event listeners on the body of the page if the mouseSaveInterval is positive (our indication that mouse tracking is desired)
            if (globals.mouseSaveInterval > 0) {
                $("body").mousemove(function (e) {
                    // var $body = $('body');
                    // var offset = $body.offset();
                    // var x = e.clientX - offset.left;
                    // var y = e.clientY - offset.top;
                    var x = e.pageX;
                    var y = e.pageY;
                    // console.log(e.pageX +  "," + e.pageY);
                    saveMouse(x, y);

                });

                $("body").bind('click', function (e) {
                    var x = e.pageX;
                    var y = e.pageY;
                    saveMouseClick(x, y);
                    // console.log(e.pageX +  "," + e.pageY);
                });
            }
            initiateElapsedTime();
            initChart();

            function tryThisComplete (problemId) {
                window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&elapsedTime='+updateElapsedTime()+'&learningCompanion=${learningCompanion}&mode=practice&topicId='+currentTopic+'&problemIdString='+problemId+'&var=b';
            }

            $('#wrapperList li').each(function(index) {
                $(this).addClass('non-selected-card');
                $(this)
                    .click(function() {
                        if (useHybridTutor)
                            window.location= "${pageContext.request.contextPath}/TutorBrain?action=MPPTryProblem&elapsedTime="+updateElapsedTime()+"&sessionId=${sessionId}&problemId="+currentProblemId+"&topicId="+currentTopic+"&studentAction=tryThis&mode=practice&var=b&comment=";
                        else
                            $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPTryProblem&elapsedTime="+updateElapsedTime()+"&sessionId=${sessionId}&problemId="+currentProblemId+"&topicId="+currentTopic+"&studentAction=tryThis&var=b&comment=",tryThisComplete(currentProblemId));
                    })
                    .hover(function() {
                        loadProblem($(this));
                    });
                if (index == 0) $(this).hide();
                if (index == 1) loadProblem($(this));

                function loadProblem(card) {
                    if (selectedCard != null) {
                        selectedCard.removeClass('selected-card');
                        selectedCard.addClass('non-selected-card');
                    }
                    selectedCard = card;
                    card.removeClass('non-selected-card');
                    card.addClass("selected-card");
                    var position = $("#problemCards").position();
                    var tPosX = position.left ;
                    var tPosY = position.top+$("#problemCards").height();
                    $(".dropDownContent").css({top:tPosY, left: tPosX}).show();
                    currentProblemId=problemList[index-1][0];
                    currentProblem=problemList[index-1][1];
                    currentEffort=problemList[index-1][2];
                    effortFeedback=problemList[index-1][5];

                    if (currentProblem.substring(0,10)=="formality_")
                    {
                        isFormality=true;
                        formalityId= currentProblem.substring(10,currentProblem.length);
                    }
                    else isFormality=false;

                    if (!isFormality) {
                        $("#js-problem-view").text(effortFeedback);
                        $("#js-problem-view").append("<img id='problemImage' style='max-width:500px; max-height:500px;' />");
                        document.getElementById("problemImage").src = problem_imageURL+currentProblemId+".jpg";
                        console.log("CCSS: " + problemList[index-1][6]);
                    } else {
                        $("#js-problem-view").text(currentProblem);
                        $("#js-problem-view").append("<iframe id='formalityProblemFrame' width='600' height='300'> </iframe>");
                        problemImagePath="http://cadmium.cs.umass.edu/formality/FormalityServlet?fxn=questionSnapshot&qID="+formalityId;
                        document.getElementById("formalityProblemFrame").src = problemImagePath;
                        console.log("CCSS: " + problemList[index-1][6]);
                    }

                    document.getElementById("problemDetailsButtons").onclick = function() {
                        if (useHybridTutor)
                            window.location="${pageContext.request.contextPath}/TutorBrain?action=MPPTryProblem&elapsedTime="+updateElapsedTime()+"&sessionId=${sessionId}&problemId="+currentProblemId+"&topicId="+currentTopic+"&studentAction=tryThis&mode=practice&comment=";
                        else $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPTryProblem&elapsedTime="+updateElapsedTime()+"&sessionId=${sessionId}&problemId="+currentProblemId+"&topicId="+currentTopic+"&studentAction=tryThis&mode=practice&comment=",tryThisComplete(currentProblemId));
                    };
                }
            });

            $(".js-go-to-my-garden").click(function() {
                var currentTopicId = ${topicId};
                updateElapsedTime();
                if (useHybridTutor) {
                    window.location = "${pageContext.request.contextPath}/TutorBrain?action=Home&sessionId=${sessionId}&eventCounter=${eventCounter + 1}"
                        + "&topicId=" + currentTopicId
                        + "&probId=" + currentProblemId
                        + "&elapsedTime=" + elapsedTime
                        + "&probElapsedTime=" + probElapsedTime
                        + "&learningCompanion=${learningCompanion}"
                        + "&var=b";
                } else {
                    $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPReturnToHut&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId="+currentTopicId+"&studentAction=backToSatHut&var=b&comment=",returnToHutComplete);
                }
            });
            
        });
    </script>
</head>
<body>
<div class="nav">
    <div class="nav__logo">
        <img src="img/mstile-150x150.png" alt="" class="nav__logo-image">
        <span class="nav__logo-text">
            <span class="nav__logo-text--green-letter">M</span>ATH<span class="nav__logo-text--green-letter">S</span>PRING
        </span>
    </div>

    <ul class="nav__list">
        <li class="nav__item">
            <a class="js-go-to-my-garden"><%= rb.getString("my_garden") %></a>
        </li>
        <li class="nav__item">
            <a
                    onclick="window.location='TutorBrain?action=navigation&from=sat_Hut&to=my_progress&elapsedTime=0&sessionId=${sessionId}'+ '&eventCounter=${eventCounter}' + '&topicId=-1&probId=${probId}&probElapsedTime=0&var=b'"
            >
                <%= rb.getString("my_progress") %>
            </a>
        </li>
<!-- 
        <li class="nav__item">
            <c:choose>
                <c:when test="${newSession}">
                    <a onclick="window.location='TutorBrain?action=EnterTutor&sessionId=${sessionId}'+'&elapsedTime=${elapsedTime}' + '&eventCounter=0&var=b'">Practice Area></a>
                </c:when>
                <c:otherwise>
<                 	<a onclick="alert('Link broken, sending you to My Progess Page");window.location='TutorBrain?action=MPPReturnToHut&sessionId=${sessionId}'+'&elapsedTime=${elapsedTime}' + '&eventCounter=${eventCounter}' + '&probId=${probId}&topicId=-1' + '&learningCompanion=${learningCompanion}&var=b'">Practice Area></a> 
                </c:otherwise>
            </c:choose>
        </li>
-->
        <li class="nav__item">
            <a href="TutorBrain?action=Logout&sessionId=${sessionId}&elapsedTime=${elapsedTime}&var=">
                <%= rb.getString("log_out") %> &nbsp;
                <span class="fa fa-sign-out"></span>
            </a>
        </li>
    </ul>
</div>
<div class="bootstrap">
    <div class="main-content">
        <div class="topic-details-wrapper">
            <div class="topic-details-view">
                <h1>${topicName}</h1>
                <div class="row topic-overview">
                    <div class="col-md-4">
                        <div class="row topic-statistics">
                            <h2><%= rb.getString("mastery_level") %></h2>
                            <div id="masteryChartDiv"></div>
                            <div>
                                <p class="problem-done-num">${problemsDone}/${totalProblems}</p>
                                <p class="problem_done_label"><%= rb.getString("topic_details_problems_done") %></p>
                            </div>
                        </div>
                        <div class="row" id="problemCards" rel="performanceDetails">
                            <ul id="wrapperList"><li></li></ul>
                        </div>
                    </div>
                    <div class="col-md-8 detail-problem-view">
                        <div id="js-problem-view"></div>
                        <div class="row">
                            <button type="button"
                                    class="btn btn-lg try-problem-button"
                                    id="problemDetailsButtons"><%= rb.getString("topic_details_click") %></button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
