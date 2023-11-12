function init() {
	var selectSearch = document.getElementById("selectSearch");
	var submitSearch = document.getElementById("submitSearch");
	populateSelectSearch(selectSearch);		
	if (selectSearch.value != 'Bücher') {
		selectSearch.disabled = false;
	}
	if (inputSearch.value != '' && selectSearch.value != 'Bücher') {
		submitSearch.disabled = false;
	}
}

function populateSelectSearch(selectSearch) {
	addOption(selectSearch, "Bücher");
	addOption(selectSearch, "Alle");
	addOption(selectSearch, "AT");
	addOption(selectSearch, "NT");
	for (var i = 0; i < books.length; i++) {
		addOption(selectSearch, books[i]);
	}
	if (selectedSearchValue != null) {
		selectSearch.value = selectedSearchValue;
	}
}

function addOption(selectSearch, name) {
	selectSearch.add(new Option(name, name));
}

var inputSearch = document.getElementById("inputSearch");

inputSearch.addEventListener("input", function(event) {
	var selectSearch = document.getElementById("selectSearch");
	var submitSearch = document.getElementById("submitSearch");
	if (inputSearch.value != '') {
		selectSearch.disabled = false;
		if (selectSearch.value != 'Bücher' && submitSearch.disabled) {
			submitSearch.disabled = false;
		}
	} else {
		selectSearch.disabled = true;
		if (!submitSearch.disabled) {
			submitSearch.disabled = true;
		}
	}
});

function setSubmitSearch() {
	var selectSearch = document.getElementById("selectSearch");
	var submitSearch = document.getElementById("submitSearch");
	if (inputSearch.value != '' && selectSearch.value != 'Bücher') {
		submitSearch.disabled = false;
	} else {
		submitSearch.disabled = true;
	}
}