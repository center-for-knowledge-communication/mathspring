<!-- Frank 	10-15-19	Issue #7 perStudentperProblemReport report -->
<!-- Frank 	10-15-19	Issue #8 X buttons to close accordian -->
<!-- Frank 	11-25-19	Issue #13 add standards filter for per student per problem report -->
<!-- Frank 	11-25-19	Issue #21 added logging of teacher event -->
<!-- Frank  01-20-20    Issue #39 and #48 use classId as alternative password -->
<!-- Frank  02-17-20    ttfixesR3 -->
<!-- Frank  03-03-20    Issue #48 more instructions -->
<!-- Frank  04-30-20    Issue #96 download not using filter -->
<!-- Frank  05-07-20    Issue #73 restrict thumbnail sizes in reports Max-width 400 max-height 400 -->
<!-- Frank  05-07-20    Issue #73 change thumbnail locations to 'top' for some reports. Fixes thumbnails off-screen.  -->
<!-- Frank  06-17-20    Issue #149 -->
<!-- Frank	07-08-20	Issue #134 156 162 -->
<!-- Frank	07-13-20	Issue #156 added continue? modal for deletes -->
<!-- Frank	07-13-20	Issue #170 removed profile option -->
<!-- Frank	07-17-20	Issue #122 added distance learning option to 'manage students' -->
<!-- Frank  07-20-20    Issue #180 Manage Topics - truncate problem nicknames to fit screen -->
<!-- Frank  07-28-20    Issue #74 protect from URL editting of teacherId and classId-->
<!-- Frank	08-03-20	Issue #122 change distance learning email text to reminder student to write down password -->
<!-- Frank	08-08-20	Issue #51 fix year selection -->
<!-- Kartik 08-10-20    Issue #75 fixed issue where bar chart increased every time it is clicked -->
<!-- Frank	08-10-20	Issue #196 splash page, split 'Manage Students' into 2 menu items -->
<!-- Frank	08-15-20	Issue #200 reverse danger and warning colors, text in common cluster report -->
<!-- Frank	08-15-20	Issue #49 added UI for deleting inactive students from current class -->
<!-- Frank	08-15-20	Issue #148 added time period (days) filter for perStudentPerProblemSet report -->
<!-- Frank	10-01-20	Issue #254R2 fix edit class form - language and schoolYear -->
<!-- Frank	10-06-20	Issue #267 fix edit class form vaildation -->
<!-- Frank	10-06-20	Issue #267 hide language selection on edit class form -->
<!-- Frank	10-12-20	Issue #272 SPLIT off from classDetail.jsp -->
<!-- Frank	11-23-20	Issue #148R3 add lastXdays filter to perCluster Report -->
<!-- Frank 12-11-20 Issue #315 default locale to en_US -->
<!-- Frank 12-18-20 Issue #336 added cache-busting for selected .js and .css files -->
<!-- Frank 12-26-20  	Issue #329 fix errors from splitting classDetails.jsp -->
<!-- Frank 03-22-21  	Issue #391 change date selection to date range popups -->
<!-- Frank 03-31-21  	Issue #418 add student dropdown and put selection in  filter -->
<!-- Frank 03-31-21  	Issue #418R4 add paging, padding and legend control -->
<!-- Frank 03-31-21  	Issue #418 display problem nickname when column header is clicked -->
<!-- Frank 05-01-21  	Hide survey selection and panel -->
<!-- Frank 05-11-21  	Issue #463 add date filter to perStudent report -->
<!-- Frank 05-11-21  	Implement multi-lingual chnique for cdn datatable utility using java resource bundle -->
<!-- Frank 05-17-21  	Issue #471 Show survey selection if logged on as Master-->
<!-- Frank 05-19-21  	Issue #474 add max-width and max-height to collective effort problem image display -->

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="springForm" uri="http://www.springframework.org/tags/form" %>
<%@page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>

<%@ page import="javax.servlet.http.HttpSession"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="edu.umass.ckc.wo.ttmain.ttservice.util.TeacherLogger"%>

<%

ResourceBundle versions = null; 
try {
	 versions = ResourceBundle.getBundle("Versions");
	 System.out.println("css_version=" + versions.getString("css_version"));
	 System.out.println("js_version=" + versions.getString("js_version"));
}
catch (Exception e) {
	 System.out.println("versions bundle ERROR");	 
//	logger.error(e.getMessage());	
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

ResourceBundle jc_rb = null;
try {
	jc_rb = ResourceBundle.getBundle("jchartML",loc);
}
catch (Exception e) {
//	logger.error(e.getMessage());
}

String msContext = request.getContextPath();
String msURL = request.getRequestURL().toString();
int index = msURL.indexOf(msContext);
String msHost = msURL.substring(0,index);
System.out.println("msHost = " + msHost + msContext);
%>

<!DOCTYPE HTML>
<html>
<head>
    <meta name="theme-color" content="#ffffff">
    <link rel="apple-touch-icon" sizes="180x180" href="${pageContext.request.contextPath}/img/apple-touch-icon.png">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="${pageContext.request.contextPath}/css/manifest.json">

    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/js/bootstrap/css/style.css">
    <link href="${pageContext.request.contextPath}/js/jquery-ui-1.10.4.custom/css/spring/jquery-ui-1.10.4.custom.min.css"
          rel="stylesheet">
    <link rel="stylesheet" href="<c:url value="/js/bootstrap/css/bootstrap.min.css" />"/>
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/ttStyleMain.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/calendar.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet">

    <!-- Datatables Css Files -->
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



    <style>
        .buttonCustomColor {
            color: #FFFFFF;
        }
    </style>
    <style> 
        input.largerCheckbox { 
            width: 20px; 
            height: 20px; 
        } 
    </style>
    
    <script type="text/javascript" src="<c:url value="/js/bootstrap/js/jquery-2.2.2.min.js" />"></script>
    <!-- js for bootstrap-->
    <script type="text/javascript" src="<c:url value="/js/bootstrap/js/bootstrap.min.js" />"></script>
    <script src="<c:url value="/js/jquery-ui-1.10.4.custom/js/jquery-ui-1.10.4.custom.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js" />"></script>


    <script type="text/javascript"
            src="<c:url value="https://cdn.datatables.net/1.10.20/js/dataTables.bootstrap4.min.js" />"></script>
    <script type="text/javascript"
            src="<c:url value="https://cdn.datatables.net/rowreorder/1.2.0/js/dataTables.rowReorder.min.js" />"></script>
    <script type="text/javascript"
            src="<c:url value="https://cdn.datatables.net/select/1.3.1/js/dataTables.select.min.js" />"></script>

    <script type="text/javascript"            
			src="<c:url value="https://cdn.datatables.net/fixedcolumns/3.3.0/js/dataTables.fixedColumns.min.js" />"></script>

    <script type="text/javascript"
            src="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-validator/0.4.5/js/bootstrapvalidator.min.js" />"></script>

    <script type="text/javascript"
            src="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.bundle.min.js" />"></script>
    <script type="text/javascript"
            src="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js" />"></script>



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
 
var studentList; 
 //Report1 Variables
var perProblemSetReport;
var perProblemSetLevelOne;
var perProblemSetLevelTwo;
var perProblemSetColumnNamesMap;
var perProblemSetLevelOneAvg;
var perProblemSetLevelOneMax;
var perProblemSetLevelOneLatest;
var filterOne = "~~Y";

//Report6
var perStudentperProblemReport;
var perStudentperProblemLevelOne;
var perStudentPerProblemColumnNamesMap;
var perStudentPerProblemXrefMap;
var filterSix = "~~Y";

//var urlColumnNames;

//Report2 Varriables
var perProblemReportTable

//Report3 Varriables
var perClusterReportTable

//Report4 variables
var filterFour = "~~";

//Report3 variables
var filterThree = "~~";



//Report5 Varribales
var perStudentReport;
var effortMap;
var perProblemObject;
var emotionMap;
var commentsMap;
var eachStudentData = [];

var studentRosterTable;
var surveyData;
var surveyReportTable;
var surveystudentData;
var surveyStudentTable;
var surveyQuestionTable;

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
          "SHELP" : "Got the problem correct but saw at least one video.",
          "NO DATA" : "No data could be gathered."
    }
}

function hideLegend() {
    $("#hideLegendBtn").hide();
    $("#showLegendBtn").show();
    $("#perStudentPerProblemLegend").hide();
}
function showLegend() {
    $("#showLegendBtn").hide();
    $("#hideLegendBtn").show();
    $("#perStudentPerProblemLegend").show();
}



function getStudentListSix() {
	
    $.ajax({
        type : "POST",
        url :pgContext+"/tt/tt/getStudentList",
        data : {
            classId: classID
        },
        success : function(response) {
        	console.log(response);
        	studentListSix = response;
        },
        error : function(e) {
            console.log(e);
        }
    });
	
}

var studentSelectionListSix = "";
function populateStudentSelectionListSix() {
	
	var studentsArrSix = studentListSix.split(",");	

	studentSelectionListSix = "<select name='students' id='studentsSix' size='5' style='width:220px' >"; 	
	studentsArrSix.forEach(addStudentSix1);
	studentsArrSix.forEach(addStudentSix2);
	studentSelectionListSix += "</select>";
	document.getElementById("studentSelectionListSix").innerHTML=studentSelectionListSix; 

}

function addStudentSix1(item, index) {
	var sArr = item.split("~");
	if ((sArr[2].length + sArr[1].length) > 0) {
		studentSelectionListSix += "<option value='" + sArr[3]  + "'>" + sArr[2] + " "  +  sArr[1]  + "</option>";
	}
}

function addStudentSix2(item, index) {
	var sArr = item.split("~");
	if  ((sArr[2].length + sArr[1].length) == 0) {
		studentSelectionListSix += "<option value='" + sArr[3]  + "'>" + sArr[0]  + "</option>";
	}
}



