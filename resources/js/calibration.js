var PointCalibrate = 0;
var CalibrationPoints={};

/**
 * Clear the canvas and the calibration button.
 */
function ClearCanvas(){
    $(".Calibration").hide();
}

/**
 * Show the instruction of using calibration at the start up screen.
 */
function PopUpInstruction(){
    ClearCanvas();
    swal({
        title:"Calibration",
        text: "Please click on each of the 9 points on the screen. When clicking, make sure you are looking at that point. You must click on each point 5 times till it goes yellow. This will calibrate your eye movements.",
        buttons:{
            cancel: "Skip",
            confirm: true
        }
    }).then((value) => {
        if (value) {
            ShowCalibrationPoint();
        } else {
            headmodel.calibrated = true;
        }
    });

}
/**
 * Load this function when the index page starts.
 * This function listens for button clicks on the html page
 * checks that all buttons have been clicked 5 times each, and then goes on to measuring the precision
 */
$(document).ready(function(){

    ClearCanvas();

    $(".Calibration").click(function(){ // click event on the calibration buttons

        var id = $(this).attr('id');

        if (!CalibrationPoints[id]){ // initialises if not done
            CalibrationPoints[id]=0;
        }

        calibratePose(id);

        CalibrationPoints[id]++; // increments values

        if (CalibrationPoints[id]==5){ //only turn to yellow after 5 clicks
            $(this).css('background-color','yellow');
            $(this).prop('disabled', true); //disables the button
            PointCalibrate++;
        }else if (CalibrationPoints[id]<5){
            //Gradually increase the opacity of calibration points when click to give some indication to user.
            var opacity = 0.2*CalibrationPoints[id]+0.2;
            $(this).css('opacity',opacity);
        }

        if (PointCalibrate >= 8){ // last point is calibrated
            //using jquery to grab every element in Calibration class and hide them except the middle point.
            $(".Calibration").hide();
            $("#Pt5").show();
            swal({
                title: "Calibration finished.",
                allowOutsideClick: false,
                buttons: {
                    cancel: "Recalibrate",
                    confirm: true,
                }
            }).then(value => {
                if (value) {
                    ClearCanvas();
                    getThreshold()
                }
                else {
                    ClearCalibration();
                    ClearCanvas();
                    ShowCalibrationPoint();
                }
            });
        }
    });
});


/**
 * Show the Calibration Points
 */
function ShowCalibrationPoint() {
    $(".Calibration").show();
}

/**
 * This function clears the calibration buttons memory
 */
function ClearCalibration(){
    window.localStorage.clear();
    $(".Calibration").css('background-color','red');
    $(".Calibration").css('opacity',0.2);
    $(".Calibration").prop('disabled',false);

    CalibrationPoints = {};
    PointCalibrate = 0;
}

function calibratePose(id) {
    if (!headmodel.calibratePoints[id]) {
        headmodel.calibratePoints[id] = [];
    }
    headmodel.calibratePoints[id].push(headmodel.value);
}

function getThreshold() {
    var up = [];
    var down = [];
    var left = [];
    var right = [];
    Object.entries(headmodel.calibratePoints).forEach(([key, value]) => {
        if (value.length >= 5) {
            switch (key) {
                case "Pt1":
                    for (let i = 0; i < value.length; i++) {
                        up.push(value[i][0]);
                        left.push(value[i][1]);
                    }
                    break;
                case "Pt2":
                    for (let i = 0; i < value.length; i++) {
                        up.push(value[i][0]);
                    }
                    break;
                case "Pt3":
                    for (let i = 0; i < value.length; i++) {
                        up.push(value[i][0]);
                        right.push(value[i][1]);
                    }
                    break;
                case "Pt4":
                    for (let i = 0; i < value.length; i++) {
                        left.push(value[i][1])
                    }
                    break;
                case "Pt6":
                    for (let i = 0; i < value.length; i++) {
                        right.push(value[i][1])
                    }
                    break;
                case "Pt7":
                    for (let i = 0; i < value.length; i++) {
                        down.push(value[i][0]);
                        left.push(value[i][1]);
                    }
                    break;
                case "Pt8":
                    for (let i = 0; i < value.length; i++) {
                        down.push(value[i][0]);
                    }
                    break;
                case "Pt9":
                    for (let i = 0; i < value.length; i++) {
                        down.push(value[i][0]);
                        right.push(value[i][1]);
                    }
                    break;
            }
        }
    });
    up = filterOutliers(up);
    down = filterOutliers(down);
    left = filterOutliers(left);
    right = filterOutliers(right);
    headmodel.upthreshold = up.reduce((previous, current) => current += previous) / up.length + 5;
    headmodel.downthreshold = down.reduce((previous, current) => current += previous) / down.length - 5;
    headmodel.rightthreshold = right.reduce((previous, current) => current += previous) / right.length + 5;
    headmodel.leftthreshold = left.reduce((previous, current) => current += previous) / left.length - 5;

    // window.alert(headmodel.upthreshold);
    // window.alert(headmodel.downthreshold);
    // window.alert(headmodel.leftthreshold);
    // window.alert(headmodel.rightthreshold);
    headmodel.calibrated = true;
}

function filterOutliers(someArray) {

    if(someArray.length < 4)
        return someArray;

    let values, q1, q3, iqr, maxValue, minValue;

    values = someArray.slice().sort( (a, b) => a - b);//copy array fast and sort

    if((values.length / 4) % 1 === 0){//find quartiles
        q1 = 1/2 * (values[(values.length / 4)] + values[(values.length / 4) + 1]);
        q3 = 1/2 * (values[(values.length * (3 / 4))] + values[(values.length * (3 / 4)) + 1]);
    } else {
        q1 = values[Math.floor(values.length / 4 + 1)];
        q3 = values[Math.ceil(values.length * (3 / 4) + 1)];
    }

    iqr = q3 - q1;
    maxValue = q3 + iqr * 1.5;
    minValue = q1 - iqr * 1.5;

    return values.filter((x) => (x >= minValue) && (x <= maxValue));
}