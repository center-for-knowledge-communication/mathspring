/**
 * Created by rezecib on 4/10/2017.
 * Handles building the layout and styling the blocks of QuickAuth problems.
 * This is used for:
 *   Actual problem display and previewing (problem_skeleton.jsp)
 *   Format editor (editProblemFormat.jsp)
 * This makes use of:
 *   jquery (just for adding/removing classes)
 *   format2json.js
 */

//Module pattern for better scoping
var quickAuthFormatBuilder = (function() {
    //The module we're exporting
var m = {},
    //A lookup for the problemFormat names for blocks to block IDs
    BLOCK_ID = {
        statement: "ProblemStatement",
        figure: "ProblemFigure",
        hints: "HintContainer",
        hintfigure: "HintFigure",
        answers: "Answers"
    };

//container: element; to which the blocks will be added
//json: string; contains the problemFormat specification
//ids: boolean; whether to assign ids to the blocks
//placeholders: boolean; whether to add placeholder background images and heights to blocks
//sample_text: boolean; whether to add sample text
m.buildProblem = function(container, json, ids, placeholders, sample_text) {
    var problemFormat = format2json.jsonToProblemFormat(json);

    //Pass it to a helper function that can go through a list and add one by one
    // this lets us reuse the code for doing each column as well (with a recursive call)
    buildProblemBlocks(problemFormat, problemFormat.order, container, ids, placeholders, sample_text);
}

//problemFormat: object; containing layout parameters
//blocks: array; containing the vertical sequence of blocks (including column pair objects)
//container: element; to which the blocks will be added
//ids: boolean; whether to assign ids to the blocks
//placeholders: boolean; whether to add placeholder background images and heights to blocks
//sample_text: boolean; whether to add sample text (this also disables placeholder images)
function buildProblemBlocks(problemFormat, blocks, container, ids, placeholders, sample_text) {
    var placeholder_height = false;
    if(placeholders) {
        var max_vertical_blocks = blocks.length;
        for(var b = 0; b < blocks.length; b++) {
            if(typeof blocks[b] === "object") {
                max_vertical_blocks += blocks[b].height - 1;
            }
        }
        placeholder_height = 100/max_vertical_blocks;
    }
    //Go through the vertical pieces one by one
    for(var b = 0; b < blocks.length; b++) {
        var block = blocks[b];
        if(typeof block === "string") { //This is a basic block
            var id = BLOCK_ID[block.toLowerCase()];
            var block_div = null;
            if(ids) {
                block_div = document.getElementById(id);
            }
            block_div = block_div || document.createElement("div");
            block_div.className = "problem-block";
            //Set its id so we can fill its contents later
            if(ids) {
                block_div.id = id;
            } else if(block == "hints") {
                //because this only executes for ids == false, it won't run on actual problems
                //hints have a specific font just for them, because reasons
                $(block_div).addClass("hint");
                $(block_div).addClass("hint-content");
            }
            //Set extra style defined in problemFormat, if there is any
            if(problemFormat[block]) {
                for(var style_prop in problemFormat[block]) {
                    block_div.style[style_prop] = problemFormat[block][style_prop];
                }
            }
            //The block below would make it lay out as if it were full-sized,
            // but it made the text too small to really see...
            // if(sample_text) {
            // var font_size = parseInt(block_div.style.fontSize || "16px");
            // block_div.style.fontSize = (font_size/3) + "px";
            // }
            if(placeholders) {
                block_div.style.height = placeholder_height + "%";
                if(!sample_text) {
                    //These images make seeing the text very hard
                    block_div.style.backgroundImage = "url('images/QuickAuthPlaceholder-" + id + ".png')";
                    block_div.style.backgroundSize = "100% 100%";
                }
            }
            if(sample_text) {
                block_div.innerHTML = block + " ipsum<br/>dolor sit amet";
                block_div.style.overflow = "hidden";
            }
            container.appendChild(block_div);
        } else { //This is a pair of columns
            buildProblemColumn(problemFormat, block.right, container, "columnR",
                100-block.width, block.height, ids, placeholder_height, sample_text);
            buildProblemColumn(problemFormat, block.left, container, "columnL",
                block.width, block.height, ids, placeholder_height, sample_text);
            //Add a clear element at the bottom to force everything afterwards to be below the columns
            var clear = document.createElement("div");
            clear.className = "clear";
            container.appendChild(clear);
        }
    }
}

//problemFormat: object; containing layout parameters
//blocks: array; containing the vertical sequence of blocks (including column pair objects)
//container: element; to which the blocks will be added
//colClass: string; className for the column ("columnL" or "columnR")
//width: number; the percentage width the column is allowed (0-100)
//height: number; how many blocks high this column's pair needs to be
//ids: boolean; whether to assign ids to the blocks
//placeholder_height: number; the height of a block, or false if no placeholders
//sample_text: boolean; whether to add sample text (currently unused because it looks awful)
function buildProblemColumn(problemFormat, blocks, container, colClass,
                            width, height, ids, placeholder_height, sample_text) {
    var column = document.createElement("div");
    column.className = colClass;
    column.style.width = width + "%";
    if(placeholder_height) column.style.height = height*placeholder_height + "%";
    container.appendChild(column);
    buildProblemBlocks(problemFormat, blocks, column, ids, placeholder_height, sample_text);
}

return m;

})();