function getFilterSix() {
	
	document.getElementById("daysFilterSix").value = "";
		
	var showNamesState = "N";
	if (document.getElementById("showNamesSix").checked == true) {
		showNamesState = "Y";
	}

	var selectedStudent =  document.getElementById("studentsSix").value;

	var d1 = parseInt(document.getElementById("selectDay_r6_cal2").value);
	var d2 =  parseInt(document.getElementById("selectDay_r6_cal1").value);

	var m1 = parseInt(document.getElementById("month_r6_cal2").value) + 1;
	var m2 =  parseInt(document.getElementById("month_r6_cal1").value) + 1;
	
	if ((d1 > 0) && (d2 > 0)) {
		$('#calendarModalPopupSix').modal('hide');

		var fromDate = m1 + "/" + document.getElementById("selectDay_r6_cal2").value + "/" +  document.getElementById("year_r6_cal2").value;
		var toDate = m2 + "/" + document.getElementById("selectDay_r6_cal1").value + "/" + document.getElementById("year_r6_cal1").value;

		if (languageSet == "es") {
			fromDate = document.getElementById("selectDay_r6_cal2").value + "/" +  m1 + "/" + document.getElementById("year_r6_cal2").value;
			toDate = document.getElementById("selectDay_r6_cal1").value + "/" + m2 + "/" + document.getElementById("year_r6_cal1").value;
		}
		
		var older = Date.parse(fromDate);
		var newer = Date.parse(toDate);
		if (newer < older) {
			var temp = fromDate;
			fromDate = toDate;
			toDate = temp;
		}	

		document.getElementById("daysFilterSix").value = fromDate + " thru " + toDate;
		filterSix = document.getElementById("standardsFilter").value + "~" + document.getElementById("daysFilterSix").value + "~" + showNamesState;
		
		if (selectedStudent.length > 0) {
			filterSix += "~" + selectedStudent;	
		}
		
		var a_href = '${pageContext.request.contextPath}';
		a_href = a_href + "/tt/tt/downLoadPerStudentPerProblemReport?teacherId=";
		a_href = a_href + teacherID;
		a_href = a_href + "&classId=";
		a_href = a_href + ${classInfo.classid};
		a_href = a_href + "&filter=";
		a_href = a_href + filterSix;
		document.getElementById("downloadReportSixBtn").href = a_href;
		
	}
	else {
		if ((d1 + d2) == 0) {
			document.getElementById("daysFilterSix").value = "";
			filterSix = document.getElementById("standardsFilter").value + "~" + document.getElementById("daysFilterSix").value + "~" + showNamesState;

			if (selectedStudent.length > 0) {
				filterSix += "~" + selectedStudent;	
			}
			var a_href = '${pageContext.request.contextPath}';
			a_href = a_href + "/tt/tt/downLoadPerStudentPerProblemReport?teacherId=";
			a_href = a_href + teacherID;
			a_href = a_href + "&classId=";
			a_href = a_href + ${classInfo.classid};
			a_href = a_href + "&filter=";
			a_href = a_href + filterSix;
			document.getElementById("downloadReportSixBtn").href = a_href;
		}
		else {
			alert("<%= rb.getString("must_select_a_day_from_each_calendar") %>");			
		}
	}
	
}

function getFilterOne() {

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
		filterOne = "~" + document.getElementById("daysFilterOne").value + "~" + "Y";
	
		var a_href = '${pageContext.request.contextPath}';
		a_href = a_href + "/tt/tt/downLoadPerProblemSetReport?teacherId=";
		a_href = a_href + teacherID;
		a_href = a_href + "&classId=";
		a_href = a_href + ${classInfo.classid};
		a_href = a_href + "&filter=";
		a_href = a_href + filterOne;
		document.getElementById("downloadReportOneBtn").href = a_href;
	}
	else {
		if ((d1 + d2) == 0) {
			document.getElementById("daysFilterOne").value = "";
			filterOne = "~" + "" + "~" + "Y";			
			var a_href = '${pageContext.request.contextPath}';
			a_href = a_href + "/tt/tt/downLoadPerProblemSetReport?teacherId=";
			a_href = a_href + teacherID;
			a_href = a_href + "&classId=";
			a_href = a_href + ${classInfo.classid};
			a_href = a_href + "&filter=";
			a_href = a_href + filterOne;
			document.getElementById("downloadReportOneBtn").href = a_href;
		}
		else {
			alert("<%= rb.getString("must_select_a_day_from_each_calendar") %>");
		}
	}
}

function getFilterFour() {

	document.getElementById("daysFilterFour").value = "";
	
	var d1 = parseInt(document.getElementById("selectDay_r4_cal2").value);
	var d2 =  parseInt(document.getElementById("selectDay_r4_cal1").value);

	var m1 = parseInt(document.getElementById("month_r4_cal2").value) + 1;
	var m2 =  parseInt(document.getElementById("month_r4_cal1").value) + 1;
	
	if ((d1 > 0) && (d2 > 0)) {
		$('#calendarModalPopupFour').modal('hide');

		var fromDate = m1 + "/" + document.getElementById("selectDay_r4_cal2").value + "/" +  document.getElementById("year_r4_cal2").value;
		var toDate = m2 + "/" + document.getElementById("selectDay_r4_cal1").value + "/" + document.getElementById("year_r4_cal1").value;

		if (languageSet == "es") {
			fromDate = document.getElementById("selectDay_r4_cal2").value + "/" +  m1 + "/" + document.getElementById("year_r4_cal2").value;
			toDate = document.getElementById("selectDay_r4_cal1").value + "/" + m2 + "/" + document.getElementById("year_r4_cal1").value;
		}
		
		var older = Date.parse(fromDate);
		var newer = Date.parse(toDate);
		if (newer < older) {
			var temp = fromDate;
			fromDate = toDate;
			toDate = temp;
		}	
		
		document.getElementById("daysFilterFour").value = fromDate + " thru " + toDate;
		
	
		filterFour = "~" + document.getElementById("daysFilterFour").value + "~" + "Y";		
		
		var a_href = '${pageContext.request.contextPath}';
		a_href = a_href + "/tt/tt/downLoadPerClusterReport?teacherId=";
		a_href = a_href + teacherID;
		a_href = a_href + "&classId=";
		a_href = a_href + ${classInfo.classid};
		a_href = a_href + "&filter=";
		a_href = a_href + filterFour;
		document.getElementById("downloadReportFourClusterBtn").href = a_href;
	
		a_href = '${pageContext.request.contextPath}';
		a_href = a_href + "/tt/tt/downLoadPerProblemReport?teacherId=";
		a_href = a_href + teacherID;
		a_href = a_href + "&classId=";
		a_href = a_href + ${classInfo.classid};
		a_href = a_href + "&filter=";
		a_href = a_href + filterFour;
		document.getElementById("downloadReportFourProblemBtn").href = a_href;
	}
	else {
		if ((d1 + d2) == 0) {
			document.getElementById("daysFilterFour").value= "";	
		
			filterFour = "~" + "" + "~" + "Y";		
			
			var a_href = '${pageContext.request.contextPath}';
			a_href = a_href + "/tt/tt/downLoadPerClusterReport?teacherId=";
			a_href = a_href + teacherID;
			a_href = a_href + "&classId=";
			a_href = a_href + ${classInfo.classid};
			a_href = a_href + "&filter=";
			a_href = a_href + filterFour;
			document.getElementById("downloadReportFourClusterBtn").href = a_href;
		
			a_href = '${pageContext.request.contextPath}';
			a_href = a_href + "/tt/tt/downLoadPerProblemReport?teacherId=";
			a_href = a_href + teacherID;
			a_href = a_href + "&classId=";
			a_href = a_href + ${classInfo.classid};
			a_href = a_href + "&filter=";
			a_href = a_href + filterFour;
			document.getElementById("downloadReportFourProblemBtn").href = a_href;	
		}
		else {
			alert("<%= rb.getString("must_select_a_day_from_each_calendar") %>");
		}
	}
}



function getStudentListThree() {
	
    $.ajax({
        type : "POST",
        url :pgContext+"/tt/tt/getStudentList",
        data : {
            classId: classID
        },
        success : function(response) {
        	console.log(response);
        	studentListThree = response;
        },
        error : function(e) {
            console.log(e);
        }
    });
	
}

var studentSelectionListThree = "";
function populateStudentSelectionListThree() {
	
	var studentsArrThree = studentListThree.split(",");	

	studentSelectionListThree = "<select name='students' id='studentsThree' size='5' style='width:220px' >"; 	
	studentsArrThree.forEach(addStudentThree1);
	studentsArrThree.forEach(addStudentThree2);
	studentSelectionListThree += "</select>";
	document.getElementById("studentSelectionListThree").innerHTML=studentSelectionListThree; 

}

function addStudentThree1(item, index) {
	var sArr = item.split("~");
	if ((sArr[2].length + sArr[1].length) > 0) {
		studentSelectionListThree += "<option value='" + sArr[3]  + "'>" + sArr[2] + " "  +  sArr[1]  + "</option>";
	}
}

function addStudentThree2(item, index) {
	var sArr = item.split("~");
	if  ((sArr[2].length + sArr[1].length) == 0) {
		studentSelectionListThree += "<option value='" + sArr[3]  + "'>" + sArr[0]  + "</option>";
	}
}




