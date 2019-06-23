<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Locale"%>
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
%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>MathSpring | <%= rb.getString("student_registration")%></title>
    <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png">
    <link rel="icon" type="image/png" href="/favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="/favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="/manifest.json">
    <meta name="theme-color" content="#ffffff">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link href="css/common_new.css" rel="stylesheet" type="text/css" />
    <link href="login/css/userRegComplete_new.css" rel="stylesheet" type="text/css" />
</head>
<body>
    <div class="main-content">
        <header class="site-header" role="banner">
            <div class="row" id="wrapper">
                <div class="navbar-header">
                    <img class="logo" src="img/ms_mini_logo_new.png">
                </div><!-- navbar-header -->
            </div>
        </header>
        <div class="row account-creation-announcement-wrapper">
            <div class="col-sm-6 col-sm-offset-3 account-creation-announcement">
                <h1><%= rb.getString("ms_user_created")%></h1>
                <p><%= rb.getString("first_name")%>: ${fName}</p>
                <p><%= rb.getString("last_name")%>: ${lName}</p>
                <p><%= rb.getString("teacher")%>: ${teacher}</p>
                <p><%= rb.getString("class")%>: ${className}</p>
                <p><%= rb.getString("username")%>: ${userName}</p>
                <form id="form1" method="post" name="login" action="${pageContext.request.contextPath}/WoLoginServlet?action=LoginK12_1&var=b">
                    <input type="hidden" name="action" value="${startPage}"/>
                    <input class="btn btn-default btn-block mathspring-btn" type="submit" name="relogin" value="<%= rb.getString("return_to_ms_login")%>">
                </form>
            </div>
        </div>
    </div>
    <footer class="bottom-sticky-footer">
        &copy; <%= rb.getString("copyright")%>
    </footer>
</body>
</html>
