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

 if (lang.equals("es")) {
 	loc = new Locale("es","AR");	
 }
 else {
 	loc = new Locale("en","US");	
 }	
 System.out.println(loc.toString());

 
ResourceBundle rb = null;
try {
	rb = ResourceBundle.getBundle("MathSpring",loc);
}
catch (Exception e) {
//	logger.error(e.getMessage());	
}
%>

<!DOCTYPE HTML>
<html>

<head>
  <title>Researcher Workbench</title>
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
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/plugins/jqplot.highlighter.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/plugins/jqplot.pointLabels.js" />"></script>

  	<link href="${pageContext.request.contextPath}/css/ttStyleMain.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet">
  	<link href="${pageContext.request.contextPath}/css/calendar.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/jqplot2021/jquery.jqplot.css" />

</head>
<body>


<style>

div.scroll {
    overflow-x: auto;
    overflow-y: hidden;
}

.content {
  	background-color: lightblue;
}

.report_filters {
	color: #000000;
  	background-color: lightblue;
  	font-size: 16px;
	
}

#selections {
    height: 500px;
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

</style>



<script>
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

var currentTeachersArr = [];

var trendDate = '7';
var trendUnit = "week";
var trendNumberOfUnits = "1";

var currentTeacher = new Object();
var currentClass = "";

var currentWeek = 1;

var filterOne = "";
var headers = "";

var plot2 = null;
var plot2b = null;
var plot2c = null;
var plot3 = null;
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
		alert("Must select a teacher on the settings page");
		isValid = false;
	}
	if ((currentClass == null) || (currentClass == "")) {
		alert("Must select a class on the settings page");
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
	var cohortsDiv = '<label for="cohortList">Select cohort</label> <select name="cohortList" id="cohortList" class="form-control selectpicker" onblur="handleCohortSelect(event);" data-show-subtext="true" data-live-search="true" size="5" style="width: 270px;"></select>';  				
	document.getElementById('cohortSelect').innerHTML = cohortsDiv;
	currentCohortId = -1;
	
 	var teachersDiv = '<label for="teacherList">Select teacher</label>';  
    document.getElementById('teacherSelect').innerHTML = teachersDiv;

 	var classesDiv = '<label for="classList">Select class</label>';  
    document.getElementById('classSelect').innerHTML = classesDiv;

    document.getElementById('chartUsername').innerHTML = "Please select a teacher from the Settings page";

    showCohorts();
}

function clearTeachers() {

	var teachersDiv = '<label for="teacherList">Select teacher</label>';  
    document.getElementById('teacherSelect').innerHTML = teachersDiv;

 	var classesDiv = '<label for="classList">Select class</label>';  
    document.getElementById('classSelect').innerHTML = classesDiv;

    document.getElementById('chartUsername').innerHTML = "Please select a teacher from the Settings page";
    document.getElementById('trendsTeacherName').innerHTML = "";
    
    document.getElementById('rpt5PopulateSelect').style.visibility = 'hidden';

}


function gotoSettingsPane() {

 	var teachersDiv = '<label for="teacherList">Select teacher</label>';  
    document.getElementById('teacherSelect').innerHTML = teachersDiv;

 	var classesDiv = '<label for="classList">Select class</label>';  
    document.getElementById('classSelect').innerHTML = classesDiv;

    document.getElementById('chartUsername').innerHTML = "Please select a teacher from the Settings page";
    document.getElementById('trendsTeacherName').innerHTML = "";

    showCohorts();
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
//	var temp = startWeek * 7;
//	var startDate = new Date(addDays(startDate,(temp)));
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
	const msToday = Date.now();        	
   	var msStartDate = new Date(cohortsArr[currentCohortIndex].cohortStartdate);
   	currentWeekRaw = ((msToday - msStartDate)  / 7);
   	currentWeekRaw = (currentWeekRaw / (1000 * 3600 * 24)) + 1;
   	currentWeek = Math.ceil(currentWeekRaw);
   	
   	console.log("currentWeek = " + currentWeek);
   	
	document.getElementById('chartsCohortName').innerHTML = "<h3>" + cohortsArr[currentCohortIndex].cohortName + " (Week # " + currentWeek + ")</h3>";
	document.getElementById('trendsCohortName').innerHTML = "<h3>" + cohortsArr[currentCohortIndex].cohortName + " (Week # " + currentWeek + ")</h3>";
	document.getElementById('adminCohortName').innerHTML = "<h3>" + cohortsArr[currentCohortIndex].cohortName + "</h3>";
      
    showTeachers();

    currentCohortDateArr = [];
   	currentCohortDateArr = buildCurrentCohortDateArr();

    
}



