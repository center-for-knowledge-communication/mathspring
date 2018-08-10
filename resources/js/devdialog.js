/**
 * Created by marshall on 6/5/17.
 */
/**
* @Author: Neeraj
* @Date: 02/17/2017
* The following three methods are added as part of feature to view all the content logs in more robust mode
*/


function publishTesterNote(idx){
    var data = $("#eventLogTable").DataTable().row(idx).data();
    var notetype = "#noteType"+idx;
    var comments = "#comments"+idx;
    var formId = "#formId"+idx;
    var strImg = "#img"+idx;
    var statusMessage = "#statusMessage"+idx;
    $(strImg).show();
    $.post(

        "/"+sysGlobals.wayangServletContext + "/TutorBrain?action=GetEventLogData&type=publishNote&sessionId=" + globals.sessionId +"&elapsedTime="+ globals.elapsedTime + "&eventCounter="+ sysGlobals.eventCounter++,
        {formData :  data['id']+"~~"+$(comments).val()+"~~"+$(notetype).find('option:selected').val()},
        function(result) {
            $(strImg).hide();
            if (result == "contentSaved") {
                $(statusMessage).html('<div class="alert alert-success" role="alert">'
                    + "<strong>Note Published Successfully</strong>"
                    + '</div>').fadeIn().fadeOut(5000);

            }else{
                $(statusMessage).html('<div class="alert alert-danger" role="alert">'
                    + "<strong>Failed to Publish Notes</strong>"
                    + '</div>').fadeIn().fadeOut(5000);

            }

        });
}

function generateHighlightRuleDialog(){
    $('#newHighlightRule_eventLog').on( 'click', function (e) {
        var index = intialializeAllHighLightFields();
        $('.colorSelector').popover({
            title: 'Select Hightlight Color',
            html : true,
            container: 'body',
            content: function() {
                var darkBlue = 'darkBlue';
                var skyBlue = 'skyBlue';
                var green = 'green';
                var orange = 'orange';
                var red = 'red';
                return '<div><div><button type="button" style="width:100%;" class="btn btn-primary btn-lg" onclick="refreshGridwithSelectColor(index,1)"></div></button></br><div><button type="button" style="width:100%;" class="btn btn-info btn-lg" onclick="refreshGridwithSelectColor(index,2)"></div></button></br><div><button type="button" style="width:100%;" class="btn btn-success btn-lg" onclick="refreshGridwithSelectColor(index,3)"></div></button></br><div><button type="button" style="width:100%;" class="btn btn-danger btn-lg" onclick="refreshGridwithSelectColor(index,4)"></div></button></br><div><button type="button" style="width:100%;" class="btn btn-warning btn-lg" onclick="refreshGridwithSelectColor(index,5)"></div></button></div>';
            }});
    } );

    $('#newHighlightRule_studentProblemHistoryLog').on( 'click', function (e) {
        var indexStudent = intialializeAllHighLightFieldsForStidentProblemLog();
        $('.probHistorcolorSelector').popover({
            title: 'Select Hightlight Color',
            html : true,
            container: 'body',
            content: function() {
                var darkBlue = 'darkBlue';
                var skyBlue = 'skyBlue';
                var green = 'green';
                var orange = 'orange';
                var red = 'red';
                return '<div><div><button type="button" style="width:100%;" class="btn btn-primary btn-lg" onclick="refreshGridwithSelectColorForStudentProblemHistory(indexStudent,1)"></div></button></br><div><button type="button" style="width:100%;" class="btn btn-info btn-lg" onclick="refreshGridwithSelectColorForStudentProblemHistory(indexStudent,2)"></div></button></br><div><button type="button" style="width:100%;" class="btn btn-success btn-lg" onclick="refreshGridwithSelectColorForStudentProblemHistory(indexStudent,3)"></div></button></br><div><button type="button" style="width:100%;" class="btn btn-danger btn-lg" onclick="refreshGridwithSelectColorForStudentProblemHistory(indexStudent,4)"></div></button></br><div><button type="button" style="width:100%;" class="btn btn-warning btn-lg" onclick="refreshGridwithSelectColorForStudentProblemHistory(indexStudent,5)"></div></button></div>';
            }});
    } );


}

