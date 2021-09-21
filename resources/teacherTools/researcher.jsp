<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>
<% 
/** 
* Frank 	08-03-21	Initial version 



I think we are most likely to want to see the last 1,2,3 or 4 weeks (or month), but also "from the start" (or maybe "all semester").
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


<script type="text/javascript">
    var ohlc = [['07/06/09', 138.7, 139.68, 135.18, 135.4],
    ['06/29/09', 143.46, 144.66, 139.79, 140.02],
    ['06/22/09', 140.67, 143.56, 132.88, 142.44],
    ['06/15/09', 136.01, 139.5, 134.53, 139.48],
    ['06/08/09', 143.82, 144.56, 136.04, 136.97],
    ['06/01/09', 136.47, 146.4, 136, 144.67],
    ['05/26/09', 124.76, 135.9, 124.55, 135.81],
    ['05/18/09', 123.73, 129.31, 121.57, 122.5],
    ['05/11/09', 127.37, 130.96, 119.38, 122.42],
    ['05/04/09', 128.24, 133.5, 126.26, 129.19],
    ['04/27/09', 122.9, 127.95, 122.66, 127.24],
    ['04/20/09', 121.73, 127.2, 118.6, 123.9],
    ['04/13/09', 120.01, 124.25, 115.76, 123.42],
    ['04/06/09', 114.94, 120, 113.28, 119.57],
    ['03/30/09', 104.51, 116.13, 102.61, 115.99],
    ['03/23/09', 102.71, 109.98, 101.75, 106.85],
    ['03/16/09', 96.53, 103.48, 94.18, 101.59],
    ['03/09/09', 84.18, 97.2, 82.57, 95.93],
    ['03/02/09', 88.12, 92.77, 82.33, 85.3],
    ['02/23/09', 91.65, 92.92, 86.51, 89.31],
    ['02/17/09', 96.87, 97.04, 89, 91.2],
    ['02/09/09', 100, 103, 95.77, 99.16],
    ['02/02/09', 89.1, 100, 88.9, 99.72],
    ['01/26/09', 88.86, 95, 88.3, 90.13],
    ['01/20/09', 81.93, 90, 78.2, 88.36],
    ['01/12/09', 90.46, 90.99, 80.05, 82.33],
    ['01/05/09', 93.17, 97.17, 90.04, 90.58],
    ['12/29/08', 86.52, 91.04, 84.72, 90.75],
    ['12/22/08', 90.02, 90.03, 84.55, 85.81],
    ['12/15/08', 95.99, 96.48, 88.02, 90],
    ['12/08/08', 97.28, 103.6, 92.53, 98.27],
    ['12/01/08', 91.3, 96.23, 86.5, 94],
    ['11/24/08', 85.21, 95.25, 84.84, 92.67],
    ['11/17/08', 88.48, 91.58, 79.14, 82.58],    
    ['11/10/08', 100.17, 100.4, 86.02, 90.24],
    ['11/03/08', 105.93, 111.79, 95.72, 98.24],
    ['10/27/08', 95.07, 112.19, 91.86, 107.59],
    ['10/20/08', 99.78, 101.25, 90.11, 96.38],
    ['10/13/08', 104.55, 116.4, 85.89, 97.4],
    ['10/06/08', 91.96, 101.5, 85, 96.8],
    ['09/29/08', 119.62, 119.68, 94.65, 97.07],
    ['09/22/08', 139.94, 140.25, 123, 128.24],
    ['09/15/08', 142.03, 147.69, 120.68, 140.91],
    ['09/08/08', 164.57, 164.89, 146, 148.94]
    ];
</script>     


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

var cohortsArr = [];
var allCohortsArr = [];
var currentCohortId = 0;
var currentCohortIndex = 0;
var currentCohortWeeks = {};
var cohortWeeks = [];

var currentTeachersArr = [];

var trendDate = '10/17/2020';
var trendUnit = "week";
var trendNumberOfUnits = 6;

const currentTeacher = new Object();
var currentClass = "";

var filterOne = "";
var headers = "";



function addDays(date, days) {
	  var result = new Date(date);
	  result.setDate(result.getDate() + days);
	  return result;
	}

$(document).ready(function () {
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/researchServices",
        data : {
            cohortId: 2,
            teacherId: 910,
            classId: 1619,
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
//	showCohorts();
// 	var teachersDiv = '<label for="teacherList">Select teacher</label>';  
//    document.getElementById('classSelect').visibility = "hidden";
    clearSettings();
}
	
function clearSettings() {
		var cohortsDiv = '<label for="cohortList">Select cohort</label> <select name="cohortList" id="cohortList" class="form-control selectpicker" onblur="handleCohortSelect(event);" data-show-subtext="true" data-live-search="true" size="5" style="width: 270px;"></select>';  				
       	document.getElementById('cohortSelect').innerHTML = cohortsDiv;
	
     	var teachersDiv = '<label for="teacherList">Select teacher</label>';  
        document.getElementById('teacherSelect').innerHTML = teachersDiv;

     	var classesDiv = '<label for="classList">Select class</label>';  
        document.getElementById('classSelect').innerHTML = classesDiv;
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
      		var cohortsDiv = '<label for="cohortList">Select cohort</label> <select name="cohortList" id="cohortList" class="form-control selectpicker" onblur="handleCohortSelect(event);" data-show-subtext="true" data-live-search="true" size="5" style="width: 270px;">';  
        	for (var i=0;i<cohortsArr.length;i++) {
				var id = "" + cohortsArr[i].cohortId;
				var name = "" + cohortsArr[i].cohortName;
               	var cohortStr = '<option id="cohort' + id + '" value="' + i + "~" + id + '">' + name + '</option>';
               	cohortsDiv = cohortsDiv + cohortStr;
            }
		    cohortsDiv = cohortsDiv + '</select>';  				
           	document.getElementById('cohortSelect').innerHTML = cohortsDiv;
           	
           	console.log(cohortsDiv);
        }
    });
}   
    

    


function handleCohortSelect(event) {

    var selectElement = event.target;
    var value = selectElement.value;
    var splitValue = value.split("~");
    
    currentCohortIndex = Number(splitValue[0]);
    currentCohortId = Number(splitValue[1]);
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/cohortAdmin",
        data : {
        	cohortId: currentCohortId,
            command: 'getCohortWeeks',
            lang: 'en_US',
            filter: '~'
        },
        success : function(data) {
        	currentCohortWeeks = $.parseJSON(data);
        	document.getElementById('chartsCohortName').innerHTML = "<h3>" + cohortsArr[currentCohortIndex].cohortName + "</h3>";
        	document.getElementById('chartsCohortWeeks').innerHTML = "<h3>" + data + "</h3>";
/*
        	var cohortDate = new Date(currentCohortWeeks.startdate);
            cohortDate.setMonth( cohortDate.getMonth() + 1 );
        	cohortWeeks.push(cohortDate);
           
            var adder = 7;
            for (var i=0;i<52;i++) {
            	var tDate = new Date(currentCohortWeeks.startdate) + adder;
            	cohortWeeks.push(tDate);
            	adder = adder + 7;
            }
*/            
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        } 
    });
    
    
    showTeachers();
}