function showTeachers() {
	
    currentTeacher.id = "0";
    currentTeacher.username = "";
	var teacherArr = allCohortsArr[currentCohortIndex];    	
 	var teachersDiv = '<label for="teacherList">Select teacher</label> <select name="teacherList" id="teacherList" class="form-control selectpicker" onblur="handleTeacherSelect(event);" data-show-subtext="true" data-live-search="true" size="5" style="width: 270px;">';  

 	var prevname = "";
    for (var i=0;i<teacherArr.cohortArr.length;i++) {
		var name = "" + teacherArr.cohortArr[i].userName;
		if (name != prevname) {
			var id = "" + teacherArr.cohortArr[i].ID;
        	var teacherStr = '<option id="teacher' + id + '" value="' + id + '~' + name + '">' + name + '</option>';
        	teachersDiv = teachersDiv + teacherStr;
        	prevname = name;
		}
	}
   	teachersDiv = teachersDiv + '</select>';  				
    document.getElementById('teacherSelect').innerHTML = teachersDiv;
    document.getElementById('rpt5PopulateSelect').style.visibility = 'hidden';
        	
}

function handleTeacherSelect(event) {

    var selectElement = event.target;
    var value = "" + selectElement.value;
    var splitter = value.split("~");
    currentTeacher.id = splitter[0];
    currentTeacher.username = splitter[1];
    document.getElementById('chartUsername').innerHTML = "<h3>" + currentTeacher.username + "</h3>";
    document.getElementById('trendsTeacherName').innerHTML = "<h3>" + currentTeacher.username + "</h3>";
    document.getElementById('rpt5PopulateSelect').style.visibility = 'visible';
    
    showClasses();
}

function showClasses() {
	
	var teacherArr = allCohortsArr[currentCohortIndex];    	
 	var classesDiv = '<label for="classList">Select class</label> <select name="classList" id="classList" class="form-control selectpicker" onblur="handleClassSelect(event);" data-show-subtext="true" data-live-search="true" size="5" style="width: 270px;">';  

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
    classesDiv = classesDiv + '</select>';  				
    document.getElementById('classSelect').innerHTML = classesDiv;
//    document.getElementById('classSelect').style.visibility = "visible";
        	
    console.log(classesDiv);
}



function handleClassSelect(event) {

    var selectElement = event.target;
    currentClass = Number(selectElement.value);
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
        		alert("response data is null");
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

	//SELECT teacherlog.teacherId, userName as uname, COUNT(*) AS total FROM teacher, teacherlog, teacher_map_cohort where teacherlog.teacherId = teacher_map_cohort.teacherid and teacher.id = teacherlog.teacherId and action = "login" GROUP BY teacherId;

	
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
            	        {label:'Logins'}
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
        		alert("response data is null");
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

	//SELECT teacherlog.teacherId, userName as uname, COUNT(*) AS total FROM teacher, teacherlog, teacher_map_cohort where teacherlog.teacherId = teacher_map_cohort.teacherid and teacher.id = teacherlog.teacherId and action = "login" GROUP BY teacherId;

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
            	        {label:'Logouts'},
            	        {label:'Actions'},
            	        {label:'Logins'}
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
        		alert("response data is null");
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
	// For horizontal bar charts, x an y values must will be "flipped"
    // from their vertical bar counterpart.
    

}

function showReport2b() {

	//SELECT teacherlog.teacherId, userName as uname, COUNT(*) AS total FROM teacher, teacherlog, teacher_map_cohort where teacherlog.teacherId = teacher_map_cohort.teacherid and teacher.id = teacherlog.teacherId and action = "login" GROUP BY teacherId;

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
            		ticks.push(jsonData[i].lname);
            	}
            	var s1 = [];

            	for (var i=0;i<jsonData.length;i = i + 1) {
            		s1.push(jsonData[i].logins);
            	}
                // var ticks = ['Paul', 'John', 'George', 'Ringo'];

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
                        	label: 'Teachers'
             			},
	                    yaxis: {
			 	            pad: 1.05,
            				min: 0,  
            				tickInterval: 10, 
            				tickOptions: { 
                				formatString: '%d'
            				}, 
            				label: 'Number of Logins'
               			}
                    },


                });

        	}
        	else {
        		alert("response data is null");
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
	// For horizontal bar charts, x an y values must will be "flipped"
    // from their vertical bar counterpart.
    
}

