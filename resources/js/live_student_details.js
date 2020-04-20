

//var api = 'https://api.coindesk.com/v1/bpi/historical/close.json?start=2017-12-31&end=2018-04-01';
//document.addEventListener("DOMContentLoaded", function (event) {

function getStudentDetails(studentId, sessionId) {
	document.getElementById('linechart').innerHTML = "";
	document.getElementById('pie_chart').innerHTML = "";
	document.getElementById('bar_chart').innerHTML = "";
	document.getElementById('last5efforts').innerHTML = "";
	document.getElementById('last5masteries').innerHTML = "";
	document.getElementById('last2topics').innerHTML = "";
	document.getElementById('studentname').innerHTML = "";
	document.getElementById('refreshButton').onclick = function (){getStudentDetails(studentId, sessionId);};
	//var api = 'https://api.coindesk.com/v1/bpi/historical/close.json?start=2017-12-31&end=2018-04-01';
	var requestJson = {	"studentId":studentId, "fromTime":"2019-12-16T06:54:29.678Z","toTime":"2019-12-16T07:47:29.678Z"};
	var api = 'http://ec2-34-203-204-126.compute-1.amazonaws.com:80/AUPredictor/getAuPreds';
	fetch(api, {
		  method: 'POST', // or 'PUT'
		  headers: {
		    'Content-Type': 'application/json',
		  },
		  body: JSON.stringify(requestJson),
		}).then(function (response) {
        return response.json();
    }).then(function (data) {

    	
        var parsedData = parseAUPredAU1(data);
        var parsedData2 = parseAUPredAU2(data);
        var parsedData3 = parseAUPredAU3(data);
        console.log("parsed data length = "+parsedData.length);
        drawLineChart(parsedData,parsedData2,parsedData3);
        drawbarchart(getAUCounts(data));
        
        var requestJson = {	"sessions":[sessionId]};
        console.log("requestjson = "+ JSON.stringify(requestJson));
    	var probHistApi = 'http://ec2-34-203-204-126.compute-1.amazonaws.com:80/AUPredictor/getActiveStudentandhist';
    	fetch(probHistApi, {
    		  method: 'GET', // or 'PUT'
    		  headers: {
    		    'Content-Type': 'application/json',
    		  },
    		  //body: JSON.stringify(requestJson),
    		}).then(function (response) {
            return response.json();
        }).then(function (data) {

        	if(Object.getOwnPropertyNames(data).length === 0){
        		console.log("student problem hist api send an empty dataset");
        		  alert("No current performance data available for the student!");
        	}else {
        		
        		console.log("data has data");
        		console.log(data);
            	var dataset = data[""+sessionId]["efforts"];
            	var studentName = data[""+sessionId]["studentName"];
                var counts = {}, i, value;
                for (i = 0; i < dataset.length; i++) {
                    value = dataset[i];
                    if (typeof counts[value] === "undefined") {
                        counts[value] = 1;
                    } else {
                        counts[value]++;
                    }
                }
                console.log(counts);
                var dataval = [];
              for(var key in counts) {
                var val = counts[key];
                dataval.push({
                  count: val,
                  keyword: key
                });
              }
              //console.log("dataval="+dataval[0].keyword);
                drawPieChart(dataval, dataset.length);
                var masteries = 'Last 5 Masteries:  ';var colornum = 1;
                var studentProbHistProjs = data[""+sessionId]["studentProbHistProjs"];
                var finalLen = studentProbHistProjs.length>5? 5:studentProbHistProjs.length;
                /*
                for(j=studentProbHistProjs.length-1;j>=studentProbHistProjs.length-finalLen;j--) {
                	masteries +='<span class="masterybox color'+colornum+ '">'+(studentProbHistProjs[j].mastery).toFixed(2)+'</span>';
                	colornum++;
                }
                
                var efforts = 'Last 5 Efforts:  ';
                colornum = 1;
               // var studentProbHistProjs = data[""+sessionId]["studentProbHistProjs"];
                for(j=studentProbHistProjs.length-1;j>=studentProbHistProjs.length-finalLen;j--) {
                	var effortVal = studentProbHistProjs[j].effort==null||studentProbHistProjs[j].effort==""?"na":studentProbHistProjs[j].effort;
                	efforts +='<span class="masterybox color'+colornum+ '">'+effortVal+'</span>';
                	colornum++;
                }
                */
                var efforts = 'Last 5 Efforts:  ';
                colornum = 1;
                for(j=studentProbHistProjs.length-1;j>=studentProbHistProjs.length-finalLen;j--) {
	                var effortVal = studentProbHistProjs[j].effort==null||studentProbHistProjs[j].effort==""?"na":studentProbHistProjs[j].effort;
	            	if(effortVal !="na"){
	            		masteries +='<span class="masterybox color'+colornum+ '">'+(studentProbHistProjs[j].mastery).toFixed(2)+'</span>';
	            		efforts +='<span class="masterybox color'+colornum+ '">'+effortVal+'</span>';
	                	colornum++;
	            	}
                }
                
                var topics = 'Last topics :  ';
                colornum = 1;
                var topicNames = data[""+sessionId]["topicNames"];
                var topicLen = topicNames.length>2? 2:topicNames.length;
                for(k=topicNames.length-1;k>=topicNames.length-topicLen;k--){
                	topics +='<span class="masterybox color'+colornum+ '">'+topicNames[k]+'</span>';
                }
                document.getElementById('last5efforts').innerHTML = masteries;
            	document.getElementById('last5masteries').innerHTML = efforts;
            	document.getElementById('last2topics').innerHTML = topics;
            	document.getElementById('studentname').innerHTML = "Name : "+studentName;
        	}
        	
        });
        
        $("#report-wrapper").hide();
        $("#report-wrapper2").hide();
        $("#form-wrapper").hide();
        $("#liveDashboard").hide();
        $("#live_student_details").show();
        //getStudentDetails();
        console.log("live_student_details is clicked!");
        //drawChart(parsedData2);
    });
//});
}

