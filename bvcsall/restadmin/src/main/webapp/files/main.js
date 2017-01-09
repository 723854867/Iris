/**
 * 
 */
var curMenu = null, zTree_Menu = null;
var setting = {
	treeId : 'treeMenu',
	view: {
		showLine: false,
		selectedMulti: false,
		dblClickExpand: false
	},
	data: {
		simpleData: {
			enable: true,
			idKey : 'id'
		}
	},
	callback: {
		//onNodeCreated: this.onNodeCreated,
		beforeClick: this.beforeClick/* ,
		onClick : this.onClick */
	}
};

function beforeClick(treeId, node) {
	//debugger;
	if (node.isParent) {
		//alert('parent');
		if (node.open) {
			zTree_Menu.expandNode(node,false);
		} else {
			zTree_Menu.expandNode(node,true);
		}
		//return false;
	}
	//alert('click'+node.tId);
	return false; //!node.isParent;
}
$(document).ready(function(){
	//$("#mainFrame").panel('setTitle',curMenu);
	//$("#mainFrame").panel({title:curMenu});
	$("#mainFrame").attr("title",curMenu);
//	$.ajax({
//		  type: "POST",
//		  url: "index/menuAjax",
//		  dataType: "json",
//		  success : function(data,status){
//			  //alert('status'+status);
//			  $.fn.zTree.init($("#treeMenu"), setting, data);
//			  	alert(current_menu_id);
//				zTree_Menu = $.fn.zTree.getZTreeObj("treeMenu");
//				zTree_Menu.expandAll(true);
//				//alert('treeid:'+zTree_Menu.setting.treeId);
//				var node = zTree_Menu.getNodeByParam('id',mid);
//				alert(node);
//				if(node != null){
//					//alert(node.name);
//					zTree_Menu.selectNode(node,false);
//					//alert('done');
//					$("#mainFrame").panel('setTitle',node.name);
//				}
//		  }
//		});	
});


//将form中的值转换为键值对。
function getFormJson(frm) {
    var o = {};
    var a = $(frm).serializeArray();
    $.each(a, function () {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });

    return o;
}

function showMessage(title,message,timeout){
	if(!timeout){
		timeout=2000;
	}
	$.messager.show({ // show error message
		title: title,
		msg: message,
		timeout:timeout,
		style:{
			right:'',
			top:document.body.scrollTop+document.documentElement.scrollTop,
			bottom:''
		}
	});
}
