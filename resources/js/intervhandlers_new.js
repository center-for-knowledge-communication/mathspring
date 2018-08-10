var tmoutwait = 0;
var timewaited = 0;

// This is for an attempt event that asks to highlight the hint button
// its a shame this function has to know the image files that are defined in the CSS rather than fetching them from it.
function highlightHintButton() {
    var $hintBtn = $('#hint');
    $hintBtn.addClass('hint-green animated tada');
    setTimeout(function() {
        $hintBtn.removeClass('hint-green animated tada');
    }, 2000);
}

// this pops up a dialog informing or asking about topic switching
function processTopicSwitchIntervention(html) {
    //alert("Switching topics because " + reason)
//    interventionDialogOpen("Switching Topics", html, NEXT_PROBLEM_INTERVENTION );

    // we set globals.lastProbType to be empty so that when the next Problem comes back to the client processNextProblemResult in tutorhut.js
    // it won't send an endProblem event.   We don't want it to send an EndProblem event when the new topic starts because the TopicSwitchAskIS
    // input response handling took care of processing an EndProblem event for the last problem in the previous topic.

    globals.lastProbType = '';
    interventionDialogOpenAsConfirm("Switching Topics", html, NEXT_PROBLEM_INTERVENTION,interventionDialogOKClick );


}

function processAskEmotionIntervention(html) {
    //alert("Switching topics because " + reason);
//    interventionDialogOpen("How are you doing", html, NEXT_PROBLEM_INTERVENTION );
    interventionDialogOpenAsConfirm("How are you doing", html, NEXT_PROBLEM_INTERVENTION,interventionDialogOKClick, 550 );

}


function processAskGoalsIntervention(html) {
    //alert("Switching topics because " + reason);
//    interventionDialogOpen("How are you doing", html, NEXT_PROBLEM_INTERVENTION );
    interventionDialogOpenAsConfirm("What are your goals", html, NEXT_PROBLEM_INTERVENTION,interventionDialogOKClick, 550 );

}

function processShowMPPIntervention () {
     showMPP();
}

function processMyProgressNavIntervention (html) {
//    interventionDialogOpen("Let's see our progress!", html, NEXT_PROBLEM_INTERVENTION);
    interventionDialogOpenAsConfirm("Let's see our progress!", html, NEXT_PROBLEM_INTERVENTION,myprogress );


}

function processMyProgressNavAskIntervention (html) {
//    interventionDialogOpen("Let's see our progress!", html, NEXT_PROBLEM_INTERVENTION);
    interventionDialogOpenAsYesNo("Let's see our progress!", html, NEXT_PROBLEM_INTERVENTION,myprogress,interventionDialogNoClick );
}



//This function continues the wait on a TimeoutIntervention
function continueInterventionTimeout(){
    timewaited = timewaited+tmoutwait;
    servletGet("InterventionTimeout", {probElapsedTime: globals.probElapsedTime, destination: globals.destinationInterventionSelector, timeWaiting: timewaited+tmoutwait}, processInterventionTimeoutResult);
}

// This sets up an intervention on the helpers screen that freezes their input until the collaboration with
// a partner (on the partners computer) is done.   This checks every 3 seconds to see if this intervention
// is complete.   The server keeps returning the same intervention/learningCompanion when it's not complete and a different
// intervention when it is complete
function processCollaborationPartnerIntervention(html, timeoutwait) {
    globals.destinationInterventionSelector = "edu.umass.ckc.wo.tutor.intervSel2.CollaborationPartnerIS";
    timewaited = 0;
    tmoutwait = timeoutwait;
    // Open the dialog with no buttons and a timeout function
    interventionDialogOpenNoButtons("Work with a partner", html, NEXT_PROBLEM_INTERVENTION, function(){
            servletGet("InterventionTimeout", {probElapsedTime: globals.probElapsedTime, destination: globals.destinationInterventionSelector, timeWaiting: timewaited + tmoutwait}, processInterventionTimeoutResult);}
            , timeoutwait);
        }

function processCollaborationConfirmationIntervention(html) {
    globals.destinationInterventionSelector = "edu.umass.ckc.wo.tutor.intervSel2.CollaborationOriginatorIS";
//    interventionDialogOpen("Work with a partner", html, NEXT_PROBLEM_INTERVENTION);
    // TODO this could be changed to ...OpenAsYesNo function call instead of Confirm.  We'd have to change the html so it doesn't have inputs and
    // just says "Do you want to work with a partner?".  The yes/no buttons would then be all that needed to be clicked and functions would send back
    // those responses to the server
    interventionDialogOpenAsConfirm("Work with a partner", html, NEXT_PROBLEM_INTERVENTION,interventionDialogOKClick );
}

