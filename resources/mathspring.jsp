<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 2/6/13
  Time: 9:49 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <title>UMass Math Tutor</title>
    <link href="css/wanDutBG.css" rel="stylesheet" type="text/css">
    <link href="css/menu.css" rel="stylesheet" type="text/css">
    <link href="css/navlog.css" rel="stylesheet" type="text/css">
    <link href="css/wayang.css" rel="stylesheet" type="text/css">
    <link href="css/button.css" rel="stylesheet" type="text/css">
    <link href="css/simple-slider.css" rel="stylesheet" type="text/css" />

    <link href="css/tutorhut.css" rel="stylesheet" type="text/css">
    <%--<link href="css/ui-lightness/jquery-ui-1.10.3.custom.css" rel="stylesheet">--%>
    <link href="js/jquery-ui-1.10.4.custom/css/spring/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">

    <!-- css for data table -->
    <link href="https://cdn.datatables.net/1.10.13/css/dataTables.bootstrap4.min.css" rel="stylesheet" type="text/css">
    <link href="https://cdn.datatables.net/colreorder/1.3.2/css/colReorder.bootstrap4.min.css" rel="stylesheet" type="text/css">

    <!-- css for bootstrap / Font Awesome -->
    <link rel="stylesheet" href="<c:url value="/js/bootstrap/css/bootstrap.min.css" />" />
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.2/css/bootstrap-select.min.css">

    <!-- updated Jquery to 2.2.2 to make use of bootstrap js-->
    <script type="text/javascript" src="<c:url value="/js/bootstrap/js/jquery-2.2.2.min.js" />"></script>
    <!-- js for bootstrap-->
    <script type="text/javascript" src="<c:url value="/js/bootstrap/js/bootstrap.min.js" />"></script>

    <%--<script src="js/jquery-ui-1.10.3/ui/jquery-ui.js"></script>--%>
    <script src="js/jquery-ui-1.10.4.custom/js/jquery-ui-1.10.4.custom.min.js"></script>
    <script src="js/jquery.dialogextend.min.js"></script>
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.2/js/bootstrap-select.min.js"></script>


    <!-- js for data table -->
    <script type="text/javascript" src="<c:url value="https://cdn.datatables.net/1.10.13/js/jquery.dataTables.min.js" />"></script>
    <script type="text/javascript" src="<c:url value="https://cdn.datatables.net/1.10.13/js/dataTables.bootstrap4.min.js" />"></script>
    <script type="text/javascript" src="<c:url value="https://cdn.datatables.net/colreorder/1.3.2/js/dataTables.colReorder.min.js" />"></script>
    <!-- js for bootstrap-->

    <script type="text/javascript" src="js/simple-slider.js"></script>
    <script type="text/javascript" src="js/tutorutils.js"></script>
    <script type="text/javascript" src="js/tutorAnswer.js"></script>
    <script type="text/javascript" src="js/tutorhint.js"></script>
    <script type="text/javascript" src="js/tutorhut.js"></script>
    <script type="text/javascript" src="js/devdialog.js"></script>
    <script type="text/javascript" src="js/tutorintervention.js"></script>
    <script type="text/javascript" src="js/intervhandlers.js"></script>
    <script type="text/javascript" src="js/swfobject.js"></script>
    <script type="text/javascript">
        var globals = {
            lastProbType: '${lastProbType}',
            isBeginningOfSession: ${isBeginningOfSession},
            sessionId: ${sessionId},
            elapsedTime: ${elapsedTime},
            probElapsedTime: 0,
            clock: 0,
            curHint: null,
            exampleCurHint: null,
            hintSequence: null,
            exampleHintSequence: null,
            lastProbId: ${lastProbId},
            trace: false,
            debug: false,
            topicId: ${topicId},
            guiLock: false,
            learningCompanion: '${learningCompanion}',
            learningCompanionMessageSelectionStrategy: '${learningCompanionMessageSelectionStrategy}',
            userName: '${userName}',
            studId : ${studId} ,
            probType : '${probType}',
            exampleProbType : null,
            probId : ${probId},
            probMode: '${probMode}',
            tutoringMode: '${tutoringMode}',
            instructions : '${instructions}',
            resource : '${resource}',
            form : '${form}',
            answer : '${answer}',
            interventionType: null,
            isInputIntervention: false ,
            learningCompanionClip: null,
            activityJSON: ${activityJSON},
            showMPP: ${showMPP},
            units: null,
            <%--The fields below turn on things for test users --%>
            showSelectProblemButton: ${showProblemSelector},
            showAnswer: ${showAnswer},
            newAnswer: null,
            params: null ,
            resumeProblem: ${resumeProblem},
            statementHTML: null,
            questionAudio: null,
            questionImage: null,
            hints: null,
            answers: null ,
            numHintsSeen: 0,
            numHints: 0 ,
            destinationInterventionSelector: null ,
            clickTime: 0

        }

        var sysGlobals = {
            isDevEnv: ${isDevEnv},
            wayangServletContext: '${wayangServletContext}',
            problemContentDomain : '${problemContentDomain}',
            problemContentPath : '${problemContentPath}',
            webContentPath : '${webContentPath}',
            <%--servletContextPath : '${pageContext.request.contextPath}',--%>
            servletName : '${servletName}',
            probplayerPath : '${probplayerPath}',
            wait: false,
            eventCounter: ${eventCounter},
            soundSync: ${soundSync} ,
            huygui: false

        }

        var transients = {
            answerChoice: null  ,
            selectedButton: null ,
            answersChosenSoFar: [] ,
            sym: null,
            component: null,
            componentAction: null,
            learningCompanionTextMessage: null
        }


        // Unfortunately the back button will run this function too which means that it can generate a BeginExternalActivity
        $(document).ready(
            function () {
                tutorhut_main(globals,sysGlobals,transients, "${learningCompanionMovie}");
                generateHighlightRuleDialog();
            }
        );
    </script>


    <style type="text/css">


        .detailInfo a {
            display: block;
            height: 20px;
            color: black;
            background-repeat: no-repeat;
            background-position: left top;
            padding-left: 0px;
        }
        .detailInfo a:hover {
            background-repeat: no-repeat;
            background-position: left top;

            text-decoration: none;  !important;
        }

        .leftcol {
            padding: 8px;
            float: right;
            width: 1000px;
        }

        .empty {
        }

        .lcdialogTitleBar {
            background-color: transparent;
            border: 0px none;
        }

        .scrolledTable{ overflow-y: auto; clear:both; }

        fieldset.scheduler-border {
            border: 1px groove #ddd !important;
            padding: 0 1.4em 1.4em 1.4em !important;
            margin: 0 0 1.5em 0 !important;
            -webkit-box-shadow:  0px 0px 0px 0px #000;
            box-shadow:  0px 0px 0px 0px #000;
        }

        .modal {
        }
        .vertical-alignment-helper {
            display:table;
            height: 100%;
            width: 100%;
        }
        .vertical-align-center {
            /* To center vertically */
            display: table-cell;
            vertical-align: middle;
        }
        .modal-content {
            /* Bootstrap sets the size of the modal in the modal-dialog class, we need to inherit it */
            width:inherit;
            height:inherit;
            /* To center horizontally */
            margin: 0 auto;
        }
        tr.highlight {
            background-color:red !important;
        }


    </style>
