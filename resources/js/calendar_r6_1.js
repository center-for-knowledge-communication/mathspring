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
document.getElementById("selectDay_r6_cal1").value = "0";
var currentMonth_r6_cal1 = today.getMonth();
var currentYear_r6_cal1 = today.getFullYear();
var selectYear_r6_cal1 = document.getElementById("year_r6_cal1");
var selectMonth_r6_cal1 = document.getElementById("month_r6_cal1");

document.getElementById("year_r6_cal1").innerHTML = generate_year_range();

var calendar_r6_cal1 = document.getElementById("calendar_r6_cal1");
var lang_r6_cal1 = calendar_r6_cal1.getAttribute('data-lang');

var months_r6_cal1 = [];
initMonthNames_r6_cal1();
var days_r6_cal1 = []; 
initDayNames_r6_cal1();

var dayHeader_r6_cal1 = "<tr>";
for (day in days_r6_cal1) {
  dayHeader_r6_cal1 += "<th data-days='" + days_r6_cal1[day] + "'>" + days_r6_cal1[day] + "</th>";
}
dayHeader_r6_cal1 += "</tr>";

document.getElementById("thead-month_r6_cal1").innerHTML = dayHeader_r6_cal1;
monthAndYear_r6_cal1 = document.getElementById("monthAndYear_r6_cal1");

function initMonthNames_r6_cal1() {
	months_r6_cal1.push(jan_name);
	months_r6_cal1.push(feb_name);
	months_r6_cal1.push(mar_name);
	months_r6_cal1.push(apr_name);
	months_r6_cal1.push(may_name);
	months_r6_cal1.push(jun_name);
	months_r6_cal1.push(jul_name);
	months_r6_cal1.push(aug_name);
	months_r6_cal1.push(sep_name);
	months_r6_cal1.push(oct_name);
	months_r6_cal1.push(nov_name);
	months_r6_cal1.push(dec_name);
}

function initDayNames_r6_cal1() {
	days_r6_cal1.push(sun_name);
	days_r6_cal1.push(mon_name);
	days_r6_cal1.push(tue_name);
	days_r6_cal1.push(wed_name);
	days_r6_cal1.push(thu_name);
	days_r6_cal1.push(fri_name);
	days_r6_cal1.push(sat_name);
}
function initCalendar_r6_cal1() {

	today = new Date();
	document.getElementById("selectDay_r6_cal1").value = "0";
	currentMonth_r6_cal1 = today.getMonth();
	currentYear_r6_cal1 = today.getFullYear();
	selectYear_r6_cal1 = document.getElementById("year_r6_cal1");
	selectMonth_r6_cal1 = document.getElementById("month_r6_cal1");
	showCalendar_r6_cal1(0,currentMonth_r6_cal1, currentYear_r6_cal1);
}
function next_r6_cal1() {
  currentYear_r6_cal1 = (currentMonth_r6_cal1 === 11) ? currentYear_r6_cal1 + 1 : currentYear_r6_cal1;
  currentMonth_r6_cal1 = (currentMonth_r6_cal1 + 1) % 12;
  showCalendar_r6_cal1(0,currentMonth_r6_cal1, currentYear_r6_cal1);
}

function previous_r6_cal1() {
  currentYear_r6_cal1 = (currentMonth_r6_cal1 === 0) ? currentYear_r6_cal1 - 1 : currentYear_r6_cal1;
  currentMonth_r6_cal1 = (currentMonth_r6_cal1 === 0) ? 11 : currentMonth_r6_cal1 - 1;
  showCalendar_r6_cal1(0,currentMonth_r6_cal1, currentYear_r6_cal1);
}

function jump_r6_cal1() {
	  currentYear_r6_cal1 = parseInt(selectYear_r6_cal1.value);
	  currentMonth_r6_cal1 = parseInt(selectMonth_r6_cal1.value);
	  showCalendar_r6_cal1(0,currentMonth_r6_cal1, currentYear_r6_cal1);
}

