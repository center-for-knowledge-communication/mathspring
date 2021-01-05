<%@ include file="partials/teacherRegister-header.jsp" %>

<%@ include file="partials/teacherRegister-nav.jsp" %>

<div class="bootstrap main-content">
    <div class="registration-form vertical-center">
        <div class="col-sm-6 col-sm-offset-3 registration-box">
            <c:if test="${message != null && not empty message}">
                <div class="alert alert-danger msg-bar" role="alert">${message}</div>
            </c:if>
            <h3 class="text-center form-label form-title"><%= rb.getString("signup_teacher")%></h3>
            <hr>
            <form
                    class="form-horizontal"
                    method="post"
                    action="${pageContext.request.contextPath}/WoAdmin?action=AdminTeacherRegistration"
            >
                <div class="form-group">
                    <label class="control-label col-sm-4" for="first_name"><%= rb.getString("first_name")%>:</label>
                    <div class="col-sm-6">
                        <input type="text" name="fname" class="form-control" id="first_name" placeholder="">
                    </div>
                </div><!-- form-group -->
                <div class="form-group">
                    <label class="control-label col-sm-4" for="last_name"><%= rb.getString("last_name")%>:</label>
                    <div class="col-sm-6">
                        <input type="text" name="lname" class="form-control" id="last_name" placeholder="">
                    </div>
                </div><!-- form-group -->
                <div class="form-group">
                    <label class="control-label col-sm-4" for="email"><%= rb.getString("email")%>:</label>
                    <div class="col-sm-6">
                        <input type="email" name="email" class="form-control" id="email" placeholder="">
                    </div>
                </div><!-- form-group -->
                <div class="form-group">
                    <label class="control-label col-sm-4" for="username"><%= rb.getString("username")%>:</label>
                    <div class="col-sm-6">
                        <input type="text" name="userName" class="form-control" id="username"   onblur="verifyUsername()" placeholder="">
                    </div>
                </div><!-- form-group -->
                <div class="form-group">
                    <label class="control-label col-sm-4" for="password"><%= rb.getString("password")%>:</label>
                    <div class="col-sm-6">
                        <input type="password" name="pw1" class="form-control" id="password" onblur="removeSingleQuote()" placeholder="">
                    </div>
                </div><!-- form-group -->
                <div class="form-group">
                    <label class="control-label col-sm-4" for="password"><%= rb.getString("re_enter_password")%>:</label>
                    <div class="col-sm-6">
                        <input type="password" name="pw2" class="form-control" id="password-confirmation" onblur="removeSingleQuote()" placeholder="">
                    </div>
                </div><!-- form-group -->

                <div class="form-group row">
                    <div class="col-sm-offset-4 col-sm-4">
                        <button type="submit" class="btn btn-default pull-right btn-block teacher-button"><%= rb.getString("submit")%></button>
                    </div>
                </div><!-- form-group -->
            </form>
        </div>
    </div>
</div>
    <footer class="footer">
        &copy; <%= rb.getString("copyright")%>
    </footer>
</body>
</html>

<script type="text/javascript">
    function verifyUsername() {

    	var username_input = document.getElementById('username').value;
    	var message = "";
    	if (username_input.search(/[^0-9A-Za-z\s]/) != -1) {
			alert("<%= rb.getString("invalid_username_character")%>");
			document.getElementById('username').value = "";
			document.getElementById('username').focus();
    	}
    } 
    
    function removeSingleQuote() {

    	var password_input = document.getElementById('password').value;
    	if (password_input.search("'") != -1) {
    		alert(" ' character not allowed. Please try again")
	    	document.getElementById('password').value = "";
    	}
    }    
</script>

