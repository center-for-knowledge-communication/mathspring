<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
    <link href="login/css/switcher-button.css" rel="stylesheet" type="text/css" />
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
            <div class="col-sm-6 col-sm-offset-3 login-box">
                <div class="row sign-in-up-box">
                    <div class="col-sm-6">
                        <form
                                method="post"
                                action="${pageContext.request.contextPath}/WoAdmin?action=AdminTeacherLogin&var=b">
                            <button
                                    class="btn btn-primary btn-lg btn-block signup-btn teacher-sign-up-btn"
                                    type="submit"
                                    name="register" value="Register"
                            >Sign up for Teacher</button>
                        </form>
                        <form action="${pageContext.request.contextPath}/WoAdmin">
                            <button
                                    class="btn btn-primary btn-lg btn-block signup-btn student-sign-up-btn"
                                    type="button"
                                    onClick="javascript:signup();"
                            >Sign up for Student</button>
                        </form>
                        <form name="guest" action="${pageContext.request.contextPath}/WoLoginServlet">
                            <input type="hidden" name="action" value="GuestLogin"/>
                            <input type="hidden" name="clientType" value="${clientType}"/>
                            <input type="hidden" name="var" value="b"/>
                            <button
                                    class="btn btn-primary btn-lg btn-block signup-btn guest-try-out-btn"
                                    type="submit">Try out as Guest</button>
                        </form>
                    </div>
                    <div class="col-sm-6 login-form">
                        <p>Have a username and password already? Enter them here!</p>
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
                                        placeholder="Username"
                                        autofocus
                                />
                            </div>
                            <div class="form-group <c:if test="${message != null}">has-error</c:if>">
                                <input
                                        type="password"
                                        name="password"
                                        value="${password}"
                                        class="form-control nav-login"
                                        placeholder="Password"
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
                                            class="btn btn-default btn-block sign-in-btn js-login-btn">Login</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="row information-box">
                    <p class="text-center">To have the best experience, enable pop-ups and turn on either speakers or a headset!</p>
                </div>
            </div>
        </div>
    </div>
    <footer class="bottom-sticky-footer">
        &copy; 2016 University of Massachusetts Amherst and Worcester Polytechnic Institute ~ All Rights Reserved.
    </footer>
</body>
</html>
