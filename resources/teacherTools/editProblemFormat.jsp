<%--
  Created by IntelliJ IDEA.
  User: rezecib
  Date: 4/10/2017
  Time: 12:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>QuickAuth Problem Format Editor</title>
    <link rel="stylesheet" type="text/css" href="css/quickAuthProblem.css"/>
    <link rel="stylesheet" type="text/css" href="css/quickAuthFormatEditor.css"/>
    <script src="js/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="js/quickAuth/format2json.js"></script>
    <script type="text/javascript" src="js/quickAuth/formatBuilder.js"></script>
    <script type="text/javascript" src="js/quickAuth/formatEditor.js"></script>
    <%--@elvariable id="templates" type="String[]"--%>
    <%--@elvariable id="fonts" type="String[]"--%>
    <%--@elvariable id="templates" type="String[]"--%>
    <%--@elvariable id="problemFormat" type="String"--%>
    <script type="text/javascript">
        function buildPage() {
            <%-- Pass the settings from the database to our javascript --%>
            quickAuthFormatEditor.buildFormatEditor(${templates}, ${fonts}, ${colors}, ${problemFormat});
        }
    </script>
</head>
<body onLoad="buildPage()" style="width:800px;height:600px;background-color:#444;">
<div style="float:right;height:100%;width:200px;display:flex;flex-direction:column;justify-content:space-around;">
    <div style="height:45%">
        <div class="header">Problem Layout<br/>Preview:</div>
        <div id="ProblemContainerLayout" class="problem-container"></div>
    </div>
    <div style="height:45%">
        <div class="header">Problem Text<br/>Preview:</div>
        <div id="ProblemContainerText" class="problem-container"></div>
    </div>
</div>
<div id="ProblemEditor">
    <div id="TemplateSelector" class="editor-panel" style="height:75px;box-sizing:content-box;">
        <div style="line-height:75px;margin-right:5px" class="header">Templates:</div>
    </div>
    <div class="clear"></div>
    <div style="width:100%;display:flex;flex-direction:row;flex:1;">
        <div class="editor-panel" style="flex:1;margin:0 10px;">
            <div class="header">Block Style</div>
            <div id="BlockEditor" class="editor-panel"></div>
        </div>
        <div class="editor-panel" style="flex:1;margin-right:10px;">
            <div class="header">Block Layout</div>
            <div id="BlockSelector"></div>
            <div id="LayoutEditor"></div>
        </div>
    </div>
    <textarea id="ProblemFormatOutput" type="text" style="resize:none;box-sizing:border-box;margin:10px 5px 5px 5px;" onClick="this.select();" readonly></textarea>
</div>
</body>
</html>
