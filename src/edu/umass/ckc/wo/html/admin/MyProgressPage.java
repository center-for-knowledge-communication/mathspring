package edu.umass.ckc.wo.html.admin;

import edu.umass.ckc.servlet.servbase.View;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.pedModel.TopicSelectorImpl;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.tutor.probSel.TopicModelParameters;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemData;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemHistory;
import edu.umass.ckc.wo.tutormeta.TopicMastery;
import edu.umass.ckc.wo.tutormeta.TopicSelector;

import java.sql.Connection;
import java.util.List;
import java.sql.SQLException;


public class MyProgressPage implements View  {

    //String backToVillage = "http://cadmium.cs.umass.edu/wayang2/flash/client/WoLogin_college.swf?sessnum=" ;
   //String backToVillage = "file:///Users/ivon/Documents/EDUCAUSE/Flash/client/WoLogin_college.swf?sessnum=" ;
    String backToVillage = null ;

    int sessionId ;
    int studId;
    List<TopicMastery> topicMasteries ;
    SessionManager smgr ;
    String client = null ;
    double masteryThreshold = 0.85 ;
    TopicSelector topicSelector;


  public MyProgressPage (SessionManager smgr_) throws Exception  {
      smgr = smgr_ ;
      sessionId = smgr.getSessionNum() ;
      studId =  smgr.getStudentId() ;
      topicMasteries = smgr.getStudentModel().getTopicMasteries() ;
      client = smgr.getClient() ;
      masteryThreshold = (smgr.getClassMasteryThreshold()) ;
      int classId = smgr.getClassID();
      Connection conn = smgr.getConnection();
      topicSelector = new TopicSelectorImpl(smgr, (TopicModelParameters) DbClass.getClassLessonModelParameters(conn, classId));
  }

  public String getHeaderView() {
      return "<!DOCTYPE html>\n" +
              "<html id=\"default\"> \n" +
              "<head>\n" +
              "<meta charset=\"utf-8\" />\n" +
              "<title>Wayang</title>\n" +
              "\n" +
              "\t<link rel=\"stylesheet\" href=\"css/wayang.css\" /><!-- Main Lar -->\n" +
              "      <link href='http://fonts.googleapis.com/css?family=IM+Fell+DW+Pica|Acme|Asul' rel='stylesheet' type='text/css'>\n" +
              "    \n" ;

  }

