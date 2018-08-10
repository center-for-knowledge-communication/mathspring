/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 12/19/13
 * Time: 12:04 PM
 * To change this template use File | Settings | File Templates.
 */

function Main(g, sysG, trans) {
    this.globals = g;
    this.sysGlobals = sysG;
    this.transients = trans;
    //constructor
}

Main.prototype.clickHandling = function() {
    var agreed=false;
    $("#"+LEARNING_COMPANION_CONTAINER).dialog(  {
            closeOnEscape: false,
            position: ['right', 200],
            open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); }
        }
    );
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
        nextProb(globals)
    });
    $("#read").click(function () {
        callReadProb()
    });
    $("#hint").click(function () {
        requestHint(globals)
    });
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
    $("#formulas").click(function () {
        showFormulas(globals)
    });
    $("#glossary").click(function () {
        showGlossary(globals)
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
    $( "#"+INTERVENTION_DIALOG).dialog( {
        autoOpen: false,
        modal:true,
        width: 500,
        height:500,
        buttons: {
            "OK": function () { interventionDialogClose("ok") ; }
        }
    });

    $( EXAMPLE_CONTAINER_DIV_ID).dialog({
        autoOpen: false,
        modal:true,
        width: 700,
        height:800,
        open: function () {
            sysGlobals.exampleWindowActive = true;
        },
        close: function () { exampleDialogCloseHandler(); } ,
        buttons: [
            {
                text: "Play next step",
                click: function() {
                    example_solveNextHint();
                }
            },
            {
                text: "Cancel",
                click: function() {
                    if (isDemoMode())   {
                        // turn off demo mode
                        globals.probMode = null;
                        globals.probType = null;
                        globals.exampleProbType = null;
                        nextProb(globals);
                    }
                    $( this ).dialog( "close" );
                }
            }
        ]
    });
    $("#"+UTIL_DIALOG).dialog({
        autoOpen: false,
        modal:false,
        width:700,
        height:700


    })

    $("#prefs").click(showUserPreferences);
    $("#instructions").click(instructions);

    $("#myProg").click(function () {
        // myprogress(globals)
        myprogress()
    });
}

Main.prototype.main = function() {
    globals = g;
    sysGlobals = sysG;
    sysGlobals.childWindow = document.getElementById(PROBLEM_WINDOW).contentWindow;
    transients = trans;
    var d = new Date();
    var startTime = d.getTime();
    toggleSolveDialogue(false);
    clickHandling();
    var d = new Date();
    globals.clock = d.getTime();
    //globals.lastProbType = globals.probType;  // this used to have single quotes around the JSP var

    // 9/9 I removed setting globals.elapsedTime and probElapsedTime.   Those are set in the mathspring.jsp appropriately for
    // the calling context


    // If this is the first time the tutor is loaded (i.e. from a login) then we send a navigation event so the server initializes
    // correctly based on the student now being in the tutor page.
    // If not the first time, then we are re-entering the tutor page and we want to call the server with nextProb to get some content
    // to show in the tutor page.
    if (globals.isBeginningOfSession)
    // First we need to tell the server that we are in the tutor hut
        servletGet("navigation",{from:'village',to:'sat_hut', clientVersion:'hybrid'});
    else if (globals.continueUnsolvedProblem && globals.probId != -1)
        resumeProb(globals);
    else nextProb(globals);


}
