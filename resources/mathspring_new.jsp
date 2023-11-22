<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>
<% 

// Frank 10-22-19 Issue # 9 Add Enable Flash message at bottom of screen
// Frank 04-24-20 Issue # 16 Added Multi-lingual text for tutorhut_new.js
// Frank 10-07-20 Issue # 261 change problem heading
// Frank 12-11-20 Issue #315 default locale to en_US
// Frank 12-18-20 Issue #336 added cache-busting for selected .js and .css files
// Frank 12-26-20 Issue #329 added lang variable for tutorhut_new.jsp to access current language for topic name
// Frank 03-01-21 Issue #399 and #400 remove Flash message from problem window
// Kartik 04-22-21 Issue #390 Added session clock functionality
// Frank	06-29-21 Added gaze functionality

ResourceBundle versions = null; 
try {
	 versions = ResourceBundle.getBundle("Versions");
}
catch (Exception e) {
	 System.out.println("teacherToolsMain ERROR");	 
}

Locale loc = request.getLocale(); 
Locale loc1 = request.getLocale(); 
Locale loc2 = request.getLocale();



int pageLangIndex = 0;

try {
	pageLangIndex = (int) request.getAttribute("pageLangIndex");
}
catch (Exception e) {
	 System.out.println("pageLangIndex " + e.getMessage());
	 pageLangIndex = 0;
}

String pageLang = (String) request.getAttribute("pageLang");

String prob_lang = "en";
int probLangIndex = 0;
prob_lang = "en";

/*
int testProbLangIndex = (int) request.getAttribute("probLangIndex");

if (testProbLangIndex == null) {
	System.out.println("Missing request param: probLangIndex ");
}
else {
	if (probLangIndex > 0) {
		prob_lang = "es";
	}
	else {
		prob_lang = "en";
	}
}
*/		

String lang = loc.getLanguage();
String country = loc.getCountry();

if ((pageLang != null) && pageLang.length() == 2) {
	lang = pageLang;	
}

System.out.println("locale set to:" + lang + "-" + country );	

String next_problem = "";
String no_translation = "";
String translate_this_problem = "";
String translate_to_spanish = "";
String translate_to_english = "";
String problem_current_topic = "";
String problem_standards = "";
String hint_text = "";
String stepText = "";
String replay_hints = "";
String read_question = "";
String show_example = "";
String show_video = "";
String formula = "";
String report_error = "";
String learning_companions= "";
String select_prob = "";
String view_log = "";
String example_problem_done = "";
String my_garden = "";
String my_progress = "";
String practice_area = "";
String logout = "";
String step = "";

String step_by_step_solution = "";
String submit_answer = "";
String alt_step_by_step_solution = "";
String alt_submit_answer = "";
String watch_and_listen_instructions = "";
String alt_watch_and_listen_instructions = "";
String example_problem_play_hints = "";
String alt_example_problem_play_hints = "";


try {
	if (pageLangIndex == 0) {
		if (lang.equals("en")) {
			lang = "en";
			loc1 = new Locale("en","US");	
			loc2 = new Locale("es","US");	
		}
		else {
			lang = "es";
			loc1 = new Locale("es","US");	
			loc2 = new Locale("en","US");		
		}
	}
	else {
		if (lang.equals("en")) {
			lang = "es";
			loc1 = new Locale("es","US");	
			loc2 = new Locale("en","US");	
		}
		else {
			lang = "en";
			loc1 = new Locale("en","US");	
			loc2 = new Locale("es","US");		
		}	
	}
}
catch (Exception e) {
	 System.out.println("pageLangIndex " + e.getMessage());
	 pageLangIndex = 0;
}

ResourceBundle rb = null;

ResourceBundle rb1 = null;
try {
	rb1 = ResourceBundle.getBundle("MathSpring",loc1);
}
catch (Exception e) {
	System.out.println(e.getMessage());
}
ResourceBundle rb2 = null;
try {
	rb2 = ResourceBundle.getBundle("MathSpring",loc2);
}
catch (Exception e) {
	System.out.println(e.getMessage());
}

if (lang.equals("en")) {
	rb = rb1;
}
else {
	rb = rb2;	
}


	

