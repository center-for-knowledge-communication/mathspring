/**

 */
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
    	var languagePreference = window.navigator.language;
    	var languageSet = "en";
    	if (languagePreference.includes("en")) {
    		languageSet = "en"
    	} else if (languagePreference.includes("es")) {
    		languageSet = "es"
    	}

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
                    effortFeedback = languageSet == "es" ? "No has visto este problema todavía" : "You have not tried this problem yet.";
                    break;

                case "SOF":
                    table.className ="correctCard";
                    cell.innerHTML="★";
                    effortFeedback = languageSet == "es" ? "Resolviste este problema correctamente en el primer intento." : "You got this problem correct on first attempt.";
                    break;

                case "NOTR":
                    table.className ="warningCard";
                    cell.innerHTML="ǃ";
                    effortFeedback = languageSet == "es" ? "Quizás necesitas trabajar en este problema con más cuidado" : "Maybe you need to work on this problem more carefully.";
                    break;

                case "BOTTOMOUT":
                    table.className ="correctWithHintsCard";
                    cell.innerHTML="H";
                    effortFeedback = languageSet == "es" ? "La ayuda realmente te ayudó a resolver este problema. ¿Quieres probar sin ayuda la próxima vez?" : "Hints helped you solve this problem. Do you want to try without hints?";
                    break;

                case "GIVEUP":
                    table.className ="giveupCard";
                    effortFeedback = languageSet == "es" ? "Abandonaste, dejase de trabajar en este problema" : "You gave up this problem.";
                    cell.innerHTML="_";
                    break;

                case "SHINT":
                    table.className ="correctWithHintsCard";
                    cell.innerHTML="H";
                    if (hints==1)   {
                    	effortFeedback = languageSet == "es" ? "Resolviste este problema con" : "You solved this problem with "+ hints+ languageSet == "es" ? "ayudita" : "hint.";
                    }else
                    { effortFeedback = languageSet == "es" ? "Resolviste este problema con" : "You solved this problem with "+ hints+ languageSet == "es" ? "ayuditas" : "hints.";  }

                    break;

                case "ATT":
                    table.className ="correctOnAttemptsCard";
                    cell.innerHTML="_";
                    effortFeedback = languageSet == "es" ? "Resolviste este problema con" : "You solved this problem in "+ languageSet == "es" ? "intento" : "attempts.";
                    break;

                case "GUESS":
                    table.className ="correctOnAttemptsCard";
                    cell.innerHTML="_";
                    effortFeedback = languageSet == "es" ? "Resolviste este problema con" : "You solved this problem in "+ languageSet == "es" ? "intento" : "attempts.";
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
        this.givePlants(plantDiv, pepperPlant, callback);
    },

