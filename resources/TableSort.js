var sortCol = 0
var previousCol = -1
var numRows = 0;
var numCols = 0;
var tableObj;
var tableStuff;
var tableMatrix;
var colTypeIsNan;

function readTable(tableID) {
	// read the table to be sorted into a two-dimensional array
	tableObj = document.getElementById(tableID);
	numRows = tableObj.rows.length;
	numCols = tableObj.rows.item(0).cells.length;
	tableMatrix = new Array(numRows);
  colTypeIsNan = new Array(numCols);
  tableStuff
  
  for (i=0; i<numCols; i++) {
		colTypeIsNan[i] = false;
  }
  
	for (i=0; i<numRows; i++) {
		tableMatrix[i] = new Array(numCols)
		for (j=0; j<numCols; j++) {
			tableMatrix[i][j] = [tableObj.rows.item(i).cells.item(j).innerHTML, tableObj.rows.item(i).cells.item(j).bgColor];
      // check data type
      if (isNaN(tableMatrix[i][j][0]))
      	colTypeIsNan[j] = true;
		}
	}
}

function sortStr(a, b) {
	// sort alphabetically
	strA = a[sortCol][0].toLowerCase();
	strB = b[sortCol][0].toLowerCase();
	if (strA < strB)
  	return -1;
	else if (strA > strB)
  	return 1;
	else 
  	return 0;
}

function sortNum(a, b) {
	// sort by value
	return a[sortCol][0] - b[sortCol][0];
}

function sortTable(colToSort, isAscending) {

	sortCol = colToSort;
	
  if (colTypeIsNan[sortCol][0])
  	// this column contains string data
   tableMatrix.sort(sortStr);
  else
  	// this column contains numeric data only
  	tableMatrix.sort(sortNum);
    
  if (!isAscending)
  	tableMatrix.reverse();
	
	// write the sorted values back to the table
	for (i=0; i<numRows; i++) {
		for (j=0; j<numCols; j++) {
			tableObj.rows.item(i).cells.item(j).innerHTML = tableMatrix[i][j][0];
			tableObj.rows.item(i).cells.item(j).bgColor = tableMatrix[i][j][1];
		}
	}
}

function insertButtons(col) {
	// insert ascending/descending button tags
	document.write(" <button onclick='sortTable(" + col + ", true)'>&darr;</button>");
  document.write("<button onclick='sortTable(" + col + ", false)'>&uarr;</button>");
}
