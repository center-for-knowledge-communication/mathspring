window.selectdiff = {
    affect_model: null,
    effort_model: null,
    head_model: null,
    au_model: null,
    affect_features: [],
    facial_features: [],

    getImage: function(bbox) {
        // Capture the current face image according to the bounding box
        return tf.tidy(function() {
            var image = tf.browser.fromPixels(webcamElement).expandDims(0);

            let image_head = tf.image.cropAndResize(image, [[Math.max(bbox.box.topLeft.y - 0.6 * bbox.box.height, 0) / webcamElement.height,
                Math.max(bbox.box.topLeft.x - 0.6 * bbox.box.width, 0) / webcamElement.width,
                Math.min(bbox.box.topLeft.y + 1.6 * bbox.box.height, webcamElement.height) / webcamElement.height,
                Math.min(bbox.box.topLeft.x + 1.6 * bbox.box.width, webcamElement.width) / webcamElement.width]], [0], [64, 64]);

            let image_face = tf.image.cropAndResize(image, [[Math.max(bbox.box.topLeft.y, 0) / webcamElement.height,
                Math.max(bbox.box.topLeft.x , 0) / webcamElement.width,
                Math.min(bbox.box.topLeft.y + bbox.box.height, webcamElement.height) / webcamElement.height,
                Math.min(bbox.box.topLeft.x + bbox.box.width, webcamElement.width) / webcamElement.width]], [0], [256, 256])
                .div(255)
                .sub([0.485, 0.456, 0.406])
                .div([0.229, 0.224, 0.225]);
            
            image_face = tf.transpose(image_face, [0, 3, 1, 2]);
            // console.log(image.shape);

            const center = [bbox.box.topLeft.x + 0.5 * bbox.box.width - webcamElement.width, 
                bbox.box.topLeft.y + 0.5 * bbox.box.height - webcamElement.height]

            return [image_head, image_face, center];
        });
    },

    predict: async function (img) {
        // extract features for each frame given face image
        // affect
        const input = new ort.Tensor(new Float32Array(img[1].dataSync()), [1, 3, 256, 256]);
        const affect_feat = await this.affect_model.run({'input.1': input});
        // console.log(affect_feat['504']);
        //this.affect_features.push(affect_feat['504'].data);
        this.affect_features.push(affect_feat['505'].data);

        // au
        // TODO: more aus
        let au_img = tf.image.resizeBilinear(tf.transpose(img[1], [0, 2, 3, 1]), [170, 170]); // TODO: should be CenterCrop(170)
        au_img = tf.transpose(au_img, [0, 3, 1, 2]);
        const au_input = new ort.Tensor(new Float32Array(au_img.dataSync()), [1, 3, 170, 170]);
        const au_feat = await this.au_model.run({'input.1': au_input});
        // au: [1, 2, 4, 6, 7, 10, 12, 14, 15, 17, 23, 24] need first 11
        let au = Array.from(au_feat['839'].data.map(a => Math.exp(a)));
        // console.log(au.slice(12, 23));
        // use prob as au intensity
        // openface: 
        // presence: 1, 2, 4, 5, 6, 7, 9, 10, 12, 14, 15, 17, 20, 23, 25, 26, 28, 45.
        // intensity: 1, 2, 4, 5, 6, 7, 9, 10, 12, 14, 15, 17, 20, 23, 25, 26, 45.
        au_presence = au.slice(12, 23).map(a => a > 0.5 ? 1 : 0).concat([0,0,0,0]);
        au_presence.splice(3, 0, 0);
        au_presence.splice(6, 0, 0);
        au_presence.splice(12, 0, 0);
        au_intensity = au.slice(12, 23).map(a => a * 5).concat([0,0,0]);
        au_intensity.splice(3, 0, 0);
        au_intensity.splice(6, 0, 0);
        au_intensity.splice(12, 0, 0);

        // head pose
        let headpose = await this.head_model.executeAsync(img[0]);
        headpose = headpose.arraySync()[0]; // yaw, pitch, roll
        headpose = [headpose[1], headpose[0], headpose[2]] // pitch (Rx), yaw (Ry), and roll (Rz)
        headpose = headpose.map(a => a / 180 * Math.PI); // in radians
        // console.log(headpose);

        // TODO: gaze model, head translation
        // use head pose to estimate gaze pose for now
        gazepose = [headpose[1], headpose[0]];
        gazepose = gazepose.concat(headpose).concat(headpose);
        head_translation = [img[2][0], img[2][1], 350];

        // feature pre-processing (normalization)
        facial_feat = gazepose.concat(head_translation).concat(headpose)
                        .concat(au_intensity).concat(au_presence);
        // console.log(facial_feat);
        const mean = facial_feat.reduce((a, b) => a + b) / facial_feat.length;
        const std = Math.sqrt(facial_feat.map(x => Math.pow(x - mean, 2)).reduce((a, b) => a + b) / facial_feat.length);
        facial_feat = facial_feat.map(a => (a - mean) / std);
        
        this.facial_features.push(facial_feat);
        // console.log(facial_feat);
        // test only
        // this.affect_features.push(new Array(8192).fill(0));
        // this.facial_features.push(new Array(49).fill(0));

        console.log(new Date());
    },

    predictDiff: async function () {
        // predict problem difficulty once "next problem" button is pressed
        // need info from MathSpring server

        var min_diff = "0.501";
        var max_diff = "0.75";

    	
    	var request = document.getElementById("previewProbInData").innerHTML;
    	var params = request.split("~");
    	
        var cur_diff = parseFloat(params[0]); // difficulty of current problem
        console.log("curr_diff = " + cur_diff);
        if (params.length > 3) {
        	min_diff = parseFloat(params[3]);
        }
        if (params.length > 4) {
        	max_diff = parseFloat(params[4]);
        }
        	var mastery = parseFloat(params[1]); // mastery of student
        var change = 0; // same
        var strChange = params[2];
        if (strChange === "EASIER") {
        	change = -1;
        }
        if (strChange === "HARDER") {
        	change = 1;
        }
//        const cur_diff = 0.6; // difficulty of current problem

        let left = min_diff;
        let right = max_diff;
        
        if (change > 0) { // increase difficulty
            left = cur_diff;
        } else if (change < 0) { // decrease difficulty
            right = cur_diff;
        } else { // keep difficulty
            // console.log(cur_diff);
            left = cur_diff - 0.1*(max_diff-min_diff);
            right = cur_diff + 0.1*(max_diff-min_diff);
        }

        console.log(this.facial_features.length, this.affect_features.length);
        if (this.facial_features.length > this.affect_features.length) {
            this.facial_features.pop()
        } else if (this.facial_features.length < this.affect_features.length) {
            this.affect_features.pop()
        }

        let next_diff = cur_diff;
        console.log(left, right);
        
        let prob = 0;
        
        while (left < right - (0.1*(max_diff-min_diff))) {
            console.log(left + "|" + right);
            next_diff = (left + right) / 2;
            const effort = await this.effort_model.run({
                'facial': new ort.Tensor(new Float32Array(this.facial_features.flat()), [1, this.facial_features.length, 49]),
                'affect': new ort.Tensor(new Float32Array(this.affect_features.map(a => [...a]).flat()), [1, this.affect_features.length, 8192]), 
                'meta': new ort.Tensor(new Float32Array([cur_diff, mastery, next_diff]), [1, 3]), 
                'lengths': new ort.Tensor(new Float32Array([this.facial_features.length]), [1]), 
            });
            // console.log(next_diff);
            // effort = [1, 0];
            //console.log(effort['output']);
            prob = tf.softmax(effort['output'].data).dataSync()[1];
            console.log("Prob=" + prob);
            
            if (effort['output'].data[0] > effort['output'].data[1]) { // negative
                right = next_diff;
            } else { // positive
                // console.log(next_diff);
                document.getElementById("previewProbOutData").innerHTML = "" + next_diff + "~" + strChange + "~" + prob;
                return;
            }
        }
        console.log(next_diff);
        document.getElementById("previewProbOutData").innerHTML = "" + cur_diff + "~" + strChange + "~" + prob;;
        return;
    },

    loadModel: async function () {
        // load models
        // TODO: use college / children? currently children

        console.log(new Date());
        console.log("affect model loading");
        this.affect_model = await ort.InferenceSession.create("https://gritjsmodels.s3.us-east-2.amazonaws.com/affect_model_uint8.onnx");
        console.log("affect model loaded");
        console.log(new Date());
        this.effort_model = await ort.InferenceSession.create("https://gritjsmodels.s3.us-east-2.amazonaws.com/effort_model.onnx");
        console.log("effort model loaded");
        console.log(new Date());
        this.head_model = await tf.loadGraphModel("https://storage.googleapis.com/jsmodel/fsa/model1/model.json");
        console.log("head pose model loaded");
        console.log(new Date());
        this.au_model = await ort.InferenceSession.create("https://gritjsmodels.s3.us-east-2.amazonaws.com/au_model_uint8.onnx");
        console.log("au model loaded");
        console.log(new Date());
    },

};