<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>MathSpring | My Garden</title>
    <link rel="apple-touch-icon" sizes="180x180" href="apple-touch-icon.png">
    <link rel="icon" type="image/png" href="favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="manifest.json">

    <link href="sass_compiled/dashboard.css" rel="stylesheet" type="text/css" />

    <script src="js/jquery-1.10.2.js"></script>
    <script src="js/jchart_new.js"></script>
    <script src="js/huy-slider.js"></script>
    <script type="text/javascript" src="js/tutorutils.js"></script>
    <script>
    
    function loadFlashContent () {
        $("#loadFlash").attr("title", title);
        $("#loadFlashIFrame").attr("src","https://get.adobe.com/flashplayer");
        $("#loadFlash").dialog("open");

    }
    
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

        $(document).ready(function() {

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

            <c:set var="newUser" value="true"/>
            <c:forEach var="ts" items="${topicSummaries}">
            <c:if test="${ts.problemsDone != 0}">
                <c:set var="newUser" value="false"/>
            </c:if>
            var topicState = "${ts.topicState}";
            var topicId = ${ts.topicId};
            var topicMastery = ${ts.mastery};
            var problemsDone = ${ts.problemsDone};
            var problemsSolved = ${ts.numProbsSolved};
            var totalProblems = ${ts.totalProblems};
            var problemsDoneWithEffort = ${ts.problemsDoneWithEffort};
            var SHINT_SOF_sequence = ${ts.SHINT_SOF_sequence};
            var SOF_SOF_sequence = ${ts.SOF_SOF_sequence};
            var neglectful_count = ${ts.neglectful_count};
            var studentState_disengaged = false;
            var chart = Chart;


            chart.init();
            chart.giveFeedbackAndPlant(
                null,
                "plant_"+topicId,
                topicState,
                studentState_disengaged,
                topicMastery,
                problemsDoneWithEffort,
                SHINT_SOF_sequence,
                SOF_SOF_sequence,
                neglectful_count,
                problemsDone,
                problemsSolved);
            </c:forEach>

            $('#slider-s').on('input', function () {
                var audio = document.getElementById('backgroundmusic');
                audio.volume = this.value / 100.0;
                console.log(this.value);
            });

            window.volumeControlOpen = false;
            $('#volume_control').on('click', toggleVolumeControl);
            $(document).mouseup(closeVolumeControl);
            $('.play-button span').on('click', playBackground);
        });

    </script>
</head>
<body>

<audio src="css/dashboard-music.mp3" id="backgroundmusic" onload="adjustVolume()" autoplay loop></audio>
<div class="nav">
    <div class="nav__logo">
        <img src="img/mstile-150x150.png" alt="" class="nav__logo-image">
        <span class="nav__logo-text">
            <span class="nav__logo-text--green-letter">M</span>ATH<span class="nav__logo-text--green-letter">S</span>PRING
        </span>
    </div>

    <ul class="nav__list">
        <li class="nav__item">
            <a href="#">My Garden</a>
        </li>
        <li class="nav__item">
            <a
                    onclick="window.location='TutorBrain?action=navigation&from=sat_Hut&to=my_progress&elapsedTime=0&sessionId=${sessionId}'+ '&eventCounter=${eventCounter}' + '&topicId=-1&probId=${probId}&probElapsedTime=0&var=b'"
            >
                My Progress
            </a>
        </li>
        <li class="nav__item">
            <c:choose>
            <c:when test="${newSession}">
            <a onclick="window.location='TutorBrain?action=EnterTutor&sessionId=${sessionId}'+'&elapsedTime=${elapsedTime}' + '&eventCounter=0&var=b'">Practice Area</a>
            </c:when>
            <c:otherwise>
        <a onclick="window.location='TutorBrain?action=MPPReturnToHut&sessionId=${sessionId}'+'&elapsedTime=${elapsedTime}' + '&eventCounter=${eventCounter}' + '&probId=${probId}&topicId=-1' + '&learningCompanion=${learningCompanion}&var=b'">Practice Area</a>
        </c:otherwise>
        </c:choose>
        </li>
        <li class="nav__item">
            <a href="TutorBrain?action=Logout&sessionId=${sessionId}&elapsedTime=${elapsedTime}&var=">
                Log Out &nbsp;
                <span class="fa fa-sign-out"></span>
            </a>
        </li>
        <li class="nav__item" id="volume_control">
            <a href="#">
                <i class="fa fa-volume-up"></i>
            </a>
        </li>
        <div class="slider-wrapper">
            <div class="play-button">
                <span class="fa fa-pause"></span>
            </div>
            <div class="volume-adjust-wrapper">
                <span class="fa fa-volume-down"></span>&nbsp;
                <input type="range" name="slider-s" id="slider-s" value="50" min="0" max="100" data-highlight="true"/>&nbsp;
                <span class="fa fa-volume-up"></span>
            </div>
        </div>
    </ul>
</div>

