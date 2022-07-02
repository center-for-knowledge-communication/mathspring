const webcamElement = document.getElementById('webcam');

async function app() {

/*
    $('#next').click(async function (e) {
        diff = await selectdiff.predictDiff();
        ui.showInfo("Problem Difficulty: " + diff, true);
        selectdiff.affect_features = [];
        selectdiff.facial_features = [];
    });
*/

    await setupWebcam();


    	console.log("gazing webcam");
    	document.getElementById("webcam").style.visibility = "hidden";	    
    	document.getElementById("webcam").style.zIndex = "3";    	
    	document.getElementById("monitorBox").style.visibility = "hidden";	    
    	document.getElementById("monitorBox").style.zIndex = "3";    	
        document.getElementById("previewInitStatus").innerHTML = initializing_camera;
    	//ui.showInfo("Loading model...", true);
    console.log("Loading model...");

    await faceapi.loadSsdMobilenetv1Model('https://test-au.s3.amazonaws.com/ssd_mobilenetv1_model-weights_manifest.json');

    console.log("Loading model FaceLandmarkModel...");
    /* Display face landmarks */
    await faceapi.loadFaceLandmarkModel('https://test-au.s3.amazonaws.com/face_landmark_68_model-weights_manifest.json');

    await selectdiff.loadModel();
//    ui.showInfo("Detecting...", true);
    
    console.log("Detecting....");
 
    
    document.getElementById("previewInitStatus").innerHTML = camera_initialized;

    highlightNextProbButton();
    
    await start();

	
}

 async function start() {
    if (this.timer) {
      this.stop();
    }
    //this.video.play();
    this.timer = requestAnimationFrame(this.doPrediction.bind(this));
}

function stop() {
    //this.video.pause();
    cancelAnimationFrame(this.timer);
}

async function doPrediction(){

	const displaySize = { width: webcamElement.width, height: webcamElement.height };
	const detections = await faceapi.detectAllFaces(webcamElement);

	const resizedResults = faceapi.resizeResults(detections, displaySize);
    // console.log(detections)
    // faceapi.draw.drawDetections(document.getElementById('overlay'), resizedResults)

    if (resizedResults.length >= 1) {
        img = selectdiff.getImage(resizedResults[0]);
        await selectdiff.predict(img);
    }

	this.timer = requestAnimationFrame(this.doPrediction.bind(this));
}



async function setupWebcam() {
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

app();