<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>
<%

/**
* Frank	06-02-2020	Issue #122 Allow student to enter class code on sign-up page
* Frank	07-17-20	Issue #122 modified for distance learning option
* Frank	09-10-20	Issue #221 Gender and email changes
* Frank 12-18-20 Issue #336 added cache-busting for selected .js and .css files
* Frank 10-29-21 Issue #526 validate user input fields
*/
ResourceBundle versions = null; 
try {
	 versions = ResourceBundle.getBundle("Versions");
}
catch (Exception e) {
	 System.out.println("teacherToolsMain ERROR");	 
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
    <title>MathSpring | <%= rb.getString("title_student_registration") %></title>
    <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png">
    <link rel="icon" type="image/png" href="/favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="/favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="/manifest.json">
    <link href="sass_compiled/teacher_register.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet">
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

    <div class="bootstrap main-content">
        <div class="registration-form vertical-center">
            <div class="col-sm-6 col-sm-offset-3 registration-box">

                <div class="alert alert-danger msg-bar hidden" role="alert"></div>

                <h3 class="text-center form-label form-title"><%= rb.getString("signup_for_students") %></h3>
                <hr>

                <form class="form-horizontal student-registration-form" method="post">
                    <input type="hidden" name="action" value="LoginK12_2"/>
                    <input type="hidden" name="skin" value="k12"/>
                    <input type="hidden" name="var" value="b"/>
                    
                    <input type="hidden" name="email" value="no"/>
                    
                    <div class="form-group">
                        <label class="control-label col-sm-4" for="first_name"><%= rb.getString("first_name") %>:</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="first_name" placeholder="" name="fname">
                        </div>
                    </div><!-- form-group -->
                    <div class="form-group">
                        <label class="control-label col-sm-4" for="last_name"><%= rb.getString("last_name") %>:</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="last_name" placeholder="" name="lname">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-4" for="age"><%= rb.getString("age") %>:</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="age" placeholder="" name="age">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-sm-4" for="gender"><%= rb.getString("gender") %>:</label>
                        <div class="col-sm-6">
                            <select class="form-control" id="gender" name="gender">
                                <option value="male"><%= rb.getString("male") %></option>
                                <option value="female"><%= rb.getString("female") %></option>
                                <option value="unknown"><%= rb.getString("gender_other") %></option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-sm-4" for="username"><%= rb.getString("username") %>:</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="username" placeholder="" name="uname">
                        </div>
                    </div><!-- form-group -->
                    <div class="form-group">
                        <label class="control-label col-sm-4" for="password"><%= rb.getString("password") %>:</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="password" placeholder="" name="password">
                        </div>
                    </div><!-- form-group -->
                    <div id="classcodeRow" class="form-group">
                        <label class="control-label col-sm-4" for="classId"><%= rb.getString("class_code_if_known") %>:</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="classId" placeholder="" name="classId">
                        </div>
                    </div>
                    <div id="studentTypeRow" class="form-group row">
                        <div class="col-md-offset-4 col-md-8">
                            <div class="form-check">
                             <input class="form-check-input" type="radio" name="userType" id="exampleRadios1" value="student" checked>
                                <label class="form-check-label">
                                    &nbsp;<%= rb.getString("student_regular") %>
                                </label>
                            </div>
                            <div class="form-check">
                             <input class="form-check-input" type="radio" name="userType" id="exampleRadios2" value="testStudent">
                                <label class="form-check-label">
                                    &nbsp;<%= rb.getString("student_test_student_view") %>
                                </label>
                            </div>
                            <div class="form-check">
                             <input class="form-check-input" type="radio" name="userType" id="exampleRadios3" value="testDeveloper">
                                <label class="form-check-label">
                                    &nbsp;<%= rb.getString("student_test_developer_view") %>
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-offset-4 col-sm-4">
                            <button type="submit" class="btn btn-default btn-block student-button"><%= rb.getString("submit") %></button>
                        </div>
                    </div><!-- form-group -->
                </form>
            </div>
        </div>
    </div>
    <footer>
        &copy; <%= rb.getString("copyright") %>
    </footer>
    <script type="text/javascript" src="js/jquery-1.10.2.js"></script>
    <script src="js/bootstrap/js/language_es.js"></script>
    <script type="text/javascript">


    $(document).ready(function() {
		var tClassId = '${classId}';
		if (tClassId.length > 1) {
			document.getElementById("classId").value = tClassId;
			document.getElementById("password").value = tClassId;
			document.getElementById("password").readOnly = true;
			$("#studentTypeRow").hide();
			$("#emailRow").hide();
			$("#classcodeRow").hide();
		}
    });
    
    $(document).ready(function() {

            var usernameInput = $('.username-wrapper');
            var passwordInput = $('.password-wrapper');
            var messageBar = $('.msg-bar');
            $('.student-registration-form').on('submit', function(event) {
                event.preventDefault();
                $.ajax('${pageContext.request.contextPath}/WoAdmin', {
                    type: 'POST',  // http method
                    data: { 
                        action: 'UserRegistrationValidateUsername',
                        userName : $('#username').val(),
                        classId: $('#classId').val(),
                        firstName: $('#first_name').val(),
                        lastName: $('#last_name').val(),
                        password: $('#password').val(),
                        age: $('#age').val()
                    },  // data to submit

//                            + "&classId=" + $('#classId').val()
//                            + "&fname=" + $('#first_name').val()
//                            + "&lname=" + $('#last_name').val()
//                            + "&password=" + $('#password').val()
//                            + "&age=" + $('#age').val(),
//                    success: function (data, status, xhr) {
//                        $('p').append('status: ' + status + ', data: ' + data);
//                    },
//                    error: function (jqXhr, textStatus, errorMessage) {
//                            $('p').append('Error' + errorMessage);
//                    }
//                });
//                $.ajax({
//                    url: "${pageContext.request.contextPath}/WoAdmin"
//                    + "?action=UserRegistrationValidateUsername"
//                    + "&userName=" + $('#username').val()
//                    + "&classId=" + $('#classId').val()
//                    + "&fname=" + $('#first_name').val()
//                    + "&lname=" + $('#last_name').val()
//                    + "&password=" + $('#password').val()
//                    + "&age=" + $('#age').val(),
                    success: function(responseText) {
                        if (responseText === "") {
                            var fname = $('#first_name').val();
                            var lname = $('#last_name').val();
                            var uname = $('#username').val();
                            var email = $('#email').val();
                            var age = $('#age').val();
                            var gender = $('#gender').val();
                            var password = $('#password').val();
                            var classId = $('#classId').val();
                            var userType = $('input:radio[name=userType]:checked').val();
                            location.href = "${pageContext.request.contextPath}/WoAdmin?"
                                    + "action=UserRegistrationAuthenticationInfo"
                                    + "&fname=" + fname
                                    + "&lname=" + lname
                                    + "&uname=" + uname
                                    + "&email=" + email
                                    + "&password=" + password
                                    + "&age=" + age
                                    + "&gender=" + gender
                                    + "&classId=" + classId
                                    + "&userType=" + userType
                                    + "&startPage=${startPage}"
                                    + "&var=b";
                        } else {
                            messageBar.removeClass('hidden');
                            messageBar.text(responseText);
                            usernameInput.addClass('has-error');
                        }
                    },
                    error: function() {
                        usernameInput.addClass('has-error');
                        messageBar.removeClass('hidden');
                    },
                    async: true});
            });
        });
    </script>
</body>
</html>
