<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--<jsp:include page="teacherToolsHeader.jsp" />      --%>
<jsp:include page="${sideMenu}" />
<jsp:useBean id="bean" scope="request" type="edu.umass.ckc.wo.beans.Classes"/>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="mainPageMargin">
  <style type="text/css">@import url("css/dragdroptable.css");</style>
  <div id="Layer1" align="center">
      <p class="a2"><b>Problem Topic Selection</b></p>
      <p>To select problems you must click on a topic below.</p>
      <p style="color:red">Note: You may not select problems for deactivated topics</p>

      <%--@elvariable id="classGradeColumn" type="int"--%>
      <%--@elvariable id="gradeColumnMask" type="boolean[]"--%>

      <table class="altrows">
          <tr class="rowheader">
              <td rowspan="2">ID</td>
              <td rowspan="2">Topic</td>
              <td rowspan="2" style="max-width:100px">Total Active Problems</td>
              <td colspan="10">Active Problems by Grade Level</td>
          </tr>
          <tr class="rowheader">
              <c:forEach var="visible" varStatus="status" items="${gradeColumnMask}">
                  <td style="${status.index eq classGradeColumn ? 'font-weight:bold;' : ''}${gradeColumnMask[status.index] ? '' : 'display:none'}">
                      <c:out value="${status.index eq 0 ? 'K' : (status.index eq 9 ? 'H' : status.index.toString().concat('th'))}"/>
                  </td>
              </c:forEach>
          </tr>

          <%--@elvariable id="topics" type="edu.umass.ckc.wo.tutor.Topic[]"--%>

          <c:forEach var="topic" items="${topics}">
              <c:if test="${topic.seqPos > 0 && topic.numProbs > 0}">
                  <tr>
                      <td><c:out value="${topic.id}"/></td>
                      <td class="a2">
                          <a  href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminSelectTopicProblems&topicId=<c:out value="${topic.id}"/>&classId=<c:out value="${classId}"/>&teacherId=<c:out value="${teacherId}"/>" >
                              <c:out value="${topic.name}"/>
                          </a>
                      </td>
                      <td>
                          <a href="${pageContext.request.contextPath}/WoAdmin?action=AdminSelectTopicProblems&teacherId=${teacherId}&classId=${classId}&topicId=${topic.id}">
                              <c:out value="${topic.numProbs}"/>
                          </a>
                      </td>
                      <c:forEach var="problemsByGrade" varStatus="status" items="${topic.problemsByGrade}">
                          <td style="${status.index eq classGradeColumn ? 'font-weight:bold' : ''}${gradeColumnMask[status.index] ? '' : 'display:none;'}">
                              <c:if test="${problemsByGrade > 0}"><c:out value="${problemsByGrade}"/></c:if>
                          </td>
                      </c:forEach>
                  </tr>
              </c:if>
          </c:forEach>
      </table>

      <div style="height:54px"></div>
      <form name="form2" id="form3" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminEditTopics" style="margin:auto">
            <input type="submit" name="Submit" value="Activate/Reorganize Topics" style="font-size:16px;padding:10px"/>
            <input type="hidden" name="classId" value="<c:out value="${classId}"/>">
            <input type="hidden" name="teacherId" value="<c:out value="${teacherId}"/>">
      </form>
      <div style="height:54px"></div>
  </div>
</div>

<jsp:include page="wayangTempTail.jsp" />