<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Wayang Math Tutor ~ Adults</title>
    <link href="login/css/woColint.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="login/js/p7EHCscripts.js"></script>
    <script type="text/javascript">
        function signup() {
            location.href = '${pageContext.request.contextPath}/WoAdmin?action=UserRegistrationStart&clientType=${clientType}&startPage=${startPage}';

        }

        function popupCheck() {
            w = window.open('', '', "width=200,height=200");
            w.document.write("<p>Popup windows are working!</p>");
            w.focus();
        }

        function checkSound() {
            w = window.open('', '', "width=500,height=200");
            w.document.write("<html>Click the play button to check sound<br><br>");
            w.document.write('<audio controls> <source src=" http://www.w3schools.com/html/horse.ogg" type="audio/ogg">');
            w.document.write('<source src="http://www.w3schools.com/html/horse.mp3"" type="audio/mpeg"> ');
            w.document.write('Your browser does not support the audio element. ');
            w.document.write('</audio> </html>');
            w.focus();

        }

    </script>

    <!--[if lte IE 7]>
    <link href="login/css/p7ccm_ie.css" rel="stylesheet" type="text/css" media="all"/>
    <![endif]-->
    <link href="login/css/p7ccm04.css" rel="stylesheet" type="text/css" media="all"/>
</head>

<body>

<div class="container">
    <div class="content">
        <div id="p7CCM_1" class="p7CCM04 p7ccm04-outer-border p7ccm04-rounded p7ccm04-outer-shadow p7ccm04-fixed-980">
            <div class="p7ccm04-content-row p7ccm04-top-rounded p7ccm04-RGBA p7ccm-row">
                <div class="p7ccm04-3col-sidebar-left-right-column1 p7ccm-col">
                    <div class="p7ccm04-3col-sidebar-left-right-column1-cnt p7ccm04-content p7ccm04-top-rounded p7ehc-1"></div>
                </div>
                <div class="p7ccm04-3col-sidebar-left-right-column2 p7ccm-col">
                    <div class="p7ccm04-3col-sidebar-left-right-column2-cnt p7ccm04-content p7ccm04-top-rounded p7ehc-1">
                        <p>
                            <%--The easiest way to turn off these buttons--%>
                            <c:set var="preSurvey" value=""></c:set>
                            <c:if test="${preSurvey ne ''}">
                                <button onclick="javascript: location.href='${preSurvey}'" type="button">First Day
                                    Survey
                                </button>
                            </c:if>
                        </p>
                    </div>
                </div>
                <div class="p7ccm04-3col-sidebar-left-right-column3 p7ccm-col">
                    <div class="p7ccm04-3col-sidebar-left-right-column3-cnt p7ccm04-content p7ccm04-top-rounded p7ehc-1">
                        <p>
                            <%--The easiest way to turn off these buttons--%>
                            <c:set var="postSurvey" value=""></c:set>
                            <c:if test="${postSurvey ne ''}">
                                <button onclick="javascript: location.href='${postSurvey}'" type="button">Last Day
                                    Survey
                                </button>
                            </c:if>
                        </p>
                    </div>
                </div>
            </div>
            <div class="p7ccm04-content-row p7ccm04-RGBA p7ccm-row">
                <div class="p7ccm04-1col-column1 p7ccm-col">
                    <div class="p7ccm04-1col-column1-cnt p7ccm04-content"><img
                            src="login/images/msinterfaceColandAdult.png" width="980" height="164"
                            alt="Wayang Math Tutor Adult"/></div>
                </div>
            </div>
            <div class="p7ccm04-content-row p7ccm04-RGBA p7ccm-row">
                <div class="p7ccm04-1col-column1 p7ccm-col">
                    <div class="p7ccm04-1col-column1-cnt p7ccm04-content">
                        <p>To use the MathSpring Tutor, you must have pop-ups enabled, and either speakers or a
                            headset. Please use the buttons at the bottom of this page to see if your browser is
                            properly configured.</p>

                        <p>&nbsp;</p>

                        <p>&nbsp;</p>

                        <div class="nest">
                            <p></p>

                            <p></p>

                            <form method="post" action="${pageContext.request.contextPath}/WoLoginServlet">
                                <input type="hidden" name="action" value="LoginAdult_2"/>
                                <input type="hidden" name="skin" value="adult"/>
                                <input type="hidden" name="clientType" value="${clientType}"/>

                                <p>User Name:
                                    <input type="text" name="uname" value="${userName}"/>
                                    <br/>
                                    <br/>
                                    Password:
                                    <input type="password" name="password" value="${password}"/>
                                    <br/>
                                    <br/>
                                    <br/>
                                </p>

                                <p>&nbsp;</p>

                                <div class="nest2">
                                    <p>
                                        <b>${message} </b> <c:if test="${message != null}"><br><br></c:if>
                                        <input value="Log In" type="submit"/>

                                    </p>

                                    <form id="signupForm" action="${pageContext.request.contextPath}/WoAdmin">

                                        <p>
                                            <button type="button" onClick="javascript:signup();">Sign up</button>
                                        </p>
                                    </form>
                                    <p><strong>Or you may</strong></p>

                                    <form name="guest" action="${pageContext.request.contextPath}/WoLoginServlet">
                                        <input type="hidden" name="action" value="GuestLogin"/>
                                        <input type="hidden" name="clientType" value="${clientType}"/>

                                        <p>
                                            <input value="Log in as guest" type="submit"/>
                                        </p>
                                    </form>
                                    <p><strong><em>Please Note:</em></strong> Work done during a guest login will not be
                                        saved. </p>

                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="p7ccm04-content-row p7ccm04-bottom-rounded p7ccm04-RGBA p7ccm-row">
                    <div class="p7ccm04-3col-sidebar-fixed-left-right-column1 p7ccm-col">
                        <div class="p7ccm04-3col-sidebar-fixed-left-right-column1-cnt p7ccm04-content p7ccm04-bottom-rounded p7ehc-2">
                            <p>
                                <button onclick="popupCheck()">Check Pop-ups</button>
                            </p>
                        </div>
                    </div>
                    <div class="p7ccm04-3col-sidebar-fixed-left-right-column2 p7ccm-col">
                        <div class="p7ccm04-3col-sidebar-fixed-left-right-column2-cnt p7ccm04-content p7ccm04-bottom-rounded p7ehc-2">
                            <p class=fltrt>
                                <button onclick='checkSound()'>Check Sound</button>
                            </p>
                        </div>
                    </div>
                    <div class="p7ccm04-3col-sidebar-fixed-left-right-column3 p7ccm-col">
                        <div class="p7ccm04-3col-sidebar-fixed-left-right-column3-cnt p7ccm04-content p7ccm04-bottom-rounded p7ehc-2">
                            <div class="footCR">The <a href="http://wayangoutpost.com/">MathSpring</a> Mathematics
                                Tutor is under development by the <a href="http://centerforknowledgecommunication.com/">Center
                                    for Knowledge Communication</a> at the <a href="http://www.umass.edu/">University of
                                    Massachusetts Amherst</a></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- end .content --></div>
    <!-- end .container --></div>
</body>
</html>
