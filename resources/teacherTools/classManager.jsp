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
<!-- Frank	10-12-20	Issue #149R2 add logging in JSO format -->
<!-- Frank	10-12-20	add page-loading indicators -->
<!-- Frank	10-30-20	Issue #293 add new items to class config form -->
<!-- Frank	10-30-20	Issue #293R2 fix validation on class config form -->
<!-- Frank	11-12-20	Issue #299 Landing Page report -->
<!-- Frank	11-12-20	Issue #276 Suppress logging if logged in as Master Teacher -->
<!-- Frank 12-11-20 	Issue #315 default locale to en_US -->
<!-- Kartik	10-30-20	Issue #290 added topic ID in Manage Topics info popup -->
<!-- Frank 12-18-20 Issue #336 added cache-busting for selected .js and .css files -->
<!-- Frank 12-26-20  	Issue #329 fix errors from spliting classDetails.jsp -->
<!-- Frank 01-05-21  	Issue #329R3 fix error - nickname null not handled -->
<!-- Frank 01-05-21  	Issue #302 teacher username only alpha and numeric characters -->
<!-- Frank 01-05-21  	Issue #366 blank screen after adding student -->
<!-- Frank 03-05-21  	Issue #388 Landing page report by date range -->
<!-- Frank 03-15-21  	Issue #398 New feature to move student from one class to another -->
<!-- Frank 05-01-21  	Hide survey selection -->
<!-- Frank 05-17-21  	Issue #471 Show survey selection if logged on as Master-->
<!-- Frank 05-20-21  	Issue #473 crop lname -->
<!-- Frank 07-11-21  	Issue #77 remove obsolete report header - common core report now in classReportCard.jsp-->
<!-- Frank 08-20-21  	Issue #496 live dashboard -->
<!-- Frank 08-20-21  	Swap positions of Landing Report 1 & 2 -->
<!-- Frank 08-20-21  	Move class code from page title to logout dropdown -->
<!-- Frank 10-09-21  	Issue #526 Improve form validation -->
<!-- Frank 10-09-21  	Issue # 523 Add user prefix lookup -->
<!-- Frank 11-30-21     Issue #551 - change how school year is displayed e.g. 2021/2022 for schoolYear=2022 -->  
<!-- Frank 02-11-22     Issue #599 - live dashboard added 2 charts -->
<!-- Frank 05-11-22     Issue #632 - Manage Topics - add ability to select problems by standard -->
<!-- Frank 06-24-22     Issue #632R2 - added reset and help to standards list popup -->
<!-- Frank 07-28-22	issue #676 removed grades 9, 10, adult from the picklist temporarily until we get some math problems for them -->
<!-- Frank 08-07-22	issue #682 add refresh button to livedashboard -->
<!-- Frank 09-23-22     Issue #632R3 - added select all/deselect all for standards list popup and made dropdown gable -->
<!-- Frank 10-06-22     Issue #632R4 - group feedback changes and fix drag element init -->
<!-- Frank 11-27-22     Issue #714 - finish multi-lingual algorithms -->
<!-- Frank 02-20-23     Issue #723 - Split off Problem set (topic) management code to classManageTopics.jsp -->


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
String country = loc.getCountry();

System.out.println("locale set to:" + lang + "-" + country );	

if (!lang.equals("es")) {
	loc = new Locale("en","US");	
}			


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

<style>
.nobull {
  list-style-type: none;
 }
 
.dropdown-content:focus {
	background-color: #ddd;
	outline: 3px solid black;
}

.dropdown-content:hover {
  background-color: lightgreen;
	outline: 3px solid black;
}

.dropdown {
  position: relative;
  display: inline-block;
}

.dropdown-content {
  background-color: #f6f6f6;
  font-size: 16px;
  max-width: 40%;
  overflow: auto;
  border: 1px solid #ddd;
  z-index: 1;
}

.dropdown-content li {
  color: black;
  text-decoration: none;
  display: block;
  list-style-type: none;
}


.standards_detail_table_bg {
  	background-color: #ddd78a;

}

.full_detail_table_bg {
  	background-color: #cbc34d;

}


.activeCenterDiv
{

  height:280px;
  width: 260px; 
  border-radius: 1em;
  border-width:thin;
  border-color:black;
  background-color: #33ffff;
  padding: 1em; 
  position: absolute;
  top: 30%;
  left: 65%;
  margin-right: -50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
}

.activeCenterDivHdr {
  border-radius: 1em;
  border-width:thin;
  border-color:black;
  padding: 10px;
  cursor: move;
  z-index: 10;
  color: white;
  background-color:black;
}


.passiveCenterDiv
{

  height:280px;
  width: 260px; 
  border-radius: 1em;
  border-width:thin;
  border-color:black;
  background-color: #00cccc;
  padding: 1em; 
  position: absolute;
  bottom: 20%;
  left: 70%;
  transform: translate(-50%, -50%);
  z-index: 1000;
}

.passiveCenterDivHdr {
  border-radius: 1em;
  border-width:thin;
  border-color:black;
  padding: 10px;
  cursor: move;
  z-index: 10;
  color: white;
  background-color:black;
}


.standardsHelpBox {
  border-radius: 1em;
  padding: 1em; 
  border: 1px solid black;
}
</style>

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
 * Frank	12-02-20	Issue #322 added currentSelection to classReportCard URL 
 */
 
 var isCluster = 0;
 var hasClusters = 0;
 var classColor = "";
 var titleClassName = "";
 var moveStudentId = "";     	       

 //Report1 Variables
var perProblemSetReport;
var perProblemSetLevelOne;
var perProblemSetLevelTwo;
var perProblemSetColumnNamesMap;
var perProblemSetLevelOneAvg;
var perProblemSetLevelOneMax;
var perProblemSetLevelOneLatest;

var perStudentperProblemReport;
var perStudentperProblemLevelOne;
var perStudentPerProblemColumnNamesMap;
var perStudentPerProblemXrefMap;

//Report5 Varribales
var landingPageReport1;
var landingPageReport2;

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
var filterLandingOne = "7";
var filterLandingTwo = "~0";

var lowGradeLevel;
var highGradeLevel;
var gradesLevelsUsedInThisClass = "";


var plot_live_dashboard = null;
var plot_effort_chart = null;
var effort_legend_labels = ["SOF",      "ATT",   "SHINT", "SHELP",     "GUESS",   "NOTR",  "SKIP", "GIVEUP",   "NODATA"];
var effort_series_colors = ['#26f213', '#9beb94','#80b1d3', '#fdb462', '#fb8072', '#ffffb3', '#8dd3c7', '#bebada',  '#d9d9d9'];
var live_series_colors = ['#0000c6'];

var languagePreference = window.navigator.language;
var localeSplitter = languagePreference.split("-");
var languageSet = localeSplitter[0];
var countrySet = localeSplitter[1];
var loc = languagePreference;    
function getFilterLandingOne() {
	

	var daysLandingOne = document.getElementById("daysFilterLandingOne").value;
	
	const nDays = parseInt(daysLandingOne);
	if (isNaN(nDays)) {
		daysLandingOne = "7";
		document.getElementById("daysFilterLandingOne").value  = "7";
	}
	else {
		daysLandingOne = "" + nDays;
	}
	filterLandingOne = daysLandingOne + '~' + classesInCluster;
}

function getFilterLandingTwo() {
		
	var d1 = parseInt(document.getElementById("selectDay_cal2").value);
	var d2 =  parseInt(document.getElementById("selectDay").value);

	var m1 = parseInt(document.getElementById("month_cal2").value) + 1;
	var m2 =  parseInt(document.getElementById("month").value) + 1;

	document.getElementById("year_cal2").value = "2023";
	document.getElementById("year").value = "2023";
	
	if ((d1 > 0) && (d2 > 0)) {
		$('#calendarModalPopup').modal('hide');

		var fromDate = m1 + "/" + document.getElementById("selectDay_cal2").value + "/" +  document.getElementById("year_cal2").value;
		var toDate = m2 + "/" + document.getElementById("selectDay").value + "/" + document.getElementById("year").value;

		if (!(countrySet == "US")) {
			fromDate = document.getElementById("selectDay_cal2").value + "/" +  m1 + "/" + document.getElementById("year_cal2").value;
			toDate = document.getElementById("selectDay").value + "/" + m2 + "/" + document.getElementById("year").value;
		}
		
		var older = Date.parse(fromDate);
		var newer = Date.parse(toDate);
		if (newer < older) {
			var temp = fromDate;
			fromDate = toDate;
			toDate = temp;
		}
		document.getElementById("daysFilterLanding2").value = fromDate + " thru " + toDate;
		filterLandingTwo = "~" + fromDate + "thru" + toDate + '~' + classesInCluster;
	    $('#landing-report-loader').show();
	    $.ajax({
            type : "POST",
            url : pgContext+"/tt/tt/getTeacherReports",
            data : {
                classId: classID,
                teacherId: teacherID,
                reportType: 'classLandingReportTwo',
                lang: loc,
                filter: filterLandingTwo
            },
            success : function(data) {
        	    $('#landing-report-loader').hide();
                $('#classLandingReportOne').collapse('hide');
                $('#classLandingReportTwo').collapse('show');
                var jsonData = $.parseJSON(data);
                landingPageReport2.clear().draw();
                landingPageReport2.rows.add(jsonData.levelOneData).draw();
                landingPageReport2.columns.adjust().draw();
            },
            error : function(e) {
                console.log(e);
            }
        });

	}
	else{
		alert("<%= rb.getString("must_select_a_day_from_each_calendar")%>");
		$('#calendarModalPopup').modal('show');
	}

}

function moveToDestination(id, isCluster) {
	
	var splitter = id.split(',');
	if(splitter[1] == "0") {
		moveThisStudent(splitter[0]);
	}
	else {
		moveThisStudent(splitter[0]);
	}
}

function moveThisStudent(id) {

	var newClassId = id;
    $('#student_info_out').find('.loader').show();
	$.ajax({
        type : "POST",
        url :pgContext+"/tt/tt/changeStudentClass",
        data : {
        	studentId: moveStudentId,
        	newClassId: newClassId,
            lang: loc
        },
        success : function(response) {
            $('#student_info_out').find('.loader').hide();
            if (response.includes("***")) {
                $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
                $('#errorMsgModelPopup').modal('show');
            }else{
                if (teacherLoginType === "Normal") {
                	logTeacherEvent("changeStudentClass",response);
                }
                $("#successMsgModelPopup").find("[class*='modal-body']").html("<%= rb.getString("student_class_changed")%>");
                $('#successMsgModelPopup').modal('show');
            }
            $("#clusterList").hide();
	    },
	    error : function(e) {
	        console.log(e);
	    }
    });
}

function logTeacherEvent(action,activityName) {

	$.ajax({
	    type : "POST",
	    url :pgContext+"/tt/tt/classLogTeacherEvent",
	    data : {
	        teacherId: teacherID,
	        classId: classID,
	        sessionId: sessionID,
	        action: action,
	        activityName: activityName
	    },
	    success : function(response) {
	        if (response.includes("success")) {
	        	console.log("Teacher Event logged");
	        }else{
	        	console.log("Teacher Event ERROR");
	        }
	    }
	});

}

/*
$('#edit_class_form').submit(function() {
	  var t1 = document.getElementById('maxProb').value;
	  var t2 = document.getElementById('minProb').value;

	  var t3 = 0 + document.getElementById('maxProb').value;
	  var t4 = 0 + document.getElementById('minProb').value;


	 
	  if (t4 >= t3) {
		  document.getElementById('maxProb').value = ${classInfo.maxProb};
		  document.getElementById('minProb').value = ${classInfo.minProb};
		  alert("Error");
		  alert(t2 + " cannot be >= " + t1);
		  return false;
	  }
	  else {
		  alert("OK");
		  return true;
	  }
});
*/

function userPrefixLookup() {
	
    var values=[];
    values[0] = document.getElementById("userPrefix").value;
    values[1] = document.getElementById("classId").value;;

    $('#student_roster_out').find('.loader').show();
    $.ajax({
        type: "POST",
        url: pgContext + "/tt/tt/isStudentPrefixInUse",
        data: {
            formData: values,
            lang: loc
        },
        success: function (data) {
            $('#student_roster_out').find('.loader').hide();
            if (data.includes("inuse")) {
            	document.getElementById('userPrefix').value = "";
                $("#errorMsgModelPopup").find("[class*='modal-body']").html( data );
                $('#errorMsgModelPopup').modal('show');
            }
        }
    });

}