if ( (pageLangIndex == 0) && (lang.equals("en")) || ((pageLangIndex == 1) && (!(lang.equals("en")))) ) {
	next_problem = rb1.getString("next_problem");
	translate_this_problem = rb1.getString("translate_this_problem");
	no_translation = rb1.getString("no_translation");
	translate_to_spanish = rb1.getString("translate_to_spanish");
	translate_to_english = rb1.getString("translate_to_english");
	problem_current_topic = rb1.getString("problem_current_topic");
	problem_standards = rb1.getString("problem_standards");
	hint_text = rb1.getString("hint");
	stepText = rb1.getString("step");
	replay_hints = rb1.getString("replay_hints");
	read_question = rb1.getString("read_question");
	show_example = rb1.getString("show_example");
	show_video = rb1.getString("show_video");
	learning_companions =  rb1.getString("learning_companions");
	formula = rb1.getString("formula");
	report_error = rb1.getString("report_error");
	select_prob = rb1.getString("select_prob");
	view_log = rb1.getString("view_log");
	my_garden = rb1.getString("my_garden");
	my_progress = rb1.getString("my_progress");
	practice_area = rb1.getString("practice_area");
	logout = rb1.getString("log_out");
	watch_and_listen_instructions = rb1.getString("watch_and_listen_instructions");
	example_problem_play_hints = rb1.getString("example_problem_play_hints");
	
	example_problem_done = rb1.getString("example_problem_done");
	step = rb1.getString("step");
}
else {
	next_problem = rb2.getString("next_problem");
	translate_this_problem = rb2.getString("translate_this_problem");
	no_translation = rb2.getString("no_translation");
	translate_to_spanish = rb2.getString("translate_to_spanish");
	translate_to_english = rb2.getString("translate_to_english");
	problem_current_topic = rb2.getString("problem_current_topic");
	problem_standards = rb2.getString("problem_standards");
	hint_text = rb2.getString("hint");
	stepText = rb2.getString("step");
	replay_hints = rb2.getString("replay_hints");
	read_question = rb2.getString("read_question");
	show_example = rb2.getString("show_example");
	show_video = rb2.getString("show_video");
	learning_companions =  rb1.getString("learning_companions");
	formula = rb2.getString("formula");
	report_error = rb2.getString("report_error");
	select_prob = rb2.getString("select_prob");
	view_log = rb2.getString("view_log");
	my_garden = rb2.getString("my_garden");
	my_progress = rb2.getString("my_progress");
	practice_area = rb2.getString("practice_area");
	logout = rb2.getString("log_out");
	watch_and_listen_instructions = rb2.getString("watch_and_listen_instructions");
	example_problem_play_hints = rb2.getString("example_problem_play_hints");
	example_problem_done = rb2.getString("example_problem_done");
	step = rb2.getString("step");

}

Locale prob_loc1 = request.getLocale(); 
Locale prob_loc2 = request.getLocale();

try {
	if (probLangIndex == 0) {
		if (prob_lang.equals("en")) {
			prob_lang = "en";
			prob_loc1 = new Locale("en","US");	
			prob_loc2 = new Locale("es","US");	
		}
		else {
			prob_lang = "es";
			prob_loc1 = new Locale("es","US");	
			prob_loc2 = new Locale("en","US");		
		}
	}
	else {
		if (prob_lang.equals("en")) {
			prob_lang = "es";
			prob_loc1 = new Locale("es","US");	
			prob_loc2 = new Locale("en","US");	
		}
		else {
			prob_lang = "en";
			prob_loc1 = new Locale("en","US");	
			prob_loc2 = new Locale("es","US");		
		}	
	}
}
catch (Exception e) {
	 System.out.println("pageLangIndex " + e.getMessage());
}

ResourceBundle prob_rb = null;
ResourceBundle prob_rb1 = null;
try {
	prob_rb1 = ResourceBundle.getBundle("MathSpring",prob_loc1);
}
catch (Exception e) {
	System.out.println(e.getMessage());
}
ResourceBundle prob_rb2 = null;
try {
	prob_rb2 = ResourceBundle.getBundle("MathSpring",prob_loc2);
}
catch (Exception e) {
	System.out.println(e.getMessage());
}

step_by_step_solution = prob_rb1.getString("step_by_step_solution");
submit_answer = prob_rb1.getString("submit_answer");
watch_and_listen_instructions = prob_rb1.getString("watch_and_listen_instructions");
example_problem_play_hints = prob_rb1.getString("example_problem_play_hints");

alt_step_by_step_solution = prob_rb2.getString("step_by_step_solution");
alt_submit_answer = prob_rb2.getString("submit_answer");
alt_watch_and_listen_instructions = prob_rb2.getString("watch_and_listen_instructions");
alt_example_problem_play_hints = prob_rb2.getString("example_problem_play_hints");


%>

<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>MathSpring | Tutoring</title>
<link rel="apple-touch-icon" sizes="180x180"
	href="img/apple-touch-icon.png">
<link rel="icon" type="image/png" href="img/favicon-32x32.png"
	sizes="32x32">
<link rel="icon" type="image/png" href="img/favicon-16x16.png"
	sizes="16x16">
<link rel="manifest" href="css/manifest.json">
<meta name="theme-color" content="#ffffff">
<link
	href="js/jquery-ui-1.10.4.custom/css/spring/jquery-ui-1.10.4.custom.min.css"
	rel="stylesheet">
<link href="css/mathspring_new.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet" type="text/css"/>

<link href="https://fonts.googleapis.com/css?family=Roboto|Source+Code+Pro" rel="stylesheet">
<link rel="stylesheet" href="css/normalize.css?ver=<%=versions.getString("css_version")%>">
<link rel="stylesheet" href="css/style.css?ver=<%=versions.getString("css_version")%>">

<%
if (lang.equals("es")) {
%>
<link href="sass_compiled/tutores.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet">
<%
}
else 
{
%>
<link href="sass_compiled/tutor.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet">
<%
}
%>

<link rel="stylesheet"
	href="<c:url value="/js/bootstrap/css/bootstrap.css" />" />

<script type="text/javascript"
	src="<c:url value="/js/bootstrap/js/jquery-2.2.2.min.js" />"></script>
<script
	src="js/jquery-ui-1.10.4.custom/js/jquery-ui-1.10.4.custom.min.js"></script>
<script src="js/jquery.dialogextend.min.js"></script>
<script type="text/javascript"
	src="<c:url value="/js/bootstrap/js/bootstrap.min.js" />"></script>

