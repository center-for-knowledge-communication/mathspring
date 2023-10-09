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

</head>


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

<body>
<div class="bootstrap fullscreen">
<div class="container-fluid tab-content">
  <h2>Mathspring Workbench</h2>

	  <ul class="nav nav-tabs">
	<!-- <li class="active"><a data-toggle="tab" href="#home" onclick="gotoSettingsPane();">Home</a></li>  -->   
	    <li><a data-toggle="tab"  href="#MSAdminTools">Admin Tools</a></li>
	    <li><a data-toggle="tab"  href="#MSExperimentTools">Experiments</a></li>
	    <li><a data-toggle="tab"  href="#MSChatTools">Chat Tools</a></li>
	    <li><a data-toggle="tab"  href="#MSAdminHelp">MS Admin Help</a></li>
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


	</div>

    <footer class="footer">
        &copy; <%= rb.getString("researcher_copyright")%>
    </footer>
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
					<label for="chatPrompt" style="width:500px">Enter Chat Prompt:</label><input id="chatPromptId" name="chatPromptId" style="width:500px"></input>
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
					<label for="translatePrompt" style="width:500px">Enter or paste text here:</label><input id="translatePromptId" name="translatePrompt" style="width:500px"></input>
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

</body>
</html>

