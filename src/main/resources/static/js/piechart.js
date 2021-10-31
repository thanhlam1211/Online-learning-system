$.ajax({
    /* for pie chart */
    url: "piechartdata",

    success: function(result){
        /* pie chart starts here */
        var series = [];
        var data = [];

        for(var i = 0; i < result.length; i++){
            var object = {};
            object.name = result[i].catName.toUpperCase();
            object.y = result[i].catCount;
            data.push(object);
        }
        var seriesObject = {
            name: 'Course By Category',
            colorByPoint: true,
            data: data
        };
        series.push(seriesObject);
        drawPieChart(series);

        /* pie chart ends here */
    }
});
/* for pie chart */
function drawPieChart(series){
    Highcharts.chart('chartContainer', {
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie'
        },
        title: {
            text: 'Browser market shares in January, 2018'
        },
        tooltip: {
            formatter: function() {
                return '<strong>'+this.key+': </strong>'+ this.y;
            }
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.y}'
                }
            }
        },
        series: series
    });
}