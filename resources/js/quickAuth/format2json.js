/**
 * Created by rezecib on 5/28/2017.
 * Handles conversion between the problemFormat string and a more complete JSON,
 * which is hierarchical and stores style in each object along the way.
 */

//Module pattern for better scoping
var format2json = (function() {
    //The module we're exporting
var m = {},
//This is for converting style objects to style strings and vice-versa
    _fake_div = document.createElement("div");

//problemFormat: object; containing the hierarchical properties
//return: string; json containing the flattened properties
m.problemFormatToJson = function(problemFormat) {
    var storage_format = {}; //store the conversion results in a separate object
    //Convert style objects to strings for storage
    for(var key in problemFormat) {
        if(key != "order") { //order requires special handling below
            var style = problemFormat[key];
            _fake_div.setAttribute("style", ""); //clear the div's style
            for(var style_prop in style) {
                _fake_div.style[style_prop] = style[style_prop];
            }
            style = _fake_div.getAttribute("style");
            if(style != "") { //don't waste space on empty styles
                storage_format[key] = style;
            }
        }
    }

    storage_format.order = problemFormatOrderToJsonOrder(problemFormat.order);
    var json = JSON.stringify(storage_format);
    return json;
}

//Recursive helper that allows nested columns to be parsed
//order: array; contains a list of blocks names and column objects
function problemFormatOrderToJsonOrder(order) {
    var order_strings = [];
    for(var b = 0; b < order.length; b++) {
        if(typeof order[b] === "string") {
            //this is a basic block, just put it in the list
            order_strings.push(order[b]);
        } else {
            //this is a column object, process it into [width leftcol, rightcol] form
            var cols = order[b];
            var lcol = cols.width + "%" + (cols.left.length > 0 ? " " : "");
            lcol += problemFormatOrderToJsonOrder(cols.left);
            var rcol = problemFormatOrderToJsonOrder(cols.right);
            order_strings.push("[" + lcol + ", " + rcol + "]");
        }
    }
    return order_strings.join(" ");
}

//json: string; containing the problemFormat json
//      object; in some cases JSON.parse has already been run on it
//return: object; with the properties from the json
m.jsonToProblemFormat = function(json) {
    //Convert most of it it JSON
    var problemFormat = json;
    if(typeof json === "string") problemFormat = JSON.parse(json);

    //Convert style strings to objects so each style attribute can be manipulated separately
    for(var key in problemFormat) {
        if(key != "order") { //order requires special handling below
            _fake_div.setAttribute("style", problemFormat[key]);
            var style = {};
            for (var i = 0; i < _fake_div.style.length; i++) {
                var name = _fake_div.style[i];
                style[name] = _fake_div.style.getPropertyValue(name);
            }
            problemFormat[key] = style;
        }
    }

    //Now convert the "order" field to an inner Javascript object
    problemFormat.order = jsonOrderToProblemFormatOrder(problemFormat.order, 0)[0];

    return problemFormat;
}

//Recursive helper function for parsing the order string into an object
//order: string; the order parameter in the stored json representation of problemFormat
//i: integer; position in the order string that we are currently parsing
//return: [blocks, i, column]; an array with the following:
//	blocks: array; list of blocks and column objects
//	i: integer; current position in the order string
//	column: string; whether we finished a left or a right column
function jsonOrderToProblemFormatOrder(order, i) {
    var blocks = [];
    var block_start = i;
    while(i < order.length) {
        if(order[i].match(/\s/)) { //whitespace, so we've completed a block
            addBlockString(order, blocks, block_start, i);
            block_start = i+1;
        } else if(order[i] == "[") { //the start of a column object
            var result = jsonOrderColumnToProblemFormatOrderColumn(order, i+1);
            blocks.push(result[0]);
            i = result[1];
            block_start = i+1;
        } else if(order[i] == ",") { //we were in a left column that ended, return
            addBlockString(order, blocks, block_start, i);
            return [blocks, i, "left"];
        } else if(order[i] == "]") { //we were in a right column that ended, return
            addBlockString(order, blocks, block_start, i);
            return [blocks, i, "right"];
        }
        ++i;
    }
    addBlockString(order, blocks, block_start, i);
    return [blocks, i, ""];
}

//This is only used by above, but to prevent copy-pasting code
function addBlockString(order, blocks, start, end) {
    if(start < end) { //don't add zero-length blocks, which could occur e.g. with multiple spaces
        blocks.push(order.slice(start, end));
    }
}

//Another part of the recursive helper above that builds the column objects
//order: string; the order parameter in the stored json representation of problemFormat
//i: integer; the position in the order string that we are currently parsing
//return: [object, integer]; an array with the column object and the current position
function jsonOrderColumnToProblemFormatOrderColumn(order, i) {
    var columns = {};
    var width_start = i;
    while(i < order.length) {
        if(order[i] == "]") { //the columns have ended here
            return [columns, i];
        } else if(order[i] == "%") { //we reached the end of the width
            columns.width = parseInt(order.slice(width_start, i));
            width_start = null; //set it to null so we know we've already gotten it
        } else if(width_start == null) {
            var result = jsonOrderToProblemFormatOrder(order, i);
            columns[result[2]] = result[0];
            i = result[1];
            if(result[2] == "right") {
                //we've done both columns, return to normal block assembly
                columns.height = Math.max(columns.left.length, columns.right.length);
                return [columns, i];
            }
        }
        ++i;
    }
    console.log("problemFormat order error: columns started but didn't finish");
}

return m;

})();
