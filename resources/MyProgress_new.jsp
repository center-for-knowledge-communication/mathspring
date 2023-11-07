<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>
<% 
/**
* Frank 05-18-2020 Commented out obsolete code which would not compile properly
* Frank 12-18-20 Issue #336 added cache-busting for selected .js and .css files
* Kartik 04-22-21 Issue #390 Added session clock functionality
* Frank	11-27-22	Merge jchart.js inline and apply multi-lingual algorithms

*/

Locale loc = request.getLocale(); 
Locale loc1 = request.getLocale(); 
Locale loc2 = request.getLocale();




String lang = loc.getLanguage();
String country = loc.getCountry();

System.out.println("locale set to:" + lang + "-" + country );	

int pageLangIndex = 0;
String strExperiment = "";

try {
	strExperiment = (String) request.getAttribute("experiment");
	
	if (strExperiment.equals("multi-lingual")) {
		pageLangIndex = 0;
	}
	else {
		pageLangIndex = (int) request.getAttribute("pageLangIndex");
	}	
	
	if (pageLangIndex == 0) {
		if (lang.equals("en")) {
			loc1 = new Locale("en","US");	
			loc2 = new Locale("es","US");	
		}
		else {
			loc1 = new Locale("es","US");	
			loc2 = new Locale("en","US");		
		}
	}
	else {
		if (lang.equals("en")) {
			loc1 = new Locale("es","US");	
			loc2 = new Locale("en","US");	
		}
		else {
			loc1 = new Locale("en","US");	
			loc2 = new Locale("es","US");		
		}	
	}

}
catch (Exception e) {
	 System.out.println("pageLangIndex " + e.getMessage());
	 pageLangIndex = 0;
}

ResourceBundle versions = null; 
try {
	 versions = ResourceBundle.getBundle("Versions");
	 System.out.println("css_version=" + versions.getString("css_version"));
	 System.out.println("js_version=" + versions.getString("js_version"));
}
catch (Exception e) {
	 System.out.println("versions bundle ERROR");	 
}

//Locale loc = request.getLocale();
//String lang = loc.getDisplayLanguage();

//ResourceBundle rb = null;
//try {
//	rb = ResourceBundle.getBundle("MathSpring",loc);
//}
//catch (Exception e) {
//	logger.error(e.getMessage());
//}


ResourceBundle rb = null;

ResourceBundle rb1 = null;
try {
	rb1 = ResourceBundle.getBundle("MathSpring",loc1);
}
catch (Exception e) {
	System.out.println(e.getMessage());
}
ResourceBundle rb2 = null;
try {
	rb2 = ResourceBundle.getBundle("MathSpring",loc2);
}
catch (Exception e) {
	System.out.println(e.getMessage());
}