function showReport2c() {

	//SELECT teacherlog.teacherId, userName as uname, COUNT(*) AS total FROM teacher, teacherlog, teacher_map_cohort where teacherlog.teacherId = teacher_map_cohort.teacherid and teacher.id = teacherlog.teacherId and action = "login" GROUP BY teacherId;

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
            		ticks.push(jsonData[i].lname);
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
                        	label: 'Teachers'

                        	
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
            				label: 'Days Since Last Login'
               			}
                    },


                });
                
        		}
        	}
        	else {
        		alert("response data is null");
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
	// For horizontal bar charts, x an y values must will be "flipped"
    // from their vertical bar counterpart.
    
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

	if (plot3 != null) {
		plot3.destroy();
		plot3 = null;
	}	
	
		
	countTeachersClasses();
	
	var s1 = [];
	var ticks = [];
	
	
	var line = [];
	for (var i=0;i<teacherClassCountArr.length;i++) {
		var splitter = 	teacherClassCountArr[i].split('~');
		var count = Number(splitter[1]);
		
		ticks.push(splitter[0]);
		s1.push(count);
	}
	
	var s2 = [460, -210, 690, 820];
	var s3 = [-260, -440, 320, 200];
	// Can specify a custom tick Array.
	// Ticks should match up one for each y value (category) in the series.
//	var ticks = ['May', 'June', 'July', 'August'];
	
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
            	label: 'Teachers'
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
            	label: 'Classes'

	        }
	    }
	});
}