function getFilterThree() {
	
	document.getElementById("daysFilterThree").value = "";
		
	var showNamesState = "N";
	if (document.getElementById("showNamesThree").checked == true) {
		showNamesState = "Y";
	}

	var selectedStudent =  document.getElementById("studentsThree").value;

	var d1 = parseInt(document.getElementById("selectDay_r3_cal2").value);
	var d2 =  parseInt(document.getElementById("selectDay_r3_cal1").value);

	var m1 = parseInt(document.getElementById("month_r3_cal2").value) + 1;
	var m2 =  parseInt(document.getElementById("month_r3_cal1").value) + 1;
	
	if ((d1 > 0) && (d2 > 0)) {
		$('#calendarModalPopupThree').modal('hide');

		var fromDate = m1 + "/" + document.getElementById("selectDay_r3_cal2").value + "/" +  document.getElementById("year_r3_cal2").value;
		var toDate = m2 + "/" + document.getElementById("selectDay_r3_cal1").value + "/" + document.getElementById("year_r3_cal1").value;

		if (languageSet == "es") {
			fromDate = document.getElementById("selectDay_r3_cal2").value + "/" +  m1 + "/" + document.getElementById("year_r3_cal2").value;
			toDate = document.getElementById("selectDay_r3_cal1").value + "/" + m2 + "/" + document.getElementById("year_r3_cal1").value;
		}
		
		var older = Date.parse(fromDate);
		var newer = Date.parse(toDate);
		if (newer < older) {
			var temp = fromDate;
			fromDate = toDate;
			toDate = temp;
		}	

		document.getElementById("daysFilterThree").value = fromDate + " thru " + toDate;
		filterThree = document.getElementById("standardsFilter").value + "~" + document.getElementById("daysFilterThree").value + "~" + showNamesState;
		
		if (selectedStudent.length > 0) {
			filterThree += "~" + selectedStudent;	
		}
		
		var a_href = '${pageContext.request.contextPath}';
		a_href = a_href + "/tt/tt/downLoadPerStudentReport?teacherId=";
		a_href = a_href + teacherID;
		a_href = a_href + "&classId=";
		a_href = a_href + ${classInfo.classid};
		a_href = a_href + "&filter=";
		a_href = a_href + filterThree;
		document.getElementById("downloadReportThreeStudentBtn").href = a_href;
			
		a_href = '${pageContext.request.contextPath}';
		a_href = a_href + "/tt/tt/downLoadStudentEmotions?teacherId=";
		a_href = a_href + teacherID;
		a_href = a_href + "&classId=";
		a_href = a_href + ${classInfo.classid};
		a_href = a_href + "&filter=";
		a_href = a_href + filterThree;
		document.getElementById("downloadReportThreeEmotionBtn").href = a_href;		
		
	}
	else {
		if ((d1 + d2) == 0) {
			document.getElementById("daysFilterThree").value = "";
			filterThree = document.getElementById("standardsFilter").value + "~" + document.getElementById("daysFilterThree").value + "~" + showNamesState;

			if (selectedStudent.length > 0) {
				filterThree += "~" + selectedStudent;	
			}
			var a_href = '${pageContext.request.contextPath}';
			a_href = a_href + "/tt/tt/downLoadPerStudentReport?teacherId=";
			a_href = a_href + teacherID;
			a_href = a_href + "&classId=";
			a_href = a_href + ${classInfo.classid};
			a_href = a_href + "&filter=";
			a_href = a_href + filterThree;
			document.getElementById("downloadReportThreeStudentBtn").href = a_href;
		
			a_href = '${pageContext.request.contextPath}';
			a_href = a_href + "/tt/tt/downLoadStudentEmotions?teacherId=";
			a_href = a_href + teacherID;
			a_href = a_href + "&classId=";
			a_href = a_href + ${classInfo.classid};
			a_href = a_href + "&filter=";
			a_href = a_href + filterThree;
			document.getElementById("downloadReportThreeEmotionBtn").href = a_href;
		}
		else {
			alert("<%= rb.getString("must_select_a_day_from_each_calendar") %>");			
		}
	}
	
}



