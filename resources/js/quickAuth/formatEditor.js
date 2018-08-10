/**
 * Created by rezecib on 5/28/2017.
 * Handles the building and management of the QuickAuth Format Editor page.
 * Makes use of formatBuilder.js and format2json.js.
 */

//TODO(rezecib): luxury; remove style from problemFormat when it holds a default value
// the above is actually pretty technically challenging, maybe not worth it

//Module pattern for better scoping
var quickAuthFormatEditor = (function() {

    //The module we will be exporting
var m = {},
    //The font options; this will be populated by buildFormatEditor
    FONTS,
    //The color options; this will be populated by buildFormatEditor
    COLORS,
    //Stores the shorthand and color of the blocks used for drag-and-drop layout
    LAYOUT_BLOCKS = {
        statement: ["S", "#0A0"],
        figure: ["F", "#A00"],
        hints: ["H", "#44C"],
        hintfigure: ["HF", "#A0A"],
        answers: ["A", "#0AA"],
        columns: ["| |", "#AAA"]
    },
    //Stores the current problem format as a javascript object
    _problemFormat,
    //Stores whether the current layout has been edited
    // so we can warn the author if they are about to wipe it
    _layoutEdited,
    //Stores the button that is selected when a template has been edited,
    // which is also the button that can be selected to open an empty template
    _editedTemplateButton,
    //Stores the currently selected template button,
    // this lets us re-enable it when another one is select
    _selectedTemplateButton;

function setEdited() {
    _layoutEdited = true;
    if(_selectedTemplateButton != _editedTemplateButton) {
        setTemplateButtonOnClick(_selectedTemplateButton, true);
        $(_editedTemplateButton).addClass("selected");
        setTemplateButtonOnClick(_editedTemplateButton, false);
        _selectedTemplateButton = _editedTemplateButton;
    }
}

//This is the main entry point; it is called from editProblemFormat.jsp,
// and sets up the editor
m.buildFormatEditor = function(templates, fonts, colors, problemFormat) {
    FONTS = fonts;
    COLORS = colors;
    //Add a miniature button-version of each template to the template selector
    var zoom = 375/(600 * (templates.length + 1)); //scales template buttons to fit in the space
    for(var i = 0; i < templates.length; i++) {
        addTemplateButton(buildTemplateButton(JSON.stringify(templates[i])), zoom);
    }
    addTemplateButton(buildEditedTemplateButton(), zoom);
    var block_selector = document.getElementById("BlockSelector");
    for(var block in LAYOUT_BLOCKS) {
        var selection_block = buildLayoutBlock(block);
        selection_block.id = "SelectionBlock-" + block;
        selection_block.dataset.blockname = block;
        block_selector.appendChild(selection_block);
    }
    _editedTemplateButton.onclick(); //default it to a new empty format
    if(problemFormat != null) setProblemFormatJson(JSON.stringify(problemFormat));
}

//Adds a template button to the template selector
//template_button: element; the button element to add
//zoom: float; the magnification level to use on the button (scale to fit all buttons)
function addTemplateButton(template_button, zoom) {
    var button_container = document.createElement("div");
    button_container.style.overflow = "visible";
    button_container.style.position = "relative";
    button_container.style.flexGrow = "1";
    template_button.style.position = "absolute";
    template_button.style.transform = "scale(" + zoom + ")";
    template_button.style.transformOrigin = "top left";
    template_button.style.marginTop = ((75 - zoom*600)/2) + "px";
    template_button.style.borderWidth = (2/zoom) + "px";
    button_container.appendChild(template_button);
    document.getElementById("TemplateSelector").appendChild(button_container);
}

//template_json: string; json for the base template (to be stored in the button's data)
function buildTemplateButton(template_json) {
    var btn = document.createElement("div");
    btn.className = "template-button selectable";
    btn.dataset.templateJson = template_json;
    quickAuthFormatBuilder.buildProblem(btn, template_json, false, true, false);
    setTemplateButtonOnClick(btn, true);
    return btn;
}

function buildEditedTemplateButton() {
    _editedTemplateButton = buildTemplateButton('{"order":""}');
    _editedTemplateButton.style.backgroundColor = "#8ED8E0";
    _editedTemplateButton.style.lineHeight = "600px";
    _editedTemplateButton.style.fontSize = "600px";
    _editedTemplateButton.style.fontWeight = "bolder";
    _editedTemplateButton.style.textAlign = "center"
    _editedTemplateButton.innerHTML = "+";
    setTemplateButtonOnClick(_editedTemplateButton, true);
    return _editedTemplateButton;
}

//btn: element; button to be configured
//enabled: boolean; whether to enable or disable
function setTemplateButtonOnClick(btn, enabled) {
    if(enabled) {
        btn.onclick = function() {
            if(_layoutEdited && !confirm("You're about to clear any layout changes you've made. Are you sure you want to do this?")) {
                return;
            }
            _layoutEdited = false;
            setProblemFormatJson(btn.dataset.templateJson);
            setTemplateButtonOnClick(btn, false);
            if(_selectedTemplateButton) {
                setTemplateButtonOnClick(_selectedTemplateButton, true);
            }
            _selectedTemplateButton = btn;
        };
        $(btn).removeClass("disabled");
    } else {
        $(btn).addClass("disabled");
        btn.onclick = null;
    }
}

//Sets the active problem format in all parts of the editor to the provided string
//json: string; the problemFormat to use
function setProblemFormatJson(json) {
    _problemFormat = format2json.jsonToProblemFormat(json);
    rebuildLayoutEditor();
    updateProblemPreview(json);
    setBlockForStyleEditing(null);
}

//Clears the drag-and-drop layout editor and rebuilds it based on the current problemFormat
function rebuildLayoutEditor() {
    var editor = document.getElementById("LayoutEditor");
    editor.innerHTML = ""; //clear the existing layout
    editor.draggable = false;
    for(var block in LAYOUT_BLOCKS) {
        var selection_block = document.getElementById("SelectionBlock-" + block);
        $(selection_block).removeClass("disabled");
        selection_block.draggable = true;
    }
    buildLayoutBlocks(editor, _problemFormat.order);
}

function buildLayoutBlocks(container, blocks) {
    for(var b = 0; b < blocks.length; b++) {
        var block = blocks[b];
        if(typeof block === "string") { //This is a normal block
            var layout_block = buildLayoutBlock(block);
            var selection_block = document.getElementById("SelectionBlock-" + layout_block.dataset.blockname);
            $(selection_block).addClass("disabled");
            selection_block.draggable = false;
            container.appendChild(layout_block);
        } else { //This is a set of columns
            //Add an empty space for placing blocks above these columns
            container.appendChild(buildLayoutBlockSpace());
            //Build a container for the columns
            var layout_columns = buildLayoutColumns(block);
            container.appendChild(layout_columns);
        }
    }
    //So you can add blocks at the bottom of this column
    container.appendChild(buildLayoutBlockSpace());
}

function buildLayoutColumns(block) {
    var layout_columns = document.createElement("div");
    layout_columns.className = "layout-columns";
    layout_columns.draggable = false;
    layout_columns.appendChild(buildLayoutColumn(block.left));
    layout_columns.appendChild(buildLayoutColumn(block.right));
    return layout_columns;
}

function buildLayoutColumn(blocks) {
    var layout_column = document.createElement("div");
    layout_column.className = "layout-column";
    layout_column.draggable = false;
    buildLayoutBlocks(layout_column, blocks);
    return layout_column;
}

function buildLayoutBlock(blockname) {
    var layout_block = document.createElement("div");
    layout_block.className = "layout-block selectable";
    var layout_block_contents = LAYOUT_BLOCKS[blockname];
    layout_block.innerHTML = layout_block_contents[0];
    layout_block.style.backgroundColor = layout_block_contents[1];
    layout_block.draggable = true;
    layout_block.dataset.blockname = blockname;
    addLayoutDragEvents(layout_block);
    layout_block.onclick = function() { setBlockForStyleEditing(layout_block); }
    return layout_block;
}

function buildLayoutBlockSpace() {
    var layout_block_space = document.createElement("div");
    layout_block_space.className = "layout-block layout-block-space";
    addLayoutDragEvents(layout_block_space);
    layout_block_space.draggable = false;
    return layout_block_space;
}

function addLayoutDragEvents(block) {
    block.addEventListener("dragstart", handleDragStart, false);
    block.addEventListener("dragover", handleDragOver, false);
    block.addEventListener("dragleave", handleDragOut, false);
    block.addEventListener("drop", handleDrop, false);
}

var _dragged;
function handleDragStart(e) {
    _dragged = e.target;
}

function handleDragOver(e) {
    e.stopPropagation();
    e.preventDefault();
    e.dataTransfer.dropEffect = "copy";
    $(e.srcElement).addClass("dragover");
}

function handleDragOut(e) {
    $(e.srcElement).removeClass("dragover");
}

function handleDrop(e) {
    e.stopPropagation();
    e.preventDefault();
    $(e.srcElement).removeClass("dragover");
    var dragparent = _dragged.parentNode;
    var changed = false;
    if(e.target.parentNode.id == "BlockSelector") {
        //The block was dragged into the top selection area, we should delete it
        if(dragparent.id != "BlockSelector") { //but not if they were dragging a selection block...
            changed = true;
            removeFromProblemFormatOrder(_problemFormat.order, _dragged.dataset.blockname);
            //Free up the selection block corresponding to this one
            var selection_block = document.getElementById("SelectionBlock-" + _dragged.dataset.blockname);
            $(selection_block).removeClass("disabled");
            selection_block.draggable = true;
            //And delete the dragged block
            dragparent.removeChild(_dragged);
        }
    } else if(_dragged != e.target && _dragged.nextElementSibling != e.target) {
        //the above condition should filter out "moves" that don't actually rearrange anything
        var should_remove = true;
        var block_to_insert = _dragged.dataset.blockname;
        if(_dragged.dataset.blockname == "columns") {
            e.target.parentNode.insertBefore(buildLayoutBlockSpace(), e.target);
            block_to_insert = {width:50,left:[],right:[]}
            _dragged = buildLayoutColumns(block_to_insert);
            should_remove = false;
        } else if(dragparent.id == "BlockSelector") {
            $(_dragged).addClass("disabled");
            _dragged.draggable = false;
            _dragged = buildLayoutBlock(_dragged.dataset.blockname);
            should_remove = false;
        }
        if(should_remove) {
            removeFromProblemFormatOrder(_problemFormat.order, block_to_insert);
            dragparent.removeChild(_dragged);
        }
        var path = [];
        findLayoutTargetPath(path, document.getElementById("LayoutEditor"), e.target);
        insertIntoProblemFormatOrder(_problemFormat.order, block_to_insert, path);
        e.target.parentNode.insertBefore(_dragged, e.target);
        changed = true;
    }
    while(dragparent.className == "layout-column"
    && dragparent.parentNode.children[0].children.length == 1
    && dragparent.parentNode.children[1].children.length == 1) {
        //while this is true, we've just emptied out a pair of columns; delete them
        var columns = dragparent.parentNode;
        dragparent = columns.parentNode; //set it to the element containing the columns
        //columns also have a spacer before them; remove that first
        dragparent.removeChild(columns.previousElementSibling);
        dragparent.removeChild(columns);
    }
    if(changed) {
        setEdited();
        updateProblemPreview();
    }
    _dragged = null;
}

//Intended use: removeFromProblemFormatOrder(_problemFormat.order, block)
//blocks: array; contains blocks and column objects
//block: string; name of the block we're removing
//return: boolean; whether a block was removed by this particular call
function removeFromProblemFormatOrder(blocks, block) {
    for(var b = 0; b < blocks.length; b++) {
        if(blocks[b] == block) { //we found our block, cut it out
            blocks.splice(b, 1);
            return true;
        } else if(typeof blocks[b] === "object") {
            //we found some columns, explore both sides
            var removed = false;
            removed = removed || removeFromProblemFormatOrder(blocks[b].left, block);
            removed = removed || removeFromProblemFormatOrder(blocks[b].right, block);
            if(removed && blocks[b].left.length == 0 && blocks[b].right.length == 0) {
                //we removed a block from these columns, and left them both empty
                //so we should also remove these columns
                blocks.splice(b, 1);
                return true;
            }
        }
    }
    return false;
}

//Intended use: insertIntoProblemFormatOrder(_problemFormat.order, block, before_element)
//blocks: array; contains blocks and column objects
//block: string; name of the block we're removing
//path: array; the path of indices to follow in blocks
function insertIntoProblemFormatOrder(blocks, block, path) {
    //Follow the path down to the next-to-last block
    for(var p = 0; p < path.length-1; p++) {
        blocks = blocks[path[p]];
    }
    //Insert the new block into the parent, before the target block
    blocks.splice(path[path.length-1], 0, block);
}

//json: string; containing the problemFormat json; by default, problemFormatToJson(_problemFormat)
function updateProblemPreview(json) {
    if(!json) json = format2json.problemFormatToJson(_problemFormat);
    //Set the copyable problemFormat text from our current problemFormat model
    var outputField = document.getElementById("ProblemFormatOutput");
    outputField.value = json;
    //Update the problem preview to reflect the current model
    var layout_container = document.getElementById("ProblemContainerLayout");
    layout_container.innerHTML = "";
    quickAuthFormatBuilder.buildProblem(layout_container, json, false, true, false);
    var text_container = document.getElementById("ProblemContainerText");
    text_container.innerHTML = "";
    quickAuthFormatBuilder.buildProblem(text_container, json, false, true, true);
}

var _styleEditorBlock;
var _styleEditorFormatObject;
var _styleEditorFormatColumns;
var _styleEditorFormatColumn;
//block: element; the layout-block that was clicked on
function setBlockForStyleEditing(block) {
    if(_styleEditorBlock) { //clear the previous selection
        $(_styleEditorBlock).removeClass("selected");
        _styleEditorFormatObject = null;
        _styleEditorFormatColumns = null;
        _styleEditorFormatColumn = null;
    }
    _styleEditorBlock = block;
    if(block) {
        var path = [];
        findLayoutTargetPath(path, document.getElementById("LayoutEditor"), block);
        _styleEditorFormatObject = _problemFormat.order
        for(var p = 0; p < path.length; p++) {
            _styleEditorFormatObject = _styleEditorFormatObject[path[p]];
            if(typeof _styleEditorFormatObject == "object" && _styleEditorFormatObject.width) {
                _styleEditorFormatColumns = _styleEditorFormatObject;
                _styleEditorFormatColumn = path[p+1]; //should be "left" or "right"
            }
        }
        if(!_problemFormat[_styleEditorFormatObject]) { //if we haven't yet defined style for this
            _problemFormat[_styleEditorFormatObject] = {};
        }
        _styleEditorFormatObject = _problemFormat[_styleEditorFormatObject];
        $(block).addClass("selected");
    }
    rebuildStyleEditor();
}

//Finds the path of indices through _problemFormat.order that leads to the object corresponding to
// the layout element target. Intended usage:
//    var path = [];
//    findLayoutTargetPath(path, document.getElementById("LayoutEditor"), target);
//path: array; should be empty at the start, will be filled with the path to target
//current: element; the node we are searching in
//target: element; the layout element we want to find the path to
function findLayoutTargetPath(path, current, target) {
    var p = 0; //this stores the index in the actual _problemFormat.order
    for(var i = 0; i < current.children.length; i++) {
        var child = current.children[i];
        if(child.className.includes("layout-columns")) {
            //this is columns, check both
            if(findLayoutTargetPath(path, child.children[0], target)) {
                path.unshift("left");
                path.unshift(p);
                return true;
            }
            if(findLayoutTargetPath(path, child.children[1], target)) {
                path.unshift("right");
                path.unshift(p);
                return true;
            }
            p++;
        } else {
            //this is a layout-block
            if(child == target) { //we found what we were looking for, return!
                path.unshift(p);
                return true;
            }
            if(!child.className.includes("layout-block-space")) {
                p++; //only increment for actual blocks
            }
        }
    }
}

//Builds the editor for an individual block's styling
function rebuildStyleEditor() {
    var editor = document.getElementById("BlockEditor");
    editor.innerHTML = "";
    if(!_styleEditorBlock) return;
    editor.appendChild(buildLabel("Font:"));
    var current_font = _styleEditorFormatObject.fontFamily || FONTS[0];
    var font_selector = buildSelector(FONTS, current_font,
        function(value) { onBlockStyleChanged("fontFamily", value); });
    font_selector.style.fontSize = "10px";
    editor.appendChild(font_selector);
    editor.appendChild(buildLabel("Font Size (px):"));
    var current_font_size = parseInt(_styleEditorFormatObject.fontSize) || 16;
    editor.appendChild(buildSliderField(8, 48, 1, current_font_size,
        function(value) { onBlockStyleChanged("fontSize", value + "px"); }));
    editor.appendChild(buildLabel("Font Color:"));
    var current_font_color = _styleEditorFormatObject.color || COLORS[0];
    editor.appendChild(buildSelector(COLORS, current_font_color,
        function(value) { onBlockStyleChanged("color", value); }));
    editor.appendChild(buildFontWeightStyleToggles());
    var current_block_spacing = parseInt(_styleEditorFormatObject.borderWidth) || 5;
    editor.appendChild(buildLabel("Block spacing (px):"));
    editor.appendChild(buildSliderField(0, 50, 1, current_block_spacing,
        function(value) { onBlockStyleChanged("borderWidth", value + "px"); }));
    if(_styleEditorFormatColumns) {
        var column_width = _styleEditorFormatColumns.width;
        if(_styleEditorFormatColumn == "right") column_width = 100 - column_width;
        editor.appendChild(buildLabel("Column width (%):"));
        editor.appendChild(buildSliderField(0, 100, 1, column_width,
            function(value) { onBlockStyleChanged("column_width", value + "%"); }));
    }
    if(_styleEditorBlock.dataset.blockname.match(/figure/i)) {
        var current_width = _styleEditorFormatObject.width || 100;
        editor.appendChild(buildLabel("Figure width (%):"));
        editor.appendChild(buildSliderField(0, 100, 1, current_width,
            function(value) { onBlockStyleChanged("width", value + "%"); }));
    }
}

function onBlockStyleChanged(style, value) {
    setEdited();
    if(_styleEditorFormatColumn && style == "column_width") {
        //This requires special handling, because we actually want to change
        // the width of the containing column, and adjust the adjacent column to match
        value = parseInt(value); //convert from "67%" to 67
        if(_styleEditorFormatColumn == "right") {
            value = 100 - value; //convert to left-column width
        }
        _styleEditorFormatColumns.width = value;
    } else {
        _styleEditorFormatObject[style] = value;
    }
    updateProblemPreview();
}

//message: string; the message to put on the label
function buildLabel(message) {
    var label = document.createElement("div");
    label.className = "label";
    label.innerHTML = message;
    return label;
}

//options: array; holds each option for the selector (e.g. each font option)
//current_value: string; holds the current value for the selector (it should match an option!)
//callback: function; called with the new selection
function buildSelector(options, current_value, callback) {
    var selector = document.createElement("select");
    for(var i = 0; i < options.length; i++) {
        var opt = document.createElement("option");
        opt.innerHTML = options[i];
        opt.value = options[i];
        selector.appendChild(opt);
        if(opt == current_value) selector.selectedIndex = i;
    }
    selector.addEventListener("change", function() {
        callback(selector.options[selector.selectedIndex].value);
    });
    return selector;
}


//min: number; the minimum value for the slider
//max: number; the maximum value for the slider
//step: number; the minimum step size between values
//current_value: number; the current value for the slider
//callback: function; called with the new value
function buildSliderField(min, max, step, current_value, callback) {
    var slider_container = document.createElement("div");
    slider_container.className = "row-container";
    var slider = document.createElement("input");
    slider.min = min;
    slider.max = max;
    slider.step = step;
    slider.type = "range";
    slider.value = current_value;
    slider_container.appendChild(slider);
    var field = document.createElement("input");
    field.type = "text";
    field.style.width = "30px";
    field.style.flex = "0 0 auto";
    field.value = current_value;
    slider_container.appendChild(field);
    slider.addEventListener("input", function() {
        field.value = slider.value;
        callback(slider.value);
    });
    field.addEventListener("input", function() {
        //unlike the slider, we need to sanitize the input
        var value = parseInt(field.value);
        if(isNaN(value)) { // it wasn't a number, reset it to the last known number
            field.value = slider.value;
            return;
        }
        value = Math.max(Math.min(value, max), min); //constrain it to the range
        field.value = value;
        slider.value = value;
        callback(value);
    });
    return slider_container;
}

function buildFontWeightStyleToggles() {
    var box_container = document.createElement("div");
    box_container.className = "row-container";
    buildCheckbox(box_container, "Bold:", "fontWeight", "bold", "normal");
    buildCheckbox(box_container, "Italic:", "fontStyle", "italic", "normal");
    return box_container;
}

//container: element; the label and checkbox container are added to this
//label_text: string; the text for the label
//style_key: string; the style property this checkbox sets
//style_value: string; the style value this checkbox sets when checked
//style_default: string; the style value this checkbox sets when not checked
function buildCheckbox(container, label_text, style_key, style_value, style_default) {
    var label = buildLabel(label_text);
    label.style.flex = "0 0 auto";
    container.appendChild(label);
    var box_container = document.createElement("div");
    var box = document.createElement("input");
    box.type = "checkbox";
    box.style.marginTop = "20px";
    box.style.width = "16px";
    box.style.height = "16px";
    box_container.appendChild(box);
    box.onclick = function() {
        onBlockStyleChanged(style_key, box.checked ? style_value : style_default);
    };
    container.appendChild(box_container);
}

return m;

})();
