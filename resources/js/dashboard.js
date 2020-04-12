
//document.addEventListener("DOMContentLoaded", function (event) {

function loadStudents(){
	//document.getElementById("student_tiles").innerHTML = '<p>hi there</p>'
	var count = 0;
	 var parsedData = 0;
     var parsedData2 = 0;
     var parsedData3 = 0;
     var auimage = "none";
     var masteries ="";
     var efforts ="";
	var api = 'http://ec2-34-203-204-126.compute-1.amazonaws.com:8080/AUPredictor/getActiveStudentandhist';
    fetch(api).then(function (response) {
    	var datajson = response.json();
    	//console.log("response = "+datajson);
        return datajson;
    }).then(function (data) {

    	//console.log("data = "+JSON.stringify(data));
    	//console.log("data = "+data[0]);

    	var sessions = Object.getOwnPropertyNames(data);
        count = sessions.length;//data.length;
        //console.log("count = "+count);
        
        //drawChart(parsedData2);
        
        if(count>0){
        	
        
        var mytable = '<table align="center" class="iconTable" cellpadding=\"0\" cellspacing=\"0\"><tbody><tr>';

        for (var i = 1; i <= count; i++) {
          if (i % 3 == 1 && i != 1) {
            mytable += '</tr><tr>';
          }
          
          var sessionId = sessions[i-1];//data[i-1].id;
          var studentId = data[""+sessionId]["studentId"];//data[i-1].studId;
          var studentName = data[""+sessionId]["studentName"];
          console.log("student id = "+studentId);
          console.log("student id = "+sessionId);
          
         

        	var aupreds = data[""+sessionId]["auPredictions"];
          	console.log("received prediction data");
          	if(aupreds.length === 0){
        		console.log("au prediction api send an empty dataset");
        		  //alert("No au prediction data available for the student!");
        	}else {
        	
        		console.log("pred length for studentid ="+studentId+" is = "+aupreds.length);
              parsedData = parsePredAU1(aupreds);
              parsedData2 = parsePredAU2(aupreds);
              parsedData3 = parsePredAU3(aupreds);
              console.log("au1 count ="+parsedData);
              console.log("au2 count ="+parsedData2);
              console.log("au3 count ="+parsedData3);
              
              if(parsedData>parsedData2){
            	  if(parsedData>parsedData3){
            		  auimage = "au4";
            	  }else if(parsedData3>parsedData2){
            		  auimage = "12au";
            	  }else {
            		  auimage = "au9";
            	  }
              }else {
            	  if(parsedData2>parsedData3){
            		  auimage = "au9";
            	  }else {
            		  auimage = "12au";
            	  }
              }
        	}
              console.log("au image = "+auimage);
         
      	

        	if(Object.getOwnPropertyNames(data).length === 0){
        		console.log("student problem hist api send an empty dataset");
        		  //alert("No current performance data available for the student!");
        	}else {
        		
        		
                masteries = 'Last 5 Masteries: <br> ';var colornum = 1;
                efforts = 'Last 5 Efforts: <br> ';
                var studentProbHistProjs = data[""+sessionId]["studentProbHistProjs"];
                var finalLen = studentProbHistProjs.length>5? 5:studentProbHistProjs.length;
                for(j=studentProbHistProjs.length-1;j>=studentProbHistProjs.length-finalLen;j--) {
                	
                	
                	var effortVal = studentProbHistProjs[j].effort==null||studentProbHistProjs[j].effort==""?"na":studentProbHistProjs[j].effort;
                	if(effortVal !="na"){
                		masteries +='<span class="masteryboxdashboard color'+colornum+ '">'+(studentProbHistProjs[j].mastery).toFixed(2)+'</span>';
                		efforts +='<span class="masteryboxdashboard color'+colornum+ '">'+effortVal+'</span>';
                    	colornum++;
                	}
                	
                }
                
            	masteries = '<div id="last5masteriesdashboard">'+masteries+'</div><br>';
			    
            	efforts = '<div id="last5effortsdashboard">'+efforts+'</div>';
            	
        	}
       
        	var sessionData = ""+JSON.stringify(data[""+sessionId]);
        	console.log("sessionData = "+ sessionData);
      	
        	if(masteries==""){
        		
        		mytable += '<td><button class="StudentButton tooltipp studentIconColor'+auimage+'" onclick="getStudentDetails('+studentId+',' +sessionId+')"><img src="../../img/'+auimage+'.png"><br>'+studentName+ '<span class="tooltiptext">No Performance Data Available</span></button></td>';
        	}else {
        		
        		mytable += '<td><button class="StudentButton tooltipp studentIconColor'+auimage+'" onclick="getStudentDetails('+studentId+' ,'+sessionId+')"><img src="../../img/'+auimage+'.png"><br>'+studentName+ masteries + efforts+/*'<span class="tooltiptext">'+ masteries + efforts+'</span>*/'</button></td>';
        	}
          
        }

        mytable += '</tr></tbody></table>';
        
        document.getElementById("student_tiles").innerHTML = mytable;
        } else {
        	alert("No Active students found!");
        	document.getElementById("student_tiles").innerHTML = "No Active students found!";
        }
    });
}



function parsePredAU1(data) {
    var arr = [];
   
    var i;
    var au1count = 0;
    for (i = 0; i < data.length; i++) {
      /*
        arr.push(
            {
                date: new Date(data[i].saveTime),   //date 
                value: parseFloat(data[i].au1).toFixed(2) //convert string to number 
            }
        );
        */
        if(parseFloat(data[i].au1).toFixed(2) >0.5){
        	au1count++;
        }
        
    }
    
    return au1count;
}

function parsePredAU2(data) {
    var arr = [];
    
    var i;
    var au2count = 0;
    for (i = 0; i < data.length; i++) {
      /*
        arr.push(
            {
                date: new Date(data[i].saveTime),   //date 
                value: parseFloat(data[i].au2).toFixed(2) //convert string to number 
               
            }
        );
        */
        
        if(parseFloat(data[i].au2).toFixed(2) >0.5){
        	au2count++;
        }
        
    }
    
    return au2count;
}

function parsePredAU3(data) {
    var arr = [];
    
    var i;
    var au3count = 0;
    for (i = 0; i < data.length; i++) {
      /*
        arr.push(
            {
                date: new Date(data[i].saveTime),   //date 
                value: parseFloat(data[i].au3).toFixed(2) //convert string to number 
            }
        );
        */
        if(parseFloat(data[i].au3).toFixed(2) >0.5){
        	au3count++;
        }
        
    }
    
    return au3count;
}

//});