  public String getScripts() throws Exception {
          String s = new String () ;
          s = s.concat("    <!-- Start Jquery and Scripts -->\n\n") ;

          s= s.concat("    <script language=\"javascript\" type=\"text/javascript\" src=\"js/jschart.js\"></script>\n" ) ;
          s= s.concat("\t\t<script language=\"javascript\" type=\"text/javascript\">\n" ) ;


        for (TopicMastery t: topicMasteries) {

              String chartData = "chartData" + t.getTopic().getId() ;
              String masteryChartName = "masteryChart" + t.getTopic().getId() ;
              int masteryValue =  (new Double( t.getMastery()*100.0)).intValue() ;

//              TopicLoader topicLoader = new TopicLoader(smgr.getConnection(),smgr.getClassID(), smgr) ;
            int numProbsSeenInTopic = 0;
            List<Integer> practiceProbsSeen;

            try {

//                StudentProblemHistory studentProblemHistory = smgr.getStudentModel().getStudentProblemHistory();
//                List<StudentProblemData> probEncountersInTopic = studentProblemHistory.getTopicHistoryMostRecentEncounters(t.getTopic().getId());
//                practiceProbsSeen = studentProblemHistory.getPracticeProblemsSeen(probEncountersInTopic);
//                practiceProbsSeen = smgr.getPedagogicalModel().getPracticeProblemsSeen(probEncountersInTopic);
                practiceProbsSeen = smgr.getExtendedStudentState().getPracticeProblemsSeenInTopic(t.getTopic().getId());
                numProbsSeenInTopic = practiceProbsSeen.size();
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                System.out.println("topicLoader.getProblemsSeenInTopic had a problem in MyProgressPage") ;
            }

            if (numProbsSeenInTopic == 0)
                    s= s.concat("\t\t\tvar " + chartData + "= \"Not seen yet;0\";\n" ) ;
              else
                    s= s.concat("\t\t\tvar " + chartData + "= \"Percent of Mastery;" + masteryValue + "\";\n" ) ;

              s= s.concat("\t\t\tfunction initChart" + masteryChartName + "() {\t\t\t\t\n") ;
              s= s.concat("\t\t\t\tvar chart = Chart;\n") ;
              s= s.concat("\t\t\t\tchart.setServletInfo(\"" + masteryChartName + "\");\n") ;
              s= s.concat("\t\t\t\tchart.title = \"\";\n") ;
              s= s.concat("\t\t\t\tchart.type = \"bar\";\n") ;
              s= s.concat("\t\t\t\tchart.debug = false;\n") ;
              s= s.concat("\t\t\t\tchart.showLegend = true;\n") ;
              s= s.concat("\t\t\t\tchart.showValues = true;\n") ;
              s= s.concat("\t\t\t\tchart.setData(" + chartData + ");\n") ;
              s= s.concat("\t\t\t\tchart.render();\n") ;
              s= s.concat("\t\t\t}\n") ;
        }
          s= s.concat("\t\t</script>\n\n") ;

          s = s.concat("\t<!-- The HTML5 Shim for older browsers (mostly older versions of IE). -->\n" +
          "\t<!--[if IE]>\n" +
          "\t<script src=\"http://html5shiv.googlecode.com/svn/trunk/html5.js\"></script>\n" +
          "\t<![endif]-->\n" +
          "\t\n" +
          "   <script type=\"text/javascript\" src=\"http://gltutors.com/Wayang/scripts/jquery-1.6.1.min.js\"></script>\n" +
          "    <script type=\"text/javascript\" src=\"http://gltutors.com/Wayang/jquery.nivo.slider.pack.js\"></script>\n" +
          "    <script type=\"text/javascript\">\n" +
          "    $(window).load(function() {\n" +
          "        $('#slider').nivoSlider();\n" +
          "    });\n" +
          "    </script>\n" +
          "\t\n" +
          "\n" +
          "\t<!-- End JQuery and Scripts-->\n" +
          "</head>\n" +
          "\n" +
          "<body onload=\"" ) ;

          for (TopicMastery t: topicMasteries) {

            String chartData = "chartData" + t.getTopic().getId() ;
            String masteryChartName = "masteryChart" + t.getTopic().getId() ;

             s=s.concat("initChart" + masteryChartName + "();") ;
          }
          s=s.concat("\">\n\n") ;   //finish body label

          s=s.concat("<div class=\"gradient_bg\">\n") ;
          s=s.concat("\n" );
          s=s.concat("<header><!-- Start Header -->\n" );
          s=s.concat("\t\t\n" );
          s=s.concat("\t\t\n" );
          s=s.concat("\t\t<!-- Start Header Middle --><div id=\"header_wrapper_small\"><!-- BEGIN HEADER MAIN -->\n" );
          s=s.concat(" <!--Start Logo--><header  id=\"wayang_logo_small\">\n" );
          s=s.concat("    Wayang Outpost\n" );
          s=s.concat("    \n" );
          s=s.concat("  </header><!--End Logo-->\n" );
          s=s.concat("  <subheader id=\"subheader_small\">My Progress</subheader><!--End Logo-->\n" );
          s=s.concat("\t\t\t\n" );

         /*
          s=s.concat("               <!-- Start Navigation -->\n" );
          s=s.concat("\t\t\t<topNav class=\"topNav_small\">\n" );
          s=s.concat("\t\t\t\t\t\t<ul>\t\t\t\t\t\t\n" );
          s=s.concat("\t\t\t\t\t\t\n" );
          s=s.concat("\t\t\t\t\t\t\t<li><a href=\"#\">Log out</a> |&nbsp;&nbsp;</li>\n" ) ;
          String learningCompanion =   smgr.getPedagogicalModel().getLearningCompanion() != null ? smgr.getPedagogicalModel().getLearningCompanion().getCharactersName()
                  : "none";
          s=s.concat("                            <li> " +
                  "<a href=" + this.getBackToVillageURL() + "&learningHutChoice=false" + 
                  "&learningCompanion=" + learningCompanion +
                  ">Back to Village </a></li>\n" +
          "\t\t\t\t\t\t</ul>\n" +
          "\t\t\t\t\t</topNav_small><!-- End Navigation -->\n" +
          */

          s = s.concat("                    \n" +
          "               </div>\t\n" +
          "\n" +
          "\t</header><!-- END HEADER -->\n" +
          "  </section>\n" +
          "  <!-- END SECTION MAIN CONTENT -->\n" +
          "\n" +
          "\n" +
          "\n" +
          "<section class=\"normal_body\">\n" +
          "<div id=\"table_body\">\n" +
          "\n" +
          "\n" +
          "<table  class=\"progress_table\">\n" +
          " <thead>\n" +
          "  <tr>\n" +
          "    <td>Skill</td>\n" +
          "    <td>Performance</td>\n" +
          "    \n" +
          "   \n" +
          "    <td>Remarks</td>\n" +
          "     <td>Actions</td>\n" +
          "  </tr>\n" +
          "  </thead>\n" +
          "  \n" +
          "  \n" +
          "\n" +
          " \n") ;

    return s ;
  }

