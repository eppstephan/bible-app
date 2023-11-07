function init() {
	var inputBook = document.getElementById('inputBook');
	var selectChapter = document.getElementById('selectChapter');
	if (inputBook.value == '') {
		selectChapter.disabled = true;
		selectChapter.options.add(new Option("Kapitel"));
	} else {
		populateChapters(inputBook.value);
		selectChapter.value = localStorage.getItem("selectedChapterKey");
	}

	initNavigation();
}

function initNavigation() {
	var spanPrev = document.getElementById('spanPrev');
	var spanNext = document.getElementById('spanNext');
	var inputBook = document.getElementById('inputBook');
	var selectChapter = document.getElementById('selectChapter');
	if (inputBook.value != '' && selectChapter.value < chapters[books.indexOf(inputBook.value)]) {
		spanNext.className = "navigation"
	} else {
		spanNext.className += " disable";
	}
	if (inputBook.value != '' && selectChapter.value > 1) {
		spanPrev.className = "navigation"
	} else {
		spanPrev.className += " disable";
	}
}

function goToPrevChapter() {
	var inputBook = document.getElementById('inputBook');
	var selectChapter = document.getElementById('selectChapter');
	if (inputBook.value != '' && selectChapter.value > 1){
		selectChapter.value = (parseInt(selectChapter.value) - 1).toString();
		localStorage.setItem("selectedChapterKey", selectChapter.value);
		document.getElementById('form').submit();
	}
}

function goToNextChapter() {
	var inputBook = document.getElementById('inputBook');
	var selectChapter = document.getElementById('selectChapter');
	if (inputBook.value != '' && selectChapter.value < chapters[books.indexOf(inputBook.value)]) {
		selectChapter.value = (parseInt(selectChapter.value) + 1).toString();
		localStorage.setItem("selectedChapterKey", selectChapter.value);
		document.getElementById('form').submit();		
	}
}

function populateChapters(bookValue) {
	var selectChapter = document.getElementById('selectChapter');
	selectChapter.disabled = false;

	selectChapter.options.length = 0;
	selectChapter.options.add(new Option("Kapitel"));
	var chapterCount = chapters[books.indexOf(bookValue)];
	for (var c = 1; c <= chapterCount; c++) {
		selectChapter.options.add(new Option(c));
	}
	initNavigation();
}

