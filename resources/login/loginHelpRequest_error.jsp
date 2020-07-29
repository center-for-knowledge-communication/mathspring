<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
	//logger.error(e.getMessage());
}

/**
 * Frank 06-18-2020 issue #135 new jsp for login help request
*/

%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>MathSpring | <%= rb.getString("login_help_request")%></title>
    <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png">
    <link rel="icon" type="image/png" href="/favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="/favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="/manifest.json">
    <meta name="theme-color" content="#ffffff">
    <%--<link href="css/bootstrap.min.css" rel="stylesheet">--%>
    <%--<link href="css/common_new.css" rel="stylesheet">--%>
    <link href="../../sass_compiled/teacher_register.css" rel="stylesheet">
    <link href="../../login/css/forgotPassword.css" rel="stylesheet" type="text/css" />
      <script type="text/javascript" src="../js/jquery-1.10.2.js"></script>
    <script type="text/javascript">

    </script>
</head>

<body>
<div class="nav">
    <div class="nav__logo">
        <img src="../../img/mstile-150x150.png" alt="" class="nav__logo-image">
        <span class="nav__logo-text">
                <span class="nav__logo-text--green-letter">M</span>ATH<span class="nav__logo-text--green-letter">S</span>PRING
            </span>
    </div>
</div>
<div class="bootstrap main-content">
    <div class="registration-form vertical-center">
        <div class="col-sm-6 col-sm-offset-3 registration-box">
            <c:if test="${message != null && not empty message}">
                <div class="alert alert-danger msg-bar" role="alert">${message}</div>
            </c:if>
            <h3 class="text-center form-label form-title"><%= rb.getString("login_help_request")%></h3>
            <hr>
            <form
                    class="form-horizontal"
                    method="post"
                    action="${pageContext.request.contextPath}/tt/tt/ttLoginHelp"
            >
                <div class="form-group">
                    <label class="control-label col-sm-4" for="email"><%= rb.getString("email")%>:</label>
                    <div class="col-sm-6">
                        <input type="email" name="email" class="form-control" id="email" placeholder="">
                    </div>
                </div><!-- form-group -->

                <div class="form-group">
                    <label id="helpmsgLabel" class="control-label col-sm-4" for="helpmsg"><%= rb.getString("type_your_message_here")%>:</label>
                    <div class="col-sm-6">
                    	<textarea id="helpmsg" name="helpmsg" class="form-control" rows="6" cols="50">
                    	</textarea>
                    </div>
                </div><!-- form-group -->

                <div class="form-group row">
                    <div class="col-sm-offset-4 col-sm-4">
                        <button type="submit" class="btn btn-default pull-right btn-block teacher-submit-button"><%= rb.getString("submit")%></button>
                    </div>
                </div><!-- form-group -->
            </form>
        </div>
    </div>
</div>
 
    <footer class="footer">
        &copy; <%= rb.getString("copyright")%>
    </footer>
</body>
</html>

