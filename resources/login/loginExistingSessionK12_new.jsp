<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>MathSpring | Existing Session</title>
    <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png">
    <link rel="icon" type="image/png" href="/favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="/favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="/manifest.json">
    <meta name="theme-color" content="#ffffff">
    <link rel="stylesheet" href="sass_compiled/existing.css">
    <%--<link href="css/bootstrap.min.css" rel="stylesheet">--%>
    <%--<link href="css/common_new.css" rel="stylesheet">--%>
    <%--<link href="login/css/loginExistingSessionK12_new.css" rel="stylesheet">--%>
</head>

<body>
<div class="nav">
    <div class="nav__logo">
        <img src="img/mstile-150x150.png" alt="" class="nav__logo-image">
        <span class="nav__logo-text">
            <span class="nav__logo-text--green-letter">M</span>ATH<span class="nav__logo-text--green-letter">S</span>PRING
        </span>
    </div>
</div>

<div class="existing">
    <div class="existing__message-box">
        <h1 class="existing__title">
            You have existing session
        </h1>
        <p class="existing__message">${message}</p>
        <div class="existing__form-wrapper">
            <form method="post" name="login" action="${pageContext.request.contextPath}/WoLoginServlet">
                <input type="hidden" name="action" value="LoginK12_1"/>
                <input type="hidden" name="skin" value="k12"/>
                <input type="hidden" name="var" value="b"/>
                <input
                        class="existing__return-button"
                        value="Return to Login Page"
                        type="submit"/>
            </form>
            <form id="signupForm" action="${pageContext.request.contextPath}/WoLoginServlet">
                <input type="hidden" name="action" value="LoginK12_2"/>
                <input type="hidden" name="uname" value="${uname}"/>
                <input type="hidden" name="password" value="${password}"/>
                <input type="hidden" name="logoutExistingSession" value="true"/>
                <input type="hidden" name="skin" value="k12"/>
                <input type="hidden" name="var" value="b"/>
                <input
                        class="existing__login-button"
                        value="I'm sure I want to login"
                        type="submit"/>
            </form>
        </div>
    </div>

</div>
<%--<footer class="bottom-sticky-footer">--%>
    <%--&copy; 2016 University of Massachusetts Amherst and Worcester Polytechnic Institute ~ All Rights Reserved.--%>
<%--</footer>--%>
</body>
</html>
