<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>
<% 
/**
* Frank 05-18-2020 Commented out obsolete code which would not compile properly
* Frank 12-18-20 Issue #336 added cache-busting for selected .js and .css files
* Kartik 04-22-21 Issue #390 Added session clock functionality

*/
ResourceBundle versions = null; 
try {
	 versions = ResourceBundle.getBundle("Versions");
	 System.out.println("css_version=" + versions.getString("css_version"));
	 System.out.println("js_version=" + versions.getString("js_version"));
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

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>MathSpring | <%= rb.getString("my_progress") %></title>
    <link rel="apple-touch-icon" sizes="180x180" href="apple-touch-icon.png">
    <link rel="icon" type="image/png" href="favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="manifest.json">
    <meta name="theme-color" content="#ffffff">
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/common_new.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet" type="text/css"/>
    <link href="css/MyProgress_new.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet" type="text/css"/>
    <link href="css/graph_new.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet" type="text/css"/>
    <script src="js/jquery-1.10.2.js"></script>
    <script src="js/jchart.js"></script>
    <script type="text/javascript" src="js/tutorutils.js"></script>
    
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/jquery.jqplot.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/plugins/jqplot.canvasTextRenderer.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/plugins/jqplot.pieRenderer.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/plugins/jqplot.enhancedPieLegendRenderer.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/plugins/jqplot.highlighter.js" />"></script>
    
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
 
        var effort_legend_labels = ["<%= rb.getString("sof_tooltip") %>", "<%= rb.getString("att_tooltip") %>",   "<%= rb.getString("shint_tooltip") %>", "<%= rb.getString("shelp_tooltip") %>",  "<%= rb.getString("guess_tooltip") %>",   "<%= rb.getString("notr_tooltip") %>",  "<%= rb.getString("skip_tooltip") %>", "<%= rb.getString("giveup_tooltip") %>"];
		// Note; colors are in reverse order
        var effort_series_colors = ['#bebada','#8dd3c7', '#ffffb3', '#fb8072', '#fdb462', '#80b1d3', '#9beb94',  '#26f213'];

        var globals = {
                mouseSaveInterval: ${mouseSaveInterval},
                mouseHistory: [],
                sessionId: ${sessionId},
                timeInSession: ${timeInSession}
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
            chart.problemsDone("problemsDone_"+topicId,problemsSolved,totalProblems);
            chart.giveFeedbackAndPlant ("remarks_"+topicId,"plant_"+topicId,topicState,studentState_disengaged,topicMastery,problemsDoneWithEffort,SHINT_SOF_sequence,SOF_SOF_sequence,neglectful_count,problemsDone,problemsSolved);

			console.log(topicId);
            var effortsForGraph = "${ts.effortsForGraph}";
            if (effortsForGraph.length == 0) {
            	return;
            }
			console.log(effortsForGraph);
			var effortValueArr = effortsForGraph.split(',');
			
    		var line = [];
  			
  			for (j=7;j>=0;j = j - 1) {
  				var eff = [];
	  			eff.push(effort_legend_labels[j],Number(effortValueArr[j]));
	  			console.log(effort_legend_labels[j] + " " + effortValueArr[j]);
	  			line.push(eff);
  			}
			var canvasName = 'pie_' + topicId;
			
			plot_live_dashboard = $.jqplot(canvasName, [line], {
		    seriesDefaults: {
              renderer: $.jqplot.PieRenderer,
		      rendererOptions: {
		        showDataLabels: true,
			    startAngle: -90,
			    padding: 10,
		        sliceMargin: 4,
			    seriesColors: effort_series_colors
		      },
		    },
//		    legend:{
//	            show:true, 
//	            location:'e',
//	            fontSize: '8pt'
//	        },
		    highlighter: {
		        show: true,
		        useAxesFormatters: false,
	            location:'n',
	            fontSize: '12pt',
		        tooltipFormatString: '%s%2d'

		      }
		 
		  });
            </c:forEach>
        }

//        function challengeComplete (topicId) {
//            window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&topicId=' +topicId+ '&eventCounter=${eventCounter + 1}&mode=challenge&elapsedTime='+updateElapsedTime()+'&learningChoice=true&learningCompanion=${learningCompanion}&var=b';
//        }

//        function tryThisComplete (topicId) {
//            window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&topicId=' +topicId+ '&eventCounter=${eventCounter + 1}&mode=continue&elapsedTime='+updateElapsedTime()+'&learningChoice=true&learningCompanion=${learningCompanion}&var=b';
//        }

//        function continueComplete (topicId) {
//            window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&topicId=' +topicId+ '&eventCounter=${eventCounter + 1}&mode=continue&elapsedTime='+updateElapsedTime()+'&learningChoice=true&learningCompanion=${learningCompanion}&var=b';
//        }

//        function reviewComplete (topicId) {
//            window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&topicId=' +topicId+ '&eventCounter=${eventCounter + 1}&mode=review&elapsedTime='+updateElapsedTime()+'&learningChoice=true&learningCompanion=${learningCompanion}&var=b';
//        }

//        function returnToHutComplete () {
//            window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&elapsedTime='+ updateElapsedTime()+'&learningCompanion=${learningCompanion}&var=b';
//        }

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
//                if (useHybridTutor)
                    window.location = "${pageContext.request.contextPath}/TutorBrain?action=MPPContinueTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=continue&var=b&comment=";
//                else $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPContinueTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=continue&var=b&comment=",continueComplete(${ts.topicId})
            });

            $("#review_${ts.topicId}").click(function(){
//              if (useHybridTutor)
                    window.location = "${pageContext.request.contextPath}/TutorBrain?action=MPPReviewTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=review&var=b&comment=";
//                else $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPReviewTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=review&var=b&comment=",reviewComplete(${ts.topicId})
            });

            $("#challenge_${ts.topicId}").click(function(){
//                if (useHybridTutor)
                    window.location = "${pageContext.request.contextPath}/TutorBrain?action=MPPChallengeTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=challenge&var=b&comment=";
//                else $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPChallengeTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=challenge&var=b&comment=", challengeComplete(${ts.topicId})
            });

            $("#tryThis_${ts.topicId}").click(function(){
//                if (useHybridTutor)
                    window.location = "${pageContext.request.contextPath}/TutorBrain?action=MPPContinueTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=tryThis&var=b&comment=";
//                else $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPContinueTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=tryThis&var=b&comment=",tryThisComplete(${ts.topicId})
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
            startSessionClock(globals.timeInSession);

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
//                if (useHybridTutor)
                    window.location =  "${pageContext.request.contextPath}/TutorBrain?action=MPPReturnToHut&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=" +currentTopicId+"&probId=" + currentProblemId +
                        "&studentAction=backToSatHut&elapsedTime="+elapsedTime+ "&probElapsedTime="+probElapsedTime+"&learningCompanion=${learningCompanion}"+"&var=b";
//                else
//                    $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPReturnToHut&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId="+currentTopicId+"&studentAction=backToSatHut&var=b&comment=",returnToHutComplete);
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
//                if (useHybridTutor) {
                    window.location = "${pageContext.request.contextPath}/TutorBrain?action=Home&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=" + currentTopicId
                        + "&probId=" + currentProblemId
                        + "&elapsedTime=" + elapsedTime
                        + "&probElapsedTime=" + probElapsedTime
                        + "&learningCompanion=${learningCompanion}"
                        + "&var=b";
//                } else {
//                    $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPReturnToHut&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId="+currentTopicId+"&studentAction=backToSatHut&var=b&comment=",returnToHutComplete);
//                }
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
            <li class="nav-item"><a class="js-go-to-my-garden"><%= rb.getString("my_garden") %></a></li>
            <li class="nav-item"><a id="myProg"><%= rb.getString("my_progress") %></a></li>
            <li class="nav-item">
                <c:choose>
                    <c:when test="${newSession}">
                        <a onclick="window.location='TutorBrain?action=EnterTutor&sessionId=${sessionId}'+'&elapsedTime=${elapsedTime}' + '&eventCounter=0&var=b'">Practice Area</a>
                    </c:when>
                    <c:otherwise>
                        <a onclick="window.location='TutorBrain?action=MPPReturnToHut&sessionId=${sessionId}'+'&elapsedTime=${elapsedTime}' + '&eventCounter=${eventCounter}' + '&probId=${probId}&topicId=-1' + '&learningCompanion=${learningCompanion}&var=b'"><%= rb.getString("practice_area") %></a>
                    </c:otherwise>
                </c:choose>
            </li>
            <li class="nav-item"><a href="TutorBrain?action=Logout&sessionId=${sessionId}&elapsedTime=${elapsedTime}&var=b"><%= rb.getString("log_out") %> &nbsp;<span class="fa fa-sign-out"></a></span>
            <li class="nav-item nav-item--last">
            	<a href="#" class="session-clock-item"> <span> <i class="fa fa-clock-o"
							aria-hidden="true"></i>
						</span> <span id="session_clock"></span> </a>
            </li>
        </nav>
    </div><!-- wrapper -->
</header>


<div class="main-content">
    <div class="row progress-data-wrapper">
        <table class="table table-bordered progress-table">
            <thead class="thead-inverse progress-table-header">
            <tr>
                <th><%= rb.getString("topic") %></th>
                <th><%= rb.getString("remark") %></th>
                <th><%= rb.getString("performance") %></th>
                <th><%= rb.getString("effort") %></th>
                <th><%= rb.getString("progress") %></th>
                <th><%= rb.getString("actions") %></th>
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
                <c:set var="pieDiv" value="pie_${topicId}"/>
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
                    <p><%= rb.getString("mastery_level") %></p>
                    <a href="#" id="masteryBar_${topicId}">
                        <div id=${masteryChartDiv}></div>
                    </a>
                    <div id=${problemsDiv}></div>
                    <a href="#"
                       id="LearnMore"
                       onclick="window.location='${pageContext.request.contextPath}/TutorBrain?action=TopicDetail&sessionId=${sessionId}&elapsedTime='+updateElapsedTime()+'&mastery=${ts.mastery}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&topicName=${topicName}&problemsDone=${ts.problemsDone}&totalProblems=${ts.totalProblems}&var=b'"
                       class="littleLink btn mathspring-important-btn"><%= rb.getString("learn_more") %></a>
                </td>
                <td align="center" valign="bottom" class="pie_wrapper col-md-1" >
					<div id="${pieDiv}" style="width:200px; height:200px;"></div>
                </td>
                <td align="center" valign="bottom" class="plant_wrapper col-md-1">
                    <a href="#" id=${plantLink}><div id=${plantDiv}></div></a>
                </td>
                <td class="actionColumn col-md-1">
                    <ul id="reviewChallenge">

                <c:choose>
                    <c:when test="${ts.problemsDone>0 && ts.hasAvailableContent}">
                        <li class="huy-button huy-button--green" id="continue_${topicId}">
                            <a href="#"><%= rb.getString("continue") %></a>
                        </li>
                        <li class="huy-button huy-button--yellow" id="review_${topicId}">
                            <a href="#"><%= rb.getString("review") %></a>
                        </li>
                        <li class="huy-button huy-button--brown" id="challenge_${topicId}">
                            <a href="#"><span class="colorPink"><%= rb.getString("challenge") %></span></a>
                        </li>
                        </ul></td></tr>
                    </c:when>
                    <c:when test="${ts.problemsDone==0}">
                        <li class="huy-button huy-button--green" id="tryThis_${topicId}">
                            <a href="#"><%= rb.getString("try_this") %></a>
                        </li>
                    </c:when>
                    <%--The tutor sometimes can't continue a topic if some criteria are satisfied, so we only offer review and challenge--%>
                    <c:otherwise>
                        <li class="huy-button huy-button--yellow" id="review_${topicId}">
                            <a href="#"><%= rb.getString("review") %></a>
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

<footer class="bottom-sticky-footer">&copy; <%= rb.getString("copyright") %>
</footer>

<div class="commentLayer" id="commentHolder" >
    <form name="commentForm">
        <textarea class="commentBox" name="commentTextArea" id="commentText"></textarea>
        <br/>
        <button id="submitCommentButton" type="button" class="thoughtbot"><%= rb.getString("submit") %></button>
        <button id="cancelCommentButton" type="button" class="clean-gray"><%= rb.getString("cancel") %></button>
    </form>
</div>

<div class="plantCommentLayer" id="plantCommentHolder" >
    <div id="plantDetails"></div>
    <form name="plantCommentForm">
        <textarea class="plantCommentBox" name="commentTextArea" id="plantCommentText"></textarea>
        <br/>
        <button id="submitPlantCommentButton" type="button" class="thoughtbot">><%= rb.getString("submit") %></button>
        <button id="cancelPlantCommentButton" type="button" class="clean-gray"<%= rb.getString("cancel") %></button>
    </form>
</div>

<div class="plantCommentLayer" id="masteryBarCommentHolder" >
    <div id="MasteryBarDetails"><%= rb.getString("is_mastery_calculation_correct?") %><br/></div>
    <form name="masteryBarCommentForm">
        <input type="radio"  name="userChoice" value="accurate"><%= rb.getString("yes_correct") %><br>
        <input type="radio"  name="userChoice" value="ok" ><%= rb.getString("no_incorrect") %><br>
        <br/>
        <%= rb.getString("you_can_also_leave_your_comment") %>:
        <textarea class="plantCommentBox" name="commentTextArea"></textarea>
        <br/>
        <button id="submitMasteryBarCommentButton" type="button" class="thoughtbot">><%= rb.getString("submit") %></button>
        <button id="cancelMasteryBarCommentButton" type="button" class="clean-gray"><%= rb.getString("cancel") %></button>
    </form>
</div>

<div class="tableTopDropDown" id="performanceReadMore" >
    <div id="performanceReadMoreText"><%= rb.getString("trying_to_calculate_mastery_level") %>  <br/>
        <%= rb.getString("are_we_good_calculating_mastery_level?") %> <br/><br/></div>

    <form name="performanceFeedbackForm">
        <%= rb.getString("please_select_your_response_from_this_list") %>:    <br/><br/>
        <input type="radio"  name="userChoice" value="accurate"><%= rb.getString("good_job_of_mastery_calculation") %><br>
        <input type="radio"  name="userChoice" value="ok" ><%= rb.getString("mastery_calculation_is_ok") %><br>
        <input type="radio"  name="userChoice" value="notAccurate" ><%= rb.getString("mastery_calculation_is_poor") %>
        <br/><br/>
        <%= rb.getString("you_can_also_leave_your_comment") %>:
        <textarea class="plantCommentBox" name="commentTextArea" id="performanceCommentText"></textarea>
        <br/>
        <button id="submitMasteryFeedbackButton" type="button" class="thoughtbot">><%= rb.getString("submit") %></button>
        <button id="cancelMasteryFeedbackButton" type="button" class="clean-gray"><%= rb.getString("cancel") %></button>
    </form>
</div>

<div class="tableTopDropDown" id="progressReadMore" >
    <div id="progressReadMoreText"><%= rb.getString("plant_growth_description") %> <br/>
        <%= rb.getString("keep_plant_representation?") %> <br/><br/></div>
    <form name="progressFeedbackForm">
        Please select your response from this list:    <br/> <br/>
        <input type="radio"  name="userChoice" value="keep"><%= rb.getString("yes_keep_plants") %><br>
        <input type="radio"  name="userChoice" value="doesnotmatter" ><%= rb.getString("it_does_not_matter") %><br>
        <input type="radio"  name="userChoice" value="takeAway" ><%= rb.getString("no_take_away_plants") %><br/><br/>
        <%= rb.getString("you_can_also_leave_your_comment") %>:
        <textarea class="plantCommentBox" name="commentTextArea" ></textarea>
        <br/>
        <button id="submitProgressFeedbackButton" type="button" class="thoughtbot">><%= rb.getString("submit") %></button>
        <button id="cancelProgressFeedbackButton" type="button" class="clean-gray"><%= rb.getString("cancel") %></button>
    </form>
</div>

<div class="tableTopDropDown" id="remarksReadMore" >
    <div id="remarksReadMoreText"><%= rb.getString("trying_to_help_you_perform_better") %><br/>
        <%= rb.getString("is_suggestion_helping") %><br/><br/></div>
    <form name="remarksFeedbackForm">
        <%= rb.getString("please_select_your_response_from_this_list") %>:    <br/> <br/>
        <input type="radio"  name="userChoice" value="yes"><%= rb.getString("yes_helping") %>.<br>
        <input type="radio"  name="userChoice" value="ok" ><%= rb.getString("ok_helping") %><br>
        <input type="radio"  name="userChoice" value="no" ><%= rb.getString("not_helping") %>
        <br/><br/>
        <%= rb.getString("you_can_also_leave_your_comment") %>:
        <textarea class="plantCommentBox" name="commentTextArea" ></textarea>
        <br/>
        <button id="submitRemarksFeedbackButton" type="button" class="thoughtbot">><%= rb.getString("submit") %></button>
        <button id="cancelRemarksFeedbackButton" type="button" class="clean-gray">C<%= rb.getString("cancel") %>ancel</button>
    </form>
</div>
<div class="thankYou" id="commentAckLayer">
    <%= rb.getString("thank_you") %> <br/>
    <%= rb.getString("we_have_saved_your_input") %>
</div>

<!-- SCRIPT - LIBRARIES -->
<script src="js/bootstrap.min.js"></script>

</body>
</html>
