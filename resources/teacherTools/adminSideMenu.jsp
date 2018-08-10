<%-- Wayang Template Header for Each page

Elizabeth Do
March 20, 2012

   --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">

    <title>Mathspring Administrator Tools</title>

    <link href="css/adminMan.css" rel="stylesheet" type="text/css">
    <link href="css/p7ccm/p7ccm03.css" rel="stylesheet" type="text/css" media="all">
    <script type="text/javascript" src="css/p7ehc/p7EHCscripts.js"></script>
    <!--[if lte IE 7]>
    <link href="css//p7ie_fixes/p7ccm_ie.css" rel="stylesheet" type="text/css" media="all" />
    <![endif]-->
    <link href="css/p7tmm/p7TMM10.css" rel="stylesheet" type="text/css" media="all">
    <script type="text/javascript" src="css/p7tmm/p7TMMscripts.js"></script>
    <script type="text/javascript" src="css/p7apm/p7APMscripts.js"></script>
    <link href="css/p7apm/p7APM08.css" rel="stylesheet" type="text/css" media="all">
    <link href="css/p7ccm/p7ccm01.css" rel="stylesheet" type="text/css" media="all">
    <script type="text/javascript" src="css/p7ttm/p7TTMscripts.js"></script>
    <link href="css/p7ttm/p7TTM01.css" rel="stylesheet" type="text/css" media="all">
    <link href="css/p7ttm/sample.css" rel="stylesheet" type="text/css" media="all">
    <script type="text/javascript">
        <!--
        P7_opTTM('id:p7Tooltip_1','id:dBar','p7TTM01',8,465,1,1,0,0,35,1,300,2,1,1,0,0,0,0);
        P7_opTTM('id:p7Tooltip_2','att:title','p7TTM01',8,300,1,1,0,0,35,1,300,2,1,1,0,0,0,0);
        //-->
    </script>

    <!--<link rel="shortcut icon" href="favicon.ico" >         -->
    <link rel="icon" type="image/png" href="images/EDwoTreeCurved2.png" >


    <link href="fonts/stylesheet.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="teacherTools/changeClassGrab.js"></script>
    <script type="text/javascript" src="teacherTools/dragAndDropTable.js"></script>
    <script type="text/javascript">
        var repeatAct = '${action}';
        var path2= '${pageContext.request.contextPath}';
    </script>
</head>

<body>

