<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>
<% 
/** 
* Frank 	08-03-21	Initial version 
* Frank		08-22-24	Issue # 781R7



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
  <title>MS Admin Workbench</title>
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

	<script src="https://cdn.plot.ly/plotly-2.9.0.min.js"></script>

<style>

.body {
	background-color: #e4f0f5;
}

.li-disabled {
    pointer-events:none; 
    opacity:0.6;         
}

.li-enabled {
    pointer-events:auto; 
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
  	background-color: #92DDA3;
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


.footer {
   position: fixed;
   left: 0;
   bottom: 0;
   width: 100%;
    background-color: #2ecc71;
   color: white;
   text-align: center;
}
</style>


<style>
.popup .popuptext {
visibility: hidden;
background-color: #555;
color: #fff;
text-align: center;
border-radius: 6px;
padding: 8px 0;
z-index: 1;
bottom: 125%;
left: 50%;
margin-left: -80px;
}
.popup .show {
visibility: visible;
-webkit-animation: fadeIn 1s;
animation: fadeIn 1s;
}
@-webkit-keyframes fadeIn {
from {opacity: 0;} 
to {opacity: 1;}
}
@keyframes fadeIn {
from {opacity: 0;}
to {opacity:1 ;}
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
 var problem_imageURL = 'https://s3.amazonaws.com/ec2-54-225-52-217.compute-1.amazonaws.com/mscontent/' + 'problemSnapshots/prob_';

 
var defaultColors = [ "#4bb2c5", "#EAA228", "#c5b47f", "#579575", "#839557", "#958c12", "#953579", "#4b5de4", "#d8b83f", "#ff5800", "#0085cc", "#c747a3", "#cddf54", "#FBD178", "#26B4E3", "#bd70c7"];



var plotProb1 = null;
var plotProb8 = null;

var filterOne = "~~";
var topicNameMapOne = new Map([]);
var problemNameMapOne = new Map([]);
var selectedTopicOne = "";
var selectedProblemOne = "";

var filterEight = "~~";
var topicNameMapEight = new Map([]);
var problemNameMapEight = new Map([]);
var selectedTopicEight = "";
var selectedProblemEight = "";

var eachProblemData = [];

var topicSelectionListOne = "";
function setSelectedTopicOne(topic) {
	selectedTopicOne = topic;		
	populateProblemSelectionListOne();
}
function populateTopicSelectionListOne() {

	var topicFilter = "English";

    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/msAdmin",
        data : {
            command: 'getTopicNamesList',
            lang: "",
            filter: topicFilter
        },
        success : function(data) {        
        	var topicData = $.parseJSON(data);
           	

            for (var i = 0; i < topicData.length; i++) {
            	topicNameMapOne.set(topicData[i].topicId, topicData[i].name);
            }
            topicSelectionListOne = "<select name='topics' id='topicsOne' size='10' style='width:400px' >"; 	
            for (var i = 0; i < topicData.length; i++) {
            	var name = "" + topicData[i].name;
        		topicSelectionListOne += "<option value='" + topicData[i].topicId  + "' onclick=setSelectedTopicOne('" + topicData[i].topicId + "');>" + name  + "</option>";
			}
        	topicSelectionListOne += "</select>";
        	document.getElementById("topicSelectionListOne").innerHTML=topicSelectionListOne;             
        },
        error : function(e) {
            console.log(e);
        }
	});
}
var topicSelectionListEight = "";
function setSelectedTopicEight(topic) {
	selectedTopicEight = topic;		
	populateProblemSelectionListEight();
}
function populateTopicSelectionListEight() {

	var topicFilter = "English";

    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/msAdmin",
        data : {
            command: 'getTopicNamesList',
            lang: "",
            filter: topicFilter
        },
        success : function(data) {        
        	var topicData = $.parseJSON(data);
           	

            for (var i = 0; i < topicData.length; i++) {
            	topicNameMapEight.set(topicData[i].topicId, topicData[i].name);
            }
            topicSelectionListEight = "<select name='topics' id='topicsEight' size='10' style='width:400px' >"; 	
            for (var i = 0; i < topicData.length; i++) {
            	var name = "" + topicData[i].name;
        		topicSelectionListEight += "<option value='" + topicData[i].topicId  + "' onclick=setSelectedTopicEight('" + topicData[i].topicId + "');>" + name  + "</option>";
			}
        	topicSelectionListEight += "</select>";
        	document.getElementById("topicSelectionListEight").innerHTML=topicSelectionListEight;             
        },
        error : function(e) {
            console.log(e);
        }
	});
}


var problemSelectionListOne = "";
function setSelectedProblemOne(problem) {
	selectedProblemOne = problem;
	showReportProb1();
}
function populateProblemSelectionListOne() {

	var problemFilter = "English";
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/msAdmin",
        data : {
            command: 'getProblemNamesList',
            lang: "",
            filter: selectedTopicOne
        },
        success : function(data) {        
        	var problemData = $.parseJSON(data);          	
            for (var i = 0; i < problemData.length; i++) {
            	problemNameMapOne.set(problemData[i].problemId, problemData[i].name);
            }
            problemSelectionListOne = "<select name='problems' id='problemsOne' size='10' style='width:500px;overflow-x: auto;' >"; 	
            for (var i = 0; i < problemData.length; i++) {
        		problemSelectionListOne += "<option value='" + problemData[i].problemId  + "' onclick=setSelectedProblemOne('" + problemData[i].problemId + "');>" + problemData[i].name + " : " + problemData[i].nickname + "</option>";
            }
        	problemSelectionListOne += "</select>";
        	document.getElementById("problemSelectionListOne").innerHTML=problemSelectionListOne; 
            
        },
        error : function(e) {
            console.log(e);
        }
	});

	
}


var problemSelectionListEight = "";
function setSelectedProblemEight(problem) {
	selectedProblemEight = problem;
}

function populateProblemSelectionListEight() {

	var problemFilter = "English";
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/msAdmin",
        data : {
            command: 'getProblemNamesList',
            lang: "",
            filter: selectedTopicEight
        },
        success : function(data) {        
        	var problemData = $.parseJSON(data);          	
            for (var i = 0; i < problemData.length; i++) {
            	problemNameMapEight.set(problemData[i].problemId, problemData[i].name);
            }
            problemSelectionListEight = "<select name='problems' id='problemsEight' size='10' style='width:500px;overflow-x: auto;' >"; 	
            for (var i = 0; i < problemData.length; i++) {
        		problemSelectionListEight += "<option value='" + problemData[i].problemId  + "' onclick=setSelectedProblemEight('" + problemData[i].problemId + "');>" + problemData[i].name + " : " + problemData[i].nickname + "</option>";
            }
        	problemSelectionListEight += "</select>";
        	document.getElementById("problemSelectionListEight").innerHTML=problemSelectionListEight; 
            
        },
        error : function(e) {
            console.log(e);
        }
	});

	
}

function getFilterEight(submit) {
	
	//document.getElementById("daysFilterEight").value = "";
		
	filterEight = "~~";

	var d1 = parseInt(document.getElementById("selectDay_r8_cal2").value);
	var d2 =  parseInt(document.getElementById("selectDay_r8_cal1").value);

	var m1 = parseInt(document.getElementById("month_r8_cal2").value) + 1;
	var m2 =  parseInt(document.getElementById("month_r8_cal1").value) + 1;
	
	if ((d1 > 0) && (d2 > 0)) {
		$('#calendarModalPopupEight').modal('hide');

		var fromDate = m1 + "/" + document.getElementById("selectDay_r8_cal2").value + "/" +  document.getElementById("year_r8_cal2").value;
		var toDate = m2 + "/" + document.getElementById("selectDay_r8_cal1").value + "/" + document.getElementById("year_r8_cal1").value;

		if (languageSet == "es") {
			fromDate = document.getElementById("selectDay_r8_cal2").value + "/" +  m1 + "/" + document.getElementById("year_r8_cal2").value;
			toDate = document.getElementById("selectDay_r8_cal1").value + "/" + m2 + "/" + document.getElementById("year_r8_cal1").value;
		}
		
		var older = Date.parse(fromDate);
		var newer = Date.parse(toDate);
		if (newer < older) {
			var temp = fromDate;
			fromDate = toDate;
			toDate = temp;
		}	

		document.getElementById("daysFilterEight").value = fromDate + " thru " + toDate;	
	
	}		
	else {
		if ( (d1 + d2) == 0 )  {
			$('#calendarModalPopupEight').modal('hide');
			document.getElementById("daysFilterEight").value = "";

			filterEight = "~" + document.getElementById("daysFilterEight").value + "~";
		}
		else {					
			if (submit == "submit") {
				alert("<%= rb.getString("must_select_a_day_from_each_calendar") %>");			
			}
		}
	}

	
}


function addExperimentForm() {

	$('#addExperimentFormModalPopup').modal('hide');
    
    var jsonData = null;
    var cols = [];

   
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/msAdmin",
        data : {
            command: 'getNewExperimentId',
            lang: loc,
            filter: "0" 
       
        },
        success : function(data) {
        	if (data) {
               	
            	jsonData = $.parseJSON(data);	
            	var newExperimentId  = jsonData.newExperimentId;
            	//document.getElementById("add_experiment_hdr").innerHTML = "Experiment Id# " + newExperimentId;            	

            	document.getElementById("newExperimentId").value = newExperimentId;
            	document.getElementById("addExperimentName").value = "";
            	document.getElementById("addExperimentSchoolYear").value = "";
            	document.getElementById("addExperimentOptionString").value = "";

                $('#addExperimentFormModalPopup').modal('show');
            
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

function editExperimentForm() {

	$('#editExperimentFormModalPopup').modal('hide');
	
	var filter = "0";
    
    var jsonData = null;

	var name;
	
   	name = prompt("Enter experiment name","");
   	if (name.length == 0) {
   		alert('Must enter experiment name');
   		return;
   	}
	
       
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/msAdmin",
        data : {
            command: 'getExperimentInfo',
            lang: loc,
            filter: name 
       
        },
        success : function(data) {
        	if (data) {
               	
            	jsonData = $.parseJSON(data);
            	var result = jsonData.result;
            	if (result == "success" ) {
	            	document.getElementById("editExperimentId").value = jsonData.id;
	            	document.getElementById("editExperimentName").value = jsonData.name;
	            	document.getElementById("editExperimentSchoolYear").value = jsonData.schoolYear;
	            	document.getElementById("editExperimentOptionString").value = jsonData.optionString;
				
                	$('#editExperimentFormModalPopup').modal('show');
            	}
            	else {
            		alert(result);
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

function editExperimentClasses(cmd) {

	var filter = "";
	var id = "";
	var expName = "";
	
	if (cmd === "remove") {
		id = prompt("Enter class id");
		expName = prompt("Enter experiment  name");
		var conf = confirm("Are you sure you want to remove class id: " + id + " from " + expName + " experiment");
		if (conf) {
			filter = "remove" + "~" +  id+ "~" + expName;
			adminExperimentClasses(filter);				
		}
	}
	if (cmd === "add") {
		id = prompt("Enter class id");
		expName = prompt("Enter experiment  name");
		var conf = confirm("Are you sure you want to add class id: " + id + " to " + expName + " experiment");
		if (conf) {
			filter = "add" + "~" +  id + "~" + expName;
			adminExperimentClasses(filter);
		}
	}
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


function showTable4b() {


	var filter = "";
	var type = "";
	var sort = "";

    const rb4bContent = document.querySelectorAll('input[name="optRadio4bContent"]');
    for (const rb4bc of rb4bContent) {
        if (rb4bc.checked) {
        	type = rb4bc.value;
            break;
        }
    }

    const rb4bSort = document.querySelectorAll('input[name="optRadio4bSort"]');
    for (const rb4bs of rb4bSort) {
        if (rb4bs.checked) {
        	sort = rb4bs.value;
            break;
        }
    }

    filter = type + "~" + sort;
    
    var jsonData_4b = null;
    var cols_4b = [];

    var tbl_4b = document.getElementById("table4b");
    document.getElementById("table4b").innerHTML = "";    
	$('#table4b-loader').show();
    
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: 0,
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
            cohortId: 0,
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
            cohortId: 0,
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
            cohortId: 0,
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
    
	$('#table4c-loader').show();
    
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: 0,
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

function addExperimentFormSubmit() {

	var filter = document.getElementById("addExperimentName").value;
	filter = filter + "~" + document.getElementById("addExperimentSchoolYear").value;
	filter = filter + "~" + document.getElementById("addExperimentOptionString").value;

	$.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/msAdmin",
        data : {
            cohortId: 0,
            command: 'addNewExperimentInfo',
            lang: loc,
            filter: filter
        },
        success : function(data) {
        	if (data) {
        		if (data.substring(0,5) == "error") {        	
            		alert(data);
        		}
        		else {        			
        			alert(document.getElementById("addExperimentName").value + " added successfully");
        			$('#addExperimentFormModalPopup').modal('hide');        			        			
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
	
function chatPromptFormDisplay() {

	$('#chatPromptFormModalPopup').modal('show');
}
function translateFormDisplay() {

	$('#translateFormModalPopup').modal('show');
}


function chatPromptSubmit() {

	var filter = "How many days are there in February?"

   	var choicesArr = [];
			
			
	filter = document.getElementById("chatPromptId").value;
	$.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/msAdmin",
        data : {
            command: 'chatPrompt',
            lang: loc,
            filter: filter
        },
        success : function(data) {
        	if (data) {
        		if (data.substring(0,5) == "error") {        	
            		alert("Error: " + data);
        		}
        		else {
        			try {
	        			var resp = data;
	                	var jsonData = $.parseJSON(data);
	                	var choicesArr = jsonData.choices;
						var text = choicesArr[0].text; 
	        			document.getElementById("chatResponseText").value = text;
        			}
       				catch (e) {
       					console.log(e);        				
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

function translateFormSubmit() {

	var filter = "Spanish"

   	var choicesArr = [];
			
			
	filter += '~' + document.getElementById("translatePromptId").value;
	$.ajax({
        type : "POST",
 
        url : pgContext+"/tt/tt/msAdmin",
        data : {
            command: 'translatePrompt',
            lang: loc,
            filter: filter
        },
        success : function(data) {
        	if (data) {
        		if (data.substring(0,5) == "error") {        	
            		alert("Error: " + data);
        		}
        		else {
        			try {
        				var resp = data;        			
	                	var jsonData = $.parseJSON(data);
	                	var choicesArr = jsonData.choices;
						var text = choicesArr[0].text; 
	        			document.getElementById("translateResponseText").value = text;
	        		}
    	   			catch (e) {
      					console.log(e);
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

function editExperimentFormSubmit() {

	var filter = document.getElementById("editExperimentId").value;
	filter = filter + "~" + document.getElementById("editExperimentName").value;
	filter = filter + "~" + document.getElementById("editExperimentSchoolYear").value;
	filter = filter + "~" + document.getElementById("editExperimentOptionString").value;

	$.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/msAdmin",
        data : {
            command: 'updateExperimentInfo',
            lang: loc,
            filter: filter
        },
        success : function(data) {
        	if (data) {
        		if (data.substring(0,5) == "error") {        	
            		alert(data);
        		}
        		else {
        			alert(document.getElementById("editExperimentName").value + " changed successfully");
        			$('#editExperimentFormModalPopup').modal('hide');         			
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



function adminExperimentClasses(filter) {

	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/msAdmin",
        data : {
            command: 'adminExperimentClasses',
            lang: loc,
            filter: filter
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

function launchChatHelp() {
	
	alert("launchChatHelp");
	
//		var a_href = '${pageContext.request.contextPath}';
//		a_href = a_href + "/img/video_help_add_cohort.mp4";
//		document.getElementById("createCohortHelpLink").href = a_href;
//		alert(a_href);
//		document.getElementById("createCohortHelpLink").click();
		
	
	displayTextHelpPopup('msadmin_chat','Admin Commands');
}

function displayTextHelpPopup(helpTopic, helpHdr) {
	

	document.getElementById("help_hdr").innerHTML = helpHdr;
	
	var helpDivContent = "";

	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getResearcherHelp",
        data : {
            helpTopic: helpTopic,
            lang: loc,
            filter: 'text'
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
                $("#videoHelpLink").hide();
        		document.getElementById("helpPopupBody").innerHTML = helpDivContent;
        		$("#helpPopupBody").show();
        		$('#helpModalPopup').modal('show');         			

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


function showVideoHelp(src,type) {

	var popup = document.getElementById("myPopup");
	popup.setAttribute("src", src);
	popup.setAttribute("type", type);
	
			
	$("#helpPopupBody").hide();
	
	popup.classList.toggle("show");

	if (popup.paused){ 
	    popup.play(); 
	    }
	  else{ 
	    popup.pause();
	    }
	 
	}
	$('#helpModalPopup').modal('show');


function displayVideoPopup(helpTopic, helpHdr) {

	document.getElementById("help_hdr").innerHTML = helpHdr;
	var helpDivContent = "";

	helpTopic = "Video Topic";
	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getResearcherHelp",
        data : {
            helpTopic: helpTopic,
            lang: loc,
            filter: 'video'
        },
        success : function(data) {
        	if (data) {

            	var jsonData = $.parseJSON(data);	
               	
            	
//            	var src = "/ms/img/video_help_add_cohort.mp4";
//            	var type = "video/mp4";
            	var srcVideo = jsonData[0].src;
            	var typeVideo = jsonData[0].type;
            	showVideoHelp(srcVideo,typeVideo);
            	//popup.setAttribute("src", "/ms/img/video_help_add_cohort.mp4");
            	//popup.setAttribute("type", "video/mp4");
                $("#videoHelpLink").show();
        		$('#helpModalPopup').modal('show');         			

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


function showReportProb1() {

		
	var prob1Filter = selectedProblemOne + "," + selectedTopicOne;
	
	if (plotProb1 != null) {
		plotProb1.destroy();
		plotProb1 = null;
	}
	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: 0,
            reportType: 'getMathProblemsEffortDashboard',
            lang: loc,
            filter: prob1Filter
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
		  			lines[i].push(jsonData[i].GUESS);
		  			lines[i].push(jsonData[i].SKIP);
		  			lines[i].push(jsonData[i].GIVEUP);
					var canvasName = 'Prob1_canvas';
					var tline = lines[i];
					plotProb1 = $.jqplot(canvasName, [tline], {
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


function showReportProb8Saved() {

	
	var prob8Filter = selectedProblemEight;
	
	if (plotProb8 != null) {
		plotProb8.destroy();
		plotProb8 = null;
	}
	
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: 0,
            reportType: 'getMathProblemsHistoryDashboard',
            lang: loc,
            filter: prob1Filter
        },
        success : function(data) {
        	if (data) {
        		
				document.getElementById("ProblemHistoryReport").style.visibility = 'visible';
                
                
               	var resultData = $.parseJSON(data);
                var jsonData = resultData[0];
            	
            	for (var i=0;i<jsonData.length;i = i + 1) {
		  			var line0 = [];
//		  			var lines = [line0,line1,line2,line3,line4,line5,line6,line7,line8,line9]
		  			var lines = [line0]
		  			lines[i].push(jsonData[i].SOF);
		  			lines[i].push(jsonData[i].ATT);
		  			lines[i].push(jsonData[i].SHINT);
		  			lines[i].push(jsonData[i].GUESS);
		  			lines[i].push(jsonData[i].SKIP);
		  			lines[i].push(jsonData[i].GIVEUP);
					var canvasName = 'Prob8_canvas';
					var tline = lines[i];
					plotProb1 = $.jqplot(canvasName, [tline], {
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

$(document).ready(function () {
	populateTopicSelectionListOne();

});

function getEffortColorRGB(effortValue) {
	
	switch (effortValue) {
		case ('SOF'): {
			return 'rgba(38, 242, 19,1)';
		}
		case ('ATT'): {
			return 'rgba(155, 235, 148,1)';
		}
		case ('SHINT'): {
			return 'rgba(128, 177, 211,1)';
		}
		case ('SHELP'): {
			return 'rgba(253, 180, 98,1)';
		}
		case ('GUESS'): {
			return 'rgba(251, 128, 114,1)';
		}
		case ('NOTR'): {
			return 'rgba(38, 242, 19,1)';
		}
		case ('SKIP'): {
			return 'rgba(141, 211, 199,1)';
		}
		case ('GIVEUP'): {
			return 'rgba(190, 186, 218,1)';
		}
		case ('NODATA'): {
			return 'rgba(217, 217, 217,1)';
		}
		default: 
			return 'rgba(217, 217, 217,1)';
	}

}


function showReportProb8() {
	
    populateTopicSelectionListOne();

    getFilterEight('submit');
    
	filterEight = filterEight + selectedProblemEight + "~" + selectedTopicEight;
	

		
	if (plotProb8 != null) {
		plotProb8.destroy();
		plotProb8 = null;
	}

    $("#problemHistoryReport").hide();
    $('#collapseEightLoader').show();
	   
    $.ajax({
        type : "POST",
        url : pgContext+"/tt/tt/getCohortReport",
        data : {
            cohortId: 0,
            reportType: 'getMathProblemsHistoryDashboard',
            lang: loc,
            filter: filterEight
        },
        success : function(data) {
        		
            $('#collapseEightLoader').hide();
			document.getElementById("problemHistoryReport").style.visibility = 'visible';
              
           	var resultDataArr = $.parseJSON(data); 

			selectedTopicEight =  document.getElementById("topicsEight").value;
			
			var rptEightTitle = 'student_problem_solving_history';
			if (selectedTopicEight.length > 0) {
				rptEightTitle = rptEightTitle + " ( Topic = " + topicNameMapEight.get(selectedTopicEight) + " )";
			}

			var problems = [];
			var problemIds = [];
			var hints = [];
			var attempts = [];
			var videos = [];
			var difficulty = [];
			var topic = [];
			var topicname = [];
			var minutesOnProblem = [];
			var maxMinutes = 0;
			var barWidth = [];
			//var myColors = ['rgba(38, 242, 19,1)', 'rgba(38, 242, 19,1)', 'rgba(141, 211, 199,1)', 'rgba(38, 242, 19,1)', 'rgba(190, 186, 218,1)','rgba(155, 235, 148,1)','rgba(141, 211, 199,1)'];
			var effColors = [];
			var effText = [];
			var effort = "No DATA";
		
			var maxYaxis = 1;       
			
       		var useThisOne = true;
		
			$.each(resultDataArr, function (i) {
            	useThisOne = true;
            	effort = resultDataArr[i].effort;
				var pTopic = selectedTopicEight;
				if ((selectedTopicEight.length > 0) && (!(pTopic == selectedTopicEight))) {
					useThisOne = false;
				}
            	if (effort === "NO DATA") {                		
					useThisOne = false;
				}
            	if (useThisOne) {                		
			   		var p = "" + i + ": "+ selectedTopicEight;
			   		var pHints = parseInt(resultDataArr[i].numHints);
			   		var pAttempts = parseInt(resultDataArr[i].numAttemptsToSolve);
			   		var pVideos = parseInt(resultDataArr[i].videoSeen);
					problems.push(p);
			   		effColors.push(getEffortColorRGB(effort));
			   		effText.push(effort);
			   		hints.push(parseInt(resultDataArr[i].numHints));
			   		attempts.push(parseInt(resultDataArr[i].numAttemptsToSolve));
			   		videos.push(parseInt(resultDataArr[i].videoSeen));
			   		var pdifficulty = parseFloat(resultDataArr[i].probDiff);
			   		pdifficulty = 10.0 * pdifficulty;
			   		difficulty.push(pdifficulty);

			   		barWidth.push(1);
			   		problemIds.push(selectedProblemEight);

					var isSolved = parseInt(resultDataArr[i].isSolved);			   		
					var timeToSolve = parseInt(resultDataArr[i].timeToSolve);
					if (timeToSolve < 0) {
						timeToSolve = 0;
					}
					var minutesToSolve = timeToSolve/60000;
					if (minutesToSolve > 15) {
						minutesToSolve = 15;
					}
					
					var timeToFirstAttempt = parseInt(resultDataArr[i].timeToFirstAttempt);
					if (timeToFirstAttempt < 0) {
						timeToFirstAttempt = 0;
					}
					var minutesToFirstAttempt =  timeToFirstAttempt/60000;
					if (minutesToFirstAttempt > 15) {
						minutesToFirstAttempt = 15;
					}
										
					if (minutesToSolve > maxYaxis) {
						maxYaxis = minutesToSolve;
					}
				
					if (pHints > maxYaxis) {
						maxYaxis = pHints;
					}
					if (pAttempts > maxYaxis) {
						maxYaxis = pAttempts;
					}
			
					if (pVideos > maxYaxis) {
						maxYaxis = pVideos;
					}	        		
			
						if (isSolved > 0) {
			       			minutesOnProblem.push(minutesToSolve);    						
						}
						else {
							if (minutesToFirstAttempt > 0) {	
								minutesOnProblem.push(minutesToFirstAttempt);
							}
							else {
								minutesOnProblem.push(0.2);
							}
						}
					
				}
			});
			
			maxYaxis = maxYaxis + 1;
			if (maxYaxis > 15) {
				maxYaxis = 15;
			}
			
			if (maxYaxis < 10) {
				maxYaxis = 10;
			}
			
			
			var maxWidth = 800;       
			if (problems.length > 16) {
				maxWidth = problems.length * 50;    		 
			}
			
				var traceHints = {
						  x: problems,
						  y: hints, 
					  name: '<%= rb.getString("hints")%>',
					  type: 'scatter',
					  mode: 'lines+markers',
					  marker: {
					    color: 'rgb(219, 64, 82)',
					    size: 8
					  },
					  line: {
					    color: 'rgb(219, 64, 82)',
					    width: 1
					  }         				
			};
			
			var traceAttempts = {
				  x: problems,
				  y: attempts, 
			  name: '<%= rb.getString("attempts")%>',
				  type: 'scatter',
				  mode: 'lines+markers',
				  marker: {
				    color: 'rgb(55, 128, 191)',
				    size: 8
				  },
				  line: {
				    color: 'rgb(55, 128, 191)',
				    width: 1
				  }         				
				};
			
			var traceVideos = {
					  x: problems,
					  y: videos, 
				  name: '<%= rb.getString("videos")%>',
					  type: 'scatter',
					  mode: 'lines+markers',
					  marker: {
					    color: 'rgb(153, 0, 153)',
					    size: 8
					  },
					  line: {
					    color: 'rgb(153, 0, 153)',
					    width: 1
					  }         				
					};
			
			var traceDifficulty = {
				  x: problems,
				  y: difficulty, 
			  name: '<%= rb.getString("difficulty")%>',
				  type: 'scatter',
				  mode: 'lines+markers',
				  marker: {
				    color: 'rgb(255, 255, 0)',
				    size: 8
				  },
				  line: {
				    color: 'rgb(255, 255, 0)',
				    width: 2
				  }         				
				};
			
				var traceProblems = {
					  x: problems,
					  y: minutesOnProblem,
					  name: '<%= rb.getString("minutes")%>',
			 showlegend: false,
			 mode: 'markers',
			 marker:{
			    color: effColors
			 },
			 type: 'bar',
			      hovertemplate: '<%= rb.getString("minutes")%> %{y:.2f}<br><i><%= rb.getString("click_to_see_the_problem")%></i>',           			  
			      text: effText,
					};           		
				
				var data = [];
			if (document.getElementById("trackHints").checked == true) {
					data.push(traceHints);
			}
			if (document.getElementById("trackAttempts").checked == true) {
			   	data.push(traceAttempts);
			}
			if (document.getElementById("trackVideos").checked == true) {
					data.push(traceVideos);
			}
			if (document.getElementById("trackDifficulty").checked == true) {
					data.push(traceDifficulty);
			}

			data.push(traceProblems);
			//	var data = [traceHints, traceAttempts, traceVideos, traceProblems];
			
			layout = {
			   	width:(maxWidth),
			   	height:500,          	         
			   	xaxis: {
				    type: 'category',
				    title: '<%= rb.getString("problems")%>',
			   	},
			   	yaxis: {
			   		title: '<%= rb.getString("minutes")%>',
			  		dtick: 1,
				   	range: [0, (maxYaxis + 1)]
				},
				legend: {
			   		x: 0,
			   		y: 1,
			   		traceorder: 'normal',
			   		font: {
			     	family: 'sans-serif',
			     	size: 12,
			     	color: '#000'
			   	},
			   		bgcolor: '#E2E2E2',
			   		bordercolor: '#000000',
			   		borderwidth: 1
				},           	         
				 	hovermode:'x',
			     	title:rptEightTitle,
			  	 	displayModeBar: false,
			
			};   
		
			var myPlot = document.getElementById('problemHistoryReport');

       		Plotly.newPlot('problemHistoryReport', data, layout);     
       		
       		myPlot.on('plotly_click', function(data){
				var i = data.points[0].pointIndex;
				var myY = data.points[0].fullData.y[i];
				var imageURL = problem_imageURL+problemIds[i] +'.jpg';
				document.getElementById('problemHistorySnapshot').innerHTML = '<span><strong><%= rb.getString("problem_id")%> :'+ problemIds[i] + '</strong></span>' + '<img  style="max-width:600px; max-height:600px;" src="'+ imageURL + '"/>';
		        $("#problemHistoryPopup").modal('show');
       		});
       		populateTopicSelectionListEight();
       		$("#problemHistoryReport").show();            
        },
        error : function(e) {
            $('#collapseEightLoader').hide();
            console.log(e);
        }
    });

}


</script>
	
</head>

<body>
<div class="bootstrap fullscreen">
<div class="container-fluid tab-content">
  <h2>Admin Workbench</h2>

	  <ul class="nav nav-tabs">
	<!-- <li class="active"><a data-toggle="tab" href="#home" onclick="gotoSettingsPane();">Home</a></li>  -->   
	    <li><a data-toggle="tab"  href="#MSAdminTools">Support Tools</a></li>
	    <li><a data-toggle="tab"  href="#MSExperimentTools">Experiments</a></li>
	    <!-- <li><a data-toggle="tab"  href="#MSChatTools">Chat Tools</a></li> -->
	    <li><a data-toggle="tab"  href="#mathProblemDashboard">Math Problem Tools</a></li>
	    <li><a data-toggle="tab"  href="#MSAdminHelp">Admin Help</a></li>
	    <li>
			<a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/ttMain"> Researcher Workbench</a>
	    </li>
	    <li>
	        <a id="logout_selector" href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/logout"><i
	                class="fa fa-fw fa-power-off"></i><%= rb.getString("log_out") %></a>
	    </li>
	  </ul>

    <div id="MSAdminTools" class="col-sm-12 tab-pane fade container">
    	<h3>MS Support Tools</h3>
		<div class="row">
			<div id="tablesCohortName">				
			</div>
		</div>
		<br>
        <div id="tables-container" class="container-fluid">

            <div id="tables-wrapper" class="row" width: 100%;">

                <div class="panel-group" id="msAdminGroup">
                
                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a class="accordion-toggle" data-toggle="collapse" data-parent="#msAdminGroup" href="#table_4b">
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
                            	<div class="row">                           
									<div class="form-group">
										<label class="radio-inline"><input id="radioErrorsSortAsc"  value="asc" type="radio" name="optRadio4bSort" checked>Sort Descending</label>
										<label class="radio-inline"><input id="radioErrorsSortDesc" value="desc" type="radio" name="optRadio4bSort">Sort Ascending</label>
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
                                <a class="accordion-toggle" data-toggle="collapse" data-parent="#msAdminGroup" href="#table_4c">
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
         </div>
	</div>
    <div id="MSExperimentTools" class="col-sm-12 tab-pane fade container">
		<h3>Experiment Admin Tools</h3>					
		<div class="row">
			<div id="tablesCohortName">				
			</div>
		</div>
		<br>
        <div id="tables-container" class="container-fluid">

            <div id="tables-wrapper" class="row" width: 100%;">

                <div class="panel-group" id="msAdminGroup">
                
				<br>
				
                   <div  id="editExperiment" class="panel panel-default">
                       <div class="panel-heading">
                           <h4 class="panel-title">
                               <a id="ms_experiment" class="accordion-toggle" data-toggle="collapse" data-parent="#msAdminGroup" href="#admin7">
                                   Experiment Information
                               </a>
                              	<button type="button" class="close" onclick="$('.collapseAdmin').collapse('hide')">&times;</button>                             
                           </h4>
                       </div>
                       <div id="admin7" class="panel-collapse collapse">  
                           <div class="panel-body report_filters">                           
							  <input class="btn btn-lg btn-primary" onclick="addExperimentForm();" type="submit" value="<%= rwrb.getString("add") %>">
							  <input class="btn btn-lg btn-primary" onclick="editExperimentForm();" type="submit" value="<%= rwrb.getString("edit_view") %>">
							  <input class="btn btn-lg btn-primary" onclick="listExperimentForm();" type="submit" value="List">
                           </div>

                           <div class="panel-body">
                               <div id="adminExperimentForm" class="table table-striped table-bordered hover display nowrap" width="100%">

                               </div>
                           </div>	
                       </div>
                   </div>                   


                   	                                       
                   <div  id="AdminClassExperiment" class="panel panel-default">
                       <div class="panel-heading">
                           <h4 class="panel-title">
                               <a id="ms_experiment_class" class="accordion-toggle" data-toggle="collapse" data-parent="#msAdminGroup" href="#admin8">
                                   Modify Experiment/Class connection
                               </a>
                              	<button type="button" class="close" onclick="$('.collapseAdmin').collapse('hide')">&times;</button>                             
                           </h4>
                       </div>
                       <div id="admin8" class="panel-collapse collapse">  
                           <div class="panel-body report_filters">                           
							  <input class="btn btn-lg btn-primary" onclick="editExperimentClasses('add');" type="submit" value="<%= rwrb.getString("add") %>">
							  <input class="btn btn-lg btn-primary" onclick="editExperimentClasses('remove');" type="submit" value="<%= rwrb.getString("remove") %>">
                           </div>

                           <div class="panel-body">
                               <div id="adminExperimentClassForm" class="table table-striped table-bordered hover display nowrap" width="100%">

                               </div>
                           </div>

                       </div>
                   </div>
               </div>
           </div>
       </div>
	 </div>
                   
                                      
   <div id="MSChatTools" class="col-sm-12 tab-pane fade container">
		<h3>AI Chat tools</h3>					
		<div class="row">
			<div id="tablesCohortName">				
			</div>
		</div>
		<br>				
        <div id="tables-container" class="container-fluid">

            <div id="tables-wrapper" class="row" width: 100%;">

                <div class="panel-group" id="msAdminGroup">
                
				<br>

                   <div  id="AdminChatForm" class="panel panel-default">
                       <div class="panel-heading">
                           <h4 class="panel-title">
                               <a id="admin_chat" class="accordion-toggle" data-toggle="collapse" data-parent="#msAdminGroup" href="#admin9">
                                   Chat with OpenAI
                               </a>
                              	<button type="button" class="close" onclick="$('.collapseAdmin').collapse('hide')">&times;</button>                             
                           </h4>
                       </div>
                       <div id="admin9" class="panel-collapse collapse">  
                           <div class="panel-body report_filters">                           
							  <input class="btn btn-lg btn-primary" onclick="chatPromptFormDisplay();" type="submit" value="Chat">
                           </div>

                           <div class="panel-body">
                               <div id="chatFormBody" class="table table-striped table-bordered hover display nowrap" width="100%">

                               </div>
                           </div>

                       </div>
                   </div>                   

                   <div  id="AdminTranslateForm" class="panel panel-default">
                       <div class="panel-heading">
                           <h4 class="panel-title">
                               <a id="admin_chat" class="accordion-toggle" data-toggle="collapse" data-parent="#msAdminGroup" href="#admin10">
                                   Translate some text
                               </a>
                              	<button type="button" class="close" onclick="$('.collapseAdmin').collapse('hide')">&times;</button>                             
                           </h4>
                       </div>
                       <div id="admin10" class="panel-collapse collapse">  
                           <div class="panel-body report_filters">                           
							  <input class="btn btn-lg btn-primary" onclick="translateFormDisplay();" type="submit" value="Translate">
                           </div>

                           <div class="panel-body">
                               <div id="translateFormBody" class="table table-striped table-bordered hover display nowrap" width="100%">

                               </div>
                           </div>

                       </div>
                   </div>                   
            	</div>
        	</div>
       	</div>
   	</div>
	  
	<div id="MSAdminHelp" class="col-sm-12 tab-pane fade container">
        <h2 class="page-header">
            Help
        </h2>

        <div id="help-container" class="container-fluid">

            <div id="help-wrapper" class="row" width: 100%;">

                <div class="panel-group" id="helpCommands">

                    <div  id="helpMSAdmin" class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="hrlp_one" class="accordion-toggle" data-toggle="collapse" data-parent="#helpCommands" href="#help1">
                                    MSAdmin Help Pages
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
									    <li onclick="launchChatHelp();"><a id="chatHelpLink"></a>How to use Chat functions</li>
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

    <div id="mathProblemDashboard" class="col-sm-12 tab-pane fade container">
        <h2 class="page-header">
            Math Problem Dashboard
        </h2>

		<br>
        <div id="problem-dashboard-container" class="container-fluid">

            <div id="problem-dashboard-wrapper" class="row" width: 100%;">

                <div class="panel-group" id="problemDashboardGroup">

                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a class="accordion-toggle" data-toggle="collapse" data-parent="#problemDashboardGroup" href="#chartReportOne">
                                    Overall Problem Effort 
                                </a>
                               	<button type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>


                        <div id="chartReportOne" class="panel-collapse collapse">  
                            <div class="panel-body">
                            	<div class="row">
							     	<div class="report_filters" style="display: block;">
							     		<div class="col-md-5">
				                      		<div class="row">
				                      			<div class="col-md-6">
												<button type="button" class="btn btn-primary btn-sm" onclick="populateTopicSelectionListOne();"><%= rb.getString("choose_topic") %></button>		                        		
				                      			</div>
				                      		</div>
				                      		<div class="row">
				                       			<div id="topicSelectionListOne" class="col-md-3">                       
												</div>
											</div>
											<br>
				                      		<div class="row">
				                      			<div class="col-md-6">
												<button type="button" class="btn btn-primary btn-sm" onclick="populateProblemSelectionListOne();"><%= rb.getString("select_problem") %></button>		                        		
				                      			</div>
				                      		</div>
				                      		<div class="row">
				                       			<div id="problemSelectionListOne" class="col-md-3">                       
												</div>
											</div>
										</div>
					                    <div id="liveDashboardEffortPane" class="col-md-7" style="background-color:powderblue; border: 2px solid #000000; border-radius: 25px; height:500px;">
					                        <div style="text-align:center;"> <h2>Problem Solving Effort</h2></div>
							            	<div id="Prob1_canvas" class="col-md-5" style="width:400px; height:400px;"></div> 
							            	<div id="Prob1_legend" class="col-md-7" style="display: none"> 		            	
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

                            <div class="panel-body report_filters">                           
								  <input class="btn btn-lg btn-primary" onclick="showReportProb1();" type="submit" value="<%= rwrb.getString("show_chart") %>">
								  <a id="showDashboardLegendBtn" class="btn btn-lg btn-primary" role="button" value="show" onclick="showDashboardLegend();"><%= rb.getString("show_legend") %></a>
								  <a id="hideDashboardLegendBtn" class="btn btn-lg btn-primary" role="button" value="show" onclick="hideDashboardLegend();"><%= rb.getString("hide_legend") %></a>
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
                    	</div>
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_eight" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseEight">
                                    Overall Problem History
                                </a>
                                <button id="eightButton" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="collapseEight" class="panel-collapse collapse">
	                            <div class="panel-body report_filters">                           
									  <input id="trackAttempts" type="checkbox" style="width:48px" name="" value="" onblur="getFilterEight('');">
									  <label class="report_filters">Track Attempts</label>
									  &nbsp;|&nbsp;
									  <input id="trackHints" type="checkbox" style="width:48px" name="" value="" onblur="getFilterEight('');">
									  <label class="report_filters">Track Hints</label>
									  &nbsp;|&nbsp;
									  <input id="trackVideos" type="checkbox" style="width:48px"  name="" value="" onblur="getFilterEight('');">
									  <label class="report_filters">Track Videos</label>
									  &nbsp;|&nbsp;
									  <input id="trackDifficulty" type="checkbox" style="width:48px" name="" value="" onblur="getFilterEight('');">
									  <label class="report_filters">Track Difficulty</label>
								</div>
		                        <div class="panel-body report_filters">
		                        	<div id="chooseDateRange" class="row">
		                        		<div class="col-md-2 offset-md-1">                       
						                	<button type="button" class="btn btn-primary" onclick="initCalendar_r8_cal1();initCalendar_r8_cal2();$('#calendarModalPopupEight').modal('show');" ><%= rb.getString("choose_date_range") %></button>
						                </div>
		                        		<div class="col-md-3">                       
										    <input id="daysFilterEight" style="width:220px" type="text" name="" value="" >   
						                </div>
		 							</div>  		
								</div>
		                        <div class="panel-body report_filters">
									<div class="col-md-12">
			                      		<div class="row">
			                      			<div class="col-md-4">
												<button type="button" class="btn btn-primary btn-sm" onclick="populateTopicSelectionListEight();"><%= rb.getString("choose_topic") %></button>		                        		
			                      			</div>
			                      			<div class="col-md-6">
												<button type="button" class="btn btn-primary btn-sm" onclick="populateProblemSelectionListEight();"><%= rb.getString("select_problem") %></button>		                        		
			                      			</div>
			                      		</div>
										<br>
			                      		<div class="row">
			                       			<div id="topicSelectionListEight" class="col-md-4">                       
											</div>
			                       			<div id="problemSelectionListEight" class="col-md-6">                       
											</div>
										</div>
									</div>
								</div>													

	                            <div class="panel-body report_filters">                           
									  <input id="showReportEightBtn" class="btn btn-lg btn-primary" onclick="showReportProb8();" type="submit" value="<%= rb.getString("show_report") %>">
	                            </div>                            
	                            <div class="panel-body">
                            </div>
                            <div id="collapseEightLoader" class="loader" style="display: none" ></div>
		            		<div id="problemHistoryReport" style="overflow-x: scroll;overflow-y: scroll;"></div> 
	                        	<div style="text-align:center;"> <h2>Problem Solving History</h2></div>
			            		<div id="Prob8_canvas" class="col-md-12" style="width:1200px; height:400px;"></div> 
		            		</div> 
                            
                        </div>



                            
                   <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a class="accordion-toggle" data-toggle="collapse" data-parent="#problemDashboardGroup" href="#chartReportTwo">
                                    Multi-lingual Problem Pairs
                                </a>
                               	<button type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                            </h4>
                        </div>
                        <div id="chartReportTwo" class="panel-collapse collapse">  
                            <div class="panel-body report_filters">                           
								  <input class="btn btn-lg btn-primary" onclick="showReportProb2();" type="submit" value="<%= rwrb.getString("show_chart") %>">
                            </div>
 
                            <div class="panel-body">
                            	<div class="row">
				                    <div id="liveDashboardEffortPane" class="col-md-8" style="background-color:powderblue; border: 2px solid #000000; border-radius: 25px; height:500px;">
				                        <div style="text-align:center;"> <h2>Problem Solving Effort</h2></div>
						            	<div id="Prob2_canvas" class="col-md-5" style="width:400px; height:400px;"></div> 
						            	<div id="Prob2_legend" class="col-md-7"> 		            	
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
		</div>
	</div>

	  
	</div>

    <footer class="footer">
        &copy; <%= rb.getString("researcher_copyright")%>
    </footer>
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



<div id="chatPromptFormModalPopup" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
            	<div id="add_cohort_hdr">OpenAI chat box</div>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
				<div>
					<label for="chatPrompt" style="width:500px">(Disabled)Enter Chat Prompt:</label><input id="chatPromptId" name="chatPromptId" style="width:500px"></input>
				</div>
            </div>
            <div class="modal-body">
				<div>
					<label for="chatResponse" style="width:500px">Chat Response:</label>
<!--				<input id="chatResponseId" name="chatResponse" style="width:500px"></input> -->
                   	<textarea id="chatResponseText" name="responseText" class="form-control" rows="10" cols="60" required ></textarea>
				</div>
            </div>
            <div class="modal-footer">
				<button type="button" class="btn btn-success" onclick="chatPromptSubmit();"><%= rb.getString("submit")%></button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="$('#chatPromptFormModalPopup').modal('hide');"><%= rb.getString("close") %></button>
            </div>
        </div>
    </div>
</div>


<div id="translateFormModalPopup" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
            	<div id="add_cohort_hdr">Translate</div>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
				<div>
					<label for="translatePrompt" style="width:500px">(Disabled) Enter or paste text here:</label><input id="translatePromptId" name="translatePrompt" style="width:500px"></input>
				</div>
            </div>
            <div class="modal-body">
				<div>
					<label for="translateResponse" style="width:500px">Translation:</label>
<!--				<input id="translateResponseId" name="translateResponse" style="width:500px"></input> -->
                   	<textarea id="translateResponseText" name="responseText" class="form-control" rows="10" cols="60" required ></textarea>
				</div>
            </div>
            <div class="modal-footer">
				<button type="button" class="btn btn-success" onclick="translateFormSubmit();"><%= rb.getString("submit")%></button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="$('#translateFormModalPopup').modal('hide');"><%= rb.getString("close") %></button>
            </div>
        </div>
    </div>
</div>

<div id="addExperimentFormModalPopup" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
            	<div id="add_experiment_hdr">Update Experiment header</div>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
				<div>
					<label for="newExperimentId" style="width:100px">Experiment Id:</label><input id="newExperimentId" name="newExperimentId" style="width:300px readonly"></input>
				</div>
				<div>
					<label for="addExperimentName" style="width:100px">Experiment Name:</label><input id="addExperimentName" name="addExperimentName" style="width:300px"></input>
				</div>
				<div>			
					<label for="addExperimentSchoolYear" style="width:100px">School Year:</label><input id="addExperimentSchoolYear" name="addExperimentSchoolYear"style="width:60px"></input>
				</div>
				<div>
					<label for="addExperimentOptionString" style="width:100px">Option String:</label><input id="addExperimentOptionString" name="addExperimentOptionString" style="width:300px"></input>
				</div>
            </div>
            <div class="modal-footer">
				<button type="button" class="btn btn-success" onclick="addExperimentFormSubmit();"><%= rb.getString("submit")%></button>
                <button type="button" class="btn btn-primary" data-dismiss="modal"><%= rb.getString("close") %></button>
            </div>
        </div>
    </div>
</div>


<div id="editExperimentFormModalPopup" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
            	<h3><div id="edit_experiment_hdr"></div></h3>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
				<div>
					<label for="editExperimentId" style="width:100px readonly">Experiment Id:</label><input id="editExperimentId" name="editExperimentId" style="width:300px"></input>
				</div>
				<div>
					<label for="editExperimentName" style="width:100px">Experiment Name:</label><input id="editExperimentName" name="editExperimentName" style="width:300px"></input>
				</div>
				<div>			
					<label for="editExperimentSchoolYear" style="width:100px">School Year:</label><input id="editExperimentSchoolYear" name="experimentSchoolYear"style="width:60px"></input>
				</div>
				<div>
					<label for="editExperimentOptionString" style="width:100px">OptionString:</label><input id="editExperimentOptionString" name="editExperimentOptionString" style="width:300px"></input>
				</div>
            </div>
            <div class="modal-footer">
				<button type="button" class="btn btn-success" id="editExperimentFormSubmit" onclick="editExperimentFormSubmit();"><%= rb.getString("submit")%></button>
                <button type="button" class="btn btn-primary" data-dismiss="modal"><%= rb.getString("close") %></button>
            </div>
        </div>
    </div>
</div>
<div id="helpModalPopup" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
            	<h3><div id="help_hdr" style="center-text"></div></h3>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div id="msadminHelpPopupBody"class="modal-body">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal"><%= rb.getString("close") %></button>
            </div>

        </div>
    </div>
</div>
<div id="problemHistoryPopup" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog modal-lg" >
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Student Problem Solving History</h4>
            </div>
            <div class="modal-body" style="min-width:900px">
                <div id="problemHistorySnapshot" ></div>
            </div>

        </div>
    </div>
</div>


<div id="studentProblemAchievementPopup" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog modal-lg" >
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Student Problem Solving Achievement</h4>
            </div>
            <div class="modal-body" style="min-width:900px">
                <div id="studentProblemAchievementSnapshot" ></div>
            </div>

        </div>
    </div>
</div>

<div id="calendarModalPopupEight" class="modal fade" data-backdrop="static" data-keyboard="false" role="dialog" style="display: none;">
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
                        <input type="hidden" id="selectDay_r8_cal1" name="selectDay_r8_cal1">
   				      <div><h3><%= rb.getString("least_recent") %>:</h3></div>
			          <div class="footer-container-calendar">
			              <label for="month_r8_cal1"><%= rb.getString("jump_to") %>: </label>
			              <select id="month_r8_cal1" onchange="jump_r8_cal1()">
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
			              <select id="year_r8_cal1" onchange="jump_r8_cal1()">
			              </select>       
			          </div>
			          <div class="button-container-calendar">
			              <div class=col-md-2><button id="previous_r8_cal1" onclick="previous_r8_cal1()">&#8249;&#8249;</button></div>
       							  <div class=col-md-8 center-text><h3 id="monthAndYear_r8_cal1"></h3></div>
			              <div class=col-md-2><button id="next_r8_cal1" onclick="next_r8_cal1()">&#8250;&#8250;</button></div>							          
			          </div>
			          
			          <table class="table-calendar" id="calendar_r8_cal1" data-lang="en">
			              <thead id="thead-month_r8_cal1"></thead>
			              <tbody id="calendar-body_r8_cal1"></tbody>
			          </table>
			          
			      </div>			      
			    </div> 
			    <div class="wrapper-calender col-sm-6">
			      <div class="container-calendar">
                        <input type="hidden" id="selectDay_r8_cal2" name="selectDay_r8_cal2">
				      <div><h3><%= rb.getString("most_recent") %>:</h3></div>
			          <div class="footer-container-calendar">
			              <label for="month_r8_cal2"><%= rb.getString("jump_to") %>: </label>
			              <select id="month_r8_cal2" onchange="jump_r8_cal2()">
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
			              <select id="year_r8_cal2" onchange="jump_r8_cal2()">
			              </select>       
			          </div>			 
			          <div class="button-container-calendar">
			              <div class=col-md-2><button id="previous_r8_cal2" onclick="previous_r8_cal2()">&#8249;&#8249;</button></div>
       							  <div class=col-md-8 center-text><h3 id="monthAndYear_r8_cal2"></h3></div>
			              <div class=col-md-2><button id="next_r8_cal2" onclick="next_r8_cal2()">&#8250;&#8250;</button></div>							          
			          </div>
			          
			          <table class="table-calendar" id="calendar_r8_cal2" data-lang="en">
			              <thead id="thead-month_r8_cal2"></thead>
			              <tbody id="calendar-body_r8_cal2"></tbody>
			          </table>
			          
			        </div>
            	</div>
            </div>
            </div>
           <div class="modal-footer">

          		<div class="offset-md-6">
	                <button type="button" class="btn btn-success" onclick="getFilterEight('submit');" ><%= rb.getString("submit") %></button>
	                <button type="button" class="btn btn-danger" data-dismiss="modal" onclick="$('#calendarModalPopupEight').modal('hide');" ><%= rb.getString("cancel") %></button>
                </div> 
         </div>
    	</div>
	</div>
</div>	

<script type="text/javascript" src="<c:url value="/js/calendar_r8_1.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/calendar_r8_2.js" />"></script>


</body>
</html>