if (lang.equals("en")) {
	rb = rb1;
}
else {
	rb = rb2;	
}
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>MathSpring | <%= rb.getString("my_progress") %></title>
    <link rel="apple-touch-icon" sizes="180x180" href="apple-touch-icon.png">
    <link rel="icon" type="image/png" href="favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="manifest.json">
    <meta name="theme-color" content="#ffffff">
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/common_new.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet" type="text/css"/>
    <link href="css/MyProgress_new.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet" type="text/css"/>
    <link href="css/graph_new.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet" type="text/css"/>
    <script src="js/jquery-1.10.2.js"></script>
    
    <script>
    var experiment = "<%= strExperiment %>";
    var Chart = {
    /**
     * The graph object. Short-hand for "this". Set in the init-method.
     */
    o				: null,

    /**
     * The graph's container element. Passed as a string argument to the init-method.
     */
    container		: null,

    /**
     *
     */
    title			: "",



    /**
     * The chart type, bars or columns.
     */
    type			: null,

    /**
     * The graph data as an array.
     */
    data			: null,

    attemptsArray	:null,
    correctArray	:null,

    /**
     * The delimiter used for separating the data string passed
     * to the method setData.
     .	  */
    dataDelimiter 	: ";",




    /**
     * Initialize the graph object.
     */
    init				: function() {
        o = this;
        o.data = null;
        o.attemptsArray=null;
        o.correctArray=null;


        //o.container	= document.getElementById(containerId);


    },

    initCharts				: function(containerId) {
        o = this;
        o.data = null;
        o.attemptsArray=null;
        o.correctArray=null;


        o.container	= document.getElementById(containerId);


    },

    /**
     * Set the chart data from a delimited string in the following format:
     * "item1;value1;item2;value2".
     */
    setData				: function(problemDetailsObj) {

        o.data = problemDetailsObj.chartData.split(o.dataDelimiter);

        var i=0;
        var dataItem = o.data[i];

        if (dataItem=="mastery")
        {i++;
            problemDetailsObj.masteryLevel=o.data[i];
            dataItem = o.data[i];
            i++;
            dataItem = o.data[i];
            /*if (dataItem=="currentProblem")
             {i++; problemDetailsObj.currentProblem=o.data[i];
             dataItem = o.data[i];}*/


            if (dataItem=="p_id")
            {i++;
                problemDetailsObj.problemID=new Array();
                problemDetailsObj.totalAttempts=new Array();
                problemDetailsObj.correct=new Array();
                problemDetailsObj.hintArray=new Array();
                var j=0;

                while(i<o.data.length) {



                    problemDetailsObj.problemID[j]=o.data[i];
                    i++;
                    problemDetailsObj.totalAttempts[j]=o.data[i];
                    i++;
                    problemDetailsObj.correct[j]=o.data[i];
                    i++;



                    //fill hint array

                    var k=0;
                    dataItem = o.data[i];
                    problemDetailsObj.hintArray[j]=new Array();

                    while(dataItem<"p_id")
                    {


                        if(problemDetailsObj.totalAttempts[j]>0){
                            if (k<parseFloat(problemDetailsObj.totalAttempts[j])){
                                problemDetailsObj.hintArray[j][k]=o.data[i];

                                k++;
                            }
                        }
                        i++;
                        dataItem = o.data[i];
                    }


                    j++;i++;
                }


            }
        }

    },

    /**
     * Render the graph on screen. Appends elements to the container element.
     * This method sets some crusial style properties to the elements to make
     * the chart render correctly.
     */



renderCharts : function(problemList, c, containerId) {
		var tableClassName = "";
		var topicState = "";
		var hints = "";
		var attempts = "";
		var effortFeedback = "";

        for (var i=0; i<c; i++){

            var listElement = document.createElement("li");
            containerId.appendChild(listElement);
            var addlink= document.createElement("a");
            addlink.setAttribute('href', '#');

            var chartWrapper=document.createElement("div");
            chartWrapper.id="cardtableWrapper";
            listElement.appendChild(chartWrapper);


            var table = document.createElement("table");
            table.id = "cardTable";
            var body = document.createElement("tbody");

            topicState=problemList[i][2];
            attempts=problemList[i][3];
            hints= problemList[i][4];

            var row = document.createElement("tr");
            var cell = document.createElement("td");
            row.appendChild(cell);


            switch(topicState){
            case "empty":
                table.className ="emptyCard";
                cell.innerHTML="_";
                effortFeedback = "<%= rb.getString("topic_state_empty")%>";
                break;

            case "SKIP":
                table.className ="warningCard";
                cell.innerHTML="_";
                effortFeedback = "<%= rb.getString("topic_state_SKIP")%>";
                break;

            case "SOF":
                table.className ="correctCard";
                cell.innerHTML="★";
                effortFeedback = "<%= rb.getString("topic_state_SOF")%>";
                break;

            case "NOTR":
                table.className ="warningCard";
                cell.innerHTML="ǃ";
                effortFeedback = "<%= rb.getString("topic_state_NOTR")%>";
                break;

            case "BOTTOMOUT":
                table.className ="correctWithHintsCard";
                cell.innerHTML="H";
                effortFeedback = "<%= rb.getString("topic_state_BOTTOMOUT")%>";
                break;

            case "GIVEUP":
                table.className ="giveupCard";
                cell.innerHTML="_";
                effortFeedback = "<%= rb.getString("topic_state_GIVEUP")%>";
                break;

            case "SHINT":
                table.className ="correctWithHintsCard";
                cell.innerHTML="H";
                if (hints==1)   {
                    effortFeedback = "<%= rb.getString("topic_state_SHINT")%>";
                }else { 
                    effortFeedback = "<%= rb.getString("topic_state_SHINT2")%>";
				}

                break;

            case "ATT":
                table.className ="correctOnAttemptsCard";
                cell.innerHTML="_";
                effortFeedback = "<%= rb.getString("topic_state_ATT")%> " +  attempts + " <%= rb.getString("topic_state_ATT2")%>";
                break;

            case "GUESS":
                table.className ="correctOnAttemptsCard";
                cell.innerHTML="_";
                effortFeedback = "<%= rb.getString("topic_state_ATT")%> " +  attempts + " <%= rb.getString("topic_state_ATT2")%>";
                break;

            default:
                table.className ="emptyCard";
                cell.innerHTML="_";
                effortFeedback = "<%= rb.getString("topic_state_empty")%>";
                break;


            }


            problemList[i][5]=effortFeedback;

            body.appendChild(row);
            table.appendChild(body);
            chartWrapper.appendChild(table);
        }


    },



    renderMastery				: function(masteryDiv, masteryValue) {

        //o.drawMastery	(parseFloat(problemDetailsObj.masteryLevel), jpt);
        o.drawMastery	(masteryDiv, masteryValue);

    },


    getColumns			: function() {


        // Iterate the data array and create columns for each data item.
        var c=0;

        while(c<problemDetailsObj.problemID.length) {



            var listElement = document.createElement("li");
            var addlink= document.createElement("a");
            addlink.setAttribute('href', '#');

            var columnElement = o.getColumn(problemDetailsObj,c);
            addlink.appendChild(columnElement);


            // Append each cell to the row.
            listElement.appendChild(addlink);
            o.container.appendChild(listElement);
            c++;
        }



    },

    /**
     * Generates a column representing a data item.
     * Returns the element.
     */
    getColumn			: function(problemDetailsObj,c) {
        // Create the actual column and set the hight according to the data item's value.
        var chartWrapper=document.createElement("div");
        chartWrapper.id="cardtableWrapper";



        var table = document.createElement("table");
        table.id = "cardTable";
        var body = document.createElement("tbody");

        if (problemDetailsObj.totalAttempts[c]=="0"){
            table.className = "empty_table";
            var row = document.createElement("tr");
            var cell = document.createElement("td");
            row.appendChild(cell);
            cell.innerHTML="_";
            body.appendChild(row);
            table.appendChild(body);
        }

        else if (problemDetailsObj.totalAttempts[c]>"0" && problemDetailsObj.correct[c]=="0"){
            table.className = "giveup_table";
            var row = document.createElement("tr");
            var cell = document.createElement("td");
            row.appendChild(cell);
            cell.innerHTML="_";
            body.appendChild(row);
            table.appendChild(body);
        }

        else if (problemDetailsObj.totalAttempts[c]>"1" && problemDetailsObj.correct[c]=="1"){
            table.className = "correctOnAttempts_table";
            var row = document.createElement("tr");
            var cell = document.createElement("td");
            row.appendChild(cell);
            cell.innerHTML="_";
            body.appendChild(row);
            table.appendChild(body);
        }

        else if (problemDetailsObj.totalAttempts[c]=="1" && problemDetailsObj.correct[c]=="1" && problemDetailsObj.hintArray[c][0]==0){

            table.id = "correct_table";
            var row = document.createElement("tr");
            var cell = document.createElement("td");
            row.appendChild(cell);
            cell.innerHTML="★";
            body.appendChild(row);
            table.appendChild(body);

        }

        else if (problemDetailsObj.totalAttempts[c]=="1" && problemDetailsObj.correct[c]=="8" && problemDetailsObj.hintArray[c][0]==1){


            table.id = "hint_table";
            var row = document.createElement("tr");
            var cell = document.createElement("td");
            row.appendChild(cell);
            cell.innerHTML="H";
            body.appendChild(row);
            table.appendChild(body);

        }

        //else if (problemDetailsObj.totalAttempts[c]=="12"){
        else if (problemDetailsObj.correct[c]=="12"){

            table.id = "warning_table";
            var row = document.createElement("tr");
            var cell = document.createElement("td");
            row.appendChild(cell);
            cell.innerHTML="ǃ";
            body.appendChild(row);
            table.appendChild(body);

        }
        else
        {
            table.style.fontSize="12px";
            table.style.fontFamily="Arial";

            for (j=0;j<4;j++) {
                var row = document.createElement("tr");



                var cell;

                cell = document.createElement("td");

                //coloring and styling starts


                row.id="incorrectCell";




                if (problemDetailsObj.correct[c]=="1"){
                    if(parseFloat(problemDetailsObj.totalAttempts[c])<=j+1)
                    { row.id="correctCell";
                    }
                    if(j==parseFloat(problemDetailsObj.totalAttempts[c])-1) row.id="first_correctCell";
                }



                //coloring and styling ends

                if (parseFloat(problemDetailsObj.hintArray[c][j])>0){

                    var hintCanvas = document.createElement('canvas');
                    hintCanvas.id="hintCircleCanvas";
                    cell.appendChild(hintCanvas);

                    var hintCircle = hintCanvas .getContext('2d');


                    hintCircle.fillStyle="#0244CF";

                    hintCircle.beginPath();
                    hintCircle.arc(50,90,40,0,2*Math.PI,false);
                    hintCircle.closePath();
                    hintCircle.fill();

                    if (parseFloat(problemDetailsObj.hintArray[c][j])>1){


                        hintCircle.beginPath();
                        hintCircle.arc(150,90,40,0,2*Math.PI,false);
                        hintCircle.closePath();
                        hintCircle.fill();

                        if (parseFloat(problemDetailsObj.hintArray[c][j])>2){


                            if (parseFloat(problemDetailsObj.hintArray[c][j])==3){

                                hintCircle.beginPath();
                                hintCircle.arc(250,90,40,0,2*Math.PI,false);
                                hintCircle.closePath();
                                hintCircle.fill();
                            }
                            else
                            {
                                hintCircle.strokeStyle = "#0244CF";
                                hintCircle.lineWidth="23";

                                hintCircle.beginPath();

                                hintCircle.arc(250,90,38,0,2*Math.PI,true);
                                hintCircle.closePath();
                                hintCircle.stroke();

                            }}}




                }


                cell.align = "middle";
                cell.vAlign = "bottom";
                cell.style.height = "20%";
                cell.style.width = "20%";
                row.appendChild(cell);

                // Append the row to the table
                body.appendChild(row);
            }
            table.appendChild(body);

        }

        chartWrapper.appendChild(table);

        return chartWrapper;
    },





    drawMastery			: function(masteryDiv, masteryLevel) {
        plotArea = document.createElement("div");
        plotArea.id = "plotArea";
        plotArea.className = "plotArea";


        // Create the actual bar and set the width according to the data item's value.
        var bar;
        bar = document.createElement("div");
        bar.className = "barArea";
        bar.style.width = ((masteryLevel ) * 100) + "%";

        var masteryRounded=Math.round(masteryLevel*100 );

        var valueElement = o.getBarValue(masteryRounded);
        bar.appendChild(valueElement);

        plotArea.appendChild(bar);


        var masteryDiv = document.getElementById(masteryDiv);
        masteryDiv.appendChild(plotArea);
    },

    getBarValue		: function(dataItemValue) {
        // Create the value text associated with the data item.
        var value;
        value = document.createElement("div");
        value.className = "barValue";
        value.innerHTML = dataItemValue;

        return value;
    },

    giveFeedbackAndPlant : function(remarksDiv,plantDiv,topicState,studentState_disengaged,topicMastery,problemsDoneWithEffort,SHINT_SOF_sequence,SOF_SOF_sequence,neglectful_count,problemsDone,problemsSolved) {

        //identify topic_pepper_state

        var topicState_pepperPlant;
        topicState_pepperPlant = "";
        var pepperPlant = "";

        if (problemsDone>0){

            if (topicMastery>=.88)  {
                if (SHINT_SOF_sequence >= 3)  pepperPlant = "rainbowPepper";
                else if (SHINT_SOF_sequence > 0 && SHINT_SOF_sequence < 3) pepperPlant = "masteryPepper_bonusPeppers";
                else if (SOF_SOF_sequence >=3)  pepperPlant = "monsterPepper";
                else if (problemsDoneWithEffort >=8)  pepperPlant = "bigPepper";
                else pepperPlant="masteryPepper";

                topicState_pepperPlant= topicState+"_"+pepperPlant;

            }


        else if (topicMastery >= .75) {
            if (neglectful_count >= 3) {
                pepperPlant = "youngPepper_wilt";
                topicState_pepperPlant = "disengagedCloseToMastery_youngPepper_wilt";
            }
            else if (SHINT_SOF_sequence == 0) {
                pepperPlant = "flowerPepper";
                topicState_pepperPlant = "closeToMastery_flowerPepper";
            }
            else if (SHINT_SOF_sequence == 1) {
                pepperPlant = "flowerPepper_bonusFlowers";
                topicState_pepperPlant = "closeToMastery_flowerPepper_bonusFlowers";
            }
            else if (SHINT_SOF_sequence > 1) {
                pepperPlant = "flowerPepper_moreBonusFlowers";
                topicState_pepperPlant = "closeToMasteryGoodHelpUsage_flowerPepper_moreBonusFlowers";
            }
        }


        else if (problemsSolved == 1) {
            pepperPlant = "babyPepper";
            topicState_pepperPlant = "babyTopicJustStarted_babyPepper";
        }

        else if (problemsDoneWithEffort  <= 1) {
                pepperPlant = "babyPepper";
                topicState_pepperPlant = "babyTopic_babyPepper";
        }

        else if (problemsDoneWithEffort < 4) {
            if (neglectful_count >= 2) {
                pepperPlant = "toddlerPepper_wilt";
                topicState_pepperPlant = "toddlerTopic_toddlerPepper_wilt";
            }

            else {
                pepperPlant = "toddlerPepper";
                topicState_pepperPlant = "toddlerTopic_toddlerPepper";
            }
        }
        else if (problemsDoneWithEffort < 6) {
            if (neglectful_count >= 3) {
                pepperPlant = "adolescentPepper_wilt";
                topicState_pepperPlant = "adolescentTopic_adolescentPepper_wilt";
            }
            else if (SHINT_SOF_sequence = 1) {
                pepperPlant = "adolescentPepper_withFlowers";
                topicState_pepperPlant = "adolescentTopic_adolescentPepper_withFlowers";
            }
            else if (SHINT_SOF_sequence > 1) {
                pepperPlant = "adolescentPepper_withMoreFlowers";
                topicState_pepperPlant = "adolescentTopicGoodHelpUsage_adolescentPepper_withMoreFlowers";
            }
            else {
                pepperPlant = "adolescentPepper";
                topicState_pepperPlant = "adolescentTopic_adolescentPepper";
            }

        }

        else if (problemsDoneWithEffort >= 6) {
            if (neglectful_count >= 3) {
                pepperPlant = "youngPepper_wilt";
                topicState_pepperPlant = "youngTopic_adolescentPepper_wilt";
            }
            else if (SHINT_SOF_sequence = 1) {
                pepperPlant = "youngPepper_withFlowers";
                topicState_pepperPlant = "youngTopic_youngPepper_withFlowers";
            }
            else if (SHINT_SOF_sequence > 1) {
                pepperPlant = "youngPepper_withMoreFlowers";
                topicState_pepperPlant = "youngTopicGoodHelpUsage_youngPepper_withMoreFlowers";
            }

            else  pepperPlant = "youngPepper";


        }
        }

        if (remarksDiv != null) {
            this.giveFeedback(remarksDiv, topicState, topicState_pepperPlant, topicMastery, studentState_disengaged);
        }
        this.givePlants(plantDiv, pepperPlant);
    },

giveFeedback	: function(remarksDiv, topic_state,topicState_pepperPlant,topicMastery, neglectful_count,studentState_disengaged) {

	
	var feedbackText="";

        //empty state

        if (topic_state=="topicEmpty") feedbackText += "<%= rb.getString("topic_state_topic_empty")%>"; 

        // correct for the first time

        if (topic_state=="correctForTheFirstTime") feedbackText += "<%= rb.getString("correctForTheFirstTime")%>";
        if (topic_state=="correctForTheFirstTime_goodHelpUsage") feedbackText += "<%= rb.getString("correctForTheFirstTime_goodHelpUsage")%>";


        // baby topic just started

        else if (topicState_pepperPlant=="babyTopicJustStarted_babyPepper" )  feedbackText += "<%= rb.getString("babyTopicJustStarted_babyPepper")%>";
        // baby topic

        else if (topicState_pepperPlant=="babyTopic_babyPepper" ) {

            if (topic_state=="SHINT") feedbackText += "<%= rb.getString("babyTopic_babyPeppe_SHINT")%>";
            else if (topic_state=="NOTR")    feedbackText += "<%= rb.getString("babyTopic_babyPepper_NOTR")%>";
            else if (topic_state=="SOF") feedbackText += "<%= rb.getString("babyTopic_babyPepper_SOF")%>";
            else feedbackText += "<%= rb.getString("babyTopic_babyPepper")%>";
        }


        // toddler topics

        else if (topicState_pepperPlant== "toddlerTopic_toddlerPepper") feedbackText =  "<%= rb.getString("toddlerTopic_toddlerPepper")%>";
        else if (topicState_pepperPlant== "toddlerTopic_toddlerPepper_wilt") feedbackText =  "<%= rb.getString("toddlerTopic_toddlerPepper_wilt")%>";

        // just mastered states
 
        else if (topicState_pepperPlant=="justMastered_rainbowPepper") feedbackText =  "<%= rb.getString("justMastered__rainbowPepper")%>";
             
        else if (topicState_pepperPlant=="justMastered_masteryPepper_bonusPeppers") feedbackText =  "<%= rb.getString("justMastered_masteryPepper_bonusPeppers")%>";

        else if (topicState_pepperPlant=="justMastered_monsterPepper") feedbackText =  "<%= rb.getString("justMastered__monsterPepper")%>";

        else if (topicState_pepperPlant=="justMastered_bigPepper") feedbackText =  "<%= rb.getString("justMastered_bigPepper")%>";

        else if (topicState_pepperPlant=="justMastered_masteryPepper") feedbackText =  "<%= rb.getString("justMastered_masteryPepper")%>";

        //in mastery states
 
 
        else if (topicState_pepperPlant=="inMastery_rainbowPepper") feedbackText = "<%= rb.getString("inMastery_rainbowPepper")%>";

        else if (topicState_pepperPlant=="inMastery_masteryPepper_bonusPeppers") feedbackText ="<%= rb.getString("inMastery_masteryPepper_bonusPeppers")%>";

        else if (topicState_pepperPlant=="inMastery_monsterPepper") feedbackText = "<%= rb.getString("inMastery_monsterPepper")%>";

        else if (topicState_pepperPlant=="inMastery_bigPepper") feedbackText = "<%= rb.getString("inMastery_bigPepper")%>";

        else if (topicState_pepperPlant=="inMastery_masteryPepper") feedbackText = "<%= rb.getString("inMastery_masteryPepper")%>";

        //remastered states

        else if (topicState_pepperPlant=="reMasteredWithGoodHelpUsage_rainbowPepper") feedbackText = "<%= rb.getString("reMasteredWithGoodHelpUsage_rainbowPepper")%>";
        else if (topicState_pepperPlant=="reMasteredWithGoodHelpUsage_masteryPepper_bonusPeppers") feedbackText = "<%= rb.getString("reMasteredWithGoodHelpUsage_masteryPepper_bonusPeppers")%>";
        else if (topicState_pepperPlant=="reMasteredSolvingLotOfProblems_bigPepper") feedbackText = "<%= rb.getString("reMasteredSolvingLotOfProblems_bigPepper")%>";
        
        //close to mastery(mastery>75) states

      
        else if (topicMastery >= 75)

        {
            feedbackText += "<%= rb.getString("closeToMastery")%>";

            if (topicState_pepperPlant=="disengagedCloseToMastery_youngPepper_wilt")  feedbackText += "<%= rb.getString("disengagedCloseToMastery_youngPepper_wilt")%>";
            else if (topicState_pepperPlant=="closeToMastery_flowerPepper_bonusFlowers")  feedbackText +="";
            else if (topicState_pepperPlant=="closeToMasteryGoodHelpUsage_flowerPepper_moreBonusFlowers")  feedbackText += "<%= rb.getString("closeToMasteryGoodHelpUsage_flowerPepper_moreBonusFlowers")%>";
            else if (topicState_pepperPlant=="closeToMastery_flowerPepper")  feedbackText+="";

            if (topic_state=="inProgress") feedbackText += "<%= rb.getString("inProgressMastery")%>";
            else if (topic_state=="inProgress_goodHelpUsage") feedbackText += "<%= rb.getString("inProgress_goodHelpUsage")%>";
            else if (topic_state=="SHINT") feedbackText += "<%= rb.getString("inProgress_SHINT")%>";


        }
        //adolescent topic
 
        else if (topicState_pepperPlant=="adolescentTopicGoodHelpUsage_adolescentPepper_withMoreFlowers") feedbackText += "<%= rb.getString("adolescentTopicGoodHelpUsage_adolescentPepper_withMoreFlowers")%>";
        else if ((topicState_pepperPlant=="adolescentTopic_adolescentPepper") || (topicState_pepperPlant=="adolescentTopic_adolescentPepper_withFlowers"))
        {
            if (topic_state=="inProgress") feedbackText += "<%= rb.getString("inProgressAdolescent")%>";
            else if (topic_state=="inProgress_goodHelpUsage") feedbackText += "<%= rb.getString("inProgress_goodHelpUsageAdolescent")%>";
            else if (topic_state=="SHINT") feedbackText += "<%= rb.getString("Adolescent_SHINT")%>";
        }
        else if (topicState_pepperPlant=="adolescentTopic_adolescentPepper_wilt") feedbackText += "<%= rb.getString("adolescentTopic_adolescentPepper_wilt")%>";

        //young topic

        else if (topicState_pepperPlant=="youngTopicGoodHelpUsage_youngPepper_withMoreFlowers") feedbackText += "<%= rb.getString("youngTopicGoodHelpUsage_youngPepper_withMoreFlowers")%>";

        else if ((topicState_pepperPlant=="youngTopic_youngPepper") || (topicState_pepperPlant=="youngTopic_youngPepper_withFlowers"))
        {
            if (topic_state=="inProgress") feedbackText += "<%= rb.getString("inProgressYoung")%>";
            else if (topic_state=="inProgress_goodHelpUsage") feedbackText += "<%= rb.getString("inProgress_goodHelpUsageYoung")%>";
            else if (topic_state=="SHINT") feedbackText += "<%= rb.getString("Young_SHINT")%>";  
        }
        else if (topicState_pepperPlant=="youngTopic_adolescentPepper_wilt") feedbackText += "<%= rb.getString("youngTopic_adolescentPepper_wilt")%>";

        //
        if (topic_state=="ATT_hardProblem") feedbackText += "<%= rb.getString("ATT_hardProblem")%>";

        else if (topic_state=="GIVEUP_hardProblem") feedbackText += "<%= rb.getString("GIVEUP_hardProblem")%>";

        //disengaged behavior

        if (neglectful_count>=2){


        if (topic_state=="NOTR") feedbackText += "<%= rb.getString("NOTR_feedback")%>";

        else if (topic_state=="GUESS_helpAvoidance") feedbackText += "<%= rb.getString("GUESS_helpAvoidance")%>";

        else if (topic_state=="BOTTOMOUT_helpMisuse") feedbackText += "<%= rb.getString("BOTTOMOUT_helpMisuse")%>";

        if (studentState_disengaged==true) feedbackText += "<%= rb.getString("studentState_disengaged")%>";
        }

        document.getElementById(remarksDiv).innerHTML=feedbackText;

    },




    givePlants: function(plant_div, pepperPlant) {

        var plantImage = document.createElement("IMG");
        if (pepperPlant.length!=0){plantImage.src="img/pp/"+pepperPlant+".png" ;
        if (document.getElementById(plant_div) == null)
            console.log(plant_div);
        else
            document.getElementById(plant_div).prepend(plantImage);


        }

    },




    problemsDone: function(problemsDone_div,problemsDone,totalProblems) {
    	
		var probsdone =  "<%= rb.getString("problems_done")%>"

        document.getElementById(problemsDone_div).innerHTML= probsdone + " : " + problemsDone + "/"+totalProblems ;
    }

}


