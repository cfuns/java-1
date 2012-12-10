function tooltipOver(event, msg) {
	var e = document.getElementById("tooltip");
	if (e != null) {
		e.innerHTML = msg;
		e.style.display = "block";
		e.style.position = "absolute";
		e.style.left = event.pageX + 0 + "px";
		e.style.top = event.pageY + 15 + "px";
	}
}

function tooltipMove(event) {
	var e = document.getElementById("tooltip");
	if (e != null) {
		e.style.left = event.pageX + 0 + "px";
		e.style.top = event.pageY + 15 + "px";
	}
}

function tooltipOut(event) {
	var e = document.getElementById("tooltip");
	if (e != null) {
		e.style.display = "none";
		e.innerHTML = "";
	}
}