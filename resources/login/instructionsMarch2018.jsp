<%--
  Created by IntelliJ IDEA.
  User: marshall
  Date: 3/19/2018
  Time: 4:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Instructions</title>

</head>
<script type="text/javascript">
function requestFlashPermission() {
    var iframe = document.createElement('iframe');
    iframe.src = 'https://get.adobe.com/flashplayer';
    iframe.sandbox = '';
    document.body.appendChild(iframe);
    document.body.removeChild(iframe);
}

 $(document).ready(function () {
	var isNewEdge = (navigator.userAgent.match(/Edge\/(\d+)/) || [])[1] > 14;
	var isNewSafari = (navigator.userAgent.match(/OS X (\d+)/) || [])[1] > 9;
	var isNewChrome = (navigator.userAgent.match(/Chrom(e|ium)\/(\d+)/) || [])[2] > 56
    && !/Mobile/i.test(navigator.userAgent);
	var canRequestPermission = isNewEdge || isNewSafari || isNewChrome;
    $(window).one('click', requestFlashPermission);    
    var languagePreference = window.navigator.language;
	var languageSet = "en";
	if (languagePreference.includes("en")) {
		languageSet = "en"
	} else if (languagePreference.includes("es")) {
		languageSet = "es"
	}
	if (languageSet == 'es') {
		$("additional-form__header").text("Gracias por usar MathSpring");
		$("additional-form__paragraph").text("Responda algunas preguntas / siga las instrucciones que se dan a continuación para que el software pueda ayudarlo mejor.");
		$("form").find("p").text("MathSpring requiere flashplayer para trabajar. Por favor, haga clic en el botón Enviar(Submit) a continuación para habilitar esto en su navegador.");
		
	}
 });

</script>
<body>

<form method="post" name="login" action="${pageContext.request.contextPath}/WoLoginServlet">
    <input type="hidden" name="action" value="LoginInterventionInput"/>
    <input type="hidden" name="sessionId" value="${sessionId}"/>
    <input type="hidden" name="skin" value="${skin}"/>
    <input type="hidden" name="interventionClass" value="${interventionClass}"/>
	<p class="additional-form__paragraph">MathSpring requires flashplayer to work. Please click submit button below to enable this on your browser.</p>
    <br><br><input type="submit"/>
</form>
</body>
</html>
