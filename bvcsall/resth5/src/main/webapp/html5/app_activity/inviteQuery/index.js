$(function(){
	var interUrl = $('#interfaceurl').val();
	var isAdmin = $('#isAdmin').val();
	$('#query').on('click',function(){
		$('table').hide();
		$('table,#content p').text('');
		var inviteCode = $('#inviteCode').val();
		var page = 1;
		var size = 10000;
		var startTime = $('#startTime').val();
		var endTime = $('#endTime').val();
		if(inviteCode == ''){
			alert('请输入邀请码');return;
		}
		$.ajax({
			url: interUrl + '/resth5/school/getInfoByInviteCode',
			dataType:'json',
			data:{
				inviteCode:inviteCode,
				page:page,
				size:size,
				startTime:startTime,
				endTime:endTime,
				isAdmin:isAdmin
				},
			async: false,
			cache: false,
			type: "post",
			success:function(json){
				$('table').show();
				var shtml = '<tr><th>用户ID</th><th>用户名</th><th>手机号</th><th>所在学校</th><th>注册时间</th></tr>';
				var data = json.result;
				if(data == ''){
					$('table').hide();alert('没有数据！');return;
				}
				var shtmlTD = '';
				$('#content p').text('总用户数'+json.extraData+'人，继续努力吧！');
				$('table').append(shtml);
				$.each(data,function(m,n){
					shtmlTD = '<tr><td>'+n.uid+'</td><td>'+n.name+'</td><td>'+n.phone+'</td><td>'+n.school+'</td><td>'+Time(n.registerTime)+'</td></tr>';
					$('table').append(shtmlTD);
				})
				
			}
		});
	});
	function Time(obj){
		//var noticeTime = obj.attr('data-time');
		var d = new Date();
		var date=new Date(parseInt(obj));
		return date.getFullYear()+"-"+fixZero(date.getMonth()+1,2)+"-"+fixZero(date.getDate(),2)+" "+fixZero(date.getHours(),2)+":"+fixZero(date.getMinutes(),2);
		function fixZero(num,length){
			var str=""+num;
			var len=str.length;
			var s="";
			for(var i=length;i-->len;){
				s+="0";
			}
			return s+str;
		}
	}
});