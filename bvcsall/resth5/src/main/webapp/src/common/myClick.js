define(['../libs/jquery-2.1.4'],function(jquery){

	var myClick = {

		init:function(){
			//初始化
		},

		attention:function(obj){
			
			$('.'+ obj ).bind('click',function(){

				if($(this).hasClass('attenBtn')){

					$(this).removeClass('attenBtn').addClass('attenBtnActive');

				}else{

					$(this).removeClass('attenBtnActive').addClass('attenBtn');

				}
			});
		},
		allAttention:function(obj1,obj2){
			
			$('.'+ obj1 ).bind('click',function(){

				$('.'+ obj2 ).removeClass('attenBtn').addClass('attenBtnActive');
			
			});
		}

	};

	return myClick;

});