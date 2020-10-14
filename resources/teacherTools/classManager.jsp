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

Locale loc = request.getLocale();
String lang = loc.getDisplayLanguage();

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
    <link href="${pageContext.request.contextPath}/css/ttStyleMain.css" rel="stylesheet">

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
var filterSix = "~~Y";
var filterOne = "~~Y";
//var urlColumnNames;

//Report2 Varriables
var perProblemReportTable

//Report3 Varriables
var perClusterReportTable

//Report5 Varribales
var perStudentReport;
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

var emsg_classLanguage   = 'Class language is mandatory field';
var emsg_className       = 'Class name is mandatory field';
var emsg_classGrade      = 'Class grade is mandatory field';
var emsg_lowEndDiff      = 'Grade level of problems - Lower is mandatory field';
var emsg_highEndDiff     = 'Grade level of problems - Higher is mandatory field';
var emsg_town            = 'Town name is mandatory field';
var emsg_schoolName      = 'School name is mandatory field';
var emsg_schoolYearRange = 'The academic year should not be greater than 2050 and less than current year';
var emsg_schoolYear      = 'School year is a mandatory field';
var emsg_gradeSection    = 'Section name is a mandatory field';

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
	emsg_classGrade      = 'El grado de la clase es obligatorio';
	emsg_lowEndDiff      = 'El grado de problemas: bajo es obligatorio';
	emsg_highEndDiff     = 'El grado de problemas: mayor es obligatorio';
	emsg_town            = 'El nombre de la ciudad es obligatorio';
	emsg_schoolName      = 'El nombre de la escuela es obligatorio';
	emsg_schoolYearRange = 'El año académico no debe ser mayor que 2050 y menor que el año actual';
	emsg_schoolYear      = 'El año escolar es obligatorio';
	emsg_gradeSection    = 'El nombre de la sección es obligatorio';
}

var resetStudentDataTitle = "";
var resetStudentDataId = "";

function resetStudentDataModal( title,studentId,username) {
		resetStudentDataTitle = title;
		resetStudentDataId = studentId;
		var temp4 = "<%= rb.getString("delete_math_data")%>" + ": " + username;
		var temp9 = "<%= rb.getString("delete_username_and_data")%>" + ": " + username;
		
		if (title == "4") {
        	$("#resetStudentDataModalPopup").find("[class*='modal-body']").html(temp4);        	
        	$('#resetStudentDataModalPopup').modal('show');
		}
		else if (title == "9") {
        	$("#resetStudentDataModalPopup").find("[class*='modal-body']").html(temp9);
        	$('#resetStudentDataModalPopup').modal('show');
		}	
}

