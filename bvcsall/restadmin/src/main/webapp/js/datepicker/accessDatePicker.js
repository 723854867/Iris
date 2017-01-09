//选择好日期之后
	function onDateSelected(startDate,endDate) {
		var startDateTemp = document.getElementById(startDate).value;
		var endDateTemp = document.getElementById(endDate).value;
		
		if(startDateTemp!="" && endDateTemp!=""){
			//request();
		}
	}
	
	//当开始日期完成选择
	function startDatePicked(startDate,endDate){
		var startDateTemp = $dp.$(startDate).value;
		var endDateTemp = $dp.$(endDate).value;
		
		if(startDateTemp!="" &&endDateTemp!="" 
				&& getDate(endDateTemp).getTime()< getDate(startDateTemp).getTime()){
			$dp.$(endDate).value = $dp.$(startDate).value;
		}
		
		$dp.$(e).focus();
	}
	//通过日期字符串获取Date
	function getDate(dateString){
		var parts = dateString.split(" ");
		var datePart = parts[0];
		var dates = datePart.split("-");
		var date = new Date();
		date.setFullYear(dates[0],dates[1],dates[2]);
		
		return date;
	}