function showReport_tcp() {

	//SELECT teacherlog.teacherId, userName as uname, COUNT(*) AS total FROM teacher, teacherlog, teacher_map_cohort where teacherlog.teacherId = teacher_map_cohort.teacherid and teacher.id = teacherlog.teacherId and action = "login" GROUP BY teacherId;
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
            	var jsonData = $.parseJSON(data);
            	console.log(data);
            	var ticks = [];
            	for (var i=0;i<jsonData.length;i = i + 1) {
            		
            		var className = jsonData[i].username + ":" + jsonData[i].classId;
            		ticks.push(className);
            	}
            	var theSeries = [];
            	var row1 = [];
            	var row2 = [];
            	var row3 = [];
             
            	for (var i=0;i<jsonData.length;i = i + 1) {            	
            		var element1 = [];
            		var element2 = [];
            		var element3 = [];
            		element1.push(jsonData[i].nbr_problems_seen);
            		element1.push(i+1);
            		row1.push(element1);
            		element2.push(jsonData[i].nbr_problems_solved);
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
//            	        {label:'Skipped'},
            	        {label:'Solved'},
            	        {label:'Seen'}
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
        		alert("response data is null");
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
	// For horizontal bar charts, x an y values must will be "flipped"
    // from their vertical bar counterpart.
    

}

function showReport_tp() {

	//SELECT teacherlog.teacherId, userName as uname, COUNT(*) AS total FROM teacher, teacherlog, teacher_map_cohort where teacherlog.teacherId = teacher_map_cohort.teacherid and teacher.id = teacherlog.teacherId and action = "login" GROUP BY teacherId;
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
            	var jsonData = $.parseJSON(data);
            	console.log(data);
            	var ticks = [];
            	for (var i=0;i<jsonData.length;i = i + 1) {
            		
            		var teacherName = jsonData[i].username;
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
            		element1.push(jsonData[i].nbr_problems_seen);
            		element1.push(i+1);
            		row1.push(element1);
            		element2.push(jsonData[i].nbr_problems_solved);
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

            	plot_tcp = $.jqplot('tp_canvas', theSeries, {

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
//            	        {label:'Skipped'},
            	        {label:'Solved'},
            	        {label:'Seen'}
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
        		alert("response data is null");
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
	// For horizontal bar charts, x an y values must will be "flipped"
    // from their vertical bar counterpart.
    

}



function showReport_tcs() {

	
	var tcsFilter = '' + currentWeek;
	
	//SELECT teacherlog.teacherId, userName as uname, COUNT(*) AS total FROM teacher, teacherlog, teacher_map_cohort where teacherlog.teacherId = teacher_map_cohort.teacherid and teacher.id = teacherlog.teacherId and action = "login" GROUP BY teacherId;
	if (plot_tcs != null) {
		plot_tcs.destroy();
		plot_tcs = null;
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
            		
            		var className = jsonData[i].username + ":" + jsonData[i].classId;
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
                    seriesColors:['#66ccff'],
            	    series:[
//            	        {label:'Skipped'},
//            	        {label:'Solved'},
            	        {label:'Active Students'}
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
        		alert("response data is null");
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
	// For horizontal bar charts, x an y values must will be "flipped"
    // from their vertical bar counterpart.
    

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

var msTeacherColors = ['#FF0000','#FFFF00','#0000FF','#999966','#008B8B','#FFA500','#4B0082','#008000','#ff99cc','#00ffcc'];

var greyTeacherColors = ['#999966','#999966','#999966','#999966','#999966','#999966','#999966','#999966','#999966','#999966'];

var rpt5Colors = [];

function getrpt5Colors() {
	return rpt5Colors;
}



function showReport5() {

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
		trendNumberOfUnits = currentWeek - 1;
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
    
	var startWeek = currentWeek - 1;
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
	var rpt5XAxisLabel = 'Weeks from current date';
	var rpt5YAxisLabel = "";
		switch(rpt5Content) {
  		case "Logouts":
			rpt5Title = 'Weekly Teachers Logouts'							
  			break;
  		case "Actions":
			rpt5Title = 'Weekly Teachers Activities'							
			break;
		default:
			rpt5Title = 'Weekly Teachers Logins'							
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


        	  var teachers = [teacher0, teacher1, teacher2, teacher3, teacher4, teacher5, teacher6, teacher7, teacher8, teacher9, teacher10];
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
			  if (canvas_width > 1200) {
				  canvas_width =  1200;
			  }
			  if (canvas_width < 320) {
				  canvas_width = 320;
			  }
			  document.getElementById("chart5_canvas").style.width = "" + canvas_width + "px";      	  			  
        	  
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
          	          }
        	          
        	      ]
        	    }
        	  );
        	}
        	else {
        		alert("Data unavailable.");
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
		trendNumberOfUnits = currentWeek - 1;
		document.getElementById('rpt6PriorWeeks').value = "";
	}
	else { 
	    if (document.getElementById('rpt6PriorWeeks').value != "") { 
	   		trendNumberOfUnits = document.getElementById('rpt6PriorWeeks').value;
	    }
	    else {
	   		trendNumberOfUnits = document.getElementById('settingsPriorWeeks').value;    	
	   		trendNumberOfUnits = document.getElementById('rpt6PriorWeeks').value = document.getElementById('settingsPriorWeeks').value;
	    }
	}
    
	var startWeek = currentWeek - 1;
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


        	  var p1_teachers = [p1_teacher0, p1_teacher1, p1_teacher2, p1_teacher3, p1_teacher4, p1_teacher5, p1_teacher6, p1_teacher7, p1_teacher8, p1_teacher9, p1_teacher10];
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


        	  var p2_teachers = [p2_teacher0, p2_teacher1, p2_teacher2, p2_teacher3, p2_teacher4, p2_teacher5, p2_teacher6, p2_teacher7, p2_teacher8, p2_teacher9, p2_teacher10];
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
			  if (canvas_width < 320) {
				  canvas_width = 320;
			  }
			  document.getElementById("chart6_canvas1").style.width = "" + canvas_width + "px";      	  			  
			  document.getElementById("chart6_canvas2").style.width = "" + canvas_width + "px";      	  			  
        	  
        	  p1_plot6 = $.jqplot('chart6_canvas1', p1_teachersFound, 
        	    { 
        	      title: 'Weekly - Math Problems Seen', 
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
        	              max: 600,
        	              tickInterval: 20, 
        	              tickOptions: { 
        	                      formatString: '%d' 
        	              }, 
        	              label: 'Number of Problems Seen'
        	          }
        	      },
        	      
        	      legend: {
        	    	  show: true,
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
          	          }
        	          
        	      ]
        	    }
        	  );

        	  
        	  p2_plot6 = $.jqplot('chart6_canvas2', p2_teachersFound, 
              	    { 
              	      title: 'Weekly - Math Problems Solved', 
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
              	              max: 600,
              	              tickInterval: 20, 
              	              tickOptions: { 
              	                      formatString: '%d' 
              	              }, 
              	              label: 'Number of Problems Solved'
              	          }
              	      },
              	      
              	      legend: {
              	    	  show: true,
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
                    	  }
              	          
              	      ]
              	    }
              	  );

        	  
        	  
        	}
        	else {
        		alert("Data unavailable.");
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
        		alert("response data is null");
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
    
}