<div class="topic-list">
    <c:if test="${newUser == true}">
        <div class="welcome">
            <h1>Welcome to MathSpring</h1>
            <p>Go to
                <a onclick="window.location='TutorBrain?action=navigation&from=sat_Hut&to=my_progress&elapsedTime=0&sessionId=${sessionId}'+ '&eventCounter=${eventCounter}' + '&topicId=-1&probId=${probId}&probElapsedTime=0&var=b'">My Progress</a>
                to see all topics in your class or
                <a onclick="window.location='TutorBrain?action=EnterTutor&sessionId=${sessionId}'+'&elapsedTime=${elapsedTime}' + '&eventCounter=0&var=b'">Practice Area</a> to get started!
            </p>
        </div>
    </c:if>

    <div id="Clouds">
        <div class="Cloud Foreground"></div>
        <div class="Cloud Background"></div>
        <div class="Cloud Foreground"></div>
        <div class="Cloud Background"></div>
        <div class="Cloud Foreground"></div>
        <div class="Cloud Background"></div>
        <div class="Cloud Background"></div>
        <div class="Cloud Foreground"></div>
        <div class="Cloud Background"></div>
        <div class="Cloud Background"></div>
        <!--  <svg viewBox="0 0 40 24" class="Cloud"><use xlink:href="#Cloud"></use></svg>-->
    </div>

    <div class="container">
        <c:forEach var="ts" items="${topicSummaries}">
            <c:set var="topicName" value="${ts.topicName}"/>
            <c:set var="numProblemsDone" value="${ts.problemsDone}"/>
            <c:set var="numTotalProblems" value="${ts.totalProblems}"/>
            <c:set var="plantDiv" value="plant_${ts.topicId}"/>
            <c:set var="percentDone" value="${numProblemsDone/numTotalProblems}"/>
            <c:set var="colorClass" value="0"/>
            <c:choose>
                <c:when test="${percentDone <= 0.2}">
                    <c:set var="colorClass" value="0"/>
                </c:when>

                <c:when test="${percentDone > 0.2 && percentDone <= 0.4}">
                    <c:set var="colorClass" value="20"/>
                </c:when>

                <c:when test="${percentDone > 0.4 && percentDone <= 0.6}">
                    <c:set var="colorClass" value="40"/>
                </c:when>

                <c:when test="${percentDone > 0.6 && percentDone <= 0.8}">
                    <c:set var="colorClass" value="60"/>
                </c:when>

                <c:when test="${percentDone > 0.8 && percentDone <= 1 }">
                    <c:set var="colorClass" value="80"/>
                </c:when>

            </c:choose>
            <c:choose>
                <c:when test="${ts.problemsDone>0 && ts.hasAvailableContent}">
                    <c:set var="challengeTopicLink" value="${pageContext.request.contextPath}/TutorBrain?action=MPPChallengeTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=challenge&location=Dashboard&comment=" />
                    <c:set var="continueTopicLink" value="${pageContext.request.contextPath}/TutorBrain?action=MPPContinueTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=continue&location=Dashboard&comment=" />
                    <c:set var="reviewTopicLink" value="${pageContext.request.contextPath}/TutorBrain?action=MPPReviewTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=review&location=Dashboard&comment=" />
                </c:when>
                <c:when test="${ts.problemsDone==0}">
                </c:when>
                <%--The tutor sometimes can't continue a topic if some criteria are satisfied, so we only offer review and challenge--%>
                <c:otherwise>
                    <c:set var="challengeTopicLink" value="${pageContext.request.contextPath}/TutorBrain?action=MPPChallengeTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=challenge&location=Dashboard&comment=" />
                    <c:set var="reviewTopicLink" value="${pageContext.request.contextPath}/TutorBrain?action=MPPReviewTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=review&location=Dashboard&comment=" />
                </c:otherwise>
            </c:choose>
            <c:if test="${ts.problemsDone != 0}">
                <div class="topic-list__item">
                    <div class="topic-list__flipper">
                        <div class="topic-list__front topic-list__front--${colorClass}">
                            <p class="topic-list__title">${topicName}</p>
                            <p class="topic-list__info">${numProblemsDone} / ${numTotalProblems} problems done</p>
                            <p class="topic-list__info">In Progress</p>
                            <div class="pot" id="${plantDiv}">
                            </div>
                        </div>
                        <div class="topic-list__back">
                            <p class="topic-list__title">${topicName}</p>
                            <p class="topic-list__info">${numProblemsDone} / ${numTotalProblems} problems done</p>
                            <p class="topic-list__info">In Progress</p>
                            <div class="topic-list__buttons">
                                <div
                                        class="topic-list__button topic-list__button--green"
                                        onclick="window.location='${continueTopicLink}'"
                                >
                                    CONTINUE
                                </div>

                                <div
                                        class="topic-list__button topic-list__button--yellow"
                                        onclick="window.location='${reviewTopicLink}'"
                                >
                                    REVIEW
                                </div>

                                <div
                                        class="topic-list__button topic-list__button--brown"
                                        onclick="window.location='${challengeTopicLink}'"
                                >
                                    CHALLENGE
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </c:forEach>

    </div>
    <div id="loadFlash" title="Enabling Flash">
    <iframe id="loadFlashIFrame" width="675" height="675"> </iframe>
	</div>
</div>


<!-- SCRIPT - LIBRARIES -->
<script src="js/bootstrap.min.js"></script>

</body>
</html>
