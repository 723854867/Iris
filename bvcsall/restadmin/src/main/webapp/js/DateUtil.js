/**
 * 
 */
/**
 * date工具类
 * version：1.0
 */
(function($){
	
	var weekDay = ["星期一","星期二","星期三","星期四","星期五","星期六","星期天"];

	$.extend({
		
		//本周第一天
		showWeekFirstDay:function(Nowdate) {
			var WeekFirstDay = new Date(Nowdate - (Nowdate.getDay() - 1) * 86400000);
			return WeekFirstDay;
		},
		
		//本周最后一天
		showWeekLastDay:function(Nowdate) {
			var WeekFirstDay = new Date(Nowdate - (Nowdate.getDay() - 1) * 86400000);
			var WeekLastDay = new Date((WeekFirstDay / 1000 + 6 * 86400) * 1000);
			return WeekLastDay;
		},
		
		//上周7天
		//返回一个数组，数组里面是7天的Date对象
		showLastWeekDays:function(now) {
		    var currentWeek = now.getDay();
		    
		    if (currentWeek == 0) {
		    	currentWeek = 7;
		    }
		    
		    var monday  = now.getTime() - (currentWeek+6)*24*60*60*1000; //星期一
		    var tuesday  = now.getTime() - (currentWeek+5)*24*60*60*1000; //星期二
		    var wednesday = now.getTime() - (currentWeek+4)*24*60*60*1000; //星期三
		    var thursday = now.getTime() - (currentWeek+3)*24*60*60*1000; //星期四
		    var friday  = now.getTime() - (currentWeek+2)*24*60*60*1000; //星期五
		    var saturday = now.getTime() - (currentWeek+1)*24*60*60*1000; //星期六
		    var sunday  = now.getTime() - (currentWeek)*24*60*60*1000;   //星期日
		    
		    var week = new Array(new Date(monday), new Date(tuesday), 
		    					 new Date(wednesday), new Date(thursday),
		    					 new Date(friday), new Date(saturday), 
		    					 new Date(sunday));
			return week; 
		},
		
		//本周7天
		//返回一个数组，数组里面是7天的Date对象
		showThisWeekDays:function(now) {
		    var currentWeek = now.getDay();
		    
		    if (currentWeek == 0) {
		    	currentWeek = 7;
		    }
		    
		    var monday  = now.getTime() - (currentWeek-1)*24*60*60*1000; //星期一
		    var tuesday  = now.getTime() - (currentWeek-2)*24*60*60*1000; //星期二
		    var wednesday = now.getTime() - (currentWeek-3)*24*60*60*1000; //星期三
		    var thursday = now.getTime() - (currentWeek-4)*24*60*60*1000; //星期四
		    var friday  = now.getTime() - (currentWeek-5)*24*60*60*1000; //星期五
		    var saturday = now.getTime() - (currentWeek-6)*24*60*60*1000; //星期六
		    var sunday  = now.getTime() - (currentWeek-7)*24*60*60*1000;   //星期日
		    
		    var week = new Array(new Date(monday), new Date(tuesday), 
		    					 new Date(wednesday), new Date(thursday),
		    					 new Date(friday), new Date(saturday), 
		    					 new Date(sunday));
			return week; 
		},
		
		//下周7天
		//返回一个数组，数组里面是7天的Date对象
		showNextWeekDays:function(now) {
			 var currentWeek = now.getDay();
			 
			 if ( currentWeek == 0 ) {
			 	currentWeek = 7;
			 }
			 
			 var monday = now.getTime() - (currentWeek-8)*24*60*60*1000;  //星期一
			 var tuesday = now.getTime() - (currentWeek-9)*24*60*60*1000;  //星期二
			 var wednesday = now.getTime() - (currentWeek-10)*24*60*60*1000; //星期三
			 var thursday = now.getTime() - (currentWeek-11)*24*60*60*1000; //星期四
			 var friday= now.getTime() - (currentWeek-12)*24*60*60*1000; //星期五
			 var saturday = now.getTime() - (currentWeek-13)*24*60*60*1000; //星期六
			 var sunday = now.getTime() - (currentWeek-14)*24*60*60*1000; //星期日
			
		    var week = new Array(new Date(monday), new Date(tuesday), 
		    					 new Date(wednesday), new Date(thursday),
		    					 new Date(friday), new Date(saturday), 
		    					 new Date(sunday));
			return week; 
		},
		
		//本月第一天
		showMonthFirstDay : function(Nowdate) {
			var MonthFirstDay = new Date(Nowdate.getFullYear(), Nowdate.getMonth(), 1);
			return MonthFirstDay;
		},
		
		//本月最后一天
		showMonthLastDay : function(Nowdate) {
			var MonthNextFirstDay = new Date(Nowdate.getFullYear(), Nowdate.getMonth() + 1, 1);
			var MonthLastDay = new Date(MonthNextFirstDay - 86400000);
			return MonthLastDay;
		},
		
		//上月
		//返回一个数组，数组里面是上月第1天和最后1天的Date对象
		showLastMonthDays:function(Nowdate) {
			var MonthFirstDay = new Date(Nowdate.getYear(),Nowdate.getMonth()-1,1);
		   	var MonthNextFirstDay = new Date(Nowdate.getYear(),Nowdate.getMonth(),1);
		  	var MonthLastDay = new Date(MonthNextFirstDay-86400000);
		  	
		  	return new Array(MonthFirstDay, MonthLastDay);
		},
		
		//下月
		//返回一个数组，数组里面是下月第1天和最后1天的Date对象
		showNextMonthDays:function(Nowdate) {
			var MonthFirstDay = new Date(Nowdate.getYear(), Nowdate.getMonth()+1, 1);
		   	var MonthNextFirstDay = new Date(MonthFirstDay.getYear(), MonthFirstDay.getMonth() + 1, 1);
		  	var MonthLastDay = new Date(MonthNextFirstDay - 86400000);
		  	
		  	return new Array(MonthFirstDay, MonthLastDay);
		},
//		get_lastDate();//返回当前日期
//	    get_lastDate("","y");//返回当前去年的日期
//	    get_lastDate("","y",-1);//返回当前去年前一天的日期
//	    get_lastDate("","y",1);//返回当前去年后一天的日期
//	    get_lastDate("","m");//返回当前上个月的日期
//	    get_lastDate("","m",-1);//返回当前上个月前一天日期
//	    get_lastDate("","m",1);//返回当前上个月后一天日期
//	    get_lastDate("","",1);//返回明天日期
//	    get_lastDate("","",-1);//返回昨天日期
//	    get_lastDate("2012-6-1","",-1);//返回2012-6-1前一天的日期
//	    get_lastDate("2012-6-30","",1);//返回2012-6-1后一天的日期
		get_lastDate : function(ymd,ym,n){
			ymd=ymd?new Date(ymd.replace(/-/g,"/")):new Date();
			ym&&(ym=="y"?ymd.setFullYear(ymd.getFullYear()-1):ymd.setMonth(ymd.getMonth()-1));
			n&&ymd.setDate(ymd.getDate()+n);
			return ymd.toLocaleDateString().match(/\d+/g).join('-');
		},
		
		//基于date 和 day（整数类型：允许正负整数）做天数的加减运算
		addMonthDay : function(date,day){
			if(!date){
				return;
			}
			var ms = date.getTime();
			ms += day * 86400000;
			return new Date(ms);
		},
		
		//基于date 和 month （整数类型：允许正负整数）做月份的加减运算
		addMonth : function(date,month){
			if(!date){
				return;
			}
			var temp = new Date(date.getTime());
			
			var currMonth = temp.getMonth();
			
			temp.setMonth(10);
			var currYear  = temp.getYear();
			if(month > 0){
				for(var i = 0 ; i < month ; i++){
					if(currMonth == 11){
						currMonth = -1;
					}
				
					currMonth += 1;
					if(currMonth > 10){
						currYear += 1;
					}
				}
				temp.setMonth(currMonth);
			}else{
				month = 0 - month;
				for(var i = 0 ; i < month ; i++){
					if(currMonth == 0){
						currMonth = 12;
					}
				
					currMonth -= 1;
					if(currMonth < 1){
						currYear -= 1;
					}
				}
				temp.setMonth(currMonth);
			}
			temp.setYear(currYear);
			return temp;
		},
		
		//简单格式化时间 yyyy-MM-dd
		dateFormat : function(date) {
			var month = date.getMonth() + 1;
			var monthStr = month;
			if(month < 10){
				monthStr = "0"+month;
			}
			return date.getFullYear() + "-" + monthStr + "-"
					+ date.getDate();
		},
		
		//简单格式化时间 yyyy-MM-dd HH:mm
		dateFormat2 : function(date) {
			var month = date.getMonth() + 1;
			var monthStr = month;
			if(month < 10){
				monthStr = "0"+month;
			}
			return date.getFullYear() + "-" + monthStr + "-"
					+ date.getDate() +" "+date.getHours() +":"+date.getMinutes();
		},
		
		//根据date获取星期的中文描述
		getWeekDay : function(date){
			var weekDayNum = date.getDay();
			return weekDay[weekDayNum];
		},
		//转化为Date对象 yyyy-MM-dd格式
		parseDate : function(str){
			return new Date(Date.parse(str.replace(/-/g,"/")));
		},
		//根据字符串yyyy-MM-dd，判断是否为周末
		isWeekend : function(str){
			if(!this.isDate(str)) return false;
			var date = new Date(Date.parse(str.replace(/-/g,"/")));
			var index = date.getDay();
			if(index ==0 || index == 6)
				return true;
			return false;
		},
		isDate : function(str){
			if(str == '') return false;
			var   reg   =   /^(\d{4})-(\d{2})-(\d{2})$/;  
		    //var   str   =   mystring;  
		    //var   arr   =   reg.exec(str);  
		    //if   (str=="")   return   true;  
		    if   (!reg.test(str) && RegExp.$2 <= 12 && RegExp.$3 <= 31){  
		        //alert("请保证"+sm+"中输入的日期格式为yyyy-mm-dd或正确的日期!");  
		        return false;  
		    }  
		    return true;
		}
	});
})(jQuery);
// 扩展Date的format方法
Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1,
		"d+" : this.getDate(),
		"h+" : this.getHours(),
		"m+" : this.getMinutes(),
		"s+" : this.getSeconds(),
		"q+" : Math.floor((this.getMonth() + 3) / 3),
		"S" : this.getMilliseconds()
	};
	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	}
	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
};
/**
 * 转换日期对象为日期字符串
 * 
 * @param date
 *            日期对象
 * @param isFull
 *            是否为完整的日期数据, 为true时, 格式如"2000-03-05 01:05:04" 为false时, 格式如
 *            "2000-03-05"
 * @return 符合要求的日期字符串
 */