function updateCohortSlice() {

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
        		alert("response data is null");
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
        		alert("response data is null");
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });    
}

function updateAllCohortSlices() {

	if (currentCohortId < 0) {	
		alert("must select a cohort");
		return;
	}
	
	
    $('#admin3').find('.loader').show();
	
	if (currentCohortId == "1") {
		
//		if (cohortsArr[currentCohortIndex].cohortEnddate != null) {		
//			alert(cohortsArr[currentCohortIndex].cohortName +  " has ended.  No more updates allowed.");
//			return;
//		}

		$('#admin3').find('.loader').show();
	
	    $.ajax({
	        type : "POST",
	        url : pgContext+"/tt/tt/cohortAdmin",
	        data : {
	            cohortId: 1,
	            command: 'updateAllCohortSlicesTeacherActivity',
	            lang: loc,
	            filter: '08/04/2020~7~34'
	        },
	        success : function(data) {
	        	if (data) {
	            	alert(data);
	        	}
	        	else {
	        		alert("response data is null");
	        	}
	    	    $.ajax({
	    	        type : "POST",
	    	        url : pgContext+"/tt/tt/cohortAdmin",
	    	        data : {
	    	            cohortId: 1,
	    	            command: 'updateAllCohortClassStudentSlices',
	    	            lang: loc,
	    	            filter: '08/04/2020~7~34'
	    	        },
	    	        success : function(data) {
	    	        	$('#admin3').find('.loader').hide();
	    	        	if (data) {
	    	            	alert(data);
	    	        	}
	    	        	else {
	    	        		alert("response data is null");
	    	        	}
	    	        },
	    	        error : function(e) {
	    	        	$('#admin3').find('.loader').hide();
	    	        	alert("error");
	    	            console.log(e);
	    	        }
	    	    });
	        },
	        error : function(e) {
	        	$('#admin3').find('.loader').hide();
	        	alert("error");
	            console.log(e);
	        }
	    });
	    
	}

	if (currentCohortId == "2") {
	
		var updateCohortFilter = currentCohortDateArr[1] + "~7~" + currentWeek;
	    
		$('#admin3').find('.loader').show();
	    $.ajax({
	        type : "POST",
	        url : pgContext+"/tt/tt/cohortAdmin",
	        data : {
	            cohortId: 2,
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
	        	            cohortId: 2,
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
	        	        		alert("response data is null");
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
	        		alert("response data is null");
	        	}
	        },
	        error : function(e) {
	        	$('#admin3').find('.loader').hide();
	        	alert("error");
	            console.log(e);
	        }
	    });
	    
	
	}    

}
/*
function updateAllCohortSlices() {


    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/cohortAdmin",
        data : {
            cohortId: currentCohortId,
            command: 'updateAllCohortSlicesTeacherActivity',
            lang: loc,
            filter: '08/02/2020~7~44'
        },
        success : function(data) {
        	if (data) {
            	alert(data);
        	}
        	else {
        		alert("response data is null");
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
    
}

*/
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