<c:if test="${showProblemSelector}">
	<link
		href="https://cdn.datatables.net/1.10.13/css/dataTables.bootstrap4.min.css"
		rel="stylesheet" type="text/css">
	<link
		href="https://cdn.datatables.net/colreorder/1.3.2/css/colReorder.bootstrap4.min.css"
		rel="stylesheet" type="text/css">

	<link
		href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
		rel="stylesheet">
	<link rel="stylesheet"
		href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.2/css/bootstrap-select.min.css">


	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.2/js/bootstrap-select.min.js"></script>
	<script type="text/javascript"
		src="<c:url value="https://cdn.datatables.net/1.10.13/js/jquery.dataTables.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="https://cdn.datatables.net/1.10.13/js/dataTables.bootstrap4.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="https://cdn.datatables.net/colreorder/1.3.2/js/dataTables.colReorder.min.js" />"></script>

</c:if>

<script type="text/javascript">
	var lang = "en";
	var stepText = "<%= rb.getString("step") %>";
	var no_example_to_show = "<%= rb.getString("no_example_to_show") %>";
	var no_video_to_show = "<%= rb.getString("no_video_to_show") %>";
	var no_instructions_to_show = "<%= rb.getString("no_instructions_to_show") %>";
	var problem_current_topic = "<%= rb.getString("problem_current_topic") %>";
	var problem_standards = "<%= rb.getString("problem_standards") %>";
	var example_problem_done = "<%= rb.getString("example_problem_done") %>";
	var hint_not_found = "<%= rb.getString("hint_not_found") %>"; 
	var please_watch_video = "<%= rb.getString("please_watch_video") %>";
	var yes_choice = "<%= rb.getString("yes") %>";	
	var no_choice = "<%= rb.getString("no") %>";
	var no_hints_seen_yet = "<%= rb.getString("no_hints_seen_yet") %>";
	var work_with_a_partner = "<%= rb.getString("work_with_a_partner") %>";
	var continue_waiting = "<%= rb.getString("continue_waiting") %>";
	var answering_rapidly = "<%= rb.getString("answering_rapidly") %>";
	var making_a_change = "<%= rb.getString("making_a_change") %>";
	var switching_topics = "<%= rb.getString("switching_topics") %>";
	var how_are_you_doing = "<%= rb.getString("how_are_you_doing") %>";
	var what_are_your_goals = "<%= rb.getString("what_are_your_goals") %>";
	var lets_see_our_progress = "<%= rb.getString("lets_see_our_progress") %>";
	var waiting_for_partner = "<%= rb.getString("waiting_for_partner") %>";
	var initializing_camera = "<%= rb.getString("initializing_camera") %>";
	var wait_for_camera = "<%= rb.getString("wait_for_camera") %>";
	var camera_initialized = "<%= rb.getString("camera_initialized") %>";
	var next_problem = "<%= next_problem %>";
	var translate_this_problem = "<%= translate_this_problem %>";
	var no_translation = "<%= no_translation %>";
	var translate_to_spanish = "<%= translate_to_spanish %>";
	var translate_to_english = "<%= translate_to_english %>";

	var hint_text = "<%= hint_text %>";
	var replay_hints = "<%= replay_hints %>";
	var read_question = "<%= read_question %>";
	var show_example = "<%= show_example %>";
	var show_video = "<%= show_video %>";
	var learning_companions = "<%= learning_companions %>";
	var formula = "<%= formula %>";
	var report_error = "<%= report_error %>";
	var select_prob = "<%= select_prob %>";
	var view_log = "<%= view_log %>";
	var my_garden = "<%= my_garden %>";
	var my_progress = "<%= my_progress %>";
	var practice_area = "<%= practice_area %>";
	var logout = "<%= logout %>";
	var example_problem_done = "<%= example_problem_done %>";	
	var stepText = "<%= stepText %>";
	window.probLang = "<%= prob_lang %>";
	window.step_by_step_solution = "<%= step_by_step_solution %>";	
	window.alt_step_by_step_solution = "<%= alt_step_by_step_solution %>";	
	window.submit_answer = "<%= submit_answer %>";	
	window.alt_submit_answer = "<%= alt_submit_answer %>";	
	window.example_problem_play_hints = "<%= example_problem_play_hints %>";	
	window.alt_example_problem_play_hints = "<%= alt_example_problem_play_hints %>";	
	window.watch_and_listen_instructions = "<%= watch_and_listen_instructions %>";	
	window.alt_watch_and_listen_instructions = "<%= alt_watch_and_listen_instructions %>";	

		
</script>



<script type="text/javascript" src="js/simple-slider.js"></script>
<script type="text/javascript" src="js/tutorutils.js?ver=<%=versions.getString("js_version")%>"></script>
<script type="text/javascript" src="js/tutorAnswer.js?ver=<%=versions.getString("js_version")%>"></script>
<script type="text/javascript" src="js/tutorhint.js?ver=<%=versions.getString("js_version")%>"></script>
<script type="text/javascript" src="js/tutorhut_new.js?ver=<%=versions.getString("js_version")%>"></script>
<script type="text/javascript" src="js/devdialog.js?ver=<%=versions.getString("js_version")%>"></script>
<script type="text/javascript" src="js/tutorintervention.js?ver=<%=versions.getString("js_version")%>"></script>
<script type="text/javascript" src="js/intervhandlers_new.js?ver=<%=versions.getString("js_version")%>"></script>



