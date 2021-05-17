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

</style>
 
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
var filterLandingOne = "~7";
var filterLandingTwo = "~0";

var emsg_classLanguage   = 'Class language is mandatory field';
var emsg_className       = 'Class name is mandatory field';
var emsg_className_invalid = 'Class name must only include letters,numbers or . _ - characters';
var emsg_classGrade      = 'Class grade is mandatory field';
var emsg_lowEndDiff      = 'Grade level of problems - Lower is mandatory field';
var emsg_highEndDiff     = 'Grade level of problems - Higher is mandatory field';
var emsg_town            = 'Town name is mandatory field';
var emsg_schoolName      = 'School name is mandatory field';
var emsg_schoolYearRange = 'The academic year should not be greater than 2050 and less than current year';
var emsg_schoolYear      = 'School year is a mandatory field';
var emsg_gradeSection    = 'Section name is a mandatory field';
var emsg_maxProbRange    = 'The Max Problems should not be greater than 40 and less than 2';
var emsg_maxProb         = 'Max Problems is a mandatory field';
var emsg_minProbRange    = 'The Min Problems should not be greater than 40 and less than 2';
var emsg_minProb         = 'Min Problems is a mandatory field';
var emsg_maxTimeRange    = 'The Max Time should not be greater than 30 and less than 0';
var emsg_maxTime         = 'Max Time is a mandatory field';
var emsg_minTimeRange    = 'The Min Time should not be greater than 30 and less than 0';
var emsg_minTime         = 'Min Time is a mandatory field';

var languagePreference = window.navigator.language;
var languageSet = "en";
var loc = "en-US";

if (languagePreference.includes("en")) {
	languageSet = "en"
	loc = "en-US";
} else if (languagePreference.includes("es")) {
	languageSet = "es"
	loc = "es-Ar";
	emsg_classLanguage   = 'El lenguaje de la clase es obligatorio';
	emsg_className       = 'El nombre de la clase es obligatorio';
	var emsg_className_invalid = 'El nombre de la clase solo debe incluir letras, números o . _ - ';
	emsg_classGrade      = 'El grado de la clase es obligatorio';
	emsg_lowEndDiff      = 'El grado de problemas: bajo es obligatorio';
	emsg_highEndDiff     = 'El grado de problemas: mayor es obligatorio';
	emsg_town            = 'El nombre de la ciudad es obligatorio';
	emsg_schoolName      = 'El nombre de la escuela es obligatorio';
	emsg_schoolYearRange = 'El año académico no debe ser mayor que 2050 y menor que el año actual';
	emsg_schoolYear      = 'El año escolar es obligatorio';
	emsg_gradeSection    = 'El nombre de la sección es obligatorio';
	emsg_maxProbRange    = 'The Max Problems should not be greater than 40 and less than 2';
	emsg_maxProb         = 'Max Problems is a mandatory field';
	emsg_minProbRange    = 'The Min Problems should not be greater than 40 and less than 2';
	emsg_minProb         = 'Min Problems is a mandatory field';
	emsg_maxTimeRange    = 'The Max Time should not be greater than 30 and less than 0';
	emsg_maxTime         = 'Max Time is a mandatory field';
	emsg_minTimeRange    = 'The Min Time should not be greater than 30 and less than 0';
	emsg_minTime         = 'Min Time is a mandatory field';
}


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
	filterLandingOne = daysLandingOne;
}

