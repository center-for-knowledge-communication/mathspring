<%@ page import="edu.umass.ckc.wo.tutormeta.TopicMastery" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.umass.ckc.wo.smgr.SessionManager" %>

<%--
  Created by IntelliJ IDEA.
  User: Dovan
  Date: 2012
  Time: 5:22:15 PM
  To change this template use File | Settings | File Templates.
--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<%--<%@ include file="TopicDetails.jsp" %>--%>

<%--@elvariable id="topicSummaries" type="java.util.List"--%>
<%--@elvariable id="ts" type="edu.umass.ckc.wo.myprogress.TopicSummary"--%>

<%--@elvariable id="backToVillageURL" type="java.lang.String"--%>
<%--@elvariable id="learningCompanion" type="java.lang.String"--%>



<html id="default"><head>
    <meta charset="utf-8" />
    <title>MathSpring </title>

    <link rel="stylesheet" href="css/wayang.css" /><!-- Main Lar -->
   <!-- <link href='http://fonts.googleapis.com/css?family=IM+Fell+DW+Pica|Acme|Asul' rel='stylesheet' type='text/css'>



    <!-- Start Jquery and Scripts -->

    <script language="javascript" type="text/javascript" src="js/jquery-1.10.2.js"></script>
    <script language="javascript" type="text/javascript">
                 ///extended functions

    $.extend({
    getUrlVars: function(){
        var vars = [], hash;
        var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
        for(var i = 0; i < hashes.length; i++)
        {
            hash = hashes[i].split('=');
            vars.push(hash[0]);
            vars[hash[0]] = hash[1];
        }
        return vars;
    },
    getUrlVar: function(name){
        return $.getUrlVars()[name];
    }
});

    </script>
    <script language="javascript" type="text/javascript" src="js/jchart.js"></script>


    <script language="javascript" type="text/javascript">

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
        var startProbElapsedTime=0;
        var problemsDoneWithEffort=2;
         var problemsSolved=0;


         var useHybridTutor =${useHybridTutor};


        function initiate() {

            startElapsedTime= ($.getUrlVar('elapsedTime'))*1;
            probElapsedTime =  ($.getUrlVar('probElapsedTime'))*1;
            var d = new Date();
            startClockTime  = d.getTime();

        }

        function updateElapsedTime() {

            var d =new Date();
            var now = d.getTime();
//            probElapsedTime += now-startClockTime ;
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


        </c:forEach>

        }


        function challengeComplete (topicId) {
            window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&topicId=' +topicId+ '&eventCounter=${eventCounter + 1}&mode=challenge&elapsedTime='+updateElapsedTime()+'&learningChoice=true&learningCompanion=${learningCompanion}';

        }

        function tryThisComplete (topicId) {
            window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&topicId=' +topicId+ '&eventCounter=${eventCounter + 1}&mode=continue&elapsedTime='+updateElapsedTime()+'&learningChoice=true&learningCompanion=${learningCompanion}';
        }

        function continueComplete (topicId) {
            window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&topicId=' +topicId+ '&eventCounter=${eventCounter + 1}&mode=continue&elapsedTime='+updateElapsedTime()+'&learningChoice=true&learningCompanion=${learningCompanion}';
        }

        function reviewComplete (topicId) {
           window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&topicId=' +topicId+ '&eventCounter=${eventCounter + 1}&mode=review&elapsedTime='+updateElapsedTime()+'&learningChoice=true&learningCompanion=${learningCompanion}';

        }

        function returnToHutComplete () {
            window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&elapsedTime='+ updateElapsedTime()+'&learningCompanion=${learningCompanion}';

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

                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId="+currentTopicId+"&studentAction=plantClicked&comment=");

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

                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId="+currentTopicId+"&studentAction=masteryBarClicked&comment=");

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
                    window.location = "${pageContext.request.contextPath}/TutorBrain?action=MPPContinueTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=continue&comment=";
                else $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPContinueTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=continue&comment=",continueComplete(${ts.topicId}));

            });

            $("#review_${ts.topicId}").click(function(){
                if (useHybridTutor)
                     window.location = "${pageContext.request.contextPath}/TutorBrain?action=MPPReviewTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=review&comment=";
                else $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPReviewTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=review&comment=",reviewComplete(${ts.topicId}));
                <%--window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&topicId=${ts.topicId}&mode=review&elapsedTime='+updateElapsedTime()+'&learningChoice=true&learningCompanion=${learningCompanion}';--%>
            });

            $("#challenge_${ts.topicId}").click(function(){
                if (useHybridTutor)
                    window.location = "${pageContext.request.contextPath}/TutorBrain?action=MPPChallengeTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=challenge&comment=";
                else $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPChallengeTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=challenge&comment=", challengeComplete(${ts.topicId}));
                <%--window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&topicId=${ts.topicId}&mode=challenge&elapsedTime='+updateElapsedTime()+'&learningChoice=true&learningCompanion=${learningCompanion}';--%>
            });

            $("#tryThis_${ts.topicId}").click(function(){
                if (useHybridTutor)
                     window.location = "${pageContext.request.contextPath}/TutorBrain?action=MPPContinueTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=tryThis&comment=";

                else $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPContinueTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=tryThis&comment=",tryThisComplete(${ts.topicId}));
                <%--window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&topicId=${ts.topicId}&mode=continue&elapsedTime='+updateElapsedTime()+'&learningChoice=true&learningCompanion=${learningCompanion}';--%>
            });



        </c:forEach>

    }



        $(document).ready(function(){

            initiate();
            renderProgressPage();
            addComments();

            <%--$.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId=0&studentAction=loadMPP&comment=");--%>



            $("#searchlink").click(function(){
                $(".dropdown_contentBox").toggle();

            });

            $(".closeWindow").click(function(){
                this.parent().hide();
            });


            $("#submitCommentButton").click(function(){

                var comment = document.commentForm.commentTextArea.value;
                $.get("TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId="+currentTopicId+"&eventCounter=${eventCounter + 1}&studentAction=saveComment&comment="+comment);
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
                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId="+currentTopicId+"&studentAction=savePlantComment&comment="+comment);
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
                <%--window.location =  "${pageContext.request.contextPath}/mathspring.jsp";--%>
                if (useHybridTutor)
                    window.location =  "${pageContext.request.contextPath}/TutorBrain?action=MPPReturnToHut&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=" +currentTopicId+"&probId=" + currentProblemId +
                        "&studentAction=backToSatHut&elapsedTime="+elapsedTime+ "&probElapsedTime="+probElapsedTime+"&learningCompanion=${learningCompanion}";
                else
                    $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPReturnToHut&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId="+currentTopicId+"&studentAction=backToSatHut&comment=",returnToHutComplete);

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

                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=0&studentAction=savePerformanceUserChoice&comment="+userChoice);
                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=0&studentAction=savePerformanceComment&comment="+comment);

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

                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId=0&eventCounter=${eventCounter + 1}&studentAction=saveProgressUserChoice&comment="+userChoice);
                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId=0&eventCounter=${eventCounter + 1}&studentAction=saveProgressComment&comment="+comment);

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

                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId=0&eventCounter=${eventCounter + 1}&studentAction=saveRemarksUserChoice&comment="+userChoice);
                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId=0&eventCounter=${eventCounter + 1}&studentAction=saveRemarksComment&comment="+comment);

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


                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId=0&eventCounter=${eventCounter + 1}&studentAction=saveRemarksUserChoice&comment="+userChoice);
                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId=0&eventCounter=${eventCounter + 1}&studentAction=saveRemarksComment&comment="+comment);


                if (comment!="" || userChoice!="" ) {

                    var position = $("#masteryBarCommentHolder").position();

                    var tPosX = position.left;
                    var tPosY = position.top+40;

                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).fadeIn(800);
                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).delay(2000).fadeOut(800);

                }

                $("#masteryBarCommentHolder").fadeOut("slow").slideUp(300);


            });


        });


    </script>



    <!-- End JQuery and Scripts-->

