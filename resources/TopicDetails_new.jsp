<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>
<% 
/**
 * Frank 05-18-2020 issue #117 restrict thumbnail sixe to 500px x 500px
 * Frank 08-20-2020 issue $194 fix link to Practice Area
*  Frank	11-27-22	Merge jchart.js inline and apply multi-lingual algorithms
* Frank	08-22-24	Issue # 781R7	Use pageLang, pageLanIndex and experiment in multi-lingual algorithms
*/

Locale loc = request.getLocale(); 
Locale loc1 = request.getLocale(); 
Locale loc2 = request.getLocale();




String lang = loc.getLanguage();
String country = loc.getCountry();

System.out.println("locale set to:" + lang + "-" + country );	

int pageLangIndex = 0;
String strExperiment = "";
String pageLang = "";

try {
	pageLangIndex = (int) request.getAttribute("pageLangIndex");
}
catch (Exception e) {
	 System.out.println("pageLangIndex " + e.getMessage());
	 pageLangIndex = 0;
}

try {
	strExperiment = (String) request.getAttribute("experiment");
	if (strExperiment == null) {
		strExperiment = "";
	}
}
catch (Exception e) {
	 System.out.println("experiment " + e.getMessage());
	 strExperiment = "";
}

try {
	pageLang = (String) request.getAttribute("pageLang");
}
catch (Exception e) {
	 System.out.println("pageLang " + e.getMessage());
	 pageLang = "en";
}


if (strExperiment.indexOf("multi-lingual") == -1) {
	pageLangIndex = 0;
}
else {
	if (pageLang.equals("en")) {
		loc1 = new Locale("en","US");	
		loc2 = new Locale("es","US");	
	}
	else {
		loc1 = new Locale("es","US");	
		loc2 = new Locale("en","US");		
	}
}

