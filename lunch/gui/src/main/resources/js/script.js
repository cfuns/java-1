function toggle() {
	var inputs = document.getElementsByTagName("input");
	for (var i = 0; i < inputs.length; ++i) {
		var input = inputs[i];
		if (input && input.type == 'checkbox') {
			input.checked = !input.checked;
		}
	}
}