function parseAUPredAU1(data) {
    var arr = [];
    //console.log("save time= "+data[0].saveTime);
    var i;
    var au1count = 0;
    for (i = 0; i < data.length; i++) {
      //text += cars[i] + "<br>";
   
    	//console.log("pred = "+pred.saveTime);
        arr.push(
            {
                date: new Date(data[i].saveTime),   //date 
                value: parseFloat(data[i].au1).toFixed(2) //convert string to number 
            }
        );
        if(parseFloat(data[i].au2).toFixed(2) >0.5){
        	au1count++;
        }
        
    }
    //console.log("date value = "+arr[0].date);
    return arr;
}

function parseAUPredAU2(data) {
    var arr = [];
    //console.log("save = "+data[0].saveTime);
    var i;
    var au2count = 0;
    for (i = 0; i < data.length; i++) {
      //text += cars[i] + "<br>";
   
    	//console.log("pred = "+pred.saveTime);
        arr.push(
            {
                date: new Date(data[i].saveTime),   //date 
                value: parseFloat(data[i].au2).toFixed(2) //convert string to number 
               // au2count: au2count>0.5?au2count+1:au2count;// capturing the number of instances havin prediction greater than 0.5
            }
        );
        
        
        if(parseFloat(data[i].au2).toFixed(2) >0.5){
        	au2count++;
        }
        
    }
    //console.log("date value = "+arr[0].date);
    return arr;
}

function parseAUPredAU3(data) {
    var arr = [];
    //console.log("save = "+data[0].saveTime);
    var i;
    var au3count = 0;
    for (i = 0; i < data.length; i++) {
      //text += cars[i] + "<br>";
   
    	//console.log("pred = "+pred.saveTime);
        arr.push(
            {
                date: new Date(data[i].saveTime),   //date 
                value: parseFloat(data[i].au3).toFixed(2) //convert string to number 
            }
        );
        
        if(parseFloat(data[i].au2).toFixed(2) >0.5){
        	au3count++;
        }
        
    }
   // console.log("au3 pred 0 = "+arr[0].value);
    //console.log("date value = "+arr[0].date);
    return arr;
}

function getAUCounts(data){
	
	
	var arr = [];
	var i;
    var au1count = 0;
    var au2count = 0;
    var au3count = 0;
    for (i = 0; i < data.length; i++) {
   
    	if(parseFloat(data[i].au1).toFixed(2) >0.5){
        	au1count++;
        }
        if(parseFloat(data[i].au2).toFixed(2) >0.1){
        	au2count++;
        }
        
        if(parseFloat(data[i].au3).toFixed(2) >0.0){
        	au3count++;
        }
        
    }
    arr.push({"au":"au1","count":au1count});
    arr.push({"au":"au2","count":au2count});
    arr.push({"au":"au3","count":au3count});
   // console.log("au3 pred 0 = "+arr[0].value);
    //console.log("date value = "+arr[0].date);
    return arr;
}

function parseData(data) {
    var arr = [];
    for (var i in data.bpi) {
        arr.push(
            {
                date: new Date(i),   //date 
                value: +(data.bpi[i]/17000) //convert string to number 
            }
        );
    }
    return arr;
}

function parseData2(data) {
    var arr = [];
    for (var i in data.bpi) {
        arr.push(
            {
                date: new Date(i),   //date 
                value: +((data.bpi[i]+500)/17000)//convert string to number 
            }
        );
    }
    return arr;
}

