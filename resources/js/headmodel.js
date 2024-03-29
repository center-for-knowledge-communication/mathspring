// Frank	07-03-21	v1.0.1	added logging of Wandering state with sample_rate 4000ms
// Frank	07-03-21	v1..0.1 comment out excessive console logging

var queue_head_up = [];
var queue_head_down = [];
var queue_head_right = [];
var queue_head_left = [];


var duration = 0;
var last_sent = -1;

window.headmodel = {
    model1: null,
    model2: null,
    model3: null,
    upthreshold: globals.gazeParamsJSON.gazwndr_up_degrees,
    downthreshold: globals.gazeParamsJSON.gazwndr_down_degrees,
    leftthreshold: globals.gazeParamsJSON.gazwndr_left_degrees,
    rightthreshold: globals.gazeParamsJSON.gazwndr_right_degrees,
    duration:  globals.gazeParamsJSON.gazwndr_sec,
    nextmsg_min: globals.gazeParamsJSON.gazwndr_nextmsg_min * 60000, 
    free_passes: globals.gazeParamsJSON.gazwndr_free_passes,
    sample_rate: globals.gazeParamsJSON.gazwndr_sample_rate * 1000,
    modelConfidence: 0,
    action: "",
    up: -1,
    down: -1,
    left: -1,
    right: -1,
    calibrated: false,
    calibratePoints: {},
    last_sent: -1,
    last_sample: -1,
    wanderingMsg: "No",
    WanderingAxis: "",
    value: null,

    getImage: function(bbox) {
        // Capture the current image in the eyes canvas as a tensor.
        return tf.tidy(function() {
            var image = tf.browser.fromPixels(webcamElement).expandDims(0);

            image = tf.image.cropAndResize(image, [[Math.max(bbox.box.topLeft.y - 0.6 * bbox.box.height, 0) / webcamElement.height,
                Math.max(bbox.box.topLeft.x - 0.6 * bbox.box.width, 0) / webcamElement.width,
                Math.min(bbox.box.topLeft.y + 1.6 * bbox.box.height, webcamElement.height) / webcamElement.height,
                Math.min(bbox.box.topLeft.x + 1.6 * bbox.box.width, webcamElement.width) / webcamElement.width]], [0], [64, 64]);

            return image;
        });
    },

    predict: async function (img) {
        let pred1 = await headmodel.model1.executeAsync(img);
        let pred2 = await headmodel.model2.executeAsync(img);
        let pred3 = await headmodel.model3.executeAsync(img);
        pred1 = pred1.arraySync();
        pred2 = pred2.arraySync();
        pred3 = pred3.arraySync();

        let yaw = (pred1[0][0] + pred2[0][0] + pred3[0][0]) / 3;
        let pitch = (pred1[0][1] + pred2[0][1] + pred3[0][1]) / 3;
        let roll = (pred1[0][2] + pred2[0][2] + pred3[0][2]) / 3;
        //console.log(yaw, pitch, roll);
        headmodel.value = [pitch, yaw];
        headmodel.duration  = globals.gazeParamsJSON.gazwndr_sec * 1000;

        headmodel.wanderingAxis = "";
        
        if (headmodel.calibrated) {
    	    if (globals.gazeParamsJSON.gazinterv_monitor_on === 1) {
    	    	document.getElementById("monitorBox").style.visibility = "visible";	    
    	    	document.getElementById("gazeMonitor1").innerHTML = "Gaze on Screen<br>";
    	    	document.getElementById("gazeMonitor2").innerHTML = "  Yaw: " + yaw.toString().substring(0,6) + "<br>";
    	    	document.getElementById("gazeMonitor3").innerHTML = "Pitch: " + pitch.toString().substring(0,6) + "<br>";
    	    	document.getElementById("gazeMonitor5").innerHTML = globals.gazeWanderingUI;
    	    	document.getElementById("gazeMonitor6").innerHTML = "Free passes left: " + headmodel.free_passes + " of " + globals.gazeParamsJSON.gazwndr_free_passes;
    	    }
    	    else {
    	    	document.getElementById("monitorBox").style.visibility = "hidden";	    
    	    }
        	if (headmodel.downthreshold > 0)
        		headmodel.downthreshold = headmodel.downthreshold * -1;
        	if (headmodel.leftthreshold > 0)
        		headmodel.leftthreshold = headmodel.leftthreshold * -1;
        	if (headmodel.upthreshold < 0)
        		headmodel.upthreshold = headmodel.upthreshold * -1;
        	if (headmodel.rightthreshold < 0)
        		headmodel.rightthreshold = headmodel.rightthreshold * -1;

        	headmodel.wanderingMsg = "No";
    	    document.getElementById("gazeMonitor4").innerHTML = "<br>";
			// Looking to the right:
            if (!(headmodel.wanderingAxis === "Y")) {
	            if (yaw > headmodel.rightthreshold) {
	                if (headmodel.right === -1) {
	                    headmodel.right = Date.now();
	                    headmodel.left = -1;
	                } else {
	                    headmodel.wanderingAxis = "X";
	                	var now = Date.now();
						var right_duration = now - headmodel.right;
	    				document.getElementById("gazeMonitor1").innerHTML = "Gaze off Screen? " + Math.floor(right_duration/1000) + "secs<br>";
	                    if (right_duration > headmodel.duration) {
	            	    	globals.gazeWanderingUI = "";
	            	    	document.getElementById("gazeMonitor5").innerHTML = "";
							//console.log("Right duration: " + (Date.now() - headmodel.right).toString());
							//console.log("Score_right: " + Math.abs(yaw - headmodel.rightthreshold).toString());
							
							var d = new Date();
																
							
							if (now > (last_sent + headmodel.nextmsg_min)) { 
								if (headmodel.free_passes <= 0) {
									headmodel.action = "Intervention";
									headmodel.wanderingMsg = "Yes - Intervention";
								}
								else {
									headmodel.action = "FreePass";
									last_sent = last_sent + headmodel.nextmsg_min;
									headmodel.wanderingMsg = "Yes - Free pass";
									headmodel.free_passes = headmodel.free_passes - 1;
    	    	                    document.getElementById("gazeMonitor6").innerHTML = "Free passes left: " + headmodel.free_passes + " of " + globals.gazeParamsJSON.gazwndr_free_passes;
								}
								var dataString = {"yaw": String(yaw), "pitch": String(pitch), "roll": String(roll),  
										  "duration": right_duration.toString(), "direction": "Right", "modelConfidence": String(headmodel.modelConfidence), "action": String(headmodel.action) };
								//var temp = JSON.stringify(dataString);
								queue_head_right.push(dataString);
								console.log("Length of the queue_right: " + queue_head_right.length);
								if (queue_head_right.length > 0) {
									console.log("Peeking front of the queue_right: " + queue_head_right[0]);
								}
								showGazeWandering (globals,JSON.stringify(dataString));
								queue_head_right.splice(0);
								last_sent = now;
								headmodel.right = -1;
							}
							else {
								if (right_duration < headmodel.nextmsg_min) {
									headmodel.wanderingMsg = "Yes - Paused";
									headmodel.action = "Wandering - Paused";
								}
			                	else {
			                		headmodel.wanderingMsg = "Yes";
									headmodel.action = "Wandering";
			                	}                	
								var dataString = {"yaw": String(yaw), "pitch": String(pitch), "roll": String(roll),  
										  "duration": right_duration.toString(), "direction": "Right", "modelConfidence": String(headmodel.modelConfidence), "action": String(headmodel.action) };
								//var temp = JSON.stringify(dataString);
								queue_head_right.push(dataString);
								console.log("Length of the queue_right: " + queue_head_right.length);
								if (queue_head_right.length <= 0) {
									console.log("Peeking front of the queue_right: " + queue_head_right[0]);
								}
								if ((headmodel.sample_rate > 0) && (now > (headmodel.last_sample + headmodel.sample_rate))) {
								    showGazeWandering (globals,JSON.stringify(dataString));
								    headmodel.last_sample = now;
								}
							}
	                    }
	                	else {
	                		headmodel.wanderingMsg = "No";
	                	}                	
	                }
	                if (headmodel.wanderingMsg === "No") {                	
	                	document.getElementById("gazeMonitor4").innerHTML = "<br>";
	                }
	                else {
	                	document.getElementById("gazeMonitor4").innerHTML = "Wandering? " + headmodel.wanderingMsg + "<br>";
	                }
	            }
				//Looking left: 
				else if (yaw < headmodel.leftthreshold) {
	                if (headmodel.left === -1) {
	                    headmodel.left = Date.now();
	                    headmodel.right = -1;
	                } else {
	                    headmodel.wanderingAxis = "X";
	                	var now = Date.now();
						var left_duration = now - headmodel.left;
	    				document.getElementById("gazeMonitor1").innerHTML = "Gaze off Screen? " + Math.floor(left_duration/1000) + "secs<br>";
	                    if (left_duration > headmodel.duration) {
	            	    	globals.gazeWanderingUI = "";
	            	    	document.getElementById("gazeMonitor5").innerHTML = "";
							//console.log("Left duration: " + (Date.now() - headmodel.left).toString());
							//console.log("Score_left: " + Math.abs(yaw - headmodel.leftthreshold).toString());
							
							var d = new Date();
							
								
							if (now > (last_sent + headmodel.nextmsg_min)) { 
								if (headmodel.free_passes <= 0) {
									headmodel.action = "Intervention";
									headmodel.wanderingMsg = "Yes - Intervention";
								}
								else {
									headmodel.action = "FreePass";
									last_sent = last_sent + headmodel.nextmsg_min;
									headmodel.wanderingMsg = "Yes - Free pass";
									headmodel.free_passes = headmodel.free_passes - 1;
    	    	                    document.getElementById("gazeMonitor6").innerHTML = "Free passes left: " + headmodel.free_passes + " of " + globals.gazeParamsJSON.gazwndr_free_passes;
								}
								var dataString = {"yaw": String(yaw), "pitch": String(pitch), "roll": String(roll),  
										  "duration": left_duration.toString(), "direction": "Left", "modelConfidence": String(headmodel.modelConfidence), "action": String(headmodel.action) };
								queue_head_left.push(dataString);
								console.log("Length of the queue_left: " + queue_head_left.length);
								if (queue_head_left.length > 0) {
									console.log("Peeking front of the queue_left: " + queue_head_left[0]);
								}
								//var temp = JSON.stringify(dataString);
								showGazeWandering (globals,JSON.stringify(dataString));
								queue_head_left.splice(0);
								last_sent = now;
								headmodel.left = -1;
							}

							else {
								if (left_duration < headmodel.nextmsg_min) {
									headmodel.wanderingMsg = "Yes - Paused";
									headmodel.action = "Wandering - Paused";
								}
			                	else {
			                		headmodel.wanderingMsg = "Yes";
									headmodel.action = "Wandering";
			                	}                	
								var dataString = {"yaw": String(yaw), "pitch": String(pitch), "roll": String(roll),  
										  "duration": left_duration.toString(), "direction": "Left", "modelConfidence": String(headmodel.modelConfidence), "action": String(headmodel.action) };
								//var temp = JSON.stringify(dataString);
								queue_head_left.push(dataString);
								console.log("Length of the queue_left: " + queue_head_left.length);
								if (queue_head_left.length <= 0) {
									console.log("Peeking front of the queue_left: " + queue_head_left[0]);
								}
								if ((headmodel.sample_rate > 0) && (now > (headmodel.last_sample + headmodel.sample_rate))) {
								    showGazeWandering (globals,JSON.stringify(dataString));
								    headmodel.last_sample = now;
								}
							}
	                    }
	                	else {
	                		headmodel.wanderingMsg = "No";
	                	}                	
		                if (headmodel.wanderingMsg === "No") {                	
		                	document.getElementById("gazeMonitor4").innerHTML = "<br>";
		                }
		                else {
		                	document.getElementById("gazeMonitor4").innerHTML = "Wandering? " + headmodel.wanderingMsg + "<br>";
		                }
	                }
	            } else {
	                headmodel.left = headmodel.right = -1;
	                headmodel.wanderingAxis === "";
	            }
	        }
    	}
        if (!(headmodel.wanderingAxis === "X")) {
	        if (pitch > headmodel.upthreshold) {
	            if (headmodel.up === -1) {
	                headmodel.up = Date.now();
	                headmodel.down = -1;
	            } else {
	            	var now = Date.now();
					var up_duration = now - headmodel.up;
					document.getElementById("gazeMonitor1").innerHTML = "Gaze off Screen? " + Math.floor(up_duration/1000) + "secs<br>";
	                if (up_duration > headmodel.duration) {
	        	    	globals.gazeWanderingUI = "";
	        	    	document.getElementById("gazeMonitor5").innerHTML = "";
						//console.log("Up duration: " + (Date.now() - headmodel.up).toString());
						//console.log("Score_up: " + Math.abs(pitch - headmodel.upthreshold).toString());
						
						var d = new Date();
	
						if (now > (last_sent + headmodel.nextmsg_min)) { 
							if (headmodel.free_passes <= 0) {
								headmodel.action = "Intervention";
								headmodel.wanderingMsg = "Yes - Intervention";
							}
							else {
								headmodel.action = "FreePass";
								last_sent = last_sent + headmodel.nextmsg_min;
								headmodel.wanderingMsg = "Yes - Free pass";
								headmodel.free_passes = headmodel.free_passes - 1;
	    	                    document.getElementById("gazeMonitor6").innerHTML = "Free passes left: " + headmodel.free_passes + " of " + globals.gazeParamsJSON.gazwndr_free_passes;
							}
							var dataString = {"yaw": String(yaw), "pitch": String(pitch), "roll": String(roll),  
									  "duration": up_duration.toString(), "direction": "Up", "modelConfidence": String(headmodel.modelConfidence), "action": String(headmodel.action) };
							queue_head_up.push(dataString);
							console.log("Length of the queue_up: " + queue_head_up.length);
							if (queue_head_up.length > 0) {
								console.log("Peeking front of the queue_up: " + queue_head_up[0]);
							}
		
							//var temp = JSON.stringify(dataString);
							showGazeWandering (globals,JSON.stringify(dataString));
							queue_head_up.splice(0);
							last_sent = now;
							headmodel.up = -1;
						}

						else {
							if (up_duration < headmodel.nextmsg_min) {
								headmodel.wanderingMsg = "Yes - Paused";
								headmodel.action = "Wandering - Paused";
							}
		                	else {
		                		headmodel.wanderingMsg = "Yes";
								headmodel.action = "Wandering";
		                	}                	
							var dataString = {"yaw": String(yaw), "pitch": String(pitch), "roll": String(roll),  
									  "duration": up_duration.toString(), "direction": "Up", "modelConfidence": String(headmodel.modelConfidence), "action": String(headmodel.action) };
							//var temp = JSON.stringify(dataString);
							queue_head_up.push(dataString);
							console.log("Length of the queue_down: " + queue_head_up.length);
							if (queue_head_up.length <= 0) {
								console.log("Peeking front of the queue_up: " + queue_head_up[0]);
							}
							if ((headmodel.sample_rate > 0) && (now > (headmodel.last_sample + headmodel.sample_rate))) {
							    showGazeWandering (globals,JSON.stringify(dataString));
							    headmodel.last_sample = now;
							}
						}

	                }
	            	else {
	            		headmodel.wanderingMsg = "No";
	            	}                	
	                if (headmodel.wanderingMsg === "No") {                	
	                	document.getElementById("gazeMonitor4").innerHTML = "<br>";
	                }
	                else {
	                	document.getElementById("gazeMonitor4").innerHTML = "Wandering? " + headmodel.wanderingMsg + "<br>";
	                }
	            }
	        }
			//Looking down: 
			else if (pitch < headmodel.downthreshold) {
	            if (headmodel.down === -1) {
	                headmodel.down = Date.now();
	                headmodel.up = -1;
	            } else {
	            	var now = Date.now();
					var down_duration = now - headmodel.down;
					document.getElementById("gazeMonitor1").innerHTML = "Gaze on Screen? " + Math.floor(down_duration/1000) + "secs<br>";
	                if (down_duration > headmodel.duration) {
	        	    	globals.gazeWanderingUI = "";
	        	    	document.getElementById("gazeMonitor5").innerHTML = "";
						//console.log("Down duration: " + (Date.now() - headmodel.down).toString());
						//console.log("Score_down: " + Math.abs(pitch - headmodel.downthreshold).toString());
						
						var d = new Date();
						
						
						if (now > (last_sent + headmodel.nextmsg_min)) { 
							if (headmodel.free_passes <= 0) {
								headmodel.action = "Intervention";
								headmodel.wanderingMsg = "Yes - Intervention";
							}
							else {
								headmodel.action = "FreePass";
								last_sent = last_sent + headmodel.nextmsg_min;
								headmodel.wanderingMsg = "Yes - Free pass";
								headmodel.free_passes = headmodel.free_passes - 1;
	    	                    document.getElementById("gazeMonitor6").innerHTML = "Free passes left: " + headmodel.free_passes + " of " + globals.gazeParamsJSON.gazwndr_free_passes;
							}
							var dataString = {"yaw": String(yaw), "pitch": String(pitch), "roll": String(roll),  
									  "duration": down_duration.toString(), "direction": "Down", "modelConfidence": String(headmodel.modelConfidence), "action": String(headmodel.action) };
							queue_head_down.push(dataString);
							console.log("Length of the queue_down: " + queue_head_down.length);
							if (queue_head_down.length <= 0) {
								console.log("Peeking front of the queue_down: " + queue_head_down[0]);
							}
							//var temp = JSON.stringify(dataString);
							showGazeWandering (globals,JSON.stringify(dataString));
							queue_head_down.splice(0);
							last_sent = now;
							headmodel.down = -1;
						}

						else {
							if (down_duration < headmodel.nextmsg_min) {
								headmodel.wanderingMsg = "Yes - Paused";
								headmodel.action = "Wandering - Paused";
							}
		                	else {
		                		headmodel.wanderingMsg = "Yes";
								headmodel.action = "Wandering";
		                	}                	
							var dataString = {"yaw": String(yaw), "pitch": String(pitch), "roll": String(roll),  
									  "duration": down_duration.toString(), "direction": "Down", "modelConfidence": String(headmodel.modelConfidence), "action": String(headmodel.action) };
							//var temp = JSON.stringify(dataString);
							queue_head_down.push(dataString);
							console.log("Length of the queue_down: " + queue_head_down.length);
							if (queue_head_down.length <= 0) {
								console.log("Peeking front of the queue_down: " + queue_head_down[0]);
							}
							if ((headmodel.sample_rate > 0) && (now > (headmodel.last_sample + headmodel.sample_rate))) {
							    showGazeWandering (globals,JSON.stringify(dataString));
							    headmodel.last_sample = now;
							}
						}
	                }
	            	else {
	            		headmodel.wanderingMsg = "No";
	            	}                	
	                if (headmodel.wanderingMsg === "No") {                	
	                	document.getElementById("gazeMonitor4").innerHTML = "<br>";
	                }
	                else {
	                	document.getElementById("gazeMonitor4").innerHTML = "Wandering? " + headmodel.wanderingMsg + "<br>";
	                }
	            }
	        } else {
	            headmodel.up = headmodel.down = -1;
	            headmodel.wanderingAxis === "";
	        }
    	}
    },

    loadModel: async function () {
        headmodel.model1 = await tf.loadGraphModel("https://storage.googleapis.com/jsmodel/fsa/model1/model.json");
        headmodel.model2 = await tf.loadGraphModel("https://storage.googleapis.com/jsmodel/fsa/model2/model.json");
        headmodel.model3 = await tf.loadGraphModel("https://storage.googleapis.com/jsmodel/fsa/model3/model.json");
        tf.print("model loaded");
    },

};