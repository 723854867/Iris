;$(function(){
	var serverUrlimg = $('#serverUrlimg').val();
	$('.userPic').each(function(){
		var imgHtml = '<img width="48" height="48" src="'+ serverUrlimg + '/restwww/download' + $(this).parent().attr('data-pic')+'">';
		$(this).append(imgHtml);
	});
	$('.ac_function_btn').click(function(){
		$('.blackBox,.ac_rule').show();
		$('.closeBtn').click(function(){
			$('.blackBox,.ac_rule').hide();
		});
	});
	var oHeight = $(window).width()/10*8 - 62;
	console.log(oHeight);
	$('.ac_copy').height(oHeight);
});