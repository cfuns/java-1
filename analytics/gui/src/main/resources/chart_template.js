$.elycharts.templates['line_basic_2'] = {
	type: "line",
	margins: [10, 10, 20, 50],
	defaultSeries: {
		plotProps: {
			"stroke-width": 4
		},
		dot: true,
		dotProps: {
			stroke: "white",
			"stroke-width": 2
		}
	},
	series: {
{colors}
},
defaultAxis: {
	labels: true
},
features: {
	grid: {
		draw: [true, false],
			props: {
			"stroke-dasharray": "-"
		}
	},
	legend: {
		horizontal: false,
			width: 250,
			height: 100,
			x: 50,
			y: 10,
			dotType: "circle",
			dotProps: {
			stroke: "white",
				"stroke-width": 2
		},
		borderProps: {
			opacity: 0.3,
				fill: "#c0c0c0",
				"stroke-width": 0
		}
	}
}
};
