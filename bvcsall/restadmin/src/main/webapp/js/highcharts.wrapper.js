/**
 * 
 */
Highcharts.setOptions({
    chart: {
//        backgroundColor: {
//            linearGradient: [0, 0, 500, 500],
//            stops: [
//                [0, 'rgb(255, 255, 255)'],
//                [1, 'rgb(240, 240, 255)']
//                ]
//        },
//        borderWidth: 2,
//        plotBackgroundColor: 'rgba(255, 255, 255, .9)',
        plotShadow: true,
        plotBorderWidth: 1
    },
    lang: {
		months: ['一月', '二月', '三月', '四月', '五月', '六月',  '七月', '八月', '九月', '十月', '十一月', '十二月'],
		weekdays: ['Dimanche', 'Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi'],
		printChart : '打印',
		downloadJPEG : '下载JPEG图片',
		downloadPDF : '下载PDF',
		downloadPNG : '下载PNG图片',
		downloadSVG : '下载SVG图片',
		drillUpText: '<< 返回 {series.name}'
	}
});
Highcharts.Wrapper = function(c) {
	var defaultConfig = {
		
	};
	var _this = this;
	_this.config = c;//TODO　iChart.applyIf(c, defaultConfig);
	/**
	 * 初始化
	 */
	this.initialize = function() {
		_this.chart = new Highcharts.Chart(_this.config);
	};
	_this.initialize();
	/**
	 * 加载数据
	 */
	this.load = function(url, data) {
		var _this = this;
		// alert('load');
		_this.requestUrl = url;
		$.messager.progress({
    		title:'请稍后',
            msg:'正在加载数据...',
            text : ''
    	});
		$.ajax({
			type : "POST",
			url : url,
			data : data,
			dataType : "json",
			success : function(d, status) {
				$.messager.progress('close');
				if (_this.config.loadSuccess) {
					_this.config.loadSuccess(_this.chart, d);
				}
			}
		});
	};
	/**
	 * 刷新数据
	 */
	this.refresh = function(url, data) {
		var _this = this;
		var _url = url;
		if (url == null)
			_url = _this.requestUrl;
		$.ajax({
			type : "POST",
			url : _url,
			data : data,
			dataType : "json",
			success : function(d, status) {
				if (_this.config.loadSuccess) {
					_this.config.loadSuccess(_this.chart, d);
				}
			}
		});
	};
	this.getChart = function(){
		return _this.chart;
	};
	/**
	 * 根据chart宽度自动计算x周显示的step
	 * xNum : x轴的分组个数
	 * perWidth : 每个x轴分组宽度
	 */
	this.dynamicxAxisStep = function(xNum, perWidth){
		//计算x轴能显示的日期个数
		//alert('redraw');
		//计算显示x坐标的间隔
		var chartW = $(_this.chart.container).width();
		var step = xNum - 1;
		var m = xNum / (chartW / perWidth);//能显示的x轴个数
		if(m < 1) step = 1;
		else if(m < xNum) step = m;
		//alert('step='+step);
		_this.chart.xAxis[0].options.labels.step = step;
		_this.chart.xAxis[0].isDirty = true;
		//alert('step='+chart.getChart().xAxis[0].options.labels.step);
		//chart.getChart().redraw();
		//alert('redraw finish');
	};
	/**
	 * 验证数据
	 */
	this.validData = function(d) {
		var data = d.data;
		// 判断是否有数据
		if (iChart.isEmpty(data)) {
			// alert('无数据');
			_this.chart.load([]);
			return false;
		} else {
			if (data.length == 1) {
				if (iChart.isEmpty(data[0].value)) {
					// alert('无数据');
					_this.chart.load([]);
					return false;
				}
			}
		}
		return true;
	};
};