function refreshGridwithSelectColorForStudentProblemHistory(id,num){
    var selector = "#studentformId"+id;
    var data = $(selector).serializeArray().reduce(function(obj, item) {obj[item.name] = item.value;return obj;}, {});

    var columnName = data['selectCol']+':name';
    var operator;
    var highlightValue = data['highLightVal'];
    var highLightColor;
    if(num == 1){
        highLightColor = '#5bc0de';
    }else if(num == 2){
        highLightColor = '#337ab7';
    }else if(num == 3){
        highLightColor = '#5cb85c';
    }else if(num == 4){
        highLightColor = '#d9534f';
    }else{
        highLightColor = '#f0ad4e';
    }
    if(data['selectOp'] == 'Equals'){
        operator = function(a, b) { return a == b;};
    }else if(data['selectOp'] == 'Greater_than'){
        operator = function(a, b) { return a > b;};
    }else{
        operator = function(a, b) { return a < b;};
    }
    $("#studentProblemHistoryTable").DataTable().column(columnName).data().each( function ( value, index ) {
        if(operator(value,highlightValue)){
            $("#studentProblemHistoryTable").DataTable().row(index).nodes().to$().css('background-color',highLightColor);
        }
    });
}

function refreshGridwithSelectColor(id,num){
    var selector = "#formId"+id;
    var data = $(selector).serializeArray().reduce(function(obj, item) {obj[item.name] = item.value;return obj;}, {});

    var columnName = data['selectCol']+':name';
    var operator;
    var highlightValue = data['highLightVal'];
    var highLightColor;
    if(num == 1){
        highLightColor = '#5bc0de';
    }else if(num == 2){
        highLightColor = '#337ab7';
    }else if(num == 3){
        highLightColor = '#5cb85c';
    }else if(num == 4){
        highLightColor = '#d9534f';
    }else{
        highLightColor = '#f0ad4e';
    }
    if(data['selectOp'] == 'Equals'){
        operator = function(a, b) { return a == b;};
    }else if(data['selectOp'] == 'Greater_than'){
        operator = function(a, b) { return a > b;};
    }else{
        operator = function(a, b) { return a < b;};
    }
    $("#eventLogTable").DataTable().column(columnName).data().each( function ( value, index ) {
        if(operator(value,highlightValue)){
            $("#eventLogTable").DataTable().row(index).nodes().to$().css('background-color',highLightColor);
        }
    });
}

var index = 0;
function intialializeAllHighLightFields(){
    index++;
    var html = '<div id="rowId'+index+'" class="row"><form id="formId'+index+'"><div class="form-group col-xs-3"><select name="selectCol" class="form-control"><option value="">--Select Column--</option><option value="action">action</option><option value="userInput">userInput</option><option value="isCorrect">isCorrect</option><option value="elapsedTime">elapsedTime</option><option value="probElapsed">probElapsed</option><option value="problemId">problemId</option><option value="hintStep">hintStep</option><option value="hintId">hintId</option><option value="emotion">emotion</option><option value="activityName">activityName</option><option value="auxId">auxId</option><option value="auxTable">auxTable</option><option value="time">time</option><option value="curTopicId">curTopicId</option><option value="testerNote">testerNote</option></select></div><div class="form-group col-xs-3"><select class="form-control" name="selectOp"><option value="">--Select Operator--</option><option value="Equals">Equals</option><option value="Greater_than">Greater than</option><option value="Less_than">Less than</option></select></div><div class="form-group col-xs-3"><input type="text" name="highLightVal" class="form-control" placeholder="--Highlight Value--"/></div></form>';
    var colorSelectHTML = "<div class='form-group col-xs-3'><button type='button' class='btn btn-secondary colorSelector'>Pick Color</button></div></div>";
    var temp = html+""+colorSelectHTML;
    $('#newHighlightRulecontainer_eventLog').append(temp);
    return index;

}

