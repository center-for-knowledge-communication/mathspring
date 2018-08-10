<%@ page import="edu.umass.ckc.wo.tutormeta.TopicMastery" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.umass.ckc.wo.smgr.SessionManager" %>
  Created by IntelliJ IDEA.
  User: Dovan
  Date: Aug 12, 2012
  Time: 5:22:15 PM
  To change this template use File | Settings | File Templates.
--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<%--@elvariable id="problemDetailsList" type="java.util.List"--%>
<%--@elvariable id="td" type="edu.umass.ckc.wo.myprogress.TopicDetails"--%>
<%--@elvariable id="pd" type="edu.umass.ckc.wo.myprogress.problemDetails"--%>



<html id="default"><head>
    <meta charset="utf-8" />
    <title>MathSpring   </title>

    <link rel="stylesheet" href="css/wayang.css" /><!-- Main Lar -->
    <link href='http://fonts.googleapis.com/css?family=IM+Fell+DW+Pica|Acme|Asul' rel='stylesheet' type='text/css'>

    <!-- Start Jquery and Scripts -->

    <script language="javascript" type="text/javascript" src="js/jquery-1.10.2.js"></script>
    <script language="javascript" type="text/javascript" src="js/jchart.js"></script>


    <script language="javascript" type="text/javascript">

        var problemList=new Array();
        var currentProblem="";
        var currentTopic=${topicId};
        var currentProblemId=0;
        var currentEffort="";

        var formalityId="";
        var isFormality=false;

        var problemImagePath="";
        var effortFeedback="";

        var elapsedTime=0;
        var probElapsedTime=0;
        var startClockTime = 0;
        var startElapsedTime=0;

        var useHybridTutor=${useHybridTutor};


        function initiateElapsedTime() {

            startElapsedTime= ${elapsedTime} ;
            var d = new Date();
            startClockTime = d.getTime();

        }

        function updateElapsedTime() {

            var d =new Date();
            var now = d.getTime();
            probElapsedTime += now-startClockTime;
            elapsedTime = startElapsedTime + probElapsedTime;
//            alert(elapsedTime);
            return  elapsedTime;

        }


        function initChart() {




            var chart = Chart;
            chart.init();


            chart.renderMastery("masteryChartDiv",${mastery} ,${problemsDone} );





        var totalProblems=${totalProblems};
        var problemsDone= ${problemsDone};


      var i=0;

        <c:forEach var="pd" items="${problemDetailsList}">

        problemList[i]=new Array(8);


        problemList[i][0]="${pd.problemId}";
        problemList[i][1]="${pd.problemName}";
        problemList[i][2]="${pd.effort}";
        problemList[i][3]=${pd.numAttemptsToSolve}
        problemList[i][4]=${pd.numHints};
        problemList[i][6]="${pd.ccstds}";
        problemList[i][7]="${pd.snapshot}";


           i++;
        </c:forEach>

        chart.problemsDone("problemsDiv",${problemsDone} ,${totalProblems} );

       chart.renderCharts(problemList,i,wrapperList);



        }


        $(document).ready(function(){

            initiateElapsedTime();
            initChart();
            <%--$.get("TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId=0&studentAction=loadTopicDetailsPage&comment=");--%>



            $("#searchlink").click(function(){
                $(".dropdown_contentBox").toggle();

            });

            $("#commentlink").click(function(){

                var position = $("#commentlink").position();

                var tPosX = position.left ;
                var tPosY = position.top+$("#commentlink").height();
                $("#leaveComment").css({top:tPosY, left: tPosX}).toggle();


            });

            function tryThisComplete (problemId) {
                window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&elapsedTime='+updateElapsedTime()+'&learningCompanion=${learningCompanion}&mode=practice&topicId='+currentTopic+'&problemIdString='+problemId;

            }

            $('#wrapperList li').each(function(index) {


                $(this)

                        .click(function(){
                            if (useHybridTutor)
                                window.location= "${pageContext.request.contextPath}/TutorBrain?action=MPPTryProblem&elapsedTime="+updateElapsedTime()+"&sessionId=${sessionId}&problemId="+currentProblemId+"&topicId="+currentTopic+"&studentAction=tryThis&mode=practice&comment=";
                            else $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPTryProblem&elapsedTime="+updateElapsedTime()+"&sessionId=${sessionId}&problemId="+currentProblemId+"&topicId="+currentTopic+"&studentAction=tryThis&comment=",tryThisComplete(currentProblemId));



                        })

                        .hover(function(){
                            $(this).addClass("td-over");

                            var position = $("#problemCards").position();

                            var tPosX = position.left ;
                            var tPosY = position.top+$("#problemCards").height();
                            $(".dropDownContent").css({top:tPosY, left: tPosX}).show();

                            currentProblemId=problemList[index-1][0];
                            currentProblem=problemList[index-1][1];
                            currentEffort=problemList[index-1][2];

                            effortFeedback=problemList[index-1][5];


                            if (currentProblem.substring(0,10)=="formality_")
                            {isFormality=true;
                                formalityId= currentProblem.substring(10,currentProblem.length);
                            }
                            else isFormality=false;






                            if (!isFormality)  {

                            $("#problemDetails").text(currentProblem+": "+effortFeedback);
                            $("#problemDetails").append("<br/>CCSS: " + problemList[index-1][6] +"<br/>");


                            $("#problemDetails").append("<button type='button' id='problemDetailsButtons'>Click to try this problem &raquo;</button>");
                            $("#problemDetails").append("<br/><br/>");



                            $("#problemDetails").append("<img id='problemImage' />");
                            document.getElementById("problemImage").src ="data:image/jpg;base64,"+problemList[index-1][7];
                            }
                            else{

                                $("#problemDetails").text(currentProblem);
                                $("#problemDetails").append("<br/>CCSS: " + problemList[index-1][6] +"<br/>");
                                $("#problemDetails").append("<button type='button' id='problemDetailsButtons'>Click to try this problem &raquo;</button>");
                                $("#problemDetails").append("<br/><br/>");
                                problemImagePath="http://cadmium.cs.umass.edu/formality/FormalityServlet?fxn=questionSnapshot&qID="+formalityId;
                                $("#problemDetails").append("<iframe id='formalityProblemFrame'  width='600' height='300'> </iframe>");
                                document.getElementById("formalityProblemFrame").src =problemImagePath;

                            }

                            document.getElementById("problemDetailsButtons").onclick= function()
                            {

                                <%--$.get("${pageContext.request.contextPath}/TutorBrain?action=MPPTryProblem&sessionId=${sessionId}&problemId="+currentProblemId+"&studentAction=tryThis&comment=",tryThisComplete(${topicId},currentProblemId));--%>
                                if (useHybridTutor)
                                    window.location="${pageContext.request.contextPath}/TutorBrain?action=MPPTryProblem&elapsedTime="+updateElapsedTime()+"&sessionId=${sessionId}&problemId="+currentProblemId+"&topicId="+currentTopic+"&studentAction=tryThis&mode=practice&comment=";
                                else $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPTryProblem&elapsedTime="+updateElapsedTime()+"&sessionId=${sessionId}&problemId="+currentProblemId+"&topicId="+currentTopic+"&studentAction=tryThis&mode=practice&comment=",tryThisComplete(currentProblemId));

                            }


                        },function(){
                            $(this).removeClass("td-over");
                        });


            });



            $(".closeWindow").click(function(){
                this.parent().hide();
            });

            $(".submitAndClear").click(function(){
                $("#leaveComment").hide();
            });



        });


    </script>



    <!-- End JQuery and Scripts-->
