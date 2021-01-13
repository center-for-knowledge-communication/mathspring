<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>

<% 
ResourceBundle versions = null; 
try {
	 versions = ResourceBundle.getBundle("Versions");
}
catch (Exception e) {
	 System.out.println("versions bundle ERROR");	 
//	logger.error(e.getMessage());	
}

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
 * Frank 06-18-2020 issue #135 link to login help message page
 * Frank 12-18-20 Issue #336 added cache-busting for selected .js and .css files
 */

%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>MathSpring | <%= rb.getString("forgot_password")%></title>
    <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png">
    <link rel="icon" type="image/png" href="/favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="/favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="/manifest.json">
    <meta name="theme-color" content="#ffffff">
    <%--<link href="css/bootstrap.min.css" rel="stylesheet">--%>
    <link href="../sass_compiled/teacher_register.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet">
    <link href="../login/css/loginK12_new.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet" type="text/css" />
    <link href="../login/css/forgotPassword.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet" type="text/css" />
      <script type="text/javascript" src="../js/jquery-1.10.2.js"></script>
    <script type="text/javascript">

    </script>
</head>

<body>
<div class="nav">
    <div class="nav__logo">
        <img src="../img/mstile-150x150.png" alt="" class="nav__logo-image">
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
            <h3 class="text-center form-label form-title"><%= rb.getString("forgot_password")%></h3>
            <hr>
            <form
                    class="form-horizontal"
                    method="post"
                    action="${pageContext.request.contextPath}/tt/tt/ttPassword"
            >
                <div class="form-group">
                    <label id="usernameLabel" class="control-label col-sm-4" for="username"><%= rb.getString("username")%>:</label>
                    <div class="col-sm-6">
                        <input type="text" name="userName" class="form-control" id="username" placeholder="">
                    </div>
                </div><!-- form-group -->

                <div class="form-group">
                    <label id="orText" class="control-label col-sm-4" >  ----- <%= rb.getString("or")%> -----  </label>
                </div><!-- form-group -->

                <div class="form-group">
                    <label class="control-label col-sm-4" for="email"><%= rb.getString("email")%>:</label>
                    <div class="col-sm-6">
                        <input type="email" name="email" class="form-control" id="email" placeholder="">
                    </div>
                </div><!-- form-group -->

                <div class="form-group row">
                    <div class="col-sm-offset-4 col-sm-4">
                        <button type="submit" class="btn btn-default pull-right btn-block teacher-submit-button"><%= rb.getString("submit")%></button>
                    </div>
                </div><!-- form-group -->
				<div class="row information-box">
				     <p class="text-center">
				     <a href='${pageContext.request.contextPath}/login/loginHelpRequest.jsp' class="text-center"><%= rb.getString("login_help_request") %>: <%= rb.getString("send_help_email") %></p></a>
			 	</div>
            </form>
        </div>
    </div>
</div>
 
    <footer class="footer">
        &copy; <%= rb.getString("copyright")%>
    </footer>
</body>
</html>