function getSmpFormatDate(date, isFull) {
	var pattern = "";
	if (isFull == true || isFull == undefined) {
		pattern = "yyyy-MM-dd hh:mm:ss";
	} else {
		pattern = "yyyy-MM-dd";
	}
	return getFormatDate(date, pattern);
}
/**
 * 转换当前日期对象为日期字符串
 * 
 * @param date
 *            日期对象
 * @param isFull
 *            是否为完整的日期数据, 为true时, 格式如"2000-03-05 01:05:04" 为false时, 格式如
 *            "2000-03-05"
 * @return 符合要求的日期字符串
 */
function getSmpFormatNowDate(isFull) {
	return getSmpFormatDate(new Date(), isFull);
}
/**
 * 转换long值为日期字符串
 * 
 * @param l
 *            long值
 * @param isFull
 *            是否为完整的日期数据, 为true时, 格式如"2000-03-05 01:05:04" 为false时, 格式如
 *            "2000-03-05"
 * @return 符合要求的日期字符串
 */
function getSmpFormatDateByLong(l, isFull) {
	if(!l) return null;
	return getSmpFormatDate(new Date(l), isFull);
}
/**
 * 转换long值为日期字符串
 * 
 * @param l
 *            long值
 * @param pattern
 *            格式字符串,例如：yyyy-MM-dd hh:mm:ss
 * @return 符合要求的日期字符串
 */
