// Frank 04-24-2020 issue #16 removed done button from example dialog
// Frank 05-06-2020 issue #87 call showVideo() at start of example problem
// Frank 05-12-2020 issue #87 commented out this feature for now
// Kartik 08-28-2020 issue #202 positioning of example dialog box
// Frank 10-07-20 Issue # 261 change problem heading
// Frank 12-26-20 Issue #329 translate topic name using lang variable
// Frank 01-13-21 Issue #354 fix topicName formatting
// Frank 01-16-21 Issue #357 fix topicName formatting
// Kartik 04-22-21 Issue #390 Removed previous display of current time in the problems screen
// Frank	07-03-21	v1..0.1 processGazeWandering ignore empty reposnse messages
// Frank	10-18-21	Fix Example window going off screen when window is less than 768 in height

var globals;
var sysGlobals;
var transients;

//var EXTERNAL = 'External';
//var HTML5 = 'html5';
//var FORMALITY = '4Mality';
var MODE_DEMO = "demo";
var MODE_EXAMPLE = "example";
var MODE_PRACTICE = "practice";
var PROBLEM_CONTAINER = "frameContainer";
var PROBLEM_CONTAINERID = "#"+PROBLEM_CONTAINER;
var PROBLEM_WINDOW = "problemWindow";
var PROBLEM_WINDOWID = "#"+PROBLEM_WINDOW;
var LEARNING_COMPANION_CONTAINER = "learningCompanionContainer";
var LEARNING_COMPANION_WINDOW = "learningCompanionWindow";
var LEARNING_COMPANION_WINDOW_ID = "#"+LEARNING_COMPANION_WINDOW;
var EXAMPLE_WINDOW = "exampleWindow";
var EXAMPLE_CONTAINER_DIV = "exampleContainer";
var EXAMPLE_CONTAINER_DIV_ID = "#"+EXAMPLE_CONTAINER_DIV;
var EXAMPLE_FRAME = "exampleFrame";
var EXAMPLE_FRAMEID = "#"+EXAMPLE_FRAME;
var UTIL_DIALOG = "utilDialog";
var INTERVENTION_DIALOG = "interventionDialog";
var INTERVENTION_DIALOG_CONTENT = "interventionDialogContent";
var INSTRUCTIONS_DIALOG = "instructionsDialog";
var SELECT_PROBLEM_DIALOG = "selectProblemDialog";
var INSTRUCTIONS_TEXT_ELT= "#instructionsP";
var UTIL_DIALOG_IFRAME = "utilDialogIframe";
var NO_MORE_PROBLEMS = "noMoreProblems";
var NO_MORE_REVIEW_PROBLEMS = "noMoreReviewProblems";
var NO_MORE_CHALLENGE_PROBLEMS = "noMoreChallengeProblems";
var INPUT_RESPONSE_FORM = "inputResponseForm";

var HTML_PROB_TYPE = "html5";
var EXTERNAL_PROB_TYPE = "EexternalActivity";
var TOPIC_INTRO_PROB_TYPE = "TopicIntro";
var INTERVENTION = "intervention";
var NEXT_PROBLEM_INTERVENTION = "NextProblemIntervention";
var IS_INPUT_INTERVENTION ="isInputIntervention";
var ATTEMPT_INTERVENTION = "AttemptIntervention";
var SAME_INTERVENTION = "SameIntervention"
const LCDIALOG_WIDTH = 300;
const LCDIALOG_HEIGHT = 700;
var DELAY = 700, clicks = 0, timer = null; //Variables required for determining the difference between single and double clicks

function isHTML5Problem() {
    return globals.probType === HTML_PROB_TYPE;
}

function isHTML5Example() {
    return globals.exampleProbType === HTML_PROB_TYPE;
}

function isDemoMode () {
    return globals.probMode === MODE_DEMO;
}

function isDemoOrExampleMode () {
    return globals.probMode === MODE_DEMO || globals.probMode === MODE_EXAMPLE;
}

function isIntervention () {
    return globals.probType === INTERVENTION;
}

function getProblemParams() {
    return globals.params;
}

function getAnswer() {
    return globals.answer;
}

function getProblemContentPath() {
    return sysGlobals.problemContentPath;
}

function getResource() {
    return globals.resource;
}

function getAnswers() {
    return globals.answers;
}

function getProblemStatement() {
    return globals.statementHTML;
}

function getProblemFigure() {
    return globals.questionImage;
}

function getProblemSound() {
    return globals.questionAudio;
}

function getHints() {
    return globals.hints;
}

function getUnits() {
    return globals.units;
}

function getForm() {
    return globals.form;
}

function isParameterized() {
    return (globals.params != null && globals.params != undefined)
}

// In the case of parameterized problems, we want to shuffle up the correct answer's position
function getNewAnswer() {
    return globals.newAnswer;
}

// each time we start a new problem, set global variables that contain info about its state.
function setGlobalProblemInfo (activity) {
    globals.numHints = activity.numHints;
    globals.numHintsSeen = 0;
    globals.standards = activity.standards;
}


function updateTimers () {
    var now = new Date().getTime();
    globals.clickTime= now;
    globals.probElapsedTime += now - globals.clock;
    globals.elapsedTime += now - globals.clock;
    globals.clock = now;
}



function incrementTimers(globals) {
    var now = new Date().getTime();
    globals.clickTime = now;
    globals.probElapsedTime += now - globals.clock;
    globals.elapsedTime += now - globals.clock;
    globals.clock = now;
}

// can be called to find out if we are waiting for results from server (and hence interface is in wait state)
window.isWaiting = function () {
    return globals.guiLock;
}

function lockGui() {
    globals.guiLock = true;
    // need to pop up a wait timer
}

function unlockGui() {
    globals.guiLock = false;
}

function showHourglassCursor(b) {
    if (b) {
        lockGui();
        $("body").css("cursor", "wait");
    }
    else {
        unlockGui();
        $("body").css("cursor", "default");
    }
}

function displayHintCount () {
	
    if (globals.numHints >= 0 && globals.numHintsSeen == 0) {
        $("#hint_label").html(hint_text + "(" + globals.numHints + ")");
    } else if (globals.numHintsSeen <= globals.numHints) {
        $("#hint_label").html(hint_text + "(" + globals.numHintsSeen + "/" + globals.numHints + ")");
    }
}

function showProblemInfo (pid, name, topic, standards) {
    $("#pid").text(pid + ":" + name);  // shows the problem ID + resource         
    
    $("#problemTopicAndStandards").html(
			 "<p style='float: left'>" + globals.teacherName + " - " + globals.className + "</p>" +
			 "<p style='float: right'>" + problem_current_topic + " " + topic + " | " + problem_standards + " "  + standards  + "</p>"
	 );
    displayHintCount();
}

function showUserInfo (userName) {
    $("#userDisplay").text("Logged in as: " + userName);
}

function showEffortInfo (effort) {
    $("#effort").text(effort);  // shows the effort of the last three problems (given as a string)
}

function showAnswer (ans) {
    $("#answer").text("Answer: " + ans);
}

function showVarBindings (varBindings) {
    if (varBindings == undefined) {
        $("#varBindings").text("No Var Bindings");
    }
    else {
        var s='';
        for (var k in varBindings)
            s = s + k + ":" + varBindings[k] + ',';
        $("#varBindings").text("Var Bindings: " + s);
    }
}

function loadIframe (iframeId, url) {
    $(iframeId).attr("src", url);
}

///////////////////////////////////////////////////////////////////////
////  Buttons on navlog (new problem, instructions, my progress
////  call these three functions
///////////////////////////////////////////////////////////////////////


