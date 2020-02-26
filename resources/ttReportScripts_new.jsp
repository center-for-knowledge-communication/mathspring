<%@ page import="java.util.ResourceBundle"%>
<script type="text/javascript">
/**
 * Created by nsmenon on 6/1/2017.
 * fsylvia 7/1/2019 - Converted to multi-lingual JSP
 * Frank 	10-15-19	Issue #7 perStudentperProblemReport report
 * Frank 	10-22-19	Issue #14 fix locale string parameter in some reports
 * Frank 	11-25-19	Issue #13 add standards filter for per student per problem report
 * Frank 	11-26-19	Issue #33 class summary 'today's problems' not working - 'faulty date compare'
 * Frank 	12-21-19	Issue #21 this file is being re-released with issue 21 to correct EOL characters which were inadvertently changed to unix style
 *						  The entire file should be replaced during 'pull request & comparison' process.
 * Frank	01-16-20  	Restore corrupted Spanish Special Characters
 * Frank    02-17-20    ttfixesR3
 */
 
 //Report1 Variables
var perProblemSetReport;
var perProblemSetLevelOne;
var perProblemSetLevelTwo;
var perProblemSetColumnNamesMap;
var perStudentPerProblemColumnNamesMap;
var perProblemSetLevelOneAvg;
var perProblemSetLevelOneMax;
var perProblemSetLevelOneLatest;

//Report2 Varriables
var perProblemReportTable

//Report3 Varriables
var perClusterReportTable

//Report5 Varribales
var perStudentReport;
var effortMap;
var perProblemObject;
var emotionMap;
var commentsMap;
var eachStudentData = [];
var activetable;
var inactivetable;
var studentRosterTable;
var surveyData;
var surveyReportTable;
var studentData;
var surveyStudentTable;
var surveyQuestionTable;
var apply_content_table;

var languagePreference = window.navigator.language;
var languageSet = "en";
var loc = "en-US";

if (languagePreference.includes("en")) {
	languageSet = "en"
	loc = "en-US";
} else if (languagePreference.includes("es")) {
	languageSet = "es"
	loc = "es-Ar";
}


<% 
/**
try {
	
	Locale loc = new Locale(languagePreference.substring(0,2),languagePreference.substring(2,4));
	rb = ResourceBundle.getBundle("MathSpring",loc);
}
catch (Exception e) {
	// Log error	
}
*/
%>

//Summary report variable
var summaryReport;

var effortLabelMap;

if (languageSet == 'es') {

	effortLabelMap = {
			"SKIP" : "El estudiante saltó el problema (no hizo nada al respecto)",
            "NOTR" : "Ni siquiera LEYENDO el problema - El estudiante respondió demasiado rápido, en menos de 4 segundos",
            "GIVEUP" : "El estudiante comenzó a trabajar en el problema, pero luego AUMENTÓ y siguió adelante sin resolverlo correctamente.",
            "SOF" :  "El estudiante resolvió el problema correctamente en el PRIMER intento, sin ninguna ayuda.",
            "ATT" : "El estudiante INTENTÓ una vez incorrectamente, pero se corrigió (contestó correctamente) en el segundo intento, no se solicitó ayuda.",
            "GUESS" : "El estudiante aparentemente, HABLADO, hizo clic en 3-5 respuestas hasta obtener la correcta, y no le pidió pistas / videos, etc.",
            "SHINT" : "El estudiante resolvió el problema correctamente después de ver PISTAS.",
            "SHELP" : "Consiguió el problema correcto, pero vio al menos un video.",
            "NO DATA" : "No se pudieron recopilar datos."
    }
}
else {
	effortLabelMap = {
		  "SKIP" : "The student SKIPPED the problem (didn't do anything on the problem)",
          "NOTR" : "NOT even READING the problem --The student answered too fast, in less than 4 seconds",
          "GIVEUP" : "The student started working on the problem, but then GAVE UP and moved on without solving it correctly.",
          "SOF" :  "The student SOLVED the problem correctly on the FIRST attempt, without any help.",
          "ATT" : "The student ATTEMPTED once incorrectly, but self-corrected (answered correctly) in the second attempt, no help request.",
          "GUESS" : "The student apparently GUESSED, clicked through 3-5 answers until getting the right one, and did not ask for hints/videos etc.",
          "SHINT" : "Student SOLVED problem correctly after seeing HINTS.",
          "SHELP" : "Got the problem correct but saw atleast one video.",
          "NO DATA" : "No data could be gathered."
    }
}
var effortChartOnPopup;
function loadEffortMap (rows,flag) {
    if (flag) {
        if ($("#iconID" + rows).hasClass('fa fa-th')) {
            $("#iconID" + rows).removeClass();
            $("#iconID" + rows).addClass('fa fa-times');
            var effortChartIdSelector = "#effortChart" + rows;
            var containerChartSelector = "#containerChart" + rows;
            var legendChart = "#legendChart" + rows;
            var effortValues = effortMap[rows];
            var effortData;
            
            if (languageSet == 'es') {
              effortData = {
                labels: ["Tipo de comportamiento en problema"],
                datasets: [{
                    backgroundColor: "#8dd3c7",
                    label: 'OMITIR: El estudiante saltó el problema (no hizo nada al respecto)',
                    data: [effortValues[0]]
                }, {
                    backgroundColor: "#ffffb3",
                    label: 'NOLEYO: Ni siquiera LEYENDO el problema - El estudiante respondió demasiado rápido, en menos de 4 segundos',
                    data: [effortValues[1]]
                }, {
                    backgroundColor: "#bebada",
                    label: 'ABANDONO: El estudiante comenzó a trabajar en el problema, pero luego AUMENTÓ y siguió adelante sin resolverlo correctamente.',
                    data: [effortValues[2]]
                }, {
                    backgroundColor: "#fb8072",
                    label: 'CORRECTO: El estudiante resolvió el problema correctamente en el PRIMER intento, sin ninguna ayuda.',
                    data: [effortValues[3]]
                }, {
                    backgroundColor: "#b3de69",
                    label: 'AUTOCORRIGIO: El estudiante INTENTÓ una vez incorrectamente, pero se corrigió (contestó correctamente) en el segundo intento, no se solicitó ayuda.',
                    data: [effortValues[4]]
                }, {
                    backgroundColor: "#fccde5",
                    label: 'ADIVINO: El estudiante aparentemente, HABLADO, hizo clic en 3-5 respuestas hasta obtener la correcta, y no le pidió pistas / videos, etc.',
                    data: [effortValues[5]]
                }, {
                    backgroundColor: "#80b1d3",
                    label: 'CONAYUDA: El estudiante resolvió el problema correctamente después de ver PISTAS.',
                    data: [effortValues[6]]
                }, {
                    backgroundColor: "#fdb462",
                    label: 'SCONAYUDA: Consiguió el problema correcto, pero vio al menos un video.',
                    data: [effortValues[7]]
                }, {
                    label: 'NOHAYDATOS: No se pudieron recopilar datos.',
                    backgroundColor: "#d9d9d9",
                    data: [effortValues[8]],
                },]
              };
            }
            else {
                effortData = {
                        labels: ["Type of behaviour in problem"],
                        datasets: [{
                            backgroundColor: "#8dd3c7",
                            label: 'SKIP: The student SKIPPED the problem (did not do anything on the problem)',
                            data: [effortValues[0]]
                        }, {
                            backgroundColor: "#ffffb3",
                            label: 'NOTR: NOT even READING the problem --The student answered too fast, in less than 4 seconds',
                            data: [effortValues[1]]
                        }, {
                            backgroundColor: "#bebada",
                            label: 'GIVEUP: The student started working on the problem, but then GAVE UP and moved on without solving it correctly.',
                            data: [effortValues[2]]
                        }, {
                            backgroundColor: "#fb8072",
                            label: 'SOF: The student SOLVED the problem correctly on the FIRST attempt, without any help.',
                            data: [effortValues[3]]
                        }, {
                            backgroundColor: "#b3de69",
                            label: 'ATT: The student ATTEMPTED once incorrectly, but self-corrected (answered correctly) in the second attempt, no help.',
                            data: [effortValues[4]]
                        }, {
                            backgroundColor: "#fccde5",
                            label: 'GUESS: The student apparently GUESSED, clicked through 3-5 answers until getting the right one',
                            data: [effortValues[5]]
                        }, {
                            backgroundColor: "#80b1d3",
                            label: 'SHINT: Student SOLVED problem correctly after seeing HINTS.',
                            data: [effortValues[6]]
                        }, {
                            backgroundColor: "#fdb462",
                            label: 'SHELP: Got the problem correct but saw atleast one video.',
                            data: [effortValues[7]]
                        }, {
                            label: 'NO DATA: No data could be gathered.',
                            backgroundColor: "#d9d9d9",
                            data: [effortValues[8]],
                        },]
                      };            	
            }
            var emotionChart = new Chart($(effortChartIdSelector), {
            type: 'horizontalBar',
            data: effortData,
            options: {
                responsive: false,
                legend: {
                    display: false
                }, legendCallback: function(chart) {
                    var text = [];
                    text.push('<table class="' + chart.id + '-legend">');
                    for (var i = 0; i < chart.data.datasets.length; i++) {
                        text.push('<tr><td><span style="background-color:' + chart.data.datasets[i].backgroundColor + '">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>');
                        if (chart.data.datasets[i].label) {
                            text.push('<td>'+ chart.data.datasets[i].label + '</td></tr>');
                        }
                    }
                    text.push('</table>');
                    return text.join('');
                },
                scales: {
                    yAxes: [{
                        stacked: true,
                        ticks: {
                            beginAtZero: false,
                            callback: function (value) {
                                return value + "%"
                            }
                        },
                        display: false
                    }],
                    xAxes: [{
                        stacked: true,
                        ticks: {
                            beginAtZero: false
                        },
                        display: false
                    }]
                }
            }
        });
        $(legendChart).prepend(emotionChart.generateLegend());
        $(containerChartSelector).show();
    } else if ($("#iconID" + rows).hasClass('fa fa-times')) {
        $("#iconID" + rows).removeClass();
        $("#iconID" + rows).addClass('fa fa-th');
        var containerChartSelector = "#containerChart" + rows;
        var legendChart = "#legendChart" + rows;
        $(containerChartSelector).hide();
        $(legendChart).empty();
    } }else {
        $('#problemSnapshot').empty();
        var completeValues = perProblemObject[rows];
        var effortValues = completeValues['studentEffortsPerProblem'];
        var imageURL = problem_imageURL+rows+'.jpg';
        var legendChart = "#lengendTable";
        $('#problemSnapshot').append('<span><strong><%= rb.getString("problem_id")%> :'+rows+'</strong></span>');
        $('#problemSnapshot').append('<img src="'+imageURL +'"/>');
        if(effortChartOnPopup) {
            effortChartOnPopup.destroy();
            $(legendChart).empty();
        }

        if (languageSet == 'es') {
			effortData = {
            labels: ["Tipo de comportamiento en problema"],
            datasets: [{
                backgroundColor: "#8dd3c7",
                label: 'SKIP: El estudiante saltó el problema (no hizo nada al respecto)',
                data: [effortValues[0]]
            }, {
                backgroundColor: "#ffffb3",
                label: 'NOTR: Ni siquiera LEYENDO el problema - El estudiante respondió demasiado rápido, en menos de 4 segundos',
                data: [effortValues[1]]
            }, {
                backgroundColor: "#bebada",
                label: 'GIVEUP: El estudiante comenzó a trabajar en el problema, pero luego AUMENTÓ y siguió adelante sin resolverlo correctamente.',
                data: [effortValues[2]]
            }, {
                backgroundColor: "#fb8072",
                label: 'SOF: El estudiante resolvió el problema correctamente en el PRIMER intento, sin ninguna ayuda.',
                data: [effortValues[3]]
            }, {
                backgroundColor: "#b3de69",
                label: 'ATT: El estudiante INTENTÓ una vez incorrectamente, pero se corrigió (contestó correctamente) en el segundo intento, no se solicitó ayuda.',
                data: [effortValues[4]]
            }, {
                backgroundColor: "#fccde5",
                label: 'GUESS: El estudiante aparentemente, HABLADO, hizo clic en 3-5 respuestas hasta obtener la correcta, y no le pidió pistas / videos, etc.',
                data: [effortValues[5]]
            }, {
                backgroundColor: "#80b1d3",
                label: 'SHINT: El estudiante resolvió el problema correctamente después de ver PISTAS.',
                data: [effortValues[6]]
            }, {
                backgroundColor: "#fdb462",
                label: 'SHELP: Consiguió el problema correcto, pero vio al menos un video.',
                data: [effortValues[7]]
            }, {
                label: 'NO DATA: NO DATA: No se pudieron recopilar datos.',
                backgroundColor: "#d9d9d9",
                data: [effortValues[8]],
            },]
        };
        }
        else {
			effortData = {
		            labels: ["Type of behaviour in problem"],
		            datasets: [{
		                backgroundColor: "#8dd3c7",
		                label: 'SKIP: The student SKIPPED the problem (did not do anything on the problem)',
		                data: [effortValues[0]]
		            }, {
		                backgroundColor: "#ffffb3",
		                label: 'NOTR: NOT even READING the problem --The student answered too fast, in less than 4 seconds',
		                data: [effortValues[1]]
		            }, {
		                backgroundColor: "#bebada",
		                label: 'GIVEUP: The student started working on the problem, but then GAVE UP and moved on without solving it correctly.',
		                data: [effortValues[2]]
		            }, {
		                backgroundColor: "#fb8072",
		                label: 'SOF: The student SOLVED the problem correctly on the FIRST attempt, without any help.',
		                data: [effortValues[3]]
		            }, {
		                backgroundColor: "#b3de69",
		                label: 'ATT: The student ATTEMPTED once incorrectly, but self-corrected (answered correctly) in the second attempt, no help.',
		                data: [effortValues[4]]
		            }, {
		                backgroundColor: "#fccde5",
		                label: 'GUESS: The student apparently GUESSED, clicked through 3-5 answers until getting the right one',
		                data: [effortValues[5]]
		            }, {
		                backgroundColor: "#80b1d3",
		                label: 'SHINT: Student SOLVED problem correctly after seeing HINTS.',
		                data: [effortValues[6]]
		            }, {
		                backgroundColor: "#fdb462",
		                label: 'SHELP: Got the problem correct but saw atleast one video.',
		                data: [effortValues[7]]
		            }, {
		                label: 'NO DATA: No data could be gathered.',
		                backgroundColor: "#d9d9d9",
		                data: [effortValues[8]],
		            },]
		        };        	
        }
            effortChartOnPopup = new Chart($("#studentEffortRecordedProblemCanvas"), {
            type: 'horizontalBar',
            data: effortData,
            options: {
                responsive: false,
                legend: {
                    display: false
                }, legendCallback: function(chart) {
                    var text = [];
                    text.push('<table class="' + chart.id + '-legend">');
                    for (var i = 0; i < chart.data.datasets.length; i++) {
                        text.push('<tr><td><span style="background-color:' + chart.data.datasets[i].backgroundColor + '">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>');
                        if (chart.data.datasets[i].label) {
                            text.push('<td>'+ chart.data.datasets[i].label + '</td></tr>');
                        }
                    }
                    text.push('</table>');
                    return text.join('');
                },
                scales: {
                    yAxes: [{
                        stacked: true,
                        ticks: {
                            beginAtZero: false,
                            callback: function (value) {
                                return value + "%"
                            }
                        },
                        display: false
                    }],
                    xAxes: [{
                        stacked: true,
                        ticks: {
                            beginAtZero: false
                        },
                        display: false
                    }]
                }
            }
        });
        $(legendChart).prepend(effortChartOnPopup.generateLegend());
        $("#studentEffortRecordedProblem").modal('show');
    }

}

function loadEmotionMap (rows) {
    var studentEmotions = emotionMap[rows];
    var containerChartSelector = "#emotionContainerChart" + rows;
    if($("#emotioniconID" + rows).hasClass('fa fa-heart')) {
        $("#emotioniconID" + rows).removeClass();
        $("#emotioniconID" + rows).addClass('fa fa-times');
        if (!jQuery.isEmptyObject(studentEmotions)) {
            Object.keys(studentEmotions).forEach(function (key) {
                var emotionChartCanvas = "#" + rows + key;
                var percentValues = jQuery.extend(true, [], studentEmotions[key]);
                var keyValue = key;
                percentValues[0] = Math.round(percentValues[0] / percentValues[5] * 100);
                percentValues[1] = Math.round(percentValues[1] / percentValues[5] * 100);
                percentValues[2] = Math.round(percentValues[2] / percentValues[5] * 100);
                percentValues[3] = Math.round(percentValues[3] / percentValues[5] * 100);
                percentValues[4] = Math.round(percentValues[4] / percentValues[5] * 100);
                var barLabels = ['<%= rb.getString("not_at_all")%>', '<%= rb.getString("a_little")%>', '<%= rb.getString("somewhat")%>', '<%= rb.getString("quite_a_bit")%>', '<%= rb.getString("extremely")%>'];
                var colorCodes;
                if (keyValue == 'Frustration') {
                    colorCodes = ['#FFB2B2', '#FF7F7F', '#FF4C4C', '#FF1919', '#CC0000'];
                } else if (keyValue == 'Confidence') {
                    colorCodes = ['#CCD8F1', '#99B1E3', '#668BD5', '#3364C7', '#003EBA'];
                } else if (keyValue == 'Excitement') {
                    colorCodes = ['#FFEAD2', '#FFD6A5', '#FFC278', '#FFAE4B', '#FF9A1F'];
                } else if (keyValue == 'Interest') {
                    colorCodes = ['#D4FBD1', '#AAF7A3', '#7FF375', '#55EF47', '#2BEB1A'];
                }

                var emotionChart = new Chart($(emotionChartCanvas), {
                    type: 'horizontalBar',
                    data: {
                        labels: [keyValue],
                        datasets: [{
                            backgroundColor: colorCodes[0],
                            label: barLabels[0],
                            data: [percentValues[0]]
                        }, {
                            backgroundColor: colorCodes[1],
                            label: barLabels[1],
                            data: [percentValues[1]]
                        }, {
                            backgroundColor: colorCodes[2],
                            label: barLabels[2],
                            data: [percentValues[2]]
                        }, {
                            backgroundColor: colorCodes[3],
                            label: barLabels[3],
                            data: [percentValues[3]]
                        }, {
                            backgroundColor: colorCodes[4],
                            label: barLabels[4],
                            data: [percentValues[4]]
                        }]
                    },
                    options: {
                        responsive: false,
                        legend: {
                            display: false
                        },
                        scales: {
                            yAxes: [{
                                stacked: true,
                                ticks: {
                                    beginAtZero: false,
                                    callback: function(value) {
                                        return value + "%"
                                    }
                                },
                                display: false
                            }],
                            xAxes: [{
                                stacked: true,
                                ticks: {
                                    beginAtZero: false
                                },
                                display: false
                            }]
                        }
                    }
                });

            });
        }
        $(containerChartSelector).show();
    }else if($("#emotioniconID" + rows).hasClass('fa fa-times')){
        $("#emotioniconID" + rows).removeClass();
        $("#emotioniconID" + rows).addClass('fa fa-heart');
        $(containerChartSelector).hide();
    }
}


