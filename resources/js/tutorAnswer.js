/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 8/26/13
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */



// Flash problems grade themselves (x or check) prior to calling the server to inform it of the attempt.
function flashProblemAnswerChosen (choice) {
    debugAlert("flashProblemAnswerChosen CALLED! with: " + choice);
    if (!sysGlobals.exampleWindowActive  || (globals.probMode != MODE_DEMO && globals.probMode != MODE_EXAMPLE && !isWaiting())) {
        incrementTimers(globals);
        servletGetWait("Attempt", {userInput: choice, probElapsedTime: globals.probElapsedTime}, processFlashProblemAnswerChosenResult);
    }
}



// if there is an intervention on attempt this will deal with it.  N.B. Flash problems grade the attempt prior to this
// being called so there is no way to not grade them if the intervention were to want to delay grading.
function processFlashProblemAnswerChosenResult(responseText, textStatus, XMLHttpRequest) {
    var json = JSON.parse(responseText);
    var isCorrect = json.isCorrect;
    var interv = json.intervention;
    showLearningCompanion(json);
    processAttemptIntervention(interv);
}

function tutorhut_shortAnswerSubmitted (sym, answer) {
    debugAlert("answerChosen CALLED! with: " + answer);
    transients.sym = sym;
    if (!isWaiting() && globals.probMode != MODE_DEMO) {
        incrementTimers(globals);
        servletGetWait("Attempt", {userInput: answer, probElapsedTime: globals.probElapsedTime}, processShortAnswerResult);

    }
}

function processShortAnswerResult (responseText, textStatus, XMLHttpRequest) {
    debugAlert("processShortAnswerResult: Server returns " + responseText);
    var json = JSON.parse(responseText);
    var isCorrect = json.isCorrect;
    var showGrade = json.showGrade;
    var interv = json.intervention;
    if (showGrade == undefined || showGrade)
        callShortAnswerProblemGrader(isCorrect, transients);
    showLearningCompanion(json);
    processAttemptIntervention(interv);
}

function callShortAnswerProblemGrader(isCorrect, transients) {
    debugAlert("In callShortAnswerProblemGrader with " + isCorrect);
    // for now we don't turn on hints associated with wrong answers
//    Comp.getStage().gradeAnswer(transients.sym,transients.answerChoice,isCorrect,false) ;
    if (globals.probMode != MODE_DEMO)
        document.getElementById(PROBLEM_WINDOW).contentWindow.prob_gradeAnswer(transients.sym, transients.answerChoice, isCorrect, false);
}


// This function is called by html5 IN problemWindow js when user attempts.  In HTML problems the attempt is
// not graded until the server returns the grade.
// selectedButton is the Edge symbol representing the button
tutorhut_answerChosen = function (selectedButton, choice) {
    debugAlert("answerChosen CALLED! with: " + choice);
    if (!isWaiting() && globals.probMode != MODE_DEMO) {
        transients.answerChoice = choice;  // must remember this for callback to problem js
        transients.selectedButton = selectedButton;  // must remember this for callback to problem js
        // This prevents sending the server multiple message about user attempts on choices that have been previously selected.
        if (transients.answersChosenSoFar.indexOf(choice) < 0) {
            transients.answersChosenSoFar[transients.answersChosenSoFar.length] = choice; // adds an element to the end of the list
            incrementTimers(globals);
            servletGetWait("Attempt", {userInput: choice, probElapsedTime: globals.probElapsedTime}, processAnswerChosenResult);
        }
    }
};

// In HTML problems the server returns the grade and a possible intervention.
// In the future it might make sense to not show the grading until processAttemptIntervention runs.   Perhaps
// the intervention should contain a flag doGrade=true/false that determines this.
function processAnswerChosenResult(responseText, textStatus, XMLHttpRequest) {
    debugAlert("processAnswerChosenResult: Server returns " + responseText);
    var json = JSON.parse(responseText);
    var isCorrect = json.isCorrect;
    var showGrade = json.showGrade;
    var interv = json.intervention;
    if (showGrade == undefined || showGrade)
        callProblemGrader(isCorrect, transients);
    showLearningCompanion(json);
    processAttemptIntervention(interv);

}

function callProblemGrader(isCorrect, transients) {
    debugAlert("In callProblemGrade with " + isCorrect);
    // for now we don't turn on hints associated with wrong answers
//    Comp.getStage().gradeAnswer(transients.selectedButton,transients.answerChoice,isCorrect,false) ;
    if (globals.probMode != MODE_DEMO)
        document.getElementById(PROBLEM_WINDOW).contentWindow.prob_gradeAnswer(transients.selectedButton, transients.answerChoice, isCorrect, false);
}