function nextProb(globals,isSessionBegin=false) {

	toggleSolveDialogue(false);
    if (!globals.showMPP)
        hideMPP()
    if (globals.trace)
        debugAlert("in NextProb ");
    incrementTimers(globals);
    // call the server with a nextProblem event and the callback fn processNextProblemResult will deal with result
    showHourglassCursor(true);
    // A HACK.  Because the Topic Intro is an intervention but it doesn't show in a dialog, it is ended by clicking on New Problem button
    // which comes in here.  We want to send back an InputResponse though becuase thats what TopicIntros should get back.
    // TODO:  Probably should replace NewProblem button when a topic intro shows.  It could have the correct handler on it.
	globals.curHint = null;
	globals.hintSequence = null;
    if (globals.lastProbType === TOPIC_INTRO_PROB_TYPE)
        servletGet("InputResponseNextProblemIntervention",
            {probElapsedTime: globals.probElapsedTime, mode: globals.tutoringMode,
                destination:globals.destinationInterventionSelector},
            processNextProblemResult) ;
    // Normal Processing
    else
        servletGet("NextProblem", {probElapsedTime: globals.probElapsedTime, mode: globals.tutoringMode,lastLocation: 'Login', isEnteringPracticeArea: isSessionBegin, probLangIndex: globals.probLangIndex}, processNextProblemResult);    	
}


function translateProb(globals,isSessionBegin=false) {

	toggleSolveDialogue(false);
    if (!globals.showMPP)
        hideMPP()
    if (globals.trace)
        debugAlert("in NextProb ");
    incrementTimers(globals);
    // call the server with a nextProblem event and the callback fn processNextProblemResult will deal with result
    showHourglassCursor(true);
    // A HACK.  Because the Topic Intro is an intervention but it doesn't show in a dialog, it is ended by clicking on New Problem button
    // which comes in here.  We want to send back an InputResponse though becuase thats what TopicIntros should get back.
    // TODO:  Probably should replace NewProblem button when a topic intro shows.  It could have the correct handler on it.
	globals.curHint = null;
	globals.hintSequence = null;
	
//    if (globals.lastProbType === TOPIC_INTRO_PROB_TYPE)
//        servletGet("InputResponseNextProblemIntervention",
//            {probElapsedTime: globals.probElapsedTime, mode: globals.tutoringMode,
//                destination:globals.destinationInterventionSelector},
//            processNextProblemResult) ;
    // Normal Processing
//    else
        servletGet("TranslateProblem", {probElapsedTime: globals.probElapsedTime, mode: globals.tutoringMode,lastLocation: 'Login', isEnteringPracticeArea: isSessionBegin, probLangIndex: globals.probLangIndex, translateProbId: globals.probId}, processNextProblemResult);    	
}


// This function can only be called if the button is showing
function selectProblemDialog () {
    $("#"+SELECT_PROBLEM_DIALOG).dialog('open');
    var url = "/"+sysGlobals.wayangServletContext + "/TutorBrain?action=GetProblemListForTester&sessionId=" + globals.sessionId +"&elapsedTime="+ globals.elapsedTime + "&eventCounter="+ sysGlobals.eventCounter++;
    loadIframe("#selectProblemDialogIframe",url)
}

// only used by test-users from a special dialog that allows selecting problems.
function forceNextProblem (id) {
    $("#"+SELECT_PROBLEM_DIALOG).dialog('close');
    // send a NextProblemEvent that forces a particular problem
    incrementTimers(globals);
    // call the server with a nextProblem event and the callback fn processNextProblemResult will deal with result
    showHourglassCursor(true);
    servletGet("NextProblem", {probElapsedTime: globals.probElapsedTime, mode: globals.tutoringMode, probID: id}, processNextProblemResult);
}

function showInstructionsDialog (instructions) {
    $(INSTRUCTIONS_TEXT_ELT).text(instructions);
    $("#"+INSTRUCTIONS_DIALOG).dialog('open');
}

function instructions () {
    // probably want something slicker than this alert dialog.
    if (globals.instructions == "")
        alert(no_instructions_to_show);
    else {
        showInstructionsDialog(globals.instructions)
//        $(INSTRUCTIONS_TEXT_ELT).text(globals.instructions);
//        $("#"+INSTRUCTIONS_DIALOG).dialog('open');
    }
    sendSimpleNotificationEvent(globals,"ShowInstructions");
    return false;
}

// function myprogress(globals) {
//     debugAlert("in myprogress");
//     globals.lastProbType = globals.probType;
//     globals.lastProbId = globals.probId;
//     document.location.href = "/"+sysGlobals.wayangServletContext + "/TutorBrain?action=navigation&sessionId=" + globals.sessionId + "&elapsedTime=" + globals.elapsedTime + "&probElapsedTime=" + globals.probElapsedTime + "&from=sat_hut&to=my_progress&topicId="+ globals.topicId +"&probId="+globals.probId + "&eventCounter="+ sysGlobals.eventCounter++;
// }

// A newer version of the above. Arg has been removed so this function can easily be passed as an argument to other functions (the interventionDialog buttons
// click handlers)
function myprogress() {
    debugAlert("in myprogress");
    globals.lastProbType = globals.probType;
    globals.lastProbId = globals.probId;
    document.location.href = "/"+sysGlobals.wayangServletContext + "/TutorBrain?action=navigation&sessionId=" + globals.sessionId + "&elapsedTime=" + globals.elapsedTime + "&probElapsedTime=" + globals.probElapsedTime + "&from=sat_hut&to=my_progress&topicId="+ globals.topicId +"&probId="+globals.probId + "&eventCounter="+ sysGlobals.eventCounter++ + "&var=b";
}

function switchLanguage() {
    alert("in switchLanguage");
    translateProb(globals, false);
//    globals.lastProbType = globals.probType;
//    globals.lastProbId = globals.probId;
//    document.location.href = "/"+sysGlobals.wayangServletContext + "/TutorBrain?action=navigation&sessionId=" + globals.sessionId + "&elapsedTime=" + globals.elapsedTime + "&probElapsedTime=" + globals.probElapsedTime + "&from=sat_hut&to=my_progress&topicId="+ globals.topicId +"&probId="+globals.probId + "&eventCounter="+ sysGlobals.eventCounter++ + "&var=b";
//    document.location.href = "#";
}

///////////////////////////////////////////////////////////////////////
////  Buttons on left menu (read prob, hint, replay hint, solve prob, show ex, show vid, formulas, glossary
////  call these functions
///////////////////////////////////////////////////////////////////////

function callReadProb() {
    debugAlert("In  callReadProb");
   	try {
	    if (isHTML5Problem())   {
	        incrementTimers(globals);
	        servletGet("ReadProblem", {probElapsedTime: globals.probElapsedTime});
	        document.getElementById(PROBLEM_WINDOW).contentWindow.prob_readProblem();
	    }
   	}
	catch(err) {
    	console.log(err.message);
	}    
}

// fields the click on the hint button.
function requestHint(globals) {
    if (isHTML5Problem()) {
        incrementTimers(globals);
        servletGetWait("Hint", {probElapsedTime: globals.probElapsedTime}, processRequestHintResult);
    }
}

// fields the click on the solve problem button
function requestSolution(globals) {
    if (isHTML5Problem())
    {
        incrementTimers(globals);
        servletGet("ShowSolveProblem", {probElapsedTime: globals.probElapsedTime}, processRequestSolutionResult);
    }
}

function showExample (globals) {
    if (isHTML5Problem()) {
        updateTimers();
        servletGet("ShowExample",{probElapsedTime: globals.probElapsedTime},processShowExample);
    }
}

function showVideo (globals) {
    if (isHTML5Problem()) {
        updateTimers();
        servletGet("ShowVideo",{probElapsedTime: globals.probElapsedTime },processShowVideo);
    }
}

function showLCList (globals) {
    if (isHTML5Problem()) {
        updateTimers();
        servletGet("ShowLCList",{probElapsedTime: globals.probElapsedTime },processShowLCList);
    }
}

