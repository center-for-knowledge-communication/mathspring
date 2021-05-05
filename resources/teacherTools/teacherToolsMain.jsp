<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="springForm" uri="http://www.springframework.org/tags/form" %>
<%@page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>
<% 
/** Frank 	01-14-20	Issue #45 & #21 add teacher logging by using the request object to get the TeacherLogger object 
 *  Frank   01-20-20    Issue #39 use classId as alternative password
 *  Framk   01-29-20	Issue #47 removed random color rotator for class selection display. Use green for active, red for inactive
 *  Frank   02-16-20    Issue #48 Student Name an d password creation
 *  Frank   02-17-20    ttfixesR3
 *  Frank   03-2-2020   Issue #45 added dynamic teacherlist selection
 *  Frank	02-26-2020	Issue #28 teacher password and profile self-maintenance
 *  Frank   06-17-20    Issue #149
 *  Frank	07-08-20	Issue #134 153 156 162
 *  Frank	07-28-20	Issue #74 Protect from URL editting of teacherId and classId
 *  Frank   07-28-20    Remove Replicate Class from menu
 *  Frank	08-08-20	issue #51 fix year selection
 *  Frank	09-14-20	issue #237 add pauseStudentUse logic
 *  Frank	10-07-20	issue #267 add school year to class thumbnails
 *  Frank	10-12-20	issue #272 send "classHomePage" selection to viewDetails
 *  Frank	10-30-20	Issue #293 add new items to class config form 
 *	Frank	12-18-20	Issue #336 added cache-busting for selected .js and .css files
 *  Frank 	01-05-21  	Issue #302 teacher username only alpha and numeric characters
 *  Frank 	02-14-21  	Issue #303R1 added teacher feedback on teacher tools
 *  Frank 	05-04-21  	ms-fixes-042921 - removed onclick event from create class submit button
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
    <link href="https://cdnjs.cloudflare.com/ajax/libs/jquery.bootstrapvalidator/0.5.0/css/bootstrapValidator.min.css"
          rel="stylesheet"/>
    <!-- Datatables Css Files -->
    <link href="https://cdn.datatables.net/1.10.13/css/dataTables.bootstrap4.min.css" rel="stylesheet" type="text/css">
    <link href="https://cdn.datatables.net/colreorder/1.3.2/css/colReorder.bootstrap4.min.css" rel="stylesheet"
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
            src="<c:url value="https://cdn.datatables.net/1.10.13/js/dataTables.bootstrap4.min.js" />"></script>
    <script type="text/javascript"
            src="<c:url value="https://cdn.datatables.net/rowreorder/1.2.0/js/dataTables.rowReorder.min.js" />"></script>
    <script type="text/javascript"
            src="<c:url value="https://cdn.datatables.net/select/1.2.1/js/dataTables.select.min.js" />"></script>

    <script type="text/javascript"
            src="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.bundle.min.js" />"></script>
    <script type="text/javascript"
            src="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js" />"></script>

    <script type="text/javascript"
            src="<c:url value="https://cdn.datatables.net/1.10.13/js/jquery.dataTables.min.js" />"></script>

    <script type="text/javascript"
            src="<c:url value="https://cdn.datatables.net/colreorder/1.3.2/js/dataTables.colReorder.min.js" />"></script>
    <script type="text/javascript"
            src="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-validator/0.4.5/js/bootstrapvalidator.min.js" />"></script>
      <script type="text/javascript" src="<c:url value="/js/bootstrap/js/language_es.js" />"></script>        
    
    <jsp:useBean id="random" class="java.util.Random" scope="application"/>


<style>

 .nobull {
  list-style-type: none;
 }
 
#myInput {
  box-sizing: border-box;
  background-repeat: no-repeat;
  font-size: 20px;
  min-width: 100%;
  padding: 6px 4px 6px 4px;
  border: 1px solid #ddd;
}

#myInput:focus {
	background-color: #ddd;
	outline: 3px solid black;
}

#myInput:hover {
	background-color: #ddd;
	outline: 3px solid black;
}

.dropdown {
  position: relative;
  display: inline-block;
}

.dropdown-content {
  background-color: #f6f6f6;
  font-size: 16px;
  min-width: 100%;
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

.teacher-dropdown li:hover {
  background-color: lightgreen;
  min-width: 100%;
  size: 3;
}



.show {display: block;}

.registration-box {
  background-color: white;
  border: 1px solid #003364;
}

.vertical-center {
  display: flex;
  align-items: center;
  min-height: 75vh;
}

.teacher-registration-form {
  padding-bottom: 70px;
  padding-right: 35px;
}

.teacher-button {
  background: #f6a623;
  border-radius: 0;
  color: white;
  border: 1px solid #003364;
  padding: 10px 30px;
}

</style>


<script type="text/javascript">

    var languagePreference = window.navigator.language;
    var languageSet = "en";
    var loc = "en-US";

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
    	var perTeacherReport;
    	var eachTeacherData = [];    	
    	var eachTeacherListData = [];    	
    	var pgContext = "${pageContext.request.contextPath}";
        var classID = '';
        var teacherID = '';
        var targetTeacherID = "";
        var targetTeacherName = "";
        
        $(document).ready(function () {
            $('#wrapper').toggleClass('toggled');
            $("#report-wrapper").show();
            $("#report-wrapper2").show();
            $("#teacher-activities-wrapper").hide();
            $("#panel-wrapper").hide();
            $("#form-wrapper").hide();
            $("#edit-teacher-wrapper").hide();          
            var pause = ${teacherPauseStudentUse};
            if (pause == "1")
            	$("#pause-status").show();            
            else 
            	$("#pause-status").hide();
            registerAllEvents();
            handleclickHandlers();
        });

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
        		document.getElementById('maxProb').value = '40';
            	document.getElementById('minProb').value = '2';
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
        		  document.getElementById('maxTime').value = '30';
        	      document.getElementById('minTime').value = '0';
        		  document.getElementById('maxTime').focus();
        	  }
        	}
        
        
/* 
        function myFunction() {
          alert("Input field lost focus.");
        }
*/
        function addToTeacherList(item, index) {
          
        	var titem = "" + item;
        	var tlist = titem.split(",");
        	
        	document.getElementById("teacherList").innerHTML += "<li class='dropdown-content' onClick=selectTeacher(this);>" + tlist[1] + "</li>";
        }

        
        function findSelectedInTeacherList(item, index) {
                    	
        	var titem = "" + item;
        	var tlist = titem.split(",");
        	if (tlist[1] == targetTeacherName) {
        		targetTeacherID = tlist[0];
        	}
        }
        
        