///extended functions

$.extend({
    getUrlVars: function(){
        var vars = [], hash;
        var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
        for(var i = 0; i < hashes.length; i++)
        {
            hash = hashes[i].split('=');
            vars.push(hash[0]);
            vars[hash[0]] = hash[1];
        }
        return vars;
    },
    getUrlVar: function(name){
        return $.getUrlVars()[name];
    }
});
    
    
    </script>
    
    <script type="text/javascript" src="js/tutorutils.js"></script>
    
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/jquery.jqplot.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/plugins/jqplot.canvasTextRenderer.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/plugins/jqplot.pieRenderer.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/plugins/jqplot.enhancedPieLegendRenderer.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/jqplot2021/plugins/jqplot.highlighter.js" />"></script>
    
    <script>
        $.extend({
            getUrlVars: function () {
                var vars = [], hash;
                var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
                for (var i = 0; i < hashes.length; i++) {
                    hash = hashes[i].split('=');
                    vars.push(hash[0]);
                    vars[hash[0]] = hash[1];
                }
                return vars;
            },
            getUrlVar: function (name) {
                return $.getUrlVars()[name];
            }
        });

        // These two vars are not initialized correctly in the initiate method below.   They appear to want them from URL params
        // but this JSP isn't getting URL params.   It is a pure forward done in servlet, so these values must be set as attributes
        // TODO find out if anyone is calling this JSP with URL params.  Perhaps this can be done in servlet forward??
        var currentTopicId=${topicId};
        var currentProblemId=${probId};
        var elapsedTime=0;
        var probElapsedTime=0;
        var mppElapsedTime=0;
        var startClockTime = 0;
        var startElapsedTime=0;
        var problemsDoneWithEffort=2;
        var useHybridTutor =${useHybridTutor};
 
        var effort_legend_labels = ["<%= rb.getString("sof_tooltip") %>", "<%= rb.getString("att_tooltip") %>",   "<%= rb.getString("shint_tooltip") %>", "<%= rb.getString("shelp_tooltip") %>",  "<%= rb.getString("guess_tooltip") %>",   "<%= rb.getString("notr_tooltip") %>",  "<%= rb.getString("skip_tooltip") %>", "<%= rb.getString("giveup_tooltip") %>"];
		// Note; colors are in reverse order
        var effort_series_colors = ['#bebada','#8dd3c7', '#ffffb3', '#fb8072', '#fdb462', '#80b1d3', '#9beb94',  '#26f213'];

        var globals = {
                mouseSaveInterval: ${mouseSaveInterval},
                mouseHistory: [],
                sessionId: ${sessionId},
                timeInSession: ${timeInSession}
            }

        var sysGlobals = {
            gritServletContext: '${gritServletContext}',
            wayangServletContext: '${wayangServletContext}',
            gritServletName: '${gritServletName}'
        }

        function initiate() {
            startElapsedTime= ($.getUrlVar('elapsedTime'))*1;
            probElapsedTime =  ($.getUrlVar('probElapsedTime'))*1;
            var d = new Date();
            startClockTime  = d.getTime();
        }

        function updateElapsedTime() {
            var d =new Date();
            var now = d.getTime();
            mppElapsedTime = now-startClockTime ;
            elapsedTime = startElapsedTime + mppElapsedTime;
            return  elapsedTime;
        }

        function renderProgressPage() {
            <c:forEach var="ts" items="${topicSummaries}">
            var topicState="${ts.topicState}";
            var topicId= ${ts.topicId};
            var topicMastery= ${ts.mastery};
            var problemsDone= ${ts.problemsDone};
            var problemsSolved = ${ts.numProbsSolved};
            var totalProblems= ${ts.totalProblems};
            var problemsDoneWithEffort= ${ts.problemsDoneWithEffort};
            var SHINT_SOF_sequence= ${ts.SHINT_SOF_sequence};
            var SOF_SOF_sequence= ${ts.SOF_SOF_sequence};
            var neglectful_count= ${ts.neglectful_count};
            var studentState_disengaged=false;
            var chart = Chart;
            
            chart.init();
            chart.renderMastery("masteryChart_"+topicId,topicMastery,problemsDone);
            chart.problemsDone("problemsDone_"+topicId,problemsSolved,totalProblems);
            chart.giveFeedbackAndPlant ("remarks_"+topicId,"plant_"+topicId,topicState,studentState_disengaged,topicMastery,problemsDoneWithEffort,SHINT_SOF_sequence,SOF_SOF_sequence,neglectful_count,problemsDone,problemsSolved);

//			console.log(topicId);
            var effortsForGraph = "${ts.effortsForGraph}";
            if (effortsForGraph.length == 0) {
            	return;
            }
//			console.log(effortsForGraph);
			var effortValueArr = effortsForGraph.split(',');
			
    		var line = [];
  			
  			for (j=7;j>=0;j = j - 1) {
  				var eff = [];
	  			eff.push(effort_legend_labels[j],Number(effortValueArr[j]));
//	  			console.log(effort_legend_labels[j] + " " + effortValueArr[j]);
	  			line.push(eff);
  			}
			var canvasName = 'pie_' + topicId;
			
			plot_live_dashboard = $.jqplot(canvasName, [line], {
		    seriesDefaults: {
              renderer: $.jqplot.PieRenderer,
		      rendererOptions: {
		        showDataLabels: true,
			    startAngle: -90,
			    padding: 10,
		        sliceMargin: 4,
			    seriesColors: effort_series_colors
		      },
		    },
//		    legend:{
//	            show:true, 
//	            location:'e',
//	            fontSize: '8pt'
//	        },
		    highlighter: {
		        show: true,
		        useAxesFormatters: false,
	            location:'n',
	            fontSize: '12pt',
		        tooltipFormatString: '%s%2d'

		      }
		 
		  });
            </c:forEach>
        }

