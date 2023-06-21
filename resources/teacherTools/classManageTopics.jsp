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
/**
	Frank	02/16/13	Extracted from classManager.jsp
*/
 

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
</script>

	 
    <script type="text/javascript" src="<c:url value="/js/bootstrap/js/language_es.js" />"></script>    
    <script type="text/javascript">
        var servletContextPath = "${pageContext.request.contextPath}";
        var pgContext = '${pageContext.request.contextPath}';
        var classID = '${classInfo.classid}';
        var teacherID = '${teacherId}';
        var teacherLoginType = '${teacherLoginType}';
        var problem_imageURL = '${webContentpath}'+'problemSnapshots/prob_';

        var isCluster = 0;
        var hasClusters = 0;
        var classColor = "";
        var titleClassName = "";


       var activeProblemSet;
       var inactiveProblemSets;

       var activeProblemSetsize = 0;
       var inactiveProblemSetsize = 0;

       var topicActiveSelectionInProgress = "";
       var topicPassiveSelectionInProgress = "";
       var topicActiveStandardInProgress = "";
       var topicPassiveStandardInProgress = "";

       var activeStandardsRow = null;
       var activeStandardsChild = null;
       var activeStandardsResponse = null;
       var activeStandardsJSONData = null;
       var activeStandardsTopicStandards = null;
       var activeStandardsTopicStandardsSorted = null;
       var activeStandardsRowID = "";

       var passiveStandardsRow = null;
       var passiveStandardsChild = null;
       var passiveStandardsResponse = null;
       var passiveStandardsJSONData = null;
       var passiveStandardsTopicStandards = null;
       var passiveStandardsTopicStandardsSorted = null;
       var passiveStandardsRowID = "";



        
        function handleclickHandlers() {

            $("#classManageTopics").click(function () {
                $('#reorg_prob_sets_handler').css('color', '#ffffff');

                $("#content-conatiner").children().hide();
                $("#problem_set_content").show();
            });

            $('#activateProbSetTable input[type="checkbox"]').click(function () {
                if ($('#activateProbSetTable input[type="checkbox"]:checked').size()) {
                    $('#deactivateProblemSets').prop('disabled', false);
                } else {
                    $('#deactivateProblemSets').prop('disabled', true);
                }
            });

            
            $('#inActiveProbSetTable input[type="checkbox"]').click(function () {
                if ($('#inActiveProbSetTable input[type="checkbox"]:checked').size()) {
                    $('#activateProblemSets').prop('disabled', false);
                } else {
                    $('#activateProblemSets').prop('disabled', true);
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
                    return '<%= rb.getString("click_to_see_problems")%>';
                }
            });

            $('a[rel="popoverstandardsProblems"]').popover({
                html: false,
                trigger: 'hover',
                container: 'body',
                placement: 'top',
                content: function () {
                    return '<%= rb.getString("click_to_see_problems_by_standards")%>';
                }
            });

            $('a[rel="popoverproblemsetSummary"]').popover({
                html: false,
                trigger: 'hover',
                container: 'body',
                placement: 'top'
            });
        }
        
        function dragElement(elmnt) {
        	  var pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
        	  if (document.getElementById(elmnt.id + "Hdr")) {
        	    /* if present, the header is where you move the DIV from:*/
        	    document.getElementById(elmnt.id + "Hdr").onmousedown = dragMouseDown;
        	  } else {
        	    /* otherwise, move the DIV from anywhere inside the DIV:*/
        	    elmnt.onmousedown = dragMouseDown;
        	  }

        	  function dragMouseDown(e) {
        	    e = e || window.event;
        	    e.preventDefault();
        	    // get the mouse cursor position at startup:
        	    pos3 = e.clientX;
        	    pos4 = e.clientY;
        	    document.onmouseup = closeDragElement;
        	    // call a function whenever the cursor moves:
        	    document.onmousemove = elementDrag;
        	  }

        	  function elementDrag(e) {
        	    e = e || window.event;
        	    e.preventDefault();
        	    // calculate the new cursor position:
        	    pos1 = pos3 - e.clientX;
        	    pos2 = pos4 - e.clientY;
        	    pos3 = e.clientX;
        	    pos4 = e.clientY;
        	    // set the element's new position:
        	    elmnt.style.top = (elmnt.offsetTop - pos2) + "px";
        	    elmnt.style.left = (elmnt.offsetLeft - pos1) + "px";
        	  }

        	  function closeDragElement() {
        	    /* stop moving when mouse button is released:*/
        	    document.onmouseup = null;
        	    document.onmousemove = null;
        	  }
        	}


        
        function registerAllEvents(){
            $('#wrapper').toggleClass('toggled');
//            $('#reorg_prob_sets_handler').css('background-color','#e6296f');
//            $('#reorg_prob_sets_handler').css('color', '#ffffff');

        	activeProblemSetsize = $('#activeproblemSetSize').val();
            if(activeProblemSetsize != 0){

        		dragElement(document.getElementById("activeProblemsFilter"));

        		activetable = $('#activateProbSetTable').DataTable({
                "bPaginate": false,
                <%=jc_rb.getString("language_text")%>
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
                        "width": "25%",
                        "orderable": false,
                    },
                    {
                        "targets": [ 2 ],
                        orderable: false,
                        "width": "10%",
                        'className': 'dt-body-center',
                        'render': function (data, type, full, meta){
                            return '<a  class="active" aria-expanded="true" aria-controls="collapseOne"><i class="glyphicon glyphicon-menu-down"></i></a>';
                        }
                    },
                    {
                        "targets": [ 2 ],
                        "orderable": false,
                        "width": "10%",
                        'className': 'dt-body-center',
                        'render': function (data, type, full, meta){
                            return '<a  class="activeStandards" aria-expanded="true" aria-controls="collapseOne"><i class="glyphicon glyphicon-menu-down"></i></a>';
                        }
                    },
                    {
                        "width": "25%",
                        "targets": [ 3 ],
                        "visible": false,
                        "orderable": false,

                    },
                    {
                        "targets": [ -2 ],
                        "orderable": false,
                        "width": "10%",
                        'className': 'dt-body-center'
                        
                    },
                    {
                        "targets": [ -1 ],
                        "orderable": false,
                        "width": "10%",
                        'className': 'dt-body-center',
                        'render': function (data, type, full, meta){
                            return '<input type="checkbox">';
                        }
                    },
                ]

            });
        	
        	 $(".active").click(function () {

        		if ((topicPassiveStandardInProgress === "passiveStandards") || (topicActiveStandardInProgress === "activeStandards")) {			
                	alert("<%= rb.getString("standards_selection_in_progress")%>  <%= rb.getString("please_close_that_window_first")%>");
        	       	return;
        		}            

                $(this).children(':first').toggleClass('rotate-icon');
                var tr = $(this).closest('tr');
                var row = activetable.row( tr );

                if ( row.child.isShown() ) {
                    row.child.hide();
                    topicActiveSelectionInProgress = "";
                }else{

        			topicActiveSelectionInProgress = "listall";
        			
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

        		
        	 $(".activeStandards").click(function () {

        		 	if ((topicActiveSelectionInProgress === "listall") || (topicPassiveSelectionInProgress === "listall")) {
        	        	alert("<%= rb.getString("full_list_selection_in_progress")%>  <%= rb.getString("please_close_that_window_first")%>");
        	        	return;
        	        }	        

        	        var test_tr = $(this).closest('tr');
        	        var testActiveStandardsRow = activetable.row( test_tr );
                    var testActiveStandardsRowID = '#'+testActiveStandardsRow.data()[0];

        	        if (testActiveStandardsRowID === activeStandardsRowID) { 
        	        	activeStandardsRowID = "";
        			 	$(this).children(':first').toggleClass('rotate-icon');
        	        	testActiveStandardsRow.child.hide();
                        $('#activeProblemsFilter').hide();
                        topicSelectionInProgress = "";
        	        	topicActiveStandardInProgress = "";

        	        }else{
        				if (activeStandardsRowID.length > 0) {
        		        	alert("<%= rb.getString("another_window_is_already_open")%>  <%= rb.getString("please_close_that_window_first")%>");
        		        	return;					
        				}
        				
        	        	$(this).children(':first').toggleClass('rotate-icon');
        		        var tr = $(this).closest('tr');
        		        activeStandardsRow = activetable.row( tr );
        	        	topicSelectionInProgress = "standards";
        	        	topicActiveStandardInProgress = "activeStandards";
        	            activeStandardsRowID = '#'+activeStandardsRow.data()[0];
        	            
        	            $.ajax({
        	                type : "POST",
        	                url :pgContext+"/tt/tt/getProblemForProblemSets",
        	                data : {
        	                    problemID: activeStandardsRow.data()[3],
        	                    classid: classID
        	                },
        	                success : function(response) {
                            	activeStandardsResponse = response;
        	                    if (activeStandardsResponse.includes("***")) {
        	                        $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
        	                        $('#errorMsgModelPopup').modal('show');
        	                    }else {
        	                        activeStandardsJSONData = JSON.parse(activeStandardsResponse);
        	                        activeStandardsTopicStandards = activeStandardsJSONData["topicStandars"];
        	                        activeStandardsTopicStandardsSorted = activeStandardsJSONData["topicStandardsSorted"];
        	                        var topicStandardsSorted = activeStandardsJSONData["topicStandardsSorted"];
        							var activeStandardsList = "";
        	                        $.each(activeStandardsTopicStandardsSorted, function (i, obj) {	                        	
        	                        	var strcode = "" + obj.code[0];
        	                       		var icode = parseInt(strcode);
        	                       		if ((icode >= lowGradeLevel) && (icode <= highGradeLevel)) {
        	                        		activeStandardsList += '<option  id="std_' + obj.code + '" value="' + obj.code + '">' + obj.code + '</option>';
        	                       		}
        	                        });

        	                        document.getElementById('activeStandardsList').innerHTML = activeStandardsList;
        	        	        	$('#activeProblemsFilter').show();
        	                        document.getElementById("activeStandardsList").focus();
        	                    }
        	                }
        	            });

        	        }
        	    });

            }

            inactiveProblemSetsize = $('#inactiveproblemSetsSize').val();
            if(inactiveProblemSetsize != 0){

        		dragElement(document.getElementById("passiveProblemsFilter"));

        		inactivetable = $('#inActiveProbSetTable').DataTable({
                "bPaginate": false,
                <%=jc_rb.getString("language_text")%>
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
                        "width": "25%"
                    },
                    {
                        "targets": [ 2 ],
                        orderable: false,
                        "width": "10%",
                        'className': 'dt-body-center',
                        'render': function (data, type, full, meta){
                            return '<a  class="passive" aria-expanded="true" aria-controls="collapseOne"><i class="glyphicon glyphicon-menu-down"></i></a>';
                        }
                    },
                    {
                        "targets": [ 2 ],
                        "orderable": false,
                        "width": "10%",
                        'className': 'dt-body-center',
                        'render': function (data, type, full, meta){
                            return '<a  class="passiveStandards" aria-expanded="true" aria-controls="collapseOne"><i class="glyphicon glyphicon-menu-down"></i></a>';
                        }
                    },
                    {
                        "width": "25%",
                        "targets": [ 3 ],
                        "visible": false

                    },
                    {
                        "targets": [ -2 ],
                        "orderable": false,
                        "width": "10%",
                        'className': 'dt-body-center'
                    },
                    {
                        "targets": [ -1 ],
                        "width": "10%",
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
            	
        		if ((topicPassiveStandardInProgress === "passiveStandards") || (topicActiveStandardInProgress === "activeStandards")) {			
                	alert("<%= rb.getString("standards_selection_in_progress")%><%= rb.getString("please_close_that_window_first")%>");
        		   	return;
        		}            
        		 
                $(this).children(':first').toggleClass('rotate-icon');
                var tr = $(this).closest('tr');
                var row = inactivetable.row( tr );

                if ( row.child.isShown() ) {
                    row.child.hide();
                    topicPassiveSelectionInProgress = "";
                }else{

        			topicPassiveSelectionInProgress = "listall";
        			
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

        	 $(".passiveStandards").click(function () {

        		 	if ((topicActiveSelectionInProgress === "listall") || (topicPassiveSelectionInProgress === "listall")) {
        	        	alert("<%= rb.getString("full_list_selection_in_progress")%><%= rb.getString("please_close_that_window_first")%>");
        	        	return;
        	        }	        

        	        var test_tr = $(this).closest('tr');
        	        var testPassiveStandardsRow = inactivetable.row( test_tr );
                    var testPassiveStandardsRowID = '#'+testPassiveStandardsRow.data()[0];

        	        if (testPassiveStandardsRowID === passiveStandardsRowID) { 
        	        	passiveStandardsRowID = "";
        			 	$(this).children(':first').toggleClass('rotate-icon');
        	        	testPassiveStandardsRow.child.hide();
                        $('#passiveProblemsFilter').hide();
                        topicSelectionInProgress = "";
        	        	topicPassiveStandardInProgress = "";

        	        }else{
        				if (passiveStandardsRowID.length > 0) {
        		        	alert("<%= rb.getString("another_window_is_already_open")%>  <%= rb.getString("please_close_that_window_first")%>");
        		        	return;					
        				}
        				
        	            $(this).children(':first').toggleClass('rotate-icon');
        		        var tr = $(this).closest('tr');
        		        passiveStandardsRow = inactivetable.row( tr );
        	        	topicSelectionInProgress = "standards";
        	        	topicPassiveStandardInProgress = "passiveStandards";
        	            passiveStandardsRowID = '#'+passiveStandardsRow.data()[0];

        	            $.ajax({
        	                type : "POST",
        	                url :pgContext+"/tt/tt/getProblemForProblemSets",
        	                data : {
        	                    problemID: passiveStandardsRow.data()[3],
        	                    classid: classID
        	                },
        	                success : function(response) {
                         	passiveStandardsResponse = response;
        	                    if (passiveStandardsResponse.includes("***")) {
        	                        $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
        	                        $('#errorMsgModelPopup').modal('show');
        	                    }else {
        	                        passiveStandardsJSONData = JSON.parse(passiveStandardsResponse);
        	                        passiveStandardsTopicStandards = passiveStandardsJSONData["topicStandars"];
        	                        passiveStandardsTopicStandardsSorted = passiveStandardsJSONData["topicStandardsSorted"];
        	                        
        							var passiveStandardsList = "";
        	                        $.each(passiveStandardsTopicStandardsSorted, function (i, obj) {
        	                        	var strcode = "" + obj.code[0];
        	                       		var icode = parseInt(strcode);
        	                       		if ((icode >= lowGradeLevel) && (icode <= highGradeLevel)) {
        	                        		passiveStandardsList += '<option  id="std_' + obj.code + '" value="' + obj.code + '">' + obj.code + '</option>';
        	                       		}
        	                        });
        	                        
        	                        document.getElementById('passiveStandardsList').innerHTML = passiveStandardsList;
        	                        $('#passiveProblemsFilter').show();
        	                        document.getElementById("passiveStandardsList").focus();
        	                    }
        	                }
        	            });

        	        }
        	    });    
            
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

            
            $("#successMsgModelPopupForProblemSets").find("[class*='btn btn-default']").click(function () {
                var newlocation = pgContext+'/tt/tt/viewClassDetails?classId='+classID+'&currentSelection=classHomePage~manage_topics';
                $(location).attr('href', newlocation);
            });
            $("#successMsgModelPopupForProblemSets").find("[class*='close']").click(function () {
                var newlocation = pgContext+'/tt/tt/viewClassDetails?classId='+classID+'&currentSelection=classHomePage~manage_topics';
                $(location).attr('href', newlocation);
            });
        	

        }
               
        $(document).ready(function () {
        	
			hasClusters = parseInt('${classInfo.hasClusters}');
			isCluster = parseInt('${classInfo.isCluster}');
			classColor = '${classInfo.color}';
			className = '${classInfo.name}';
			currentSelection = '${currentSelection}';
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
   			document.getElementById("classGrades").innerText = "<%= rb.getString("grade_levels")%> : [" + displayem + "]";

   			registerAllEvents();
            handleclickHandlers();

           	var titleClassname = "";
            if (isCluster == 0) {                        
            	titleClassname = 'home-title-' + '${classInfo.color}';
            	document.getElementById("titleLine").innerHTML =  '<%= rb.getString("manage_problem_sets") %> : <span class="' +  titleClassname + '">&nbsp;&nbsp;<strong>${classInfo.name}</strong>&nbsp;&nbsp;</span>';
        	}
            else {
            	document.getElementById("titleLine").innerHTML =  '<%= rb.getString("manage_problem_sets") %> : <span class="' +  titleClassname + '">&nbsp;&nbsp;<strong>${classInfo.name}</strong>&nbsp;&nbsp;</span>';
            	titleClassname = 'home-title-' + '${classInfo.color}' + '-cluster';                	
            }

            $("#content-conatiner").children().hide();
            $("#problem_set_content").show();
                                
        });


        
        
        
        
        
        
        function problemDetails(data, response) {
            var JSONData = JSON.parse(response);
            var standards = JSONData["topicStandars"];
            var standardsSorted = JSONData["topicStandardsSorted"];
            var problems = JSONData["problems"];
            var html = "";
            $.each(standardsSorted, function (i, obj) {
            	var stdClass = "" + obj.code;
            	var stdGrade = stdClass.substring(0,2);
            	if (gradesLevelsUsedInThisClass.indexOf(stdGrade) >= 0) {
                	html += '<span style="margin-right: 10px;"><a href=' + obj.url + '>' + obj.code + '</a></span>';
            	}
            });

            var saveselector = "#"+JSONData["problemLevelId"]+"_full_save_handler";

            $(document.body).on('click', saveselector ,function(){
                var rows = $("#"+JSONData["problemLevelId"]).dataTable(
                    { "bPaginate": false,
                        <%=jc_rb.getString("language_text")%>
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

            var closeSelector = "#"+JSONData["problemLevelId"]+"_full_close_handler";

            $(document.body).on('click', closeSelector ,function(){
            	var newlocation = pgContext+'/tt/tt/viewClassDetails?classId='+classID+'&currentSelection=classManager';
                $(location).attr('href', newlocation);
                        

            });	


            
            var selectallSelector = "#"+JSONData["problemLevelId"]+"_full_selectall_handler";    
            
            $(document.body).on('click', selectallSelector ,function(){
                var rows = $("#"+JSONData["problemLevelId"]).dataTable(
                    { "bPaginate": false,
                        <%=jc_rb.getString("language_text")%>
                        "bFilter": false,
                        "bLengthChange": false,
                        rowReorder: false,
                        "bSort": false

                        
                    }).fnGetNodes();

                var problemIds = [""];
                
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
          
            
            var deselectallSelector = "#"+JSONData["problemLevelId"]+"_full_deselectall_handler";    
            
            $(document.body).on('click', deselectallSelector ,function(){
                var rows = $("#"+JSONData["problemLevelId"]).dataTable(
                    { "bPaginate": false,
                        <%=jc_rb.getString("language_text")%>
                        "bFilter": false,
                        "bLengthChange": false,
                        rowReorder: false,
                        "bSort": false

                        
                    }).fnGetNodes();

                var j=0;
                var rowsArray = [];
                var problemIds = [""];

                var i = 0;
               
                $("input:checkbox:not(:checked)", rows).each(function(){
                    rowsArray[i] = $(this).closest('tr');
                    i++;
                });

                $("input:checkbox:checked", rows).each(function(){
                    rowsArray[i] = $(this).closest('tr');
                    i++;
                });
                
                for(j=0; j < rowsArray.length; j++)
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
            
            
               
            
        var save_selections = "<%= rb.getString("save_selections")%>";
        var close_selections = "<%= rb.getString("close_cancel")%>";
        var activate_save_all = "<%= rb.getString("activate_save_all")%>";
        var deactivate_save_all = "<%= rb.getString("deactivate_save_all")%>";
        var higherlevelDetailp1="<%= rb.getString("problem_set")%>";
        var higherlevelDetailp2="<%= rb.getString("standards_covered_in_problemset")%>";
        var higherlevelDetailp3="<%= rb.getString("student_will_see_selected_problems")%>";
        var summaryLabel="<%= rb.getString("summary")%>"; 
        var higherlevelDetail = "<div id=" + data[0] + " class='panel-body animated zoomOut full_detail_table_bg'> " +
            " <div class='panel panel-default full_detail_table_bg'> <div class='panel-body'><strong>"+higherlevelDetailp1+": " + JSONData["topicName"] + "</strong></div> " +
            " <div class='panel-body full_detail_table_bg'><strong>"+higherlevelDetailp2+": " + html + "</strong></div>" +
            " <div class='panel-body full_detail_table_bg'><strong>"+summaryLabel+": " + JSONData["topicSummary"] + "</strong></div>"+
            "<div class='panel-body full_detail_table_bg'>"+higherlevelDetailp3+"</div>"+
            "<div class='panel-body full_detail_table_bg'>"+
            "<button id="+JSONData["problemLevelId"]+'_full_save_handler'+" class='btn btn-primary btn-lg' aria-disabled='true'>"+save_selections+"</button>&nbsp;&nbsp;"+
            "<button id="+JSONData["problemLevelId"]+'_full_selectall_handler'+"  class='btn btn-primary btn-lg' aria-disabled='true'>"+activate_save_all+"</button>&nbsp;&nbsp;"+
            "<button id="+JSONData["problemLevelId"]+'_full_deselectall_handler'+"  class='btn btn-primary btn-lg hidden' aria-disabled='true'>"+deactivate_save_all+"</button>&nbsp;&nbsp;"+
            "<button id="+JSONData["problemLevelId"]+'_full_close_handler'+" class='btn btn-primary btn-lg' aria-disabled='true'>"+close_selections+"</button>&nbsp;&nbsp;"+
            "</div>"+
            "</div>";

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


        var selectedStandard = "";

        function handleActiveStandardsSelect() {
            var selected = $('#activeStandardsList').val();
            selectedStandard = "";
            $.each(selected, function (i, std) {
            	if (i > 0) {
            		selectedStandard = selectedStandard + ',';
            	}
            	selectedStandard = selectedStandard + std;
            });

        	activeStandardsChild = standardsProblemDetails(activeStandardsRow.data(), activeStandardsResponse, selectedStandard);	                    	
        	activeStandardsRow.child(activeStandardsChild).show();	                    	
            $('a[rel=popoverPerProblem]').popover({
                html: true,
                trigger: 'hover',
                placement: 'top',
                container: 'body',
                content: function () {
                    return '<img  style="max-width:400px; max-height:400px;" src="' + $(this).data('img') + '" />';
                }
            });
            $(activeStandardsRowID).toggleClass('zoomIn zoomOut');

        }

        function handleActiveStandardsReset() {
         
            var elements = document.getElementById("activeStandardsList").options;
            for(var i = 0; i < elements.length; i++){
              if(elements[i].selected)
        	    elements[i].selected = false;
            }
        }


        var activeHelpDisplayed = 0;
        function displayActiveStandardsHelp() {
        	if (activeHelpDisplayed == 1) {
        		document.getElementById("activeStandardsPopupHelp").style.visibility = 'hidden';		
        		activeHelpDisplayed = 0;
        	}
        	else {
        		document.getElementById("activeStandardsPopupHelp").style.visibility = 'visible';
        		activeHelpDisplayed = 1;
        	}
        }


        function handlePassiveStandardsSelect() {
            selectedStandard = "";

            var selected = $('#passiveStandardsList').val();
            selectedStandard = "";
            $.each(selected, function (i, std) {
            	if (i > 0) {
            		selectedStandard = selectedStandard + ',';
            	}
            	selectedStandard = selectedStandard + std;
            });

            passiveStandardsChild = standardsProblemDetails(passiveStandardsRow.data(), passiveStandardsResponse, selectedStandard);	                    	
        	passiveStandardsRow.child(passiveStandardsChild).show();	                    	
            $('a[rel=popoverPerProblem]').popover({
                html: true,
                trigger: 'hover',
                placement: 'top',
                container: 'body',
                content: function () {
                    return '<img  style="max-width:400px; max-height:400px;" src="' + $(this).data('img') + '" />';
                }
            });
            $(passiveStandardsRowID).toggleClass('zoomIn zoomOut');

        }

        function handlePassiveStandardsReset() {
        	 
            var elements = document.getElementById("passiveStandardsList").options;
            for(var i = 0; i < elements.length; i++){
              if(elements[i].selected)
        	    elements[i].selected = false;
            }
        }

        var passiveHelpDisplayed = 0;
        function displayPassiveStandardsHelp() {
        	if (passiveHelpDisplayed == 1) {
        		document.getElementById("passiveStandardsPopupHelp").style.visibility = 'hidden';		
        		passiveHelpDisplayed = 0;
        	}
        	else {
        		document.getElementById("passiveStandardsPopupHelp").style.visibility = 'visible';
        		passiveHelpDisplayed = 1;
        	}
        }

        function activeStandardsFilter() {
        	  var div, input, filter, ul, li, a, i;
        	  input = document.getElementById("myActiveInput");
        	  filter = input.value.toUpperCase();
        	  div = document.getElementById("activeStandardsList");
        	  a = div.getElementsByTagName("option");
        	  for (i = 0; i < a.length; i++) {
        	    var txtValue = a[i].textContent || a[i].innerText;
        	    if (txtValue.toUpperCase().indexOf(filter) > -1) {
        	      a[i].style.display = "";
        	    } else {
        	      a[i].style.display = "none";
        	    }
        	  }
        	}
        	
        function passiveStandardsFilter() {
        	  var div, input, filter, ul, li, a, i;
        	  input = document.getElementById("myPassiveInput");
        	  filter = input.value.toUpperCase();
        	  div = document.getElementById("passiveStandardsList");
        	  a = div.getElementsByTagName("option");
        	  for (i = 0; i < a.length; i++) {
        	    var txtValue = a[i].textContent || a[i].innerText;
        	    if (txtValue.toUpperCase().indexOf(filter) > -1) {
        	      a[i].style.display = "";
        	    } else {
        	      a[i].style.display = "none";
        	    }
        	  }
        	}
        	

        function standardsProblemDetails(data, response, state) {

            var JSONData = JSON.parse(response);
            var standards = JSONData["topicStandars"];
            var standardsSorted = JSONData["topicStandardsSorted"];
            var problems = JSONData["problems"];
            var html = "";
            var html = "";
            $.each(standardsSorted, function (i, obj) {
            	var stdClass = "" + obj.code;
            	var stdGrade = stdClass.substring(0,2);
            	if (gradesLevelsUsedInThisClass.indexOf(stdGrade) >= 0) {
                	html += '<span style="margin-right: 10px;"><a href=' + obj.url + '>' + obj.code + '</a></span>';
            	}
            });

            
            var saveSelector = "#"+JSONData["problemLevelId"]+"_save_handler";

            $(document.body).on('click', saveSelector ,function(){
                var rows = $("#"+JSONData["problemLevelId"]).dataTable(
                    { "bPaginate": false,
                        <%=jc_rb.getString("language_text")%>
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

            var closeSelector = "#"+JSONData["problemLevelId"]+"_close_handler";

            $(document.body).on('click', closeSelector ,function(){
            	var newlocation = pgContext+'/tt/tt/viewClassDetails?classId='+classID+'&currentSelection=classManager';
                $(location).attr('href', newlocation);
                        

            });	


            
            var selectallSelector = "#"+JSONData["problemLevelId"]+"_selectall_handler";    
            
            $(document.body).on('click', selectallSelector ,function(){
                var rows = $("#"+JSONData["problemLevelId"]).dataTable(
                    { "bPaginate": false,
                        <%=jc_rb.getString("language_text")%>
                        "bFilter": false,
                        "bLengthChange": false,
                        rowReorder: false,
                        "bSort": false

                        
                    }).fnGetNodes();

                var problemIds = [""];
                
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
          
            
            var deselectallSelector = "#"+JSONData["problemLevelId"]+"_deselectall_handler";    
            
            $(document.body).on('click', deselectallSelector ,function(){
                var rows = $("#"+JSONData["problemLevelId"]).dataTable(
                    { "bPaginate": false,
                        <%=jc_rb.getString("language_text")%>
                        "bFilter": false,
                        "bLengthChange": false,
                        rowReorder: false,
                        "bSort": false

                        
                    }).fnGetNodes();

                var j=0;
                var rowsArray = [];
                var problemIds = [""];
                var i = 0;
                $("input:checkbox:not(:checked)", rows).each(function(){
                    rowsArray[i] = $(this).closest('tr');
                    i++;
                });

                $("input:checkbox:checked", rows).each(function(){
                    rowsArray[i] = $(this).closest('tr');
                    i++;
                });
                
                for(j=0; j < rowsArray.length; j++)
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
            
            
            
            
        	var save_selections = "<%= rb.getString("save_selections")%>";
        	var close_selections = "<%= rb.getString("close_cancel")%>";
        	var activate_save_all = "<%= rb.getString("activate_save_all")%>";
        	var deactivate_save_all = "<%= rb.getString("deactivate_save_all")%>";
        	var higherlevelDetailp1="<%= rb.getString("problem_set")%>";
        	var higherlevelDetailp2="<%= rb.getString("standards_covered_in_problemset")%>";
        	var higherlevelDetailp3="<%= rb.getString("student_will_see_selected_problems")%>";
        	var summaryLabel="<%= rb.getString("summary")%>"; 
        	var higherlevelDetail = "<div id=" + data[0] + " class='panel-body animated zoomOut standards_detail_table_bg'> " +
            " <div class='panel panel-default standards_detail_table_bg'> <div class='panel-body'><strong>"+higherlevelDetailp1+": " + JSONData["topicName"] + "</strong></div> " +
            " <div class='panel-body standards_detail_table_bg'><strong>"+higherlevelDetailp2+": " + html + "</strong></div>" +
            " <div class='panel-body standards_detail_table_bg'><strong>"+summaryLabel+": " + JSONData["topicSummary"] + "</strong></div>"+
            "<div class='panel-body standards_detail_table_bg'>"+higherlevelDetailp3+"</div>"+
            "<div class='panel-body standards_detail_table_bg'>"+
            "<button id="+JSONData["problemLevelId"]+'_save_handler'+" class='btn btn-primary btn-lg' aria-disabled='true'>"+save_selections+"</button>&nbsp;&nbsp;"+
            "<button id="+JSONData["problemLevelId"]+'_selectall_handler'+"  class='btn btn-primary btn-lg' aria-disabled='true'>"+activate_save_all+"</button>&nbsp;&nbsp;"+
            "<button id="+JSONData["problemLevelId"]+'_deselectall_handler'+"  class='btn btn-primary btn-lg hidden' aria-disabled='true'>"+deactivate_save_all+"</button>&nbsp;&nbsp;"+
            "<button id="+JSONData["problemLevelId"]+'_close_handler'+" class='btn btn-primary btn-lg' aria-disabled='true'>"+close_selections+"</button>&nbsp;&nbsp;"+
            "</div>"+
            "</div>";

        	return higherlevelDetail + standardsProblemLevelDetails(JSONData,problems,selectedStandard);
            
        }

        function standardsProblemLevelDetails(JSONData,problems, selectedStd){
            var tableHeader = '<table id='+JSONData["problemLevelId"]+' class="table table-striped table-bordered hover" cellspacing="0" width="100%"><thead><tr><th><%= rb.getString("activated")%></th><th><%= rb.getString("id")%></th><th><%= rb.getString("name")%></th><th><%= rb.getString("nickname")%></th><th><%= rb.getString("difficulty")%></th><th><%= rb.getString("cc_standard")%></th><th>Type</th></tr></thead><tbody>';
            var attri = ", 'ProblemPreview'"+","+"'width=750,height=550,status=yes,resizable=yes'";
//            selectedStd = "8.G.B.8";
            $.each(problems, function (i, obj) {
            	var foundStandard = false;
                var html = "";
                
                var flash = "";
                var checkBox = "";
                var flashWindow = "'" + JSONData["uri"]+"?questionNum="+obj.problemNo + "'" + attri ;
                var htmlWindow =  "'" + JSONData["html5ProblemURI"]+obj.htmlDirectory+"/"+obj.resource+ "'" + attri;
                var imageURL = problem_imageURL+obj['id']+'.jpg';
                $.each(obj.ccStand, function (i, obj) {
                	if (selectedStd.indexOf(obj.idABC) > -1) {
                		foundStandard = true;
                	}
                    html += '<span style="margin-right: 10px;"><a href=' + obj.url + '>' + obj.code + '</a></span>';
                });
                if (foundStandard) {
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
                }
            });
            return tableHeader + "</tbody></table></div>";
        }
        
       
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

            <li><a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/viewClassDetails?classId=${classInfo.classid}&currentSelection=manage_roster_handler" id="manage_roster_handler"><i class="fa fa-fw fa-users"></i> <%= rb.getString("manage_class_roster") %></a></li>

            <li><a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/viewClassDetails?classId=${classInfo.classid}&currentSelection=manage_student_info_handler" id="manage_student_info_handler"><i class="fa fa-fw fa-users"></i> <%= rb.getString("manage_student_info") %></a></li>

            <li><a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/viewClassDetails?classId=${classInfo.classid}&currentSelection=manage_class_handler" id="manage_class_handler"><i class="fa fa-fw fa-cog"></i> <%= rb.getString("manage_class") %></a></li>

            <!-- <li><a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/viewClassDetails?classId=${classInfo.classid}&currentSelection=resetSurveySettings_handler" id="resetSurveySettings_handler"><i class="fa fa-fw fa-cog"></i><%= rb.getString("survey_settings") %></a></li> -->
            
            <li><a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/viewClassDetails?classId=${classInfo.classid}&currentSelection=content_apply_handler" id="content_apply_handler"><i class="fa fa-fw fa-cogs"></i><%= rb.getString("apply_class_content") %></a></li>
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


			<div id="problem_set_content" style="width: 100%;">
            <div class="loader" style="display: none" ></div>               
			<input type="hidden" id="activeproblemSetSize" name="activeproblemSetSize" value="${activeproblemSet.size()}">
			<input type="hidden" id="inactiveproblemSetsSize" name="inactiveproblemSetsSize" value="${inactiveproblemSets.size()}">
			<c:if test="${activeproblemSet.size() != 0}">
                <div>
                    <h3 class="tt-page-header">
                        <small><%= rb.getString("active_problem_sets") %></small>
                    </h3>
					<div id="activeProblemsFilter" class="activeCenterDiv report_filters" style="display:none;">
  						<div id="activeProblemsFilterHdr" class="activeCenterDivHdr"><%= rb.getString("click_here_to_drag") %></div>
						<label><%= rb.getString("select_active_standard") %></label>
                     	<input type="text" placeholder="<%= rb.getString("search") %>.." id="myActiveInput" multiple onkeyup="activeStandardsFilter()">
                        <select name="activeStandardsList" id="activeStandardsList" class="form-control selectpicker"  multiple data-show-subtext="true" data-live-search="true" size="6" style="width: 200px;">  
                       	</select>
                       	<button id="activeFilterSubmit" class="btn btn-primary btn-sm" aria-disabled="true" onclick="handleActiveStandardsSelect()"><%= rb.getString("show_problems") %></button> 
                       	<button id="activeFilterClear" class="btn btn-danger btn-sm" aria-disabled="true" onclick="handleActiveStandardsReset()"><%= rb.getString("reset") %></button> 
                       	<button id="activeFilterHelp" class="btn btn-info btn-sm" aria-disabled="true" onclick="displayActiveStandardsHelp()"><%= rb.getString("help") %></button> 
						<div id="activeStandardsPopupHelp" class="standardsHelpBox report_filters_help" style="visibility: hidden;">
							<label><%= rb.getString("popup_btn_help_hdr") %></label><br>
							<label><%= rb.getString("popup_btn_help_4") %></label><br>
							<label><%= rb.getString("popup_btn_help_1") %></label><br>
							<label><%= rb.getString("popup_btn_help_2") %></label><br>
							<label><%= rb.getString("popup_btn_help_3") %></label><br>
							
						</div>
					</div>

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
                            <th rowspan="2" align="center"><span><%= rb.getString("order") %>&nbsp;&nbsp;</span><a rel="popoverOrder"><i class="fa fa-question-circle-o" aria-hidden="true"></i></a></th>
                            <th rowspan="2"><%= rb.getString("problem_set") %></th>
                            <th rowspan="2"><%= rb.getString("choose_from_full_list") %><span>&nbsp;</span><a rel="popoveractivatedProblems"><i class="fa fa-question-circle-o" aria-hidden="true"></i></a></th>
                            <th rowspan="2"><%= rb.getString("problem_id") %></th>
                            <th rowspan="2"><%= rb.getString("choose_by_standards") %><span>&nbsp;</span><a rel="popoverstandardsProblems"><i class="fa fa-question-circle-o" aria-hidden="true"></i></a></th>
                            <th style="text-align: center;" colspan="<c:out value='${activeproblemSetHeaders.size()}'/>"><%= rb.getString("gradewise_distribution") %></th>
                            <th rowspan="2"><%= rb.getString("number_of_problems") %></th>
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
	                                    <a  class="active" aria-expanded="true" aria-controls="collapseOne">
	                                        <i class="glyphicon glyphicon-menu-down"></i>
	                                    </a>
	                                </td>
	                                <td>${problemSet.id}</td>
	                                <td>
	                                    <a  class="activeStandards" aria-expanded="true" aria-controls="collapseOne">
	                                        <i class="glyphicon glyphicon-menu-down"></i>
	                                    </a>
	                                </td>
	                                <c:forEach var="problemSetHeaders" items="${activeproblemSetHeaders}">
	                                    <td><c:out value="${gradeWiseProbNos[problemSetHeaders.key]}"/></td>
	                                </c:forEach>
	                                
	                                <td>${problemSet.numProbs}</td>
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

					<div id="passiveProblemsFilter" class="passiveCenterDiv report_filters" style="display:none;">
  						<div id="passiveProblemsFilterHdr" class="passiveCenterDivHdr"><%= rb.getString("click_here_to_drag") %></div>
						<label><%= rb.getString("select_inactive_standard") %></label>
                     	<input type="text" placeholder="<%= rb.getString("search") %>.." id="myPassiveInput" multiple onkeyup="passiveStandardsFilter()">
                        <select name="passiveStandardsList" id="passiveStandardsList" class="form-control selectpicker"  multiple data-show-subtext="true" data-live-search="true" size="6" style="width: 200px;">  
                       	</select>
                       	<button id="passiveFilterSubmit" class="btn btn-primary btn-sm" onclick="handlePassiveStandardsSelect()"><%= rb.getString("show_problems") %></button> 
                       	<button id="passiveFilterClear" class="btn btn-danger btn-sm" aria-disabled="true" onclick="handlePassiveStandardsReset()"><%= rb.getString("reset") %></button> 
                       	<button id="passiveFilterHelp" class="btn btn-info btn-sm" aria-disabled="true" onclick="displayPassiveStandardsHelp()"><%= rb.getString("help") %></button> 
						<div id="passiveStandardsPopupHelp" class="standardsHelpBox report_filters_help" style="visibility: hidden;">
							<label><%= rb.getString("popup_btn_help_hdr") %></label><br>
							<label><%= rb.getString("popup_btn_help_4") %></label><br>
							<label><%= rb.getString("popup_btn_help_1") %></label><br>
							<label><%= rb.getString("popup_btn_help_2") %></label><br>
							<label><%= rb.getString("popup_btn_help_3") %></label><br>
							
						</div>
					</div>

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
                            <th rowspan="2"><%= rb.getString("choose_from_full_list") %><span>&nbsp;</span><a rel="popoveractivatedProblems"><i class="fa fa-question-circle-o" aria-hidden="true"></i></a></th>
                            <th rowspan="2"><%= rb.getString("problem_id") %></th>
                            <th rowspan="2"><%= rb.getString("choose_by_standards") %><span>&nbsp;</span><a rel="popoverstandardsProblems"><i class="fa fa-question-circle-o" aria-hidden="true"></i></a></th>
                            <th style="text-align: center;" colspan="<c:out value='${inActiveproblemSetHeaders.size()}'/>"><%= rb.getString("gradewise_distribution") %></th>
                            <th rowspan="2"><%= rb.getString("number_of_problems") %></th>
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
	                                    <a  class="passive" aria-expanded="true" aria-controls="collapseOne">
	                                        <i class="glyphicon glyphicon-menu-down"></i>
	                                    </a>
	                                </td>
	                                <td>${problemSet.id}</td>
	                                <td>
	                                    <a  class="passiveStandards" aria-expanded="true" aria-controls="collapseOne">
	                                        <i class="glyphicon glyphicon-menu-down"></i>
	                                    </a>
	                                
	                                </td>
	                                <c:forEach var="problemSetHeaders" items="${inActiveproblemSetHeaders}">
	                                    <td><c:out value="${gradeWiseProbNo[problemSetHeaders.key]}"/></td>
	                                </c:forEach>

	                                <td>${problemSet.numProbs}</td>
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
<div id = "statusMessage" class="spin-loader-message" align = "center" style="display: none;"></div>


</html>