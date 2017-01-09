define(['../libs/jquery-2.1.4'], function(jquery) {

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
	
	return aler;

})