//        function challengeComplete (topicId) {
//            window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&topicId=' +topicId+ '&eventCounter=${eventCounter + 1}&mode=challenge&elapsedTime='+updateElapsedTime()+'&learningChoice=true&learningCompanion=${learningCompanion}&var=b';
//        }

//        function tryThisComplete (topicId) {
//            window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&topicId=' +topicId+ '&eventCounter=${eventCounter + 1}&mode=continue&elapsedTime='+updateElapsedTime()+'&learningChoice=true&learningCompanion=${learningCompanion}&var=b';
//        }

//        function continueComplete (topicId) {
//            window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&topicId=' +topicId+ '&eventCounter=${eventCounter + 1}&mode=continue&elapsedTime='+updateElapsedTime()+'&learningChoice=true&learningCompanion=${learningCompanion}&var=b';
//        }

//        function reviewComplete (topicId) {
//            window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&topicId=' +topicId+ '&eventCounter=${eventCounter + 1}&mode=review&elapsedTime='+updateElapsedTime()+'&learningChoice=true&learningCompanion=${learningCompanion}&var=b';
//        }

//        function returnToHutComplete () {
//            window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&elapsedTime='+ updateElapsedTime()+'&learningCompanion=${learningCompanion}&var=b';
//        }

        function addComments() {
            <c:forEach var="ts" items="${topicSummaries}">
            $("#commentLink_${ts.topicId}").click(function(){
                $.get("TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId="+currentTopicId+"&studentAction=commentClicked&comment=");
                currentTopicId=${ts.topicId};
                var position = $("#commentLink_${ts.topicId}").position();

                var tPosX = position.left ;
                var tPosY = position.top+$("#commentLink_${ts.topicId}").height();
                $("#commentHolder").css({top:tPosY, left: tPosX}).slideDown("slow");
                document.commentForm.commentTextArea.focus();
                document.commentForm.commentTextArea.value="";
            });

            $("#plantLink_${ts.topicId}").click(function(){
                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId="+currentTopicId+"&studentAction=plantClicked&var=b&comment=");
                problemsDoneWithEffort=${ts.problemsDoneWithEffort};
                var position = $("#plantLink_${ts.topicId}").position();
                var tPosX = position.left +130;
                var tPosY = position.top;

                $("#plantCommentHolder").css({top:tPosY, left: tPosX}).fadeIn("slow");
                document.plantCommentForm.commentTextArea.focus();
                document.plantCommentForm.commentTextArea.value="";

                $("#plantDetails").text("In this topic, you have solved "+problemsDoneWithEffort+" problems without guessing or getting bottom out hint.");
                $("#plantDetails").append("<br/>This plant will grow bigger as you put more effort on solving the problems.   <br/> <br/> You can leave your comment:");

            });

            $("#masteryBar_${ts.topicId}").click(function(){
                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId="+currentTopicId+"&studentAction=masteryBarClicked&var=b&comment=");

                var position = $("#masteryBar_${ts.topicId}").position();
                var tPosX = position.left +266;
                var tPosY = position.top-26;

                $("#masteryBarCommentHolder").css({top:tPosY, left: tPosX}).fadeIn("slow");
                document.masteryBarCommentForm.commentTextArea.value="";

                for (var i=0; i < document.masteryBarCommentForm.userChoice.length; i++)
                {
                    document.masteryBarCommentForm.userChoice[i].checked=false;
                }
            });

            $("#continue_${ts.topicId}").click(function(){
//                if (useHybridTutor)
                    window.location = "${pageContext.request.contextPath}/TutorBrain?action=MPPContinueTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=continue&var=b&comment=";
//                else $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPContinueTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=continue&var=b&comment=",continueComplete(${ts.topicId})
            });

            $("#review_${ts.topicId}").click(function(){
//              if (useHybridTutor)
                    window.location = "${pageContext.request.contextPath}/TutorBrain?action=MPPReviewTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=review&var=b&comment=";
//                else $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPReviewTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=review&var=b&comment=",reviewComplete(${ts.topicId})
            });

            $("#challenge_${ts.topicId}").click(function(){
//                if (useHybridTutor)
                    window.location = "${pageContext.request.contextPath}/TutorBrain?action=MPPChallengeTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=challenge&var=b&comment=";
//                else $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPChallengeTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=challenge&var=b&comment=", challengeComplete(${ts.topicId})
            });

            $("#tryThis_${ts.topicId}").click(function(){
//                if (useHybridTutor)
                    window.location = "${pageContext.request.contextPath}/TutorBrain?action=MPPContinueTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=tryThis&var=b&comment=";
//                else $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPContinueTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=tryThis&var=b&comment=",tryThisComplete(${ts.topicId})
            });
            </c:forEach>
        }

        $(document).ready(function() {

	        var changeBtn = document.getElementById('changeLanguageButton');
	    	
	       	if (experiment.indexOf('multi-lingual') < 0) {
	       		changeBtn.style.display = 'none';
	       	}
	       	else {
	       		changeBtn.style.display = 'block';       		
	       	}

            // Set up mouse tracking if the mouseSaveInterval is positive (our indicator that mouse tracking is desired)
            if (globals.mouseSaveInterval > 0)
                setInterval(sendMouseData, 1000 * globals.mouseSaveInterval); // send mouse data to server every # of seconds
            // Only set up event listeners on the body of the page if the mouseSaveInterval is positive (our indication that mouse tracking is desired)
            if (globals.mouseSaveInterval > 0) {
                $("body").mousemove(function (e) {
                    // var $body = $('body');
                    // var offset = $body.offset();
                    // var x = e.clientX - offset.left;
                    // var y = e.clientY - offset.top;
                    var x = e.pageX;
                    var y = e.pageY;
                    // console.log(e.pageX +  "," + e.pageY);
                    saveMouse(x, y);

                });

                $("body").bind('click', function (e) {
                    var x = e.pageX;
                    var y = e.pageY;
                    saveMouseClick(x, y);
                    // console.log(e.pageX +  "," + e.pageY);
                });
            }

            initiate() ;
            renderProgressPage();
            addComments();
            startSessionClock(globals.timeInSession);

            $("#searchlink").click(function(){
                $(".dropdown_contentBox").toggle();
            });

            $(".closeWindow").click(function(){
                this.parent().hide();
            });

            $("#submitCommentButton").click(function(){
                var comment = document.commentForm.commentTextArea.value;
                $.get("TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId="+currentTopicId+"&eventCounter=${eventCounter + 1}&studentAction=saveComment&var=b&comment="+comment);
                $("#commentHolder").slideUp("fast");

                if (comment!=""  ) {
                    var position = $("#commentHolder").position();
                    var tPosX = position.left;
                    var tPosY = position.top+30;
                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).fadeIn(800);
                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).delay(2000).fadeOut(800);
                }
            });

            $("#cancelCommentButton").click(function(){
                document.commentForm.commentTextArea.value="";
                $("#commentHolder").slideUp("fast");
            });

            $("#submitPlantCommentButton").click(function(){
                var comment = document.plantCommentForm.commentTextArea.value;
                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId="+currentTopicId+"&studentAction=savePlantComment&var=b&comment="+comment);
                $("#plantCommentHolder").fadeOut("slow");

                if (comment!="" ) {
                    var position = $("#plantCommentHolder").position();
                    var tPosX = position.left;
                    var tPosY = position.top+40;

                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).fadeIn(400);
                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).delay(2000).fadeOut(800);
                }
            });

            $("#cancelPlantCommentButton").click(function(){
                document.plantCommentForm.commentTextArea.value="";
                $("#plantCommentHolder").fadeOut("slow");
            });

            $("#cancelMasteryBarCommentButton").click(function(){
                document.masteryBarCommentForm.commentTextArea.value="";
                $("#masteryBarCommentHolder").fadeOut("slow");
            });

            $("#cancelMasteryFeedbackButton").click(function(){
                document.performanceFeedbackForm.commentTextArea.value="";
                $("#performanceReadMore").fadeOut("slow").slideUp(300);
            });

            $("#cancelProgressFeedbackButton").click(function(){
                document.progressFeedbackForm.commentTextArea.value="";
                $("#progressReadMore").fadeOut("slow").slideUp(300);
            });

            $("#cancelRemarksFeedbackButton").click(function(){
                document.remarksFeedbackForm.commentTextArea.value="";
                $("#remarksReadMore").fadeOut("slow").slideUp(300);
            });

            $("#backToVillage").click(function(){
                updateElapsedTime();
//                if (useHybridTutor)
                    window.location =  "${pageContext.request.contextPath}/TutorBrain?action=MPPReturnToHut&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=" +currentTopicId+"&probId=" + currentProblemId +
                        "&studentAction=backToSatHut&elapsedTime="+elapsedTime+ "&probElapsedTime="+probElapsedTime+"&learningCompanion=${learningCompanion}"+"&var=b";
//                else
//                    $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPReturnToHut&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId="+currentTopicId+"&studentAction=backToSatHut&var=b&comment=",returnToHutComplete);
            });

            $("#Performance").click(function(){
                var position = $("#Performance").position();
                var tPosX = position.left-80;
                var tPosY = position.top+26;

                $("#performanceReadMore").css({top:tPosY, left: tPosX}).slideDown("slow").fadeIn(800);
                for (var i=0; i < document.performanceFeedbackForm.userChoice.length; i++)
                {
                    document.performanceFeedbackForm.userChoice[i].checked=false;
                }
                document.performanceFeedbackForm.commentTextArea.value="";
            });

            $("#Progress").click(function(){
                var position = $("#Progress").position();
                var tPosX = position.left-50;
                var tPosY = position.top+26;

                $("#progressReadMore").css({top:tPosY, left: tPosX}).slideDown("slow").fadeIn(400);

                for (var i=0; i < document.progressFeedbackForm.userChoice.length; i++)
                {
                    document.progressFeedbackForm.userChoice[i].checked=false;
                }
                document.progressFeedbackForm.commentTextArea.value="";
            });

            $("#Remarks").click(function(){
                var position = $("#Remarks").position();
                var tPosX = position.left-280;
                var tPosY = position.top+26;

                $("#remarksReadMore").css({top:tPosY, left: tPosX}).slideDown("slow").fadeIn(400);

                for (var i=0; i < document.remarksFeedbackForm.userChoice.length; i++)
                {
                    document.remarksFeedbackForm.userChoice[i].checked=false;
                }

                document.remarksFeedbackForm.commentTextArea.value="";
            });

            $("#submitMasteryFeedbackButton").click(function(){
                var userChoice = "";
                for (var i=0; i < document.performanceFeedbackForm.userChoice.length; i++)
                {
                    if (document.performanceFeedbackForm.userChoice[i].checked)
                    {
                        userChoice= document.performanceFeedbackForm.userChoice[i].value;
                    }
                }

                var comment = document.performanceFeedbackForm.commentTextArea.value;

                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=0&studentAction=savePerformanceUserChoice&var=b&comment="+userChoice);
                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=0&studentAction=savePerformanceComment&var=b&comment="+comment);
                $("#performanceReadMore").fadeOut("slow").slideUp(300);

                if (comment!="" || userChoice!="" ) {
                    var position = $("#performanceReadMore").position();
                    var tPosX = position.left;
                    var tPosY = position.top+80;

                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).fadeIn(800);
                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).delay(2000).fadeOut(800);
                }
            });

            $("#submitProgressFeedbackButton").click(function(){
                var userChoice = "";

                for (var i=0; i < document.progressFeedbackForm.userChoice.length; i++)
                {
                    if (document.progressFeedbackForm.userChoice[i].checked)
                    {
                        userChoice= document.progressFeedbackForm.userChoice[i].value;
                    }
                }

                var comment = document.progressFeedbackForm.commentTextArea.value;

                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId=0&eventCounter=${eventCounter + 1}&studentAction=saveProgressUserChoice&var=b&comment="+userChoice);
                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId=0&eventCounter=${eventCounter + 1}&studentAction=saveProgressComment&var=b&comment="+comment);
                $("#progressReadMore").fadeOut("slow").slideUp(300);

                if (comment!="" || userChoice!="" ) {
                    var position = $("#progressReadMore").position();
                    var tPosX = position.left;
                    var tPosY = position.top+80;
                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).fadeIn(800);
                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).delay(2000).fadeOut(800);
                }
            });

            $("#submitRemarksFeedbackButton").click(function(){
                var userChoice = "";
                for (var i=0; i < document.remarksFeedbackForm.userChoice.length; i++)
                {
                    if (document.remarksFeedbackForm.userChoice[i].checked)
                    {
                        userChoice= document.remarksFeedbackForm.userChoice[i].value;
                    }
                }

                var comment = document.remarksFeedbackForm.commentTextArea.value;

                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId=0&eventCounter=${eventCounter + 1}&studentAction=saveRemarksUserChoice&var=b&comment="+userChoice);
                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId=0&eventCounter=${eventCounter + 1}&studentAction=saveRemarksComment&var=b&comment="+comment);
                $("#remarksReadMore").fadeOut("slow").slideUp(300);

                if (comment!="" || userChoice!="" ) {
                    var position = $("#remarksReadMore").position();
                    var tPosX = position.left;
                    var tPosY = position.top+80;

                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).fadeIn(800);
                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).delay(2000).fadeOut(800);
                }
            });

            $("#submitMasteryBarCommentButton").click(function(){
                var userChoice = "";
                for (var i=0; i < document.masteryBarCommentForm.userChoice.length; i++)
                {
                    if (document.masteryBarCommentForm.userChoice[i].checked)
                    {
                        userChoice= document.masteryBarCommentForm.userChoice[i].value;
                    }
                }

                var comment = document.masteryBarCommentForm.commentTextArea.value;

                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId=0&eventCounter=${eventCounter + 1}&studentAction=saveRemarksUserChoice&var=b&comment="+userChoice);
                $.get("${pageContext.request.contextPath}/TutorBrain?action=SaveComment&sessionId=${sessionId}&topicId=0&eventCounter=${eventCounter + 1}&studentAction=saveRemarksComment&var=b&comment="+comment);
                if (comment!="" || userChoice!="" ) {
                    var position = $("#masteryBarCommentHolder").position();
                    var tPosX = position.left;
                    var tPosY = position.top+40;

                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).fadeIn(800);
                    $("#commentAckLayer").css({top:tPosY, left: tPosX}).delay(2000).fadeOut(800);
                }

                $("#masteryBarCommentHolder").fadeOut("slow").slideUp(300);
            });

            $(".js-go-to-my-garden").click(function() {
                updateElapsedTime();
//                if (useHybridTutor) {
                    window.location = "${pageContext.request.contextPath}/TutorBrain?action=Home&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=" + currentTopicId
                        + "&probId=" + currentProblemId
                        + "&elapsedTime=" + elapsedTime
                        + "&probElapsedTime=" + probElapsedTime
                        + "&learningCompanion=${learningCompanion}"
                        + "&var=b";
//                } else {
//                    $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPReturnToHut&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId="+currentTopicId+"&studentAction=backToSatHut&var=b&comment=",returnToHutComplete);
//                }
            });
        });
    </script>
