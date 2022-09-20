<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>
<% 
/** 
* Frank 	08-03-21	Initial version 



I think we are most likely to want to see the last 1,2,3 or 4 weeks (or month), but also "from the start"currentCohortId (or maybe "all semester").
That seems better than picking a date. So I would suggest  [ ] Show for prior _____ weeks, or [ ] Show from beginning.
Where the "[ ]" is a check box you select, and the "_______" is a multi-choice (or type in?) with numbers 1...10 

*/

 System.out.println("teacherToolsMain starting");
 ResourceBundle versions = null; 
 try {
	 versions = ResourceBundle.getBundle("Versions");
	 System.out.println("css_version=" + versions.getString("css_version"));
	 System.out.println(" js_version=" + versions.getString("js_version"));
 }
 catch (Exception e) {
	 System.out.println("teacherToolsMain ERROR");
// 	logger.error(e.getMessage());	
 }

 
 Locale loc = request.getLocale(); 
 String lang = loc.getLanguage();
 String country = loc.getCountry();

 System.out.println("locale set to:" + lang + "-" + country );	

 if (!lang.equals("es")) {
 	loc = new Locale("en","US");	
 }			

 
ResourceBundle rb = null;
ResourceBundle rwrb = null;
ResourceBundle rhrb = null;
try {
	rb = ResourceBundle.getBundle("MathSpring",loc);
	rwrb = ResourceBundle.getBundle("MSResearcherWorkbench",loc);
	rhrb = ResourceBundle.getBundle("MSResearcherHelp",loc);
}
catch (Exception e) {
//	logger.error(e.getMessage());	
}


%>

<!DOCTYPE HTML>
<html>

<head>
  <title><%= rwrb.getString("researcher_workbench") %></title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

    <!-- Datatables Css Files -->
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.datatables.net/1.10.20/css/dataTables.bootstrap4.min.css" rel="stylesheet" type="text/css">
    <link href="https://cdn.datatables.net/rowreorder/1.2.0/css/rowReorder.dataTables.min.css" rel="stylesheet"
          type="text/css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/jquery.bootstrapvalidator/0.5.0/css/bootstrapValidator.min.css"
          rel="stylesheet"/>
    <link href="https://cdn.datatables.net/select/1.2.1/css/select.dataTables.min.css" rel="stylesheet"
          type="text/css">
          
    <link href="https://cdn.datatables.net/1.10.20/css/jquery.dataTables.min.css" rel="stylesheet" 
    	type="text/css">
    
	<link href="https://cdn.datatables.net/fixedcolumns/3.3.0/css/fixedColumns.dataTables.min.css" rel="stylesheet"
          type="text/css">
    <script type="text/javascript" src="<c:url value="https://cdn.datatables.net/1.10.15/js/jquery.dataTables.min.js" />"></script>

    <script type="text/javascript" src="<c:url value="/js/jqplot2021/jquery.jqplot.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/plugins/jqplot.barRenderer.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/plugins/jqplot.categoryAxisRenderer.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/plugins/jqplot.canvasTextRenderer.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/plugins/jqplot.canvasAxisLabelRenderer.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/plugins/jqplot.canvasAxisTickRenderer.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/plugins/jqplot.dateAxisRenderer.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/plugins/jqplot.pieRenderer.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/plugins/jqplot.enhancedPieLegendRenderer.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/plugins/jqplot.highlighter.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/plugins/jqplot.pointLabels.js" />"></script>

  	<link href="${pageContext.request.contextPath}/css/researcherStyle.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet">
  	<link href="${pageContext.request.contextPath}/css/calendar.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/jqplot2021/jquery.jqplot.css" />

</head>
<body>


<style>

.li-disabled {
    pointer-events:none; 
    opacity:0.6;         
}

.li-enabled {
    opacity:1.0;      
}

.comment-style {
  width: 800px;
  word-wrap: break-word;
}

div.scroll {
    overflow-x: auto;
    overflow-y: hidden;
}

.tab-content {
  	background-color: lightblue;
}

.report_filters {
	color: #000000;
  	background-color: lightblue;
  	font-size: 16px;
	
}

#selections {
    min-height: 400px;
  	background: lightblue;
}

select {
    width: 200px;
    height: 30px;
    background-color: white;
    border-style: solid;
    border-left-width: 3px;
    border-left-color: #00DDDD;
    border-top: none;
    border-bottom: none;
    border-right: none;
    color: #141414 !important;
    font-size: 18px;
    font-weight: 200;
    padding-left: 6px;
    -webkit-appearance: none;
    -moz-appearance: none;
    appearance: none;

}
.myopt
{
    font-size: 16px;

}


thead,
tfoot {
    background-color: #3f87a6;
    color: #fff;
}

tbody {
    background-color: #e4f0f5;
}

caption {
    padding: 10px;
    caption-side: bottom;
}

table {
    padding: 40px;
    border-collapse: collapse;
    border: 2px solid rgb(200, 200, 200);
    letter-spacing: 1px;
    font-family: sans-serif;
    font-size: 20px;
}

th, td {
  padding: 4px;
}

</style>





<script>


function JSON2CSV(objArray) {
    var array = typeof objArray != 'object' ? JSON.parse(objArray) : objArray;
    var str = '';
    var line = '';

    if ($("#labels").is(':checked')) {
        var head = array[0];
        if ($("#quote").is(':checked')) {
            for (var index in array[0]) {
                var value = index + "";
                line += '"' + value.replace(/"/g, '""') + '",';
            }
        } else {
            for (var index in array[0]) {
                line += index + ',';
            }
        }

        line = line.slice(0, -1);
        str += line + '\r\n';
    }

    for (var i = 0; i < array.length; i++) {
        var line = '';

        if ($("#quote").is(':checked')) {
            for (var index in array[i]) {
                var value = array[i][index] + "";
                line += '"' + value.replace(/"/g, '""') + '",';
            }
        } else {
            for (var index in array[i]) {
                line += array[i][index] + ',';
            }
        }

        line = line.slice(0, -1);
        str += line + '\r\n';
    }
    return str;
}

/*
var cohortListQuery = select distinct researchcohortid from teacher_map_cohort;
var cohortTeacherListQuery = select * from teacher t, teacher_map_cohort tmc where t.ID = tmc.teacherid;
var cohortClassListQuery = select * from class c, teacher t, teacher_map_cohort tmc where t.ID = ? and t.ID = tmc.teacherid and c.teacherId = t.ID;
*/

// Global variables
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

 var pgContext = "${pageContext.request.contextPath}";

 
var defaultColors = [ "#4bb2c5", "#EAA228", "#c5b47f", "#579575", "#839557", "#958c12", "#953579", "#4b5de4", "#d8b83f", "#ff5800", "#0085cc", "#c747a3", "#cddf54", "#FBD178", "#26B4E3", "#bd70c7"];

var cohortsArr = [];
var allCohortsArr = [];
var currentCohortId = 0;
var currentCohortIndex = 0;
var currentCohortWeeks = {};
var cohortWeeks = [];
var currentCohortDateArr = [];

var noEndDate = "2000-01-01 00:00:00.0";

var currentTeachersArr = [];

var trendDate = '7';
var trendUnit = "week";
var trendNumberOfUnits = "1";

var currentTeacher = new Object();
var currentClass = "";

var currentWeek = 1;
var finalWeek = 1;

var filterOne = "";
var headers = "";

var plot2 = null;
var plot2b = null;
var plot2c = null;
var plot3 = null;
var plot3a = null;
var plot3b = null;

var plot3c0 = null;
var plot3c1 = null;
var plot3c2 = null;
var plot3c3 = null;
var plot3c4 = null;
var plot3c5 = null;
var plot3c6 = null;
var plot3c7 = null;
var plot3c8 = null;
var plot3c9 = null;
var plot3c10 = null;
var plot3c11 = null;
var plot3c12 = null;
var plot3c13 = null;
var plot3c14 = null;
var plot3c15 = null;
var plot3c16 = null;
var plot3c17 = null;
var plot3c18 = null;
var plot3c19 = null;

var plots3c = [plot3c0,plot3c1,plot3c2,plot3c3];

var plot3cf0 = null;
var plots3cf = [plot3cf0];

var plot3d = null;
var plot3e = null;

var plot_tcp = null;
var plot_tp = null;
var plot_tcs = null;
var plot5 = null;
var p1_plot6 = null;
var p2_plot6 = null;


function addDays(date, days) {
	  var result = new Date(date);
	  result.setDate(result.getDate() + days);
	  return result;
	}

function launchReportCard() {

	var isValid = true;
	
	if (currentTeacher.id == null) {
		alert('<%= rwrb.getString("must_select_teacher") %>');
		isValid = false;
	}
	if ((currentClass == null) || (currentClass == "")) {
		alert('<%= rwrb.getString("must_select_class") %>');
		isValid = false;
	}
	if (isValid) {
		var a_href = '${pageContext.request.contextPath}';
		a_href = a_href + "/tt/tt/researcherViewClassReportCard?teacherId=";
		a_href = a_href + currentTeacher.id;
		a_href = a_href + "&classId=";
		a_href = a_href + currentClass;
		document.getElementById("reportCardLink").href = a_href;
		document.getElementById("reportCardLink").click();
	}
}


function launchCohortSlicesHelp() {

	displayHelpPopup('cohort_slices','Weekly Slices');
	
}

function launchCreateCohortHelp() {
	
//		var a_href = '${pageContext.request.contextPath}';
//		a_href = a_href + "/img/video_help_add_cohort.mp4";
//		document.getElementById("createCohortHelpLink").href = a_href;
//		document.getElementById("createCohortHelpLink").click();
		
	
	displayHelpPopup('cohort_admin','Admin Commands');
}

function displayHelpPopup(helpTopic, helpHdr) {
	

	document.getElementById("cohort_help_hdr").innerHTML = "Cohort Help - " + helpHdr;

	var helpDivContent = "";

	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortHelp",
        data : {
            helpTopic: helpTopic,
            lang: loc,
            filter: ''
        },
        success : function(data) {
        	if (data) {

            	var jsonData = $.parseJSON(data);	
               	

                for (var i = 0; i < jsonData.length; i++) {
            		helpDivContent += "<div style='overflow-x: auto;overflow-y: auto; display: block;'>";
            		helpDivContent += "<label>" + jsonData[i].label + ":</label>";
            		helpDivContent += "<br>";
            		helpDivContent += "<p>" + jsonData[i].paragraph + "<p>";
            		helpDivContent += "</div";
                }
        		document.getElementById("cohortHelpPopupBody").innerHTML = helpDivContent;
        		$('#cohortHelpModalPopup').modal('show');         			

        	}
        	else {
        		alert("error");
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }

    });
}


function refreshLocalData() {
	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/researchServices",
        data : {
            cohortId: 0,
            teacherId: 0,
            classId: 0,
            reportType: 'getCohortTeachersClasses',
            lang: 'en_US',
            filter: '~'
        },
        success : function(data) {
        	allCohortsArr = $.parseJSON(data);
        	
        }
    });
    
	document.getElementById("chart2_div").style.visibility = 'hidden';
	document.getElementById("chart2b_div").style.visibility = 'hidden';
	document.getElementById("chart2c_div").style.visibility = 'hidden';
	document.getElementById("chart3_div").style.visibility = 'hidden';
	document.getElementById("chart3a_div").style.visibility = 'hidden';
	document.getElementById("chart3b_div").style.visibility = 'hidden';
	document.getElementById("chart3c_div1").style.visibility = 'hidden';
	document.getElementById("chart3c_div2").style.visibility = 'hidden';
	document.getElementById("chart5_div").style.visibility = 'hidden';


	document.getElementById("liveDashboardProblemPane").style.visibility = 'hidden';
	document.getElementById("liveDashboardEffortPane").style.visibility = 'hidden';
	
}

$(document).ready(function () {
	
	headers = changeTeacherActivitiesReportHeaderAccordingToLanguage();

    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/researchServices",
        data : {
            cohortId: 0,
            teacherId: 0,
            classId: 0,
            reportType: 'getCohortTeachersClasses',
            lang: 'en_US',
            filter: '~'
        },
        success : function(data) {
        	allCohortsArr = $.parseJSON(data);
        	
        }
    });
    
	if (languageSet == 'es') {
	    
	    perTeacherReport  =  $('#perTeacherReport').DataTable({
	        data: [],
	        destroy: true,
	        columns: [
	            { title: headers['tstamp'] },
	            { title: headers['tid'] },
	            { title: headers['tname']  },
	            { title: headers['uname']  },
	            { title: headers['action']  },
	            { title: headers['classId']  },
	            { title: headers['activityName']  },
	        ],
            "bPaginate": true,
            "pageLength": 16,
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
	                "visible": true

	            },
	            {
	                "width": "5%",
	                "targets": [ 1 ],
	                "visible": false

	            },
	            {
	                "width": "5%",
	                "targets": [ 2 ],
	                "visible": false

	            },
	            {
	                "width": "5%",
	                "targets": [ 3 ],
	                "visible": false

	            },
	            {
	                "width": "5%",
	                "targets": [ 4 ],
	                "visible": true

	            },
	            {
	                "width": "5%",
	                "targets": [ 5 ],
	                "visible": true

	            },            	        	
	            {
	                "width": "5%",
	                "targets": [ 6 ],
	                "visible": true

	            }            	        	
	        ]
	    }    
	    );
	}
	else {
	    perTeacherReport  =  $('#perTeacherReport').DataTable({
	        data: [],
	        destroy: true,
	        columns: [
	            { title: headers['tstamp'] },
	            { title: headers['tid'] },
	            { title: headers['tname']  },
	            { title: headers['uname']  },
	         	{ title: headers['action']  },
	            { title: headers['classId']  },
	         	{ title: headers['activityName']  },
	        ],
            "bPaginate": true,
            "pageLength": 16,
	        "bFilter": false,
	        "bLengthChange": false,
	        rowReorder: false,                
	        "scrollX": true,
	        "bSort" : false,
	        "columnDefs": [
	            {
	                "width": "5%",
	                "targets": [ 0 ],
	                "visible": true

	            },
	            {
	                "width": "5%",
	                "targets": [ 1 ],
	                "visible": false

	            },
	            {
	                "width": "5%",
	                "targets": [ 2 ],
	                "visible": false

	            },
	            {
	                "width": "5%",
	                "targets": [ 3 ],
	                "visible": false

	            },
	            {
	                "width": "5%",
	                "targets": [ 4 ],
	                "visible": true

	            },
	            {
	                "width": "5%",
	                "targets": [ 5 ],
	                "visible": true

	            },                   {
	                "width": "5%",
	                "targets": [ 6 ],
	                "visible": true

	            }            	        	
	        ]
	    }    
	    );

	}
});

function openSettingsPane() {

    clearSettings();
}
	
	
	
function clearSettings() {
	refreshLocalData();
	var cohortsDiv = '<label for="cohortList">Select cohort</label> <select name="cohortList" id="cohortList" class="form-control selectpicker" onblur="handleCohortSelect(event);" data-show-subtext="true" data-live-search="true" size="8" style="width: 270px;"></select>';  				
	document.getElementById('cohortSelect').innerHTML = cohortsDiv;
	currentCohortId = -1;
	
 	var teachersDiv = '<label for="teacherList"><%= rwrb.getString("select_teacher") %></label>';  
    document.getElementById('teacherSelect').innerHTML = teachersDiv;

 	var classesDiv = '<label for="classList"><%= rwrb.getString("select_class") %></label>';  
    document.getElementById('classSelect').innerHTML = classesDiv;

    document.getElementById('chartUsername').innerHTML = '<%= rwrb.getString("please_select_teacher") %>';

    $("#li-population").removeClass("li-enabled");
    $("#li-population").addClass("li-disabled");
    $("#li-ttActivityReports").removeClass("li-enabled");
    $("#li-ttActivityReports").addClass("li-disabled");
    $("#li-classroomTrends").removeClass("li-enabled");
    $("#li-classroomTrends").addClass("li-disabled");
    $("#li-classroomDashboard").removeClass("li-enabled");
    $("#li-classroomDashboard").addClass("li-disabled");
    $("#li-reportCard").removeClass("li-enabled");
    $("#li-reportCard").addClass("li-disabled");
   
	
    showCohorts();
}

function clearTeachers() {

	var teachersDiv = '<label for="teacherList"><%= rwrb.getString("select_teacher") %></label>';  
    document.getElementById('teacherSelect').innerHTML = teachersDiv;

 	var classesDiv = '<label for="classList"><%= rwrb.getString("select_class") %></label>';  
    document.getElementById('classSelect').innerHTML = classesDiv;

    document.getElementById('chartUsername').innerHTML = '<%= rwrb.getString("please_select_teacher") %>';
    document.getElementById('trendsTeacherName').innerHTML = "";
    document.getElementById('dashboardTeacherName').innerHTML = "";
    
    document.getElementById('rpt5PopulateSelect').style.visibility = 'hidden';

}


function gotoSettingsPane() {

 	
	var teachersDiv = '<label for="teacherList"><%= rwrb.getString("select_teacher") %></label>';  
    document.getElementById('teacherSelect').innerHTML = teachersDiv;

 	var classesDiv = '<label for="classList"><%= rwrb.getString("select_class") %></label>';  
    document.getElementById('classSelect').innerHTML = classesDiv;

    document.getElementById('chartUsername').innerHTML = '<%= rwrb.getString("please_select_teacher") %>';
    document.getElementById('trendsTeacherName').innerHTML = "";

    $("#li-population").removeClass("li-enabled");
    $("#li-population").addClass("li-disabled");
    $("#li-ttActivityReports").removeClass("li-enabled");
    $("#li-ttActivityReports").addClass("li-disabled");
    $("#li-classroomTrends").removeClass("li-enabled");
    $("#li-classroomTrends").addClass("li-disabled");
    $("#li-classroomDashboard").removeClass("li-enabled");
    $("#li-classroomDashboard").addClass("li-disabled");
    $("#li-reportCard").removeClass("li-enabled");
    $("#li-reportCard").addClass("li-disabled");
    
	showCohorts();
}


function validatePriorWeeks(tid) {
	var temp = document.getElementById(tid).value;
	if (temp.length > 0) {
		if (isNaN(temp)) {
			document.getElementById(tid).value = "";
			alert("must enter a number");
		}
	}
}

function showCohorts() {
	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohorts",
        data : {
            reportType: 'getCohorts',
            lang: 'en_US',
            filter: '~'
        },
        success : function(data) {
        	cohortsArr = $.parseJSON(data);
        	
			var cohortIdCheckedIndex = -1;
        	
        	var cohortsDiv = '<label for="cohortList">Select cohort</label> <select name="cohortList" id="cohortList" class="form-control selectpicker" onblur="handleCohortSelect(event);" data-show-subtext="true" data-live-search="true" size="5" style="width: 270px;">';  
        	for (var i=0;i<cohortsArr.length;i++) {
				var id = "" + cohortsArr[i].cohortId;
				var name = "" + cohortsArr[i].cohortName; 
				var cohortStr = "";
				if (currentCohortId == cohortsArr[i].cohortId) {
					cohortIdCheckedIndex = i;
				}
           		cohortStr = '<option id="cohort' + id + '" value="' + i + "~" + id + '" onblur="clearTeachers();">' + name + '</option>';
               	cohortsDiv = cohortsDiv + cohortStr;
            }
		    cohortsDiv = cohortsDiv + '</select>';  				
           	document.getElementById('cohortSelect').innerHTML = cohortsDiv;
           	if (cohortIdCheckedIndex >= 0) {
           		document.getElementById('cohortList').options[cohortIdCheckedIndex].selected = true;  
           	}
        }
    });
}   
    
var tempCohortDateArr = [];

function buildCurrentCohortDateArr() {
	
	tempCohortDateArr = [];
	tempCohortDateArr.push("");
	var startDate = new Date(cohortsArr[currentCohortIndex].cohortStartdate);
	var startDateStr = startDate.toLocaleDateString();
	tempCohortDateArr.push(startDateStr);
	
	for (var i = 1; i < 52; i++) {
		var tdate = new Date(addDays(startDate,(7*i)));
		var dateStr = tdate.toLocaleDateString();
		tempCohortDateArr.push(dateStr);
	}
	return tempCohortDateArr;
	
}
  


function handleCohortSelect(event) {

    var selectElement = event.target;
    var value = selectElement.value;
    var splitValue = value.split("~");
    
    var currentWeekRaw = 1;
    currentCohortIndex = Number(splitValue[0]);
    currentCohortId = Number(splitValue[1]);
	var msToday = new Date(Date.now());        	
   	var msStartDate = new Date(cohortsArr[currentCohortIndex].cohortStartdate);
   	var intStartDate = msStartDate.getTime();
   	var intToday = msToday.getTime();
   	currentWeekRaw = ((intToday - intStartDate)  / 7);
   	currentWeekRaw = (currentWeekRaw / (1000 * 3600 * 24));
   	currentWeek = Math.ceil(currentWeekRaw);

	var currentWeekHdr = "";
   	
   	if (currentWeek < 1) {
   		currentWeek = 0;
   		currentWeekHdr = " has not started yet."
   	}
   	else {
   		var testEndDate = cohortsArr[currentCohortIndex].cohortEnddate;
	   	if ((typeof testEndDate === 'undefined') || (cohortsArr[currentCohortIndex].cohortEnddate == noEndDate)) {
	   		finalWeek = -1;
	   	}
	   	else {
		   	var msEndDate = new Date(cohortsArr[currentCohortIndex].cohortEnddate);
		   	finalWeekRaw = (msEndDate - msStartDate)  / 7;
		   	finalWeekRaw = (finalWeekRaw / (1000 * 3600 * 24));
		   	finalWeek = Math.ceil(finalWeekRaw);
		   	currentWeek = finalWeek;
	   	}
	   	var msEndDate = new Date(cohortsArr[currentCohortIndex].cohortEnddate);
		var msEndDateStr = msEndDate.toLocaleDateString();
	   	
	   	if ((typeof testEndDate === 'undefined') || (msEndDateStr == "1/1/2000")) {
	   	   	var currentWeekHdr = " (Week # " + currentWeek + ")";
	   	}
	   	else {
	   		currentWeekHdr = " - ended " + msEndDateStr;   			   		
	   	}
   	}

   	console.log("currentWeek = " + currentWeek);
   	console.log("finalWeek = " + finalWeek);
   	
	document.getElementById('chartsCohortName').innerHTML = "<h3>" + cohortsArr[currentCohortIndex].cohortName + currentWeekHdr + "</h3>";
	document.getElementById('trendsCohortName').innerHTML = "<h3>" + cohortsArr[currentCohortIndex].cohortName + currentWeekHdr + "</h3>";
	document.getElementById('adminCohortName').innerHTML = "<h3>" + cohortsArr[currentCohortIndex].cohortName + currentWeekHdr + "</h3>";
	document.getElementById('populationCohortName').innerHTML = "<h3>" + cohortsArr[currentCohortIndex].cohortName + currentWeekHdr + "</h3>";
	document.getElementById('dashboardCohortName').innerHTML = "<h3>" + cohortsArr[currentCohortIndex].cohortName + currentWeekHdr + "</h3>";
      
	$(".how-long li").slice(3).addClass('change-color');
	
    $("#li-population").removeClass("li-disabled");
    $("#li-population").addClass("li-enabled");
    $("#tpsa-panel").hide();	
    showTeachers();

    currentCohortDateArr = [];
   	currentCohortDateArr = buildCurrentCohortDateArr();

    
}



function showTeachers() {
	
    currentTeacher.id = "0";
    currentTeacher.username = "";
//    if (allCohortsArr.length < currentCohortIndex) {
		var teacherArr = allCohortsArr[currentCohortIndex];
		
	 	var teachersDiv = '<label for="teacherList"><%= rwrb.getString("select_teacher") %></label> <select name="teacherList" id="teacherList" class="form-control selectpicker" onblur="handleTeacherSelect(event);" data-show-subtext="true" data-live-search="true" size="5" style="width: 270px; border: solid">';  
	
	 	var prevname = "";
	    for (var i=0;i<teacherArr.cohortArr.length;i++) {
			var name = "" + teacherArr.cohortArr[i].userName;
			if (name != prevname) {
				var lname = "" + teacherArr.cohortArr[i].lname;
				var id = "" + teacherArr.cohortArr[i].ID;
	        	var teacherStr = '<option id="teacher' + id + '" value="' + id + '~' + name + '">' + lname + " [" +  name + "]" + '</option>';
	        	teachersDiv = teachersDiv + teacherStr;
	        	prevname = name;
			}
		}
	   	teachersDiv = teachersDiv + '</select>';  				
	    document.getElementById('teacherSelect').innerHTML = teachersDiv;
	    document.getElementById('rpt5PopulateSelect').style.visibility = 'hidden';
//    }        	
}

