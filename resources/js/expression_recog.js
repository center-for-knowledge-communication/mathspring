const webcamElement =  document.createElement('video');
//webcamElement.setAttribute("srcObject","");


//document.getElementById('webcam');
//const webcamElement2 = document.getElementById('webcam2');
//const imgEl = document.getElementById('my_pic');
//const pred = document.getElementById('pred_val');


//const modelpath = 'https://test-au.s3.amazonaws.com/mtcnn_model-weights_manifest.json'
/*
const PARAMS = {
    minFaceSize: 50,
    scaleFactor: 0.709,
    maxNumScales: 10,
    scoreThresholds: [0.7, 0.7, 0.7],
};

const imageSize = 250;
const NORMALIZATION_OFFSET = 255;

let net;
let face_api;
let audetector;
let img;
let face_canvas;

this.face_canvas = document.getElementById('facecanvas');
*/

async function start_capture() {
	console.log("studId="+globals.studId);
	 //webcamElement = document.createElement('video');//getElementById('webcam');
	 imgEl = document.getElementById('my_pic');
	 pred = document.getElementById('pred_val');
	
	modelpath = 'https://test-au.s3.amazonaws.com/mtcnn_model-weights_manifest.json'
		 PARAMS = {
		    minFaceSize: 50,
		    scaleFactor: 0.709,
		    maxNumScales: 10,
		    scoreThresholds: [0.7, 0.7, 0.7],
		};

		 imageSize = 250;
		 NORMALIZATION_OFFSET = 255;

		 //net;
		// face_api;
		 //audetector;
		 //img;
		// face_canvas;
		face_canvas = document.getElementById('facecanvas');
    // Load the model.

    //await initModels();

    //Loading face-api model
    await setupWebcam();

    await faceapi.loadSsdMobilenetv1Model('https://test-au.s3.amazonaws.com/ssd_mobilenetv1_model-weights_manifest.json');

    // Loading AU detector model  
    console.log('loading au detector from aws')
    audetector = await tf.loadLayersModel('https://test-au.s3.amazonaws.com/landmark/model.json');
    console.log('loaded au detector from aws')

    const displaySize = { width: 250, height: 250 };
    // resize the overlay canvas to the input dimensions
    const canvas = document.getElementById('landmarkcanvas');


    /* Display face landmarks */

    await faceapi.loadFaceLandmarkModel('https://test-au.s3.amazonaws.com/face_landmark_68_model-weights_manifest.json');
    await start();

}

async function start() {
    if (this.timer) {
        this.stop();
    }
    this.timer = requestAnimationFrame(this.doPrediction.bind(this));
}

function stop() {
    this.video.pause();
    cancelAnimationFrame(this.timer);
}

async function doPrediction() {

	console.log("about to make prediction");
    const imgEl = webcamElement;
    //const imgEl2 = webcamElement2;
    const displaySize = { width: 250, height: 250 };

    //croping face from 
    console.log(imgEl);
    console.log("imgEl source = "+imgEl.srcObject);

   // console.log("imgEl2 source = "+imgEl2.srcObject);
    //console.log("imgEl ==imgEl2  = "+imgEl.srcObject===imgEl2.srcObject);
    faces = await faceapi.detectAllFaces(imgEl);
    console.log("original faces ="+faces);
   // faces2 = await faceapi.detectAllFaces(imgEl2);
   // console.log("faces2 ="+faces2);
    face = [];
    face = await findAndExtractFaces3(imgEl, faces);
    console.log("face[0]="+face);
    if (face[0]) {

       // face_pixel = await tf.browser.fromPixels(face[0]);
        //console.log('face image');
       // //console.log(face_pixel);
      //  face_pixel = tf.image.resizeBilinear(face_pixel, [250, 250]);
     //   gray_pixel = rgb_to_gray(face_pixel);

      //  reshaped_face = gray_pixel.reshape([1, ...gray_pixel.shape]);
       // //console.log(reshaped_face.shape)

        const detectionsWithLandmarks = await faceapi.detectAllFaces(imgEl).withFaceLandmarks();

        console.log("detectionsWithLandmarks");
        if(detectionsWithLandmarks[0]){
        	features = get_features(detectionsWithLandmarks[0].landmarks);

            input_feature = tf.tensor([features]);

            //console.log(input_feature.shape);
            output = audetector.predict(input_feature);

            // Save predictions on the component
            predictions = Array.from(output.dataSync());
            predStr = predictions[0] > 0.5 ? "A4 " : "";
            predStr += predictions[1] > 0.5 ? "A9 " : "";
            predStr += predictions[2] > 0.5 ? "A12 " : "";
            document.getElementById("pred_val").value = predStr;
            auApi(predictions);
        }
        else {
        	console.log("detectionsWithLandmarks[0] was empty!");
        }
        

    }
    else {
    	console.log("face[0] was empty!");
    }

    this.timer = requestAnimationFrame(this.doPrediction.bind(this));
}