</head>
<body>

<!-- NAVIGATION BAR -->
<header class="site-header" role="banner">
    <div id="wrapper">
        <div class="navbar-header">
            <img src="img/mstile-150x150.png" alt="" class="nav__logo-image">
            <span class="nav__logo-text"><span class="nav__logo-text--green-letter">M</span>ATH<span class="nav__logo-text--green-letter">S</span>PRING
        </span>
        </div><!-- navbar-header -->

        <nav id="main_nav" class="nav navbar-nav navbar-right">
            <li class="nav-item"><a class="js-go-to-my-garden"><%= rb.getString("my_garden") %></a></li>
            <li class="nav-item"><a id="myProg"><%= rb.getString("my_progress") %></a></li>
            <li class="nav-item">
                <c:choose>
                    <c:when test="${newSession}">
                        <a onclick="window.location='TutorBrain?action=EnterTutor&sessionId=${sessionId}'+'&elapsedTime=${elapsedTime}' + '&eventCounter=0&var=b'"><%= rb.getString("practice_area") %></a>
                    </c:when>
                    <c:otherwise>
                        <a onclick="window.location='TutorBrain?action=MPPReturnToHut&sessionId=${sessionId}'+'&elapsedTime=${elapsedTime}' + '&eventCounter=${eventCounter}' + '&probId=${probId}&topicId=-1' + '&learningCompanion=${learningCompanion}&var=b'"><%= rb.getString("practice_area") %></a>
                    </c:otherwise>
                </c:choose>
            </li>
	        <li id="changeLanguageButton" class="nav-item">
		            <a onclick="window.location='TutorBrain?action=navigation&from=my_progress&to=my_progress&elapsedTime=0&sessionId=${sessionId}'+ '&eventCounter=${eventCounter}' + '&topicId=-1&probId=${probId}&probElapsedTime=0&var=b'"><%= rb.getString("change_language")%></a>
	        </li>
            <li class="nav-item"><a href="TutorBrain?action=Logout&sessionId=${sessionId}&elapsedTime=${elapsedTime}&var=b"><%= rb.getString("log_out") %> &nbsp;<span class="fa fa-sign-out"></a></span>
            <li class="nav-item--last">
            	<a href="#" class="session-clock-item"> <span> <i class="fa fa-clock-o"
							aria-hidden="true"></i>
						</span> <span id="session_clock"></span> </a>
            </li>
        </nav>
    </div><!-- wrapper -->