function handleTeacherSelect(event) {

	var selectedTeachers = [];
	for (var option of document.getElementById('teacherList').options) {
		if (option.selected) {
			selectedTeachers.push(option.value);
		}
	}
	
	
	var selectElement = event.target;
    var value = "" + selectedTeachers[0];
    var splitter = value.split("~");
    currentTeacher.id = splitter[0];
    currentTeacher.username = splitter[1];
    document.getElementById('chartUsername').innerHTML = "<h3>" + currentTeacher.username + "</h3>";
    document.getElementById('trendsTeacherName').innerHTML = "<h3>" + currentTeacher.username + "</h3>";
    document.getElementById('rpt5PopulateSelect').style.visibility = 'visible';
    
    showClasses();
    
    $("#li-ttActivityReports").removeClass("li-disabled");
    $("#li-ttActivityReports").addClass("li-enabled");
    $("#li-classroomTrends").removeClass("li-disabled");
    $("#li-classroomTrends").addClass("li-enabled");
    $("#li-classroomDashboard").removeClass("li-disabled");
    $("#li-classroomDashboard").addClass("li-enabled");
    $("#li-reportCard").removeClass("li-disabled");
    $("#li-reportCard").addClass("li-enabled");

	document.getElementById("liveDashboardProblemPane").style.visibility = 'hidden';
	document.getElementById("liveDashboardEffortPane").style.visibility = 'hidden';
    
}

function showClasses() {
	
//    if (allCohortsArr.length < currentCohortIndex) {
		var teacherArr = allCohortsArr[currentCohortIndex];    	
	 	var classesDiv = '<label for="classList"><%= rwrb.getString("select_class") %></label> <select name="classList" id="classList" class="form-control selectpicker" onblur="handleClassSelect(event);" data-show-subtext="true" data-live-search="true" size="5" style="width: 270px;; border: solid">';  
	
	 	var prevClassName = "";
	    for (var i=0;i<teacherArr.cohortArr.length;i++) {
			var userName = "" + teacherArr.cohortArr[i].userName;
			if (userName === currentTeacher.username) {
				var className = "" + teacherArr.cohortArr[i].name;
				if (className != prevClassName) {
					var classId = "" + teacherArr.cohortArr[i].id;
	    	    	var classStr = '<option id="class' + classId + '" value="' + classId + '">' + className + '</option>';
	    	    	classesDiv = classesDiv + classStr;
	        		prevClassName = className;
				}
			}
	    }
//	}
    classesDiv = classesDiv + '</select>';  				
    document.getElementById('classSelect').innerHTML = classesDiv;
//    document.getElementById('classSelect').style.visibility = "visible";
        	
    console.log(classesDiv);
}



function handleClassSelect(event) {

    var selectElement = event.target;
    currentClass = Number(selectElement.value);

    document.getElementById('dashboardTeacherName').innerHTML =  "<h3>" + currentTeacher.username + " ClassId:" + selectElement.value + "</h3>";

}



function editCohortTeachers(cmd) {

	var filter = "";

	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
	}
	if (cmd === "remove") {
		var tname = prompt("Enter teacher last name;");
		var id = prompt("Enter teacher id");
		var conf = confirm("Are you sure you want to remove teacher: " + tname + " with id: " + id + " from " + cohortsArr[currentCohortIndex].cohortName);
		if (conf) {
			filter = "remove" + "~" +  id + "~" + tname;
			adminCohortTeachers(filter);		
		}
	}
	if (cmd === "add") {
		var tname = prompt("Enter teacher last name;");
		var id = prompt("Enter teacher id");
		filter = "add" + "~" +  id + "~" + tname;
		adminCohortTeachers(filter);		
	}
}

function editCohortClasses(cmd) {

	var filter = "";
	if (cmd === "remove") {
		var tname = prompt("Enter teacher last name");
		var id = prompt("Enter class id");
		var conf = confirm("Are you sure you want to remove class id: " + id + " of teacher " + tname + " from " + cohortsArr[currentCohortIndex].cohortName);
		if (conf) {
			filter = "remove" + "~" +  id + "~" + tname;
			adminCohortClasses(filter);				
		}
	}
	if (cmd === "add") {
		var tname = prompt("Enter teacher last name");
		var id = prompt("Enter class id");
		filter = "add" + "~" +  id + "~" + tname;
		adminCohortClasses(filter);				
	}
}

function addCohortForm() {
	

	$('#addCohortFormModalPopup').modal('hide');
    
    var jsonData = null;
    var cols = [];

   
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/cohortAdmin",
        data : {
            cohortId: currentCohortId,
            command: 'getNewCohortId',
            lang: loc,
            filter: "0" 
       
        },
        success : function(data) {
        	if (data) {
               	
            	jsonData = $.parseJSON(data);	
            	var newCohortId  = jsonData.newCohortId;
            	document.getElementById("add_cohort_hdr").innerHTML = "Cohort Id# " + newCohortId;            	

            	document.getElementById("newCohortId").value = newCohortId;
            	document.getElementById("cohortName").value = "";
            	document.getElementById("cohortSchoolYear").value = "";
            	var cohortStartDate  = jsonData.cohortStartDate;
           		document.getElementById("cohortStartDate").value = "";

                $('#addCohortFormModalPopup').modal('show');
            
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}

        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });

}


function editCohortForm() {
	

	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
		return;
	}

	$('#editCohortFormModalPopup').modal('hide');
    
    var jsonData = null;
    var cols = [];

   
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/cohortAdmin",
        data : {
            cohortId: currentCohortId,
            command: 'getCohortInfo',
            lang: loc,
            filter: currentCohortId 
       
        },
        success : function(data) {
        	if (data) {
               	
            	jsonData = $.parseJSON(data);	

            	document.getElementById("edit_cohort_hdr").innerHTML = "Cohort Id# " + currentCohortId;            	

            	var cohortName  = jsonData.cohortName;
            	document.getElementById("cohortName").value = cohortName;
            	var cohortSchoolYear  = jsonData.cohortSchoolYear;
            	document.getElementById("cohortSchoolYear").value = cohortSchoolYear;
            	var cohortStartDate  = jsonData.cohortStartDate;
            	if (jsonData.cohortStartDate == "01/01/2000") {
            		document.getElementById("cohortStartDate").value = "";
            	}
            	else {
                	document.getElementById("cohortStartDate").value = cohortStartDate;            		
            	}

            	var cohortEndDate  = jsonData.cohortEndDate;
            	if (jsonData.cohortEndDate == "01/01/2000") {
            		document.getElementById("cohortEndDate").value = "";
            	}
            	else {
                	document.getElementById("cohortEndDate").value = cohortEndDate;            		
            	}

                $('#editCohortFormModalPopup').modal('show');
            
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}

        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });

}


function changeTeacherActivitiesReportHeaderAccordingToLanguage(){
	var languagePreference = window.navigator.language;
	var languageSet = "en";
	if (languagePreference.includes("en")) {
		languageSet = "en"
	} else if (languagePreference.includes("es")) {
		languageSet = "es"
	}
	if (languageSet == 'es') {
		var header = {'tstamp':  'Timestamp','tid':  'Numero Identificador del maestro','tname': 'Nombre del  meastro','uname':  'Nombre de usuario','action': 'Acción','classId': 'Código de clase',  'activityName': 'Actividad'};
		return header;
	}else{
	 	var header = {'tstamp':  'Timestamp','tid':  'Teacher ID','tname': 'Teacher Name','uname':  'Username','action': 'Action','classId': 'Class Id', 'activityName': 'Activity'};
	 	return header;
	}
}


function getFilterOne() {

	$('#calendarModalPopup').modal('hide');
	
	document.getElementById("daysFilterOne").value = "";
	
	var d1 = parseInt(document.getElementById("selectDay_r1_cal2").value);
	var d2 =  parseInt(document.getElementById("selectDay_r1_cal1").value);

	var m1 = parseInt(document.getElementById("month_r1_cal2").value) + 1;
	var m2 =  parseInt(document.getElementById("month_r1_cal1").value) + 1;
	if ((d1 > 0) && (d2 > 0)) {
		$('#calendarModalPopupOne').modal('hide');

		var fromDate = m1 + "/" + document.getElementById("selectDay_r1_cal2").value + "/" +  document.getElementById("year_r1_cal2").value;
		var toDate = m2 + "/" + document.getElementById("selectDay_r1_cal1").value + "/" + document.getElementById("year_r1_cal1").value;

		if (languageSet == "es") {
			fromDate = document.getElementById("selectDay_r1_cal2").value + "/" +  m1 + "/" + document.getElementById("year_r1_cal2").value;
			toDate = document.getElementById("selectDay_r1_cal1").value + "/" + m2 + "/" + document.getElementById("year_r1_cal1").value;
		}
		
		var older = Date.parse(fromDate);
		var newer = Date.parse(toDate);
		if (newer < older) {
			var temp = fromDate;
			fromDate = toDate;
			toDate = temp;
		}	

		document.getElementById("daysFilterOne").value = fromDate + " thru " + toDate;
		document.getElementById("chartDateRange").value = fromDate + " thru " + toDate;
		filterOne = "~" + document.getElementById("daysFilterOne").value + "~" + "Y";
	
	}
	else {
		if ((d1 + d2) == 0) {
			document.getElementById("daysFilterOne").value = "";
			filterOne = "~" + "" + "~" + "Y";			
		}
		else {
			alert("<%= rb.getString("must_select_a_day_from_each_calendar") %>");
		}
	}
	
}



//$('#showReportBtn1').on('click', function ()  {
//$('#showReportBtn1').on('show.bs.collapse', function ()  {