</head>

<body>
<audio id="beeper" src="http://www.soundjay.com/button/beep-07.wav" autostart="false" ></audio>


<audio id='questionaudio' name='questionaudio'><source id='questionogg' src='' type='audio/ogg'><source id='questionmp3' src='' type='audio/mpeg'>Your browser does not support the audio element.</audio>
<%-- This div is a dialog that is shown when the user clicks on Show Example.  It plays an example problem in the dialog--%>
<div id="exampleContainer" width="600" height="600" title="Watch/listen to this example. Use 'Play Next Step' to move along" >
    <%-- This iframe gets replaced by swfobject.embed.   It replaces it with the Flash object/embed tags for showing a problem OR an the html
     of an HTML5 problem (perhaps in an iframe if we must)--%>
    <iframe id="exampleFrame"
            name="iframe2"
            width="600"
            height="600"
            src=""
            frameborder="no"
            scrolling="no">
    </iframe>
</div>

<div id="utilDialog" title="">
    <iframe id="utilDialogIframe" width="675" height="675"> </iframe>
</div>

<div id="interventionDialog" title="">
    <div id="interventionDialogContent"></div>

</div>

<div id="selectProblemDialog" title="Select Problem">
    <iframe id="selectProblemDialogIframe" width="500" height="500"></iframe>