function showGazeWandering (globals,gazeData) {
				
	var tgazeData = gazeData;
	tgazeData = tgazeData.replaceAll("{","%7B");
	tgazeData = tgazeData.replaceAll("}","%7D");							

    servletGet("GazeWandering",{probElapsedTime: globals.probElapsedTime, "gazeJSONData": tgazeData },processGazeWandering);
}

// TODO this should be changed to use a non-modal dialog
function showFormulas (globals) {
	var formURL = "img/g6refsheet.PNG"
	try {
		var tstandard = globals.standards;
		var grade = tstandard.substring(0,2);	
		if (grade === "8.") {
			formURL = "img/g8refsheet.PNG";
		}
		if (grade === "7.") {
			formURL = "img/g7refsheet.PNG";
		}
		if (grade === "5.") {
			formURL = "img/g5refsheet.PNG";
		}
	}
	catch(err) {
    	console.log(err.message);
	} 	
    utilDialogOpen(formURL, "Formulas");
    sendSimpleNotificationEvent(globals,"ShowFormulas");
//    window.open(formURL, "width=500, height=500");
}

function showUserPreferences (globals) {
    alert("Coming soon.  Editable user preferences!");
}

function showDashboard () {
    sendEndEvent(globals);
    globals.lastProbType = globals.probType;
    globals.lastProbId = globals.probId;
    document.location.href = "/"+sysGlobals.wayangServletContext + "/TutorBrain?action=Home&sessionId=" + globals.sessionId + "&elapsedTime=" + globals.elapsedTime + "&probElapsedTime=" + globals.probElapsedTime + "&probId="+ globals.probId + "&learningCompanion=" + globals.learningCompanion + "&eventCounter="+ sysGlobals.eventCounter++ + "&var=b";

}



//////////////////////////////////////////////////////////////////////
///  end of button handlers on left menu
//////////////////////////////////////////////////////////////////////




function processShowExample (responseText, textStatus, XMLHttpRequest) {
	
	var amsg = no_example_to_show;
	checkError(responseText);
    var activity = JSON.parse(responseText);
    if (activity.activityType === NO_MORE_PROBLEMS) {
        alert(amsg);
        return;
    }

    var mode = activity.mode; // this will be 'example'
    var form = activity.form; // quickAuth or null
    var pid = activity.id;
    var resource =activity.resource;
    var ans = activity.answer;
    var solution = activity.solution;

    globals.exampleProbType = activity.activityType;
    // solution is an array of hints.   Each hint has a label that we want to pull out and put in globals.example_hint_sequence

    if (form === 'quickAuth')
        showQuickAuthProblem(pid,solution,resource,mode,activity.questType);
    else showHTMLProblem(pid,solution,resource,MODE_EXAMPLE);

}


function processShowVideo (responseText, textStatus, XMLHttpRequest) {

	checkError(responseText);
    var activity = JSON.parse(responseText);
    var video = activity.video;
    // khanacademy won't play inside an iFrame because it sets X-Frame-Options to SAMEORIGIN.
    if (video != null) {
    	if (sysGlobals.exampleWindowActive) {
    		alert(please_watch_video);
    	}
        window.open(video, "width=500, height=500");
	}
    else {
    	alert(no_video_to_show);
    }
}

var selectedLC = "";
var lcName ="";
var radioButtons = null;

function clickLCButton() {
	
	if (!(radioButtons == null)) {
		for (const radioButton of radioButtons) {
		    if (radioButton.checked) {
		        selectedLC = radioButton.value;
		        break;
		    }
		}
		
		if (selectedLC == "") {
			document.getElementById('lcSubmitBtn').style.visiblity = "hidden";			
			alert("Select a learning companion or click 'close'");
		}
		else {
			document.getElementById('lcSubmitBtn').style.visiblity = "visible";
//			alert("LCid = " + selectedLC);
		}
	}

}


function processShowLCList (responseText, textStatus, XMLHttpRequest) {

	checkError(responseText);

	var lcArr = JSON.parse(responseText);

    var lcHtml = "<div >";
    var rowCount = 0;

    if (lcArr.LCList.length > 0) {
    
	    for (var i = 0; i <  lcArr.LCList.length; i++) {
	    	if ((i == 0) || (i%2 == 0)) {
	        	lcHtml = lcHtml  + "<div class='row'>";
	    	}
			rowCount = rowCount + 1;    		
	    	lcHtml = lcHtml  + "<div class='col-md-3'><label class='radio-inline'>";
	    	lcHtml = lcHtml  + "<input type='radio' name='optLC' id='LC" + i + "' value='" + lcArr.LCList[i].id + "' " + lcArr.LCList[i].lcname + ">";
	   		lcHtml = lcHtml  + 	"<img src=" +lcArr.LCList[i].url + lcArr.LCList[i].lcshortname + "/character.png width='120px' height='150px'>";    	
	   		lcHtml = lcHtml  + 	"<span style='display:block; text-align: center;'>" + lcArr.LCList[i].lcshortname + " " + lcArr.LCList[i].lang + "</span>";
	   		lcHtml = lcHtml  + 	"</label>";
	   		lcHtml = lcHtml  + 	"</div>";
		    if (rowCount == 3) {
	   	   		lcHtml = lcHtml  + 	"</div>";   
	   	   		rowCount = 0;
	   		}
	    }
	    lcHtml = lcHtml  + "</div>";
	    
	    document.getElementById("lcBody").innerHTML = lcHtml;
	    document.getElementById("lcBody").style.padding = "20px";
	
	    radioButtons = document.querySelectorAll('input[name="optLC"]');

    	addEventListener("click", clickLCButton, false);
	    
	    $('#lcModal').modal('show');
    }
}

function changeStudentLC() {
	
	console.log("Sending LC=" + selectedLC);
	
	/* change local variable */
    servletGet("ChangeStudentLC",{probElapsedTime: globals.probElapsedTime, "LCid": selectedLC, "sessionId":globals.sessionId },processChangeStudentLC);
		
}

function processChangeStudentLC (responseText, textStatus, XMLHttpRequest) {

	// force an idle LC message
	
	console.log(responseText);
    var resp = JSON.parse(responseText);
	globals.learningCompanion = resp.LCNew;
	showNewLearningCompanion(resp.LCNew);
}	
function processGazeWandering (responseText, textStatus, XMLHttpRequest) {


	if (!(responseText === "")) {

	
		console.log(responseText);
		
		var gazeJSON = JSON.parse(responseText);

		/* How algorithm this works:
		 * 	pauseDuration is used to keep track of what each intervention's pause should be from the start of the function
		 *    playSound if selected, has no pause duration and tells the next ibtervention to pause for at least 5000ms.
		 *    LCompanion if selected, has no pause if there was no sound selected, and a 5000ms pause if sound was selected, and it adds 5000 to the accumated puase for the next intervemtion
		 *    flashBox if selected has a pause for the accumulated intervention pauses so far, and adds 5000ms to the start-pause for this intervention and 5000 more for the end-pause, resuting in a 5000ms screen flash
		 *    texBox if selected has a pause for the accumulated intervention pauses so far, and puts up the confirmation box
		 */
		
		var pauseDuration = 0;
		if (gazeJSON.params.playSound === 1) {
			globals.gazeWanderingUI = "playSound<br>";
			var audio = new Audio('airport_sound.mp3');
			audio.play();
			pauseDuration = 5000;
		}
	
		if (gazeJSON.params.LCompanion === 1) {
			globals.gazeWanderingUI = "LC: " + gazeJSON.params.LCFilename + "<br>";
			var lc_url = sysGlobals.webContentPath + "LearningCompanion/" + globals.learningCompanion + "/" + gazeJSON.params.LCFilename + ".html";
			if (pauseDuration > 0) {
				var tpause = pauseDuration;
				pauseDuration = pauseDuration + 8000;
				setTimeout(function() { doLC(lc_url); }, tpause);			
			}
			else {
				pauseDuration = pauseDuration + 8000;
				loadIframe(LEARNING_COMPANION_WINDOW_ID, lc_url);
			}
		}
		
		if (gazeJSON.params.flashBox === 1) {
			if (pauseDuration > 0) {
				var tpause = pauseDuration;
				pauseDuration = pauseDuration + 5000;			
				setTimeout(function() { doBeforeFlash(); }, tpause);			
			}
			else {
				pauseDuration = pauseDuration + 5000;			
				globals.gazeWanderingUI = "flashBox<br>";		
				$('body').plainOverlay('show')
				setTimeout(function() { afterFlash("Hey"); }, 5000);
			}
		}
		
		if (gazeJSON.params.textBox === 1) {
			globals.gazeWanderingUI = "textBox<br>";
			var theText = gazeJSON.params.textBoxChoice;
			console.log(theText);
			if (pauseDuration > 0) {
				setTimeout(function() { doTextBox(theText); }, pauseDuration);			
			}
			else {
	
				swal({
					title: theText,
					confirmButtonColor: "#DD6B55", 
					confirmButtonText: "OK",
					closeOnConfirm: true,
				});
			}
		}
		
	}
}

