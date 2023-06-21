function generate_year_range() {
  let currenYearStr =  new Date().getFullYear();
  
  var currentYear = Number(currenYearStr);
  var years = "";
  for (var year = currentYear - 3; year <= currentYear; year++) {
	  if (year == currentYear)  { 
		  years += "<option value='" + year + "' checked>" + year + "</option>";
	  }
	  else {
		  years += "<option value='" + year + "'>" + year + "</option>";
	  }
  }
  return years;
}

var today = new Date();
document.getElementById("selectDay_m1_cal1").value = "0";
var currentMonth_m1_cal1 = today.getMonth();
var currentYear_m1_cal1 = today.getFullYear();
var selectYear_m1_cal1 = document.getElementById("year_m1_cal1");
var selectMonth_m1_cal1 = document.getElementById("month_m1_cal1");

document.getElementById("year_m1_cal1").innerHTML = generate_year_range();

var calendar_m1_cal1 = document.getElementById("calendar_m1_cal1");
var lang_m1_cal1 = calendar_m1_cal1.getAttribute('data-lang');

var months_m1_cal1 = [];
initMonthNames_m1_cal1();
var days_m1_cal1 = []; 
initDayNames_m1_cal1();

var dayHeader_m1_cal1 = "<tr>";
for (day in days_m1_cal1) {
  dayHeader_m1_cal1 += "<th data-days='" + days_m1_cal1[day] + "'>" + days_m1_cal1[day] + "</th>";
}
dayHeader_m1_cal1 += "</tr>";

document.getElementById("thead-month_m1_cal1").innerHTML = dayHeader_m1_cal1;
monthAndYear_m1_cal1 = document.getElementById("monthAndYear_m1_cal1");

function initMonthNames_m1_cal1() {
	months_m1_cal1.push(jan_name);
	months_m1_cal1.push(feb_name);
	months_m1_cal1.push(mar_name);
	months_m1_cal1.push(apr_name);
	months_m1_cal1.push(may_name);
	months_m1_cal1.push(jun_name);
	months_m1_cal1.push(jul_name);
	months_m1_cal1.push(aug_name);
	months_m1_cal1.push(sep_name);
	months_m1_cal1.push(oct_name);
	months_m1_cal1.push(nov_name);
	months_m1_cal1.push(dec_name);
}

function initDayNames_m1_cal1() {
	days_m1_cal1.push(sun_name);
	days_m1_cal1.push(mon_name);
	days_m1_cal1.push(tue_name);
	days_m1_cal1.push(wed_name);
	days_m1_cal1.push(thu_name);
	days_m1_cal1.push(fri_name);
	days_m1_cal1.push(sat_name);
}

function initCalendar_m1_cal1() {
	today = new Date();
	document.getElementById("selectDay_m1_cal1").value = "0";
	currentMonth_m1_cal1 = today.getMonth();
	currentYear_m1_cal1 = today.getFullYear();
	selectYear_m1_cal1 = document.getElementById("year_m1_cal1");
	selectMonth_m1_cal1 = document.getElementById("month_m1_cal1");
	showCalendar_m1_cal1(0,currentMonth_m1_cal1, currentYear_m1_cal1);
}
function next_m1_cal1() {
  currentYear_m1_cal1 = (currentMonth_m1_cal1 === 11) ? currentYear_m1_cal1 + 1 : currentYear_m1_cal1;
  currentMonth_m1_cal1 = (currentMonth_m1_cal1 + 1) % 12;
  showCalendar_m1_cal1(0,currentMonth_m1_cal1, currentYear_m1_cal1);
}

function previous_m1_cal1() {
  currentYear_m1_cal1 = (currentMonth_m1_cal1 === 0) ? currentYear_m1_cal1 - 1 : currentYear_m1_cal1;
  currentMonth_m1_cal1 = (currentMonth_m1_cal1 === 0) ? 11 : currentMonth_m1_cal1 - 1;
  showCalendar_m1_cal1(0,currentMonth_m1_cal1, currentYear_m1_cal1);
}

