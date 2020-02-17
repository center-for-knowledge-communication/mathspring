<%@ include file="partials/loginK12-header.jsp" %>
<%@ include file="partials/loginK12-nav.jsp" %>
<%@page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>

<% 
/**
 * Frank 01-20-2020 Issue #39 use classId as alternative password
 * Frank 02-17-2020 ttfixes issue #45
*/
Locale loc = request.getLocale();
String lang = loc.getDisplayLanguage();

ResourceBundle rb = null;
try {
	rb = ResourceBundle.getBundle("MathSpring",loc);
}
catch (Exception e) {
//	logger.error(e.getMessage());
}

// styles contain hard-coded content - conditionally include style based on langauge
if (lang == "Spanish" ) {
%>
<style>
.onoffswitch-inner:before {
  content: "si"; 
  padding-left: 10px;
  color: #000;
}
.onoffswitch-inner:after {
  content:"no"; 
  background-color: white;
  color: #000;
  text-align: right;
  position: relative;
  left: 22px;
}
</style>
<%
}
else {
%>
<style>
.onoffswitch-inner:before {
  content: "yes"; 
  padding-left: 10px;
  color: #000;
}
.onoffswitch-inner:after {
  content:"no"; 
  background-color: white;
  color: #000;
  text-align: right;
  position: relative;
  left: 22px;
}
</style>
<% 
}
%>
<div class="fullscreen">
    <div class="bootstrap vertical-center">
        <div class="container">
            <c:if test="${message != null && not empty message}">
                <div class="row col-md-8 col-md-offset-2 alert alert-danger msg-bar" role="alert">${message}</div>
            </c:if>
            <div class="row">
                <div class="col-md-8 col-md-offset-2 main-box">
                    <div class="row">
                        <div class="col-sm-6">
                            <p><%= rb.getString("have_username_already") %></p>
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

                                <div class="form-group switch-group">
                                    <div class="switch-group-1">
                                        <div class="switch-label"><%= rb.getString("are_you_teacher") %></div>
                                        <div class="onoffswitch">
                                            <input
                                                    type="checkbox"
                                                    name="usertype"
                                                    class="onoffswitch-checkbox"
                                                    id="usertypeswitcher">
                                            <label class="onoffswitch-label" for="usertypeswitcher">
                                                <span class="onoffswitch-inner"></span>
                                                <span class="onoffswitch-switch"></span>
                                            </label>
                                        </div>
                                    </div>
                                    <button
                                            type="submit"
                                            class="btn btn-success sign-in-btn js-login-btn"><%= rb.getString("login") %>
                                    </button>
                                </div>
                            </form>
                        </div>

                        <div class="col-sm-6">
                            <p><%= rb.getString("do_you_need_username") %></p>
                            <form
                                    method="post"
                                    action="${pageContext.request.contextPath}/WoAdmin?action=AdminTeacherLogin&var=b">
                                <button
                                        class="btn btn-primary btn-lg btn-block signup-btn--teacher"
                                        type="submit"
                                        name="register" value="Register"
                                ><%= rb.getString("signup_teacher") %>
                                </button>
                            </form>

                            <form action="${pageContext.request.contextPath}/WoAdmin">
                                <button
                                        class="btn btn-primary btn-lg btn-block signup-btn--student"
                                        type="button"
                                        onClick="javascript:signup();"
                                ><%= rb.getString("signup_student") %>
                                </button>
                            </form>

                            <form name="guest" action="${pageContext.request.contextPath}/WoLoginServlet">
                                <input type="hidden" name="action" value="GuestLogin"/>
                                <input type="hidden" name="clientType" value="${clientType}"/>
                                <input type="hidden" name="var" value="b"/>
                                <button
                                        class="btn btn-primary btn-lg btn-block signup-btn--guest"
                                        type="submit"><%= rb.getString("signup_guest") %>
                                </button>
                            </form>
                        </div>
                    </div>
                    <hr>
                    <div class="row information-box">
                        <p class="text-center"><%= rb.getString("use_audio_device") %></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<footer>
    &copy; <%= rb.getString("copyright") %>
</footer>

</body>
</html>
<%@ include file="partials/loginK12-footer.jsp" %>