function doBeforeFlash() {
	globals.gazeWanderingUI = "flashBox<br>";		
	$('body').plainOverlay('show');
	setTimeout(function() { afterFlash("Hey"); }, 5000);	
}

function afterFlash() {
	$('body').plainOverlay('hide');
	pauseDuration = 0;
}

function doLC(lc_url) {
	loadIframe(LEARNING_COMPANION_WINDOW_ID, lc_url);
	pauseDuration = 8000;
}

function doTextBox(theText) {

	swal({
		title: theText,
		confirmButtonColor: "#DD6B55", 
		confirmButtonText: "OK",
		closeOnConfirm: true,
	});
}


function openExampleDialog(solution){
    if (solution != 'undefined' && solution != null) {
        globals.exampleHintSequence = new Array(solution.length);
        for (i=0;i<solution.length;i++) {
            globals.exampleHintSequence[i] = solution[i].label;
        }
        globals.exampleCurHint = globals.exampleHintSequence[0];
    }
//    globals.probMode = mode;
    // show a div that contains the example.
//    $("#frameContainer").hide();              // hide the current problem
    $(EXAMPLE_CONTAINER_DIV_ID).dialog("open");        // TODO need to shrink height

}

// makes a beeping sound which is used at the beginning of each problem when the classconfig.soundSync is true.
// used in experiments where video capture needs to sync to a beeping sound.
function playBeep() {
    if (sysGlobals.soundSync) {
        var sound = document.getElementById("beeper");
        sound.play();
    }
}




function showQuickAuthProblem (pid, solution, resource, mode, questType) {
    hideHTMLProblem(false);
    globals.probMode = mode;
    transients.answersChosenSoFar=[];
    var probParams,hints ;
    if (globals.params)  {
        probParams = JSON.stringify(globals.params);
    }
    if (globals.hints)  {
        hints = JSON.stringify(globals.hints);
        var h = JSON.parse(hints);
    }
    if (mode != MODE_EXAMPLE) {
        globals.probElapsedTime = 0;
        globals.lastProbType = HTML_PROB_TYPE;
        globals.lastProbId = pid;
    }
    var isDemo = mode === MODE_DEMO || mode == MODE_EXAMPLE;
    if (isDemo)
        openExampleDialog(solution);

    if (!isDemo)  {
        loadIframe(PROBLEM_WINDOWID, getTutorServletURL("GetQuickAuthProblemSkeleton","&probId="+pid+"&mode=problem"));
//        loadIframe(PROBLEM_WINDOWID, "quickAuthProblem.jsp?stmt="+ encodeURIComponent(globals.statementHTML) +
//            "&figure=" +  encodeURIComponent(globals.questionImage) +
//            "&audio="+ encodeURIComponent(globals.questionAudio) +
//            "&hints=" + hints +
//            "&answers=" + encodeURIComponent(globals.answers) +
//            "&newAnswer=" + encodeURIComponent(globals.newAnswer ) +
//            "&answer=" + encodeURIComponent(globals.answer) +
//            "&units="+ encodeURIComponent(globals.units)  +
//            "&mode=" + encodeURIComponent(mode) +
//            "&questType=" + encodeURIComponent(questType) +
//            "&resource=" + encodeURIComponent(resource) +
//            "&probContentPath=" + encodeURIComponent(sysGlobals.problemContentPath ) +
//             "&problemParams="+ encodeURIComponent(probParams));
        $(PROBLEM_WINDOWID).attr("domain", sysGlobals.problemContentDomain);
    }
    else
        loadIframe(EXAMPLE_FRAMEID, getTutorServletURL("GetQuickAuthProblemSkeleton","&probId="+pid+"&mode=example"));
//        loadIframe(EXAMPLE_FRAMEID, "quickAuthProblem.jsp?stmt="+ encodeURIComponent(globals.statementHTML) +
//            "&figure=" +  encodeURIComponent(globals.questionImage) +
//            "&audio="+ encodeURIComponent(globals.questionAudio) +
//            "&hints=" + encodeURIComponent(hints) +
//            "&answers=" + encodeURIComponent(globals.answers) +
//            "&newAnswer=" + encodeURIComponent(globals.newAnswer ) +
//            "&answer=" + encodeURIComponent(globals.answer) +
//            "&units="+ encodeURIComponent(globals.units)  +
//            "&mode=" + encodeURIComponent(mode) +
//            "&questType=" + encodeURIComponent(questType) +
//            "&resource=" + encodeURIComponent(resource) +
//            "&probContentPath=" + encodeURIComponent(sysGlobals.problemContentPath ) +
//            "&problemParams="+ encodeURIComponent(probParams));

}

function showTopicIntroHTML (htmlResource) {
    hideHTMLProblem(false);
    globals.probMode = false;
    globals.probElapsedTime = 0;
    globals.lastProbType = HTML_PROB_TYPE;
    loadIframe(PROBLEM_WINDOWID, sysGlobals.problemContentPath + "/topicIntros/" + htmlResource + "/" + htmlResource+".html");
    $(PROBLEM_WINDOWID).attr("domain", sysGlobals.problemContentDomain);
}

function showHTMLProblem (pid, solution, resource, mode) {
    hideHTMLProblem(false);
    globals.probMode = mode;
    transients.answersChosenSoFar=[];
    if (mode != MODE_EXAMPLE) {
        globals.probElapsedTime = 0;
        globals.lastProbType = HTML_PROB_TYPE;
        globals.lastProbId = pid;
    }
    var isDemo = mode === MODE_DEMO || mode == MODE_EXAMPLE;
    if (isDemo)
        openExampleDialog(solution);

    // the name of the problem (e.g. problem090.html) is stripped off to find a directory (e.g. problem090)
    if (!isDemo)  {
        var dir = resource.split(".")[0];
        loadIframe(PROBLEM_WINDOWID, sysGlobals.problemContentPath + "/html5Probs/" + dir + "/" + resource);

//        $(PROBLEM_WINDOWID).load(function () {
//            var content = $(PROBLEM_WINDOWID).contents();
//            var body = content.find('body');
//            body.attr("style", "background-color: white !important");
//        });
        $(PROBLEM_WINDOWID).attr("domain", sysGlobals.problemContentDomain);
    }
    else {
        var dir = resource.split(".")[0];
        loadIframe(EXAMPLE_FRAMEID, sysGlobals.problemContentPath + "/html5Probs/" + dir + "/" + resource);

    }


}