giveFeedback    : function(remarksDiv, topic_state,topicState_pepperPlant,topicMastery, neglectful_count,studentState_disengaged) {


	var languagePreference = window.navigator.language;
	var languageSet = "en";
	if (languagePreference.includes("en")) {
		languageSet = "en"
	} else if (languagePreference.includes("es")) {
		languageSet = "es"
	}
	
	var feedbackText="";

        //empty state


    	//feedbackText += languageSet == "es" ? "" : "";

	
        if (topic_state=="topicEmpty") feedbackText += languageSet == "es" ? "Tema no intentado: ¿te gustaría probar este tema ahora mismo?" : "Untried topic- Would you like to try this topic now?";

        // correct for the first time

        if (topic_state=="correctForTheFirstTime") feedbackText += languageSet == "es" ? "Contestaste el último problema en el primer intento. ¡¡Felicitaciones!! ¿Querés probar más problemas como este?" : "Got last problem right on first attempt. Congratulations!! Do you want to try more problems like this?";
        if (topic_state=="correctForTheFirstTime_goodHelpUsage") feedbackText += languageSet == "es" ? "¡Contestaste el último problema en el primer intento! También has usado de la ayuda muy bien en el problema anterior. Estás utilizando este programa correctamente ya que pedís ayuda cuando la necesitás y resolvés por su cuenta cuando podés." : "Got last problem right on first attempt! You had also made good use of tutor help in previous problem. You are using this tutor correctly as you are asking for help when you need it and solving on your own when you can.";


        // baby topic just started

        else if (topicState_pepperPlant=="babyTopicJustStarted_babyPepper" )  feedbackText += languageSet == "es" ? "Recibiste esta plantita de pimiento cuando comenzaste a trabajar en este tema nuevo. No olvides utilizar la AYUDA, los videos y los ejemplos cuando sea necesario." : "You have received this baby pepper plant as you have started working on this new topic. Don't forget to use hints, videos and examples when needed.";
        // baby topic

        else if (topicState_pepperPlant=="babyTopic_babyPepper" ) {

            if (topic_state=="SHINT") feedbackText += languageSet == "es" ? "¡Excelente esfuerzo! ¡Seguí usando la ayuda, los videos y los ejemplos!" : "Great effort! Keep using the hints, videos and examples!";
            else if (topic_state=="NOTR")    feedbackText += languageSet == "es" ? "¿No te gusta leer? Hacé que la computadora lea en voz alta: hacé clic en el botón de LEER EN VOZ ALTA." : "Don’t like reading? Have the computer read aloud - click the read aloud button.";
            else if (topic_state=="SOF") feedbackText += languageSet == "es" ? "¡Tengo el último problema justo en el primer intento! ¡¡Felicitaciones!!" : "Got last problem right on first attempt! Congratulations!!";
            else feedbackText += languageSet == "es" ? "A medida que te esfuerces más para resolver los problemas, la planta de pimiento crece para dar frutos." : "As you put more effort on solving the problems, the baby pepper plant grows to give pepper fruits.";
        }


        // toddler topics

        else if (topicState_pepperPlant=="toddlerTopic_toddlerPepper" ) feedbackText += languageSet == "es" ? "Tu planta de pimiento crece a medida que te esfuerces trabajando en estos problemas matemáticos." : "Your pepper plant grows as you put effort working on these math problems.";
        else if (topicState_pepperPlant=="toddlerTopic_toddlerPepper_wilt" ) feedbackText += languageSet == "es" ? "Si no te esforzás en resolver los problemas, sino que adivinás y te das por vencido y no lees con cuidado, la planta se marchita." : "If you do not put effort on solving the problems, but rather keep guessing and giving up and not reading carefully, the plant wilts.";

//TBD

        // just mastered states

        else if (topicState_pepperPlant=="justMastered_rainbowPepper") feedbackText = languageSet == "es" ? "¡Felicitaciones! Has dominado este tema y la barra de tu conocimiento supera el 88%. También hizo un uso excepcional de las funciones de ayuda del tutor. Por lo tanto, obtienes un especial Rainbow Pepper. Doble felicitaciones !!" : "Congratulations! You’ve mastered this topic and the mastery bar is over 88%. You also made an exceptional use of tutor help features. You are therefore getting a special Rainbow Pepper. Double Congratulations!!";

        else if (topicState_pepperPlant=="justMastered_masteryPepper_bonusPeppers") feedbackText = languageSet == "es" ? "¡Felicitaciones! Has dominado este tema y la estimación de tu conocimiento supera el 88%. También hizo un uso excepcional de las funciones de ayuda del tutor. Por lo tanto, obtienes un especial Rainbow Pepper. Doble felicitaciones !!" : "Congratulations! You’ve mastered this topic and the mastery bar is over 88%. You also made a good use of tutor help features. You have received bonus peppers. ";

        else if (topicState_pepperPlant=="justMastered_monsterPepper") feedbackText = languageSet == "es" ? "¡Felicitaciones! Has dominado este tema y la estimación de tu conocimiento supera el 88%. Has demostrado un rendimiento excepcional. Por lo tanto, obtienes una planta de Pimientos Gigantes especial. Doble felicitaciones !!" : "Congratulations! You’ve mastered this topic and the mastery bar is over 88%. You have demonstrated exceptional performance. You are therefore getting a special Monster Pepper. Double Congratulations!! ";

        else if (topicState_pepperPlant=="justMastered_bigPepper") feedbackText = languageSet == "es" ? "¡Felicitaciones! Has dominado este tema y la estimación de tu conocimiento supera el 88%. Has resuelto muchos problemas. Por lo tanto, Has recibido una planta de Big Pepper. " : "Congratulations! You’ve mastered this topic and the mastery bar is over 88%. You have solved a lot of problems. You have thus received a Big Pepper plant. ";

        else if (topicState_pepperPlant=="justMastered_masteryPepper") feedbackText = languageSet == "es" ? "¡Felicitaciones! Has dominado este tema y la estimación de tu conocimiento supera el 88%. Ahora hay frutas en su planta de pimiento." : "Congratulations! You’ve mastered this topic and the mastery bar is over 88%. There are now fruits on your pepper plant.";

        //in mastery states

        else if (topicState_pepperPlant=="inMastery_rainbowPepper") feedbackText = languageSet == "es" ? "Tema dominado! Has demostrado un uso excepcional de la ayuda en este tema y ganaste un pimiento arcoíris. Ahora, ¿quieres probar problemas más desafiantes o probar un nuevo tema?" : "Skill mastered! You have demonstrated exceptional help usage in this topic and have this rainbow pepper. Now, do you want to try more challenging problems, or try a new topic? ";

        else if (topicState_pepperPlant=="inMastery_masteryPepper_bonusPeppers") feedbackText = languageSet == "es" ? "Tema dominado! Has demostrado un uso excepcional de la ayuda en este tema y tenés ahora un pimiento arcoíris. Ahora, ¿quieres probar problemas más desafiantes o probar un nuevo tema?" : "Skill mastered! You have demonstrated exceptional help usage in this topic and have this rainbow pepper. Now, do you want to try more challenging problems, or try a new topic? ";

        else if (topicState_pepperPlant=="inMastery_monsterPepper") feedbackText = languageSet == "es" ? "Tema dominado! Has demostrado un rendimiento excepcional en este tema y tenés  este monstruo de pimienta. Ahora, ¿quieres probar problemas más desafiantes o probar un nuevo tema?" : "Skill mastered! You have demonstrated exceptional performance in this topic and have this monster pepper. Now, do you want to try more challenging problems, or try a new topic? ";

        else if (topicState_pepperPlant=="inMastery_bigPepper") feedbackText = languageSet == "es" ? "Tema dominado! Has resuelto muchos problemas en este tema. Ahora, ¿quieres probar problemas más desafiantes o probar un nuevo tema? " : "Skill mastered! You have solved a lot of problems in this topic. Now, do you want to try more challenging problems, or try a new topic? ";

        else if (topicState_pepperPlant=="inMastery_masteryPepper") feedbackText = languageSet == "es" ? "¡Felicitaciones! Has dominado este tema y la estimación de tu conocimiento supera el 88%." : "Congratulations! You’ve mastered this topic and the mastery bar is over 88%. ";

        //remastered states

        else if (topicState_pepperPlant=="reMasteredWithGoodHelpUsage_rainbowPepper") feedbackText = languageSet == "es" ? "¡Buen trabajo para mantener el dominio! Tenés un pimiento arcoíris que indica que hizo un uso efectivo de la ayuda del tutor." : "Good job on keeping mastery up! You have a rainbow pepper indicating that you made effective use of tutor help.";
        else if (topicState_pepperPlant=="reMasteredWithGoodHelpUsage_masteryPepper_bonusPeppers") feedbackText = languageSet == "es" ? "¡Buen trabajo para mantener el dominio! Tenés  pimientos extra por usar la ayuda del tutor de una manera muy efectiva" : "Good job on keeping mastery up! You have bonus peppers for using tutor help effectively..";
        else if (topicState_pepperPlant=="reMasteredSolvingLotOfProblems_bigPepper") feedbackText = languageSet == "es" ? "¡Buen trabajo para mantener el dominio! Tenés un gran pimiento que indica que Has resuelto muchos problemas en este tema." : "Good job on keeping mastery up! You have a big pepper indicating that you have solved a lot of problems in this topic.";

        
// END TBD
        
        //close to mastery(mastery>75) states

        else if (topicMastery >= 75)

        {
            feedbackText += languageSet == "es" ? "Estás cerca de dominar este tema. " : "You are close to mastering this topic. ";

            if (topicState_pepperPlant=="disengagedCloseToMastery_youngPepper_wilt")  feedbackText += languageSet == "es" ? "Pero, parecés estar desconectado últimamente. Tu pimiento florecido se está marchitando." : "But, you seem to be disengaged lately. Your flowered pepper is wilting.";
            else if (topicState_pepperPlant=="closeToMastery_flowerPepper_bonusFlowers")  feedbackText +="";
            else if (topicState_pepperPlant=="closeToMasteryGoodHelpUsage_flowerPepper_moreBonusFlowers")  feedbackText += languageSet == "es" ? "Parece que le Has ido bastante bien en este tema, utilizando la ayuda del tutor de manera efectiva. Seguid así." : "You seem to have done quite well in this topic, using the tutor help effectively. Keep it up.";
            else if (topicState_pepperPlant=="closeToMastery_flowerPepper")  feedbackText+="";

            if (topic_state=="inProgress") feedbackText += languageSet == "es" ? "Tienes el último problema correcto en el primer intento. Sigan con el buen trabajo y pronto podrán obtener el dominio." : "You got the last problem correct on first attempt. Keep up the good work and you can soon get the mastery.";
            else if (topic_state=="inProgress_goodHelpUsage") feedbackText += languageSet == "es" ? "Obtuvo el último problema correcto en el primer intento. También hizo un buen uso de la ayuda del tutor en el problema anterior. Está utilizando este tutor correctamente ya que solicita ayuda cuando la necesita y resuelve por su cuenta cuando puede." : "You got the last problem correct on first attempt.You had also made good use of tutor help in previous problem. You are using this tutor correctly as you are asking for help when you need it and solving on your own when you can.";
            else if (topic_state=="SHINT") feedbackText += languageSet == "es" ? "¡Gran esfuerzo! ¡Sigue usando las pistas, videos y ejemplos!" : "Great effort! Keep using the hints, videos and examples!";


        }
        //adolescent topic

        else if (topicState_pepperPlant=="adolescentTopicGoodHelpUsage_adolescentPepper_withMoreFlowers") feedbackText += languageSet == "es" ? "Parece que Has utilizado muy bien las funciones de ayuda del tutor en este tema. Tu planta tenés muchas flores." : "You seem to have used tutor help features very well in this topic. Your plant has a lot of flowers.";
        else if ((topicState_pepperPlant=="adolescentTopic_adolescentPepper") || (topicState_pepperPlant=="adolescentTopic_adolescentPepper_withFlowers"))
        {
            if (topic_state=="inProgress") feedbackText += languageSet == "es" ? "Tienes el último problema correcto en el primer intento. Sigan con el buen trabajo y pronto podrán obtener el dominio." : "You got the last problem correct on first attempt. Keep up the good work and you can soon get the mastery.";
            else if (topic_state=="inProgress_goodHelpUsage") feedbackText += languageSet == "es" ? "Obtuvo el último problema correcto en el primer intento. También hizo un buen uso de la ayuda del tutor en el problema anterior. Está mostrando una buena resolución de problemas, solicitando ayuda cuando la necesita y resolviendo por su cuenta cuando puede." : "You got the last problem correct on first attempt.You had also made good use of tutor help in previous problem. You are showing good problem solving, asking for help when you need it and solving on your own when you can.";
            else if (topic_state=="SHINT") feedbackText += languageSet == "es" ? "¡Gran esfuerzo! ¡Sigue usando las pistas, videos y ejemplos!" : "Great effort! Keep using the hints, videos and examples!";  }

        else if (topicState_pepperPlant=="adolescentTopic_adolescentPepper_wilt") feedbackText += languageSet == "es" ? "Parece que has perdido interés. Utilice sugerencias, videos y ejemplos si lo necesita." : "You seem to have lost interest. Please use hints, videos and examples if you need.";

        //young topic

        else if (topicState_pepperPlant=="youngTopicGoodHelpUsage_youngPepper_withMoreFlowers") feedbackText += languageSet == "es" ? "Has progresado bien en este tema. Parece que has utilizado muy bien las funciones de ayuda del tutor. Tu planta tenés muchas flores." : "You have progressed well on this topic. You seem to have used tutor help features very well. Your plant has a lot of flowers.";

        else if ((topicState_pepperPlant=="youngTopic_youngPepper") || (topicState_pepperPlant=="youngTopic_youngPepper_withFlowers"))
        {
            if (topic_state=="inProgress") feedbackText += languageSet == "es" ? "Has progresado bien en este tema.Tenés  el último problema correcto en el primer intento. Sigan con el buen trabajo y pronto podrán obtener el dominio." : "You have progressed well on this topic. You got the last problem correct on first attempt. Keep up the good work and you can soon get the mastery.";
            else if (topic_state=="inProgress_goodHelpUsage") feedbackText += languageSet == "es" ? "Obtuvo el último problema correcto en el primer intento. También hizo un buen uso de la ayuda del tutor en el problema anterior. Está mostrando una buena resolución de problemas, solicitando ayuda cuando la necesita y resolviendo por su cuenta cuando puede." : "You got the last problem correct on first attempt.You had also made good use of tutor help in previous problem. You are showing good problem solving, asking for help when you need it and solving on your own when you can.";
            else if (topic_state=="SHINT") feedbackText += languageSet == "es" ? "¡Gran esfuerzo! ¡Sigue usando las pistas, videos y ejemplos!" : "Great effort! Keep using the hints, videos and examples!";  }

        else if (topicState_pepperPlant=="youngTopic_adolescentPepper_wilt") feedbackText += languageSet == "es" ? "Parece que has perdido interés. Utiliza las sugerencias, videos y ejemplos si lo necesitas." : "You seem to have lost interest. Please use hints, videos and examples if you need.";

        //
        if (topic_state=="ATT_hardProblem") feedbackText += languageSet == "es" ? "Ese último problema fue difícil. ¡Buen trabajo!" : "That last problem was a hard one. Good work!";

        else if (topic_state=="GIVEUP_hardProblem") feedbackText += languageSet == "es" ? "Tal vez ese era demasiado confuso. El siguiente problema será más fácil. En caso de duda, intentá hacer clic en la Ayuda, ¡es útil!" : "Maybe that one was too confusing. The next problem will be easier. When in doubt try clicking ‘solve it’ - it’s useful!";

        //disengaged behavior

        if (neglectful_count>=2){


        if (topic_state=="NOTR") feedbackText += languageSet == "es" ? "¿No te gusta leer? Hacé que la computadora lea en voz alta: haga clic en el botón leer en voz alta." : "Don’t like reading? Have the computer read aloud - click the read aloud button.";

        else if (topic_state=="GUESS_helpAvoidance") feedbackText += languageSet == "es" ? "Muchos encuentran sugerencias, videos y ejemplos útiles. ¡Pruébalos! " : "Many find hints, videos, and examples helpful.  Try them! ";

        else if (topic_state=="BOTTOMOUT_helpMisuse") feedbackText += languageSet == "es" ? "Cuando hayas practicado un poco, usar menos pistas te ayudará a resolver estos problemas." : "When you’ve had some practice, using fewer hints will help you master these problems.";

        if (studentState_disengaged==true) feedbackText += languageSet == "es" ? "<br/>¿Estás frustrado? Levanta la mano y alguien te ayudará." : "<br/>Are you frustrated?  Raise your hand and someone will help you.";
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
                    text: 'Challenge',
                    click: function($vexContent, event) {
                        window.location = challengeTopicLink;
                    }
                });
            }
            if (reviewTopicLink !== "") {
                buttons[buttons.length] = $.extend({}, vex.dialog.buttons.NO, {
                    className: 'btn btn-lg mathspring-warning-btn',
                    text: 'Review',
                    click: function($vexContent, event) {
                        window.location = reviewTopicLink;
                    }
                });
            }
            if (continueTopicLink !== "") {
                buttons[buttons.length] = $.extend({}, vex.dialog.buttons.NO, {
                    className: 'btn btn-lg mathspring-btn',
                    text: 'Continue',
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
                    '<p>' + numComplete + ' / ' + numTotal + ' problems done</p>',
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

    	var probsdone = languageSet == "es" ? "Problemas hechos" : "Problems Done";

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
