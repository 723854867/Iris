$(function(){
	$('.user1').on('click',function(){
		$('.entrance').hide();
		$('.bg1').show();
		$('.bg2').hide();
	})
	$('.user2').on('click',function(){
		$('.entrance').hide();
		$('.bg2').show();
		$('.bg1').hide();
	})
	$('.rule').on('click',function(){
		$('.ruleBlock').show();
		$('.returnBtn').on('click',function(){
			$('.ruleBlock').hide();
		});
	});
	$('.makeSure_lianmeng').on('click',function(){
		var BliveID = $('.BliveID_lianmeng').val();
		if(BliveID == ''){
			aler.alertMsg("请输入BliveID");return;
		}else{
			$('.url').text($('#interfaceurl').val()+'/resth5/wow/share?BliveID='+BliveID);
			$('#urlBox').show();
			$('.blackBox').show();
			$('.closeBtn').click(function(){
				$('#urlBox').hide();
				$('.blackBox').hide();				
			});
		}
	}); 
	$('.makeSure_buluo').on('click',function(){
		var BliveID = $('.BliveID_buluo').val();
		if(BliveID == ''){
			aler.alertMsg("请输入LIVEID");return;
		}else{
			$('.url').text($('#interfaceurl').val()+'/resth5/wow/share?BliveID='+BliveID);
			$('#urlBox').show();
			$('.blackBox').show();
			$('.closeBtn').click(function(){
				$('#urlBox').hide();
				$('.blackBox').hide();				
			});
		}
	});
	var aler = {
		alertMsg: function(msg,callFn){
			var box = $('<div class="promptBox">'+msg+'</div>');
			$('body').append(box);
			setTimeout(function(){
				box.remove();
			},1500);
			callFn && callFn();
		},
		alertJamp: function(msg,url,callFn){
			var box = $('<div class="promptBox">'+msg+'</div>');
			$('body').append(box);
			setTimeout(function(){
				box.remove();
				callFn && callFn();
				window.location.href = url;
			},1500);
		}
	};
});