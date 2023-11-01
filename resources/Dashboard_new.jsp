<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.Locale"%>
<%@ page import="java.util.ResourceBundle"%>

<% 

/**
* Kartik 04-22-21 Issue #390 Added session clock functionality
* Frank	11-27-22	Merge jchart_new.js inline and apply multi-lingual algorithms

*/

Locale loc = request.getLocale(); 

ResourceBundle rb = null;


String lang = loc.getLanguage();
String country = loc.getCountry();

System.out.println("Browser locale set to:" + lang + "-" + country );	

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
			try {
				rb = ResourceBundle.getBundle("MathSpring", new Locale("en","US"));
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		else {
			lang = "es";
			try {
				rb = ResourceBundle.getBundle("MathSpring",new Locale("es","US"));
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	else {
		if (lang.equals("en")) {
			lang = "es";
			try {
				rb = ResourceBundle.getBundle("MathSpring", new Locale("es","US"));
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		else {
			try {
				rb = ResourceBundle.getBundle("MathSpring", new Locale("en","US"));
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
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


	
/*
ResourceBundle rb1 = null;
try {
	rb1 = ResourceBundle.getBundle("MathSpring",loc1);
}
catch (Exception e) {
	System.out.println(e.getMessage());
}
ResourceBundle rb2 = null;
try {
	rb2 = ResourceBundle.getBundle("MathSpring",testloc);
}
catch (Exception e) {
	
	System.out.println(e.getMessage());
}

if (lang.equals("en")) {
	try {
		rb = ResourceBundle.getBundle("MathSpring",loc1);
	}
	catch (Exception e) {
		System.out.println(e.getMessage());
	}
}
else {
	try {
		rb = ResourceBundle.getBundle("MathSpring",loc2);
	}
	catch (Exception e) {
		
		System.out.println(e.getMessage());
	}
}
*/




%>






<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>MathSpring | <%= rb.getString("my_garden")%></title>
    <link rel="apple-touch-icon" sizes="180x180" href="apple-touch-icon.png">
    <link rel="icon" type="image/png" href="favicon-32x32.png" sizes="32x32">
    <link rel="icon" type="image/png" href="favicon-16x16.png" sizes="16x16">
    <link rel="manifest" href="manifest.json">
    <link href="css/Dashboard_new.css?ver=<%=versions.getString("css_version")%>" rel="stylesheet" type="text/css"/>
    <link href="sass_compiled/dashboard.css" rel="stylesheet" type="text/css" />
    <script src="js/jquery-1.10.2.js"></script>
    
    <script>
    var experiment = "<%= strExperiment %>";
    var Chart = {

    	    /**
    	     * The graph object. Short-hand for "this". Set in the init-method.
    	     */
    	    o               : null,

    	    /**
    	     * The graph's container element. Passed as a string argument to the init-method.
    	     */
    	    container       : null,

    	    /**
    	     *
    	     */
    	    title           : "",



    	    /**
    	     * The chart type, bars or columns.
    	     */
    	    type            : null,

    	    /**
    	     * The graph data as an array.
    	     */
    	    data            : null,

    	    attemptsArray   :null,
    	    correctArray    :null,

    	    /**
    	     * The delimiter used for separating the data string passed
    	     * to the method setData.
    	     .    */
    	    dataDelimiter   : ";",




    	    /**
    	     * Initialize the graph object.
    	     */
    	    init                : function() {
    	        o = this;
    	        o.data = null;
    	        o.attemptsArray=null;
    	        o.correctArray=null;


    	        //o.container   = document.getElementById(containerId);


    	    },

    	    initCharts              : function(containerId) {
    	        o = this;
    	        o.data = null;
    	        o.attemptsArray=null;
    	        o.correctArray=null;


    	        o.container = document.getElementById(containerId);


    	    },

    	    /**
    	     * Set the chart data from a delimited string in the following format:
    	     * "item1;value1;item2;value2".
    	     */
    	    setData             : function(problemDetailsObj) {

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



    	    renderCharts    : function(problemList, c, containerId) {

    	        var tableClassName="";
    	        var topicState="";
    	        var hints="";
    	        var attempts="";
    	        var effortFeedback="";

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
    	            hints=problemList[i][4];





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



    	    renderMastery               : function(masteryDiv, masteryValue) {

    	        //o.drawMastery (parseFloat(problemDetailsObj.masteryLevel), jpt);
    	        o.drawMastery   (masteryDiv, masteryValue);

    	    },


    	    getColumns          : function() {


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
    	    getColumn           : function(problemDetailsObj,c) {
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





    	    drawMastery         : function(masteryDiv, masteryLevel) {
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

    	    getBarValue     : function(dataItemValue) {
    	        // Create the value text associated with the data item.
    	        var value;
    	        value = document.createElement("div");
    	        value.className = "barValue";
    	        value.innerHTML = dataItemValue;

    	        return value;
    	    },

    	    giveFeedbackAndPlant : function(remarksDiv,plantDiv,topicState,studentState_disengaged,topicMastery,problemsDoneWithEffort,SHINT_SOF_sequence,SOF_SOF_sequence,neglectful_count,problemsDone,problemsSolved, callback) {

    	        //identify topic_pepper_state

    	        var topicState_pepperPlant;
    	        topicState_pepperPlant = "";
    	        var pepperPlant = "";

//    	        if (problemsDone0){

    	            if (problemsSolved == 0) {
    	                pepperPlant = "noPepper";
    	                topicState_pepperPlant = "topicEmpty";
    	            }

    	            else if (topicMastery>=.88)  {
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
//    	        }

    	        if (remarksDiv != null) {
    	            this.giveFeedback(remarksDiv, topicState, topicState_pepperPlant, topicMastery, studentState_disengaged);
    	        }
    	        this.givePlants(plantDiv, pepperPlant, callback);
    	    },

    	giveFeedback    : function(remarksDiv, topic_state,topicState_pepperPlant,topicMastery, neglectful_count,studentState_disengaged) {


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



    	    givePlants: function(plant_div, pepperPlant, callback) {
    	        function showOverlay(title, numComplete, numTotal) {
    	            var percentage = numComplete / numTotal;
    	            console.log(percentage);
    	            var className = '';
    	            if (percentage <= 0.33) {
    	                className = 'zero-progress';
    	            } else if (percentage > 0.33 && percentage <= 0.66) {
    	                className = 'thirty-three-progress';
    	            } else {
    	                className = 'sixty-six-progress';
    	            }

    	            var buttons = [];
    	            var $plantDiv = $('#' + plant_div);
    	            var challengeTopicLink = $plantDiv.attr('challengeTopicLink');
    	            var continueTopicLink = $plantDiv.attr('continueTopicLink');
    	            var reviewTopicLink = $plantDiv.attr('reviewTopicLink');
    	            if (challengeTopicLink !== "") {
    	                buttons[buttons.length] = $.extend({}, vex.dialog.buttons.NO, {
    	                    className: 'btn btn-lg mathspring-important-btn',
    	                    text: '<%= rb.getString("dashboard_challenge")%>',
    	                    click: function($vexContent, event) {
    	                        window.location = challengeTopicLink;
    	                    }
    	                });
    	            }
    	            if (reviewTopicLink !== "") {
    	                buttons[buttons.length] = $.extend({}, vex.dialog.buttons.NO, {
    	                    className: 'btn btn-lg mathspring-warning-btn',
    	                    text: '<%= rb.getString("dashboard_review")%>',
    	                    click: function($vexContent, event) {
    	                        window.location = reviewTopicLink;
    	                    }
    	                });
    	            }
    	            if (continueTopicLink !== "") {
    	                buttons[buttons.length] = $.extend({}, vex.dialog.buttons.NO, {
    	                    className: 'btn btn-lg mathspring-btn',
    	                    text: '<%= rb.getString("dashboard_continue")%>',
    	                    click: function($vexContent, event) {
    	                        window.location = continueTopicLink;
    	                    }
    	                });
    	            }

    	            vex.dialog.open({
    	                message: '',
    	                buttons: buttons,
    	                input: [
    	                    '<style>',
    	                    '.vex-custom-field-wrapper {',
    	                    'margin: 1em 0;',
    	                    '}',
    	                    '.vex-custom-field-wrapper > label {',
    	                    'display: inline-block;',
    	                    'margin-bottom: .2em;',
    	                    '}',
    	                    '.topic-title {',
    	                    'font-weight: bold;',
    	                    '}',
    	                    '</style>',
    	                    '<div>',
    	                    '<h3 class="topic-title">' + title + '</h3>',
    	                    '<div class="visible num-questions">',
    	                    '<p>' + numComplete + ' / ' + numTotal + ' <%= rb.getString("problems_done")%></p>',
    	                    '</div>',
    	                    '<div class="progress overlay">',
    	                    '<div class="progress-bar ' + className + '" role="progressbar" aria-valuenow="' + percentage + '"',
    	                    'aria-valuemin="0" aria-valuemax="100" style="width:' + percentage * 100 + '%">',
    	                    '</div><!-- progress-bar -->',
    	                    '</div><!-- progress -->',
    	                    '</div><!-- section-panel -->',
    	                ].join(''),
    	                onSubmit: function() {
    	                },
    	            });
    	        }
    	        var plantImage = document.createElement("IMG");
    	        if (pepperPlant.length!=0){
    	            plantImage.src="img/pp/"+pepperPlant+".png" ;
    	            if (document.getElementById(plant_div) == null) {
    	                console.log(plant_div);
    	            } else {
    	                document.getElementById(plant_div).prepend(plantImage);
    	            }
    	        }
    	    },




    	    problemsDone: function(problemsDone_div,problemsDone,totalProblems,problemsSolved) {

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
    
    <script src="js/jchart_new.js?ver=<%=versions.getString("js_version")%>"></script>
    <script src="js/huy-slider.js"></script>
    <script type="text/javascript" src="js/tutorutils.js"></script>
    <script src="js/bootstrap/js/language_es.js"></script>
    <script>
    
    function loadFlashContent () {
        $("#loadFlash").attr("title", title);
        $("#loadFlashIFrame").attr("src","https://get.adobe.com/flashplayer");
        $("#loadFlash").dialog("open");

    }
    
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

            <c:set var="newUser" value="true"/>
            <c:forEach var="ts" items="${topicSummaries}">
            <c:if test="${ts.problemsDone != 0}">
                <c:set var="newUser" value="false"/>
            </c:if>
            var topicState = "${ts.topicState}";
            var topicId = ${ts.topicId};
            var topicMastery = ${ts.mastery};
            var problemsDone = ${ts.problemsDone};
            var problemsSolved = ${ts.numProbsSolved};
            var totalProblems = ${ts.totalProblems};
            var problemsDoneWithEffort = ${ts.problemsDoneWithEffort};
            var SHINT_SOF_sequence = ${ts.SHINT_SOF_sequence};
            var SOF_SOF_sequence = ${ts.SOF_SOF_sequence};
            var neglectful_count = ${ts.neglectful_count};
            var studentState_disengaged = false;
            var chart = Chart;


            chart.init();
            startSessionClock(globals.timeInSession);
            chart.giveFeedbackAndPlant(
                null,
                "plant_"+topicId,
                topicState,
                studentState_disengaged,
                topicMastery,
                problemsDoneWithEffort,
                SHINT_SOF_sequence,
                SOF_SOF_sequence,
                neglectful_count,
                problemsDone,
                problemsSolved);
            </c:forEach>

            $('#slider-s').on('input', function () {
                var audio = document.getElementById('backgroundmusic');
                audio.volume = this.value / 100.0;
                console.log(this.value);
            });

            window.volumeControlOpen = false;
            $('#volume_control').on('click', toggleVolumeControl);
            $(document).mouseup(closeVolumeControl);
            $('.play-button span').on('click', playBackground);
        });

    </script>
</head>
<body>

<audio src="css/dashboard-music.mp3" id="backgroundmusic" onload="adjustVolume()" autoplay loop></audio>
<div class="nav">
    <div class="nav__logo">
        <img src="img/mstile-150x150.png" alt="" class="nav__logo-image">
        <span class="nav__logo-text">
            <span class="nav__logo-text--green-letter">M</span>ATH<span class="nav__logo-text--green-letter">S</span>PRING
        </span>
    </div>

    <ul class="nav__list">
        <li class="nav__item">
            <a href="#"><%= rb.getString("my_garden")%></a>
        </li>
        <li class="nav__item">
            <a
                    onclick="window.location='TutorBrain?action=navigation&from=sat_Hut&to=my_progress&elapsedTime=0&sessionId=${sessionId}'+ '&eventCounter=${eventCounter}' + '&topicId=-1&probId=${probId}&probElapsedTime=0&var=b'"
            >
                <%= rb.getString("my_progress")%>
            </a>
        </li>
        <li class="nav__item">
        <c:choose>
            <c:when test="${newSession}">
            	<a onclick="window.location='TutorBrain?action=EnterTutor&sessionId=${sessionId}'+'&elapsedTime=${elapsedTime}' + '&eventCounter=0&var=b'"><%= rb.getString("practice_area")%></a>
            </c:when>
            <c:otherwise>
        		<a onclick="window.location='TutorBrain?action=MPPReturnToHut&sessionId=${sessionId}'+'&elapsedTime=${elapsedTime}' + '&eventCounter=${eventCounter}' + '&probId=${probId}&topicId=-1' + '&learningCompanion=${learningCompanion}&var=b'"><%= rb.getString("practice_area")%></a>
        	</c:otherwise>
        </c:choose>
        </li>
        <li class="nav__item">
        	<div id="changeLanguageButton")
            	<a onclick="window.location='TutorBrain?action=ChangeLanguage&from=sat_Hut&to=sat_Hut&elapsedTime=0&sessionId=${sessionId}'+ '&eventCounter=${eventCounter}' + '&topicId=-1&probId=${probId}&probElapsedTime=0&var=b'"><%= rb.getString("change_language")%></a>
            >
            </div>
        </li>
        <li class="nav__item">
            <a href="TutorBrain?action=Logout&sessionId=${sessionId}&elapsedTime=${elapsedTime}&var=">
                <%= rb.getString("log_out")%> &nbsp;
            <span class="fa fa-sign-out"></span>
            </a>
        </li>
        <li class="nav__item" id="volume_control">
            <a href="#">
                <i class="fa fa-volume-up"></i>
            </a>
        </li>
        <li class="nav__item" id="sessionclock">
        	<a href="#" class="session-clock-item"> <span> <i class="fa fa-clock-o"
							aria-hidden="true"></i>
			</span> <span id="session_clock"></span> </a>
        </li>
        <div class="slider-wrapper">
            <div class="play-button">
                <span class="fa fa-pause"></span>
            </div>
            <div class="volume-adjust-wrapper">
                <span class="fa fa-volume-down"></span>&nbsp;
                <input type="range" name="slider-s" id="slider-s" value="50" min="0" max="100" data-highlight="true"/>&nbsp;
                <span class="fa fa-volume-up"></span>
            </div>
        </div>
    </ul>
</div>

<div class="topic-list">
    <c:if test="${newUser == true}">
        <div class="welcome">
            <h1><%= rb.getString("welcome_to_mathspring")%></h1>
            <p><%= rb.getString("go_to_instructions")%>
            </p>
        </div>
    </c:if>

    <div id="Clouds">
        <div class="Cloud Foreground"></div>
        <div class="Cloud Background"></div>
        <div class="Cloud Foreground"></div>
        <div class="Cloud Background"></div>
        <div class="Cloud Foreground"></div>
        <div class="Cloud Background"></div>
        <div class="Cloud Background"></div>
        <div class="Cloud Foreground"></div>
        <div class="Cloud Background"></div>
        <div class="Cloud Background"></div>
        <!--  <svg viewBox="0 0 40 24" class="Cloud"><use xlink:href="#Cloud"></use></svg>-->
    </div>

    <div class="container">
        <c:forEach var="ts" items="${topicSummaries}">
            <c:set var="topicName" value="${ts.topicName}"/>
            <c:set var="numProblemsDone" value="${ts.numProbsSolved}"/>
            <c:set var="numTotalProblems" value="${ts.totalProblems}"/>
            <c:set var="plantDiv" value="plant_${ts.topicId}"/>
            <c:set var="percentDone" value="${numProblemsDone/numTotalProblems}"/>
            <c:set var="colorClass" value="0"/>
            <c:choose>
                <c:when test="${percentDone <= 0.2}">
                    <c:set var="colorClass" value="0"/>
                </c:when>

                <c:when test="${percentDone > 0.2 && percentDone <= 0.4}">
                    <c:set var="colorClass" value="20"/>
                </c:when>

                <c:when test="${percentDone > 0.4 && percentDone <= 0.6}">
                    <c:set var="colorClass" value="40"/>
                </c:when>

                <c:when test="${percentDone > 0.6 && percentDone <= 0.8}">
                    <c:set var="colorClass" value="60"/>
                </c:when>

                <c:when test="${percentDone > 0.8 && percentDone <= 1 }">
                    <c:set var="colorClass" value="80"/>
                </c:when>

            </c:choose>
            <c:choose>
                <c:when test="${ts.problemsDone>0 && ts.hasAvailableContent}">
                    <c:set var="challengeTopicLink" value="${pageContext.request.contextPath}/TutorBrain?action=MPPChallengeTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=challenge&location=Dashboard&comment=" />
                    <c:set var="continueTopicLink" value="${pageContext.request.contextPath}/TutorBrain?action=MPPContinueTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=continue&location=Dashboard&comment=" />
                    <c:set var="reviewTopicLink" value="${pageContext.request.contextPath}/TutorBrain?action=MPPReviewTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=review&location=Dashboard&comment=" />
                </c:when>
                <c:when test="${ts.problemsDone==0}">
                    <c:set var="beginTopicLink" value="${pageContext.request.contextPath}/TutorBrain?action=MPPContinueTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=tryThis&var=b&comment=" />
                </c:when>
                <%--The tutor sometimes can't continue a topic if some criteria are satisfied, so we only offer review and challenge--%>
                <c:otherwise>
                    <c:set var="challengeTopicLink" value="${pageContext.request.contextPath}/TutorBrain?action=MPPChallengeTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=challenge&location=Dashboard&comment=" />
                    <c:set var="reviewTopicLink" value="${pageContext.request.contextPath}/TutorBrain?action=MPPReviewTopic&sessionId=${sessionId}&eventCounter=${eventCounter + 1}&topicId=${ts.topicId}&studentAction=review&location=Dashboard&comment=" />
                </c:otherwise>
            </c:choose>
            <c:if test="${ts.problemsDone != 0}">
                <div class="topic-list__item">
                    <div class="topic-list__flipper">
                        <div class="topic-list__front topic-list__front--${colorClass}">
                            <p class="topic-list__title">${topicName}</p>
                            <p class="topic-list__info">${numProblemsDone} / ${numTotalProblems} <%= rb.getString("problems_done")%></p>
                            <p class="topic-list__info"><%= rb.getString("in_progress")%></p>
                            <div class="pot" id="${plantDiv}">
                            </div>
                        </div>
                        <div class="topic-list__back">
                            <p class="topic-list__title">${topicName}</p>
                            <p class="topic-list__info">${numProblemsDone} / ${numTotalProblems} <%= rb.getString("problems_done")%></p>
                            <p class="topic-list__info"><%= rb.getString("in_progress")%></p>
                            <div class="topic-list__buttons">
                                <div
                                        class="topic-list__button topic-list__button--green"
                                        onclick="window.location='${continueTopicLink}'"
                                >
                                    <%= rb.getString("dashboard_continue")%>
                                </div>

                                <div
                                        class="topic-list__button topic-list__button--yellow"
                                        onclick="window.location='${reviewTopicLink}'"
                                >
                                    <%= rb.getString("dashboard_review")%>
                                </div>

                                <div
                                        class="topic-list__button topic-list__button--brown"
                                        onclick="window.location='${challengeTopicLink}'"
                                >
                                    <%= rb.getString("dashboard_challenge")%>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${ts.problemsDone == 0}">
                <div class="topic-list__item">
                    <div class="topic-list__flipper">
                        <div class="topic-list__front topic-list__front--${colorClass}">
                            <p class="topic-list__title">${topicName}</p>
                            <p class="topic-list__info">${numTotalProblems} <%= rb.getString("problems_available")%></p>
                            <p class="topic-list__info"><%= rb.getString("not_started_yet")%></p>
                            <div class="pot" id="${plantDiv}">
                            </div>
                        </div>
                        <div class="topic-list__back">
                            <p class="topic-list__title">${topicName}</p>
                            <p class="topic-list__info">${numTotalProblems} <%= rb.getString("problems_available")%></p>
                            <div class="topic-list__buttons">
                                <div
                                        class="topic-list__button topic-list__button--green"
                                        onclick="window.location='${beginTopicLink}'"
                                >
                                    <%= rb.getString("dashboard_begin")%>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </c:forEach>

    </div>
</div>


<!-- SCRIPT - LIBRARIES -->
<script src="js/bootstrap.min.js"></script>

</body>
</html>
