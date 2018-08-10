<%--
  Created by IntelliJ IDEA.
  User: marshall
  Date: Jan 30, 2008
  Time: 1:05:03 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="${sideMenu}" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="classInfo" scope="request" type="edu.umass.ckc.wo.beans.ClassInfo"/>
<jsp:useBean id="pool" scope="request" type="edu.umass.ckc.wo.beans.PretestPool"/>

<div id="Layer1" >
  <p align="left" class="a2"><font color="#FFFFFF"><b><font face="Arial, Helvetica, sans-serif">Class information</font></b></font></p>

    <input type="hidden" name="classId" value="<c:out value="${classInfo.classid}"/>"/>
    <table width="334" border="0" height="98">
       <tr>
        <td width="102"><font color="#FFFFFF" face="Arial, Helvetica, sans-serif">ID:</font></td>
        <td width="222" class="whArial"><c:out value="${classInfo.classid}"/></td>
      </tr>
      <tr>
        <td width="102"><font color="#FFFFFF" face="Arial, Helvetica, sans-serif">Class Name:</font></td>
        <td width="222" class="whArial"><c:out value="${classInfo.name}"/></td>
      </tr>
      <tr>
        <td width="102"><font color="#FFFFFF" face="Arial, Helvetica, sans-serif">Town:</font></td>
        <td width="222" class="whArial"><c:out value="${classInfo.town}"/></td>
      </tr>
      <tr>
        <td width="102"><font color="#FFFFFF" face="Arial, Helvetica, sans-serif">School:</font></td>
        <td width="222" class="whArial">  <c:out value="${classInfo.school}"/></td>
      </tr>

      <tr>
        <td width="102"><font color="#FFFFFF" face="Arial, Helvetica, sans-serif">Year:</font></td>
        <td width="222" class="whArial"> <c:out value="${classInfo.schoolYear}"/></td>
      </tr>
      <tr>
        <td width="102"><font color="#FFFFFF" face="Arial, Helvetica, sans-serif">Section:</font></td>
        <td width="222" class="whArial"><c:out value="${classInfo.section}"/></td>
      </tr>


     <tr>
        <td width="102"><font color="#FFFFFF" face="Arial, Helvetica, sans-serif">Pretest Pool:</font></td>
        <td width="222" class="whArial"><c:out value="${pool.description}"/></td>
      </tr>

      <tr>
        <td width="102"><font color="#FFFFFF" face="Arial, Helvetica, sans-serif">Email Status Reports Interval:</font></td>
        <td width="222" class="whArial"><c:out value="${classInfo.emailInterval}"/></td>
      </tr>

    </table>
  </div>

  <div id="Layer2" style="position:absolute; width:900px; height:450px; z-index:1; left: 10px; top: 345px">
  <p class="whArial">Pedagogies in use: </p>
  <table width="90%"  border="0">
          <tr>
              <td width="17%" class="whArial">ID</td>
              <td width="83%" class="whArial">Name</td>
          </tr>
          <%--@elvariable id="pedagogies" type="java.util.List"--%>
          <%--@elvariable id="ped" type="edu.umass.ckc.wo.tutor.Pedagogy"--%>
          <c:forEach var="ped" items="${pedagogies}">
           <tr>
              <td class="whArial">
              <c:out value="${ped.id}"/></td>
              <td class="whArial"><c:out value="${ped.name}"/> </td>
          </tr>
          </c:forEach>
      </table>
      <p class="whArial">
      Control Content:
    <table> <tr>

  <td>
  <form name="form4" id="form4" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminEditTopics">
        <input type="submit" name="Submit" value="Topic Control" />
      <input type="hidden" name="teacherId"  value="<c:out value="${classInfo.teachid}"/>"/>
        <input type="hidden" name="classId" value="<c:out value="${classInfo.classid}"/>"/>
    </form>
  </td>
    <td  valign="center">
    <form name="form5" id="form5" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminProblemSelection">
        <input type="submit" name="Submit" value="Problem Selection" />
        <input type="hidden" name="teacherId"  value="<c:out value="${classInfo.teachid}"/>"/>
        <input type="hidden" name="classId" value="<c:out value="${classInfo.classid}"/>"/>
    </form>
  </td>
  <td>

    <form name="form6" id="form6" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminAlterClassPretest">
        <input type="submit" name="Submit" value="Pretest Selection" />
        <input type="hidden" name="teacherId"  value="<c:out value="${classInfo.teachid}"/>"/>
        <input type="hidden" name="classId" value="<c:out value="${classInfo.classid}"/>"/>
    </form>
  </td>



    </tr>
    </table>

     <p class="whArial">
      Tutor operations:
      <table> <tr>
      <td valign="center">

  <form name="form3" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminAlterClassPedagogies">
        <input type="submit" name="save" value="Change Pedagogies" />
      <input type="hidden" name="teacherId"  value="<c:out value="${classInfo.teachid}"/>"/>
        <input type="hidden" name="classId" value="<c:out value="${classInfo.classid}"/>"/>
    </form> </td>
    <td>
     <form name="form7" id="form7" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminAlterClassActivateHuts">
        <input type="submit" name="Submit" value="Activate Huts" />
         <input type="hidden" name="teacherId"  value="<c:out value="${classInfo.teachid}"/>"/>
        <input type="hidden" name="classId" value="<c:out value="${classInfo.classid}"/>"/>
    </form>
    </td>
    </tr>
    </table>

  <p class="whArial">
      Class Operations:
   <table> <tr>
        <td>
       <form name="form8" id="form8" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminAlterClassCloneClass">
        <input type="submit" name="Submit" value="Clone Class" />
           <input type="hidden" name="teacherId"  value="<c:out value="${classInfo.teachid}"/>"/>
        <input type="hidden" name="classId" value="<c:out value="${classInfo.classid}"/>"/>
    </form>
        </td>
   <td>
      <form name="form10" id="form10" method="post"  action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminEditClassList">
        <input type="submit" name="Submit" value="Student List" />
        <input type="hidden" name="teacherId"  value="<c:out value="${classInfo.teachid}"/>"/>
        <input type="hidden" name="classId"  value="<c:out value="${classInfo.classid}"/>"/>
    </form>
        </td>
         <td>
       <form name="form9" id="form9" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminOtherClassConfig">
        <input type="submit" name="Submit" value="Other Class Settings" />
           <input type="hidden" name="teacherId"  value="<c:out value="${classInfo.teachid}"/>"/>
        <input type="hidden" name="classId" value="<c:out value="${classInfo.classid}"/>"/>
    </form>
        </td>
    </tr>
    </table>
  <p><br><br>
  <form name="form2" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminChooseActivity">
        <input type="submit" name="newActivity" value="Select New Activity" />
       <input type="hidden" name="teacherId"  value="<c:out value="${classInfo.teachid}"/>"/>
        <input type="hidden" name="classId" value="<c:out value="${classInfo.classid}"/>"/>
    </form>

  </div>


</body>
</html>