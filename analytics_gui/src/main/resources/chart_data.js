$(function() {
  $("#chart").chart({
  template: "line_basic_2",
  tooltips: {
{tooltips}
  },
  values: {
{values}
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
