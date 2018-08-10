/**

 */
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



    renderCharts	: function(problemList, c, containerId) {

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
                    effortFeedback="You have not tried this problem yet.";
                    break;

                case "SOF":
                    table.className ="correctCard";
                    cell.innerHTML="★";
                    effortFeedback="You got this problem correct on first attempt.";
                    break;

                case "NOTR":
                    table.className ="warningCard";
                    cell.innerHTML="ǃ";
                    effortFeedback="Maybe you need to work on this problem more carefully.";
                    break;

                case "BOTTOMOUT":
                    table.className ="correctWithHintsCard";
                    cell.innerHTML="H";
                    effortFeedback="Hints helped you solve this problem. Do you want to try without hints?";
                    break;

                case "GIVEUP":
                    table.className ="giveupCard";
                    effortFeedback="You gave up this problem.";
                    cell.innerHTML="_";
                    break;
                case "SKIP":
                    table.className ="giveupCard";
                    effortFeedback="You gave up this problem.";
                    cell.i;
                    break;

                case "SHINT":
                    table.className ="correctWithHintsCard";
                    cell.innerHTML="H";
                    if (hints==1)   {
                    effortFeedback="You solved this problem with "+ hints+" hint.";
                    }else
                    {effortFeedback="You solved this problem with "+ hints+" hints.";  }

                    break;

                case "ATT":
                    table.className ="correctOnAttemptsCard";
                    cell.innerHTML="_";

                    effortFeedback="You solved this problem in "+ attempts+" attempts.";
                    break;

                case "GUESS":
                    table.className ="correctOnAttemptsCard";
                    cell.innerHTML="_";
                    effortFeedback="You solved this problem with "+ attempts+" attempts.";
                    break;

                default:
                    table.className ="emptyCard";
                    cell.innerHTML="_";
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


        if (topic_state=="topicEmpty") feedbackText+="Untried topic- Would you like to try this topic now?";

        // correct for the first time

        if (topic_state=="correctForTheFirstTime") feedbackText+="Got last problem right on first attempt. Congratulations!! Do you want to try more problems like this?";
        if (topic_state=="correctForTheFirstTime_goodHelpUsage") feedbackText+="Got last problem right on first attempt! You had also made good use of tutor help in previous problem. You are using this tutor correctly as you are asking for help when you need it and solving on your own when you can.";


        // baby topic just started

        else if (topicState_pepperPlant=="babyTopicJustStarted_babyPepper" )  feedbackText+="You have received this baby pepper plant as you have started working on this new topic. Don't forget to use hints, videos and examples when needed. "

        // baby topic

        else if (topicState_pepperPlant=="babyTopic_babyPepper" ) {

            if (topic_state=="SHINT") feedbackText+="Great effort! Keep using the hints, videos and examples!";
            else if (topic_state=="NOTR")    feedbackText+="Don’t like reading? Have the computer read aloud - click the read aloud button.";
            else if (topic_state=="SOF") feedbackText+="Got last problem right on first attempt! Congratulations!!";
            else feedbackText+="As you put more effort on solving the problems, the baby pepper plant grows to give pepper fruits.";
        }


        // toddler topics

        else if (topicState_pepperPlant=="toddlerTopic_toddlerPepper" ) feedbackText+="Your pepper plant grows as you put effort working on these math problems.";
        else if (topicState_pepperPlant=="toddlerTopic_toddlerPepper_wilt" ) feedbackText+="If you do not put effort on solving the problems, but rather keep guessing and giving up and not reading carefully, the plant wilts.";



        // just mastered states

        else if (topicState_pepperPlant=="justMastered_rainbowPepper") feedbackText="Congratulations! You’ve mastered this topic and the mastery bar is over 88%. You also made an exceptional use of tutor help features. You are therefore getting a special Rainbow Pepper. Double Congratulations!! ";

        else if (topicState_pepperPlant=="justMastered_masteryPepper_bonusPeppers") feedbackText="Congratulations! You’ve mastered this topic and the mastery bar is over 88%. You also made a good use of tutor help features. You have received bonus peppers. ";

        else if (topicState_pepperPlant=="justMastered_monsterPepper") feedbackText="Congratulations! You’ve mastered this topic and the mastery bar is over 88%. You have demonstrated exceptional performance. You are therefore getting a special Monster Pepper. Double Congratulations!! ";

        else if (topicState_pepperPlant=="justMastered_bigPepper") feedbackText="Congratulations! You’ve mastered this topic and the mastery bar is over 88%. You have solved a lot of problems. You have thus received a Big Pepper plant. ";

        else if (topicState_pepperPlant=="justMastered_masteryPepper") feedbackText="Congratulations! You’ve mastered this topic and the mastery bar is over 88%. There are now fruits on your pepper plant.";

        //in mastery states

        else if (topicState_pepperPlant=="inMastery_rainbowPepper") feedbackText="Skill mastered! You have demonstrated exceptional help usage in this topic and have this rainbow pepper. Now, do you want to try more challenging problems, or try a new topic? ";

        else if (topicState_pepperPlant=="inMastery_masteryPepper_bonusPeppers") feedbackText="Skill mastered! You have demonstrated exceptional help usage in this topic and have this rainbow pepper. Now, do you want to try more challenging problems, or try a new topic? ";

        else if (topicState_pepperPlant=="inMastery_monsterPepper") feedbackText="Skill mastered! You have demonstrated exceptional performance in this topic and have this monster pepper. Now, do you want to try more challenging problems, or try a new topic? ";

        else if (topicState_pepperPlant=="inMastery_bigPepper") feedbackText="Skill mastered! You have solved a lot of problems in this topic. Now, do you want to try more challenging problems, or try a new topic? ";

        else if (topicState_pepperPlant=="inMastery_masteryPepper") feedbackText="Congratulations! You’ve mastered this topic and the mastery bar is over 88%. ";

        //remastered states

        else if (topicState_pepperPlant=="reMasteredWithGoodHelpUsage_rainbowPepper") feedbackText="Good job on keeping mastery up! You have a rainbow pepper indicating that you made effective use of tutor help.";
        else if (topicState_pepperPlant=="reMasteredWithGoodHelpUsage_masteryPepper_bonusPeppers") feedbackText="Good job on keeping mastery up! You have bonus peppers for using tutor help effectively..";
        else if (topicState_pepperPlant=="reMasteredSolvingLotOfProblems_bigPepper") feedbackText="Good job on keeping mastery up! You have a big pepper indicating that you have solved a lot of problems in this topic.";

        //close to mastery(mastery>75) states

        else if (topicMastery >= 75)

        {
            feedbackText+="You are close to mastering this topic. ";

            if (topicState_pepperPlant=="disengagedCloseToMastery_youngPepper_wilt")  feedbackText+="But, you seem to be disengaged lately. Your flowered pepper is wilting."
            else if (topicState_pepperPlant=="closeToMastery_flowerPepper_bonusFlowers")  feedbackText+="";
            else if (topicState_pepperPlant=="closeToMasteryGoodHelpUsage_flowerPepper_moreBonusFlowers")  feedbackText+="You seem to have done quite well in this topic, using the tutor help effectively. Keep it up."
            else if (topicState_pepperPlant=="closeToMastery_flowerPepper")  feedbackText+="";

            if (topic_state=="inProgress") feedbackText+="You got the last problem correct on first attempt. Keep up the good work and you can soon get the mastery.";
            else if (topic_state=="inProgress_goodHelpUsage") feedbackText+="You got the last problem correct on first attempt.You had also made good use of tutor help in previous problem. You are using this tutor correctly as you are asking for help when you need it and solving on your own when you can.";
            else if (topic_state=="SHINT") feedbackText+="Great effort! Keep using the hints, videos and examples!";


        }
        //adolescent topic

        else if (topicState_pepperPlant=="adolescentTopicGoodHelpUsage_adolescentPepper_withMoreFlowers") feedbackText+="You seem to have used tutor help features very well in this topic. Your plant has a lot of flowers."
        else if ((topicState_pepperPlant=="adolescentTopic_adolescentPepper") || (topicState_pepperPlant=="adolescentTopic_adolescentPepper_withFlowers"))
        {
            if (topic_state=="inProgress") feedbackText+="You got the last problem correct on first attempt. Keep up the good work and you can soon get the mastery.";
            else if (topic_state=="inProgress_goodHelpUsage") feedbackText+="You got the last problem correct on first attempt.You had also made good use of tutor help in previous problem. You are showing good problem solving, asking for help when you need it and solving on your own when you can.";
            else if (topic_state=="SHINT") feedbackText+="Great effort! Keep using the hints, videos and examples!";  }

        else if (topicState_pepperPlant=="adolescentTopic_adolescentPepper_wilt") feedbackText+="You seem to have lost interest. Please use hints, videos and examples if you need."

        //young topic

        else if (topicState_pepperPlant=="youngTopicGoodHelpUsage_youngPepper_withMoreFlowers") feedbackText+="You have progressed well on this topic. You seem to have used tutor help features very well. Your plant has a lot of flowers."

        else if ((topicState_pepperPlant=="youngTopic_youngPepper") || (topicState_pepperPlant=="youngTopic_youngPepper_withFlowers"))
        {
            if (topic_state=="inProgress") feedbackText+="You have progressed well on this topic. You got the last problem correct on first attempt. Keep up the good work and you can soon get the mastery.";
            else if (topic_state=="inProgress_goodHelpUsage") feedbackText+="You got the last problem correct on first attempt.You had also made good use of tutor help in previous problem. You are showing good problem solving, asking for help when you need it and solving on your own when you can.";
            else if (topic_state=="SHINT") feedbackText+="Great effort! Keep using the hints, videos and examples!";  }

        else if (topicState_pepperPlant=="youngTopic_adolescentPepper_wilt") feedbackText+="You seem to have lost interest. Please use hints, videos and examples if you need."

        //
        if (topic_state=="ATT_hardProblem") feedbackText+="That last problem was a hard one. Good work!";

        else if (topic_state=="GIVEUP_hardProblem") feedbackText+="Maybe that one was too confusing. The next problem will be easier. When in doubt try clicking ‘solve it’ - it’s useful!";

        //disengaged behavior

        if (neglectful_count>=2){


        if (topic_state=="NOTR") feedbackText+="Don’t like reading? Have the computer read aloud - click the read aloud button.";

        else if (topic_state=="GUESS_helpAvoidance") feedbackText+="Many find hints, videos, and examples helpful.  Try them! ";

        else if (topic_state=="BOTTOMOUT_helpMisuse") feedbackText+="When you’ve had some practice, using fewer hints will help you master these problems.";

        if (studentState_disengaged==true) feedbackText+="<br/>Are you frustrated?  Raise your hand and someone will help you.";
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

        document.getElementById(problemsDone_div).innerHTML="Problems Done : " + problemsDone + "/"+totalProblems ;
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
