var problemImage;

function gotoInitialLayout() {
    var cell = document.getElementById('problemImageCell');
    for (i=0;i<cell.childNodes.length;i++) {
	  var n = cell.childNodes(i);
	  if (n.nodeName.toLowerCase() == "img")
	      cell.removeChild(n);
    }
}
function gotoPrevHint () {

}
function gotoNextHint () {
}
function gotoLastHint () {
}

 function createElementFromString(str) {
	var node, a = str.match(/<(\w+)(\s+)?([^>]+)?>/);
	if (a != null) {
	node = document.createElement(a[1]);
	if (a[3] != null) {
	var attrs = a[3].split(" ");
	if (attrs.length > 0) {
	for ( var i = 0; i < attrs.length; i++) {
	var att = attrs[i].split("=");
	if (att[0].length > 0 &&
	att[0] != "/" && att[1].length != 2) {
	var a_n = document.createAttribute(att[0]);
	a_n.value = att[1].replace(/^['"](.+)['"]$/, "$1");
	node.setAttributeNode(a_n);
	}
	}
	}
	}
	}
	return node;
	}