$("#main-top").load("../usercontrol/top.html");
reqMenu();
function reqMenu(){
	$.ajax({
	    url:'/jilu/backstage?action=menulist_get',
	    dataType: 'json',
	    method: 'POST',
	    success: function(data) {
	    	var menulist=data.data;
	    	printMenu(menulist);
	    },
	    error: function(xhr) {
	        alert('error:' + JSON.stringify(xhr));
	    }
	});	
}
function printMenu(menulist){
	var htmlMenu="";
	htmlMenu=htmlMenu+"<li><a href='#'> <i class='glyphicon glyphicon-home'></i> 首页</a></li>";
	$.each( menulist, function(index1, content1)
	{ 
		if(content1.type==0&&content1.permissionId!=1)
		{
			htmlMenu=htmlMenu+"<li><a href='#mn"+index1+"' class='nav-header collapsed' data-toggle='collapse'> <i class='glyphicon glyphicon-th-large'></i>"+content1.permissionName+"<span class='pull-right glyphicon glyphicon-chevron-toggle'></span></a>";
			htmlMenu=htmlMenu+"<ul id='mn"+index1+"' class='nav nav-list secondmenu collapse' style='height: 0px;'>";
			
			$.each( menulist, function(index2, content2)
			{ 
				if(content2.pid==content1.permissionId)
				{
					htmlMenu=htmlMenu+"<li><a href='/jilu/"+content2.href+"'><i class='glyphicon glyphicon-list'></i>&nbsp;"+content2.permissionName+"</a></li>";
				}
			});
			
			htmlMenu=htmlMenu+"</ul>";
			htmlMenu=htmlMenu+"</li>";
		}
		else
		{
			
		}
	});
	$("#main-nav").html(htmlMenu);
	var url = window.location.href;
	$(".secondmenu li").each(function (index) {
        var href = $(this).find("a").attr("href");
        href=href.replace("../","");
        if (url.indexOf(href)>0 && url.indexOf("static")<0) {
            $(this).addClass('active');
            $(this).parent().addClass('in');
            $(this).parent().attr("aria-expanded", "true");
            $(this).parent().css("height", "");
        }
    });
}