</head>

<body >


<div class="gradient_bg">




    <!--end of the info>

 <header><!-- Start Header -->


    <!-- Start Header Middle --><div id="header_wrapper_small" ><!-- BEGIN HEADER MAIN -->
    <!--Start Logo--><header  id="wayang_logo_small">
        <img src="img/mathspring_logo.png" height="34" alt="MathSpring">
        </header>

    <!--End Logo-->
    <subheader id="subheader_small">Topic Details</subheader><!--End Logo-->



</div>




    </header><!-- END HEADER -->
    </section>
    <!-- END SECTION MAIN CONTENT -->



    <section class="clear">
    </section>
    <section class="normal_body">
        <div id="progress_body">



            <a href="#" onclick="window.location='TutorBrain?action=navigation&from=satHut&to=my_progress&elapsedTime='+updateElapsedTime()+'&sessionId=${sessionId}&clientVersion=${clientVersion}'" class="blueLink" >< Go back to Progress Page</a>


           <div class="progress_subheading">${topicName} </div>


            <div> Mastery Level<div id="masteryChartDiv"></div>
            <div id="problemsDiv"></div>
            </div>

            <br/>


            You can see a list of rectangles, each rectangle representing a problem.  <br/>
            <br/>
            The leftmost problems are the easiest and the rightmost problems are the hardest.
            <br/>
            <br/>


<!--list of marked cards corresponding to each problem -->
   <div id="problemCards"   rel="performanceDetails" style="position:absolute;" ><ul id="wrapperList"><li><table ></table></li></ul></div>
        </div>
    </section>

    <footer><!-- BEGIN FOOTER -->
        &nbsp;

    </footer><!-- END FOOTER -->


</div>


<!--Drop Down problem details element -->
<DIV id="performanceDetails" class="dropDownContent">

    <div class="closeWindow">[ <a href="">X Close this window</a> ]</div><br/>

    <div id="problemDetails" style="font-size:16px"></div>​


</DIV>


</body>

</html>