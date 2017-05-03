$.ajax({
    url:'/qydj/common?action=anno_get&annoId='+getUrlParam('annoId'),
    dataType: 'json',
    method: 'GET',
    success: function(data) {
    	//debug;
    	var anno=data.data;
    	$('#txt_title').html(anno.title);
    	$('#txt_updated').html(anno.updated);
    	$('#txt_author').html(anno.author);
    	$('#txt_source').html(anno.source);
        $('#txt_content').html(anno.content);
    },
    error: function(xhr) {
        // 导致出错的原因较多，以后再研究
        //alert('error:' + JSON.stringify(xhr));
    }
})
.done(function(data) {
    // 请求成功后要做的工作
    console.log('success');
})
.fail(function() {
    // 请求失败后要做的工作
    console.log('error');
})
.always(function() {
    // 不管成功或失败都要做的工作
    console.log('complete');
});

//获取url中的参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}s