/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 6/12/13
 * Time: 2:42 PM
 * To change this template use File | Settings | File Templates.

* Kartik 04-22-21 Issue #390 Added session clock functionality
* Frank	07-03-21  gitServletPost - change error response from alert to debugAlert
 */

function debugAlert(msg) {
    if (globals.debug || globals.trace) {
        alert(msg);
    }
}

// Given an action, an array of args like [p1, v1, p2, v2], and a callback fn,  call HTTP get using jquery

function servletGet (action, args, callbackFn) {
    var extraArgs = "";
    for (var p in args) {
        value = args[p];
        extraArgs += "&" + p + "=" + value;
    }
    debugAlert("The action is: <" + action +">");
    $.get(getTutorServletURL(action,extraArgs),
        callbackFn);
}

function servletPost(action, args, callbackFn) {
    var extraArgs = "";
    for (var p in args) {
        value = args[p];
        extraArgs += "&" + p + "=" + value;
    }
    $.post(getTutorServletURL(action,extraArgs),
        callbackFn)
}

function saveMouse (x, y) {
    // console.log("x:"+x+"y:"+y);
    globals.mouseHistory.push({x:x,y:y,t:Date.now()});
}

function saveMouseClick (x, y) {
    // console.log("x:"+x+"y:"+y);
    globals.mouseHistory.push({x:x,y:y,t:Date.now(),action:1});
}

// Called on a timer (every 5 min) to ship the mouse history to the server.
function sendMouseData () {
    var url = "saveMouseData";
    var clone = globals.mouseHistory.slice(0);
    // reset the mouseHistory array BEFORE server call so we can collect points in a new list.
    globals.mouseHistory= [];
    var pointLst = {points: clone};
    console.log("Sending " + clone.length + " points");
    // obj = {data: [{x: 10, y:34, t: Date.now()}, {x: 5, y:66, t: Date.now()}]};
    var  obj= {mouseData: JSON.stringify(pointLst)};
    // TODO make sure server returns code that indicates the mouse data was saved.
    gritServletPost("saveMouseData",obj);
}

function gritServletPost(action, data) {
    // var jsonStr = encodeURIComponent(JSON.stringify(data));
    // add in the other common fields for post
    data.action = action;
    data.sessionId= globals.sessionId;
    data.elapsedTime = globals.elapsedTime;
    data.clickTime = globals.clickTime;
    data.eventCounter=sysGlobals.eventCounter;
    // data = JSON.stringify(data);
    $.ajax({
        type: "POST",
        url: getGritServletURL(action),
        dataType: 'json',
        cache: false,
        async: true,
        crossDomain: false,
        data: data ,
        error: function (xhr, textStatus, errorThrown) {
        	debugAlert("Failure to post mouse data " + textStatus + errorThrown);
        }
    });
}

function servletFormPost (action, args, callbackFn) {
    $.post(getTutorServletURL(action,args),
        callbackFn)
}

// args is assumed to begin with an &  and is a string of parameters like &p1=44&p2=8484 ...
function getTutorServletURL (action, args) {
    return "/" + sysGlobals.wayangServletContext + "/" + sysGlobals.servletName
        + "?action=" + action
        + "&sessionId=" + globals.sessionId
        + "&elapsedTime=" + globals.elapsedTime
        + "&clickTime=" + globals.clickTime
        + "&eventCounter="+ sysGlobals.eventCounter++
        + "&var=b"
        + args ;
}

// A POST URL should not have parameters
function getGritServletURL (action) {
    return "/" + sysGlobals.wayangServletContext + "/" + sysGlobals.gritServletContext + "/" + sysGlobals.gritServletName;
}

// Makes a synchronous call to the server.
function servletGetWait (action, args, callbackFn) {
    var extraArgs = "";
    for (var p in args) {
    	if(p == 'userInput')
			args[p] = encodeURIComponent(args[p]);
        value = args[p];
        extraArgs += "&" + p + "=" + value;
    }
    debugAlert("The action is: <" + action +">");
    $.ajax({url: getTutorServletURL(action,extraArgs),
           success: callbackFn,
           async: false});
}