async function auApi(predictions) {

	console.log("about to save prediction");
    var current_timestamp = new Date();

    var jsonObj = '{"studentId":'+globals.studId+',"au1":"' + predictions[0] + '","au2":"' + predictions[1] + '","au3":"' + predictions[2] + '","saveTime":"' + current_timestamp.toISOString() + '"}';

    //console.log(jsonObj);
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            //alert(this.responseText);
        	console.log("prediction saved")
        }
        else if (this.status != 200){
        	console.log("save prediction api call failed with status "+ this.status);
        }
    };
    xhttp.open("POST", "http://ec2-34-203-204-126.compute-1.amazonaws.com:80/AUPredictor/saveAuPred", true);
    xhttp.setRequestHeader("Content-type", "application/json");
    xhttp.send(jsonObj);
}


function get_features(landmarks) {

    const jawOutline = landmarks.getJawOutline();
    const noseline = landmarks.getNose();
    const mouth = landmarks.getMouth();
    const leftEye = landmarks.getLeftEye();
    const rightEye = landmarks.getRightEye();
    const leftEyeBrow = landmarks.getLeftEyeBrow();
    const rightEyeBrow = landmarks.getRightEyeBrow();

    /*
    (jEStart, jEEnd) = (0, 16)
    (rEBStart, rEBEnd) = (17, 21)
    (lEBStart, lEBEnd) = (22, 26)
    (nStart, nEnd) = (27, 35)
    (rEStart, rEEnd) = (36, 41)
    (lEStart, lEEnd) = (42,47)
    (mStart, mEnd) = (48, 67)
    */

    // Left eye
    /*
    (x,y) = (leftEye[1].x + leftEye[2].x)/2;
        (x2,y2) = shape[lEStart+2];
        */
    f1x = (leftEye[1].x + leftEye[2].x) / 2;
    f1y = (leftEye[1].y + leftEye[2].y) / 2;
    /*
        (x3,y3) = shape[lEEnd];
        (x32,y32) = shape[lEEnd-1];
        f2x = (x3+x32)/2;
        f2y = (y3+y32)/2;
        */

    f2x = (leftEye[4].x + leftEye[5].x) / 2;
    f2y = (leftEye[4].y + leftEye[5].y) / 2;

    /*
    (f3x,f3y) = shape[lEStart];
    (f4x,f4y) = shape[lEEnd-2];

    */

    f3x = leftEye[0].x;
    f3y = leftEye[0].y;

    f4x = leftEye[3].x;
    f4y = leftEye[3].y;

    //right eye

    /*
      (rx,ry) = shape[rEStart+1];
      (rx2,ry2) = shape[rEStart+2];
      f5x = (rx+rx2)/2;
      f5y = (ry+ry2)/2;
  
      */

    f5x = (rightEye[1].x + rightEye[2].x) / 2;
    f5y = (rightEye[1].y + rightEye[2].y) / 2;


    /*
        (rx3,ry3) = shape[rEEnd];
        (rx32,ry32) = shape[rEEnd+1];
        f6x = (rx3+rx32)/2;
        f6y = (ry3+ry32)/2;
      */

    f6x = (rightEye[4].x + rightEye[5].x) / 2;
    f6y = (rightEye[4].y + rightEye[5].y) / 2;
    /*
      (f7x,f7y) = shape[rEStart];
      (f8x,f8y) = shape[rEEnd-2];
    */

    f7x = rightEye[0].x;
    f7y = rightEye[0].y;

    f8x = rightEye[3].x;
    f8y = rightEye[3].y;




    // nose
    /*
        (f9x,f9y) = shape[nStart+3];
    */
    f9x = noseline[3].x;
    f9y = noseline[3].y;

    //Left eyebrow left corner (F10), Left eyebrow right corner (F11), Left eyebrow point directly above left eye centre (F12),
    //Right eyebrow left corner (F13),Right eyebrow right corner (F14), Right eyebrow point directly above right eye centre (F15).

    // lefteyebrow
    /*
    (f10x,f10y) = shape[lEBStart];
    (f11x,f11y) = shape[lEBEnd];
    (f12x,f12y) = shape[lEBEnd-2];
  */

    f10x = leftEyeBrow[0].x;
    f10y = leftEyeBrow[0].y;

    f11x = leftEyeBrow[4].x;
    f11y = leftEyeBrow[4].y;

    f12x = leftEyeBrow[2].x;
    f12y = leftEyeBrow[2].y;

    /*
      (f13x,f13y) = shape[rEBStart];
      (f14x,f14y) = shape[rEBEnd];
      (f15x,f15y) = shape[rEBEnd-2];
  */

    f13x = rightEyeBrow[0].x;
    f13y = rightEyeBrow[0].y;

    f14x = rightEyeBrow[4].x;
    f14y = rightEyeBrow[4].y;

    f15x = rightEyeBrow[2].x;
    f15y = rightEyeBrow[2].y;



    // lips
    //Left corner (F16), right corner (F17) and point directly below nose centre (F18).

    /*
    (f16x,f16y) = shape[mStart];
    (f17x,f17y) = shape[mStart + 6];
  */

    f16x = mouth[0].x;
    f16y = mouth[0].y;

    f17x = mouth[6].x;
    f17y = mouth[6].y;


    /*
        (mx,my) = shape[mStart+3];
        (mx2,my2) = shape[mStart+9];
        f18x = (mx+my)/2;
        f18y = (mx2+my2)/2;
    
        */

    f18x = (mouth[3].x + mouth[9].x) / 2;
    f18y = (mouth[3].y + mouth[9].y) / 2;


    v1x = f1x - f2x
    v1y = f1y - f2y

    v2x = f4x - f3x
    v2y = f4y - f3y

    v3x = f5x - f6x
    v3y = f5y - f6y

    v4x = f8x - f7x
    v4y = f8y - f7y

    v5x = f11x - f10x
    v5y = f11y - f10y

    v6x = f14x - f13x
    v6y = f14y - f13y

    v7x = f17x - f16x
    v7y = f17y - f16y

    v8x = f12x - f1x
    v8y = f12y - f1y

    v9x = f15x - f5x
    v9y = f15x - f5y

    v10x = f9x - f18x
    v10y = f9y - f18y

    v11x = f2x - f16x
    v11y = f2y - f16y

    v12x = f6x - f17x
    v12y = f6y - f17y

    final = [v1x, v2x, v3x, v4x, v5x, v6x, v7x, v8x, v9x, v10x, v11x, v12x, v1y, v2y, v3y, v4y, v5y, v6y, v7y, v8y, v9y, v10y, v11y, v12y]

    return final

}

