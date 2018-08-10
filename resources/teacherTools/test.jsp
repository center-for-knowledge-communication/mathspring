<%--
  Created by IntelliJ IDEA.
  User: marshall
  Date: Jan 29, 2008
  Time: 5:22:15 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="teacherToolsHeader.jsp" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
 <div id="Layer1" style="position:absolute; width:365px; height:375px; z-index:1; left: 350px; top: 5px;">
    <c:forEach var="pool" items="${pools}">
      The pool is <c:out value="${pool.description}"/>  &nbsp;
       <c:out value="${pool.id}"/>   <br>
    </c:forEach>
 <br>Class info is:
<jsp:useBean id="classInfo" scope="request" type="edu.umass.ckc.wo.beans.ClassInfo"/>
<jsp:useBean id="ccc" scope="request" type="edu.umass.ckc.wo.beans.ClassInfo"/>
<jsp:useBean id="aaa" scope="request" type="edu.umass.ckc.wo.beans.PretestPool"/>
 <c:out value="${classInfo.name}"/> <br>
     The pool is  <c:out value="${aaa.description}"/>

</div>
</body>
</html>