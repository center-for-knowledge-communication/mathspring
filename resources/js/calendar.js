function generate_year_range(start, end) {
  var years = "";
  for (var year = start; year <= end; year++) {
      years += "<option value='" + year + "'>" + year + "</option>";
  }
  return years;
}

var today = new Date();
document.getElementById("selectDay").value = "0";
var currentMonth = today.getMonth();
var currentYear = today.getFullYear();
var selectYear = document.getElementById("year");
var selectMonth = document.getElementById("month");


var createYear = generate_year_range(2020, 2022);
/** or
* createYear = generate_year_range( 1970, currentYear );
*/

document.getElementById("year").innerHTML = createYear;

var calendar = document.getElementById("calendar");
var lang = calendar.getAttribute('data-lang');

var months = [];
initMonthNames();
var days = []; 
initDayNames();

var dayHeader = "<tr>";
for (day in days) {
  dayHeader += "<th data-days='" + days[day] + "'>" + days[day] + "</th>";
}
dayHeader += "</tr>";

document.getElementById("thead-month").innerHTML = dayHeader;
monthAndYear = document.getElementById("monthAndYear");

function initMonthNames() {
	months.push(jan_name);
	months.push(feb_name);
	months.push(mar_name);
	months.push(apr_name);
	months.push(may_name);
	months.push(jun_name);
	months.push(jul_name);
	months.push(aug_name);
	months.push(sep_name);
	months.push(oct_name);
	months.push(nov_name);
	months.push(dec_name);
}

function initDayNames() {
	days.push(sun_name);
	days.push(mon_name);
	days.push(tue_name);
	days.push(wed_name);
	days.push(thu_name);
	days.push(fri_name);
	days.push(sat_name);
}

function initCalendar() {
	today = new Date();
	document.getElementById("selectDay").value = "0";
	currentMonth = today.getMonth();
	currentYear = today.getFullYear();
	selectYear = document.getElementById("year");
	selectMonth = document.getElementById("month");
	showCalendar(0,currentMonth, currentYear);
}


function next() {
  currentYear = (currentMonth === 11) ? currentYear + 1 : currentYear;
  currentMonth = (currentMonth + 1) % 12;
  showCalendar(0,currentMonth, currentYear);
}

function previous() {
  currentYear = (currentMonth === 0) ? currentYear - 1 : currentYear;
  currentMonth = (currentMonth === 0) ? 11 : currentMonth - 1;
  showCalendar(0,currentMonth, currentYear);
}

function jump() {
	  currentYear = parseInt(selectYear.value);
	  currentMonth = parseInt(selectMonth.value);
	  showCalendar(0,currentMonth, currentYear);
}

