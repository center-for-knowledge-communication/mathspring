<%--
  Created by IntelliJ IDEA.
  User: marshall
  Date: 7/16/12
  Time: 2:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head><title>Wayang Outpost External Activity</title>
    <link type="text/css" href="css/south-street/jquery-ui-1.8.22.custom.css" rel="stylesheet" />
    <script type="text/javascript" src="js/jquery-1.7.2.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.22.custom.min.js"></script>
    <script type="text/javascript">

        var elapsedTime=${elapsedTime};
        var lastProbType=null;
        var probElapsedTime=0;
        var clock=0;
        var lastProbId=${probId};
        var debug=false;
        var topicId=${topicId};

        function debugAlert (msg) {
            if (debug) {
                alert(msg);
            }
        }

        // don't send an endExternalActivity if the last prob was 4mality.  4mality sends its own begin/ends
        function sendEndEvent () {
            if (lastProbType != '4Mality') {
                $.post("${pageContext.request.contextPath}/TutorBrain?action=EndExternalActivity&sessionId=${sessionId}&elapsedTime="
                        +elapsedTime+ "&probElapsedTime="+probElapsedTime+"&xactId=" + lastProbId);
            }
            else {
                $.post("${pageContext.request.contextPath}/TutorBrain?action=FormalityEndProblem&sessionId=${sessionId}&elapsedTime="
                        +elapsedTime+ "&probElapsedTime="+probElapsedTime+"&probId=" + lastProbId);
            }
        }


        function processNextProblemResult(responseText, textStatus, XMLHttpRequest) {
            debugAlert("Server returns " + responseText);
            var re = new RegExp("<activity>(.)*</activity>");
            var m = re.exec(responseText);
            if (m == null) {
                debugAlert("no match");
            }
            else {
                var activityXML = m[0];
                debugAlert("activity is: " + activityXML);
                var dom = $.parseXML(activityXML),
                        $xml = $(dom),
                        $prob = $xml.find("problemURL"),
                        $xact = $xml.find("externalURL"),
                        $topicIntro = $xml.find("topicIntro"),
                        $instr = $xml.find("instructions");

                var instructions = $instr.text();
                $("#instructionsP").text(instructions);
                var now = new Date().getTime();
                probElapsedTime += now - clock;
                elapsedTime += now - clock;
                clock = now;

                // got back XML that indicates its an external problem
                if ($xact.length > 0) {
                    var pid = $xact.attr("id");
                    var resource = $xact.text();
                    debugAlert("Its an external problem.   Changing iframe src attribute to " + resource);
                    // Don't send an endExternalActivity if the last prob was 4mality.  They send their own begin/ends
                    sendEndEvent();
                    probElapsedTime = 0;
                    $.post("${pageContext.request.contextPath}/TutorBrain?action=BeginExternalActivity&sessionId=${sessionId}&elapsedTime="
                        +elapsedTime+ "&probElapsedTime="+probElapsedTime+"&xactId=" + pid);
                    lastProbId= pid;
                    lastProbType='External';
                     $("#iframe").attr("src",resource);

                }
                else if ($prob.length > 0) {
                    var mode = $prob.attr("mode");

                    if (mode == "4Mality") {
                        var pid = $prob.attr("id");
                        var resource = $prob.text();
                        debugAlert("Its a 4mality problem.  URL is " + resource);
                        sendEndEvent();
                        probElapsedTime = 0;
                        // formality problems call the servlet with their own begin /end events
                        lastProbId=pid;
                        lastProbType='4Mality';
                        // Change the src of the iframe to be the Formality page.  The user ID for 4mality doesn't matter as long as it exists
                        $("#iframe").attr("src","${formalityServlet}?fxn=wayang&mode=viewq&qID="
                                +resource+ "&un=1864&wayangProbId="+ pid +
                                "&wayangStudId=${studId}&wayangSessId=${sessionId}&elapsedTime="+elapsedTime
                                + "&wayangServletContext="+'${wayangServletContext}');

                    }
                    else {
                        var pid = $prob.attr("id");
                        var resource = $prob.text();

                        debugAlert("its a flash problem.   We need to call WoLogin or something");
                        // send an END for the first xAct
                        debugAlert("Calling servlet with EndExternalActivity");
                        // don't send an endExternalActivity if the last prob was 4mality bc 4mality sends its own begin/ends
                        sendEndEvent();
                        lastProbType='Flash';
                        // not sure what the other params are for flash to show a problem in the tutor hut
                        <%--document.location.href = "${flashClientPath}?sessnum=${sessionId}&learningHutChoice=true&elapsedTime=" +elapsedTime+ "&learningCompanion=${learningCompanion}&force_first_problem=1&which_first_problem=" + resource;--%>
                        document.location.href = "${flashClientPath}?sessnum=${sessionId}&sessionId=${sessionId}&learningHutChoice=true&elapsedTime=" +elapsedTime+ "&learningCompanion=${learningCompanion}&problem_name=" + resource+ "&mode=practice"; // &topicId=" + topicId;
                    }
                }
                else if ($topicIntro.length > 0) {
                    var resource = $topicIntro.find("resource").text();
                    sendEndEvent();
                    lastProbType='Flash';
                    document.location.href = "${flashClientPath}?sessnum=${sessionId}&sessionId=${sessionId}&learningHutChoice=true&elapsedTime=" +elapsedTime+ "&learningCompanion=${learningCompanion}&problem_name=" + resource + "&mode=practice"; //&topicId=" + topicId;
                }
                // We got XML that we don't understand so it must be an intervention.   We call Flash and pass it the XML
                else {
                    sendEndEvent();
                    lastProbType='Flash';
                    debugAlert('Calling Flash for intervention XML: ' + activityXML);
                    <%--document.location.href = "${flashClientPath}?sessnum=${sessionId}&sessionId=${sessionId}&learningHutChoice=true&elapsedTime=" +elapsedTime+ "&learningCompanion=${learningCompanion}&intervention=" + "ThisIsAnInterventionButItsNotXML" + "&mode=intervention"; // &topicId=" + topicId;--%>
                    document.location.href = "${flashClientPath}?sessnum=${sessionId}&sessionId=${sessionId}&learningHutChoice=true&elapsedTime=" +elapsedTime+ "&learningCompanion=${learningCompanion}&intervention=" + encodeURIComponent(activityXML) + "&mode=intervention"; // &topicId=" + topicId;
                    <%--document.location.href = "${flashClientPath}?sessnum=${sessionId}&sessionId=${sessionId}&learningHutChoice=true&elapsedTime=" +elapsedTime+ "&learningCompanion=${learningCompanion}&topicId=" + topicId+"&mode=continue";--%>

                }

            }
        }

        // Unfortunately the back button will run this function too which means that it can generate a BeginExternalActivity
        $(document).ready(function() {
            var d =new Date();
            var startTime = d.getTime();

            $("#nextProb").click(function () {
                // get the id of the next problem from the input box

                d = new Date();
                var now = d.getTime();
                probElapsedTime += now-clock;
                clock=now;
                elapsedTime +=  probElapsedTime;
                // For debugging we need to uncomment the input box that allows us to type in the id of the next problem we wish
                // the server to return when we click "Next"
                if (debug) {
                    var nextProbId = $("#probId").val();
                    var wantExternal = $("#checkbox").prop('checked');
                     $.get("${pageContext.request.contextPath}/TutorBrain?action=NextProblem&sessionId=${sessionId}&elapsedTime="
                        +elapsedTime+ "&probElapsedTime=" +probElapsedTime+ "&probID=" + nextProbId + "&isExternal="+wantExternal,
                        processNextProblemResult);
                }
                else {
                    // call the server with a nextProblem event and the callback fn processNextProblemResult will deal with result
                    $.get("${pageContext.request.contextPath}/TutorBrain?action=NextProblem&sessionId=${sessionId}&elapsedTime="
                        +elapsedTime+ "&probElapsedTime=" +probElapsedTime, processNextProblemResult);
                }

            });

            $('#dialog').dialog({
					autoOpen: true,
					width: 600,
					buttons: {
						"Ok": function() {
							$(this).dialog("close");
						},
						"Cancel": function() {
							$(this).dialog("close");
						}
					}
				});
            $("#instructions").click(function () {
                // probably want something slicker than this alert dialog.
                <%--alert("Instructions: ${instructions}");--%>
                $("#dialog").dialog('open');
                return false;
            });

            $("#myProg").click(function () {
                d = new Date();
                var now = d.getTime();
                probElapsedTime += now-startTime;
                elapsedTime += probElapsedTime;
                document.location.href = "${pageContext.request.contextPath}/TutorBrain?action=navigation&sessionId=${sessionId}&elapsedTime=" +elapsedTime+ "&from=sat_hut&to=my_progress";

            });
            d =new Date();
            clock = d.getTime();
            var loadTime =  clock - startTime;
            elapsedTime += loadTime;
            probElapsedTime = loadTime;
            lastProbType = '${probType}';

            // send a BEGIN for the xAct if its not a 4mality prob.   4mality sends its own begin event
            if (lastProbType != '4Mality') {
                $.get("${pageContext.request.contextPath}/TutorBrain?action=BeginExternalActivity&sessionId=${sessionId}&elapsedTime="+elapsedTime+"&probElapsedTime="+probElapsedTime+"&xactId=${probId}");
            }
        });


    </script>


</head>
<body>

<div id="main">
    <iframe id="iframe" src="${externalActivityURL}" style="width:100%; height:80%;">

    </iframe>
</div>

<div id="dialog" title="Instructions">
    <p id="instructionsP">${instructions}</p>
</div>
<img src="images/navlog.jpg" usemap="#navbuttons">
<map name="navbuttons">
    <area id="nextProb" shape="rect" coords="156,40,304,72" >
    <area id="instructions" shape="rect" coords="451,40,590,72">
    <area id="myProg" shape="rect" coords="747,40,889,72">
</map>
<%--<button id="nextProb" name="nextProb" value="Next Problem">New Problem</button>--%>
<%--<button id="instructions" name="instructions" value="Instructions">Instructions</button>--%>
<%--<button id="myProg" name="myprog" value="My Progress">My Progress</button>--%>
<%--Uncomment the next two lines for debugging--%>
<%--<input id="probId" type="text"/>--%>
<%--&nbsp; External Problem: <input id="checkbox" type="checkbox"/>--%>

</body>
</html>