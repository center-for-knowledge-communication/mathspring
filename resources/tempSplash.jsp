<%@ page import="java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<body>
Please select a topic to begin with:<br>
<%--@elvariable id="topics" type="java.util.List"--%>
<%--@elvariable id="topic" type="edu.umass.ckc.wo.beans.Topic"--%>

<c:forEach var="topic" items="${topics}">
    <a href="${pageContext.request.contextPath}/TutorBrain?action=MPPContinueTopic&sessionId=${sessionId}&topicId=${topic.id}&studentAction=continue&comment=">
        ${topic.name}</a> <br>

</c:forEach>
<br>
<a href="${pageContext.request.contextPath}/TutorBrain?action=EnterTutor&sessionId=${sessionId}&elapsedTime=0">
    Select a topic from my class lesson</a>

</body>
</html>