function resetStudentData( title,studentId) {
    $.ajax({
        type : "POST",
        url :pgContext+"/tt/tt/resetStudentdata",
        data : {
            studentId: studentId,
            action: title,
            lang: loc
        },
        success : function(response) {
            if (response.includes("***")) {
                $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
                $('#errorMsgModelPopup').modal('show');
            }else{
                $("#successMsgModelPopup").find("[class*='modal-body']").html("<%= rb.getString("student_info_updated")%>");
                $('#successMsgModelPopup').modal('show');
            }
        }
    });
    return false;

}

function resetPassWordForThisStudent(id,uname){
    var newPassWordToSet = $("#resetPasswordfor"+id).serializeArray()[0].value;
     $.ajax({
         type : "POST",
         url :pgContext+"/tt/tt/resetStudentPassword",
         data : {
             studentId: id,
             userName: uname,
             newPassWord : newPassWordToSet
         },
         success : function(response) {
             if (response.includes("***")) {
                 $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
                 $('#errorMsgModelPopup').modal('show');
             }else{
                 $("#successMsgModelPopup").find("[class*='modal-body']").html( "<%= rb.getString("password_is_reset")%>  <%= rb.getString("new_password_is")%>"+response+"");
                 $('#successMsgModelPopup').modal('show');
             }
         }
     });
    return false;

}

function cnfirmStudentPasswordForTagDownload() {
    window.location.href = pgContext + "/tt/tt/printStudentTags" + "?classId=" + classID + "&formdata=" + classID;
    cnfirmPasswordToDownLoadTag
}


function updateStudentInfo(formName){
    var dataForm = $("#edit_Student_Form"+formName).serializeArray();
    var values = [];
    $.each(dataForm, function(i, field){
        values[i] = field.value;
    });
    $.ajax({
        type : "POST",
        url :pgContext+"/tt/tt/editStudentInfo",
        data : {
            studentId: formName,
            formData: values,
            lang: loc
        },
        success : function(response) {
            if (response.includes("***")) {
                $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
                $('#errorMsgModelPopup').modal('show');
            }else{
                $("#successMsgModelPopup").find("[class*='modal-body']").html( response );
                $('#successMsgModelPopup').modal('show');
            }
        }

    });
}
/** Moved back to classDetail.jsp
function editStudentInformation(id,fname,lname,uname,context){
    var tr = context.closest('tr')
    var row = $('#student_roster').DataTable().row( tr );

    if ( row.child.isShown() ) {
        row.child( false ).remove();
    }else{
       // var editStudentInfoDiv = $($('#editStudentInfoDiv').html());
        if(fname == ''){
            var tempStudentName =  '<div class="form-group"><div class="input-group"><label for="studentFname">First Name</label></div><div class="input-group">'+
                '<input type="text" id="studentFname" class="form-control" name="studentFname" /></div></div>';
        }else{
            var tempStudentName =  '<div class="form-group"><div class="input-group"><label for="studentFname">First Name</label></div><div class="input-group">'+
                '<input type="text" value='+fname+' id="studentFname" class="form-control" name="studentFname" /></div></div>';
        }

        if(lname == ''){
            var tempStudentLastName =  '<div class="form-group"><div class="input-group"><label for="studentLname">Last Name</label></div><div class="input-group">'+
                '<input type="text" id="studentLname" class="form-control" name="studentLname" /></div></div>';

        }   else{
            var tempStudentLastName =  '<div class="form-group"><div class="input-group"><label for="studentLname">Last Name</label></div><div class="input-group">'+
                '<input type="text" value='+lname+' id="studentLname" class="form-control" name="studentLname" /></div></div>';
        }

        var tempStudentUserName =  '<div class="form-group"><div class="input-group"><label for="studentUsername">Username</label></div><div class="input-group">'+
            '<input type="text" value='+uname+' id="studentUsername" class="form-control" name="studentUsername"/></div></div>';

        var formHtml = '<div class="panel-body"><form id="edit_Student_Form'+id+'" onsubmit="event.preventDefault();"><div class="form-group"><div class="input-group"><label for="studentId">UserId</label></div><div class="input-group">'+
            '<input type="text" value='+id+' id="studentId" class="form-control" name="studentId" disabled="disabled" /></div></div>'+tempStudentUserName
            + tempStudentName + tempStudentLastName +
            '<div class="input-group"><button role="button" onclick="updateStudentInfo('+id+')" class="btn btn-primary">Update Information</button></div></form></div>';

        var formHtmlPassWord = '<div class="panel-body"><form id="resetPasswordfor'+id+'" onsubmit="event.preventDefault();"><div class="form-group"><div class="input-group"><label for="newPassword">New Password</label></div><div class="input-group">'+
            '<input type="password"  placeholder="New password to be set" id="newPassword" class="form-control" name="newPassword"/></div></div>' +
            '<div class="input-group"><button role="button" onclick="resetPassWordForThisStudent('+id+',\'' + uname + '\')" type="button" class="btn btn-primary">Reset Password</button></div></form></div>';



        var tabPanel = '<div style="width: 40%"> <ul class="nav nav-tabs" role="tablist"> <li class="active"> ' +
            '<a href="#home'+id+'" role="tab" data-toggle="tab"> <i class="fa fa-address-card-o" aria-hidden="true"></i> Update Student Information </a> </li> ' +
            '<li><a href="#profile'+id+'" role="tab" data-toggle="tab"> <i class="fa fa-key" aria-hidden="true"></i> Reset Password for Student </a> </li> </ul>'+
            '<div class="tab-content"> <div class="tab-pane fade active in" id="home'+id+'">'+formHtml+'</div><div class="tab-pane fade" id="profile'+id+'">'+formHtmlPassWord+'</div> </div> </div>';

        row.child(tabPanel).show();

    }

}
*/
function problemDetails(data, response) {
    var JSONData = JSON.parse(response);
    var standards = JSONData["topicStandars"];
    var problems = JSONData["problems"];
    var html = "";
    $.each(standards, function (i, obj) {
        html += '<span style="margin-right: 10px;"><a href=' + obj.url + '>' + obj.code + '</a></span>';
    });

    var selector = "#"+JSONData["problemLevelId"]+"_handler";
if (languageSet == 'es') {
    $(document.body).on('click', selector ,function(){
        var rows = $("#"+JSONData["problemLevelId"]).dataTable(
            { "bPaginate": false,
                "bFilter": false,
                "bLengthChange": false,
                rowReorder: false,
                "bSort": false,
                "language": {
                    "sProcessing":     "Procesando...",
                    "sLengthMenu":     "Mostrar _MENU_ registros",
                    "sZeroRecords":    "No se encontraron resultados",
                    "sEmptyTable":     "Ningún dato disponible en esta tabla",
                    "sInfo":           "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
                    "sInfoEmpty":      "Mostrando registros del 0 al 0 de un total de 0 registros",
                    "sInfoFiltered":   "(filtrado de un total de _MAX_ registros)",
                    "sInfoPostFix":    "",
                    "sSearch":         "Buscar:",
                    "sUrl":            "",
                    "sInfoThousands":  ",",
                    "sLoadingRecords": "Cargando...",
                    "oPaginate": {
                        "sFirst":    "Primero",
                        "sLast":     "Último",
                        "sNext":     "Siguiente",
                        "sPrevious": "Anterior"
                    },
                    "oAria": {
                        "sSortAscending":  ": Activar para ordenar la columna de manera ascendente",
                        "sSortDescending": ": Activar para ordenar la columna de manera descendente"
                    }
                }
                
            }).fnGetNodes();

        var rowsArray = [];
        var problemIds = [""];
        var i = 0;
        $("input:checkbox:not(:checked)", rows).each(function(){
            rowsArray[i] = $(this).closest('tr');
            i++;
        });
        for(var j=0; j < rowsArray.length; j++)
            problemIds      [j]  = $("#"+JSONData["problemLevelId"]).DataTable().row( rowsArray [j] ).data()[1];

        $.ajax({
            type : "POST",
            url :pgContext+"/tt/tt/saveChangesForProblemSet",
            data : {
                problemIds: problemIds,
                classid: classID,
                problemsetId: JSONData["problemLevelId"]
            },
            success : function(response) {
                if (response.includes("***")) {
                    $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
                    $('#errorMsgModelPopup').modal('show');
                }else{
                    $("#successMsgModelPopupForProblemSets").find("[class*='modal-body']").html( "<%= rb.getString("content_changes_saved")%>" );
                    $('#successMsgModelPopupForProblemSets').modal('show');
                }
            }
        });

    });
}
else {
    $(document.body).on('click', selector ,function(){
        var rows = $("#"+JSONData["problemLevelId"]).dataTable(
            { "bPaginate": false,
                "bFilter": false,
                "bLengthChange": false,
                rowReorder: false,
                "bSort": false

                
            }).fnGetNodes();

        var rowsArray = [];
        var problemIds = [""];
        var i = 0;
        $("input:checkbox:not(:checked)", rows).each(function(){
            rowsArray[i] = $(this).closest('tr');
            i++;
        });
        for(var j=0; j < rowsArray.length; j++)
            problemIds      [j]  = $("#"+JSONData["problemLevelId"]).DataTable().row( rowsArray [j] ).data()[1];

        $.ajax({
            type : "POST",
            url :pgContext+"/tt/tt/saveChangesForProblemSet",
            data : {
                problemIds: problemIds,
                classid: classID,
                problemsetId: JSONData["problemLevelId"]
            },
            success : function(response) {
                if (response.includes("***")) {
                    $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
                    $('#errorMsgModelPopup').modal('show');
                }else{
                    $("#successMsgModelPopupForProblemSets").find("[class*='modal-body']").html( "<%= rb.getString("content_changes_saved")%>" );
                    $('#successMsgModelPopupForProblemSets').modal('show');
                }
            }
        });

    });	
}
var save_changes = "<%= rb.getString("save_changes")%>";
var higherlevelDetailp1="<%= rb.getString("problem_set")%>";
var higherlevelDetailp2="<%= rb.getString("standards_covered_in_problemset")%>";
var higherlevelDetailp3="<%= rb.getString("student_will_see_selected_problems")%>";

var higherlevelDetail = "<div id=" + data[0] + " class='panel-body animated zoomOut'> " +
    " <div class='panel panel-default'> <div class='panel-body'><strong>"+higherlevelDetailp1+": " + JSONData["topicName"] + "</strong></div> " +
    " <div class='panel-body'><strong>"+higherlevelDetailp2+": " + html + "</strong></div>" +
    " <div class='panel-body'><strong>Summary : " + JSONData["topicSummary"] + "</strong></div>"+
    "<div class='panel-body'>"+higherlevelDetailp3+"</div>"+
    "<div class='panel-body'> <button id="+JSONData["problemLevelId"]+'_handler'+" class='btn btn-primary btn-lg' aria-disabled='true'>"+save_changes+"</button></div></div>";


    return higherlevelDetail + problemLevelDetails(JSONData,problems);

}

function problemLevelDetails(JSONData,problems){
    var tableHeader = '<table id='+JSONData["problemLevelId"]+' class="table table-striped table-bordered hover" cellspacing="0" width="100%"><thead><tr><th><%= rb.getString("activated")%></th><th><%= rb.getString("id")%></th><th><%= rb.getString("name")%></th><th><%= rb.getString("nickname")%></th><th><%= rb.getString("difficulty")%></th><th><%= rb.getString("cc_standard")%></th><th>Type</th></tr></thead><tbody>';
    var attri = ", 'ProblemPreview'"+","+"'width=750,height=550,status=yes,resizable=yes'";
    $.each(problems, function (i, obj) {
        var html = "";
        var flash = "";
        var checkBox = "";
        var flashWindow = "'" + JSONData["uri"]+"?questionNum="+obj.problemNo + "'" + attri ;
        var htmlWindow =  "'" + JSONData["html5ProblemURI"]+obj.htmlDirectory+"/"+obj.resource+ "'" + attri;
        var imageURL = problem_imageURL+obj['id']+'.jpg';
        $.each(obj.ccStand, function (i, obj) {
            html += '<span style="margin-right: 10px;"><a href=' + obj.url + '>' + obj.code + '</a></span>';
        });
        if(obj.type=='flash'){
            flash = '<td><a rel="popoverPerProblem" data-img="' + imageURL + '">'+obj.name+'</a></td>';
        }else{
            flash = '<td><a href="'+pgContext+'/WoAdmin?action=AdminGetQuickAuthSkeleton&probId='+obj.id+'&teacherId=-1&reload=true&zoom=1" target="_blank" style="cursor:pointer;" rel="popoverPerProblem" data-img="' + imageURL + '">'+obj.name+'</a></td>';
        }
        if(obj.activated){
            checkBox =  "<tr><td><input type='checkbox' name='activated' checked='checked'></td>"
        }else{
            checkBox =  "<tr><td><input type='checkbox' name='activated'></td>"
        }
        tableHeader +=  checkBox+
            "<td>"+obj.id+"</td>"+
            flash+
            "<td>"+obj.nickName+"</td>"+
            "<td>"+obj.difficulty+"</td>"+
            "<td>"+html+"</td>"+
            "<td>"+obj.type+"</td></tr>";
    });
    return tableHeader + "</tbody><table></div>";
}

function handleclickHandlers() {
    $('#reports_handler').click(function () {
        $('#reorg_prob_sets_handler').css('background-color', '');
        $('#reorg_prob_sets_handler').css('color', '#dddddd');

        $("#content-conatiner").children().hide();
        $("#report-wrapper").show();
        $("#report-wrapper2").show();
    });

    $("#copyClass_handler").click(function () {
        $('#reorg_prob_sets_handler').css('background-color', '');
        $('#reorg_prob_sets_handler').css('color', '#dddddd');

        $("#content-conatiner").children().hide();
        $("#clone_class_out").show();
    });

    $("#reorg_prob_sets_handler").click(function () {
        $('#reorg_prob_sets_handler').css('color', '#ffffff');

        $("#content-conatiner").children().hide();
        $("#problem_set_content").show();
    });

    $("#resetSurveySettings_handler").click(function () {
        $('#reorg_prob_sets_handler').css('background-color', '');
        $('#reorg_prob_sets_handler').css('color', '#dddddd');

        $("#content-conatiner").children().hide();
        $("#reset_survey_setting_out").show();
    });


    $("#addMoreStudentsToClass").click(function () {
        $("#addMoreStudents").show();
        $("#addMoreStudentsToClass").prop('disabled', true);
    });

    $("#cancelForm").click(function () {
        $("#addMoreStudents").hide();
        $("#addMoreStudentsToClass").prop('disabled', false);
    });

    $("#reconfigure_student_handler").click(function () {
        $('#reorg_prob_sets_handler').css('background-color', '');
        $('#reorg_prob_sets_handler').css('color', '#dddddd');

        $("#content-conatiner").children().hide();
        $("#student_roster_out").show();
    });

    $('#activateProbSetTable input[type="checkbox"]').click(function () {
        if ($('#activateProbSetTable input[type="checkbox"]:checked').size()) {
            $('#deacivateProblemSets').prop('disabled', false);
        } else {
            $('#deacivateProblemSets').prop('disabled', true);
        }
    });

    $('#select_activeSurveyList').click(function () {
        var client_table = $("#activeSurveyList").dataTable();
        var activate_Pre_Data;
        var activate_Post_Data;
        $( client_table.$('input[type="radio"]:checked').map(function () {
            var name = $(this)[0].name;
            if(name == 'pre_id')
                activate_Pre_Data =  $("#activeSurveyList").DataTable().row($(this).closest('tr') ).data()[0];
            else
                activate_Post_Data =  $("#activeSurveyList").DataTable().row($(this).closest('tr') ).data()[0];
        } ) );

        var surveyToActivate = activate_Pre_Data+","+activate_Post_Data
        $.ajax({
            type : "POST",
            url :pgContext+"/tt/tt/activatePrePostSurveys",
            data : {
                activatePrePostSurveys: surveyToActivate,
                classid: classID,
            },
            success : function(response) {
                if (response.includes("***")) {
                    $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
                    $('#errorMsgModelPopup').modal('show');
                }else{
                    $("#successMsgModelPopupForProblemSets").find("[class*='modal-body']").html( "<%= rb.getString("selected_surveys_active_for_class")%>" );
                    $('#successMsgModelPopupForProblemSets').modal('show');
                }
            }
        });

    });

    $('#inActiveProbSetTable input[type="checkbox"]').click(function () {
        if ($('#inActiveProbSetTable input[type="checkbox"]:checked').size()) {
            $('#acivateProblemSets').prop('disabled', false);
        } else {
            $('#acivateProblemSets').prop('disabled', true);
        }
    });
    
    $("#content_apply_handler").click(function () {
        $('#content_apply_handler').css('background-color', '');
        $('#content_apply_handler').css('color', '#dddddd');

        $("#content-conatiner").children().hide();
        $("#content_apply_handle").show();
    });

    $('a[rel=initialPopover]').popover({
        html: true,
        trigger: 'hover',
        container: 'body',
        title: '<%= rb.getString("what_is_mastery")%>',
        placement: 'right',
        content: function () {
            return "<ul><li><%= rb.getString("what_is_mastery_popover1")%></li><li><%= rb.getString("what_is_mastery_popover2")%></li></ul><%= rb.getString("what_is_mastery_popover3")%>";
        }
    });

    $('a[rel="popoverOrder"]').popover({
        html: false,
        trigger: 'hover',
        container: 'body',
        placement: 'top',
        content: function () {
            return '<%= rb.getString("order_problemset_will_be_shown")%>';
        }
    });
    $('a[rel="popoveractivatedProblems"]').popover({
        html: false,
        trigger: 'hover',
        container: 'body',
        placement: 'top',
        content: function () {
            return '<%= rb.getString("nbr_activated_problems_click_to_see")%>';
        }
    });

    $('a[rel="popoverproblemsetSummary"]').popover({
        html: false,
        trigger: 'hover',
        container: 'body',
        placement: 'top'
    });


}