</div>


<div class="wrapper">
    <div class="container"><img src="img/header3.png" width="1200" height="106" alt="mathematics">
        <div id="userDisplay" style="position:absolute; top:10px; right:10px; width:300px; height:50px; ">Logged in as:</div>

    </div>
    <div class="container">
        <div class="content">
            <div>
                <ul class="buttons">
                    <li class="prob1">
                        <a id="nextProb" href="#" class="button">New Problem</a>
                    </li>
                    <li class="inst2">
                        <a id="instructions" href="#" class="button">Instructions</a>
                    </li>
                    <li id="mppButton" class="inst2">
                        <a id="myProg" href="#" class="button">My Progress</a>
                    </li>
                    <c:if test="${showProblemSelector}">
                        <li id="selectProblemButton" class="inst2">
                            <a id="selectProb" href="#" class="button">Select Problem</a>
                        </li>

                        <li id="selectProblemButton" class="inst2">
                            <a id="getEventLogs" href="#" class="button">View Event Log</a>
                        </li>

                    </c:if>

                </ul>
            </div>

            <div class="leftCol">
                <div class="mntop">
                    Help
                </div>
                <ul class="mnCNT">
                    <li class="elb_read">
                        <a id="read" href="#">Read Problem</a>
                    </li>
                    <li class="elb_hint">
                        <a id="hint" href="#">Hint</a>
                    </li>
                    <li class="elb_replay">
                        <a id="replay" href="#">Replay Hint</a>
                    </li>
                    <%--<li class="elb_solve">--%>
                    <%--<a id="solve" href="#">Show Answer</a>--%>
                    <%--</li>--%>
                    <%--<li class="elb_solve">--%>
                    <%--<a id="solve" href="#">Solve Problem</a>--%>
                    <%--</li>--%>
                </ul>
                <br>
                <div class="mntop">
                    Resources
                </div>
                <ul class="mnCNT">
                    <li class="elb_show">
                        <a id="example" href="#">Show Example</a>
                    </li>
                    <li class="elb_vid">
                        <a id="video" href="#">Show Video</a>
                    </li>
                    <li class="elb_formulas">
                        <a id="formulas" href="#">Formulas</a>
                    </li>
                    <li class="elb_glossary">
                        <a id="glossary" href="#">Glossary</a>
                    </li>
                </ul>
                <br>
                <div class="mntop">
                    Places
                </div>
                <ul class="mnCNT">
                    <li class="elb_mpp">
                        <a id="prefs" href="#">Preferences</a>
                    </li>
                    <li class="elb_home">
                        <a id="home" href="#">Home</a>
                    </li>
                    <li class="detailInfo">
                        <a id="pid">${probId}</a>
                    </li>
                    <li class="detailInfo">
                        <a id="clock"></a>
                    </li>

                    <li class="elb_pid">
                        <div id="effort">${effort}</div>
                    </li>
                    <%-- Answer field is only filled for test users to show the correct answer--%>
                    <li class="elb_pid">
                        <div id="answer">${globals.answer}</div>
                    </li>
                </ul>
                <p>&nbsp;

                </p>
            </div>

            <%-- This div contains information about the current problem (its topic and standard)--%>
            <div id="problemTopicAndStandards" style="position:absolute; top:650px; right:400px">Topic:<br/>Standards:</div>
            <%-- Only shown to test users--%>
            <div id="varBindings" style="position:absolute; top:690px; left:200px"></div>

            <div id="flashContainer1" >
                <div id="flashContainer2"></div>
            </div>

            <div id="frameContainer" class="problemDiv">
                <iframe id="problemWindow" class="probWindow"
                        name="iframe1"
                        width="600"
                        height="600"
                        src="${activityURL}"
                        frameborder="no"
                        scrolling="no">
                </iframe>
            </div>


            <div  id="learningCompanionContainer">
                <iframe id="learningCompanionWindow"
                        name="lciframe"
                        width="258"
                        height="588"
                        src="${learningCompanionMovie}"
                        frameborder="yes"
                        scrolling="no"
                        onload="lcLoaded(this)"
                >
                </iframe>
            </div>




            <%--<iframe id="iframe" name="iframe1" width="1000" height="600" src="http://cadmium.cs.umass.edu/wayang2/flash/Problems/AW_001/AdditionDecimals.swf" frameborder="yes" scrolling="yes"> </iframe>--%>

            <!-- end .content -->
        </div>
        <!-- end .container -->
    </div>
    <div id="eventLogWindow" title="Event Logs" style="display:none;">
        <div class = "containers">
            <div class="panel-group" id="accordion">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                Event Log
                            </a>
                        </h4>
                    </div>
                    <div id="collapseOne" class="panel-collapse collapse in">
                        <div class="panel-body">
                            <div class='scrolledTable'>

                                <fieldset class="scheduler-border">
                                    <legend>Add/Remove Columns</legend>

                                    <a type="button" class="btn btn-default toggle-vis" data-column="0">
                                        <span class="glyphicon glyphicon-remove"></span> id
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="1">
                                        <span class="glyphicon glyphicon-remove"></span> studId
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="2">
                                        <span class="glyphicon glyphicon-remove"></span> sessNum
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="3">
                                        <span class="glyphicon glyphicon-remove"></span> action
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="4">
                                        <span class="glyphicon glyphicon-remove"></span> userInput
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="5">
                                        <span class="glyphicon glyphicon-remove"></span> isCorrect
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="6">
                                        <span class="glyphicon glyphicon-remove"></span> elapsedTime
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="7">
                                        <span class="glyphicon glyphicon-remove"></span> probElapsed
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="8">
                                        <span class="glyphicon glyphicon-remove"></span> problemId
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="9">
                                        <span class="glyphicon glyphicon-remove"></span> hintStep
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="10">
                                        <span class="glyphicon glyphicon-remove"></span> hintId
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="11">
                                        <span class="glyphicon glyphicon-remove"></span> emotion
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="12">
                                        <span class="glyphicon glyphicon-remove"></span> activityName
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis"data-column="13">
                                        <span class="glyphicon glyphicon-remove"></span> auxId
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="14">
                                        <span class="glyphicon glyphicon-remove"></span> auxTable
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="15">
                                        <span class="glyphicon glyphicon-remove"></span> time
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="16">
                                        <span class="glyphicon glyphicon-remove"></span> curTopicId
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="17">
                                        <span class="glyphicon glyphicon-remove"></span> testerNote
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="18">
                                        <span class="glyphicon glyphicon-remove"></span> clickTime
                                    </a>
                                </fieldset>
                                <fieldset class="scheduler-border">
                                    <legend>Hightlight Rule Editor</legend>
                                    <div class="form-group">
                                        <div class="row">
                                            <button class="btn btn-success" role="button" id="newHighlightRule_eventLog" aria-label="Add new hightlightRule">
                                                <i class="fa fa-plus" aria-hidden="true"></i>&nbsp;Add new Hightlight Rule
                                            </button>
                                        </div>
                                    </div>
                                    <div id="newHighlightRulecontainer_eventLog"></div>
                                </fieldset>

                                <table id="eventLogTable" class="table table-striped table-bordered" cellspacing="0" width="100%">
                                    <thead><tr>
                                        <th class="details-control">Make a Note</th>
                                        <th>id</th>
                                        <th>studId</th>
                                        <th>sessNum</th>
                                        <th>action</th>
                                        <th>userInput</th>
                                        <th>isCorrect</th>
                                        <th>elapsedTime</th>
                                        <th>probElapsed</th>
                                        <th>problemId</th>
                                        <th>hintStep</th>
                                        <th>hintId</th>
                                        <th>emotion</th>
                                        <th>activityName</th>
                                        <th>auxId</th>
                                        <th>auxTable</th>
                                        <th>time</th>
                                        <th>curTopicId</th>
                                        <th>testerNote</th>
                                        <th>clickTime</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                </div>

                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">
                                Student Problem History Log
                            </a>
                        </h4>
                    </div>
                    <div id="collapseTwo" class="accordion-body collapse">
                        <div class="panel-body">
                            <div class='scrolledTable'>

                                <fieldset class="scheduler-border">
                                    <legend>Add/Remove Columns</legend>

                                    <a type="button" class="btn btn-default toggle-vis" data-column="0">
                                        <span class="glyphicon glyphicon-remove"></span> id
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="1">
                                        <span class="glyphicon glyphicon-remove"></span> studId
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="2">
                                        <span class="glyphicon glyphicon-remove"></span> sessionId
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="8">
                                        <span class="glyphicon glyphicon-remove"></span> problemId
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="3">
                                        <span class="glyphicon glyphicon-remove"></span> topicId
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="4">
                                        <span class="glyphicon glyphicon-remove"></span> problemBeginTime
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="5">
                                        <span class="glyphicon glyphicon-remove"></span> problemEndTime
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="6">
                                        <span class="glyphicon glyphicon-remove"></span> timeInSession
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="7">
                                        <span class="glyphicon glyphicon-remove"></span> timeInTutor
                                    </a>

                                    <a type="button" class="btn btn-default toggle-vis" data-column="9">
                                        <span class="glyphicon glyphicon-remove"></span> timeToFirstAttempt
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="10">
                                        <span class="glyphicon glyphicon-remove"></span> timeToFirstHint
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="11">
                                        <span class="glyphicon glyphicon-remove"></span> timeToSolve
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="12">
                                        <span class="glyphicon glyphicon-remove"></span> numMistakes
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis"data-column="13">
                                        <span class="glyphicon glyphicon-remove"></span> numHints
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="14">
                                        <span class="glyphicon glyphicon-remove"></span> videoSeen
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="15">
                                        <span class="glyphicon glyphicon-remove"></span> numAttemptsToSolve
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="16">
                                        <span class="glyphicon glyphicon-remove"></span> solutionHintGiven
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="17">
                                        <span class="glyphicon glyphicon-remove"></span> mode
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="18">
                                        <span class="glyphicon glyphicon-remove"></span> mastery
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="18">
                                        <span class="glyphicon glyphicon-remove"></span> emotionAfter
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="18">
                                        <span class="glyphicon glyphicon-remove"></span> emotionLevel
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="18">
                                        <span class="glyphicon glyphicon-remove"></span> effort
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="18">
                                        <span class="glyphicon glyphicon-remove"></span> exampleSeen
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="18">
                                        <span class="glyphicon glyphicon-remove"></span> textReaderUsed
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="18">
                                        <span class="glyphicon glyphicon-remove"></span> numHintsBeforeSolve
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="18">
                                        <span class="glyphicon glyphicon-remove"></span> isSolved
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="18">
                                        <span class="glyphicon glyphicon-remove"></span> adminFlag
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="18">
                                        <span class="glyphicon glyphicon-remove"></span> authorFlag
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="18">
                                        <span class="glyphicon glyphicon-remove"></span> collaboratedWith
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="18">
                                        <span class="glyphicon glyphicon-remove"></span> timeToSecondAttempt
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="18">
                                        <span class="glyphicon glyphicon-remove"></span> timeToThirdAttempt
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="18">
                                        <span class="glyphicon glyphicon-remove"></span> timeToSecondHint
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="18">
                                        <span class="glyphicon glyphicon-remove"></span> timeToThirdHint
                                    </a>
                                    <a type="button" class="btn btn-default toggle-vis" data-column="18">
                                        <span class="glyphicon glyphicon-remove"></span> probDiff
                                    </a>
                                </fieldset>
                                <fieldset class="scheduler-border">
                                    <legend>Hightlight Rule Editor</legend>
                                    <div class="form-group">
                                        <div class="row">
                                            <a class="btn btn-success" role="button" id="newHighlightRule_studentProblemHistoryLog" aria-label="Add new hightlightRule">
                                                <i class="fa fa-plus" aria-hidden="true"></i>&nbsp;Add new Hightlight Rule
                                            </a>
                                        </div>
                                    </div>
                                    <div id="newHighlightRulecontainer_studentProblemHistoryLog"></div>
                                </fieldset>

                                <table id="studentProblemHistoryTable" class="table table-striped table-bordered" cellspacing="0" width="100%">
                                    <thead><tr>
                                        <th class="details-control">Make a Note</th>
                                        <th>id</th>
                                        <th>studId</th>
                                        <th>sessionId</th>
                                        <th>problemId</th>
                                        <th>topicId</th>
                                        <th>problemBeginTime</th>
                                        <th>problemEndTime</th>
                                        <th>timeInSession</th>
                                        <th>timeInTutor</th>
                                        <th>timeToFirstAttempt</th>
                                        <th>timeToFirstHint</th>
                                        <th>timeToSolve</th>
                                        <th>numMistakes</th>
                                        <th>numHints</th>
                                        <th>videoSeen</th>
                                        <th>numAttemptsToSolve</th>
                                        <th>solutionHintGiven</th>
                                        <th>mode</th>
                                        <th>mastery</th>
                                        <th>emotionAfter</th>
                                        <th>emotionLevel</th>
                                        <th>effort</th>
                                        <th>exampleSeen</th>
                                        <th>textReaderUsed</th>
                                        <th>numHintsBeforeSolve</th>
                                        <th>isSolved</th>
                                        <th>adminFlag</th>
                                        <th>authorFlag</th>
                                        <th>collaboratedWith</th>
                                        <th>timeToSecondAttempt</th>
                                        <th>timeToThirdAttempt</th>
                                        <th>timeToSecondHint</th>
                                        <th>timeToThirdHint</th>
                                        <th>probDiff</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>


                            </div>
                        </div>
                    </div>
                </div>


            </div>
        </div>
    </div>

    <!-- Modal Start here-->
    <div class="modal fade bs-example-modal-sm" id="myPleaseWait" tabindex="-1"
         role="dialog" aria-hidden="true" data-backdrop="static">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">
							<span class="glyphicon glyphicon-time">
							</span>Please Wait
                    </h4>
                </div>
                <div class="modal-body">
                    <div class="progress">
                        <div class="progress-bar progress-bar-info
							progress-bar-striped active"
                             style="width: 100%">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- Modal ends Here -->
    <!--<div class="container">
    -->
    <div style="z-index:100;" id="instructionsDialog" title="Instructions">
        <p id="instructionsP">${instructions}</p>

        <div class="empty"></div>
    </div>

    <!-- end .container -->
    <!--</div>-->
    <!-- end .content -->
</div>

<%--Old code here--%>

</body>
</html>