<script type="text/javascript">



        var globals = {
            lastProbType: '${lastProbType}',
            isBeginningOfSession: ${isBeginningOfSession},
            sessionId: ${sessionId},
            timeInSession: ${timeInSession},
            elapsedTime: ${elapsedTime},
            probElapsedTime: 0,
            clock: 0,
            curHint: null,
            exampleCurHint: null,
            hintSequence: null,
            exampleHintSequence: null,
            lastProbId: ${lastProbId},
            untranslateProbId: 0,
            trace: false,
            debug: false,
            topicId: ${topicId},
            guiLock: false,
            learningCompanion: '${learningCompanion}',
            learningCompanionMessageSelectionStrategy: '${learningCompanionMessageSelectionStrategy}',
            userName: "${userName}",
            studId : ${studId} ,
            className : "${className}" ,
            teacherName : "${teacherName}" ,
            gazeDetectionOn: ${gazeDetectionOn},
            gazeParamsJSON: ${gazeParamsJSON},
            gazeWanderingUI: "",
            probType: '${probType}',
            experiment : '${experiment}',
            probLangIndex : 0,
            isTranslation: 0,
            studentLangIndex : 1,
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
            clickTime: 0,
            mouseSaveInterval: ${mouseSaveInterval},
            mouseHistory: []

        }

        var sysGlobals = {
            isDevEnv: ${isDevEnv},
            wayangServletContext: '${wayangServletContext}',
            gritServletContext: '${gritServletContext}',
            gritServletName: '${gritServletName}',
            problemContentDomain : '${problemContentDomain}',
            problemContentPath : '${problemContentPath}',
            webContentPath : '${webContentPath}',
            webContentPath2 : '${webContentPath2}',
            servletContextPath : '${pageContext.request.contextPath}',
            servletName : '${servletName}',
            probplayerPath : '${probplayerPath}',
            wait: false,
            eventCounter: ${eventCounter},
            huygui: true


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
        $(document).ready(function () {
            tutorhut_main(globals,sysGlobals,transients, "${learningCompanionMovie}");
            generateHighlightRuleDialog();

            if (!globals.showAnswer) {
                $(".dev-view").remove();
            } else {
                $(".dev-view").show();
            }
            
        	startSessionClock(globals.timeInSession);

            // Adjust the width of the character window
            var srcCompanion = $('#learningCompanionWindow').attr('src');
            var isJake = /.*Jake/.test(srcCompanion);
            if (isJake) {
                console.log('Hello world');
                $('.huytran-practice__character-window').width(269);
            } else {
                $('.huytran-practice__character-window').width(250);
            }
        });

    	var pageLangIndexStr = "<%= pageLangIndex %>";
   		var pageLangIndex = Number(pageLangIndexStr);
        
    </script>


<style type="text/css">
/*Overwrite bootstrap rule for developer mode*/

fieldset.scheduler-border {
	border: 1px groove #ddd !important;
	padding: 0 1.4em 1.4em 1.4em !important;
	margin: 0 0 1.5em 0 !important;
	-webkit-box-shadow: 0px 0px 0px 0px #000;
	box-shadow: 0px 0px 0px 0px #000;
}

a {
	text-decoration: none !important;
}

a span {
	font-family: Raleway !important;
}

.huytran-practice__navitem {
	font-family: Raleway !important;
}

.huytran-practice__navitem span {
	font-family: FontAwesome !important;
}

label {
	font-family: Raleway !important;
	font-weight: normal;
}

.huytran-practice__hide-button span {
	top: 0 !important;
}

.glyphicon {
	font-family: 'Glyphicons Halflings' !important;
}

.huytran-sitenav__button {
	margin: 0px; !important;
}

.disable_a_href{
    pointer-events: none;
	color: #e1e1e1;
}

.not-allowed {
     cursor: not-allowed! important;
}

#translateProbWrapper:hover {
  background: #92DDA3;
  cursor: pointer;
}

</style>
</head>
<body>

	<video id="webcam" width="180" height="180" autoplay></video>
	<canvas id="overlay" width="180" height="180"></canvas>
	<div id="monitorBox" width="240px" height="150px">
		<span id="p1"></span>
		<span id="gazeMonitor1"></span>
		<span id="gazeMonitor2"></span>
		<span id="gazeMonitor3"></span>
		<span id="gazeMonitor4"></span>
		<span id="gazeMonitor5"></span>
		<span id="gazeMonitor6"></span>
	</div>

	<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/@tensorflow/tfjs@2.4.0/dist/tf.min.js"></script>
