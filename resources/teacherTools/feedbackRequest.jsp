<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>

<% 
ResourceBundle versions = null; 
try {
	 versions = ResourceBundle.getBundle("Versions");
}
catch (Exception e) {
	 System.out.println("versions bundle ERROR");	 
//	logger.error(e.getMessage());	
}

Locale loc = request.getLocale();
String lang = loc.getDisplayLanguage();

ResourceBundle rb = null;
try {
	rb = ResourceBundle.getBundle("MathSpring",loc);
}
catch (Exception e) {
	//logger.error(e.getMessage());
}


/**

 */

%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>MathSpring | <%= rb.getString("teacher_tools_feedback_form")%></title>
    <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png">
    <link rel="icon" type="image/png" href="/favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="/favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="/manifest.json">
    <meta name="theme-color" content="#ffffff">
    <link href="../sass_compiled/teacher_register.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet">
      <script type="text/javascript" src="../js/jquery-1.10.2.js"></script>
    <script type="text/javascript">

    </script>
</head>

<body>
<div class="nav">
    <div class="nav__logo">
        <img src="../img/mstile-150x150.png" alt="" class="nav__logo-image">
        <span class="nav__logo-text">
                <span class="nav__logo-text--green-letter">M</span>ATH<span class="nav__logo-text--green-letter">S</span>PRING
            </span>
    </div>
</div>
<div class="bootstrap main-content">
	    <div class="feedback-form vertical-center">
        <div class="col-sm-6 col-sm-offset-3 registration-box">
            <c:if test="${message != null && not empty message}">
                <div class="alert alert-danger msg-bar" role="alert">${message}</div>
            </c:if>
            <h3 class="text-center form-label form-title"><%= rb.getString("teacher_feedback_title1")%></h3>
            <hr>
            <form
                    class="form-horizontal"
                    method="post"
                    action="${pageContext.request.contextPath}/tt/tt/ttLogFeedback"
            >		
                <input type="hidden" name="messageType" value="teacherToolFeedback"/>
                <input type="hidden" name="teacherId" value="${teacherId}"/>
                <input type="hidden" name="objectId" value="0"/>
                <div class="form-group">
                    <label id="msgLabel" class="control-label col-sm-4" for="msg"><%= rb.getString("type_your_message_here")%>:</label>
                    <div class="col-sm-6">
                    	<textarea id="msg" name="msg" class="form-control" rows="6" cols="60" defaultValue=">"></textarea>
                    </div>
                </div><!-- form-group -->

                   <div class="form-group">
                       <label class="control-label col-sm-4" for="priority"><%= rb.getString("priority")%>:</label>
                       <div class="col-sm-6">
                           <select class="form-control" id="priority" name="priority">
                               <option value="serious"><%= rb.getString("serious_issue")%></option>
                               <option value="minor"><%= rb.getString("minor_suggestion")%></option>
                               <option value="other"><%= rb.getString("other")%></option>
                           </select>
                       </div>
                   </div>
                <div class="form-group row">
                    <div class="col-sm-offset-4 col-sm-2">
                        <button class="btn btn-success pull-right btn-block"><%= rb.getString("submit")%></button>
                    </div>
                    <div class="col-sm-2">
                        <button class="btn btn-danger pull-right btn-block" onclick="self.close()"><%= rb.getString("close")%></button>
                    </div>
                </div><!-- form-group -->
            </form>
            <hr>
            <h4 class="text-center form-label form-title"><%= rb.getString("teacher_feedback_title2")%></h4>
        </div>
   </div>   	
</div>
 
    <footer class="footer">
        &copy; <%= rb.getString("copyright")%>
    </footer>
</body>
</html>

