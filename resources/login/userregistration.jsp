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


        // When the user clicks the "sign up" button this sends the username to the server to see if its in use.
        //
        function validateFields() {
            $.ajax({url: "${pageContext.request.contextPath}/WoAdmin" + "?action=UserRegistrationValidateUsername" + "&userName=" + $('#userName').val(),
                success: validateResult,
                async: true});


        }

        // responseText will be empty if validateFields results in a legal inputs.   If not, an error message
        // comes back and we show it in an alert.
        function validateResult(responseText, textStatus, XMLHttpRequest) {
            if ($('#password').val() === '') {
                alert("Please provide a password");
            }
            else if (responseText === "") {
                var userType = $( "input:radio[name=bar]:checked" ).val();
                var fname= $('#fName').val();
                var lname= $('#lName').val();
                var uname= $('#userName').val();
                var email= $('#email').val();
                var age= $('#age').val();
                var gender= $('#gender').val();
                var password= $('#password').val();
                var userType = $('input:radio[name=userType]:checked' ).val();
                location.href = "${pageContext.request.contextPath}/WoAdmin?action=UserRegistrationAuthenticationInfo&fname=" +fname+ "&lname=" +lname+
                        "&uname=" +uname+ "&email=" +email+ "&password=" + password +
                        "&age=" +age + "&gender=" + gender +
                        "&userType=" +userType + "&startPage=${startPage}";

            }
            else {
                alert(responseText);
            }
        }



    </script>
</head>

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
                        <p>Lets create a Mathspring user for you!</p>

                        <p>&nbsp;</p>

                        <div class="nest">
                            <p></p>

                            <form id="form1" method="post" name="login" action="${pageContext.request.contextPath}/WoLoginServlet">
                                <input type="hidden" name="action" value="LoginK12_2"/>
                                <input type="hidden" name="skin" value="k12"/>

                                <p> First Name:
                                    <input id="fName" type="text" name="fname" value="${fName}"/>
                                    <br/>
                                    Last Name:
                                    <input id="lName" type="text" name="lname" value="${lName}"/>
                                    <br/>
                                    Email:
                                    <input id="email" type="text" name="email" value="${email}"/>
                                    <br/>
                                    Age:
                                    <input id="age" type="text" name="age" value="${age}"/>
                                    <br/>
                                    Gender:
                                    <select id="gender" name="gender" form="form1">
                                        <option value="male">male</option>
                                        <option value="female">female</option>
                                    </select>
                                    <br/>
                                    *User Name:
                                    <input id="userName" type="text" name="uname" value="${userName}"/>
                                    <br/>
                                    *Password:
                                    <input id="password" type="password" name="password" value="${password}"/>
                                    <br/>
                                    <br/>
                                    <input id="regular" type="radio" name="userType" value="student" checked>Regular
                                    Student</input> <br>
                                    <input id="testStudent" type="radio" name="userType" value="testStudent">System
                                    testing (student view)</input><br>
                                    <input id="testDeveloper" type="radio" name="userType" value="testDeveloper">System
                                    testing (developer view)</input><br>
                                    <br><br>
                                    *Required fields

                                </p>

                                <p>&nbsp;</p>

                                <div class="nest2">
                                    <%--<p>--%>
                                    <%--<b>${message} </b> <c:if test="${message != null}"><br><br></c:if>--%>
                                    <%--<input value="Log In" type="submit"/>--%>

                                    <%--</p>--%>

                                    <!-- </form> -->
                                    <form id="signupForm" action="${pageContext.request.contextPath}/WoAdmin">

                                        <p>
                                            <button type="button" onClick="javascript:validateFields();">Sign up
                                            </button>
                                        </p>
                                    </form>


                                    <p>&nbsp;</p>
                                </div>

                            </form>
                        </div>
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

