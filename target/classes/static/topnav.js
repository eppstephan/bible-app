function handleTopNav() {
	var topnav = document.getElementById("topnav");
	if (topnav.className === "topnav") {
		topnav.className += " responsive";
		document.body.style.backgroundColor = "rgba(0,0,0,0.5)";
	} else {
		topnav.className = "topnav";
		document.body.style.backgroundColor = "transparent";
		// document.body.style.removeProperty('background-color');
	}
}