</header>


<div class="main-content">
    <div class="row progress-data-wrapper">
        <table class="table table-bordered progress-table">
            <thead class="thead-inverse progress-table-header">
            <tr>
                <th><%= rb.getString("topic") %></th>
                <th><%= rb.getString("remark") %></th>
                <th><%= rb.getString("performance") %></th>
                <th><%= rb.getString("effort") %></th>
                <th><%= rb.getString("progress") %></th>
                <th><%= rb.getString("actions") %></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="ts" items="${topicSummaries}">
                <c:set var="topicName" value="${ts.topicName}"/>
                <c:set var="topicId" value="${ts.topicId}"/>
                <c:set var="masteryChartDiv" value="masteryChart_${topicId}"/>
                <c:set var="remarksDiv" value="remarks_${topicId}"/>
                <c:set var="problemsDiv" value="problemsDone_${topicId}"/>
                <c:set var="plantDiv" value="plant_${topicId}"/>
                <c:set var="pieDiv" value="pie_${topicId}"/>
                <c:set var="commentLink" value="commentLink_${topicId}"/>
                <c:set var="plantLink" value="plantLink_${topicId}"/>
                <c:set var="backToVillageURL" value="${backToVillageURL}"/>
                <tr>
                <td class="topic col-md-3">${topicName}</td>
                <td valign="top" class="remarks col-md-5">
                    <div id=${remarksDiv}></div>
                    <a href="#" class="littleLink" id=${commentLink}>
                        <img src="img/commentIcon.png" height="14">Comment&gt;
                    </a>
                </td>
                <td class="performance text-center col-md-2">
                    <p><%= rb.getString("mastery_level") %></p>
                    <a href="#" id="masteryBar_${topicId}">
                        <div id=${masteryChartDiv}></div>
                    </a>
                    <div id=${problemsDiv}></div>
                    <a href="#"
                       id="LearnMore"
                       onclick="window.location='${pageContext.request.contextPath}/TutorBrain?action=TopicDetail&sessionId=${sessionId}&elapsedTime='+updateElapsedTime()+'&mastery=${ts.mastery}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&topicName=${topicName}&problemsDone=${ts.problemsDone}&totalProblems=${ts.totalProblems}&var=b'"
                       class="littleLink btn mathspring-important-btn"><%= rb.getString("learn_more") %></a>
                </td>
                <td align="center" valign="bottom" class="pie_wrapper col-md-1" >
					<div id="${pieDiv}" style="width:200px; height:200px;"></div>
                </td>
                <td align="center" valign="bottom" class="plant_wrapper col-md-1">
                    <a href="#" id=${plantLink}><div id=${plantDiv}></div></a>
                </td>
                <td class="actionColumn col-md-1">
                    <ul id="reviewChallenge">

                <c:choose>
                    <c:when test="${ts.problemsDone>0 && ts.hasAvailableContent}">
                        <li class="huy-button huy-button--green" id="continue_${topicId}">
                            <a href="#"><%= rb.getString("continue") %></a>
                        </li>
                        <li class="huy-button huy-button--yellow" id="review_${topicId}">
                            <a href="#"><%= rb.getString("review") %></a>
                        </li>
                        <li class="huy-button huy-button--brown" id="challenge_${topicId}">
                            <a href="#"><span class="colorPink"><%= rb.getString("challenge") %></span></a>
                        </li>
                        </ul></td></tr>
                    </c:when>
                    <c:when test="${ts.problemsDone==0}">
                        <li class="huy-button huy-button--green" id="tryThis_${topicId}">
                            <a href="#"><%= rb.getString("try_this") %></a>
                        </li>
                    </c:when>
                    <%--The tutor sometimes can't continue a topic if some criteria are satisfied, so we only offer review and challenge--%>
                    <c:otherwise>
                        <li class="huy-button huy-button--yellow" id="review_${topicId}">
                            <a href="#"><%= rb.getString("review") %></a>
                        </li>

                        </ul></td></tr>
                    </c:otherwise>
                </c:choose>
                    </ul>
                </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<footer class="bottom-sticky-footer">&copy; <%= rb.getString("copyright") %>
