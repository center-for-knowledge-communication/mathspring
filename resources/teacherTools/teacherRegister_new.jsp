<%@ include file="partials/teacherRegister-header.jsp" %>

<%@ include file="partials/teacherRegister-nav.jsp" %>

<div class="bootstrap main-content">
    <div class="registration-form vertical-center">
        <div class="col-sm-6 col-sm-offset-3 registration-box">
            <c:if test="${message != null && not empty message}">
                <div class="alert alert-danger msg-bar" role="alert">${message}</div>
            </c:if>
            <h3 class="text-center form-label form-title">Sign up for teachers</h3>
            <hr>
            <form
                    class="form-horizontal"
                    method="post"
                    action="${pageContext.request.contextPath}/WoAdmin?action=AdminTeacherRegistration"
            >
                <div class="form-group">
                    <label class="control-label col-sm-4" for="first_name">First Name:</label>
                    <div class="col-sm-6">
                        <input type="text" name="fname" class="form-control" id="first_name" placeholder="Enter your first name">
                    </div>
                </div><!-- form-group -->
                <div class="form-group">
                    <label class="control-label col-sm-4" for="last_name">Last Name:</label>
                    <div class="col-sm-6">
                        <input type="text" name="lname" class="form-control" id="last_name" placeholder="Enter your last name">
                    </div>
                </div><!-- form-group -->
                <div class="form-group">
                    <label class="control-label col-sm-4" for="email">Email:</label>
                    <div class="col-sm-6">
                        <input type="email" name="email" class="form-control" id="email" placeholder="Enter email">
                    </div>
                </div><!-- form-group -->
                <div class="form-group">
                    <label class="control-label col-sm-4" for="username">Username:</label>
                    <div class="col-sm-6">
                        <input type="text" name="userName" class="form-control" id="username" placeholder="Enter username">
                    </div>
                </div><!-- form-group -->
                <div class="form-group">
                    <label class="control-label col-sm-4" for="password">Password:</label>
                    <div class="col-sm-6">
                        <input type="password" name="pw1" class="form-control" id="password" placeholder="Enter password">
                    </div>
                </div><!-- form-group -->
                <div class="form-group">
                    <label class="control-label col-sm-4" for="password">Retype password:</label>
                    <div class="col-sm-6">
                        <input type="password" name="pw2" class="form-control" id="password-confirmation" placeholder="Retype password">
                    </div>
                </div><!-- form-group -->

                <div class="form-group row">
                    <div class="col-sm-offset-4 col-sm-4">
                        <button type="submit" class="btn btn-default pull-right btn-block teacher-button">Submit</button>
                    </div>
                </div><!-- form-group -->
            </form>
        </div>
    </div>
</div>

<%@ include file="partials/teacherRegister-footer.jsp" %>