function getFormatDateByLong(l, pattern) {
	return getFormatDate(new Date(l), pattern);
}
/**
 * 转换日期对象为日期字符串
 * 
 * @param l
 *            long值
 * @param pattern
 *            格式字符串,例如：yyyy-MM-dd hh:mm:ss
 * @return 符合要求的日期字符串
 */
function getFormatDate(date, pattern) {
	if (date == undefined) {
		date = new Date();
	}
	if (pattern == undefined) {
		pattern = "yyyy-MM-dd hh:mm:ss";
	}
	return date.format(pattern);
}
/************************************************************多媒体播放**************************************************************/
/**  
*视频播放  
*参数说明  
u - 媒体URL  
w - 媒体宽度width  
h - 媒体高度height  
*/  

//播放Mediaplayer格式的视频，包括.avi .mpg .mpeg .wmv .wma .asf .mid .mp3等  
function pv_m(u, w, h){  
	var pv='';  
	pv += '<object width="'+w+'" height="'+h+'" id="iask_v" classid="CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95" codebase="http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2inf.cab#Version=6,4,5,715" standby="Loading Microsoft Windows Media Player components..." type="application/x-oleobject">';  
	pv += '<param name="FileName" value="'+u+'">';  
	pv += '<param name="AutoStart" value="1">';  
	pv += '<param name="AutoSize" value="1">';  
	pv += '<param name="ShowControls" value="1">';  
	pv += '<param name="ShowPositionControls" value="0">';  
	pv += '<param name="ShowAudioControls" value="1">';  
	pv += '<param name="ShowTracker" value="1">';  
	pv += '<param name="ShowDisplay" value="0">';  
	pv += '<param name="ShowStatusBar" value="1">';  
	pv += '<param name="ShowGotoBar" value="0">';  
	pv += '<param name="ShowCaptioning" value="0">';  
	pv += '<param name="PlayCount" value="1">';  
	pv += '<param name="AnimationAtStart" value="0">';  
	pv += '<param name="TransparentAtStart" value="0">';  
	pv += '<param name="AllowScan" value="0">';  
	pv += '<param name="EnableContextMenu" value="0">';  
	pv += '<param name="ClickToPlay" value="0">';  
	pv += '<param name="InvokeURLs" value="1">';  
	pv += '<param name="DefaultFrame" value="">';  
	pv += '<embed src="'+u+'" width="'+w+'" height="'+h+'" type="application/x-mplayer2" pluginspage="http://www.microsoft.com/isapi/redir.dll?prd=windows&;sbp=mediaplayer&ar=media&sba=plugin&" name="MediaPlayer" showcontrols="1" showpositioncontrols="0" showaudiocontrols="1" showtracker="1" showdisplay="0" showstatusbar="1" autosize="0" showgotobar="0" showcaptioning="0" autostart="1" autorewind="0" animationatstart="0" transparentatstart="0" allowscan="1" enablecontextmenu="1" clicktoplay="0" invokeurls="1" defaultframe=""></embed>';  
	pv += '</object>';  
	return pv; 
}  

