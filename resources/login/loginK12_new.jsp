<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>MathSpring Login</title>
    <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png">
    <link rel="icon" type="image/png" href="/favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="/favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="/manifest.json">
    <meta name="theme-color" content="#ffffff">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link href="css/common_new.css" rel="stylesheet" type="text/css" />
    <link href="login/css/loginK12_new.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="js/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="login/js/p7EHCscripts.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            var $userSwitcher = $('#usertypeswitcher');
            var $userLoginForm = $('.user-login-form');
            var $userLoginFormUsername = $('.user-login-form-username');
            var $loginSubmitBtn = $('.js-login-btn');
            $userSwitcher.change(function() {
                if ($(this).is(':checked')) {
                    $userLoginForm.attr('action', '${pageContext.request.contextPath}/WoLoginServlet');
                    $userLoginFormUsername.attr('name', 'uname');
                } else {
                    $userLoginForm.attr('action', '${pageContext.request.contextPath}/tt/tt/ttMain');
                    $userLoginFormUsername.attr('name', 'userName');
                    $loginSubmitBtn.attr('name', 'login');
                    $loginSubmitBtn.attr('value', 'Login');
                }
            });
        });
        function signup() {
            location.href = '${pageContext.request.contextPath}/WoAdmin?action=UserRegistrationStart&var=b&startPage=${startPage}';
        }
    </script>


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