</footer>

<div class="commentLayer" id="commentHolder" >
    <form name="commentForm">
        <textarea class="commentBox" name="commentTextArea" id="commentText"></textarea>
        <br/>
        <button id="submitCommentButton" type="button" class="thoughtbot"><%= rb.getString("submit") %></button>
        <button id="cancelCommentButton" type="button" class="clean-gray"><%= rb.getString("cancel") %></button>
    </form>
</div>

<div class="plantCommentLayer" id="plantCommentHolder" >
    <div id="plantDetails"></div>
    <form name="plantCommentForm">
        <textarea class="plantCommentBox" name="commentTextArea" id="plantCommentText"></textarea>
        <br/>
        <button id="submitPlantCommentButton" type="button" class="thoughtbot"><%= rb.getString("submit") %></button>
        <button id="cancelPlantCommentButton" type="button" class="clean-gray"><%= rb.getString("cancel") %></button>
    </form>
</div>

<div class="plantCommentLayer" id="masteryBarCommentHolder" >
    <div id="MasteryBarDetails"><%= rb.getString("is_mastery_calculation_correct?") %><br/></div>
    <form name="masteryBarCommentForm">
        <input type="radio"  name="userChoice" value="accurate"><%= rb.getString("yes_correct") %><br>
        <input type="radio"  name="userChoice" value="ok" ><%= rb.getString("no_incorrect") %><br>
        <br/>
        <%= rb.getString("you_can_also_leave_your_comment") %>:
        <textarea class="plantCommentBox" name="commentTextArea"></textarea>
        <br/>
        <button id="submitMasteryBarCommentButton" type="button" class="thoughtbot">><%= rb.getString("submit") %></button>
        <button id="cancelMasteryBarCommentButton" type="button" class="clean-gray"><%= rb.getString("cancel") %></button>
    </form>
