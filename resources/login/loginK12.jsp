<%@ include file="partials/loginK12-header.jsp" %>
<%@ include file="partials/loginK12-nav.jsp" %>

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
                            <p>Have a username already?</p>
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

                                <div class="form-group switch-group">
                                    <div class="switch-group-1">
                                        <div class="switch-label">Are you a teacher?</div>
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
                                            class="btn btn-success sign-in-btn js-login-btn">Login
                                    </button>
                                </div>
                            </form>
                        </div>

                        <div class="col-sm-6">
                            <p>Do you need a username?</p>
                            <form
                                    method="post"
                                    action="${pageContext.request.contextPath}/WoAdmin?action=AdminTeacherLogin&var=b">
                                <button
                                        class="btn btn-primary btn-lg btn-block signup-btn--teacher"
                                        type="submit"
                                        name="register" value="Register"
                                >Sign up for Teacher
                                </button>
                            </form>

                            <form action="${pageContext.request.contextPath}/WoAdmin">
                                <button
                                        class="btn btn-primary btn-lg btn-block signup-btn--student"
                                        type="button"
                                        onClick="javascript:signup();"
                                >Sign up for Student
                                </button>
                            </form>

                            <form name="guest" action="${pageContext.request.contextPath}/WoLoginServlet">
                                <input type="hidden" name="action" value="GuestLogin"/>
                                <input type="hidden" name="clientType" value="${clientType}"/>
                                <input type="hidden" name="var" value="b"/>
                                <button
                                        class="btn btn-primary btn-lg btn-block signup-btn--guest"
                                        type="submit">Try out as Guest
                                </button>
                            </form>
                        </div>
                    </div>
                    <hr>
                    <div class="row information-box">
                        <p class="text-center">To have the best experience, enable pop-ups and turn on either speakers
                            or a headset!</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="partials/loginK12-footer.jsp" %>