<!-- 	<script src="https://cdn.jsdelivr.net/npm/@tensorflow/tfjs@2.4.0/dist/tf.js"></script> -->
  	<script src="js/face-api.js?ver=<%=versions.getString("js_version")%>"></script>
  	<script src="js/ui.js?ver=<%=versions.getString("js_version")%>"></script>
  	<script src="js/headmodel.js?ver=<%=versions.getString("js_version")%>"></script>
  	<script src="js/main.js?ver=<%=versions.getString("js_version")%>"></script>
  	<script src="js/calibration.js?ver=<%=versions.getString("js_version")%>"></script>
  	<script src="js/plain-overlay.min.js"></script>
  	<script src="js/jquery.plainoverlay.min.js"></script>

	<audio id='questionaudio' name='questionaudio'>
		<source id='questionogg' src='' type='audio/ogg'>
		<source id='questionmp3' src='' type='audio/mpeg'>
		<%= rb.getString("browser_does_not_support_the_audio_element") %>
	</audio>

	<%-- This div is a dialog that is shown when the user clicks on Show Example.  It plays an example problem in the dialog--%>
	<div id="exampleContainer" width="650" height="650"
		title="<%= rb.getString("watch_and_listen_instructions")%>"
		<iframe id="exampleFrame" name="iframe2" width="650" height="650"
			src="" frameborder="no" scrolling="no"> </iframe>
	</div>

	<div id="utilDialog" title="">
		<iframe id="utilDialogIframe" width="675" height="675"> </iframe>
	</div>

	<div id="interventionDialog" title="">
		<div id="interventionDialogContent"></div>
	</div>

	<div id="selectProblemDialog" title="<%= rb.getString("select_problem")%>">
		<iframe id="selectProblemDialogIframe" width="500" height="500"></iframe>
	</div>

	<div class="huytran-tutor">
		<div class="huytran-sitenav">
			<div class="huytran-sitenav__menu">
				<div class="huytran-sitenav__burger" onclick="toggleNav()">
					<i class="fa fa-bars" aria-hidden="true" scale="1.5"></i>
				</div>
			</div>

			<div class="huytran-sitenav__main">
				<input type="checkbox" class="huytran-sitenav__showmore-state"
					id="post" /> 
				<a href="#"	class="huytran-sitenav__button huytran-sitenav__button--first"
					id="nextProb"> <span class="huytran-sitenav__icon"> <i
						class="fa fa-plus" aria-hidden="true"></i>
				</span> <span id = "nextProbText" class="huytran-sitenav__buttontitle"><%= rb.getString("next_problem") %></span>
				<span id = "next_prob_spinner" class="huytran-sitenav__icon" style="display: none"><i class="fa fa-refresh fa-spin" style="font-size:16px;color:green"></i></span>
				</a>
				<div id="translateProbWrapper">
					<a href="#" id="translateProb" class="huytran-sitenav__button"> 
					<span class="huytran-sitenav__icon"> <i class="fa fa-plus" aria-hidden="true"></i></span> 
					<span id= "translateProbText" class="huytran-sitenav__buttontitle"><%= rb.getString("no_translation") %></span>
					<span id = "trans_prob_spinner" class="huytran-sitenav__icon" style="display: none"><i class="fa fa-refresh fa-spin" style="font-size:16px;color:green"></i></span>
					</a>
				</div>
				
				
												
				<a href="#"  id="read" class="huytran-sitenav__button"> 
					<span class="huytran-sitenav__icon"> <i class="fa fa-bullhorn" aria-hidden="true"></i></span>
					<span id="readText"class="huytran-sitenav__buttontitle"><%= rb.getString("read_question") %></span>
				</a> 
				
				<a href="#" id="hint" class="huytran-sitenav__button"> 
					<span class="huytran-sitenav__icon"> <i class="fa fa-lightbulb-o" aria-hidden="true"></i></span>
					<span id="hint_label" class="huytran-sitenav__buttontitle">hint_text</span>
				</a>
				 
				<a href="#" id="replay" class="huytran-sitenav__button" > 
					<span class="huytran-sitenav__icon"> <i class="fa fa-repeat" aria-hidden="true"></i></span>
					<span id="replayText" class="huytran-sitenav__buttontitle"><%= rb.getString("replay_hints") %></span>
				</a>
				 
				<a href="#"
					class="huytran-sitenav__button huytran-sitenav__showmore-target"
					id="example"> <span class="huytran-sitenav__icon"> <i
						 class="fa fa-question" aria-hidden="true"></i>
				</span> <span id="exampleText" class="huytran-sitenav__buttontitle"><%= rb.getString("show_example") %></span>
				</a>
				 
				<a href="#"
					class="huytran-sitenav__button huytran-sitenav__showmore-target"
					id="video"> <span class="huytran-sitenav__icon"> <i
						class="fa fa-video-camera" aria-hidden="true"></i>
				</span> <span id="videoText" class="huytran-sitenav__buttontitle"><%= rb.getString("show_video") %></span>
				</a>
				 
				<a class="huytran-sitenav__button huytran-sitenav__showmore-target"
					id="showLCList"> <span	class="huytran-sitenav__icon"> <i
						class="fa fa-exclamation aria-hidden="true"></i>
				</span> <span id="learningCompanionsText" class="huytran-sitenav__buttontitle"><%= rb.getString("learning_companions") %></span>
				</a>

				<a href="#"
					class="huytran-sitenav__button huytran-sitenav__showmore-target"
					id="formulas"> <span class="huytran-sitenav__icon"> <i
						 class="fa fa-magic" aria-hidden="true"></i>
					</span> <span id="formulaText" class="huytran-sitenav__buttontitle"><%= rb.getString("formula") %></span>
				
				</a>
				 
				<a class="huytran-sitenav__button huytran-sitenav__showmore-target"
					data-toggle="modal" data-target="#reportModal"> <span
					class="huytran-sitenav__icon"> <i
						class="fa fa-exclamation aria-hidden="true"></i>
				</span> <span id="reportErrorText" class="huytran-sitenav__buttontitle"><%= rb.getString("report_error") %></span>
				</a>
				
				<c:if test="${showProblemSelector}">
					<a id="selectProb" href="#" class="huytran-sitenav__button"> <span
						class="huytran-sitenav__icon"> <i class="fa fa-check"
							aria-hidden="true"></i>
					</span> <span class="huytran-sitenav__buttontitle"><%= rb.getString("select_prob") %></span>
					</a>

					<a id="getEventLogs" href="#" class="huytran-sitenav__button">
						<span class="huytran-sitenav__icon"> <i class="fa fa-eye"
							aria-hidden="true"></i>
					</span> <span class="huytran-sitenav__buttontitle"><%= rb.getString("view_log") %></span>
					</a>
				</c:if>
				</a> <label class="huytran-sitenav__showmore-trigger" for="post">
				</label>
			</div>
		</div>

		<div class="huytran-practice">
			<div class="huytran-practice__menu">
				<div class="huytran-practice__nav">
					<a class="huytran-practice__navitem" id="home"><%= rb.getString("my_garden") %></a> 
					<a class="huytran-practice__navitem" id="myProg"><%= rb.getString("my_progress") %></a> 
					<a class="huytran-practice__navitem" id="practiceArea" href="#"><%= rb.getString("practice_area") %></a>
						
				<a class="huytran-practice__navitem" id="logout"
					href="TutorBrain?action=Logout&sessionId=${sessionId}&elapsedTime=${elapsedTime}&var=">
					<%= rb.getString("log_out") %> &nbsp; 
					<span class="fa fa-sign-out"></span>
				</a>
				<a href="#" class="huytran-practice__navitem--clock huytran-practice__navitem--last"> <span
						class="huytran-sitenav__icon"> <i class="fa fa-clock-o"
							aria-hidden="true"></i>
						</span> <span id="session_clock"></span> </a>
				</div>				
			</div>

			<div class="huytran-practice__container">
				<div class="huytran-practice__main" id="frameContainer">
					<div class="huytran-practice__topic">
						<div id="problemTopicAndStandards" style="clear: both;"></div>
					</div>
					<iframe id="problemWindow" class="probWindow" name="iframe1"
						width="650" height="650" src="${activityURL}" frameborder="no"
						scrolling="no"> </iframe>
					<div class="huytran-practice__info">
						<p id="pid">${probId}</p>
						<h2>&nbsp</h2>
						<h2>&nbsp</h2>
						<div class="dev-view">
							<p id="effort">${effort}</p>
							<p class="dev-view-label" id="answer"><%= rb.getString("answer")%>:
								${globals.answer}</p>
						</div>
					</div>
				</div>
				<div class="huytran-practice__character">
					<div class="huytran-practice__hide-button"
						onclick="toggleCharacter()">
						<span class="fa fa-minus"></span>
					</div>
					<div class="huytran-practice__character-window">
						<div class="learningCompanionContainer">
							<iframe id="learningCompanionWindow" name="lciframe" width="280"
								height="600" src="${learningCompanionMovie}"
								onload="lcLoaded(this)" scrolling="no" allow="autoplay">
							</iframe>
						</div>
					</div>
				</div>
				<div class="huytran-practice__character-collapse hide">
					<span class="huytran-practice__show-button"
						onclick="toggleCharacter()"> <span
						class="fa fa-plus"></span>
					</span> <span>Character</span>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="reportModal" tabindex="-1" role="dialog"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLabel"><%= rb.getString("report_error")%></h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<form id="report-form" data-target="#reportModal" method="POST">
						<div class="form-group">
							<label for="message-text" class="form-control-label"><%= rb.getString("message")%>:</label>
							<textarea class="form-control" id="message-text" name="message"></textarea>
						</div>
						<div class="form-check">
							<label class="form-check-label"> <input id="is-broken"
								type="checkbox" class="form-check-input"> <%= rb.getString("is_current_problem_broken")%>
							</label>
						</div>
						<button type="submit" class="btn btn-success"><%= rb.getString("submit")%></button>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal"><%= rb.getString("close")%></button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" height="500px" data-backdrop="static" id="lcModal" tabindex="-1" role="dialog"
		aria-labelledby="lcModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="lcModalLabel">Learning companions</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div id="lcBody" class="modal-body">
				</div>
				<div class="modal-footer">
					<button id="lcSubmitBtn" type="button" class="btn btn-secondary" onclick="changeStudentLC();"><%= rb.getString("submit")%></button>
					<button                  type="button" class="btn btn-secondary" data-dismiss="modal"><%= rb.getString("close")%></button>
				</div>
			</div>
		</div>
	</div>


	<div id="eventLogWindow" title="Event Logs" style="display: none;">
		<div class="containers">
			<div class="panel-group" id="accordion">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle" data-toggle="collapse"
								data-parent="#accordion" href="#collapseOne"> Event Log </a>
						</h4>
					</div>
					<div id="collapseOne" class="panel-collapse collapse in">
						<div class="panel-body">
							<div class='scrolledTable'>

								<fieldset class="scheduler-border">
									<legend>Add/Remove Columns</legend>

									<a type="button" class="btn btn-default toggle-vis"
										data-column="0"> <span class="glyphicon glyphicon-remove"></span>
										id
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="1"> <span class="glyphicon glyphicon-remove"></span>
										studId
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="2"> <span class="glyphicon glyphicon-remove"></span>
										sessNum
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="3"> <span class="glyphicon glyphicon-remove"></span>
										action
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="4"> <span class="glyphicon glyphicon-remove"></span>
										userInput
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="5"> <span class="glyphicon glyphicon-remove"></span>
										isCorrect
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="6"> <span class="glyphicon glyphicon-remove"></span>
										elapsedTime
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="7"> <span class="glyphicon glyphicon-remove"></span>
										probElapsed
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="8"> <span class="glyphicon glyphicon-remove"></span>
										problemId
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="9"> <span class="glyphicon glyphicon-remove"></span>
										hintStep
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="10"> <span class="glyphicon glyphicon-remove"></span>
										hintId
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="11"> <span class="glyphicon glyphicon-remove"></span>
										emotion
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="12"> <span class="glyphicon glyphicon-remove"></span>
										activityName
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="13"> <span class="glyphicon glyphicon-remove"></span>
										auxId
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="14"> <span class="glyphicon glyphicon-remove"></span>
										auxTable
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="15"> <span class="glyphicon glyphicon-remove"></span>
										time
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="16"> <span class="glyphicon glyphicon-remove"></span>
										curTopicId
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="17"> <span class="glyphicon glyphicon-remove"></span>
										testerNote
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="18"> <span class="glyphicon glyphicon-remove"></span>
										clickTime
									</a>
								</fieldset>

								<fieldset class="scheduler-border">
									<legend>Hightlight Rule Editor</legend>
									<div class="form-group">
										<div class="row">
											<button class="btn btn-success" role="button"
												id="newHighlightRule_eventLog"
												aria-label="Add new hightlightRule">
												<i class="fa fa-plus" aria-hidden="true"></i>&nbsp;Add new
												Hightlight Rule
											</button>
										</div>
									</div>
									<div id="newHighlightRulecontainer_eventLog"></div>
								</fieldset>

								<table id="eventLogTable"
									class="table table-striped table-bordered" cellspacing="0"
									width="100%">
									<thead>
										<tr>
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
							<a class="accordion-toggle" data-toggle="collapse"
								data-parent="#accordion" href="#collapseTwo"> Student
								Problem History Log </a>
						</h4>
					</div>
					<div id="collapseTwo" class="accordion-body collapse">
						<div class="panel-body">
							<div class='scrolledTable'>

								<fieldset class="scheduler-border">
									<legend>Add/Remove Columns</legend>

									<a type="button" class="btn btn-default toggle-vis"
										data-column="0"> <span class="glyphicon glyphicon-remove"></span>
										id
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="1"> <span class="glyphicon glyphicon-remove"></span>
										studId
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="2"> <span class="glyphicon glyphicon-remove"></span>
										sessionId
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="8"> <span class="glyphicon glyphicon-remove"></span>
										problemId
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="3"> <span class="glyphicon glyphicon-remove"></span>
										topicId
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="4"> <span class="glyphicon glyphicon-remove"></span>
										problemBeginTime
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="5"> <span class="glyphicon glyphicon-remove"></span>
										problemEndTime
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="6"> <span class="glyphicon glyphicon-remove"></span>
										timeInSession
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="7"> <span class="glyphicon glyphicon-remove"></span>
										timeInTutor
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="9"> <span class="glyphicon glyphicon-remove"></span>
										timeToFirstAttempt
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="10"> <span class="glyphicon glyphicon-remove"></span>
										timeToFirstHint
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="11"> <span class="glyphicon glyphicon-remove"></span>
										timeToSolve
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="12"> <span class="glyphicon glyphicon-remove"></span>
										numMistakes
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="13"> <span class="glyphicon glyphicon-remove"></span>
										numHints
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="14"> <span class="glyphicon glyphicon-remove"></span>
										videoSeen
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="15"> <span class="glyphicon glyphicon-remove"></span>
										numAttemptsToSolve
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="16"> <span class="glyphicon glyphicon-remove"></span>
										solutionHintGiven
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="17"> <span class="glyphicon glyphicon-remove"></span>
										mode
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="18"> <span class="glyphicon glyphicon-remove"></span>
										mastery
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="18"> <span class="glyphicon glyphicon-remove"></span>
										emotionAfter
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="18"> <span class="glyphicon glyphicon-remove"></span>
										emotionLevel
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="18"> <span class="glyphicon glyphicon-remove"></span>
										effort
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="18"> <span class="glyphicon glyphicon-remove"></span>
										exampleSeen
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="18"> <span class="glyphicon glyphicon-remove"></span>
										textReaderUsed
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="18"> <span class="glyphicon glyphicon-remove"></span>
										numHintsBeforeSolve
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="18"> <span class="glyphicon glyphicon-remove"></span>
										isSolved
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="18"> <span class="glyphicon glyphicon-remove"></span>
										adminFlag
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="18"> <span class="glyphicon glyphicon-remove"></span>
										authorFlag
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="18"> <span class="glyphicon glyphicon-remove"></span>
										collaboratedWith
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="18"> <span class="glyphicon glyphicon-remove"></span>
										timeToSecondAttempt
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="18"> <span class="glyphicon glyphicon-remove"></span>
										timeToThirdAttempt
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="18"> <span class="glyphicon glyphicon-remove"></span>
										timeToSecondHint
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="18"> <span class="glyphicon glyphicon-remove"></span>
										timeToThirdHint
									</a> <a type="button" class="btn btn-default toggle-vis"
										data-column="18"> <span class="glyphicon glyphicon-remove"></span>
										probDiff
									</a>
								</fieldset>
								<fieldset class="scheduler-border">
									<legend>Hightlight Rule Editor</legend>
									<div class="form-group">
										<div class="row">
											<a class="btn btn-success" role="button"
												id="newHighlightRule_studentProblemHistoryLog"
												aria-label="Add new hightlightRule"> <i
												class="fa fa-plus" aria-hidden="true"></i>&nbsp;Add new
												Hightlight Rule
											</a>
										</div>
									</div>
									<div id="newHighlightRulecontainer_studentProblemHistoryLog"></div>
								</fieldset>

								<table id="studentProblemHistoryTable"
									class="table table-striped table-bordered" cellspacing="0"
									width="100%">
									<thead>
										<tr>
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


	<script>
	
    function toggleNav() {
        $('.huytran-sitenav__main').toggleClass('hide');
    }

    function toggleCharacter() {
        $('.huytran-practice__character').toggleClass('hide');
        $('.huytran-practice__character-collapse').toggleClass('hide');
    }

    $(document).ready(function() {
    	var pgContext = '${pageContext.request.contextPath}';
    	if (globals.gazeParamsJSON.gazinterv_monitor_on === 1) {
	    	document.getElementById("webcam").style.zIndex = "3";    	
	    	document.getElementById("monitorBox").style.visibility = "visible";	    
	    	document.getElementById("monitorBox").style.zIndex = "3";    	
	    	document.getElementById("gazeMonitor2").innerHTML = "";
	    	document.getElementById("gazeMonitor3").innerHTML = "";
	    	document.getElementById("gazeMonitor4").innerHTML = "";
	    	document.getElementById("gazeMonitor5").innerHTML = "";
	    	document.getElementById("gazeMonitor6").innerHTML = "";
	    }
	    else {
    		document.getElementById("monitorBox").style.visibility = "hidden";
	    }
    	if (globals.experiment.indexOf("multi-lingual") < 0) {
    		document.getElementById("translateProbWrapper").style.display = "none";
    	}
    	else {
    		document.getElementById("translateProbText").innerHTML =  translate_this_problem;    		
    	}

		document.getElementById("nextProbText").innerHTML =  next_problem;		
		document.getElementById("replayText").innerHTML =  replay_hints;
		document.getElementById("readText").innerHTML =  read_question;
		document.getElementById("exampleText").innerHTML =  show_example;
		document.getElementById("hint_label").innerHTML =  hint_text;
		document.getElementById("videoText").innerHTML =  show_video;
		document.getElementById("learningCompanionsText").innerHTML =  learning_companions;
		document.getElementById("formulaText").innerHTML = formula;
		document.getElementById("home").innerHTML =  my_garden;
		document.getElementById("myProg").innerHTML =  my_progress;
		document.getElementById("practiceArea").innerHTML =  practice_area;
		document.getElementById("logout").innerHTML =  logout;
		document.getElementById("reportErrorText").innerHTML =  report_error;
//		document.getElementById("selectProb").innerHTML =  select_prob;
//		document.getElementById("view_log").innerHTML =  view_log;
		document.getElementById("exampleContainer").title =  watch_and_listen_instructions;
		
		problem_current_topic = "<%= problem_current_topic %>";
		problem_standards = "<%= problem_standards %>";

        if (pageLangIndex == 0) {
            document.getElementById("exampleContainer").title = watch_and_listen_instructions;;
    	}
		else {
            document.getElementById("exampleContainer").title = "WTF";
		}
		
        document.getElementById('lcModal').style.height = window.height / 2 ;

        
	    addEventListener("click", clickLCButton, false);
	    
    });
    
    $(document).ready(function() {
        $('#report-form').submit(function (event) {
            var formData = {
                'sessionId': globals.sessionId,
                'message': $('textarea[name=message]').val(),
                'isBroken': $('#is-broken').is(':checked')
            };

            $.ajax({
                type: 'GET',
                url: "${pageContext.request.contextPath}/TutorBrain"
                + "?action=ReportError"
                + "&isBroken=" + formData.isBroken
                + "&sessionId=" + formData.sessionId
                    + "&elapsedTime=" + globals.elapsedTime
                    + "&probElapsedTime=" + globals.probElapsedTime
                    + "&mode=" + globals.probMode
                + "&message=" + formData.message
            }).done(function (data) {
                console.log(data);
                // if the problem is reported as broken, then request the next one.
                if (formData.isBroken) {
                    nextProb(globals);
                }
            });
            $('#reportModal').modal('toggle');

            event.preventDefault();
        });

        $('#reportModal').on('hidden.bs.modal', function(){
            $(this).find('form')[0].reset();
        });

    });
    
</script>



	<div style="z-index: 100;" id="instructionsDialog" title="Instructions">
		<p id="instructionsP">${instructions}</p>
		<div class="empty"></div>
	</div>
	<%-- This div contains information about the current problem --%>
	<div id="problemId" style="display: block;">Problem Id: ${probId}</div>
	<%-- Only shown to test users--%>
	<div id="varBindings" style="display: none;"></div>

</body>
</html>