<div class="container">
  <h2>Researcher Workbench</h2>

  <ul class="nav nav-tabs">
    <li class="active"><a data-toggle="tab" href="#home" onclick="openSettingsPane();">Home</a></li>
    <li><a data-toggle="tab" href="#Settings" onclick="gotoSettingsPane();">Settings</a></li>
    <li><a data-toggle="tab" href="#Charts">Charts</a></li>
    <li><a data-toggle="tab" href="#Trends">Trends</a></li>
    <li id="ReportCardLink" onclick="launchReportCard();"><a data-toggle="tab" >Class Report Cards</a></li>
    <li><a data-toggle="tab" href="#Tables">Tables</a></li>
    <li><a data-toggle="tab" href="#AdminTools">Admin Tools</a></li>
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
      	<h3>Settings</h3>
        <p>Selection criteria for this session</p>
		<br>
		<label for="setname">other</label>
	    <input type="text" class="form-control" id="setname" placeholder="Set Name" name="uname" required>
		<br>
		<div id="selections">
			<div class="row">
				<br>
				<div id="cohortSelect" class="col-sm-3 border offset-md-1 border-dark">
				</div>
				<div id="teacherSelect" class="col-sm-3">
				</div>
				<div id="classSelect" class="col-sm-3">
				</div>
			</div>
			<br>
			<hr>			
       		<p>These selections will apply to all Chart and Trend report selections. They may be over-ridden on the individual report page.</p>                            
           	<div class="row">
				<div class="form-group">
				    <div class="offset-md-1 col-md-3 pull-left">
						<label class="radio-inline"><input id="radioWeeksAll"  value="all"  type="radio" name="optRadioWeeks">Show from beginning.</label>
				    </div>
				    <div class="offset-md-2 col-md-3 pull-left">
						<label class="radio-inline"><input id="radioWeekSelect"  value="select"  type="radio" name="optRadioWeeks" checked>Show [X] prior weeks.</label>
				    </div>
				    <div class="offset-md-1 col-md-2 pull-left">
						<input type="text" maxlength="2" size="2" class="form-control" style="width: 50px;" id="settingsPriorWeeks" value="4">
				    </div>
				</div>
			</div>			
			<br>
			<br>
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
	
	
	
	
    <div id="Charts" class="col-sm-12 tab-pane fade">
        <h1 class="page-header">
            Teacher Activity Reports
            <br>
        </h1>
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
					<label class="radio-inline"><input id="radioChartsWeeksAll"  value="all"  type="radio" name="optRadioChartsWeeks">Show from beginning.</label>
			    </div>
			    <div class="offset-md-2 col-md-3 pull-left">
					<label class="radio-inline"><input id="radioChartsWeekSelect"  value="select"  type="radio" name="optRadioChartsWeeks" checked>Show [X] prior weeks.</label>
			    </div>
			    <div class="offset-md-1 col-md-2 pull-left">
					<input type="text" maxlength="2" size="2" class="form-control" style="width: 50px;" id="chartsPriorWeeks">
			    </div>
			</div>
		</div>
		<br>

        <div id="content-container" class="container-fluid">

            <div id="report-wrapper" class="row" width: 100%;">

                <div class="panel-group" id="accordion">
                  

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_six" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#chart1">
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
                                <a id="report_2" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#chartTwo">
                                    Teacher Activity Metrics
                                </a>
                               	<button id="Button2" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartTwo" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								  <input id="showReport2Btn" class="btn btn-lg btn-primary" onclick="showReport2();" type="submit" value="<%= rb.getString("show_report") %>">
                            </div>
 
 							<!-- SELECT teacherlog.teacherId, COUNT(*) AS total FROM teacherlog, teacher_map_cohort where teacherlog.teacherId = teacher_map_cohort.teacherid and action = "login" GROUP BY teacherId;
 -->
                            <div class="panel-body">
				            	<div id="chart2_canvas" style="width:800px; height:600px;"></div> 
                            </div>

                        </div>
                    </div>

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_2b" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#chartTwoB">
                                    Teacher Logins
                                </a>
                               	<button id="Button2b" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartTwoB" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								  <input id="showReport2bBtn" class="btn btn-lg btn-primary" onclick="showReport2b();" type="submit" value="<%= rb.getString("show_report") %>">
                            </div>
 
 							<!-- SELECT teacherlog.teacherId, COUNT(*) AS total FROM teacherlog, teacher_map_cohort where teacherlog.teacherId = teacher_map_cohort.teacherid and action = "login" GROUP BY teacherId;
 -->
                            <div class="panel-body">
				            	<div id="chart2b_canvas" style="width:800px; height:400px;"></div> 
                            </div>

                        </div>
                    </div>


                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_2c" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#chartTwoC">
                                   Days Since Last Login 
                                </a>
                               	<button id="Button2c" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartTwoC" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								  <input id="showReport2cBtn" class="btn btn-lg btn-primary" onclick="showReport2c();" type="submit" value="<%= rb.getString("show_report") %>">
                            </div>
 
 							<!-- SELECT teacherlog.teacherId, COUNT(*) AS total FROM teacherlog, teacher_map_cohort where teacherlog.teacherId = teacher_map_cohort.teacherid and action = "login" GROUP BY teacherId;
 -->
                            <div class="panel-body">
				            	<div id="chart2c_canvas" style="width:800px; height:400px;"></div> 
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
								  <input id="showReport3Btn" class="btn btn-lg btn-primary" onclick="showReport3();" type="submit" value="<%= rb.getString("show_report") %>">
                            </div>
 
 							<!-- SELECT teacherId, COUNT(*) AS total FROM teacherlog where action = "login" GROUP BY teacherId; -->
                            <div class="panel-body">
				            	<div id="chart3_canvas" style="width:800px; height:400px;"></div> 
                            </div>

                        </div>
                    </div>

                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_tp" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#chartTP">
                                   Student Problem Solving Totals by Teacher
                                </a>
                               	<button id="ButtonTP" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartTP" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								  <input id="showReportTPBtn" class="btn btn-lg btn-primary" onclick="showReport_tp();" type="submit" value="<%= rb.getString("show_report") %>">
                            </div>
 
                            <div class="panel-body">
				            	<div id="tp_canvas" style="width:800px; height:800px;"></div> 
                            </div>

                        </div>
                    </div>

                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_tcp" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#chartTCP">
                                    Student Problem Solving Totals by Class
                                </a>
                               	<button id="ButtonTCP" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartTCP" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								  <input id="showReportTCPBtn" class="btn btn-lg btn-primary" onclick="showReport_tcp();" type="submit" value="<%= rb.getString("show_report") %>">
                            </div>
 
                            <div class="panel-body">
				            	<div id="tcp_canvas" style="width:800px; height:800px;"></div> 
                            </div>

                        </div>
                    </div>

                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_tcs" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#chartTCS">
                                    Active Students This Week
                                </a>
                               	<button id="ButtonTCS" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartTCS" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								  <input id="showReportTCSBtn" class="btn btn-lg btn-primary" onclick="showReport_tcs();" type="submit" value="<%= rb.getString("show_report") %>">
                            </div>
 
                            <div class="panel-body">
				            	<div id="tcs_canvas" style="width:800px; height:500px;"></div> 
                            </div>

                        </div>
                    </div>

            	</div>
        	</div>
		</div>
    </div>


    <div id="Trends" class="col-sm-12 tab-pane fade container">
        <h1 class="page-header">
            Teacher Activity Trends
            <br>
        </h1>
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

                <div class="panel-group" id="trendsGroup">
                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_5" class="accordion-toggle" data-toggle="collapse" data-parent="#trendsGroup" href="#chartFive">
                                    Teacher Activities - Weekly Counts
                                </a>
                               	<button id="Button5" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartFive" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">
                            	<div class="row">                           
								<div class="form-group">
								    <div class="offset-md-1 col-md-3 pull-left">
										<label class="radio-inline"><input id="radio5WeeksAll"  value="all"  type="radio" name="optRadio5Weeks">Show from beginning.</label>
								    </div>
								    <div class="offset-md-2 col-md-3 pull-left">
										<label class="radio-inline"><input id="radio5WeekSelect"  value="select"  type="radio" name="optRadio5Weeks" checked>Show [X] prior weeks.</label>
								    </div>
								    <div class="offset-md-1 col-md-2 pull-left">
										<input type="text" maxlength="2" size="2" class="form-control" style="width: 50px;" id="rpt5PriorWeeks">
								    </div>
								</div>
								</div>
								<br>
                            	<div class="row">                           
									<div class="form-group">
										<label class="radio-inline"><input id="radioLogins"  value="Logins" type="radio" name="optRadio5Content" checked>Logins</label>
										<label class="radio-inline"><input id="radioActions" value="Actions" type="radio" name="optRadio5Content">Actions</label>
										<label class="radio-inline"><input id="radioLogouts" value="Logouts" type="radio" name="optRadio5Content">Logouts</label>
									</div>
								</div>                            
                            	<div id="rpt5PopulateSelect" class="row">      
									<div class="form-group">
										<label class="radio-inline"><input id="radio5ShowAll"                  value="showAll"                 type="radio" name="optRadio5Populate" checked>Show All</label>
										<label class="radio-inline"><input id="radio5ShowSingleOnly"           value="showSingleOnly"          type="radio" name="optRadio5Populate">Show Single Only</label>
										<label class="radio-inline"><input id="radio5ShowSingleWithAnonymous"  value="showSingleWithAnonymous" type="radio" name="optRadio5Populate">Show Single With Anonymous</label>
									</div>
								</div>
                            </div>
                            <div class="panel-body report_filters">                           
								  <input id="showReport5Btn" class="btn btn-lg btn-primary" onclick="showReport5();" type="submit" value="<%= rb.getString("show_report") %>">
                            </div>
 
                            <div  class="panel-body">
			            		<div id="chart5_canvas" style="width:1000px; height:600px;overflow-x: auto;"></div>
                            </div>

                        </div>
                    </div>

                    
                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_5" class="accordion-toggle" data-toggle="collapse" data-parent="#trendsGroup" href="#chartSix">
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
										<label class="radio-inline"><input id="radio6WeeksAll"  value="all"  type="radio" name="optRadio6Weeks">Show from beginning.</label>
								    </div>
								    <div class="offset-md-2 col-md-3 pull-left">
										<label class="radio-inline"><input id="radio6WeekSelect"  value="select"  type="radio" name="optRadio6Weeks" checked>Show [X] prior weeks.</label>
								    </div>
								    <div class="offset-md-1 col-md-2 pull-left">
										<input type="text" maxlength="2" size="2" class="form-control" style="width: 50px;" id="rpt6PriorWeeks">
								    </div>
								</div>
								</div>
								<br>
                            	<div class="row">                           
									<div class="form-group hidden">
										<label class="radio-inline"><input id="radioProblemsSeen"  value="ProblemsSeen" type="radio" name="optRadio6Content" checked>Problems Seen</label>
										<label class="radio-inline"><input id="radioProblemsSolved" value="ProblemsSolved" type="radio" name="optRadio6Content">Problems Solved</label>
									</div>
								</div>                           
                            	<div class="row">      
									<div class="form-group">
										<label class="radio-inline"><input id="radio6ShowAll"                  value="showAll"                 type="radio" name="optRadio6Populate" checked>Show All</label>
										<label class="radio-inline"><input id="radio6ShowSingleOnly"           value="showSingleOnly"          type="radio" name="optRadio6Populate">Show Single Only</label>
										<label class="radio-inline"><input id="radio6ShowSingleWithAnonymous"  value="showSingleWithAnonymous" type="radio" name="optRadio6Populate">Show Single With Anonymous</label>
									</div>
								</div>
                            </div>
                            
                            <div class="panel-body report_filters">                           
								  <input id="showReport6Btn" class="btn btn-lg btn-primary" onclick="showReport6();" type="submit" value="<%= rb.getString("show_report") %>">
                            </div>


                            <div  class="panel-body">
			            		<div id="chart6_canvas1" style="width:1200px; height:400px;overflow-x: auto;"></div>
			            		<br>
			            		<div id="chart6_canvas2" style="width:1200px; height:400px;overflow-x: auto;"></div>
                            </div>
                        </div>
                    </div>
            	</div>
        	</div>
		</div>
	</div>

    <div id="Tables" class="col-sm-12 tab-pane fade">
      <h3>Tables</h3>
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
    </div>



    <div id="AdminTools" class="col-sm-12 tab-pane fade container">
		<div class="row">
			<div id="adminCohortName">				
			</div>
		</div>			
        <h1 class="page-header">
            Researcher Admin Tools
        </h1>

        <div id="admin-container" class="container-fluid">

            <div id="admin-wrapper" class="row" width: 100%;">

                <div class="panel-group" id="adminCommands">

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="admin_one" class="accordion-toggle" data-toggle="collapse" data-parent="#adminCommands" href="#admin1">
                                    Create Cohort Slice
                                </a>
                               	<button type="button" class="close" onclick="$('.collapseAdmin').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="admin1" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								  <input id="admin1Btn" class="btn btn-lg btn-primary" onclick="createCohortSlice();" type="submit" value="<%= rb.getString("submit") %>">
                            </div>
 
                            <div class="panel-body">
                                <div id="admin1Status" class="table table-striped table-bordered hover display nowrap" width="100%"></div>
                            </div>

                        </div>
                    </div>
                    
                    <div id="updateWeeklySlices" class="panel panel-default hidden">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="admin_two" class="accordion-toggle" data-toggle="collapse" data-parent="#adminCommands" href="#admin2">
                                    Update Cohort Weekly Slice
                                </a>
                               	<button type="button" class="close" onclick="$('.collapseAdmin').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="admin2" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								  <input id="admin2Btn" class="btn btn-lg btn-primary" onclick="updateCohortSlice();" type="submit" value="<%= rb.getString("submit") %>">
                            </div>
 
                            <div class="panel-body">
                                <div id="admin2Status" class="table table-striped table-bordered hover display nowrap" width="100%"></div>
                            </div>

                        </div>
                    </div>
                    
                    <div  id="updateAllSlices" class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="admin_three" class="accordion-toggle" data-toggle="collapse" data-parent="#adminCommands" href="#admin3">
                                    Update All Cohort Weekly Slices
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

    <div id="classViews" class="col-sm-12 tab-pane fade">
      <h3>Tables</h3>
      <p>View report card</p>
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

