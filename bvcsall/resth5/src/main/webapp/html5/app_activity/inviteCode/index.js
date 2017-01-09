$(function(){
	function alertMsg(msg,callFn){
		var box = $('<div class="promptBox">'+msg+'</div>');
		$('body').append(box);
		setTimeout(function(){
			box.remove();
		},1500);
		callFn && callFn();
	};
	var interUrl = $('#interfaceurl').val();
		$('#btn').click(function(){
			var uid = $('#userId').val();
			var inviteCode = $('#inviteCode').val();
			if(uid == ''){
				alertMsg('Id不能为空');return;
			}
			if(inviteCode == ''){
				alertMsg('请输入邀请码');return;
			}
			$.ajax({
				url: interUrl + '/resth5/spread/commit',
				dataType:'json',
				data:{
					uid:uid,
					inviteCode:inviteCode
					},
				async: false,
				cache: false,
				type: "post",
				success:function(data){
					if(data.code == '-1'){
						alertMsg(data.message);
					}else if(data.code == 200){
						alertMsg(data.message);
					}
				}
			})
		});
	})