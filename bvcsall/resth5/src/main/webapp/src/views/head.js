
define(['../libs/jquery-2.1.4','../common/info','../common/aler','../service/service','../common/browse'], function(jquery,info,aler,sever,browse) {

	var uid = info.getCookie('uid');
	var interfaceurl = $('#interfaceurl').val();
	var interUrl = info.getRequestPrefix();
	var timer= 1000*60*60;
	var head = {
			init:function(){
				head.addBgBox();
				info.scrollTop();//返回顶部
				$('#toPerCener').on('click',function(){
					head.goPage('userId');
				});
				$('#toAttention').on('click',function(){
					head.goPage('attentionVideoList');
				});
				$('.logo').click(function(){
					_czc.push(["_trackEvent","我拍logo","点击logo"]);
				});
				if(info.getCookie('closeBar')){
					$('.downBar').hide();
					$('.activeIndex').css('padding-bottom','0px');
					$('.videoList1,.videoList').css('padding-bottom','0px');
					$('.funPerson,.personalCenter,.activeUseList').css('margin-bottom','0px');
					$('#btnMore').css('margin-bottom','5px');
					head.setTimer();
				}else{
					$('.downBar').show();
				};
				$('.downClose').on('click',function(){
					info.setCookie('closeBar','closeTime',1);
					$('.activeIndex').css('padding-bottom','0px');
					$('.videoList1,.videoList').css('padding-bottom','0px');
					$('.funPerson,.personalCenter,.activeUseList').css('margin-bottom','0px');
					$('#btnMore').css('margin-bottom','5px');
					$(this).parent().hide();
					head.setTimer();
				});
				if(info.getCookie('channel')=='openg'){
					$('.downBar,.active-go-app,.downBar2,.join-tag,.integral-ad').hide();
					$('.activeIndex').css('padding-bottom','0px');
					$('.videoList1,.videoList').css('padding-bottom','0px');
					$('.funPerson,.personalCenter,.activeUseList').css('margin-bottom','0px');
					$('#btnMore').css('margin-bottom','5px');
				}else{
					$('.downBar,.active-go-app,.downBar2,.join-tag,.integral-ad').show();
					$('.active-go-app').css('display','block')
				}
				if(info.getCookie('channel')=='openg'){
						$('.openApp a').text('观看TA的更多视频！');
						$('.openApp a').removeClass('open-app-btn').addClass('openg');
				}
			},
			setTimer: function(){
				setTimeout(function(){
					info.clearCookie('closeBar');
					$('.activeIndex').css('padding-bottom','38px');
					$('.videoList1,.videoList').css('padding-bottom','38px');
					$('.funPerson,.personalCenter,.activeUseList').css('margin-bottom','40px');
					$('#btnMore').css('margin-bottom','50px');
				},timer);
			},
			goPage: function(type,typeId){
				var url= ''
				var params= {};
				if(type == 'video'){
					url= interUrl+"/page/video/videoDetail?videoId="+typeId
					params= {
						uid: info.getCookie('uid')
					}
				}else if(type == 'userId'){
					url= interUrl+'/page/user/userCenter?access_token='+info.getCookie('access_token');
					params= {
						uid: info.getCookie('uid')
					}
				}else if(type == 'attention'){
					url= interUrl+'/page/attention/getAttention?userId='+typeId+'&access_token='+info.getCookie('access_token');
					params= {
							uid: info.getCookie('uid')
						}
				}else if(type == 'attentionVideoList'){
					url= interUrl+'/page/attention/attentionVideoList?'+'access_token='+info.getCookie('access_token');
					params= {
							uid: info.getCookie('uid')
						}
				}
				info.postform(url,params)
			},
			addBgBox:function(){
				var blockBox = '<div class="bgBox2">请竖屏浏览</div>';
				$('body').append(blockBox);
			}

	}
	head.init();

window.onorientationchange=function(){
    if(window.orientation==0 || window.orientation==180){
        $('.bgBox2').hide().siblings().show();
        $('.skyBox-anz,.skyBox,#cnzz_stat_icon_1256005299').hide();
    }else
    {
        //aler.alertMsg('请竖屏展示')
        $('.bgBox2').show().siblings().hide();
        

    }
}; 
window.orientationchange=function(){
    if(window.orientation==0 || window.orientation==180){
    	$('.bgBox2').hide().siblings().show();
    	$('.skyBox-anz,.skyBox,#cnzz_stat_icon_1256005299').hide();
    }else
    {
        //aler.alertMsg('请竖屏展示')
    	$('.bgBox2').show().siblings().hide();

    }
}; 
})