// On EndProblem event we know the effort of the last problem so we get it and display it.
function processEndProblem  (responseText, textStatus, XMLHttpRequest) {
    checkError(responseText);
    var activity = JSON.parse(responseText);
    showEffortInfo(activity.effort);
}

// When a TimeoutIntervention gets a result from the server, this processes it.
// It can either be an intervention, a problem, or an indication to keep running the current TimeoutIntervention
//   6/15 DM - Not doing anything with this yet.  Melissa will need to complete this.
function processInterventionTimeoutResult (responseText, textStatus, XMLHttpRequest) {
    checkError(responseText);
    var activity = JSON.parse(responseText);
    var activityType = activity.activityType;

    if (activityType == INTERVENTION){
        if(activity.interventionType == SAME_INTERVENTION){
            setTimeout(continueInterventionTimeout, 5000);
        }
        else{
            processNextProblemIntervention(activity);
        }
    }
    else{
        processNextProblemResult(responseText,textStatus,XMLHttpRequest);
    }
}

// after a BeginProblem event is sent to server, we need to show any learning companion message it returns
function processBeginProblemResult (responseText, textStatus, XMLHttpRequest) {
    checkError(responseText);
    var activity = JSON.parse(responseText);
    showLearningCompanion(activity);
}

function checkError (responseText) {
    var json = JSON.parse(responseText);
    errType = json.error;
    if (errType) {
        if (json.fatal) {
            console.log("Fatal Error reported by server " + json.message);
            alert("Fatal Error reported by server.  Restart Mathspring in your browser. \n" + json.message);
            // If its fatal we change the page to the login screen unless this is a dev env where we'd want to stay and debug it.
            if (!sysGlobals.isDevEnv)
                document.location.href = "/"+sysGlobals.wayangServletContext + "/WoLoginServlet?action=LoginK12_1";
            return true;
        }
        else {
            console.log("Error reported by server " + json.message);
            alert("Error reported by server.  You may continue to use Mathspring. \n" + json.message);
            return false;
        }

    }
    return false;
}

function checkTranslateProbError (responseText) {
    var json = JSON.parse(responseText);
    errType = json.error;
    if (errType) {
        if (json.fatal) {
            console.log("Fatal Error reported by server " + json.message);
            alert("Fatal Error reported by server.  Restart Mathspring in your browser. \n" + json.message);
            // If its fatal we change the page to the login screen unless this is a dev env where we'd want to stay and debug it.
            if (!sysGlobals.isDevEnv)
                document.location.href = "/"+sysGlobals.wayangServletContext + "/WoLoginServlet?action=LoginK12_1";
            return true;
        }
        else {
            console.log("Error reported by server " + json.message);
            alert(errType + ': ' + json.message + '  Click this message to continue.');
            return false;
        }

    }
    return false;
}

var altProbId = 0;

function processNextProblemResult(responseText, textStatus, XMLHttpRequest) {
    $("#next_prob_spinner").show();
   	$("#nextProb").addClass("disable_a_href");
   	checkTranslateProbError(responseText);
    $(PROBLEM_WINDOWID).attr("src","");
    // Replaceing the example div for the same reason as the above.
    $(EXAMPLE_CONTAINER_DIV_ID).html('<iframe id="'+EXAMPLE_FRAME+'" name="iframe2" width="650" height="650" src="" frameborder="no" scrolling="no"></iframe>');
    var activity = JSON.parse(responseText);
    console.log(responseText);

    if (activity.isTranslation == 1) {
    	globals.isTranslation = 1;
    	globals.probLangIndex = 1;    	
    }
    else {
        globals.untranslateProbid = activity.id;
    	globals.probLangIndex = 0;    	    	
    }
    altProbId = activity.altProbId;
    
    if( typeof altProbId === 'undefined' || altProbId === null ) {
    	altProbId = 0;
    }
    else {
    	altProbId = Number(activity.altProbId);
    }
    console.log("altProbId=" + altProbId);
    console.log("pageLangIndex=" + pageLangIndex);
    console.log("globals.experiment=" + globals.experiment);
	if (globals.experiment.indexOf("multi-lingual") < 0) {
		document.getElementById("translateProb").style.display = "none";
	}
	else {
		if (altProbId > 0) {
			
			// Show 'Translate problema' button using pageLangIndex
//			if (pageLangIndex == 0) {
//				document.getElementById("translateProbText").innerHTML =  translate_this_problem_alt;
//			}
//			else {
//				document.getElementById("translateProbText").innerHTML =  translate_this_problem_pri;				
//			}
			// Add a mouseover event listener
			document.getElementById("translateProb").addEventListener('mouseover', () => {
			  // Change the button's background color
  				document.getElementById("translateProb").style.backgroundColor = '#92DDA3';
			});

			// Add a mouseout event listener
			document.getElementById("translateProb").addEventListener('mouseout', () => {
			  // Change the button's background color back to its original color
				document.getElementById("translateProb").style.backgroundColor = "White";
			});
			$("#translateProb").removeClass("disable_a_href");
			$("#translateProbWrapper").removeClass("not-allowed");
			document.getElementById("translateProb").style.background = "White";
		    if (activity.isTranslation == 1) {
				document.getElementById("translateProbText").innerHTML = translate_to_english;
		    }
		    else {
				document.getElementById("translateProbText").innerHTML =  translate_to_spanish;		    	
		    }
		    $("trans_prob_spinner").show();
		}
		else {
			document.getElementById("translateProb").style.color = "Black";
			document.getElementById("translateProb").style.background = "LightGray";
			$("#translateProb").addClass("disable_a_href");
			$("#translateProbWrapper").addClass("not-allowed");
			document.getElementById("translateProbText").innerHTML = no_translation;
		}
		document.getElementById("translateProb").style.display = "block";
	}
    var mode = activity.mode;
    var activityType = activity.activityType;
    var type = activity.type;

    if (activityType == NO_MORE_PROBLEMS || activityType == NO_MORE_CHALLENGE_PROBLEMS || activityType == NO_MORE_REVIEW_PROBLEMS)  {
        // send EndEvent for previous problem
        sendEndEvent(globals);
        hideHTMLProblem(false);
        var url = activity.endPage;
        loadIframe(PROBLEM_WINDOWID,url);
    }
    else  {
        var pid = activity.id;
        var resource =activity.resource;

        var topic = "";
        
 //       if (activity.topicName != null && activity.topicName != undefined && activity.topicName != "" && activity.topicName != " ") {
        var topicName = activity.topicName;   
        
		if (activity.isTranslation == 1) {
			probLang = "es"
		}
		else {
			probLang = "en"
		}
            
        var standards = activity.standards;
        var varBindings = activity.parameters;
        setGlobalProblemInfo(activity);

        showProblemInfo(pid,resource,topicName,standards);
        showEffortInfo(activity.effort);
        if (globals.showAnswer) {
            // If server shuffles the answer to a different position, then newAnswer contains this position
            if (activity.newAnswer != null && activity.newAnswer != undefined) {
                globals.newAnswer = activity.newAnswer;
                globals.answer = activity.answer
                showAnswer(activity.newAnswer);
                showVarBindings(varBindings);
            }
            else {
                globals.newAnswer = activity.newAnswer;
                globals.answer = activity.answer;
                // The answer may be in the var bindings  as  $Best_Answer
                if (varBindings != undefined) {
                    alert("Varbindings are:"+ varBindings);
                    var bestAns = varBindings.$Best_Answer;
                    alert("Best answer is: "+ bestAns);
                    showAnswer(bestAns);
                    showVarBindings(varBindings);
                }
                else {
                    showAnswer(activity.answer);
                    showVarBindings(varBindings);
                }
            }

        }
        globals.resource = activity.resource;
        // hardwired instructions since what comes from the server is mostly useless.
//        globals.instructions = activity.instructions;
        globals.instructions =  "Read, think and try to solve this problem (please use paper and pencil to write down the solution as many of these are hard!). Use the tools on the left to help you solve the problem (read aloud, hints, examples, videos). Don't be afraid of asking for help! We know that using the help tools is how you get to learn.";
        updateTimers();
        globals.probType = activityType;

        // If its an intervention
        if (isIntervention()) {
            processNextProblemIntervention(activity);

        }

        else if (isHTML5Problem()) {
            // send EndEvent for previous problem
            sendEndEvent(globals);
//            showProblemInfo(pid,resource);
            // formality problems call the servlet with their own begin /end events
            var solution = activity.solution;
            globals.params = activity.parameters;
            globals.oldAnswer = activity.oldAnswer;
            if (mode == MODE_DEMO) {
                globals.exampleProbType = activityType;
            }
            if (activity.form==="quickAuth") {
                globals.form = "quickAuth";
                globals.statementHTML = activity.statementHTML;
                globals.questionAudio = activity.questionAudio;
                globals.questionImage = activity.questionImage;
                globals.hints = activity.hints;
                globals.answers = activity.answers;
                globals.units = activity.units;
            }
            else {
                globals.form = null;
                globals.statementHTML = null;
                globals.questionAudio = null;
                globals.questionImage = null;
                globals.units = null;
                globals.hints = null;
                globals.answers = null;
            }
            playBeep();
            sendBeginEvent(globals,pid,mode, processBeginProblemResult);
            if (activity.form==='quickAuth')
                showQuickAuthProblem(pid,solution,resource,mode,activity.questType);
            else showHTMLProblem(pid,solution,resource,mode);
            if (activity.intervention != null) {
                processNextProblemIntervention(activity.intervention);
            }
            globals.topicId = activity.topicId;
            globals.probId = pid;
        }
        // DM 6/11/15 topic intros are now interventions

//        else if (activityType === TOPIC_INTRO_PROB_TYPE) {
//            globals.instructions =  "This is an introduction to a topic.  Please review it before beginning work by clicking the new-problem button.";
//
//            // send EndEvent for previous problem
//            sendEndEvent(globals);
//            globals.probElapsedTime = 0;
//            sendBeginEvent(globals);
//            showTopicIntro(resource,activity.topicName);
//            globals.topicId = activity.topicId;
//            globals.probId = pid;
//
//        }
        // We got XML that we don't understand so it must be an intervention.   We call Flash and pass it the XML
        else {
            globals.lastProbType = HTML_PROB_TYPE;
            debugAlert('Unknown return result: ' + activity);
            globals.topicId = activity.topicId;
            globals.probId = pid;
//            document.location.href = sysGlobals.flashClientPath + "?sessnum=" + globals.sessionId + "&sessionId=" + globals.sessionId + "&learningHutChoice=true&elapsedTime=" + globals.elapsedTime + "&learningCompanion=" + globals.learningCompanion + "&intervention=" + encodeURIComponent(activityXML) + "&mode=intervention"; // &topicId=" + topicId;
        }
        // Because beginProblem events sometimes return a lc message that is important we don't want this to play an idle message right
        // on top of it.    This is because the rule-based lc messages are combining with lc messages coming from the LC classes that are plugged into the pedagogy.
        // Those classes need to no longer return any messages because they are stepping on the ones from the rule-base.   However this issue is kind of bug in that
        // NextProblem is sent and gets back an 'idle' message, then EndProblem is sent then BeginProblem is sent (which gets back a real message) but then the last thing done by in the NextProblem processing is
        // to show the lc idle message.   A global var says if the lc is rule-based or programmatic in its message selection.   If it's programmatic we will show messages
        // in this position - o/w all messages must be returned by rules on events that are documented to support running rules (i.e. NextProblem is not one).
        if (globals.learningCompanionMessageSelectionStrategy == "programmatic")
            showLearningCompanion(activity);
    }
    showHourglassCursor(false);
    
    if(isHTML5Problem()){
    	setTimeout(function(){
    		document.getElementById(PROBLEM_WINDOW).contentWindow.postMessage(globals,"*"); //send the message and target URI
    	},6000);
    }
    
	 $("#next_prob_spinner").hide();
	 
	 $("#nextProb").removeClass("disable_a_href");
//	   	if (globals.experiment == "multi-lingual") {
//	   		$("#translateProb").removeClass("disable_a_href");
//			document.getElementById("translateProb").style.background = 'white';
//	   	}
}

