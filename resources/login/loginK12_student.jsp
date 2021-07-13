<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>
<% 
/**

 * Frank 01-10-21 Issue #289 new file cloned from loginK12_new.jsp for re-org Welcome.html interface - provides 'student only' login page

 */
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
//	logger.error(e.getMessage());
}
%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>MathSpring | <%= rb.getString("login")%></title>
    <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png">
    <link rel="icon" type="image/png" href="/favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="/favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="/manifest.json">
    <meta name="theme-color" content="#ffffff">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link href="css/common_new.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet" type="text/css" />
    <link href="login/css/loginK12_new.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="js/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="login/js/p7EHCscripts.js"></script>
    <script type="text/javascript">
    $(document).ready(function() {

        var $userLoginForm = $('.user-login-form');
        var $userLoginFormUsername = $('.user-login-form-username');
        var $loginSubmitBtn = $('.js-login-btn');
/*
        $("#forgotPasswordBtn").hide();
        
        $userSwitcher.change(function() {
            if ($(this).is(':checked')) {
                $userLoginForm.attr('action', '${pageContext.request.contextPath}/tt/tt/ttMain');
                $userLoginFormUsername.attr('name', 'userName');
                $loginSubmitBtn.attr('name', 'login');
                $loginSubmitBtn.attr('value', 'Login');
                $("#forgotPasswordBtn").show();
            } else {
                $userLoginForm.attr('action', '${pageContext.request.contextPath}/WoLoginServlet');
                $userLoginFormUsername.attr('name', 'uname');
                $("#forgotPasswordBtn").hide();
            }
        });
    });
*/           
        function signup() {
            location.href = '${pageContext.request.contextPath}/WoAdmin?action=UserRegistrationStart&var=b&startPage=${startPage}&classId=0';
        }

    </script>

</head>

<header class="site-header" role="banner">
    <div class="row" id="wrapper">
        <div class="navbar-header">
            <img class="logo" src="img/ms_mini_logo_new.png">
        </div><!-- navbar-header -->
    </div>
</header>
<body>
<div>
    <div class="bootstrap fullscreen">
        <div class="container">
	        <div class="row vertical-center">
	            <div class="col-sm-9 col-sm-offset-2 main-box">
			        <c:if test="${message != null && not empty message}">
	    		        <div class="alert alert-danger msg-bar" role="alert">${message}</div>
	        		</c:if>
	                <div class="row sign-in-up-box">
	                    <div class="col-sm-6">
                            <p class="text-center"><%= rb.getString("have_username_already") %></p>
                            <p class="text-center"><%= rb.getString("enter_them_here") %></p>
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
	                                        placeholder="<%= rb.getString("password_placeholder") %>"
	                                />
	                            </div>
	                            <div class="form-group <c:if test="${message != null}">has-error</c:if>">
	                                <input
	                                        type="hidden"
	                                        name="forgot"
	                                        id="forgot";
	                                        value="on"
	                                        class="form-control nav-login"
	                                />
	                            </div>
	                            
                                <div class="row">

                                <div class="col-sm-6">
                                    <button
                                            type="submit"
                                            class="btn btn-default btn-block sign-in-btn js-login-btn"><%= rb.getString("login") %></button>
                                </div>
                            </div>
	                       	</form>
	                    </div>                
	
	                
	                    <div class="col-sm-6  login-form">
	                        <p class="text-center"><%= rb.getString("do_you_need_username") %></p>
	                        <p class="text-center"></p>
	                        <p> </p>                   
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
	                                    class="btn btn-danger btn-lg btn-block signup-btn guest-try-out-btn"
	                                    type="submit"><%= rb.getString("signup_guest") %></button>
	                        </form>
	                    </div>
	
	                
	                </div>
	                <div class="row information-box">
	                    <p class="text-center"><%= rb.getString("use_audio_device") %></p>
	                </div>
	            </div>
	        </div>
    	</div>
    </div>
</div>
    <footer class="bottom-sticky-footer">
        &copy; <%= rb.getString("copyright") %>
    </footer>
</body>
</html>
