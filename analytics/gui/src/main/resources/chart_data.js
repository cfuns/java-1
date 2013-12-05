$(function() {
		$("#chart").chart({
			template: "line_basic_2",
			tooltips: {
		{tooltips}
	},
	values: {
	{values}
},
legend : {
	{legend}
},
defaultSeries: {
	fill: true,
		stacked: false,
		highlight: {
		scale: 2
	},
	tooltip: {
		active: true,
			width: 250,
			height: 70,
	},
	startAnimation: {
		active: true,
			type: "grow",
			easing: "bounce"
	}
}
});

});