if (pageLangIndex == 0) {
	if (pageLang.equals("en")) {
		loc1 = new Locale("en","US");	
		loc2 = new Locale("es","US");	
	}
	else {
		loc1 = new Locale("es","US");	
		loc2 = new Locale("en","US");		
	}
}
else {
	if (pageLangIndex == 1) {
		if (pageLang.equals("en")) {
			loc1 = new Locale("es","US");	
			loc2 = new Locale("en","US");	
		}
		else {
			loc1 = new Locale("en","US");	
			loc2 = new Locale("es","US");		
		}
	}
	else {
		loc1 = new Locale(pageLang,"US");	
		loc2 = new Locale(pageLang,"US");			
	}
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
    <title>MathSpring | <%= rb.getString("topic_details") %></title>
    <link rel="apple-touch-icon" sizes="180x180" href="apple-touch-icon.png">
    <link rel="icon" type="image/png" href="favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="manifest.json">
    <meta name="theme-color" content="#ffffff">
    <link rel="stylesheet" href="sass_compiled/details.css">
    <link href="css/graph_new.css" rel="stylesheet" type="text/css"/>

    <script src="js/jquery-1.10.2.js"></script>
    
    
        <script>

    var Chart = {
    /**
    }
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
                effortFeedback = "<%= rb.getString("topic_state_ATT")%>" +  attempts + "<%= rb.getString("topic_state_ATT2")%>";
                break;

            case "GUESS":
                table.className ="correctOnAttemptsCard";
                cell.innerHTML="_";
                effortFeedback = "<%= rb.getString("topic_state_ATT")%>" +  attempts + "<%= rb.getString("topic_state_ATT2")%>";
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
<!-- <script src="js/bootstrap/js/language_es.js"></script> -->

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
        var selectedCard = null;
        var problem_imageURL = '${webContentpath}'+'problemSnapshots/prob_';
   
        var globals = {
            mouseSaveInterval: ${mouseSaveInterval},
            mouseHistory: [],
            sessionId: ${sessionId}
        }

        var sysGlobals = {
            gritServletContext: '${gritServletContext}',
            wayangServletContext: '${wayangServletContext}',
            gritServletName: '${gritServletName}'
        }


        function initiateElapsedTime() {
            startElapsedTime= ${elapsedTime} ;
            var d = new Date();
            startClockTime = d.getTime();
        }

        function updateElapsedTime() {
            var d = new Date();
            var now = d.getTime();
            probElapsedTime += now-startClockTime;
            elapsedTime = startElapsedTime + probElapsedTime;
            return elapsedTime;
        }

        function initChart() {
            var chart = Chart;
            chart.init();
            chart.renderMastery("masteryChartDiv",${mastery} ,${problemsDone} );
            var i=0;

            <c:forEach var="pd" items="${problemDetailsList}">
                problemList[i]=new Array(8);
                problemList[i][0]="${pd.problemId}";
                problemList[i][1]="${pd.problemName}";
                problemList[i][2]="${pd.effort}";
                problemList[i][3]=${pd.numAttemptsToSolve};
                problemList[i][4]=${pd.numHints};
                problemList[i][6]="${pd.ccstds}";
                problemList[i][7]="${pd.snapshot}";
                i++;
            </c:forEach>

            chart.renderCharts(problemList,i,wrapperList);
        }

        $(document).ready(function(){

            initiateElapsedTime();
            initChart();

            function tryThisComplete (problemId) {
                window.location='${backToVillageURL}&sessionId=${sessionId}&learningHutChoice=true&elapsedTime='+updateElapsedTime()+'&learningCompanion=${learningCompanion}&mode=practice&topicId='+currentTopic+'&problemIdString='+problemId+'&var=b';
            }

            $('#wrapperList li').each(function(index) {
                $(this).addClass('non-selected-card');
                $(this)
                    .click(function() {
                        if (useHybridTutor)
                            window.location= "${pageContext.request.contextPath}/TutorBrain?action=MPPTryProblem&elapsedTime="+updateElapsedTime()+"&sessionId=${sessionId}&problemId="+currentProblemId+"&topicId="+currentTopic+"&studentAction=tryThis&mode=practice&var=b&comment=";
                        else
                            $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPTryProblem&elapsedTime="+updateElapsedTime()+"&sessionId=${sessionId}&problemId="+currentProblemId+"&topicId="+currentTopic+"&studentAction=tryThis&var=b&comment=",tryThisComplete(currentProblemId));
                    })
                    .hover(function() {
                        loadProblem($(this));
                    });
                if (index == 0) $(this).hide();
                if (index == 1) loadProblem($(this));

                function loadProblem(card) {
                    if (selectedCard != null) {
                        selectedCard.removeClass('selected-card');
                        selectedCard.addClass('non-selected-card');
                    }
                    selectedCard = card;
                    card.removeClass('non-selected-card');
                    card.addClass("selected-card");
                    var position = $("#problemCards").position();
                    var tPosX = position.left ;
                    var tPosY = position.top+$("#problemCards").height();
                    $(".dropDownContent").css({top:tPosY, left: tPosX}).show();
                    currentProblemId=problemList[index-1][0];
                    currentProblem=problemList[index-1][1];
                    currentEffort=problemList[index-1][2];
                    effortFeedback=problemList[index-1][5];

                    if (currentProblem.substring(0,10)=="formality_")
                    {
                        isFormality=true;
                        formalityId= currentProblem.substring(10,currentProblem.length);
                    }
                    else isFormality=false;

                    if (!isFormality) {
                        $("#js-problem-view").text(effortFeedback);
                        $("#js-problem-view").append("<img id='problemImage' style='max-width:500px; max-height:500px;' />");
                        document.getElementById("problemImage").src = problem_imageURL+currentProblemId+".jpg";
                        console.log("CCSS: " + problemList[index-1][6]);
                    } else {
                        $("#js-problem-view").text(currentProblem);
                        //$("#js-problem-view").append("<iframe id='formalityProblemFrame' width='600' height='300'> </iframe>");
                        problemImagePath="http://cadmium.cs.umass.edu/formality/FormalityServlet?fxn=questionSnapshot&qID="+formalityId;
                        document.getElementById("formalityProblemFrame").src = problemImagePath;
                        console.log("CCSS: " + problemList[index-1][6]);
                    }

                    document.getElementById("problemDetailsButtons").onclick = function() {
                        if (useHybridTutor)
                            window.location="${pageContext.request.contextPath}/TutorBrain?action=MPPTryProblem&elapsedTime="+updateElapsedTime()+"&sessionId=${sessionId}&problemId="+currentProblemId+"&topicId="+currentTopic+"&studentAction=tryThis&mode=practice&comment=";
                        else $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPTryProblem&elapsedTime="+updateElapsedTime()+"&sessionId=${sessionId}&problemId="+currentProblemId+"&topicId="+currentTopic+"&studentAction=tryThis&mode=practice&comment=",tryThisComplete(currentProblemId));
                    };
                }
            });

            $(".js-go-to-my-garden").click(function() {
                var currentTopicId = ${topicId};
                updateElapsedTime();
                if (useHybridTutor) {
                    window.location = "${pageContext.request.contextPath}/TutorBrain?action=Home&sessionId=${sessionId}&eventCounter=${eventCounter + 1}"
                        + "&topicId=" + currentTopicId
                        + "&probId=" + currentProblemId
                        + "&elapsedTime=" + elapsedTime
                        + "&probElapsedTime=" + probElapsedTime
                        + "&learningCompanion=${learningCompanion}"
                        + "&var=b";
                } else {
                    $.get("${pageContext.request.contextPath}/TutorBrain?action=MPPReturnToHut&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId="+currentTopicId+"&studentAction=backToSatHut&var=b&comment=",returnToHutComplete);
                }
            });

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
            
            
        });
    </script>
</head>
<body>
<div class="nav">
    <div class="nav__logo">
        <img src="img/mstile-150x150.png" alt="" class="nav__logo-image">
        <span class="nav__logo-text">
            <span class="nav__logo-text--green-letter">M</span>ATH<span class="nav__logo-text--green-letter">S</span>PRING
        </span>
    </div>

    <ul class="nav__list">
        <li class="nav__item">
            <a class="js-go-to-my-garden"><%= rb.getString("my_garden") %></a>
        </li>
        <li class="nav__item">
            <a
                    onclick="window.location='TutorBrain?action=navigation&from=sat_Hut&to=my_progress&elapsedTime=0&sessionId=${sessionId}'+ '&eventCounter=${eventCounter}' + '&topicId=-1&probId=${probId}&probElapsedTime=0&var=b'"
            >
                <%= rb.getString("my_progress") %>
            </a>
        </li>
        <li class="nav__item">
            <c:choose>
                <c:when test="${newSession}">
                    <a onclick="window.location='TutorBrain?action=EnterTutor&sessionId=${sessionId}'+'&elapsedTime=${elapsedTime}' + '&eventCounter=0&var=b'"><%= rb.getString("practice_area") %></a>
                </c:when>
                <c:otherwise>
                 	<a onclick="window.location='TutorBrain?action=MPPReturnToHut&sessionId=${sessionId}'+'&elapsedTime=${elapsedTime}' + '&eventCounter=${eventCounter}' + '&probId=${probId}&topicId=-1' + '&learningCompanion=${learningCompanion}&var=b'"><%= rb.getString("practice_area") %></a>
                </c:otherwise>
            </c:choose>
        </li>
        <li class="nav__item">
            <a href="TutorBrain?action=Logout&sessionId=${sessionId}&elapsedTime=${elapsedTime}&var=">
                <%= rb.getString("log_out") %> &nbsp;
                <span class="fa fa-sign-out"></span>
            </a>
        </li>
    </ul>
</div>
<div class="bootstrap">
    <div class="main-content">
        <div class="topic-details-wrapper">
            <div class="topic-details-view">
                <h1>${topicName}</h1>
                <div class="row topic-overview">
                    <div class="col-md-4">
                        <div class="row topic-statistics">
                            <h2><%= rb.getString("mastery_level") %></h2>
                            <div id="masteryChartDiv"></div>
                            <div>
                                <p class="problem-done-num">${problemsDone}/${totalProblems}</p>
                                <p class="problem_done_label"><%= rb.getString("topic_details_problems_done") %></p>
                            </div>
                        </div>
                        <div class="row" id="problemCards" rel="performanceDetails">
                            <ul id="wrapperList"><li></li></ul>
                        </div>
                    </div>
                    <div class="col-md-8 detail-problem-view">
                        <div id="js-problem-view"></div>
                        <div class="row">
                            <button type="button"
                                    class="btn btn-lg try-problem-button"
                                    id="problemDetailsButtons"><%= rb.getString("topic_details_click") %></button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
