var lookingDownWin = null;
var queue_head_down = [];
var queue_head_right = [];
var queue_head_left = [];

var head_down_count = 0;
var duration = 0;
var down_sent = -1;
var right_sent = -1;
var left_sent = -1;


window.headmodel = {
    model1: null,
    model2: null,
    model3: null,
    upthreshold: 10,
    downthreshold: -30,
    leftthreshold: -30,
    rightthreshold: 30,
    up: -1,
    down: -1,
    left: -1,
    right: -1,
    calibrated: false,
    calibratePoints: {},
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
		//var lookingDownWin = null
        let pred1 = await headmodel.model1.executeAsync(img);
        let pred2 = await headmodel.model2.executeAsync(img);
        let pred3 = await headmodel.model3.executeAsync(img);
        pred1 = pred1.arraySync();
        pred2 = pred2.arraySync();
        pred3 = pred3.arraySync();

        let yaw = (pred1[0][0] + pred2[0][0] + pred3[0][0]) / 3;
        let pitch = (pred1[0][1] + pred2[0][1] + pred3[0][1]) / 3;
        let roll = (pred1[0][2] + pred2[0][2] + pred3[0][2]) / 3;
        console.log(yaw, pitch, roll);
        headmodel.value = [pitch, yaw];

        if (headmodel.calibrated) {
            if (pitch > headmodel.upthreshold) {
                if (headmodel.up === -1) {
                    headmodel.up = Date.now();
                    headmodel.down = -1;
                } else {
                    if (Date.now() - headmodel.up > 5000) {
                        ui.showInfo("Looking up!", true);
						console.log("===== Looking away - UP =====");
						//showGazeWandering (globals,"Looking up");
						//alert("You were looking UP");
                        //document.getElementById("ok").style.visibility = "visible";
                    }
                }
            }
			//Looking down: 
			else if (pitch < headmodel.downthreshold) {
                if (headmodel.down === -1) {
                    headmodel.down = Date.now();
                    headmodel.up = -1;
                } else {
                    if (Date.now() - headmodel.down > 5000) {
                        //ui.showInfo("Looking down!", true);
						console.log("===== Looking away - Down =====");
						console.log("Down duration: " + (Date.now() - headmodel.down).toString());
						console.log("Score_down: " + Math.abs(pitch - headmodel.downthreshold).toString());
						var down_duration = Date.now() - headmodel.down;
						//head_down_count++;
						
						//var dataString = "===== Looking away - Down =====";
						var d = new Date();
						//var dataString = {"yaw": String(yaw), "pitch": String(pitch), "roll": String(roll), "time": Date.now().toString(), "direction": "Down"};
						var dataString = {"yaw": String(yaw), "pitch": String(pitch), "roll": String(roll), "time_human": d.toString(), "time_milli": Date.now().toString(), 
										  "duration": down_duration.toString(), "direction": "Down"};
						
						//var start_down = (queue_head_down.length > 0) ? (queue_head_down.length - 1) : 0;
						
						queue_head_down.push(dataString);
						console.log("Length of the queue_down: " + queue_head_down.length);
						if (queue_head_down.length > 0) {
							console.log("Peeking front of the queue_down: " + queue_head_down[0]);
							//duration = parseInt(queue_head_down[queue_head_down.length - 1].time_milli) - parseInt(queue_head_down[0].time_milli);
							//console.log("Down duration: " + duration.toString());
						}
						
						if (down_duration > 5000 && down_sent === -1) {
							showGazeWandering (globals,dataString);

							$.ajax({
	        					type: "POST",
	        					url: "WoGazeServlet",
	        					//contentType: "application/json",
	        					data: {myData: JSON.stringify(dataString)},
	        					success: function (xhr, textStatus, errorThrown) {
	            					//alert("Looking Down sent to WoGazeServlet");
									swal({
			 								title: "Looking Down sent to WoGazeServlet",
											confirmButtonColor: "#DD6B55", 
											confirmButtonText: "OK",
											closeOnConfirm: true,
									});
	        					}
	    					});
		
							down_sent = 1;
						} else if (down_duration < 6000)
							down_sent = -1;

						
						//send a ajax post to server
						//lookingDownWin = window.open("http://rose.cs.umass.edu/mathspring/mscontent/LearningCompanion/Jane/frustratedCombo1.html", "_blank", "height=600, width=400, modal=yes, alwaysRaised=yes")
						//if (window.focus) {
							//lookingDownWin.focus()
						//}
						//alert("You were looking DOWN");

                        //document.getElementById("ok").style.visibility = "visible";
                    }
                }
            } else {
                headmodel.up = headmodel.down = -1;
            }
			// Looking to the right:
            if (yaw > headmodel.rightthreshold) {
                if (headmodel.right === -1) {
                    headmodel.right = Date.now();
                    headmodel.left = -1;
                } else {
                    if (Date.now() - headmodel.right > 5000) {
                        //ui.showInfo("Looking right!", true);
						console.log("===== Looking away - right =====");
						console.log("Right duration: " + (Date.now() - headmodel.right).toString());
						console.log("Score_right: " + Math.abs(yaw - headmodel.rightthreshold).toString());
						var right_duration = Date.now() - headmodel.right;
						//alert("You were looking away to the RIGHT");
						
						var d = new Date();
						
						var dataString = {"yaw": String(yaw), "pitch": String(pitch), "roll": String(roll), "time_human": d.toString(), "time_milli": Date.now().toString(), 
										  "duration": right_duration.toString(), "direction": "Right"};
								
						queue_head_right.push(dataString);
						console.log("Length of the queue_right: " + queue_head_right.length);
						if (queue_head_right.length > 0) {
							console.log("Peeking front of the queue_right: " + queue_head_right[0]);
							//duration = parseInt(queue_head_down[queue_head_down.length - 1].time_milli) - parseInt(queue_head_down[0].time_milli);
							//console.log("Down duration: " + duration.toString());
						}
						
						if (right_duration > 18000 && right_sent === -1) {
							var audio = new Audio('airport_sound.mp3');
							audio.play();
							await new Promise(r => setTimeout(r, 2000));
							audio.play();

							
							//var w = transparencyElement.width;
							//var h = transparencyElement.height;
							//var d = transparencyElement.display;
							
							//var transparency = document.getElementById("transbox");
							
							//var poverlay = new PlainOverlay(document.getElementById('problemWindow'), {duration: 400}, {style: {backgroundColor: 'rgba(255, 255, 255, 0.6)'}});
							//poverlay.show();
							$('body').plainOverlay('show')
							await new Promise(r => setTimeout(r, 2000));
							//poverlay.hide();
							$('body').plainOverlay('hide')
							
							$.ajax({
	        					type: "POST",
	        					url: "WoGazeServlet",
	        					//contentType: "application/json",
	        					data: {myData: JSON.stringify(dataString)},
	        					success: function (xhr, textStatus, errorThrown) {
	            					//alert("Looking Down sent to WoGazeServlet");
									swal({
			 								title: "Looking Right sent to WoGazeServlet",
											confirmButtonColor: "#DD6B55", 
											confirmButtonText: "OK",
											closeOnConfirm: true,
									});
	        					}
	    					});
		
							right_sent = 1;
						} else if (right_duration < 6000)
							right_sent = -1;
						
						/*swal({
 								title: "You were looking away to the RIGHT",
								confirmButtonColor: "#DD6B55", 
								confirmButtonText: "OK",
								closeOnConfirm: true,
							});*/

                        //document.getElementById("ok").style.visibility = "visible";
                    }
                }
            }
			//Looking left: 
			else if (yaw < headmodel.leftthreshold) {
                if (headmodel.left === -1) {
                    headmodel.left = Date.now();
                    headmodel.right = -1;
                } else {
                    if (Date.now() - headmodel.left > 5000) {
                        //ui.showInfo("Looking left!", true);
						console.log("===== Looking away - Left =====");
						console.log("Left duration: " + (Date.now() - headmodel.left).toString());
						console.log("Score_left: " + Math.abs(yaw - headmodel.leftthreshold).toString());
						var left_duration = Date.now() - headmodel.left;
						//alert("You were looking away to the LEFT");
						
						var d = new Date();
						
						var dataString = {"yaw": String(yaw), "pitch": String(pitch), "roll": String(roll), "time_human": d.toString(), "time_milli": Date.now().toString(), 
										  "duration": left_duration.toString(), "direction": "Left"};
								
						
						queue_head_left.push(dataString);
						console.log("Length of the queue_left: " + queue_head_left.length);
						if (queue_head_left.length > 0) {
							console.log("Peeking front of the queue_left: " + queue_head_left[0]);
						}
							
						if (left_duration > 5000 && left_sent === -1) { 
							showGazeWandering (globals,JSON.stringify(dataString));
							/*
							$.ajax({
	        					type: "POST",
	        					url: "WoGazeServlet",
	        					//contentType: "application/json",
	        					data: {myData: JSON.stringify(dataString)},
	        					success: function (xhr, textStatus, errorThrown) {
	            					//alert("Looking Down sent to WoGazeServlet");
									swal({
			 								title: "Looking left sent to WoGazeServlet",
											confirmButtonColor: "#DD6B55", 
											confirmButtonText: "OK",
											closeOnConfirm: true,
									});
	        					}
	    					});
							*/
							left_sent = 1;
						} else if (left_duration < 6000)
							left_sent = -1;
						
						
						/*swal({
 								title: "You were looking away to the LEFT",
								confirmButtonColor: "#DD6B55", 
								confirmButtonText: "OK",
								closeOnConfirm: true,
							});*/
							
                        //document.getElementById("ok").style.visibility = "visible";
                    }
                }
            } else{
                headmodel.left = headmodel.right = -1;
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