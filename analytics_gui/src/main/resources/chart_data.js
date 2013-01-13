$(function() {
  $("#chart").chart({
  template: "line_basic_2",
  tooltips: {
    serie1: [{tooltips}]
  },
  values: {
    serie1: [{values}],
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
