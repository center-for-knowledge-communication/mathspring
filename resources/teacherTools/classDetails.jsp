<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="springForm" uri="http://www.springframework.org/tags/form" %>

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

    <script type="text/javascript"
            src="<c:url value="https://cdn.datatables.net/1.10.15/js/jquery.dataTables.min.js" />"></script>
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
    <script type="text/javascript" src="<c:url value="/js/ttReportScripts.js" />"></script>
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
			langPrefrenceForDetailsPage();
        });

    </script>
    <script type="text/template"  id="child_table_perCluster">
        <table class="table table-striped table-bordered hover">
            <thead>
            <tr>
                <th>Problem ID</th>
                <th>Problem Name</th>
                <th>CC Standard</th>
                <th># of Students seen the problem</th>
                <th>Collective effort on Problem</th>
                <th>% of Students solved the problem on the first attempt</th>
                <th># of Students solved the problem on the second attempt</th>
                <th>% of Students repeated the problem</th>
                <th>% of Students skipped the problem</th>
                <th>% of Students gave up</th>
                <th>Most Frequent Incorrect Response</th>
                <th>Similar Problems</th>
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
                    <i class="fa fa-tachometer" aria-hidden="true"></i> Teacher Tools
                </a>
            </li>
        </ul>
        <ul class="nav navbar-right top-nav buttonCustomColor">
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i
                        class="fa fa-user"></i> ${fn:toUpperCase(teacherName)} <b class="caret"></b></a>
                <ul class="dropdown-menu">
                    <li>
                        <a href="#"><i class="fa fa-fw fa-user"></i> Profile</a>
                    </li>
                    <li class="divider"></li>
                    <li>
                        <a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/logout"><i
                                class="fa fa-fw fa-power-off"></i>Log Out</a>
                    </li>
                </ul>
            </li>
        </ul>
    </nav>
    <nav class="navbar navbar-inverse navbar-fixed-top" id="sidebar-wrapper" role="navigation">
        <ul class="nav sidebar-nav">
            <li>
                <a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/ttMain?teacherId=${teacherId}"><i
                        class="fa fa-fw fa-home"></i> Home</a>
            </li>

            <li>
                <a href="#" id="reports_handler"><i class="fa fa-bar-chart"></i> Class Report Card</a>
            </li>

            <li><a id="reorg_prob_sets_handler"><i class="fa fa-book"></i> Manage Problem Sets</a></li>

            <li><a id="reconfigure_student_handler"><i class="fa fa-fw fa-id-badge"></i> Manage Students</a></li>
            <li>
                <a href="#" id="copyClass_handler"><i class="fa fa-files-o"></i> Replicate Class</a>
            </li>

            <li><a id="resetSurveySettings_handler"><i class="fa fa-fw fa-cog"></i>Survey Settings</a></li>

        </ul>
        <!-- /#sidebar-end -->
    </nav>
    <div id="page-content-wrapper">

        <h1 class="page-header">
            <strong>${classInfo.name}</strong>
        </h1>

        <div id="content-conatiner" class="container-fluid">

            <div id="problem_set_content" style="width: 100%;">
			<input type="hidden" id="activeproblemSetSize" name="activeproblemSetSize" value="${activeproblemSet.size()}">
			<input type="hidden" id="inactiveproblemSetSize" name="inactiveproblemSetSize" value="${inactiveproblemSets.size()}">
			<c:if test="${activeproblemSet.size() != 0}">
                <div>
                    <h3 class="page-header">
                        <small>Active Problem Sets</small>
                    </h3>

                    <div class="panel panel-default">
                        <div class="panel-body">The Following Table shows Active problem sets for this class. Check the problem sets you wish to deactivate and click button below
                        </div>
                        <div class="panel-body">PS : Problems shown to students will follow the order given below. Problem set rows may be reordered using the drag/drop option on 'Seq' column.
                        </div>
                        <div class="panel-body">
                            <button id="deacivateProblemSets" class="btn btn-primary btn-lg" aria-disabled="true" disabled="disabled">Deactivate Problem Sets</button>
                        </div>
                    </div>

                    <table id="activateProbSetTable" class="table table-striped table-bordered hover" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th rowspan="2" align="center"><span>Order&nbsp;&nbsp;</span><a rel="popoverOrder"><i class="fa fa-question-circle-o" aria-hidden="true"></i></a></th>
                            <th rowspan="2">Problem Set</th>
                            <th rowspan="2"><span># of Activated Problems&nbsp;&nbsp;</span><a rel="popoveractivatedProblems"><i class="fa fa-question-circle-o" aria-hidden="true"></i></a></th>
                            <th rowspan="2">Problem Id</th>
                            <th style="text-align: center;" colspan="<c:out value="${activeproblemSetHeaders.size()}"/>">Gradewise Distribution</th>
                            <th rowspan="2">Deactivate Problem set</th>
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
                        <big>No Active Problem Sets Found for the given combination of Grade and Language</big>
                    </h5>
					</div>
				</c:if>
				<c:if test="${inactiveproblemSets.size() != 0}">
                <div>
                    <h3 class="page-header">
                        <small>Inactive Problem Sets</small>
                    </h3>

                    <div class="panel panel-default">
                        <div class="panel-body">The Following Tables shows Inactive problem sets for this class. Check the problem sets you wish to activate and click button below
                        </div>
                        <div class="panel-body">
                            <button id="acivateProblemSets" class="btn btn-primary btn-lg" disabled="disabled" aria-disabled="true">Activate Problem Sets</button>
                        </div>
                    </div>

                    <table id="inActiveProbSetTable" class="table table-striped table-bordered hover" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th rowspan="2">Order</th>
                            <th rowspan="2">Problem Set</th>
                            <th rowspan="2">Available Problems</th>
                            <th rowspan="2">Problem Id</th>
                            <th style="text-align: center;" colspan="<c:out value="${inActiveproblemSetHeaders.size()}"/>">Gradewise Distribution</th>
                            <th rowspan="2">Activate Problem Sets</th>
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
                        <big>No Inactive Problem Sets Found for the given combination of Grade and Language</big>
                    </h5>
					</div>
				</c:if>

            </div>

            <div id="clone_class_out" style="display:none; width: 75%;">
                <h1 class="page-header">
                    <small>Replicate Class</small>
                </h1>

                <springForm:form id="clone_class_form" method="post"
                                 action="${pageContext.request.contextPath}/tt/tt/ttCloneClass"
                                 modelAttribute="createClassForm">

                <div class="form-group">
                    <div class="input-group">
                        <span class="input-group-addon"><i class="glyphicon glyphicon-blackboard"></i></span>
                        <springForm:input path="className" id="className" name="className" placeholder="Class Name"
                                          class="form-control" type="text"/>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <span class="input-group-addon"><i class="glyphicon glyphicon-menu-hamburger"></i></span>
                        <springForm:input path="gradeSection" id="gradeSection" name="gradeSection"
                                          placeholder="Section" class="form-control" type="text"/>
                    </div>
                </div>

                    <input type="hidden" name="teacherId" id="teacherId" value="${teacherId}">
                    <input type="hidden" name="classId" id="classId" value=" ${classInfo.classid}">


                    <div class="form-group">
                    <button role="button" type="submit" class="btn btn-primary">Clone Class</button>
                    </div>

                    <span class="input-group label label-warning">P.S</span>
                    <label><span>You are about to clone class</span><span><c:out value="${classInfo.name}"/></span><span>and section</span><span><c:out
                            value="${classInfo.section}"/>.</span><span>You must give the new class a different name and
                        section</span></label>


                </springForm:form>
            </div>

            <div id="report-wrapper" class="row" style="display:none;width: 100%;">

                <div class="panel-group" id="accordion">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a id="report_three" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseThree">
                                    Class Summary Per Student
                                </a>
                            </h4>
                        </div>
                        <div id="collapseThree" class="panel-collapse collapse">
                            <div class="panel-body">
                                <ul>
                                    <li>
                                        <label style="padding-right: 10px;">Download student data, many rows per
                                            student</label>
                                        <a href="${pageContext.request.contextPath}/tt/tt/downLoadPerStudentReport?teacherId=${teacherId}&classId=${classInfo.classid}"
                                           data-toggle="tooltip" title="Download this report"
                                           class="downloadPerStudentReport" aria-expanded="true"
                                           aria-controls="collapseThree">
                                            <i class="fa fa-download fa-2x" aria-hidden="true"></i>
                                        </a>
                                    </li>
                                    <li>
                                        <label style="padding-right: 10px;">Download Emotion data, many rows per
                                            student</label>
                                        <a href="${pageContext.request.contextPath}/tt/tt/downloadStudentEmotions?teacherId=${teacherId}&classId=${classInfo.classid}"
                                           data-toggle="tooltip" title="Download this report"
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
                                    Class Summary Per Common Core Cluster
                                </a>
                            </h4>
                        </div>
                        <div id="collapseFour" class="panel-collapse collapse">
                            <div class="panel-body">
                                <ul>
                                    <li><label>Common core cluster evaluation of students in this class</label>
                                        <a href="${pageContext.request.contextPath}/tt/tt/downLoadPerClusterReport?teacherId=${teacherId}&classId=${classInfo.classid}"
                                           data-toggle="tooltip" title="Download this report"
                                           class="downloadPerStudentReport" aria-expanded="true"
                                           aria-controls="collapseOne">
                                            <i class="fa fa-download fa-2x" aria-hidden="true"></i>
                                        </a></li>
                                    <li><label>Problem wise performance of students in this class</label>
                                        <a href="${pageContext.request.contextPath}/tt/tt/downLoadPerProblemReport?teacherId=${teacherId}&classId=${classInfo.classid}"
                                           data-toggle="tooltip" title="Download this report"
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
                                        <th>% Range</th>
                                        <th>Meaning</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>For more than 5 problems, only 20%-40% problems for this standard have been solved correctly on first attempt</td>
                                        <td class="span-warning-layer-one">Clusters that students found challenging</td>
                                    </tr>
                                    <tr>
                                        <td>For more than 5 problems, less than 20% problems for this standard have been solved correctly on first attempt</td>
                                        <td class="span-danger-layer-one">Clusters that students found really hard</td>
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
                                    Class Summary Per Student Per Problemset
                                </a>
                            </h4>
                        </div>

                        <div id="collapseOne" class="panel-collapse collapse">
                            <div class="panel-body">
                                <label>This table shows problem set-wise performance of students of this class.</label>
                                <a  href="${pageContext.request.contextPath}/tt/tt/downLoadPerProblemSetReport?teacherId=${teacherId}&classId=${classInfo.classid}" data-toggle="tooltip" title="Download this report" class="downloadPerStudentReport" aria-expanded="true" aria-controls="collapseOne">
                                    <i class="fa fa-download fa-2x" aria-hidden="true"></i>
                                </a>
                            </div>
                            <div class="panel-body">
                                <table id="perTopicReportLegendTable" class="table table-striped table-bordered hover" width="40%">
                                    <thead>
                                    <tr>
                                        <th>Mastery Range</th>
                                        <th>Grade & Color Code for 2 or more problems</th>
                                        <th>Grade/Color Code for 10 or more problems</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>0.75 or Greater</td>
                                        <td class="span-sucess-layer-two">Grade A (Excellent)</td>
                                        <td class="span-sucess-layer-one">Grade A (Excellent)</td>
                                    </tr>
                                    <tr>
                                        <td>Between 0.5 and 0.75</td>
                                        <td class="span-info-layer-two">Grade B (Good)</td>
                                        <td class="span-info-layer-one">Grade B (Good)</td>
                                    </tr>
                                    <tr>
                                        <td>Between 0.25 and 0.5</td>
                                        <td class="span-warning-layer-two">Grade C (Needs Improvement)</td>
                                        <td class="span-warning-layer-one">Grade C (Needs Improvement)</td>
                                    </tr>
                                    <tr>
                                        <td>0.25 or Less</td>
                                        <td class="span-danger-layer-two">Grade D (Unsatisfactory)</td>
                                        <td class="span-danger-layer-one">Grade D (Unsatisfactory)</td>
                                    </tr>
                                    </tbody>
                                </table>
                                <div class="loader" style="display: none"></div>
                            </div>

                            <div class="panel-body">
                                <ul>
                                    <li>Each cell shows [number solved on first attempt / number problems solved] along with highest recorded "Mastery" <a title="What is Mastery?" style="cursor:pointer" rel="initialPopover"> <i class="fa fa-question-circle-o" aria-hidden="true"></i></a> value for that problem set.</li>
                                    <li>Cell wherein students have attempted 2 or more problems are color coded.</li>
                                    <li>Click on the cell to get the complete "Mastery Trajectory" for given student and problem set</li>
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
                                    Summary of surveys/tests report
                                </a>
                            </h4>
                        </div>

                        <div id="collapseFive" class="panel-collapse collapse">
                            <div class="panel-body">
                                <label>This table shows summary of survey/tests reports of students of this class.</label>
                                <a  href="${pageContext.request.contextPath}/tt/tt/downLoadPerSummSurReport?teacherId=${teacherId}&classId=${classInfo.classid}" data-toggle="tooltip" title="Download this report" class="downLoadPerSummSurReport" aria-expanded="true" aria-controls="collapseOne">
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

                        <div class="col-md-6 col-sm-6">
                            <div class="panel panel-default">

                                <div class="panel-body">
                                    <h1 class="page-header">
                                        <small>Turn On/Off Surveys</small>
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
                                        <span class="form-check-label">Pre-Test Survey : Students will see this survey  - the first time a student logs in to MathSpring.</span>
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
                                        <span class="form-check-label">Post-Test Survey : Students will see this survey every time they log in.</span>
                                    </div>
                                </div>
                                <div class="panel-body">
                                    <button role="button" type="submit" class="btn btn-primary">Submit
                                    </button>
                                </div>
                            </div>
                            </springForm:form>
                        </div>


                        <div class="col-md-6 col-sm-6">
                            <div class="panel panel-default">
                                <div class="panel-body">
                                    <h1 class="page-header">
                                        <small>Available Surveys</small>
                                    </h1>
                                </div>
                                <div class="panel-body">

                                    <table id="activeSurveyList" class="table table-striped table-bordered hover"
                                           width="80%">
                                        <thead>
                                        <tr>
                                            <th>Survey ID</th>
                                            <th>List of surveys/quizzes</th>
                                            <th>*First Time Student Logs In</th>
                                            <th>*Next Time Student Logs In</th>
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
                                            * Note: This survey will show only ONCE, the first time a student logs in to
                                            MathSpring.
                                        </li>
                                        <li>
                                            ** Note: If there is a survey chosen for the “next time the student logs
                                            in”, students will see this survey every next time they log in. You should
                                            only turn this on when you are sure that you want them to get this new
                                            survey/quiz.
                                        </li>
                                    </ul>
                                </div>
                                <div class="panel-body">
                                    <button id="select_activeSurveyList" class="btn btn-primary btn-lg"
                                            aria-disabled="true">Publish Survey Settings
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
                        <small>Reconfigure Student Information</small>
                    </h3>

                    <div class="panel panel-default"  style="width: 60%;">
                        <div class="panel-body">The Following Table shows student Id information for this class. You can create more student Id's to this class by clicking button below.
                        </div>
                        <div class="panel-body">
                            <button id="addMoreStudentsToClass" class="btn btn-primary btn-lg" aria-disabled="true">Create Student Id</button>
                            <a  data-toggle="modal" data-target="#cnfirmPasswordToDownLoadTag" title="Download Student Tags" class="btn btn-primary btn-lg pull-right">Download Student Tags</a>
                        </div>

                        <div class="panel-body" id="addMoreStudents" style="display: none;">
                            <springForm:form id="create_Student_id" method="post"
                                             action="${pageContext.request.contextPath}/tt/tt/createStudentId"
                                             modelAttribute="createClassForm" onsubmit="event.preventDefault();">

                                <div class="form-group">
                                    <div class="input-group">
                                        <span class="input-group-addon"><i class="fa fa-user-o"></i></span>
                                        <springForm:input path="userPrefix" id="userPrefix" name="userPrefix"
                                                          placeholder="Username Prefix" class="form-control" type="text"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="input-group">
                                        <span class="input-group-addon"><i class="fa fa-eye"></i></span>
                                        <springForm:input path="passwordToken" id="passwordToken" name="passwordToken"
                                                          placeholder="Password" class="form-control" type="password"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="input-group">
                                        <span class="input-group-addon"><i class="fa fa-location-arrow"></i></span>
                                        <springForm:input path="noOfStudentAccountsForClass" id="noOfStudentAccountsForClass" name="noOfStudentAccountsForClass"
                                                          placeholder="Number of Student Id's to be create for this class" class="form-control" type="text"/>
                                    </div>
                                </div>
                                <input type="hidden" name="teacherId" id="teacherId" value="${teacherId}">
                                <input type="hidden" name="classId" id="classId" value="${classInfo.classid}">
                                <div class="form-group">
                                    <button role="button" type="submit" id="createMoreStudentId" class="btn btn-primary">Add Student Ids</button>
                                    <button role="button" type="button" id="cancelForm" class="btn btn-default">Cancel</button>
                                </div>
                            </springForm:form>

                        </div>
                    </div>
                    <table id="student_roster" class="table table-striped table-bordered hover" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th rowspan="2">Student ID</th>
                            <th rowspan="2">First Name</th>
                            <th rowspan="2">Last Name</th>
                            <th rowspan="2">Username</th>
                            <th colspan="7" style="text-align: center;"> Student Data </th>
                        </tr>

                        <tr>
                            <%--  <th>Clear All</th>--%>
                            <th>Delete math problem data from this student</th>
                            <%-- <th>Reset Practice Hut</th>
                             <th>Clear Pretest</th>
                             <th>Clear Posttest</th>--%>
                            <th>Delete username, and all its data</th>
                            <th>Change Password or Username</th>
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
                <h4 class="modal-title">Collective Students Effort on This Problem</h4>
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
                <h4 class="modal-title">Something went wrong...</h4>
            </div>
            <div class="modal-body alert alert-danger" role="alert">
                Some text in the modal
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
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
                <h4 class="modal-title">Success</h4>
            </div>
            <div class="modal-body alert alert-success" role="alert">
                Some text in the modal
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
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
                <h4 class="modal-title">Success</h4>
            </div>
            <div class="modal-body alert alert-success" role="alert">
                Some text in the modal
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
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
                <h4 class="modal-title">Complete Mastery Chart for Student</h4>
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
                <h4 class="modal-title">Confirm password to download tags</h4>
            </div>
            <div class="modal-body" role="alert">
                <div class="panel-body">
                    <span class="input-group label label-warning">P.S</span>
                    <label>Please provide the student's password you set while creating this class to confirm your download.</label>
                </div>
                <div class="panel-body">
                    <form id="validatestudentPasswordForDownload" onsubmit="event.preventDefault();">
                        <div class="form-group">
                            <div class="input-group"><label for="newPassword">Password created for this class</label>
                            </div>
                            <div class="input-group"><input type="password" placeholder="Password provided on setup" id="newPassword" class="form-control" name="newPassword"/>
                            </div>
                        </div>
                        <div class="input-group">
                            <button role="button" onclick="cnfirmStudentPasswordForTagDownload()" type="button" data-dismiss="modal" class="btn btn-primary">Submit
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
                <h4 class="modal-title">Mastery Trajectory Report</h4>
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