function showReport1() {

	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'perTeacherReport',
            lang: loc,
            filter: currentTeacher.id
        },
        success : function(data) {
        	if (data) {
            	var jsonData = $.parseJSON(data);
                eachTeacherData = jsonData.levelOneData;
	            perTeacherReport.clear().draw();
    	        perTeacherReport.rows.add(jsonData.levelOneData).draw();
        	    perTeacherReport.columns.adjust().draw();            	    
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
    
}

/*
function rptTeacherClassesCount() {

	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
		return;
	}

	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'teacherClassesCount',
            lang: loc,
            filter: ''
        },
        success : function(data) {
        	if (data) {
            	var jsonData = $.parseJSON(data);
            	var ticks = [];
            	for (var i=0;i<10;i = i + 1) {
            		ticks.push(jsonData[i].username);
            	}
            	var series = [];
        		var row = [];
            	for (var i=0;i<10;i = i + 1) {
            	
            		var element = [];
            		element.push(jsonData[i].logins);
            		element.push(i+1);
            		row.push(element);
            	}
        		series.push(row);
                // var ticks = ['Paul', 'John', 'George', 'Ringo'];

                var plot2b = $.jqplot('chart2b_canvas', series, {
                    seriesDefaults: {
                        renderer:$.jqplot.BarRenderer,
                        // Show point labels to the right ('e'ast) of each bar.
                        // edgeTolerance of -15 allows labels flow outside the grid
                        // up to 15 pixels.  If they flow out more than that, they 
                        // will be hidden.
					    seriesDefaults:{
					        renderer:$.jqplot.BarRenderer,
				            varyBarColor: true
					    },                        
					    pointLabels: { show: true, location: 'e', edgeTolerance: -15 },
                        // Rotate the bar shadow as if bar is lit from top right.
                        shadowAngle: 135,
                        // Here's where we tell the chart it is oriented horizontally.
                        rendererOptions: {
                            barDirection: 'horizontal',
                            varyBarColor: true
                        }
                    },
            	    series:[
            	        {label:'<%= rwrb.getString("logins") %>'}
            	    ],
                    axes: {
                        yaxis: {
							renderer: $.jqplot.CategoryAxisRenderer,
							ticks: ticks            
             			}
                    },


                });

        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });

}
*/
function showReport2() {

	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
		return;
	}
	if (plot2 != null) {
		plot2.destroy();
		plot2 = null;
	}	


    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'teacherActivityMetrics',
            lang: loc,
            filter: ''
        },
        success : function(data) {
        	if (data) {
            	var jsonData = $.parseJSON(data);
            	console.log(data);
            	var ticks = [];
            	for (var i=0;i<jsonData.length;i = i + 1) {
            		ticks.push(jsonData[i].username);
            	}
            	var theSeries = [];
            	var row1 = [];
            	var row2 = [];
            	var row3 = [];
             
            	for (var i=0;i<jsonData.length;i = i + 1) {            	
            		var element1 = [];
            		var element2 = [];
            		var element3 = [];
            		element1.push(jsonData[i].logouts);
            		element1.push(i+1);
            		row1.push(element1);
            		element2.push(jsonData[i].actions);
            		element2.push(i+1);
            		row2.push(element2);
            		var element3 = [];
            		element3.push(jsonData[i].logins);
            		element3.push(i+1);
            		row3.push(element3);
            	}
            	theSeries.push(row1);
            	theSeries.push(row2);
            	theSeries.push(row3);

        		document.getElementById("chart2_div").style.visibility = 'visible';
            	plot2 = $.jqplot('chart2_canvas', theSeries, {

                    seriesDefaults: {
                        renderer:$.jqplot.BarRenderer,
                        // Show point labels to the right ('e'ast) of each bar.
                        // edgeTolerance of -15 allows labels flow outside the grid
                        // up to 15 pixels.  If they flow out more than that, they 
                        // will be hidden.
                        pointLabels: { show: true, location: 'e', edgeTolerance: -15 },
                        // Rotate the bar shadow as if bar is lit from top right.
                        shadowAngle: 135,
                        // Here's where we tell the chart it is oriented horizontally.
                        rendererOptions: {
                            barDirection: 'horizontal'
                        }
                    },
            	    series:[
            	        {label:'<%= rwrb.getString("logouts") %>'},
            	        {label:'<%= rwrb.getString("actions") %>'},
            	        {label:'<%= rwrb.getString("logins") %>'}
            	    ],
            	    legend: {
            	        show: true,
            	        placement: 'outsideGrid'
            	    },                

                    axes: {
                        yaxis: {
                        	renderer: $.jqplot.CategoryAxisRenderer,
                        	ticks: ticks            
             			}
                    }
            	});
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
}

function showReport2b() {

	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
		return;
	}
	if (plot2b != null) {
		plot2b.destroy();
		plot2b = null;
	}	
	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'teacherLoginCount',
            lang: loc,
            filter: ''
        },
        success : function(data) {
        	if (data) {
            	var jsonData = $.parseJSON(data);
            	var ticks = [];
            	for (var i=0;i<jsonData.length;i = i + 1) {
            		ticks.push(jsonData[i].username);
            	}
            	var s1 = [];

            	for (var i=0;i<jsonData.length;i = i + 1) {
            		s1.push(jsonData[i].logins);
            	}
                // var ticks = ['Paul', 'John', 'George', 'Ringo'];

           		document.getElementById("chart2b_div").style.visibility = 'visible';

                plot2b = $.jqplot('chart2b_canvas', [s1], {
            	    seriesDefaults:{
            	        renderer:$.jqplot.BarRenderer,
            	        rendererOptions: {
            	        	fillToZero: true,
            	        	varyBarColor: true	        	
            	        }
                        
            	    },
            	    axes: {
                        xaxis: {
            	            renderer: $.jqplot.CategoryAxisRenderer,
            	            ticks: ticks,
            	            tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                        	tickOptions: { 
                            	angle: 15
                        	},
                        	label: '<%= rwrb.getString("teachers") %>'
             			},
	                    yaxis: {
			 	            pad: 1.05,
            				min: 0,  
            				tickInterval: 10, 
            				tickOptions: { 
                				formatString: '%d'
            				}, 
            				label: '<%= rwrb.getString("number_of_logins") %>'
               			}
                    },


                });

        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
}

function showReport2c() {


	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
		return;
	}
	if (plot2c != null) {
		plot2c.destroy();
		plot2c = null;
	}	

    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'teacherDaysSinceLogin',
            lang: loc,
            filter: ''
        },
        success : function(data) {
        	if (data) {
        		if (data === "Not Implemented") {
        			alert(data);	
        		}
        		else {
            	var jsonData = $.parseJSON(data);
        		var s1 = [];
        		var ticks = [];
        		
            	for (var i=0;i<jsonData.length;i = i + 1) {
            		ticks.push(jsonData[i].username);
            	}
//            	var series = [];

            	for (var i=0;i<jsonData.length;i = i + 1) {
            	

            		var currentDate = new Date();
            		var sinceDateStr = jsonData[i].since;
            		var sinceDate = new Date(sinceDateStr);            		 
            		// calculate the time difference of two dates
            		var Difference_In_Time = currentDate.getTime() - sinceDate.getTime();            		  
            		// calculate the no. of days between two dates
            		var Difference_In_Days = Difference_In_Time / (1000 * 3600 * 24);
            		var diffStr = "" + Difference_In_Days;
            		
            		var splitter = diffStr.split(".");
            		var diffNbr = Number(splitter[0]);
            		s1.push(diffNbr)
            		//series.push(element);
            	}

        		document.getElementById("chart2c_div").style.visibility = 'visible';

            	plot2c = $.jqplot('chart2c_canvas', [s1], {
            	    seriesDefaults:{
            	        renderer:$.jqplot.BarRenderer,
            	        rendererOptions: {
            	        	fillToZero: true,
            	        	varyBarColor: true	        	
            	        }
                        
            	    },
                    axes: {
                        xaxis: {
            	            renderer: $.jqplot.CategoryAxisRenderer,
            	            ticks: ticks,
            	            tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                        	tickOptions: { 
                            	angle: 15
                        	},
                        	label: '<%= rwrb.getString("teachers") %>'

                        	
//                        	renderer: $.jqplot.CategoryAxisRenderer,
//	            			ticks: ticks,
//	            			tickRenderer: $.jqplot.CanvasAxisTickRenderer,
//			            	tickOptions: { 
//			                    angle: -30,
//			                    fontFamily: 'Courier New',
//			                    fontSize: '9pt'
//            				},
 //           				label: 'Teachers'
             			},
	                    yaxis: {
			 	            pad: 1.05,
            				min: 0,  
            				tickInterval: 10, 
            				tickOptions: { 
                				formatString: '%d'
            				}, 
            				label: '<%= rwrb.getString("days_since_login") %>'
               			}
                    },


                });
                
        		}
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
}



var teacherClassCountArr = [];

function countTeachersClasses() {
	
	var teacherArr = allCohortsArr[currentCohortIndex];    	
	teacherClassCountArr = [];
	var count = 0;
	var teacherline = "";
	var prevUserName = teacherArr.cohortArr[0].userName;
    for (var i=0;i<teacherArr.cohortArr.length;i++) {
		if (teacherArr.cohortArr[i].userName != prevUserName) {
			teacherline = prevUserName + "~" + count;
			teacherClassCountArr.push(teacherline);
			prevUserName = teacherArr.cohortArr[i].userName;
			count = 1;
		} 
		else {
			count = count + 1;
		}
	}
    teacherline = prevUserName + "~" + count;
	teacherClassCountArr.push(teacherline);
}

function showReport3() {


	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
		return;
	}
	
	if (plot3 != null) {
		plot3.destroy();
		plot3 = null;
	}	

	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'getTeacherClassCount',
            lang: loc,
            filter: ''
        },
        success : function(data) {
        	if (data) {
            	var jsonData = $.parseJSON(data);	
            	
            	var s1 = [];
            	var ticks = [];

            	for (var i=0;i<jsonData.length;i = i + 1) {
            		ticks.push(jsonData[i].userName);
            		s1.push(jsonData[i].classCount);
            	}

				// Can specify a custom tick Array.
				// Ticks should match up one for each y value (category) in the series.
			
				
				document.getElementById("chart3_div").style.visibility = 'visible';

				plot3 = $.jqplot('chart3_canvas', [s1], {
				    // The "seriesDefaults" option is an options object that will
				    // be applied to all series in the chart.
				    seriesDefaults:{
				        renderer:$.jqplot.BarRenderer,
				        rendererOptions: {
				        	fillToZero: true,
				        	varyBarColor: true	        	
				        }
			            
				    },
				    // Custom labels for the series are specified with the "label"
				    // option on the series option.  Here a series option object
				    // is specified for each series.
				    // Show the legend and put it outside the grid, but inside the
				    // plot container, shrinking the grid to accomodate the legend.
				    // A value of "outside" would not shrink the grid and allow
				    // the legend to overflow the container.
				    axes: {
				        // Use a category axis on the x axis and use our custom ticks.
				        xaxis: {
				            renderer: $.jqplot.CategoryAxisRenderer,
				            ticks: ticks,
				            tickRenderer: $.jqplot.CanvasAxisTickRenderer,
			            	tickOptions: { 
			                	angle: 15
			            	},
			            	label: '<%= rwrb.getString("teachers") %>'
				        },
				        // Pad the y axis just a little so bars can get close to, but
				        // not touch, the grid boundaries.  1.2 is the default padding.
				        yaxis: {
				            pad: 1.05,
			            	min: 0,  
			            	tickInterval: 1, 
			            	tickOptions: { 
			                	formatString: '%d'
			            	}, 
			            	label: '<%= rwrb.getString("classes") %>'
			
				        }
				    }
				});
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
}


function showReport3a() {


	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
		return;
	}
	
	if (plot3a != null) {
		plot3a.destroy();
		plot3a = null;
	}	

    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'getTeacherStudentCount',
            lang: loc,
            filter: ''
        },
        success : function(data) {
        	if (data) {
            	var jsonData = $.parseJSON(data);	
            	
            	var s1 = [];
            	var ticks = [];

            	for (var i=0;i<jsonData.length;i = i + 1) {
            		ticks.push(jsonData[i].userName);
            		s1.push(jsonData[i].studentCount);
            	}

				// Can specify a custom tick Array.
				// Ticks should match up one for each y value (category) in the series.
			
				document.getElementById("chart3a_div").style.visibility = 'visible';
				
				plot3a = $.jqplot('chart3a_canvas', [s1], {
				    // The "seriesDefaults" option is an options object that will
				    // be applied to all series in the chart.
				    seriesDefaults:{
				        renderer:$.jqplot.BarRenderer,
				        rendererOptions: {
				        	fillToZero: true,
				        	varyBarColor: true	        	
				        }
			            
				    },
				    // Custom labels for the series are specified with the "label"
				    // option on the series option.  Here a series option object
				    // is specified for each series.
				    // Show the legend and put it outside the grid, but inside the
				    // plot container, shrinking the grid to accomodate the legend.
				    // A value of "outside" would not shrink the grid and allow
				    // the legend to overflow the container.
				    axes: {
				        // Use a category axis on the x axis and use our custom ticks.
				        xaxis: {
				            renderer: $.jqplot.CategoryAxisRenderer,
				            ticks: ticks,
				            tickRenderer: $.jqplot.CanvasAxisTickRenderer,
			            	tickOptions: { 
			                	angle: 90
			            	},
			            	label: '<%= rwrb.getString("teachers") %>'
				        },
				        // Pad the y axis just a little so bars can get close to, but
				        // not touch, the grid boundaries.  1.2 is the default padding.
				        yaxis: {
				            pad: 1.05,
			            	min: 0,  
			            	tickInterval: 1, 
			            	tickOptions: { 
			                	formatString: '%d'
			            	}, 
			            	label: '<%= rwrb.getString("students") %>'
			
				        }
				    }
				});
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
}

function showTable3a() {

	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
		return;
	}
	
	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'getTeacherStudentCount',
            lang: loc,
            filter: ''
        },
        success : function(data) {
        	if (data) {
               	var resultData = $.parseJSON(data);

            	var jsonData = $.parseJSON(data);	
               	
//            	var jsonData = resultData[0];
//            	var footerData = resultData[1];
            	
                var cols = [];
                 
                for (var i = jsonData.length-1; i >= 0 ; i--) {
                    for (var k in jsonData[i]) {
                        if (cols.indexOf(k) === -1) {
                             
                            // Push all keys to the array
                            cols.push(k);
                        }
                    }
                }
                 
        	    var tbl_3a = document.getElementById("table3a");
        	    tbl_3a.innerHTML = "";
                                 
                // Create table row tr element of a table
                var tr = tbl_3a.insertRow(-1);
                 
                for (var i = 0; i < cols.length; i++) {
                     
                    // Create the table header th element
                    var theader = document.createElement("th");
                    theader.innerHTML = cols[i];
                     
                    // Append columnName to the table row
                    tr.appendChild(theader);
                }
                // Adding the data to the table
                for (var i = jsonData.length-1; i >= 0 ; i--) {
                     
                    // Create a new row
                    trow = tbl_3a.insertRow(-1);
                    for (var j = 0; j < cols.length; j++) {
                        var cell = trow.insertCell(-1);
                         
                        // Inserting the cell at particular place
                       	cell.innerHTML = jsonData[i][cols[j]];
                    }
                }              
/*
                var fcols = [];

                for (var i = footerData.length-1; i >= 0 ; i--) {
                    for (var k in footerData[i]) {
                        if (fcols.indexOf(k) === -1) {
                             
                            // Push all keys to the array
                            fcols.push(k);
                        }
                    }
                }

                // Adding the data to the table
                for (var i = footerData.length-1; i >= 0 ; i--) {
                     
                    // Create a new row
                    trow = tbl.insertRow(-1);
                    for (var j = 0; j < fcols.length; j++) {
                        var cell = trow.insertCell(-1);
                         
                        // Inserting the cell at particular place
                       	cell.innerHTML = '<b>' + footerData[i][fcols[j]] + '</b>';
                    }
                }              
*/
            
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
}


function showReport3b() {


	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
		return;
	}
	
	if (plot3b != null) {
		plot3b.destroy();
		plot3b = null;
	}	

    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'getTeacherClassStudentCount',
            lang: loc,
            filter: ''
        },
        success : function(data) {
        	if (data) {
            	var jsonData = $.parseJSON(data);	
            	
            	var s1 = [];
            	var ticks = [];

            	for (var i=0;i<jsonData.length;i = i + 1) {
            		var className = jsonData[i].userName + ":" + jsonData[i].className + "[" + jsonData[i].classId + "]";;
            		ticks.push(className);
            		s1.push(jsonData[i].studentCount);
            	}

				// Can specify a custom tick Array.
				// Ticks should match up one for each y value (category) in the series.
			
				document.getElementById("chart3b_div").style.visibility = 'visible';
				
				plot3b = $.jqplot('chart3b_canvas', [s1], {
				    // The "seriesDefaults" option is an options object that will
				    // be applied to all series in the chart.
				    seriesDefaults:{
				        renderer:$.jqplot.BarRenderer,
				        rendererOptions: {
				        	fillToZero: true,
				        	varyBarColor: true	        	
				        }
			            
				    },
				    // Custom labels for the series are specified with the "label"
				    // option on the series option.  Here a series option object
				    // is specified for each series.
				    // Show the legend and put it outside the grid, but inside the
				    // plot container, shrinking the grid to accomodate the legend.
				    // A value of "outside" would not shrink the grid and allow
				    // the legend to overflow the container.
				    axes: {
				        // Use a category axis on the x axis and use our custom ticks.
				        xaxis: {
				            renderer: $.jqplot.CategoryAxisRenderer,
				            ticks: ticks,
				            tickRenderer: $.jqplot.CanvasAxisTickRenderer,
			            	tickOptions: { 
			                	angle: 90
			            	},
			            	label: '<%= rwrb.getString("classes") %>'
				        },
				        // Pad the y axis just a little so bars can get close to, but
				        // not touch, the grid boundaries.  1.2 is the default padding.
				        yaxis: {
				            pad: 1.05,
			            	min: 0,  
			            	tickInterval: 1, 
			            	tickOptions: { 
			                	formatString: '%d'
			            	}, 
			            	label: '<%= rwrb.getString("students") %>'
			
				        }
				    }
				});
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
}

function showTable3b() {

	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
		return;
	}
	
	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'getTeacherClassStudentCount',
            lang: loc,
            filter: ''
        },
        success : function(data) {
        	if (data) {
               	var resultData = $.parseJSON(data);

            	var jsonData = $.parseJSON(data);	
               	
//            	var jsonData = resultData[0];
//            	var footerData = resultData[1];
            	
                var cols = [];
                 
                for (var i = jsonData.length-1; i >= 0 ; i--) {
                    for (var k in jsonData[i]) {
                        if (cols.indexOf(k) === -1) {
                             
                            // Push all keys to the array
                            cols.push(k);
                        }
                    }
                }
                 
        	    var tbl_3b = document.getElementById("table3b");
        	    tbl_3b.innerHTML = "";
                                 
                // Create table row tr element of a table
                var tr = tbl_3b.insertRow(-1);
                 
                for (var i = 0; i < cols.length; i++) {
                     
                    // Create the table header th element
                    var theader = document.createElement("th");
                    theader.innerHTML = cols[i];
                     
                    // Append columnName to the table row
                    tr.appendChild(theader);
                }
                // Adding the data to the table
                for (var i = jsonData.length-1; i >= 0 ; i--) {
                     
                    // Create a new row
                    trow = tbl_3b.insertRow(-1);
                    for (var j = 0; j < cols.length; j++) {
                        var cell = trow.insertCell(-1);
                         
                        // Inserting the cell at particular place
                       	cell.innerHTML = jsonData[i][cols[j]];
                    }
                }              
/*
                var fcols = [];

                for (var i = footerData.length-1; i >= 0 ; i--) {
                    for (var k in footerData[i]) {
                        if (fcols.indexOf(k) === -1) {
                             
                            // Push all keys to the array
                            fcols.push(k);
                        }
                    }
                }

                // Adding the data to the table
                for (var i = footerData.length-1; i >= 0 ; i--) {
                     
                    // Create a new row
                    trow = tbl.insertRow(-1);
                    for (var j = 0; j < fcols.length; j++) {
                        var cell = trow.insertCell(-1);
                         
                        // Inserting the cell at particular place
                       	cell.innerHTML = '<b>' + footerData[i][fcols[j]] + '</b>';
                    }
                }              
*/
            
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
}

function hideLegend() {
    $("#hideLegendBtn").hide();
    $("#showLegendBtn").show();
    $("#effortLegend").hide();
}
function showLegend() {
    $("#showLegendBtn").hide();
    $("#hideLegendBtn").show();
    $("#effortLegend").show();
}

function hideDashboardLegend() {
    $("#hideDashboardLegendBtn").hide();
    $("#showDashboardLegendBtn").show();
    $("#dashboardEffortLegend").hide();
}
function showDashboardLegend() {
    $("#showDashboardLegendBtn").hide();
    $("#hideDashboardLegendBtn").show();
    $("#dashboardEffortLegend").show();
}



var effort_legend_labels = ["SOF",      "ATT",   "SHINT", "SHELP",     "GUESS",   "NOTR",  "SKIP", "GIVEUP",   "NODATA"];
var effort_series_colors = ['#26f213', '#9beb94','#80b1d3', '#fdb462', '#fb8072', '#ffffb3', '#8dd3c7', '#bebada',  '#d9d9d9'];


function showReport3c() {


	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
		return;
	}
	
	if (plot3c0 != null) {
		plot3c0.destroy();
		plot3c0 = null;
	}
	if (plot3c1 != null) {
		plot3c1.destroy();
		plot3c1 = null;
	}
	if (plot3c2 != null) {
		plot3c2.destroy();
		plot3c2 = null;
	}
	if (plot3c3 != null) {
		plot3c3.destroy();
		plot3c3 = null;
	}
	if (plot3c4 != null) {
		plot3c4.destroy();
		plot3c4 = null;
	}
	if (plot3c5 != null) {
		plot3c5.destroy();
		plot3c5 = null;
	}
	if (plot3c6 != null) {
		plot3c6.destroy();
		plot3c6 = null;
	}
	if (plot3c7 != null) {
		plot3c7.destroy();
		plot3c7 = null;
	}
	if (plot3c8 != null) {
		plot3c8.destroy();
		plot3c8 = null;
	}
	if (plot3c9 != null) {
		plot3c9.destroy();
		plot3c9 = null;
	}
	

	if (plot3c10 != null) {
		plot3c20.destroy();
		plot3c10 = null;
	}
	if (plot3c11 != null) {
		plot3c11.destroy();
		plot3c11 = null;
	}
	if (plot3c12 != null) {
		plot3c12.destroy();
		plot3c12 = null;
	}
	if (plot3c13 != null) {
		plot3c13.destroy();
		plot3c13 = null;
	}
	if (plot3c14 != null) {
		plot3c14.destroy();
		plot3c14 = null;
	}
	if (plot3c15 != null) {
		plot3c15.destroy();
		plot3c15 = null;
	}
	if (plot3c16 != null) {
		plot3c16.destroy();
		plot3c16 = null;
	}
	if (plot3c17 != null) {
		plot3c17.destroy();
		plot3c17 = null;
	}
	if (plot3c18 != null) {
		plot3c18.destroy();
		plot3c18 = null;
	}
	if (plot3c19 != null) {
		plot3c19.destroy();
		plot3c19 = null;
	}
	
	
	
	if (plot3cf0 != null) {
		plot3cf0.destroy();
		plot3cf0 = null;
	}

    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'getTeacherProblemsEffort',
            lang: loc,
            filter: ''
        },
        success : function(data) {
        	if (data) {
        		
               	var resultData = $.parseJSON(data);

            	var jsonData = resultData[0];
            	var footerData = resultData[1];

				document.getElementById("chart3c_div1").style.visibility = 'visible';
				document.getElementById("chart3c_div2").style.visibility = 'visible';
            	
            	for (var i=0;i<jsonData.length;i = i + 1) {
            		var teacherName = jsonData[i].teacherName;
					
				
		  			//var line1 = [['SOF',7], ['ATT',13],  ['GUESS', 6], ['SHINT', 20], ['GIVEUP',14], ['NOTR',7], ['SHELP',5], ['NODATA',2]];
//		  			var line1 = [35,13,8,5,3,2,5,8,1];
		  			var line0 = [];
		  			var line1 = [];
		  			var line2 = [];
		  			var line3 = [];
		  			var line4 = [];
		  			var line5 = [];
		  			var line6 = [];
		  			var line7 = [];
		  			var line8 = [];
		  			var line9 = [];
		  			var line10 = [];
		  			var line11 = [];
		  			var line12 = [];
		  			var line13 = [];
		  			var line14 = [];
		  			var line15 = [];
		  			var line16 = [];
		  			var line17 = [];
		  			var line18 = [];
		  			var line19 = [];
		  			var lines = [line0,line1,line2,line3,line4,line5,line6,line7,line8,line9,line10,line11,line12,line13,line14,line15,line16,line17,line18,line19]
		  			lines[i].push(jsonData[i].SOF);
		  			lines[i].push(jsonData[i].ATT);
		  			lines[i].push(jsonData[i].SHINT);
		  			lines[i].push(jsonData[i].SHELP);
		  			lines[i].push(jsonData[i].GUESS);
		  			lines[i].push(jsonData[i].NOTR);
		  			lines[i].push(jsonData[i].SKIP);
		  			lines[i].push(jsonData[i].GIVEUP);
		  			lines[i].push(jsonData[i].NODATA);
					var canvasName = 'chart3c_canvas' + i;
					var tline = lines[i];
					plots3c[i] = $.jqplot(canvasName, [tline], {
				    title: jsonData[i].teacherName,		    
				    seriesDefaults: {
		              renderer: $.jqplot.PieRenderer,
				      rendererOptions: {
				        showDataLabels: true,
					    startAngle: -90,
					    padding: 10,
				        sliceMargin: 6
				      },
				    },
	//			    legend: {
	//			      show: true,
	//		          location: 'w',
	//		          labels: effort_legend_labels		         
	//			    },
				    seriesColors: effort_series_colors
				 
				  });
	          	}
//            	for (var i=0;i<footerData.length;i = i + 1) {
               	for (var i=0;i<1;i = i + 1) {
		  			var fline0 = [];
		  			var flines = [line0]
		  			flines[i].push(footerData[i].SOF);
		  			flines[i].push(footerData[i].ATT);
		  			flines[i].push(footerData[i].SHINT);
		  			flines[i].push(footerData[i].SHELP);
		  			flines[i].push(footerData[i].GUESS);
		  			flines[i].push(footerData[i].NOTR);
		  			flines[i].push(footerData[i].SKIP);
		  			flines[i].push(footerData[i].GIVEUP);
		  			flines[i].push(footerData[i].NODATA);
					var canvasName = 'chart3c_canvas_footer' + i;
					var fline = flines[i];
					plots3cf[i] = $.jqplot(canvasName, [fline], {
				    title: footerData[i].cohortName,		    
				    seriesDefaults: {
		              renderer: $.jqplot.PieRenderer,
				      rendererOptions: {
				        showDataLabels: true,
					    startAngle: -90,
					    padding: 10,
				        sliceMargin: 6
				      },
				    },
				    legend: {
				      show: true,
			          location: 'e',
			          labels: effort_legend_labels		         
				    },
				    seriesColors: effort_series_colors
				 
				  });
            	}
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
}


function showReport3d() {


	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
		return;
	}
	if ((currentClass == null) || (currentClass == "")) {
		alert('<%= rwrb.getString("must_select_class") %>');
		return;
	}
		
	if (plot3d != null) {
		plot3d.destroy();
		plot3d = null;
	}

    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'getClassProblemsEffortDashboard',
            lang: loc,
            filter: currentClass
        },
        success : function(data) {
        	if (data) {
        		
				document.getElementById("liveDashboardEffortPane").style.visibility = 'visible';
                //$("#liveDashboardEffortPane").show();
                
               	var resultData = $.parseJSON(data);
                var jsonData = resultData[0];
            	
            	for (var i=0;i<jsonData.length;i = i + 1) {
		  			var line0 = [];
//		  			var lines = [line0,line1,line2,line3,line4,line5,line6,line7,line8,line9]
		  			var lines = [line0]
		  			lines[i].push(jsonData[i].SOF);
		  			lines[i].push(jsonData[i].ATT);
		  			lines[i].push(jsonData[i].SHINT);
		  			lines[i].push(jsonData[i].SHELP);
		  			lines[i].push(jsonData[i].GUESS);
		  			lines[i].push(jsonData[i].NOTR);
		  			lines[i].push(jsonData[i].SKIP);
		  			lines[i].push(jsonData[i].GIVEUP);
		  			lines[i].push(jsonData[i].NODATA);
					var canvasName = 'chart3d_canvas';
					var tline = lines[i];
					plot3d = $.jqplot(canvasName, [tline], {
				    seriesDefaults: {
		              renderer: $.jqplot.PieRenderer,
				      rendererOptions: {
				        showDataLabels: true,
					    startAngle: -90,
					    padding: 10,
				        sliceMargin: 6
				      },
				    },
//				    legend: {
//				      show: true,
//			          location: 'w',
//			          labels: effort_legend_labels		         
//				    },
				    seriesColors: effort_series_colors
				 
				  });
            	}
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
}

function showReport3e() {


	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
		return;
	}
	
	if (plot3e != null) {
		plot3e.destroy();
		plot3e = null;
	}

    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'getClassProblemsSolvedDashboard',
            lang: loc,
            filter: currentClass
       },
        success : function(data) {
        	if (data) {
            	var jsonData = $.parseJSON(data);	
            	
				document.getElementById("liveDashboardProblemPane").style.visibility = 'visible';
                //$("#liveDashboardProblemPane").show();
            	var s1 = [];
            	var ticks = [];
            	
            	var liveDashboardTotal = parseInt(data);
            	
            	var theMax = liveDashboardTotal * 2;
            	theInterval = liveDashboardTotal / 25;
           		ticks.push("Today");
           		s1.push(liveDashboardTotal);
				
				plot3e = $.jqplot('chart3e_canvas', [s1], {
				    seriesDefaults:{
				        renderer:$.jqplot.BarRenderer,
				        rendererOptions: {
				        	fillToZero: true,
				        	varyBarColor: true	        	
				        }
			            
				    },
				    axes: {
				        xaxis: {
				            renderer: $.jqplot.CategoryAxisRenderer,
				            ticks: ticks,
				            tickRenderer: $.jqplot.CanvasAxisTickRenderer,
			            	tickOptions: { 
			                	angle: 15
			            	},
			            	label: 'Total for today'
				        },
				        yaxis: {
				            pad: 1.05,
				            max: theMax,
			            	min: 0,  
			            	tickInterval: theInterval, 
			            	tickOptions: { 
			                	formatString: '%d'
			            	}, 
			            	label: 'Problems Solved'
			
				        }
				    }
				});
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
}

function showTable3f() {

	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
		return;
	}
	
	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'getStudentCensus',
            lang: loc,
            filter: ''
        },
        success : function(data) {
        	if (data) {
            	var jsonData = $.parseJSON(data);	
               	
                var cols = [];
                 
                for (var i = jsonData.length-1; i >= 0 ; i--) {
                    for (var k in jsonData[i]) {
                        if (cols.indexOf(k) === -1) {
                             
                            // Push all keys to the array
                            cols.push(k);
                        }
                    }
                }
                 
        	    var tbl_3f = document.getElementById("table3f");
        	    tbl_3f.innerHTML = "";
                                 
                // Create table row tr element of a table
                var tr = tbl_3f.insertRow(-1);
                 
                for (var i = 0; i < cols.length; i++) {
                     
                    // Create the table header th element
                    var theader = document.createElement("th");
                    theader.innerHTML = cols[i];
                     
                    // Append columnName to the table row
                    tr.appendChild(theader);
                }
                // Adding the data to the table
                for (var i = jsonData.length-1; i >= 0 ; i--) {
                     
                    // Create a new row
                    trow = tbl_3f.insertRow(-1);
                    for (var j = 0; j < cols.length; j++) {
                        var cell = trow.insertCell(-1);
                         
                        // Inserting the cell at particular place
                       	cell.innerHTML = jsonData[i][cols[j]];
                    }
                }
                
                var csv = JSON2CSV(jsonData);
                var downloadLink = document.createElement("a");
                var blob = new Blob(["\ufeff", csv]);
                var url = URL.createObjectURL(blob);
                downloadLink.href = url;
                downloadLink.download = cohortsArr[currentCohortIndex].cohortName + "_StudentCensus.csv";                
                downloadLink.click();
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
}

function showTable4a() {

	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
	}

    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'getTeacherClassTableSlices',
            lang: loc,
            filter: ''
        },
        success : function(data) {
        	if (data) {
               	var resultData = $.parseJSON(data);

            	var jsonData = $.parseJSON(data);	
               	
//            	var jsonData = resultData[0];
//            	var footerData = resultData[1];
            	
                var cols = [];
                 
                for (var i = jsonData.length-1; i >= 0 ; i--) {
                    for (var k in jsonData[i]) {
                        if (cols.indexOf(k) === -1) {
                             
                            // Push all keys to the array
                            cols.push(k);
                        }
                    }
                }
                 
        	    var tbl_4a = document.getElementById("table4a");
        	    tbl_4a.innerHTML = "";
                                 
                // Create table row tr element of a table
                var tr = tbl_4a.insertRow(-1);
                 
                for (var i = 0; i < cols.length; i++) {
                     
                    // Create the table header th element
                    var theader = document.createElement("th");
                    theader.innerHTML = cols[i];
                     
                    // Append columnName to the table row
                    tr.appendChild(theader);
                }
                // Adding the data to the table
                for (var i = jsonData.length-1; i >= 0 ; i--) {
                     
                    // Create a new row
                    trow = tbl_4a.insertRow(-1);
                    for (var j = 0; j < cols.length; j++) {
                        var cell = trow.insertCell(-1);
                         
                        // Inserting the cell at particular place
                       	cell.innerHTML = jsonData[i][cols[j]];
                    }
                }              
/*
                var fcols = [];

                for (var i = footerData.length-1; i >= 0 ; i--) {
                    for (var k in footerData[i]) {
                        if (fcols.indexOf(k) === -1) {
                             
                            // Push all keys to the array
                            fcols.push(k);
                        }
                    }
                }

                // Adding the data to the table
                for (var i = footerData.length-1; i >= 0 ; i--) {
                     
                    // Create a new row
                    trow = tbl.insertRow(-1);
                    for (var j = 0; j < fcols.length; j++) {
                        var cell = trow.insertCell(-1);
                         
                        // Inserting the cell at particular place
                       	cell.innerHTML = '<b>' + footerData[i][fcols[j]] + '</b>';
                    }
                }              
*/
            
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });


}

function showTable4b() {


	var filter = "";

    const rb4bContent = document.querySelectorAll('input[name="optRadio4bContent"]');
    for (const rb4bc of rb4bContent) {
        if (rb4bc.checked) {
        	filter = rb4bc.value;
            break;
        }
    }
    	
    var jsonData_4b = null;
    var cols_4b = [];

    var tbl_4b = document.getElementById("table4b");
    tbl_4b.innerHTML = "";
	$('#table4b-loader').show();
    
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'getReportedProblemErrors',
            lang: loc,
            filter: filter 
        },
        success : function(data) {
        	if (data) {

        		var commentIndex = -1;
        		var sessionIndex = -1;
        		var eventIndex = -1;
        		
        		$('#table4b-loader').hide();

        		//              var resultData = $.parseJSON(data);
//            	var jsonData = resultData[0];
//            	var footerData = resultData[1];

            	jsonData_4b = $.parseJSON(data);	
               	
            	var index = 0;
                 
                for (var i = jsonData_4b.length-1; i >= 0 ; i--) {
                    for (var k in jsonData_4b[i]) {
                        if (cols_4b.indexOf(k) === -1) {                       
                            if (k == 'Comment') {
                            	commentIndex = index;
                            }
                            if (k == 'Session Id') {
                            	sessionIndex = index;
                            }
                            if (k == 'Event Id') {
                            	eventIndex = index;
                            }
                        	index = index + 1;	
                            // Push all keys to the array
                            cols_4b.push(k);
                        }
                    }
                }
                 
                                 
                // Create table row tr element of a table
                var tr = tbl_4b.insertRow(-1);
                 

                for (var i = 0; i < cols_4b.length; i++) {
                     
                    // Create the table header th element
                    var theader = document.createElement("th");
                    theader.innerHTML = cols_4b[i];
                     
                    // Append columnName to the table row
                    tr.appendChild(theader);
                }
                // Adding the data to the table
                for (var i = jsonData_4b.length-1; i >= 0 ; i--) {
                     
                    // Create a new row
                    trow = tbl_4b.insertRow(-1);
                    for (var j = 0; j < cols_4b.length; j++) {
                        var cid = "td4b_" + i + "_" + j;
                    	var cell = trow.insertCell(-1);
                       
                       	cell.innerHTML = jsonData_4b[i][cols_4b[j]];                       	


                       	if (j == eventIndex) {
                           	cell.innerHTML = "<button id='" + cid + "' value='" + jsonData_4b[i][cols_4b[j]] + "'>" + "Update" + "</button>";
                        	document.getElementById(cid).addEventListener("click", updateProblemStatusForm, false);                       	
                       	}
                       	if (j == commentIndex) {
                        	cell.style.color="red";
	                       	cell.style.width="500px";
	                       	cell.style.maxWidth = "500px";
	                       	cell.style.wordWrap = "break-word";
	                       	cell.style.textAlign = 'left';

                        }
                        if (j == sessionIndex) {
                           	cell.innerHTML = "<button id='" + cid + "' value='" + jsonData_4b[i][cols_4b[j]] + "'>" + jsonData_4b[i][cols_4b[j]] + "</button>";
                        	cell.style.color="green";
                        	document.getElementById(cid).addEventListener("click", showSessionProblems, false);
                        	
                        }
                    }
                }              
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });


}


function showSessionProblems() {
	
	var sessionId = this.value;

    $('#showSessionProblemsModalPopup').modal('hide');
    
    var jsonData = null;
    var cols = [];

    var tbl_4b_session = document.getElementById("table4b_session");
    tbl_4b_session.innerHTML = "";
    var tbl_4b_session_hdr = document.getElementById("table4b_session_hdr");
    tbl_4b_session_hdr.innerHTML = "Session: " + sessionId;

    
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'getSessionProblems',
            lang: loc,
            filter: sessionId 
        },
        success : function(data) {
        	if (data) {
               	
            	var index = 0;
            	var cols = [];
                 
            	jsonData = $.parseJSON(data);	

            	for (var i = jsonData.length-1; i >= 0 ; i--) {
                    for (var k in jsonData[i]) {
                        if (cols.indexOf(k) === -1) {                       
                        	index = index + 1;	
                            // Push all keys to the array
                            cols.push(k);
                        }
                    }
                }
                 
                                 
                // Create table row tr element of a table
                var tr = tbl_4b_session.insertRow(-1);
                 
                for (var i = 0; i < cols.length; i++) {
                     
                    // Create the table header th element
                    var theader = document.createElement("th");
                    theader.innerHTML = cols[i];
                     
                    // Append columnName to the table row
                    tr.appendChild(theader);
                }
                // Adding the data to the table
                for (var i = jsonData.length-1; i >= 0 ; i--) {
                     
                    // Create a new row
                    trow = tbl_4b_session.insertRow(-1);
                    for (var j = 0; j < cols.length; j++) {
                        var cell = trow.insertCell(-1);
                        // Inserting the cell at particular place
                       	cell.innerHTML = jsonData[i][cols[j]];
                    }
                }              
                $('#showSessionProblemsModalPopup').modal('show');
            
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });

}