<div class="container">

    <div class="content">

        <div class="wrapperContainer">


        <div id="p7CCM_1" class="p7CCM03 p7ccm03-rounded p7ccm03-outer-shadow p7ccm03-auto">

            <div class="p7ccm03-content-row p7ccm03-top-rounded p7ccm03-trans p7ccm03-RGBA p7ccm-row">
                <div class="p7ccm03-1col-column1 p7ccm-col">
                    <div class="p7ccm03-1col-column1-cnt p7ccm03-content p7ccm03-top-rounded">
                        <div class="headerWrapper">
                        <table width="100%">
                            <tr>
                                <td width=60%>
                        <p id="header">Mathspring Administrator Tools </p>
                                </td>
                                <td width=40%>
                        <p  align="right" width="400"><font color="#8BB42D" size="4" face="Arial, Helvetica, sans-serif">Class: ${classId}:${classInfo.name} </font> </p>
                                </td>
                            </tr>
                        </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="p7ccm03-content-row p7ccm03-dyn-img p7ccm03-RGBA p7ccm-row">
                <div class="p7ccm03-2col-sidebar-fixed-left-column1 p7ccm-col">

                    <div class="p7ccm03-2col-sidebar-fixed-left-column1-cnt p7ccm03-content p7ccm03-content-inner-shadow p7ehc-1">
                        <div class="nestl">
                        <p><img src="images/EDwoTreeCurved.png" width="180" height="195" alt="logo image Tree"></p>
                        <div class="leftNav">
                            <div id="p7TMM_1" class="p7TMM10">
                                <ul class="p7TMM">
                                    <li><a href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminMainPage&teacherId=${teacherId}&classId=${classId}">Main</a></li>

                                    <c:if test="${teacherId > 0}">
                                        <li><a href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminCreateNewClass&teacherId=${teacherId}">Create Class</a></li>
                                    </c:if>
                                    <li><a href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminTeacherRegistration">Create Teacher</a></li>
                                    <li><a href="#">Edit Classes/Topics</a>
                                        <div>
                                            <ul>
                                                <li><a href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminSimpleClassSetup&teacherId=${teacherId}&classId=${classId}">Simple Tutor Configuration</a></li>

                                                <li><a href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminEditTopics&teacherId=${teacherId}&classId=${classId}"> Topic Control </a></li>
                                                <li><a href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminProblemSelection&teacherId=${teacherId}&classId=${classId}"> Problem Selection </a></li>
                                                <%--<li><a href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminAlterClassPretest&teacherId=${teacherId}&classId=${classId}">Pre and Post Tests</a></li>--%>
                                                <li><a href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminAlterClassPedagogies&teacherId=${teacherId}&classId=${classId}">Pedagogy Selection</a></li>
                                                <li><a href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminAlterClassPrePost&teacherId=${teacherId}&classId=${classId}">Pre/Post Surveys</a></li>
                                                <%--<li><a href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminAlterClassActivateHuts&teacherId=${teacherId}&classId=${classId}">Hut Activation</a></li>--%>

                                                <li><a href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminEditClassList&teacherId=${teacherId}&classId=${classId}">Student List </a></li>
                                                <li><a href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminAlterClassCloneClass&teacherId=${teacherId}&classId=${classId}">Clone a Class</a></li>
                                                <li><a href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminOtherClassConfig&teacherId=${teacherId}&classId=${classId}">Class Email Settings</a></li>
                                                <li><a href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminAlterClassEdit&teacherId=${teacherId}&classId=${classId}">Class Information </a></li>



                                            </ul>
                                        </div>
                                    </li>
                                    <li><a href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminViewReport&teacherId=${teacherId}&classId=${classId}">Class Reports</a></li>


                                    <li><a href="${pageContext.request.contextPath}/WoAdmin?action=AdminTutor&teacherId=${teacherId}">Administrate Tutor </a></li>

                                    <li><a href="http://mathspring.org/">MathSpring Website </a></li>


                                </ul>
                                <!--[if lte IE 6]>
                                <style>.p7TMM10 .p7TMM, .p7TMM10 a, .p7TMM10 li {height:1%;}</style>
                                <![endif]-->
                                <!--[if IE 5]>
                                <style>.p7TMM10 a, .p7TMM10 a {overflow: visible !important;}</style>
                                <![endif]-->
                                <script type="text/javascript">
                                    <!--
                                    P7_TMMop('p7TMM_1',1,0,3,3,1,1,0,1,-1,150);
                                    //-->
                                </script>
                            </div>
                        </div>

                        <p>&nbsp;</p>
                        <div class="search">
                            <form method="get" action="http://www.mysite.com/search.html">
                                <table cellpadding="0px" cellspacing="0px">
                                    <tr>
                                        <td style="border-style:solid;border-color:#FFDD76;border-width:1px;"><input type="text" name="zoom_query" style="width:135px; border:0px solid; height:17px; padding:0px 3px; position:relative;"></td>
                                        <td style="border-style:solid;border-color:#FFDD76;border-width:0px;"><input type="submit" value="" style="border-style: none; background: url('searchbutton2.gif') no-repeat; width: 24px; height: 22px;"></td>
                                    </tr>
                                </table>
                                Search
                            </form>
                        </div>
                        </div>
                        <div class="treeCR"></div>
                    </div>
                </div>
                <div class="p7ccm03-2col-sidebar-fixed-left-column2 p7ccm-col">
                    <div class="p7ccm03-2col-sidebar-fixed-left-column2-cnt p7ccm03-content p7ccm03-content-inner-shadow p7ehc-1">


<jsp:include page="changeClass.jsp" />