function changeReportThreeHeaderAccordingToLanguage(){
	var languagePreference = window.navigator.language;
	var languageSet = "en";
	if (languagePreference.includes("en")) {
		languageSet = "en"
	} else if (languagePreference.includes("es")) {
		languageSet = "es"
	}
	if (languageSet == 'es') {
		var header = {'sid':  'Numero Identificador del alumno','sname': 'Nombre del  alumno','uname':  'Nombre de usuario','problems': '# de problemas intentados','effchart': 'Gráfico de esfuerzo','emochart': 'Gráfico de emociones'};
		return header;
	}else{
	 	var header = {'sid':  'Student ID','sname': 'Student Name','uname':  'Username','problems': 'No of problems attempted','effchart': 'Effort Chart','emochart': 'Emotion Chart'};
	 	return header;
	}
}



function changeReportFourHeaderAccordingToLanguage(){
	var languagePreference = window.navigator.language;
	var languageSet = "en";
	if (languagePreference.includes("en")) {
		languageSet = "en"
	} else if (languagePreference.includes("es")) {
		languageSet = "es"
	}
	if (languageSet == 'es') {
		var header = {'cclusters': 'Unidades Curriculares','problems': '# de problemas recibidos sobre este tema','fattempt':  '% resueltos en el primer intento','avgratio': '# de ayudas/pistas vistas'};
	 	return header;
	}else{
	 	var header = {'cclusters': 'Clusters in Class','problems': '# of problems of this kind encountered','fattempt':  '% solved in the first attempt','avgratio': 'Total hints viewed'};
	 	return header;
	}
}

