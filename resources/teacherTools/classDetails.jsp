<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="springForm" uri="http://www.springframework.org/tags/form" %>
<%@page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>
<% 

Locale loc = request.getLocale();
String lang = loc.getDisplayLanguage();

ResourceBundle rb = null;
try {
	rb = ResourceBundle.getBundle("MathSpring",loc);
}
catch (Exception e) {
//	logger.error(e.getMessage());
}
ResourceBundle dataTable_rb = null;
try {
	dataTable_rb = ResourceBundle.getBundle("dataTable",loc);
}
catch (Exception e) {
//	logger.error(e.getMessage());
}
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
    <link href="${pageContext.request.contextPath}/css/ttStyleMain.css" rel="stylesheet">

    <!-- Datatables Css Files -->
    <link href="https://cdn.datatables.net/1.10.15/css/dataTables.bootstrap4.min.css" rel="stylesheet" type="text/css">
    <link href="https://cdn.datatables.net/rowreorder/1.2.0/css/rowReorder.dataTables.min.css" rel="stylesheet"
          type="text/css">
    <link href="https://cdn.datatables.net/select/1.2.1/css/select.dataTables.min.css" rel="stylesheet"
          type="text/css">

    <style>
        .buttonCustomColor {
            color: #FFFFFF;
        }
    </style>

    <script type="text/javascript" src="<c:url value="/js/bootstrap/js/jquery-2.2.2.min.js" />"></script>
    <!-- js for bootstrap-->
    <script type="text/javascript" src="<c:url value="/js/bootstrap/js/bootstrap.min.js" />"></script>
    <script src="<c:url value="/js/jquery-ui-1.10.4.custom/js/jquery-ui-1.10.4.custom.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="https://cdn.datatables.net/1.10.15/js/jquery.dataTables.min.js" />"></script>


    <script type="text/javascript"
            src="<c:url value="https://cdn.datatables.net/1.10.15/js/dataTables.bootstrap4.min.js" />"></script>
    <script type="text/javascript"
            src="<c:url value="https://cdn.datatables.net/rowreorder/1.2.0/js/dataTables.rowReorder.min.js" />"></script>
    <script type="text/javascript"
            src="<c:url value="https://cdn.datatables.net/select/1.2.1/js/dataTables.select.min.js" />"></script>

    <script type="text/javascript"
            src="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.bundle.min.js" />"></script>
    <script type="text/javascript"
            src="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js" />"></script>

    
    
    <%@ include file="/ttReportScripts_new.jsp" %>
	<!-- Replaces
	<script type="text/javascript" src="<c:url value="/js/ttReportScripts.js" />"></script>
	-->
	
	
	
	 
    <script type="text/javascript" src="<c:url value="/js/bootstrap/js/language_es.js" />"></script>    
    <script type="text/javascript">
        var servletContextPath = "${pageContext.request.contextPath}";
        var pgContext = '${pageContext.request.contextPath}';
        var classID = '${classInfo.classid}';
        var teacherID = '${teacherId}';
        var prePostIds = '${prepostIds}'.split("~~");		
        var problem_imageURL = '${webContentpath}'+'problemSnapshots/prob_';
        $(document).ready(function () {
            registerAllEvents();
            handleclickHandlers();

            $('#cnfirmPasswordToDownLoadTag').on('hidden.bs.modal', function(){
                $(this).find('form')[0].reset();
            });

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
/**			langPrefrenceForDetailsPage(); */
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



        var tabPanel = '<div style="width: 40%"> <ul class="nav nav-tabs" role="tablist"> <li class="active"> ' +
            '<a href="#home'+id+'" role="tab" data-toggle="tab"> <i class="fa fa-address-card-o" aria-hidden="true"></i> <%= rb.getString("update_student_information") %> </a> </li> ' +
            '<li><a href="#profile'+id+'" role="tab" data-toggle="tab"> <i class="fa fa-key" aria-hidden="true"></i> <%= rb.getString("reset_password_for_student") %> </a> </li> </ul>'+
            '<div class="tab-content"> <div class="tab-pane fade active in" id="home'+id+'">'+formHtml+'</div><div class="tab-pane fade" id="profile'+id+'">'+formHtmlPassWord+'</div> </div> </div>';

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
                        <a href="#"><i class="fa fa-fw fa-user"></i> <%= rb.getString("profile") %></a>
                    </li>
                    <li class="divider"></li>
                    <li>
                        <a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/logout"><i
                                class="fa fa-fw fa-power-off"></i><%= rb.getString("log_out") %>t</a>
                    </li>
                </ul>
            </li>
        </ul>
    </nav>
    <nav class="navbar navbar-inverse navbar-fixed-top" id="sidebar-wrapper" role="navigation">
        <ul class="nav sidebar-nav">
            <li>
                <a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/ttMain?teacherId=${teacherId}"><i
                        class="fa fa-fw fa-home"></i> <%= rb.getString("home") %></a>
            </li>

            <li>
                <a href="#" id="reports_handler"><i class="fa fa-bar-chart"></i> <%= rb.getString("class_report_card") %></a>
            </li>

            <li><a id="reorg_prob_sets_handler"><i class="fa fa-book"></i> <%= rb.getString("manage_problem_sets") %></a></li>

            <li><a id="reconfigure_student_handler"><i class="fa fa-fw fa-id-badge"></i> <%= rb.getString("manage_students") %></a></li>
            <li>
                <a href="#" id="copyClass_handler"><i class="fa fa-files-o"></i> <%= rb.getString("replicate_class") %></a>
            </li>

            <li><a id="resetSurveySettings_handler"><i class="fa fa-fw fa-cog"></i><%= rb.getString("survey_settings") %></a></li>
            
             <li><a id="content_apply_handler"><i class="fa fa-fw fa-cogs"></i><%= rb.getString("apply_class_content") %></a></li>

        </ul>
        <!-- /#sidebar-end -->
    </nav>
    <div id="page-content-wrapper">

        <h1 class="page-header">
            <strong>${classInfo.name}</strong>
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
			<input type="hidden" id="activeproblemSetSize" name="activeproblemSetSize" value="${activeproblemSet.size()}">
			<input type="hidden" id="inactiveproblemSetSize" name="inactiveproblemSetSize" value="${inactiveproblemSets.size()}">
			<c:if test="${activeproblemSet.size() != 0}">
                <div>
                    <h3 class="page-header">
                        <small><%= rb.getString("active_problem_sets") %></small>
                    </h3>

                    <div class="panel panel-default">
                        <div class="panel-body"><%= rb.getString("active_problem_sets_note1") %>
                        </div>
                        <div class="panel-body"><%= rb.getString("active_problem_sets_note2") %>
                        </div>
                        <div class="panel-body">
                            <button id="deacivateProblemSets" class="btn btn-primary btn-lg" aria-disabled="true" disabled="disabled"><%= rb.getString("deactivate_problem_sets") %></button>
                        </div>
                    </div>

                    <table id="activateProbSetTable" class="table table-striped table-bordered hover" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th rowspan="2" align="center"><span>Order&nbsp;&nbsp;</span><a rel="popoverOrder"><i class="fa fa-question-circle-o" aria-hidden="true"></i></a></th>
                            <th rowspan="2"><%= rb.getString("problem_set") %></th>
                            <th rowspan="2"><span><%= rb.getString("number_of_activated_problems") %>&nbsp;&nbsp;</span><a rel="popoveractivatedProblems"><i class="fa fa-question-circle-o" aria-hidden="true"></i></a></th>
                            <th rowspan="2"><%= rb.getString("problem_id") %></th>
                            <th style="text-align: center;" colspan="<c:out value="${activeproblemSetHeaders.size()}"/>"<%= rb.getString("gradewise_distribution") %></th>
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
                            <c:set var="gradeWiseProbNos" value="${problemSet.gradewiseProblemDistribution}"/>
                            <tr>
                                <td>${i.index + 1}</td>
                                <td>${problemSet.name}&nbsp;&nbsp;<a rel="popoverproblemsetSummary" data-content='${problemSet.summary}'><i class="fa fa-question-circle-o" aria-hidden="true"></i></a></td>
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
                        </c:forEach>
                        </tbody>
                       
                    </table>
					 
                </div>
				</c:if>
				<c:if test="${activeproblemSet.size() == 0}">
				 <div>
                    <h5 class="page-header">
                        <big><%= rb.getString("no_active_problem_sets_found") %></big>
                    </h5>
					</div>
				</c:if>
				<c:if test="${inactiveproblemSets.size() != 0}">
                <div>
                    <h3 class="page-header">
                        <small><%= rb.getString("inactive_problem_sets") %></small>
                    </h3>

                    <div class="panel panel-default">
                        <div class="panel-body"><%= rb.getString("deactive_problem_sets_note1") %>
                        </div>
                        <div class="panel-body">
                            <button id="acivateProblemSets" class="btn btn-primary btn-lg" disabled="disabled" aria-disabled="true"><%= rb.getString("activate_problem_sets") %></button>
                        </div>
                    </div>

                    <table id="inActiveProbSetTable" class="table table-striped table-bordered hover" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th rowspan="2"><%= rb.getString("order") %></th>
                            <th rowspan="2"><%= rb.getString("problem_set") %></th>
                            <th rowspan="2"><%= rb.getString("available_problems") %></th>
                            <th rowspan="2"><%= rb.getString("problem_id") %></th>
                            <th style="text-align: center;" colspan="<c:out value="${inActiveproblemSetHeaders.size()}"/>"<%= rb.getString("gradewise_distribution") %></th>
                            <th rowspan="2"><%= rb.getString("active_problem_sets") %></th>
                        </tr>
                        <tr>
                            <c:forEach var="problemSetHeaders" items="${inActiveproblemSetHeaders}">
                                <th style="border-right-width: 1px;">${problemSetHeaders.key}</th>
                            </c:forEach>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="problemSet" varStatus="i" items="${inactiveproblemSets}">
                            <c:set var="gradeWiseProbNo" value="${problemSet.gradewiseProblemDistribution}"/>
                            <tr>
                                <td>${i.index + 1}</td>
                                <td>${problemSet.name}&nbsp;&nbsp;<a rel="popoverproblemsetSummary" data-content='${problemSet.summary}'><i class="fa fa-question-circle-o" aria-hidden="true"></i></a></td>
                               <td>
                                   ${problemSet.numProbs}
                                </td>
                                <td>${problemSet.id}</td>
                                <c:forEach var="problemSetHeaders" items="${inActiveproblemSetHeaders}">
                                    <td><c:out value="${gradeWiseProbNo[problemSetHeaders.key]}"/></td>
                                </c:forEach>
                                <td></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
				</c:if>
				<c:if test="${inactiveproblemSets.size() == 0}">
				 <div>
                    <h5 class="page-header">
                        <big><%= rb.getString("no_inactive_problem_sets_found") %></big>
                    </h5>
					</div>
				</c:if>

            </div>

            <div id="clone_class_out" style="display:none; width: 75%;">
                <h1 class="page-header">
                    <small><%= rb.getString("replicate_class") %></small>
                </h1>

                <springForm:form id="clone_class_form" method="post"
                                 action="${pageContext.request.contextPath}/tt/tt/ttCloneClass"
                                 modelAttribute="createClassForm">

                <div class="form-group">
                    <div class="input-group">
                        <span class="input-group-addon"><i class="glyphicon glyphicon-blackboard"></i><%= rb.getString("class_name") %></span>
                        <springForm:input path="className" id="className" name="className" placeholder=""
                                          class="form-control" type="text"/>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <span class="input-group-addon"><i class="glyphicon glyphicon-menu-hamburger"></i><%= rb.getString("section") %></span>
                        <springForm:input path="gradeSection" id="gradeSection" name="gradeSection"
                                          placeholder="" class="form-control" type="text"/>
                    </div>
                </div>

                    <input type="hidden" name="teacherId" id="teacherId" value="${teacherId}">
                    <input type="hidden" name="classId" id="classId" value=" ${classInfo.classid}">


                    <div class="form-group">
                    <button role="button" type="submit" class="btn btn-primary"><%= rb.getString("clone_class") %></button>
                    </div>

                    <span class="input-group label label-warning">P.S</span>
                    <label><span><%= rb.getString("you_are_about_to_clone_class") %></span><span><c:out value="${classInfo.name}"/></span><span> <%= rb.getString("and_section") %></span><span><c:out
                            value="${classInfo.section}"/>.</span><span><%= rb.getString("you_must_change_name_and_section") %></span></label>


                </springForm:form>
            </div>

            <div id="report-wrapper" class="row" style="display:none;width: 100%;">

                <div class="panel-group" id="accordion">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_three" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseThree">
                                    <%= rb.getString("class_summary_per_student") %>
                                </a>
                            </h4>
                        </div>
                        <div id="collapseThree" class="panel-collapse collapse">
                            <div class="panel-body">
                                <ul>
                                    <li>
                                        <label style="padding-right: 10px;"><%= rb.getString("download_student_data") %></label>
                                        <a href="${pageContext.request.contextPath}/tt/tt/downLoadPerStudentReport?teacherId=${teacherId}&classId=${classInfo.classid}"
                                           data-toggle="tooltip" title="<%= rb.getString("download_this_report") %>"
                                           class="downloadPerStudentReport" aria-expanded="true"
                                           aria-controls="collapseThree">
                                            <i class="fa fa-download fa-2x" aria-hidden="true"></i>
                                        </a>
                                    </li>
                                    <li>
                                        <label style="padding-right: 10px;<%= rb.getString("download_emotion_data") %>></label>
                                        <a href="${pageContext.request.contextPath}/tt/tt/downloadStudentEmotions?teacherId=${teacherId}&classId=${classInfo.classid}"
                                           data-toggle="tooltip" title="<%= rb.getString("download_this_report") %>"
                                           class="downloadPerStudentReport" aria-expanded="true"
                                           aria-controls="collapseThree">
                                            <i class="fa fa-download fa-2x" aria-hidden="true"></i>
                                        </a>
                                    </li>
                                </ul>
                            </div>

                            <div class="panel-body">
                                <table id="perStudentReport" class="table table-striped table-bordered hover" width="100%"></table>
                            </div>

                        </div>
                    </div>

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_four" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseFour">
                                    <%= rb.getString("class_summary_common_core_cluster") %>
                                </a>
                            </h4>
                        </div>
                        <div id="collapseFour" class="panel-collapse collapse">
                            <div class="panel-body">
                                <ul>
                                    <li><label><%= rb.getString("common_core_evaluation_students_in_class") %></label>
                                        <a href="${pageContext.request.contextPath}/tt/tt/downLoadPerClusterReport?teacherId=${teacherId}&classId=${classInfo.classid}"
                                           data-toggle="tooltip" title="<%= rb.getString("download_this_report") %>"
                                           class="downloadPerStudentReport" aria-expanded="true"
                                           aria-controls="collapseOne">
                                            <i class="fa fa-download fa-2x" aria-hidden="true"></i>
                                        </a></li>
                                    <li><label><%= rb.getString("problem_wise_performance_students_in_class") %></label>
                                        <a href="${pageContext.request.contextPath}/tt/tt/downLoadPerProblemReport?teacherId=${teacherId}&classId=${classInfo.classid}"
                                           data-toggle="tooltip" title="<%= rb.getString("download_this_report") %>"
                                           class="downloadPerStudentReport" aria-expanded="true"
                                           aria-controls="collapseOne">
                                            <i class="fa fa-download fa-2x" aria-hidden="true"></i>
                                        </a></li>
                                </ul>
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
                                        <td class="span-warning-layer-one"><%= rb.getString("clusters_found_challenging") %></td>
                                    </tr>
                                    <tr>
                                        <td><%= rb.getString("for_more_than_5_problems_from_20_to_40_percent") %></td>
                                        <td class="span-danger-layer-one"><%= rb.getString("clusters_found_hard") %></td>
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

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_one" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                    <%= rb.getString("class_summary_per_student_per_problem_set") %>
                                </a>
                            </h4>
                        </div>

                        <div id="collapseOne" class="panel-collapse collapse">
                            <div class="panel-body">
                                <label><%= rb.getString("table_shows_set-wise_performance_of_students_class") %></label>
                                <a  href="${pageContext.request.contextPath}/tt/tt/downLoadPerProblemSetReport?teacherId=${teacherId}&classId=${classInfo.classid}" data-toggle="tooltip" title="<%= rb.getString("download_this_report") %>" class="downloadPerStudentReport" aria-expanded="true" aria-controls="collapseOne">
                                    <i class="fa fa-download fa-2x" aria-hidden="true"></i>
                                </a>
                            </div>
                            <div class="panel-body">
                                <table id="perTopicReportLegendTable" class="table table-striped table-bordered hover" width="40%">
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
                                    <li><%= rb.getString("cell_info2") %></li>
                                </ul>
                            </div>

                            <div class="panel-body">
                                <table id="perTopicStudentReport" class="table table-striped table-bordered hover display nowrap" width="100%"></table>
                            </div>

                        </div>
                    </div>

					<div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_five" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseFive">
                                    <%= rb.getString("summary_surveys_test_report") %>
                                </a>
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


            <div id="reset_survey_setting_out" style="display:none; width: 100%;">

                <div class="container-fluid">
                    <div class="row">

                        <div class="col-md-4 col-sm-4">
                            <div class="panel panel-default">

                                <div class="panel-body">
                                    <h1 class="page-header">
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
                                    <h1 class="page-header">
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

                <div>
                    <h3 class="page-header">
                        <small><%= rb.getString("reconfigure_student_info") %></small>
                    </h3>

                    <div class="panel panel-default"  style="width: 60%;">
                        <div class="panel-body"><%= rb.getString("create_more_ids_instructions") %>
                        </div>
                        <div class="panel-body">
                            <button id="addMoreStudentsToClass" class="btn btn-primary btn-lg" aria-disabled="true"><%= rb.getString("create_student_id") %></button>
                            <a  data-toggle="modal" data-target="#cnfirmPasswordToDownLoadTag" title="<%= rb.getString("download_student_tags") %>" class="btn btn-primary btn-lg pull-right"><%= rb.getString("download_student_tags") %></a>
                        </div>

                        <div class="panel-body" id="addMoreStudents" style="display: none;">
                            <springForm:form id="create_Student_id" method="post"
                                             action="${pageContext.request.contextPath}/tt/tt/createStudentId"
                                             modelAttribute="createClassForm" onsubmit="event.preventDefault();">

                                <div class="form-group">
                                    <div class="input-group">
                                        <span class="input-group-addon"><i class="fa fa-user-o"></i></span>
                                        <springForm:input path="userPrefix" id="userPrefix" name="userPrefix"
                                                          placeholder="" class="form-control" type="text"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="input-group">
                                        <span class="input-group-addon"><i class="fa fa-eye"></i></span>
                                        <springForm:input path="passwordToken" id="passwordToken" name="passwordToken"
                                                          placeholder="" class="form-control" type="password"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="input-group">
                                        <span class="input-group-addon"><i class="fa fa-location-arrow"></i></span>
                                        <springForm:input path="noOfStudentAccountsForClass" id="noOfStudentAccountsForClass" name="noOfStudentAccountsForClass"
                                                          placeholder="" class="form-control" type="text"/>
                                    </div>
                                </div>
                                <input type="hidden" name="teacherId" id="teacherId" value="${teacherId}">
                                <input type="hidden" name="classId" id="classId" value="${classInfo.classid}">
                                <div class="form-group">
                                    <button role="button" type="submit" id="createMoreStudentId" class="btn btn-primary"><%= rb.getString("add_student_ids") %></button>
                                    <button role="button" type="button" id="cancelForm" class="btn btn-default"><%= rb.getString("cancel") %></button>
                                </div>
                            </springForm:form>

                        </div>
                    </div>
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
                                    <%-- <td>
                                         <a  onclick="resetStudentData(4,${studentInfo.id})" class="success details-control" aria-expanded="true">
                                             <i class="fa fa-window-close" aria-hidden="true"></i>
                                         </a>
                                     </td>--%>
                                <td>
                                    <a  onclick="resetStudentData(5,${studentInfo.id})" class="success details-control" aria-expanded="true">
                                        <i class="fa fa-window-close" aria-hidden="true"></i>
                                    </a>
                                </td>
                                    <%-- <td>
                                         <a  onclick="resetStudentData(6,${studentInfo.id})" class="success details-control" aria-expanded="true">
                                             <i class="fa fa-window-close" aria-hidden="true"></i>
                                         </a>
                                     </td>
                                     <td>
                                         <a  onclick="resetStudentData(7,${studentInfo.id})" class="success details-control" aria-expanded="true">
                                             <i class="fa fa-window-close" aria-hidden="true"></i>
                                         </a>
                                     </td>
                                     <td>
                                         <a  onclick="resetStudentData(8,${studentInfo.id})" class="success details-control" aria-expanded="true">
                                             <i class="fa fa-window-close" aria-hidden="true"></i>
                                         </a>
                                     </td>--%>
                                <td>
                                    <a  onclick="resetStudentData(9,${studentInfo.id})" class="success details-control" aria-expanded="true">
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
             <div id="content_apply_handle" style="display:none;width: 100%;">
             <div>
                    <h3 class="page-header">
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
            
        </div>
</div>
</div>

<div id = "statusMessage" class="spin-loader-message" align = "center" style="display: none;"></div>

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
                <button type="button" class="btn btn-default" data-dismiss="modal"><%= rb.getString("close") %>Close</button>
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
<div id="cnfirmPasswordToDownLoadTag" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title"><%= rb.getString("confirm_password_to_download_tags") %></h4>
            </div>
            <div class="modal-body" role="alert">
                <div class="panel-body">
                    <span class="input-group label label-warning">P.S</span>
                    <label><%= rb.getString("provide_password_for_download") %></label>
                </div>
                <div class="panel-body">
                    <form id="validatestudentPasswordForDownload" onsubmit="event.preventDefault();">
                        <div class="form-group">
                            <div class="input-group"><label for="newPassword"><%= rb.getString("password_created_for_this_class") %></label>
                            </div>
                            <div class="input-group"><input type="password" placeholder="Password provided on setup" id="newPassword" class="form-control" name="newPassword"/>
                            </div>
                        </div>
                        <div class="input-group">
                            <button role="button" onclick="cnfirmStudentPasswordForTagDownload()" type="button" data-dismiss="modal" class="btn btn-primary"><%= rb.getString("submit") %>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Modal -->

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
<!-- Modal -->
</body>
</html>