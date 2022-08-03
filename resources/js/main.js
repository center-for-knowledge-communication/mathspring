const webcamElement = document.getElementById('webcam');
//const transparencyElement = document.getElementById("transbox");

async function appGaze() {


    if (globals.gazeDetectionOn == 1) {    	

    	$('#ok').click(function (e) {
	        ui.showInfo("Detecting...", true);
	        document.getElementById("ok").style.visibility = "hidden";
	        headmodel.up = headmodel.down = headmodel.left = headmodel.right = -1;
	    });

	    await gazeSetupWebcam();
	
	    ui.showInfo("Loading model...", true);
	
	    await faceapi.loadSsdMobilenetv1Model('https://test-au.s3.amazonaws.com/ssd_mobilenetv1_model-weights_manifest.json');
	
	    /* Display face landmarks */
	    await faceapi.loadFaceLandmarkModel('https://test-au.s3.amazonaws.com/face_landmark_68_model-weights_manifest.json');
	
	    await headmodel.loadModel();
	    //ui.showInfo("Calibration...", true);
	    headmodel.calibrated = true;
	    ui.showInfo("Detecting...", true);
	    await appGazeStart();

    }
	
}

 async function appGazeStart() {
    if (this.timer) {
      this.stop();
    }
    //this.video.play();
    this.timer = requestAnimationFrame(this.doGazePrediction.bind(this));
}

function appGazeStop() {
    //this.video.pause();
    cancelAnimationFrame(this.timer);
}

async function doGazePrediction(){

	const displaySize = { width: webcamElement.width, height: webcamElement.height };
    const detections = await faceapi.detectAllFaces(webcamElement);

	const resizedResults = faceapi.resizeResults(detections, displaySize);
    // console.log(detections)
    // faceapi.draw.drawDetections(document.getElementById('overlay'), resizedResults)

    if (resizedResults.length >= 1) {
        img = headmodel.getImage(resizedResults[0]);

        
        // Sleep for 1 second
        await new Promise(r => setTimeout(r, 1000));
        
        await headmodel.predict(img);
    }

	this.timer = requestAnimationFrame(this.doGazePrediction.bind(this));
}



async function gazeSetupWebcam() {
  return new Promise((resolve, reject) => {
    const navigatorAny = navigator;
    navigator.getUserMedia = navigator.getUserMedia ||
        navigatorAny.webkitGetUserMedia || navigatorAny.mozGetUserMedia ||
        navigatorAny.msGetUserMedia;
    if (navigator.getUserMedia) {
      navigator.getUserMedia({video: true},
        stream => {
          webcamElement.srcObject = stream;
          webcamElement.addEventListener('loadeddata',  () => resolve(), false);
        },
        error => reject());
    } else {
      reject();
    }
  });
}

// function mouseMove() {
//     head.up = head.down = head.left = head.right = -1;
// }

if (globals.gazeDetectionOn == 1) {    	
	appGaze();
}

// document.addEventListener('mousemove', mouseMove, false);