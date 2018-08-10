<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>MathSpring K-12</title>
    <script type="text/javascript" src="js/jquery-1.10.2.js"></script>
    <link href="login/css/portK12.css" rel="stylesheet" type="text/css"/>
    <link href="login/css/p7ccm04.css" rel="stylesheet" type="text/css" media="all"/>
    <!--[if lte IE 7]>
    <link href="/login/css/p7ccm_ie.css" rel="stylesheet" type="text/css" media="all"/>
    <![endif]-->
    <script type="text/javascript" src="login/js/p7EHCscripts.js"></script>

    <script type="text/javascript">

    </script>
</head>
<%--<jsp:useBean id="classes" scope="request" type="edu.umass.ckc.wo.beans.Classes"/>--%>

<body>

<div class="container">
    <div class="content">
        <div id="p7CCM_1" class="p7CCM04 p7ccm04-fixed-980">
            <div class="p7ccm04-content-row p7ccm04-RGBA p7ccm-row">
                <div class="p7ccm04-3col-sidebar-left-right-column1 p7ccm-col">
                    <div class="p7ccm04-3col-sidebar-left-right-column1-cnt p7ccm04-content"></div>
                </div>
                <div class="p7ccm04-3col-sidebar-left-right-column2 p7ccm-col">
                    <div class="p7ccm04-3col-sidebar-left-right-column2-cnt p7ccm04-content">
                        <p>
                            <%--<c:if test="${preSurvey ne ''}"><button onclick="javascript: location.href='${preSurvey}'" type="button">First Day Survey</button> </c:if>--%>
                        </p>
                    </div>
                </div>
                <div class="p7ccm04-3col-sidebar-left-right-column3 p7ccm-col">
                    <div class="p7ccm04-3col-sidebar-left-right-column3-cnt p7ccm04-content">
                        <p>
                            <%--<c:if test="${postSurvey ne ''}"><button onclick="javascript: location.href='${postSurvey}'" type="button">Last Day Survey</button> </c:if>--%>
                        </p>
                    </div>
                </div>
            </div>
            <div class="p7ccm04-content-row p7ccm04-RGBA p7ccm-row">
                <div class="p7ccm04-1col-column1 p7ccm-col">
                    <div class="p7ccm04-1col-column1-cnt p7ccm04-content"><img src="login/images/ms-k12-portMast.png"
                                                                               width="980" height="148"
                                                                               alt="MathSpring Math Tutor K 12"/></div>
                </div>
            </div>
            <div class="p7ccm04-content-row p7ccm04-RGBA p7ccm-row">
                <div class="p7ccm04-1col-column1 p7ccm-col">
                    <div class="p7ccm04-1col-column1-cnt p7ccm04-content">
                        <p>Select the class you are registering for:</p>
                        <table width="70%" border="1">
                            <tr>
                                <td height="22">Teacher</td>
                                <td height="22">School</td>
                                <td height="22">Class</td>
                                <td height="22">Town</td>
                                <td height="22">Choose</td>
                            </tr>
                            <%--@elvariable id="c" type="edu.umass.ckc.wo.beans.ClassInfo"--%>
                            <c:forEach var="c" items="${classes}">
                                <tr>
                                <td height="22">${c.teacherName}</td>
                                <td height="22">${c.school}</td>
                                <td height="22">${c.name}</td>
                                <td height="22">${c.town}</td>
                                <td height="22"><a href="${pageContext.request.contextPath}/WoAdmin?action=UserRegistrationClassSelection&classId=${c.classid}&studId=${studId}&startPage=${startPage}">
                                    <img src="images/arrow.gif" width="18" height="16" border="0">
                                </a></td>
                                </tr>
                            </c:forEach>
                        </table>


                    </div>
                </div>
            </div>
            <div class="p7ccm04-content-row p7ccm-row">
                <div class="p7ccm04-3col-sidebar-left-right-column1 p7ccm-col">
                    <div class="p7ccm04-3col-sidebar-left-right-column1-cnt p7ccm04-content p7ehc-1">
                        <div class="footCR">The <a href="http://wayangoutpost.com/">MathSpring</a> Mathematics Tutor is
                            under development by the <a href="http://centerforknowledgecommunication.com/">Center for
                                Knowledge Communication</a> at the <a href="http://www.umass.edu/">University of
                                Massachusetts Amherst</a></div>
                    </div>
                </div>
                <div class="p7ccm04-3col-sidebar-left-right-column2 p7ccm-col">
                    <div class="p7ccm04-3col-sidebar-left-right-column2-cnt p7ccm04-content p7ehc-1">
                        <p>
                            <%--<button onclick="popupCheck()">Check Pop-ups</button>--%>
                        </p>
                    </div>
                </div>
                <div class="p7ccm04-3col-sidebar-left-right-column3 p7ccm-col">
                    <div class="p7ccm04-3col-sidebar-left-right-column3-cnt p7ccm04-content p7ehc-1">
                        <p class=fltrt>
                            <%--<button onclick='checkSound()'>Check Sound</button>--%>
                        </p>
                    </div>
                </div>
            </div>
        </div>
        <!-- end .container --></div>
</body>
</html>