function updateProblemStatusForm() {
	
	var eventId = this.value;

    $('#updateProblemStatusModalPopup').modal('hide');
    
    var jsonData = null;
    var cols = [];

   
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'getProblemHistoryErrorData',
            lang: loc,
            filter: eventId 
        },
        success : function(data) {
        	if (data) {
               	
            	jsonData = $.parseJSON(data);	

            	var problemId  = jsonData.problemId;
            	var comment  = jsonData.comment;
            	var probStatus = jsonData.probStatus;
            	var historyId = jsonData.historyId;

            	document.getElementById("hiddentEventId").value = eventId;
            	document.getElementById("hiddenHistoryId").value = historyId;
            	document.getElementById("table4b_update_hdr").innerHTML = "<%= rb.getString("problem_id") %>: " + problemId;     
            	document.getElementById("message-text").value = comment;     
            	
            	if (probStatus == "0") {
            		document.getElementById("radio4bReported").checked = true;
            	}            	
            	if (probStatus == "1") {
            		document.getElementById("radio4bBroken").checked = true;
            	}
            	if (probStatus == "2") {
            		document.getElementById("radio4bFixed").checked = true;
            	}
            	if (probStatus == "3") {
            		document.getElementById("radio4bIgnore").checked = true;
            	}
        	
        	//document.getElementById("is-broken").value = probStatus;     
            	
            	
                $('#updateProblemStatusModalPopup').modal('show');
            
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}

        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });

}

function updateProblemStatusSubmit() {
	
	
	
	var probStatus = '0';
	
	if (document.getElementById("radio4bReported").checked == true) {
		probStatus = '0';		
	}
	if (document.getElementById("radio4bBroken").checked == true) {
		probStatus = '1';		
	}
	if (document.getElementById("radio4bFixed").checked == true) {
		probStatus = '2';		
	}
	if (document.getElementById("radio4bIgnore").checked == true) {
		probStatus = '3';		
	}

	var params = document.getElementById("hiddentEventId").value + "~" + document.getElementById("hiddenHistoryId").value + "~" + probStatus + "~" + document.getElementById("message-text").value + " UPDATED" ;

	
	$.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'updateProblemHistoryErrorData',
            lang: loc,
            filter: params
        },
        success : function(data) {
        	if (data) {
               	alert(data);
                $('#updateProblemStatusModalPopup').modal('hide');
        	}
        	else {
            	alert("error" + data);
                $('#updateProblemStatusModalPopup').modal('hide');
        	}

        },
        error : function(e) {
        	alert("error" + " - no reponse");
            console.log(e);
            $('#updateProblemStatusModalPopup').modal('hide');
        }
    });

}


function showTable4c() {


	var filter = "teacherId";

    const rb4cContent = document.querySelectorAll('input[name="optRadio4cContent"]');
    for (const rb4cc of rb4cContent) {
        if (rb4cc.checked) {
        	filter = rb4cc.value;
            break;
        }
    }
    	
    var jsonData_4c = null;
    var cols_4c = [];

    var tbl_4c = document.getElementById("table4c");
    tbl_4c.innerHTML = "";
	$('#table4c-loader').show();
    
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'getTeacherFeedback',
            lang: loc,
            filter: filter 
        },
        success : function(data) {
        	if (data) {

        		
        		$('#table4c-loader').hide();

        		//              var resultData = $.parseJSON(data);
//            	var jsonData = resultData[0];
//            	var footerData = resultData[1];

            	jsonData_4c = $.parseJSON(data);	
               	
            	var index = 0;
            	var commentIndex = 0;
            	var teacherIndex = 0;
                 
                for (var i = jsonData_4c.length-1; i >= 0 ; i--) {
                    for (var k in jsonData_4c[i]) {
                        if (cols_4c.indexOf(k) === -1) {                       
                            if (k == 'Comment') {
                            	commentIndex = index;
                            }
                            if (k == 'TeacherId') {
                            	teacherIndex = index;
                            }
                        	index = index + 1;	
                            // Push all keys to the array
                            cols_4c.push(k);
                        }
                    }
                }
                 
                                 
                // Create table row tr element of a table
                var tr = tbl_4c.insertRow(-1);
                 

                for (var i = 0; i < cols_4c.length; i++) {
                     
                    // Create the table header th element
                    var theader = document.createElement("th");
                    theader.innerHTML = cols_4c[i];
                     
                    // Append columnName to the table row
                    tr.appendChild(theader);
                }
                // Adding the data to the table
                for (var i = jsonData_4c.length-1; i >= 0 ; i--) {
                     
                    // Create a new row
                    trow = tbl_4c.insertRow(-1);
                    for (var j = 0; j < cols_4c.length; j++) {
                        var cid = "td4c_" + i + "_" + j;
                    	var cell = trow.insertCell(-1);
                       
                       	cell.innerHTML = jsonData_4c[i][cols_4c[j]];                       	

                       	if (j == commentIndex) {
                        	cell.style.color="black";
	                       	cell.style.width="500px";
	                       	cell.style.maxWidth = "500px";
	                       	cell.style.wordWrap = "break-word";
	                       	cell.style.textAlign = 'left';

                        }
                        if (j == teacherIndex) {
                           	cell.innerHTML = "<button id='" + cid + "' value='" + jsonData_4c[i][cols_4c[j]] + "'>" + jsonData_4c[i][cols_4c[j]] + "</button>";
                        	cell.style.color="green";                        	
                        }
                    }
                }              
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });


}


function showReport_tcp() {

	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
		return;
	}
	if (plot_tcp != null) {
		plot_tcp.destroy();
		plot_tcp = null;
	}	

	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'teacherClassProblems',
            lang: loc,
            filter: ''
        },
        success : function(data) {
        	if (data) {
            	var resultData = $.parseJSON(data);
            	console.log(data);
            	var jsonData = resultData[0];
            	var ticks = [];
            	for (var i=0;i<jsonData.length;i = i + 1) {
            		if (jsonData[i].TID === "") {
            			continue;
            		}
            		var className = jsonData[i].Teacher + ":" + jsonData[i].Class + "[" + jsonData[i].CID + "]"+ " ( " + jsonData[i].pctSolved + "% solved )";;
            		ticks.push(className);
            	}
            	var theSeries = [];
            	var row1 = [];
            	var row2 = [];
            	var row3 = [];
                
            	var index = 1;
            	for (var i=0;i<jsonData.length;i = i + 1) {
            		if (jsonData[i].TID === "") {
            			continue;
            		}            	
            		var element1 = [];
            		var element2 = [];
            		element1.push(jsonData[i].Seen);
            		element1.push(index);
            		row1.push(element1);
            		element2.push(jsonData[i].Solved);
            		element2.push(index);
            		row2.push(element2);
//            		var element3 = [];
//            		element3.push(jsonData[i].nbr_problems_skipped);
//            		element3.push(i+1);
//            		row3.push(element3);
            		index = index + 1;
            	}
//            	theSeries.push(row3);
            	theSeries.push(row2);
            	theSeries.push(row1);

            	plot_tcp = $.jqplot('tcp_canvas', theSeries, {

                    seriesDefaults: {
                        renderer:$.jqplot.BarRenderer,
                        // Show point labels to the right ('e'ast) of each bar.
                        // edgeTolerance of -15 allows labels flow outside the grid
                        // up to 15 pixels.  If they flow out more than that, they 
                        // will be hidden.
                        pointLabels: { show: true, location: 'e', edgeTolerance: -15 },
                        // Rotate the bar shadow as if bar is lit from top right.
                        shadowAngle: 135,
                        // Here's where we tell the chart it is oriented horizontally.
                        rendererOptions: {
                            barDirection: 'horizontal'
                        }
                    },
                    seriesColors:['#ff0066', '#66ccff'],
            	    series:[
//            	        {label:'<%= rwrb.getString("skipped") %>'},
            	        {label:'<%= rwrb.getString("solved") %>'},
            	        {label:'<%= rwrb.getString("seen") %>'}
            	    ],
            	    legend: {
            	        show: true
            	    },                

                    axes: {
                        yaxis: {
							renderer: $.jqplot.CategoryAxisRenderer,
							ticks: ticks            
             			}
                    }
            	});
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
}

function showTable_tcp() {

	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
		return;
	}
	
	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'teacherClassProblems',
            lang: loc,
            filter: ''
        },
        success : function(data) {
        	if (data) {
            	console.log(data);
               	var resultData = $.parseJSON(data);
                           	    
            	var jsonData = resultData[0];
            	var footerData = resultData[1];
            	
                var cols = [];
                 
                for (var i = jsonData.length-1; i >= 0 ; i--) {
                    for (var k in jsonData[i]) {
                        if (cols.indexOf(k) === -1) {
                             
                            // Push all keys to the array
                            cols.push(k);
                        }
                    }
                }
                 
        	    var tcp_tbl = document.getElementById("tcp_table");
        	    tcp_tbl.innerHTML = "";
                                 
                // Create table row tr element of a table
                var tr = tcp_tbl.insertRow(-1);
                 
                for (var i = 0; i < cols.length; i++) {
                     
                    // Create the table header th element
                    var theader = document.createElement("th");
                    
                    theader.innerHTML = cols[i];
                     
                    // Append columnName to the table row
                    tr.appendChild(theader);
                }
                // Adding the data to the table
                for (var i = jsonData.length-1; i >= 0 ; i--) {
                     
                    // Create a new row
                    trow = tcp_tbl.insertRow(-1);
                    for (var j = 0; j < cols.length; j++) {
                        var cell = trow.insertCell(-1);
                         
                        // Inserting the cell at particular place
                       	cell.innerHTML = jsonData[i][cols[j]];
                    }
                }              

                var fcols = [];

                for (var i = footerData.length-1; i >= 0 ; i--) {
                    for (var k in footerData[i]) {
                        if (fcols.indexOf(k) === -1) {
                             
                            // Push all keys to the array
                            fcols.push(k);
                        }
                    }
                }

                // Adding the data to the table
                for (var i = footerData.length-1; i >= 0 ; i--) {
                     
                    // Create a new row
                    trow = tcp_tbl.insertRow(-1);
                    for (var j = 0; j < fcols.length; j++) {
                        var cell = trow.insertCell(-1);
                         
                        // Inserting the cell at particular place
                       	cell.innerHTML = '<b>' + footerData[i][fcols[j]] + '</b>';
                    }
                }              
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
}


function showReport_tp() {

	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
		return;
	}

	if (plot_tp != null) {
		plot_tp.destroy();
		plot_tp = null;
	}	

	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'teacherProblems',
            lang: loc,
            filter: ''
        },
        success : function(data) {
        	if (data) {
            	var resultData = $.parseJSON(data);
            	console.log(data);
            	var jsonData = resultData[0];
            	var ticks = [];
            	for (var i=0;i<jsonData.length;i = i + 1) {
            		
            		var teacherName = jsonData[i].Teacher + " ( " + jsonData[i].pctSolved + "% solved )";
            		ticks.push(teacherName);
            	}
            	var theSeries = [];
            	var row1 = [];
            	var row2 = [];
            	var row3 = [];
             
            	for (var i=0;i<jsonData.length;i = i + 1) {            	
            		var element1 = [];
            		var element2 = [];
            		var element3 = [];
            		element1.push(jsonData[i].Seen);
            		element1.push(i+1);
            		row1.push(element1);
            		element2.push(jsonData[i].Solved);
            		element2.push(i+1);
            		row2.push(element2);
//            		var element3 = [];
//            		element3.push(jsonData[i].nbr_problems_skipped);
//            		element3.push(i+1);
//            		row3.push(element3);
            	}
//            	theSeries.push(row3);
            	theSeries.push(row2);
            	theSeries.push(row1);

            	plot_tp = $.jqplot('tp_canvas', theSeries, {

                    seriesDefaults: {
                        renderer:$.jqplot.BarRenderer,
                        // Show point labels to the right ('e'ast) of each bar.
                        // edgeTolerance of -15 allows labels flow outside the grid
                        // up to 15 pixels.  If they flow out more than that, they 
                        // will be hidden.
                        pointLabels: { show: true, location: 'e', edgeTolerance: -15 },
                        // Rotate the bar shadow as if bar is lit from top right.
                        shadowAngle: 135,
                        // Here's where we tell the chart it is oriented horizontally.
                        rendererOptions: {
                            barDirection: 'horizontal'
                        }
                    },
                    seriesColors:['#ff0066', '#66ccff'],
            	    series:[
//            	        {label:'<%= rwrb.getString("skipped") %>'},
            	        {label:'<%= rwrb.getString("solved") %>'},
            	        {label:'<%= rwrb.getString("seen") %>'}
            	    ],
            	    legend: {
            	        show: true
            	    },                

                    axes: {
                        yaxis: {
							renderer: $.jqplot.CategoryAxisRenderer,
			             	fontFamily: 'Georgia',
             			 	fontSize: '10pt',
							ticks: ticks            
             			}
                    }
            	});
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
}

function showTable_tp() {

	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
	}
	
	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'teacherProblems',
            lang: loc,
            filter: ''
        },
        success : function(data) {
        	if (data) {
               	var resultData = $.parseJSON(data);
           	    
            	var jsonData = resultData[0];
            	var footerData = resultData[1];
            	
                var cols = [];
                 
                for (var i = jsonData.length-1; i >= 0 ; i--) {
                    for (var k in jsonData[i]) {
                        if (cols.indexOf(k) === -1) {
                             
                            // Push all keys to the array
                            cols.push(k);
                        }
                    }
                }
                 
        	    var tp_tbl = document.getElementById("tp_table");
        	    tp_tbl.innerHTML = "";
                                 
                // Create table row tr element of a table
                var tr = tp_tbl.insertRow(-1);
                 
                for (var i = 0; i < cols.length; i++) {
                     
                    // Create the table header th element
                    var theader = document.createElement("th");
                    theader.innerHTML = cols[i];
                     
                    // Append columnName to the table row
                    tr.appendChild(theader);
                }
                // Adding the data to the table
                for (var i = jsonData.length-1; i >= 0 ; i--) {
                     
                    // Create a new row
                    trow = tp_tbl.insertRow(-1);
                    for (var j = 0; j < cols.length; j++) {
                        var cell = trow.insertCell(-1);
                         
                        // Inserting the cell at particular place
                       	cell.innerHTML = jsonData[i][cols[j]];
                    }
                }              

                var fcols = [];

                for (var i = footerData.length-1; i >= 0 ; i--) {
                    for (var k in footerData[i]) {
                        if (fcols.indexOf(k) === -1) {
                             
                            // Push all keys to the array
                            fcols.push(k);
                        }
                    }
                }

                // Adding the data to the table
                for (var i = footerData.length-1; i >= 0 ; i--) {
                     
                    // Create a new row
                    trow = tp_tbl.insertRow(-1);
                    for (var j = 0; j < fcols.length; j++) {
                        var cell = trow.insertCell(-1);
                         
                        // Inserting the cell at particular place
                       	cell.innerHTML = '<b>' + footerData[i][fcols[j]] + '</b>';
                    }
                }              

            
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
}


function showTable_tpsa() {

	
	var teacherComments = '';
	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
	}
	
		
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'teacherProblemsStudentAverages',
            lang: loc,
            filter: ''
        },
        success : function(data) {
        	if (data) {
               	var resultData = $.parseJSON(data);
           	    
                $("#tpsa-panel").show();	
               	
            	var jsonData = resultData[0];
            	var footerData = resultData[1];
            	
                var cols = [];
                 
                for (var i = jsonData.length-1; i >= 0 ; i--) {
                    for (var k in jsonData[i]) {
                        if (cols.indexOf(k) === -1) {
                             
                            // Push all keys to the array
                            cols.push(k);
                        }
                    }
                }
                 
        	    var tpsa_head = document.getElementById("tpsa_header_table");
        	    tpsa_head.innerHTML = "";
        	    var headerWidth = cols.length * 100;
                                 
                // Create table row tr element of a table
                var tr = tpsa_head.insertRow(-1);
                 
                for (var i = 0; i < cols.length; i++) {
                     
                    // Create the table header th element
                    var theader = document.createElement("th");
                    if (i == 0) {
                    	theader.className += 'tt-tpsa-cell-small';
                    }
                    else {
                    	if (i == cols.length-1) {
                    		theader.className += 'tt-tpsa-cell-large';
                    	}
                    	else {
                    		theader.className += 'tt-tpsa-cell';
                    	}
                    }
                    theader.innerHTML = cols[i];
                     
                    // Append columnName to the table row
                    tr.appendChild(theader);
                }

                
        	    var tpsa_body = document.getElementById("tpsa_body_table");
				// Adding the data to the table
				tpsa_body.innerHTML = "";
				teacherComments = '';
        	    for (var i = jsonData.length-1; i >= 0 ; i--) {
                     
                    // Create a new row
                    trow = tpsa_body.insertRow(-1);
                    for (var j = 0; j < cols.length; j++) {
                        var cell = trow.insertCell(-1);
                         
                        if (j == 0) {
                        	cell.className += 'tt-tpsa-cell-small';
                        }
                        else {
                       		if (j == cols.length-1) {
                       			cell.className += 'tt-tpsa-cell-large';
                       		}
                       		else {
                            	cell.className += 'tt-tpsa-cell';                    	                       			
                       		}
                        }
                        var cellValue = "" + jsonData[i][cols[j]];

                        // Inserting the cell at particular place
                        let result = cellValue.indexOf("~");
                        if (result < 0) {
                        	cell.style.backgroundColor = "white";
                           	cell.innerHTML = cellValue;
                        }
                        else {
	                        var splitValue = cellValue.split("~");
	                        cellValue = splitValue[0];
                        	cell.style.backgroundColor = splitValue[1];
                           	cell.innerHTML = splitValue[0];
                           	if (splitValue.length > 2) {
                           		cell.title = splitValue[2];
                               	cell.style.cursor = "pointer";
//                                cell.onclick = function(){
//                                    alert(teacherComments);
//                                }                               	
                           	}
                        }
                       	
                    }
                }              

                var fcols = [];

                for (var i = footerData.length-1; i >= 0 ; i--) {
                    for (var k in footerData[i]) {
                        if (fcols.indexOf(k) === -1) {
                             
                            // Push all keys to the array
                            fcols.push(k);
                        }
                    }
                }

                // Adding the data to the table
                for (var i = footerData.length-1; i >= 0 ; i--) {
                     
                    // Create a new row
                    trow = tpsa_body.insertRow(-1);
                    for (var j = 0; j < fcols.length; j++) {
                        var cell = trow.insertCell(-1);
                         
                        // Inserting the cell at particular place
                       	cell.innerHTML = '<b>' + footerData[i][fcols[j]] + '</b>';
                    }
                }              

            
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
}



function showReport_tcs() {

	
	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
	}
	var tcsFilter = '';

	var trendUnit = 7; 
	var rpt_tcs_Weeks = "";
    const rb_tcsweeks = document.querySelectorAll('input[name="opt_tcs_RadioWeeks"]');

    for (const rb_tcsw of rb_tcsweeks) {
        if (rb_tcsw.checked) {
        	rpt_tcs_Weeks = rb_tcsw.value;
            break;
        }
    }
    
	if (rpt_tcs_Weeks == "all") {
		document.getElementById('tcs_PriorWeeks').value = "";
	}
	else {     
	    if (document.getElementById('tcs_PriorWeeks').value != "") { 
	   		trendNumberOfUnits = document.getElementById('tcs_PriorWeeks').value;
	    }
	    else {
	   		trendNumberOfUnits = document.getElementById('settingsPriorWeeks').value;    	
	   		document.getElementById('tcs_PriorWeeks').value = document.getElementById('settingsPriorWeeks').value;
	    }
	}

	var startWeek = currentWeek;
    var intNumberOfUnits = Number(trendNumberOfUnits);
    if (intNumberOfUnits < 1) {
    	intNumberOfUnits = 1;
    }
    if (intNumberOfUnits > 1) {
    	startWeek = startWeek - intNumberOfUnits;
    }
    	
	var tcsFilter = "" + startWeek + "~" + trendUnit + "~" + trendNumberOfUnits;
	
	
	//SELECT teacherlog.teacherId, userName as uname, COUNT(*) AS total FROM teacher, teacherlog, teacher_map_cohort where teacherlog.teacherId = teacher_map_cohort.teacherid and teacher.id = teacherlog.teacherId and action = "login" GROUP BY teacherId;
	if (plot_tcs != null) {
		plot_tcs.destroy();
		plot_tcs = null;
	}	

	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
	}
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: currentCohortId,
            reportType: 'teacherClassActiveStudentCount',
            lang: loc,
            filter: tcsFilter
        },
        success : function(data) {
        	if (data) {
            	var jsonData = $.parseJSON(data);
            	console.log(data);
            	var ticks = [];
            	for (var i=0;i<jsonData.length;i = i + 1) {
            		var className = jsonData[i].username + ":" + jsonData[i].className + "(" + jsonData[i].classId + ")";
            		ticks.push(className);
            	}
            	var theSeries = [];
            	var row1 = [];
//            	var row2 = [];
//            	var row3 = [];
             
            	for (var i=0;i<jsonData.length;i = i + 1) {            	
            		var element1 = [];
//            		var element2 = [];
//            		var element3 = [];
            		element1.push(jsonData[i].nbr_active_students);
            		element1.push(i+1);
            		row1.push(element1);
//            		element2.push(jsonData[i].nbr_problems_solved);
//            		element2.push(i+1);
//            		row2.push(element2);
//            		var element3 = [];
//            		element3.push(jsonData[i].nbr_problems_skipped);
//            		element3.push(i+1);
//            		row3.push(element3);
            	}
//            	theSeries.push(row3);
//            	theSeries.push(row2);
            	theSeries.push(row1);

            	plot_tcs = $.jqplot('tcs_canvas', theSeries, {

                    seriesDefaults: {
                        renderer:$.jqplot.BarRenderer,
                        // Show point labels to the right ('e'ast) of each bar.
                        // edgeTolerance of -15 allows labels flow outside the grid
                        // up to 15 pixels.  If they flow out more than that, they 
                        // will be hidden.
                        pointLabels: { show: true, location: 'e', edgeTolerance: -15 },
                        // Rotate the bar shadow as if bar is lit from top right.
                        shadowAngle: 135,
                        // Here's where we tell the chart it is oriented horizontally.
                        rendererOptions: {
                            barDirection: 'horizontal'
                        }
                    },
//                    seriesColors:['#66ccff'],
//            	    series:[
//            	        {label:'Skipped'},
//            	        {label:'Solved'},
//            	        {label:'Active Students'}
//            	    ],
//            	    legend: {
//            	        show: true,
//            	        placement: 'outsideGrid'
//            	    },                

                    axes: {
                        xaxis: {
							min: 0,
             				max: 600,
             				interval: 25,
                            font: '15px sans-serif'				
               			},
                        yaxis: {
							renderer: $.jqplot.CategoryAxisRenderer,
							ticks: ticks            
             			}
                    }
            	});
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
}







var rpt5usernameXref = [];
var rpt5usernameStrXref = [];

function rpt5mapUsernameIndex(index,username){

	if (rpt5usernameXref.length == 0) {
		rpt5usernameXref.push(index);
		rpt5usernameStrXref.push(username);
		return 0;
	}
	else {
		for (var i=0; i<rpt5usernameXref.length; i++) {
			if (rpt5usernameXref[i] == index) {
				return i;
			}
			else {
				if (i == (rpt5usernameXref.length - 1))	{			
					rpt5usernameXref.push(index);
					rpt5usernameStrXref.push(username);
					return (i+1);
				}
			}
		}
	}
}

function rpt5getUsernameIndex(username) {

	var teacherArr = allCohortsArr[currentCohortIndex].cohortArr;    	

	for (var i=0;i<teacherArr.length;i++) {

		if (teacherArr[i].userName === username) {			
			return rpt5mapUsernameIndex(i,username);
		}
	}
	return -1;
	
}

var rpt5Populate = "ShowAll";

function rpt5getUsernameDisplay(index) {

	if (rpt5Populate == "showSingleWithAnonymous") {
		for (var i=0;i<8;i++) {		
			if (rpt5usernameStrXref[index] === currentTeacher.username) {
				
				rpt5Colors[index] = msTeacherColors[index];
				return currentTeacher.username;
			}
		}
		return("XXXXXXXX");
	}
	else {
		return rpt5usernameStrXref[index];
	}
	
}

var msTeacherColors = [ "#4bb2c5", "#EAA228", "#c5b47f", "#579575", "#839557", "#958c12", "#953579", "#4b5de4", "#d8b83f", "#ff5800", "#0085cc", "#c747a3", "#cddf54", "#FBD178", "#26B4E3", "#bd70c7",'#FFA500','#4B0082','#ff99cc','#00ffcc'];
//var msTeacherColors = ['#FF0000','#FFFF00','#0000FF','#999966','#008B8B','#FFA500','#4B0082','#008000','#ff99cc','#00ffcc','#999966','#999966','#999966','#999966','#999966','#999966','#999966','#999966','#999966','#999966'];

var greyTeacherColors = ['#999966','#999966','#999966','#999966','#999966','#999966','#999966','#999966','#999966','#999966','#999966','#999966','#999966','#999966','#999966','#999966','#999966','#999966','#999966','#999966'];

var rpt5Colors = [];

function getrpt5Colors() {
	return rpt5Colors;
}



function showReport5() {

	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
	}

	rpt5Colors = [];

    const rb5Populate = document.querySelectorAll('input[name="optRadio5Populate"]');

    for (const rb5p of rb5Populate) {
        if (rb5p.checked) {
        	rpt5Populate = rb5p.value;
            break;
        }
    }
	
	switch(rpt5Populate) {
		case "showSingleWithAnonymous":
			rpt5Colors = Array.from(greyTeacherColors);
			break;
		case "showSingleOnly":
			rpt5Colors = Array.from(msTeacherColors);		
			break;
		default:
			rpt5Colors = Array.from(msTeacherColors);		
		}
	
	
	rpt5usernameXref = [];
	rpt5usernameStrXref = [];
	var trendUnit = 7; 

	
	if (plot5 != null) {
		plot5.destroy();
		plot5 = null;
	}	
	
	var rpt5Weeks = "";
    const rb5weeks = document.querySelectorAll('input[name="optRadio5Weeks"]');

    for (const rb5w of rb5weeks) {
        if (rb5w.checked) {
        	rpt5Weeks = rb5w.value;
            break;
        }
    }
    
	if (rpt5Weeks == "all") {
		trendNumberOfUnits = currentWeek;
		document.getElementById('rpt5PriorWeeks').value = "";
	}
	else {     
	    if (document.getElementById('rpt5PriorWeeks').value != "") { 
	   		trendNumberOfUnits = document.getElementById('rpt5PriorWeeks').value;	   		
	    }
	    else {
	   		trendNumberOfUnits = document.getElementById('settingsPriorWeeks').value;    	
	   		document.getElementById('rpt5PriorWeeks').value = document.getElementById('settingsPriorWeeks').value;
	    }
	}
    
	var startWeek = currentWeek;
	
    var intNumberOfUnits = Number(trendNumberOfUnits);
    if (intNumberOfUnits < 1) {
    	intNumberOfUnits = 1;
    }
    if (intNumberOfUnits > 1) {
    	startWeek = startWeek - intNumberOfUnits;
    }
    
	var trendFilter = "" + startWeek + "~" + trendUnit + "~" + trendNumberOfUnits;
	var teacherFilter = "";
	if (!(currentTeacher.id === "0")) {
		trendFilter += "~" + currentTeacher.id;
		trendFilter += "~" + rpt5Populate;
	}

	var dateArr = [];
	dateArr.push("");
	var startDate = new Date(cohortsArr[currentCohortIndex].cohortStartdate);
	var temp = startWeek * 7;
	var startDate = new Date(addDays(startDate,(temp)));
	var startDateStr = startDate.toLocaleDateString();
/*	
	if (intNumberOfUnits > 1) {
		for (var i = 1; i <= currentWeek-intNumberOfUnits; i++) {
			var tdate = new Date(addDays(startDate,(7*i)));
			startDateStr = tdate.toLocaleDateString();
		}
	}
*/
	var endDateStr = "";
	dateArr.push(startDateStr);
	
	for (var i = 1; i <= intNumberOfUnits; i++) {
		var tdate = new Date(addDays(startDate,(7*i)));
		var dateStr = tdate.toLocaleDateString();
		dateArr.push(dateStr);
		endDateStr = dateStr;
	}

	var rpt5Content = "";
    const rb5Content = document.querySelectorAll('input[name="optRadio5Content"]');

    for (const rb5c of rb5Content) {
        if (rb5c.checked) {
        	rpt5Content = rb5c.value;
            break;
        }
    }
	var rpt5Title = "";
	var rpt5XAxisLabel = '<%= rwrb.getString("weeks_from_current_date") %>';
	var rpt5YAxisLabel = "";
		switch(rpt5Content) {
  		case "Logouts":
			rpt5Title = '<%= rwrb.getString("weekly_teacher_logouts") %>'							
  			break;
  		case "Actions":
			rpt5Title = '<%= rwrb.getString("weekly_teacher_activities") %>'							
			break;
		default:
			rpt5Title = '<%= rwrb.getString("weekly_teacher_logins") %>'							
  		}
	

    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/cohortAdmin",
        data : {
            cohortId: currentCohortId,
            command: 'getCohortRangeActivitySlices',
            lang: loc,
            filter: trendFilter
        },
        success : function(data) {
        	
        	if (data && (data.charAt(0) == '[')) {
            	//alert(data);
            	jsonRangeData = $.parseJSON(data);
            	var jsonWeekArr = [];
				
            	for (var j=0; j<jsonRangeData.length;j++ ) {
                	jsonWeekArr.push(jsonRangeData[j]);
            	
            	}
            	
          	  var teacher0 = [];
        	  var teacher1 = [];
        	  var teacher2 = [];
        	  var teacher3 = [];
        	  var teacher4 = [];
        	  var teacher5 = [];
        	  var teacher6 = [];
        	  var teacher7 = [];
        	  var teacher8 = [];
        	  var teacher9 = [];
        	  var teacher10 = [];
        	  var teacher11 = [];
        	  var teacher12 = [];
        	  var teacher13 = [];
        	  var teacher14 = [];
        	  var teacher15 = [];
        	  var teacher16 = [];
        	  var teacher17 = [];
        	  var teacher18 = [];
        	  var teacher19 = [];
        	  var teacher20 = [];


        	  var teachers = [teacher0, teacher1, teacher2, teacher3, teacher4, teacher5, teacher6, teacher7, teacher8, teacher9, teacher10, teacher11, teacher12, teacher13, teacher14, teacher15, teacher16, teacher17, teacher18, teacher19, teacher20 ];
        	  var teachersFound = [];
        	  
        	  for (var w=0; w < jsonWeekArr.length; w++ ) {
        		  
        	  	  var weekName = "Week" + (w+1);
        	  	  
    			  if ((jsonWeekArr.length > w) && (jsonWeekArr[w][weekName].length > 0)) {
    				  for (i=0;i < jsonWeekArr[w][weekName].length; i++) {
    					  	var count = 0;
    				  		switch(rpt5Content) {
    				  		case "Logouts":
    				  			count = jsonWeekArr[w][weekName][i].logouts;
    				  			break;
    				  		case "Actions":
    				  			count = jsonWeekArr[w][weekName][i].actions;
    							break;
    						default:
    				  			count = jsonWeekArr[w][weekName][i].logins;							
    				  		}
    				  		var username = jsonWeekArr[w][weekName][i].username;
    						var week_date = jsonWeekArr[w][weekName][i].week_date;
    						if (w == 0) {
    							startDateStr = week_date;
    						}
    						else {
    							endDateStr  = week_date;
    						}
     		  		  	    var index = rpt5getUsernameIndex(username);
     		  		  	    if (index >= 0) {
    				  			teachers[index].push([week_date, count]);
    						}
    						else {
    							console.log("index = -1");
    						}
    				  }
    			  }        		  
        	  }
			  for (var t = 0; t<rpt5usernameXref.length; t++) {
				  teachersFound.push(teachers[t]);
			  }
			  var canvas_width = jsonWeekArr.length * 80;
			  if (canvas_width > 1600) {
				  canvas_width =  1600;
			  }
			  if (canvas_width < 600) {
				  canvas_width = 600;
			  }
			  document.getElementById("chart5_canvas").style.width = "" + canvas_width + "px";      	  			  
			  document.getElementById("chart5_div").style.visibility = 'visible';
        	  
        	  plot5 = $.jqplot('chart5_canvas', teachersFound, 
        	    { 
        	      title: rpt5Title, 
        	      // Set default options on all series, turn on smoothing.
        	      seriesDefaults: {
        	          rendererOptions: {
        	              smooth: true
        	          }
        	      },
        	      axes: {
        	          xaxis:{  	         
        	          	label: rpt5XAxisLabel,
	           	        renderer:$.jqplot.DateAxisRenderer, 
        	            min:startDateStr, 
        	            max:endDateStr, 
        	            tickInterval:'1 week',
                        rendererOptions:{
                            tickRenderer:$.jqplot.CanvasAxisTickRenderer
                        },
                    	tickOptions: { 
                        	angle: 15
                    	} 
        	          },
        	          yaxis: {
        	              labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
        	              labelOptions:{
        	                  fontFamily:'Helvetica'
        	                  //fontSize: '14pt'
        	              },
        	              min: 0,  
        	              tickInterval: 5, 
        	              tickOptions: { 
        	                      formatString: '%d' 
        	              }, 
        	              label: rpt5YAxisLabel
        	          }
        	      },
        	      
        	      legend: {
        	    	  show: true,
        	    	  placement: 'outsideGrid'
        	    	},
        	      // Series options are specified as an array of objects, one object
        	      // for each series.
				  seriesColors: getrpt5Colors(),
        	      series:[ 
        	          {
        	            // Change our line width and use a diamond shaped marker.
        	            label: rpt5getUsernameDisplay(0),
        	            lineWidth:3, 
        	            markerOptions: { style:'circle' }
        	          }, 
        	          {
        	            // Don't show a line, just show markers.
        	            // Make the markers 7 pixels with an 'x' style
        	            label: rpt5getUsernameDisplay(1),
        	            lineWidth:3, 
        	            markerOptions: { style:"circle" }
        	          },
        	          { 
        	            // Use (open) circlular markers.
        	            label: rpt5getUsernameDisplay(2),
        	            lineWidth:3, 
        	            markerOptions: { style:"circle" }
        	          }, 
        	          {
        	            // Use a thicker, 5 pixel line and 10 pixel
        	            // filled square markers.
        	            label: rpt5getUsernameDisplay(3),
        	            lineWidth:3, 
        	            markerOptions: { style:"circle" }
        	          },
        	          { 
          	            // Use (open) circlular markers.
        	            label: rpt5getUsernameDisplay(4),
          	            lineWidth:3, 
          	            markerOptions: { style:"circle" }
          	          }, 
        	          { 
          	            // Use (open) circlular markers.
        	            label: rpt5getUsernameDisplay(5),
          	            lineWidth:3, 
          	            markerOptions: { style:"circle" }
          	          }, 
        	          { 
          	            // Use (open) circlular markers.
        	            label: rpt5getUsernameDisplay(6),
          	            lineWidth:3, 
          	            markerOptions: { style:"circle" }
          	          }, 
        	          { 
          	            // Use (open) circlular markers.
        	            label: rpt5getUsernameDisplay(7),
          	            lineWidth:3, 
          	            markerOptions: { style:"circle" }
          	          },
        	          { 
            	            // Use (open) circlular markers.
          	            label: rpt5getUsernameDisplay(8),
            	        lineWidth:3, 
            	        markerOptions: { style:"circle" }
            	      },
        	          { 
          	            // Use (open) circlular markers.
        	            label: rpt5getUsernameDisplay(9),
          	            lineWidth:3, 
          	            markerOptions: { style:"circle" }
          	          },
        	          {
          	            label: rpt5getUsernameDisplay(10),
          	            lineWidth:3, 
          	            markerOptions: { style:'circle' }
          	          }, 
          	          {
          	            // Don't show a line, just show markers.
          	            // Make the markers 7 pixels with an 'x' style
          	            label: rpt5getUsernameDisplay(11),
          	            lineWidth:3, 
          	            markerOptions: { style:"circle" }
          	          },
          	          { 
          	            // Use (open) circlular markers.
          	            label: rpt5getUsernameDisplay(12),
          	            lineWidth:3, 
          	            markerOptions: { style:"circle" }
          	          }, 
          	          {
          	            // Use a thicker, 5 pixel line and 10 pixel
          	            // filled square markers.
          	            label: rpt5getUsernameDisplay(13),
          	            lineWidth:3, 
          	            markerOptions: { style:"circle" }
          	          },
          	          { 
            	            // Use (open) circlular markers.
          	            label: rpt5getUsernameDisplay(14),
           	            lineWidth:3, 
           	            markerOptions: { style:"circle" }
           	          }, 
          	          { 
            	            // Use (open) circlular markers.
          	            label: rpt5getUsernameDisplay(15),
           	            lineWidth:3, 
           	            markerOptions: { style:"circle" }
           	          }, 
          	          { 
            	            // Use (open) circlular markers.
          	            label: rpt5getUsernameDisplay(16),
           	            lineWidth:3, 
           	            markerOptions: { style:"circle" }
           	          }, 
          	          { 
            	            // Use (open) circlular markers.
          	            label: rpt5getUsernameDisplay(17),
           	            lineWidth:3, 
           	            markerOptions: { style:"circle" }
           	          },
          	          { 
              	            // Use (open) circlular markers.
           	            label: rpt5getUsernameDisplay(18),
              	        lineWidth:3, 
              	        markerOptions: { style:"circle" }
              	      },
          	          { 
            	            // Use (open) circlular markers.
          	            label: rpt5getUsernameDisplay(19),
           	            lineWidth:3, 
           	            markerOptions: { style:"circle" }
           	      	  }

            	      
        	      ]
        	    }
        	  );
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("Communication error!");
            console.log(e);
        }
    });
    
    
	   	
}

