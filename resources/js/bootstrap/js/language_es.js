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