function newBrowserWindow (url,w, h) {
    posx = window.screenLeft + window.innerWidth - w;
    posy = window.screenTop + window.innerHeight - h;
    newwindow=window.open(url,'name','height=600,width=260,location=no,scrollbars=no,left='+posx+',top='+posy);
    if (window.focus) {newwindow.focus()}
    return false;
}

//  Called when the learning companion animations end
function learningCompanionDone () {
    // temporarily commented out because some of the lc animations are actually calling this
    // alert("This idiot is done jabbering!") ;
    // $("#"+LEARNING_COMPANION_CONTAINER).dialog('close');
}


// unused.  If lc were a simple div and not an iframe, we'd load it into the div like this.
function loadLCFile (url) {
    $("#Container").load(url);
}

// When a non-idle video is shown, we expand the LC window (in case it was shrunk or closed).
// After 10 seconds we reveal the close button so the user can get rid of the window if necessary
function successfulLCResult (url) {
    console.log("Showing LC " + url);
    // if the lc dialog is minified, expand it to original size if not an idle message.
    if (url.indexOf('idle.html') == -1) {
        expandLC();
        loadIframe(LEARNING_COMPANION_WINDOW_ID, url);
    }
}

// When the iframe is done loading the HTML file, we need to go into it and find the canvas element and change its margins because
// its not well laid out to fit inside the iframe without having some gray space around it.
function lcLoaded () {
/*   var iframe = document.getElementById(LEARNING_COMPANION_WINDOW);
    var innerDoc = iframe.contentDocument || iframe.contentWindow.document;
    var canvs = innerDoc.getElementsByTagName("canvas");
    var canv = canvs[0];
    // TODO not working in some cases.   The iframe probably has no content at the time this was called
    if (canv)
        canv.style.margin = "-10px -10px";*/
}

function failureLCResult (url) {
    alert("Cannot find the learning companion URL " + url + "  Message: " + transients.learningCompanionTextMessage);
}

// unused
function addCloseButton () {
    $(".ui-dialog-titlebar-close").show();
}

// if the learning companion is being shown we need to set it to the correct state from its given state.
function expandLC () {
    switch ($("#"+LEARNING_COMPANION_CONTAINER).dialogExtend("state")) {
        case "minimized":
            $("#" + LEARNING_COMPANION_CONTAINER).dialogExtend("restore");
            break;
        default:
            if  ( ! $("#" + LEARNING_COMPANION_CONTAINER).dialog("isOpen")) {
                $("#"+LEARNING_COMPANION_CONTAINER).dialog("open");
                $("#"+LEARNING_COMPANION_CONTAINER).dialog( "option", "width", LCDIALOG_WIDTH );
                $("#"+LEARNING_COMPANION_CONTAINER).dialog( "option", "height", LCDIALOG_HEIGHT );
            }
    }

}

function showLearningCompanion (json) {

    var files = json.learningCompanionFiles;
    var file = undefined;
    var url;
    transients.learningCompanionTextMessage = json.lcTextMessage;
    if (files != undefined && files != null)  {
        if (files instanceof Array)
            file = files[0];
        else file = files;
    }
    else return;

    try {
	    if (file != globals.learningCompanionClip) {
	        globals.learningCompanionClip = file;
	        console.log("LC=" + file);
	        if (lcName.length > 0) {
		        var offset = file.indexOf("/");
		        file = lcName + file.substring(offset)
	        }
		    if ((file.indexOf("Jane") >= 0) || (file.indexOf("Jake") >= 0)) {
	            url = sysGlobals.problemContentPath + "/LearningCompanion/" + file;         	
	            httpHead(url, successfulLCResult, failureLCResult);
	        }
	        else {
	        	url = sysGlobals.webContentPath2 + "LearningCompanion/" + file;
	            httpHead(url, successfulLCResult, failureLCResult);
	        }
	    }
    }
	catch(err) {
    	console.log(err.message + url);
        $(LEARNING_COMPANION_WINDOW_ID).attr("src","")
	}    
}

