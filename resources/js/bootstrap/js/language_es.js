function langPrefrenceForWelcomePage() {
	var languagePreference = window.navigator.language;
	var languageSet = "en";
	if (languagePreference.includes("en")) {
		languageSet = "en"
	} else if (languagePreference.includes("es")) {
		languageSet = "es"
	}
	if (languageSet == 'es') {
		//welcome.html
		$("#intro-text").find("h1").text("Cultivá tus matemáticas con MathSpring");
		$("#intro-text").children( ".banner-text" ).text("El MathSpring (Cultivando Matemáticas) te ayudará a tener éxito en las pruebas de matemáticas,  enseñándote a comprender el material profundamente");
		$('#signup-button').text('COMENZAR');
		
		$("#stat").find("h2").text("MathSpring es...");
		$("#stat").find( ".container").children("p").text("Software personalizado que utiliza multimedia interactiva para ayudar a los alumnos a resolver problemas de matemáticas");
		$('#stat-text1').text('Más de 800 preguntas para ayudarte a practicar para pruebas de Matematicas');
		$('#stat-text2').text('20% de mejora en las calificaciones después de solo 3 horas de usar el software');
		$('#stat-text3').text('Más de 2000 estudiantes han usado el sistema de tutoría MathSpring en los Estados Unidos');
		
		$("#testimony").find("h3").text("Lo que dice la gente sobre MathSpring");
		$("#testimony").find(".col-xs-8:eq(0)").html("<p> No me gustó, me encantó! Me ayudó a aprender más de lo que pensaba. La página para ver mi progreso me ayudó porque ahora sé como ando ahora en matemáticas.</p><p><em>--Estudiante A</em></p>");
		$("#testimony").find(".col-xs-8:eq(1)").html("<p> Aprendes cosas nuevas. Si no sabes una respuesta, puedes ver un video, o hacer click en el botón de Ayuda. MathSpring te ayuda a aprender cosas que realmente no sabes.</p><p><em>--Estudiante B</em></p>");
		$("#testimony").find(".col-xs-8:eq(2)").html("<p> Lo que me gustó del software es que es una gran herramienta de aprendizaje y es divertido, creo yo.</p><p><em>--Estudiante C</em></p>");
		
		$("footer").text("© 2016 Universidad de Massachusetts Amherst y Worcester Polytechnic Institute ~ Todos los Derechos Reservados.");
	}
}

