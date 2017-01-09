/**
 * ichart 封装
 */
IChartPack = function(){
	/**
	 * 构造函数
	 */
	function IChartPack(chart){
		this.chart = chart;
	}
	/**
	 * 加载数据
	 */
	function load(url,data){
		var _this = this;
		$.ajax({
 		  type: "POST",
 		  url: url,
 		  data : data,
 		  dataType: "text",
 		  success : function(d,status){
 			  var result = $.parseJSON(d);
 			  if(result.success){
 				 _this.chart.load(result.data);
 				 _this.chart.show();
 			  } else {
 				  alert('加载出错：' + result.msg);
 			  }
 		  }
 		});
	}
	
};