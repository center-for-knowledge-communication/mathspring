<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <title>MathSpring K-12</title>


    <link href="login/css/portK12.css" rel="stylesheet" type="text/css" />
    <link href="css/simple-slider.css" rel="stylesheet" type="text/css" />
    <link href="login/css/p7ccm04.css" rel="stylesheet" type="text/css" media="all" />
    <!--[if lte IE 7]>
    <link href="/p7ie_fixes/p7ccm_ie.css" rel="stylesheet" type="text/css" media="all" />
    <![endif]-->
    <script type="text/javascript" src="login/js/p7EHCscripts.js"></script>
    <link href="js/jquery-ui-1.10.4.custom/css/spring/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
    <script src="js/jquery-1.10.2.js"></script>
    <%--<script src="js/jquery-ui-1.10.3/ui/jquery-ui.js"></script>--%>
    <script src="js/jquery-ui-1.10.4.custom/js/jquery-ui-1.10.4.custom.min.js"></script>
    <script type="text/javascript" src="js/simple-slider.js"></script>
    <script type="text/javascript" src="js/login.js"></script>
    <script type="text/javascript">
        var huygui=false;

        // Unfortunately the back button will run this function too which means that it can generate a BeginExternalActivity
        $(document).ready(
                function () {
                    surveyButton('${servletContext}', '${servletName}', '${URL}', '${skin}', ${sessionId}, '${interventionClass}');

                });


    </script>
    <style type="text/css">
    </style>
</head>

<body>

<div class="container">
    <div class="content">
        <div id="p7CCM_1" class="p7CCM04 p7ccm04-fixed-980">
            <div class="p7ccm04-content-row p7ccm04-RGBA p7ccm-row">
                <div class="p7ccm04-3col-sidebar-left-right-column2 p7ccm-col">
                    <div class="p7ccm04-3col-sidebar-left-right-column2-cnt p7ccm04-content">          </div>
                </div>
            </div>
            <div class="p7ccm04-content-row p7ccm04-RGBA p7ccm-row">
                <div class="p7ccm04-1col-column1 p7ccm-col">

                    <div class="p7ccm04-1col-column1-cnt p7ccm04-content"> <img src="img/header3.png" width="980" height="100" alt="MathSpring Math Tutor K 12" /></div>
                </div>
            </div>

            <div class="p7ccm04-content-row p7ccm04-RGBA p7ccm-row">
                <div class="p7ccm04-1col-column1 p7ccm-col">
                    <div class="p7ccm04-1col-column1-cnt p7ccm04-content">
                        <p>Thank you for using the MathSpring Tutor.  Please answer some questions so the software can be more personable in helping you.</p>
                        <div class="nest">
                            <div class="set"> </div>
                        </div>
                        <div class="set">
                            <jsp:include page="${innerjsp}" />
                        </div>

                        <p>&nbsp;</p>
                        <p>&nbsp;</p>
                        <p class="centered">&nbsp;</p>
                    </div>
                </div>
            </div>
            <div class="p7ccm04-content-row p7ccm-row">
                <div class="p7ccm04-3col-sidebar-left-right-column1 p7ccm-col">
                    <div class="p7ccm04-3col-sidebar-left-right-column1-cnt p7ccm04-content p7ehc-1">
                        <div class="footCR">The <a href="http://wayangoutpost.com/">MathSpring</a> Mathematics Tutor is under development by the <a href="http://centerforknowledgecommunication.com/">Center for Knowledge Communication</a> at the <a href="http://www.umass.edu/">University of Massachusetts Amherst</a></div>
                    </div>
                </div>
                <div class="p7ccm04-3col-sidebar-left-right-column3 p7ccm-col"></div>
            </div>
        </div>
        <!-- end .container --></div>
</body>
</html>
