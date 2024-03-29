<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>
<% 


ResourceBundle versions = null; 
try {
	 versions = ResourceBundle.getBundle("Versions");
}
catch (Exception e) {
//	logger.error(e.getMessage());	
}

Locale loc = request.getLocale(); 
String lang = loc.getLanguage();
String country = loc.getCountry();

System.out.println("locale set to:" + lang + "-" + country );	

if (!lang.equals("es")) {
	loc = new Locale("en","US");	
}			
		
ResourceBundle rb = null;
try {
	rb = ResourceBundle.getBundle("MathSpring",loc);
}
catch (Exception e) {
//	logger.error(e.getMessage());
}
%>

<!-- Frank 011021 Issue #289 -->

<!-- **** WARNING **** -->
<!-- **** WARNING **** -->
<!-- **** WARNING **** -->

<!-- if any hard-coded text is changed you must update its translation in js/bootstrap/js/language_es.js -->
<!--   and the script reference below -  language_es.js?ver=011021 with the ver=current date to force cache refresh -->

<!-- if anything in js/bootstrap/css/style.css is changed you must update the script reference below - js/bootstrap/css/style.css?ver=011021 with the ver=current date to force cache refresh -->

<!-- **** END WARNING **** -->

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Grow Your Math with MathSpring</title>
    <link rel="apple-touch-icon" sizes="180x180" href="img/apple-touch-icon.png">
    <link rel="icon" type="image/png" href="img/favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="img/favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="css/manifest.json">
    <meta name="theme-color" content="#ffffff">

    <!-- Font -->
    <link href="https://fonts.googleapis.com/css?family=Raleway:300,400" rel="stylesheet">

    <!-- Custom CSS -->
    <link rel="stylesheet" href="js/bootstrap/css/style.css?<%=versions.getString("css_version")%>">

    <!-- JS LIBRARIES AND CUSTOM JS FILE -->
    <script src="js/bootstrap/js/jquery-2.2.2.min.js"></script>
    <script src="js/bootstrap/js/bootstrap.min.js"></script>
    <script src="js/bootstrap/js/parallax.js"></script>
    <script src="js/bootstrap/js/custom-animate.js"></script>
    <script src="js/bootstrap/js/language_es.js?<%=versions.getString("js_version")%>"></script>
    <script type="text/javascript">
    $(document).ready(function () {
    	langPrefrenceForWelcomePage();
    });
    </script>
</head>

<body>
<div id="google_translate_element"></div>
<!-- NAVIGATION BAR -->
<header class="site-header" role="banner">
    <div id="wrapper">
        <div class="navbar-header">
            <img class="logo" src="img/ms_mini_logo_new.png">
        </div>
</header>

<!-- BIG BANNER AND BRIEF INTRODUCTION -->
<section id="big-banner" data-type="background" data-speed="5">
    <article>
        <div class="container clearfix">
            <div class="row">
                <div class="col-sm-12" id="intro-text">
                    <h1 style="text-align:center">Grow your Math with MathSpring</h1>
                    <p class="banner-text" style="text-align:center">The MathSpring Tutor will help you
                        succeed at math standardized tests
                        by deeply understanding the material.</p>
                </div><!-- intro-text-->
                <div class="col-sm-3 col-sm-offset-2">
                    <a
                            class="btn btn-default"
                            id="signup-button"
                            href="/ms/WoLoginServlet?action=LoginK12_1&var=b&msRole=student">STUDENT PORTAL</a>
                </div><!-- col-md-offset-9 -->
                <div class="col-sm-3 col-sm-offset-2">
                    <a
                            class="btn btn-default"
                            id="signup-teacher-button"
                            href="/ms/WoLoginServlet?action=LoginK12_1&var=b&msRole=teacher">TEACHER PORTAL</a>
                </div><!-- col-md-offset-9 -->
            </div><!-- row -->
        </div><!-- container clearfix -->
    </article>
    <div id="scrollBtn">
        <img src="img/down_arrow.svg" alt=""/>
    </div>
</section><!-- big-banner -->

<!-- STATS ABOUT MATHSPRING SECTION -->
<section id="stat">
    <div class="container">
        <h2>MathSpring is ...</h2>
        <p>
            personalized software that uses interactive multimedia to support students as they solve math problems.
        </p>
        <div class="row">
            <div class="col-md-4">
                <div class="round-number" id="round-1">800+</div>
                <p id="stat-text1">questions to help you better prepare for Math
                    standardized exams</p>
            </div>
            <div class="col-md-4">
                <div class="round-number" id="round-2">10%</div>
                <p id="stat-text2">improvement on scores after only 3 hours of instruction</p>
            </div>
            <div class="col-md-4">
                <div class="round-number" id="round-3">2000+</div>
                <p id="stat-text3">students have used the MathSpring Tutoring
                    system around the United States</p>
            </div>
        </div>
    </div>
</section>

<hr>

<!-- TESTIMONIALS SECTION -->
<section id="testimony">
    <div class="container">
        <div class="row">
            <h3>What People Are Saying About MathSpring</h3>
            <div class="col-sm-8 col-sm-offset-2">
                <div class="row  testimony-article">
                    <div class="col-sm-4">
                        <img class="avatar" src="img/avatar.svg" alt="">
                    </div><!-- col-sm-4 -->

                    <div class="col-xs-8">
                        <p>I didn't like it, I loved it! It helped me learn more
                            than I thought. The progress page helped me because I
                            now know where I am now, in math.</p>
                        <p><em>-- Student A</em></p>
                    </div><!-- col-xs-8 -->
                </div><!-- row -->
            </div><!-- col-sm-8 -->

            <div class="col-sm-8 col-sm-offset-2">
                <div class="row testimony-article">
                    <div class="col-sm-4">
                        <img class="avatar" src="img/avatar.svg" alt="">
                    </div><!-- col-sm-4 -->

                    <div class="col-xs-8">
                        <p>You learn new things. If you don't get an answer you can watch a video, click the hint button. MathSpring helps you learn things you don't really know.
                        </p>
                        <p><em>-- Student B</em></p>
                    </div><!-- col-xs-8 -->
                </div><!-- row -->
            </div><!-- col-sm-8 -->

            <div class="col-sm-8 col-sm-offset-2">
                <div class="row testimony-article">
                    <div class="col-sm-4">
                        <img class="avatar" src="img/avatar.svg" alt="">
                    </div><!-- col-sm-4 -->

                    <div class="col-xs-8">
                        <p>What I liked about the software is that it is a great learning tool and it is fun I guess.
                        </p>
                        <p><em>-- Student C</em></p>
                    </div><!-- col-xs-8 -->
                </div><!-- row -->
            </div><!-- col-sm-8 -->
        </div><!-- row -->
    </div><!-- container -->
</section><!-- testimony -->

<footer>
    &copy; 2016 University of Massachusetts Amherst and Worcester Polytechnic Institute  ~ All Rights Reserved.
</footer>
</body>
</html>
