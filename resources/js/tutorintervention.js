/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 11/15/13
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 * 
 * Frank	04-26-21	Issue# 432 - add ShowVideoIntervention and ShowExampleIntervention to processAttemptIntervention() 
 */

function processNextProblemIntervention(activityJSON) {
    var interventionType = activityJSON.interventionType;
    checkIfInputIntervention(activityJSON);
    var resource = activityJSON.resource;
    var pid = activityJSON.id;
    var changeGUIIntervention = activityJSON.changeGUI==true;
    var destinationIS = activityJSON.destinationIS;   // TODO only ExternalActivities use this.  All interventions should

    if (interventionType === "TopicSwitch") {
        processTopicSwitchIntervention(activityJSON.html)
    }
    else if (changeGUIIntervention)
        processChangeGUIIntervention(activityJSON);
    else if (interventionType === "TopicIntro")
        processTopicIntroIntervention(activityJSON);
//    else if (interventionType === "ExternalActivityAskIntervention")
//        processExternalActivityAskIntervention(activityJSON.html);
    else if (interventionType === "ExternalActivity") {
       processExternalActivityIntervention(activityJSON);
    }
    else if (interventionType === "AskEmotionIntervention")
        processAskEmotionIntervention(activityJSON.html);
    else if (interventionType === "AskGoalsIntervention")
        processAskGoalsIntervention(activityJSON.html);
    else if (interventionType === "ShowMPPButton")
        processShowMPPIntervention(activityJSON.html);
    else if (interventionType === "MyProgressNavigation")
        processMyProgressNavIntervention(activityJSON.html);
    else if (interventionType === "MyProgressNavigationAsk")
        processMyProgressNavAskIntervention(activityJSON.html);
    else if(interventionType === "CollaborationPartnerIntervention")
        processCollaborationPartnerIntervention(activityJSON.html, activityJSON.timeoutwait);
    else if(interventionType === "CollaborationConfirmationIntervention")
        processCollaborationConfirmationIntervention(activityJSON.html);
    else if(interventionType === "CollaborationOriginatorIntervention")
        processCollaborationOriginatorIntervention(activityJSON.html, activityJSON.timeoutwait);
    else if(interventionType === "FinishCollaborationIntervention")
        processCollaborationFinishedIntervention(activityJSON.html, activityJSON.source);
    else if(interventionType === "CollaborationTimedoutIntervention")
        processCollaborationTimedoutIntervention(activityJSON.html);
    else if(interventionType === "CollaborationOptionIntervention")
        processCollaborationOptionIntervention(activityJSON.html);

    sendBeginIntervention(globals,interventionType);

}



function processTopicIntroIntervention (interv) {
    globals.instructions =  "This is an introduction to a topic.  Please review it before beginning work by clicking the new-problem button.";
    globals.destinationInterventionSelector = interv.destinationInterventionSelector;  // needs this so we can send back to IS when topic intro ends
    // send EndEvent  to end the previous problem
   // sendEndEvent(globals); // no longer done this way.  don't want this kind of dependency
//            showProblemInfo(pid,resource);
    globals.probElapsedTime = 0;
//    sendBeginEvent(globals);
    showTopicIntro(interv.resource,interv.topicName,interv.resourceType);
    globals.topicId = interv.topicId;
    globals.probId = 999;  // a dummy indicator that this "problem" is a topic intro
    globals.lastProbType=TOPIC_INTRO_PROB_TYPE; // needed so that nextproblem button knows that its ending a topic intro
}


function showTopicIntro (resource, topic, resourceType) {
    if (resourceType === 'html')
        showTopicIntroHTML(resource);
    // if nothing pop up an alert
    else if (typeof(resource) != 'undefined' && resource != '')
        showFlashProblem(resource,null,null,FLASH_CONTAINER_INNER, false);
    else alert("Beginning topic: "  + topic + ".  No Flash movie to show")


}


function processAttemptIntervention (interv) {
    if (interv != null) {
        var type = interv.interventionType;
        var changeGUIIntervention = interv.changeGUI==true;

        checkIfInputIntervention(interv);
        if (changeGUIIntervention)
            processChangeGUIIntervention(interv);
        if (type === 'ShowVideoIntervention') {
            updateTimers();
            servletGet("ShowVideo",{probElapsedTime: globals.probElapsedTime },processShowVideo);
        }
        else if (type === 'ShowExampleIntervention') {
            updateTimers();
            servletGet("ShowExample",{probElapsedTime: globals.probElapsedTime },processShowExample);
        }
        else if (type === 'HighlightHintButton')
            highlightHintButton();
        else if (type === 'RapidAttemptIntervention')
            processRapidAttemptIntervention(interv.html);
        sendBeginIntervention(globals,type);
    }
}

// WHen the mode of the external activity intervention selector is ASK.  This will first ask the user if they want to see it.
// We call back the server with the answer, and only show it if they say yes.

function processExternalActivityAskIntervention (html) {
    // Asks if they want the external activity.  Calls one of the two functions depending on the student answer
    interventionDialogOpenAsYesNo("Let's try something different!", html, NEXT_PROBLEM_INTERVENTION,wantsExternalActivity,doesNotWantExternalActivity );
}

