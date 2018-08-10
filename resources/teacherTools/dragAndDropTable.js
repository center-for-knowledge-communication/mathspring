/**
 * Created by rezec on 11/15/2016.
 */

var dragElt = null;  //the element being dragged
var selected = null; //the element being dragged over
function handleDragGetTr(e) {
    var elt = e.target;
    while(elt != null && elt.tagName != "TR") {
        elt = elt.parentNode;
    }
    return elt == null ? e.target : elt;
}
function handleDragStart(e) {
    dragElt = e.target;
    dragElt.classList.add("dragging");
}
function handleDragEnter(e) {
    var elt = handleDragGetTr(e);
    if(selected != null && elt != selected) {
        selected.classList.remove("dragover-above");
        selected.classList.remove("dragover-below");
    }
    selected = elt;
    var move_from = dragElt.getAttribute("data-index"); //topic to move
    var move_to = selected.getAttribute("data-index");  //topic to move to the position on
    if(move_to <= move_from) {
        elt.classList.add("dragover-above");
    }
    if(move_to >= move_from) {
        elt.classList.add("dragover-below");
    }
}
function handleDragOver(e)  {
    if(e.preventDefault) { e.preventDefault(); } //Allows us to drop
    e.dataTransfer.dropEffect = "move";
}
function handleDragLeave(e) { handleDragGetTr(e).classList.remove("dragover"); }
function handleDrop(e) {
    if(e.stopPropagation()) { e.stopPropagation(); } //Stops browser from redirecting
    if(selected != null && selected.tagName == "TR" && dragElt != null && dragElt.tagName == "TR") {
        var t = selected.parentNode.parentNode;
        //TODO: Some way to move a topic to position zero
        var move_from = dragElt.getAttribute("data-index"); //topic to move
        var move_to = selected.getAttribute("data-index");  //topic to move to the position on
        if(move_from != move_to) {
            window.location.href = t.getAttribute("data-context-path")
                + "/WoAdmin?action=AdminReorderTopics&reorderType=move"
                + "&topicFrom=" + move_from
                + "&topicTo=" + move_to
                + "&teacherId=" + t.getAttribute("data-teacher-id")
                + "&classId=" + t.getAttribute("data-class-id")
                + "&topicId=" + dragElt.getAttribute("data-topic-id");
        }
    }
    return false;
}
function handleDragEnd(e) {
    selected.classList.remove("dragover-above");
    selected.classList.remove("dragover-below");
    selected = null;
    dragElt.classList.remove("dragging");
    dragElt = null;
}
window.onload = function() {
    var rows = document.querySelectorAll("[draggable=true]");
    rows.forEach(function(row) {
        row.addEventListener("dragstart",   handleDragStart,    false);
        row.addEventListener("dragenter",   handleDragEnter,    false);
        row.addEventListener("dragover",    handleDragOver,     false);
        //row.addEventListener("dragleave",   handleDragLeave,    false);
        row.addEventListener("drop",        handleDrop,         false);
        row.addEventListener("dragend",     handleDragEnd,      false);
    })
}