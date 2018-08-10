/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 5/11/15
 * Time: 11:50 AM
 * To change this template use File | Settings | File Templates.
 */

var surveyOpen = false;

function surveyButton (ctxt, srvlet, url, skin,sessionId,intervClass) {
    $("#surveyButton").click(function () {
        if (!surveyOpen) {
            surveyOpen=true;
            window.open(url);
            // change button to say Continue
            $("#surveyButton").prop('value', 'Continue'); //versions newer than 1.6
        }
        else {
           continueLogin(ctxt,srvlet, skin,sessionId,intervClass)
        }
    });
}

// Given an action, an array of args like [p1, v1, p2, v2], and a callback fn,  call HTTP get using jquery

function continueLogin (ctxt,srvlet, skin, sessId, intervClass) {
//    var ctxt = "/mt";
//    var srvlet = "WoLoginServlet";
    var action="LoginInterventionInput";
    var url = ctxt+ "/" + srvlet+"?action=" +action+"&sessionId="+sessId+"&skin="+skin+"&interventionClass="+intervClass;
//    alert("continuing login with: " + url);
    document.location.href= url;
}