function langPrefrenceForLoginPage(){
	var languagePreference = window.navigator.language;
	var languageSet = "en";
	if (languagePreference.includes("en")) {
		languageSet = "en"
	} else if (languagePreference.includes("es")) {
		languageSet = "es"
	}
	if (languageSet == 'es') {
		$(".fullscreen").find(".col-sm-6:eq(0)").children("p").text("¿Ya tenés un nombre de usuario?");
		$(".fullscreen").find(".col-sm-6:eq(0)").children("form").find(".switch-label").text("¿Sos maestro?");
		$(".fullscreen").find(".col-sm-6:eq(0)").children("form").find("button").text("Loguearse");
		$(".fullscreen").find(".col-sm-6:eq(1)").children("p").text("¿Querés crear un nombre de usuario?");
		$(".fullscreen").find(".col-sm-6:eq(1)").find("form:eq(0)").find("button").text("Crear un usuario de Maestro");
		$(".fullscreen").find(".col-sm-6:eq(1)").find("form:eq(1)").find("button").text("Crear un usuario de Alumno");
		$(".fullscreen").find(".col-sm-6:eq(1)").find("form:eq(2)").find("button").text("Probar el programa como Visitante");
		$(".information-box").html("<p class='text-center'>Para tener una mejor experiencia, por favor fijarse que el sonido funciona, y  habilitar los pop-ups.</p>");
		$("footer").text("© 2016 Universidad de Massachusetts Amherst y Worcester Polytechnic Institute ~ Todos los Derechos Reservados.");
	}
}	
	
	function langPrefrenceForNewUserPage(isNewStudentPage){
		var languagePreference = window.navigator.language;
		var languageSet = "en";
		if (languagePreference.includes("en")) {
			languageSet = "en"
		} else if (languagePreference.includes("es")) {
			languageSet = "es"
		}
		if (languageSet == 'es') {
			if(isNewStudentPage){
			$(".form-title").text("Registro para alumnos");
			$(".student-registration-form").find(".col-sm-4:eq(0)").text("Nombre:");
			$(".student-registration-form").find(".col-sm-4:eq(1)").text("Apellido:");
			$(".student-registration-form").find(".col-sm-4:eq(2)").text("Edad:");
			$(".student-registration-form").find(".col-sm-4:eq(3)").text("Sexo:");
			$( "#gender option[value='male']").text("Masculino");
			$( "#gender option[value='female']").text("Femenino");
			$(".student-registration-form").find(".col-sm-4:eq(4)").text("Correo electrónico");
			$(".student-registration-form").find(".col-sm-4:eq(5)").text("Nombre de usuario:");
			$(".student-registration-form").find(".col-sm-4:eq(6)").text("Contraseña:");
			$(".form-check-label:eq(0)").text("Alumno regular");
			$(".form-check-label:eq(1)").text("Prueba del sistema (versión de alumno)");
			$(".form-check-label:eq(2)").text("Prueba del sistema (versión de desarrollador)");
			$(".student-button").text("Enviar");
		}else{
			$(".form-title").text("Registro para Maéstras");
			$(".form-horizontal").find(".col-sm-4:eq(0)").text("Nombre:");
			$(".form-horizontal").find(".col-sm-4:eq(1)").text("Apellido:");
			$(".form-horizontal").find(".col-sm-4:eq(2)").text("Correo electrónico:");
			$(".form-horizontal").find(".col-sm-4:eq(3)").text("Nombre de usuario:");
			$(".form-horizontal").find(".col-sm-4:eq(4)").text("Contraseña:");
			$(".form-horizontal").find(".col-sm-4:eq(5)").text("Reescribe contraseña:");
			$(".teacher-button").text("Enviar");
		}
			$("footer").text("© 2016 Universidad de Massachusetts Amherst y Worcester Polytechnic Institute ~ Todos los Derechos Reservados.");
		}
		
	}
	
	function langPrefrenceForDashBoardPage(){
		var languagePreference = window.navigator.language;
		var languageSet = "en";
		if (languagePreference.includes("en")) {
			languageSet = "en"
		} else if (languagePreference.includes("es")) {
			languageSet = "es"
		}
		if (languageSet == 'es') {
			
			$("ul").find(".nav__item:eq(0)").children("a").text("Mi Jardín");
			$("ul").find(".nav__item:eq(1)").children("a").text("Mi Progreso");
			$("ul").find(".nav__item:eq(2)").children("a").text("Area de Práctica");
			$("ul").find(".nav__item:eq(3)").children("a").text("Cerrar sesión");
			$(".topic-list").find(".welcome").children("h1").text("Bienvenidos a MathSpring");
			$(".topic-list").find(".welcome").children("p").text("¡Ve a Mi Progreso para ver todas las lecciones, o al Area de Práctica para comenzar!")
		}
		
	}
	
	function langPrefrenceForPracticePage(){
		var languagePreference = window.navigator.language;
		var languageSet = "en";
		if (languagePreference.includes("en")) {
			languageSet = "en"
		} else if (languagePreference.includes("es")) {
			languageSet = "es"
		}
		if (languageSet == 'es') {
			$('.huytran-sitenav__main .huytran-sitenav__button').each(function(i){
					$( this ).css("margin", "0px");			
			});
			$(".huytran-sitenav__main").children().find(".huytran-sitenav__button").first().css('margin', '');
			$(".huytran-sitenav__main").find(".huytran-sitenav__buttontitle:eq(0)").text("Próximo Problema");
			$(".huytran-sitenav__main").find(".huytran-sitenav__buttontitle:eq(1)").text("Ayuda");
			$(".huytran-sitenav__main").find(".huytran-sitenav__buttontitle:eq(2)").text("Repetir Ayuda");
			$(".huytran-sitenav__main").find(".huytran-sitenav__buttontitle:eq(3)").text("Leer en voz alta");
			$(".huytran-sitenav__main").find(".huytran-sitenav__buttontitle:eq(4)").text("Un Ejemplo");
			$(".huytran-sitenav__main").find(".huytran-sitenav__buttontitle:eq(5)").text("Un Video");
			$(".huytran-sitenav__main").find(".huytran-sitenav__buttontitle:eq(6)").text("Fórmulas");
			$(".huytran-sitenav__main").find(".huytran-sitenav__buttontitle:eq(7)").text("Reportar Error");
			$(".huytran-sitenav__main").find(".huytran-sitenav__buttontitle:eq(8)").text("Diccionario");
			
			$(".huytran-practice__nav").find(".huytran-practice__navitem:eq(0)").text("Mi Jardín");
			$(".huytran-practice__nav").find(".huytran-practice__navitem:eq(1)").text("Mi Progreso");
			$(".huytran-practice__nav").find(".huytran-practice__navitem:eq(2)").text("Area de Práctica");
			$(".huytran-practice__navitem--last").html("Cerrar sesión &nbsp;<span class='fa fa-sign-out'></span>");
		
		}
	}
	
	function langPrefrenceForProgressPage(){
		var languagePreference = window.navigator.language;
		var languageSet = "en";
		if (languagePreference.includes("en")) {
			languageSet = "en"
		} else if (languagePreference.includes("es")) {
			languageSet = "es"
		}
		if (languageSet == 'es') {
			
			$("nav").find(".nav-item:eq(0)").children("a").text("Mi Jardín");
			$("nav").find(".nav-item:eq(1)").children("a").text("Mi Progreso");
			$("nav").find(".nav-item:eq(2)").children("a").text("Area de Práctica");
			$("nav").find(".nav-item:eq(3)").children("a").text("Cerrar sesión");
		
			$(".progress-table").find(".progress-table-header").find("th:eq(0)").text("Lección");
			$(".progress-table").find(".progress-table-header").find("th:eq(1)").text("Comentarios**");
			$(".progress-table").find(".progress-table-header").find("th:eq(2)").text("Rendimiento");
			$(".progress-table").find(".progress-table-header").find("th:eq(3)").text("Esfuerzo");
			$(".progress-table").find(".progress-table-header").find("th:eq(4)").text("Acción");
			
			$(".progress-table").find(".col-md-2").children("p").text("Nivel de Conocimiento Alcanzado");
			$(".progress-table").find(".col-md-2").find(".mathspring-important-btn").text("Más detalles");
			$("footer").text("© 2016 Universidad de Massachusetts Amherst y Worcester Polytechnic Institute ~ Todos los Derechos Reservados.");
		
		}
	}
	
	function langPrefrenceForTTMainPage(){
		var languagePreference = window.navigator.language;
		var languageSet = "en";
		if (languagePreference.includes("en")) {
			languageSet = "en"
		} else if (languagePreference.includes("es")) {
			languageSet = "es"
		}
		if (languageSet == 'es') {
			
			$("#PageRefresh").html('<i class="fa fa-fw fa-home"></i> Inicio</a>');
			$("#createClass_handler").html('<i class="fa fa-fw fa-pencil"></i> Crear una lección nueva');
			$("#survey_problems_site").html('<i class="fa fa-fw fa-pencil"></i> Crear encuestas, pruebas o problemas de matemáticas');
			$("#report-wrapper").find(".page-header").children("small").text("Clases existentes");
			$("#report-wrapper").find(".panel-footer").find(".pull-left").text("Ver detalles");
			$("#profile_selector").html('<i class="fa fa-fw fa-user"></i> Perfil');
			$("#logout_selector").html('<i class="fa fa-fw fa-power-off"></i>Cerrar Sesión');
			
			
			$("#form-wrapper").find(".page-header").children("small").text("Organización de clase");
			$("#create_class_out").find(".panel-heading").text("Paso uno : Configuración de la clase");
			$("#create_class_out").find(".panel-body").find(".form-group:eq(0)").children("label").text("Idioma de la clase");
			$("#create_class_out").find(".panel-body").find(".form-group:eq(1)").children("label").text("Nombre de la clase");
			$("#create_class_out").find(".panel-body").find(".form-group:eq(2)").children("label").text("Localidad y Pais");
			$("#create_class_out").find(".panel-body").find(".form-group:eq(3)").children("label").text("Escuela");
			$("#create_class_out").find(".panel-body").find(".form-group:eq(4)").children("label").text("Año");
			$("#create_class_out").find(".panel-body").find(".form-group:eq(5)").children("label").text("Sección");
			$("#create_class_out").find(".panel-body").find(".form-group:eq(6)").children("label").text("Grado de la clase");
			$("#create_class_out").find(".panel-body").find(".form-group:eq(7)").children("label").text("Mínima Complejidad de problemas");
			$("#create_class_out").find(".panel-body").find(".form-group:eq(8)").children("label").text("Máxima Complejidad de problemas");
			
			$("#add_students_out_panel_default").find(".panel-heading").text("Paso dos : Listado de alumnos (Opcional)");
			$("#add_students_out_panel_default").find(".panel-body:eq(0)").children("label").text("Los siguientes campos son opcionales. Si quieres crear nombres de usuarios para los alumnos ahora mismo, asegurate de dar un prefijo único para los nombres de usuario, una contraseña y el número de usuarios que quieres crear.");
			$("#add_students_out_panel_default").find(".panel-body:eq(1)").find(".form-group:eq(0)").children("label").text("Prefijo de nombre de usuario");
			$("#add_students_out_panel_default").find(".panel-body:eq(1)").find(".form-group:eq(1)").children("label").text("Contraseña");
			$("#add_students_out_panel_default").find(".panel-body:eq(1)").find(".form-group:eq(2)").children("label").text("# de identificaciónes para crear");
			
			
		}
		}
	
	function langPrefrenceForDetailsPage(){
		var languagePreference = window.navigator.language;
		var languageSet = "en";
		if (languagePreference.includes("en")) {
			languageSet = "en"
		} else if (languagePreference.includes("es")) {
			languageSet = "es"
		}
		if (languageSet == 'es') {
			
			//Manage Problemsets
			$("#problem_set_content").find("h3:eq(0)").children("small").text("Grupos de problemas activos");
			$("#problem_set_content").find(".panel-default:eq(0)").find(".panel-body:eq(0)").text("La siguiente tabla muestra grupos de problemas activos para esta clase. Fijate qué grupos de problemas quieres desactivar y hacer clic en el botón de abajo, que dice 'Desactivar'.");
			$("#problem_set_content").find(".panel-default:eq(0)").find(".panel-body:eq(1)").text("PD: Los Grupos de problemas que verán los alumnos seguirán el orden indicado debajo. Este orden se puede reorganizar arrastrando las fillas que indican cada ''Grupo de Problemas' hacia arriba o abajo.");
			$("#problem_set_content").find(".panel-default:eq(0)").find(".panel-body:eq(2)").children("button").text("Desactivar");
			$("#activateProbSetTable").children("thead").find("tr:eq(0)").find("th:eq(0)").children("span").html("Orden&nbsp;&nbsp;");
			$("#activateProbSetTable").children("thead").find("tr:eq(0)").find("th:eq(1)").text("Grupo de problemas");
			$("#activateProbSetTable").children("thead").find("tr:eq(0)").find("th:eq(2)").children("span").html("# de Problemas Activados&nbsp;&nbsp;");
			$("#activateProbSetTable").children("thead").find("tr:eq(0)").find("th:eq(3)").text("Distribución por Grado");
			$("#activateProbSetTable").children("thead").find("tr:eq(0)").find("th:eq(4)").text("Desactivar");
			
			$("#problem_set_content").find("h3:eq(1)").children("small").text("Grupos de problemas activos");
			$("#problem_set_content").find(".panel-default:eq(1)").find(".panel-body:eq(0)").text("Las siguientes tablas muestran grupos de problemas inactivos para esta clase.");
			$("#problem_set_content").find(".panel-default:eq(1)").find(".panel-body:eq(1)").children("button").text("Activar");
			$("#inActiveProbSetTable").children("thead").find("tr:eq(0)").find("th:eq(0)").text("Orden");
			$("#inActiveProbSetTable").children("thead").find("tr:eq(0)").find("th:eq(1)").text("Grupo de problemas");
			$("#inActiveProbSetTable").children("thead").find("tr:eq(0)").find("th:eq(2)").text("Problemas disponibles");
			$("#inActiveProbSetTable").children("thead").find("tr:eq(0)").find("th:eq(3)").text("Distribución por grado");
			$("#inActiveProbSetTable").children("thead").find("tr:eq(0)").find("th:eq(4)").text("Activar Grupos de Problemas");
			
			//Manage Students
			$("#student_roster_out").find(".page-header").children("small").text("Reconfigurar información de alumno");
			$("#student_roster_out").find(".panel-default").find(".panel-body:eq(0)").text("La siguiente tabla muestra nombres de usuario para los alumnos de esta clase. Se pueden crear más nombres de usuario haciendo clic en el botón debajo.");
			
			$("#student_roster_out").find(".panel-default").find(".panel-body:eq(1)").children("button").text("Crear nombre de usuario para alumno");
			$("#student_roster_out").find(".panel-default").find(".panel-body:eq(1)").children("a").text("Descargar etiquetas con nombres de usuario, para cada alumno");
			
			$("#student_roster").children("thead").find("tr:eq(0)").find("th:eq(0)").text("Identificacíón de alumno");
			$("#student_roster").children("thead").find("tr:eq(0)").find("th:eq(1)").text("Nombre");
			$("#student_roster").children("thead").find("tr:eq(0)").find("th:eq(2)").text("Apellido");
			$("#student_roster").children("thead").find("tr:eq(0)").find("th:eq(3)").text("Nombre de usuario");
			$("#student_roster").children("thead").find("tr:eq(0)").find("th:eq(4)").text("Datos del alumno");
			$("#student_roster").children("thead").find("tr:eq(1)").find("th:eq(0)").text("Eliminar registros  de problemas de matematics para este alumno");
			$("#student_roster").children("thead").find("tr:eq(1)").find("th:eq(1)").text("Eliminar el nombre de usuario y todos sus datos/registros");
			$("#student_roster").children("thead").find("tr:eq(1)").find("th:eq(2)").text("Cambiar contraseña");
			
			
		//Replicate Classes
			$("#clone_class_out").find(".page-header").children("small").text("Duplicar esta Clase");
			$("#clone_class_out").find(".form-group:eq(0)").find("#className").attr("placeholder","Nombre de la clase");
			$("#clone_class_out").find(".form-group:eq(1)").find("#gradeSection").attr("placeholder","Sección");
			$("#clone_class_out").find(".form-group:eq(2)").children("button").text("Clonar esta clase");
			$("#clone_class_out").find("label").find("span:eq(0)").html("Estás por duplicar clase&nbsp;&nbsp;");
			$("#clone_class_out").find("label").find("span:eq(2)").html("y sección&nbsp;&nbsp;");
			$("#clone_class_out").find("label").find("span:eq(4)").html("Debes dar un nombre y sección diferente a esta nueva clase");
			
			
			//Survey Settings
			$("#reset_survey_setting_out").find(".container-fluid").find(".panel-default:eq(0)").find(".panel-body:eq(0)").find(".page-header").children("small").text("Encender/Apagar el cuestionario");
			$("#reset_survey_setting_out").find(".container-fluid").find(".panel-default:eq(0)").find(".panel-body:eq(1)").find(".form-check-label").text("Cuestionario anterior: Los alumnos verán este cuestionario solo la primera vez que el alumno use MathSpring.");
			$("#reset_survey_setting_out").find(".container-fluid").find(".panel-default:eq(0)").find(".panel-body:eq(2)").find(".form-check-label").text("Los alumnos verán este cuestionario cada vez que inician una sesion, hasta que lo completen");
			$("#reset_survey_setting_out").find(".container-fluid").find(".panel-default:eq(0)").find(".panel-body:eq(3)").children("button").text("Enviar");
			$("#reset_survey_setting_out").find(".container-fluid").find(".panel-default:eq(1)").find(".panel-body:eq(0)").children("h1").children("small").text("Cuestionarios/Pruebas disponibles");
			$("#reset_survey_setting_out").find(".container-fluid").find(".panel-default:eq(1)").find(".panel-body:eq(1)").find("#activeSurveyList").children("thead").find("tr").find("th:eq(0)").text("Identificación del Cuestionario");
			$("#reset_survey_setting_out").find(".container-fluid").find(".panel-default:eq(1)").find(".panel-body:eq(1)").find("#activeSurveyList").children("thead").find("tr").find("th:eq(1)").text("Lista de cuestionarios/pruebas");
			$("#reset_survey_setting_out").find(".container-fluid").find(".panel-default:eq(1)").find(".panel-body:eq(1)").find("#activeSurveyList").children("thead").find("tr").find("th:eq(2)").text("Primera vez que el alumno inicia una sesión");
			$("#reset_survey_setting_out").find(".container-fluid").find(".panel-default:eq(1)").find(".panel-body:eq(1)").find("#activeSurveyList").children("thead").find("tr").find("th:eq(3)").text("Siguiente vez que el alumno inicia una sesión");
			
			$("#reset_survey_setting_out").find(".container-fluid").find(".panel-default:eq(1)").find(".panel-body:eq(2)").children("ul").find("li:eq(0)").text("* Nota: Esto cuestionario se mostrará solamente una vez, la primera vez que el alumno inicia sesión en MathSpring");
			$("#reset_survey_setting_out").find(".container-fluid").find(".panel-default:eq(1)").find(".panel-body:eq(2)").children("ul").find("li:eq(1)").text("** Nota: Si hay un cuestionario elejido para la 'siguiente vez alumno inicia sesión', alumnos lo verán cada vez que incian sesión. Debes encender esto solamente cuando estás seguro que quieres recibir esto nuevo cuestionario/prubea");
			$("#reset_survey_setting_out").find(".container-fluid").find(".panel-default:eq(1)").find(".panel-body:eq(3)").children("button").text("Publicar Configuración del cuestionario");
			
			//Class Reports
			$("#report_three").text("Resumen de la Clase por Alumno");
			$("#collapseThree").find(".panel-body:eq(0)").find("label:eq(0)").text("Descargar datos de alumno, muchas filas por alumno");
			$("#collapseThree").find(".panel-body:eq(0)").find("label:eq(1)").text("Descargar datos de emoción, muchas filas por alumno");
			
			
			$("#report_four").text("Resumen de clase por Unidad Curricular");
			$("#perClusterLegendTable").children("thead").find("tr").find("th:eq(0)").text("Rango de %");
			$("#perClusterLegendTable").children("thead").find("tr").find("th:eq(1)").text("Significado");
			
			$("#perClusterLegendTable").children("tbody").find("tr:eq(0)").find("td:eq(0)").text("Para más que 5 problemas, solo 20%-40% para  esta norma se resolvieron en el primer intento");
			$("#perClusterLegendTable").children("tbody").find("tr:eq(0)").find("td:eq(1)").text("Temas que fueron dificiles para tus alumnos");
			$("#perClusterLegendTable").children("tbody").find("tr:eq(1)").find("td:eq(0)").text("Para más que 5 problemas, menos que 20% de problemas se resolvieron en el primer intento");
			$("#perClusterLegendTable").children("tbody").find("tr:eq(1)").find("td:eq(1)").text("Grupos que alumnos hallaron muy difíciles");
			
			
			$("#report_one").text("Resumen de clase por alumno, por cada grupo de problemas");
			$("#collapseOne").find(".panel-body:eq(0)").find("label").text("Esta tabla muestra el desempeño de los alumnos de esta clase, en cada grupo de problemas");
			$("#perTopicReportLegendTable").children("thead").find("tr").find("th:eq(0)").text("Rango de Estimación del Nivel Alcanzado por el Alumno");
			$("#perTopicReportLegendTable").children("thead").find("tr").find("th:eq(1)").text("Nota y clave de color para 2 o más problemas");
			$("#perTopicReportLegendTable").children("thead").find("tr").find("th:eq(2)").text("Nota y clave de color para 10 o más problemas");
			$("#perTopicReportLegendTable").children("tbody").find("tr:eq(0)").find("td:eq(0)").text("0.75 o más");
			$("#perTopicReportLegendTable").children("tbody").find("tr:eq(0)").find("td:eq(1)").text("Nota:  E (Excelente)");
			$("#perTopicReportLegendTable").children("tbody").find("tr:eq(0)").find("td:eq(2)").text("Nota:  E (Excelente)");
			$("#perTopicReportLegendTable").children("tbody").find("tr:eq(1)").find("td:eq(0)").text("Entre 0.5 y 0.75");
			$("#perTopicReportLegendTable").children("tbody").find("tr:eq(1)").find("td:eq(1)").text("Nota: B (Bueno o Muy Bueno)");
			$("#perTopicReportLegendTable").children("tbody").find("tr:eq(1)").find("td:eq(2)").text("Nota: B (Bueno o Muy Bueno)");
			$("#perTopicReportLegendTable").children("tbody").find("tr:eq(2)").find("td:eq(0)").text("Entre 0.25 y 0.5");
			$("#perTopicReportLegendTable").children("tbody").find("tr:eq(2)").find("td:eq(1)").text("Nota: S (Satisfactorio, pero Necesita Mejorar)");
			$("#perTopicReportLegendTable").children("tbody").find("tr:eq(2)").find("td:eq(2)").text("Nota: S (Satisfactorio, pero Necesita Mejorar)");
			$("#perTopicReportLegendTable").children("tbody").find("tr:eq(3)").find("td:eq(0)").text("0.25 o menos");
			$("#perTopicReportLegendTable").children("tbody").find("tr:eq(3)").find("td:eq(1)").text("Nota: NS (No Satisfactorio)");
			$("#perTopicReportLegendTable").children("tbody").find("tr:eq(3)").find("td:eq(2)").text("Nota: NS (No Satisfactorio)");
			
			$("#collapseOne").find(".panel-body:eq(2)").find("li:eq(0)").text("Cada celda muestra [# problemas resueltos en el primer intento / total de problemas resueltos] junto con el nivel más alto de Maestría alcanzado para eso grupo de problemas");
			$("#collapseOne").find(".panel-body:eq(2)").find("li:eq(1)").text("Sólo cuando el alumno ha intentado 2 o más problemas, la ceda está codificada con color");
			$("#collapseOne").find(".panel-body:eq(2)").find("li:eq(2)").text("Haz clic en la celda para ver la trayectoria de Nivel Alcanzado por el alumno, para cada grupo de problemas");
			
		}
		}
	
	