// If the student says yes to the external activity, this function is called and we report yes to the server.
// It will return the external activity intervention and we will show it.
function wantsExternalActivity () {
    alert("Student wants the external activity") ;
    // send an InputResponse to the server with YES.  callback fn should be processNextProblemResult
    sendNextProblemInputResponse("&answer=YES");
}

// If the student says yes to the external activity, this function is called and we report no to the server
// It will go on with normal problem selection and we will proceed as normal
function doesNotWantExternalActivity () {
    alert("Student does not want the external activity") ;
    // send an InputResponse to the server with NO.  callback fn should be processNextProblemResult
    sendNextProblemInputResponse("&answer=NO");
    servletGet("InputResponseNextProblemIntervention","&probElapsedTime="+globals.probElapsedTime  + "&destination="+globals.destinationInterventionSelector,
        processNextProblemResult)
}


//Send an InputResponse for NextProblemIntervention to server.
// arg will be something like "&answer=YES"
function sendNextProblemInputResponse (arg) {
    // send an InputResponse to the server with NO.  callback fn should be processNextProblemResult
    servletGet("InputResponseNextProblemIntervention","&probElapsedTime="+globals.probElapsedTime  + "&destination="+globals.destinationInterventionSelector + arg,
        processNextProblemResult)
}

function processExternalActivityIntervention(activityJSON) {
    var pid = activityJSON.id;
    var resource = activityJSON.resource;
    var instructions = activityJSON.instructions;
    var destinationIS = activityJSON.destinationIS;
    var ask = (activityJSON.askMode=== "ask");
    debugAlert("Its an external problem.   Changing problemWindow src attribute to " + resource);
    globals.probElapsedTime = 0;

    globals.lastProbId = pid;
    globals.lastProbType = EXTERNAL_PROB_TYPE;
    globals.instructions= instructions;
    globals.destinationInterventionSelector = destinationIS;
    globals.isInputIntervention = true;
    //    showInstructionsDialog(instructions);
    if (typeof instructions != 'undefined' && !ask)
        interventionDialogOpenAsConfirm("External Activity Instructions", instructions, NEXT_PROBLEM_INTERVENTION,interventionDialogOKClick );
    else if (typeof instructions != 'undefined' && ask)   {
        interventionDialogOpenAsYesNo("External Activity Instructions", instructions, NEXT_PROBLEM_INTERVENTION,interventionDialogYesClick,interventionDialogNoClick );
    }
    else {
        servletGet("BeginExternalActivity", {xactId: pid, probElapsedTime: globals.probElapsedTime});
        window.open(resource,'External Activity', "height=600,width=800");
    }
//        $("#" + PROBLEM_WINDOW).attr("src", resource);    // shows it in the iframe
}


// check to see if the isInputIntervention flag is true and set a global variable so that
// we send back correct event when intervention ends
function checkIfInputIntervention (interv) {
    var isInputIntervention = interv.isInputIntervention;
    // this flag will alter the event sent when the intervention dialog closes
    if (isInputIntervention || isInputIntervention === 'true')
        globals.isInputIntervention = true;
    else globals.isInputIntervention = false;
    return globals.isInputIntervention;
}

// function interventionDialogYesClickOld () {
//     myprogress(globals)  ;
// }

//  The intervention dialog can be opened with either an OK button or a Yes-No buttons.  When those buttons are clicked, one of the three buttons below is
// called.
// At the time the intervention dialog is opened (say with Yes-No buttons), two function names are also provided - one for handling yes, one for no.
// These function names are stored inside click attribute of the button part of the dialog

// This function is used by a Yes/No dialog which should have no HTML form.   The yes button was clicked so we
// just need to send 'yes' back to the server as userInput
function interventionDialogYesClick () {
    if (globals.interventionType === NEXT_PROBLEM_INTERVENTION)
        sendInterventionDialogYesNoConfirmInputResponse("InputResponseNextProblemIntervention",processNextProblemResult,"yes");
    else
        sendInterventionDialogYesNoConfirmInputResponse("InputResponseAttemptIntervention",processNextProblemResult,"yes");
    doCloseInterventionDialog();
}

// This function is used by a Yes/No dialog which should have no HTML form.   The no button was clicked so we
// just need to send 'no' back to the server as userInput
function interventionDialogNoClick () {
    if (globals.interventionType === NEXT_PROBLEM_INTERVENTION)
        sendInterventionDialogYesNoConfirmInputResponse("InputResponseNextProblemIntervention",processNextProblemResult,"no");
    else
        sendInterventionDialogYesNoConfirmInputResponse("InputResponseAttemptIntervention",processNextProblemResult,"no");
    doCloseInterventionDialog();
}


// An confirm dialog may or may not have other inputs to send back (e.g. gather some emotion data and click OK)
// So it must send back all the inputs including the OK
function interventionDialogOKClick () {
    interventionDialogClose("ok");
}




