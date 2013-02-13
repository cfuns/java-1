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
    tooltip : { 
      width : 'auto', 
      height: 'auto',
    },
    legend: { 
      width : 'auto', 
      height: 'auto',
    },
    startAnimation: {
      active: true,
      type: "grow",
      easing: "bounce"
    }
  }
});

});