// When the originator is waiting for a partner this checks the server every 5 seconds to see if the partner is available to work
// with.   Every 60 seconds it asks if they want to continue waiting for a partner.
function processCollaborationOriginatorIntervention(html, timeoutwait) {
    globals.destinationInterventionSelector = "edu.umass.ckc.wo.tutor.intervSel2.CollaborationOriginatorIS";
    timewaited = 0;
    tmoutwait = timeoutwait;
    // Open the intervention dialog with no buttons and timeout handling
        interventionDialogOpenNoButtons("Waiting for a partner", html, NEXT_PROBLEM_INTERVENTION, function(){
            servletGet("InterventionTimeout", {probElapsedTime: globals.probElapsedTime, destination: globals.destinationInterventionSelector, timeWaiting: timewaited+tmoutwait}, processInterventionTimeoutResult);}
            , timeoutwait);
}




function processCollaborationFinishedIntervention(html, destination) {
    if(destination === "Partner"){
        globals.destinationInterventionSelector = "edu.umass.ckc.wo.tutor.intervSel2.CollaborationPartnerIS";
    }
    else{
        globals.destinationInterventionSelector = "edu.umass.ckc.wo.tutor.intervSel2.CollaborationOriginatorIS";
    }
    interventionDialogOpenAsConfirm("Collaboration over", html, NEXT_PROBLEM_INTERVENTION,interventionDialogOKClick );
}

function processCollaborationTimedoutIntervention(html) {
    globals.destinationInterventionSelector = "edu.umass.ckc.wo.tutor.intervSel2.CollaborationOriginatorIS";
//    interventionDialogOpen("Continue Waiting?", html, NEXT_PROBLEM_INTERVENTION );
    //  TODO This could be turned into a Yes/No dialog call
    interventionDialogOpenAsConfirm("Continue Waiting?", html, NEXT_PROBLEM_INTERVENTION,interventionDialogOKClick );
}

function processCollaborationOptionIntervention(html) {
    globals.destinationInterventionSelector = "edu.umass.ckc.wo.tutor.intervSel2.CollaborationOriginatorIS";
//    interventionDialogOpen("Work with a partner?", html, NEXT_PROBLEM_INTERVENTION);
    interventionDialogOpenAsConfirm("Work with a partner?", html, NEXT_PROBLEM_INTERVENTION,interventionDialogOKClick );
}


function processRapidAttemptIntervention (html) {
//    interventionDialogOpen("Answering Rapidly", html, ATTEMPT_INTERVENTION );
    interventionDialogOpenAsConfirm("Answering Rapidly", html, ATTEMPT_INTERVENTION,interventionDialogOKClick );

}


// Interventions that change the GUI come with some attributes that dictate the behavior:
// 1.   If notify is false,  just make changes to the GUI
// 2.  If notify is true, and when = 'before',  notify the user with a dialog that the GUI is being altered.  Then
//     when the user confirms by clicking on the dialog button, the GUI is actually changed.
// 3.  If notify is true and when='after',  change the GUI and then notify with a dialog.
//   Changing the GUI is specified by giving <component, action>.   The component is some on-screen item like a button
//   and the action is one of show/hide/highlight.
function processChangeGUIIntervention (activity) {
    var notify = activity.notify==true;
    var when = activity.when;
    var component = activity.component;
    var action = activity.action;
    var html = activity.html;
    // No notification means we just make the GUI change.
    if (!notify) {
        doGUIChange(component,action);
    }
    // The GUI change happens when the dialog is closed.  The callback function gets <component,action> from the transients object
    else if (when === 'before')  {
        transients.component = component;
        transients.componentAction = action;
        interventionDialogOpenAsConfirm("Making a change!", html, NEXT_PROBLEM_INTERVENTION, guiChanger );
    }
    // Notify after changing the GUI
    else if (when === 'after') {
        doGUIChange(component,action);
        // the noopHandler does nothing when the dialog is closed.
        interventionDialogOpenAsConfirm("Making a change!", html, NEXT_PROBLEM_INTERVENTION, noopHandler );
    }

}

// Intervention Dialog calls this method to perform the GUI change.   This relies on global variables that hold the component and action
function guiChanger () {
    doGUIChange(transients.component,transients.componentAction);
}

// Do nothing.
function noopHandler () {

}

// For now we just have a couple components that can be acted on but every item that has an id can be acted on in this way.
// TODO we'd need to make  show/hide/highlight into generic functions that can take a component id.
function doGUIChange(component, action) {
    if (component == 'MPP' && action == 'show')
        myprogress();
    else if (component == 'Hint' && action == 'highlight')
        highlightHintButton()


}