function parseData3(data) {
    var arr = [];
    for (var i in data.bpi) {
        arr.push(
            {
                date: new Date(i),   //date 
                value: +((data.bpi[i]-500)/17000)//convert string to number 
            }
        );
    }
    return arr;
}

function drawLineChart(data,data2,data3) {
	
    var svgWidth = 600, svgHeight = 400;
    var margin = { top: 20, right: 20, bottom: 30, left: 50 };
    var width = svgWidth - margin.left - margin.right; 
    var height = svgHeight - margin.top - margin.bottom;
    var svg = d3.select('#linechart').append('svg').attr("width", svgWidth).attr("height", svgHeight+30);
    var g = svg.append("g").attr("transform","translate(" + margin.left + "," + margin.top + ")" );
    var x = d3.scaleTime().rangeRound([0, width]);
    var y = d3.scaleLinear().rangeRound([height, 0]);
    var line = d3.line().x(function(d) { 
                            return x(d.date)}).y(function(d) {
                                             return y(d.value)});
                                             
    var line2 = d3.line().x(function(d) { 
                             return x(d.date)}).y(function(d) {
                                                   return y(d.value)});
    var line3 = d3.line().x(function(d) { return x(d.date)}).y(function(d) {return y(d.value)});

    x.domain(d3.extent(data2, function(d) { return d.date }));
    y.domain(d3.extent(data, function(d) { return d.value }));
    
    g.append("g").attr("transform", "translate(0," + height + ")")
                .call(d3.axisBottom(x))
                .select(".domain")
                .append("text")
                .attr("fill", "#fff")
                //.attr("transform", "rotate(-90)")
                .attr("x", 200)   
                .attr("dx", "1em")   
                .attr("text-anchor", "end")  
                .text("Date");
                //.remove();

                
    g.append("g")   
     .call(d3.axisLeft(y))
     .append("text")
     .attr("fill", "#000")
     .attr("transform", "rotate(-90)")
     .attr("y", -40)   
     .attr("dy", "1em")   
     .attr("text-anchor", "end")   
     .text("AU Confidence (0-1)");

    g.append("path")
        .datum(data2)
        .attr("fill", "none")
        .attr("stroke", "steelblue")
        .attr("stroke-linejoin", "round")
        .attr("stroke-linecap", "round")
        .attr("stroke-width", 1.5)
        .attr("d", line);

    g.append("path")
        .datum(data3)
        .attr("fill", "none")
        .attr("stroke", "green")
        .attr("stroke-linejoin", "round")
        .attr("stroke-linecap", "round")
        .attr("stroke-width", 1.5)
        .attr("d", line2);
    
    g.append("path")
        .datum(data)
        .attr("fill", "none")
        .attr("stroke", "orange")
        .attr("stroke-linejoin", "round")
        .attr("stroke-linecap", "round")
        .attr("stroke-width", 1.5)
        .attr("d", line3);
    
    svg.append("g")
    .attr("transform", "translate(" + (width / 2 - 50) + "," + (svgHeight+10) + ")")
    .append("text")
    .text("Action Unit Prediction With Time")
    .attr("class", "title");

}

function drawAuChart(data) {
	console.log("starting to draw chart");
    var svgWidth = 600, svgHeight = 400;
    var margin = { top: 20, right: 20, bottom: 30, left: 50 };
    var width = svgWidth - margin.left - margin.right; 
    var height = svgHeight - margin.top - margin.bottom;
    var svg = d3.select('svg').attr("width", svgWidth).attr("height", svgHeight);
    var g = svg.append("g").attr("transform","translate(" + margin.left + "," + margin.top + ")" );
    var x = d3.scaleTime().rangeRound([0, width]);
    var y = d3.scaleLinear().rangeRound([height, 0]);
    var line = d3.line().x(function(d) { 
                            return x(d.date)}).y(function(d) {
                                             return y(d.value)});
     /*                                        
    var line2 = d3.line().x(function(d) { 
                             return x(d.date)}).y(function(d) {
                                                   return y(d.value)});
    var line3 = d3.line().x(function(d) { return x(d.date)}).y(function(d) {return y(d.value)});
*/
    x.domain(d3.extent(data, function(d) { return d.date }));
    y.domain(d3.extent(data, function(d) { return d.value }));
    
    g.append("g").attr("transform", "translate(0," + height + ")")
                .call(d3.axisBottom(x))
                .select(".domain")
                .append("text")
                .attr("fill", "#fff")
                //.attr("transform", "rotate(-90)")
                .attr("x", 106)   
                .attr("dx", "0.71em")   
                .attr("text-anchor", "end")  
                .text("Date");
                //.remove();

                
    g.append("g")   
     .call(d3.axisLeft(y))
     .append("text")
     .attr("fill", "#000")
     .attr("transform", "rotate(-90)")
     .attr("y", 6)   
     .attr("dy", "1em")   
     .attr("text-anchor", "end")   
     .text("AU Confidence");

    g.append("path")
        .datum(data)
        .attr("fill", "none")
        .attr("stroke", "steelblue")
        .attr("stroke-linejoin", "round")
        .attr("stroke-linecap", "round")
        .attr("stroke-width", 1.5)
        .attr("d", line);

        

}

