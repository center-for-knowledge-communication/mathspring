<%@ page import="edu.umass.ckc.wo.tutormeta.TopicMastery" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.umass.ckc.wo.smgr.SessionManager" %>

<%--
  Created by IntelliJ IDEA.
  User: Dovan
  Date: 2014
  Time: 5:22:15 PM
  To change this template use File | Settings | File Templates.
--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--@elvariable id="ss" type="edu.umass.ckc.wo.myprogress.StudentSummary"--%>
<%--@elvariable id="topicsList" type="java.util.List"--%>
<%--@elvariable id="TopicListMasteredInLastSession" type="java.util.List"--%>
<%--@elvariable id="dayDetailsList" type="java.util.List"--%>
<%--@elvariable id="dd" type="edu.umass.ckc.wo.myprogress.dayDetails"--%>


<html id="default">
<head>
    <meta charset="utf-8"/>
    <title>MathSpring </title>

    <link rel="stylesheet" href="css/wayang.css"/>
    <!-- Main Lar -->


    <!-- Start Jquery and Scripts -->

    <script language="javascript" type="text/javascript" src="js/jquery-1.10.2.js"></script>
    <script language="javascript" type="text/javascript" src="js/tree.js"></script>


    <script language="javascript" type="text/javascript">

        // These two vars are not initialized correctly in the initiate method below.   They appear to want them from URL params
        // but this JSP isn't getting URL params.   It is a pure forward done in servlet, so these values must be set as attributes
        // TODO find out if anyone is calling this JSP with URL params.  Perhaps this can be done in servlet forward??

        var teacherName = '${teacherName}';
        var studentFirstName = '${studentFirstName}';

        var studentLastName = '${studentLastName}';
        var currentTopicId =${topicId};
        var currentProblemId =${probId};


        var problemsSolved = '${problemsSolved}';
        var topicsDone = '${topicsDone}';
        var daysSinceLastSession = '${daysSinceLastSession}';
        var problemsDoneInLastSession = '${problemsDoneInLastSession}';
        var totalMasteredTopics = '${totalMasteredTopics}';
        var topicsMasteredInLastSession = '${topicsMasteredInLastSession}';
        var masteryThreshold = '${masteryThreshold}';


        var skillList = new Array();
        var skillMastery = new Array();

        var dayList = new Array();

        var TopicListMasteredInLastSession = new Array();

        var l = 0;

        <c:forEach var="tl" items="${topicsList}">


        skillList[l] = "${tl}";
        l++;

        </c:forEach>

        l = 0;

        <c:forEach  var="tml" items="${topicMasteryList}">


        skillMastery[l] = "${tml}";

        l++;

        </c:forEach>

        l = 0;

        <c:forEach var="tmls" items="${TopicListMasteredInLastSession}">


        TopicListMasteredInLastSession[l] = "${tmls}";
        l++;

        </c:forEach>

        var i = 0;
        var j;

        <c:forEach var="dd" items="${dayDetailsList}">

        dayList[i] = new Array();

        dayList[i][0] = "${dd.dayIndex}";
        dayList[i][1] = "${dd.day}";
        dayList[i][2] = "${dd.problemsSolved}";
        dayList[i][3] = "${dd.topicsDone}";

        //alert(dayList[i][3]);


        dayList[i][4] = new Array();
        dayList[i][5] = new Array();

        j = 0;
        <c:forEach var="topicName" items="${dd.topicsList}">


        dayList[i][4][j] = "${topicName}";

        j++;

        </c:forEach>

        var k = 0;
        <c:forEach var="mastery" items="${dd.topicMasteryList}">


        dayList[i][5][k] = "${topicMastery}";

        k++;

        </c:forEach>

        i++;
        </c:forEach>


        $(document).ready(function () {


            loadTreeParameters(problemsSolved, topicsDone);
            generateGreetings(studentFirstName, daysSinceLastSession, problemsSolved, topicsDone, problemsDoneInLastSession, totalMasteredTopics, topicsMasteredInLastSession);

            if (problemsSolved > 0) {
                generateDayHistoryButtons(dayList);
                dayListClickEvents();
            }

            else {
                drawSampleTree();
            }


        });


    </script>


    <!-- End JQuery and Scripts-->

