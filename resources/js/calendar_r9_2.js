function generate_year_range() {
  let currenYearStr =  new Date().getFullYear();
  
  var currentYear = Number(currenYearStr);
  var years = "";
  for (var year = currentYear - 5; year <= currentYear; year++) {
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
document.getElementById("selectDay_r9_cal2").value = "0";
var currentMonth_r9_cal2 = today.getMonth();
var currentYear_r9_cal2 = today.getFullYear();
var selectYear_r9_cal2 = document.getElementById("year_r9_cal2");
var selectMonth_r9_cal2 = document.getElementById("month_r9_cal2");

document.getElementById("year_r9_cal2").innerHTML = generate_year_range();

var calendar_r9_cal2 = document.getElementById("calendar_r9_cal2");
var lang_r9_cal2 = calendar_r9_cal2.getAttribute('data-lang');

var months_r9_cal2 = [];
initMonthNames_r9_cal2();
var days_r9_cal2 = []; 
initDayNames_r9_cal2();

var dayHeader_r9_cal2 = "<tr>";
for (day in days_r9_cal2) {
  dayHeader_r9_cal2 += "<th data-days='" + days_r9_cal2[day] + "'>" + days_r9_cal2[day] + "</th>";
}
dayHeader_r9_cal2 += "</tr>";

document.getElementById("thead-month_r9_cal2").innerHTML = dayHeader_r9_cal2;
monthAndYear_r9_cal2 = document.getElementById("monthAndYear_r9_cal2");

function initMonthNames_r9_cal2() {
	months_r9_cal2.push(jan_name);
	months_r9_cal2.push(feb_name);
	months_r9_cal2.push(mar_name);
	months_r9_cal2.push(apr_name);
	months_r9_cal2.push(may_name);
	months_r9_cal2.push(jun_name);
	months_r9_cal2.push(jul_name);
	months_r9_cal2.push(aug_name);
	months_r9_cal2.push(sep_name);
	months_r9_cal2.push(oct_name);
	months_r9_cal2.push(nov_name);
	months_r9_cal2.push(dec_name);
}

function initDayNames_r9_cal2() {
	days_r9_cal2.push(sun_name);
	days_r9_cal2.push(mon_name);
	days_r9_cal2.push(tue_name);
	days_r9_cal2.push(wed_name);
	days_r9_cal2.push(thu_name);
	days_r9_cal2.push(fri_name);
	days_r9_cal2.push(sat_name);
}
function initCalendar_r9_cal2() {
	today = new Date();
	document.getElementById("selectDay_r9_cal2").value = "0";
	currentMonth_r9_cal2 = today.getMonth();
	currentYear_r9_cal2 = today.getFullYear();
	selectYear_r9_cal2 = document.getElementById("year_r9_cal2");
	selectMonth_r9_cal2 = document.getElementById("month_r9_cal2");
	showCalendar_r9_cal2(0,currentMonth_r9_cal2, currentYear_r9_cal2);
}
function next_r9_cal2() {
  currentYear_r9_cal2 = (currentMonth_r9_cal2 === 11) ? currentYear_r9_cal2 + 1 : currentYear_r9_cal2;
  currentMonth_r9_cal2 = (currentMonth_r9_cal2 + 1) % 12;
  showCalendar_r9_cal2(0,currentMonth_r9_cal2, currentYear_r9_cal2);
}

function previous_r9_cal2() {
  currentYear_r9_cal2 = (currentMonth_r9_cal2 === 0) ? currentYear_r9_cal2 - 1 : currentYear_r9_cal2;
  currentMonth_r9_cal2 = (currentMonth_r9_cal2 === 0) ? 11 : currentMonth_r9_cal2 - 1;
  showCalendar_r9_cal2(0,currentMonth_r9_cal2, currentYear_r9_cal2);
}

function jump_r9_cal2() {
	  currentYear_r9_cal2 = parseInt(selectYear_r9_cal2.value);
	  currentMonth_r9_cal2 = parseInt(selectMonth_r9_cal2.value);
	  showCalendar_r9_cal2(0,currentMonth_r9_cal2, currentYear_r9_cal2);
}

function showCalendar_r9_cal2(day_r9_cal2, month_r9_cal2, year_r9_cal2) {

  document.getElementById("selectDay_r9_cal2").value = day_r9_cal2;
  var firstDay_r9_cal2 = ( new Date( year_r9_cal2, month_r9_cal2 ) ).getDay();

  tbl_r9_cal2 = document.getElementById("calendar-body_r9_cal2");

  
  tbl_r9_cal2.innerHTML = "";

  monthAndYear_r9_cal2.innerHTML = months_r9_cal2[month_r9_cal2] + " " + year_r9_cal2;
  selectYear_r9_cal2.value = year_r9_cal2;
  selectMonth_r9_cal2.value = month_r9_cal2;

  // creating all cells
  var date_r9_cal2 = 1;
  for ( var i = 0; i < 6; i++ ) {
      var row_r9_cal2 = document.createElement("tr");

      for ( var j = 0; j < 7; j++ ) {
          if ( i === 0 && j < firstDay_r9_cal2 ) {
              cell_r9_cal2 = document.createElement( "td" );
              cellText_r9_cal2 = document.createTextNode("");
              cell_r9_cal2.appendChild(cellText_r9_cal2);
              row_r9_cal2.appendChild(cell_r9_cal2);
          } else if (date_r9_cal2 > daysInMonth(month_r9_cal2, year_r9_cal2)) {
              break;
          } else {
              cell_r9_cal2 = document.createElement("td");
              cell_r9_cal2.id = "cell" + date_r9_cal2 + "_r9_cal2";
              cell_r9_cal2.setAttribute("data-date", date_r9_cal2);
              cell_r9_cal2.setAttribute("data-month", month_r9_cal2 + 1);
              cell_r9_cal2.setAttribute("data-year", year_r9_cal2);
              cell_r9_cal2.setAttribute("data-month_name", months_r9_cal2[month_r9_cal2]);
              cell_r9_cal2.className = "date-picker";
              cell_r9_cal2.innerHTML = "<span>" + date_r9_cal2 + "</span>";
              row_r9_cal2.appendChild(cell_r9_cal2);
              date_r9_cal2++;
          } 

      }

      tbl_r9_cal2.appendChild(row_r9_cal2);

  }
  
  
  
  document.getElementById("cell1_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell1_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 1;  });
  document.getElementById("cell2_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell2_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 2;  });
  document.getElementById("cell3_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell3_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 3;  });
  document.getElementById("cell4_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell4_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 4;  });
  document.getElementById("cell5_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell5_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 5;  });
  document.getElementById("cell6_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell6_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 6;  });
  document.getElementById("cell7_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell7_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 7;  });
  document.getElementById("cell8_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell8_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 8;  });
  document.getElementById("cell9_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell9_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 9;  });
  document.getElementById("cell10_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell10_r9_cal2").className = "date-picker selected"; document.getElementById("selectDay_r9_cal2").value = 10;  });

  document.getElementById("cell11_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell11_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 11;  });
  document.getElementById("cell12_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell12_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 12;  });
  document.getElementById("cell13_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell13_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 13;  });
  document.getElementById("cell14_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell14_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 14;  });
  document.getElementById("cell15_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell15_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 15;  });
  document.getElementById("cell16_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell16_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 16;  });
  document.getElementById("cell17_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell17_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 17;  });
  document.getElementById("cell18_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell18_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 18;  });
  document.getElementById("cell19_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell19_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 19;  });
  document.getElementById("cell20_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell20_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 20;  });

  document.getElementById("cell21_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell21_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 21;  });
  document.getElementById("cell22_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell22_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 22;  });
  document.getElementById("cell23_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell23_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 23;  });
  document.getElementById("cell24_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell24_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 24;  });
  document.getElementById("cell25_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell25_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 25;  });
  document.getElementById("cell26_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell26_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 26;  });
  document.getElementById("cell27_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell27_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 27;  });
  document.getElementById("cell28_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell28_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 28;  });
  if (daysInMonth(month_r9_cal2, year_r9_cal2) > 28)
	  document.getElementById("cell29_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell29_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 29;  });
  if (daysInMonth(month_r9_cal2, year_r9_cal2) > 29)
	  document.getElementById("cell30_r9_cal2").addEventListener("click", function() {  clearCalendar_r9_cal2(); document.getElementById("cell30_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 30;  });
  if (daysInMonth(month_r9_cal2, year_r9_cal2) > 30)
	  document.getElementById("cell31_r9_cal2").addEventListener("click", function() { clearCalendar_r9_cal2();  document.getElementById("cell31_r9_cal2").className = "date-picker selected";	document.getElementById("selectDay_r9_cal2").value = 31;  });

}  
  
function clearCalendar_r9_cal2() {


  document.getElementById("cell1_r9_cal2").className = "date-picker ";;
  document.getElementById("cell2_r9_cal2").className = "date-picker ";;
  document.getElementById("cell3_r9_cal2").className = "date-picker ";;
  document.getElementById("cell4_r9_cal2").className = "date-picker ";;
  document.getElementById("cell5_r9_cal2").className = "date-picker ";;
  document.getElementById("cell6_r9_cal2").className = "date-picker ";;
  document.getElementById("cell7_r9_cal2").className = "date-picker ";;
  document.getElementById("cell8_r9_cal2").className = "date-picker ";;
  document.getElementById("cell9_r9_cal2").className = "date-picker ";;
  document.getElementById("cell10_r9_cal2").className = "date-picker ";;

  document.getElementById("cell11_r9_cal2").className = "date-picker ";;
  document.getElementById("cell12_r9_cal2").className = "date-picker ";;
  document.getElementById("cell13_r9_cal2").className = "date-picker ";;
  document.getElementById("cell14_r9_cal2").className = "date-picker ";;
  document.getElementById("cell15_r9_cal2").className = "date-picker ";;
  document.getElementById("cell16_r9_cal2").className = "date-picker ";;
  document.getElementById("cell17_r9_cal2").className = "date-picker ";;
  document.getElementById("cell18_r9_cal2").className = "date-picker ";;
  document.getElementById("cell19_r9_cal2").className = "date-picker ";;
  document.getElementById("cell20_r9_cal2").className = "date-picker ";;

  document.getElementById("cell21_r9_cal2").className = "date-picker ";;
  document.getElementById("cell22_r9_cal2").className = "date-picker ";;
  document.getElementById("cell23_r9_cal2").className = "date-picker ";;
  document.getElementById("cell24_r9_cal2").className = "date-picker ";;
  document.getElementById("cell25_r9_cal2").className = "date-picker ";;
  document.getElementById("cell26_r9_cal2").className = "date-picker ";;
  document.getElementById("cell27_r9_cal2").className = "date-picker ";;
  document.getElementById("cell28_r9_cal2").className = "date-picker ";;
  if (daysInMonth(month_r9_cal2, year_r9_cal2) > 28)
	  document.getElementById("cell29_r9_cal2").className = "date-picker ";;
  if (daysInMonth(month_r9_cal2, year_r9_cal2) > 29)
	  document.getElementById("cell30_r9_cal2").className = "date-picker ";;
  if (daysInMonth(month_r9_cal2, year_r9_cal2) > 30)
	  document.getElementById("cell31_r9_cal2").className = "date-picker ";;

}

function daysInMonth(iMonth, iYear) {
  return 32 - new Date(iYear, iMonth, 32).getDate();
}