function startClusterLoader() {
	
    $('#createClusterLoader').show();

}


function cloneCurrentClass() {
	
//	CreateClassForm createForm = null;
	
	$('#clone_class_out').find('.loader').show();
	$.ajax({
	    type: "POST",
	    url: pgContext + "/tt/ttCloneClass",
	    data: {
	        classId: classId
	    },
	    success: function (data) {
	        $('#clone_class_out').find('.loader').hide();

            if (response.includes("***")) {
                $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
                $('#errorMsgModelPopup').modal('show');
            }else{
                if (teacherLoginType === "Normal") {
                	logTeacherEvent("createdClassCluster","");
                }
                $("#successMsgModelPopup").find("[class*='modal-body']").html("Created Class Cluster");
                $('#successMsgModelPopup').modal('show');
            }
	    
	    }
	    ,
        error : function(e) {
        	alert("error");
            console.log(e);
        }
	});
}

function verifyProbMinMax() {


	var t1 = 0;
	if (isNaN(document.getElementById('maxProb').value)) 
		t1 = 0;			
	else 
		t1 = parseInt(document.getElementById('maxProb').value);		
		
	var t2 = 0;
	if (isNaN(document.getElementById('minProb').value)) 
		t2 = 0;			
	else
		t2 = parseInt(document.getElementById('minProb').value);		
	   
	if (t2 >= t1) {
		alert("<%= rb.getString("max_problems_per_topic") %> must be > <%= rb.getString("min_problems_per_topic") %>");
		document.getElementById('maxProb').value = ${classInfo.maxProb};
    	document.getElementById('minProb').value = ${classInfo.minProb};
		document.getElementById('maxProb').focus();
	}
}

function verifyTimeMinMax() {

	var t1 = 0;
	if (isNaN(document.getElementById('maxTime').value)) 
		t1 = 0;			
	else 
		t1 = parseInt(document.getElementById('maxTime').value);		
		
	var t2 = 0;
	if (isNaN(document.getElementById('minTime').value)) 
		t2 = 0;			
	else
		t2 = parseInt(document.getElementById('minTime').value);		

	 
	  if (t2 >= t1) {
		  alert("<%= rb.getString("max_time_in_topic") %> must be > <%= rb.getString("min_time_in_topic") %>");
		  document.getElementById('maxTime').value = ${classInfo.maxTime};
	      document.getElementById('minTime').value = ${classInfo.minTime};
		  document.getElementById('maxTime').focus();
	  }
	
}


function showReport3d() {
	
	if (plot_live_dashboard != null) {
		plot_live_dashboard.destroy();
		plot_live_dashboard = null;
	}
	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: 0,
            reportType: 'getClassProblemsEffortRpt',
            lang: loc,
            filter: classID
        },
        success : function(data) {
        	if (data) {
        		
               	var resultData = $.parseJSON(data);
            	var jsonData = resultData[0];
            	
            	for (var i=0;i<jsonData.length;i = i + 1) {
		  			var effortValue = new Array(9);
		  			effortValue[0] = parseInt(jsonData[i].SOF);
		  			effortValue[1] = parseInt(jsonData[i].ATT);
		  			effortValue[2] = parseInt(jsonData[i].SHINT);
		  			effortValue[3] = parseInt(jsonData[i].SHELP);
		  			effortValue[4] = parseInt(jsonData[i].GUESS);
		  			effortValue[5] = parseInt(jsonData[i].NOTR);
		  			effortValue[6] = parseInt(jsonData[i].SKIP);
		  			effortValue[7] = parseInt(jsonData[i].GIVEUP);
		  			effortValue[8] = parseInt(jsonData[i].NODATA);
		  			
            		var line0 = [];
		  			var lines = [line0];
		  			for (j=0;j<9;j = j + 1) {
		  				var eff = [];
			  			eff.push(effort_legend_labels[j],effortValue[j]);
			  			lines[i].push(eff);
		  				
		  			}
					var canvasName = 'chart3d_canvas';
					var tline = lines[i];
					plot_live_dashboard = $.jqplot(canvasName, [tline], {
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
        		alert('No data available');
        	}
        },
        error : function(e) {
        	alert("error");
            console.log(e);
        }
    });
	
}




var liveDashboardTotal = 0;

function liveDashboardPopulate() {
	
	var liveDashboardFilter = "ProblemsSolved" + "~" + classesInCluster;
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getTeacherReports",
        data : {
        	classId: classID,
            teacherId: teacherID,
            reportType: 'classLiveDashboard',
            lang: loc,
            filter: liveDashboardFilter

        },
        success : function(data) {
       	    if (data) {
       			$("#ModalPopupFireworks").modal('hide');
       	    	liveDashboardTotal = "" + data;
       	    	document.getElementById('live-dashboard-content').innerHTML = '<h1 class="tt-live-dashboard-content">' + liveDashboardTotal + '</h1>';
       	    	document.getElementById("live-dashboard").style.visibility = 'visible';

       	    	if (plot_effort_chart != null) {
       	    		plot_effort_chart.destroy();
       	    		plot_effort_chart = null;
       	    	}       	                	
               	var s1 = [];
               	var ticks = [];
               	
               	var intLiveDashboardTotal = parseInt(data);

               	var liveMaxStr = document.getElementById("tt-live-goal").value;
               	if ((liveMaxStr == "") || isNaN(liveMaxStr)) {
               		document.getElementById("tt-live-goal").value = ' ';
               		liveMaxStr = '0';
               	}
               	else {
               		$("#tt-live-goal-msg").hide();
               	}
               	liveMax = parseInt(liveMaxStr);
               	if (liveMax <= 100) {
                   	theInterval = 5;               		
               	}
               	else {
               		if (liveMax <= 500) {
	                   	theInterval = 25;               		
	               	}
	               	else {
	               		if (liveMax <= 1000) {
	               			theInterval = 50;
	               		}
	               		else {
		               		if (liveMax <= 2000) {
		               			theInterval = 100;
		               		}
		               		else {
			               		if (liveMax <= 4000) {
			               			theInterval = 200;
			               		}
			               		else {
			               			theInterval = 400;               			
			               		}
		               		}
	               		}
	               	}
               	}
               	ticks.push(" ");
              	s1.push(liveDashboardTotal);
   				
   				plot_effort_chart = $.jqplot('live_dashboard_canvas', [s1], {
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
   			            	label: '<%= rb.getString("total_problems_solved")%>'
   				        },
   				        yaxis: {
   				            pad: 1.05,
   				            max: liveMax,
   			            	min: 0,  
   			            	tickInterval: theInterval, 
   			            	tickOptions: { 
   			                	formatString: '%d'
   			            	} 
   			
   				        },
   					    seriesColors: live_series_colors
   				    }
   				});
   				if (liveMax > 0) {
	                if (intLiveDashboardTotal >= liveMax) {
	            		$("#ModalPopupFireworks").modal('show');
	            		document.getElementById("tt-live-goal").value = " ";
	            		liveMax = 0;
	            	}
	                else {
	            		$("#ModalPopupFireworks").modal('hide');
	                }
   				}
       	    	liveDashboardLoop();
       	    }
           	else {
           		console.log("response data is null");
           	}
        },
        error : function(e) {
            console.log(e);
        }
    });

}

function liveDashboardLoop() {

	setTimeout(function() {
		liveDashboardPopulate();
	}, 30000);	
	 
}

function liveDashboardStart() {

	liveDashboardPopulate();

	$("#tt-live-goal-msg").hide();
	$("#live-dashboard").show();


}


var gardenShownames = "";

function liveGardenPopulate(showNames) {
		
	if (gardenShownames === "") {
		gardenShownames = "N";		
	}
	else {
		if (gardenShownames === "N") {
			gardenShownames = "Y";	
		}
		else {
			gardenShownames = "N";
		}
	}

	var filter = gardenShownames;
    $('#live-garden-loader').show();
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getTeacherReports",
        data : {
        	classId: classID,
            teacherId: teacherID,
            reportType: 'classLiveGarden',
            lang: loc,
            filter: filter

        },
        success : function(data) {
       	    if (data) {
				//alert(data);
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
                 
        	    var lgt_head = document.getElementById("live-garden-thead");
        	    lgt_head.innerHTML = "";
        	    var headerWidth = cols.length * 75;
        	    lgt_head.style.width = "" + headerWidth + "px";
                // Create table row tr element of a table
                var tr = lgt_head.insertRow(-1);
                 
                for (var i = 0; i < cols.length; i++) {

                	if (i > 0) {
	                    // Create the table header th element
	                    var theader = document.createElement("th");
	                    
	                    theader.className += 'tt-plant-header';
	                    // Keep cell width to a minimum by forcing wrap
	                    var tcol = cols[i];
	                    theader.title = tcol;
	                    tcol = tcol.replace("/"," /");
	                    tcol = tcol.replace("&"," &");
	                    tcolSplitter = tcol.split(" ");
	                    theader.innerHTML = "";
	                    for (var tc = 0; tc < tcolSplitter.length; tc++ ) {
	                    	if (tcolSplitter[tc].length > 9)
	                    		tcolSplitter[tc] = tcolSplitter[tc].substr(0,9);
	               			theader.innerHTML += tcolSplitter[tc] + " ";               			
	               			if (tc > 3)
	               				break;
                		}

	                    // Append columnName to the table row
    	                tr.appendChild(theader);
                	}
                	else {
	                    // Create the table header th element
	                    var theader = document.createElement("th");
	                    
//                       	if (showNames === "Y") {
    	                    theader.className += 'tt-plant-header0';
    	                    // Keep cell width to a minimum by forcing wrap
		                    var tcol = cols[i];
		            		if (gardenShownames === "N") {
	    	           			theader.innerHTML = '<%= rb.getString("show_student_names")%>';
		            		}
		            		else {
	    	           			theader.innerHTML = '<%= rb.getString("hide_student_names")%>';		            			
		            		}
	    	           		theader.style.cursor = 'pointer';
		                    // Append columnName to the table row
                            theader.onclick = function(){
                            	liveGardenPopulate("");
		                    }

	    	                tr.appendChild(theader);
//                       	}
                	}
                }
                // End of header
                
        	    var lgt_body = document.getElementById("live-garden-tbody");
        	    lgt_body.innerHTML = "";
        	    var bodyWidth = cols.length * 75;
        	    lgt_body.style.width = "" + bodyWidth + "px";

                // Adding the data to the table
                for (var i = jsonData.length-1; i >= 1 ; i--) {
                     
                    // Create a new row
                    trow = lgt_body.insertRow(-1);
                    for (var j = 0; j < cols.length; j++) {
                        if (j > 0)  {
                            var cell = trow.insertCell(-1);
                       		cell.className += 'tt-plant';
                        	var plant = jsonData[i][cols[j]];
                        	if (plant.length > 0) {
                        		if (plant === "noPepper") {
		                           	cell.innerHTML = "";                        			
                        		}
                        		else {
	                       			var im = "<img src='../../img/pp/" + plant + ".png' height='75' width='75'>";
		                           	cell.innerHTML = im;
                        		}
                        	}
                        	else {
	                       		cell.innerHTML = "";
                        	}
                        }
                        else {
//                        	if (showNames === "Y") {
	                        	// Inserting the cell at particular place
	                        	var cell = trow.insertCell(-1);
	                       		cell.className += 'tt-plant';
	                       		cell.innerHTML = jsonData[i][cols[j]];
                        	}
//                        }
                    }
                }              
                $('#live-garden-loader').hide();
            	$("#live-garden").show();

       	    }
           	else {
           		console.log("response data is null");
           	}
        },
        error : function(e) {
            console.log(e);
        }
    });

}



function liveGardenStart() {

	liveGardenPopulate();




}



var resetStudentDataTitle = "";
var resetStudentDataId = "";
var resetStudentDataLogmsg = "";

function resetStudentDataModal( title,studentId,username) {
		resetStudentDataTitle = title;
		resetStudentDataId = studentId;
		var temp4 = "<%= rb.getString("delete_math_data")%>" + ": " + username;
		var temp9 = "<%= rb.getString("delete_username_and_data")%>" + ": " + username;
		
		if (title == "4") {
			resetStudentDataLogmsg = "{ \"cmd\" : \"delete_math_data\", \"username\" : \"" + username + "\", \"id\" : \"" + resetStudentDataId + "\"}";
        	$("#resetStudentDataModalPopup").find("[class*='modal-body']").html(temp4);        	
        	$('#resetStudentDataModalPopup').modal('show');
		}
		else if (title == "9") {
			resetStudentDataLogmsg = "{ \"cmd\" : \"delete_username_and_data\", \"username\" : \"" + username + "\", \"id\" : \"" + resetStudentDataId + "\"}";
        	$("#resetStudentDataModalPopup").find("[class*='modal-body']").html(temp9);
        	$('#resetStudentDataModalPopup').modal('show');
		}	
}