function rgb_to_gray(face_pixel) {

    dim0 = [];
    dim1 = [];
    dim2 = [];
    face_pixel_gray = face_pixel.arraySync();

    for (i = 0; i < 250; i++) {
        dimj = [];
        for (j = 0; j < 250; j++) {
            dimj.push(face_pixel_gray[i][j][0]);
        }
        dim0.push(dimj);
    }

    for (i = 0; i < 250; i++) {
        dimj = [];
        for (j = 0; j < 250; j++) {
            dimj.push(face_pixel_gray[i][j][1]);
        }
        dim1.push(dimj);
    }

    for (i = 0; i < 250; i++) {
        dimj = [];
        for (j = 0; j < 250; j++) {
            dimj.push(face_pixel_gray[i][j][2]);
        }
        dim2.push(dimj);
    }

    //console.log(dim0)
    r = tf.tensor(dim0).mul(0.299);
    g = tf.tensor(dim1).mul(0.587);
    b = tf.tensor(dim2).mul(0.114);
    //console.log(r.shape)
    y = tf.addN([r, g, b]);
    gray_pixel = tf.stack([y, y, y], 2);
   // console.log(gray_pixel.shape)



    return gray_pixel;

}

async function printGrayImage(gray_pixel) {

    face_pixel_gray_3 = gray_pixel.toFloat().div(NORMALIZATION_OFFSET);
    console.log('face_pixel_gray_3 shape');
   // //console.log(face_pixel_gray_3.shape);
    face_pixel2 = await tf.browser.toPixels(face_pixel_gray_3, this.face_canvas);

}

