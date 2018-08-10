var surveyOpen = false;

function surveyButton(ctxt, srvlet, url, skin, sessionId, intervClass) {
    $("#surveyButton").click(function () {
        if (!surveyOpen) {
            surveyOpen = true;
            window.open(url);
            // change button to say Continue
            $("#surveyButton").prop('value', 'Continue'); //versions newer than 1.6
        } else {
            continueLogin(ctxt, srvlet, skin, sessionId, intervClass)
        }
    });
}

// Given an action, an array of args like [p1, v1, p2, v2], and a callback fn,  call HTTP get using jquery
function continueLogin(ctxt, srvlet, skin, sessId, intervClass) {
    var action = "LoginInterventionInput";
    var url = ctxt + "/" + srvlet + "?action=" + action + "&sessionId=" + sessId + "&skin=" + skin + "&interventionClass=" + intervClass + "&var=b";
    document.location.href = url;
}