// styles contain hard-coded content - conditionally include style based on langauge
if (lang == "Spanish" ) {
%>
<style>
.onoffswitch {
    margin-top: 2px;
    position: relative; width: 100px;
    -webkit-user-select:none; -moz-user-select:none; -ms-user-select: none;
}
.onoffswitch-checkbox {
    display: none;
}
.onoffswitch-label {
    display: block; overflow: hidden; cursor: pointer;
    border: 1px solid #999999; border-radius: 20px;
}
.onoffswitch-inner {
    display: block; width: 200%; margin-left: -100%;
    transition: margin 0.3s ease-in 0s;
}
.onoffswitch-inner:before, .onoffswitch-inner:after {
    display: block; float: left; width: 50%; height: 30px; padding: 0; line-height: 30px;
    font-size: 14px; color: white; font-family: Raleway, san-serif;
    box-sizing: border-box;
}
.onoffswitch-inner:before {
    content: "Alumno";
    padding-left: 10px;
    background-color: #FFFFFF; color: #000;
}
.onoffswitch-inner:after {
    content: "Maestro";
    padding-right: 10px;
    background-color: #FFFFFF; color: #000;
    text-align: right;
}
.onoffswitch-switch {
    display: block; width: 20px; margin: 6px;
    background: #FFFFFF;
    position: absolute; top: 0; bottom: 0;
    right: 66px;
    border: 1px solid #999999; border-radius: 25px;
    transition: all 0.3s ease-in 0s;
}
.onoffswitch-checkbox:checked + .onoffswitch-label .onoffswitch-inner {
    margin-left: 0;
}
.onoffswitch-checkbox:checked + .onoffswitch-label .onoffswitch-switch {
    right: 0px;
}
</style>
<%
}
else {
%>
<style>
.onoffswitch {
    margin-top: 2px;
    position: relative; width: 100px;
    -webkit-user-select:none; -moz-user-select:none; -ms-user-select: none;
}
.onoffswitch-checkbox {
    display: none;
}
.onoffswitch-label {
    display: block; overflow: hidden; cursor: pointer;
    border: 1px solid #999999; border-radius: 20px;
}
.onoffswitch-inner {
    display: block; width: 200%; margin-left: -100%;
    transition: margin 0.3s ease-in 0s;
}
.onoffswitch-inner:before, .onoffswitch-inner:after {
    display: block; float: left; width: 50%; height: 30px; padding: 0; line-height: 30px;
    font-size: 14px; color: white; font-family: Raleway, san-serif;
    box-sizing: border-box;
}
.onoffswitch-inner:before {
    content: "Student";
    padding-left: 10px;
    background-color: #FFFFFF; color: #000;
}
.onoffswitch-inner:after {
    content: "Teacher";
    padding-right: 10px;
    background-color: #FFFFFF; color: #000;
    text-align: right;
}
.onoffswitch-switch {
    display: block; width: 20px; margin: 6px;
    background: #FFFFFF;
    position: absolute; top: 0; bottom: 0;
    right: 66px;
    border: 1px solid #999999; border-radius: 25px;
    transition: all 0.3s ease-in 0s;
}
.onoffswitch-checkbox:checked + .onoffswitch-label .onoffswitch-inner {
    margin-left: 0;
}
.onoffswitch-checkbox:checked + .onoffswitch-label .onoffswitch-switch {
    right: 0px;
}
</style>
<% 
}
%>
</head>
<body>
    <div class="main-content">
        <c:if test="${message != null && not empty message}">
            <div class="alert alert-danger msg-bar" role="alert">${message}</div>
        </c:if>
        <header class="site-header" role="banner">
            <div class="row" id="wrapper">
                <div class="navbar-header">
                    <img class="logo" src="img/ms_mini_logo_new.png">
                </div><!-- navbar-header -->
            </div>
        </header>
        <div class="row login-box-wrapper">
            <div class="col-sm-8 col-sm-offset-3 login-box">
                <div class="row sign-in-up-box">
                    <div class="col-sm-7">
                        <form
                                method="post"
                                action="${pageContext.request.contextPath}/WoAdmin?action=AdminTeacherLogin&var=b">
                            <button
                                    class="btn btn-primary btn-lg btn-block signup-btn teacher-sign-up-btn"
                                    type="submit"
                                    name="register" value="Register"
                            ><%= rb.getString("signup_teacher") %></button>
                        </form>
                        <form action="${pageContext.request.contextPath}/WoAdmin">
                            <button
                                    class="btn btn-primary btn-lg btn-block signup-btn student-sign-up-btn"
                                    type="button"
                                    onClick="javascript:signup();"
                            ><%= rb.getString("signup_student") %></button>
                        </form>
                        <form name="guest" action="${pageContext.request.contextPath}/WoLoginServlet">
                            <input type="hidden" name="action" value="GuestLogin"/>
                            <input type="hidden" name="clientType" value="${clientType}"/>
                            <input type="hidden" name="var" value="b"/>
                            <button
                                    class="btn btn-primary btn-lg btn-block signup-btn guest-try-out-btn"
                                    type="submit"><%= rb.getString("signup_guest") %></button>
                        </form>
                    </div>
                    <div class="col-sm-5 login-form">
                        <p><%= rb.getString("have_username_already_enter_here") %></p>
                        <form
                                class="user-login-form"
                                method="post"
                                name="login"
                                action="${pageContext.request.contextPath}/WoLoginServlet">
                            <input type="hidden" name="action" value="LoginK12_2"/>
                            <input type="hidden" name="skin" value="k12"/>
                            <input type="hidden" name="var" value="b"/>
                            <div class="form-group <c:if test="${message != null}">has-error</c:if>">
                                <input
                                        type="text"
                                        name="uname"
                                        value="${userName}"
                                        class="form-control nav-login user-login-form-username"
                                        placeholder="<%= rb.getString("username") %>"
                                        autofocus
                                />
                            </div>
                            <div class="form-group <c:if test="${message != null}">has-error</c:if>">
                                <input
                                        type="password"
                                        name="password"
                                        value="${password}"
                                        class="form-control nav-login"
                                        placeholder="<%= rb.getString("password") %>"
                                />
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <div class="onoffswitch">
                                        <input
                                                type="checkbox"
                                                name="usertype"
                                                class="onoffswitch-checkbox"
                                                id="usertypeswitcher" checked>
                                        <label class="onoffswitch-label" for="usertypeswitcher">
                                            <span class="onoffswitch-inner"></span>
                                            <span class="onoffswitch-switch"></span>
                                        </label>
                                    </div>
                                </div>
                                <div class="col-sm-6">
                                    <button
                                            type="submit"
                                            class="btn btn-default btn-block sign-in-btn js-login-btn"><%= rb.getString("login") %></button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="row information-box">
                    <p class="text-center"><%= rb.getString("use_audio_device") %></p>
                </div>
            </div>
        </div>
    </div>
    <footer class="bottom-sticky-footer">
        &copy; <%= rb.getString("copyright") %>
    </footer>
</body>
</html>
