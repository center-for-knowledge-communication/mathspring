<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>All Topic Mastery history for student ${studentName} in class ${className}</title>
    <script src="js/jquery-1.10.2.js"></script>
    <%--<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>--%>
    <script type="text/javascript" src="js/jqplot/jquery.jqplot.min.js"></script>
    <script type="text/javascript" src="js/jqplot/plugins/jqplot.canvasTextRenderer.min.js"></script>
    <script type="text/javascript" src="js/jqplot/plugins/jqplot.canvasAxisLabelRenderer.min.js"></script>
    <link rel="stylesheet" type="text/css" href="js/jqplot/jquery.jqplot.min.css" />
    <script class="code" type="text/javascript">

        function formHandler(form){
            var URL = document.form.site.options[document.form.site.selectedIndex].value;
            window.open( URL);
        }

        $(document).ready(function(){
            var ticks = [${xLabels}] ;

            var plot1 = $.jqplot ('chart1', ${masterySequence}, {
                animate: true,
                series: [
                    <c:set var="c" value="${numTopics}"></c:set>
                    <c:forEach var="topic" items="${topicNames}">
                    {showLine: false,
                    label : '${topic}'}
                    <%--  We want a comma on every line except the last one--%>
                    <c:if test="${c>1}"> , </c:if>
                    <c:set var="c" value="${c - 1}"></c:set>
                    </c:forEach>
                ],
                legend: {
                    show: true,
                    location: 's',
                    placement: 'outside'
                },
                axes: {
                    xaxis: {

                        label: 'Problems in order seen' ,
                        ticks: ticks

                    },
                    yaxis: {
                        min: 0.0,
                        max: 1.0,
                        label: 'Mastery',
                        labelRenderer: $.jqplot.CanvasAxisLabelRenderer
                    }
                }
            });
        });
    </script>

</head>
<h3>All Topics Mastery history for student ${studentName}</h3>
<h5><a href='?action=AdminViewReport&teacherId=${teacherId}&classId=${classId}'>Choose another report</a></h5>
<br>


<div id="chart1" style="height:300px; width:${width+100}px;"></div>

<pre class="code prettyprint brush: js"></pre>

<h3>${message}</h3>


<form name="form">
    <%--@elvariable id="problems" type="edu.umass.ckc.wo.content.Problem[]"--%>
    <select name="site" size=1>
        <c:forEach var="problem" items="${problems}">
            <option value="${problem.getPreviewerURL()}">${problem.id}:${problem.name}-${problem.nickname} : ${problem.type}</option>
        </c:forEach>
    </select>
    <input type=button value="Preview" onClick="javascript:formHandler(this)">
</form>