/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 8/26/13
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 * 
 * Frank 04-24-2020 issue #16 removed done button from example dialog
 * Frank 04-27-2020 Issue #16 handle problems with duplicate hint labels hanging the example processing"
 * Frank 05-01-2020 Issue #16R2 cut and paste Typo caused catastrophic failure
 * Frank 12-03-2020 Issue #317 Stop all example hint audios before playing the next hint audio
 */

function requestSolution(globals) {

        incrementTimers(globals);
        servletGet("ShowSolveProblem", {probElapsedTime: globals.probElapsedTime}, processRequestSolutionResult);
}


// we eliminated the div that was a control for "solve problem".
// TODO Remove processing of Solve Problem.  We eliminated that menu item
function toggleSolveDialogue(show) {
//    if (show)
//        $("#SolveDiv").show();
//    else
//        $("#SolveDiv").hide();
}

function setMPPVisibility (showMPP) {
    if (!showMPP)
        hideMPP();

}



function processRequestSolutionResult (responseText, textStatus, XMLHttpRequest) {
    checkError(responseText);
    //console.log("processRequestSolutionResult Server returns " + responseText);
    var activity = $.parseJSON(responseText);
    var soln = activity.solution;
    var labels = new Array();
    for (i = 0; i < soln.length; i++) {
        labels[i] = soln[i].label;
    }
    //console.log("Here are the hints " + labels);
    callProblemPlayHintSequence(labels);
}

// No longer getting XML back from server.   This is here only so that we have some code that demonstrates
// XML parsing in case we ever need it.
function processRequestSolutionResult2(responseText, textStatus, XMLHttpRequest) {
    checkError(responseText);
    //console.log("processRequestSolutionResult2 Server returns " + responseText);
//    var re = new RegExp("&hint=(\w*)&*");  // collect the label out of the param string
    var solutionXML = getXMLElement(responseText, "solution");
//    debugAlert("XML for hint " + hintXML);
    if (solutionXML != null) {
        var dom = $.parseXML(solutionXML),
            $xml = $(dom),
            $hints = $xml.find("hint");
        var labels = new Array();
        for (i = 0; i < $hints.length; i++) {
            labels[i] = $hints[i].textContent;
        }
        // var labels = $hints.text();
        //console.log("Here are the hints " + labels);
        callProblemPlayHintSequence(labels);
    }

}


function processRequestHintResult(responseText, textStatus, XMLHttpRequest) {
    checkError(responseText);
//    var re = new RegExp("&hint=(\w*)&*");  // collect the label out of the param string
    var hint = JSON.parse(responseText);
    var label = hint.label;
    var id = hint.id;
    if (globals.numHintsSeen < globals.numHints)  {
        globals.numHintsSeen++;
        displayHintCount();
    }
    //console.log("processRequestHintResult hint returned is ID: " + id + " label: " + label);
    globals.curHint = label;
    callProblemPlayHint(label);
    showLearningCompanion(hint);
}


function callProblemReplayHint() {
    // These Composition IDs are different for each problem.   So we need to figure out where to get from.
    //    Comp.getStage().play(hintLabel) ;

    if (!(globals.curHint == null)) {
        playHint(globals.curHint);    	
    }
    else {
    	alert(no_hints_seen_yet);
    }
    
}

// This function is called when the hint link is clicked on.
// It will call the hint function that lives within the document
// in the childWindow.  In order to access this function it has to
// read the DOM for that page and find the hint function and then
// pass it the hint label so it can jump to that label and start
// playing
function callProblemPlayHint(hintLabel) {
    //console.log("In callProblemPlayHint with " + hintLabel);
    // These Composition IDs are different for each problem.   So we need to figure out where to get from.
    //    Comp.getStage().play(hintLabel) ;
    if (!(globals.curHint == null)) {
        playHint(globals.curHint);    	
    }
    else {
    	alert(hint_not_found);
    }
}

function playHint (hintLabel) {
	if (isHTML5Problem()) {
        var innerdoc =  document.getElementById(PROBLEM_WINDOW).contentWindow;
        innerdoc.prob_playHint(hintLabel);
    }
}

var exampleHintStatus = true;
function example_playHint(hintLabel) {
	
   	try {
        if (isHTML5Example())
            document.getElementById(EXAMPLE_FRAME).contentWindow.prob_playHint(hintLabel);
	}
	catch(err) {
        exampleHintStatus = false;
    	console.log(err.message);
	}
}

function solveNextHint () {
    var index = globals.hintSequence.indexOf(globals.curHint);
    if (index < globals.hintSequence.length) {
        playHint(globals.curHint);
        globals.curHint = globals.hintSequence[index+1];
    }
    else {
        toggleSolveDialogue(false);
    }
}

function example_solveNextHint () {
    var index = globals.exampleHintSequence.indexOf(globals.exampleCurHint);
    if (index < globals.exampleHintSequence.length) {
    	var currHint = globals.exampleCurHint;
        globals.exampleCurHint = globals.exampleHintSequence[index+1];
        if (globals.exampleHintSequence[index+1] == globals.exampleHintSequence[index]) {
        	console.log("Duplicate step label");        	
        	globals.exampleCurHint = "undefined";
        }
        stopExampleAudio();
        exampleHintStatus = true;
        example_playHint(currHint);
        if (!exampleHintStatus) {
        	alert(hint_not_found);
        	globals.exampleCurHint = "undefined";
        }

    }
}


// We attempted to show  a dialog box after each hint asking whether the user wanted the next hint but the
// dialog comes up immediately.
function callProblemPlayHintSequence(hintSequence) {
    toggleSolveDialogue(true);
    globals.hintSequence = hintSequence;
    globals.curHint = hintSequence[0];
//    playHint(globals.curHint);
}

function stopExampleAudio(){
	var sounds = document.getElementsByTagName("audio");
    for(i = 1; i < sounds.length; i++) {
        if(sounds[i].readyState > 0) {
            sounds[i].pause();
            sounds[i].currentTime = 0;
        }
    }
}