  public String getBody() {

      String s = new String("   <tbody>\n") ;
      String learningCompanion =   smgr.getPedagogicalModel().getLearningCompanion() != null ? smgr.getPedagogicalModel().getLearningCompanion().getCharactersName()
                  : "none";

      for (TopicMastery t: topicMasteries) {

          String masteryChartName = "masteryChart" + t.getTopic().getId() ;
      
          s = s.concat("  <tr>\n" ) ;
          s = s.concat("    <td>" + t.getTopic().getName()) ;

          if ( t.getMastery() > masteryThreshold )
                  s = s.concat("<p><img src=\"images/star.png\" alt=\"Topic Mastered!\" height=\"42\" width=\"42\" /></p>" ) ;

          s = s.concat("</td>\n") ;
          s = s.concat("    <td valign=\"bottom\" ><div id=\""+ masteryChartName +"\"></div>\n" ) ;
          s= s.concat("    </td>\n" ) ;
          s=s.concat("    <td></td>\n" ) ;
          s=s.concat("    <td> <ul id=\"reviewChallenge\">\n" ) ;
          s=s.concat("      <li><a href=\"" + getBackToVillageURL() + "&learningHutChoice=true&topicId="
                  + t.getTopic().getId() +
                  "&learningCompanion=" + learningCompanion +
                  "\">Continue &raquo;</a></li>") ;
          s=s.concat("      <li> <a href=\"" + getBackToVillageURL() + "&learningHutChoice=true&topicId=" +
                  + t.getTopic().getId() + "&mode=review" + 
                  "&learningCompanion=" + learningCompanion +
                  "\">&laquo; Review</a></li>") ;
          s=s.concat("      <li><a href=\"" + getBackToVillageURL() + "&learningHutChoice=true&topicId=" +
                  + t.getTopic().getId() + "&mode=challenge" +
                  "&learningCompanion=" + learningCompanion +
                  "\">Challenge &raquo;</a></li>") ;
          s=s.concat("   </ul></td></tr>\n\n" ) ;
      }
      s.concat("  </tbody>\n" ) ;
      s.concat("  </table>\n") ;

      return s ;
  }

   public String getFooterView() {
      return "</div>\n" +
      "</section>\n" +
      "\n" +
      "<footer><!-- BEGIN FOOTER -->\n" +
      "&nbsp;\n" +
      "\n" +
      "</footer><!-- END FOOTER -->\n" +
      "<!-- Free template distributed by \n" +
      "\n" +
      "http://freehtml5templates.com -->\t\n" +
      "\n" +
      "</div>\n" +
      "</body>\n" +
      "\n" +
      "</html>" ;
    }

  public String getView () throws Exception {
    return getHeaderView() + getScripts() + getBody() + getFooterView() ; 
  }

  public void setMsg_(String msg_) {
    this.msg_ = msg_;
  }
  public String getMsg_() {
    return msg_;
  }

  private String msg_;

  private String getBackToVillageURL() {
      return Settings.flashClientPath + this.client + ".swf?sessnum=" + sessionId ;
  }


}
