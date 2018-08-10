<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>MathSpring | My Progress</title>
    <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png">
    <link rel="icon" type="image/png" href="/favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="/favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="/manifest.json">
    <meta name="theme-color" content="#ffffff">
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/common_new.css" rel="stylesheet" type="text/css"/>
    <link href="css/MyProgress_new.css" rel="stylesheet" type="text/css"/>
    <link href="css/graph_new.css" rel="stylesheet" type="text/css"/>
    <script src="js/jquery-1.10.2.js"></script>
    <script src="js/jchart.js"></script>
    <script type="text/javascript" src="js/tutorutils.js"></script>
    <script>
        $.extend({
            getUrlVars: function () {
                var vars = [], hash;
                var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
                for (var i = 0; i < hashes.length; i++) {
                    hash = hashes[i].split('=');
                    vars.push(hash[0]);
                    vars[hash[0]] = hash[1];
                }
                return vars;
            },
            getUrlVar: function (name) {
                return $.getUrlVars()[name];
            }
        });

        // These two vars are not initialized correctly in the initiate method below.   They appear to want them from URL params
        // but this JSP isn't getting URL params.   It is a pure forward done in servlet, so these values must be set as attributes
        // TODO find out if anyone is calling this JSP with URL params.  Perhaps this can be done in servlet forward??
        var currentTopicId=${topicId};
        var currentProblemId=${probId};
        var elapsedTime=0;
        var probElapsedTime=0;
        var mppElapsedTime=0;
        var startClockTime = 0;
        var startElapsedTime=0;
        var problemsDoneWithEffort=2;
        var useHybridTutor =${useHybridTutor};

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

        function initiate() {
            startElapsedTime= ($.getUrlVar('elapsedTime'))*1;
            probElapsedTime =  ($.getUrlVar('probElapsedTime'))*1;
            var d = new Date();
            startClockTime  = d.getTime();
        }

        function updateElapsedTime() {
            var d =new Date();
            var now = d.getTime();
            mppElapsedTime = now-startClockTime ;
            elapsedTime = startElapsedTime + mppElapsedTime;
            return  elapsedTime;
        }

        function renderProgressPage() {
            <c:forEach var="ts" items="${topicSummaries}">
            var topicState="${ts.topicState}";
            var topicId= ${ts.topicId};
            var topicMastery= ${ts.mastery};
            var problemsDone= ${ts.problemsDone};
            var problemsSolved = ${ts.numProbsSolved};
            var totalProblems= ${ts.totalProblems};
            var problemsDoneWithEffort= ${ts.problemsDoneWithEffort};
            var SHINT_SOF_sequence= ${ts.SHINT_SOF_sequence};
            var SOF_SOF_sequence= ${ts.SOF_SOF_sequence};
            var neglectful_count= ${ts.neglectful_count};
            var studentState_disengaged=false;
            var chart = Chart;

            chart.init();
            chart.renderMastery("masteryChart_"+topicId,topicMastery,problemsDone);
            chart.problemsDone("problemsDone_"+topicId,problemsDone,totalProblems,problemsSolved);
            chart.giveFeedbackAndPlant ("remarks_"+topicId,"plant_"+topicId,topicState,studentState_disengaged,topicMastery,problemsDoneWithEffort,SHINT_SOF_sequence,SOF_SOF_sequence,neglectful_count,problemsDone,problemsSolved);
            </c:forEach>
        }

        function challengeComplete (topicId) {
            window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&topicId=' +topicId+ '&eventCounter=${eventCounter + 1}&mode=challenge&elapsedTime='+updateElapsedTime()+'&learningChoice=true&learningCompanion=${learningCompanion}&var=b';
        }

        function tryThisComplete (topicId) {
            window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&topicId=' +topicId+ '&eventCounter=${eventCounter + 1}&mode=continue&elapsedTime='+updateElapsedTime()+'&learningChoice=true&learningCompanion=${learningCompanion}&var=b';
        }

        function continueComplete (topicId) {
            window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&topicId=' +topicId+ '&eventCounter=${eventCounter + 1}&mode=continue&elapsedTime='+updateElapsedTime()+'&learningChoice=true&learningCompanion=${learningCompanion}&var=b';
        }

        function reviewComplete (topicId) {
            window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&topicId=' +topicId+ '&eventCounter=${eventCounter + 1}&mode=review&elapsedTime='+updateElapsedTime()+'&learningChoice=true&learningCompanion=${learningCompanion}&var=b';
        }

        function returnToHutComplete () {
            window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&elapsedTime='+ updateElapsedTime()+'&learningCompanion=${learningCompanion}&var=b';
        }

        function addComments() {
            <c:forEach var="ts" items="${topicSummaries}">
            $("#commentLink_${ts.topicId}").click(function(){
                $.get("TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId="+currentTopicId+"&studentAction=commentClicked&comment=");
                currentTopicId=${ts.topicId};
                var position = $("#commentLink_${ts.topicId}").position();

                var tPosX = position.left ;
                var tPosY = position.top+$("#commentLink_${ts.topicId}").height();
                $("#commentHolder").css({top:tPosY, left: tPosX}).slideDown("slow");
                document.commentForm.commentTextArea.focus();
                document.commentForm.commentTextArea.value="";
            });

            $("#plantLink_${ts.topicId}").click(function(){
                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId="+currentTopicId+"&studentAction=plantClicked&var=b&comment=");
                problemsDoneWithEffort=${ts.problemsDoneWithEffort};
                var position = $("#plantLink_${ts.topicId}").position();
                var tPosX = position.left +130;
                var tPosY = position.top;

                $("#plantCommentHolder").css({top:tPosY, left: tPosX}).fadeIn("slow");
                document.plantCommentForm.commentTextArea.focus();
                document.plantCommentForm.commentTextArea.value="";

                $("#plantDetails").text("In this topic, you have solved "+problemsDoneWithEffort+" problems without guessing or getting bottom out hint.");
                $("#plantDetails").append("<br/>This plant will grow bigger as you put more effort on solving the problems.   <br/> <br/> You can leave your comment:");
            });

            $("#masteryBar_${ts.topicId}").click(function(){
                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId="+currentTopicId+"&studentAction=masteryBarClicked&var=b&comment=");

                var position = $("#masteryBar_${ts.topicId}").position();
                var tPosX = position.left +266;
                var tPosY = position.top-26;

                $("#masteryBarCommentHolder").css({top:tPosY, left: tPosX}).fadeIn("slow");
                document.masteryBarCommentForm.commentTextArea.value="";

                for (var i=0; i < document.masteryBarCommentForm.userChoice.length; i++)
                {
                    document.masteryBarCommentForm.userChoice[i].checked=false;
                }
            });

            $("#continue_${ts.topicId}").click(function(){
                if (useHybridTutor)
                    window.location = "${pageContext.request.contextPath}/TutorBrain?action=MPPContinueTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=continue&var=b&comment=";
                else $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPContinueTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=continue&var=b&comment=",continueComplete(${ts.topicId}));
            });

            $("#review_${ts.topicId}").click(function(){
                if (useHybridTutor)
                    window.location = "${pageContext.request.contextPath}/TutorBrain?action=MPPReviewTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=review&var=b&comment=";
                else $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPReviewTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=review&var=b&comment=",reviewComplete(${ts.topicId}));
            });

            $("#challenge_${ts.topicId}").click(function(){
                if (useHybridTutor)
                    window.location = "${pageContext.request.contextPath}/TutorBrain?action=MPPChallengeTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=challenge&var=b&comment=";
                else $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPChallengeTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=challenge&var=b&comment=", challengeComplete(${ts.topicId}));
            });

            $("#tryThis_${ts.topicId}").click(function(){
                if (useHybridTutor)
                    window.location = "${pageContext.request.contextPath}/TutorBrain?action=MPPContinueTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=tryThis&var=b&comment=";
                else $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPContinueTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=tryThis&var=b&comment=",tryThisComplete(${ts.topicId}));
            });
            </c:forEach>
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

            initiate() ;
            renderProgressPage();
            addComments();

            $("#searchlink").click(function(){
                $(".dropdown_contentBox").toggle();
            });

            $(".closeWindow").click(function(){
                this.parent().hide();
            });

            $("#submitCommentButton").click(function(){
                var comment = document.commentForm.commentTextArea.value;
                $.get("TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId="+currentTopicId+"&eventCounter=${eventCounter + 1}&studentAction=saveComment&var=b&comment="+comment);
                $("#commentHolder").slideUp("fast");

                if (comment!=""  ) {
                    var position = $("#commentHolder").position();
                    var tPosX = position.left;
                    var tPosY = position.top+30;
                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).fadeIn(800);
                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).delay(2000).fadeOut(800);
                }
            });

            $("#cancelCommentButton").click(function(){
                document.commentForm.commentTextArea.value="";
                $("#commentHolder").slideUp("fast");
            });

            $("#submitPlantCommentButton").click(function(){
                var comment = document.plantCommentForm.commentTextArea.value;
                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId="+currentTopicId+"&studentAction=savePlantComment&var=b&comment="+comment);
                $("#plantCommentHolder").fadeOut("slow");

                if (comment!="" ) {
                    var position = $("#plantCommentHolder").position();
                    var tPosX = position.left;
                    var tPosY = position.top+40;

                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).fadeIn(400);
                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).delay(2000).fadeOut(800);
                }
            });

            $("#cancelPlantCommentButton").click(function(){
                document.plantCommentForm.commentTextArea.value="";
                $("#plantCommentHolder").fadeOut("slow");
            });

            $("#cancelMasteryBarCommentButton").click(function(){
                document.masteryBarCommentForm.commentTextArea.value="";
                $("#masteryBarCommentHolder").fadeOut("slow");
            });

            $("#cancelMasteryFeedbackButton").click(function(){
                document.performanceFeedbackForm.commentTextArea.value="";
                $("#performanceReadMore").fadeOut("slow").slideUp(300);
            });

            $("#cancelProgressFeedbackButton").click(function(){
                document.progressFeedbackForm.commentTextArea.value="";
                $("#progressReadMore").fadeOut("slow").slideUp(300);
            });

            $("#cancelRemarksFeedbackButton").click(function(){
                document.remarksFeedbackForm.commentTextArea.value="";
                $("#remarksReadMore").fadeOut("slow").slideUp(300);
            });

            $("#backToVillage").click(function(){
                updateElapsedTime();
                if (useHybridTutor)
                    window.location =  "${pageContext.request.contextPath}/TutorBrain?action=MPPReturnToHut&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=" +currentTopicId+"&probId=" + currentProblemId +
                        "&studentAction=backToSatHut&elapsedTime="+elapsedTime+ "&probElapsedTime="+probElapsedTime+"&learningCompanion=${learningCompanion}"+"&var=b";
                else
                    $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPReturnToHut&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId="+currentTopicId+"&studentAction=backToSatHut&var=b&comment=",returnToHutComplete);
            });

            $("#Performance").click(function(){
                var position = $("#Performance").position();
                var tPosX = position.left-80;
                var tPosY = position.top+26;

                $("#performanceReadMore").css({top:tPosY, left: tPosX}).slideDown("slow").fadeIn(800);
                for (var i=0; i < document.performanceFeedbackForm.userChoice.length; i++)
                {
                    document.performanceFeedbackForm.userChoice[i].checked=false;
                }
                document.performanceFeedbackForm.commentTextArea.value="";
            });

            $("#Progress").click(function(){
                var position = $("#Progress").position();
                var tPosX = position.left-50;
                var tPosY = position.top+26;

                $("#progressReadMore").css({top:tPosY, left: tPosX}).slideDown("slow").fadeIn(400);

                for (var i=0; i < document.progressFeedbackForm.userChoice.length; i++)
                {
                    document.progressFeedbackForm.userChoice[i].checked=false;
                }
                document.progressFeedbackForm.commentTextArea.value="";
            });

            $("#Remarks").click(function(){
                var position = $("#Remarks").position();
                var tPosX = position.left-280;
                var tPosY = position.top+26;

                $("#remarksReadMore").css({top:tPosY, left: tPosX}).slideDown("slow").fadeIn(400);

                for (var i=0; i < document.remarksFeedbackForm.userChoice.length; i++)
                {
                    document.remarksFeedbackForm.userChoice[i].checked=false;
                }

                document.remarksFeedbackForm.commentTextArea.value="";
            });

            $("#submitMasteryFeedbackButton").click(function(){
                var userChoice = "";
                for (var i=0; i < document.performanceFeedbackForm.userChoice.length; i++)
                {
                    if (document.performanceFeedbackForm.userChoice[i].checked)
                    {
                        userChoice= document.performanceFeedbackForm.userChoice[i].value;
                    }
                }

                var comment = document.performanceFeedbackForm.commentTextArea.value;

                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=0&studentAction=savePerformanceUserChoice&var=b&comment="+userChoice);
                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=0&studentAction=savePerformanceComment&var=b&comment="+comment);
                $("#performanceReadMore").fadeOut("slow").slideUp(300);

                if (comment!="" || userChoice!="" ) {
                    var position = $("#performanceReadMore").position();
                    var tPosX = position.left;
                    var tPosY = position.top+80;

                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).fadeIn(800);
                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).delay(2000).fadeOut(800);
                }
            });

            $("#submitProgressFeedbackButton").click(function(){
                var userChoice = "";

                for (var i=0; i < document.progressFeedbackForm.userChoice.length; i++)
                {
                    if (document.progressFeedbackForm.userChoice[i].checked)
                    {
                        userChoice= document.progressFeedbackForm.userChoice[i].value;
                    }
                }

                var comment = document.progressFeedbackForm.commentTextArea.value;

                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId=0&eventCounter=${eventCounter + 1}&studentAction=saveProgressUserChoice&var=b&comment="+userChoice);
                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId=0&eventCounter=${eventCounter + 1}&studentAction=saveProgressComment&var=b&comment="+comment);
                $("#progressReadMore").fadeOut("slow").slideUp(300);

                if (comment!="" || userChoice!="" ) {
                    var position = $("#progressReadMore").position();
                    var tPosX = position.left;
                    var tPosY = position.top+80;
                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).fadeIn(800);
                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).delay(2000).fadeOut(800);
                }
            });

            $("#submitRemarksFeedbackButton").click(function(){
                var userChoice = "";
                for (var i=0; i < document.remarksFeedbackForm.userChoice.length; i++)
                {
                    if (document.remarksFeedbackForm.userChoice[i].checked)
                    {
                        userChoice= document.remarksFeedbackForm.userChoice[i].value;
                    }
                }

                var comment = document.remarksFeedbackForm.commentTextArea.value;

                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId=0&eventCounter=${eventCounter + 1}&studentAction=saveRemarksUserChoice&var=b&comment="+userChoice);
                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId=0&eventCounter=${eventCounter + 1}&studentAction=saveRemarksComment&var=b&comment="+comment);
                $("#remarksReadMore").fadeOut("slow").slideUp(300);

                if (comment!="" || userChoice!="" ) {
                    var position = $("#remarksReadMore").position();
                    var tPosX = position.left;
                    var tPosY = position.top+80;

                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).fadeIn(800);
                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).delay(2000).fadeOut(800);
                }
            });

            $("#submitMasteryBarCommentButton").click(function(){
                var userChoice = "";
                for (var i=0; i < document.masteryBarCommentForm.userChoice.length; i++)
                {
                    if (document.masteryBarCommentForm.userChoice[i].checked)
                    {
                        userChoice= document.masteryBarCommentForm.userChoice[i].value;
                    }
                }

                var comment = document.masteryBarCommentForm.commentTextArea.value;

                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId=0&eventCounter=${eventCounter + 1}&studentAction=saveRemarksUserChoice&var=b&comment="+userChoice);
                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId=0&eventCounter=${eventCounter + 1}&studentAction=saveRemarksComment&var=b&comment="+comment);
                if (comment!="" || userChoice!="" ) {
                    var position = $("#masteryBarCommentHolder").position();
                    var tPosX = position.left;
                    var tPosY = position.top+40;

                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).fadeIn(800);
                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).delay(2000).fadeOut(800);
                }

                $("#masteryBarCommentHolder").fadeOut("slow").slideUp(300);
            });

            $(".js-go-to-my-garden").click(function() {
                updateElapsedTime();
                if (useHybridTutor) {
                    window.location = "${pageContext.request.contextPath}/TutorBrain?action=Home&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=" + currentTopicId
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

<!-- NAVIGATION BAR -->
<header class="site-header" role="banner">
    <div id="wrapper">
        <div class="navbar-header">
            <img src="img/mstile-150x150.png" alt="" class="nav__logo-image">
            <span class="nav__logo-text"><span class="nav__logo-text--green-letter">M</span>ATH<span class="nav__logo-text--green-letter">S</span>PRING
        </span>
        </div><!-- navbar-header -->

        <nav id="main_nav" class="nav navbar-nav navbar-right">
            <li class="nav-item"><a class="js-go-to-my-garden">My Garden</a></li>
            <li class="nav-item"><a id="myProg">My Progress</a></li>
            <li class="nav-item">
                <c:choose>
                    <c:when test="${newSession}">
                        <a onclick="window.location='TutorBrain?action=EnterTutor&sessionId=${sessionId}'+'&elapsedTime=${elapsedTime}' + '&eventCounter=0&var=b'">Practice Area</a>
                    </c:when>
                    <c:otherwise>
                        <a onclick="window.location='TutorBrain?action=MPPReturnToHut&sessionId=${sessionId}'+'&elapsedTime=${elapsedTime}' + '&eventCounter=${eventCounter}' + '&probId=${probId}&topicId=-1' + '&learningCompanion=${learningCompanion}&var=b'">Practice Area</a>
                    </c:otherwise>
                </c:choose>
            </li>
            <li class="nav-item nav-item--last"><a href="TutorBrain?action=Logout&sessionId=${sessionId}&elapsedTime=${elapsedTime}&var=b">Log Out &nbsp; <span class="fa fa-sign-out"></span></a>
        </nav>
    </div><!-- wrapper -->
</header>


<div class="main-content">
    <div class="row progress-data-wrapper">
        <table class="table table-bordered progress-table">
            <thead class="thead-inverse progress-table-header">
            <tr>
                <th>Topic</th>
                <th>Remark</th>
                <th>Performance</th>
                <th>Effort</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="ts" items="${topicSummaries}">
                <c:set var="topicName" value="${ts.topicName}"/>
                <c:set var="topicId" value="${ts.topicId}"/>
                <c:set var="masteryChartDiv" value="masteryChart_${topicId}"/>
                <c:set var="remarksDiv" value="remarks_${topicId}"/>
                <c:set var="problemsDiv" value="problemsDone_${topicId}"/>
                <c:set var="plantDiv" value="plant_${topicId}"/>
                <c:set var="commentLink" value="commentLink_${topicId}"/>
                <c:set var="plantLink" value="plantLink_${topicId}"/>
                <c:set var="backToVillageURL" value="${backToVillageURL}"/>
                <tr>
                <td class="topic col-md-3">${topicName}</td>
                <td valign="top" class="remarks col-md-5">
                    <div id=${remarksDiv}></div>
                    <a href="#" class="littleLink" id=${commentLink}>
                        <img src="img/commentIcon.png" height="14">Comment&gt;
                    </a>
                </td>
                <td class="performance text-center col-md-2">
                    <p>Mastery Level</p>
                    <a href="#" id="masteryBar_${topicId}">
                        <div id=${masteryChartDiv}></div>
                    </a>
                    <div id=${problemsDiv}></div>
                    <a href="#"
                       id="LearnMore"
                       onclick="window.location='${pageContext.request.contextPath}/TutorBrain?action=TopicDetail&sessionId=${sessionId}&elapsedTime='+updateElapsedTime()+'&mastery=${ts.mastery}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&topicName=${topicName}&problemsDone=${ts.problemsDone}&totalProblems=${ts.totalProblems}&var=b'"
                       class="littleLink btn mathspring-important-btn">Learn More</a>
                </td>
                <td align="center" valign="bottom" class="plant_wrapper col-md-1">
                    <a href="#" id=${plantLink}><div id=${plantDiv}></div></a>
                </td>
                <td class="actionColumn col-md-1">
                    <ul id="reviewChallenge">

                <c:choose>
                    <c:when test="${ts.problemsDone>0 && ts.hasAvailableContent}">
                        <li class="huy-button huy-button--green" id="continue_${topicId}">
                            <a href="#">Continue</a>
                        </li>
                        <li class="huy-button huy-button--yellow" id="review_${topicId}">
                            <a href="#">Review</a>
                        </li>
                        <li class="huy-button huy-button--brown" id="challenge_${topicId}">
                            <a href="#"><span class="colorPink">Challenge</span></a>
                        </li>
                        </ul></td></tr>
                    </c:when>
                    <c:when test="${ts.problemsDone==0}">
                        <li class="huy-button huy-button--green" id="tryThis_${topicId}">
                            <a href="#">Try this</a>
                        </li>
                    </c:when>
                    <%--The tutor sometimes can't continue a topic if some criteria are satisfied, so we only offer review and challenge--%>
                    <c:otherwise>
                        <li class="huy-button huy-button--yellow" id="review_${topicId}">
                            <a href="#">Review</a>
                        </li>

                        </ul></td></tr>
                    </c:otherwise>
                </c:choose>
                    </ul>
                </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<footer class="bottom-sticky-footer">&copy; 2016 University of Massachusetts Amherst and Worcester Polytechnic Institute ~ All Rights Reserved.
</footer>

<div class="commentLayer" id="commentHolder" >
    <form name="commentForm">
        <textarea class="commentBox" name="commentTextArea" id="commentText"></textarea>
        <br/>
        <button id="submitCommentButton" type="button" class="thoughtbot">Submit</button>
        <button id="cancelCommentButton" type="button" class="clean-gray">Cancel</button>
    </form>
</div>

<div class="plantCommentLayer" id="plantCommentHolder" >
    <div id="plantDetails"></div>
    <form name="plantCommentForm">
        <textarea class="plantCommentBox" name="commentTextArea" id="plantCommentText"></textarea>
        <br/>
        <button id="submitPlantCommentButton" type="button" class="thoughtbot">Submit</button>
        <button id="cancelPlantCommentButton" type="button" class="clean-gray">Cancel</button>
    </form>
</div>

<div class="plantCommentLayer" id="masteryBarCommentHolder" >
    <div id="MasteryBarDetails">Do you think that this mastery calculation is correct?<br/></div>
    <form name="masteryBarCommentForm">
        <input type="radio"  name="userChoice" value="accurate">Yes, it is correct<br>
        <input type="radio"  name="userChoice" value="ok" >No, it is incorrect.<br>
        <br/>
        You can also leave your comment:
        <textarea class="plantCommentBox" name="commentTextArea"></textarea>
        <br/>
        <button id="submitMasteryBarCommentButton" type="button" class="thoughtbot">Submit</button>
        <button id="cancelMasteryBarCommentButton" type="button" class="clean-gray">Cancel</button>
    </form>
</div>

<div class="tableTopDropDown" id="performanceReadMore" >
    <div id="performanceReadMoreText">We are trying to calculate your mastery level from your performance.  <br/>
        Do you think that we are doing a good job in calculating your mastery level? <br/><br/></div>

    <form name="performanceFeedbackForm">
        Please select your response from this list:    <br/><br/>
        <input type="radio"  name="userChoice" value="accurate">You are doing very good job of Mastery calculation.<br>
        <input type="radio"  name="userChoice" value="ok" >Mastery calculation is ok<br>
        <input type="radio"  name="userChoice" value="notAccurate" >You are doing poor job of Mastery calculation.
        <br/><br/>
        You can also leave your comment:
        <textarea class="plantCommentBox" name="commentTextArea" id="performanceCommentText"></textarea>
        <br/>
        <button id="submitMasteryFeedbackButton" type="button" class="thoughtbot">Submit</button>
        <button id="cancelMasteryFeedbackButton" type="button" class="clean-gray">Cancel</button>
    </form>
</div>

<div class="tableTopDropDown" id="progressReadMore" >
    <div id="progressReadMoreText">Your progress in a topic is represented by a plant. As you put more effort on solving problems, the plant will grow and eventually
        will give fruits once you master the topic. <br/>
        Do you think that we should keep plants to represent your progress? <br/><br/></div>
    <form name="progressFeedbackForm">
        Please select your response from this list:    <br/> <br/>
        <input type="radio"  name="userChoice" value="keep">Yes, keep the plants.<br>
        <input type="radio"  name="userChoice" value="doesnotmatter" >It does not really matter.<br>
        <input type="radio"  name="userChoice" value="takeAway" >No, take away the plants.
        <br/><br/>
        You can also leave your comment:
        <textarea class="plantCommentBox" name="commentTextArea" ></textarea>
        <br/>
        <button id="submitProgressFeedbackButton" type="button" class="thoughtbot">Submit</button>
        <button id="cancelProgressFeedbackButton" type="button" class="clean-gray">Cancel</button>
    </form>
</div>

<div class="tableTopDropDown" id="remarksReadMore" >
    <div id="remarksReadMoreText">Based on your performance, we are trying to come up with suggestion to help you perform better in each topic.<br/>
        Do you think that our suggestion is helping you? <br/><br/></div>
    <form name="remarksFeedbackForm">
        Please select your response from this list:    <br/> <br/>
        <input type="radio"  name="userChoice" value="yes">Yes, they are helpful.<br>
        <input type="radio"  name="userChoice" value="ok" >The suggestions are ok.<br>
        <input type="radio"  name="userChoice" value="no" >No, they are not helpful.
        <br/><br/>
        You can also leave your comment:
        <textarea class="plantCommentBox" name="commentTextArea" ></textarea>
        <br/>
        <button id="submitRemarksFeedbackButton" type="button" class="thoughtbot">Submit</button>
        <button id="cancelRemarksFeedbackButton" type="button" class="clean-gray">Cancel</button>
    </form>
</div>
<div class="thankYou" id="commentAckLayer">
    Thank you! <br/>
    We have saved your input.
</div>

<!-- SCRIPT - LIBRARIES -->
<script src="js/bootstrap.min.js"></script>

</body>
</html>
