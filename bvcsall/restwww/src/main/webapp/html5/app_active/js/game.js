function init(){
	if (window.DeviceMotionEvent) {
	// 移动浏览器支持运动传感事件
	window.addEventListener('devicemotion', deviceMotionHandler, false);
	}

}
var SHAKE_THRESHOLD = 3000;
// 定义一个变量保存上次更新的时间
var last_update = 0;
// 紧接着定义x、y、z记录三个轴的数据以及上一次出发的时间
var x;
var y;
var z;
var last_x;
var last_y;
var last_z;
var count = 0;



var serviceUrl= $("#interfaceurl").val();
var postIndex= serviceUrl+"/restwww/lotteryDraw/index"; //活动首页接口
var postInvita= serviceUrl+"/restwww/lotteryDraw/inviteFriend"; //邀请好友接口
var postDraw= serviceUrl+"/restwww/lotteryDraw/doRaffle";  //抽奖动作
var postSaveUserInfo= serviceUrl+"/restwww/lotteryDraw/saveUserInfo";  //保存用户信息

var uid= jQuery.getCookie('uid');
var access_token= jQuery.getCookie('access_token');
var params= {
    'userId': uid,
    'uid': uid
}

jQuery.postDate(postIndex,"?userId="+uid+"&uid="+uid,function(data){
    console.log(44343)
})
function deviceMotionHandler(eventData) {
	// 获取含重力的加速度
	var acceleration = eventData.accelerationIncludingGravity;

	// 获取当前时间
	var curTime = new Date().getTime();
	var diffTime = curTime -last_update;
	// 固定时间段
	if (diffTime > 100) {
    	last_update = curTime;

    	x = acceleration.x;
    	y = acceleration.y;
    	z = acceleration.z;

    	var speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 30000;
        document.title = count;
        if (speed > SHAKE_THRESHOLD) {
        	// TODO:在此处可以实现摇一摇之后所要进行的数据逻辑操作
        	count++;
            var px = count*19;
            var text = count*8;
            $(".tree-head").addClass('cur');
            if(px>=100)
            {
                document.title='达到了';
                jQuery.postDate(postDraw,params,function(data){
                    //document.title='达到了';
                    if(data.result.giftType== '0') {
                        $('.firstwin').show();
                        alert(43)
                        //空的
                    }else if(data.result.giftType== '1'){
                        $('.winin-integral').show();
                        //积分
                    }else if(data.result.giftType== '2'){
                        $('.winin-apple').show();
                        //水果
                    }
                });
            }
        }else{
            $(".tree-head").removeClass('cur');
       }

        last_x = x;
        last_y = y;
        last_z = z;
	}
}


//deviceMotionHandler: function(eventData){
//    // 获取含重力的加速度
//    var acceleration = eventData.accelerationIncludingGravity;
//    var num= 5;
//    var timer= null;
//
//    // 获取当前时间
//    var curTime = new Date().getTime();
//    var diffTime = curTime -last_update;
//	　　		// 固定时间段
//    if (diffTime > 100) {
//        last_update = curTime;
//        x = acceleration.x;
//        y = acceleration.y;
//        z = acceleration.z;
//        var speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 30000;
//        if (speed > SHAKE_THRESHOLD) {
//            document.title= speed+':'+SHAKE_THRESHOLD;
//            // TODO:在此处可以实现摇一摇之后所要进行的数据逻辑操作
//            count++;
//            var px = count*19;
//            var text = count*8;
//            $(".tree-head").addClass('cur');
//            //document.title= count;
//            if(px >= 380){
//
//
//            }
//        }else{
//            $(".tree-head").removeClass('cur');
//        }
//
//	　　　　last_x = x;
//	　　　　last_y = y;
//	　　　　last_z = z;
//	　　}
//},