function getFilterLandingTwo() {
	
	
	var d1 = parseInt(document.getElementById("selectDay_cal2").value);
	var d2 =  parseInt(document.getElementById("selectDay").value);

	var m1 = parseInt(document.getElementById("month_cal2").value) + 1;
	var m2 =  parseInt(document.getElementById("month").value) + 1;
	
	if ((d1 > 0) && (d2 > 0)) {
		$('#calendarModalPopup').modal('hide');

		var fromDate = m1 + "/" + document.getElementById("selectDay_cal2").value + "/" +  document.getElementById("year_cal2").value;
		var toDate = m2 + "/" + document.getElementById("selectDay").value + "/" + document.getElementById("year").value;

		if (languageSet == "es") {
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
		filterLandingTwo = "~" + fromDate + "thru" + toDate;
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
var teacherClassSelections = "";

function moveThisStudent(id) {

	var newClassId = document.getElementById('moveBtn').value;
    $('#student_info_out').find('.loader').show();
	$.ajax({
        type : "POST",
        url :pgContext+"/tt/tt/changeStudentClass",
        data : {
        	studentId:id,
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
	    },
	    error : function(e) {
	        console.log(e);
	    }
    });
}



function selectTeacherClass(myClassId) {



	var txtClassId = "" + myClassId;
	for (var i=0; i < classChoices.length; i++) {	
		var splitter = classChoices[i].split(',');
		if (splitter[2] === txtClassId) {
			document.getElementById('moveBtn').value = splitter[2];			
			document.getElementById('moveBtn').innerHTML = '<%= rb.getString("move_this_student_to")%>: ' + splitter[1];
			break;
		}
	}
	
	$("#moveBtn").show();
}

var classChoices = [];

function addToTeacherList(item, index) {
    
	var titem = "" + item;
	var tlist = titem.split(",");

	if (tlist[2] != classID) {
		classChoices.push(titem);	
		teacherClassSelections = teacherClassSelections +  "<li id='Class" + tlist[2] + "' class='dropdown-content' value='" + tlist[2] + "' onclick='selectTeacherClass(" + tlist[2] + ");'>" + tlist[1] + "</li>";
	}
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

	    $('#problem_set_content').find('.loader').show();
        $.ajax({
            type : "POST",
            url :pgContext+"/tt/tt/saveChangesForProblemSet",
            data : {
                problemIds: problemIds,
                classid: classID,
                problemsetId: JSONData["problemLevelId"]
            },
            success : function(response) {
        	    $('#problem_set_content').find('.loader').hide();
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

	    $('#problem_set_content').find('.loader').show();
	    $.ajax({
            type : "POST",
            url :pgContext+"/tt/tt/saveChangesForProblemSet",
            data : {
                problemIds: problemIds,
                classid: classID,
                problemsetId: JSONData["problemLevelId"]
            },
            success : function(response) {
        	    $('#problem_set_content').find('.loader').hide();
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
var summaryLabel="<%= rb.getString("summary")%>"; 
var higherlevelDetail = "<div id=" + data[0] + " class='panel-body animated zoomOut'> " +
    " <div class='panel panel-default'> <div class='panel-body'><strong>"+higherlevelDetailp1+": " + JSONData["topicName"] + "</strong></div> " +
    " <div class='panel-body'><strong>"+higherlevelDetailp2+": " + html + "</strong></div>" +
    " <div class='panel-body'><strong>"+summaryLabel+": " + JSONData["topicSummary"] + "</strong></div>"+
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
        var tnickname = obj.nickName;
        if (tnickname == null) {
        	tnickname = "";
        }
        if (tnickname.length > 94) {
        	tnickname = tnickname.substr(0,90) + "...";
        }
        tableHeader +=  checkBox+
            "<td>"+obj.id+"</td>"+
            flash+
            "<td>"+tnickname+"</td>"+
            "<td>"+obj.difficulty+"</td>"+
            "<td>"+html+"</td>"+
            "<td>"+obj.type+"</td></tr>";
    });
    return tableHeader + "</tbody></table></div>";
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
                        message: emsg_className
                    },
			        regexp: {
            			regexp: /^[a-zA-Z0-9_\-\.]+$/,
                        message: emsg_className_invalid
        			}        
                }
            },
            classGrade: {
                validators: {
                    notEmpty: {
                        message: emsg_classGrade
                    }
                }
            },
            lowEndDiff: {
                validators: {
                    notEmpty: {
                        message: emsg_lowEndDiff
                    }
                }
            }, highEndDiff: {
                validators: {
                    notEmpty: {
                        message: emsg_highEndDiff
                    }
                }
            }, town: {
                validators: {
                    notEmpty: {
                        message: emsg_town
                    }
                }
            }, schoolName: {
                validators: {
                    notEmpty: {
                        message: emsg_schoolName
                    }
                }
            }, schoolYear: {
                validators: {

                    between: {
                        min: new Date().getFullYear(),
                        max: 2050,
                        message: emsg_schoolYearRange
                    },

                    notEmpty: {
                        message: emsg_schoolYear
                    }
                }
            }, gradeSection: {
                validators: {
                    notEmpty: {
                        message: emsg_gradeSection
                    }
                }
            }, maxProb: {
                validators: {

                    between: {
                        min: 2,
                        max: 40,
                        message: emsg_maxProbRange
                    },

                    notEmpty: {
                        message: emsg_maxProb
                    }
                }
            }, minProb: {
                validators: {

                    between: {
                        min: 2,
                        max: 40,
                        message: emsg_minProbRange
                    },

                    notEmpty: {
                        message: emsg_minProb
                    }
                }
            }, maxTime: {
                validators: {

                    between: {
                        min: 0,
                        max: 30,
                        message: emsg_maxTimeRange
                    },

                    notEmpty: {
                        message: emsg_maxTime
                    }
                }
            }, minTime: {
                validators: {

                    between: {
                        min: 0,
                        max: 30,
                        message: emsg_minTimeRange
                    },

                    notEmpty: {
                        message: emsg_minTime
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
	
    $("#classHomePage").click(function () {
        $('#reorg_prob_sets_handler').css('color', '#ffffff');
        $("#content-conatiner").children().hide();
        
        $("#splash_page").show();
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

    $("#manage_roster_handler").click(function () {
    	$('#reorg_prob_sets_handler').css('background-color', '');
        $('#reorg_prob_sets_handler').css('color', '#dddddd');

        $("#content-conatiner").children().hide();
        $("#student_roster_out").show();
    });
    
    $("#manage_student_info_handler").click(function () {
    	$('#reorg_prob_sets_handler').css('background-color', '');
        $('#reorg_prob_sets_handler').css('color', '#dddddd');

        $("#content-conatiner").children().hide();
        $("#student_info_out").show();
    });
    
    $("#manage_class_handler").click(function () {  
        $('#reorg_prob_sets_handler').css('background-color', '');
        $('#reorg_prob_sets_handler').css('color', '#dddddd');

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
        $('#reorg_prob_sets_handler').css('color', '#ffffff');
        $("#content-conatiner").children().hide();
        $("#splash_page").show();
    });


    $('#activateProbSetTable input[type="checkbox"]').click(function () {
        if ($('#activateProbSetTable input[type="checkbox"]:checked').size()) {
            $('#deactivateProblemSets').prop('disabled', false);
        } else {
            $('#deactivateProblemSets').prop('disabled', true);
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
            $('#activateProblemSets').prop('disabled', false);
        } else {
            $('#activateProblemSets').prop('disabled', true);
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

function changeLandingPageHeaderAccordingToLanguage(){
	var languagePreference = window.navigator.language;
	var languageSet = "en";
	if (languagePreference.includes("en")) {
		languageSet = "en"
	} else if (languagePreference.includes("es")) {
		languageSet = "es"
	}
	if (languageSet == 'es') {
		var header = {'sid':  'Numero Identificador del alumno','sname': 'Nombre del  alumno','uname':  'Nombre de usuario','problems': 'Número de problemas vistos','timeInMS': 'Tiempo resolviendo problemas (minutos)','latestLogin': 'Inicio de sesión más reciente'};
		return header;
	}else{
	 	var header = {'sid':  'Student ID','sname': 'Student Name','uname':  'Username','problems': 'Number of problems seen','timeInMS': 'Time solving problems (minutes)','latestLogin': 'Most recent login'};
	 	return header;
	}
}

function changeLandingPageHeader2AccordingToLanguage(){
	var languagePreference = window.navigator.language;
	var languageSet = "en";
	if (languagePreference.includes("en")) {
		languageSet = "en"
	} else if (languagePreference.includes("es")) {
		languageSet = "es"
	}
	if (languageSet == 'es') {
		var header = {'sid':  'Numero Identificador del alumno','sname': 'Nombre del  alumno','uname':  'Nombre de usuario','problems': 'Número de problemas vistos','timeInMS': 'Tiempo resolviendo problemas (minutos)','latestLogin': 'Inicio de sesión más reciente'};
		return header;
	}else{
	 	var header = {'sid':  'Student ID','sname': 'Student Name','uname':  'Username','problems': 'Number of problems seen','timeInMS': 'Time solving problems (minutes)','latestLogin': 'Most recent login'};
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

    var headers = changeLandingPageHeaderAccordingToLanguage();
    var headers2 = changeLandingPageHeader2AccordingToLanguage();
  
    if (languageSet == 'es') {
    
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
    }
    else {
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
    }
    
    $('#classLandingReportOne').on('show.bs.collapse', function ()  {

    	
    	getFilterLandingOne(); 
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


    if (languageSet == 'es') {
    
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
    }
    else {
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
                                return '<img  style="max-width:400px; max-height:400px;" src="' + $(this).data('img') + '" />';
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
	    $('#problem_set_content').find('.loader').show();
        $.ajax({
            type : "POST",
            url :pgContext+"/tt/tt/reOrderProblemSets",
            data : {
                problemSets: result,
                classid: classID
            },
            success : function(response) {
        	    $('#problem_set_content').find('.loader').hide();
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
    	    $('#problem_set_content').find('.loader').show();
            $.ajax({
                type : "POST",
                url :pgContext+"/tt/tt/getProblemForProblemSets",
                data : {
                    problemID: row.data()[3],
                    classid: classID
                },
                success : function(response) {
            	    $('#problem_set_content').find('.loader').hide();
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
                                return '<img style="max-width:400px; max-height:400px;" src="' + $(this).data('img') + '" />';
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

    $("#deactivateProblemSets").click(function () {
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
	    $('#problem_set_content').find('.loader').show();
        $.ajax({
            type : "POST",
            url :pgContext+"/tt/tt/configureProblemSets",
            data : {
                activateData: activateData,
                classid: classID,
                activateFlag: 'deactivate'
            },
            success : function(response) {
        	    $('#problem_set_content').find('.loader').hide();
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


    $("#activateProblemSets").click(function () {
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
        var newlocation = pgContext+'/tt/tt/viewClassDetails?classId='+classID+'&currentSelection=reorg_prob_sets_handler';
        $(location).attr('href', newlocation);
    });
    $("#successMsgModelPopupForProblemSets").find("[class*='close']").click(function () {
        var newlocation = pgContext+'/tt/tt/viewClassDetails?classId='+classID+'&currentSelection=reorg_prob_sets_handler';
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
        $(document).ready(function () {
        	generate_year_range(2020,2022);
            registerAllEvents();
            handleclickHandlers();

            $("#content-conatiner").children().hide();

            if (currentSelection == "classHomePage") {
                $("#splash_page").show();            	
            	$("#classLandingReportOne").collapse('show');
            }
            else if (currentSelection == "reorg_prob_sets_handler") {
                    $('#reorg_prob_sets_handler').click();
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

    function editStudentInformation(id,fname,lname,uname,context){

    	
        $.ajax({
            type : "POST",
            url : pgContext+"/tt/tt/getTeacherReports",
            data : {
            	classId: classID,
                teacherId: teacherID,
                reportType: 'teacherClassList',
                lang: loc,
                filter: "~"

            },
            success : function(data) {
            	    if (data) {
            	    	teacherClassSelections = "";
            	    	$("#moveBtn").hide();
            	    	var msg = "" + data;
                       	var jsonData = $.parseJSON(data);
       	                eachTeacherListData = jsonData.levelOneData;
       	                eachTeacherListData.forEach(addToTeacherList);		           	             	
       	             	document.getElementById("teacherList").innerHTML = "<ul>" + teacherClassSelections + "</ul>";
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
	
    	
    var tr = context.closest('tr')
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

        var formHtml = '<div class="panel-body"><form id="edit_Student_Form'+id+'" onsubmit="event.preventDefault();"><div class="form-group"><div class="input-group"><label for="studentId"><%= rb.getString("user_id") %></label></div><div class="input-group">'+
            '<input type="text" value='+id+' id="studentId" class="form-control" name="studentId" disabled="disabled" /></div></div>'+tempStudentUserName
            + tempStudentName + tempStudentLastName +
            '<div class="input-group"><button role="button" onclick="updateStudentInfo('+id+')" class="btn btn-primary"><%= rb.getString("update_information") %></button></div></form></div>';

        var formHtmlPassWord = '<div class="panel-body"><form id="resetPasswordfor'+id+'" onsubmit="event.preventDefault();"><div class="form-group"><div class="input-group"><label for="newPassword"><%= rb.getString("new_password") %></label></div><div class="input-group">'+
            '<input type="password"  placeholder="New password to be set" id="newPassword" class="form-control" name="newPassword"/></div></div>' +
            '<div class="input-group"><button role="button" onclick="resetPassWordForThisStudent('+id+',\'' + uname + '\')" type="button" class="btn btn-primary"> <%= rb.getString("reset_password") %></button></div></form></div>';


        var formHtmlChangeClass = '<div class="panel-body"><form id="ChangeClassfor'+id+'" onsubmit="event.preventDefault();"><div class="form-group"><div class="input-group"><label for="newClass">New Class</label></div><div id="teacherList"></div><div class="input-group"><button id=moveBtn role="button" onclick="moveThisStudent('+id+')" type="button" class="btn btn-primary"></button></div></form></div>';    
            
        var tabPanel = '<div style="width: 70%"> <ul class="nav nav-tabs" role="tablist"> <li class="active"> ' +
            '<a href="#home'+id+'" role="tab" data-toggle="tab"> <%= rb.getString("update_student_information") %> </a> </li> ' +
            '<li><a href="#profile'+id+'" role="tab" data-toggle="tab"> <%= rb.getString("reset_password_for_student") %> </a> </li> '+
            '<li><a href="#changeClass'+id+'" role="tab" data-toggle="tab"> <%= rb.getString("move_student_to_different_class") %> </a> </li> </ul>'+
            '<div class="tab-content"> <div class="tab-pane fade active in" id="home'+id+'">'+formHtml+'</div><div class="tab-pane fade" id="profile'+id+'">'+formHtmlPassWord+'</div> <div class="tab-pane fade" id="changeClass'+id+'">'+formHtmlChangeClass+'</div> </div> </div>';

        row.child(tabPanel).show();
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
                        <a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/logout"><i
                                class="fa fa-fw fa-power-off"></i><%= rb.getString("log_out") %></a>
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
            <li>
                <a id="classHomePage" href="#"><i class="fa fa-fw fa-home"></i> <%= rb.getString("class_home") %></a>
            </li>

            <li><a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/viewClassReportCard?classId=${classInfo.classid}&currentSelection=classReportCard" id="report_card"><i class="fa fa-bar-chart"></i> <%= rb.getString("class_report_card") %></a></li>

            <li><a id="reorg_prob_sets_handler"><i class="fa fa-list"></i> <%= rb.getString("manage_problem_sets") %></a></li>

            <li><a id="manage_roster_handler"><i class="fa fa-fw fa-users"></i> <%= rb.getString("manage_class_roster") %></a></li>

            <li><a id="manage_student_info_handler"><i class="fa fa-fw fa-users"></i> <%= rb.getString("manage_student_info") %></a></li>

            <li><a id="manage_class_handler"><i class="fa fa-fw fa-cog"></i> <%= rb.getString("manage_class") %></a></li>

             <li id="resetSurveySettings"><a id="resetSurveySettings_handler"><i class="fa fa-fw fa-cog"></i><%= rb.getString("survey_settings") %></a></li>
            
             <li><a id="content_apply_handler"><i class="fa fa-fw fa-cogs"></i><%= rb.getString("apply_class_content") %></a></li>

        </ul>
        <!-- /#sidebar-end -->
    </nav>
    <div id="page-content-wrapper">

        <h1 class="page-header">
            <%= rb.getString("home_page_for_class") %>: <strong>${classInfo.name}</strong>&nbsp; [<%= rb.getString("class_code") %>:${classInfo.classid}]
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

				<div id="problem_set_content" style="width: 100%;">
                <div class="loader" style="display: none" ></div>               
			<input type="hidden" id="activeproblemSetSize" name="activeproblemSetSize" value="${activeproblemSet.size()}">
			<input type="hidden" id="inactiveproblemSetSize" name="inactiveproblemSetSize" value="${inactiveproblemSets.size()}">
			<c:if test="${activeproblemSet.size() != 0}">
                <div>
                    <h3 class="tt-page-header">
                        <small><%= rb.getString("active_problem_sets") %></small>
                    </h3>

                    <div class="panel panel-default">
                        <div class="panel-body"><%= rb.getString("active_problem_sets_note1") %>
                        </div>
                        <div class="panel-body"><%= rb.getString("active_problem_sets_note2") %>
                        </div>
                        <div class="panel-body">
                            <button id="deactivateProblemSets" class="btn btn-primary btn-lg" aria-disabled="true" disabled="disabled"><%= rb.getString("deactivate_problem_sets") %></button>
                        </div>
                    </div>

                    <table id="activateProbSetTable" class="table table-striped table-bordered hover" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th rowspan="2" align="center"><span>Order&nbsp;&nbsp;</span><a rel="popoverOrder"><i class="fa fa-question-circle-o" aria-hidden="true"></i></a></th>
                            <th rowspan="2"><%= rb.getString("problem_set") %></th>
                            <th rowspan="2"><span><%= rb.getString("number_of_activated_problems") %>&nbsp;&nbsp;</span><a rel="popoveractivatedProblems"><i class="fa fa-question-circle-o" aria-hidden="true"></i></a></th>
                            <th rowspan="2"><%= rb.getString("problem_id") %></th>
                            <th style="text-align: center;" colspan="<c:out value='${activeproblemSetHeaders.size()}'/>"><%= rb.getString("gradewise_distribution") %></th>
                            <th rowspan="2"><%= rb.getString("deactivate_problem_set") %></th>
                        </tr>

                        <tr>
                            <c:forEach var="problemSetHeaders" items="${activeproblemSetHeaders}">
                                <th style="border-right-width: 1px;">${problemSetHeaders.key}</th>
                            </c:forEach>
                        </tr>

                        </thead>
                        <tbody>
                        <c:forEach var="problemSet" varStatus="i" items="${activeproblemSet}">
                        	<c:if test="${problemSet.numProbs > 0}">
	                            <c:set var="gradeWiseProbNos" value="${problemSet.gradewiseProblemDistribution}"/>
	                            <tr>
	                                <td>${i.index + 1}</td>
	                                <td>${problemSet.name}&nbsp;&nbsp;<a rel="popoverproblemsetSummary" data-content='ID=${problemSet.id} ${problemSet.summary}'><i class="fa fa-question-circle-o" aria-hidden="true"></i></a></td>
	                                <td>
	                                    <label style="width: 50%;">${problemSet.numProbs}</label>
	                                    <a  class="active" aria-expanded="true" aria-controls="collapseOne">
	                                        <i class="glyphicon glyphicon-menu-down"></i>
	                                    </a>
	                                </td>
	                                <td>${problemSet.id}</td>
	                                <c:forEach var="problemSetHeaders" items="${activeproblemSetHeaders}">
	                                    <td><c:out value="${gradeWiseProbNos[problemSetHeaders.key]}"/></td>
	                                </c:forEach>
	                                <td></td>
	                            </tr>
                            </c:if>
                        </c:forEach>
                        </tbody>
                       
                    </table>
					 
                </div>
				</c:if>
				<c:if test="${activeproblemSet.size() == 0}">
				 <div>
                    <h5 class="tt-page-header">
                        <big><%= rb.getString("no_active_problem_sets_found") %></big>
                    </h5>
					</div>
				</c:if>
				<c:if test="${inactiveproblemSets.size() != 0}">
                <div>
                    <h3 class="tt-page-header">
                        <small><%= rb.getString("inactive_problem_sets") %></small>
                    </h3>

                    <div class="panel panel-default">
                        <div class="panel-body"><%= rb.getString("deactive_problem_sets_note1") %>
                        </div>
                        <div class="panel-body">
                            <button id="activateProblemSets" class="btn btn-primary btn-lg" disabled="disabled" aria-disabled="true"><%= rb.getString("activate_problem_sets") %></button>
                        </div>
                    </div>

                    <table id="inActiveProbSetTable" class="table table-striped table-bordered hover" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th rowspan="2"><%= rb.getString("order") %></th>
                            <th rowspan="2"><%= rb.getString("problem_set") %></th>
                            <th rowspan="2"><%= rb.getString("available_problems") %></th>
                            <th rowspan="2"><%= rb.getString("problem_id") %></th>
                            <th style="text-align: center;" colspan="<c:out value='${inActiveproblemSetHeaders.size()}'/>"><%= rb.getString("gradewise_distribution") %></th>
                            <th rowspan="2"><%= rb.getString("activate_problem_sets") %></th>
                        </tr>
                        <tr>
                            <c:forEach var="problemSetHeaders" items="${inActiveproblemSetHeaders}">
                                <th style="border-right-width: 1px;">${problemSetHeaders.key}</th>
                            </c:forEach>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="problemSet" varStatus="i" items="${inactiveproblemSets}">
                        	<c:if test="${problemSet.numProbs > 0}">
                            	<c:set var="gradeWiseProbNo" value="${problemSet.gradewiseProblemDistribution}"/>
	                            <tr>
	                                <td>${i.index + 1}</td>
	                                <td>${problemSet.name}&nbsp;&nbsp;<a rel="popoverproblemsetSummary" data-content='ID=${problemSet.id} ${problemSet.summary}'><i class="fa fa-question-circle-o" aria-hidden="true"></i></a></td>
	                               <td>
	                                   ${problemSet.numProbs}
	                                </td>
	                                <td>${problemSet.id}</td>
	                                <c:forEach var="problemSetHeaders" items="${inActiveproblemSetHeaders}">
	                                    <td><c:out value="${gradeWiseProbNo[problemSetHeaders.key]}"/></td>
	                                </c:forEach>
	                                <td></td>
	                            </tr>
                            </c:if>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
				</c:if>
				<c:if test="${inactiveproblemSets.size() == 0}">
				 <div>
                    <h5 class="tt-page-header">
                        <big><%= rb.getString("no_inactive_problem_sets_found") %></big>
                    </h5>
					</div>
				</c:if>

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
				                                                          placeholder="Math" class="form-control" type="text"/>
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
				                                            <springForm:option value="2020">2020</springForm:option>
				                                            <springForm:option value="2021">2021</springForm:option>
				                                            <springForm:option value="2022">2022</springForm:option>
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
				                                            <springForm:option value="9"><%= rb.getString("grade") %> 9</springForm:option>
				                                            <springForm:option value="10"><%= rb.getString("grade") %> 10</springForm:option>
				                                            <springForm:option value="adult"><%= rb.getString("adult") %></springForm:option>
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
				                                                           name="highEndDiff">
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

             <div id="splash_page" style="display:none;width: 100%;">
             	<div>
                    <h3 class="tt-page-header">
                    <%= rb.getString("select_activities_from_menu") %>
                    </h3>
                </div>
                <div class="panel-group" id="accordion">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="reportOne" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#classLandingReportOne">
				                    <%= rb.getString("recent_student_activities") %>
                                </a>
                                <button id="landingPageReportButton1" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
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
          	
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="reportTwo" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#classLandingReportTwo">
				                    <%= rb.getString("landing_report2_title") %>
                                </a>
                                <button id="landingPageReportButton2" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div class="panel-body report_filters">
                        	<div id="chooseDateRange" class="row">
                        		<div class="col-md-2 offset-md-1">                       
				                	<button type="button" class="btn btn-primary" onclick="initCalendar();initCalendar_cal2();$('#calendarModalPopup').modal('show');" ><%= rb.getString("choose_date_range") %></button>
				                </div>
 							</div>  

						</div>
                        <div id="classLandingReportTwo" class="panel-collapse collapse">
                            <div class="panel-body">
                                <table id="landingPageReport2" class="table table-striped table-bordered hover" width="100%"></table>
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



<!-- Modal -->
    <script type="text/javascript" src="<c:url value="/js/calendar.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/calendar2.js" />"></script>
</body>
</html>