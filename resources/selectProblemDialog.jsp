<%--
  Created by IntelliJ IDEA.
  User: marshall
  Date: 9/30/14
  Time: 11:42 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/jquery.tablesorter.min.js"></script>
    <script src="js/jquery.tablesorter.widgets.js"></script>
    <script>
        function forceNextProblem (id) {
            window.parent.forceNextProblem(id);
        }

        $(document).ready(function() {
            $("#selectProblemTable").tablesorter({
                widgets: ["zebra", "filter"],
                widgetOptions: {
                    filter_external: '#selectProblemTableFilter',
                    filter_columnFilters: false,
                    filter_liveSearch: true
                },
                sortList: [[0,0], [1,0], [2,0]]
            });
            $("#selectProblemTableFilter").focus();
        });
    </script>
    <style>
        /* rows hidden by filtering (needed for child rows) */
        .tablesorter .filtered {
            display: none;
        }

        /* ajax error row */
        .tablesorter .tablesorter-errorRow td {
            text-align: center;
            cursor: pointer;
            background-color: #e6bf99;
        }
    </style>
    <title></title>
</head>
<body>
<input id="selectProblemTableFilter" type="search" placeholder="Search..." data-column="all"/>
<table id="selectProblemTable">
    <thead>
    <tr><th>problem ID</th><th>name</th><th>status</th><th>type</th><th>select</th></tr>
    </thead>
    <tbody>
    <%--@elvariable id="problems" type="edu.umass.ckc.wo.content.Problem[]"--%>
    <c:forEach var="problem" items="${problems}">
        <tr><td>${problem.id}</td><td style="max-width:160px;word-wrap:break-word">${problem.name}</td><td>${problem.status}</td><td>${problem.form}</td><td>&nbsp; &nbsp;<a onclick="forceNextProblem(${problem.id})" href="#">choose</a></td></tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>