function jump_m1_cal1() {
	  currentYear_m1_cal1 = parseInt(selectYear_m1_cal1.value);
	  currentMonth_m1_cal1 = parseInt(selectMonth_m1_cal1.value);
	  showCalendar_m1_cal1(0,currentMonth_m1_cal1, currentYear_m1_cal1);
}

function showCalendar_m1_cal1(day_m1_cal1, month_m1_cal1, year_m1_cal1) {

  document.getElementById("selectDay_m1_cal1").value = day_m1_cal1;
  var firstDay_m1_cal1 = ( new Date( year_m1_cal1, month_m1_cal1 ) ).getDay();

  tbl_m1_cal1 = document.getElementById("calendar-body_m1_cal1");

  
  tbl_m1_cal1.innerHTML = "";

  monthAndYear_m1_cal1.innerHTML = months_m1_cal1[month_m1_cal1] + " " + year_m1_cal1;
  selectYear_m1_cal1.value = year_m1_cal1;
  selectMonth_m1_cal1.value = month_m1_cal1;

  // creating all cells
  var date_m1_cal1 = 1;
  for ( var i = 0; i < 6; i++ ) {
      var row_m1_cal1 = document.createElement("tr");

      for ( var j = 0; j < 7; j++ ) {
          if ( i === 0 && j < firstDay_m1_cal1 ) {
              cell_m1_cal1 = document.createElement( "td" );
              cellText_m1_cal1 = document.createTextNode("");
              cell_m1_cal1.appendChild(cellText_m1_cal1);
              row_m1_cal1.appendChild(cell_m1_cal1);
          } else if (date_m1_cal1 > daysInMonth(month_m1_cal1, year_m1_cal1)) {
              break;
          } else {
              cell_m1_cal1 = document.createElement("td");
              cell_m1_cal1.id = "cell" + date_m1_cal1 + "_m1_cal1";
              cell_m1_cal1.setAttribute("data-date", date_m1_cal1);
              cell_m1_cal1.setAttribute("data-month", month_m1_cal1 + 1);
              cell_m1_cal1.setAttribute("data-year", year_m1_cal1);
              cell_m1_cal1.setAttribute("data-month_name", months_m1_cal1[month_m1_cal1]);
              cell_m1_cal1.className = "date-picker";
              cell_m1_cal1.innerHTML = "<span>" + date_m1_cal1 + "</span>";
              row_m1_cal1.appendChild(cell_m1_cal1);
              date_m1_cal1++;
          } 

      }

      tbl_m1_cal1.appendChild(row_m1_cal1);

  }
  
  
  
  document.getElementById("cell1_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell1_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 1;  });
  document.getElementById("cell2_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell2_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 2;  });
  document.getElementById("cell3_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell3_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 3;  });
  document.getElementById("cell4_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell4_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 4;  });
  document.getElementById("cell5_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell5_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 5;  });
  document.getElementById("cell6_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell6_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 6;  });
  document.getElementById("cell7_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell7_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 7;  });
  document.getElementById("cell8_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell8_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 8;  });
  document.getElementById("cell9_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell9_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 9;  });
  document.getElementById("cell10_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell10_m1_cal1").className = "date-picker selected"; document.getElementById("selectDay_m1_cal1").value = 10;  });

  document.getElementById("cell11_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell11_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 11;  });
  document.getElementById("cell12_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell12_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 12;  });
  document.getElementById("cell13_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell13_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 13;  });
  document.getElementById("cell14_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell14_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 14;  });
  document.getElementById("cell15_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell15_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 15;  });
  document.getElementById("cell16_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell16_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 16;  });
  document.getElementById("cell17_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell17_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 17;  });
  document.getElementById("cell18_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell18_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 18;  });
  document.getElementById("cell19_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell19_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 19;  });
  document.getElementById("cell20_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell20_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 20;  });

  document.getElementById("cell21_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell21_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 21;  });
  document.getElementById("cell22_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell22_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 22;  });
  document.getElementById("cell23_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell23_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 23;  });
  document.getElementById("cell24_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell24_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 24;  });
  document.getElementById("cell25_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell25_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 25;  });
  document.getElementById("cell26_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell26_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 26;  });
  document.getElementById("cell27_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell27_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 27;  });
  document.getElementById("cell28_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell28_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 28;  });
  if (daysInMonth(month_m1_cal1, year_m1_cal1) > 28)
	  document.getElementById("cell29_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell29_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 29;  });
  if (daysInMonth(month_m1_cal1, year_m1_cal1) > 29)
	  document.getElementById("cell30_m1_cal1").addEventListener("click", function() {  clearCalendar_m1_cal1(); document.getElementById("cell30_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 30;  });
  if (daysInMonth(month_m1_cal1, year_m1_cal1) > 30)
	  document.getElementById("cell31_m1_cal1").addEventListener("click", function() { clearCalendar_m1_cal1();  document.getElementById("cell31_m1_cal1").className = "date-picker selected";	document.getElementById("selectDay_m1_cal1").value = 31;  });

}  
  
function clearCalendar_m1_cal1() {


  document.getElementById("cell1_m1_cal1").className = "date-picker ";;
  document.getElementById("cell2_m1_cal1").className = "date-picker ";;
  document.getElementById("cell3_m1_cal1").className = "date-picker ";;
  document.getElementById("cell4_m1_cal1").className = "date-picker ";;
  document.getElementById("cell5_m1_cal1").className = "date-picker ";;
  document.getElementById("cell6_m1_cal1").className = "date-picker ";;
  document.getElementById("cell7_m1_cal1").className = "date-picker ";;
  document.getElementById("cell8_m1_cal1").className = "date-picker ";;
  document.getElementById("cell9_m1_cal1").className = "date-picker ";;
  document.getElementById("cell10_m1_cal1").className = "date-picker ";;

  document.getElementById("cell11_m1_cal1").className = "date-picker ";;
  document.getElementById("cell12_m1_cal1").className = "date-picker ";;
  document.getElementById("cell13_m1_cal1").className = "date-picker ";;
  document.getElementById("cell14_m1_cal1").className = "date-picker ";;
  document.getElementById("cell15_m1_cal1").className = "date-picker ";;
  document.getElementById("cell16_m1_cal1").className = "date-picker ";;
  document.getElementById("cell17_m1_cal1").className = "date-picker ";;
  document.getElementById("cell18_m1_cal1").className = "date-picker ";;
  document.getElementById("cell19_m1_cal1").className = "date-picker ";;
  document.getElementById("cell20_m1_cal1").className = "date-picker ";;

  document.getElementById("cell21_m1_cal1").className = "date-picker ";;
  document.getElementById("cell22_m1_cal1").className = "date-picker ";;
  document.getElementById("cell23_m1_cal1").className = "date-picker ";;
  document.getElementById("cell24_m1_cal1").className = "date-picker ";;
  document.getElementById("cell25_m1_cal1").className = "date-picker ";;
  document.getElementById("cell26_m1_cal1").className = "date-picker ";;
  document.getElementById("cell27_m1_cal1").className = "date-picker ";;
  document.getElementById("cell28_m1_cal1").className = "date-picker ";;
  if (daysInMonth(month_m1_cal1, year_m1_cal1) > 28)
	  document.getElementById("cell29_m1_cal1").className = "date-picker ";;
  if (daysInMonth(month_m1_cal1, year_m1_cal1) > 29)
	  document.getElementById("cell30_m1_cal1").className = "date-picker ";;
  if (daysInMonth(month_m1_cal1, year_m1_cal1) > 30)
	  document.getElementById("cell31_m1_cal1").className = "date-picker ";;

}

function daysInMonth(iMonth, iYear) {
  return 32 - new Date(iYear, iMonth, 32).getDate();
}