function interventionDialogClose (userInput) {
    // NO LONGER HAVE Continue events
//    if (globals.interventionType === NEXT_PROBLEM_INTERVENTION && !globals.isInputIntervention)
//        servletGet("ContinueNextProblemIntervention", {probElapsedTime: globals.probElapsedTime, destination: globals.destinationInterventionSelector}, processNextProblemResult);
//    else if (globals.interventionType === ATTEMPT_INTERVENTION && !globals.isInputIntervention)
//        servletGet("ContinueAttemptIntervention", {probElapsedTime: globals.probElapsedTime, destination: globals.destinationInterventionSelector}, processNextProblemResult);

    // If closing down an intervention dialog for next problem we send the InputResponse event and ask processNextProblemResult
    // to handle what the server returns.   For attempts we send InputResponse event but, FOR NOW, we don't expect the server
    // to return anything so no callback is given.
    // TODO We may want to actually do something based on an input response intervention dialog on attempts so we'd need some
    // kind of handler in place that can react to what the server returns.
    if (globals.interventionType === NEXT_PROBLEM_INTERVENTION && globals.isInputIntervention)
        sendInterventionDialogInputResponse("InputResponseNextProblemIntervention",userInput, processNextProblemResult);

    else if (globals.interventionType === ATTEMPT_INTERVENTION && globals.isInputIntervention)
        sendInterventionDialogInputResponse("InputResponseAttemptIntervention", userInput);
    doCloseInterventionDialog();

}

function doCloseInterventionDialog () {
    $("#"+INTERVENTION_DIALOG_CONTENT).html("");
    globals.interventionType = null;
    globals.isInputIntervention= false;
    $("#"+INTERVENTION_DIALOG).dialog("close");
}

// Presumably this is being used as a message that stays up until a timeout delay and then the timeoutFunction gets called.
function interventionDialogOpenNoButtons (title, html, type, timeoutFunction,delay) {
    $("#ok_button").hide();
    $("#noButton").hide();
    $("#yesButton").hide();
    openInterventionDialog(title,html,type);
    setTimeout(timeoutFunction, delay)
}

function interventionDialogOpenAsConfirm (title, html, type, confirmFunction, height) {
    $("#ok_button").show();
    $("#noButton").hide();
    $("#yesButton").hide();
    setInterventionDialogButtonHandlerFunction("#ok_button",confirmFunction) ;
    openInterventionDialog(title,html,type,height);
}

function interventionDialogOpenAsYesNo (title, html, type, yesFunction, noFunction) {
    $("#ok_button").hide();
    $("#noButton").show();
    $("#yesButton").show();
    setInterventionDialogButtonHandlerFunction("#noButton",noFunction);
    setInterventionDialogButtonHandlerFunction("#yesButton",yesFunction);
    openInterventionDialog(title,html,type);
}

function setInterventionDialogButtonHandlerFunction (buttonId, buttonHandlerFunction)  {
    // get the button by its id  and replace its click attribute with the given function
   var button = $(buttonId)
   button.unbind("click");  // get rid of previous handlers attached to the button
   button.click(buttonHandlerFunction);
}

function openInterventionDialog (title, html, type, height) {
    globals.interventionType = type;
    $("#"+INTERVENTION_DIALOG).attr("title", title);
    $("#"+INTERVENTION_DIALOG_CONTENT).html(html);
    if (height)
        $("#"+INTERVENTION_DIALOG).dialog("option", "height", height);
    $("#"+INTERVENTION_DIALOG).dialog("open");
}


// The userInput will be what the user did to close the dialog (e.g. "ok") so that if form-inputs are not present
// this value is what matters to the server.
function sendInterventionDialogInputResponse (event, userInput, fn) {
    // If the dialog contains an HTML form, this gets the values of the form inputs and creates the URL parameters for them
    var formInputs = $("#"+INPUT_RESPONSE_FORM).serialize() ;
    // Not sure if this has a leading "&" on the first variable or not.  I need it to begin with that so I conditionally add it
    if (formInputs.length > 0 && formInputs[0] != '&')
        formInputs = "&" + formInputs;
    incrementTimers(globals);
    servletFormPost(event,formInputs + "&probElapsedTime="+globals.probElapsedTime  +
        "&destination="+globals.destinationInterventionSelector + "&userInput="+userInput, fn)
}

/**
 * Yes/No and confirm dialogs for interventions that request a response will not have an HTML form.  So we simply
 * send the userInput with yes, no, or ok.
 * @param event
 * @param fn
 */
function sendInterventionDialogYesNoConfirmInputResponse (event, fn, userInput) {

    incrementTimers(globals);
    servletFormPost(event, "&probElapsedTime="+globals.probElapsedTime  + "&destination="+globals.destinationInterventionSelector + "&userInput="+userInput,
        fn) ;
}


//send a BeginProblem event for HTMl5 problems.
function sendBeginIntervention(globals, intervType) {
    incrementTimers(globals);
    servletGetWait("BeginIntervention", {probElapsedTime: globals.probElapsedTime, interventionType: intervType});

}

