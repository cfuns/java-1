$(function() {
  $("#chart").chart({
  template: "line_basic_2",
  tooltips: {
    serie1: ["a", "b", "c", "d"],
    serie2: ["a", "b", "c", "d"]
  },
  values: {
    serie1: [23, 74, 94, 76],
    serie2: [60, 20, 92, 62]
  },
  defaultSeries: {
    fill: true,
    stacked: false,
    highlight: {
      scale: 2
    },
    startAnimation: {
      active: true,
      type: "grow",
      easing: "bounce"
    }
  }
});

});