function startprediction() {
    (async () => {
        await analyzeFaces(webcamElement);
        //console.log("faces = " + faces)
    });

}

function sleep(miliseconds) {
    var currentTime = new Date().getTime();

    while (currentTime + miliseconds >= new Date().getTime()) {
    }
}
async function analyzeFaces(img) {

    await tf.nextFrame();
    const faceResults = await this.findAndExtractFaces(webcamElement)
    const { detections, faces } = faceResults
    //console.log(faces)

}

async function nextFrame() {
    new Promise(resolve => {
        requestAnimationFrame(() => requestAnimationFrame(() => resolve()))
    })
}

async function prepImg(img, size) {
    // Convert to tensor
    //console.log("img = " + img)
    const imgTensor = tf.browser.fromPixels(img);
    //console.log(" image tensor shape" + imgTensor.shape);
    // Normalize from [0, 255] to [-1, 1].

    const normalized = imgTensor
        .toFloat()
        .sub(NORMALIZATION_OFFSET)
        .div(NORMALIZATION_OFFSET)
    //console.log(imgTensor.shape);
    if (imgTensor.shape[0] === size && imgTensor.shape[1] === size) {
        return normalized
    }
    //console.log("about to resize");
    // Resize image to proper dimensions
    const alignCorners = true
    return tf.image.resizeBilinear(normalized, [size, size], alignCorners)
}



async function initModels() {

    //console.log('loading face-api model');
    this.face_api = await faceApiLoad();
    //console.log('Sucessfully loaded face-api model');
    //console.log('loading au detector model');
    //console.log("model=" + this.face_api);
    //audetector = await tf.loadLayersModel('https://foo.bar/tfjs_artifacts/model.json');
    //audetector = await tf.loadLayersModel('au_model.json');
}

async function setupWebcam() {
	const hdConstraints = {
			  video: {width: 250, height: 250, frameRate: {
		            ideal: 1,
		            max: 3
		        }}
			};
    return new Promise((resolve, reject) => {
        const navigatorAny = navigator;
        navigator.getUserMedia = navigator.getUserMedia ||
            navigatorAny.webkitGetUserMedia || navigatorAny.mozGetUserMedia ||
            navigatorAny.msGetUserMedia;
        if (navigator.getUserMedia) {
        	//video: { frameRate: { ideal: 10, max: 15 } } could be used for the frame rate
            //navigator.getUserMedia({ video: true },
            navigator.getUserMedia(hdConstraints,
                stream => {
                    webcamElement.srcObject = stream;
                    webcamElement.autoplay = true;
                    
                    webcamElement.addEventListener('loadeddata', () => resolve(), false);
                    
                   // webcamElement2.srcObject = stream;
                   // webcamElement2.addEventListener('loadeddata', () => resolve(), false);
                },
                error => reject());
        } else {
        	console.log("no userMedia found");
            reject();
        }
    });
}

// Face api methods

function faceApiInit(path = '', params = PARAMS) {
    this.path = path
    this.params = params
}

async function faceApiLoad() {
    //faceApiInit();
    model = new faceapi.Mtcnn();
    //console.log(modelpath);
    //console.log(model);
    //console.log(model.nets)
    await model.load(modelpath);
    //console.log("loaded faceapi")
    return model;
}



async function findAndExtractFaces3(img, faces) {

    var newfaces = await faceapi.extractFaces(img, faces);

    return newfaces;
}


start_capture();
//console.log(" logs after the call of face api")