function drawPieChart(data,len) {

    //var data = [2, 4, 8, 10];

    var svgWidth = 500, svgHeight = 400;
    //var margin = { top: 20, right: 20, bottom: 30, left: 50 };
    var width = svgWidth;// - margin.left - margin.right;
    var height = svgHeight;// - margin.top - margin.bottom;



    var svg = d3.select("#pie_chart").append("svg").attr("width", width).attr("height", height+20),

        radius = Math.min(width, height) / 2;

    var g = svg.append("g")
        .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

    var color = d3.scaleOrdinal(['#6daf4a', '#2daf4a', '#577eb8','#177eb8','#4daf4a', '#377eb8', '#ff7f00', '#984ea3', '#e41a1c']);

    var pie = d3.pie().value(function (d) {
        console.log("d.values()=" + d.count)
        return d.count;
    });

    var path = d3.arc()
        .outerRadius(radius - 10)
        .innerRadius(0);

    var label = d3.arc()
        .outerRadius(radius)
        .innerRadius(radius - 80);

    var arc = g.selectAll(".arc")
        .data(pie(data))
        .enter().append("g")
        .attr("class", "arc");

    arc.append("path")
        .attr("d", path)
        .attr("fill", function (d, i) { console.log("d.keys()="); return color(i); });

    console.log(arc)

    arc.append("text")
        .attr("transform", function (d) {
            return "translate(" + label.centroid(d) + ")";
        })
        .text(function (d) { return d.data.keyword; });

    svg.append("g")
        .attr("transform", "translate(" + (width / 2 - 50) + "," + (height+10) + ")")
        .append("text")
        .text("Effort consolidation("+len+")")
        .attr("class", "title");
    	//.attr("font-weight","bold");

}

function drawbarchart(data) {
	
	console.log("data 0 ="+ JSON.stringify(data[0]));
	console.log("data 1 ="+ data[1]);
	console.log("data 2 ="+ data[2]);
	/*
	data = [
			{
				"au":"au1",
				"value": 100
			},
			{
				"au":"au2",
				"value": 100
			},
			{
				"au":"au3",
				"value": 100
			}
	];
	*/
    var width = 500;
    var height = 300;
   
    
    var svg = d3.select("#bar_chart").append("svg").attr("width", width).attr("height", height+100);


    

 svg.append("text")
   .attr("transform", "translate(" + (width / 2 - 100) + "," + (height+10) + ")")
   .attr("x", 50)
   .attr("y", 50)
   .attr("font-size", "16px")
   .text("Action Unit Counts")
  
   /*
    svg.append("g")
    .attr("transform", "translate(" + (width / 2 - 50) + "," + (svgHeight+10) + ")")
    .append("text")
    .text("Action Unit Counts")
    .attr("class", "title");
  */

var xScale = d3.scaleBand().range([0, width]).padding(0.4),
    yScale = d3.scaleLinear().range([height, 0]);

var g = svg.append("g")
           .attr("transform", "translate(50,0)");

    xScale.domain(data.map(function(d) { return d.au; }));
    yScale.domain([0, d3.max(data, function(d) { return d.count; })]);

    g.append("g")
     .attr("transform", "translate(0," + height + ")")
     .call(d3.axisBottom(xScale))
     .append("text")
     .attr("y", height - 250)
     .attr("x", width - 100)
     .attr("text-anchor", "end")
     .attr("stroke", "black")
     .text("AU");
     

    g.append("g")
     .call(d3.axisLeft(yScale).tickFormat(function(d){
         return "" + d;
     })
     .ticks(10))
     .append("text")
     .attr("transform", "rotate(-90)")
     .attr("y", 6)
     .attr("dy", "-4.1em")
     .attr("text-anchor", "end")
     .attr("stroke", "black")
     .text("AU Count");

    g.selectAll(".bar")
     .data(data)
     .enter().append("rect")
     .attr("class", "bar")
     .attr("x", function(d) { return xScale(d.au); })
     .attr("y", function(d) { return yScale(d.count); })
     .attr("width", xScale.bandwidth())
     .attr("height", function(d) { return height - yScale(d.count); });
    
}

