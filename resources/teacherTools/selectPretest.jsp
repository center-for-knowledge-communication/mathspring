<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- <jsp:include page="teacherToolsHeader.jsp" />  --%>
<jsp:useBean id="classInfo" scope="request" type="edu.umass.ckc.wo.beans.ClassInfo"/>
<jsp:useBean id="bean" scope="request" type="edu.umass.ckc.wo.beans.Classes"/>

<jsp:include page="${sideMenu}" />


<script type="text/javascript">
    function isFormValid (thisform) {
	// place any other field validations that you require here
	// validate myradiobuttons
	myOption = -1;
	for (i=thisform.poolId.length-1; i > -1; i--) {
		if (thisform.poolId[i].checked) {
			myOption = i; i = -1;
		}
	}
	if (myOption == -1) {
		alert("You must select a radio button");
		return false;
	}

	// place any other field validations that you require here
	//thisform.submit(); // this line submits the form after validation
	return true;
}

</script>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="mainPageMargin">
  <div id="Layer1" align = "center"><%-- width:400px; height:375px; z-index:1; left: 350px; top: 5px"--%>
      <p><b>Post Survey</b></p>
      <p style="color: #000000"><font face="Arial, Helvetica, sans-serif" size=".5">Turn on/off a post survey.
          </font></p>
      <p style="color: #000000"><font face="Arial, Helvetica, sans-serif" size=".5">If the post survey is on, the
          next time students in this class login, a survey will be shown.  It will only be shown to them once.
          This will only happen if the class uses a pedagogy that has a post-survey login intervention (defined in logins.xml) </font>

    <%--<form name="form1" method="post" onsubmit="return isFormValid(form1);"--%>
    <form name="form1" method="post"
          action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminAlterClassSubmitPrePost" >

      <input type="radio" name="postSurvey" value="off" <c:if test="${!classInfo.showPostSurvey}">checked</c:if>>Off</input>
      <input type="radio" name="postSurvey" value="on" <c:if test="${classInfo.showPostSurvey}">checked</c:if>>On</input>
      <br>
      <br>
      ${message}
      <br>
      <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="submit" name="Submit" value="Submit">
        <input type="hidden" name="classId" value="<c:out value="${classId}"/>">
        <input type="hidden" name="teacherId" value="<c:out value="${teacherId}"/>">
    </form>




      </div>
</div>

<jsp:include page="wayangTempTail.jsp" />