</head>

<body >


<div class="gradient_bg">




    <!--end of the info>

 <header><!-- Start Header -->


    <!-- Start Header Middle --><div id="header_wrapper_small" ><!-- BEGIN HEADER MAIN -->
    <!--Start Logo--><header  id="wayang_logo_small">
        <img src="img/mathspring_logo.png" height="34" alt="MathSpring">
    </header>
<!--End Logo-->
    <subheader id="subheader_small">My Progress</subheader><!--End Logo-->

    <!-- Start Navigation -->
    <topNav class="topNav_small">
        <ul>


            <li id="backToVillage"> <a href="#"><b>&laquo; Go back to Tutor </b></a></li>
            <%--<li>${ts.userName}  &nbsp;|&nbsp;</li>--%>
        </ul>
        </topNav><!-- End Navigation -->

</div>




    </header><!-- END HEADER -->
    </section>
    <!-- END SECTION MAIN CONTENT -->



    <section class="clear">
    </section>
    <section class="normal_body">
        <div id="table_body">




            <table  class="progress_table">
                <thead>
                <tr>
                    <td>Topic</td>
                    <td width="120" align="center"><a href="#" id="Progress">Progress<img src="img/commentIcon.png"  height="14"></a></td>
                    <td width="300"><a href="#" id="Performance">Performance<img src="img/commentIcon.png"  height="14"></a>

                      </td>



                    <td><a href="#" id="Remarks">Remarks<img src="img/commentIcon.png"  height="14"></a></td>
                    <td >Actions</td>
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
                        <td class="topic"> ${topicName} </td>
                        <td align="center" valign="bottom"  class="plant_wrapper"> <a href="#" id=${plantLink}><div id=${plantDiv}></div></a></td>

                        <td class="performance">Mastery Level<a href="#" id="masteryBar_${topicId}"><div id=${masteryChartDiv}></div></a><div id=${problemsDiv}></div>
                            <a href="#" id="LearnMore" onclick="window.location='${pageContext.request.contextPath}/TutorBrain?action=TopicDetail&sessionId=${sessionId}&elapsedTime='+updateElapsedTime()+'&mastery=${ts.mastery}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&topicName=${topicName}&problemsDone=${ts.problemsDone}&totalProblems=${ts.totalProblems}'" class="littleLink"  >Learn More &gt;</a><!---<div id="problemCards"   rel="performanceDetails" style="height:70px;position:absolute;width:350px;" ><ul id="wrapperList"><li><table  ></table></li></ul></div> -->
                        </td>

                        <td valign="top" class="remarks"> <div id=${remarksDiv}  > </div> <a href="#" class="littleLink" id=${commentLink} ><img src="img/commentIcon.png"  height="14">Comment &gt;</a></td>
                        <td class="actionColumn"> <ul id="reviewChallenge">

                        <c:choose>
                            <c:when test="${ts.problemsDone>0 && ts.hasAvailableContent}">
                            <li id="continue_${topicId}"><a href="#" >Continue  &raquo;</a></li>
                            <li id="review_${topicId}"> <a href="#" >&laquo; Review</a></li>
                            <li id="challenge_${topicId}"><a href="#" ><span class="colorPink">Challenge</span></a></li></ul></td></tr>
                            </c:when>
                            <c:when test="${ts.problemsDone==0}"> <li id="tryThis_${topicId}"><a href="#" >Try this &raquo;</a></li></c:when>
                            <%--The tutor sometimes can't continue a topic if some criteria are satisfied, so we only offer review and challenge--%>
                            <c:otherwise> <li id="review_${topicId}"> <a href="#" >&laquo; Review</a></li>
                                <li id="challenge_${topicId}"><a href="#" ><span class="colorPink">Challenge</span></a></li></ul></td></tr></c:otherwise>
                        </c:choose>
                        </ul></td></tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </section>

    <footer><!-- BEGIN FOOTER -->
        &nbsp;

    </footer><!-- END FOOTER -->
    <!-- Free template distributed by

    http://freehtml5templates.com -->

