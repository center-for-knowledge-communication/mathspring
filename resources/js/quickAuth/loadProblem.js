//Module pattern for better scoping
var quickAuthLoadProblem = (function(){
    //The module we're exporting
var m = {},
    previewMode;

m.requestProblemData = function(probId, sessionId, elapsedTime, eventCounter, servletContext, servletName, previewmode, teacherId) {
    previewMode = previewmode;
    var args = {probId: probId};
    if(previewMode) {
        args.teacherId = teacherId;
    } else {
        args.sessionId = sessionId;
        args.elapsedTime = elapsedTime;
        args.eventCounter = eventCounter;
    }
    servletGet(
        !previewMode,
        previewMode ? "AdminGetQuickAuthProblem" : "GetQuickAuthProblem",
        servletContext,
        servletName,
        args,
        buildProblem
    );
}

function servletGet (useTutorServlet, action, servletContext, servletName, args, callbackFn) {
    var extraArgs = "";
    for (var p in args) {
        extraArgs += "&" + p + "=" + args[p];
    }
    var getServletURL = useTutorServlet ? getTutorServletURL : getAdminServletURL;
    $.get(getServletURL(action, servletContext, servletName, extraArgs), callbackFn);
}

function getAdminServletURL (action, servletContext, servletName, args) {
    return "/" + servletContext + "/WoAdmin?action=" + action + args;
}

function getTutorServletURL (action, servletContext, servletName, args) {
    return "/" + servletContext + "/" + servletName + "?action=" + action + args;
}

function buildProblem(responseText, textStatus, XMLHttpRequest) {
    quickAuthBuildProblem.build(JSON.parse(responseText), previewMode);
}

return m;

})();
