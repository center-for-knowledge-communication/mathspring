<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>MathSpring Login</title>
    <link rel="apple-touch-icon" sizes="180x180" href="${pageContext.request.contextPath}/img/apple-touch-icon.png">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="css/manifest.json">
    <meta name="theme-color" content="#ffffff">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/sass_compiled/logink12.css">
    <%--<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">--%>
    <%--<link href="${pageContext.request.contextPath}/css/common_new.css" rel="stylesheet" type="text/css" />--%>
    <%--<link href="${pageContext.request.contextPath}/login/css/loginK12_new.css" rel="stylesheet" type="text/css" />--%>
    <%--<link href="${pageContext.request.contextPath}/login/css/switcher-button.css" rel="stylesheet" type="text/css" />--%>
    <script type="text/javascript" src="<c:url value="/js/bootstrap/js/jquery-2.2.2.min.js" />"></script>
    <script type="text/javascript" src="<c:url value="/login/js/p7EHCscripts.js" />"></script>
    <script src="js/bootstrap/js/language_es.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            var $userSwitcher = $('#usertypeswitcher');
            var $userLoginForm = $('.user-login-form');
            var $userLoginFormUsername = $('.user-login-form-username');
            var $loginSubmitBtn = $('.js-login-btn');
            $userSwitcher.change(function() {
                if (! $(this).is(':checked')) {
                    $userLoginForm.attr('action', '${pageContext.request.contextPath}/WoLoginServlet');
                    $userLoginFormUsername.attr('name', 'uname');
                } else {
                    $userLoginForm.attr('action', '${pageContext.request.contextPath}/tt/tt/ttMain');
                    $userLoginFormUsername.attr('name', 'userName');
                    $loginSubmitBtn.attr('name', 'login');
                    $loginSubmitBtn.attr('value', 'Login');
                }
            });
            langPrefrenceForLoginPage();
        });
        function signup() {
            location.href = '${pageContext.request.contextPath}/WoAdmin?action=UserRegistrationStart&var=b&startPage=${startPage}';
        }
    </script>
</head>
<body>