function nicknameOpen(problemName) {

    var tmpProblemName = "" + problemName;
    var tmp = perStudentPerProblemXrefMap[tmpProblemName];
    var tmpArr = tmp.split("^");
	document.getElementById("perStudentPerProblemImageHdr").textContent = "<%= rb.getString("problem_id")%>" + ": " + tmpProblemName;
	document.getElementById("perStudentPerProblemContent").textContent = "" + tmpArr[1];
	$("#ModalPopupProblem").modal('show');
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
                    backgroundColor: "#26f213",
                    label: 'CORRECTO: El estudiante resolvió el problema correctamente en el PRIMER intento, sin ninguna ayuda.',
                    data: [effortValues[3]]
                }, {
                    backgroundColor: "#9beb94",
                    label: 'AUTOCORRIGIO: El estudiante INTENTÓ una vez incorrectamente, pero se corrigió (contestó correctamente) en el segundo intento, no se solicitó ayuda.',
                    data: [effortValues[4]]
                }, {
                    backgroundColor: "#fb8072",
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
                            backgroundColor: "#26f213",
                            label: 'SOF: The student SOLVED the problem correctly on the FIRST attempt, without any help.',
                            data: [effortValues[3]]
                        }, {
                            backgroundColor: "#9beb94",
                            label: 'ATT: The student ATTEMPTED once incorrectly, but self-corrected (answered correctly) in the second attempt, no help.',
                            data: [effortValues[4]]
                        }, {
                            backgroundColor: "#fb8072",
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
                responsive: true,
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
    } 
  }else {
        $('#problemSnapshot').empty();
        var completeValues = perProblemObject[rows];
        var effortValues = completeValues['studentEffortsPerProblem'];
        var imageURL = problem_imageURL+rows+'.jpg';
        var legendChart = "#lengendTable";
        $('#problemSnapshot').append('<span><strong><%= rb.getString("problem_id")%> :'+rows+'</strong></span>');
        $('#problemSnapshot').append('<img  style="max-width:600px; max-height:600px;" src="'+imageURL +'"/>');
               
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
                backgroundColor: "#26f213",
                label: 'SOF: El estudiante resolvió el problema correctamente en el PRIMER intento, sin ninguna ayuda.',
                data: [effortValues[3]]
            }, {
                backgroundColor: "#9beb94",
                label: 'ATT: El estudiante INTENTÓ una vez incorrectamente, pero se corrigió (contestó correctamente) en el segundo intento, no se solicitó ayuda.',
                data: [effortValues[4]]
            }, {
                backgroundColor: "#fb8072",
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
                        backgroundColor: "#26f213",
                        label: 'SOF: The student SOLVED the problem correctly on the FIRST attempt, without any help.',
                        data: [effortValues[3]]
                    }, {
                        backgroundColor: "#9beb94",
                        label: 'ATT: The student ATTEMPTED once incorrectly, but self-corrected (answered correctly) in the second attempt, no help.',
                        data: [effortValues[4]]
                    }, {
                        backgroundColor: "#fb8072",
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
                responsive: true,
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
                        responsive: true,
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







function handleclickHandlers() {
	
    $('#reports_handler').click(function () {
        $('#reorg_prob_sets_handler').css('background-color', '');
        $('#reorg_prob_sets_handler').css('color', '#dddddd');

        $("#content-conatiner").children().hide();
        $("#report-wrapper").show();
        $("#report-wrapper2").show();
        $("#perStudentPerProblemSetReport").hide();
        $("#perClusterReport").hide();
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
//    $('#reorg_prob_sets_handler').css('background-color','#e6296f');
//    $('#reorg_prob_sets_handler').css('color', '#ffffff');


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


    perProblemSetReport = $('#perStudentPerProblemSetReport').DataTable({
        data: [],
        <%=jc_rb.getString("language_text")%>
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
	
    
    perProblemReportTable = $('#perProblemReport').DataTable({
        data: [],
        <%=jc_rb.getString("language_text")%>
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
    	
    
    var cc_headers = changeReportFourHeaderAccordingToLanguage();

    perClusterReportTable = $('#perClusterReport').DataTable({
        data: [],
        <%=jc_rb.getString("language_text")%>
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
	
    
    var headers = changeReportThreeHeaderAccordingToLanguage();

    
    perStudentReport  =  $('#perStudentReport').DataTable({
        data: [],
        <%=jc_rb.getString("language_text")%>
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
                    var dataContent = "<div id="+containerChart+" style='width:900px;height:600px;display:none'><div class='panel panel-default'><div class='panel-heading'>"+headers['effchart']+"</div><div class='panel-body'><canvas width='800' height='150' id="+effortChartId+"></canvas></div><div class='panel-body' id='"+legendChart+"'></div></div></div>";
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
        var studentId = row.data()['studentId'];

        $.ajax({
            type: "POST",
            url: pgContext + "/tt/tt/getMasterProjectionsForCurrentTopic",
            data: {
                classId: classID,
                topicID: topicId,
                studentId: studentId,
                filter: filterOne
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
                    <%=jc_rb.getString("language_text")%>
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
                            placement: 'top',
                            content: function () {
                                return '<img style="max-width:400px; max-height:400px;" src="' + $(this).data('img') + '" />';
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
        	        	
            var studentDataList = eachStudentData[rowID];
            var outputStudentDataList = Object.keys(studentDataList).map(function(key) {return studentDataList[key];});

            var allProblemsHeader;


        	
            outputStudentDataList.sort(function(a,b) {
                if(a[10] == 'Problem was not completed')
                    return new Date('1900-01-01 00:00:01.0').getTime() - new Date('1900-01-01 00:00:00.0').getTime();
                if( b[10] == 'Problem was not completed' )
                    return new Date('1900-01-01 00:00:00.0').getTime() - new Date('1900-01-01 00:00:01.0').getTime();
                else
                    return new Date(b[10]).getTime() - new Date(a[10]).getTime();
            });
                                      
            //show all other problems
            allProblemsHeader = '<table id='+rowID+' class="table table-striped table-bordered" cellspacing="0" width="100%"><thead><tr><th> <%= rb.getString("problem")%></th><th> <%= rb.getString("problem_nickname")%></th><th> <%= rb.getString("problem_finished_on")%></th><th> <%= rb.getString("problem_description")%></th><th> <%= rb.getString("solved_correctly")%></th><th> <%= rb.getString("nbr_mistakes_made")%></th><th> <%= rb.getString("nbr_hints_seen")%></th><th> <%= rb.getString("nbr_attempts_made")%></th><th> <%= rb.getString("nbr_videos_seen")%></th><th> <%= rb.getString("nbr_examples_seen")%></th><th><%= rb.getString("effort")%></th></tr></thead><tbody>';
            $.each(outputStudentDataList, function (i, obj) {
                var correctHtml = "";
                var imageURL = problem_imageURL+obj[11]+'.jpg';
                var problemImgHTML = "<td> <a style='cursor:pointer' rel='popover' data-img='" + imageURL + "'>" + obj[0] + "</a></td>"
                var effortLabelHTML = "<td> <a style='cursor:pointer' rel='popoverLabel' data-content='"+effortLabelMap[obj[8]]+"'>" + obj[8] + "</a></td>"
                if ("1" == obj[4])
                    correctHtml = "<td><img style='width:15%;' src='"+servletContextPath+"/images/check.png'/></td>";
                else
                    correctHtml = "<td><img style='width:15%;' src='"+servletContextPath+"/images/x.png'/></td>";

                allProblemsHeader += "<tr>" + problemImgHTML + "<td>" + obj[1] + "</td><td>" +obj[10]+ "</td><td>" + obj[2] + "</td>" + correctHtml + "<td>" + obj[5] + "</td><td>" + obj[6] + "</td><td>" + obj[7] + "</td><td>" + obj[12] + "</td><td>" + obj[13] + "</td>"+effortLabelHTML+"</tr>";

            });
            allProblemsHeader += "</tbody></table></div>"

            var tabDetails = '<div id="allProblemsPane" class="border border-2" >'+allProblemsHeader+'</div>'
            row.child(tabDetails).show();
  
            
            $('a[rel=popover]').popover({
                html: true,
                trigger: 'hover',
                placement: 'top',
                container: 'body',
                content: function () {
                    return '<img style="max-width:400px; max-height:400px;" src="' + $(this).data('img') + '" />';
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
                $('#completeMasteryForStudent').modal('hide');
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
                $('#completeMasteryForStudent').modal('hide');
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
                $('#completeMasteryForStudent').modal('hide');
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

    $('#showReportOneBtn').on('click', function ()  {    	
        $('#collapseOne').find('.loader').show();
        $.ajax({
            type : "POST",
            url : pgContext+"/tt/tt/getTeacherReports",
            data : {
                classId: classID,
                teacherId: teacherID,
                reportType: 'perStudentPerProblemSetReport',
                lang: loc,
                filter: filterOne
            },
            success : function(data) {
                $('#collapseOne').find('.loader').hide();
                $("#perStudentPerProblemSetReport").show();
                var jsonData = $.parseJSON(data);
                perProblemSetLevelOne = jsonData.levelOneData;
                perProblemSetColumnNamesMap = jsonData.columns;

                var indexcolumn = 3;
                var columNvalues = $.map(perProblemSetColumnNamesMap, function (v) {
                        var temp = {
                            "title": v, "name": v.replace(/\s/g, ''), "targets": indexcolumn,
                            "createdCell": function (td, cellData, rowData, row, col) {
                                if (cellData == null) {
                                    $(td).text();
                                    return;
                                }
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
                    $('#perStudentPerProblemSetReport').empty();
                }


                perProblemSetReport = $('#perStudentPerProblemSetReport').DataTable({
                    data: perProblemSetLevelOneFullTemp,
                    <%=jc_rb.getString("language_text")%>
                    "fixedColumns": {
                        "leftColumns": 2,
                        "heightMatch": 'auto'                        
                    },                   
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
                            placement: 'top',
                            content: function () {
                                return '<ul><li><a style="cursor: pointer;" class="getCompleteMasteryByAverage"> <%= rb.getString("get_mastery_by_average")%> </a></li>' +
                                '<li><a style="cursor: pointer;" class="getCompleteMasteryByMax"> <%= rb.getString("get_mastery_by_highest")%></a></li>' +
                                '<li><a style="cursor: pointer;" class="getCompleteMasteryByLatest"> <%= rb.getString("get_mastery_by_last_reported")%></a></li></ul>';
                        }
                        })
                    }
                });
                
            }
        });


    });
   
    $('#showReportSixBtn').on('click', function ()  {
        $('#collapseSix').find('.loader').show();
        var showNamesState = "N";
        if (document.getElementById("showNamesSix").checked == true) {
        	showNamesState = "Y";
        }

        filterSix=document.getElementById("standardsFilter").value + "~" + document.getElementById("daysFilterSix").value + "~" + showNamesState;
 
    	var selectedStudent =  document.getElementById("studentsSix").value;
		if (selectedStudent.length > 0) {
			filterSix += "~" + selectedStudent;	
		}
		
        $.ajax({
            type : "POST",
            url : pgContext+"/tt/tt/getTeacherReports",
            data : {
                classId: classID,
                teacherId: teacherID,
                reportType: 'perStudentPerProblemReport',
                lang: loc,
                filter: filterSix
            },
            success : function(data) {
                $('#collapseSix').find('.loader').hide();
                //alert(data);
                var jsonData = $.parseJSON(data);
                perStudentperProblemLevelOne = jsonData.levelOneData;               
                perStudentPerProblemColumnNamesMap = jsonData.columns;
                perStudentPerProblemXrefMap = jsonData.IdXref;
                var problemImageWindow = [];
 
                var txt="";
                var abbr="";

                var popover = "popoverTop";
                var indexcolumn = 3;
                var columNvalues = $.map(perStudentPerProblemColumnNamesMap, function (v) {
                        var temp = {
  		
                            "title": v, "name": v.replace(/\s/g, ''), "targets": indexcolumn,
                            "createdCell": function (td, cellData, rowData, row, col) {
                                if (cellData == '') {
                                    $(td).text("");
                                    return;
                                }
                                
                                var trow = row % 16;
								if (trow > 8) {
									popover = "popoverTop";
								}
								else {
									popover = "popoverBottom";
								}
								
                                var cellArray = cellData.split("^");
                                var cellEffort = cellArray[0];
                                var cellDate   = cellArray[1];
                                var cellProblemId   = cellArray[2];
								
                                if ((cellData == null) || (cellData == "null")) {
                                	cellData = " ";
                                    $(td).html("ooops");
                                }                                
                                else if (cellEffort == "SKIP") {                                	
                                	txt = "<%= rb.getString("skip")%>";
                                    var abbr = txt.split(":");
                                    var imageURL = problem_imageURL+cellProblemId+'.jpg';
                                    $(td).html("<a style='cursor:pointer' rel='" + popover + "' data-img='" + imageURL + "'>" + "<p>" + abbr[0] + " " + cellDate + "</p>" + "</a>");
                                	$(td).addClass('p-SKIP');
                                }
                                else if (cellEffort == "NOTR") {
                                	txt = "<%= rb.getString("notr")%>";
                                    var abbr = txt.split(":");
                                    var imageURL = problem_imageURL+cellProblemId+'.jpg';
                                    $(td).html("<a style='cursor:pointer' rel='" + popover + "' data-img='" + imageURL + "'>" + "<p>" + abbr[0] + " " + cellDate + "</p>" + "</a>");
                                	$(td).addClass('p-NOTR');                                                                    
                                }
                                else if (cellEffort == "GIVEUP") {
                                	txt = "<%= rb.getString("giveup")%>";
                                    var abbr = txt.split(":");
                                    var imageURL = problem_imageURL+cellProblemId+'.jpg';
                                    $(td).html("<a style='cursor:pointer' rel='" + popover + "' data-img='" + imageURL + "'>" + "<p>" + abbr[0] + " " + cellDate + "</p>" + "</a>");
                                	$(td).addClass('p-GIVEUP');
                                }
                                else if (cellEffort == "SOF") {
                                	txt = "<%= rb.getString("sof")%>";
                                    var abbr = txt.split(":");
                                	var imageURL = problem_imageURL+cellProblemId+'.jpg';
//                                	$(td).html('<a href="'+pgContext+'/WoAdmin?action=AdminGetQuickAuthSkeleton&probId='+cellProblemId+'&teacherId=-1&reload=true&zoom=1" target="_blank" style="cursor:pointer" rel="popoverPerProblem" data-img="' + imageURL + '">' + '<p>' + abbr[0] + ' ' + cellDate + '</p>' + '</a>');
                                    $(td).html("<a style='cursor:pointer' rel='" + popover + "' data-img='" + imageURL + "'>" + "<p>" + abbr[0] + " " + cellDate + "</p>" + "</a>");
                                    $(td).addClass('p-SOF');                             
                                }
                                else if (cellEffort == "ATT") {
                                	txt = "<%= rb.getString("att")%>";
                                    var abbr = txt.split(":");2
                                    var imageURL = problem_imageURL+cellProblemId+'.jpg';
                                    $(td).html("<a style='cursor:pointer' rel='" + popover + "' data-img='" + imageURL + "'>" + "<p>" + abbr[0] + " " + cellDate + "</p>" + "</a>");
                                	$(td).addClass('p-ATT');
                                }
                                else if (cellEffort == "GUESS") {
                                	txt = "<%= rb.getString("guess")%>";
                                    var abbr = txt.split(":");
                                    var imageURL = problem_imageURL+cellProblemId+'.jpg';
                                    $(td).html("<a style='cursor:pointer' rel='" + popover + "' data-img='" + imageURL + "'>" + "<p>" + abbr[0] + " " + cellDate + "</p>" + "</a>");
                                	$(td).addClass('p-GUESS');
                                }
                                else if (cellEffort == "SHINT") {
                                	txt = "<%= rb.getString("shint")%>";
                                    var abbr = txt.split(":");
                                    var imageURL = problem_imageURL+cellProblemId+'.jpg';
                                    $(td).html("<a style='cursor:pointer' rel='" + popover + "' data-img='" + imageURL + "'>" + "<p>" + abbr[0] + " " + cellDate + "</p>" + "</a>");
                                	$(td).addClass('p-SHINT');
                                }
                                else if (cellEffort == "SHELP") {
                                	txt = "<%= rb.getString("shelp")%>";
                                    var abbr = txt.split(":");
                                    var imageURL = problem_imageURL+cellProblemId+'.jpg';
                                    $(td).html("<a style='cursor:pointer' rel='" + popover + "' data-img='" + imageURL + "'>" + "<p>" + abbr[0] + " " + cellDate + "</p>" + "</a>");
                                	$(td).addClass('p-SHELP');
                                }
                                else if (cellEffort == "NODATA") {
                                	txt = "<%= rb.getString("no_data")%>";
                                    var abbr = txt.split(":");
                                    var imageURL = problem_imageURL+cellProblemId+'.jpg';
                                    $(td).html("<a style='cursor:pointer' rel='" + popover + "' data-img='" + imageURL + "'>" + "<p>" + abbr[0] + " " + cellDate + "</p>" + "</a>");
                                	$(td).addClass('p-NODATA');
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

                columNvalues.unshift({"title" : "<%= rb.getString("student_name")%>","name":"studentName" , "width": "12%", "targets": [0]},
                        {"title" : "<%= rb.getString("username")%>","name":"userName", "width": "12%", "targets": [1]}, 
                        {"title" : "<%= rb.getString("student_id")%>","name":"studentId", "targets": [2], visible : false} );                

				var columDvalues = $.map(perStudentPerProblemColumnNamesMap, function(v) {
                    v = v.replace(/\s/g, '');
                    return  { width: "10%", data : v };
                }
            	);
               
                columDvalues.unshift({data: "studentName"},{data: "userName"},{data: "studentId"});
                var perStudentperProblemLevelOneFullTemp = [];
                $.map(perStudentperProblemLevelOne, function (item,k) {
                        var perStudentperProblemLevelOneTemp = {};
                                    item.forEach(function(e){
                                        var itemArrays = e.split("~~~");
                                        perStudentperProblemLevelOneTemp[itemArrays[0]] = itemArrays[1]
                                        perStudentperProblemLevelOneTemp['studentId'] = k;
                                })
                        perStudentperProblemLevelOneFullTemp.push(perStudentperProblemLevelOneTemp);
                    }
                );

      

                if (perStudentperProblemReport) {
                	perStudentperProblemReport.destroy();
                    $('#perStudentPerProblemReport').empty();
                }
                	perStudentperProblemReport = $('#perStudentPerProblemReport').DataTable({
                    data: perStudentperProblemLevelOneFullTemp,
                    <%=jc_rb.getString("language_text")%>
                    "fixedColumns": {
                        "leftColumns": 2,
                        "heightMatch": 'auto'                        
                    },
                    "columns": columDvalues,
                    "columnDefs": columNvalues,
                    "bPaginate": true,
                    "pageLength": 16,
                    "scrollX": true,
                    "scrollY": "600px",
                    "scrollCollapse": true,                    
                    "bFilter": false,
                    "bLengthChange": false,
                    rowReorder: false,
                    "bSort": false,
                    "drawCallback": function () {
                        $('a[rel=popoverHeader]').popover({
                        	html: true,
                            container : 'body',
                            trigger : 'hover',
                            placement: 'bottom'
                        });
                        $('a[rel=popoverTop]').popover({
                            html: true,
                            trigger: 'hover',
                            placement: 'top',
                            content: function () {
                                return '<img style="max-width:400px; max-height:400px;" src="' + $(this).data('img') + '" />';
                            }
                        });
                        $('a[rel=popoverBottom]').popover({
                            html: true,
                            trigger: 'hover',
                            placement: 'bottom',
                            content: function () {
                                return '<img style="max-width:400px; max-height:400px;" src="' + $(this).data('img') + '" />';
                            }
                        });
                    },
                    headerCallback: function headerCallback(thead, data, start, end, display) {
						var str = thead.cells.length;
						var i = 2;
						for (i=2;i<thead.cells.length;i++) {
							var inner = "" + thead.cells[i].innerText;
							var t_html = "<div class='btn btn-primary btn-sm' onclick='nicknameOpen(\"" + inner + "\");' >" + inner + "</div>";
	    					$(thead).find('th').eq(i).html(t_html);							
						}
                     }                    
                });
                
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
                            var attri = ", '<%= rb.getString("problem_preview")%>'"+","+"'width=750,height=550,status=yes,resizable=yes'";
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
                            placement: 'top',
                            container: 'body',
                            content: function () {
                                return '<img style="max-width:400px; max-height:400px;" src="' + $(this).data('img') + '" />';
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

    $('#showReportThreeBtn').on('click', function ()  {    	
        getFilterThree();
        $('#collapseThreeLoader').show();
        $.ajax({
            type : "POST",
            url : pgContext+"/tt/tt/getTeacherReports",
            data : {
                classId: classID,
                teacherId: teacherID,
                reportType: 'perStudentReport',
                lang: loc,
                filter: filterThree
            },
            success : function(data) {
                $('#collapseThreeLoader').hide();
                var jsonData = $.parseJSON(data);
                effortMap = jsonData.effortChartValues;
//                var junk = JSON.stringify(effortmap);
//                alert("junk=" + junk);
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

    $('#showReportFourBtn').on('click', function ()  {    	

   
        getFilterFour();
        $('#collapseFourLoader').show();

        $.ajax({
            type : "POST",
            url : pgContext+"/tt/tt/getTeacherReports",
            data : {
                classId: classID,
                teacherId: teacherID,
                reportType: 'commonCoreClusterReport',
                lang: loc,
                filter: filterFour
            },
            success : function(data) {
                $('#collapseFourLoader').hide();
                $("#perClusterReport").show();
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

                perClusterReportTable = $('#perClusterReport').DataTable({
                    data: dataPerCluster,
                    <%=jc_rb.getString("language_text")%>
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
                            placement: 'top',
                            container: 'body',
                        });
                    }
                });                	
                
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

                surveyReportTable = $('#surveyReport').DataTable({
                    data: surveyList,
                    <%=jc_rb.getString("language_text")%>
                	"columns": [ { title: "<%= rb.getString("survey_name")%>" , width: "30%"}],
                    "columnDefs": columNvalues,
                    "bFilter": false,
                    "bPaginate": false,
                    "bLengthChange": false,
                    rowReorder: false,
                    "bSort": true,
                    
                });
                	
                
                
            },
            error : function(e) {
            	alert("error occured");
                console.log(e);
            }
        });

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
            surveystudentData = surveyStudents;
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
                    clusterId: clusterId,
                    filter: filterFour

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
                                var attri = ", '<%= rb.getString("problem_preview")%>'"+","+"'width=750,height=550,status=yes,resizable=yes'";
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
                                    return '<img style="max-width:400px; max-height:400px;" src="' + $(this).data('img') + '" />';
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

	
	
	






	 
    <script type="text/javascript" src="<c:url value="/js/bootstrap/js/language_es.js" />"></script>    
    <script type="text/javascript">
        var servletContextPath = "${pageContext.request.contextPath}";
        var pgContext = '${pageContext.request.contextPath}';
        var classID = '${classInfo.classid}';
        var teacherID = '${teacherId}';
        var teacherLoginType = '${teacherLoginType}';
        var prePostIds = '${prepostIds}'.split("~~");		
        var problem_imageURL = '${webContentpath}'+'problemSnapshots/prob_';
        
        $(document).ready(function () {
            registerAllEvents();
            handleclickHandlers();
            $("#content-conatiner").children().hide();
            $("#splash_page").show();

			if (teacherLoginType === "Normal") {
				$("#report_five_panel").hide();
			}
			else {
				$("#report_five_panel").show();
			}
        
            getFilterOne();
            getFilterFour();

            getStudentListThree();           
          	getFilterThree();
            
            getStudentListSix();           
          	getFilterSix();
            
            $('#reorg_prob_sets_handler').css('background-color', '');
            $('#reorg_prob_sets_handler').css('color', '#dddddd');

            $("#content-conatiner").children().hide();
            $("#report-wrapper").show();
            $("#report-wrapper2").show();
            $("#perStudentPerProblemSetReport").hide();

                     
            $('#activeSurveyList').DataTable({
                "bPaginate": false,
                "bFilter": false,
                "bLengthChange": false,
                rowReorder: true,
                columnDefs: [{
                    "targets": [2],
                    "orderable": false,
                    "width": "20%",
                    'className': 'dt-body-center',
                    'render': function (data, type, full, meta) {
                        if(full[0] == prePostIds[0])
                            return '<input type="radio" checked="checked" name="pre_id" value="' + data + '">';
                        return '<input type="radio" name="pre_id" value="' + data + '">';
                    }
                },
                    {
                        "targets": [3],
                        "orderable": false,
                        "width": "20%",
                        'className': 'dt-body-center',
                        'render': function (data, type, full, meta) {
                            if(full[0] == prePostIds[1])
                                return '<input type="radio" checked="checked" name="post_id" value="' + data + '">';
                            return '<input type="radio" name="post_id" value="' + data + '">';
                        }
                    }
                ],
                select: {
                    style: 'os',
                    selector: 'td:first-child'
                },
            });
        });

    </script>
    <script type="text/template"  id="child_table_perCluster">
        <table class="table table-striped table-bordered hover">
            <thead>
            <tr>
                <th><%= rb.getString("perClustercol0") %></th>
                <th><%= rb.getString("perClustercol1") %></th>
                <th><%= rb.getString("perClustercol2") %></th>
                <th><%= rb.getString("perClustercol3") %></th>
                <th><%= rb.getString("perClustercol4") %></th>
                <th><%= rb.getString("perClustercol5") %></th>
                <th><%= rb.getString("perClustercol6") %></th>
                <th><%= rb.getString("perClustercol7") %></th>
                <th><%= rb.getString("perClustercol8") %></th>
                <th><%= rb.getString("perClustercol9") %></th>
                <th><%= rb.getString("perClustercol10") %></th>
                <th><%= rb.getString("perClustercol11") %></th>
            </tr>
            </thead>
        </table>
    </script>
    
    
        
</head>

<body>
<div id="wrapper">
    <!-- Sidebar -->
    <nav class="navbar navbar-inverse navbar-fixed-top" id="topbar-wrapper" role="navigation">
        <ul class="nav sidebar-nav">
            <li class="sidebar-brand">
                <a href="#">
                    <i class="fa fa-tachometer" aria-hidden="true"></i> <%= rb.getString("teacher_tools") %>
                </a>
            </li>
        </ul>
        <ul class="nav navbar-right top-nav buttonCustomColor">
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i
                        class="fa fa-user"></i> ${fn:toUpperCase(teacherName)} <b class="caret"></b></a>
                <ul class="dropdown-menu">
                    <li>
                        <a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/logout"><i
                                class="fa fa-fw fa-power-off"></i><%= rb.getString("log_out") %></a>
                    </li>
                </ul>
            </li>
        </ul>
    </nav>
    <nav class="navbar navbar-inverse navbar-fixed-top" id="sidebar-wrapper" role="navigation">
        <ul class="nav sidebar-nav">
            <li><a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/ttMain"><i class="fa fa-fw fa-home"></i> <%= rb.getString("home") %></a></li>

            <li><a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/viewClassDetails?classId=${classInfo.classid}&currentSelection=classHomePage" id="class_home"><i class="fa fa-home"></i> <%= rb.getString("class_home") %></a></li>

            <li><a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/viewClassDetails?classId=${classInfo.classid}&currentSelection=reorg_prob_sets_handler" id="reorg_prob_sets_handler"><i class="fa fa-list"></i> <%= rb.getString("manage_problem_sets") %></a></li>

            <li><a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/viewClassDetails?classId=${classInfo.classid}&currentSelection=manage_roster_handler" id="manage_roster_handler"><i class="fa fa-fw fa-users"></i> <%= rb.getString("manage_class_roster") %></a></li>

            <li><a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/viewClassDetails?classId=${classInfo.classid}&currentSelection=manage_student_info_handler" id="manage_student_info_handler"><i class="fa fa-fw fa-users"></i> <%= rb.getString("manage_student_info") %></a></li>

            <li><a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/viewClassDetails?classId=${classInfo.classid}&currentSelection=manage_class_handler" id="manage_class_handler"><i class="fa fa-fw fa-cog"></i> <%= rb.getString("manage_class") %></a></li>

            <!-- <li><a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/viewClassDetails?classId=${classInfo.classid}&currentSelection=resetSurveySettings_handler" id="resetSurveySettings_handler"><i class="fa fa-fw fa-cog"></i><%= rb.getString("survey_settings") %></a></li> -->
            
            <li><a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/viewClassDetails?classId=${classInfo.classid}&currentSelection=content_apply_handler" id="content_apply_handler"><i class="fa fa-fw fa-cogs"></i><%= rb.getString("apply_class_content") %></a></li>
        </ul>
        <!-- /#sidebar-end -->
    </nav>
    <div id="page-content-wrapper">

        <h1 class="page-header">
            <%= rb.getString("report_card_for_class") %>: <strong>${classInfo.name}</strong>&nbsp; [<%= rb.getString("class_code") %>:${classInfo.classid}]
        </h1>


        <div id="content-conatiner" class="container-fluid">

				<div id="loading_spinner" style="display: none">
					<i class="fa fa-refresh fa-spin"
						style="font-size: 36px; color: red"></i> <i
						class="fa fa-refresh fa-spin" style="font-size: 36px; color: blue"></i>
					<i class="fa fa-refresh fa-spin"
						style="font-size: 36px; color: green"></i> <i
						class="fa fa-refresh fa-spin"
						style="font-size: 36px; color: orange"></i> <i
						class="fa fa-refresh fa-spin"
						style="font-size: 36px; color: black"></i>
				</div>


            <div id="report-wrapper" class="row" style="display:none;width: 100%;">

                <div class="panel-group" id="accordion">
                
                

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_six" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseSix">
                                    <%= rb.getString("perStudentPerProblemReport") %>
                                </a>
                               	<button id="sixButton" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>

                        <div id="collapseSix" class="panel-collapse collapse">                
                            <div class="panel-body report_filters">                           
								  <label class="report_filters"><%= rb.getString("standards_e_g") %></label>
								  <input id="standardsFilter" style="width:48px" type="text" name="" value="" onblur="getFilterSix();">
							</div>
	                        <div class="panel-body report_filters">
	                        	<div id="chooseDateRange" class="row">
	                        		<div class="col-md-2 offset-md-1">                       
					                	<button type="button" class="btn btn-primary" onclick="initCalendar_r6_cal1();initCalendar_r6_cal2();$('#calendarModalPopupSix').modal('show');" ><%= rb.getString("choose_date_range") %></button>
					                </div>
	                        		<div class="col-md-3">                       
									    <input id="daysFilterSix" style="width:220px" type="text" name="" value="" >   
					                </div>
	 							</div>  
	
							</div>
	                        <div class="panel-body report_filters">
	                        	<div id="chooseStudents" class="row">
	                        		<div class="col-md-2 offset-md-1">                       
					                	<button type="button" class="btn btn-primary" onclick="populateStudentSelectionListSix();" ><%= rb.getString("choose_student") %></button>
					                </div>
	                        		<div id="studentSelectionListSix" name="studentSelectionListSix" class="col-md-5">                       
					                </div>
	 							</div>  
	
							</div>
                            <div class="panel-body report_filters">
      							<input class="report_filters largerCheckbox" type="checkbox" id="showNamesSix" name="" value="Y"  onblur="getFilterSix();"checked>&nbsp;&nbsp;<%= rb.getString("show_names") %>
                            </div>
                            <div class="panel-body report_filters">                           
								  <input id="showReportSixBtn" class="btn btn-lg btn-primary" type="submit" value="<%= rb.getString("show_report") %>">
								  <a id="downloadReportSixBtn" class="btn btn-lg btn-primary" role="button"><%= rb.getString("download_this_report") %></a>
								  <a id="showLegendBtn" class="btn btn-lg btn-primary" role="button" value="show" onclick="showLegend();"><%= rb.getString("show_legend") %></a>
								  <a id="hideLegendBtn" class="btn btn-lg btn-primary" style="display: none" role="button" value="show" onclick="hideLegend();"><%= rb.getString("hide_legend") %></a>
                            </div>
                            <div class="panel-body">
                                <div class="loader" style="display: none"></div>
                                <table id="perStudentPerProblemLegend" class="table table-striped table-bordered hover" width="40%" style="display: none">
                                    <thead>
                                    <tr>
                                        <th><%= rb.getString("student_effort")%>:</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr><td class="span-SKIP"><%= rb.getString("skip") %></td></tr>
                                    <tr><td class="span-NOTR"><%= rb.getString("notr") %></td></tr>
                                    <tr><td class="span-GIVEUP"><%= rb.getString("giveup") %></td></tr>
                                    <tr><td class="span-SOF"><%= rb.getString("sof") %></td></tr>
                                    <tr><td class="span-ATT"><%= rb.getString("att") %></td></tr>
                                    <tr><td class="span-GUESS"><%= rb.getString("guess") %></td></tr>
                                    <tr><td class="span-SHINT"><%= rb.getString("shint") %></td></tr>
                                    <tr><td class="span-SHELP"><%= rb.getString("shelp") %></td></tr>
                                    <tr><td class="span-NODATA"><%= rb.getString("no_data") %></td></tr>
                                    </tbody>
                                </table>
                            </div>

                            <div class="panel-body">
                                <table id="perStudentPerProblemReport" class="table table-striped table-bordered hover display nowrap" width="100%"></table>
                            </div>

                        </div>
                    </div>                
                
                
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_three" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseThree">
                                    <%= rb.getString("perStudentReport") %>
                                </a>
                                <button id="threeButton" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>

                        <div id="collapseThree" class="panel-collapse collapse">
                           <div class="panel-body report_filters hidden">                           
								  <label class="report_filters"><%= rb.getString("standards_e_g") %></label>
								  <input id="standardsFilter" style="width:48px" type="text" name="" value="" onblur="getFilterThree();">
							</div>
	                        <div class="panel-body report_filters">
	                        	<div id="chooseDateRange" class="row">
	                        		<div class="col-md-2 offset-md-1">                       
					                	<button type="button" class="btn btn-primary" onclick="initCalendar_r3_cal1();initCalendar_r3_cal2();$('#calendarModalPopupThree').modal('show');" ><%= rb.getString("choose_date_range") %></button>
					                </div>
	                        		<div class="col-md-3">                       
									    <input id="daysFilterThree" style="width:220px" type="text" name="" value="" >   
					                </div>
	 							</div>  
	
							</div>
	                        <div class="panel-body report_filters">
	                        	<div id="chooseStudents" class="row">
	                        		<div class="col-md-2 offset-md-1">                       
					                	<button type="button" class="btn btn-primary" onclick="populateStudentSelectionListThree();" ><%= rb.getString("choose_student") %></button>
					                </div>
	                        		<div id="studentSelectionListThree" name="studentSelectionListThree" class="col-md-5">                       
					                </div>
	 							</div>  
	
							</div>
                            <div class="panel-body report_filters hidden">
      							<input class="report_filters largerCheckbox" type="checkbox" id="showNamesThree" name="" value="Y"  onblur="getFilterThree();"checked>&nbsp;&nbsp;<%= rb.getString("show_names") %>
                            </div>
                            <div class="panel-body report_filters">                           
								  <input id="showReportThreeBtn" class="btn btn-lg btn-primary" type="submit" value="<%= rb.getString("show_report") %>">
								  <a id="downloadReportThreeStudentBtn" class="btn btn-lg btn-primary" role="button"><%= rb.getString("download_student_data") %></a>
								  <a id="downloadReportThreeEmotionBtn" class="btn btn-lg btn-primary" role="button"><%= rb.getString("download_emotion_data") %></a>
                            </div>
                            <div id="collapseThreeLoader" class="loader" style="display: none" ></div>

                            <div class="panel-body">
                                <table id="perStudentReport" class="table table-striped table-bordered hover" width="100%"></table>
                            </div>

                        </div>
                    </div>

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_one" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                    <%= rb.getString("perStudentPerProblemSetReport") %>
                                </a>
                                <button id="oneButton" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>

                        <div id="collapseOne" class="panel-collapse collapse">
                            <label><h3><%= rb.getString("table_shows_set-wise_performance_of_students_class") %></h3></label>
                            <div class="panel-body report_filters">                           
	                        	<div id="chooseDateRange" class="row">
	                        		<div class="col-md-2 offset-md-1">                       
					                	<button type="button" class="btn btn-primary" onclick="initCalendar_r1_cal1();initCalendar_r1_cal2();$('#calendarModalPopupOne').modal('show');" ><%= rb.getString("choose_date_range") %></button>
					                </div>
	                        		<div class="col-md-3">                       
									    <input id="daysFilterOne" style="width:220px" type="text" name="" value="" >   
					                </div>
	 							</div>
	 						</div>  
                            <div class="panel-body report_filters">                           
								  <input id="showReportOneBtn" class="btn btn-lg btn-primary" type="submit" value="<%= rb.getString("show_report") %>">
								  <a id="downloadReportOneBtn" class="btn btn-lg btn-primary" role="button"><%= rb.getString("download_this_report") %></a>
                            </div>
                            <div class="panel-body">
                                <table id="perTopicReportLegendTable" class="table table-striped table-bordered hover" width="70%">
                                    <thead>
                                    <tr>
                                        <th><%= rb.getString("mastery_range") %></th>
                                        <th><%= rb.getString("grade_color_for_2_or_more_problems") %></th>
                                        <th><%= rb.getString("grade_color_for_10_or_more_problems") %></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td><%= rb.getString("grade_a_range") %></td>
                                        <td class="span-sucess-layer-two"><%= rb.getString("grade_a_description") %></td>
                                        <td class="span-sucess-layer-one"><%= rb.getString("grade_a_description") %></td>
                                    </tr>
                                    <tr>
                                        <td><%= rb.getString("grade_b_range") %></td>
                                        <td class="span-info-layer-two"><%= rb.getString("grade_b_description") %></td>
                                        <td class="span-info-layer-one"><%= rb.getString("grade_b_description") %>)</td>
                                    </tr>
                                    <tr>
                                        <td><%= rb.getString("grade_c_range") %></td>
                                        <td class="span-warning-layer-two"><%= rb.getString("grade_c_description") %></td>
                                        <td class="span-warning-layer-one"><%= rb.getString("grade_c_description") %></td>
                                    </tr>
                                    <tr>
                                        <td><%= rb.getString("grade_d_range") %></td>
                                        <td class="span-danger-layer-two"><%= rb.getString("grade_d_description") %></td>
                                        <td class="span-danger-layer-one"><%= rb.getString("grade_d_description") %></td>
                                    </tr>
                                    </tbody>
                                </table>
                                <div class="loader" style="display: none"></div>
                            </div>

                            <div class="panel-body">
                                <ul>
                                    <li><%= rb.getString("cell_info1") %> <a title="<%= rb.getString("what_is_mastery")%>" style="cursor:pointer" rel="initialPopover"> <i class="fa fa-question-circle-o" aria-hidden="true"></i></a> value for that problem set.</li>
                                    <li><%= rb.getString("cell_info2") %></li>
                                    <li><%= rb.getString("cell_info3") %></li>
                                </ul>
                            </div>

                            <div class="panel-body">
                                <table id="perStudentPerProblemSetReport" class="table table-striped table-bordered hover display nowrap" width="100%"></table>
                            </div>

                        </div>
                    </div>

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_four" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseFour">
                                    <%= rb.getString("commonCoreClusterReport") %>
                                </a>
                                <button id="fourButton" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="collapseFour" class="panel-collapse collapse">
	                            <div class="panel-body report_filters hidden">                           
									  <label class="report_filters"><%= rb.getString("standards_e_g") %></label>
									  <input id="standardsFilterFour" style="width:48px" type="text" name="" value="" onblur="getFilterFour();">
								</div>
	                            <div class="panel-body report_filters">                           
		                        	<div id="chooseDateRange" class="row">
		                        		<div class="col-md-2 offset-md-1">                       
						                	<button type="button" class="btn btn-primary" onclick="initCalendar_r4_cal1();initCalendar_r4_cal2();$('#calendarModalPopupFour').modal('show');" ><%= rb.getString("choose_date_range") %></button>
						                </div>
		                        		<div class="col-md-3">                       
										    <input id="daysFilterFour" style="width:220px" type="text" name="" value="" >   
						                </div>
		 							</div>								
	 							</div>
	                            <div class="panel-body report_filters">                           
									  <input id="showReportFourBtn" class="btn btn-lg btn-primary" type="submit" value="<%= rb.getString("show_report") %>">
									  <a id="downloadReportFourClusterBtn" class="btn btn-lg btn-primary" role="button"><%= rb.getString("download_common_core_evaluation") %></a>
									  <a id="downloadReportFourProblemBtn" class="btn btn-lg btn-primary" role="button"><%= rb.getString("download_problem_wise_performance_data") %></a>
	                            </div>                            
	                            <div class="panel-body">
                            </div>
                            <div class="panel-body">
                                <table id="perClusterLegendTable" class="table table-striped table-bordered hover" width="60%">
                                    <thead>
                                    <tr>
                                        <th><%= rb.getString("percent_range") %></th>
                                        <th><%= rb.getString("meaning") %></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td><%= rb.getString("for_more_than_5_problems_less_than_20_percent") %></td>
                                        <td class="span-danger-layer-one"><%= rb.getString("clusters_found_hard") %></td>
                                    </tr>
                                    <tr>
                                        <td><%= rb.getString("for_more_than_5_problems_from_20_to_40_percent") %></td>
                                        <td class="span-warning-layer-one"><%= rb.getString("clusters_found_challenging") %></td>
                                    </tr>
                                    </tbody>
                                </table>
                                <div id="collapseFourLoader" class="loader" style="display: none" ></div>
                            </div>
                            <div class="panel-body">
                                <table id="perClusterReport" class="table table-striped table-bordered hover" width="100%"></table>
                            </div>
                        </div>
                    </div>

                   <%-- <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_two" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">
                                    Class Summary Per Problem
                                </a>
                            </h4>
                        </div>
                        <div id="collapseTwo" class="panel-collapse collapse">
                            <div class="panel-body">
                                <label>Problem wise performance of students in this class</label>
                                <a  href="${pageContext.request.contextPath}/tt/tt/downLoadPerProblemReport?teacherId=${teacherId}&classId=${classInfo.classid}" data-toggle="tooltip" title="Download this report" class="downloadPerStudentReport" aria-expanded="true" aria-controls="collapseOne">
                                    <i class="fa fa-download fa-2x" aria-hidden="true"></i>
                                </a>
                            </div>
                            <div class="panel-body">
                                <table id="perProblemReportLegendTable" class="table table-striped table-bordered hover" width="40%">
                                    <thead>
                                    <tr>
                                        <th>% Range</th>
                                        <th>Symbol</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>% greater than or equal to 80</td>
                                        <td><i class='fa fa-thumbs-up' aria-hidden='true'></i></td>
                                    </tr>
                                    <tr>
                                        <td>% less than 20</td>
                                        <td class="span-danger-layer-one">Unsatisfactory</td>
                                    </tr>
                                    </tbody>
                                </table>
                                <div class="loader" style="display: none"></div>
                            </div>
                            <div class="panel-body">
                                <table id="perProblemReport" class="table table-striped table-bordered hover" width="100%"></table>
                            </div>
                        </div>
                    </div>--%>




					<div id="report_five_panel" class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_five" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseFive">
                                    <%= rb.getString("summarySurveyReport") %>
                                </a>
                                <button id="fiveButton" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>

                        <div id="collapseFive" class="panel-collapse collapse">
                            <div class="panel-body">
                                <label><%= rb.getString("summary_surveys_test_report_this_class") %></label>
                                <a  href="${pageContext.request.contextPath}/tt/tt/downLoadPerSummSurReport?teacherId=${teacherId}&classId=${classInfo.classid}" data-toggle="tooltip" title="<%= rb.getString("download_this_report") %>" class="downLoadPerSummSurReport" aria-expanded="true" aria-controls="collapseOne">
                                    <i class="fa fa-download fa-2x" aria-hidden="true"></i>
                                </a>
                            </div>
                            <div class="panel-body">
                             
                                <table id="surveyReport" class="table table-striped table-bordered hover" width="50%"></table>
                            
                           
                                <div class="loader" style="display: none"></div>
                            </div>

                            
 
                        </div>
                    </div>
                    
                    
                </div>

            </div>

             <div id="splash_page" style="display:none;width: 100%;">
             <div>
                    <h3 class="tt-page-header">
                    <%= rb.getString("select_activities_from_menu") %>
                    </h3>
                </div>
             </div>
            
        </div>
</div>
</div>

<div id = "statusMessage" class="spin-loader-message" align = "center" style="display: none;"></div>

<div id="calendarModalPopupSix" class="modal fade" data-backdrop="static" data-keyboard="false" role="dialog" style="display: none;">
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
                        <input type="hidden" id="selectDay_r6_cal1" name="selectDay_r6_cal1">
   				      <div><h3><%= rb.getString("least_recent") %>:</h3></div>
			          <div class="button-container-calendar">
			              <div class=col-md-2><button id="previous_r6_cal1" onclick="previous_r6_cal1()">&#8249;&#8249;</button></div>
       							  <div class=col-md-8 center-text><h3 id="monthAndYear_r6_cal1"></h3></div>
			              <div class=col-md-2><button id="next_r6_cal1" onclick="next_r6_cal1()">&#8250;&#8250;</button></div>							          
			          </div>
			          
			          <table class="table-calendar" id="calendar_r6_cal1" data-lang="en">
			              <thead id="thead-month_r6_cal1"></thead>
			              <tbody id="calendar-body_r6_cal1"></tbody>
			          </table>
			          
			          <div class="footer-container-calendar">
			              <label for="month_r6_cal1"><%= rb.getString("jump_to") %>: </label>
			              <select id="month_r6_cal1" onchange="jump_r6_cal1()">
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
			              <select id="year_r6_cal1" onchange="jump_r6_cal1()">
			                  <option value=2020>2020</option>
			                  <option value=2021>2021</option>
			                  <option value=2022>2022</option>			              
			              </select>       
			          </div>
			      </div>			      
			    </div> 
			    <div class="wrapper-calender col-sm-6">
			      <div class="container-calendar">
                        <input type="hidden" id="selectDay_r6_cal2" name="selectDay_r6_cal2">
				      <div><h3><%= rb.getString("most_recent") %>:</h3></div>
			          <div class="button-container-calendar">
			              <div class=col-md-2><button id="previous_r6_cal2" onclick="previous_r6_cal2()">&#8249;&#8249;</button></div>
       							  <div class=col-md-8 center-text><h3 id="monthAndYear_r6_cal2"></h3></div>
			              <div class=col-md-2><button id="next_r6_cal2" onclick="next_r6_cal2()">&#8250;&#8250;</button></div>							          
			          </div>
			          
			          <table class="table-calendar" id="calendar_r6_cal2" data-lang="en">
			              <thead id="thead-month_r6_cal2"></thead>
			              <tbody id="calendar-body_r6_cal2"></tbody>
			          </table>
			          
			          <div class="footer-container-calendar">
			              <label for="month_r6_cal2"><%= rb.getString("jump_to") %>: </label>
			              <select id="month_r6_cal2" onchange="jump_r6_cal2()">
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
			              <select id="year_r6_cal2" onchange="jump_r6_cal2()">
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
	                <button type="button" class="btn btn-success" onclick="getFilterSix();" ><%= rb.getString("submit") %></button>
	                <button type="button" class="btn btn-danger" data-dismiss="modal" onclick="$('#calendarModalPopupSix').modal('hide');" ><%= rb.getString("cancel") %></button>
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

<div id="calendarModalPopupFour" class="modal fade" data-backdrop="static" data-keyboard="false" role="dialog" style="display: none;">
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
                        <input type="hidden" id="selectDay_r4_cal1" name="selectDay_r4_cal1">
   				      <div><h3><%= rb.getString("least_recent") %>:</h3></div>
			          <div class="button-container-calendar">
			              <div class=col-md-2><button id="previous_r4_cal1" onclick="previous_r4_cal1()">&#8249;&#8249;</button></div>
       							  <div class=col-md-8 center-text><h3 id="monthAndYear_r4_cal1"></h3></div>
			              <div class=col-md-2><button id="next_r4_cal1" onclick="next_r4_cal1()">&#8250;&#8250;</button></div>							          
			          </div>
			          
			          <table class="table-calendar" id="calendar_r4_cal1" data-lang="en">
			              <thead id="thead-month_r4_cal1"></thead>
			              <tbody id="calendar-body_r4_cal1"></tbody>
			          </table>
			          
			          <div class="footer-container-calendar">
			              <label for="month_r4_cal1"><%= rb.getString("jump_to") %>: </label>
			              <select id="month_r4_cal1" onchange="jump_r4_cal1()">
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
			              <select id="year_r4_cal1" onchange="jump_r4_cal1()">
			                  <option value=2020>2020</option>
			                  <option value=2021>2021</option>
			                  <option value=2022>2022</option>			              
			              </select>       
			          </div>
			      </div>			      
			    </div> 
			    <div class="wrapper-calender col-sm-6">
			      <div class="container-calendar">
                        <input type="hidden" id="selectDay_r4_cal2" name="selectDay_r4_cal2">
				      <div><h3><%= rb.getString("most_recent") %>:</h3></div>
			          <div class="button-container-calendar">
			              <div class=col-md-2><button id="previous_r4_cal2" onclick="previous_r4_cal2()">&#8249;&#8249;</button></div>
       							  <div class=col-md-8 center-text><h3 id="monthAndYear_r4_cal2"></h3></div>
			              <div class=col-md-2><button id="next_r4_cal2" onclick="next_r4_cal2()">&#8250;&#8250;</button></div>							          
			          </div>
			          
			          <table class="table-calendar" id="calendar_r4_cal2" data-lang="en">
			              <thead id="thead-month_r4_cal2"></thead>
			              <tbody id="calendar-body_r4_cal2"></tbody>
			          </table>
			          
			          <div class="footer-container-calendar">
			              <label for="month_r4_cal2"><%= rb.getString("jump_to") %>: </label>
			              <select id="month_r4_cal2" onchange="jump_r4_cal2()">
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
			              <select id="year_r4_cal2" onchange="jump_r4_cal2()">
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
	                <button type="button" class="btn btn-success" onclick="getFilterFour();" ><%= rb.getString("submit") %></button>
	                <button type="button" class="btn btn-danger" data-dismiss="modal" onclick="$('#calendarModalPopupFour').modal('hide');" ><%= rb.getString("cancel") %></button>
                </div> 
         </div>
    	</div>
	</div>
</div>	


<div id="calendarModalPopupThree" class="modal fade" data-backdrop="static" data-keyboard="false" role="dialog" style="display: none;">
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
                        <input type="hidden" id="selectDay_r3_cal1" name="selectDay_r3_cal1">
   				      <div><h3><%= rb.getString("least_recent") %>:</h3></div>
			          <div class="button-container-calendar">
			              <div class=col-md-2><button id="previous_r3_cal1" onclick="previous_r3_cal1()">&#8249;&#8249;</button></div>
       							  <div class=col-md-8 center-text><h3 id="monthAndYear_r3_cal1"></h3></div>
			              <div class=col-md-2><button id="next_r3_cal1" onclick="next_r3_cal1()">&#8250;&#8250;</button></div>							          
			          </div>
			          
			          <table class="table-calendar" id="calendar_r3_cal1" data-lang="en">
			              <thead id="thead-month_r3_cal1"></thead>
			              <tbody id="calendar-body_r3_cal1"></tbody>
			          </table>
			          
			          <div class="footer-container-calendar">
			              <label for="month_r3_cal1"><%= rb.getString("jump_to") %>: </label>
			              <select id="month_r3_cal1" onchange="jump_r3_cal1()">
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
			              <select id="year_r3_cal1" onchange="jump_r3_cal1()">
			                  <option value=2020>2020</option>
			                  <option value=2021>2021</option>
			                  <option value=2022>2022</option>			              
			              </select>       
			          </div>
			      </div>			      
			    </div> 
			    <div class="wrapper-calender col-sm-6">
			      <div class="container-calendar">
                        <input type="hidden" id="selectDay_r3_cal2" name="selectDay_r3_cal2">
				      <div><h3><%= rb.getString("most_recent") %>:</h3></div>
			          <div class="button-container-calendar">
			              <div class=col-md-2><button id="previous_r3_cal2" onclick="previous_r3_cal2()">&#8249;&#8249;</button></div>
       							  <div class=col-md-8 center-text><h3 id="monthAndYear_r3_cal2"></h3></div>
			              <div class=col-md-2><button id="next_r3_cal2" onclick="next_r3_cal2()">&#8250;&#8250;</button></div>							          
			          </div>
			          
			          <table class="table-calendar" id="calendar_r3_cal2" data-lang="en">
			              <thead id="thead-month_r3_cal2"></thead>
			              <tbody id="calendar-body_r3_cal2"></tbody>
			          </table>
			          
			          <div class="footer-container-calendar">
			              <label for="month_r3_cal2"><%= rb.getString("jump_to") %>: </label>
			              <select id="month_r3_cal2" onchange="jump_r3_cal2()">
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
			              <select id="year_r3_cal2" onchange="jump_r3_cal2()">
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
	                <button type="button" class="btn btn-success" onclick="getFilterThree();" ><%= rb.getString("submit") %></button>
	                <button type="button" class="btn btn-danger" data-dismiss="modal" onclick="$('#calendarModalPopupThree').modal('hide');" ><%= rb.getString("cancel") %></button>
                </div> 
         </div>
    	</div>
	</div>
</div>	





<div id="studentsModalPopupSix" class="modal fade" data-backdrop="static" data-keyboard="false" role="dialog" style="display: none;">

    <div class="modal-dialog modal-lg">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body" role="dialog">
			     <div class="wrapper-students col-sm-6">
			      <div class="container-calendar">
			          
			          <div class="footer-container-calendar">
			              <label for="studentsSix">Select Students: </label>
						  <select name='students' id='studentsSix' size='5' multiple>;
			              </select>
     
			          </div>
			      </div>			      
			    </div> 
            </div>

           <div class="modal-footer">

          		<div class="offset-md-6">
	                <button type="button" class="btn btn-success" onclick="alert('Hello');" ><%= rb.getString("submit") %></button>
	                <button type="button" class="btn btn-danger" data-dismiss="modal" onclick="$('#studentsModalPopup').modal('hide');" ><%= rb.getString("cancel") %></button>
                </div> 
         </div>
    	</div>
	</div>
</div>	


<div id="studentsModalPopupThree" class="modal fade" data-backdrop="static" data-keyboard="false" role="dialog" style="display: none;">

    <div class="modal-dialog modal-lg">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body" role="dialog">
			     <div class="wrapper-students col-sm-6">
			      <div class="container-calendar">
			          
			          <div class="footer-container-calendar">
			              <label for="studentsThree">Select Students: </label>
						  <select name='students' id='studentsThree' size='5' multiple>;
			              </select>
     
			          </div>
			      </div>			      
			    </div> 
            </div>

           <div class="modal-footer">

          		<div class="offset-md-6">
	                <button type="button" class="btn btn-success" onclick="alert('Hello');" ><%= rb.getString("submit") %></button>
	                <button type="button" class="btn btn-danger" data-dismiss="modal" onclick="$('#studentsModalPopup').modal('hide');" ><%= rb.getString("cancel") %></button>
                </div> 
         </div>
    	</div>
	</div>
</div>	


<!-- Modal -->



<!-- Modal For Mastery Trajecotory Report-->
<div id="studentEffortRecordedProblem" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title"><%= rb.getString("collective_student_effort") %></h4>
            </div>
            <div class="modal-body" role="alert">
                <div id="problemSnapshot" ></div>
                <canvas id="studentEffortRecordedProblemCanvas" width='900' height='150'></canvas>
                <div id="lengendTable"></div>
            </div>

        </div>
    </div>
</div>
<!-- Modal -->


<!-- Modal Error-->
<div id="errorMsgModelPopup" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title"><%= rb.getString("something_went_wrong") %></h4>
            </div>
            <div class="modal-body alert alert-danger" role="alert">
                <%= rb.getString("some_text_in_modal") %>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><%= rb.getString("close") %></button>
            </div>
        </div>

    </div>
</div>
<!-- Modal -->

<!-- Modal Popup Problem -->
<div id="ModalPopupProblem" class="modal fade" role="dialog" style="display: none;">
    <div class="pspp-modal-dialog modal-md modal-dialog-centered">
        <!-- Modal content-->
        <div class="pspp-modal-content ">
            <div class="pspp-modal-header">
            	<span id="perStudentPerProblemImageHdr" class="modal-title"></span></div>
            <div>
            	<div id="perStudentPerProblemContent" ></div>
            </div>
        </div>

    </div>
</div>
<!-- Modal -->


<!-- Modal Success-->
<div id="successMsgModelPopup" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title"><%= rb.getString("success") %></h4>
            </div>
            <div class="modal-body alert alert-success" role="alert">
                <%= rb.getString("some_text_in_modal") %>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><%= rb.getString("close") %></button>
            </div>
        </div>

    </div>
</div>
<!-- Modal -->

<!-- Modal Success-->
<div id="successMsgModelPopupForProblemSets" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title"><%= rb.getString("success") %></h4>
            </div>
            <div class="modal-body alert alert-success" role="alert">
                <%= rb.getString("some_text_in_modal") %>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><%= rb.getString("close") %></button>
            </div>
        </div>

    </div>
</div>

<!-- Modal -->
<div id="completeMasteryForStudent" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title"><%= rb.getString("complete_mastery_chart") %></h4>
            </div>
            <div class="modal-body" role="alert">
                <canvas id="completeMasteryForStudentCanvas"></canvas>
            </div>

        </div>
    </div>
</div>

<!-- Modal For Mastery Trajecotory Report-->
<div id="masteryTrajectoryReport" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title"><%= rb.getString("master_trajectory_report") %></h4>
            </div>
            <div class="modal-body" role="alert">
                <canvas id="masteryTrajectoryReportCanvas"></canvas>
                <div>
                    <table id="masteryTrajecotoryLegend" class="table table-striped table-bordered" cellspacing="0"
                           width="50%"/>
                </div>
            </div>

        </div>
    </div>
</div>

</body>
<!-- Modal -->
    <script type="text/javascript" src="<c:url value="/js/calendar_r1_1.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/calendar_r1_2.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/calendar_r4_1.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/calendar_r4_2.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/calendar_r6_1.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/calendar_r6_2.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/calendar_r3_1.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/calendar_r3_2.js" />"></script>
</body>

</html>