</div>

<div class="tableTopDropDown" id="performanceReadMore" >
    <div id="performanceReadMoreText"><%= rb.getString("trying_to_calculate_mastery_level") %>  <br/>
        <%= rb.getString("are_we_good_calculating_mastery_level?") %> <br/><br/></div>

    <form name="performanceFeedbackForm">
        <%= rb.getString("please_select_your_response_from_this_list") %>:    <br/><br/>
        <input type="radio"  name="userChoice" value="accurate"><%= rb.getString("good_job_of_mastery_calculation") %><br>
        <input type="radio"  name="userChoice" value="ok" ><%= rb.getString("mastery_calculation_is_ok") %><br>
        <input type="radio"  name="userChoice" value="notAccurate" ><%= rb.getString("mastery_calculation_is_poor") %>
        <br/><br/>
        <%= rb.getString("you_can_also_leave_your_comment") %>:
        <textarea class="plantCommentBox" name="commentTextArea" id="performanceCommentText"></textarea>
        <br/>
        <button id="submitMasteryFeedbackButton" type="button" class="thoughtbot">><%= rb.getString("submit") %></button>
        <button id="cancelMasteryFeedbackButton" type="button" class="clean-gray"><%= rb.getString("cancel") %></button>
    </form>
</div>

<div class="tableTopDropDown" id="progressReadMore" >
    <div id="progressReadMoreText"><%= rb.getString("plant_growth_description") %> <br/>
        <%= rb.getString("keep_plant_representation?") %> <br/><br/></div>
    <form name="progressFeedbackForm">
        Please select your response from this list:    <br/> <br/>
        <input type="radio"  name="userChoice" value="keep"><%= rb.getString("yes_keep_plants") %><br>
        <input type="radio"  name="userChoice" value="doesnotmatter" ><%= rb.getString("it_does_not_matter") %><br>
        <input type="radio"  name="userChoice" value="takeAway" ><%= rb.getString("no_take_away_plants") %><br/><br/>
        <%= rb.getString("you_can_also_leave_your_comment") %>:
        <textarea class="plantCommentBox" name="commentTextArea" ></textarea>
        <br/>
        <button id="submitProgressFeedbackButton" type="button" class="thoughtbot">><%= rb.getString("submit") %></button>
        <button id="cancelProgressFeedbackButton" type="button" class="clean-gray"><%= rb.getString("cancel") %></button>
    </form>
</div>

<div class="tableTopDropDown" id="remarksReadMore" >
    <div id="remarksReadMoreText"><%= rb.getString("trying_to_help_you_perform_better") %><br/>
        <%= rb.getString("is_suggestion_helping") %><br/><br/></div>
    <form name="remarksFeedbackForm">
        <%= rb.getString("please_select_your_response_from_this_list") %>:    <br/> <br/>
        <input type="radio"  name="userChoice" value="yes"><%= rb.getString("yes_helping") %>.<br>
        <input type="radio"  name="userChoice" value="ok" ><%= rb.getString("ok_helping") %><br>
        <input type="radio"  name="userChoice" value="no" ><%= rb.getString("not_helping") %>
        <br/><br/>
        <%= rb.getString("you_can_also_leave_your_comment") %>:
        <textarea class="plantCommentBox" name="commentTextArea" ></textarea>
        <br/>
        <button id="submitRemarksFeedbackButton" type="button" class="thoughtbot">><%= rb.getString("submit") %></button>
        <button id="cancelRemarksFeedbackButton" type="button" class="clean-gray">C<%= rb.getString("cancel") %>ancel</button>
    </form>
</div>
<div class="thankYou" id="commentAckLayer">
    <%= rb.getString("thank_you") %> <br/>
    <%= rb.getString("we_have_saved_your_input") %>
</div>

<!-- SCRIPT - LIBRARIES -->
<script src="js/bootstrap.min.js"></script>

</body>
</html>