function httpHead(url, successCallbackFn, failureCallbackFn) {
    $.ajax({
        type: "GET",
        dataType: "HTML",
        crossDomain: true,
        async: true,
        url: url,
        success: function (data, textStatus, jqXHR) { 
        	successCallbackFn(url);
        	} ,
        error: function (jqXHR, textStatus, errorThrown) { 
        }
    });
}

// return the XML that is <elementName>xxx</elementName>
// Note that we must use begin and end tags as above.   No short-cuts.
function getXMLElement(xml, elementName) {
    debugAlert("in getXMLElemet" + xml);
    var eltbegin = "<" + elementName;
    var eltend = "</" + elementName + ">"
    var re = new RegExp("(<" + elementName + ".*(?:(?:</" + elementName + ">)|(?:/>)))");
    var m = re.exec(xml);
    if (m == null) {
        debugAlert("no match");
        return null;
    }
    else {
        debugAlert("match is " + m[0]);
        return m[0];
    }
}


// don't send an endExternalActivity if the last prob was 4mality.  4mality sends its own begin/ends
function sendEndEvent(globals) {
    updateTimers();
    if (globals.lastProbType == '')
        return;
    else if (globals.lastProbType == HTML_PROB_TYPE)
    {
        isExample = isDemoOrExampleMode()
        servletGetWait("EndProblem",{probId: globals.lastProbId, probElapsedTime: globals.probElapsedTime,  clickTime: globals.clickTime, isExample: isExample},processEndProblem);
    }
    else if (globals.lastProbType === TOPIC_INTRO_PROB_TYPE)
        ; // Topic Intros are no longer problems and thus we don't need to end them
    else
        servletGetWait("EndExternalActivity", {xactId: globals.lastProbId, clickTime: globals.clickTime, probElapsedTime: globals.probElapsedTime});


}

//send a BeginProblem event for HTMl5 problems.
function sendBeginEvent(globals, probId, mode, callbackFn) {
    incrementTimers(globals);
    globals.probElapsedTime=0;
    servletGetWait("BeginProblem", {probElapsedTime: globals.probElapsedTime, probId: probId, clickTime: globals.clickTime, mode: mode}, callbackFn);

}

function sendResumeProblemEvent (globals) {
    incrementTimers(globals);
    servletGetWait("ResumeProblem", {probElapsedTime: globals.probElapsedTime, clickTime: globals.clickTime, probId: globals.lastProbId})
}


// don't send an endExternalActivity if the last prob was 4mality.  4mality sends its own begin/ends
function sendSimpleNotificationEvent(globals, eventName) {
    updateTimers();
    servletGetWait(eventName,{probId: globals.lastProbId, clickTime: globals.clickTime, probElapsedTime: globals.probElapsedTime});
}



function utilDialogOpen (url, title, html) {
    $("#"+UTIL_DIALOG).attr("title", title);
    $("#"+UTIL_DIALOG_IFRAME).attr("src",url);
    $("#"+UTIL_DIALOG).dialog("open");

}

function startSessionClock(curTime) {
	var seconds = Math.floor(curTime/1000);
	var minutes = Math.floor(seconds/60);
	var hours = Math.floor(minutes/60);
	
	minutes = minutes-(hours*60);
	seconds = seconds-(hours*60*60)-(minutes*60);
    
	seconds = checkTime(seconds);
	minutes = checkTime(minutes);
	hours = checkTime(hours);
	document.getElementById('session_clock').innerHTML = hours + ":" + minutes + ":" + seconds;
	curTime = curTime + 1000;
    var t = setTimeout(startSessionClock.bind(this, curTime), 1000);

}

function checkTime(i) {
    if (i < 10) {i = "0" + i};  // add zero in front of numbers < 10
    return i;
}