</head>

<body>

<div class="gradient_bg">
    <header><!-- Start Header -->


        <!-- Start Header Middle -->
        <div id="header_wrapper"><!-- BEGIN HEADER MAIN -->

            <div class="container">
                <div id="primary">
                    <div id="slogan">
                        <!--Start Logo-->
                        <header id="header1">
                            <img src="img/mathspring_logo_new.png" alt="Math Spring">


                        </header>
                        <!--End Logo-->
                        <subheader id="subheader">Dashboard</subheader>
                        <!--End Logo-->


                        <!-- Start Navigation -->
                        <topNav>
                            <ul>


                                <li><a href="TutorBrain?action=Logout&sessionId=${sessionId}&elapsedTime=${elapsedTime}">Log Out</a></li>
                                <li>${studentFirstName}&nbsp;${studentLastName} &nbsp;|&nbsp;</li>
                            </ul>
                        </topNav>
                        <!-- End Navigation -->


                    </div>
                </div>
            </div>
        </div>


    </header>
    <!-- END HEADER -->


    <section class="colorGreen">
        <!-- BEGIN SECTION MAIN CONTENT -->


        <div class="container">
            <div class="main_content">

                <section class="clear">&nbsp;</section>

                <section class="left">

                    <canvas id="myCanvas" width="450" height="500"
                            style="border:0px solid #005900; padding:0px; margin:0px;">
                        Your browser does not support the HTML5 canvas tag.
                    </canvas>


                </section>


                <section class="right">
                    <div id="dashboard_nav_wrapper">

                        <ul id="dashboard_nav_big">

                            <li id="bigSize"><a href="#"
                                                onclick="window.location='TutorBrain?action=navigation&from=sat_Hut&to=my_progress&elapsedTime=0&sessionId=${sessionId}'+ '&eventCounter=${eventCounter}' + '&topicId=${topicId}&probId=${probId}&probElapsedTime=0'"><span
                                    class="colorGreen">My Progress</span></a></li>
                            <li>
                                <c:choose>
                                <c:when test="${newSession}">
                                <a href="#"
                                   onclick="window.location='TutorBrain?action=EnterTutor&sessionId=${sessionId}'+'&elapsedTime=${elapsedTime}' + '&eventCounter=0'"><span
                                    class="colorOrange"><b>Start Working</b></span></a>
                                </c:when>
                                <c:otherwise>
                                    <a href="#"
                                       onclick="window.location='TutorBrain?action=MPPReturnToHut&sessionId=${sessionId}'+'&elapsedTime=${elapsedTime}' + '&eventCounter=${eventCounter}' + '&probId=${probId}&topicId=${topicId}' + '&learningCompanion=${learningCompanion}'"><span
                                            class="colorOrange"><b>Resume tutoring</b></span></a>
                                </c:otherwise>
                                </c:choose>

                            </li>
                        </ul>

                    </div>


                    </select>
                    <section class="clear">&nbsp;</section>

                    <div id="greetings">
                        <div id="greetingsText">

                        </div>

                        <div id="dayListDiv">
                            <div id="dayDetailsText"></div>
                            <ul id="dayList_ul"></ul>


                        </div>

                        <div id="finalWords"></div>

                    </div>


                </section>
            </div>
        </div>
    </section>

    </section>
    <!-- END SECTION MAIN CONTENT -->


    <section class="clear"></section>

    <footer><!-- BEGIN FOOTER -->


    </footer>
    <!-- END FOOTER -->
    <!-- Free template distributed by

   http://freehtml5templates.com -->

</div>


</body>

</html>