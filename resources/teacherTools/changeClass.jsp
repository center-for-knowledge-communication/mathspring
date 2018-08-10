<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div align="left">
    <table width="100%" border="0" height="72">
        <tr>
            <td>
                <a href="http://wayangoutpost.info/"><img src="images/lighbulb.png" width="100" height="100" alt="help"></a>
            </td>
            <c:if test="${teacherName != null}">
                <td>Teacher:</td>
                <td>${teacherName}</td>
            </c:if>
            <c:if test="${bean != null}">
                <td align="right">Select one to change class:</td>
                <td align="left">
                    <select name="classList" id="classSelection" onchange="changeClass(this,${teacherId});">;
                        <c:forEach var="c" items="${bean.classes}">
                            <%--<% for (var in bean.classes){  %>  --%>
                            <c:if test ="${c.classid == classId}">
                                <option value='${c.classid}' selected="selected">${c.classid}: ${c.name} ${c.section}</option>
                            </c:if>
                            <c:if test = "${c.classid != classId}">
                                <option value='${c.classid}'>${c.classid}: ${c.name} ${c.section}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </td>
            </c:if>
        </tr>
    </table>
</div>