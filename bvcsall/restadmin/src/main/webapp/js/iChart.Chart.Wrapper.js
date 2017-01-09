/**
 * 对 iChartjs 的chart组件的统一封装 参数： c：json对象，配置描述。 {type:'linebasic2D',……}
 * type为必选属性，值为iChartjs 支持的chart组件对象（转小写）
 */
iChart.Chart.Wrapper = function(c) {
	//alert('width:'+document.body.scrollWidth);
	//alert('height:'+document.body.scrollHeight);
	//alert(c.render);
	var parentDiv = $('#'+c.render).parent();
	//alert(parentDiv);
	//设置默认宽高
	var default_height = parentDiv.height() * 0.8;
	var default_width = parentDiv.width() * 0.8;
	
	var height = 550;
	var width = 850;
	var defaultConfig = {
		width : default_width,
		height : default_height,
		align : 'center',
		shadow : true,
		shadow_blur : 10,
		background_color : '#EFEFEF',
		animation : true
	};
	var _this = this;
	_this.config = iChart.applyIf(c, defaultConfig);
	/**
	 * 初始化
	 */
	this.initialize = function() {
		_this.chart = iChart.create(_this.config);
	};
	_this.initialize();
	/**
	 * 加载数据
	 */
	this.load = function(url, data) {
		var _this = this;
		// alert('load');
		_this.requestUrl = url;
		$.ajax({
			type : "POST",
			url : url,
			data : data,
			dataType : "json",
			success : function(d, status) {
				if (!_this.validData(d))
					return false;
				
				_this.chart.fullData = d;
				// 调用beforeLoad事件
				if (_this.config.beforeLoad) {
					_this.config.beforeLoad(_this.chart, d);
				}
				var labels = d.labels;
				// alert(JSON.stringify(d));
				if (labels) {// 如果有labels 属性，则添加labels属性
					_this.chart.push('labels', labels);
				}
				_this.chart.load(d.data);
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
				// alert(_this.chart.destroy);
				/*
				 * _this.chart.destroy(); var labels = d.labels;
				 * //alert(labels.length); if(labels.length <= 0){
				 * $('#'+_this.chart.render).html('<span>无数据</span>'); return; }
				 * if(labels){//如果有labels 属性，则添加labels属性 //设置x轴的label
				 * _this.chart.get('coordinate.scale')[1]['labels'] = labels;
				 * //重置x轴坐标 _this.chart.push('point_space',0);//重置x轴坐标 }
				 * 
				 * _this.chart.load(d.data);
				 */
				if (!_this.validData(d))
					return false;

				_this.chart.destroy(_this.chart);
				_this.chart = null;
				_this.initialize();
				
				
				_this.chart.fullData = d;
				
				// 调用beforeLoad事件
				if (_this.config.beforeLoad) {
					_this.config.beforeLoad(_this.chart, d);
				}
				var labels = d.labels;
				if (labels) {// 如果有labels 属性，则添加labels属性
					_this.chart.push('labels', labels);
					_this.chart.push('point_space', 0);// 重置x轴坐标
				}
				_this.chart.load(d.data);
			}
		});
	};
	this.getChart = function(){
		return _this.chart;
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
	/*
	 * this.load = function(d){ var labels = d.labels;
	 * //alert(JSON.stringify(d)); if(labels)//如果有labels 属性，则添加labels属性
	 * _this.chart.push('labels',labels); _this.chart.push('data',d.data);
	 * _this.chart.setUp(); _this.chart.draw(); }; this.refresh = function(d){
	 * var labels = d.labels; //alert(labels.length); if(labels.length <= 0){
	 * $('#'+_this.chart.render).html('<span>无数据</span>'); return; }
	 * if(labels){//如果有labels 属性，则添加labels属性 //设置x轴的label
	 * _this.chart.get('coordinate.scale')[1]['labels'] = labels; //重置x轴坐标
	 * _this.chart.push('point_space',0);//重置x轴坐标 } // _this.chart.load(d.data);
	 * _this.chart.push('data',d.data); _this.chart.setUp(); _this.chart.draw(); };
	 */
};