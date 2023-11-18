function handleTopNav() {
	var topnav = document.getElementById("topnav");
	if (topnav.className === "topnav") {
		topnav.className += " responsive";
		document.getElementById("overlay").style.display = "block";
	} else {
		topnav.className = "topnav";
		document.getElementById("overlay").style.display = "none";
	}
}