function showNewLearningCompanion (lcNew) {

    var url;

    try {
	    if ((lcNew == "Jane") || (lcNew == "Jake")){
            url = sysGlobals.problemContentPath + "/LearningCompanion/" + lcNew + "/idle.html";         	
            httpHead(url, successfulLCResult, failureLCResult);
        }
        else {
        	url = sysGlobals.webContentPath2 + "LearningCompanion/" + lcNew + "/idle.html";
            httpHead(url, successfulLCResult, failureLCResult);
        }
    }
	catch(err) {
    	console.log(err.message + url);
        $(LEARNING_COMPANION_WINDOW_ID).attr("src","")
	}   
    expandLC();
    loadIframe(LEARNING_COMPANION_WINDOW_ID, url);

}


// when the lc dialog is closed we need remove the src of the iframe because
// jquery retriggers the HTML5 file within to begin playing again.
function lcBeforeClose (event, ui) {
    // alert("Closing the LC Iframe");
    $(LEARNING_COMPANION_WINDOW_ID).attr("src","")

}


function showMPP () {
    $("#mppButton").show();
}

function hideMPP () {
    $("#mppButton").hide();
}

function hideHTMLProblem (isHide) {
    if (isHide)
        $(PROBLEM_WINDOWID).hide();
    else
        $(PROBLEM_WINDOWID).show();
}



///////////////////////////////////////////////////////////////////////
//////   Main event loop established by tutorMain.hsp in its jquery onReady function
//////////////////////////////////////////////////////////////////////////

function exampleDialogCloseHandler () {
    if (isDemoMode())   {
        // turn off demo mode
        globals.probMode = null;
        globals.probType = null;
        globals.exampleProbType = null;
        nextProb(globals);
    }
    sysGlobals.exampleWindowActive = false;
    globals.probMode = MODE_PRACTICE;
    //sendEndEvent(globals);

}


var DELAY = 700, clicks = 0, timer = null;
function clickHandling () {
    var width = Math.min(document.documentElement.clientWidth, window.innerWidth || 0);
    var height = Math.min(document.documentElement.clientHeight, window.innerHeight || 0);
    var lcx = width - 268 - 10;  // subtract off the width of the learning companion div + some extra for its close button
    var agreed = false;
    globals.clickTime = new Date().getTime();
    // We turn on mouse tracking if the mouseSaveInterval is > 0.  mouseSaveInterval is given in # of seconds between
    // saves to the server.
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


    $("#" + LEARNING_COMPANION_CONTAINER).dialog({
            classes: {
                "ui-dialog-titlebar": "lcdialogTitleBar"
            },
            autoOpen: false,
            resizable: true,
            minHeight: 100,
            minWidth: 100,
            width: LCDIALOG_WIDTH,
            height: LCDIALOG_HEIGHT,
            closeOnEscape: false,
            position: ['right', 'bottom'],
            show: {
                effect: "blind",
                duration: 1000
            },
            // open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
            // when close dialog remove iframe contents so it doesn't trigger playing it again (a bug)
            beforeClose: lcBeforeClose,
            hide: {
                effect: "blind",
//                effect: { effect: "scale", scale: "both", percent: "5", origin: "bottom", direction: "vertical" },
                duration: 1000
            }

        }
    ).dialogExtend({
        "closable": true,
        "maximizable": false,
        "minimizable": true,
        "collapsable": false,
        "dblclick": "collapse",
        "titlebar": "transparent",
        "minimizeLocation": "right",
        "icons": {
            "close": "ui-icon-circle-close",
            "maximize": "ui-icon-circle-plus",
            "minimize": "ui-icon-circle-minus",
            "collapse": "ui-icon-triangle-1-s",
            "restore": "ui-icon-bullet"
        },
        "load": function (evt, dlg) {
            return;
        },
        "beforeCollapse": function (evt, dlg) {
            return;
        },
        "beforeMaximize": function (evt, dlg) {
            return;
        },
        "beforeMinimize": function (evt, dlg) {
            return;
        },
        "beforeRestore": function (evt, dlg) {
            return;
        },
        "collapse": function (evt, dlg) {
            return;
        },
        "maximize": function (evt, dlg) {
            return;
        },
        "minimize": function (evt, dlg) {
            return;
        },
        "restore": function (evt, dlg) {
            return;
        }
    });

    // $("#"+LEARNING_COMPANION_CONTAINER + " .ui-dialog-titlebar").css({
    //     "background-color" : "transparent",
    //     "border" : "0px none"
    // });

    // $( "#"+LEARNING_COMPANION_CONTAINER ).on( "dialogopen", function( event, ui ) {} );
    //   This will hide the title bar on just the lc dialog but we want the close button too so can't
    // $(".ui-dialog-titlebar").hide();

//    $("#"+LEARNING_COMPANION_CONTAINER).draggable(
//        {
//        start: function(e, ui) {
//            alert("start");
//
//        },
//        stop: function(e, ui) {
//            alert("stop");
//        }
//    });
    $("#solveNext").click(function () {
        solveNextHint();
    });

    $("#nextProb").click(function () {
    	$("#next_prob_spinner").show();
        if (!isWaiting()) {
        	globals.probLangIndex = 0;
            nextProb(globals);
        }
        $("#next_prob_spinner").hide();
    });
    
    $("#translateProb").click(function () {
       	if (globals.experiment.indexOf("multi-lingual") >= 0) {
	    	$("#trans_prob_spinner").show();
	        if (!isWaiting()) {
	        	var saveId = globals.probId;
	        	if (globals.isTranslation == 1) {
	        		globals.probId = globals.untranslateProbid;
	        	}
	            translateProb(globals);
	            globals.probId = saveId;
	        	if (globals.isTranslation == 1) {
		        	globals.isTranslation = 0;
		        	globals.probLangIndex = 0;    	
	        	}
	        }
	        $("#trans_prob_spinner").hide();
	    }
    });
    $("#read").click(function () {
        callReadProb()
    });

    $("#hint").click(function () {
        clicks++;  //count clicks
        if(clicks === 1) {
            timer = setTimeout(function() {
//                alert("Single click");
                requestHint(globals);  // perform single-click action
                clicks = 0;             //after action performed, reset counter

            }, DELAY);

        } else {

            clearTimeout(timer);    //prevent single-click action
//            alert("Double click");
            requestHint(globals);  //perform double-click action
            clicks = 0;             //after action performed, reset counter
        }
    })
    $("#hint").dblclick(function (e) {
        e.preventDefault();
    })


    $("#replay").click(function () {
        callProblemReplayHint()
    });
    $("#solve").click(function () {
        requestSolution(globals)
    });
    $("#example").click(function () {
        showExample(globals)
    });
    $("#video").click(function () {
        showVideo(globals)
    });
    $("#showLCList").click(function () {
        showLCList(globals)
    });
    $("#formulas").click(function () {
        showFormulas(globals)
    });
    $('#'+INSTRUCTIONS_DIALOG).dialog({
//        autoOpen: ((globals.instructions == "") ? false : true),
        autoOpen: false,
        width: 600,
        buttons: {
            "Close": function () {
                $(this).dialog("close");
            }
        }
    });
    $("#"+SELECT_PROBLEM_DIALOG).dialog( {
        autoOpen: false,
        modal: true,
        width: 600,
        height: 600,
        buttons: [
            {
                text: "Cancel",
                click: function() {
                    $( this ).dialog( "close" );
                }
            }
        ]
    });
    $( "#"+INTERVENTION_DIALOG).dialog( {
        autoOpen: false,
        modal:true,
        width: 500,
        height:500,
        open: function (event, ui) {
            $(".ui-dialog-titlebar-close", this.parentNode).hide();
        } ,
        buttons: [
            {
                id: "yesButton",
                text: "Yes",
                click: interventionDialogYesClick
            },
            {
                id: "noButton",
                text: "No",
                click: interventionDialogNoClick
            },
            {
                id: "ok_button",
                text: "OK",
                click: interventionDialogOKClick
            }
        ]
    });

    $( EXAMPLE_CONTAINER_DIV_ID).dialog({
        autoOpen:false,

        modal:true,
        width:715,
        height:500,
		position: ['center',0],
        open: function (event, ui) {
            $(".ui-dialog-titlebar-close", this.parentNode).hide();
            sysGlobals.exampleWindowActive = true;
            $(EXAMPLE_CONTAINER_DIV_ID).css('overflow', 'scroll');
            var id_exists = document.getElementById('play_button');
            if (id_exists)  {
                if (pageLangIndex == 0) {
                	$("#play_button").text(example_problem_play_hints);
                	//document.getElementById('pulsate_play_button').text = example_problem_play_hints;
            	}
        		else {
                	$("#play_button").text(alt_example_problem_play_hints);
//        			document.getElementById('pulsate_play_button').text = alt_example_problem_play_hints;    	
        		}
                document.getElementById('play_button').id = 'pulsate_play_button';
            }
            
			
			$("#exampleContainer").css('height','600px');
			$("#exampleContainer").css('width','auto');
			$("#pulsate_play_button").css({
				position: 'absolute',
    			top: '6%',
    			left: '43%'
			});
        },
        close: function () { exampleDialogCloseHandler(); } ,
        buttons: [
            {
                id: 'play_button',
                text: "Play next step",
                click: function() {
                    var id_exists = document.getElementById('pulsate_play_button');
                    if (id_exists)  {
                        document.getElementById('pulsate_play_button').id = 'play_button';
                    }
                    var txt = $("#play_button").text();
                    if (txt == example_problem_done) {
                		$( this ).dialog( "close" );
                		return;
                    }
                    clicks++;  //count clicks
                    if(clicks === 1) {
                        timer = setTimeout(function() {
                     
                            example_solveNextHint();  // perform single-click action
                            var index = globals.exampleHintSequence.indexOf(globals.exampleCurHint);
                            if (index == -1) {
                            	$("#play_button").text(example_problem_done);
                            }
                            clicks = 0;             //after action performed, reset counter

                        }, DELAY);

                    } else {

                    	clearTimeout(timer);    //prevent single-click action
                    	example_solveNextHint();  //perform double-click action
                        var index = globals.exampleHintSequence.indexOf(globals.exampleCurHint);
                        if (index == -1) {
                        	$("#play_button").text(example_problem_done);
                        }
                        clicks = 0;             //after action performed, reset counter
                    }
                },
                dblclick: (function (e) {
                    e.preventDefault();
                })
            }
        ]
    });
    $("#"+UTIL_DIALOG).dialog({
        autoOpen: false,
        modal:false,
        width:750,
        height:700,

        buttons: [
            {
                text: "Close",
                click: function() {
                    $( this ).dialog( "close" );
                }
            }
        ]
    });
    $("#selectProb").click(selectProblemDialog);
    $("#prefs").click(showUserPreferences);
    $("#home").click(showDashboard);
    $("#instructions").click(instructions);

    $("#myProg").click(function () {
        myprogress(globals)
    });
    
    $("#switchLanguage").click(function () {
        switchLanguage(globals)
    });

}