//播放Realplay格式的视频，包括.rm .ram .rmvb等  
function pv_r(u, w, h){  
	var pv='';  
	pv += '<object width="'+w+'" height="'+h+'" id="iask_v" classid="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA">';  
	pv += '<param name="SRC" value="'+u+'">';  
	pv += '<param name="AUTOSTART" value="1">';  
	pv += '<param name="CONTROLS" value="Imagewindow,StatusBar,ControlPanel">';  
	pv += '<param name="_ExtentX" value="18415">';  
	pv += '<param name="_ExtentY" value="9102">';  
	pv += '<param name="SHUFFLE" value="0">';  
	pv += '<param name="PREFETCH" value="0">';  
	pv += '<param name="NOLABELS" value="0">';  
	pv += '<param name="CONSOLE" value="Clip1">';  
	pv += '<param name="LOOP" value="0">';  
	pv += '<param name="NUMLOOP" value="0">';  
	pv += '<param name="CENTER" value="0">';  
	pv += '<param name="MAINTAINASPECT" value="0">';  
	pv += '<param name="BACKGROUNDCOLOR" value="#000000">';  
	pv += '<embed src="'+u+'" width="'+w+'" height="'+h+'" type="audio/x-pn-realaudio-plugin" console="Clip1" controls="Imagewindow,StatusBar,ControlPanel" autostart="true">';  
	pv += '</object>';  
	return pv; 
}  

//播放Quicktime格式的视频，包括.mov .amr .3gp等  
function pv_q(u, w, h,autoplay){  
	var pv = [];  
	pv.push('<object width="'+w+'" height="'+h+'" classid="clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B" codebase="http://www.apple.com/qtactivex/qtplugin.cab" >');  
	pv.push('<param name="src" value="'+u+'">');  
	pv.push('<param name="controller" value="true">');  
	pv.push('<param name="type" value="video/quicktime">');   //video/quicktime
	pv.push('<param name="autoplay" value="'+autoplay+'">');  
	pv.push('<param name="target" value="myself">');  
	pv.push('<param name="bgcolor" value="#cccccc">');  
	pv.push('<param name="pluginspage" value="http://www.apple.com/quicktime/download/index.html">');  
	pv.push('<embed src="'+u+'" width="'+w+'" height="'+h+'" controller="true" align="middle" autoplay="'+autoplay+'" bgcolor="#cccccc" target="myself" type="video/quicktime" pluginspage="http://www.apple.com/quicktime/download/index.html"></embed>');  
	pv.push('</object>');  
	return pv.join('');  
} 