var rpt6usernameXref = [];
var rpt6usernameStrXref = [];

function rpt6mapUsernameIndex(index,username){

	if (rpt6usernameXref.length == 0) {
		rpt6usernameXref.push(index);
		rpt6usernameStrXref.push(username);
		return 0;
	}
	else {
		for (var i=0; i<rpt6usernameXref.length; i++) {
			if (rpt6usernameXref[i] == index) {
				return i;
			}
			else {
				if (i == (rpt6usernameXref.length - 1))	{			
					rpt6usernameXref.push(index);
					rpt6usernameStrXref.push(username);
					return (i+1);
				}
			}
		}
	}
}

function rpt6getUsernameIndex(username) {

	var teacherArr = allCohortsArr[currentCohortIndex].cohortArr;    	

	for (var i=0;i<teacherArr.length;i++) {

		if (teacherArr[i].userName === username) {			
			return rpt6mapUsernameIndex(i,username);
		}
	}
	return -1;
	
}

var rpt6Populate = "ShowAll";

function rpt6getUsernameDisplay(index) {

	if (rpt6Populate == "showSingleWithAnonymous") {
		for (var i=0;i<8;i++) {		
			if (rpt6usernameStrXref[index] === currentTeacher.username) {
				
				rpt6Colors[index] = msTeacherColors[index];
				return currentTeacher.username;
			}
		}
		return("XXXXXXXX");
	}
	else {
		return rpt6usernameStrXref[index];
	}
	
}

var rpt6Colors = [];

function getrpt6Colors() {
	return rpt6Colors;
}



function showReport6() {

	if (currentCohortId == "") {
		alert('<%= rwrb.getString("must_select_cohort") %>');
	}

	rpt6Colors = [];

    const rb6Populate = document.querySelectorAll('input[name="optRadio6Populate"]');

    for (const rb6p of rb6Populate) {
        if (rb6p.checked) {
        	rpt6Populate = rb6p.value;
            break;
        }
    }
	
	switch(rpt6Populate) {
		case "showSingleWithAnonymous":
			rpt6Colors = Array.from(greyTeacherColors);
			break;
		case "showSingleOnly":
			rpt6Colors = Array.from(msTeacherColors);		
			break;
		default:
			rpt6Colors = Array.from(msTeacherColors);		
		}
	
	rpt6usernameXref = [];
	rpt6usernameStrXref = [];
	var trendUnit = 7; 

	
	if (p1_plot6 != null) {
		p1_plot6.destroy();
		p1_plot6 = null;
	}	
	
	if (p2_plot6 != null) {
		p2_plot6.destroy();
		p2_plot6 = null;
	}	
	
	var rpt6Weeks = "";
    const rb6Weeks = document.querySelectorAll('input[name="optRadio6Weeks"]');

    for (const rb6w of rb6Weeks) {
        if (rb6w.checked) {
        	rpt6Weeks = rb6w.value;
            break;
        }
    }
	
	if (rpt6Weeks == "all") {
		trendNumberOfUnits = currentWeek;
		document.getElementById('rpt6PriorWeeks').value = "";
	}
	else { 
	    if (document.getElementById('rpt6PriorWeeks').value != "") { 
	   		trendNumberOfUnits = document.getElementById('rpt6PriorWeeks').value;
	    }
	    else {
	   		trendNumberOfUnits = document.getElementById('settingsPriorWeeks').value;    	
	   		document.getElementById('rpt6PriorWeeks').value = document.getElementById('settingsPriorWeeks').value;
	    }
	}
    
	var startWeek = currentWeek;
    var intNumberOfUnits = Number(trendNumberOfUnits);
    if (intNumberOfUnits < 1) {
    	intNumberOfUnits = 1;
    }
    if (intNumberOfUnits > 1) {
    	startWeek = startWeek - intNumberOfUnits;
    }
    	
    var trendFilter = "" + startWeek + "~" + trendUnit + "~" + trendNumberOfUnits;
	var teacherFilter = "";
	if (!(currentTeacher.id === "0")) {
		trendFilter += "~" + currentTeacher.id;
		trendFilter += "~" + rpt6Populate;
	}

	
	var dateArr = [];
	dateArr.push("");
	var startDate = new Date(cohortsArr[currentCohortIndex].cohortStartdate);
	var temp = startWeek * 7;
	var startDate = new Date(addDays(startDate,(temp)));
	var startDateStr = startDate.toLocaleDateString();
/*	
	if (intNumberOfUnits > 1) {
		for (var i = 1; i <= currentWeek-intNumberOfUnits; i++) {
			var tdate = new Date(addDays(startDate,(7*i)));
			startDateStr = tdate.toLocaleDateString();
		}
	}
*/
	var endDateStr = "";
	dateArr.push(startDateStr);
	
	for (var i = 1; i <= intNumberOfUnits; i++) {
		var tdate = new Date(addDays(startDate,(7*i)));
		var dateStr = tdate.toLocaleDateString();
		dateArr.push(dateStr);
		endDateStr = dateStr;
	}

	var rpt6Content = "";
    const rb6Content = document.querySelectorAll('input[name="optRadio6Content"]');

    for (const rb6c of rb6Content) {
        if (rb6c.checked) {
        	rpt6Content = rb6c.value;
            break;
        }
    }

	
	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/cohortAdmin",
        data : {
            cohortId: currentCohortId,
            command: 'getCohortRangeTeacherClassSlices',
            lang: loc,
            filter: trendFilter
        },
        success : function(data) {
        	if (data && (data.charAt(0) == '[')) {
            	//alert(data);
            	jsonRangeData = $.parseJSON(data);
            	var jsonWeekArr = [];
				
            	for (var j=0; j<jsonRangeData.length;j++ ) {
                	jsonWeekArr.push(jsonRangeData[j]);
            	
            	}
 
            	var rpt6MaxVal = 20;
            	var rpt6Interval = 1;
            	if (trendNumberOfUnits > 5 ) {
            		rpt6MaxVal = trendNumberOfUnits * 25;
            		rpt6Interval = rpt6MaxVal / 20;
            	}
            	
        	  var p1_teacher0 = [];
        	  var p1_teacher1 = [];
        	  var p1_teacher2 = [];
        	  var p1_teacher3 = [];
        	  var p1_teacher4 = [];
        	  var p1_teacher5 = [];
        	  var p1_teacher6 = [];
        	  var p1_teacher7 = [];
        	  var p1_teacher8 = [];
        	  var p1_teacher9 = [];
        	  var p1_teacher10 = [];
        	  var p1_teacher11 = [];
        	  var p1_teacher12 = [];
        	  var p1_teacher13 = [];
        	  var p1_teacher14 = [];
        	  var p1_teacher15 = [];
        	  var p1_teacher16 = [];
        	  var p1_teacher17 = [];
        	  var p1_teacher18 = [];
        	  var p1_teacher19 = [];
        	  var p1_teacher20 = [];



        	  var p1_teachers = [p1_teacher0, p1_teacher1, p1_teacher2, p1_teacher3, p1_teacher4, p1_teacher5, p1_teacher6, p1_teacher7, p1_teacher8, p1_teacher9, p1_teacher10, p1_teacher11, p1_teacher12, p1_teacher13, p1_teacher14, p1_teacher15, p1_teacher16, p1_teacher17, p1_teacher18, p1_teacher19, p1_teacher20 ];
        	  var p1_teachersFound = [];

        	  var p2_teacher0 = [];
        	  var p2_teacher1 = [];
        	  var p2_teacher2 = [];
        	  var p2_teacher3 = [];
        	  var p2_teacher4 = [];
        	  var p2_teacher5 = [];
        	  var p2_teacher6 = [];
        	  var p2_teacher7 = [];
        	  var p2_teacher8 = [];
        	  var p2_teacher9 = [];
        	  var p2_teacher10 = [];
        	  var p2_teacher11 = [];
        	  var p2_teacher12 = [];
        	  var p2_teacher13 = [];
        	  var p2_teacher14 = [];
        	  var p2_teacher15 = [];
        	  var p2_teacher16 = [];
        	  var p2_teacher17 = [];
        	  var p2_teacher18 = [];
        	  var p2_teacher19 = [];
        	  var p2_teacher20 = [];


        	  var p2_teachers = [p2_teacher0, p2_teacher1, p2_teacher2, p2_teacher3, p2_teacher4, p2_teacher5, p2_teacher6, p2_teacher7, p2_teacher8, p2_teacher9, p2_teacher10, p2_teacher11, p2_teacher12, p2_teacher13, p2_teacher14, p2_teacher15, p2_teacher16, p2_teacher17, p2_teacher18, p2_teacher19, p2_teacher20];
        	  var p2_teachersFound = [];
        	  
        	  for (var w=0; w < jsonWeekArr.length; w++ ) {
        		  
        	  	  var weekName = "Week" + (w+1);
        	  	  
    			  if ((jsonWeekArr.length > w) && (jsonWeekArr[w][weekName].length > 0)) {
    				  for (i=0;i < jsonWeekArr[w][weekName].length; i++) {
    						var username = jsonWeekArr[w][weekName][i].username;
    						var week_date = jsonWeekArr[w][weekName][i].week_date;
    						if (w == 0) {
    							startDateStr = week_date;
    						}
    						else {
    							endDateStr  = week_date;
    						}
     		  		  	    var index = rpt6getUsernameIndex(username);
     		  		  	    if (index >= 0) {
    				  			p1_teachers[index].push([week_date, jsonWeekArr[w][weekName][i].problems_seen]);
    				  			p2_teachers[index].push([week_date, jsonWeekArr[w][weekName][i].problems_solved]);
    						}
    						else {
    							console.log("index = -1");
    						}
    				  }
    			  }        		  
        	  }

        	  for (var t = 0; t<rpt6usernameXref.length; t++) {
				  p1_teachersFound.push(p1_teachers[t]);
				  p2_teachersFound.push(p2_teachers[t]);
			  }
			  var canvas_width = jsonWeekArr.length * 80;
			  if (canvas_width > 1200) {
				  canvas_width =  1200;
			  }
			  if (canvas_width < 600) {
				  canvas_width = 600;
			  }
			  document.getElementById("chart6_canvas1").style.width = "" + canvas_width + "px";      	  			  
			  document.getElementById("chart6_canvas2").style.width = "" + canvas_width + "px";      	  			  
        	  
        	  p1_plot6 = $.jqplot('chart6_canvas1', p1_teachersFound, 
        	    { 
        	      title: '<%= rwrb.getString("weekly_problems_seen") %>', 
        	      // Set default options on all series, turn on smoothing.
        	      seriesDefaults: {
        	          rendererOptions: {
        	              smooth: true
        	          }
        	      },
        	      axes: {
        	          xaxis:{  	         
	           	        renderer:$.jqplot.DateAxisRenderer, 
        	            min:startDateStr, 
        	            max:endDateStr, 
        	            tickInterval:'1 week',
                        rendererOptions:{
                            tickRenderer:$.jqplot.CanvasAxisTickRenderer
                        },
                    	tickOptions: { 
                        	angle: 30
                    	} 
        	          },
        	          yaxis: {
        	              labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
        	              labelOptions:{
        	                  fontFamily:'Helvetica'
        	                  //fontSize: '14pt'
        	              },
        	              min: 0,
        	              max: rpt6MaxVal,
        	              tickInterval: rpt6Interval, 
        	              tickOptions: { 
        	                      formatString: '%d' 
        	              }, 
        	              label: 'Number of Problems Seen'
        	          }
        	      },
        	      
        	      legend: {
        	    	  show: true,
          	    	  location: 'ne',
          	    	  placement: 'outsideGrid'
        	    	},
        	      // Series options are specified as an array of objects, one object
        	      // for each series.
				  seriesColors: getrpt6Colors(),
				  series:[ 
        	          {
        	            // Change our line width and use a diamond shaped marker.
        	            label: rpt6getUsernameDisplay(0),
        	            lineWidth:3, 
        	            markerOptions: { style:'circle' }
        	          }, 
        	          {
        	            // Don't show a line, just show markers.
        	            // Make the markers 7 pixels with an 'x' style
        	            label: rpt6getUsernameDisplay(1),
        	            lineWidth:3, 
        	            markerOptions: { style:"circle" }
        	          },
        	          { 
        	            // Use (open) circlular markers.
        	            label: rpt6getUsernameDisplay(2),
        	            lineWidth:3, 
        	            markerOptions: { style:"circle" }
        	          }, 
        	          {
        	            // Use a thicker, 5 pixel line and 10 pixel
        	            // filled square markers.
        	            label: rpt6getUsernameDisplay(3),
        	            lineWidth:3, 
        	            markerOptions: { style:"circle" }
        	          },
        	          { 
          	            // Use (open) circlular markers.
        	            label: rpt6getUsernameDisplay(4),
          	            lineWidth:3, 
          	            markerOptions: { style:"circle" }
          	          }, 
        	          { 
          	            // Use (open) circlular markers.
        	            label: rpt6getUsernameDisplay(5),
          	            lineWidth:3, 
          	            markerOptions: { style:"circle" }
          	          }, 
        	          { 
          	            // Use (open) circlular markers.
        	            label: rpt6getUsernameDisplay(6),
          	            lineWidth:3, 
          	            markerOptions: { style:"circle" }
          	          }, 
        	          { 
          	            // Use (open) circlular markers.
        	            label: rpt6getUsernameDisplay(7),
          	            lineWidth:3, 
          	            markerOptions: { style:"circle" }
          	          },
        	          { 
          	            // Use (open) circlular markers.
        	            label: rpt6getUsernameDisplay(8),
          	            lineWidth:3, 
          	            markerOptions: { style:"circle" }
          	      	  },
        	          { 
          	            // Use (open) circlular markers.
        	            label: rpt6getUsernameDisplay(9),
          	            lineWidth:3, 
          	            markerOptions: { style:"circle" }
          	          },
        	          {
          	            // Change our line width and use a diamond shaped marker.
          	            label: rpt6getUsernameDisplay(10),
          	            lineWidth:3, 
          	            markerOptions: { style:'circle' }
          	          }, 
          	          {
          	            // Don't show a line, just show markers.
          	            // Make the markers 7 pixels with an 'x' style
          	            label: rpt6getUsernameDisplay(11),
          	            lineWidth:3, 
          	            markerOptions: { style:"circle" }
          	          },
          	          { 
          	            // Use (open) circlular markers.
          	            label: rpt6getUsernameDisplay(12),
          	            lineWidth:3, 
          	            markerOptions: { style:"circle" }
          	          }, 
          	          {
          	            // Use a thicker, 5 pixel line and 10 pixel
          	            // filled square markers.
          	            label: rpt6getUsernameDisplay(13),
          	            lineWidth:3, 
          	            markerOptions: { style:"circle" }
          	          },
          	          { 
            	            // Use (open) circlular markers.
          	            label: rpt6getUsernameDisplay(14),
            	            lineWidth:3, 
            	            markerOptions: { style:"circle" }
            	          }, 
          	          { 
            	            // Use (open) circlular markers.
          	            label: rpt6getUsernameDisplay(15),
            	            lineWidth:3, 
            	            markerOptions: { style:"circle" }
            	          }, 
          	          { 
            	            // Use (open) circlular markers.
          	            label: rpt6getUsernameDisplay(16),
            	            lineWidth:3, 
            	            markerOptions: { style:"circle" }
            	          }, 
          	          { 
            	            // Use (open) circlular markers.
          	            label: rpt6getUsernameDisplay(17),
            	            lineWidth:3, 
            	            markerOptions: { style:"circle" }
            	          },
          	          { 
            	            // Use (open) circlular markers.
          	            label: rpt6getUsernameDisplay(18),
            	            lineWidth:3, 
            	            markerOptions: { style:"circle" }
            	      	  },
          	          { 
            	            // Use (open) circlular markers.
          	            label: rpt6getUsernameDisplay(19),
            	            lineWidth:3, 
            	            markerOptions: { style:"circle" }
            	      }
        	          

          	    ]
        	    }
        	  );

        	  
        	  p2_plot6 = $.jqplot('chart6_canvas2', p2_teachersFound, 
              	    { 
              	      title: '<%= rwrb.getString("weekly_problems_solved") %>', 
              	      // Set default options on all series, turn on smoothing.
              	      seriesDefaults: {
              	          rendererOptions: {
              	              smooth: true
              	          }
              	      },
              	      axes: {
              	          xaxis:{  	         
      	           	        renderer:$.jqplot.DateAxisRenderer, 
              	            min:startDateStr, 
              	            max:endDateStr, 
              	            tickInterval:'1 week',
                              rendererOptions:{
                                  tickRenderer:$.jqplot.CanvasAxisTickRenderer
                              },
                          	tickOptions: { 
                              	angle: 30
                          	} 
              	          },
              	          yaxis: {
              	              labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
              	              labelOptions:{
              	                  fontFamily:'Helvetica'
              	                  //fontSize: '14pt'
              	              },
              	              min: 0,
              	              max: rpt6MaxVal,
              	              tickInterval: rpt6Interval, 
              	              tickOptions: { 
              	                      formatString: '%d' 
              	              }, 
              	              label: 'Number of Problems Solved'
              	          }
              	      },
              	      
              	      legend: {
              	    	  show: true,
              	    	  location: 'ne',
              	    	  placement: 'outsideGrid'
              	    	},
              	      // Series options are specified as an array of objects, one object
              	      // for each series.
              	      seriesColors: getrpt6Colors(),
              	      series:[ 
              	          {
              	            // Change our line width and use a diamond shaped marker.
              	            label: rpt6getUsernameDisplay(0),
              	            lineWidth:3, 
              	            markerOptions: { style:'circle' }
              	          }, 
              	          {
              	            // Don't show a line, just show markers.
              	            // Make the markers 7 pixels with an 'x' style
              	            label: rpt6getUsernameDisplay(1),
              	            lineWidth:3, 
              	            markerOptions: { style:"circle" }
              	          },
              	          { 
              	            // Use (open) circlular markers.
              	            label: rpt6getUsernameDisplay(2),
              	            lineWidth:3, 
              	            markerOptions: { style:"circle" }
              	          }, 
              	          {
              	            // Use a thicker, 5 pixel line and 10 pixel
              	            // filled square markers.
              	            label: rpt6getUsernameDisplay(3),
              	            lineWidth:3, 
              	            markerOptions: { style:"circle" }
              	          },
              	          { 
                	            // Use (open) circlular markers.
              	            label: rpt6getUsernameDisplay(4),
                	            lineWidth:3, 
                	            markerOptions: { style:"circle" }
                	          }, 
              	          { 
                	            // Use (open) circlular markers.
              	            label: rpt6getUsernameDisplay(5),
                	            lineWidth:3, 
                	            markerOptions: { style:"circle" }
                	          }, 
              	          { 
                	            // Use (open) circlular markers.
              	            label: rpt6getUsernameDisplay(6),
                	            lineWidth:3, 
                	            markerOptions: { style:"circle" }
                	          }, 
              	          { 
                	            // Use (open) circlular markers.
              	            label: rpt6getUsernameDisplay(7),
                	        lineWidth:3, 
                	        markerOptions: { style:"circle" }
                	      },
                  	      { 
                    	        // Use (open) circlular markers.
                  	        label: rpt6getUsernameDisplay(8),
                    	        lineWidth:3, 
                    	        markerOptions: { style:"circle" }
                    	  },
                  	      { 
                    	        // Use (open) circlular markers.
                  	        label: rpt6getUsernameDisplay(9),
                    	    lineWidth:3, 
                    	    markerOptions: { style:"circle" }
                    	  },
              	          {
               	            // Change our line width and use a diamond shaped marker.
               	            label: rpt6getUsernameDisplay(0),
               	            lineWidth:3, 
               	            markerOptions: { style:'circle' }
               	          }, 
               	          {
               	            // Don't show a line, just show markers.
               	            // Make the markers 7 pixels with an 'x' style
               	            label: rpt6getUsernameDisplay(1),
               	            lineWidth:3, 
               	            markerOptions: { style:"circle" }
               	          },
               	          { 
               	            // Use (open) circlular markers.
               	            label: rpt6getUsernameDisplay(2),
               	            lineWidth:3, 
               	            markerOptions: { style:"circle" }
               	          }, 
               	          {
               	            // Use a thicker, 5 pixel line and 10 pixel
               	            // filled square markers.
               	            label: rpt6getUsernameDisplay(3),
               	            lineWidth:3, 
               	            markerOptions: { style:"circle" }
               	          },
               	          { 
                 	            // Use (open) circlular markers.
               	            label: rpt6getUsernameDisplay(4),
                 	            lineWidth:3, 
                 	            markerOptions: { style:"circle" }
                 	      }, 
               	          { 
                 	            // Use (open) circlular markers.
               	            label: rpt6getUsernameDisplay(5),
                 	            lineWidth:3, 
                 	            markerOptions: { style:"circle" }
                 	      }, 
               	          { 
                 	            // Use (open) circlular markers.
               	            	label: rpt6getUsernameDisplay(6),
                 	            lineWidth:3, 
                 	            markerOptions: { style:"circle" }
                 	      }, 
                	      { 
                  	           // Use (open) circlular markers.
                	            label: rpt6getUsernameDisplay(7),
                  	        	lineWidth:3, 
                  	        	markerOptions: { style:"circle" }
                  	      },
                    	      { 
                      	        // Use (open) circlular markers.
                    	        label: rpt6getUsernameDisplay(8),
                      	        lineWidth:3, 
                      	        markerOptions: { style:"circle" }
                      	  },
                    	  { 
                      	        // Use (open) circlular markers.
                    	        label: rpt6getUsernameDisplay(9),
                      	    	lineWidth:3, 
                      	   		markerOptions: { style:"circle" }
                      	  }
              	          
              	      ]
              	    }
              	  );

        	  
        	  
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("Communication error!");
            console.log(e);
        }
    });
    
    
	   	
}