function showHTMLProblemAtStart () {
    var activity = globals.activityJSON;
    var mode = activity.mode;
    var isExample =  (mode == MODE_DEMO || mode == MODE_EXAMPLE);
    var pid = activity.id;
    var resource = activity.resource;

    var topicName = activity.topicName;   
    var topic = topicName.en;
    if (lang == "es") {
        topic = topicName.es;
    }        

    var topicName = topic;
    var standards = activity.standards;
    var solution = activity.solution;
    setGlobalProblemInfo(activity);
    var activityType = activity.activityType;
    var ans = activity.answer;

    var form = activity.form;
    if (form==="quickAuth") {
        globals.isQuickAuth = true;
        globals.statementHTML = activity.statementHTML;
        globals.questionAudio = activity.questionAudio;
        globals.questionImage = activity.questionImage;
        globals.hints = activity.hints;
        globals.answers = activity.answers;
    }
    globals.params = activity.parameters;
    if (isExample) {
        globals.exampleProbType = activityType;
    }
    // THe resumeProblem flag is on if a previous problem was unsolved and the user is returning to it
    if (globals.resumeProblem) {
        globals.resumeProblem = false;
        sendResumeProblemEvent(globals);
    }
    // end the last problem
    else {
        if (globals.lastProbId != -1)
            sendEndEvent(globals);
        playBeep();
        sendBeginEvent(globals,pid,mode,processBeginProblemResult) ;
    }
    showProblemInfo(pid,resource,topicName,standards);

    if (globals.showAnswer)
        showAnswer(ans);
    if (form==="quickAuth")
        showQuickAuthProblem(pid,solution,resource,mode,activity.questType);
    else showHTMLProblem(pid,solution,resource, mode);
}

// TODO need to create a test for this.
// This came up after being in a problem and attempting it (correctly), going to MPP, then return to hut.
function showInterventionAtStart () {
    if (sysGlobals.isDevEnv)
        ;
//        alert("Returning to Mathspring and playing intervention: " + globals.activityJSON);

//        var ajson = globals.activityJSON;
//        var qt = '\\"';
//        var re = new RegExp(qt,'g');
//        var cleanJSON = ajson.replace(re,'\\\"');
    var activity = globals.activityJSON;
    if (sysGlobals.isDevEnv)
        ;
//            alert("Activity is " + activity);
    if (globals.lastProbId != -1)
        sendEndEvent(globals);
    processNextProblemIntervention(activity);

}


function tutorhut_main(g, sysG, trans, learningCompanionMovieClip) {
    globals = g;
    sysGlobals = sysG;
    transients = trans;
    var d = new Date();
    var startTime = d.getTime();
    toggleSolveDialogue(false);
    setMPPVisibility(globals.showMPP);
    showUserInfo(globals.userName);
    clickHandling();
    var d = new Date();
    globals.clock = d.getTime();
    console.log("tutorHut main " + globals.isBeginningOfSession)
    // If this is the first time the tutor is loaded (i.e. from a login) then we send a navigation event so the server initializes
    // correctly based on the student now being in the tutor page.
    // If not the first time, then we are re-entering the tutor page and we want to show a particular problem or intervention
    if (globals.isBeginningOfSession) {
        console.log("next problem " + globals.isBeginningOfSession)
        nextProb(globals, globals.isBeginningOfSession);
    }
    else if (globals.activityJSON != null && globals.probType === HTML_PROB_TYPE) {
        showHTMLProblemAtStart();
    }
    else if (globals.activityJSON != null && globals.probType === INTERVENTION) {
        showInterventionAtStart();
    }
    else {
        if (sysGlobals.isDevEnv)
            alert("making a cyclic call to nextprob from a new mathspring.jsp");
        nextProb(globals);
    }
    if (learningCompanionMovieClip != '')  {
        $("#"+LEARNING_COMPANION_CONTAINER).dialog("open");
    }
    globals.isBeginningOfSession=false;

    configureModalWindowForPopup();

}