function resetStudentData() {

	    $('#student_info_out').find('.loader').show();
    	$.ajax({
        type : "POST",
        url :pgContext+"/tt/tt/resetStudentdata",
        data : {
            studentId: resetStudentDataId,
            action: resetStudentDataTitle,
            lang: loc
        },
        success : function(response) {
            $('#student_info_out').find('.loader').hide();
            if (response.includes("***")) {
                $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
                $('#errorMsgModelPopup').modal('show');
            }else{
                if (teacherLoginType === "Normal") {
                	logTeacherEvent("resetStudentData",resetStudentDataLogmsg);
                }
                $("#successMsgModelPopup").find("[class*='modal-body']").html("<%= rb.getString("student_info_updated")%>");
                $('#successMsgModelPopup').modal('show');
            }
        }
    });
    return false;

}



function deleteInactiveStudentsModal( title,studentId,username) {
	var temp = "<%= rb.getString("delete_inactive_students")%>";
	
   	$("#deleteInactiveStudentsModalPopup").find("[class*='modal-body']").html(temp);        	
   	$('#deleteInactiveStudentsModalPopup').modal('show');
}


function deleteInactiveStudents() {

    $('#student_roster_out').find('.loader').show();
	$.ajax({
    type : "POST",
    url :pgContext+"/tt/tt/deleteInactiveStudents",
    data : {
        classId: classID,
        action: "0",
        lang: loc
    },
    success : function(response) {
        $('#student_roster_out').find('.loader').hide();
        if (response.includes("***")) {
            $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
            $('#errorMsgModelPopup').modal('show');
        }else{
        	var logmsg = "";
        	var responseSplitter = response.split(":");
        	var list = responseSplitter[0].trim();
        	if (list.size > 1) {
        		logmsg = "{ \"result\" : \"deleted_student_ids\", \"idArray\" : \"[ " + list + "]\"}";
        	}
        	else {
        		logmsg = response;
        		logmsg = "{ \"result\" : \"[" + response + "]\"}";
        	}
            if (teacherLoginType === "Normal") {
	            logTeacherEvent("deleteInactiveStudents",logmsg);
            }
        	$("#successMsgModelPopup").find("[class*='modal-body']").html( response );
            $('#successMsgModelPopup').modal('show');
        }
    }
});
return false;

}



function resetPassWordForThisStudent(id,uname){
    var newPassWordToSet = $("#resetPasswordfor"+id).serializeArray()[0].value;
    $('#student_info_out').find('.loader').show();
     $.ajax({
         type : "POST",
         url :pgContext+"/tt/tt/resetStudentPassword",
         data : {
             studentId: id,
             userName: uname,
             newPassWord : newPassWordToSet
         },
         success : function(response) {
             $('#student_info_out').find('.loader').hide();
             if (response.includes("***")) {
                 $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
                 $('#errorMsgModelPopup').modal('show');
             }else{
            	 var logMsg  = "{ \"id\" : \"" + id + "\", \"username\" : \"" + uname + "\", \"msg\" : \"password_is_reset\" }";
                 if (teacherLoginType === "Normal") {
				 	logTeacherEvent("resetStudentPassword",logMsg);
                 }
	             $("#successMsgModelPopup").find("[class*='modal-body']").html( "<%= rb.getString("password_is_reset")%>  <%= rb.getString("new_password_is")%> "+response+"");
                 $('#successMsgModelPopup').modal('show');
             }
         }
     });
    return false;
}

function cnfirmStudentPasswordForTagDownload() {
    window.location.href = pgContext + "/tt/tt/printStudentTags" + "?classId=" + classID + "&formdata=" + classID;
}


function updateStudentInfo(formName){
	var activity = "";
    var dataForm = $("#edit_Student_Form"+formName).serializeArray();
    var values = [];
    var names = [];
    $.each(dataForm, function(i, field){
        values[i] = field.value;
    });
    
    
    $('#student_info_out').find('.loader').show();
    $.ajax({
        type : "POST",
        url :pgContext+"/tt/tt/editStudentInfo",
        data : {
            studentId: formName,
            formData: values,
            lang: loc
        },
        success : function(response) {
            $('#student_info_out').find('.loader').hide();
            if (response.includes("***")) {
                $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
                $('#errorMsgModelPopup').modal('show');
            }else{
            	var JSONData = JSON.parse(response);
            	var msg = JSONData["msg"];
                if (teacherLoginType === "Normal") {
	                logTeacherEvent("updateStudentInfo",response);
                }
                $("#successMsgModelPopup").find("[class*='modal-body']").html(msg);
                $('#successMsgModelPopup').modal('show');
            }
        }

    });
}


function handleclickHandlers() {

    $("#edit_class_form").bootstrapValidator({
        // To use feedback icons, ensure that you use Bootstrap v3.1.0 or later
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            className: {
                validators: {
                    notEmpty: {
                        message: '<%= rb.getString("emsg_className")%>'
                    },
			        regexp: {
            			regexp: /^[a-zA-Z0-9 _\-\.]+$/,
                        message: '<%= rb.getString("emsg_field_invalid")%>'
        			}        
                }
            },
            classGrade: {
                validators: {
                    notEmpty: {
                        message: '<%= rb.getString("emsg_classGrade")%>'
                    }
                }
            },
            lowEndDiff: {
                validators: {
                    notEmpty: {
                        message: '<%= rb.getString("emsg_lowEndDiff")%>'
                    }
                }
            }, highEndDiff: {
                validators: {
                    notEmpty: {
                        message: '<%= rb.getString("emsg_highEndDiff")%>'
                    }
                }
            }, town: {
                validators: {
                    notEmpty: {
                        message: '<%= rb.getString("emsg_town")%>'
                    }
                },
		        regexp: {
        			regexp: /^[a-zA-Z0-9 _\-\.]+$/,
                    message: '<%= rb.getString("emsg_field_invalid")%>'
    			}        
                
            }, schoolName: {
                validators: {
                    notEmpty: {
                        message: '<%= rb.getString("emsg_schoolName")%>'
                    }
                },
		        regexp: {
        			regexp: /^[a-zA-Z0-9 _\-\.]+$/,
                    message: '<%= rb.getString("emsg_field_invalid")%>'
    			}        
                
            }, schoolYear: {
                validators: {

                    between: {
                        min: new Date().getFullYear(),
                        max: 2050,
                        message: '<%= rb.getString("emsg_schoolYearRange")%>'
                    },

                    notEmpty: {
                        message: '<%= rb.getString("emsg_schoolYear")%>'
                    }
                }
            }, gradeSection: {
                validators: {
                    notEmpty: {
                        message: '<%= rb.getString("emsg_gradeSection")%>'
                    },
			        regexp: {
            			regexp: /^[a-zA-Z0-9 _\-\.]+$/,
                        message: '<%= rb.getString("emsg_field_invalid")%>'
        			}                            
                }
            }, maxProb: {
                validators: {

                    between: {
                        min: 2,
                        max: 40,
                        message: '<%= rb.getString("emsg_maxProbRange")%>'
                    },

                    notEmpty: {
                        message: '<%= rb.getString("emsg_maxProbRange")%>'
                    }
                }
            }, minProb: {
                validators: {

                    between: {
                        min: 2,
                        max: 40,
                        message: '<%= rb.getString("emsg_minProbRange")%>'
                    },

                    notEmpty: {
                        message: '<%= rb.getString("emsg_minProb")%>'
                    }
                }
            }, maxTime: {
                validators: {

                    between: {
                        min: 0,
                        max: 30,
                        message: '<%= rb.getString("emsg_maxTimeRange")%>'
                    },

                    notEmpty: {
                        message: '<%= rb.getString("emsg_maxTime")%>'
                    }
                }
            }, minTime: {
                validators: {

                    between: {
                        min: 0,
                        max: 30,
                        message: '<%= rb.getString("emsg_minTimeRange")%>'
                    },

                    notEmpty: {
                        message: '<%= rb.getString("emsg_minTime")%>'
                    }
                }
            },
            color: {
                validators: {
                    notEmpty: {
                        message: '<%= rb.getString("emsg_class_color") %>'
                    }
                }
            }
            
        }
    }).on('success.form.bv', function (e) {
        $("#edit_class_form").data('bootstrapValidator').resetForm();
        e.preventDefault();
        var $form = $(e.target);
        var bv = $form.data('bootstrapValidator');
        $.post($form.attr('action'), $form.serialize(), function (result) {        	
        })
    });	

    $("#cluster_class_form").bootstrapValidator({
        // To use feedback icons, ensure that you use Bootstrap v3.1.0 or later
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            className: {
                validators: {
                    notEmpty: {
                        message: '<%= rb.getString("emsg_className")%>'
                    },
			        regexp: {
            			regexp: /^[a-zA-Z0-9 _\-\.]+$/,
                        message: '<%= rb.getString("emsg_field_invalid")%>'
        			}        
                }
            },
	    	classLanguage: {
	            validators: {
	                notEmpty: {
	                    message: '<%= rb.getString("emsg_classLanguage") %>'
	                }
	            }
	        }
        
        }
    }).on('success.form.bv', function (e) {
        $("#cluster_class_form").data('bootstrapValidator').resetForm();
        e.preventDefault();
        var $form = $(e.target);
        var bv = $form.data('bootstrapValidator');
        $.post($form.attr('action'), $form.serialize(), function (result) {        	
        })
    });
	
    
    $("#classHomePage").click(function () {
        $("#content-conatiner").children().hide();
        if (isCluster == 0) {        
        	$("#splash_page").show();
        }
    });
    
    $("#resetSurveySettings_handler").click(function () {
        $("#content-conatiner").children().hide();
        $("#reset_survey_setting_out").show();
    });