function createCohortSlice() {

	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/cohortAdmin",
        data : {
            cohortId: currentCohortId,
            command: 'createCohortSlice',
            lang: loc,
            filter: '2021~11'
        },
        success : function(data) {
        	if (data) {
            	alert(data);
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
    
}

function addCohortFormSubmit() {

	var teacherId =  document.getElementById("addCohortTeacherId").value;
	var classId =  document.getElementById("addCohortClassId").value;
	if ((teacherId.length == 0) || (classId.length == 0)) {
		alert("Must provide a valid teacher Id and a valid class id for that teahcer");
		return;
	}
	var filter = document.getElementById("addCohortName").value;
	filter = filter + "~" + document.getElementById("addCohortSchoolYear").value;
	var startDate = document.getElementById("addCohortStartDate").value;
	if (startDate.length == 0) {
		filter = filter + "~" + "empty";		
	}
	else {
		filter = filter + "~" + document.getElementById("addCohortStartDate").value;
	}
	filter = filter + "~" + teacherId + "~" + classId;

	$.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/cohortAdmin",
        data : {
            cohortId: document.getElementById("newCohortId").value,
            command: 'addNewCohortInfo',
            lang: loc,
            filter: filter
        },
        success : function(data) {
        	if (data) {
        		if (data.substring(0,5) == "error") {        	
            		alert(data);
        		}
        		else {        			
        			alert(document.getElementById("addCohortName").value + " added successfully");
        			$('#addCohortFormModalPopup').modal('hide');        			        			
        		}
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
    
}

function editCohortFormSubmit() {

	var filter = document.getElementById("cohortName").value;
	filter = filter + "~" + document.getElementById("cohortSchoolYear").value;
	var startDate = document.getElementById("cohortStartDate").value;
	if (startDate.length == 0) {
		filter = filter + "~" + "empty";		
	}
	else {
		filter = filter + "~" + document.getElementById("cohortStartDate").value;
	}

	var endDate = document.getElementById("cohortEndDate").value;
	if (endDate.length == 0) {
		filter = filter + "~" + "empty";		
	}
	else {
		filter = filter + "~" + document.getElementById("cohortEndDate").value;
	}

	$.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/cohortAdmin",
        data : {
            cohortId: currentCohortId,
            command: 'updateCohortInfo',
            lang: loc,
            filter: filter
        },
        success : function(data) {
        	if (data) {
        		if (data.substring(0,5) == "error") {        	
            		alert(data);
        		}
        		else {
        			alert(document.getElementById("cohortName").value + " changed successfully");
        			$('#editCohortFormModalPopup').modal('hide');         			
        		}
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
    
}

function adminCohortTeachers(filter) {

	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/cohortAdmin",
        data : {
            cohortId: currentCohortId,
            command: 'adminCohortTeachers',
            lang: loc,
            filter: filter
        },
        success : function(data) {
        	if (data) {
            	alert(data);
            	refreshLocalData();
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
    
}


function adminCohortClasses(filter) {

	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/cohortAdmin",
        data : {
            cohortId: currentCohortId,
            command: 'adminCohortClasses',
            lang: loc,
            filter: filter
        },
        success : function(data) {
        	if (data) {
            	alert(data);
            	refreshLocalData();
            }
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
    
}



function updateCohortSlice() {
	
	
	alert('deprecated');
	return;

	var updateCohortFilter = currentCohortDateArr[currentWeek] + "~7~1"
	
	$.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/cohortAdmin",
        data : {
            cohortId: currentCohortId,
            command: 'updateAllCohortSlicesTeacherActivity',
            lang: loc,
            filter: updateCohortFilter
        },
        success : function(data) {
        	if (data) {
            	alert(data);
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
 
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/cohortAdmin",
        data : {
            cohortId: currentCohortId,
            command: 'updateAllCohortClassStudentSlices',
            lang: loc,
            filter: updateCohortFilter
        },
        success : function(data) {
        	if (data) {
            	alert(data);
        	}
        	else {
        		alert('<%= rwrb.getString("response_data_null") %>');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });    
}

function updateAllCohortSlices() {

	var updateCohortFilter = "";
	
	var pwd = prompt("Enter magic word");
	if (pwd != "keylimepie") {
		alert("The magic word is not " + pwd);
		return;
	}

	if (currentCohortId == "") {	
		alert('<%= rwrb.getString("must_select_cohort") %>');
		return;
	}
	
	
	
//	if (currentCohortId == "5") {
		
		if (!(cohortsArr[currentCohortIndex].cohortEnddate == noEndDate)) {		
			alert(cohortsArr[currentCohortIndex].cohortName +  " has ended.  No more updates allowed.");
			return;
		}

		updateCohortFilter = currentCohortDateArr[currentCohortIndex] + "~7~" + currentWeek;
	    
		$('#admin3').find('.loader').show();
	    $.ajax({
	        type : "POST",
	        url : pgContext+"/tt/tt/cohortAdmin",
	        data : {
	            cohortId: currentCohortId,
	            command: 'updateAllCohortSlicesTeacherActivity',
	            lang: loc,
	            filter: updateCohortFilter
	        },
	        success : function(data) {
	        	if (data) {
	            	alert(data);
	        	    $.ajax({
	        	        type : "POST",
	        	        url : pgContext+"/tt/tt/cohortAdmin",
	        	        data : {
	        	            cohortId: currentCohortId,
	        	            command: 'updateAllCohortClassStudentSlices',
	        	            lang: loc,
	        	            filter: updateCohortFilter
	        	        },
	        	        success : function(data) {
	        	        	$('#admin3').find('.loader').hide();
	        	        	if (data) {
	        	            	alert(data);
	        	        	}
	        	        	else {
	        	        		alert('<%= rwrb.getString("response_data_null") %>');
	        	        	}
	        	        },
	        	        error : function(e) {
	        	        	$('#admin3').find('.loader').hide();
	        	        	alert("error");
	        	            console.log(e);
	        	        }
	        	    });
	        	}
	        	else {
	        		alert('<%= rwrb.getString("response_data_null") %>');
	        	}
	        },
	        error : function(e) {
	        	$('#admin3').find('.loader').hide();
	        	alert("error");
	            console.log(e);
	        }
	    });
	    
	
//	}    


}

</script>

<script type="text/javascript">
	var jan_name = "<%=rb.getString("January")%>";
	var feb_name = "<%=rb.getString("February")%>";
	var mar_name = "<%=rb.getString("March")%>";
	var apr_name = "<%=rb.getString("April")%>";
	var may_name = "<%=rb.getString("May")%>";
	var jun_name = "<%=rb.getString("June")%>";
	var jul_name = "<%=rb.getString("July")%>";
	var aug_name = "<%=rb.getString("August")%>";
	var sep_name = "<%=rb.getString("September")%>";
	var oct_name = "<%=rb.getString("October")%>";
	var nov_name = "<%=rb.getString("November")%>";
	var dec_name = "<%=rb.getString("December")%>";

	var sun_name = "<%=rb.getString("Sun")%>";
	var mon_name = "<%=rb.getString("Mon")%>";
	var tue_name = "<%=rb.getString("Tue")%>";
	var wed_name = "<%=rb.getString("Wed")%>";
	var thu_name = "<%=rb.getString("Thu")%>";
	var fri_name = "<%=rb.getString("Fri")%>";
	var sat_name = "<%=rb.getString("Sat")%>";
	
	</script>


<div class="container-fluid tab-content">
  <h2>Researcher Workbench</h2>

  <ul class="nav nav-tabs">
<!-- <li class="active"><a data-toggle="tab" href="#home" onclick="gotoSettingsPane();">Home</a></li>  -->   
    <li><a data-toggle="tab" id="settings"  href="#Settings" onclick="gotoSettingsPane();"><%= rwrb.getString("settings") %></a></li>
    <li><a data-toggle="tab" id="li-population" class="li-disabled"   href="#Population"><%= rwrb.getString("status_and_population") %></a></li>
    <li><a data-toggle="tab" id="li-ttActivityReports" class="li-disabled"  href="#ttActivityReports"><%= rwrb.getString("teacher_tools_activities") %></a></li>
    <li><a data-toggle="tab" id="li-classroomTrends" class="li-disabled"  href="#classroomTrends"><%= rwrb.getString("classroom_activities") %></a></li>
    <li><a data-toggle="tab" id="li-classroomDashboard" class="li-disabled"  class="li-disabled" href="#classroomDashboard"><%= rwrb.getString("classroom_dashboard") %></a></li>
    <li onclick="launchReportCard();"><a data-toggle="tab"id="li-reportCard" class="li-disabled"   ><%= rwrb.getString("class_report_card") %></a></li>
    <li><a data-toggle="tab"  href="#MSViewerTools">MS Viewer Tools</a></li>
    <li><a data-toggle="tab"  href="#CohortAdminTools">Cohort <%= rwrb.getString("admin_tools") %></a></li>
    <li><a data-toggle="tab"  href="#Help">Help</a></li>
    <li>
        <a id="logout_selector" href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/logout"><i
                class="fa fa-fw fa-power-off"></i><%= rb.getString("log_out") %></a>
    </li>
  </ul>

  <div id="content" class="tab-content">
    <div id="home" class="tab-pane fade in active">
      <h3>HOME</h3>
      <p></p>
    </div>
    <div id="Settings" class="container col-sm-12 tab-pane fade">
      	<h3><%= rwrb.getString("settings") %></h3>
        <p><%= rwrb.getString("settings_selection_msg1") %></p>
		<br>
		<label for="setname">other</label>
	    <input type="text" class="form-control" id="setname" placeholder="Set Name" name="uname" required>
		<br>
		<div id="selections">
			<div class="row">
				<br>
				<div id="cohortSelect" class="offset-md-2 col-sm-3 border">
				</div>
				<div id="teacherSelect" class="col-sm-3 border">
				</div>
				<div id="classSelect" class="col-sm-3 border">
				</div>
			</div>
			<br>
			<hr>			
       		<H3><%= rwrb.getString("settings_selection_msg2") %></H3>                            
           	<div class="row">
				<div class="form-group">
				    <div class="offset-md-3 col-md-3 pull-left">
						<label class="radio-inline"><input id="radioWeeksAll"  value="all"  type="radio" name="optRadioWeeks"><%= rwrb.getString("show_from_beginning") %></label>
				    </div>
				    <div class="offset-md-2 col-md-3 pull-left">
						<label class="radio-inline"><input id="radioWeekSelect"  value="select"  type="radio" name="optRadioWeeks" checked><%= rwrb.getString("show_prior_weeks") %></label>
				    </div>
				    <div class="offset-md-1 col-md-2 pull-left">
						<input type="text" maxlength="2" size="2" class="form-control" style="width: 50px;" onblur="validatePriorWeeks('settingsPriorWeeks')"; id="settingsPriorWeeks" value="4">
				    </div>
				</div>
			</div>			
			<div class="row"  style="display: none;">
	           	<div id="chooseDateRange">
	                 <div class="col-md-2 offset-md-1">                       
	             		<button type="button" class="btn btn-primary" onclick="initCalendar_r1_cal1();initCalendar_r1_cal2();$('#calendarModalPopupOne').modal('show');" ><%= rb.getString("choose_date_range") %></button>
	             	</div>
	                 <div class="col-md-3">                       
		    			<input id="daysFilterOne" style="width:220px" type="text" name="" value="" >   
	             	</div>
				</div>  
			</div>	

		</div>
		<br>
		<div class="row">
<!--		
			<button type="recall" class="btn btn-primary col-sm-2 col-sm-offset-2">Recall Settings</button>
			<button type="save" class="btn btn-primary col-sm-2 col-sm-offset-1">Save Settings</button>
-->			
			<button type="clear" class="btn btn-danger col-sm-2 col-sm-offset-4" onclick="clearSettings()";">Clear Settings</button>
		</div>
	</div>
	
	
	
	
    <div id="ttActivityReports" class="col-sm-12 tab-pane fade">
        <h2 class="page-header">
            <%= rwrb.getString("teacher_tools_activities") %>
        </h2>
		<div class="row">
			<div id="chartsCohortName">				
			</div>
			<div id="chartsCohortWeeks">				
			</div>
		</div>			
<!--
		<div class="row"  style="display: visible;">
           	<div id="chooseDateRange">
                 <div class="col-md-2 offset-md-1">                       
             		<button type="button" class="btn btn-primary" onclick="initCalendar_r1_cal1();initCalendar_r1_cal2();$('#calendarModalPopupOne').modal('show');" ><%= rb.getString("choose_date_range") %></button>
             	</div>
                 <div class="col-md-3">                       
	    			<input id="daysFilterOne" style="width:220px" type="text" name="" value="" >   
             	</div>
			</div>  
		</div>	
		<br>
-->		

        <div class="row hidden">                           
			<div class="form-group  report_filters">
			    <div class="offset-md-1 col-md-3 pull-left">
					<label class="radio-inline"><input id="radioChartsWeeksAll"  value="all"  type="radio" name="optRadioChartsWeeks"><%= rwrb.getString("show_from_beginning") %></label>
			    </div>
			    <div class="offset-md-2 col-md-3 pull-left">
					<label class="radio-inline"><input id="radioChartsWeekSelect"  value="select"  type="radio" name="optRadioChartsWeeks" checked><%= rwrb.getString("show_prior_weeks") %></label>
			    </div>
			    <div class="offset-md-1 col-md-2 pull-left">
					<input type="text" maxlength="2" size="2" class="form-control" style="width: 50px;" onblur="validatePriorWeeks('chartsPriorWeeks')"; id="chartsPriorWeeks">
			    </div>
			</div>
		</div>
		<br>

        <div id="content-container" class="container-fluid">

            <div id="report-wrapper" class="row" width: 100%;">

                <div class="panel-group" id="teacherGroup">
                  

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_six" class="accordion-toggle" data-toggle="collapse" data-parent="#teacherGroup" href="#chart1">
                                    <%= rb.getString("teacher_log_report") %>
                                </a>
                               	<button type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chart1" class="panel-collapse collapse">  
                            <div class="panel-body"> 
							  <div class="row justify-content-center">
							    <div id="chartUsername" class="col-4">
							    </div>
							  </div>
                            </div>
                            <div class="panel-body report_filters">                           
								  <input id="showReportBtn1" class="btn btn-lg btn-primary" onclick="showReport1();" type="submit" value="<%= rb.getString("show_report") %>">
                            </div>
 
                            <div class="panel-body">
                                <table id="perTeacherReport" class="table table-striped table-bordered hover display nowrap" width="100%"></table>
                            </div>

                        </div>
                    </div>
                    
                    
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_2" class="accordion-toggle" data-toggle="collapse" data-parent="#teacherGroup" href="#chartTwo">
                                    <%= rwrb.getString("teacher_activity_metrics") %>
                                </a>
                               	<button id="Button2" type="button" class="close" onclick="$('.collapse').collapse('hide');">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartTwo" class="panel-collapse collapse" >  
                            <div class="panel-body report_filters">                           
								  <input id="showReport2Btn" class="btn btn-lg btn-primary" onclick="showReport2();" type="submit" value="<%= rwrb.getString("show_chart") %>">
                            </div>
                            <div id="chart2_div" class="panel-body">
				            	<div id="chart2_canvas" style="width:800px; height:600px;"></div> 
                            </div>

                        </div>
                    </div>

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_2b" class="accordion-toggle" data-toggle="collapse" data-parent="#teacherGroup" href="#chartTwoB">
                                    <%= rwrb.getString("teacher_logins") %>
                                </a>
                               	<button id="Button2b" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartTwoB" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								  <input id="showReport2bBtn" class="btn btn-lg btn-primary" onclick="showReport2b();" type="submit" value="<%= rwrb.getString("show_chart") %>">
                            </div>
 
                             <div id="chart2b_div" class="panel-body">
				            	<div id="chart2b_canvas" style="width:800px; height:600px;"></div> 
                            </div>

                        </div>
                    </div>


                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_2c" class="accordion-toggle" data-toggle="collapse" data-parent="#teacherGroup" href="#chartTwoC">
                                   <%= rwrb.getString("days_since_last_logins") %> 
                                </a>
                               	<button id="Button2c" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartTwoC" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								  <input id="showReport2cBtn" class="btn btn-lg btn-primary" onclick="showReport2c();" type="submit" value="<%= rwrb.getString("show_chart") %>">
                            </div>
 
                            <div id="chart2c_div" class="panel-body">
				            	<div id="chart2c_canvas" style="width:800px; height:600px;"></div> 
                            </div>

                        </div>
                    </div>

                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_5" class="accordion-toggle" data-toggle="collapse" data-parent="#teacherGroup" href="#chartFive">
                                    <%= rwrb.getString("teacher_activities_weekly") %>
                                </a>
                               	<button id="Button5" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartFive" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">
                            	<div class="row">                           
								<div class="form-group">
								    <div class="offset-md-1 col-md-3 pull-left">
										<label class="radio-inline"><input id="radio5WeeksAll"  value="all"  type="radio" name="optRadio5Weeks"><%= rwrb.getString("show_from_beginning") %></label>
								    </div>
								    <div class="offset-md-2 col-md-3 pull-left">
										<label class="radio-inline"><input id="radio5WeekSelect"  value="select"  type="radio" name="optRadio5Weeks" checked><%= rwrb.getString("show_prior_weeks") %></label>
								    </div>
								    <div class="offset-md-1 col-md-2 pull-left">
										<input type="text" maxlength="2" size="2" class="form-control" style="width: 50px;" onblur="validatePriorWeeks('rpt5PriorWeeks')"; id="rpt5PriorWeeks">
								    </div>
								</div>
								</div>
								<br>
                            	<div class="row">                           
									<div class="form-group">
										<label class="radio-inline"><input id="radioLogins"  value="Logins" type="radio" name="optRadio5Content" checked><%= rwrb.getString("logins") %></label>
										<label class="radio-inline"><input id="radioActions" value="Actions" type="radio" name="optRadio5Content"><%= rwrb.getString("actions") %></label>
										<label class="radio-inline"><input id="radioLogouts" value="Logouts" type="radio" name="optRadio5Content"><%= rwrb.getString("logouts") %></label>
									</div>
								</div>                            
                            	<div id="rpt5PopulateSelect" class="row">      
									<div class="form-group">
										<label class="radio-inline"><input id="radio5ShowAll"                  value="showAll"                 type="radio" name="optRadio5Populate" checked><%= rwrb.getString("show_all") %></label>
										<label class="radio-inline"><input id="radio5ShowSingleOnly"           value="showSingleOnly"          type="radio" name="optRadio5Populate"><%= rwrb.getString("show_single_only") %></label>
										<label class="radio-inline"><input id="radio5ShowSingleWithAnonymous"  value="showSingleWithAnonymous" type="radio" name="optRadio5Populate"><%= rwrb.getString("show_single_with_anon") %></label>
									</div>
								</div>
                            </div>
                            <div class="panel-body report_filters">                           
								  <input id="showReport5Btn" class="btn btn-lg btn-primary" onclick="showReport5();" type="submit" value="<%= rwrb.getString("show_graph") %>">
                            </div>
 
                            <div  id="chart5_div" class="panel-body">
			            		<div id="chart5_canvas" style="width:1000px; height:600px;overflow-x: auto;"></div>
                            </div>

                        </div>
                    </div>




            	</div> <!-- End panel group -->
        	</div>
		</div>
    </div>


    <div id="classroomTrends" class="col-sm-12 tab-pane fade container">
        <h2 class="page-header">
            <%= rwrb.getString("classroom_activities") %>
            <br>
        </h2>
		<div class="row">
			<div id="trendsCohortName">				
			</div>
			<div id="trendsCohortWeeks">				
			</div>
			<div id="trendsTeacherName">				
			</div>
		</div>
<!--         
		<div class="row justify-content-start report_filters">
			<br>
			<div class="form-group">
			    <div class="offset-md-1 col-md-3 pull-left">
					<label class="radio-inline"><input type="radio" name="optRadioWeeks">Show from beginning.</label>
			    </div>
			    <div class="offset-md-2 col-md-3 pull-left">
					<label class="radio-inline"><input type="radio" name="optRadioWeeks" checked>Show [X] prior weeks.</label>
			    </div>
			    <div class="offset-md-1 col-md-2 pull-left">
					<input type="text" maxlength="2" size="2" class="form-control" style="width: 50px;" id="priorWeeks">
			    </div>
			</div>
			<br>
			<br>
		</div>	
		<div class="row"  style="display: visible;">
           	<div id="chooseDateRange">
                 <div class="col-md-2 offset-md-1">                       
             		<button type="button" class="btn btn-primary" onclick="initCalendar_r1_cal1();initCalendar_r1_cal2();$('#calendarModalPopupOne').modal('show');" ><%= rb.getString("choose_date_range") %></button>
             	</div>
                 <div class="col-md-3">                       
	    			<input id="daysFilterOne" style="width:220px" type="text" name="" value="" >   
             	</div>
			</div>  
		</div>	
-->
		<br>
        <div id="trends-container" class="container-fluid">

            <div id="trends-wrapper" class="row" width: 100%;">

                <div class="panel-group" id="classroomTrendsGroup">



                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_tp" class="accordion-toggle" data-toggle="collapse" data-parent="#classroomTrendsGroup" href="#chartTP">
                                   Student Problem Solving Totals by Teacher
                                </a>
                               	<button id="ButtonTP" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>

                        <div id="chartTP" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								  <input id="showReportTPBtn" class="btn btn-lg btn-primary" onclick="showReport_tp();" type="submit" value="<%= rwrb.getString("show_chart") %>">
								  <input id="showTableTPBtn" class="btn btn-lg btn-primary" onclick="showTable_tp();" type="submit" value="<%= rwrb.getString("show_table") %>">
                            </div>
 
                            <div class="panel-body col-md-12">
				            	<div id="tp_canvas" class="col-md-6" style="width:800px; height:500px;"></div> 
				            	<div id="tp_table_panel" class="col-md-6" style="width:600px; height:500px;">
				            	   <table align = "center"
            							id="tp_table" border="1">
    							   </table>
				            	</div> 
                            </div>

                        </div>
                    </div>

                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_tcp" class="accordion-toggle" data-toggle="collapse" data-parent="#classroomTrendsGroup" href="#chartTCP">
                                    Student Problem Solving Totals by Class
                                </a>
                               	<button id="ButtonTCP" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartTCP" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								  <input id="showReportTCPBtn" class="btn btn-lg btn-primary" onclick="showReport_tcp();" type="submit" value="<%= rwrb.getString("show_chart") %>">
								  <input id="showTableTCPBtn" class="btn btn-lg btn-primary" onclick="showTable_tcp();" type="submit" value="<%= rwrb.getString("show_table") %>">
                            </div>
 
                            <div class="panel-body col-md-12">
				            	<div id="tcp_canvas" class="col-md-6" style="width:700px; height:800px;"></div> 
				            	<div id="tcp_table_panel" class="col-md-6" style="width:600px; height:800px;">
				            	   <table align = "center"
            							id="tcp_table" border="1">
    							   </table>
				            	</div> 
                            </div>

                        </div>
                    </div>

                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a class="accordion-toggle" data-toggle="collapse" data-parent="#classroomTrendsGroup" href="#chartThreeC">
                                    Student Problem Solving Effort by Teacher
                                </a>
                               	<button type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartThreeC" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								  <input class="btn btn-lg btn-primary" onclick="showReport3c();" type="submit" value="<%= rwrb.getString("show_chart") %>">
								  <a id="showLegendBtn" class="btn btn-lg btn-primary" role="button" value="show" onclick="showLegend();"><%= rb.getString("show_legend") %></a>
								  <a id="hideLegendBtn" class="btn btn-lg btn-primary" style="display: none" role="button" value="show" onclick="hideLegend();"><%= rb.getString("hide_legend") %></a>
<!-- 						  <input id="showTable3bBtn" class="btn btn-lg btn-primary" onclick="showTable3b();" type="submit" value="<%= rwrb.getString("show_table") %>"> -->		
                            </div>
 
 							<!-- SELECT teacherId, COUNT(*) AS total FROM teacherlog where action = "login" GROUP BY teacherId; -->
                            <div id="chart3c_div1" class="panel-body">
                                <table id="effortLegend" class="table table-striped table-bordered hover" width="40%" style="display: none">
                                    <thead>
                                    <tr>
                                        <th><%= rb.getString("student_effort")%>:</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr><td class="span-SOF"><%= rb.getString("sof") %></td></tr>
                                    <tr><td class="span-ATT"><%= rb.getString("att") %></td></tr>
                                    <tr><td class="span-SHINT"><%= rb.getString("shint") %></td></tr>
                                    <tr><td class="span-SHELP"><%= rb.getString("shelp") %></td></tr>
                                    <tr><td class="span-GUESS"><%= rb.getString("guess") %></td></tr>
                                    <tr><td class="span-NOTR"><%= rb.getString("notr") %></td></tr>
                                    <tr><td class="span-SKIP"><%= rb.getString("skip") %></td></tr>
                                    <tr><td class="span-GIVEUP"><%= rb.getString("giveup") %></td></tr>
                                    <tr><td class="span-NODATA"><%= rb.getString("no_data") %></td></tr>
                                    </tbody>
                                </table>
                            </div>
                            
                            <div id="chart3c_div2" class="panel-body">
                            	<div class="row">
					            	<div id="chart3c_canvas0" class="col-md-3" style="width:200px; height:200px;"></div> 
					            	<div id="chart3c_canvas1" class="col-md-3" style="width:200px; height:200px;"></div> 
					            	<div id="chart3c_canvas2" class="col-md-3" style="width:200px; height:200px;"></div> 
					            	<div id="chart3c_canvas3" class="col-md-3" style="width:200px; height:200px;"></div> 
					            	<div id="chart3c_canvas4" class="col-md-3" style="width:200px; height:200px;"></div> 
					            	<div id="chart3c_canvas5" class="col-md-3" style="width:200px; height:200px;"></div> 
					            	<div id="chart3c_canvas6" class="col-md-3" style="width:200px; height:200px;"></div> 
					            	<div id="chart3c_canvas7" class="col-md-3" style="width:200px; height:200px;"></div> 
					            	<div id="chart3c_canvas8" class="col-md-3" style="width:200px; height:200px;"></div> 
					            	<div id="chart3c_canvas9" class="col-md-3" style="width:200px; height:200px;"></div> 
					            	<div id="chart3c_canvas10" class="col-md-3" style="width:200px; height:200px;"></div> 
					            	<div id="chart3c_canvas11" class="col-md-3" style="width:200px; height:200px;"></div> 
					            	<div id="chart3c_canvas12" class="col-md-3" style="width:200px; height:200px;"></div> 
					            	<div id="chart3c_canvas13" class="col-md-3" style="width:200px; height:200px;"></div> 
					            	<div id="chart3c_canvas14" class="col-md-3" style="width:200px; height:200px;"></div> 
					            	<div id="chart3c_canvas15" class="col-md-3" style="width:200px; height:200px;"></div> 
					            	<div id="chart3c_canvas16" class="col-md-3" style="width:200px; height:200px;"></div> 
					            	<div id="chart3c_canvas17" class="col-md-3" style="width:200px; height:200px;"></div> 
					            	<div id="chart3c_canvas18" class="col-md-3" style="width:200px; height:200px;"></div> 
					            	<div id="chart3c_canvas19" class="col-md-3" style="width:200px; height:200px;"></div> 
				            	</div>
				            	<hr>
                            	<div class="row">
					            	<div class="col-md-5" style="width:450px; height:450px;"></div> 
					            	<div id="chart3c_canvas_footer0" class="col-md-5" style="width:600px; height:450px;"></div> 
				            	</div>
<!--
					            	<div id="table3c_panel" class="col-md-6" style="width:800px; height:800px;">
				            	   <table align = "center"
            							id="table3c" border="1">
    							   </table>
				            	</div>
-->				            	 
                            </div>

                        </div>
                    </div>

                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_tcs" class="accordion-toggle" data-toggle="collapse" data-parent="#classroomTrendsGroup" href="#chartTCS">
                                    Student Sessions in Recent Weeks
                                </a>
                               	<button id="ButtonTCS" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartTCS" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								<div class="form-group">
								    <div class="offset-md-3 col-md-3 pull-left">
										<label class="radio-inline"><input id="tcs_radioWeeksAll"  value="all"  type="radio" name="opt_tcs_RadioWeeks"><%= rwrb.getString("show_from_beginning") %></label>
								    </div>
								    <div class="offset-md-2 col-md-3 pull-left">
										<label class="radio-inline"><input id="tcs_radioWeekSelect"  value="select"  type="radio" name="opt_tcs_RadioWeeks" checked><%= rwrb.getString("show_prior_weeks") %></label>
								    </div>
								    <div class="offset-md-1 col-md-2 pull-left">
										<input type="text" maxlength="2" size="2" class="form-control" style="width: 50px;" onblur="validatePriorWeeks('tcs_PriorWeeks')"; id="tcs_PriorWeeks">
								    </div>
								</div>
                            </div>
                            <div class="panel-body report_filters">                           
								<input id="showReportTCSBtn" class="btn btn-lg btn-primary" onclick="showReport_tcs();" type="submit" value="<%= rwrb.getString("show_chart") %>">
							</div> 
                            <div class="panel-body">
				            	<div id="tcs_canvas" style="width:1200px; height:500px;"></div> 
                            </div>

                        </div>
                    </div>



                    
                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_6" class="accordion-toggle" data-toggle="collapse" data-parent="#classroomTrendsGroup" href="#chartSix">
                                    Teacher Class Problem Solving Results - Weekly Counts
                                </a>
                               	<button id="Button6" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartSix" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">
                            	<div class="row">                           
								<div class="form-group">
								    <div class="offset-md-1 col-md-3 pull-left">
										<label class="radio-inline"><input id="radio6WeeksAll"  value="all"  type="radio" name="optRadio6Weeks"><%= rwrb.getString("show_from_beginning") %></label>
								    </div>
								    <div class="offset-md-2 col-md-3 pull-left">
										<label class="radio-inline"><input id="radio6WeekSelect"  value="select"  type="radio" name="optRadio6Weeks" checked><%= rwrb.getString("show_prior_weeks") %></label>
								    </div>
								    <div class="offset-md-1 col-md-2 pull-left">
										<input type="text" maxlength="2" size="2" class="form-control" style="width: 50px;" onblur="validatePriorWeeks('rpt6PriorWeeks')"; id="rpt6PriorWeeks">
								    </div>
								</div>
								</div>
								<br>
                            	<div class="row">                           
									<div class="form-group hidden">
										<label class="radio-inline"><input id="radioProblemsSeen"  value="ProblemsSeen" type="radio" name="optRadio6Content" checked><%= rwrb.getString("problems_seen") %></label>
										<label class="radio-inline"><input id="radioProblemsSolved" value="ProblemsSolved" type="radio" name="optRadio6Content"><%= rwrb.getString("problems_solved") %></label>
									</div>
								</div>                           
                            	<div class="row">      
									<div class="form-group">
										<label class="radio-inline"><input id="radio6ShowAll"                  value="showAll"                 type="radio" name="optRadio6Populate" checked><%= rwrb.getString("show_all") %></label>
										<label class="radio-inline"><input id="radio6ShowSingleOnly"           value="showSingleOnly"          type="radio" name="optRadio6Populate"><%= rwrb.getString("show_single_only") %></label>
										<label class="radio-inline"><input id="radio6ShowSingleWithAnonymous"  value="showSingleWithAnonymous" type="radio" name="optRadio6Populate"><%= rwrb.getString("show_single_with_anon") %></label>
									</div>
								</div>
                            </div>
                            
                            <div class="panel-body report_filters">                           
								  <input id="showReport6Btn" class="btn btn-lg btn-primary" onclick="showReport6();" type="submit" value="<%= rwrb.getString("show_graph") %>">
                            </div>


                            <div  class="panel-body">
			            		<div id="chart6_canvas1" style="width:1600px; height:420px;overflow-x: hidden;"></div>
			            		<br>
			            		<div id="chart6_canvas2" style="width:1600px; height:420px;overflow-x: hidden;"></div>
                            </div>
                        </div>
                    </div>
            	</div>
        	</div>
		</div>
	</div>

    <div id="classroomDashboard" class="col-sm-12 tab-pane fade container">
        <h2 class="page-header">
            <%= rwrb.getString("classroom_dashboard") %>
        </h2>
		<div class="row">
			<div id="dashboardCohortName">				
			</div>
			<div id="dashboardCohortWeeks">				
			</div>
			<div id="dashboardTeacherName">				
			</div>
		</div>
		<br>
        <div id="dashboard-container" class="container-fluid">

            <div id="dashboard-wrapper" class="row" width: 100%;">

                <div class="panel-group" id="dashboardGroup">

                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a class="accordion-toggle" data-toggle="collapse" data-parent="#dashboardGroup" href="#chartThreeD">
                                    Live Dashboard 
                                </a>
                               	<button type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartThreeD" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								  <input class="btn btn-lg btn-primary" onclick="showReport3d();showReport3e();" type="submit" value="<%= rwrb.getString("show_chart") %>">
								  <a id="showDashboardLegendBtn" class="btn btn-lg btn-primary" role="button" value="show" onclick="showDashboardLegend();"><%= rb.getString("show_legend") %></a>
								  <a id="hideDashboardLegendBtn" class="btn btn-lg btn-primary" style="display: none" role="button" value="show" onclick="hideDashboardLegend();"><%= rb.getString("hide_legend") %></a>
                            </div>
 
                            <div class="panel-body">
                                <table id="dashboardEffortLegend" class="table table-striped table-bordered hover" width="40%" style="display: none">
                                    <thead>
                                    <tr>
                                        <th><%= rb.getString("student_effort")%>:</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr><td class="span-SOF" style="height:10px;padding:0;"><%= rb.getString("sof") %></td></tr>
                                    <tr><td class="span-ATT" style="height:10px;padding:0;"><%= rb.getString("att") %></td></tr>
                                    <tr><td class="span-SHINT" style="height:10px;padding:0;"><%= rb.getString("shint") %></td></tr>
                                    <tr><td class="span-SHELP" style="height:10px;padding:0;"><%= rb.getString("shelp") %></td></tr>
                                    <tr><td class="span-GUESS" style="height:10px;padding:0;"><%= rb.getString("guess") %></td></tr>
                                    <tr><td class="span-NOTR" style="height:10px;padding:0;"><%= rb.getString("notr") %></td></tr>
                                    <tr><td class="span-SKIP" style="height:10px;padding:0;"><%= rb.getString("skip") %></td></tr>
                                    <tr><td class="span-GIVEUP" style="height:10px;padding:0;"><%= rb.getString("giveup") %></td></tr>
                                    <tr><td class="span-NODATA" style="height:10px;padding:0;"><%= rb.getString("no_data") %></td></tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="panel-body">
                            	<div class="row">
				                    <div id="liveDashboardProblemPane" class="col-md-4 " style="background-color:lightgreen; padding: 50px; border: 2px solid #000000; border-radius: 25px;height:500px;">
					            		<div id="chart3e_canvas" class="col-md-4" style="width:300px; height:400px;"></div>
					            	</div> 
				                    <div id="liveDashboardEffortPane" class="col-md-8" style="background-color:powderblue; border: 2px solid #000000; border-radius: 25px; height:500px;">
				                        <div style="text-align:center;"> <h2>Problem Solving Effort</h2></div>
						            	<div id="chart3d_canvas" class="col-md-5" style="width:400px; height:400px;"></div> 
						            	<div id="chart3d_legend" class="col-md-7"> 		            	
						            	     <table class="table table-striped table-bordered hover">
			                                    <tbody>
			                                    <tr><td class="span-SOF"    style="height:8px;padding:0;"><h5><%= rb.getString("sof") %></h5></td></tr>
			                                    <tr><td class="span-ATT"    style="height:8px;padding:0;"><h5><%= rb.getString("att") %></h5></td></tr>
			                                    <tr><td class="span-SHINT"  style="height:8px;padding:0;"><h5><%= rb.getString("shint") %></h5></td></tr>
			                                    <tr><td class="span-SHELP"  style="height:8px;padding:0;"><h5><%= rb.getString("shelp") %></h5></td></tr>
			                                    <tr><td class="span-GUESS"  style="height:8px;padding:0;"><h5><%= rb.getString("guess") %></h5></td></tr>
			                                    <tr><td class="span-NOTR"   style="height:8px;padding:0;"><h5><%= rb.getString("notr") %></h5></td></tr>
			                                    <tr><td class="span-SKIP"   style="height:8px;padding:0;"><h5><%= rb.getString("skip") %></h5></td></tr>
			                                    <tr><td class="span-GIVEUP" style="height:8px;padding:0;"><h5><%= rb.getString("giveup") %></h5></td></tr>
			                                    <tr><td class="span-NODATA" style="height:8px;padding:0;"><h5><%= rb.getString("no_data") %></h5></td></tr>
			                                    </tbody>
			                                </table>
						            	</div>
					            	</div> 
				            	</div>
                            </div>
                        </div>
                    </div>
            	</div>
        	</div>
		</div>
	</div>


    <div id="Population" class="col-sm-12 tab-pane fade container">
        <h2 class="page-header">
            <%= rwrb.getString("status_and_population") %>
            <br>
        </h2>
		<div class="row">
			<div id="populationCohortName">				
			</div>
		</div>
		<br>
        <div id="population-container" class="container-fluid">

            <div id="population-wrapper" class="row" width: 100%;">



                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_tpsa" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#chartTPSA">
                                   Study Status Snapshot
                                </a>
                               	<button id="ButtonTPSA" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>

                        <div id="chartTPSA" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								  <input id="showTableTPSABtn" class="btn btn-lg btn-primary" onclick="showTable_tpsa();" type="submit" value="<%= rwrb.getString("show_table") %>">
                            </div>
 
                            <div class="panel-body col-md-12">
                            
	             			<div id="tpsa-panel" class="container-fluid" style="display:none;width: 100%; overflow-x: scroll;">
				               	<div class="row">
					             	<div class="col-md-12">
						            	<div class= "tt-tpsa-header">
						            	    <table align = "left"	id="tpsa_header_table" border="1">
					            	    		<thead id="tpsa-thead">
				  								</thead>
				  							</table>
						            	</div> 
					                </div>
				            	</div>
				               	<div class="row">
					             	<div class="col-md-12">
						            	<div class= "tt-tpsa-body">
						            	    <table align = "left"	id="tpsa_body_table" border="1">
					            	    		<tbody id="tpsa-tbody">
					            	    		</tbody>
				  							</table>
						            	</div> 
					                </div>
				            	</div>
                            
                            </div>

                        </div>
                    </div>


             </div>




                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_3" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#chartThree">
                                    Teacher Class Count
                                </a>
                               	<button id="Button3" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartThree" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								  <input id="showReport3Btn" class="btn btn-lg btn-primary" onclick="showReport3();" type="submit" value="<%= rwrb.getString("show_chart") %>">
                            </div>
 
 							<!-- SELECT teacherId, COUNT(*) AS total FROM teacherlog where action = "login" GROUP BY teacherId; -->
                            <div id="chart3_div" class="panel-body">
				            	<div id="chart3_canvas" style="width:800px; height:400px;"></div> 
                            </div>

                        </div>
                    </div>

                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_3a" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#chartThreeA">
                                    Teacher Student Counts 
                                </a>
                               	<button id="Button3a" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartThreeA" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								  <input id="showReport3aBtn" class="btn btn-lg btn-primary" onclick="showReport3a();" type="submit" value="<%= rwrb.getString("show_chart") %>">
								  <input id="showTable3aBtn" class="btn btn-lg btn-primary" onclick="showTable3a();" type="submit" value="<%= rwrb.getString("show_table") %>">
                            </div>
 
 							<!-- SELECT teacherId, COUNT(*) AS total FROM teacherlog where action = "login" GROUP BY teacherId; -->
                            <div id="chart3a_div" class="panel-body">
				            	<div id="chart3a_canvas" class="col-md-6" style="width:800px; height:800px;"></div> 
				            	<div id="table3a_panel" class="col-md-6" style="width:800px; height:800px;">
				            	   <table align = "center"
            							id="table3a" border="1">
    							   </table>
				            	</div> 
                            </div>

                        </div>
                    </div>

                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_3b" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#chartThreeB">
                                    Class Student Counts 
                                </a>
                               	<button id="Button3b" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartThreeB" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								  <input id="showReport3bBtn" class="btn btn-lg btn-primary" onclick="showReport3b();" type="submit" value="<%= rwrb.getString("show_chart") %>">
								  <input id="showTable3bBtn" class="btn btn-lg btn-primary" onclick="showTable3b();" type="submit" value="<%= rwrb.getString("show_table") %>">
                            </div>
 
 							<!-- SELECT teacherId, COUNT(*) AS total FROM teacherlog where action = "login" GROUP BY teacherId; -->
                            <div id="chart3b_div" class="panel-body">
				            	<div id="chart3b_canvas" class="col-md-6" style="width:800px; height:800px;"></div> 
				            	<div id="table3b_panel" class="col-md-6" style="width:800px; height:800px;">
				            	   <table align = "center"
            							id="table3b" border="1">
    							   </table>
				            	</div> 
                            </div>

                        </div>
                    </div>

                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_3f" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#chartThreeF">
                                    Student Census 
                                </a>
                               	<button id="Button3f" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartThreeF" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								  <input id="showTable3fBtn" class="btn btn-lg btn-primary" onclick="showTable3f();" type="submit" value="<%= rwrb.getString("show_table") %>">
                            </div>
 
                            <div class="panel-body">
				            	<div id="table3f_panel" class="col-md-12" style="width:1200px; height:700px;overflow-x: auto;overflow-y: auto;">
				            	   <table align = "center"
            							id="table3f" border="1">
    							   </table>
				            	</div> 
                            </div>

                        </div>
                    </div>


            	</div>
        	</div>
		</div>


	    <div id="MSViewerTools" class="col-sm-12 tab-pane fade container">
	    	<h3>MS Support Tools</h3>
			<div class="row">
				<div id="tablesCohortName">				
				</div>
			</div>
			<br>
	        <div id="tables-container" class="container-fluid">
	
	            <div id="tables-wrapper" class="row" width: 100%;">
	
	                   <div class="panel panel-default">
	                        <div class="panel-heading">
	                            <h4 class="panel-title">
	                                <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#table_4b">
	                                    Errors Reported by Students 
	                                </a>
	                               	<button type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
	                            </h4>
	                        </div>
                            <div id="table_4b" class="panel-collapse collapse">  
	                            <div class="panel-body report_filters">
	                            	<div class="row">                           
										<div class="form-group">
											<label class="radio-inline"><input id="radioErrorsDate"  value="date" type="radio" name="optRadio4bContent" checked>Order By Date</label>
											<label class="radio-inline"><input id="radioErrorsClassId" value="classId" type="radio" name="optRadio4bContent">Order By Class Id</label>
											<label class="radio-inline"><input id="radioErrorsProdlemId" value="problemId" type="radio" name="optRadio4bContent">Order By Problem Id</label>
										</div>
									</div>                            
	                            </div>	                        
	                            <div class="panel-body report_filters">                           
									  <input class="btn btn-lg btn-primary" onclick="showTable4b();" type="submit" value="<%= rwrb.getString("show_table") %>">
	                            </div>
	                            <div id="table4b-loader" class="loader" style="display: none"></div>
	 
	                            <div class="panel-body">
					            	<div id="table_4b_panel" class="col-md-12" style="width:1200px; height:800px;overflow-x: auto;overflow-y: auto;">
					            	   <table align = "center"
	            							id="table4b" border="1">
	    							   </table>
					            	</div> 
	                            </div>
	
	                        </div>
	                    </div>
	
	                   <div class="panel panel-default">
	                        <div class="panel-heading">
	                            <h4 class="panel-title">
	                                <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#table_4c">
	                                    Teacher Feedback 
	                                </a>
	                               	<button type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
	                            </h4>
	                        </div>
                            <div id="table_4c" class="panel-collapse collapse">  
	                            <div class="panel-body report_filters">                           
	                            	<div class="row">                           
										<div class="form-group">
											<label class="radio-inline"><input id="radioFeedbackDate"  value="teacherId" type="radio" name="optRadio4cContent" checked>Order By Teacher Id</label>
											<label class="radio-inline"><input id="radioFeedbackPriority" value="priority" type="radio" name="optRadio4cContent">Order By Priority</label>
											<label class="radio-inline"><input id="radioFeedbackDate" value="date" type="radio" name="optRadio4cContent">Order By Date</label>
										</div>
									</div>    
								</div>                           
								<div class="panel-body report_filters"> 
									  <input class="btn btn-lg btn-primary" onclick="showTable4c();" type="submit" value="<%= rwrb.getString("show_table") %>">
	                            </div>
	                            <div id="table4c-loader" class="loader" style="display: none"></div>
	 
	                            <div class="panel-body">
					            	<div id="table_4c_panel" class="col-md-12" style="width:1200px; height:800px;overflow-x: auto;overflow-y: auto;">
					            	   <table align = "center"
	            							id="table4c" border="1">
	    							   </table>
					            	</div> 
	                            </div>
	
	                        </div>
	                    </div>
	
	
	
	                    
	
	            	</div>
	        	</div>
<!--
	      	<p>Tabular formats for export</p>
				<label for="uname">paste your query here</label>
			    <input type="text" class="form-control" id="uname" placeholder="Enter other" name="uname" required>
				<div class="dropdown">
				  <button class="btn btn-basic dropdown-toggle" type="button" data-toggle="dropdown">Select output format
				  <span class="caret"></span></button>
				  <ul class="dropdown-menu">
				    <li><a href="#">CSV</a></li>
				    <li><a href="#">Excel</a></li>
				    <li><a href="#">PDF</a></li>
				  </ul>
				</div>
  -->				
	    </div>
	
	
	
	    <div id="CohortAdminTools" class="col-sm-12 tab-pane fade container">
			<div class="row">
				<div id="adminCohortName">				
				</div>
			</div>			
	        <h2 class="page-header">
	            <%= rwrb.getString("admin_tools") %>
	        </h2>
	
	        <div id="admin-container" class="container-fluid">
	
	            <div id="admin-wrapper" class="row" width: 100%;">
	
	                <div class="panel-group" id="adminCommands">

	                    <div  id="editCohort" class="panel panel-default">
	                        <div class="panel-heading">
	                            <h4 class="panel-title">
	                                <a id="admin_four" class="accordion-toggle" data-toggle="collapse" data-parent="#adminCommands" href="#admin4">
	                                    <%= rwrb.getString("add_change_cohort") %>
	                                </a>
	                               	<button type="button" class="close" onclick="$('.collapseAdmin').collapse('hide')">&times;</button>                             
	                            </h4>
	                        </div>
	                        <div id="admin4" class="panel-collapse collapse">  
	                            <div class="panel-body report_filters">                           
									  <input class="btn btn-lg btn-primary" onclick="addCohortForm();" type="submit" value="<%= rwrb.getString("add") %>">
									  <input class="btn btn-lg btn-primary" onclick="editCohortForm();" type="submit" value="<%= rwrb.getString("change") %>">
	                            </div>
	 
	                            <div class="panel-body">
	                                <div id="admin4Form" class="table table-striped table-bordered hover display nowrap" width="100%">
	
	                                </div>
	                            </div>	
	                        </div>
	                    </div>                   
	                    
	                    <div  id="addCohortTeacher" class="panel panel-default">
	                        <div class="panel-heading">
	                            <h4 class="panel-title">
	                                <a id="admin_five" class="accordion-toggle" data-toggle="collapse" data-parent="#adminCommands" href="#admin5">
	                                    <%= rwrb.getString("add_remove_cohort_teacher") %>
	                                </a>
	                               	<button type="button" class="close" onclick="$('.collapseAdmin').collapse('hide')">&times;</button>                             
	                            </h4>
	                        </div>
	                        <div id="admin5" class="panel-collapse collapse">  
	                            <div class="panel-body report_filters">                           
									  <input class="btn btn-lg btn-primary" onclick="editCohortTeachers('add');" type="submit" value="<%= rwrb.getString("add") %>">
									  <input class="btn btn-lg btn-primary" onclick="editCohortTeachers('remove');" type="submit" value="<%= rwrb.getString("remove") %>">
	                            </div>
	 
	                            <div class="panel-body">
	                                <div id="admin5Form" class="table table-striped table-bordered hover display nowrap" width="100%">
	
	                                </div>
	                            </div>
	
	                        </div>
	                    </div>                   
	                                       
	                    <div  id="addCohortClass" class="panel panel-default">
	                        <div class="panel-heading">
	                            <h4 class="panel-title">
	                                <a id="admin_six" class="accordion-toggle" data-toggle="collapse" data-parent="#adminCommands" href="#admin6">
	                                    <%= rwrb.getString("add_remove_cohort_classes") %>
	                                </a>
	                               	<button type="button" class="close" onclick="$('.collapseAdmin').collapse('hide')">&times;</button>                             
	                            </h4>
	                        </div>
	                        <div id="admin6" class="panel-collapse collapse">  
	                            <div class="panel-body report_filters">                           
									  <input class="btn btn-lg btn-primary" onclick="editCohortClasses('add');" type="submit" value="<%= rwrb.getString("add") %>">
									  <input class="btn btn-lg btn-primary" onclick="editCohortClasses('remove');" type="submit" value="<%= rwrb.getString("remove") %>">
	                            </div>
	 
	                            <div class="panel-body">
	                                <div id="admin6Form" class="table table-striped table-bordered hover display nowrap" width="100%">
	
	                                </div>
	                            </div>
	
	                        </div>
	                    </div>                   

	                    <div  id="updateAllSlices" class="panel panel-default">
	                        <div class="panel-heading">
	                            <h4 class="panel-title">
	                                <a id="admin_three" class="accordion-toggle" data-toggle="collapse" data-parent="#adminCommands" href="#admin3">
	                                    <%= rwrb.getString("update_all_cohort_slices") %>
	                                </a>
	                               	<button type="button" class="close" onclick="$('.collapseAdmin').collapse('hide')">&times;</button>                             
	                            </h4>
	                        </div>
	                        <div id="admin3" class="panel-collapse collapse">  
	                            <div class="panel-body report_filters">                           
									  <input id="admin3Btn" class="btn btn-lg btn-primary" onclick="updateAllCohortSlices();" type="submit" value="<%= rb.getString("submit") %>">
	                            </div>
	                            <div class="loader" style="display: none"></div>
	 
	                            <div class="panel-body">
	                                <div id="admin3Status" class="table table-striped table-bordered hover display nowrap" width="100%">
	
	                                </div>
	                            </div>
	
	                        </div>
	                    </div>                   

	                   <div class="panel panel-default">
	                        <div class="panel-heading">
	                            <h4 class="panel-title">
	                                <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#table_4a">
	                                    Teacher Class Weekly Data Table
	                                </a>
	                               	<button type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
	                            </h4>
	                        </div>
	                        <div id="table_4a" class="panel-collapse collapse">  
	                            <div class="panel-body report_filters">                           
									  <input class="btn btn-lg btn-primary" onclick="showTable4a();" type="submit" value="<%= rwrb.getString("show_table") %>">
	                            </div>
	 
	                            <div class="panel-body">
					            	<div id="table_4a_panel" class="col-md-12" style="width:1600px; height:800px;overflow-x: auto;overflow-y: auto;">
					            	   <table align = "center"
	            							id="table4a" border="1">
	    							   </table>
					            	</div> 
	                            </div>
	
	                        </div>
	                    </div>
	

	                    
	            	</div>
	        	</div>
			</div>
	  </div>
	  
	  
	    <div id="Help" class="col-sm-12 tab-pane fade container">
	        <h2 class="page-header">
	            Help
	        </h2>
	
	        <div id="help-container" class="container-fluid">
	
	            <div id="help-wrapper" class="row" width: 100%;">
	
	                <div class="panel-group" id="helpCommands">

	                    <div  id="helpCohort" class="panel panel-default">
	                        <div class="panel-heading">
	                            <h4 class="panel-title">
	                                <a id="hrlp_one" class="accordion-toggle" data-toggle="collapse" data-parent="#helpCommands" href="#help1">
	                                    Display Cohort Help
	                                </a>
	                               	<button type="button" class="close" onclick="$('.collapseHelp').collapse('hide')">&times;</button>                             
	                            </h4>
	                        </div>
	                        <div id="help1" class="panel-collapse collapse">  
	                            <div class="panel-body report_filters">                           
	                            </div>
	 
	                            <div class="panel-body">
	                            	<div class="col-md-3"></div>
	                            	<div class="col-md-6">
										<div class="dropdown">
										  <button class="btn btn-basic dropdown-toggle" type="button" data-toggle="dropdown">Select Topic
										  <span class="caret"></span></button>
										  <ul class="dropdown-menu">
										    <li onclick="launchCreateCohortHelp();"><a id="createCohortHelpLink"></a>Creating a New Cohort</li>
										    <li onclick="launchCohortSlicesHelp();"><a id="cohortSlicesHelpLink"></a>About Cohort Slices</li>
										  </ul>
										</div>
									</div>
	                            	<div class="col-md-3"></div>
	                            </div>	
	                        </div>
	                    </div>                   	                    
	            	</div>
	        	</div>
			</div>
	  </div>
	  
	  
	  
	  
	  
	  
	  
	  
	    <div id="Notifications" class="col-sm-12 tab-pane fade">
	      <h3>Notifications</h3>
	      <p>Tools used to support Mathspring</p>
			<div class="dropdown">
			  <button type="button" class="btn btn-basic dropdown-toggle" data-toggle="dropdown">
			    Notification Tools
			  </button>
			  <div class="dropdown-menu">
			    <a class="dropdown-item" href="#">Modify MS Global settings</a><br>
			    <a class="dropdown-item" href="#">Create a cohort</a><br>
			    <a class="dropdown-item" href="#">What else</a><br>
			  </div>
			</div>
	    </div>


    <div id="classViews" class="col-md-12 hidden">
			<div class="dropdown">
			  <button class="btn btn-basic dropdown-toggle" type="button" data-toggle="dropdown">Select View
			  <span class="caret"></span></button>
			  <ul class="dropdown-menu">
			    <li onclick="launchReportCard();"><a id="reportCardLink"</a>Class Report Card</li>
			  </ul>
			</div>
    </div>


</div>


</div>


<div id="showSessionProblemsModalPopup" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog modal-lg">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
            	<h3><div id="table4b_session_hdr"></div></h3>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
	           	<div id="table_4b_session_panel" class="col-md-12" style="width:900px; height:400px;overflow-x: auto;overflow-y: auto;">
	           	   <table align = "center"
	       				id="table4b_session" border="1">
					</table>
	           	</div>           	
            </div>
            <br>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal"><%= rb.getString("close") %></button>
            </div>
        </div>

    </div>
</div>

<div id="updateProblemStatusModalPopup" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
            	<h3><div id="table4b_update_hdr"></div></h3>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
				<div>
					<label for="message-text" ><%= rb.getString("message")%>:</label>
					<br>
					<textarea id="message-text" name="message" style="width:500px; height:200px;overflow-x: auto;overflow-y: auto;"></textarea>
				</div>
				<div class="form-group">
					<label class="radio-inline"><input id="radio4bReported"  value="Reported" type="radio" name="optRadio4bContent">Reported</label>
					<label class="radio-inline"><input id="radio4bBroken"  value="Broken" type="radio" name="optRadio4bContent">Broken</label>
					<label class="radio-inline"><input id="radio4bFixed"   value="Fixed" type="radio" name="optRadio4bContent">Fixed</label>
					<label class="radio-inline"><input id="radio4bIgnore"  value="Ignore" type="radio" name="optRadio4bContent">Ignore</label>
				</div>                            
				<div> 
					<input type="hidden" id="hiddentEventId">
					<input type="hidden" id="hiddenHistoryId">
				</div>
            </div>
            <div class="modal-footer">
				<button type="button" class="btn btn-success" onclick="updateProblemStatusSubmit();"><%= rb.getString("submit")%></button>
                <button type="button" class="btn btn-primary" data-dismiss="modal"><%= rb.getString("close") %></button>
            </div>
        </div>

    </div>
</div>

<div id="addCohortFormModalPopup" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
            	<div id="add_cohort_hdr">Update Cohort header</div>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
				<div>
					<label for="newCohortId" style="width:100px">Cohort Id:</label><input id="newCohortId" name="newCohortId" style="width:300px readonly"></input>
				</div>
				<div>
					<label for="addCohortName" style="width:100px">Cohort Name:</label><input id="addCohortName" name="addCohortName" style="width:300px"></input>
				</div>
				<div>			
					<label for="addCohortSchoolYear" style="width:100px">School Year:</label><input id="addCohortSchoolYear" name="addCohortSchoolYear"style="width:60px"></input>
				</div>
				<div>
					<label for="addCohortStartDate" style="width:100px">Start Date:</label><input id="addCohortStartDate" name="addCohortStartDate"style="width:200px"></input>
				</div>
				<div>
					<label for="addCohortTeacherId" style="width:100px">A Teacher Id:</label><input id="addCohortTeacherId" name="addCohortTeacherId"style="width:200px"></input>
				</div>
				<div>
					<label for="addCohortClassId" style="width:100px">A Class Id:</label><input id="addCohortClassId" name="addCohortClassId"style="width:200px"></input>
				</div>
            </div>
            <div class="modal-footer">
				<button type="button" class="btn btn-success" onclick="addCohortFormSubmit();"><%= rb.getString("submit")%></button>
                <button type="button" class="btn btn-primary" data-dismiss="modal"><%= rb.getString("close") %></button>
            </div>
        </div>
    </div>
</div>


<div id="editCohortFormModalPopup" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
            	<h3><div id="edit_cohort_hdr"></div></h3>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
				<div>
					<label for="cohortName" style="width:100px">Cohort Name:</label><input id="cohortName" name="cohortName" style="width:300px"></input>
				</div>
				<div>			
					<label for="cohortSchoolYear" style="width:100px">School Year:</label><input id="cohortSchoolYear" name="cohortSchoolYear"style="width:60px"></input>
				</div>
				<div>
					<label for="cohortStartDate" style="width:100px">Start Date:</label><input id="cohortStartDate" name="cohortStartDate"style="width:200px"></input>
				</div>
				<div>
					<label for="cohortEndDate" style="width:100px">End Date:</label><input id="cohortEndDate" name="cohortEndDate"style="width:200px"></input>
				</div>
            </div>
            <div class="modal-footer">
				<button type="button" class="btn btn-success" onclick="editCohortFormSubmit();"><%= rb.getString("submit")%></button>
                <button type="button" class="btn btn-primary" data-dismiss="modal"><%= rb.getString("close") %></button>
            </div>
        </div>
    </div>
</div>

<div id="cohortHelpModalPopup" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
            	<h3><div id="cohort_help_hdr" style="center-text"></div></h3>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div id="cohortHelpPopupBody"class="modal-body">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal"><%= rb.getString("close") %></button>
            </div>

        </div>
    </div>
</div>

<div id="calendarModalPopupOne" class="modal fade" data-backdrop="static" data-keyboard="false" role="dialog" style="display: none;">
    <div class="modal-dialog modal-lg">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="row">
            <div class="modal-body" role="dialog">
			     <div class="wrapper-calender col-sm-6">
			      <div class="container-calendar">
                        <input type="hidden" id="selectDay_r1_cal1" name="selectDay_r1_cal1">
   				      <div><h3><%= rb.getString("least_recent") %>:</h3></div>
			          <div class="button-container-calendar">
			              <div class=col-md-2><button id="previous_r1_cal1" onclick="previous_r1_cal1()">&#8249;&#8249;</button></div>
       							  <div class=col-md-8 center-text><h3 id="monthAndYear_r1_cal1"></h3></div>
			              <div class=col-md-2><button id="next_r1_cal1" onclick="next_r1_cal1()">&#8250;&#8250;</button></div>							          
			          </div>
			          
			          <table class="table-calendar" id="calendar_r1_cal1" data-lang="en">
			              <thead id="thead-month_r1_cal1"></thead>
			              <tbody id="calendar-body_r1_cal1"></tbody>
			          </table>
			          
			          <div class="footer-container-calendar">
			              <label for="month_r1_cal1"><%= rb.getString("jump_to") %>: </label>
			              <select id="month_r1_cal1" onchange="jump_r1_cal1()">
			                  <option value=0><%= rb.getString("Jan") %></option>
			                  <option value=1><%= rb.getString("Feb") %></option>
			                  <option value=2><%= rb.getString("Mar") %></option>
			                  <option value=3><%= rb.getString("Apr") %></option>
			                  <option value=4><%= rb.getString("May") %></option>
			                  <option value=5><%= rb.getString("Jun") %></option>
			                  <option value=6><%= rb.getString("Jul") %></option>
			                  <option value=7><%= rb.getString("Aug") %></option>
			                  <option value=8><%= rb.getString("Sep") %></option>
			                  <option value=9><%= rb.getString("Oct") %></option>
			                  <option value=10><%= rb.getString("Nov") %></option>
			                  <option value=11><%= rb.getString("Dec") %></option>
			              </select>
			              <select id="year_r1_cal1" onchange="jump_r1_cal1()">
			                  <option value=2020>2020</option>
			                  <option value=2021>2021</option>
			                  <option value=2022>2022</option>			              
			              </select>       
			          </div>
			      </div>			      
			    </div> 
			    <div class="wrapper-calender col-sm-6">
			      <div class="container-calendar">
                        <input type="hidden" id="selectDay_r1_cal2" name="selectDay_r1_cal2">
				      <div><h3><%= rb.getString("most_recent") %>:</h3></div>
			          <div class="button-container-calendar">
			              <div class=col-md-2><button id="previous_r1_cal2" onclick="previous_r1_cal2()">&#8249;&#8249;</button></div>
       							  <div class=col-md-8 center-text><h3 id="monthAndYear_r1_cal2"></h3></div>
			              <div class=col-md-2><button id="next_r1_cal2" onclick="next_r1_cal2()">&#8250;&#8250;</button></div>							          
			          </div>
			          
			          <table class="table-calendar" id="calendar_r1_cal2" data-lang="en">
			              <thead id="thead-month_r1_cal2"></thead>
			              <tbody id="calendar-body_r1_cal2"></tbody>
			          </table>
			          
			          <div class="footer-container-calendar">
			              <label for="month_r1_cal2"><%= rb.getString("jump_to") %>: </label>
			              <select id="month_r1_cal2" onchange="jump_r1_cal2()">
			                  <option value=0><%= rb.getString("Jan") %></option>
			                  <option value=1><%= rb.getString("Feb") %></option>
			                  <option value=2><%= rb.getString("Mar") %></option>
			                  <option value=3><%= rb.getString("Apr") %></option>
			                  <option value=4><%= rb.getString("May") %></option>
			                  <option value=5><%= rb.getString("Jun") %></option>
			                  <option value=6><%= rb.getString("Jul") %></option>
			                  <option value=7><%= rb.getString("Aug") %></option>
			                  <option value=8><%= rb.getString("Sep") %></option>
			                  <option value=9><%= rb.getString("Oct") %></option>
			                  <option value=10><%= rb.getString("Nov") %></option>
			                  <option value=11><%= rb.getString("Dec") %></option>
			              </select>
			              <select id="year_r1_cal2" onchange="jump_r1_cal2()">
			                  <option value=2020>2020</option>
			                  <option value=2021>2021</option>
			                  <option value=2022>2022</option>			              
			              </select>       
			          </div>			 
			        </div>
            	</div>
            </div>
            </div>
           <div class="modal-footer">

          		<div class="offset-md-6">
	                <button type="button" class="btn btn-success" onclick="getFilterOne();" ><%= rb.getString("submit") %></button>
	                <button type="button" class="btn btn-danger" data-dismiss="modal" onclick="$('#calendarModalPopupOne').modal('hide');" ><%= rb.getString("cancel") %></button>
                </div> 
         </div>
    	</div>
	</div>
</div>	


</body>
    <script type="text/javascript" src="<c:url value="/js/calendar_r1_1.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/calendar_r1_2.js" />"></script>
</html>