</div>


<div class="commentLayer" id="commentHolder" >

    <form name="commentForm">
        <textarea class="commentBox" name="commentTextArea" id="commentText">
        </textarea>
        <br/>

        <button id="submitCommentButton" type="button" class="thoughtbot">Submit</button>
        <button id="cancelCommentButton" type="button" class="clean-gray">Cancel</button>

         </form>

</div>


<div class="plantCommentLayer" id="plantCommentHolder" >
    <div id="plantDetails"></div>

    <form name="plantCommentForm">
        <textarea class="plantCommentBox" name="commentTextArea" id="plantCommentText">
        </textarea>
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

        <textarea class="plantCommentBox" name="commentTextArea" >
        </textarea>
        <br/>

        <button id="submitMasteryBarCommentButton" type="button" class="thoughtbot">Submit</button>
        <button id="cancelMasteryBarCommentButton" type="button" class="clean-gray">Cancel</button>



    </form>

</div>

<div class="tableTopDropDown" id="performanceReadMore" >
    <div id="performanceReadMoreText">We are trying to calculate your mastery level from your performance.  <br/>
    Do you think that we are doing a good job in calculating your mastery level? <br/><br/></div>

    <form name="performanceFeedbackForm">

        Please select your response from this list:    <br/> <br/>

            <input type="radio"  name="userChoice" value="accurate">You are doing very good job of Mastery calculation.<br>
            <input type="radio"  name="userChoice" value="ok" >Mastery calculation is ok<br>
            <input type="radio"  name="userChoice" value="notAccurate" >You are doing poor job of Mastery calculation.

              <br/><br/>
        You can also leave your comment:

            <textarea class="plantCommentBox" name="commentTextArea" id="performanceCommentText">
            </textarea>

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

        <textarea class="plantCommentBox" name="commentTextArea" >
        </textarea>

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

        <textarea class="plantCommentBox" name="commentTextArea" >
        </textarea>

        <br/>


        <button id="submitRemarksFeedbackButton" type="button" class="thoughtbot">Submit</button>
        <button id="cancelRemarksFeedbackButton" type="button" class="clean-gray">Cancel</button>



    </form>

</div>

<div class="thankYou" id="commentAckLayer" >

Thank you! <br/>
    We have saved your input.
    </div>

</body>

</html>