function showCalendar_r6_cal1(day_r6_cal1, month_r6_cal1, year_r6_cal1) {

  document.getElementById("selectDay_r6_cal1").value = day_r6_cal1;
  var firstDay_r6_cal1 = ( new Date( year_r6_cal1, month_r6_cal1 ) ).getDay();

  tbl_r6_cal1 = document.getElementById("calendar-body_r6_cal1");

  
  tbl_r6_cal1.innerHTML = "";

  monthAndYear_r6_cal1.innerHTML = months_r6_cal1[month_r6_cal1] + " " + year_r6_cal1;
  selectYear_r6_cal1.value = year_r6_cal1;
  selectMonth_r6_cal1.value = month_r6_cal1;

  // creating all cells
  var date_r6_cal1 = 1;
  for ( var i = 0; i < 6; i++ ) {
      var row_r6_cal1 = document.createElement("tr");

      for ( var j = 0; j < 7; j++ ) {
          if ( i === 0 && j < firstDay_r6_cal1 ) {
              cell_r6_cal1 = document.createElement( "td" );
              cellText_r6_cal1 = document.createTextNode("");
              cell_r6_cal1.appendChild(cellText_r6_cal1);
              row_r6_cal1.appendChild(cell_r6_cal1);
          } else if (date_r6_cal1 > daysInMonth(month_r6_cal1, year_r6_cal1)) {
              break;
          } else {
              cell_r6_cal1 = document.createElement("td");
              cell_r6_cal1.id = "cell" + date_r6_cal1 + "_r6_cal1";
              cell_r6_cal1.setAttribute("data-date", date_r6_cal1);
              cell_r6_cal1.setAttribute("data-month", month_r6_cal1 + 1);
              cell_r6_cal1.setAttribute("data-year", year_r6_cal1);
              cell_r6_cal1.setAttribute("data-month_name", months_r6_cal1[month_r6_cal1]);
              cell_r6_cal1.className = "date-picker";
              cell_r6_cal1.innerHTML = "<span>" + date_r6_cal1 + "</span>";
              row_r6_cal1.appendChild(cell_r6_cal1);
              date_r6_cal1++;
          } 

      }

      tbl_r6_cal1.appendChild(row_r6_cal1);

  }
  
  
  
  document.getElementById("cell1_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell1_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 1;  });
  document.getElementById("cell2_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell2_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 2;  });
  document.getElementById("cell3_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell3_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 3;  });
  document.getElementById("cell4_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell4_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 4;  });
  document.getElementById("cell5_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell5_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 5;  });
  document.getElementById("cell6_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell6_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 6;  });
  document.getElementById("cell7_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell7_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 7;  });
  document.getElementById("cell8_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell8_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 8;  });
  document.getElementById("cell9_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell9_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 9;  });
  document.getElementById("cell10_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell10_r6_cal1").className = "date-picker selected"; document.getElementById("selectDay_r6_cal1").value = 10;  });

  document.getElementById("cell11_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell11_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 11;  });
  document.getElementById("cell12_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell12_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 12;  });
  document.getElementById("cell13_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell13_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 13;  });
  document.getElementById("cell14_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell14_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 14;  });
  document.getElementById("cell15_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell15_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 15;  });
  document.getElementById("cell16_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell16_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 16;  });
  document.getElementById("cell17_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell17_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 17;  });
  document.getElementById("cell18_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell18_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 18;  });
  document.getElementById("cell19_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell19_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 19;  });
  document.getElementById("cell20_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell20_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 20;  });

  document.getElementById("cell21_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell21_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 21;  });
  document.getElementById("cell22_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell22_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 22;  });
  document.getElementById("cell23_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell23_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 23;  });
  document.getElementById("cell24_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell24_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 24;  });
  document.getElementById("cell25_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell25_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 25;  });
  document.getElementById("cell26_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell26_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 26;  });
  document.getElementById("cell27_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell27_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 27;  });
  document.getElementById("cell28_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell28_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 28;  });
  if (daysInMonth(month_r6_cal1, year_r6_cal1) > 28)
	  document.getElementById("cell29_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell29_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 29;  });
  if (daysInMonth(month_r6_cal1, year_r6_cal1) > 29)
	  document.getElementById("cell30_r6_cal1").addEventListener("click", function() {  clearCalendar_r6_cal1(); document.getElementById("cell30_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 30;  });
  if (daysInMonth(month_r6_cal1, year_r6_cal1) > 30)
	  document.getElementById("cell31_r6_cal1").addEventListener("click", function() { clearCalendar_r6_cal1();  document.getElementById("cell31_r6_cal1").className = "date-picker selected";	document.getElementById("selectDay_r6_cal1").value = 31;  });

}  
  
function clearCalendar_r6_cal1() {


  document.getElementById("cell1_r6_cal1").className = "date-picker ";;
  document.getElementById("cell2_r6_cal1").className = "date-picker ";;
  document.getElementById("cell3_r6_cal1").className = "date-picker ";;
  document.getElementById("cell4_r6_cal1").className = "date-picker ";;
  document.getElementById("cell5_r6_cal1").className = "date-picker ";;
  document.getElementById("cell6_r6_cal1").className = "date-picker ";;
  document.getElementById("cell7_r6_cal1").className = "date-picker ";;
  document.getElementById("cell8_r6_cal1").className = "date-picker ";;
  document.getElementById("cell9_r6_cal1").className = "date-picker ";;
  document.getElementById("cell10_r6_cal1").className = "date-picker ";;

  document.getElementById("cell11_r6_cal1").className = "date-picker ";;
  document.getElementById("cell12_r6_cal1").className = "date-picker ";;
  document.getElementById("cell13_r6_cal1").className = "date-picker ";;
  document.getElementById("cell14_r6_cal1").className = "date-picker ";;
  document.getElementById("cell15_r6_cal1").className = "date-picker ";;
  document.getElementById("cell16_r6_cal1").className = "date-picker ";;
  document.getElementById("cell17_r6_cal1").className = "date-picker ";;
  document.getElementById("cell18_r6_cal1").className = "date-picker ";;
  document.getElementById("cell19_r6_cal1").className = "date-picker ";;
  document.getElementById("cell20_r6_cal1").className = "date-picker ";;

  document.getElementById("cell21_r6_cal1").className = "date-picker ";;
  document.getElementById("cell22_r6_cal1").className = "date-picker ";;
  document.getElementById("cell23_r6_cal1").className = "date-picker ";;
  document.getElementById("cell24_r6_cal1").className = "date-picker ";;
  document.getElementById("cell25_r6_cal1").className = "date-picker ";;
  document.getElementById("cell26_r6_cal1").className = "date-picker ";;
  document.getElementById("cell27_r6_cal1").className = "date-picker ";;
  document.getElementById("cell28_r6_cal1").className = "date-picker ";;
  if (daysInMonth(month_r6_cal1, year_r6_cal1) > 28)
	  document.getElementById("cell29_r6_cal1").className = "date-picker ";;
  if (daysInMonth(month_r6_cal1, year_r6_cal1) > 29)
	  document.getElementById("cell30_r6_cal1").className = "date-picker ";;
  if (daysInMonth(month_r6_cal1, year_r6_cal1) > 30)
	  document.getElementById("cell31_r6_cal1").className = "date-picker ";;

}

function daysInMonth(iMonth, iYear) {
  return 32 - new Date(iYear, iMonth, 32).getDate();
}