function registerAllEvents(){
    $('#wrapper').toggleClass('toggled');
    $('#reorg_prob_sets_handler').css('background-color','#e6296f');
    $('#reorg_prob_sets_handler').css('color', '#ffffff');


   $('#perTopicReportLegendTable').DataTable({
       "bPaginate": false,
       "bFilter": false,
       "bLengthChange": false,
       "ordering": false
   });

    $('#perProblemReportLegendTable').DataTable({
        "bPaginate": false,
        "bFilter": false,
        "bLengthChange": false,
        "ordering": false
    });

    $('#perClusterLegendTable').DataTable({
        "bPaginate": false,
        "bFilter": false,
        "bLengthChange": false,
        "ordering": false
    });


    $('#masteryTrajecotoryLegend').DataTable({
        "columnDefs" : [{"title" : "<%= rb.getString("problem_id")%>", "targets": [0]},{"title" : "<%= rb.getString("problem_name")%>", "targets": [1]},{"title" : "<%= rb.getString("student_effort")%>", "targets": [2]}],
        destroy: true,
        "bFilter": false,
        "bLengthChange": false,
        "ordering": false
    });

if (languageSet == 'es') {
    perProblemSetReport = $('#perTopicStudentReport').DataTable({
        data: [],
        destroy: true,
        columns: [
            { title: "<%= rb.getString("student_id")%>" },
            { title: "<%= rb.getString("student_name")%>" },
            { title: "<%= rb.getString("username")%>" },
            { title: "<%= rb.getString("nbr_problems_attempted")%>" },
            { title: "<%= rb.getString("effchart")%>" },
        ],
        "bPaginate": false,
        "bFilter": false,
        "bLengthChange": false,
        rowReorder: false,
        "bSort" : false,
        "language": {
            "sProcessing":     "Procesando...",
            "sLengthMenu":     "Mostrar _MENU_ registros",
            "sZeroRecords":    "No se encontraron resultados",
            "sEmptyTable":     "Ningún dato disponible en esta tabla",
            "sInfo":           "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
            "sInfoEmpty":      "Mostrando registros del 0 al 0 de un total de 0 registros",
            "sInfoFiltered":   "(filtrado de un total de _MAX_ registros)",
            "sInfoPostFix":    "",
            "sSearch":         "Buscar:",
            "sUrl":            "",
            "sInfoThousands":  ",",
            "sLoadingRecords": "Cargando...",
            "oPaginate": {
                "sFirst":    "Primero",
                "sLast":     "Último",
                "sNext":     "Siguiente",
                "sPrevious": "Anterior"
            },
            "oAria": {
                "sSortAscending":  ": Activar para ordenar la columna de manera ascendente",
                "sSortDescending": ": Activar para ordenar la columna de manera descendente"
            }
        }

    } );
}
else {
    perProblemSetReport = $('#perTopicStudentReport').DataTable({
        data: [],
        destroy: true,
        columns: [
            { title: "<%= rb.getString("student_id")%>" },
            { title: "<%= rb.getString("student_name")%>" },
            { title: "<%= rb.getString("username")%>" },
            { title: "<%= rb.getString("nbr_problems_attempted")%>" },
            { title: "<%= rb.getString("effchart")%>" },
        ],
        "bPaginate": false,
        "bFilter": false,
        "bLengthChange": false,
        rowReorder: false,
        "bSort" : false,

    } );
	
}
    var classListSize = $('#classListSize').val();
    if(classListSize != 0){

    	apply_content_table = $('#apply_content_table').DataTable({
        "bPaginate": false,
        "bFilter": false,
        "bLengthChange": false,
        rowReorder: true,
        "columnDefs": [
            {
                "targets": [ 0 ],
                "width": "40%",
                orderable: false
            },
            {
                "targets": [ 1 ],
                "width": "40%",
                "orderable": false,
            },
            {
                "targets": [ -1 ],
                "orderable": false,
                "width": "20%",
                'className': 'dt-body-center',
                'render': function (data, type, full, meta){
                    return '<input type="checkbox">';
                }
            }
        ]

    });
	
 }
    
    if (languageSet == 'es') {
    perProblemReportTable = $('#perProblemReport').DataTable({
        data: [],
        destroy: true,
        columns: [
            { title: "<%= rb.getString("problem_id")%>", data : "problemId" },
            { title: "<%= rb.getString("problem_name")%>", data : "problemName" },
            { title: "<%= rb.getString("nbr_students_seen_problem")%>", data : "noStudentsSeenProblem" },
            { title: "<%= rb.getString("nbr_students_solved_problem")%>", data : "getPercStudentsSolvedEventually" },
            { title: "<%= rb.getString("nbr_students_solved_problem_first")%>", data : "getGetPercStudentsSolvedFirstTry" },
            { title: "<%= rb.getString("nbr_students_solved_problem_second")%>", data : "getGetPercStudentsSolvedSecondTry" },
            { title: "<%= rb.getString("nbr_students_repeated_problem")%>", data : "percStudentsRepeated" },
            { title: "<%= rb.getString("nbr_students_skipped_problem")%>", data : "percStudentsSkipped" },
            { title: "<%= rb.getString("nbr_students_gave_up")%>", data : "percStudentsGaveUp" },
            { title: "<%= rb.getString("most_frequent_incorrect_response")%>", data : "mostIncorrectResponse" }
        ],
        "bPaginate": false,
        "language": {
            "sProcessing":     "Procesando...",
            "sLengthMenu":     "Mostrar _MENU_ registros",
            "sZeroRecords":    "No se encontraron resultados",
            "sEmptyTable":     "Ningún dato disponible en esta tabla",
            "sInfo":           "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
            "sInfoEmpty":      "Mostrando registros del 0 al 0 de un total de 0 registros",
            "sInfoFiltered":   "(filtrado de un total de _MAX_ registros)",
            "sInfoPostFix":    "",
            "sSearch":         "Buscar:",
            "sUrl":            "",
            "sInfoThousands":  ",",
            "sLoadingRecords": "Cargando...",
            "oPaginate": {
                "sFirst":    "Primero",
                "sLast":     "Último",
                "sNext":     "Siguiente",
                "sPrevious": "Anterior"
            },
            "oAria": {
                "sSortAscending":  ": Activar para ordenar la columna de manera ascendente",
                "sSortDescending": ": Activar para ordenar la columna de manera descendente"
            }
        },
        "bFilter": false,
        "bLengthChange": false,
        rowReorder: false

    } );
    }
    else { 
        perProblemReportTable = $('#perProblemReport').DataTable({
            data: [],
            destroy: true,
            columns: [
                { title: "<%= rb.getString("problem_id")%>", data : "problemId" },
                { title: "<%= rb.getString("problem_name")%>", data : "problemName" },
                { title: "<%= rb.getString("nbr_students_seen_problem")%>", data : "noStudentsSeenProblem" },
                { title: "<%= rb.getString("nbr_students_solved_problem")%>", data : "getPercStudentsSolvedEventually" },
                { title: "<%= rb.getString("nbr_students_solved_problem_first")%>", data : "getGetPercStudentsSolvedFirstTry" },
                { title: "<%= rb.getString("nbr_students_solved_problem_second")%>", data : "getGetPercStudentsSolvedSecondTry" },
                { title: "<%= rb.getString("nbr_students_repeated_problem")%>", data : "percStudentsRepeated" },
                { title: "<%= rb.getString("nbr_students_skipped_problem")%>", data : "percStudentsSkipped" },
                { title: "<%= rb.getString("nbr_students_gave_up")%>", data : "percStudentsGaveUp" },
                { title: "<%= rb.getString("most_frequent_incorrect_response")%>", data : "mostIncorrectResponse" }
            ],
            "bPaginate": false,
            "bFilter": false,
            "bLengthChange": false,
            rowReorder: false

        } );
    	
    }
    var cc_headers = changeReportFourHeaderAccordingToLanguage();

    if (languageSet == 'es') {
    perClusterReportTable = $('#perClusterReport').DataTable({
        data: [],
        destroy: true,
        columns: [
            { title: cc_headers['cclusters'], data : "clusterName" },
            { title: cc_headers['problems'], data : "noOfProblemsInCluster" },
            { title: cc_headers['fattempt'], data : "noOfProblemsonFirstAttempt" },
            { title: cc_headers['avgratio'], data : "totalHintsViewedPerCluster" }
        ],
        "bPaginate": false,
        "language": {
            "sProcessing":     "Procesando...",
            "sLengthMenu":     "Mostrar _MENU_ registros",
            "sZeroRecords":    "No se encontraron resultados",
            "sEmptyTable":     "Ningún dato disponible en esta tabla",
            "sInfo":           "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
            "sInfoEmpty":      "Mostrando registros del 0 al 0 de un total de 0 registros",
            "sInfoFiltered":   "(filtrado de un total de _MAX_ registros)",
            "sInfoPostFix":    "",
            "sSearch":         "Buscar:",
            "sUrl":            "",
            "sInfoThousands":  ",",
            "sLoadingRecords": "Cargando...",
            "oPaginate": {
                "sFirst":    "Primero",
                "sLast":     "Último",
                "sNext":     "Siguiente",
                "sPrevious": "Anterior"
            },
            "oAria": {
                "sSortAscending":  ": Activar para ordenar la columna de manera ascendente",
                "sSortDescending": ": Activar para ordenar la columna de manera descendente"
            }
        },
        "bFilter": false,
        "bLengthChange": false,
        rowReorder: false

    } );
    }
    else { 
        perClusterReportTable = $('#perClusterReport').DataTable({
            data: [],
            destroy: true,
            columns: [
                { title: cc_headers['cclusters'], data : "clusterName" },
                { title: cc_headers['problems'], data : "noOfProblemsInCluster" },
                { title: cc_headers['fattempt'], data : "noOfProblemsonFirstAttempt" },
                { title: cc_headers['avgratio'], data : "totalHintsViewedPerCluster" }
            ],
            "bPaginate": false,
            "bFilter": false,
            "bLengthChange": false,
            rowReorder: false

        } );
    	
    }
    
    
    var headers = changeReportThreeHeaderAccordingToLanguage();

    
    if (languageSet == 'es') {
    
    perStudentReport  =  $('#perStudentReport').DataTable({
        data: [],
        destroy: true,
        columns: [
            { title: headers['sid'] },
            { title: headers['sname']  },
            { title: headers['uname']  },
            { title: headers['problems']  },
            { title: headers['effchart']  },
            { title: headers['emochart']  },
        ],
        "bPaginate": false,
        "bFilter": false,
        "bLengthChange": false,
        rowReorder: false,                
        "language": {
            "sProcessing":     "Procesando...",
            "sLengthMenu":     "Mostrar _MENU_ registros",
            "sZeroRecords":    "No se encontraron resultados",
            "sEmptyTable":     "Ningún dato disponible en esta tabla",
            "sInfo":           "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
            "sInfoEmpty":      "Mostrando registros del 0 al 0 de un total de 0 registros",
            "sInfoFiltered":   "(filtrado de un total de _MAX_ registros)",
            "sInfoPostFix":    "",
            "sSearch":         "Buscar:",
            "sUrl":            "",
            "sInfoThousands":  ",",
            "sLoadingRecords": "Cargando...",
            "oPaginate": {
                "sFirst":    "Primero",
                "sLast":     "Último",
                "sNext":     "Siguiente",
                "sPrevious": "Anterior"
            },
            "oAria": {
                "sSortAscending":  ": Activar para ordenar la columna de manera ascendente",
                "sSortDescending": ": Activar para ordenar la columna de manera descendente"
            }
        },

        "scrollX": true,
        "bSort" : false,
        "columnDefs": [
            {
                "width": "5%",
                "targets": [ 0 ],
                "visible": false

            },{
                "width": "5%",
                "targets": [ 1 ],
                "visible": true

            },{
                "width": "5%",
                "targets": [ 2 ],
                "visible": true

            },
            {
                "width": "5%",
                "targets": [ 3 ],
                "visible": true,
                'className': 'dt-body-center',
                'render': function ( data, type, row ) {
                    return '<label>'+data+'&nbsp&nbsp</lable><a  class="viewEachStudentDetail" aria-expanded="true" aria-controls="collapseOne"><i class="glyphicon glyphicon-menu-down"></i></a>';
                }

            },
            {
                "targets": [ 4 ],
                "width": "10%",
                'className': 'dt-body-center',
                'render': function ( data, type, row ) {
                    var effortChartId = "effortChart"+row[0];
                    var containerChart = "containerChart"+row[0];
                    var legendChart = "legendChart"+row[0];
                    var dataContent = "<div id="+containerChart+" style='width:900px;height:500px;display:none'><div class='panel panel-default'><div class='panel-heading'>"+headers['effchart']+"</div><div class='panel-body'><canvas width='800' height='150' id="+effortChartId+"></canvas></div><div class='panel-body' id='"+legendChart+"'></div></div></div>";
                    return "<i id='iconID"+row[0]+"' style='cursor:pointer;' class='fa fa-th' aria-hidden='true' onclick='loadEffortMap("+row[0]+",true);'></i>"+dataContent;
                }
            }, {
                "targets": [ 5 ],
                "width": "70%",
                'className': 'dt-body-center',
                'render': function ( data, type, row ) {
                    var studentEmotions = emotionMap[row[0]];
                    var studentComments = commentsMap[row[0]];
                    var emotionContainerChart = "emotionContainerChart"+row[0];
                    var containerConvas = "<div id="+emotionContainerChart+" style='width:100%;display:none'>";
                    if(jQuery.isEmptyObject(studentEmotions)) {
                        var noEmotionReported = "<span><label><%= rb.getString("no_emotions_reported")%> </label></span>";
                        var containerChartSelector = "#emotionContainerChart" +row[0];
                         containerConvas += noEmotionReported;
                    }else {
                        var i = 0;
                        var divBlockOne = "<div id='bloc1' style='margin: 1%; float: left;'>";
                        var divBlockTwo = "<div id='bloc2' style='margin: 1%; float: left;'>";
                        Object.keys(studentEmotions).forEach(function (key) {
                            var eachEmotionComment = studentComments[key];
                            var commentsTable = "";
                            if (!jQuery.isEmptyObject(eachEmotionComment)) {
                                commentsTable = "<table style='width:80%;' class='table table-striped table-bordered hover'><thead><tr><%= rb.getString("comments")%></tr></thead><tbody>"
                                var emotionComments = "";
                                eachEmotionComment.forEach(function (comments) {
                                    if (comments != "")
                                        emotionComments += "<tr><td>" + comments + "</td></tr>";
                                });
                                emotionComments += "</tbody></table>"
                                commentsTable += emotionComments;
                            }

                            var emo = "undefined";
                                          
                            if (key == "Frustration") {
                            	emo = "Frustración";
                            }
                            if (key == "Confidence") {
                            	emo = "Confianza";
                            }
                            if (key == "Excitement") {
                            	emo = "Estusiasmo";
                            }
                            if (key == "Interest") {
                            	emo = "Inters";
                            }
                            
                            var canvasTags = "<div class='panel panel-default'><div class='panel-heading'>" + emo + "</div><div class='panel-body'><canvas width='300' height='100' id='" + row[0] + key + "'></canvas>" + commentsTable+"</div></div>"

                            if (i == 0 || i == 1)
                                divBlockOne += canvasTags;
                            if (i == 2 || i == 3)
                                divBlockTwo += canvasTags;
                            if (i == 3) {
                                divBlockOne += "</div>";
                                divBlockTwo += "</div>";
                            }
                            i++;
                        });
                        containerConvas = containerConvas + divBlockOne + divBlockTwo + "</div>"
                    }
                    return "<i id='emotioniconID"+row[0]+"' style='cursor:pointer;' class='fa fa-heart' aria-hidden='true' onclick='loadEmotionMap("+row[0]+");'></i>"+containerConvas;
                }
            },
                
    	]
    }    
    );
    }
    else {
        perStudentReport  =  $('#perStudentReport').DataTable({
            data: [],
            destroy: true,
            columns: [
                { title: headers['sid'] },
                { title: headers['sname']  },
                { title: headers['uname']  },
                { title: headers['problems']  },
                { title: headers['effchart']  },
                { title: headers['emochart']  },
            ],
            "bPaginate": false,
            "bFilter": false,
            "bLengthChange": false,
            rowReorder: false,                
            "scrollX": true,
            "bSort" : false,
            "columnDefs": [
                {
                    "width": "5%",
                    "targets": [ 0 ],
                    "visible": false

                },{
                    "width": "5%",
                    "targets": [ 1 ],
                    "visible": true

                },{
                    "width": "5%",
                    "targets": [ 2 ],
                    "visible": true

                },
                {
                    "width": "5%",
                    "targets": [ 3 ],
                    "visible": true,
                    'className': 'dt-body-center',
                    'render': function ( data, type, row ) {
                        return '<label>'+data+'&nbsp&nbsp</lable><a  class="viewEachStudentDetail" aria-expanded="true" aria-controls="collapseOne"><i class="glyphicon glyphicon-menu-down"></i></a>';
                    }

                },
                {
                    "targets": [ 4 ],
                    "width": "10%",
                    'className': 'dt-body-center',
                    'render': function ( data, type, row ) {
                        var effortChartId = "effortChart"+row[0];
                        var containerChart = "containerChart"+row[0];
                        var legendChart = "legendChart"+row[0];
                        var dataContent = "<div id="+containerChart+" style='width:900px;height:500px;display:none'><div class='panel panel-default'><div class='panel-heading'>"+headers['effchart']+"</div><div class='panel-body'><canvas width='800' height='150' id="+effortChartId+"></canvas></div><div class='panel-body' id='"+legendChart+"'></div></div></div>";
                        return "<i id='iconID"+row[0]+"' style='cursor:pointer;' class='fa fa-th' aria-hidden='true' onclick='loadEffortMap("+row[0]+",true);'></i>"+dataContent;
                    }
                }, {
                    "targets": [ 5 ],
                    "width": "70%",
                    'className': 'dt-body-center',
                    'render': function ( data, type, row ) {
                        var studentEmotions = emotionMap[row[0]];
                        var studentComments = commentsMap[row[0]];
                        var emotionContainerChart = "emotionContainerChart"+row[0];
                        var containerConvas = "<div id="+emotionContainerChart+" style='width:100%;display:none'>";
                        if(jQuery.isEmptyObject(studentEmotions)) {
                            var noEmotionReported = "<span><label><%= rb.getString("no_emotions_reported")%> </label></span>";
                            var containerChartSelector = "#emotionContainerChart" +row[0];
                             containerConvas += noEmotionReported;
                        }else {
                            var i = 0;
                            var divBlockOne = "<div id='bloc1' style='margin: 1%; float: left;'>";
                            var divBlockTwo = "<div id='bloc2' style='margin: 1%; float: left;'>";
                            Object.keys(studentEmotions).forEach(function (key) {
                                var eachEmotionComment = studentComments[key];
                                var commentsTable = "";
                                if (!jQuery.isEmptyObject(eachEmotionComment)) {
                                    commentsTable = "<table style='width:80%;' class='table table-striped table-bordered hover'><thead><tr><%= rb.getString("comments")%></tr></thead><tbody>"
                                    var emotionComments = "";
                                    eachEmotionComment.forEach(function (comments) {
                                        if (comments != "")
                                            emotionComments += "<tr><td>" + comments + "</td></tr>";
                                    });
                                    emotionComments += "</tbody></table>"
                                    commentsTable += emotionComments;
                                }
                                var canvasTags = "<div class='panel panel-default'><div class='panel-heading'>" + key + "</div><div class='panel-body'><canvas width='300' height='100' id='" + row[0] + key + "'></canvas>" + commentsTable+"</div></div>"

                                if (i == 0 || i == 1)
                                    divBlockOne += canvasTags;
                                if (i == 2 || i == 3)
                                    divBlockTwo += canvasTags;
                                if (i == 3) {
                                    divBlockOne += "</div>";
                                    divBlockTwo += "</div>";
                                }
                                i++;
                            });
                            containerConvas = containerConvas + divBlockOne + divBlockTwo + "</div>"
                        }
                        return "<i id='emotioniconID"+row[0]+"' style='cursor:pointer;' class='fa fa-heart' aria-hidden='true' onclick='loadEmotionMap("+row[0]+");'></i>"+containerConvas;
                    }
                },
                    
        	]
        }    
        );    	
    }
    
	var activeProblemSetsize = $('#activeproblemSetSize').val();
    if(activeProblemSetsize != 0){
    	
        if (languageSet == 'es') {
    	activetable = $('#activateProbSetTable').DataTable({
        "bPaginate": false,
        "language": {
            "sProcessing":     "Procesando...",
            "sLengthMenu":     "Mostrar _MENU_ registros",
            "sZeroRecords":    "No se encontraron resultados",
            "sEmptyTable":     "Ningún dato disponible en esta tabla",
            "sInfo":           "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
            "sInfoEmpty":      "Mostrando registros del 0 al 0 de un total de 0 registros",
            "sInfoFiltered":   "(filtrado de un total de _MAX_ registros)",
            "sInfoPostFix":    "",
            "sSearch":         "Buscar:",
            "sUrl":            "",
            "sInfoThousands":  ",",
            "sLoadingRecords": "Cargando...",
            "oPaginate": {
                "sFirst":    "Primero",
                "sLast":     "Último",
                "sNext":     "Siguiente",
                "sPrevious": "Anterior"
            },
            "oAria": {
                "sSortAscending":  ": Activar para ordenar la columna de manera ascendente",
                "sSortDescending": ": Activar para ordenar la columna de manera descendente"
            }
        },
        "bFilter": false,
        "bLengthChange": false,
        rowReorder: true,
        "columnDefs": [
            {
                "targets": [ 0 ],
                "width": "10%",
                'className': 'reorder',
                orderable: false
            },
            {
                "targets": [ 1 ],
                "width": "30%",
                "orderable": false,
            },
            {
                "targets": [ 2 ],
                orderable: false,
                "width": "10%",
            },
            {
                "width": "30%",
                "targets": [ 3 ],
                "visible": false,
                "orderable": false,

            },
            {
                "targets": [ -1 ],
                "orderable": false,
                "width": "20%",
                'className': 'dt-body-center',
                'render': function (data, type, full, meta){
                    return '<input type="checkbox">';
                }
            },
        ]

    });
    }
    else {
    	activetable = $('#activateProbSetTable').DataTable({
            "bPaginate": false,
            "bFilter": false,
            "bLengthChange": false,
            rowReorder: true,
            "columnDefs": [
                {
                    "targets": [ 0 ],
                    "width": "10%",
                    'className': 'reorder',
                    orderable: false
                },
                {
                    "targets": [ 1 ],
                    "width": "30%",
                    "orderable": false,
                },
                {
                    "targets": [ 2 ],
                    orderable: false,
                    "width": "10%",
                },
                {
                    "width": "30%",
                    "targets": [ 3 ],
                    "visible": false,
                    "orderable": false,

                },
                {
                    "targets": [ -1 ],
                    "orderable": false,
                    "width": "20%",
                    'className': 'dt-body-center',
                    'render': function (data, type, full, meta){
                        return '<input type="checkbox">';
                    }
                },
            ]

        });        	
    }
	
	 $(".active").click(function () {
        $(this).children(':first').toggleClass('rotate-icon');
        var tr = $(this).closest('tr');
        var row = activetable.row( tr );

        if ( row.child.isShown() ) {
            row.child.hide();
        }else{
            var rowID = '#'+row.data()[0];
            $.ajax({
                type : "POST",
                url :pgContext+"/tt/tt/getProblemForProblemSets",
                data : {
                    problemID: row.data()[3],
                    classid: classID
                },
                success : function(response) {
                    if (response.includes("***")) {
                        $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
                        $('#errorMsgModelPopup').modal('show');
                    }else {
                        var child = problemDetails(row.data(), response);
                        row.child(child).show();
                        $('a[rel=popoverPerProblem]').popover({
                            html: true,
                            trigger: 'hover',
                            placement: 'top',
                            container: 'body',
                            content: function () {
                                return '<img src="' + $(this).data('img') + '" />';
                            }
                        });
                        $(rowID).toggleClass('zoomIn zoomOut');
                    }
                }
            });

        }
    });
	
	activetable.on( 'row-reorder', function ( e, diff, edit ) {
        activetable.$('input').removeAttr( 'checked' );
        var result = [];
        for ( var i=0; i< diff.length ; i++ ) {
            var rowData = activetable.row( diff[i].node ).data();
            result[i] = rowData[3]+'~~'+ diff[i].newData+'~~'+diff[i].oldData;
        }
        $.ajax({
            type : "POST",
            url :pgContext+"/tt/tt/reOrderProblemSets",
            data : {
                problemSets: result,
                classid: classID
            },
            success : function(response) {
                if (response.includes("***")) {
                    $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
                    $('#errorMsgModelPopup').modal('show');
                }
            }
        });
    } );

	}

    var inactiveProblemSetsize = $('#inactiveproblemSetSize').val();
    if(inactiveProblemSetsize != 0){
    inactivetable = $('#inActiveProbSetTable').DataTable({
        "bPaginate": false,
        "bFilter": false,
        "bSort" : false,
        "bLengthChange": false,
        rowReorder: false,
        "bSort" : false,
        "columnDefs": [
            {
                "targets": [ 0 ],
                "width": "10%",
                orderable: true
            },
            {


                "targets": [ 1 ],
                "width": "30%"
            },
            {
                "targets": [ 2 ],
                orderable: false,
                "width": "10%",
                'render': function (data, type, full, meta){
					var labelHtml = '<label style="width: 50%;">'+data+'</label>';
					if(data != 0)
						labelHtml+='<a  class="passive" aria-expanded="true" aria-controls="collapseOne"><i class="glyphicon glyphicon-menu-down"></i></a>';
					
					return labelHtml;
				}
            },
            {
                "width": "30%",
                "targets": [ 3 ],
                "visible": false

            },
            {
                "targets": [ -1 ],
                "width": "20%",
                'className': 'dt-body-center',
                'render': function (data, type, full, meta){
                	if(full[2] != 0)
                    return '<input type="checkbox">';
                	else
                	return '<input type="checkbox" disabled>';	
                }
            },
        ]

    });
	
	
    $(".passive").click(function () {
        $(this).children(':first').toggleClass('rotate-icon');
        var tr = $(this).closest('tr');
        var row = inactivetable.row( tr );

        if ( row.child.isShown() ) {
            row.child.hide();
        }else{
            var rowID = '#'+row.data()[0];
            $.ajax({
                type : "POST",
                url :pgContext+"/tt/tt/getProblemForProblemSets",
                data : {
                    problemID: row.data()[3],
                    classid: classID
                },
                success : function(response) {
                    if (response.includes("***")) {
                        $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
                        $('#errorMsgModelPopup').modal('show');
                    }else {
                        var child = problemDetails(row.data(), response);
                        row.child(child).show();
                        $('a[rel=popoverPerProblem]').popover({
                            html: true,
                            trigger: 'hover',
                            placement: 'top',
                            container: 'body',
                            content: function () {
                                return '<img src="' + $(this).data('img') + '" />';
                            }
                        });
                        $(rowID).toggleClass('zoomIn zoomOut');
                    }
                }
            });

        }
    });
	
    }

    var studentRosterSize = $('#studentRosterSize').val();
    if(studentRosterSize != 0) {
        if (languageSet == 'es') {
            studentRosterTable = $('#student_roster').DataTable({
                "bPaginate": false,
                "bFilter": false,
                "bLengthChange": false,
                "bSort": false,            
                "language": {
                    "sProcessing":     "Procesando...",
                    "sLengthMenu":     "Mostrar _MENU_ registros",
                    "sZeroRecords":    "No se encontraron resultados",
                    "sEmptyTable":     "Ningún dato disponible en esta tabla",
                    "sInfo":           "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
                    "sInfoEmpty":      "Mostrando registros del 0 al 0 de un total de 0 registros",
                    "sInfoFiltered":   "(filtrado de un total de _MAX_ registros)",
                    "sInfoPostFix":    "",
                    "sSearch":         "Buscar:",
                    "sUrl":            "",
                    "sInfoThousands":  ",",
                    "sLoadingRecords": "Cargando...",
                    "oPaginate": {
                        "sFirst":    "Primero",
                        "sLast":     "Último",
                        "sNext":     "Siguiente",
                        "sPrevious": "Anterior"
                    },
                    "oAria": {
                        "sSortAscending":  ": Activar para ordenar la columna de manera ascendente",
                        "sSortDescending": ": Activar para ordenar la columna de manera descendente"
                    }
                }

            });
        } 
        else {
        studentRosterTable = $('#student_roster').DataTable({
            "bPaginate": false,
            "bFilter": false,
            "bLengthChange": false,
            "bSort": false            

        });
        }
    }

    $("#deacivateProblemSets").click(function () {
        var rows = $("#activateProbSetTable").dataTable().fnGetNodes();
        var rowsArray = [];
        var activateData = [];
        var i = 0;

        if (rows.length == 1) {
            $("#errorMsgModelPopup").find("[class*='modal-body']").html("<%= rb.getString("every_class_must_have_active_problem")%>");
            $('#errorMsgModelPopup').modal('show');
        }
        $("input:checkbox:not(:checked)",rows).each(function(){
            rowsArray[i] = $(this).closest('tr');
            i++;
        });
        for(var j=0; j < rowsArray.length; j++) {
            activateData[j] = $("#activateProbSetTable").DataTable().row(rowsArray [j]).data()[3];
        }
        $.ajax({
            type : "POST",
            url :pgContext+"/tt/tt/configureProblemSets",
            data : {
                activateData: activateData,
                classid: classID,
                activateFlag: 'deactivate'
            },
            success : function(response) {
                if (response.includes("***")) {
                    $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
                    $('#errorMsgModelPopup').modal('show');
                }else{
                    $("#successMsgModelPopupForProblemSets").find("[class*='modal-body']").html( "<%= rb.getString("selected_problemsets_are_deactivated")%>" );
                    $('#successMsgModelPopupForProblemSets').modal('show');
                }
            }
        });

    });

    $('#createMoreStudentId').click(function () {

        var dataForm = $("#create_Student_id").serializeArray();
        var values=[];
        $.each(dataForm, function(i, field){
            values[i] = field.value;
        });

        $.ajax({
            type: "POST",
            url: pgContext + "/tt/tt/createMoreStudentIds",
            data: {
                formData: values,
                lang: loc
            },
            success: function (data) {
                if (data.includes("***")) {
                    $("#errorMsgModelPopup").find("[class*='modal-body']").html( data );
                    $('#errorMsgModelPopup').modal('show');
                }else{
                    $("#successMsgModelPopup").find("[class*='modal-body']").html( "<%= rb.getString("user_creation_successful")%> " );
                    $('#successMsgModelPopup').modal('show');
                }

            }
        });

    });


    $("#acivateProblemSets").click(function () {
        var rows = $("#inActiveProbSetTable").dataTable().fnGetNodes();
        var rowsArray = [];
        var activateData = [];
        var i = 0;
        $("input:checked", rows).each(function(){
            rowsArray[i] = $(this).closest('tr');
            i++;
        });
        for(var j=0; j < rowsArray.length; j++)
            activateData[j]  = $("#inActiveProbSetTable").DataTable().row( rowsArray [j] ).data()[3];

        $.ajax({
            type : "POST",
            url :pgContext+"/tt/tt/configureProblemSets",
            data : {
                activateData: activateData,
                classid: classID,
                activateFlag: 'activate'
            },
            success : function(response) {
                if (response.includes("***")) {
                    $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
                    $('#errorMsgModelPopup').modal('show');
                }else{
                    $("#successMsgModelPopupForProblemSets").find("[class*='modal-body']").html( "<%= rb.getString("selected_problemsets_are_activated")%>" );
                    $('#successMsgModelPopupForProblemSets').modal('show');
                }
            }
        });

    });
    
    
    $("#apply_content").click(function () {
    	$("#loading_spinner").show();
        var rows = $("#apply_content_table").dataTable().fnGetNodes();
        var rowsArray = [];
        var activateData = [];
        var i = 0;
        $("input:checked", rows).each(function(){
            rowsArray[i] = $(this).closest('tr');
            i++;
        });
        for(var j=0; j < rowsArray.length; j++)
            activateData[j]  = $("#apply_content_table").DataTable().row( rowsArray [j] ).data()[0];

        $.ajax({
            type : "POST",
            url :pgContext+"/tt/tt/continousContentApply",
            data : {
                classesToApply: activateData,
                classid: classID,
                teacherId: teacherID
            },
            success : function(response) {
            	$("#loading_spinner").hide();
                if (response.includes("***")) {
                    $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
                    $('#errorMsgModelPopup').modal('show');
                }else{
                    $("#successMsgModelPopupForProblemSets").find("[class*='modal-body']").html( "<%= rb.getString("current_class_content_applied")%>" );
                    $('#successMsgModelPopupForProblemSets').modal('show');
                }
            }
        });

    });


    $("#successMsgModelPopupForProblemSets").find("[class*='btn btn-default']").click(function () {
        var newlocation = pgContext+'/tt/tt/viewClassDetails?teacherId='+teacherID+'&classId='+classID;
        $(location).attr('href', newlocation);
    });
    $("#successMsgModelPopupForProblemSets").find("[class*='close']").click(function () {
        var newlocation = pgContext+'/tt/tt/viewClassDetails?teacherId='+teacherID+'&classId='+classID;
        $(location).attr('href', newlocation);
    });
	
    var myLineChart;
    $('body').on('click', 'div.getMastery-trajectory-for-problemset', function () {

        var topicId = $(this).find("span").text();
        var td = $(this).closest('td');
        var bgcolor = "#BDB7B5"

        if(td.attr('class')){
            if(td.attr('class') == 'span-danger-layer-one')
                bgcolor = '#FF4766'
            else if(td.attr('class') == 'span-warning-layer-one')
                bgcolor = '#FFB647'
            else if(td.attr('class') == 'span-info-layer-one')
                bgcolor = '#33b5e5'
            else if(td.attr('class') == 'span-sucess-layer-one')
                bgcolor = '#00C851'
            else if(td.attr('class') == 'span-danger-layer-two')
                bgcolor = '#FF99AA'
            else if(td.attr('class') == 'span-warning-layer-two')
                bgcolor = '#FFD699'
            else if(td.attr('class') == 'span-info-layer-two')
                bgcolor = '#8ed6f0'
            else if(td.attr('class') == 'span-sucess-layer-two')
                bgcolor = '#4dff94'

        }
        var tr = $(this).closest('tr');
        var row = perProblemSetReport.row(tr);
        var topicId = $(this).find("span").text();
        var studentId = row.data()['studentId'];

        $.ajax({
            type: "POST",
            url: pgContext + "/tt/tt/getMasterProjectionsForCurrentTopic",
            data: {
                classId: classID,
                topicID: topicId,
                studentId: studentId
            },
            success: function (response) {
                var masteryProjectionsForThisTopic = $.parseJSON(response);
                var problemsMap = {};
                var chartLabel = [];
                var chartData = [];
                var perProblemSetLevelOneFullTemp = [];
                if(myLineChart) {
                    myLineChart.destroy();
                }
                masteryProjectionsForThisTopic.forEach(function (e) {
                    var perProblemSetLevelOneTemp = {};
                    perProblemSetLevelOneTemp['problemId'] = e[0];
                    perProblemSetLevelOneTemp['problemName'] = e[1];
                    perProblemSetLevelOneTemp['studentEffort'] =e[7];

                    problemsMap[e[1]] =  e[4];
                    chartLabel.push(e[0]);
                    chartData.push(e[5]);

                    perProblemSetLevelOneFullTemp.push(perProblemSetLevelOneTemp);
                });


                var columDvalues = [{data : "problemId"},{data : "problemName"},{data : "studentEffort"}]
                var columNvalues = [{"title" : "<%= rb.getString("problem_id")%>", "targets": [0]},{"title" : "<%= rb.getString("problem_name")%>", "targets": [1], "render": function ( data, type, row ) {
                    var imageURL = problem_imageURL+row['problemId']+'.jpg';
                    return  "<a style='cursor:pointer' rel='popover' data-img='" + imageURL + "'>" + data + "</a>";
                }
                },{"title" : "<%= rb.getString("student_effort")%>", "targets": [2], "render": function ( data, type, row ) {
                    return  "<a style='cursor:pointer' rel='popoverLabel' data-content='"+effortLabelMap[data]+"'>" + data + "</a>";
                }
                }];

                var  masteryTrajecotoryLegend = $('#masteryTrajecotoryLegend').DataTable({
                    data: perProblemSetLevelOneFullTemp,
                    destroy: true,
                    "scrollCollapse": true,
                    "bInfo": false,
                    "columns" : columDvalues,
                    "columnDefs" : columNvalues,
                    "bFilter": false,
                    "bLengthChange": false,
                    rowReorder: false,
                    "bSort" : false ,
                    "drawCallback": function() {
                        $('a[rel=popover]').popover({
                            html: true,
                            trigger: 'hover',
                            placement: 'right',
                            content: function () {
                                return '<img src="' + $(this).data('img') + '" />';
                            }
                        });
                        $('a[rel=popoverLabel]').popover({
                            html: false,
                            trigger: 'hover',
                            placement: 'right',
                        });
                    }
                });

                myLineChart = new Chart($("#masteryTrajectoryReportCanvas"), {
                    type: 'line',
                    data: {
                        labels: chartLabel,
                        datasets: [{
                            label: '<%= rb.getString("mastery_recorded")%>',
                            data: chartData,
                            backgroundColor: bgcolor
                        }]
                    }, options: {
                        scales: {
                            xAxes: [{
                                scaleLabel: {
                                    display: true,
                                    labelString: '<%= rb.getString("problems_seen_in_order")%>'
                                }
                            }],
                            yAxes: [{
                                display: true,
                                ticks: {
                                    suggestedMin: 0.00,
                                    max: 1.00
                                }
                            }]
                        } ,
                        legend: {
                            display: false,
                            position: 'bottom',
                        }
                    }
                });
                $('#masteryTrajectoryReport').modal('show');

            }
        });
    });


    $('body').on('click', 'a.viewEachStudentDetail', function () {
        $(this).children(':first').toggleClass('rotate-icon');
        var tr = $(this).closest('tr');
        var row = perStudentReport.row(tr);
        var rowID = row.data()[0];
        var containerChartSelector = "#containerChart" + rowID;
        var legendChart = "#legendChart" + rowID;
        $(containerChartSelector).hide();
        $("#iconID" + rowID).show();
        $(legendChart).empty();

        if (row.child.isShown()) {
            row.child.hide();
        } else {
        	var tabPanel =  '<ul class="nav nav-tabs"> <li class="active"><a  href="#1" data-toggle="tab"><%= rb.getString("problems_solved_today")%></a></li> <li><a href="#2" data-toggle="tab"><%= rb.getString("problems_solved_most_recently")%></a> </li> <li><a href="#3" data-toggle="tab"><%= rb.getString("all_solved_problems")%></a> </li> </ul>';	
            var studentDataList = eachStudentData[rowID];
            var outputStudentDataList = Object.keys(studentDataList).map(function(key) {return studentDataList[key];});
            var outputStudentDataTodayList =[];
            var outputStudentDataYesterdayList =[];
            var firstHeader;
            var secondHeader;
            var thirdHeader;

          
            outputStudentDataList.sort(function(a,b) {
                if(a[10] == 'Problem was not completed')
                    return new Date('1900-01-01 00:00:01.0').getTime() - new Date('1900-01-01 00:00:00.0').getTime();
                if( b[10] == 'Problem was not completed' )
                    return new Date('1900-01-01 00:00:00.0').getTime() - new Date('1900-01-01 00:00:01.0').getTime();
                else
                    return new Date(b[10]).getTime() - new Date(a[10]).getTime();
            });
            for ( var i=0; i< 3 ; i++ ) {
                if(i == 0){
                	
                    var currentDate = new Date();
                    var yr = currentDate.getFullYear();
                    var mon = currentDate.getMonth() + 1;
                    var day = currentDate.getDate();
                    var today = yr + "-" + mon + "-" + day + " " + "00:00:00.0";       			
        			var midnight = new Date(today);
        			var cmpDate = midnight.getTime();

                    outputStudentDataList.forEach(function(e){
                    var tempDateHolder  = new Date(e[10]).getTime();
                    if (tempDateHolder > cmpDate){
                        outputStudentDataTodayList.push(e);                            
   	                }
                    });
                  if(jQuery.isEmptyObject(outputStudentDataTodayList)) {
                      secondHeader = '<div id=' + "panel1" + rowID + ' class="panel-body animated zoomOut"> <%= rb.getString("no_problems_done_today")%></div>'
                  } else {
                      secondHeader = '<div id='+"panel1"+rowID+' class="panel-body animated zoomOut"><table id='+rowID+' class="table table-striped table-bordered" cellspacing="0" width="100%"><thead><tr><th> <%= rb.getString("problem")%></th><th> <%= rb.getString("problem_nickname")%></th><th> <%= rb.getString("problem_finished_on")%></th><th> <%= rb.getString("problem_description")%></th><th> <%= rb.getString("solved_correctly")%></th><th> <%= rb.getString("nbr_mistakes_made")%></th><th> <%= rb.getString("nbr_hints_seen")%></th><th> <%= rb.getString("nbr_attempts_made")%></th><th> <%= rb.getString("nbr_videos_seen")%></th><th> <%= rb.getString("nbr_examples_seen")%></th><th><%= rb.getString("effort")%></th></tr></thead><tbody>';
                    $.each(outputStudentDataTodayList, function (i, obj) {
                        var correctHtml = "";
                        var imageURL = problem_imageURL+obj[11]+'.jpg';
                        var problemImgHTML = "<td> <a style='cursor:pointer' rel='popover' data-img='" + imageURL + "'>" + obj[0] + "</a></td>"
                        var effortLabelHTML = "<td> <a style='cursor:pointer' rel='popoverLabel' data-content='"+effortLabelMap[obj[8]]+"'>" + obj[8] + "</a></td>"
                        if ("1" == obj[4])
                            correctHtml = "<td><img style='width:15%;' src='"+servletContextPath+"/images/check.png'/></td>";
                        else
                            correctHtml = "<td><img style='width:15%;' src='"+servletContextPath+"/images/x.png'/></td>";

                        secondHeader += "<tr>" + problemImgHTML + "<td>" + obj[1] + "</td><td>" +obj[10]+ "</td><td>" + obj[2] + "</td>" + correctHtml + "<td>" + obj[5] + "</td><td>" + obj[6] + "</td><td>" + obj[7] + "</td><td>" + obj[12] + "</td><td>" + obj[13] + "</td>"+effortLabelHTML+"</tr>";

                    });
                      secondHeader += "</tbody></table></div>"
                }

                }else if(i == 1){
                    //Show only problems done yesterday
                    var firstDate = 0;
                    var latestDate;
                    outputStudentDataList.forEach(function(e){
                        if(firstDate == 0) {
                            latestDate = new Date(e[10]).getDate();
                            firstDate++;
                        }
                        var tempDateHolder  = new Date(e[10]).getDate();
                        if(latestDate === tempDateHolder){
                            outputStudentDataYesterdayList.push(e);
                        }
                    });
                    if(jQuery.isEmptyObject(outputStudentDataYesterdayList)) {
                        thirdHeader = '<div id=' + "panel2" + rowID + ' class="panel-body animated zoomOut"><%= rb.getString("no_problems_done_yesterday")%></div>'
                    } else {
                        thirdHeader = '<div id='+"panel2"+rowID+' class="panel-body animated zoomOut"><table id='+rowID+' class="table table-striped table-bordered" cellspacing="0" width="100%"><thead><tr><th> <%= rb.getString("problem")%></th><th> <%= rb.getString("problem_nickname")%></th><th> <%= rb.getString("problem_finished_on")%></th><th> <%= rb.getString("problem_description")%></th><th> <%= rb.getString("solved_correctly")%></th><th> <%= rb.getString("nbr_mistakes_made")%></th><th> <%= rb.getString("nbr_hints_seen")%></th><th> <%= rb.getString("nbr_attempts_made")%></th><th> <%= rb.getString("nbr_videos_seen")%></th><th> <%= rb.getString("nbr_examples_seen")%></th><th><%= rb.getString("effort")%></th></tr></thead><tbody>';
                        $.each(outputStudentDataYesterdayList, function (i, obj) {
                            var correctHtml = "";
                            var imageURL = problem_imageURL+obj[11]+'.jpg';
                            var problemImgHTML = "<td> <a style='cursor:pointer' rel='popover' data-img='" + imageURL + "'>" + obj[0] + "</a></td>"
                            var effortLabelHTML = "<td> <a style='cursor:pointer' rel='popoverLabel' data-content='"+effortLabelMap[obj[8]]+"'>" + obj[8] + "</a></td>"
                            if ("1" == obj[4])
                                correctHtml = "<td><img style='width:15%;' src='"+servletContextPath+"/images/check.png'/></td>";
                            else
                                correctHtml = "<td><img style='width:15%;' src='"+servletContextPath+"/images/x.png'/></td>";

                            thirdHeader += "<tr>" + problemImgHTML + "<td>" + obj[1] + "</td><td>" +obj[10]+ "</td><td>" + obj[2] + "</td>" + correctHtml + "<td>" + obj[5] + "</td><td>" + obj[6] + "</td><td>" + obj[7] + "</td><td>" + obj[12] + "</td><td>" + obj[13] + "</td>"+effortLabelHTML+"</tr>";

                        });
                        thirdHeader += "</tbody></table></div>"
                    }
                }else{
                    //show all other problems
                    firstHeader = '<div id='+"panel"+rowID+' class="panel-body animated zoomOut"><table id='+rowID+' class="table table-striped table-bordered" cellspacing="0" width="100%"><thead><tr><th> <%= rb.getString("problem")%></th><th> <%= rb.getString("problem_nickname")%></th><th> <%= rb.getString("problem_finished_on")%></th><th> <%= rb.getString("problem_description")%></th><th> <%= rb.getString("solved_correctly")%></th><th> <%= rb.getString("nbr_mistakes_made")%></th><th> <%= rb.getString("nbr_hints_seen")%></th><th> <%= rb.getString("nbr_attempts_made")%></th><th> <%= rb.getString("nbr_videos_seen")%></th><th> <%= rb.getString("nbr_examples_seen")%></th><th><%= rb.getString("effort")%></th></tr></thead><tbody>';
                    $.each(outputStudentDataList, function (i, obj) {
                        var correctHtml = "";
                        var imageURL = problem_imageURL+obj[11]+'.jpg';
                        var problemImgHTML = "<td> <a style='cursor:pointer' rel='popover' data-img='" + imageURL + "'>" + obj[0] + "</a></td>"
                        var effortLabelHTML = "<td> <a style='cursor:pointer' rel='popoverLabel' data-content='"+effortLabelMap[obj[8]]+"'>" + obj[8] + "</a></td>"
                        if ("1" == obj[4])
                            correctHtml = "<td><img style='width:15%;' src='"+servletContextPath+"/images/check.png'/></td>";
                        else
                            correctHtml = "<td><img style='width:15%;' src='"+servletContextPath+"/images/x.png'/></td>";

                        firstHeader += "<tr>" + problemImgHTML + "<td>" + obj[1] + "</td><td>" +obj[10]+ "</td><td>" + obj[2] + "</td>" + correctHtml + "<td>" + obj[5] + "</td><td>" + obj[6] + "</td><td>" + obj[7] + "</td><td>" + obj[12] + "</td><td>" + obj[13] + "</td>"+effortLabelHTML+"</tr>";

                    });
                    firstHeader += "</tbody></table></div>"
                }
            }

            var tabDetails = '<div class="tab-content "><div class="tab-pane active" id="1">'+secondHeader+'</div><div class="tab-pane" id="2">'+thirdHeader+'</div><div class="tab-pane" id="3">'+firstHeader+'</div></div>'
            row.child(tabPanel+tabDetails).show();

            $("#panel"+rowID).toggleClass('zoomIn zoomOut');
            $("#panel1"+rowID).toggleClass('zoomIn zoomOut');
            $("#panel2"+rowID).toggleClass('zoomIn zoomOut');

            $('a[rel=popover]').popover({
                html: true,
                trigger: 'hover',
                placement: 'top',
                container: 'body',
                content: function () {
                    return '<img src="' + $(this).data('img') + '" />';
                }
            });
            $('a[rel=popoverLabel]').popover({
                html: false,
                trigger: 'hover',
                placement: 'left',
            });

        }

    });
var completeDataChart;
    $(document).on('click', 'a.getCompleteMasteryByAverage', function () {
        var tr = $(this).closest('tr');
        var row = perProblemSetReport.row(tr);
        var studentID = row.data()['studentId'];
        $.ajax({
            type: "POST",
            url: pgContext + "/tt/tt/getCompleteMasteryProjectionForStudent",
            data: {
                classId: classID,
                chartType: 'avg',
                studentId: studentID
            },
            success: function (response) {
                var completeProjectionByAVG = $.parseJSON(response);
                var problemsetName = [];
                var masteryData = [];
                if(completeDataChart) {
                    completeDataChart.destroy();
                }
                $.each( completeProjectionByAVG, function (i, obj) {
                    var tmp = obj.split("~~~");
                    problemsetName.push(perProblemSetColumnNamesMap[tmp[0]]);
                    masteryData.push(tmp[1]);
                });

                completeDataChart = new Chart($("#completeMasteryForStudentCanvas"), {
                    type: 'bar',
                    data: {
                        labels: problemsetName,
                        datasets: [{
                            label: '<%= rb.getString("average_mastery_recorded")%>',
                            data: masteryData,
                            backgroundColor: '#33b5e5'
                        }]
                    }, options: {
                        legend: {
                            display: false,
                            position: 'bottom'
                        },scales: {
                            yAxes: [{
                                display: true,
                                ticks: {
                                    suggestedMin: 0.00,
                                    max: 1.00
                                }
                            }]
                        }
                    }
                });
                $('#completeMasteryForStudent').modal('show');
            }
        });
    });

    $(document).on('click', 'a.getCompleteMasteryByMax', function () {
        var tr = $(this).closest('tr');
        var row = perProblemSetReport.row(tr);
        var studentID = row.data()['studentId'];
        $.ajax({
            type: "POST",
            url: pgContext + "/tt/tt/getCompleteMasteryProjectionForStudent",
            data: {
                classId: classID,
                chartType: 'max',
                studentId: studentID
            },
            success: function (response) {
                var completeProjectionByMax = $.parseJSON(response);
                var problemsetName = [];
                var masteryData = [];
                if(completeDataChart) {
                    completeDataChart.destroy();
                }
                $.each( completeProjectionByMax, function (i, obj) {
                    var tmp = obj.split("~~~");
                    problemsetName.push(perProblemSetColumnNamesMap[tmp[0]]);
                    masteryData.push(tmp[1]);
                });

                completeDataChart = new Chart($("#completeMasteryForStudentCanvas"), {
                    type: 'bar',
                    data: {
                        labels: problemsetName,
                        datasets: [{
                            label: '<%= rb.getString("max_mastery_recorded")%>',
                            data: masteryData,
                            backgroundColor: '#33b5e5'
                        }]
                    }, options: {
                        legend: {
                            display: false,
                            position: 'bottom'
                        },scales: {
                            yAxes: [{
                                display: true,
                                ticks: {
                                    suggestedMin: 0.00,
                                    max: 1.00
                                }
                            }]
                        }
                    }
                });
                $('#completeMasteryForStudent').modal('show');
            }
        });

    });

    $(document).on('click', 'a.getCompleteMasteryByLatest', function () {
        var tr = $(this).closest('tr');
        var row = perProblemSetReport.row(tr);
        var studentID = row.data()['studentId'];
        $.ajax({
            type: "POST",
            url: pgContext + "/tt/tt/getCompleteMasteryProjectionForStudent",
            data: {
                classId: classID,
                chartType: 'latest',
                studentId: studentID
            },
            success: function (response) {
                var completeProjectionByLatest = $.parseJSON(response);
                var problemsetName = [];
                var masteryData = [];
                if(completeDataChart) {
                    completeDataChart.destroy();
                }
                $.each( completeProjectionByLatest, function (i, obj) {
                    var tmp = obj.split("~~~");
                    problemsetName.push(perProblemSetColumnNamesMap[tmp[0]]);
                    masteryData.push(tmp[1]);
                });

                completeDataChart = new Chart($("#completeMasteryForStudentCanvas"), {
                    type: 'bar',
                    data: {
                        labels: problemsetName,
                        datasets: [{
                            label: '<%= rb.getString("last_recorded_mastery")%>',
                            data: masteryData,
                            backgroundColor: '#33b5e5'
                        }]
                    }, options: {
                        legend: {
                            display: false,
                            position: 'bottom'
                        },scales: {
                            yAxes: [{
                                display: true,
                                ticks: {
                                    suggestedMin: 0.00,
                                    max: 1.00
                                }
                            }]
                        }
                    }
                });
                $('#completeMasteryForStudent').modal('show');
            }
        });

    });

    /** Report Handler Starts **/

    $('#collapseOne').on('show.bs.collapse', function ()  {
        $('#collapseOne').find('.loader').show();
        $.ajax({
            type : "POST",
            url : pgContext+"/tt/tt/getTeacherReports",
            data : {
                classId: classID,
                teacherId: teacherID,
                reportType: 'perStudentPerProblemSetReport',
                lang: loc,
                filter: ''
            },
            success : function(data) {
                $('#collapseOne').find('.loader').hide();
                var jsonData = $.parseJSON(data);
                perProblemSetLevelOne = jsonData.levelOneData;
                perProblemSetColumnNamesMap = jsonData.columns;

                var indexcolumn = 3;
                var columNvalues = $.map(perProblemSetColumnNamesMap, function (v) {
                        var temp = {
                            "title": v, "name": v.replace(/\s/g, ''), "targets": indexcolumn,
                            "createdCell": function (td, cellData, rowData, row, col) {
                                if (cellData == '') {
                                    $(td).text();
                                    return;
                                }
                                var dataArray = cellData.split("---");
                                $(td).html(""+dataArray[0] + dataArray[1]+"&nbsp;&nbsp;<div class='fa fa-line-chart getMastery-trajectory-for-problemset' title='Get Mastery Trajectory' style='cursor: pointer;' aria-hidden='true'><span style='display: none'>"+dataArray[3]+"</span></div>");
                                if (dataArray[1] <= 0.25) {
                                    if (dataArray[2] >= 10) {
                                        $(td).addClass('span-danger-layer-one');
                                    }else if(dataArray[2] < 10 && dataArray[2] >= 2){
                                        $(td).addClass('span-danger-layer-two');
                                    }

                                } else if (dataArray[1] > 0.25 && dataArray[1] < 0.5) {
                                    if (dataArray[2] >= 10) {
                                        $(td).addClass('span-warning-layer-one');
                                    }else if(dataArray[2] < 10 && dataArray[2] >= 2){
                                        $(td).addClass('span-warning-layer-two');
                                    }
                                } else if (dataArray[1] > 0.5 && dataArray[1] < 0.75) {
                                    if (dataArray[2] >= 10) {
                                        $(td).addClass('span-info-layer-one');
                                    }else if(dataArray[2] < 10 && dataArray[2] >= 2){
                                        $(td).addClass('span-info-layer-two');
                                    }
                                } else if (dataArray[1] > 0.75) {
                                    if (dataArray[2] >= 10) {
                                        $(td).addClass('span-sucess-layer-one');
                                    }else if(dataArray[2] < 10 && dataArray[2] >= 2){
                                        $(td).addClass('span-sucess-layer-two');
                                    }
                                }
                            }
                        };
                        indexcolumn++;
                        return temp;
                    }
                );
                columNvalues.unshift({"title" : "<%= rb.getString("student_name")%>","name":"studentName" , "targets": [0]},
                    {"title" : "<%= rb.getString("username")%>","name":"userName", "targets": [1],   "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).html(cellData+"&nbsp;&nbsp;" +
                                "<a tabindex='0' rel='completeMasteryChartPopover' data-toggle='popover' data-trigger='focus' title='<%= rb.getString("get_complete_mastery_chart")%>' style='cursor: pointer;' aria-hidden='true'><i class='fa fa-bar-chart' aria-hidden='true'/></a>");
                        }
                    },
                    {"title" : "<%= rb.getString("student_id")%>","name":"studentId", "targets": [2], visible : false});
                var columDvalues = $.map(perProblemSetColumnNamesMap, function(v) {
                        v = v.replace(/\s/g, '');
                        return  { width: "20%", data : v };
                    }
                );
                columDvalues.unshift({data: "studentName"},{data: "userName"},{data: "studentId"});
                var perProblemSetLevelOneFullTemp = [];
                $.map(perProblemSetLevelOne, function (item,k) {
                        var perProblemSetLevelOneTemp = {};
                                    item.forEach(function(e){
                                        var itemArrays = e.split("~~~");
                                        perProblemSetLevelOneTemp[itemArrays[0]] = itemArrays[1]
                                        perProblemSetLevelOneTemp['studentId'] = k;
                                })
                        perProblemSetLevelOneFullTemp.push(perProblemSetLevelOneTemp);
                    }
                );

                if (perProblemSetReport) {
                    perProblemSetReport.destroy();
                    $('#perTopicStudentReport').empty();
                }

                if (languageSet == "es") {
                perProblemSetReport = $('#perTopicStudentReport').DataTable({
                    data: perProblemSetLevelOneFullTemp,
                    destroy: true,
                    "columns": columDvalues,
                    "columnDefs": columNvalues,
                    "bPaginate": true,
                    "language": {
                            "sProcessing":     "Procesando...",
                            "sLengthMenu":     "Mostrar _MENU_ registros",
                            "sZeroRecords":    "No se encontraron resultados",
                            "sEmptyTable":     "Ningún dato disponible en esta tabla",
                            "sInfo":           "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
                            "sInfoEmpty":      "Mostrando registros del 0 al 0 de un total de 0 registros",
                            "sInfoFiltered":   "(filtrado de un total de _MAX_ registros)",
                            "sInfoPostFix":    "",
                            "sSearch":         "Buscar:",
                            "sUrl":            "",
                            "sInfoThousands":  ",",
                            "sLoadingRecords": "Cargando...",
                            "oPaginate": {
                                "sFirst":    "Primero",
                                "sLast":     "Último",
                                "sNext":     "Siguiente",
                                "sPrevious": "Anterior"
                            },
                            "oAria": {
                                "sSortAscending":  ": Activar para ordenar la columna de manera ascendente",
                                "sSortDescending": ": Activar para ordenar la columna de manera descendente"
                            }
                    },
                    "scrollX": true,
                    "bFilter": false,
                    "bLengthChange": false,
                    rowReorder: false,
                    "bSort": false,
                    "drawCallback": function () {
                        $('a[rel=completeMasteryChartPopover]').popover({
                            html: true,
                            trigger: 'focus',
                            placement: 'right',
                            content: function () {
                                return '<ul><li><a style="cursor: pointer;" class="getCompleteMasteryByAverage"> <%= rb.getString("get_mastery_by_average")%> </a></li>' +
                                    '<li><a style="cursor: pointer;" class="getCompleteMasteryByMax"> <%= rb.getString("get_mastery_by_highest")%></a></li>' +
                                    '<li><a style="cursor: pointer;" class="getCompleteMasteryByLatest"> <%= rb.getString("get_mastery_by_last_reported")%></a></li></ul>';
                            }
                        })
                    }
                });
                }
                else {
                perProblemSetReport = $('#perTopicStudentReport').DataTable({
                    data: perProblemSetLevelOneFullTemp,
                    destroy: true,
                    "columns": columDvalues,
                    "columnDefs": columNvalues,
                    "bPaginate": true,
                    "scrollX": true,
                    "bFilter": false,
                    "bLengthChange": false,
                    rowReorder: false,
                    "bSort": false,
                    "drawCallback": function () {
                        $('a[rel=completeMasteryChartPopover]').popover({
                            html: true,
                            trigger: 'focus',
                            placement: 'right',
                            content: function () {
                                return '<ul><li><a style="cursor: pointer;" class="getCompleteMasteryByAverage"> <%= rb.getString("get_mastery_by_average")%> </a></li>' +
                                '<li><a style="cursor: pointer;" class="getCompleteMasteryByMax"> <%= rb.getString("get_mastery_by_highest")%></a></li>' +
                                '<li><a style="cursor: pointer;" class="getCompleteMasteryByLatest"> <%= rb.getString("get_mastery_by_last_reported")%></a></li></ul>';
                        }
                        })
                    }
                });
                }
                
                
                
            }
        });


    });

    $('#standardsBtn').on('click', function ()  {
        $('#collapseSix').find('.loader').show();
        var filter=document.getElementById("standardsFilter").value;
        
        $.ajax({
            type : "POST",
            url : pgContext+"/tt/tt/getTeacherReports",
            data : {
                classId: classID,
                teacherId: teacherID,
                reportType: 'perStudentPerProblemReport',
                lang: loc,
                filter: filter
            },
            success : function(data) {
                $('#collapseSix').find('.loader').hide();
                //alert(data);
                var jsonData = $.parseJSON(data);
                perProblemSetLevelOne = jsonData.levelOneData;
                perStudentPerProblemColumnNamesMap = jsonData.columns;
				
                
                var txt="";
                var abbr="";
                var indexcolumn = 3;
                var columNvalues = $.map(perStudentPerProblemColumnNamesMap, function (v) {
                        var temp = {
  		
                            "title": v, "name": v.replace(/\s/g, ''), "targets": indexcolumn,
                            "createdCell": function (td, cellData, rowData, row, col) {
                                if (cellData == '') {
                                    $(td).text("");
                                    return;
                                }
								
                                if ((cellData == null) || (cellData == "null")) {
                                	cellData = " ";
                                    $(td).html("");
                                }                                
                                else if (cellData == "SKIP") {                                	
                                	txt = "<%= rb.getString("skip")%>";
                                    var abbr = txt.split(":");
                                    $(td).html(""+abbr[0]);
                                	$(td).addClass('span-SKIP');
                                }
                                else if (cellData == "NOTR") {
                                	txt = "<%= rb.getString("notr")%>";
                                    var abbr = txt.split(":");
                                    $(td).html(""+abbr[0]);
                                	$(td).addClass('span-NOTR');
                                }
                                else if (cellData == "GIVEUP") {
                                	txt = "<%= rb.getString("giveup")%>";
                                    var abbr = txt.split(":");
                                    $(td).html(""+abbr[0]);
                                	$(td).addClass('span-GIVEUP');
                                }
                                else if (cellData == "SOF") {
                                	txt = "<%= rb.getString("sof")%>";
                                    var abbr = txt.split(":");
                                    $(td).html(""+abbr[0]);
                                	$(td).addClass('span-SOF');
                                }
                                else if (cellData == "ATT") {
                                	txt = "<%= rb.getString("att")%>";
                                    var abbr = txt.split(":");
                                    $(td).html(""+abbr[0]);
                                	$(td).addClass('span-ATT');
                                }
                                else if (cellData == "GUESS") {
                                	txt = "<%= rb.getString("guess")%>";
                                    var abbr = txt.split(":");
                                    $(td).html(""+abbr[0]);
                                	$(td).addClass('span-GUESS');
                                }
                                else if (cellData == "SHINT") {
                                	txt = "<%= rb.getString("shint")%>";
                                    var abbr = txt.split(":");
                                    $(td).html(""+abbr[0]);
                                	$(td).addClass('span-SHINT');
                                }
                                else if (cellData == "SHELP") {
                                	txt = "<%= rb.getString("shelp")%>";
                                    var abbr = txt.split(":");
                                    $(td).html(""+abbr[0]);
                                	$(td).addClass('span-SHELP');
                                }
                                else if (cellData == "NODATA") {
                                	txt = "<%= rb.getString("no_data")%>";
                                    var abbr = txt.split(":");
                                    $(td).html(""+abbr[0]);
                                	$(td).addClass('span-NODATA');
                                }
                                else {
                                    $(td).html("");
                                }
                            }
                        };
                        indexcolumn++;
                        return temp;
                    }
                );



                columNvalues.unshift({"title" : "<%= rb.getString("student_name")%>","name":"studentName" , "targets": [0]},
                                     {"title" : "<%= rb.getString("username")%>","name":"userName", "targets": [1]}, 
                                     {"title" : "<%= rb.getString("student_id")%>","name":"studentId", "targets": [2], visible : false} );


<%
/**
                indexcolumn = 3;
                
				var columDvalues = $.map(perStudentPerProblemColumnNamesMap, function(v) {
                    var temp = {
                        "title": "problemId",
                        "name": "problemId",
                        "width": "10%", 
                        "data" : v,
                        "targets": [indexcolumn],
                        "render": function (data, type, full, meta) {
                            var problemId = full['problemId'];
                            var attri = ", 'ProblemPreview'"+","+"'width=750,height=550,status=yes,resizable=yes'";
                            var window = "'" + problemImageWindow[problemId] + "'" + attri ;
                            var imageURL = problem_imageURL+full['problemId']+'.jpg';
                            return '<a href="'+pgContext+'/WoAdmin?action=AdminGetQuickAuthSkeleton&probId='+problemId+'&teacherId=-1&reload=true&zoom=1" target="_blank" style="cursor:pointer" rel="popoverPerProblem" data-img="' + imageURL + '">' + data + '</a>';
                        }
                    }
                    indexcolumn++;
   	                return temp;
                });
**/
%>
				var columDvalues = $.map(perStudentPerProblemColumnNamesMap, function(v) {
                    v = v.replace(/\s/g, '');
                    var imageURL = problem_imageURL+v+'.jpg';
                    var problemImgHTML = "<a style='cursor:pointer' rel='popover' data-img='" + imageURL + "'>" + v + "</a>";
                    return  { width: "10%", data : v };
                }
            	);
               
                columDvalues.unshift({data: "studentName"},{data: "userName"},{data: "studentId"});
                var perProblemSetLevelOneFullTemp = [];
                $.map(perProblemSetLevelOne, function (item,k) {
                        var perProblemSetLevelOneTemp = {};
                                    item.forEach(function(e){
                                        var itemArrays = e.split("~~~");
                                        perProblemSetLevelOneTemp[itemArrays[0]] = itemArrays[1]
                                        perProblemSetLevelOneTemp['studentId'] = k;
                                })
                        perProblemSetLevelOneFullTemp.push(perProblemSetLevelOneTemp);
                    }
                );

      

                if (perProblemSetReport) {
                    perProblemSetReport.destroy();
                    $('#perStudentPerProblemReport').empty();
                }
                if (languageSet == "es") {
                perProblemSetReport = $('#perStudentPerProblemReport').DataTable({
                    data: perProblemSetLevelOneFullTemp,
                    destroy: true,
                    "columns": columDvalues,
                    "columnDefs": columNvalues,
                    "bPaginate": true,
                    "language": {
                            "sProcessing":     "Procesando...",
                            "sLengthMenu":     "Mostrar _MENU_ registros",
                            "sZeroRecords":    "No se encontraron resultados",
                            "sEmptyTable":     "Ningún dato disponible en esta tabla",
                            "sInfo":           "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
                            "sInfoEmpty":      "Mostrando registros del 0 al 0 de un total de 0 registros",
                            "sInfoFiltered":   "(filtrado de un total de _MAX_ registros)",
                            "sInfoPostFix":    "",
                            "sSearch":         "Buscar:",
                            "sUrl":            "",
                            "sInfoThousands":  ",",
                            "sLoadingRecords": "Cargando...",
                            "oPaginate": {
                                "sFirst":    "Primero",
                                "sLast":     "Último",
                                "sNext":     "Siguiente",
                                "sPrevious": "Anterior"
                            },
                            "oAria": {
                                "sSortAscending":  ": Activar para ordenar la columna de manera ascendente",
                                "sSortDescending": ": Activar para ordenar la columna de manera descendente"
                            }
                    },
                    "scrollX": true,
                    "bFilter": false,
                    "bLengthChange": false,
                    rowReorder: false,
                    "bSort": false,
                    "drawCallback": function () {
                        $('a[rel=popoverPerProblem]').popover({
                            html: true,
                            trigger: 'hover',
                            placement: 'top',
                            container: 'body',
                            content: function () {
                                return '<img src="' + $(this).data('img') + '" />';
                            }
                        });

                    }

                });
                }
                else {
                perProblemSetReport = $('#perStudentPerProblemReport').DataTable({
                    data: perProblemSetLevelOneFullTemp,
                    destroy: true,
                    "columns": columDvalues,
                    "columnDefs": columNvalues,
                    "bPaginate": true,
                    "scrollX": true,
                    "bFilter": false,
                    "bLengthChange": false,
                    rowReorder: false,
                    "bSort": false,
                    "drawCallback": function () {
                        $('a[rel=popoverPerProblem]').popover({
                            html: true,
                            trigger: 'hover',
                            placement: 'top',
                            container: 'body',
                            content: function () {
                                return '<img src="' + $(this).data('img') + '" />';
                            }
                        });

                    }

                });
                }
                
                
                
            }
        });


    });


    
    $('#collapseTwo').on('show.bs.collapse', function ()  {
        $('#collapseTwo').find('.loader').show();
        $.ajax({
            type : "POST",
            url : pgContext+"/tt/tt/getTeacherReports",
            data : {
                classId: classID,
                teacherId: teacherID,
                reportType: 'perProblemReport',
                lang: loc,
                filter: ''
            },
            success : function(data) {
                $('#collapseTwo').find('.loader').hide();
                var jsonData = $.parseJSON(data);
                var eachProblemData = jsonData.levelOneDataPerProblem;
                var perProblemSetLevelOneFullTemp = [];
                var problemImageMap = [];
                var problemImageWindow = [];
                $.map(eachProblemData, function (item, key) {
                    var perProblemSetLevelOneTemp = {};
                    perProblemSetLevelOneTemp['problemId'] = key;
                    $.map(item, function (itemValues, k) {
                        if (k == 'problemName' || k == 'noStudentsSeenProblem' ||
                            k == 'getGetPercStudentsSolvedFirstTry' || k == 'getGetPercStudentsSolvedSecondTry' || k == 'percStudentsRepeated' ||
                            k == 'percStudentsSkipped' || k == 'percStudentsGaveUp' || k == 'mostIncorrectResponse' || k=='problemStandardAndDescription') {
                            perProblemSetLevelOneTemp[k] = itemValues;
                            }else if(k=='imageURL'){
                            problemImageMap[key] = itemValues;
                            }else if(k == 'problemURLWindow'){
                            problemImageWindow[key]  = itemValues;
                            }
                        });
                        perProblemSetLevelOneFullTemp.push(perProblemSetLevelOneTemp);
                        });
                var columNvalues = [
                    { "title": "<%= rb.getString("problem_id")%>", "name" : "problemId" , "targets" : [0]},
                    { "title": "<%= rb.getString("problem_name")%>", "name" : "problemName" , "targets" : [1],"render": function ( data, type, full, meta ) {
                            var problemId = full['problemId'];
                            var attri = ", 'ProblemPreview'"+","+"'width=750,height=550,status=yes,resizable=yes'";
                             var window = "'" + problemImageWindow[problemId] + "'" + attri ;
                            return '<a  onclick="window.open('+window+');" style="cursor:pointer" rel="popoverPerProblem" data-img="' + problemImageMap[problemId] + '">' + data + '</a>';
                    }},
                    { "title": "<%= rb.getString("cc_standard")%>", "name" : "problemStandardAndDescription" , "targets" : [2],"render": function ( data, type, full, meta ) {
                        var standardSplitter = data.split(":");
                        return "<a style='cursor:pointer' rel='popoverstandard' title='"+standardSplitter[1]+"'  data-content='" + standardSplitter[2]+ "'>" + standardSplitter[0] + "</a>";
                    }},
                    { "title": "<%= rb.getString("nbr_students_seen_problem")%>", "name" : "noStudentsSeenProblem","targets" : [3] },
                    { "title": "<%= rb.getString("pct_students_solved_problem_first")%>", "name" : "getGetPercStudentsSolvedFirstTry","targets" : [4] ,"render": function ( data, type, full, meta ) {
                        return data+" %";
                    },"createdCell": function (td, cellData, rowData, row, col) {
                            if(cellData >= 80){
                                $(td).html(cellData +"&nbsp;&nbsp;<i class='fa fa-thumbs-up' aria-hidden='true'></i>");
                            }else if(cellData <= 20){
                                $(td).addClass('span-danger-layer-one');
                            }
                    } },
                    { "title": "<%= rb.getString("nbr_students_solved_problem_second")%>", "name" : "getGetPercStudentsSolvedSecondTry","targets" : [5], visible : false },
                    { "title": "<%= rb.getString("pct_students_repeated_problem")%>", "name" : "percStudentsRepeated","targets" : [6],"render": function ( data, type, full, meta ) {
                        return data+" %";
                    }},
                    { "title": "<%= rb.getString("pct_students_skipped_problem")%>", "name" : "percStudentsSkipped","targets" : [7] ,"render": function ( data, type, full, meta ) {
                        return data+" %";
                    }},
                    { "title": "<%= rb.getString("pct_students_gave_up")%>", "name" : "percStudentsGaveUp","targets" : [8],"render": function ( data, type, full, meta ) {
                        return data+" %";
                    }},
                    { "title": "<%= rb.getString("most_frequent_incorrect_response")%>", "name" : "mostIncorrectResponse","targets" : [9] }
                ];
                var columDvalues = [
                    { width: "10%", data : "problemId"},
                    { width: "10%", data : "problemName" },
                    { width: "10%", data : "problemStandardAndDescription" },
                    { width: "10%", data : "noStudentsSeenProblem" },
                    { width: "10%", data : "getGetPercStudentsSolvedFirstTry" },
                    { width: "5%", data : "getGetPercStudentsSolvedSecondTry"},
                    { width: "10%", data : "percStudentsRepeated"},
                    {width: "10%", data : "percStudentsSkipped"},
                    { width: "10%", data : "percStudentsGaveUp"},
                    { width: "10%", data : "mostIncorrectResponse"}
                ];

                if (perProblemReportTable) {
                    perProblemReportTable.destroy();
                    $('#perProblemReport').empty();
                }

                perProblemReportTable = $('#perProblemReport').DataTable({
                    data: perProblemSetLevelOneFullTemp,
                    destroy: true,
                    "columns": columDvalues,
                    "columnDefs": columNvalues,
                    "bPaginate": true,
                    "scrollX": true,
                    "bFilter": false,
                    "bLengthChange": false,
                    rowReorder: false,
                    "bSort": true,
                    "order": [[ 3, "desc" ]],
                    "drawCallback": function () {
                        $('a[rel=popoverPerProblem]').popover({
                            html: true,
                            trigger: 'hover',
                            placement: 'right',
                            container: 'body',
                            content: function () {
                                return '<img src="' + $(this).data('img') + '" />';
                            }
                        });

                        $('a[rel=popoverstandard]').popover({
                            html: false,
                            trigger: 'hover',
                            placement: 'right',
                            container: 'body'
                        });
                        $('a[rel=popoverHeader]').popover({
                            container : 'body',
                            trigger : 'hover',
                            placement: 'top',
                        });

                    },
                    headerCallback: function headerCallback(thead, data, start, end, display) {
                       $(thead).find('th').eq(5).html('<%= rb.getString("pct_students_repeated_problem")%> &nbsp;&nbsp;<a rel="popoverHeader"  data-content="<%= rb.getString("students_who_repeat")%>"><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>');
                       $(thead).find('th').eq(6).html('<%= rb.getString("pct_students_skipped_problem")%> &nbsp;&nbsp;<a rel="popoverHeader" data-content="<%= rb.getString("students_who_skip")%>"><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>');
                       $(thead).find('th').eq(7).html('<%= rb.getString("pct_students_gave_up")%> &nbsp;&nbsp;<a rel="popoverHeader" data-content="<%= rb.getString("students_who_gave_up")%>"><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>');

                    }
                });


            }

        });

    });

    $('#collapseThree').on('show.bs.collapse', function ()  {
        $.ajax({
            type : "POST",
            url : pgContext+"/tt/tt/getTeacherReports",
            data : {
                classId: classID,
                teacherId: teacherID,
                reportType: 'perStudentReport',
                lang: loc,
                filter: ''
            },
            success : function(data) {
                var jsonData = $.parseJSON(data);
                effortMap = jsonData.effortChartValues;
                emotionMap = jsonData.fullstudentEmotionsMap;
                commentsMap = jsonData.fullstudentEmotionsComments;
                eachStudentData = jsonData.eachStudentDataValues;
                perStudentReport.clear().draw();
                perStudentReport.rows.add(jsonData.levelOneData).draw();
                perStudentReport.columns.adjust().draw();
            },
            error : function(e) {
                console.log(e);
            }
        });

    });

    $('#collapseFour').on('show.bs.collapse', function ()  {
        $('#collapseFourLoader').show();
        $.ajax({
            type : "POST",
            url : pgContext+"/tt/tt/getTeacherReports",
            data : {
                classId: classID,
                teacherId: teacherID,
                reportType: 'commonCoreClusterReport',
                lang: loc,
                filter: ''
            },
            success : function(data) {
                $('#collapseFourLoader').hide();
                var jsonData = $.parseJSON(data);
                var cc_headers = changeReportFourHeaderAccordingToLanguage();
                var columNvalues = [
                    { "title": cc_headers['cclusters'], "name" : "clusterNames" , "targets" : [0],"render": function ( data, type, full, meta ) {
                        var clusterCCName = full['clusterCCName'];
                        return "<a style='cursor:pointer' rel='popoverCluster' data-content='"+clusterCCName+"'>" + data + "</a>";
                    },"createdCell": function (td, cellData, rowData, row, col) {
                        if (rowData['noOfProblemsonFirstAttempt'] < 20 && rowData['noOfProblemsInCluster'] >= 5 ) {
                            $(td).addClass('span-danger-layer-one');
                        } else if ((rowData['noOfProblemsonFirstAttempt'] > 20) && (rowData['noOfProblemsonFirstAttempt'] <  40) && (rowData['noOfProblemsInCluster'] >= 5 )) {
                            $(td).addClass('span-warning-layer-one');
                        }
                    }},
                    { "title": cc_headers['problems'], "name" : "noOfProblemsInCluster" , "targets" : [1],"render": function ( data, type, full, meta ) {
                        return '<label style="width: 50%;">'+data+'</label><a  class="getProblemDetailsPerCluster" aria-expanded="true" aria-controls="collapseOne"><i class="glyphicon glyphicon-menu-down"></i></a>';
                    },"createdCell": function (td, cellData, rowData, row, col) {
                        if (rowData['noOfProblemsonFirstAttempt'] < 20 && rowData['noOfProblemsInCluster'] >= 5 ) {
                            $(td).addClass('span-danger-layer-one');
                        } else if ((rowData['noOfProblemsonFirstAttempt'] > 20) && (rowData['noOfProblemsonFirstAttempt'] <  40) && (rowData['noOfProblemsInCluster'] >= 5 )) {
                            $(td).addClass('span-warning-layer-one');
                        }
                    }},
                    { "title": cc_headers['fattempt'], "name" : "noOfProblemsonFirstAttempt","targets" : [2],"createdCell": function (td, cellData, rowData, row, col) {
                        if (rowData['noOfProblemsonFirstAttempt'] < 20 && rowData['noOfProblemsInCluster'] >= 5) {
                            $(td).addClass('span-danger-layer-one');
                        } else if ((rowData['noOfProblemsonFirstAttempt'] > 20) && (rowData['noOfProblemsonFirstAttempt'] <  40) && (rowData['noOfProblemsInCluster'] >= 5)) {
                            $(td).addClass('span-warning-layer-one');
                        }
                    } },
                    { "title": cc_headers['avgratio'], "name" : "totalHintsViewedPerCluster","targets" : [3],"createdCell": function (td, cellData, rowData, row, col) {
                        if (rowData['noOfProblemsonFirstAttempt'] < 20 && rowData['noOfProblemsInCluster'] >= 5) {
                            $(td).addClass('span-danger-layer-one');
                        } else if ((rowData['noOfProblemsonFirstAttempt'] > 20) && (rowData['noOfProblemsonFirstAttempt'] <  40) && (rowData['noOfProblemsInCluster'] >= 5)) {
                            $(td).addClass('span-warning-layer-one');
                        }
                    }},
                    { "title": "<%= rb.getString("cluster_id")%>", "name" : "clusterId","targets" : [4], visible : false},
                    { "title": "<%= rb.getString("cluster_description")%>", "name" : "clusterCCName","targets" : [5], visible : false}
                ];
                var columDvalues = [
                    { width: "30%", data : "categoryCodeAndDisplayCode"},
                    { width: "20%", data : "noOfProblemsInCluster" },
                    { width: "20%", data : "noOfProblemsonFirstAttempt" },
                    { width: "20%", data : "totalHintsViewedPerCluster" },
                    { width: "5%", data : "clusterId"},
                    { width: "5%", data : "clusterCCName"}

                ];
                var dataPerCluster = [];
                $.map(jsonData, function (item, key) {
                    var perProblemSetLevelOneTemp = {};
                    $.map(item, function (itemValues, k)
                    {
                        perProblemSetLevelOneTemp[k] = itemValues;
                    });
                    dataPerCluster.push(perProblemSetLevelOneTemp);
                });

                if (perClusterReportTable) {
                    perClusterReportTable.destroy();
                    $('#perClusterReport').empty();
                }
                if (languageSet == 'es') {

                perClusterReportTable = $('#perClusterReport').DataTable({
                    data: dataPerCluster,
                    destroy: true,
                    "columns": columDvalues,
                    "columnDefs": columNvalues,
                    "bFilter": false,
                    "bPaginate": false,                
                    "language": {
                        "sProcessing":     "Procesando...",
                        "sLengthMenu":     "Mostrar _MENU_ registros",
                        "sZeroRecords":    "No se encontraron resultados",
                        "sEmptyTable":     "Ningún dato disponible en esta tabla",
                        "sInfo":           "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
                        "sInfoEmpty":      "Mostrando registros del 0 al 0 de un total de 0 registros",
                        "sInfoFiltered":   "(filtrado de un total de _MAX_ registros)",
                        "sInfoPostFix":    "",
                        "sSearch":         "Buscar:",
                        "sUrl":            "",
                        "sInfoThousands":  ",",
                        "sLoadingRecords": "Cargando...",
                        "oPaginate": {
                            "sFirst":    "Primero",
                            "sLast":     "Último",
                            "sNext":     "Siguiente",
                            "sPrevious": "Anterior"
                        },
                        "oAria": {
                            "sSortAscending":  ": Activar para ordenar la columna de manera ascendente",
                            "sSortDescending": ": Activar para ordenar la columna de manera descendente"
                        }
                    },
                    "bLengthChange": false,
                    rowReorder: false,
                    "bSort": true,
                    "drawCallback": function () {
                        $('a[rel=popoverCluster]').popover({
                            html: false,
                            trigger: 'hover',
                            placement: 'right',
                            container: 'body',
                        });
                    }
                });
                }
                else {
                    perClusterReportTable = $('#perClusterReport').DataTable({
                        data: dataPerCluster,
                        destroy: true,
                        "columns": columDvalues,
                        "columnDefs": columNvalues,
                        "bFilter": false,
                        "bPaginate": false,                
                        "bLengthChange": false,
                        rowReorder: false,
                        "bSort": true,
                        "drawCallback": function () {
                            $('a[rel=popoverCluster]').popover({
                                html: false,
                                trigger: 'hover',
                                placement: 'right',
                                container: 'body',
                            });
                        }
                    });                	
                }
            }
        });

    });

    
    $('#collapseFive').on('show.bs.collapse', function ()  {
    	
    	$('#collapseFive').find('.loader').show();
        $.ajax({
            type : "POST",
            url : pgContext+"/tt/tt/getTeacherReports",
            data : {
            	
                classId: classID,
                teacherId: teacherID,
                reportType: 'summarySurveyReport',
                lang: loc,
                filter: ''
            },
            success : function(data) {
            	$('#collapseFive').find('.loader').hide();
            	
                var jsonData = $.parseJSON(data);
                surveyData = jsonData;
                
                
                
                var columNvalues = [
                    
                    { "title": "<%= rb.getString("survey_name")%>", "name" : "SurveyName" , "targets" : [0],"render": function ( data, type, full, meta ) {
                        return '<label style="width: 50%;">'+data+'</label><a  class="getStudentDetail" aria-expanded="true" aria-controls="collapseOne"><i class="glyphicon glyphicon-menu-down"></i></a>';
                    }},
                    
                ];
                
                
                var surveyList = [];
                $.map(jsonData, function (item, key) {
                    
                    surveyList.push([key]);
                    
                });
                if (languageSet == 'es') {
                surveyReportTable = $('#surveyReport').DataTable({
                    data: surveyList,
                    destroy: true,
                    "columns": [ { title: "<%= rb.getString("survey_name")%>" , width: "30%"}],
                    "columnDefs": columNvalues,
                    "bFilter": false,
                    "bPaginate": false,
                    "language": {
                        "sProcessing":     "Procesando...",
                        "sLengthMenu":     "Mostrar _MENU_ registros",
                        "sZeroRecords":    "No se encontraron resultados",
                        "sEmptyTable":     "Ningún dato disponible en esta tabla",
                        "sInfo":           "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
                        "sInfoEmpty":      "Mostrando registros del 0 al 0 de un total de 0 registros",
                        "sInfoFiltered":   "(filtrado de un total de _MAX_ registros)",
                        "sInfoPostFix":    "",
                        "sSearch":         "Buscar:",
                        "sUrl":            "",
                        "sInfoThousands":  ",",
                        "sLoadingRecords": "Cargando...",
                        "oPaginate": {
                            "sFirst":    "Primero",
                            "sLast":     "Último",
                            "sNext":     "Siguiente",
                            "sPrevious": "Anterior"
                        },
                        "oAria": {
                            "sSortAscending":  ": Activar para ordenar la columna de manera ascendente",
                            "sSortDescending": ": Activar para ordenar la columna de manera descendente"
                        }
                    },
                    "bLengthChange": false,
                    rowReorder: false,
                    "bSort": true,
                    
                });
                }
                else {
                    surveyReportTable = $('#surveyReport').DataTable({
                        data: surveyList,
                        destroy: true,
                        "columns": [ { title: "<%= rb.getString("survey_name")%>" , width: "30%"}],
                        "columnDefs": columNvalues,
                        "bFilter": false,
                        "bPaginate": false,
                        "bLengthChange": false,
                        rowReorder: false,
                        "bSort": true,
                        
                    });
                	
                }
                
            },
            error : function(e) {
            	alert("error occured");
                console.log(e);
            }
        });

    });
    
    
    
    $('body').on('click', 'a.getQuestionDetail', function () {
        $(this).children(':first').toggleClass('rotate-icon');
        var tr = $(this).closest('tr');
        var row = surveyStudentTable.row(tr);
        if ( row.child.isShown() ) {
            row.child.hide();
        }else {
        	
            var studentId = row.data()[2];
            var surveyStudent = studentData[studentId];
            
            var surveyQuestion = surveyStudent.questionset;
            var questionList = [];
            $.map(surveyQuestion, function (item, key) {
                
            	questionList.push([item.description, item.studentAnswer]);
               
            });
            
            var columNvalues = [
            	{ "title": "<%= rb.getString("question")%>", "name" : "Question" , "targets" : [0],"render": function ( data, type, full, meta ) {
                    return '<label style="width: 90%;">'+data+'</label>';
                }},
                { "title": "<%= rb.getString("answer")%>", "name" : "Answer" , "targets" : [1],"render": function ( data, type, full, meta ) {
                    return '<label style="width: 90%;">'+data+'</label>';
                }}
                
                
            ];

            var $perSurveyQuestiontable = $($('#question_table_Survey').html());
            $perSurveyQuestiontable.css('width', '100%');
            if (languageSet == 'es') {
            surveyQuestionTable = $perSurveyQuestiontable.DataTable({
                data: questionList,
                destroy: true,
                "columns": [ { title: "<%= rb.getString("question")%>" , width: "20%"}, { title: "<%= rb.getString("answer")%>" , width: "20%"}],
                "columnDefs": columNvalues,
                "bFilter": false,
                "bPaginate": false,
                "language": {
                    "sProcessing":     "Procesando...",
                    "sLengthMenu":     "Mostrar _MENU_ registros",
                    "sZeroRecords":    "No se encontraron resultados",
                    "sEmptyTable":     "Ningún dato disponible en esta tabla",
                    "sInfo":           "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
                    "sInfoEmpty":      "Mostrando registros del 0 al 0 de un total de 0 registros",
                    "sInfoFiltered":   "(filtrado de un total de _MAX_ registros)",
                    "sInfoPostFix":    "",
                    "sSearch":         "Buscar:",
                    "sUrl":            "",
                    "sInfoThousands":  ",",
                    "sLoadingRecords": "Cargando...",
                    "oPaginate": {
                        "sFirst":    "Primero",
                        "sLast":     "Último",
                        "sNext":     "Siguiente",
                        "sPrevious": "Anterior"
                    },
                    "oAria": {
                        "sSortAscending":  ": Activar para ordenar la columna de manera ascendente",
                        "sSortDescending": ": Activar para ordenar la columna de manera descendente"
                    }
                },
                "bLengthChange": false,
                rowReorder: false,
                "bSort": true,
                
            });
            }
            else { 
                surveyQuestionTable = $perSurveyQuestiontable.DataTable({
                    data: questionList,
                    destroy: true,
                    "columns": [ { title: "<%= rb.getString("question")%>" , width: "20%"}, { title: "<%= rb.getString("answer")%>" , width: "20%"}],
                    "columnDefs": columNvalues,
                    "bFilter": false,
                    "bPaginate": false,
                    "bLengthChange": false,
                    rowReorder: false,
                    "bSort": true,
                    
                });
            	
            }
            
            surveyStudentTable.row(tr).child(surveyQuestionTable.table().container()).show();
        }
    });
    
    
    $('body').on('click', 'a.getStudentDetail', function () {
        $(this).children(':first').toggleClass('rotate-icon');
        var tr = $(this).closest('tr');
        var row = surveyReportTable.row(tr);
        if ( row.child.isShown() ) {
            row.child.hide();
        }else {
        	
            var surveyName = row.data()[0];
            var surveyStudents = surveyData[surveyName];
            studentData = surveyStudents;
            var studentList = [];
            $.map(surveyStudents, function (item, key) {
                
            	studentList.push([item.studentName, item.studentUserName, item.studentId]);
                
            });
            
            var columNvalues = [
            	{ "title": "<%= rb.getString("student_name")%>", "name" : "Student Name" , "targets" : [0],"render": function ( data, type, full, meta ) {
                    return '<label style="width: 90%;">'+data+'</label>';
                }},
                { "title": "<%= rb.getString("username")%>", "name" : "Username" , "targets" : [1],"render": function ( data, type, full, meta ) {
                    return '<label style="width: 50%;">'+data+'</label><a  class="getQuestionDetail" aria-expanded="true" aria-controls="collapseOne"><i class="glyphicon glyphicon-menu-down"></i></a>';
                }},
                { "title": "<%= rb.getString("student_id")%>", "name" : "StudentId" , "targets" : [2],"render": function ( data, type, full, meta ) {
                    return '<label style="width: 50%;">'+data+'</label>';
                }}
                
            ];
            
            var $perSurveyStudenttable = $($('#student_table_Survey').html());
            $perSurveyStudenttable.css('width', '100%');
            surveyStudentTable = $perSurveyStudenttable.DataTable({
                data: studentList,
                destroy: true,
                "columns": [ { title: "<%= rb.getString("student_name")%>" , width: "20%"}, { title: "<%= rb.getString("username")%>" , width: "20%"}, { title: "<%= rb.getString("student_id")%>" , width: "20%"}],
                "columnDefs": columNvalues,
                "bFilter": false,
                "bPaginate": false,
                "bLengthChange": false,
                rowReorder: false,
                "bSort": true,
                
            });
            
            surveyStudentTable.column(2).visible(false);
            
            surveyReportTable.row(tr).child(surveyStudentTable.table().container()).show();
            
        }
    });

    /** Report Handler Ends **/

    $('body').on('click', 'a.getProblemDetailsPerCluster', function () {
        $(this).children(':first').toggleClass('rotate-icon');
        var tr = $(this).closest('tr');
        var row = perClusterReportTable.row(tr);
        if ( row.child.isShown() ) {
            row.child.hide();
        }else {
            var clusterId = row.data()['clusterId'];

            $.ajax({
                type: "POST",
                url: pgContext + "/tt/tt/getProblemDetailsPerCluster",
                data: {
                    classId: classID,
                    teacherId: teacherID,
                    clusterId: clusterId
                },
                success: function (data) {
                    var jsonData = $.parseJSON(data);
                    var eachProblemData = jsonData;
                    perProblemObject = eachProblemData;
                    var perProblemSetLevelOneFullTemp = [];
                    var problemImageMap = [];
                    var problemImageWindow = [];
                    $.map(eachProblemData, function (item, key) {
                        var perProblemSetLevelOneTemp = {};
                        perProblemSetLevelOneTemp['problemId'] = key;
                        $.map(item, function (itemValues, k) {
                            if (k == 'problemName' || k == 'noStudentsSeenProblem' ||
                                k == 'getGetPercStudentsSolvedFirstTry' || k == 'getGetPercStudentsSolvedSecondTry' || k == 'percStudentsRepeated' ||
                                k == 'percStudentsSkipped' || k == 'percStudentsGaveUp' || k == 'mostIncorrectResponse' || k == 'problemStandardAndDescription' || k == 'similarproblems') {
                                perProblemSetLevelOneTemp[k] = itemValues;
                            } else if (k == 'imageURL') {
                                problemImageMap[key] = itemValues;
                            }else if(k == 'problemURLWindow'){
                                problemImageWindow[key]  = itemValues;
                            }

                        });
                        perProblemSetLevelOneFullTemp.push(perProblemSetLevelOneTemp);
                    });
                    var columNvalues = [
                        {"title": "<%= rb.getString("problem_id")%>", "name": "problemId", "targets": [0]},
                        {
                            "title": "<%= rb.getString("problem_name")%>",
                            "name": "problemName",
                            "targets": [1],
                            "render": function (data, type, full, meta) {
                                var problemId = full['problemId'];
                                var attri = ", 'ProblemPreview'"+","+"'width=750,height=550,status=yes,resizable=yes'";
                                var window = "'" + problemImageWindow[problemId] + "'" + attri ;
                                var imageURL = problem_imageURL+full['problemId']+'.jpg';
                                return '<a href="'+pgContext+'/WoAdmin?action=AdminGetQuickAuthSkeleton&probId='+problemId+'&teacherId=-1&reload=true&zoom=1" target="_blank" style="cursor:pointer" rel="popoverPerProblem" data-img="' + imageURL + '">' + data + '</a>';
                            }
                        },
                        {
                            "title": "<%= rb.getString("cc_standard")%>",
                            "name": "problemStandardAndDescription",
                            "targets": [2],
                            "render": function (data, type, full, meta) {
                                var standardSplitter = data.split(":");
                                return "<a style='cursor:pointer' rel='popoverstandard' title='"+standardSplitter[1]+"'  data-content='" + standardSplitter[2]+ "'>" + standardSplitter[0] + "</a>";
                            }
                        },
                        {"title": "<%= rb.getString("nbr_students_seen_problem")%>", "name": "noStudentsSeenProblem", "targets": [3]},
                        {
                            "title": "<%= rb.getString("pct_students_solved_problem_first")%>",
                            "name": "getGetPercStudentsSolvedFirstTry",
                            "targets": [4],
                            "createdCell": function (td, cellData, rowData, row, col) {
                                if(cellData >= 80 && rowData['noStudentsSeenProblem'] > 5){
                                    $(td).html(cellData +"&nbsp;&nbsp;<i class='fa fa-thumbs-up' aria-hidden='true'></i>");
                                }else if(cellData <= 20 && rowData['noStudentsSeenProblem'] > 5 ){
                                    $(td).addClass('span-danger-layer-one');
                                }else if(cellData >= 20 && cellData <= 40 && rowData['noStudentsSeenProblem'] > 5 ){
                                    $(td).addClass('span-warning-layer-one');
                                }
                            },"render": function ( data, type, full, meta ) {
                            return data+" %";
                        }
                        },
                        {
                            "title": "<%= rb.getString("nbr_students_solved_problem_second")%>",
                            "name": "getGetPercStudentsSolvedSecondTry",
                            "targets": [5],
                            visible: false
                        },
                        {
                            "title": "<%= rb.getString("pct_students_repeated_problem")%>",
                            "name": "percStudentsRepeated",
                            "targets": [6],
                            "render": function ( data, type, full, meta ) {
                            return data+" %";
                        }
                        },
                        {
                            "title": "<%= rb.getString("pct_students_skipped_problem")%>",
                            "name": "percStudentsSkipped",
                            "targets": [7],
                            "render": function ( data, type, full, meta ) {
                            return data+" %";
                        }
                        },
                        {
                            "title": "<%= rb.getString("pct_students_gave_up")%>",
                            "name": "percStudentsGaveUp",
                            "targets": [8],
                            "render": function ( data, type, full, meta ) {
                            return data+" %";
                        }
                        },
                        {"title": "<%= rb.getString("most_frequent_incorrect_response")%>", "name": "mostIncorrectResponse", "targets": [9]},
                        {
                            "title": "<%= rb.getString("similar_problems")%>",
                            "name": "similarproblems",
                            "targets": [10],"render": function ( data, type, full, meta ) {
                                var gradeIdCode = full['problemStandardAndDescription'].split(":")[0];
                                var grade = gradeIdCode.split(".")[0];
                                var strandValue = "Mathematics."+gradeIdCode.split(".")[0]+"."+gradeIdCode.split(".")[1];
                                var standardValue = "Mathematics."+full['problemStandardAndDescription'].split(":")[0];
                                return '<a href="http://www.doe.mass.edu/MCAS/SEarch/default.aspx?YearCode=%25&GradeID='+grade+'&QuestionTypeCode=%25&QuestionSetID=All&FrameworkCode=Mathematics&Strand='+strandValue+'&Standard='+standardValue+'&KeywordVal=&ReportingCategoryCode=&ShowReportingCategory=&originalpage=1&allowCalculator=&page=1&mode=&answers=&questionanswer=&removeQuestionID=&unreleased=no&intro=no&FormSubmitted=yes" target="_blank" ><i class="fa fa-question" aria-hidden="true"></i></a>';

                            }
                        },{
                            "title": "<%= rb.getString("collective_effort_on_problem")%>",
                            "name": "collectiveEffortOnProblem",
                            "targets": [ 11 ],
                            "width": "10%",
                            'className': 'dt-body-center',
                            'render': function ( data, type, row ) {
                                var effortChartId = "effortChart"+row['problemId'];
                                var containerChart = "containerChart"+row['problemId'];
                                var legendChart = "legendChart"+row['problemId'];
                                //var dataContent = "<div id="+containerChart+" style='width:900px;height:300px;display:none'><div class='panel panel-default'><div class='panel-heading'>Effort Chart</div><div class='panel-body'><canvas width='800' height='150' id="+effortChartId+"></canvas></div><div class='panel-body' id='"+legendChart+"'></div></div></div>";
                                return "<i id='iconID"+row['problemId']+"' style='cursor:pointer;' class='fa fa-th' aria-hidden='true' onclick='loadEffortMap("+row['problemId']+",false);'></i>";
                            }
                        }
                    ];

                    var columDvalues = [
                        {width: "10%", data: "problemId"},
                        {width: "10%", data: "problemName"},
                        {width: "10%", data: "problemStandardAndDescription"},
                        {width: "10%", data: "noStudentsSeenProblem"},
                        {width: "10%", data: "getGetPercStudentsSolvedFirstTry"},
                        {width: "5%", data: "getGetPercStudentsSolvedSecondTry"},
                        {width: "10%", data: "percStudentsRepeated"},
                        {width: "10%", data: "percStudentsSkipped"},
                        {width: "10%", data: "percStudentsGaveUp"},
                        {width: "10%", data: "mostIncorrectResponse"},
                        {width: "5%", data: "similarproblems"},
                        {width: "5%", data: "collectiveEffortOnProblem"},
                    ];


                    var $perClusterChildtable = $($('#child_table_perCluster').html());
                                       
                    $perClusterChildtable.css('width', '100%');
                        var perClusterChildtable = $perClusterChildtable.DataTable({
                        data: perProblemSetLevelOneFullTemp,
                        destroy: true,
                        responsive: true,
                        columns: columDvalues,
                        "columnDefs": columNvalues,
                        "bPaginate": false,
                        "bFilter": false,
                        "bLengthChange": false,
                        rowReorder: false,
                        "bSort": true,
                        "drawCallback": function () {
                            $('a[rel=popoverPerProblem]').popover({
                                html: true,
                                trigger: 'hover',
                                placement: 'top',
                                container: 'body',
                                content: function () {
                                    return '<img src="' + $(this).data('img') + '" />';
                                }
                            });

                            $('a[rel=popoverstandard]').popover({
                                html: false,
                                trigger: 'hover',
                                placement: 'top',
                                container: 'body'
                            });

                            $('a[rel=popoverHeader]').popover({
                                container: 'body',
                                trigger: 'hover',
                                placement: 'top'
                            });

                        },
                        headerCallback: function headerCallback(thead, data, start, end, display) {
                            $(thead).find('th').eq(5).html('<%= rb.getString("pct_students_repeated_problem")%> &nbsp;&nbsp;<a rel="popoverHeader"  data-content="<%= rb.getString("students_repeated_problem_popover")%>"><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>');
                            $(thead).find('th').eq(6).html('<%= rb.getString("pct_students_skipped_problem")%> &nbsp;&nbsp;<a rel="popoverHeader" data-content="<%= rb.getString("students_skipped_problem_popover")%>"><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>');
                            $(thead).find('th').eq(7).html('<%= rb.getString("pct_students_gave_up")%> &nbsp;&nbsp;<a rel="popoverHeader" data-content="<%= rb.getString("students_gave_up_problem_popover")%>"><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>');
                            $(thead).find('th').eq(9).html('<%= rb.getString("see_similar_problems")%> &nbsp;&nbsp;<a rel="popoverHeader" data-content="<%= rb.getString("click_to_see_similar_problems")%>"><i class="fa fa-question-circle-o" aria-hidden="true"></i></a>');

                        }
                    });
                    perClusterReportTable.row(tr).child(perClusterChildtable.table().container()).show();
                    var oTable = $perClusterChildtable.dataTable();
                    oTable.fnSort( [ [0,'asc']] );
                }
            });
        }

    });

}
</script>