//        document.getElementById("className").addEventListener("blur", myFunction);
        
        function handleclickHandlers() {
            $("#create_class_form").bootstrapValidator({
                // To use feedback icons, ensure that you use Bootstrap v3.1.0 or later
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                fields: {
                	classLanguage: {
                        validators: {
                            notEmpty: {
                                message: emsg_classLanguage
                            }
                        }
                    },                    
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
                $("#create_class_form").data('bootstrapValidator').resetForm();
                e.preventDefault();
                var $form = $(e.target);
                var bv = $form.data('bootstrapValidator');
                $.post($form.attr('action'), $form.serialize(), function (result) {
                })
            });

            $("#createClass_handler").click(function () {
                $("#report-wrapper").hide();
                $("#report-wrapper2").hide();
                $("#teacher-activities-wrapper").hide();
                $("#panel-wrapper").hide();
                $("#edit-teacher-wrapper").hide();
                $("#form-wrapper").show();
            });

            $("#editTeacher_handler").click(function () {
                $("#report-wrapper").hide();
                $("#report-wrapper2").hide();
                $("#teacher-activities-wrapper").hide();
                $("#panel-wrapper").hide();
                $("#form-wrapper").hide();
                $("#edit-teacher-wrapper").show();
            });
            
            $("#teacher_feedback_handler").click(function () { 
                window.open( 
                  "${pageContext.request.contextPath}/teacherTools/feedbackRequest.jsp", "_blank"); 
                $("#report-wrapper").show();
                $("#report-wrapper2").show();
                $("#teacher-activities-wrapper").hide();
                $("#panel-wrapper").hide();
                $("#form-wrapper").hide();
                $("#edit-teacher-wrapper").hide();
            });
            
            $("#teacher_activities_handler").click(function () {
                $("#report-wrapper").hide();
                $("#report-wrapper2").hide();
                $("#form-wrapper").hide();
                $("#edit-teacher-wrapper").hide();
                $("#teacher-activities-wrapper").show();
                $("#teacher-activities-input").show();
                $("#dropdownDiv").hide();
                               
                });
            		 
             $("#pwdBtn").click(function () {                    
                 var pwd = document.getElementById("pwd").value; 
                 $.ajax({
                     type : "POST",
                     url : pgContext+"/tt/tt/getTeacherReports",
                     data : {
                         classId: targetTeacherID,
                         teacherId: teacherID,
                         reportType: 'teacherList',
                         lang: loc,
                         filter: pwd
                     },
                     success : function(data) {
                     	    if (data) {
	                       		var msg = "" + data;
	                       		if (msg == "InvalidAccessCode") {
	                           		alert("<%= rb.getString("invalid_access_code") %>");
	                       		}
	                       		else {
		                           	var jsonData = $.parseJSON(data);
		           	                eachTeacherListData = jsonData.levelOneData;
		           	                
		           	                document.getElementById("teacherList").innerHTML = "";
		           	                eachTeacherListData.forEach(addToTeacherList);		           	             	
		           	             	$("#dropdownDiv").show();
	                       		}
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

              
              //document.getElementById("teacherList").innerHTML = "<li class='dropdown-content' onClick=selectTeacher(this);>maestrcordoba:840</li><li class='dropdown-content' onClick=selectTeacher(this);>fstester1:866</li><li class='dropdown-content' onClick=selectTeacher(this);>fstester2:867</li><li class='dropdown-content' onClick=selectTeacher(this);>fstester3:868</li>";

          	
          });
                         
            $('#PageRefresh').click(function () {
                location.reload();
            });
  
        }
        
        function filterFunction() {
        	  var input, filter, ul, li, a, i;
        	  input = document.getElementById("myInput");
        	  filter = input.value.toUpperCase();
        	  div = document.getElementById("myDropdown");
        	  a = div.getElementsByTagName("li");
        	  for (i = 0; i < a.length; i++) {
        	    txtValue = a[i].textContent || a[i].innerText;
        	    if (txtValue.toUpperCase().indexOf(filter) > -1) {
        	      a[i].style.display = "block";
        	    } 
        	    if (txtValue.toUpperCase().indexOf(filter) == -1) {
        	      a[i].style.display = "none";
        	    }
        	  }
        	}

		function selectTeacher(t) {
			
			document.getElementById("myInput").value = t.innerHTML;
			targetTeacherName = t.innerHTML;
			eachTeacherListData.forEach(findSelectedInTeacherList);
			filterFunction();
            $("#panel-wrapper").show();
           	$("#teacher-activities-content").show();
        	document.getElementById("myDropdown").classList.toggle("show");

		}
</script>
    
<script>





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


function registerAllEvents(){

	var headers = changeTeacherActivitiesReportHeaderAccordingToLanguage();
	
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
	
	
    $('#teacherActivities').on('show.bs.collapse', function ()  {
        $.ajax({
            type : "POST",
            url : pgContext+"/tt/tt/getTeacherReports",
            data : {
                classId: targetTeacherID,
                teacherId: teacherID,
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
        
    });

    
	
    $('#cohortMonitoring').on('show.bs.collapse', function ()  {
        $.ajax({
            type : "POST",
            url : pgContext+"/tt/tt/getTeacherReports",
            data : {
                classId: targetTeacherID,
                teacherId: teacherID,
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
        
    });    
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
                        <a id= "editTeacher_handler" href="#"><i class="fa fa-fw fa-user"></i> <%= rb.getString("profile") %></a>
                    </li>
                    <li class="divider"></li>
                    <li>
                        <a id="logout_selector" href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/logout"><i
                                class="fa fa-fw fa-power-off"></i><%= rb.getString("log_out") %></a>
                    </li>
                </ul>
            </li>
        </ul>
    </nav>
    <nav class="navbar navbar-inverse navbar-fixed-top" id="sidebar-wrapper" role="navigation">
        <ul class="nav sidebar-nav">
            <li>
            	<a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/ttMain"><i class="fa fa-fw fa-home"></i> <%= rb.getString("home") %></a>
            </li>
            <li>
                <a href="#" id="createClass_handler"><i class="fa fa-fw fa-pencil"></i> <%= rb.getString("create_new_class") %></a>
            </li>
            <li>
                <a id="survey_problems_site" href="http://rose.cs.umass.edu/msadmin?${teacherId}"><i class="fa fa-fw fa-pencil"></i> <%= rb.getString("create_surveys_and_math_problems") %></a>
            </li>
            <li>
                <a href="#" id="teacher_activities_handler"><i class="fa fa-fw fa-search"></i> <%= rb.getString("view_teacher_activities") %></a>
            </li>
            <li>
                <a id="teacher_feedback_handler"><i class="fa fa-fw fa-search"></i> <%= rb.getString("send_us_feedback") %></a>
            </li>
<!--

            <li id="pause_logins_handler">
                <a href="#" id="pause_logins_handler"><i class="fa fa-fw fa-ban"></i> Pause student logins</a>
            </li>
            <li id="resume_logins_handler">
                <a href="#"><i class="fa fa-fw fa-play-circle-o"></i> Resume student logins</a>
            </li>
 -->
         </ul>
        <!-- /#sidebar-end -->
    </nav>
    <div id="page-content-wrapper">
       	<div class="loader" style="display: none" ></div>               
        <div id="content-conatiner" class="container-fluid">
            <div id="pause-status">
            	<div class="row">
                 <h1 class="tt-paused-logins-message">
                 	<%= rb.getString("student_logins_are_paused") %>
                 </h1>
                 </div>
            </div>
        
        <div id="pause-wrapper" style="display: none;">
    		<div vertical-center">
		        <div class="col-sm-6 col-sm-offset-3 registration-box">
		            <form
		                    class="form-horizontal"
		                    method="post"
		                    action="${pageContext.request.contextPath}/WoAdmin?action=AdminTeacherEdit"
		            >
		                <div class="form-group">
		                    <label class="control-label col-sm-6" for="first_name"><%= rb.getString("student_logins_are") %>: </label>
		                    <div class="col-sm-3">
		                        <input type="text" name="fname" class="form-control" id="first_name" value="paused">
		                    </div>
		                    <div class="col-sm-3">
		                        <button type="submit" class="btn btn-default pull-right btn-block teacher-button"><%= rb.getString("resume") %></button>
		                    </div>
		                </div><!-- form-group -->
		            </form>
		            <hr>
		        </div>
	        </div>
        </div>            
        <div id="report-wrapper" class="row">
           	<div class="loader" style="display: none" ></div>               
                <div class="row">
                    <div class="col-lg-12">
                        <h1 class="page-header">
                            <c:choose>
                                <c:when test="${noClass == false}">
                                    <small><%= rb.getString("existing_classes") %></small>
                                </c:when>
                                <c:otherwise>
                                    <small><%= rb.getString("no_pre-existing_classes") %></small>
                                </c:otherwise>
                            </c:choose>

                        </h1>
                    </div>
                </div>
                <!-- /.row -->
                <c:if test="${noClass == false}">

                <c:forEach var="c" items="${classbean.classes}" varStatus="loop">
                <c:if test="${(loop.index == 0 || loop.index%4  == 0)}">
                <c:set var="terminator" value="${loop.index + 3}"/>
                <div class="row">
                    </c:if>
                    <div class="col-lg-3 col-md-6">
                        <div class="panel panel-green">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <i class="fa fa-bar-chart fa-5x"></i>
                                    </div>
                                    <div class="col-xs-9 text-right">
                                        <div class="huge">${c.name}</div>
	                                    <div class="pull-right">&nbsp;&nbsp;<%= rb.getString("year") %>:${c.schoolYear}</div>
	                                    <div class="pull-right">&nbsp;[<%= rb.getString("class_code") %>:${c.classid}]</div>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-footer">
                           		<a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/viewClassDetails?classId=${c.classid}&currentSelection=classHomePage">
                                  <div> 
                                  	<span class="pull-left"><i class="fa fa-eye fa-2x"></i>&nbsp;</span>
                                  	<span class="pull-left"><%= rb.getString("view_class") %></span>
                                  </div>
                              	</a>
                                <a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/setClassActiveFlag?teacherId=${teacherId}&classId=${c.classid}&activeFlag=0">
                               		<div>
	                                  	<span class="pull-right"><%= rb.getString("archive_class") %></span>
	                                  	<span class="pull-right"><i class="fa fa-archive fa-2x">&nbsp;</i></span>
	                                  </div>
	                            </a>
                                <div class="clearfix"></div>
                             </div>
                        </div>
                    </div>
                    <c:if test="${loop.index == terminator}">
                    <!-- t div-->
                </div>
                </c:if>
                <c:if test="${loop.last == 'true'}">
                <!-- s div-->
            </div>
            </c:if>
            </c:forEach>
            </c:if>
        </div>
        <div id="report-wrapper2" class="row">
         <div class="row">
                    <div class="col-lg-12">
                        <h1 class="page-header">
                           <small><%= rb.getString("existing_classes_archived") %></small>
                        </h1>
                    </div>
         </div>
          <c:if test="${noClass == false}">
                <c:forEach var="c" items="${classbeanArchived.classes}" varStatus="loop">
                <c:if test="${(loop.index == 0 || loop.index%4  == 0)}">
                <c:set var="terminator" value="${loop.index + 3}"/>
                <div class="row">
                    </c:if>
                    <div class="col-lg-3 col-md-6">
                        <div class="panel panel-red">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <i class="fa fa-bar-chart fa-5x"></i>
                                    </div>
                                    <div class="col-xs-9 text-right">
                                        <div class="huge">${c.name}</div>
	                                    <div class="pull-right">&nbsp;&nbsp;<%= rb.getString("year") %>:${c.schoolYear}</div>
	                                    <div class="pull-right">&nbsp;[<%= rb.getString("class_code") %>:${c.classid}]</div>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-footer">
                           		<a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/viewClassDetails?classId=${c.classid}&currentSelection=classHomePage">
                                  <div> 
                                  	<span class="pull-left"><i class="fa fa-eye"></i>&nbsp;</span>
                                  	<span class="pull-left"><%= rb.getString("view_class") %>&nbsp;&nbsp;&nbsp;</span>
                                  </div>
                              	</a>
                                <a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/setClassActiveFlag?teacherId=${teacherId}&classId=${c.classid}&activeFlag=1">
                               		<div>
	                                  	<span class="pull-left"><i class="fa fa-undo"></i>&nbsp;</span>
	                                  	<span class="pull-left"><%= rb.getString("restore_class") %>&nbsp;&nbsp;&nbsp;</span>
	                                  </div>
	                            </a>
                               <a href="<c:out value="${pageContext.request.contextPath}"/>/tt/tt/setClassActiveFlag?teacherId=${teacherId}&classId=${c.classid}&activeFlag=-1">
                               		<div>
	                                  	<span class="pull-left"><i class="fa fa-trash">&nbsp;</i></span>
	                                  	<span class="pull-left"><%= rb.getString("delete_class") %></span>
	                                  </div>
	                            </a>
                                <div class="clearfix"></div>
                             </div>
                        </div>





                    </div>
                    <c:if test="${loop.index == terminator}">
                    <!-- t div-->
                </div>
                </c:if>
                <c:if test="${loop.last == 'true'}">
                <!-- s div-->
            </div>
            </c:if>
            </c:forEach>
            </c:if>
            </div>
            
       	<div id="teacher-activities-wrapper" style="display: none;">
	        <div class="row">
	            <div class="col-lg-12">
	                <h1 class="page-header">
	                    <small><%= rb.getString("view_teacher_activities") %></small>
	                </h1>
	            </div>
            </div>
			<div>
		        <div class="row">
		           	<div id="teacher-activities-input" class="col-lg-12">
			            <div class="row">
			                <label class="pull-left" for="pwd"><%= rb.getString("access_code")%>: </label>
			                <div class="col-sm-2">
			                    <input type="text" name="pwd" class="form-control pull-left" id="pwd">
			                </div>		 		 
			                <div class="col-sm-2">
			                    <button id="pwdBtn" type="text" class="btn btn-success pull-left"><%= rb.getString("submit")%></button>
			                </div> 							
						</div>
			        </div>
		        </div>
				      
		        <div class="row">
		        	<p> </p>
		        </div>
		        <div class="row">

		           		<div id="dropdownDiv" class="col-lg-2 teacher-dropdown">
						  <ul id="myDropdown" class="nobull">
						    <i class="fa fa-search" aria-hidden="true"></i>
						    <input type="text" class="report_filters" placeholder="Search..." id="myInput" onkeyup="filterFunction()" >
						    <div id="teacherList">
						  </ul>
						</div>			           		
		        </div>
		    </div>
		</div>
        <div id="panel-wrapper" class="row" style="display:none;width: 100%;">

            <div class="panel-group" id="accordion">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a id="report_one" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#teacherActivities">
                                <%= rb.getString("teacher_log_report") %>
                            </a>
                            <button id="oneButton" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                        </h4>
                    </div>
                    <div id="teacherActivities" class="panel-collapse collapse">
                        <div class="panel-body hidden">
                            <ul>
                                <li>
                                    <label style="padding-right: 10px;">Download Teacher Report (TBD)</label>
<!--
                                    <a href="${pageContext.request.contextPath}/tt/tt/downLoadPerStudentReport?teacherId=${teacherId}&classId=${classInfo.classid}"
                                       data-toggle="tooltip" title="rb.getString("download_this_report")>"
                                       class="downloadPerStudentReport" aria-expanded="true"
                                       aria-controls="teacherActivities">
                                        <i class="fa fa-download fa-2x" aria-hidden="true"></i>
                                    </a>
-->
                                </li>
                            </ul>
                        </div>


                        <div class="panel-body">
				           	<div id="teacher-activities-content" class="col-lg-10">
	                            <table id="perTeacherReport" class="table table-striped table-bordered hover" width="100%"></table>
				           	</div>
                        </div>

                    </div>
                </div>
                  
                <div class="panel panel-default hidden">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a id="report_two" class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#teacherActivities">
                                REPORT TWO
                            </a>
                            <button id="twoButton" type="button" class="close" onclick="$('.collapse').collapse('hide')">&times;</button>                             
                        </h4>
                    </div>
                    <div id="cohortMontioring" class="panel-collapse collapse">
                        <div class="panel-body">
				           	<div id="cohort-monitoring-content" class="col-lg-10">
	                            <table id="cohortReport" class="table table-striped table-bordered hover" width="100%"></table>
				           	</div>
                        </div>

                    </div>
                </div>
            </div>
            <div id="class_Level_Reports_Container" class="row" style="display:none;width: 75%;">
            </div>
		</div>


        <div id="form-wrapper" style="display: none;">
            <div class="col-lg-12">
                <h1 class="page-header">
                    <small><%= rb.getString("class_setup") %></small>
                </h1>
            </div>
           	<div class="loader" style="display: none" ></div>               
            <springForm:form id="create_class_form" method="post"
                             action="${pageContext.request.contextPath}/tt/tt/ttCreateClass"
                             modelAttribute="createClassForm">
                <div class="row">
                    <div class="panel panel-default">
	                    <div id="create_class_out_left" class="col-md-4 col-sm-4">
                            <div class="panel-heading">
                                <%= rb.getString("identification_settings") %>
                            </div>
                             <div class="panel-body">
                               <div class="form-group">
                                    <label for="classLanguage"><%= rb.getString("class_language") %></label>
                                    <div class="input-group">
                                        <span class="input-group-addon"><i
                                                class="glyphicon glyphicon-education"></i></span>
                                        <springForm:select path="classLanguage" class="form-control" id="classLanguage"
                                                           name="classLanguage">
                                            <springForm:option value=""><%= rb.getString("select_language_for_class") %></springForm:option>
                                            <springForm:option value="en:English"><%= rb.getString("english") %></springForm:option>
                                            <springForm:option value="es:Spanish"><%= rb.getString("spanish") %></springForm:option>
                                        </springForm:select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="className"><%= rb.getString("class_name") %></label>
                                    <div class="input-group">
                                    <span class="input-group-addon"><i
                                            class="glyphicon glyphicon-blackboard"></i></span>
                                        <springForm:input path="className" id="className" name="className"
                                                          class="form-control" type="text" required="required" />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="town"><%= rb.getString("town") %></label>
                                    <div class="input-group">
                                    <span class="input-group-addon"><i
                                            class="glyphicon glyphicon-tree-deciduous"></i></span>
                                        <springForm:input path="town" id="town" name="town"
                                                          class="form-control"
                                                          type="text"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="schoolName"><%= rb.getString("school") %></label>
                                    <div class="input-group">
                                        <span class="input-group-addon"><i class="fa fa-university"></i></span>
                                        <springForm:input path="schoolName" id="schoolName" name="schoolName"
                                                          class="form-control" type="text"/>
                                    </div>
                                </div>
                               <div class="form-group">
                                    <label for="schoolYear"><%= rb.getString("year") %></label>
                                    <div class="input-group">
                                        <span class="input-group-addon"><i
                                                class="glyphicon glyphicon-hourglass"></i></span>
                                        <springForm:select path="schoolYear" class="form-control" id="schoolYear"
                                                           name="schoolYear">
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
                                                          class="form-control" type="text"/>
                                    </div>
                                </div>
                        	</div>
                    	</div>
                   
                    	<div id="create_class_out_middle" class="col-md-4 col-sm-4">
                            <div class="panel-heading">
                                <%= rb.getString("grade_level_settings") %>
                            </div>
                            <div class="panel-body">
                                <div class="form-group">
                                    <label for="classGrade"><%= rb.getString("class_grade") %></label>
                                    <div class="input-group">
                                        <span class="input-group-addon"><i
                                                class="glyphicon glyphicon-education"></i></span>
                                        <springForm:select path="classGrade" class="form-control" id="classGrade"
                                                           name="classGrade">
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
                                                           name="lowEndDiff">
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
                                                          class="form-control" type="text" value="40" onblur="verifyProbMinMax()"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="minProb"><%= rb.getString("min_problems_per_topic") %></label>
                                    <div class="input-group">
                                    <span class="input-group-addon"><i
                                            class="glyphicon glyphicon-menu-hamburger"></i></span>
                                        <springForm:input path="minProb" id="minProb" name="minProb"
                                                          class="form-control" type="text" value="2" onblur="verifyProbMinMax()"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="maxTime"><%= rb.getString("max_time_in_topic") %></label>
                                    <div class="input-group">
                                    <span class="input-group-addon"><i
                                            class="glyphicon glyphicon-menu-hamburger"></i></span>
                                        <springForm:input path="maxTime" id="maxTime" name="maxTime"
                                                          class="form-control" type="text" value="30" onblur="verifyTimeMinMax()"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="minTime"><%= rb.getString("min_time_in_topic") %></label>
                                    <div class="input-group">
                                    <span class="input-group-addon"><i
                                            class="glyphicon glyphicon-menu-hamburger"></i></span>
                                        <springForm:input path="minTime" id="minTime" name="minTime"
                                                          class="form-control" type="text" value="0" onblur="verifyTimeMinMax()"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div id="add_students_out" class="col-md-12 col-sm-12">
                        <div id="add_students_out_panel_default" class="panel panel-default">
                            <div class="panel-heading">
                               <%= rb.getString("part_two_student_roster") %>
                            </div>

                            <div class="panel-body">
                                <span class="input-group label label-warning">P.S</span>
                                <label><%= rb.getString("student_name_instructions") %></label>
                            </div>

                            <div class="panel-body">
                                <div class="form-group">
                                    <label for="userPrefix"><%= rb.getString("student_username_prefix") %></label>
                                    <div class="input-group">
                                        <springForm:input path="userPrefix" id="userPrefix" name="userPrefix"
                                                          class="form-control" type="text"/>
                                    </div>
                                </div>
                                <div class="form-group hidden">
                                    <label for="passwordToken"><%= rb.getString("student_password") %></label>
                                    <div class="input-group">
                                        <springForm:input path="passwordToken" id="passwordToken" name="passwordToken" value="useClass"
                                                          class="form-control" type="password"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="noOfStudentAccountsForClass"><%= rb.getString("number_IDs_to_create") %></label>
                                    <div class="input-group">
                                        <springForm:input path="noOfStudentAccountsForClass"
                                                          id="noOfStudentAccountsForClass"
                                                          name="noOfStudentAccountsForClass" class="form-control"
                                                          type="text"/>
                                    </div>
                                </div>
                                <input type="hidden" name="teacherId" id="teacherId" value="${teacherId}">
                            </div>
                        </div>
                    </div>
                </div>
                <div style="text-align:center;">
                    <button role="button" type="submit" class="btn btn-primary btn-lg" ><%= rb.getString("create_class") %></button>
                </div>
            </springForm:form>
            <div class="col-lg-12">
                <h1 class="page-header">
                    <small><%= rb.getString("add_students_to_roster_instructions") %></small>
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
                
         <div id="edit-teacher-wrapper" style="display: none;">
    		<div class="registration-form vertical-center">
		        <div class="col-sm-6 col-sm-offset-3 registration-box">
		            <c:if test="${message != null && not empty message}">
		                <div class="alert alert-danger msg-bar" role="alert">${message}</div>
		            </c:if>
		            <h3 class="text-center form-label form-title"><%= rb.getString("edit_teacher_profile")%></h3>
		            <hr>
		            <form
		                    class="form-horizontal"
		                    method="post"
		                    action="${pageContext.request.contextPath}/WoAdmin?action=AdminTeacherEdit"
		            >
		                <div class="form-group">
		                    <label class="control-label col-sm-4" for="first_name"><%= rb.getString("first_name")%>:</label>
		                    <div class="col-sm-6">
		                        <input type="text" name="fname" class="form-control" id="first_name" value="${teacherFname}">
		                    </div>
		                </div><!-- form-group -->
		                <div class="form-group">
		                    <label class="control-label col-sm-4" for="last_name"><%= rb.getString("last_name")%>:</label>
		                    <div class="col-sm-6">
		                        <input type="text" name="lname" class="form-control" id="last_name" value="${teacherLname}">
		                    </div>
		                </div><!-- form-group -->
		                <div class="form-group">
		                    <label class="control-label col-sm-4" for="email"><%= rb.getString("email")%>:</label>
		                    <div class="col-sm-6">
		                        <input type="email" name="email" class="form-control" id="email" value="${teacherEmail}">
		                    </div>
		                </div><!-- form-group -->
		                <div class="form-group">
		                    <label class="control-label col-sm-4" for="password"><%= rb.getString("password")%>:</label>
		                    <div class="col-sm-6">
		                        <input type="password" name="pw1" class="form-control" id="password">
		                    </div>
		                </div><!-- form-group -->
		                <div class="form-group">
		                    <label class="control-label col-sm-4" for="password"><%= rb.getString("re_enter_password")%>:</label>
		                    <div class="col-sm-6">
		                        <input type="password" name="pw2" class="form-control" id="password-confirmation">
		                    </div>
		                </div><!-- form-group -->
		
		                <div class="form-group row">
		                    <div class="col-sm-offset-4 col-sm-4">
		                        <button type="submit" class="btn btn-default pull-right btn-block teacher-button"><%= rb.getString("submit")%></button>
		                    </div>
		                </div><!-- form-group -->
		                <div class="form-group">
		                    <label class="control-label col-sm-4" for="password">Teacher Id:</label>
		                    <div class="col-sm-6">
		                        <input type="text" name="teacherId" class="form-control" id="teacherId" value="${teacherId}" readonly>
		                    </div>
		                </div><!-- form-group -->
		            </form>
		        </div>
	        </div>
        </div>

    </div>

    <!--#page-container ends-->
</div>
<!--#page-content-wrapper ends-->

<!--Wrapper!-->

</body>
</html>