var indexStudent = 0;
function intialializeAllHighLightFieldsForStidentProblemLog(){
    indexStudent++;
    var html = '<div id="rowId'+indexStudent+'" class="row"><form id="studentformId'+indexStudent+'"><div class="form-group col-xs-3"><select name="selectCol" class="form-control"><option value="">--Select Column--</option><option value="problemId">problemId</option><option value="topicId">topicId</option><option value="problemBeginTime">problemBeginTime</option><option value="problemEndTime">problemEndTime</option><option value="timeInSession">timeInSession</option><option value="timeInTutor">timeInTutor</option><option value="timeToFirstAttempt">timeToFirstAttempt</option><option value="timeToFirstHint">timeToFirstHint</option><option value="timeToSolve">timeToSolve</option><option value="numMistakes">numMistakes</option><option value="numHints">numHints</option><option value="videoSeen">videoSeen</option><option value="numAttemptsToSolve">numAttemptsToSolve</option><option value="solutionHintGiven">solutionHintGiven</option><option value="mode">mode</option><option value="mastery">mastery</option><option value="emotionAfter">emotionAfter</option><option value="emotionLevel">emotionLevel</option><option value="effort">effort</option><option value="exampleSeen">exampleSeen</option><option value="textReaderUsed">textReaderUsed</option><option value="numHintsBeforeSolve">numHintsBeforeSolve</option><option value="isSolved">isSolved</option><option value="adminFlag">adminFlag</option><option value="authorFlag">authorFlag</option><option value="collaboratedWith">collaboratedWith</option><option value="timeToSecondAttempt">timeToSecondAttempt</option><option value="timeToThirdAttempt">timeToThirdAttempt</option><option value="timeToSecondHint">timeToSecondHint</option><option value="timeToThirdHint">timeToThirdHint</option><option value="probDiff">probDiff</option></select></div><div class="form-group col-xs-3"><select class="form-control" name="selectOp"><option value="">--Select Operator--</option><option value="Equals">Equals</option><option value="Greater_than">Greater than</option><option value="Less_than">Less than</option></select></div><div class="form-group col-xs-3"><input type="text" name="highLightVal" class="form-control" placeholder="--Highlight Value--"/></div></form>';
    var colorSelectHTML = "<div class='form-group col-xs-3'><button type='button' class='btn btn-secondary probHistorcolorSelector'>Pick Color</button></div></div>";
    var temp = html+""+colorSelectHTML;
    $('#newHighlightRulecontainer_studentProblemHistoryLog').append(temp);
    return indexStudent;

}
function loadAllDataTables(data){
    // var json = JSON.parse(data);

    $('body').on('click', function (e) {
        $('.fa-sticky-note').each(function () {
            if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
                $(this).popover('hide');
            }
        });
        $('.colorSelector').each(function () {
            if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
                $(this).popover('hide');
            }
        });
        $('.probHistorcolorSelector').each(function () {
            if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
                $(this).popover('hide');
            }
        });

    });
    var eventLogData = 	data['eventLog'];
    var studentProblemHistoryData = data['studentProblemHistory'];
    var otable = $("#eventLogTable").DataTable({
        "bPaginate": true,
        "bFilter": false,
        "bLengthChange": false,
        colReorder: true,
        destroy: true,
        "bInfo": false,
        "aaData": eventLogData ,

        "columnDefs": [
            {"targets": 0,"data": "download_link","render": function ( data, type, full, meta ) {var index = 'myModal'+meta.row; return '<a class="fa fa-sticky-note"  style="cursor:pointer;" aria-hidden="true"></a>'}},
            { "width": "5%", "targets": 1 },
            { "width": "5%", "targets": 2 },
            { "width": "5%", "targets": 3 },
            { "width": "5%", "targets": 4 },
            { "width": "5%", "targets": 5 },
            { "width": "5%", "targets": 6 },
            { "width": "5%", "targets": 7 },
            { "width": "5%", "targets": 8},
            { "width": "10%", "targets": 9},
            { "width": "5%", "targets": 10 },
            { "width": "5%", "targets": 11},
            { "width": "5%", "targets": 12},
            { "width": "5%", "targets": 13},
            { "width": "5%", "targets": 14},
            { "width": "5%", "targets": 15},
            { "width": "5%", "targets": 16 },
            { "width": "5%", "targets": 17 },
            { "width": "5%", "targets": 18}
        ],
        "aoColumns": [
            {"mDataProp" : "make a note",
                "name" : "make_a_note"
            },
            {"mDataProp" : "id",
                "name" : "id"},
            {"mDataProp" : "studId",
                "name" : "studId"},
            {"mDataProp" : "sessNum",
                "name" : "sessNum"},
            {"mDataProp" : "action",
                "name" : "action"},
            {"mDataProp" : "userInput",
                "name" : "userInput"},
            {"mDataProp" : "isCorrect",
                "name" : "isCorrect"},
            {"mDataProp" : "elapsedTime",
                "name" : "elapsedTime"},
            {"mDataProp" : "probElapsed",
                "name" : "probElapsed"},
            {"mDataProp" : "problemId",
                "name" : "problemId"},
            {"mDataProp" : "hintStep",
                "name" : "hintStep"},
            {"mDataProp" : "hintId",
                "name" : "hintId"},
            {"mDataProp" : "emotion",
                "name" : "emotion"},
            {"mDataProp" : "activityName",
                "name" : "activityName"},
            {"mDataProp" : "auxId",
                "name" : "auxId"},
            {"mDataProp" : "auxTable",
                "name" : "auxTable"},
            {"mDataProp" : "time",
                "name" : "time"},
            {"mDataProp" : "curTopicId",
                "name" : "curTopicId"},
            {"mDataProp" : "testerNote",
                "name" : "testerNote"},
            {"mDataProp" : "clickTime",
                "name" : "clickTime"}
        ],
        drawCallback: function() {
            $('.fa-sticky-note').popover({
                title: 'Developers Note',
                html : true,
                container: 'body',
                content: function() {
                    var tr = $(this).closest('tr');
                    var idx = otable.row( tr ).index();
                    var data = otable.row( idx).data();
                    return '<div> <form id="formId'+idx+'" method="POST"> <div class="form-group"> <label for="exampleSelect1">Type</label> <select id="noteType'+idx+'" class="form-control"> <option value="BUG">BUG</option> <option value="NOTE">NOTE</option></select> </div><div class="form-group"><label for="exampleTextarea">Tester Comments</label><textarea id="comments'+idx+'" class="form-control" rows="5"></textarea></div> <div class="form-group"><button type="button" onclick="publishTesterNote('+idx+')" class="btn btn-success">Save changes</button></div></form><span id="img'+idx+'" style="display: none"><i class="fa fa-spinner fa-spin" style="font-size:24px"></i></span><div id = "statusMessage'+idx+'" align = "center"></div></div>';
                }});}
    });

    var phtable = $("#studentProblemHistoryTable").dataTable({
        "bPaginate": true,
        "bFilter": false,
        "bLengthChange": false,
        colReorder: true,
        destroy: true,
        "bInfo": false,
        "aaData": studentProblemHistoryData ,
        "aoColumns": [
            {"mDataProp" : "make a note",
                "name" : "make_a_note"
            },
            {"mDataProp" : "id",
                "name" : "id"
            },
            {"mDataProp" : "studId",
                "name" : "studId"
            },
            {"mDataProp" : "sessionId",
                "name" : "sessionId"},
            {"mDataProp" : "problemId",
                "name" : "problemId"},
            {"mDataProp" : "topicId",
                "name" : "topicId"},
            {"mDataProp" : "problemBeginTime",
                "name" : "problemBeginTime"},
            {"mDataProp" : "problemEndTime",
                "name" : "problemEndTime"},
            {"mDataProp" : "timeInSession",
                "name" : "timeInSession"},
            {"mDataProp" : "timeInTutor",
                "name" : "timeInTutor"},
            {"mDataProp" : "timeToFirstAttempt",
                "name" : "timeToFirstAttempt"},
            {"mDataProp" : "timeToFirstHint",
                "name" : "timeToFirstHint"},
            {"mDataProp" : "timeToSolve",
                "name" : "timeToSolve"},
            {"mDataProp" : "numMistakes",
                "name" : "numMistakes"},
            {"mDataProp" : "numHints",
                "name" : "numHints"},
            {"mDataProp" : "videoSeen",
                "name" : "videoSeen"},
            {"mDataProp" : "numAttemptsToSolve",
                "name" : "numAttemptsToSolve"},
            {"mDataProp" : "solutionHintGiven",
                "name" : "solutionHintGiven"},
            {"mDataProp" : "mode",
                "name" : "mode"},
            {"mDataProp" : "mastery",
                "name" : "mastery"},
            {"mDataProp" : "emotionAfter",
                "name" : "emotionAfter"},
            {"mDataProp" : "emotionLevel",
                "name" : "emotionLevel"},
            {"mDataProp" : "effort",
                "name" : "effort"},
            {"mDataProp" : "exampleSeen",
                "name" : "exampleSeen"},
            {"mDataProp" : "textReaderUsed",
                "name" : "textReaderUsed"},
            {"mDataProp" : "numHintsBeforeSolve",
                "name" : "numHintsBeforeSolve"},
            {"mDataProp" : "isSolved",
                "name" : "isSolved"},
            {"mDataProp" : "adminFlag",
                "name" : "adminFlag"},
            {"mDataProp" : "authorFlag",
                "name" : "authorFlag"},
            {"mDataProp" : "collaboratedWith",
                "name" : "collaboratedWith"},
            {"mDataProp" : "timeToSecondAttempt",
                "name" : "timeToSecondAttempt"},
            {"mDataProp" : "timeToThirdAttempt",
                "name" : "timeToThirdAttempt"},
            {"mDataProp" : "timeToSecondHint",
                "name" : "timeToSecondHint"},
            {"mDataProp" : "timeToThirdHint",
                "name" : "timeToThirdHint"},
            {"mDataProp" : "probDiff",
                "name" : "probDiff"}
        ],

        "columnDefs": [
            {"targets": 0,"data": "download_link","render": function ( data, type, full, meta ) {var index = 'myModal'+meta.row; return '<a class="fa fa-sticky-note"  style="cursor:pointer;" aria-hidden="true"></a>'}},
            { "width": "5%", "targets": 1 },
            { "width": "5%", "targets": 2 },
            { "width": "5%", "targets": 3 },
            { "width": "5%", "targets": 4 },
            { "width": "5%", "targets": 5 },
            { "width": "5%", "targets": 6 },
            { "width": "5%", "targets": 7 },
            { "width": "5%", "targets": 8},
            { "width": "10%", "targets": 9},
            { "width": "5%", "targets": 10 },
            { "width": "5%", "targets": 11},
            { "width": "5%", "targets": 12},
            { "width": "5%", "targets": 13},
            { "width": "5%", "targets": 14},
            { "width": "5%", "targets": 15},
            { "width": "5%", "targets": 16 },
            { "width": "5%", "targets": 17 },
            { "width": "5%", "targets": 18},
            { "width": "5%", "targets": 19 },
            { "width": "5%", "targets": 20 },
            { "width": "5%", "targets": 21 },
            { "width": "5%", "targets": 22 },
            { "width": "5%", "targets": 23 },
            { "width": "5%", "targets": 24 },
            { "width": "5%", "targets": 25 },
            { "width": "5%", "targets": 26},
            { "width": "10%", "targets": 27},
            { "width": "5%", "targets": 28 },
            { "width": "5%", "targets": 29},
            { "width": "5%", "targets": 30},
            { "width": "5%", "targets": 31},
            { "width": "5%", "targets": 32},
            { "width": "5%", "targets": 33}
        ],
        drawCallback: function() {
            $('.fa-sticky-note').popover({
                title: 'Developers Note',
                html : true,
                container: 'body',
                content: function() {
                    var tr = $(this).closest('tr');
                    var idx = otable.row( tr ).index();
                    var data = otable.row( idx).data();
                    return '<div> <form id="formId'+idx+'" method="POST"> <div class="form-group"> <label for="exampleSelect1">Type</label> <select id="noteType'+idx+'" class="form-control"> <option value="BUG">BUG</option> <option value="NOTE">NOTE</option></select> </div><div class="form-group"><label for="exampleTextarea">Tester Comments</label><textarea id="comments'+idx+'" class="form-control" rows="5"></textarea></div> <div class="form-group"><button type="button" onclick="publishTesterNote('+idx+')" class="btn btn-success">Save changes</button></div></form><span id="img'+idx+'" style="display: none"><i class="fa fa-spinner fa-spin" style="font-size:24px"></i></span><div id = "statusMessage'+idx+'" align = "center"></div></div>';
                }});}
    });
}