function autocomplete(input, possibleValues) {
	/*the autocomplete function takes two arguments,
	the text field element and an array of possible autocompleted values:*/
	var currentFocus;
	/*execute a function when someone writes in the text field:*/
	input.addEventListener("input", function(e) {
		var a, b, i, val = this.value;
		/*close any already open lists of autocompleted values*/
		closeAllLists();
		if (!val) { return false; }
		currentFocus = -1;
		/*create a DIV element that will contain the items (values):*/
		a = document.createElement("DIV");
		a.setAttribute("id", this.id + "autocomplete-list");
		a.setAttribute("class", "autocomplete-items");
		/*append the DIV element as a child of the autocomplete container:*/
		this.parentNode.appendChild(a);
		/*for each item in the array...*/
		for (i = 0; i < possibleValues.length; i++) {
			/*check if the item starts with the same letters as the text field value:*/
			if (possibleValues[i].toUpperCase().includes(val.toUpperCase()) /*arr[i].substr(0, val.length).toUpperCase() == val.toUpperCase()*/) {
				/*create a DIV element for each matching element:*/
				b = document.createElement("DIV");
				/*make the matching letters bold:*/
				var beginIndex = possibleValues[i].toUpperCase().indexOf(val.toUpperCase());
				if (beginIndex == 0) {
					b.innerHTML = "<strong>" + possibleValues[i].substring(0, val.length) + "</strong>";
					b.innerHTML += possibleValues[i].substring(val.length);
				} else {
					b.innerHTML = possibleValues[i].substring(0, beginIndex);
					b.innerHTML += "<strong>" + possibleValues[i].substring(beginIndex, beginIndex + val.length) + "</strong>";
					b.innerHTML += possibleValues[i].substring(beginIndex + val.length);
				}
				/*insert a input field that will hold the current array item's value:*/
				b.innerHTML += "<input type='hidden' value='" + possibleValues[i] + "'>";
				/*execute a function when someone clicks on the item value (DIV element):*/
				b.addEventListener("click", function(e) {
					/*insert the value for the autocomplete text field:*/
					input.value = this.getElementsByTagName("input")[0].value;
					populateChapters(input.value);
					/*close the list of autocompleted values,
					(or any other open lists of autocompleted values:*/
					closeAllLists();
				});
				a.appendChild(b);
			}
		}
	});
	/*execute a function presses a key on the keyboard:*/
	input.addEventListener("keydown", function(e) {
		var x = document.getElementById(this.id + "autocomplete-list");
		if (x) x = x.getElementsByTagName("div");
		if (e.keyCode == 40) {
			/*If the arrow DOWN key is pressed,
			increase the currentFocus variable:*/
			currentFocus++;
			/*and and make the current item more visible:*/
			addActive(x);
		} else if (e.keyCode == 38) { //up
			/*If the arrow UP key is pressed,
			decrease the currentFocus variable:*/
			currentFocus--;
			/*and and make the current item more visible:*/
			addActive(x);
		} else if (e.keyCode == 13) {
			/*If the ENTER key is pressed, prevent the form from being submitted,*/
			e.preventDefault();
			if (currentFocus > -1) {
				/*and simulate a click on the "active" item:*/
				if (x) x[currentFocus].click();
			}
		}
	});

	function addActive(x) {
		/*a function to classify an item as "active":*/
		if (!x) return false;
		/*start by removing the "active" class on all items:*/
		removeActive(x);
		if (currentFocus >= x.length) currentFocus = 0;
		if (currentFocus < 0) currentFocus = (x.length - 1);
		/*add class "autocomplete-active":*/
		x[currentFocus].classList.add("autocomplete-active");
	}
	function removeActive(x) {
		/*a function to remove the "active" class from all autocomplete items:*/
		for (var i = 0; i < x.length; i++) {
			x[i].classList.remove("autocomplete-active");
		}
	}
	function closeAllLists(elmnt) {
		/*close all autocomplete lists in the document,
		except the one passed as an argument:*/
		var x = document.getElementsByClassName("autocomplete-items");
		for (var i = 0; i < x.length; i++) {
			if (elmnt != x[i] && elmnt != input) {
				x[i].parentNode.removeChild(x[i]);
			}
		}
	}
	/*execute a function when someone clicks in the document:*/
	document.addEventListener("click", function(e) {
		closeAllLists(e.target);
	});
}

var books = ["1. Mose", "2. Mose", "3. Mose", "4. Mose", "5. Mose", "Josua", "Richter", "Ruth", "1. Samuel", "2. Samuel", "1. Könige", "2. Könige", "1. Chronik", "2. Chronik", "Esra", "Nehemia", "Ester", "Hiob", "Psalm", "Sprüche", "Prediger", "Hohelied", "Jesaja", "Jeremia", "Klagelieder", "Hesekiel", "Daniel", "Hosea", "Joel", "Amos", "Obadja", "Jona", "Micha", "Nahum", "Habakuk", "Zefanja", "Haggai", "Sacharja", "Maleachi", "Matthäus", "Markus", "Lukas", "Johannes", "Apostelgeschichte", "Römer", "1. Korinther", "2. Korinther", "Galater", "Epheser", "Philipper", "Kolosser", "1. Thessalonicher", "2. Thessalonicher", "1. Timotheus", "2. Timotheus", "Titus", "Philemon", "Hebräer", "Jakobus", "1. Petrus", "2. Petrus", "1. Johannes", "2. Johannes", "3. Johannes", "Judas", "Offenbarung"];
var chapters = ["50", "40", "27", "36", "34", "24", "21", "4", "31", "24", "22", "25", "29", "36", "10", "13", "10", "42", "150", "31", "12", "8", "66", "52", "5", "48", "12", "14", "4", "9", "1", "4", "7", "3", "3", "3", "2", "14", "3", "28", "16", "24", "21", "28", "16", "16", "13", "6", "6", "4", "4", "5", "3", "6", "4", "3", "1", "13", "5", "5", "3", "5", "1", "1", "1", "22"];

// Initiate the autocomplete function on the "input" elements and pass along the books array as possible autocomplete values
autocomplete(document.getElementById("inputBook"), books);

function doSubmit() {
	var selectChapter = document.getElementById('selectChapter');
	if (selectChapter.value != 'Kapitel') {
		localStorage.setItem("selectedChapterKey", selectChapter.value);
		document.getElementById('form').submit();
	}
}