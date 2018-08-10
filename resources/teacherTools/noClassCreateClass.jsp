<%--
  Created by IntelliJ IDEA.
  User: marshall
  Date: Jan 29, 2008
  Time: 5:22:15 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="${sideMenu}" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="mainPageMargin">
    <div align="left">
        <form name="form1" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminMainPage&teacherId=<c:out value="${teacherId}"/>">
            <input type="submit" name="Submit" value="Exit Create Class Mode" />
        </form>

    </div>
    <p>&nbsp</p>

<div id="Layer1" align="center" style="width:400px; height:375px; z-index:1;">
    <p align="center" class="a2"><font color="#000000"><b><font face="Arial, Helvetica, sans-serif">Create
        a class</font></b></font></p>
    <form name="form1" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminNoClassSubmitClassForm">

        <table width="334" border="0" height="98">
            <tr>
                <td width="102"><font color="#000000" face="Arial, Helvetica, sans-serif">* Class
                    Name:</font></td>
                <td width="222">
                    <input type="text" name="className" size="30" value="">
                </td>
            </tr>
            <tr>
                <td width="102"><font color="#000000" face="Arial, Helvetica, sans-serif">* Grade:</font></td>

                <td width="222">
                    <select name="grade">
                        <option value="5">5</option>
                        <option value="6">6</option>
                        <option value="7">7</option>
                        <option value="8">8</option>
                        <option value="9">9</option>
                        <option value="10">10</option>
                        <option value="adult">adult</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td width="102"><font color="#000000" face="Arial, Helvetica, sans-serif">* Town:</font></td>

                <td width="222">
                    <input type="text" name="town" size="30" value="">
                </td>
            </tr>
            <tr>
                <td width="102"><font color="#000000" face="Arial, Helvetica, sans-serif">* School:</font></td>
                <td width="222">
                    <input type="text" name="school" size="20" value="">
                </td>
            </tr>

            <tr>
                <td width="102"><font color="#000000" face="Arial, Helvetica, sans-serif">* Year:</font></td>
                <td width="222">
                    <input type="text" name="year" size="20" value="">
                </td>
            </tr>
            <tr>
                <td width="102"><font color="#000000" face="Arial, Helvetica, sans-serif">* Section:</font></td>
                <td width="222">
                    <input type="text" name="section" size="20" value="">

                </td>
            </tr>
        </table>
        <p/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="submit" name="Submit" value="Submit">
        <input type="hidden" name="teacherId" value="<c:out value="${teacherId}"/>">
    </form>
    <p/>
    <font color="#000000" face="Arial, Helvetica, sans-serif">* required field</font>
    <p/>

    <font color="#000000" face="Arial, Helvetica, sans-serif"><c:out value="${message}"/></font>
</div>
</div>


</body>
</html>