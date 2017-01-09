/**
 * easyui 的默认配置
 */
/*$.extend($.fn.datebox.methods,{
	 // 设置日期控件的日期范围
	setScope : function(start,end){
		
	}
});*/
//(function($){
	$.extend($.fn.datebox.methods,{
		 // 设置日期控件的日期范围
		setScope : function(target,start,end){
			$(target).datebox('calendar').calendar.validator = function(date){
				return dateValid(date,start,end);
			}
			alert($(target).datebox('calendar').calendar.validator);
			
		}
	});
//})(jQuery);
/**
 * 日期控件，判断日期有效性
 */
function dateValid(date,start,end){
	if(start != null){
		if(date < start)
			return false;
	}
	if(end != null){
		if(date > end) 
			return false;
	}
	return true;
}