//    $("#manage_topics").click(function () {
//        var newlocation = '/ms/tt/tt/viewClassDetails?classId='+classID+'&currentSelection=classManageTopics';
//        $(location).attr('href', newlocation);
//    });

    $("#addMoreStudentsToClass").click(function () {
        $("#addMoreStudents").show();
        $("#addMoreStudentsToClass").prop('disabled', true);
    });

    $("#cancelForm").click(function () {
        $("#addMoreStudents").hide();
        $("#addMoreStudentsToClass").prop('disabled', false);
    });

    $("#manage_roster_handler").click(function () {
        $("#content-conatiner").children().hide();
        $("#student_roster_out").show();
    });
    
    $("#manage_student_info_handler").click(function () {
        $("#content-conatiner").children().hide();
        $("#student_info_out").show();
    });
    
    $("#manage_class_handler").click(function () {  
        $("#content-conatiner").children().hide();
/**
        $.ajax({
            type : "POST",
            url :pgContext+"/tt/tt/isClassInUse",
            data : {
                classId: classID
            },
            success : function(response) {
                if (response == "Y") {
                	$("#archiveClassBtn").show();
                	$("#deleteClassBtn").hide();
                }else{
                	$("#archiveClassBtn").hide();
                	$("#deleteClassBtn").show();
                }
            },
            error : function(e) {
                console.log(e);
            }
        });
*/
        $("#class_profile_out").show();
       
    });

    $("#cancelClassProfileBtn").click(function () {
        $("#content-conatiner").children().hide();
        if (isCluster == 0) {        
        	$("#splash_page").show();
        }
    });

    $("#clone_class_handler").click(function () {
        $("#content-conatiner").children().hide();
        
        $("#clone_class_out").show();
    });
    
    $("#cancelCloneClassBtn").click(function () {
        $("#content-conatiner").children().hide();
        if (isCluster == 0) {        
        	$("#splash_page").show();
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

    $("#teacher_feedback_handler").click(function () { 
        window.open( 
          "${pageContext.request.contextPath}/teacherTools/feedbackRequest.jsp", "_blank"); 
    });
    

    $("#content_apply_handler").click(function () {
        $('#content_apply_handler').css('background-color', '');
        $('#content_apply_handler').css('color', '#dddddd');

        $("#content-conatiner").children().hide();
        $("#content_apply_handle").show();
    });

    $("#live-dashboard_handler").click(function () {
        $('#content_apply_handler').css('background-color', '');
        $('#content_apply_handler').css('color', '#dddddd');

        $("#content-conatiner").children().hide();
        liveDashboardStart();
    	showReport3d();
    });

    $("#live-garden_handler").click(function () {
        $('#content_apply_handler').css('background-color', '');
        $('#content_apply_handler').css('color', '#dddddd');

        $("#content-conatiner").children().hide();
        liveGardenPopulate("N");
    	
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



}

function changeLandingPageHeaderAccordingToLanguage(){

	var header = {'sid':  '<%= rb.getString("student_id")%>','sname': '<%= rb.getString("student_name")%>','uname':  '<%= rb.getString("username")%>','problems': '<%= rb.getString("problems_solved")%>','timeInMS': '<%= rb.getString("time_solving_problems")%>','latestLogin': '<%= rb.getString("most_recent_login")%>'};
	return header;
}

function changeLandingPageHeader2AccordingToLanguage(){

	var header = {'sid':  '<%= rb.getString("student_id")%>','sname': '<%= rb.getString("student_name")%>','uname':  '<%= rb.getString("username")%>','problems': '<%= rb.getString("problems_solved")%>','timeInMS': '<%= rb.getString("time_solving_problems")%>','latestLogin': '<%= rb.getString("most_recent_login")%>'};
	return header;
}

function registerAllEvents(){
	
    $('#wrapper').toggleClass('toggled');


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

    var headers = changeLandingPageHeaderAccordingToLanguage();
    var headers2 = changeLandingPageHeader2AccordingToLanguage();
  

        landingPageReport1  =  $('#landingPageReport1').DataTable({
            data: [],
            destroy: true,
            columns: [
                { title: headers['sid'] },
                { title: headers['sname']  },
                { title: headers['uname']  },
                { title: headers['problems']  },
                { title: headers['timeInMS']  },
                { title: headers['latestLogin']  },
            ],
            "bPaginate": false,
            <%=jc_rb.getString("language_text")%>
            "bFilter": false,
            "bLengthChange": false,
            rowReorder: false,                
            "scrollX": true,
            "bSort" : false,
            "columnDefs": [
                {
                    "width": "10%",
                    "targets": [ 0 ],
                    "visible": false

                },{
                    "width": "10%",
                    "targets": [ 1 ],
                    "visible": true

                },{
                    "width": "10%",
                    "targets": [ 2 ],
                    "visible": false

                },
                {
                    "width": "10%",
                    "targets": [ 3 ],
                    "visible": true

                },
                {
                    "targets": [ 4 ],
                    "width": "10%",
                    "visible": true
                }, 
                {
                    "targets": [ 5 ],
                    "width": "10%",
                    "visible": true
                }
                    
        	]
        }    
        );    	
    
    
    $('#classLandingReportOne').on('show.bs.collapse', function ()  {

    	
    	getFilterLandingOne(); 
	    $('#landing-report-loader').show();

       	$.ajax({
            type : "POST",
            url : pgContext+"/tt/tt/getTeacherReports",
            data : {
                classId: classID,
                teacherId: teacherID,
                reportType: 'classLandingReportOne',
                lang: loc,
                filter: filterLandingOne
            },
            success : function(data) {
        	    $('#landing-report-loader').hide();
                var jsonData = $.parseJSON(data);
                landingPageReport1.clear().draw();
                landingPageReport1.rows.add(jsonData.levelOneData).draw();
                landingPageReport1.columns.adjust().draw();
            },
            error : function(e) {
                console.log(e);
            }
        });

    });


        landingPageReport2  =  $('#landingPageReport2').DataTable({
            data: [],
            destroy: true,
            columns: [
                { title: headers2['sid'] },
                { title: headers2['sname']  },
                { title: headers2['uname']  },
                { title: headers2['problems']  },
                { title: headers2['timeInMS']  },
                { title: headers2['latestLogin']  },
            ],
            "bPaginate": false,
	        <%=jc_rb.getString("language_text")%>
            "bFilter": false,
            "bLengthChange": false,
            rowReorder: false,                
            "scrollX": true,
            "bSort" : false,
            "columnDefs": [
                {
                    "width": "10%",
                    "targets": [ 0 ],
                    "visible": false

                },{
                    "width": "10%",
                    "targets": [ 1 ],
                    "visible": true

                },{
                    "width": "10%",
                    "targets": [ 2 ],
                    "visible": false

                },
                {
                    "width": "10%",
                    "targets": [ 3 ],
                    "visible": true

                },
                {
                    "targets": [ 4 ],
                    "width": "10%",
                    "visible": true
                }, 
                {
                    "targets": [ 5 ],
                    "width": "10%",
                    "visible": true
                }
                    
        	]
            
        }
        );    	
    
    

    var classListSize = $('#classListSize').val();
    if(classListSize != 0){

    	apply_content_table = $('#apply_content_table').DataTable({
	        "bPaginate": false,
	        <%=jc_rb.getString("language_text")%>
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
        
    var studentRosterSize = $('#studentRosterSize').val();
    if(studentRosterSize != 0) {
        studentRosterTable = $('#student_roster').DataTable({
            "bPaginate": false,
            <%=jc_rb.getString("language_text")%>
            "bFilter": false,
            "bLengthChange": false,
            "bSort": false            

        });
    }

    $('#createMoreStudentId').click(function () {

        var dataForm = $("#create_Student_id").serializeArray();
        var values=[];
        $.each(dataForm, function(i, field){
            values[i] = field.value;
        });

        $('#student_roster_out').find('.loader').show();
        $.ajax({
            type: "POST",
            url: pgContext + "/tt/tt/createMoreStudentIds",
            data: {
                formData: values,
                lang: loc
            },
            success: function (data) {
                $('#student_roster_out').find('.loader').hide();
                if (data.includes("***")) {
                    $("#errorMsgModelPopup").find("[class*='modal-body']").html( data );
                    $('#errorMsgModelPopup').modal('show');
                }else{
                    if (teacherLoginType === "Normal") {
	                    logTeacherEvent("createMoreStudentId",data);
                    }
                    $("#successMsgModelPopup").find("[class*='modal-body']").html( "<%= rb.getString("user_creation_successful")%> " );
                    $('#successMsgModelPopup').modal('show');
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
        var newlocation = pgContext+'/tt/tt/viewClassDetails?classId='+classID+'&currentSelection=classHomePage';
        $(location).attr('href', newlocation);
    });
    $("#successMsgModelPopupForProblemSets").find("[class*='close']").click(function () {
        var newlocation = pgContext+'/tt/tt/viewClassDetails?classId='+classID+'&currentSelection=classHomePage';
        $(location).attr('href', newlocation);
    });
	

	
}
    /** Report Handler Starts **/
    

    /** Report Handler Ends **/

</script>

	
	
	






	 
    <script type="text/javascript" src="<c:url value="/js/bootstrap/js/language_es.js" />"></script>    
    <script type="text/javascript">
        var servletContextPath = "${pageContext.request.contextPath}";
        var pgContext = '${pageContext.request.contextPath}';
        var classID = '${classInfo.classid}';
        var teacherID = '${teacherId}';
        var teacherLoginType = '${teacherLoginType}';
        var currentSelection = '${currentSelection}';
        var sessionID = "0";
        var prePostIds = '${prepostIds}'.split("~~");		
        var problem_imageURL = '${webContentpath}'+'problemSnapshots/prob_';
        var classesInCluster = "";
        $(document).ready(function () {

        	classesInCluster = '${classInfo.classesInCluster}'; 
			hasClusters = parseInt('${classInfo.hasClusters}');
			isCluster = parseInt('${classInfo.isCluster}');
			classColor = '${classInfo.color}';
			className = '${classInfo.name}';
        	var classGrade = parseInt('${classInfo.grade}');
        	var simpleLowDiff = '${classInfo.simpleLowDiff}';
        	simpleLowDiff = simpleLowDiff.replace('below',"");
        	var simpleHighDiff = '${classInfo.simpleHighDiff}';
        	simpleHighDiff = simpleHighDiff.replace('above',"");
            lowGradeLevel = classGrade - parseInt(simpleLowDiff);
            highGradeLevel = classGrade + parseInt(simpleHighDiff);
   			gradesLevelsUsedInThisClass = "";
            for (var gradeLevel = lowGradeLevel; gradeLevel <= highGradeLevel;gradeLevel = gradeLevel + 1) {
            	gradesLevelsUsedInThisClass = gradesLevelsUsedInThisClass + gradeLevel + ".";
            } 
            var displayem = gradesLevelsUsedInThisClass.replaceAll("."," ");
   		
        	generate_year_range(2021,2023);
            registerAllEvents();
            handleclickHandlers();

            if (isCluster == 1) {
            	document.getElementById("li_classHomePage").style.display = 'none';
            	document.getElementById("li_classReportCard").style.display = 'none';
            	document.getElementById("li_manage_roster").style.display = 'none';
            	document.getElementById("li_manage_class").style.display = 'none';
            	document.getElementById("li_live_garden").style.display = 'none';
            	document.getElementById("li_live_dashboard").style.display = 'none';
            	document.getElementById("li_teacher_feedback").style.display = 'none';
            	document.getElementById("li_clone_class").style.display = 'none';
            	document.getElementById("li_apply_content").style.display = 'none';
            }

            
            $("#content-conatiner").children().hide();

        	var selection = "";
        	var redirect = "";
        	if (currentSelection.indexOf("~") >= 0) {
        		var selections = currentSelection.split("~");
        		selection = selections[0];
        		redirect = selections[1];
        	}
        	else {
        		selection = currentSelection;
        	}
        	if (redirect == "manage_topics") {
        		window.location.href = '/ms/tt/tt/classManagerSubPage?classId=' + ${classInfo.classid} + '&currentSelection=classManageTopics';
        	}
        	else {
       
	            if (currentSelection == "classHomePage") {
	            	var titleClassname = "";
	                if (isCluster == 0) {                        
		            	titleClassname = 'home-title-' + '${classInfo.color}';
		            	document.getElementById("titleLine").innerHTML =  '<%= rb.getString("home_page_for_class") %>: <span class="' +  titleClassname + '">&nbsp;&nbsp;<strong>${classInfo.name}</strong>&nbsp;&nbsp;</span>';
	            	}
	                else {
	                	document.getElementById("titleLine").innerHTML =  '<%= rb.getString("home_page_for_cluster") %>: <span class="' +  titleClassname + '">&nbsp;&nbsp;<strong>${classInfo.name}</strong>&nbsp;&nbsp;</span>';
	                	titleClassname = 'home-title-' + '${classInfo.color}' + '-cluster';                	
	                }
	       			document.getElementById("classGrades").innerText = "<%= rb.getString("grade_levels")%> : [" + displayem + "]";
	            	$("#splash_page").show();            
	        	    $('#landing-report-loader').show();
	            	$("#classLandingReportOne").collapse('show');
	            }
	            else if (currentSelection == "manage_roster_handler") {
	                $('#manage_roster_handler').click();
	        	}            
	            else if (currentSelection == "manage_student_info_handler") {
	                $('#manage_student_info_handler').click();
	        	}            
	            else if (currentSelection == "manage_class_handler") {
	                $('#manage_class_handler').click();
	        	}            
	            else if (currentSelection == "content_apply_handler") {
	                $('#content_apply_handler').click();
	        	}            
				if (teacherLoginType === "Normal") {
	            	$("#resetSurveySettings").hide();
				}
				else {
	            	$("#resetSurveySettings").show();    				
				}
				
	            $('#grade').val("${classInfo.grade}").change();
	            $('#lowEndDiff').val("${classInfo.simpleLowDiff}").change();
	            $('#highEndDiff').val("${classInfo.simpleHighDiff}").change();
	            $("#schoolYear").val("${classInfo.schoolYear}").change();
	                     
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
        	}
        });

    </script>
    
    <script type="text/template"  id="student_table_Survey">
        <table class="table table-striped table-bordered hover">
            <thead>
            <tr>
                <th>Student Name</th>
                <th>Username</th>
				<th>Student Id</th>
                
            </tr>
            </thead>
        </table>
    </script>
    
    <script type="text/template"  id="question_table_Survey">
        <table class="table table-striped table-bordered hover">
            <thead>
            <tr>
                <th>Question</th>
                <th>Answer</th>
				
            </tr>
            </thead>
        </table>
    </script>
    
    <script id="editStudentInformation">

    var teacherClusterSelections = "";
    var teacherClassSelections = "";
    var classChoices = [];
    var clusterChoices = [];

    function selectTeacherCluster(myClassId) {

    	var txtClassId = "" + myClassId;
    	for (var i=0; i < clusterChoices.length; i++) {	
    		var splitter = clusterChoices[i].split(',');
    		if (splitter[1] === txtClassId) {
    			moveToDestination(splitter[1] + ',' + splitter[3]);
    			break;
    		}
    	}
    }



    function addToTeacherClassList(item, index) {
        
    	var titem = "" + item;
    	var tlist = titem.split(",");

    	if ((tlist[1] != classID) && (tlist[3] == 0))  {
    		clusterChoices.push(titem);	
    		teacherClusterSelections = teacherClusterSelections +  "<li id='Class" + tlist[1]  + "' class='dropdown-content' value='" + tlist[1] + "' onclick='selectTeacherCluster(" + tlist[1] + ");'>" + tlist[0] + "</li>";
    	}
    }

    function addToTeacherClusterList(item, index) {
        
    	var titem = "" + item;
    	var tlist = titem.split(",");

    	if (tlist[1] != classID) {
    		clusterChoices.push(titem);	
    		teacherClusterSelections = teacherClusterSelections +  "<li id='Class" + tlist[1]  + "' class='dropdown-content' value='" + tlist[1] + "' onclick='selectTeacherCluster(" + tlist[1] + ");'>" + tlist[0] + "</li>";
    	}
    }


    function editStudentInformation(id,fname,lname,uname,context){


    	
    var tr = context.closest('tr');
    var row = $('#student_roster').DataTable().row( tr );


    if ( row.child.isShown() ) {
        row.child( false ).remove();
    }else{
       // var editStudentInfoDiv = $($('#editStudentInfoDiv').html());
        if(fname == ''){
            var tempStudentName =  '<div class="form-group"><div class="input-group"><label for="studentFname"><%= rb.getString("first_name") %></label></div><div class="input-group">'+
                '<input type="text" id="studentFname" class="form-control" name="studentFname" /></div></div>';
        }else{
            var tempStudentName =  '<div class="form-group"><div class="input-group"><label for="studentFname"><%= rb.getString("first_name") %></label></div><div class="input-group">'+
                '<input type="text" value='+fname+' id="studentFname" class="form-control" name="studentFname" /></div></div>';
        }

        if(lname == ''){
            var tempStudentLastName =  '<div class="form-group"><div class="input-group"><label for="studentLname"><%= rb.getString("last_name") %></label></div><div class="input-group">'+
                '<input type="text" id="studentLname" class="form-control" name="studentLname" /></div></div>';

        }   else{
            var tempStudentLastName =  '<div class="form-group"><div class="input-group"><label for="studentLname"><%= rb.getString("last_name") %></label></div><div class="input-group">'+
                '<input type="text" value='+lname+' id="studentLname" class="form-control" name="studentLname" /></div></div>';
        }

        var tempStudentUserName =  '<div class="form-group"><div class="input-group"><label for="studentUsername"><%= rb.getString("username") %></label></div><div class="input-group">'+
            '<input type="text" value='+uname+' id="studentUsername" class="form-control" name="studentUsername"/></div></div>';

        var origStudentUserName =  '<div class="form-group"><div class="input-group"></div><div class="input-group hidden">'+
            '<input type="text" value='+uname+' id="origstudentUsername" class="form-control" name="origstudentUsername"/></div></div>';

        var formHtml = '<div class="panel-body"><form id="edit_Student_Form'+id+'" onsubmit="event.preventDefault();"><div class="form-group"><div class="input-group"><label for="studentId"><%= rb.getString("user_id") %></label></div><div class="input-group">'+
            '<input type="text" value='+id+' id="studentId" class="form-control" name="studentId" disabled="disabled" /></div></div>'+tempStudentUserName
            + tempStudentName + tempStudentLastName + origStudentUserName +
            '<div class="input-group"><button role="button" onclick="updateStudentInfo('+id+')" class="btn btn-primary"><%= rb.getString("update_information") %></button></div></form></div>';

        var formHtmlPassWord = '<div class="panel-body"><form id="resetPasswordfor'+id+'" onsubmit="event.preventDefault();"><div class="form-group"><div class="input-group"><label for="newPassword"><%= rb.getString("new_password") %></label></div><div class="input-group">'+
            '<input type="password"  placeholder="<%= rb.getString("new_password_to_be_set") %>" id="newPassword" class="form-control" name="newPassword"/></div></div>' +
            '<div class="input-group"><button role="button" onclick="resetPassWordForThisStudent('+id+',\'' + uname + '\')" type="button" class="btn btn-primary"> <%= rb.getString("reset_password") %></button></div></form></div>';




        var moveTabHeading = '';
        var tabPanel = "";
		    moveStudentId = id;     	       
 	       	moveTabHeading = '<%= rb.getString("move_the_student") %>';
            var formHtmlAssignToCluster = '<div class="panel-body"><form id="AssignToCluster'+id+'" onsubmit="event.preventDefault();"><div class="form-group"><div class="input-group"><label for="select_a_destination"><%= rb.getString("select_a_destination") %></label></div><div id="clusterList"></div><div class="input-group"><button id="moveClusterBtn" role="button" onclick="moveToDestination('+id+')" type="button" class="btn btn-primary"><%= rb.getString("submit") %></button></div></form></div>';  
            tabPanel = '<div style="width: 70%"> <ul class="nav nav-tabs" role="tablist"> <li class="active"> ' +
            '<a href="#home'+id+'" role="tab" data-toggle="tab"> <%= rb.getString("update_student_information") %> </a> </li> ' +
            '<li><a href="#profile'+id+'" role="tab" data-toggle="tab"> <%= rb.getString("reset_password_for_student") %> </a> </li> ' +
         	'<li><a href="#assignCluster'+id+'" role="tab" data-toggle="tab">' + moveTabHeading + '</a> </li> </ul>'+
         	'<div class="tab-content"> <div class="tab-pane fade active in" id="home'+id+'">'+formHtml+'</div><div class="tab-pane fade" id="profile'+id+'">'+formHtmlPassWord+'</div> <div class="tab-pane fade" id="assignCluster'+id+'">'+formHtmlAssignToCluster+'</div> </div> </div>';
 
        row.child(tabPanel).show();

        var clusterListFilter = "";
       	if (hasClusters == 1) {
       		clusterListFilter = "clusterList";
       	}
   		else {
       		clusterListFilter = "classList";
		}
       		
            $.ajax({
                type : "POST",
                url : pgContext+"/tt/tt/getTeacherReports",
                data : {
                	classId: classID,
                    teacherId: teacherID,
                    reportType: 'classClusterList',
                    lang: loc,
                    filter: clusterListFilter

                },
                success : function(data) {
                	    if (data) {
                	    	clusterChoices = [];
                	    	teacherClusterSelections = "";
                	    	$("#moveClusterBtn").hide();
                	    	var msg = "" + data;
                           	var jsonData = $.parseJSON(data);
           	                eachTeacherListData = jsonData.levelOneData;
           	                if (clusterListFilter ==  "clusterList") {
           	                	eachTeacherListData.forEach(addToTeacherClusterList);
           	                }
           	                else {
           	                	eachTeacherListData.forEach(addToTeacherClassList);           	                	
           	                }
           	             	document.getElementById("clusterList").innerHTML = "<ul>" + teacherClusterSelections + "</ul>";
           	             	$("#clusterList").show();
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
    }
    

    </script>
    
        
    <script type="text/template" id="editStudentInfoDiv">
        <div style="width: 50%">
            <!-- Nav tabs -->
            <ul class="nav nav-tabs" role="tablist">
                <li class="active">
                    <a href="#home" role="tab" data-toggle="tab">
                        <i class="fa fa-address-card-o" aria-hidden="true"></i> Update Student Information
                    </a>
                </li>
                <li><a href="#profile" role="tab" data-toggle="tab">
                    <i class="fa fa-key" aria-hidden="true"></i> Reset Password for Student
                </a>
                </li>
            </ul>
            <!-- Tab panes -->
            <div class="tab-content">
                <div class="tab-pane fade active in" id="home">

                </div>
                <div class="tab-pane fade" id="profile">

                </div>
            </div>
        </div>
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
                        <a <i class="fa fa-fw fa-info-circle"></i>> <%= rb.getString("class_code") %>: ${classInfo.classid}</a>
                    </li>
                    <li>
                        <a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/logout"><i
                                class="fa fa-fw fa-power-off"></i> <%= rb.getString("log_out") %></a>
                    </li>
                </ul>
            </li>
        </ul>
    </nav>
    <nav class="navbar navbar-inverse navbar-fixed-top" id="sidebar-wrapper" role="navigation">
        <ul class="nav sidebar-nav">
            <li>
                <a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/ttMain"><i
                        class="fa fa-fw fa-home"></i> <%= rb.getString("home") %></a>
            </li>
            <li id="li_classHomePage">
                <a id="classHomePage" href="#"><i class="fa fa-fw fa-home"></i> <%= rb.getString("class_home") %></a>
            </li>

            <li id="li_classReportCard"><a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/viewClassReportCard?classId=${classInfo.classid}&currentSelection=classReportCard" id="report_card"><i class="fa fa-bar-chart"></i> <%= rb.getString("class_report_card") %></a></li>
		
            <li id="li_manageTopics"><a id="manage_topics" href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/classManagerSubPage?classId=${classInfo.classid}&currentSelection=classManageTopics"><i class="fa fa-list"></i> <%= rb.getString("manage_problem_sets") %></a></li>
        
            <li id="li_manage_roster"><a id="manage_roster_handler"><i class="fa fa-fw fa-users"></i> <%= rb.getString("manage_class_roster") %></a></li>

            <li id="li_manage_student_info"><a id="manage_student_info_handler"><i class="fa fa-fw fa-users"></i> <%= rb.getString("manage_student_info") %></a></li>

            <li id="li_manage_class"><a id="manage_class_handler"><i class="fa fa-fw fa-cog"></i> <%= rb.getString("manage_class") %></a></li>
		
             <li id="resetSurveySettings"><a id="resetSurveySettings_handler"><i class="fa fa-fw fa-cog"></i><%= rb.getString("survey_settings") %></a></li>
            
             <li id="li_apply_content"><a id="content_apply_handler"><i class="fa fa-fw fa-cogs"></i><%= rb.getString("apply_class_content") %></a></li>

             <li id="li_live_dashboard"><a id="live-dashboard_handler"><i class="fa fa-fw fa-cogs"></i> <%= rb.getString("live_dashboard") %></a></li>

             <li id="li_live_garden"><a id="live-garden_handler"><i class="fa fa-fw fa-cogs"></i> <%= rb.getString("class_garden") %></a></li>
             
             <li id="li_teacher_feedback"> <a id="teacher_feedback_handler"><i class="fa fa-fw fa-search"></i> <%= rb.getString("send_us_feedback") %></a></li>
             
            <li id="li_clone_class" ><a id="clone_class_handler"><i class="fa fa-fw fa-cogs"></i> <%= rb.getString("create_class_cluster") %></a></li>
		</ul>
        <!-- /#sidebar-end -->
    </nav>
    <div id="page-content-wrapper">

        <h1 id="classManagerHeader" class="home-page-header" >
        	<div id="titleLine"></div>
        </h1>
        <div id="classGrades" style="font-size:16px;" class="home-page-header2">
        </div>

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
            <div id="reset_survey_setting_out" style="display:none; width: 100%;">

                <div class="container-fluid">
                    <div class="row">

                        <div class="col-md-4 col-sm-4">
                            <div class="panel panel-default">

                                <div class="panel-body">
                                    <h1 class="tt-page-header">
                                        <small><%= rb.getString("turn_off_on_surveys") %></small>
                                    </h1>
                                </div>
                                <springForm:form id="rest_survey_setting_form" method="post"
                                                 action="${pageContext.request.contextPath}/tt/tt/ttResetSurvey"
                                                 modelAttribute="createClassForm">


                                <input type="hidden" name="teacherId" value="${teacherId}">
                                <input type="hidden" name="classId" value=" ${classInfo.classid}">
                                <div class="panel-body">
                                    <div class="form-check">
                                        <c:choose>
                                            <c:when test="${classInfo.showPreSurvey}">
                                            <springForm:checkbox path="showPreSurvey" value="${classInfo.showPreSurvey}" checked = "checked"/>
                                            </c:when>
                                            <c:otherwise>
                                                <springForm:checkbox path="showPreSurvey" value="${classInfo.showPreSurvey}" disabled="true"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <span class="form-check-label"><%= rb.getString("pre_test_survey") %></span>
                                    </div>
                                </div>
                                <div class="panel-body">
                                    <div class="form-check">
                                        <c:choose>
                                            <c:when test="${classInfo.showPostSurvey}">
                                                <springForm:checkbox path="showPostSurvey" value="${classInfo.showPostSurvey}" checked = "checked"/>
                                            </c:when>
                                            <c:otherwise>
                                                <springForm:checkbox path="showPostSurvey" value="${classInfo.showPostSurvey}"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <span class="form-check-label"><%= rb.getString("post_test_survey") %></span>
                                    </div>
                                </div>
                                <div class="panel-body">
                                    <button role="button" type="submit" class="btn btn-primary"><%= rb.getString("submit") %>
                                    </button>
                                </div>
                            </div>
                            </springForm:form>
                        </div>


                        <div class="col-md-8 col-sm-8">
                            <div class="panel panel-default">
                                <div class="panel-body">
                                    <h1 class="tt-page-header">
                                        <small><%= rb.getString("available_surveys") %></small>
                                    </h1>
                                </div>
                                <div class="panel-body">

                                    <table id="activeSurveyList" class="table table-striped table-bordered hover"
                                           width="80%">
                                        <thead>
                                        <tr>
                                            <th><%= rb.getString("survey_id") %></th>
                                            <th><%= rb.getString("list_of_surveys") %></th>
                                            <th><%= rb.getString("first_time_logs_in") %></th>
                                            <th><%= rb.getString("next_time_logs_in") %></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="activeSurvey" items="${activeSurveys}">
                                            <tr>
                                                <td><c:out value="${activeSurvey.key}"/></td>
                                                <td><c:out value="${activeSurvey.value}"/></td>
                                                <td></td>
                                                <td></td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="panel-body">
                                    <ul>
                                        <li>
                                            <%= rb.getString("survey_note1") %>
                                        </li>
                                        <li>
                                            <%= rb.getString("survey_note2") %>
                                        </li>
                                    </ul>
                                </div>
                                <div class="panel-body">
                                    <button id="select_activeSurveyList" class="btn btn-primary btn-lg"
                                            aria-disabled="true"><%= rb.getString("publish_survey_settings") %>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            <div id="class_Level_Reports_Container" class="row" style="display:none;width: 75%;">
            </div>

    </div>

            <div id="student_roster_out" style="display:none;width: 100%;">

                <div class="row">
	                <div class="col-md-10">
	                    <h3 class="tt-page-header">
	                        <small><%= rb.getString("manage_class_roster") %></small>
	                    </h3>
	                </div>
                </div>
                <div class="loader" style="display: none" ></div>               
                 <div class="panel-group" id="accordion2">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseClassroom">
                                    <%= rb.getString("in_the_classroom") %>
                                </a>
                                <button type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="collapseClassroom" class="panel-collapse collapse">
                            <div class="panel-body">
			                	<div id="inClassOption">
				                    <div class="panel panel-default"  style="width: 100%;">
				                        <div class="panel-body">
				                        	<div class="col-md-8 border">
				                        	<%= rb.getString("create_more_ids_instructions_heading") %>
				                        	<br>
				                            <button id="addMoreStudentsToClass" class="btn btn-primary btn-lg" aria-disabled="true"><%= rb.getString("create_student_id") %></button>
				                            <button id="download_student_tags" class="btn btn-primary btn-lg pull-right" aria-disabled="true" onclick="cnfirmStudentPasswordForTagDownload()"><%= rb.getString("download_student_tags") %></button>
				                        	</div>
				                        	<div class="col-md-2">
				                        	<br>
				                        	</div>				                        	
				                        	<div class="col-md-2">
				                        	<%= rb.getString("delete_inactive_usernames") %>
				                        	<br><br><br>
				                            <button id="deleteAllUnused" class="btn btn-danger btn-lg pull-right" aria-disabled="true" onclick="deleteInactiveStudentsModal()"><%= rb.getString("delete") %></button>
											</div>
				                        </div>
									</div>
				                    <div class="panel panel-default"  style="width: 100%;">
				                        <div class="panel-body" id="addMoreStudents" style="display: none;">
				                            <springForm:form id="create_Student_id" method="post"
				                                             action="${pageContext.request.contextPath}/tt/tt/createStudentId"
				                                             modelAttribute="createClassForm" onsubmit="event.preventDefault();">
				
				                                <div class="form-group">
				                                    <label for="userPrefix"><%= rb.getString("student_username_prefix") %></label>
				                                    <div class="input-group">
				                                        <springForm:input path="userPrefix" id="userPrefix" name="userPrefix" value="Math"
				                                                          placeholder="Math" class="form-control" type="text" onblur="userPrefixLookup()"/>
				                                    </div>
				                                </div>
				                                <div class="form-group hidden">
				                                    <div class="input-group">
				                                        <span class="input-group-addon"><i class="fa fa-eye"></i></span>
				                                        <springForm:input path="passwordToken" id="passwordToken" name="passwordToken" value="useClass"
				                                                          placeholder="" class="form-control" type="password"/>
				                                    </div>
				                                </div>
				                                <div class="form-group">
				                                    <label for="noOfStudentAccountsForClass"><%= rb.getString("number_IDs_to_create") %></label>
				                                    <div class="input-group">
				                                        <springForm:input path="noOfStudentAccountsForClass" id="noOfStudentAccountsForClass" name="noOfStudentAccountsForClass"
				                                                          placeholder="" class="form-control" type="text"/>
				                                    </div>
				                                </div>
				                                <input type="hidden" name="teacherId" id="teacherId" value="${teacherId}">
				                                <input type="hidden" name="classId" id="classId" value="${classInfo.classid}">
				                                <div class="form-group">
				                                    <button role="button" type="submit" id="createMoreStudentId" class="btn btn-primary"><%= rb.getString("add_student_ids") %></button>
				                                    <button role="button" type="button" id="cancelForm" class="btn btn-danger"><%= rb.getString("cancel") %></button>
				                                </div>
				                            </springForm:form>
				
				                        </div>
				                    </div>
				                </div>
                            </div>
                        </div>
                    </div>
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseDistance">
                                   <%= rb.getString("distance_learning") %>
		                		<p><%= rb.getString("distance_learning_instructions") %></p>
                                </a>
                                <button type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="collapseDistance" class="panel-collapse collapse">
                            <div class="panel-body">
					            <div id="distanceOption">
				               		<div class="col-md-2"></div>
					            	<div class="col-md-8">
					            		<p><%= rb.getString("distance_learning_email_greeting")%></p>
					            		<p><%= rb.getString("distance_learning_email_text")%></p>
					            		<p><%=msHost%><%=msContext%>/WoAdmin?action=UserRegistrationStart&var=b&startPage=LoginK12_1&classId=${classInfo.classid}&msRole=student</p>
					            		<p><%= rb.getString("distance_learning_email_password_is")%>:  ${classInfo.classid}</p>
					            		<p><%= rb.getString("distance_learning_email_write_it_down")%></p>
									</div>
					            	<div class="col-md-2"></div>	               		
					            </div>

                            </div>
                        </div>
                    </div>
				</div>
            </div>
            


            <div id="student_info_out" style="display:none;width: 100%;">

                <div class="row">
	                <div class="col-md-10">
	                    <h3 class="tt-page-header">
	                        <small><%= rb.getString("manage_student_info") %></small>
	                    </h3>
	                </div>
	                <div class="col-md-10">
	                    <h3 class="tt-page-header">
	                        <small><%= rb.getString("students_password_will_be") %>:</small>&nbsp${classInfo.classid}
	                    </h3>
	                </div>
                </div>
                <div class="loader" style="display: none" ></div>               
                <div class="panel-group">
                    <div class="panel panel-default">
                    	<div class="panel-body">			                	
		                    <table id="student_roster" class="table table-striped table-bordered hover" cellspacing="0" width="100%">
		                        <thead>
		                        <tr>
		                            <th rowspan="2"><%= rb.getString("student_id") %></th>
		                            <th rowspan="2"><%= rb.getString("first_name") %></th>
		                            <th rowspan="2"><%= rb.getString("last_name") %> </th>
		                            <th rowspan="2"><%= rb.getString("username") %></th>
		                            <th colspan="7" style="text-align: center;"> <%= rb.getString("student_data") %></th>
		                        </tr>
		
		                        <tr>
		                            <%--  <th>Clear All</th>--%>
		                            <th><%= rb.getString("delete_math_data") %></th>
		                            <%-- <th>Reset Practice Hut</th>
		                             <th>Clear Pretest</th>
		                             <th>Clear Posttest</th>--%>
		                            <th><%= rb.getString("delete_username_and_data") %></th>
		                            <th><%= rb.getString("change_password_and_username") %></th>
		                        </tr>
		                        </thead>
		                        <tbody>
		                        <input type="hidden" id="studentRosterSize" name="studentRosterSize" value="${students.size()}">
		                        <c:forEach var="studentInfo" varStatus="i" items="${students}">
		                            <tr>
		                                <td>${studentInfo.id}</td>
		                                <td>${studentInfo.fname}</td>
		                                <td>${studentInfo.lname}</td>
		                                <td>${studentInfo.uname}</td>
		                                <td>
		                                     <a  onclick="resetStudentDataModal(4,'${studentInfo.id}','${studentInfo.uname}')" class="success details-control" aria-expanded="true">
		                                         <i class="fa fa-window-close" aria-hidden="true"></i>
		                                     </a>
		                                </td>
		                                <td>
		                                     <a  onclick="resetStudentDataModal(9,'${studentInfo.id}','${studentInfo.uname}')" class="success details-control" aria-expanded="true">
		                                         <i class="fa fa-window-close" aria-hidden="true"></i>
		                                     </a>
		                                </td>
			                            <td>
			                                 <a onclick="editStudentInformation('${studentInfo.id}','${studentInfo.fname}','${studentInfo.lname}','${studentInfo.uname}',this)" class="success details-control" aria-expanded="true">
			                                     <i class="fa fa-pencil-square-o" aria-hidden="true"></i>
			                                 </a>
			                            </td>
		                            </tr>
		                        </c:forEach>
		                        </tbody>
		                    </table>				                
                    	</div>                        
                    </div>
				</div>
            </div>


            <div id="clone_class_out" style="display:none;width: 100%;">

                <div class="row">
	                <div class="col-md-10">
	                    <h3 class="tt-page-header">
	                        <small><%= rb.getString("create_class_cluster") %></small>
	                    </h3>
	                </div>
                </div>
                <div class="loader" style="display: none" ></div>               
                        <div class="panel-body" id="cloneClassProfile">
				            <springForm:form id="cluster_class_form" method="post"
				                             action="${pageContext.request.contextPath}/tt/tt/ttCloneClass"
				                             modelAttribute="createClassForm">
				                <div class="row">
				                    <input type="hidden" name="classId" id="classId" value=" ${classInfo.classid}">
				                    <input type="hidden" name="teacherId" id="teacherId" value="${teacherId}">
				                    
				                    <input type="hidden" name="town" id="town" value="${classInfo.town}">
				                    <input type="hidden" name="school" id="school" value="${classInfo.school}">
				                    <input type="hidden" name="schoolYear" id="schoolYear" value="${classInfo.schoolYear}">
				                    <input type="hidden" name="section" id="section" value="${classInfo.section}">
				                    
				                    <input type="hidden" name="classGrade" id="classGrade" value="${classInfo.grade}">
				                    <input type="hidden" name="lowEndDiff" id="lowEndDiff" value="${classInfo.simpleLowDiff}">
				                    <input type="hidden" name="highEndDiff" id="highEndDiff" value="${classInfo.simpleHighDiff}">
				                    
				                    <input type="hidden" name="maxProb" id="maxProb" value="${classInfo.maxProb}">
				                    <input type="hidden" name="minProb" id="minProb" value="${classInfo.minProb}">
				                    <input type="hidden" name="maxTime" id="maxTime" value="${classInfo.maxTime}">
				                    <input type="hidden" name="minTime" id="minTime" value="${classInfo.minTime}">
				                    <input type="hidden" name="color"   id="color"   value="${classInfo.color}">

				                    
			                        <div class="panel panel-default">
					                    <div id="clone_class_out" class="col-md-4 col-sm-4">
				                            <div class="panel-heading">
				                                <%= rb.getString("identification_settings") %>
				                            </div>
				                             <div class="panel-body">
				                                <div class="form-group">
				                                    <label for="className"><%= rb.getString("cluster_name") %></label>
				                                    <div class="input-group">
				                                    <span class="input-group-addon"><i
				                                            class="glyphicon glyphicon-blackboard"></i></span>
				                                        <springForm:input path="className" id="className" name="className"
				                                                          class="form-control" type="text" onblur="startClusterLoader()" value="${classInfo.name}"/>
				                                    </div>
				                                </div>
				                                
				                               <div class="form-group">
				                                    <label for="classLanguage"><%= rb.getString("class_language") %></label>
				                                    <div class="input-group">
				                                        <span class="input-group-addon"><i
				                                                class="glyphicon glyphicon-education"></i></span>
				                                        <springForm:select path="classLanguage" class="form-control" id="classLanguage"
				                                                           name="classLanguage" value="${classInfo.classLanguageCode}>">
				                                            <springForm:option value=""><%= rb.getString("select_language_for_class") %></springForm:option>
				                                            <springForm:option value="en:English"><%= rb.getString("english") %></springForm:option>
				                                            <springForm:option value="es:Spanish"><%= rb.getString("spanish") %></springForm:option>
				                                        </springForm:select>
				                                    </div>
				                                </div>
				                                
				                                
					                        </div>
					                    </div>
					                   		                        
				                    </div>
				                </div>
				                
				                <div class="row">
				                        <div class="panel-body class="col-md-offset-5 col-sm-offset-5 col-md-4 col-sm-4">
				                            <button id="cloneClassBtn" type="submit" class="btn btn-primary btn-lg" aria-disabled="true"><%= rb.getString("submit_changes") %></button>
				                            <button id="cancelCloneClassBtn" class="btn btn-danger btn-lg" aria-disabled="true"><%= rb.getString("cancel") %></button>
				                        </div>
				                </div>
				            </springForm:form>
				            <div id="createClusterLoader" class="loader" style="display: none" ></div>               
				            <div class="col-lg-12">
				                <h1 class="page-header">
				                    <small><%= rb.getString("setup_cluster_instructions") %></small>
				                </h1>
				            </div>
				            <div id="no-refresh-msg">
				            	<div class="row">
				                 <h1 class="tt-paused-logins-message">
									<%= rb.getString("do_not_reload_page") %>                 
								</h1>
				                 </div>
				            </div>
                </div>            
                        
            </div>





            <div id="class_profile_out" style="display:none;width: 100%;">

                <div>
                    <h3 class="tt-page-header">
                        <%= rb.getString("class_configuration") %>
                    </h3>

                        <div class="panel-body" id="editClassProfile">
				            <springForm:form id="edit_class_form" method="post"
				                             action="${pageContext.request.contextPath}/tt/tt/ttEditClass"
				                             modelAttribute="createClassForm">
				                <div class="row">
				                    <input type="hidden" name="classId" id="classId" value=" ${classInfo.classid}">
				                    <input type="hidden" name="teacherId" id="teacherId" value="${teacherId}">
				                    <input type="hidden" name="classLanguage" id="classLanguage" value="${classInfo.classLanguageCode}">
			                        <div class="panel panel-default">
					                    <div id="create_class_out" class="col-md-4 col-sm-4">
				                            <div class="panel-heading">
				                                <%= rb.getString("identification_settings") %>
				                            </div>
				                             <div class="panel-body">
				                                <div class="form-group">
				                                    <label for="className"><%= rb.getString("class_name") %></label>
				                                    <div class="input-group">
				                                    <span class="input-group-addon"><i
				                                            class="glyphicon glyphicon-blackboard"></i></span>
				                                        <springForm:input path="className" id="className" name="className"
				                                                          class="form-control" type="text" value="${classInfo.name}"/>
				                                    </div>
				                                </div>
				                                <div class="form-group">
				                                    <label for="town"><%= rb.getString("town") %></label>
				                                    <div class="input-group">
				                                    <span class="input-group-addon"><i
				                                            class="glyphicon glyphicon-tree-deciduous"></i></span>
				                                        <springForm:input path="town" id="town" name="town"
				                                                          class="form-control"
				                                                          type="text" value="${classInfo.town}"/>
				                                    </div>
				                                </div>
				                                <div class="form-group">
				                                    <label for="schoolName"><%= rb.getString("school") %></label>
				                                    <div class="input-group">
				                                        <span class="input-group-addon"><i class="fa fa-university"></i></span>
				                                        <springForm:input path="schoolName" id="schoolName" name="schoolName"
				                                                          class="form-control" type="text" value="${classInfo.school}"/>
				                                    </div>
				                                </div>
				                               <div class="form-group">
				                                    <label for="schoolYear"><%= rb.getString("year") %></label>
				                                    <div class="input-group">
				                                        <span class="input-group-addon"><i
				                                                class="glyphicon glyphicon-hourglass"></i></span>
				                                        <springForm:select path="schoolYear" class="form-control" id="schoolYear"
				                                                           name="schoolYear" value="${classInfo.schoolYear}">
				                                            <springForm:option value=""><%= rb.getString("select_year") %></springForm:option>
				                                            <springForm:option value="2021">2020/2021</springForm:option>
				                                            <springForm:option value="2022">2021/2022</springForm:option>
				                                            <springForm:option value="2023">2022/2023</springForm:option>
				                                        </springForm:select>
				                                    </div>
				                                </div>
				                                <div class="form-group">
				                                    <label for="gradeSection"><%= rb.getString("section") %></label>
				                                    <div class="input-group">
				                                    <span class="input-group-addon"><i
				                                            class="glyphicon glyphicon-menu-hamburger"></i></span>
				                                        <springForm:input path="gradeSection" id="gradeSection" name="gradeSection"
				                                                          class="form-control" type="text" value="${classInfo.section}"/>
				                                    </div>
				                                </div>
				                                <div class="form-group">
				                                    <label for="color"><%= rb.getString("color_scheme") %></label>
				                                    <div class="input-group">
				                                        <span class="input-group-addon"><i
				                                                class="glyphicon glyphicon-education"></i></span>
				                                        <springForm:select path="color" class="form-control" id="classColor"
				                                                           name="color">
				                                            <springForm:option value=""><%= rb.getString("select_color_scheme") %></springForm:option>
				                                            <springForm:option class="panel-green" value="green"><%= rb.getString("green") %> </springForm:option>
				                                            <springForm:option class="panel-blue" value="blue"><%= rb.getString("blue") %> </springForm:option>
				                                            <springForm:option class="panel-red" value="red"><%= rb.getString("red") %> </springForm:option>
				                                            <springForm:option class="panel-violet" value="violet"><%= rb.getString("violet") %> </springForm:option>
				                                            <springForm:option class="panel-yellow" value="orange"><%= rb.getString("yellow") %> </springForm:option>
				                                        </springForm:select>
				                                    </div>
				                                </div>
					                        	</div>
					                    	</div>
					                   
					                    	<div id="create_class_out_middle" class="col-md-4 col-sm-4">
					                            <div class="panel-heading">
					                                <%= rb.getString("grade_level_settings") %>
					                            </div>
				                                <div class="form-group">
				                                    <label for="classGrade"><%= rb.getString("class_grade") %></label>
				                                    <div class="input-group">
				                                        <span class="input-group-addon"><i
				                                                class="glyphicon glyphicon-education"></i></span>
				                                        <springForm:select path="classGrade" class="form-control" id="grade"
				                                                           name="classGrade" value="${classInfo.grade}">
				                                            <springForm:option value=""><%= rb.getString("select_grade") %></springForm:option>
				                                            <springForm:option value="5"><%= rb.getString("grade") %> 5</springForm:option>
				                                            <springForm:option value="6"><%= rb.getString("grade") %> 6</springForm:option>
				                                            <springForm:option value="7"><%= rb.getString("grade") %> 7</springForm:option>
				                                            <springForm:option value="8"><%= rb.getString("grade") %> 8</springForm:option>
<!--                                        
                                            				<springForm:option value="9"><%= rb.getString("grade") %> 9</springForm:option>
                                            				<springForm:option value="10"><%= rb.getString("grade") %> 10</springForm:option>
                                            				<springForm:option value="adult"><%= rb.getString("grade") %> adult</springForm:option>
-->                                           
				                                        </springForm:select>
				                                    </div>
				                                </div>
				
				                                <div class="form-group">
				                                    <label for="lowEndDiff"><%= rb.getString("problem_complexity_lower") %></label>
				                                    <div class="input-group">
				                                        <span class="input-group-addon"><i
				                                                class="glyphicon glyphicon-education"></i></span>
				                                        <springForm:select path="lowEndDiff" class="form-control" id="lowEndDiff"
				                                                           name="lowEndDiff" value="${classInfo.simpleLowDiff}">
				                                            <springForm:option value=""><%= rb.getString("select_complexity") %></springForm:option>
				                                            <springForm:option value="below3"><%= rb.getString("three_grades_below") %></springForm:option>
				                                            <springForm:option value="below2"><%= rb.getString("two_grades_below") %></springForm:option>
				                                            <springForm:option value="below1"><%= rb.getString("one_grades_below") %></springForm:option>
				                                            <springForm:option value="below0"><%= rb.getString("no_grades_below") %></springForm:option>
				                                        </springForm:select>
				                                    </div>
				                                </div>
				
				                                <div class="form-group">
				                                    <label for="highEndDiff"><%= rb.getString("problem_complexity_higher") %></label>
				                                    <div class="input-group">
				                                        <span class="input-group-addon"><i
				                                                class="glyphicon glyphicon-education"></i></span>
				                                        <springForm:select path="highEndDiff" class="form-control" id="highEndDiff"
				                                                           name="highEndDiff" value="${classInfo.simpleHighDiff}">
				                                            <springForm:option value=""><%= rb.getString("select_complexity") %></springForm:option>
				                                            <springForm:option value="above3"><%= rb.getString("three_grades_above") %></springForm:option>
				                                            <springForm:option value="above2"><%= rb.getString("two_grades_above") %></springForm:option>
				                                            <springForm:option value="above1"><%= rb.getString("one_grades_above") %></springForm:option>
				                                            <springForm:option value="above0"><%= rb.getString("no_grades_above") %></springForm:option>
				                                        </springForm:select>
				                                    </div>
				                                </div>
				                            </div>
					                    	<div id="create_class_out_left" class="col-md-4 col-sm-4">
					                            <div class="panel-heading">
					                                <%= rb.getString("advanced_settings") %>
					                            </div>
					                            <div class="panel-body">
					                                
					                                <div class="form-group">
					                                    <label for="maxProb"><%= rb.getString("max_problems_per_topic") %></label>
					                                    <div class="input-group">
					                                    <span class="input-group-addon"><i
					                                            class="glyphicon glyphicon-menu-hamburger"></i></span>
					                                        <springForm:input path="maxProb" id="maxProb" name="maxProb"
					                                                          class="form-control" type="text" value="${classInfo.maxProb}" onblur="verifyProbMinMax()" />
					                                    </div>
					                                </div>
					                                <div class="form-group">
					                                    <label for="minProb"><%= rb.getString("min_problems_per_topic") %></label>
					                                    <div class="input-group">
					                                    <span class="input-group-addon"><i
					                                            class="glyphicon glyphicon-menu-hamburger"></i></span>
					                                        <springForm:input path="minProb" id="minProb" name="minProb"
					                                                          class="form-control" type="text" value="${classInfo.minProb}" onblur="verifyProbMinMax()"/>
					                                    </div>
					                                </div>
					                                <div class="form-group">
					                                    <label for="maxTime"><%= rb.getString("max_time_in_topic") %></label>
					                                    <div class="input-group">
					                                    <span class="input-group-addon"><i
					                                            class="glyphicon glyphicon-menu-hamburger"></i></span>
					                                        <springForm:input path="maxTime" id="maxTime" name="maxTime"
					                                                          class="form-control" type="text" value="${classInfo.maxTime}" onblur="verifyTimeMinMax()"/>
					                                    </div>
					                                </div>
					                                <div class="form-group">
					                                    <label for="minTime"><%= rb.getString("min_time_in_topic") %></label>
					                                    <div class="input-group">
					                                    <span class="input-group-addon"><i
					                                            class="glyphicon glyphicon-menu-hamburger"></i></span>
					                                        <springForm:input path="minTime" id="minTime" name="minTime"
					                                                          class="form-control" type="text" value="${classInfo.minTime}" onblur="verifyTimeMinMax()"/>
					                                    </div>
					                                </div>
					                            </div>
                        					</div>				                        
				                    </div>
				                </div>
				                <div class="row">
				                        <div class="panel-body class="col-md-offset-5 col-sm-offset-5 col-md-4 col-sm-4">
				                            <button id="editClassProfileBtn" type="submit" class="btn btn-primary btn-lg" aria-disabled="true"><%= rb.getString("submit_changes") %></button>
				                            <button id="cancelClassProfileBtn" class="btn btn-danger btn-lg" aria-disabled="true"><%= rb.getString("cancel") %></button>
				                        </div>
				                </div>
				            </springForm:form>
                        </div>
					</div>                                         
                 </div>
            
 
            
             <div id="content_apply_handle" style="display:none;width: 100%;">
             <div>
                    <h3 class="tt-page-header">
                        <small><%= rb.getString("apply_content_to_all_classes") %></small>
                    </h3>
					<input type="hidden" id="classListSize" name="classListSize" value=" ${fn:length(classList)}">
                    <div class="panel panel-default"  style="width: 60%;">
                        <div class="panel-body"><%= rb.getString("apply_content_instructions") %>
                        </div>
                        <div class="panel-body">
                            <button id="apply_content" class="btn btn-primary btn-lg" aria-disabled="true"><%= rb.getString("apply_content") %></button>
                        </div>
                    </div>
                    <table id="apply_content_table" class="table table-striped table-bordered hover" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                        <th><%= rb.getString("class_id") %></th>
                        <th><%= rb.getString("class_name") %></th>
                        <th><%= rb.getString("apply_content") %></th>
                         </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="classList" varStatus="i" items="${classList}">
                        <c:if test="${classInfo.classid != classList.classid}">
                            <tr>
                             <td>${classList.classid}</td>
                              <td>${classList.name}</td>
                               <td></td>
                            </tr>
                          </c:if>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
             </div>

             <div id="live-dashboard" class="container-fluid" style="display:none;width: 100%;">
               	<div class="row">
	             	<div class="col-md-12">                                
	                    <div id="liveDashboardProblemPane" class="col-md-4 tt-LiveDashboardProblemPane">
	
	                        <div class="row">
		                        <div style="text-align:center;">
		                        	<h3><%= rb.getString("number_problems_class_solved") %></h3>
		                        </div>	                       
		            		</div>
	                        <div class="row">
		                        <div style="text-align:center;">
			                        <h4><%= rb.getString("with_or_without_hints") %></h4>
		                        </div>	                       
		            		</div>
	                        <div class="row" style="margin-left:50px">
								<label><h3><%= rb.getString("live_goal") %></h3></label>&nbsp
								<input id="tt-live-goal" style="width:50px" type="text" name="" value="0">&nbsp   
								<button type="button" class="btn btn-primary btn-small" onclick='$("#tt-live-goal-msg").show();'><%= rb.getString("set_a_new_goal")%></button>
								<label id="tt-live-goal-msg"><%= rb.getString("your_goal_will_be_ready")%></label>
								
		            		</div>
	                        <div class="row">
		                        <div id="classTotalProblems" class="tt-live-dashboard-box" style="width:300px; height:200px;"">
		                        	<div id="live-dashboard-content" >
										<h1 class="tt-live-dashboard-content"></h1>
									</div>
		                        </div>
		            		</div>
	                        <div class="row">
			            		<div id="live_dashboard_canvas" class="col-md-4 tt-Live_dashboard_canvas"></div>
		            		</div>
	                    </div>
	                    <div id="liveDashboardEffortPane" class="col-md-8 tt-LiveDashboardEffortPane">
	                        <div style="text-align:center;"> <h2><%= rb.getString("problem_solving_effort") %></h2></div>
	                        <div class="row">	                        	
	                        	<div class="col-md-2"></div>
	                        	<div class="col-md-2">
									<button type="button" class="btn btn-primary btn-small" id="live-dashboard-effort-button" onclick="showReport3d();"><%= rb.getString("refresh_chart") %></button>
								</div>
							</div>
	                        <div class="row">
				            	<div id="chart3d_canvas" class="col-md-4 float-left" style="margin-top:50px; width:400px; height:400px;"></div> 
				            	<div id="chart3d_legend" class="col-md-5 float-left" style="margin-top:20px";> 		            	
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

             <div id="live-garden-loader" class="loader" style="display: none" ></div>               
             <div id="live-garden" class="container-fluid" style="display:none;width: 100%; overflow-x: scroll;">
               	<div class="row">
	             	<div class="col-md-12">
		            	<div class= "tt-garden-header">
		            	    <table align = "left"	id="liveGarden_header_table" border="1">
	            	    		<thead id="live-garden-thead">
  								</thead>
  							</table>
		            	</div> 
	                </div>
            	</div>
               	<div class="row">
	             	<div class="col-md-12">
		            	<div id="liveGarden_table_panel" class= "tt-garden-body">
		            	    <table align = "left"	id="liveGarden_body_table" border="1">
	            	    		<tbody id="live-garden-tbody">
	            	    		</tbody>
  							</table>
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
                <div class="panel-group" id="accordion">
 		             <div id="landing-report-loader" class="loader" style="display: none" ></div>               
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">
                                <a id="reportTwo" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#classLandingReportTwo">
				                    <%= rb.getString("landing_report2_title") %>
                                </a>
                                <button id="landingPageReportButton2" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h3>
                        </div>
                        <div class="panel-body report_filters">
                        	<div id="chooseDateRange" class="row">
                        		<div class="col-md-2 offset-md-1">                       
				                	<button type="button" class="btn btn-primary" onclick="initCalendar();initCalendar_cal2();$('#calendarModalPopup').modal('show');" ><%= rb.getString("choose_date_range") %></button>
				                </div>
                        		<div class="col-md-3">                       
								    <input id="daysFilterLanding2" style="width:220px" type="text" name="" value="" >   
				                </div>
				                
 							</div>  

						</div>
                        <div id="classLandingReportTwo" class="panel-collapse collapse">
                            <div class="panel-body">
                                <table id="landingPageReport2" class="table table-striped table-bordered hover" width="100%"></table>
                            </div>

                        </div>
                    </div>                    
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">
                                <a id="reportOne" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#classLandingReportOne">
				                    <%= rb.getString("recent_student_activities") %>
                                </a>
                                <button id="landingPageReportButton1" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h3>
                        </div>
                        <div class="panel-body report_filters">
                        	<div id="lastXDays" class="row">                         
							  <label class="report_filters" ><%= rb.getString("show_only_last") %></label>
							  <input id="daysFilterLandingOne" style="width:32px" type="text" name="" value="7">   
							  <label class="report_filters"><%= rb.getString("days") %>.  </label>
							</div>  
						</div>
                        <div id="classLandingReportOne" class="panel-collapse collapse">
                            <div class="panel-body">
                                <table id="landingPageReport1" class="table table-striped table-bordered hover" width="100%"></table>
                            </div>

                        </div>
                       <div class="panel-body report_note"">                           
							  <label class="report_note"" ><%= rb.getString("landing_report_note1") %></label>
							  <label class="report_note"" ><%= rb.getString("landing_report_note2") %></label>
						</div>
 
                    </div>
                </div>            
          	

                </div>            
			    <div>
                    <h3 class="tt-page-header">
                    <%= rb.getString("select_report_card_from_menu") %>
                    </h3>
                </div>
        	</div>
	</div>
</div>

<div id = "statusMessage" class="spin-loader-message" align = "center" style="display: none;"></div>


<!-- Modal For Mastery Trajecotory Report-->
<div id="calendarModalPopup" class="modal fade" data-backdrop="static" data-keyboard="false" role="dialog" style="display: none;">
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
                        <input type="hidden" id="selectDay" name="selectDay">
   				      <div><h3><%= rb.getString("most_recent") %>:</h3></div>
			          <div class="button-container-calendar">
			              <div class=col-md-2><button id="previous" onclick="previous()">&#8249;&#8249;</button></div>
       							  <div class=col-md-8 center-text><h3 id="monthAndYear"></h3></div>
			              <div class=col-md-2><button id="next" onclick="next()">&#8250;&#8250;</button></div>							          
			          </div>
			          
			          <table class="table-calendar" id="calendar" data-lang="en">
			              <thead id="thead-month"></thead>
			              <tbody id="calendar-body"></tbody>
			          </table>
			          
			          <div class="footer-container-calendar">
			              <label for="month"><%= rb.getString("jump_to") %>: </label>
			              <select id="month" onchange="jump()">
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
			              <select id="year" onchange="jump()">
			                  <option value=2020>2020</option>
			                  <option value=2021>2021</option>
			                  <option value=2022>2022</option>			              
			                  <option value=2023>2023</option>			              
			              </select>       
			          </div>
			      </div>			      
			    </div> 
			    <div class="wrapper-calender col-sm-6">
			      <div class="container-calendar">
                        <input type="hidden" id="selectDay_cal2" name="selectDay_cal_2">
				      <div><h3><%= rb.getString("least_recent") %>:</h3></div>
			          <div class="button-container-calendar">
			              <div class=col-md-2><button id="previous_cal2" onclick="previous_cal2()">&#8249;&#8249;</button></div>
       							  <div class=col-md-8 center-text><h3 id="monthAndYear_cal2"></h3></div>
			              <div class=col-md-2><button id="next_cal2" onclick="next_cal2()">&#8250;&#8250;</button></div>							          
			          </div>
			          
			          <table class="table-calendar" id="calendar_cal2" data-lang="en">
			              <thead id="thead-month_cal2"></thead>
			              <tbody id="calendar-body_cal2"></tbody>
			          </table>
			          
			          <div class="footer-container-calendar">
			              <label for="month_cal2"><%= rb.getString("jump_to") %>: </label>
			              <select id="month_cal2" onchange="jump_cal2()">
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
			              <select id="year_cal2" onchange="jump_cal2()">
			                  <option value=2020>2020</option>
			                  <option value=2021>2021</option>
			                  <option value=2022>2022</option>			              
			                  <option value=2023>2023</option>			              
			              </select>       
			          </div>			 
			        </div>
            	</div>
            </div>
            </div>
           <div class="modal-footer">

          		<div class="offset-md-6">
	                <button type="button" class="btn btn-success" onclick="getFilterLandingTwo();" ><%= rb.getString("submit") %></button>
	                <button type="button" class="btn btn-danger" data-dismiss="modal" onclick="$('#calendarModalPopup').modal('hide');" ><%= rb.getString("cancel") %></button>
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
                <div id="problemSnapshot"></div>
                <canvas id="studentEffortRecordedProblemCanvas" width='900' height='150'></canvas>
                <div id="lengendTable"></div>
            </div>

        </div>
    </div>
</div>
<!-- Modal -->

<div id="resetStudentDataModalPopup" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title"><%= rb.getString("are_you_sure_continue") %></h4>
            </div>
            <div class="modal-body alert alert-primary" role="alert">
                <%= rb.getString("some_text_in_modal") %>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" data-dismiss="modal" onclick="resetStudentData();" ><%= rb.getString("yes") %></button>
                <button type="button" class="btn btn-danger" data-dismiss="modal"><%= rb.getString("no") %></button>
            </div>
        </div>

    </div>
</div>

<div id="deleteInactiveStudentsModalPopup" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title"><%= rb.getString("are_you_sure_continue") %></h4>
            </div>
            <div class="modal-body alert alert-primary" role="alert">
                <%= rb.getString("some_text_in_modal") %>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" data-dismiss="modal" onclick="deleteInactiveStudents();" ><%= rb.getString("yes") %></button>
                <button type="button" class="btn btn-danger" data-dismiss="modal"><%= rb.getString("no") %></button>
            </div>
        </div>

    </div>
</div>


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
        <div class="pspp-modal-content perStudentperProblem-modal-content">
            <div class="pspp-modal-header">
            	<span id="perStudentPerProblemImageHdr" class="modal-title"></span></div>
            <div>
            	<div id="perStudentPerProblemImage" ></div>
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

<!-- Modal Popup fireworks -->
<div id="ModalPopupFireworks" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog modal-dialog-centered">
        <!-- Modal content-->
        <div class="fireworks-modal-content ">
            	<div id="fireworksContent" >
					<div id="fireworksImage"><img src="../../images/Fireworks_1000.gif" /></div>
	            </div>
            </div>
        </div>
        <div class="fireworks-modal-header">
        	<span id="fireworksImageHdr" class="modal-title mx-auto style="width:300px"><h3><%= rb.getString("hooray") %>&nbsp<%= rb.getString("we_reached_our_goal") %></h3></span></div>
        <div>

    </div>
</div>
<!-- Modal -->


<!-- Modal -->
    <script type="text/javascript" src="<c:url value="/js/calendar.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/calendar2.js" />"></script>
</body>
</html>