function showTeachers() {
	
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
//    document.getElementById('teacherSelect').visibility = "visible";
        	
}

function handleTeacherSelect(event) {

    var selectElement = event.target;
    var value = "" + selectElement.value;
    var splitter = value.split("~");
    currentTeacher.id = splitter[0];
    currentTeacher.username = splitter[1];
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
//    document.getElementById('classSelect').visibility = "visible";
        	
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

	headers = changeTeacherActivitiesReportHeaderAccordingToLanguage();
	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getTeacherReports",
        data : {
            classId: "" + currentTeacher.id,
            teacherId: "" + currentTeacher.id,
            reportType: 'perTeacherReport',
            lang: loc,
            filter: ''
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

function showReport2() {

	//SELECT teacherlog.teacherId, userName as uname, COUNT(*) AS total FROM teacher, teacherlog, teacher_map_cohort where teacherlog.teacherId = teacher_map_cohort.teacherid and teacher.id = teacherlog.teacherId and action = "login" GROUP BY teacherId;

	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: "1",
            reportType: 'teacherActivityMetrics',
            lang: loc,
            filter: ''
        },
        success : function(data) {
        	if (data) {
            	var jsonData = $.parseJSON(data);
            	console.log(data);
            	var ticks = [];
            	for (var i=0;i<10;i = i + 1) {
            		ticks.push(jsonData[i].username);
            	}
            	var theSeries = [];
            	var row1 = [];
            	var row2 = [];
            	var row3 = [];
             
            	for (var i=0;i<10;i = i + 1) {            	
            		var element1 = [];
            		var element2 = [];
            		var element3 = [];
            		element1.push(jsonData[i].logins);
            		element1.push(i+1);
            		row1.push(element1);
            		element2.push(jsonData[i].actions);
            		element2.push(i+1);
            		row2.push(element2);
            		var element3 = [];
            		element3.push(jsonData[i].logouts);
            		element3.push(i+1);
            		row3.push(element3);
            	}
            	theSeries.push(row1);
            	theSeries.push(row2);
            	theSeries.push(row3);

            	var plot2 = $.jqplot('chart2canvas', theSeries, {

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

	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: "1",
            reportType: 'teacherLoginCount',
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

                var plot2 = $.jqplot('chart2bcanvas', series, {
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
	
	// For horizontal bar charts, x an y values must will be "flipped"
    // from their vertical bar counterpart.
    
}


function showReport3() {

	var s1 = [200, 600, 700, 1000];
	var s2 = [460, -210, 690, 820];
	var s3 = [-260, -440, 320, 200];
	// Can specify a custom tick Array.
	// Ticks should match up one for each y value (category) in the series.
	var ticks = ['May', 'June', 'July', 'August'];
	
	var plot1 = $.jqplot('chart3canvas', [s1, s2, s3], {
	    // The "seriesDefaults" option is an options object that will
	    // be applied to all series in the chart.
	    seriesDefaults:{
	        renderer:$.jqplot.BarRenderer,
	        rendererOptions: {fillToZero: true},
            varyBarColor: true
	    },
	    // Custom labels for the series are specified with the "label"
	    // option on the series option.  Here a series option object
	    // is specified for each series.
	    series:[
	        {label:'Hotel'},
	        {label:'Event Regristration'},
	        {label:'Airfare'}
	    ],
	    // Show the legend and put it outside the grid, but inside the
	    // plot container, shrinking the grid to accomodate the legend.
	    // A value of "outside" would not shrink the grid and allow
	    // the legend to overflow the container.
	    legend: {
	        show: true,
	        placement: 'outsideGrid'
	    },
	    axes: {
	        // Use a category axis on the x axis and use our custom ticks.
	        xaxis: {
	            renderer: $.jqplot.CategoryAxisRenderer,
	            ticks: ticks
	        },
	        // Pad the y axis just a little so bars can get close to, but
	        // not touch, the grid boundaries.  1.2 is the default padding.
	        yaxis: {
	            pad: 1.05,
	            tickOptions: {formatString: '$%d'}
	        }
	    }
	});
}


function showReport4() {
	
    title: "CandleStick Chart",
    // Globally enable plugins like cursor and highlighter
    $.jqplot.config.enablePlugins = true;

    // for 2 digit years, set the default centry to 2000.
    $.jsDate.config.defaultCentury = 2000;
    
    plot1 = $.jqplot('chart4canvas',[ohlc],{
        axes: {
          xaxis: {
              renderer:$.jqplot.DateAxisRenderer
          },
          yaxis: {
              tickOptions:{prefix: '$'}
          }
        },
        series: [
            {
                renderer:$.jqplot.OHLCRenderer, 
                rendererOptions:{ 
                    candleStick:true 
                }
            }
        ],
        highlighter: {
            showMarker:false,
            tooltipAxes: 'xy',
            yvalues: 4,
            formatString:'<table class="jqplot-highlighter"> \
                <tr><td>date:</td><td>%s</td></tr> \
                <tr><td>open:</td><td>%s</td></tr> \
                <tr><td>hi:</td><td>%s</td></tr> \
                <tr><td>low:</td><td>%s</td></tr> \
                <tr><td>close:</td><td>%s</td></tr> \
            </table>'
        }
    });
	
}

function getUsernameIndex(username) {

	var teacherArr = allCohortsArr[currentCohortIndex].cohortArr;    	

	for (var i=0;i<teacherArr.length;i++) {
		if (teacherArr[i].userName === username) {
			return i;
		}
	}
	return -1;
	
}

var usernameXref = [];
var usernameStrXref = [];

function mapUsernameIndex(index,username){

	if (usernameXref.length == 0) {
		usernameXref.push(index);
		usernameStrXref.push(username);
		return 0;
	}
	else {
		for (var i=0; i<usernameXref.length; i++) {
			if (usernameXref[i] == index) {
				return i;
			}
			else {
				if (i == (usernameXref.length - 1))	{			
					usernameXref.push(index);
					usernameStrXref.push(username);
					return (i+1);
				}
			}
		}
	}
}

function getUsernameIndex(username) {

	var teacherArr = allCohortsArr[currentCohortIndex].cohortArr;    	

	for (var i=0;i<teacherArr.length;i++) {
		if (teacherArr[i].userName === username) {			
			return mapUsernameIndex(i,username);
		}
	}
	return -1;
	
}


function showReport5() {

	usernameXref = [];
	usernameStrXref = [];
	var trendUnit = 7; 
	var trendFilter = trendDate + "~" + trendUnit + "~" + trendNumberOfUnits;
	
	var dateArr = [];
	dateArr.push("");
	var startDate = new Date(trendDate);
	var startDateStr = startDate.toLocaleDateString();
	var endDateStr = "";
	dateArr.push();
	
	for (var i = 1; i <= trendNumberOfUnits; i++) {
		var tdate = new Date(addDays(trendDate,(7*i)));
		var dateStr = tdate.toLocaleDateString();
		dateArr.push(dateStr);
		endDateStr = dateStr;
	}

	var rpt5Content = "";
    const rbs = document.querySelectorAll('input[name="optRadio"]');

    for (const rb of rbs) {
        if (rb.checked) {
        	rpt5Content = rb.value;
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
	
	//	var date = addDays(trendDate,7);
	
//	alert(trendFilter);
	
	document.getElementById('trendLoader').visibility = "visible";

    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/cohortAdmin",
        data : {
            cohortId: "1",
            command: 'getCohortRangeSlices',
            lang: loc,
            filter: trendFilter
        },
        success : function(data) {
        	document.getElementById('trendLoader').visibility = "hidden";
        	if (data) {
            	//alert(data);
            	jsonRangeData = $.parseJSON(data);
            	var jsonWeekArr = [];
				
            	for (var j=0; j<jsonRangeData.length;j++ ) {
                	jsonWeekArr.push(jsonRangeData[j]);
            	
            	}
/*            	
            	jsonWeekArr.push(jsonRangeData[0]);
            	jsonWeekArr.push(jsonRangeData[1]);
            	jsonWeekArr.push(jsonRangeData[2]);
            	jsonWeekArr.push(jsonRangeData[3]);
            	jsonWeekArr.push(jsonRangeData[4]);
            	jsonWeekArr.push(jsonRangeData[5]);
            	jsonWeekArr.push(jsonRangeData[6]);
            	jsonWeekArr.push(jsonRangeData[7]);


      		    	
            	var jsonDataWeek1 = jsonWeekArr[0]["Week1"];
            	var jsonDataWeek2 = jsonWeekArr[1]["Week2"];
            	var jsonDataWeek3 = jsonWeekArr[2]["Week3"];
            	var jsonDataWeek4 = jsonWeekArr[3]["Week4"];
            	var jsonDataWeek5 = jsonWeekArr[4]["Week5"];
            	var jsonDataWeek6 = jsonWeekArr[5]["Week6"];
            	var jsonDataWeek7 = jsonWeekArr[6]["Week7"];
            	var jsonDataWeek8 = jsonWeekArr[7]["Week8"];
*/
			  
			  
            	
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


        	  var teachers = [teacher0, teacher1, teacher2, teacher3, teacher4, teacher5, teacher6, teacher7, teacher8, teacher9];
        	  var teachersFound = [];
        	  
			  if ((jsonWeekArr.length > 0) && (jsonWeekArr[0]["Week1"].length > 0)) {
				  for (i=0;i < jsonWeekArr[0]["Week1"].length; i++) {
					  	var count = 0;
				  		switch(rpt5Content) {
				  		case "Logouts":
				  			count = jsonWeekArr[0]["Week1"][i].logouts;
				  			break;
				  		case "Actions":
				  			count = jsonWeekArr[0]["Week1"][i].actions;
							break;
						default:
				  			count = jsonWeekArr[0]["Week1"][i].logins;							
				  		}
						var username = jsonWeekArr[0]["Week1"][i].username;
 		  		  	    var index = getUsernameIndex(username);
				  		teachers[index].push([dateArr[1], count]);
				  }
			  }		  
			  if ((jsonWeekArr.length > 1) && (jsonWeekArr[1]["Week2"].length > 0)) {
				  for (i=0;i < jsonWeekArr[1]["Week2"].length; i++) {					
					  	var count = 0;
				  		switch(rpt5Content) {
				  		case "Logouts":
				  			count = jsonWeekArr[1]["Week2"][i].logouts;
				  			break;
				  		case "Actions":
				  			count = jsonWeekArr[1]["Week2"][i].actions;
							break;
						default:
				  			count = jsonWeekArr[1]["Week2"][i].logins;							
				  		}
						var username = jsonWeekArr[1]["Week2"][i].username;
 		  		  	    var index = getUsernameIndex(username);
				  		teachers[index].push([dateArr[2], count]);
				  }
			  }
			  if ((jsonWeekArr.length > 2) && (jsonWeekArr[2]["Week3"].length > 0)) {
				  for (i=0;i < jsonWeekArr[2]["Week3"].length; i++) {					
					  	var count = 0;
				  		switch(rpt5Content) {
				  		case "Logouts":
				  			count = jsonWeekArr[2]["Week3"][i].logouts;
				  			break;
				  		case "Actions":
				  			count = jsonWeekArr[2]["Week3"][i].actions;
							break;
						default:
				  			count = jsonWeekArr[2]["Week3"][i].logins;							
				  		}
						var username = jsonWeekArr[2]["Week3"][i].username;
 		  		  	    var index = getUsernameIndex(username);
				  		teachers[index].push([dateArr[3], count]);
				  }
			  }

			  if ((jsonWeekArr.length > 3) &&  (jsonWeekArr[3]["Week4"].length > 0)) {
				  for (i=0;i < jsonWeekArr[3]["Week4"].length; i++) {					
					  	var count = 0;
				  		switch(rpt5Content) {
				  		case "Logouts":
				  			count = jsonWeekArr[3]["Week4"][i].logouts;
				  			break;
				  		case "Actions":
				  			count = jsonWeekArr[3]["Week4"][i].actions;
							break;
						default:
				  			count = jsonWeekArr[3]["Week4"][i].logins;							
				  		}
						var username = jsonWeekArr[3]["Week4"][i].username;
 		  		  	    var index = getUsernameIndex(username);
				  		teachers[index].push([dateArr[4], count]);
				  }
			  }

			  if ((jsonWeekArr.length > 4) && (jsonWeekArr[4]["Week5"].length > 0)) {
				  for (i=0;i < jsonWeekArr[4]["Week5"].length; i++) {					
					  	var count = 0;
				  		switch(rpt5Content) {
				  		case "Logouts":
				  			count = jsonWeekArr[4]["Week5"][i].logouts;
				  			break;
				  		case "Actions":
				  			count = jsonWeekArr[4]["Week5"][i].actions;
							break;
						default:
				  			count = jsonWeekArr[4]["Week5"][i].logins;							
				  		}
						var username = jsonWeekArr[4]["Week5"][i].username;
 		  		  	    var index = getUsernameIndex(username);
				  		teachers[index].push([dateArr[5], count]);
				  }
			  }

			  if ((jsonWeekArr.length > 5) && (jsonWeekArr[5]["Week6"].length > 0)) {
				  for (i=0;i < jsonWeekArr[5]["Week6"].length; i++) {					
					  	var count = 0;
				  		switch(rpt5Content) {
				  		case "Logouts":
				  			count = jsonWeekArr[5]["Week6"][i].logouts;
				  			break;
				  		case "Actions":
				  			count = jsonWeekArr[5]["Week6"][i].actions;
							break;
						default:
				  			count = jsonWeekArr[5]["Week6"][i].logins;							
				  		}
						var username = jsonWeekArr[5]["Week6"][i].username;
 		  		  	    var index = getUsernameIndex(username);
				  		teachers[index].push([dateArr[6], count]);
				  }
			  }

			  if ((jsonWeekArr.length > 6) && (jsonWeekArr[6]["Week7"].length > 0)) {
				  for (i=0;i < jsonWeekArr[6]["Week7"].length; i++) {					
					  	var count = 0;
				  		switch(rpt5Content) {
				  		case "Logouts":
				  			count = jsonWeekArr[6]["Week7"][i].logouts;
				  			break;
				  		case "Actions":
				  			count = jsonWeekArr[6]["Week7"][i].actions;
							break;
						default:
				  			count = jsonWeekArr[6]["Week7"][i].logins;							
				  		}
						var username = jsonWeekArr[6]["Week7"][i].username;
 		  		  	    var index = getUsernameIndex(username);
				  		teachers[index].push([dateArr[7], count]);
				  }
			  }

			  if ((jsonWeekArr.length > 7) &&  (jsonWeekArr[7]["Week8"].length > 0)) {
				  for (i=0;i < jsonWeekArr[7]["Week8"].length; i++) {					
					  	var count = 0;
				  		switch(rpt5Content) {
				  		case "Logouts":
				  			count = jsonWeekArr[7]["Week8"][i].logouts;
				  			break;
				  		case "Actions":
				  			count = jsonWeekArr[7]["Week8"][i].actions;
							break;
						default:
				  			count = jsonWeekArr[7]["Week8"][i].logins;							
				  		}
						var username = jsonWeekArr[7]["Week8"][i].username;
 		  		  	    var index = getUsernameIndex(username);
				  		teachers[index].push([dateArr[8], count]);
				  }
			  }

			  for (var t = 0; t<usernameXref.length; t++) {
				  teachersFound.push(teachers[t]);
			  }
        	  			  
        	  
        	  var plot3 = $.jqplot('chart5canvas', teachersFound, 
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
//        	            min:'ctober 10, 2020 8:00AM', 
//        	            max:'December 12, 2020 8:00AM', 
        	            min:startDateStr, 
        	            max:endDateStr, 
        	            tickInterval:'1 week',
        	            tickOptions: {
       	                    // labelPosition: 'middle',
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
        	      series:[ 
        	          {
        	            // Change our line width and use a diamond shaped marker.
        	            label: usernameStrXref[0],
        	            lineWidth:2, 
        	            markerOptions: { style:'circle' }
        	          }, 
        	          {
        	            // Don't show a line, just show markers.
        	            // Make the markers 7 pixels with an 'x' style
        	            label: usernameStrXref[1],
        	            lineWidth:2, 
        	            markerOptions: { style:"circle" }
        	          },
        	          { 
        	            // Use (open) circlular markers.
        	            label: usernameStrXref[2],
        	            lineWidth:2, 
        	            markerOptions: { style:"circle" }
        	          }, 
        	          {
        	            // Use a thicker, 5 pixel line and 10 pixel
        	            // filled square markers.
        	            label: usernameStrXref[3],
        	            lineWidth:2, 
        	            markerOptions: { style:"circle" }
        	          },
        	          { 
          	            // Use (open) circlular markers.
        	            label: usernameStrXref[4],
          	            lineWidth:2, 
          	            markerOptions: { style:"circle" }
          	          }, 
        	          { 
          	            // Use (open) circlular markers.
        	            label: usernameStrXref[5],
          	            lineWidth:2, 
          	            markerOptions: { style:"circle" }
          	          }, 
        	          { 
          	            // Use (open) circlular markers.
        	            label: usernameStrXref[6],
          	            lineWidth:2, 
          	            markerOptions: { style:"circle" }
          	          }, 
        	          { 
          	            // Use (open) circlular markers.
        	            label: usernameStrXref[7],
          	            lineWidth:2, 
          	            markerOptions: { style:"circle" }
          	          }
        	          
        	      ]
        	    }
        	  );
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


function createCohortSlice() {

	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/cohortAdmin",
        data : {
            cohortId: "1",
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


    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/cohortAdmin",
        data : {
            cohortId: "1",
            command: 'updateCohortSliceTeacherActivity',
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

function updateAllCohortSlices() {


    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/cohortAdmin",
        data : {
            cohortId: "1",
            command: 'updateAllCohortSlicesTeacherActivity',
            lang: loc,
            filter: '09/06/2020~7~15'
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
    <li class="active"><a data-toggle="tab" href="#home">Home</a></li>
    <li><a data-toggle="tab" href="#Settings" onclick="openSettingsPane();">Settings</a></li>
    <li><a data-toggle="tab" href="#Charts">Charts</a></li>
    <li><a data-toggle="tab" href="#Trends">Trends</a></li>
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
			<br>			
			

		</div>
		<br>
		<div class="row">
			<button type="recall" class="btn btn-primary col-sm-2 col-sm-offset-2">Recall Settings</button>
			<button type="save" class="btn btn-primary col-sm-2 col-sm-offset-1">Save Settings</button>
			<button type="clear" class="btn btn-danger col-sm-2 col-sm-offset-1" onclick="clearSettings()";">Clear Settings</button>
		</div>
	</div>
    <div id="Charts" class="col-sm-12 tab-pane fade">
		<div class="row">
			<div id="chartsCohortName">				
			</div>
			<div id="chartsCohortWeeks">				
			</div>
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
		<br>
        <h1 class="page-header">
            Teacher Activity Reports
            <br>
        </h1>

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
                            <div class="panel-body report_filters">                           
								  <input id="showReportBtn1" class="btn btn-lg btn-primary" onclick="showReport1();" type="submit" value="<%= rb.getString("show_report") %>">
                            </div>
 
 							<!-- SELECT teacherId, COUNT(*) AS total FROM teacherlog where action = "login" GROUP BY teacherId; -->
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
				            	<div id="chart2canvas" style="width:800px; height:600px;"></div> 
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
				            	<div id="chart2bcanvas" style="width:800px; height:400px;"></div> 
                            </div>

                        </div>
                    </div>


                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_3" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#chartThree">
                                    Vertical Bar Chart - 3 metrics
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
				            	<div id="chart3canvas" style="width:400px; height:300px;"></div> 
                            </div>

                        </div>
                    </div>

            	</div>
        	</div>
		</div>
    </div>


    <div id="Trends" class="col-sm-12 tab-pane fade">
		<div class="row">
			<div id="trendsCohortName">				
			</div>
		</div>			
		<div class="row">
			<div class="form-group">
				<label class="radio-inline"><input type="radio" name="optradio" checked>Show data from prior</label>
				<input type="text" class="form-control" id="usr"> &nbsp;weeks.&nbsp;&nbsp;
				<label class="radio-inline"><input type="radio" name="optradio">Show from beginning.</label>
			</div>
		</div>
		<br>
        <h1 class="page-header">
            Teacher Activity Trends
            <br>
        </h1>

        <div id="trends-container" class="container-fluid">

            <div id="trends-wrapper" class="row" width: 100%;">

                <div id="trendLoader" class="loader" style="display: none"></div>
                <div class="panel-group" id="trendsGroup">
                  

                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_4" class="accordion-toggle" data-toggle="collapse" data-parent="#trendsGroup" href="#chartFour">
                                    Horizontal Trend Chart - one metric
                                </a>
                               	<button id="Button4" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartFour" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								  <input id="showReport4Btn" class="btn btn-lg btn-primary" onclick="showReport4();" type="submit" value="<%= rb.getString("show_report") %>">
                            </div>
 
 							<!-- SELECT teacherId, COUNT(*) AS total FROM teacherlog where action = "login" GROUP BY teacherId; -->
                            <div class="panel-body">
				            	<div id="chart4canvas" style="width:800px; height:600px;"></div> 
                            </div>

                        </div>
                    </div>

                    
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
								<div class="form-group">
									<label class="radio-inline"><input id="radioLogins"  value="Logins" type="radio" name="optRadio" checked>Logins</label>
									<label class="radio-inline"><input id="radioActions" value="Actions" type="radio" name="optRadio">Actions</label>
									<label class="radio-inline"><input id="radioLogouts" value="Logouts" type="radio" name="optRadio">Logouts</label>
								</div>
                            </div>
                            <div class="panel-body report_filters">                           
								  <input id="showReport5Btn" class="btn btn-lg btn-primary" onclick="showReport5();" type="submit" value="<%= rb.getString("show_report") %>">
                            </div>
 
 							<!-- SELECT teacherId, COUNT(*) AS total FROM teacherlog where action = "login" GROUP BY teacherId; -->
                            <div class="panel-body">
				            	<div id="chart5canvas" style="width:800px; height:600px;"></div> 
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
    <div id="AdminTools" class="col-sm-12 tab-pane fade">
      <h3>Admin Tools</h3>

		<div class="row">
			<div id="trendsCohortName">				
			</div>
		</div>			
		<div class="form-group">
			<div class="row">
				<div class="col-md-12">
					<div class=" offset-md-2 col-md-4">					
						<label class="radio"><input type="radio" name="optradio" checked>Show data for [X] prior weeks.</label>
						<input type="text" maxlength="2" size="2" class="form-control" style="width: 50px;" id="priorWeeks">
					</div>
				</div>
			</div>
			<br>
			<div class="row">
				<div class="col-md-12">
					<div class=" offset-md-2 col-md-2">
						<label class="radio"><input type="radio" name="optradio">Show from beginning.</label>
					</div>				
				</div>				
			</div>
		</div>
		<br>
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
                    
                    <div class="panel panel-default">
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
                    
                    <div class="panel panel-default">
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
 
                            <div class="panel-body">
                                <div id="admin3Status" class="table table-striped table-bordered hover display nowrap" width="100%"></div>
                            </div>

                        </div>
                    </div>                   
                    
                                       
            	</div>
        	</div>
		</div>
  </div>
    <div id="Notifications" class="col-sm-12 tab-pane fade">
      <h3>Admin Tools</h3>
      <p>Tools used to support Mathspring</p>
		<div class="dropdown">
		  <button type="button" class="btn btn-basic dropdown-toggle" data-toggle="dropdown">
		    Administrator Tools
		  </button>
		  <div class="dropdown-menu">
		    <a class="dropdown-item" href="#">Modify MS Global settings</a><br>
		    <a class="dropdown-item" href="#">Create a cohort</a><br>
		    <a class="dropdown-item" href="#">What else</a><br>
		  </div>
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

