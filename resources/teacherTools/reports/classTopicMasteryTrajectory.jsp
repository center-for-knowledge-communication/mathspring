<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Average Topic Mastery for class- ${className}</title>
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
            var line1 = ${processMastery};
            var line2 = ${answerMastery};
//            var plot1 = $.jqplot ('chart1', [line1,line2], {
            var plot1 = $.jqplot ('chart1', [line1], {
                animate: true,
                series: [
                    {label: 'Average Process Mastery'}
//                    {label: 'Average Answer Mastery'}
                ],
                legend: {
                    show: true,
                    location: 'e',
                    placement: 'outside'
                },
                axes: {
                    xaxis: {

                        label: 'Number of problems seen' ,
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
<h3>Topic Mastery history for class ${className} for topic ${topicName}</h3>
<h5><a href='?action=AdminViewReport&teacherId=${teacherId}&classId=${classId}'>Choose another report</a></h5>
<br>


<div id="chart1" style="height:300px; width:${width+100}px;"></div>

<pre class="code prettyprint brush: js"></pre>

<h3>${message}</h3>