function resetStudentData() {

    	$.ajax({
        type : "POST",
        url :pgContext+"/tt/tt/resetStudentdata",
        data : {
            studentId: resetStudentDataId,
            action: resetStudentDataTitle,
            lang: loc
        },
        success : function(response) {
            if (response.includes("***")) {
                $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
                $('#errorMsgModelPopup').modal('show');
            }else{
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

	$.ajax({
    type : "POST",
    url :pgContext+"/tt/tt/ deleteInactiveStudents",
    data : {
        classId: classID,
        action: "0",
        lang: loc
    },
    success : function(response) {
        if (response.includes("***")) {
            $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
            $('#errorMsgModelPopup').modal('show');
        }else{
            $("#successMsgModelPopup").find("[class*='modal-body']").html( response );
            $('#successMsgModelPopup').modal('show');
        }
    }
});
return false;

}



function resetPassWordForThisStudent(id,uname){
    var newPassWordToSet = $("#resetPasswordfor"+id).serializeArray()[0].value;
     $.ajax({
         type : "POST",
         url :pgContext+"/tt/tt/resetStudentPassword",
         data : {
             studentId: id,
             userName: uname,
             newPassWord : newPassWordToSet
         },
         success : function(response) {
             if (response.includes("***")) {
                 $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
                 $('#errorMsgModelPopup').modal('show');
             }else{
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
    var dataForm = $("#edit_Student_Form"+formName).serializeArray();
    var values = [];
    $.each(dataForm, function(i, field){
        values[i] = field.value;
    });
    $.ajax({
        type : "POST",
        url :pgContext+"/tt/tt/editStudentInfo",
        data : {
            studentId: formName,
            formData: values,
            lang: loc
        },
        success : function(response) {
            if (response.includes("***")) {
                $("#errorMsgModelPopup").find("[class*='modal-body']").html( response );
                $('#errorMsgModelPopup').modal('show');
            }else{
                $("#successMsgModelPopup").find("[class*='modal-body']").html( response );
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

        $.ajax({
            type : "POST",
            url :pgContext+"/tt/tt/saveChangesForProblemSet",
            data : {
                problemIds: problemIds,
                classid: classID,
                problemsetId: JSONData["problemLevelId"]
            },
            success : function(response) {
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

        $.ajax({
            type : "POST",
            url :pgContext+"/tt/tt/saveChangesForProblemSet",
            data : {
                problemIds: problemIds,
                classid: classID,
                problemsetId: JSONData["problemLevelId"]
            },
            success : function(response) {
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

var higherlevelDetail = "<div id=" + data[0] + " class='panel-body animated zoomOut'> " +
    " <div class='panel panel-default'> <div class='panel-body'><strong>"+higherlevelDetailp1+": " + JSONData["topicName"] + "</strong></div> " +
    " <div class='panel-body'><strong>"+higherlevelDetailp2+": " + html + "</strong></div>" +
    " <div class='panel-body'><strong>Summary : " + JSONData["topicSummary"] + "</strong></div>"+
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
    return tableHeader + "</tbody><table></div>";
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


    $('#activateProbSetTable input[type="checkbox"]').click(function () {
        if ($('#activateProbSetTable input[type="checkbox"]:checked').size()) {
            $('#deacivateProblemSets').prop('disabled', false);
        } else {
            $('#deacivateProblemSets').prop('disabled', true);
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
            $('#acivateProblemSets').prop('disabled', false);
        } else {
            $('#acivateProblemSets').prop('disabled', true);
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
        $.ajax({
            type : "POST",
            url :pgContext+"/tt/tt/reOrderProblemSets",
            data : {
                problemSets: result,
                classid: classID
            },
            success : function(response) {
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

    $("#deacivateProblemSets").click(function () {
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
        $.ajax({
            type : "POST",
            url :pgContext+"/tt/tt/configureProblemSets",
            data : {
                activateData: activateData,
                classid: classID,
                activateFlag: 'deactivate'
            },
            success : function(response) {
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

        $.ajax({
            type: "POST",
            url: pgContext + "/tt/tt/createMoreStudentIds",
            data: {
                formData: values,
                lang: loc
            },
            success: function (data) {
                if (data.includes("***")) {
                    $("#errorMsgModelPopup").find("[class*='modal-body']").html( data );
                    $('#errorMsgModelPopup').modal('show');
                }else{
                    $("#successMsgModelPopup").find("[class*='modal-body']").html( "<%= rb.getString("user_creation_successful")%> " );
                    $('#successMsgModelPopup').modal('show');
                }

            }
        });

    });


    $("#acivateProblemSets").click(function () {
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
        var currentSelection = '${currentSelection}';
        var prePostIds = '${prepostIds}'.split("~~");		
        var problem_imageURL = '${webContentpath}'+'problemSnapshots/prob_';
        $(document).ready(function () {
            registerAllEvents();
            handleclickHandlers();
            $("#content-conatiner").children().hide();

            if (currentSelection == "classHomePage") {
                $("#splash_page").show();            	
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
            else if (currentSelection == "resetSurveySettings_handler") {
                $('#resetSurveySettings_handler').click();
        	}            
            else if (currentSelection == "content_apply_handler") {
                $('#content_apply_handler').click();
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

            <li><a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/viewClassReportCard?classId=${classInfo.classid}" id="report_card"><i class="fa fa-bar-chart"></i> <%= rb.getString("class_report_card") %></a></li>

            <li><a id="reorg_prob_sets_handler"><i class="fa fa-list"></i> <%= rb.getString("manage_problem_sets") %></a></li>

            <li><a id="manage_roster_handler"><i class="fa fa-fw fa-users"></i> <%= rb.getString("manage_class_roster") %></a></li>

            <li><a id="manage_student_info_handler"><i class="fa fa-fw fa-users"></i> <%= rb.getString("manage_student_info") %></a></li>

            <li><a id="manage_class_handler"><i class="fas fa-fw fa-tools"></i> <%= rb.getString("manage_class") %></a></li>

            <li><a id="resetSurveySettings_handler"><i class="fa fa-fw fa-cog"></i><%= rb.getString("survey_settings") %></a></li>
            
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
					            		<p><%=msHost%><%=msContext%>/WoAdmin?action=UserRegistrationStart&var=b&startPage=LoginK12_1&classId=${classInfo.classid}</p>
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
				                    <div id="create_class_out" class="col-md-6 col-sm-6">
				                        <div class="panel panel-default">
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
				                        </div>
				                    </div>
				                </div>
				                <div class="row">
				                        <div class="panel-body class="col-md-6 col-sm-6">
				                            <button id="editClassProfileBtn" type="submit" class="btn btn-primary btn-lg" aria-disabled="true"><%= rb.getString("submit_changes") %></button>
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
                    Select activities from the menu on the left
                    </h3>
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


<!-- Modal -->
</body>
</html>