function configureModalWindowForPopup(){

    var width1= $(window).width();
    var width2= width1* 0.9;
    var height1= $(window).height();
    var height2= height1* 0.9;
    $("#getEventLogs").click(function(){
        $.ajax({
            url : "/"+sysGlobals.wayangServletContext + "/TutorBrain?action=GetEventLogData&type=getLoggedData&sessionId=" + globals.sessionId +"&elapsedTime="+ globals.elapsedTime + "&eventCounter="+ sysGlobals.eventCounter++,
            traditional : true,
            data : globals.sessionId,
            type : "POST",
            contentType : "application/json;",
            success : function(data) {

                loadAllDataTables(data);
                $("#newHighlightRulecontainer_eventLog").empty().off("*");
                $("#newHighlightRulecontainer_studentProblemHistoryLog").empty().off("*");
                $('a.toggle-vis').on( 'click', function (e) {
                    e.preventDefault();

                    // Get the column API object
                    var column = $("#eventLogTable").DataTable().column( $(this).attr('data-column') );
                    var columnForStudentProblemHistory = $("#studentProblemHistoryTable").DataTable().column( $(this).attr('data-column') );

                    if($(this).find('span:first').hasClass('glyphicon-remove')){
                        $(this).find('span:first').toggleClass( 'glyphicon-remove', false );
                        $(this).find('span:first').toggleClass( 'glyphicon-plus', true );
                    }else{
                        $(this).find('span:first').toggleClass( 'glyphicon-remove', true );
                        $(this).find('span:first').toggleClass( 'glyphicon-plus', false );
                    }

                    // Toggle the visibility
                    column.visible( ! column.visible() );
                    columnForStudentProblemHistory.visible( ! columnForStudentProblemHistory.visible() );
                } );


                //$("#eventLogTable").css("max-width","1000px;")
                $("#eventLogWindow").dialog({"width" : width2},{"height" : height2}).dialogExtend({
                    "closable" : true, // enable/disable close button
                    "maximizable" : true, // enable/disable maximize button
                    "minimizable" : true, // enable/disable minimize button
                    "collapsable" : false, // enable/disable collapse button
                    "dblclick" : "collapse", // set action on double click. false, 'maximize', 'minimize', 'collapse'
                    "minimizeLocation" : "left", // sets alignment of minimized dialogues
                    "icons" : { // jQuery UI icon class
                        "close" : "ui-icon-circle-close",
                        "maximize" : "ui-icon-circle-plus",
                        "minimize" : "ui-icon-circle-minus"
                    },
                    "restore": function (event, ui) {
                        $('#myPleaseWait').modal('show');
                        $.ajax({
                            url : "/"+sysGlobals.wayangServletContext + "/TutorBrain?action=GetEventLogData&type=getLoggedData&sessionId=" + globals.sessionId +"&elapsedTime="+ globals.elapsedTime + "&eventCounter="+ sysGlobals.eventCounter++,
                            traditional : true,
                            data : globals.sessionId,
                            type : "POST",
                            contentType : "application/json;",
                            success : function(data) {
                                $('#myPleaseWait').modal('hide');
                                $("#newHighlightRulecontainer_eventLog").empty().off("*");
                                $("#newHighlightRulecontainer_studentProblemHistoryLog").empty().off("*");
                                loadAllDataTables(data);
                            } ,
                            error : function(xhr, status, errorThrown) {
                            },
                            complete : function(xhr, status) {
                            }
                        });

                    }
                });
            },
            error : function(xhr, status, errorThrown) {
            },
            complete : function(xhr, status) {
            }
        });
    });


}
