$("#main-top").load("top.html");

var serverbase='';
$.ajax({
    url:serverbase+'/jilu/mweb?action=menulist_get',
    dataType: 'json',
    method: 'GET',
    success: function(data) {
    	//debug;
    	var menulist=data.data;
    	var htmlMenu="";
    	htmlMenu=htmlMenu+"<li><a href='index.html'> <i class='glyphicon glyphicon-home'></i> 首页</a></li>";
    	$.each( menulist, function(index1, content1)
		{ 
			//alert( "the man's no. is: " + index + ",and " + content.name + " is learning " + content.lang );
			if(content1.parentmenuid==0)
			{
				htmlMenu=htmlMenu+"<li><a href='#mn"+index1+"' class='nav-header collapsed' data-toggle='collapse'> <i class='glyphicon glyphicon-th-large'></i>"+content1.cname+"<span class='pull-right glyphicon glyphicon-chevron-toggle'></span></a>";
				htmlMenu=htmlMenu+"<ul id='mn"+index1+"' class='nav nav-list secondmenu collapse' style='height: 0px;'>";
				
				$.each( menulist, function(index2, content2)
				{ 
					if(content2.parentmenuid==content1.menuid)
					{
						htmlMenu=htmlMenu+"<li><a href='/jilu/"+content2.url+"'><i class='glyphicon glyphicon-list'></i>&nbsp;"+content2.cname+"</a></li>";
					}
				});
				
				htmlMenu=htmlMenu+"</ul>";
				htmlMenu=htmlMenu+"</li>";
			}
			else
			{
				
			}
		});
    	//alert(htmlMenu);
    	//console.log(htmlMenu);
    	$("#main-nav").html(htmlMenu);
    	
    	
    	SelectParentPage();
    	
    },
    error: function(xhr) {
        // 导致出错的原因较多，以后再研究
        alert('error:' + JSON.stringify(xhr));
    }
})
.done(function(data) {
    // 请求成功后要做的工作
    //console.log('success');
})
.fail(function() {
    // 请求失败后要做的工作
    //console.log('error');
})
.always(function() {
    // 不管成功或失败都要做的工作
    //console.log('complete');
});


function SelectParentPage()
{
	var curpagepathname=window.location.pathname;
	var curpagepath=curpagepathname.replace("/jilu/", "");
	$.ajax({
	    url:serverbase+'/jilu/mweb?action=sys_page_getparent&pagePath='+curpagepath,
	    dataType: 'json',
	    method: 'GET',
	    success: function(data) {
	    	//debug;
	    	var parentpage=data.data;

	    	
	    	$(".secondmenu li").each(function (index) {
	    		
	            var href = $(this).find("a").attr("href");
	            href=href.replace("/jilu/","");
	            var url = parentpage;
	            //console.log("href:"+href.split('/').pop());
	            //console.log("url:"+url.split('/').pop().split('?')[0]);
	            if (href == url) {
	            	
	                $(this).addClass('active');
	                $(this).parent().addClass('in');
	                $(this).parent().attr("aria-expanded", "true");
	                $(this).parent().css("height", "");
	            }
	        });
	    	
	    },
	    error: function(xhr) {
	        // 导致出错的原因较多，以后再研究
	        alert('error:' + JSON.stringify(xhr));
	    }
	})
	.done(function(data) {
	    // 请求成功后要做的工作
	    //console.log('success');
	})
	.fail(function() {
	    // 请求失败后要做的工作
	    //console.log('error');
	})
	.always(function() {
	    // 不管成功或失败都要做的工作
	    //console.log('complete');
	});
	
	
		
}
exit=function(){
	$.ajax({
	    type: 'POST',
	    url: path+'/mweb',
	    data: {'action':'login_out'},
	    dataType: 'json',
	    success: function (json) {
	    	if(json["code"]==0)
	    		window.location.href=path+'/mweb/login.html';
	    },
	    error: function () {
	        alert("数据加载失败");
	    }
	});
}