function showCalendar(day, month, year) {

  document.getElementById("selectDay").value = day;
  var firstDay = ( new Date( year, month ) ).getDay();

  tbl = document.getElementById("calendar-body");

  
  tbl.innerHTML = "";

  monthAndYear.innerHTML = months[month] + " " + year;
  selectYear.value = year;
  selectMonth.value = month;

  // creating all cells
  var date = 1;
  for ( var i = 0; i < 6; i++ ) {
      var row = document.createElement("tr");

      for ( var j = 0; j < 7; j++ ) {
          if ( i === 0 && j < firstDay ) {
              cell = document.createElement( "td" );
              cellText = document.createTextNode("");
              cell.appendChild(cellText);
              row.appendChild(cell);
          } else if (date > daysInMonth(month, year)) {
              break;
          } else {
              cell = document.createElement("td");
              cell.id = "cell" + date;
              cell.setAttribute("data-date", date);
              cell.setAttribute("data-month", month + 1);
              cell.setAttribute("data-year", year);
              cell.setAttribute("data-month_name", months[month]);
              cell.className = "date-picker";
              cell.innerHTML = "<span>" + date + "</span>";
              row.appendChild(cell);
              date++;
          } 

      }

      tbl.appendChild(row);

  }
  
  
  document.getElementById("cell1").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell1").className = "date-picker selected";	document.getElementById("selectDay").value = 1;  });
  document.getElementById("cell2").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell2").className = "date-picker selected";	document.getElementById("selectDay").value = 2;  });
  document.getElementById("cell3").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell3").className = "date-picker selected";	document.getElementById("selectDay").value = 3;  });
  document.getElementById("cell4").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell4").className = "date-picker selected";	document.getElementById("selectDay").value = 4;  });
  document.getElementById("cell5").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell5").className = "date-picker selected";	document.getElementById("selectDay").value = 5;  });
  document.getElementById("cell6").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell6").className = "date-picker selected";	document.getElementById("selectDay").value = 6;  });
  document.getElementById("cell7").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell7").className = "date-picker selected";	document.getElementById("selectDay").value = 7;  });
  document.getElementById("cell8").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell8").className = "date-picker selected";	document.getElementById("selectDay").value = 8;  });
  document.getElementById("cell9").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell9").className = "date-picker selected";	document.getElementById("selectDay").value = 9;  });
  document.getElementById("cell10").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell10").className = "date-picker selected"; document.getElementById("selectDay").value = 10;  });

  document.getElementById("cell11").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell11").className = "date-picker selected";	document.getElementById("selectDay").value = 11;  });
  document.getElementById("cell12").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell12").className = "date-picker selected";	document.getElementById("selectDay").value = 12;  });
  document.getElementById("cell13").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell13").className = "date-picker selected";	document.getElementById("selectDay").value = 13;  });
  document.getElementById("cell14").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell14").className = "date-picker selected";	document.getElementById("selectDay").value = 14;  });
  document.getElementById("cell15").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell15").className = "date-picker selected";	document.getElementById("selectDay").value = 15;  });
  document.getElementById("cell16").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell16").className = "date-picker selected";	document.getElementById("selectDay").value = 16;  });
  document.getElementById("cell17").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell17").className = "date-picker selected";	document.getElementById("selectDay").value = 17;  });
  document.getElementById("cell18").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell18").className = "date-picker selected";	document.getElementById("selectDay").value = 18;  });
  document.getElementById("cell19").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell19").className = "date-picker selected";	document.getElementById("selectDay").value = 19;  });
  document.getElementById("cell20").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell20").className = "date-picker selected";	document.getElementById("selectDay").value = 20;  });

  document.getElementById("cell21").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell21").className = "date-picker selected";	document.getElementById("selectDay").value = 21;  });
  document.getElementById("cell22").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell22").className = "date-picker selected";	document.getElementById("selectDay").value = 22;  });
  document.getElementById("cell23").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell23").className = "date-picker selected";	document.getElementById("selectDay").value = 23;  });
  document.getElementById("cell24").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell24").className = "date-picker selected";	document.getElementById("selectDay").value = 24;  });
  document.getElementById("cell25").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell25").className = "date-picker selected";	document.getElementById("selectDay").value = 25;  });
  document.getElementById("cell26").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell26").className = "date-picker selected";	document.getElementById("selectDay").value = 26;  });
  document.getElementById("cell27").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell27").className = "date-picker selected";	document.getElementById("selectDay").value = 27;  });
  document.getElementById("cell28").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell28").className = "date-picker selected";	document.getElementById("selectDay").value = 28;  });
  if (daysInMonth(month, year) > 28)
	  document.getElementById("cell29").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell29").className = "date-picker selected";	document.getElementById("selectDay").value = 29;  });
  if (daysInMonth(month, year) > 29)
	  document.getElementById("cell30").addEventListener("click", function() {  clearCalendar(); document.getElementById("cell30").className = "date-picker selected";	document.getElementById("selectDay").value = 30;  });
  if (daysInMonth(month, year) > 30)
	  document.getElementById("cell31").addEventListener("click", function() { clearCalendar();  document.getElementById("cell31").className = "date-picker selected";	document.getElementById("selectDay").value = 31;  });

}  
  
function clearCalendar() {


  document.getElementById("cell1").className = "date-picker";
  document.getElementById("cell2").className = "date-picker";
  document.getElementById("cell3").className = "date-picker";
  document.getElementById("cell4").className = "date-picker";
  document.getElementById("cell5").className = "date-picker";
  document.getElementById("cell6").className = "date-picker";
  document.getElementById("cell7").className = "date-picker";
  document.getElementById("cell8").className = "date-picker";
  document.getElementById("cell9").className = "date-picker";
  document.getElementById("cell10").className = "date-picker";

  document.getElementById("cell11").className = "date-picker";
  document.getElementById("cell12").className = "date-picker";
  document.getElementById("cell13").className = "date-picker";
  document.getElementById("cell14").className = "date-picker";
  document.getElementById("cell15").className = "date-picker";
  document.getElementById("cell16").className = "date-picker";
  document.getElementById("cell17").className = "date-picker";
  document.getElementById("cell18").className = "date-picker";
  document.getElementById("cell19").className = "date-picker";
  document.getElementById("cell20").className = "date-picker";

  document.getElementById("cell21").className = "date-picker";
  document.getElementById("cell22").className = "date-picker";
  document.getElementById("cell23").className = "date-picker";
  document.getElementById("cell24").className = "date-picker";
  document.getElementById("cell25").className = "date-picker";
  document.getElementById("cell26").className = "date-picker";
  document.getElementById("cell27").className = "date-picker";
  document.getElementById("cell28").className = "date-picker";
  if (daysInMonth(month, year) > 28)
	  document.getElementById("cell29").className = "date-picker";
  if (daysInMonth(month, year) > 29)
	  document.getElementById("cell30").className = "date-picker";
  if (daysInMonth(month, year) > 30)
	  document.getElementById("cell31").className = "date-picker";

}

function daysInMonth(iMonth, iYear) {
  return 32 - new Date(iYear, iMonth, 32).getDate();
}
