<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!--
    <script type="text/javascript" src="js/ajaxfileupload.js"></script>
     -->
    <script src="js/fp/vendor/jquery.ui.widget.js"></script>
    <script src="js/fp/jquery.iframe-transport.js"></script>
    <script src="js/fp/jquery.fileupload.js"></script>
    <script type='text/javascript' src="js/admin/query_customize_activity.js"></script>
    <title>定制活动管理</title>
    <script type="text/javascript">
    
    function showActivityDialog() {
    	
        $("#ca_dlg").show();
        $("#ca_dlg").dialog({
            title: '选择活动',
            width: 800,
            height: 700,
            closed: false,
            cache: true,
            modal: true
        });
    }

    function selectActivity(){
        var row = $("#activityTable").datagrid('getChecked');
        
        var activityName = row[0].title;
        var activityId = row[0].id;
        $("#activityId").val(activityId);
        $("#activityName").val(activityName);
        $("#ca_dlg").dialog("close");
    }


    function activityListSearch(){
        $("#activityTable").datagrid('reload',{keyword:$('#searchForm1').find('[name=keyword]').val()});
    }
    
    
	function showTemplateDialog(type) {
		
		$("#templateType").val(type);
		$("#templateType2").val(type);
		$("#templateTable").datagrid('reload',{type:type});
// 		if(type==1){
// 			$("#templateTable").datagrid('reload',{type:$('#searchForm2').find('[name=type]').val()});
// 		}else if(type==4){
// 			$("#templateTable").datagrid('reload',{type:$('#searchForm2').find('[name=type]').val()});
// 		}
		
		
    	
        $("#template_dlg").show();
        $("#template_dlg").dialog({
            title: '选择片头或MV',
            width: 800,
            height: 700,
            closed: false,
            cache: true,
            modal: true
        });
    }

    function selectTemplate(){
    	var type=$("#templateType").val();
        var row = $("#templateTable").datagrid('getChecked');
        
        var templateName = row[0].title;
        var templateId = row[0].id;
        //alert(type);
        if(type==0){
        	$("#headId").val(templateId);
            $("#headName").val(templateName);
        }else if(type==3){
        	$("#mvId").val(templateId);
            $("#mvName").val(templateName);
        }
        $("#template_dlg").dialog("close");
    }


    function templateListSearch(){
        $("#templateTable").datagrid('reload',{keyword:$('#searchForm2').find('[name=type]').val()});
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
        var playId = "";
        $(function () {
            //启用表单验证
            $('.validatebox-text').bind('blur', function () {
                $(this).validatebox('enableValidation').validatebox('validate');
            });
            
            
            $('#actPicIosFile').fileupload({
                url: 'customizeActivity/fileUpload?fileType=actPicIos',
                sequentialUploads: true,
                dataType: 'json',
                type: 'post',
                crossDomain: true,
                done: function (e, data) {
                    uploadresult = data.result;

                    $("#actPicIos").val(uploadresult["result"]);
                    showMessage("通知", "恭喜您上传成功");
                },
                progress: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#p1').progressbar('setValue', progress);
                },
                start: function (e) {
                    $('#p1').show();
                    $('#p1').progressbar('setValue', 0);
                },
                change: function (e, data) {
                    var fileName = data.files[0].name;
                    var fileext = fileName.substring(fileName.lastIndexOf("."));
                    fileext = fileext.toLowerCase();
                    if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
                        showMessage("Error", "请上传jpg，gif，jpeg，png，bmp等类型的文件");
                        return false;
                    }
                }
            });
            
            $('#actIconIosFile').fileupload({
                url: 'customizeActivity/fileUpload?fileType=actIconIos',
                sequentialUploads: true,
                dataType: 'json',
                type: 'post',
                crossDomain: true,
                done: function (e, data) {
                    uploadresult = data.result;

                    $("#actIconIos").val(uploadresult["result"]);
                    showMessage("通知", "恭喜您上传成功");
                },
                progress: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#p2').progressbar('setValue', progress);
                },
                start: function (e) {
                    $('#p2').show();
                    $('#p2').progressbar('setValue', 0);
                },
                change: function (e, data) {
                    var fileName = data.files[0].name;
                    var fileext = fileName.substring(fileName.lastIndexOf("."));
                    fileext = fileext.toLowerCase();
                    if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
                        showMessage("Error", "请上传jpg，gif，jpeg，png，bmp等类型的文件");
                        return false;
                    }
                }
            });
            
            $('#loadPicIosFile').fileupload({
                url: 'customizeActivity/fileUpload?fileType=loadPicIos',
                sequentialUploads: true,
                dataType: 'json',
                type: 'post',
                crossDomain: true,
                done: function (e, data) {
                    uploadresult = data.result;

                    $("#loadPicIos").val(uploadresult["result"]);
                    showMessage("通知", "恭喜您上传成功");
                },
                progress: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#p3').progressbar('setValue', progress);
                },
                start: function (e) {
                    $('#p3').show();
                    $('#p3').progressbar('setValue', 0);
                },
                change: function (e, data) {
                    var fileName = data.files[0].name;
                    var fileext = fileName.substring(fileName.lastIndexOf("."));
                    fileext = fileext.toLowerCase();
                    if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
                        showMessage("Error", "请上传jpg，gif，jpeg，png，bmp等类型的文件");
                        return false;
                    }
                }
            });
            
            $('#loadFailPicIosFile').fileupload({
                url: 'customizeActivity/fileUpload?fileType=loadFailPicIos',
                sequentialUploads: true,
                dataType: 'json',
                type: 'post',
                crossDomain: true,
                done: function (e, data) {
                    uploadresult = data.result;

                    $("#loadFailPicIos").val(uploadresult["result"]);
                    showMessage("通知", "恭喜您上传成功");
                },
                progress: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#p4').progressbar('setValue', progress);
                },
                start: function (e) {
                    $('#p4').show();
                    $('#p4').progressbar('setValue', 0);
                },
                change: function (e, data) {
                    var fileName = data.files[0].name;
                    var fileext = fileName.substring(fileName.lastIndexOf("."));
                    fileext = fileext.toLowerCase();
                    if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
                        showMessage("Error", "请上传jpg，gif，jpeg，png，bmp等类型的文件");
                        return false;
                    }
                }
            });
            
            $('#bagIosFile').fileupload({
                url: 'customizeActivity/fileUpload?fileType=bagIos',
                sequentialUploads: true,
                dataType: 'json',
                type: 'post',
                crossDomain: true,
                done: function (e, data) {
                    uploadresult = data.result;

                    $("#bagIos").val(uploadresult["result"]);
                    showMessage("通知", "恭喜您上传成功");
                },
                progress: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#p5').progressbar('setValue', progress);
                },
                start: function (e) {
                    $('#p5').show();
                    $('#p5').progressbar('setValue', 0);
                },
                change: function (e, data) {
                    var fileName = data.files[0].name;
                    var fileext = fileName.substring(fileName.lastIndexOf("."));
                    fileext = fileext.toLowerCase();
                    if ((fileext != '.zip')) {
                        showMessage("Error", "请上传zip类型的文件");
                        return false;
                    }
                }
            });
            
            $('#buttonIconIosFile').fileupload({
                url: 'customizeActivity/fileUpload?fileType=buttonIconIos',
                sequentialUploads: true,
                dataType: 'json',
                type: 'post',
                crossDomain: true,
                done: function (e, data) {
                    uploadresult = data.result;

                    $("#buttonIconIos").val(uploadresult["result"]);
                    showMessage("通知", "恭喜您上传成功");
                },
                progress: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#p11').progressbar('setValue', progress);
                },
                start: function (e) {
                    $('#p11').show();
                    $('#p11').progressbar('setValue', 0);
                },
                change: function (e, data) {
                    var fileName = data.files[0].name;
                    var fileext = fileName.substring(fileName.lastIndexOf("."));
                    fileext = fileext.toLowerCase();
                    if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
                        showMessage("Error", "请上传jpg，gif，jpeg，png，bmp等类型的文件");
                        return false;
                    }
                }
            });
            
            
            
            
            $('#actPicAndroidFile').fileupload({
                url: 'customizeActivity/fileUpload?fileType=actPicAndroid',
                sequentialUploads: true,
                dataType: 'json',
                type: 'post',
                crossDomain: true,
                done: function (e, data) {
                    uploadresult = data.result;

                    $("#actPicAndroid").val(uploadresult["result"]);
                    showMessage("通知", "恭喜您上传成功");
                },
                progress: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#p6').progressbar('setValue', progress);
                },
                start: function (e) {
                    $('#p6').show();
                    $('#p6').progressbar('setValue', 0);
                },
                change: function (e, data) {
                    var fileName = data.files[0].name;
                    var fileext = fileName.substring(fileName.lastIndexOf("."));
                    fileext = fileext.toLowerCase();
                    if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
                        showMessage("Error", "请上传jpg，gif，jpeg，png，bmp等类型的文件");
                        return false;
                    }
                }
            });
            
            $('#actIconAndroidFile').fileupload({
                url: 'customizeActivity/fileUpload?fileType=actIconAndroid',
                sequentialUploads: true,
                dataType: 'json',
                type: 'post',
                crossDomain: true,
                done: function (e, data) {
                    uploadresult = data.result;

                    $("#actIconAndroid").val(uploadresult["result"]);
                    showMessage("通知", "恭喜您上传成功");
                },
                progress: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#p7').progressbar('setValue', progress);
                },
                start: function (e) {
                    $('#p7').show();
                    $('#p7').progressbar('setValue', 0);
                },
                change: function (e, data) {
                    var fileName = data.files[0].name;
                    var fileext = fileName.substring(fileName.lastIndexOf("."));
                    fileext = fileext.toLowerCase();
                    if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
                        showMessage("Error", "请上传jpg，gif，jpeg，png，bmp等类型的文件");
                        return false;
                    }
                }
            });
            
            $('#loadPicAndroidFile').fileupload({
                url: 'customizeActivity/fileUpload?fileType=loadPicAndroid',
                sequentialUploads: true,
                dataType: 'json',
                type: 'post',
                crossDomain: true,
                done: function (e, data) {
                    uploadresult = data.result;

                    $("#loadPicAndroid").val(uploadresult["result"]);
                    showMessage("通知", "恭喜您上传成功");
                },
                progress: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#p8').progressbar('setValue', progress);
                },
                start: function (e) {
                    $('#p8').show();
                    $('#p8').progressbar('setValue', 0);
                },
                change: function (e, data) {
                    var fileName = data.files[0].name;
                    var fileext = fileName.substring(fileName.lastIndexOf("."));
                    fileext = fileext.toLowerCase();
                    if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
                        showMessage("Error", "请上传jpg，gif，jpeg，png，bmp等类型的文件");
                        return false;
                    }
                }
            });
            
            $('#loadFailPicAndroidFile').fileupload({
                url: 'customizeActivity/fileUpload?fileType=loadFailPicAndroid',
                sequentialUploads: true,
                dataType: 'json',
                type: 'post',
                crossDomain: true,
                done: function (e, data) {
                    uploadresult = data.result;

                    $("#loadFailPicAndroid").val(uploadresult["result"]);
                    showMessage("通知", "恭喜您上传成功");
                },
                progress: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#p9').progressbar('setValue', progress);
                },
                start: function (e) {
                    $('#p9').show();
                    $('#p9').progressbar('setValue', 0);
                },
                change: function (e, data) {
                    var fileName = data.files[0].name;
                    var fileext = fileName.substring(fileName.lastIndexOf("."));
                    fileext = fileext.toLowerCase();
                    if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
                        showMessage("Error", "请上传jpg，gif，jpeg，png，bmp等类型的文件");
                        return false;
                    }
                }
            });
            
            $('#bagAndroidFile').fileupload({
                url: 'customizeActivity/fileUpload?fileType=bagAndroid',
                sequentialUploads: true,
                dataType: 'json',
                type: 'post',
                crossDomain: true,
                done: function (e, data) {
                    uploadresult = data.result;

                    $("#bagAndroid").val(uploadresult["result"]);
                    showMessage("通知", "恭喜您上传成功");
                },
                progress: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#p10').progressbar('setValue', progress);
                },
                start: function (e) {
                    $('#p10').show();
                    $('#p10').progressbar('setValue', 0);
                },
                change: function (e, data) {
                    var fileName = data.files[0].name;
                    var fileext = fileName.substring(fileName.lastIndexOf("."));
                    fileext = fileext.toLowerCase();
                    if ((fileext != '.zip')) {
                        showMessage("Error", "请上传zip等类型的文件");
                        return false;
                    }
                }
            });
            
            $('#buttonIconAndroidFile').fileupload({
                url: 'customizeActivity/fileUpload?fileType=buttonIconAndroid',
                sequentialUploads: true,
                dataType: 'json',
                type: 'post',
                crossDomain: true,
                done: function (e, data) {
                    uploadresult = data.result;

                    $("#buttonIconAndroid").val(uploadresult["result"]);
                    showMessage("通知", "恭喜您上传成功");
                },
                progress: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#p12').progressbar('setValue', progress);
                },
                start: function (e) {
                    $('#p12').show();
                    $('#p12').progressbar('setValue', 0);
                },
                change: function (e, data) {
                    var fileName = data.files[0].name;
                    var fileext = fileName.substring(fileName.lastIndexOf("."));
                    fileext = fileext.toLowerCase();
                    if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
                        showMessage("Error", "请上传jpg，gif，jpeg，png，bmp等类型的文件");
                        return false;
                    }
                }
            });
            
            
            
            
            $("#activityTable").datagrid({
                nowrap: true, //是否换行
                autoRowHeight: true, //自动行高
                fitColumns: true,
                fit: true,
                striped: true,
                pageNumber: 1,
                collapsible: true, //是否可折叠
                remoteSort: true,
                singleSelect: true, //是否单选
                pagination: true, //分页控件
                rownumbers: true, //行号
                scrollbarSize: 0,
                loadMsg: "数据加载中.....",
                pagePosition: 'bottom',
                url: "activity/queryActivityList",
                columns: [[
					{field: 'id', title: '编号', width: 100},
                    {field: 'title', title: '活动名称', width: 100}
                ]],
                onLoadSuccess: function () {
                    $('#activityTable').datagrid('clearSelections');
                },
                pageSize: 20,
                pageList: [20, 40, 60, 80, 100],
                beforePageText: '第', //页数文本框前显示的汉字
                afterPageText: '页    共 {pages} 页',
                displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
            });
            
            $("#templateTable").datagrid({
                nowrap: true, //是否换行
                autoRowHeight: true, //自动行高
                fitColumns: true,
                fit: true,
                striped: true,
                pageNumber: 1,
                collapsible: true, //是否可折叠
                remoteSort: true,
                singleSelect: true, //是否单选
                pagination: true, //分页控件
                rownumbers: true, //行号
                scrollbarSize: 0,
                loadMsg: "数据加载中.....",
                pagePosition: 'bottom',
                url: "template/queryInitPageList",
                columns: [[
					{field: 'id', title: '编号', width: 100},
					{
			            field: 'img',
			            title: '缩略图',
			            width: 100,
			            height: 100,
			            align: "center",
			            formatter: function (value, row, index) {
			                if ($.trim(row.pic) != "") {
			                    return "<img src='/restadmin/download" + row.pic + "' style='margin:0 auto;' width='90' height='90'/>";
			                }
			            }
			        },
                    {field: 'title', title: '名称', width: 100},
                    {field: 'description', title: '描述', width: 100},
                    {	field: 'type', title: '<span class="columnTitle">类型</span>', width: 70,align:'center',
                    	formatter: function (value) {
                            if (value == 0) {
                                return "片头";
                            }else if (value == 3) {
                                return "MV";
                            } else {
                                
                            }
                        }
                    },
                ]],
                onLoadSuccess: function () {
                    $('#activityTable').datagrid('clearSelections');
                },
                pageSize: 20,
                pageList: [20, 40, 60, 80, 100],
                beforePageText: '第', //页数文本框前显示的汉字
                afterPageText: '页    共 {pages} 页',
                displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
            });
            
           var type='${activity.type}';
           
           if(type){
        	   if(type=='1'){
        		   showTr('url');
        		   hideTr('activity');
        		   hideTr('shoot');
        	   }else if(type=='2'){
        		   showTr('activity');
        		   hideTr('url');
        		   hideTr('shoot');
        	   }else if(type=='3'){
        		   showTr('shoot');
        		   hideTr('url');
        		   hideTr('activity');
        		   $("#activityTr").show();
        	   }else if(type=='4'){
        		   hideTr('shoot');
        		   hideTr('url');
        		   hideTr('activity');
        	   }
        	   
           }else{
        	   showTr('url');
    		   hideTr('activity');
    		   hideTr('shoot');
           }


            

        });
        
        function showTr(type){
        	
        	$("tr[classType='"+type+"']").each(function(index ,element){
                //alert($(this).text())
                element.style.display = 'table-row';
            });
        }
        
		function hideTr(type){
			$("tr[classType='"+type+"']").each(function(index ,element){
                //alert($(this).text())
                element.style.display = 'none';
            });
        }
		
		function changeType(obj){
			var type=obj.value;
			if(type){
        	   if(type=='1'){
        		   showTr('url');
        		   hideTr('activity');
        		   hideTr('shoot');
        	   }else if(type=='2'){
        		   showTr('activity');
        		   hideTr('url');
        		   hideTr('shoot');
        	   }else if(type=='3'){
        		   showTr('shoot');
        		   hideTr('url');
        		   hideTr('activity');
        		   $("#activityTr").show();
        	   }else if(type=='4'){
        		   hideTr('shoot');
        		   hideTr('url');
        		   hideTr('activity');
        	   }
        	   
           }else{
        	   showTr('url');
    		   hideTr('activity');
    		   hideTr('shoot');
           }
			
		}


        function clickupload() {
            var id;
            $("#playId").val(playId);
            var formjson = getFormJson('#myform');
            if ($.trim(playId) == "error") {
                showMessage("Error", "文件上传失败，请重新选择文件上传");
                return;
            }
            if ($.trim(playId) == "") {
                showMessage("提示", "请等待文件上传完成后，再点击确认");
                return;
            }
            $.ajax({
                url: 'video/uploadsave',
                data: formjson,
                type: "post",
                dataType: "json",
                beforeSend: function () {
                    return $('#myform').form('enableValidation').form('validate');
                },
                success: function (result) {
                    location.href = "video/checledVideos";
                }
            });
        }

        function start() {
            var value = $('#p').progressbar('getValue');
            if (value < 100) {
                value += Math.floor(Math.random() * 10);
                $('#p').progressbar('setValue', value);
                setTimeout(arguments.callee, 200);
            }
        }
        ;


        function chooseOldVideo(obj, activityId, videoId) {

            var url = "<c:url value='/activity/showRemoveActivityList?activityId='/>" + activityId + "&videoId=" + videoId;

            var rValue = openWindow(url, '', '', '');


        }

        function createNewVideo(obj) {

            var url = "<c:url value='/video/popUpload?popFlg=1'/>";

            var rValue = openWindow(url, '', '950', '650');


        }

        function chooseOldVideo(obj) {

            var url = "<c:url value='/video/showUserVideos?popFlg=1'/>";

            var rValue = openWindow(url, '', '1100', '');


        }

        function openWindow(url, name, width, height, feature) {

            var iWidth = 800; //弹出窗口的宽度;
            var iHeight = 600; //弹出窗口的高度;
            if (width && width != '') {
                iWidth = width;
            }
            if (height && height != '') {
                iHeight = height;
            }
            var iTop = (window.screen.availHeight - 30 - iHeight) / 2; //获得窗口的垂直位置;
            var iLeft = (window.screen.availWidth - 10 - iWidth) / 2; //获得窗口的水平位置;
            //alert(iTop);
            //alert(iLeft);
            if (!feature || feature == null || feature == '') {
                feature = "height=" + iHeight + ",width=" + iWidth + ", top=" + iTop + ", left=" + iLeft + ",alwaysRaised=yes,resizable=no,z-look=yes";
            } else {
                feature = "height=" + iHeight + ",width=" + iWidth + ", top=" + iTop + ", left=" + iLeft + "," + feature;
            }
            var rValue = window.open(url, name, feature);
            //alert('window');
            //alert(rValue);
            if (typeof (rValue) == "undefined") {
                rValue = window.ReturnValue;
            }

            return rValue;
        }

        function getValue(obj, key) {
            if (window.showModalDialog) {
                window.returnValue = obj;
                if (window.opener && window.opener != null) {
                    window.opener.ReturnValue = obj;
                }
            } else {
                window.opener.ReturnValue = obj;
                window.opener.setValue(obj, key);
            }

            window.close();
        }

        function setValue(id, name) {
// 		$('#videoId').val(id);
// 		$('#videoName').val(name);

            document.getElementById("videoId").value = id;
            document.getElementById("videoName").value = name;
        }

        function flushPage() {
            window.location.reload();
        }

        function pageSub() {
            var title = $('#title').val();
            if (!title) {
                alert('请输入标题!');
                return
            }
            
            var type = $('#type').val();
            if (!type) {
                alert('请选择类型!');
                return
            }
            
            var url = $('#url').val();
            if (type==1&&!url) {
                alert('请填写URL链接!');
                return
            }
            
            var activityId = $('#activityId').val();
            if ((type==2||type==3)&&!activityId) {
                alert('请选择活动!');
                return
            }
            
            var activityDes = $('#activityDes').val();
            if (type==3&&!activityDes) {
                alert('请填写活动描述!');
                return
            }
            
            var shareDes = $('#shareDes').val();
            if (type==3&&!shareDes) {
                alert('请填写分享描述!');
                return
            }
            
            /* var headId = $('#headId').val();
            if (type==3&&!headId) {
                alert('请选择片头!');
                return
            } */
            
            var mvId = $('#mvId').val();
            if (type==3&&!mvId) {
                alert('请选择MV!');
                return
            }
            
            var timeStartStr = $('#timeStartStr').datebox('getValue');
            if (!timeStartStr) {
                alert('请选择开始时间!');
                return
            }
            
            var timeEndStr = $('#timeEndStr').datebox('getValue');
            if (!timeEndStr) {
                alert('请选择结束时间!');
                return
            }
            
            
            var actPicAndroid = $('#actPicIos').val();
            if (!actPicIos) {
                alert('请选择活动大图Ios!');
                return
            }
            
            var actIconIos = $('#actIconIos').val();
            if (!actIconIos) {
                alert('请选择活动缩略图Ios!');
                return
            }
            
            var actIconIos = $('#actIconIos').val();
            if (type==3&&!actIconIos) {
                alert('请选择素材加载图Ios!');
                return
            }
            
            var bagIos = $('#bagIos').val();
            if (type==3&&!bagIos) {
                alert('请选择拍摄素材包Ios!');
                return
            }
            
            var buttonIconIos = $('#buttonIconIos').val();
            if (!buttonIconIos) {
                alert('请选择活动ButtonIos!');
                return
            }
            
            
            var actPicAndroid = $('#actPicAndroid').val();
            if (!actPicAndroid) {
                alert('请选择活动大图Android!');
                return
            }
            
            var actIconAndroid = $('#actIconAndroid').val();
            if (!actIconAndroid) {
                alert('请选择活动缩略图Android!');
                return
            }
            
            var actIconAndroid = $('#actIconAndroid').val();
            if (type==3&&!actIconAndroid) {
                alert('请选择素材加载图Android!');
                return
            }
            
            var bagAndroid = $('#bagAndroid').val();
            if (type==3&&!bagAndroid) {
                alert('请选择拍摄素材包 Android!');
                return
            }
            
            var buttonIconAndroid = $('#buttonIconAndroid').val();
            if (!buttonIconAndroid) {
                alert('请选择活动ButtonAndroid!');
                return
            }
        	
        	
            $('#myform').submit();
        }


    </script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true" style="overflow-y:scroll;">
    <div id="a" region="north" border="false"
         style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
        <div data-options="region:'north',border:false"
             style="height: 40px; padding-top: 5px; overflow: hidden;">
            <h2 style="float:left;padding-left:10px;margin: 1px">定制活动管理</h2>
        </div>
        <div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
            <form id="myform" method="post" action="customizeActivity/customizeActivityAdd">
                <table style="margin:0 auto;text-align: left;" class="table-doc">
                    <tr>
                        <td  colspan="1"  style="text-align: right;">标题:</td>
                        <td  colspan="1"  style="text-align: left;">
                            <input class="easyui-validate" name="title" id="title" value="${activity.title}"
                                   <%--<c:if test="${not empty activity.title }">readonly="readonly"</c:if>--%>/>
                            <input class="easyui-validate" name="id" id="id" type="hidden" value="${activity.id}"/>
                            <input class="easyui-validate" name="templateType" id="templateType" type="hidden" value=""/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" style="text-align: right;">类型:</td>
                        <td  colspan="1" style="text-align: left;">
                            <select required="true" id="type"  name="type" onchange="changeType(this);" >
                                <option value="1" <c:if test="${activity.type eq 1 }">selected="true"</c:if>>url跳转</option>
                                
                                <option value="2" <c:if test="${activity.type eq 2 }">selected="true"</c:if>>端内活动跳转</option>
                                
                                <option value="3" <c:if test="${activity.type eq 3 }">selected="true"</c:if>>定制拍摄</option>
                                
                                <option value="4" <c:if test="${activity.type eq 4 }">selected="true"</c:if>>直播列表</option>
                            </select>

                            <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
                                 class="easyui-progressbar"></div>
                        </td>
                    </tr>
                    <tr classType='url' >
                        <td  colspan=""  style="text-align: right;">URL链接:</td>
                        <td  colspan="" style="text-align: left;">
                            <input class="easyui-validate" name="url" id="url" value="${activity.url}" />
                        </td>
                    </tr>
                    
                    <tr id="activityTr"  classType='activity' >
			            <td style="text-align: right;">关联活动:</td>
			            <td style="text-align: left;">
			                <input name=activityName id="activityName" readonly="" value="${activity.activity.title}" onfocus="showActivityDialog()">
			                <input name="activityId" id="activityId" type="hidden" value="${activity.activityId}">
			            </td>
			        </tr>
			        
			        <tr classType='shoot'  >
                        <td style="text-align: right;">活动描述:</td>
                        <td style="text-align: left;">
                        <textarea name="activityDes" id="activityDes" class="" cols="60" rows="5"
                                  value="">${activity.activityDes }</textarea>
                            <br>
                        </td>
                    </tr>
                    
                    <tr classType='shoot' >
                        <td style="text-align: right;">分享描述:</td>
                        <td style="text-align: left;">
                        <textarea name="shareDes" id="shareDes" class="" cols="60" rows="5"
                                  value="">${activity.shareDes }</textarea>
                            <br>
                        </td>
                    </tr>
                    <c:if test="${false }">
                    <tr classType='shoot' >
			            <td style="text-align: right;">指定片头:</td>
			            <td style="text-align: left;">
			                <input name=headName id="headName" readonly="" value="${activity.head.title}" onfocus="showTemplateDialog(0)">
			                <input name="headId" id="headId" type="hidden" value="${activity.headId}">
			            </td>
			        </tr>
			        </c:if>
			        
			        <tr classType='shoot' >
			            <td style="text-align: right;">指定MV:</td>
			            <td style="text-align: left;">
			                <input name=mvName id="mvName" readonly="" value="${activity.mv.title}" onfocus="showTemplateDialog(3)">
			                <input name="mvId" id="mvId" type="hidden" value="${activity.mvId}">
			            </td>
			        </tr>
                    
                    <tr>
                        <td style="text-align: right;">状态:</td>
                        <td style="text-align: left;">
                            <select required="true" class="easyui-combobox" name="status">
                                <option value="1" <c:if test="${activity.status eq 1 }">selected="true"</c:if>>上线</option>
                                <option value="2" <c:if test="${activity.status eq 2 }">selected="true"</c:if>>下线</option>
                            </select>

                            <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
                                 class="easyui-progressbar"></div>
                        </td>
                    </tr>
                    
                    <tr>
			            <td style="text-align: right;">生效时间:</td>
			            <td style="text-align: left;">
			                <input type="text"  class="easyui-datebox" style="width: 120px;" name="timeStartStr" id="timeStartStr" value="${activity.timeStart }" >
		                    &nbsp;----&nbsp;
		                    <input type="text" class="easyui-datebox" style="width: 120px;" name="timeEndStr" id="timeEndStr" value="${activity.timeEnd }" >
			            </td>
			        </tr>
                    
                    
                    <tr>
                        <td style="text-align: right;">活动大图 地址Ios:</td>
                        <td colspan="2" valign="middle" style="vertical-align: middle;text-align: left;">
                            <div style="float: left;">
                                <input name="file" id="actPicIosFile" type="file"/>
                                <input name="actPicIos" id="actPicIos" type="hidden" value="${activity.actPicIos }"/>

                                <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p1"
                                     class="easyui-progressbar"></div>
                            </div>
                            <c:if test="${not empty activity.actPicIos}">
                                <div style="float: left;">
                                    <img style="border: 1px solid #CCCCCC;width: 70px;height: 70px;"
                                         src="/restadmin/download${activity.actPicIos}"/><br>
                                </div>
                            </c:if>
                        </td>
                    </tr>
                    
                    <tr>
                        <td style="text-align: right;">活动缩略图 地址Ios:</td>
                        <td colspan="2" valign="middle" style="vertical-align: middle;text-align: left;">
                            <div style="float: left;">
                                <input name="file" id="actIconIosFile" type="file"/>
                                <input name="actIconIos" id="actIconIos" type="hidden" value="${activity.actIconIos }"/>

                                <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p2"
                                     class="easyui-progressbar"></div>
                            </div>
                            <c:if test="${not empty activity.actIconIos}">
                                <div style="float: left;">
                                    <img style="border: 1px solid #CCCCCC;width: 70px;height: 70px;"
                                         src="/restadmin/download${activity.actIconIos}"/><br>
                                </div>
                            </c:if>
                        </td>
                    </tr>
                    
                    <tr classType='shoot' >
                        <td style="text-align: right;">素材加载图 地址Ios:</td>
                        <td colspan="2" valign="middle" style="vertical-align: middle;text-align: left;">
                            <div style="float: left;">
                                <input name="file" id="loadPicIosFile" type="file"/>
                                <input name="loadPicIos" id="loadPicIos" type="hidden" value="${activity.loadPicIos }"/>

                                <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p3"
                                     class="easyui-progressbar"></div>
                            </div>
                            <c:if test="${not empty activity.loadPicIos}">
                                <div style="float: left;">
                                    <img style="border: 1px solid #CCCCCC;width: 70px;height: 70px;"
                                         src="/restadmin/download${activity.loadPicIos}"/><br>
                                </div>
                            </c:if>
                        </td>
                    </tr>
                    
                    <c:if test="${false }">
                    <tr classType='shoot' >
                        <td style="text-align: right;">素材加载失败图 地址Ios:</td>
                        <td colspan="2" valign="middle" style="vertical-align: middle;text-align: left;">
                            <div style="float: left;">
                                <input name="file" id="loadFailPicIosFile" type="file"/>
                                <input name="loadFailPicIos" id="loadFailPicIos" type="hidden" value="${activity.loadFailPicIos }"/>

                                <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p4"
                                     class="easyui-progressbar"></div>
                            </div>
                            <c:if test="${not empty activity.loadFailPicIos}">
                                <div style="float: left;">
                                    <img style="border: 1px solid #CCCCCC;width: 70px;height: 70px;"
                                         src="/restadmin/download${activity.loadFailPicIos}"/><br>
                                </div>
                            </c:if>
                        </td>
                    </tr>
                    </c:if>
                    
                    <tr classType='shoot' >
                        <td style="text-align: right;">拍摄素材包 地址Ios:</td>
                        <td colspan="2" valign="middle" style="vertical-align: middle;text-align: left;">
                            <div style="float: left;">
                                <input name="file" id="bagIosFile" type="file"/>
                                <input name="bagIos" id="bagIos" type="hidden" value="${activity.bagIos }"/>

                                <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p5"
                                     class="easyui-progressbar"></div>
                            </div>
                            <c:if test="${not empty activity.bagIos}">
                                <div style="float: left;">
                                	<a href="/restadmin/download${activity.bagIos}" >下载</a>
                                </div>
                            </c:if>
                        </td>
                    </tr>
                    
                    <tr>
                        <td style="text-align: right;">活动buttonIos:</td>
                        <td colspan="2" valign="middle" style="vertical-align: middle;text-align: left;">
                            <div style="float: left;">
                                <input name="file" id="buttonIconIosFile" type="file"/>
                                <input name="buttonIconIos" id="buttonIconIos" type="hidden" value="${activity.buttonIconIos }"/>

                                <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p11"
                                     class="easyui-progressbar"></div>
                            </div>
                            <c:if test="${not empty activity.buttonIconIos}">
                                <div style="float: left;">
                                    <img style="border: 1px solid #CCCCCC;width: 70px;height: 70px;"
                                         src="/restadmin/download${activity.buttonIconIos}"/><br>
                                </div>
                            </c:if>
                        </td>
                    </tr>
                    
                    
                    
                    
                    <tr>
                        <td style="text-align: right;">活动大图 地址Android:</td>
                        <td colspan="2" valign="middle" style="vertical-align: middle;text-align: left;">
                            <div style="float: left;">
                                <input name="file" id="actPicAndroidFile" type="file"/>
                                <input name="actPicAndroid" id="actPicAndroid" type="hidden" value="${activity.actPicAndroid }"/>

                                <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p6"
                                     class="easyui-progressbar"></div>
                            </div>
                            <c:if test="${not empty activity.actPicAndroid}">
                                <div style="float: left;">
                                    <img style="border: 1px solid #CCCCCC;width: 70px;height: 70px;"
                                         src="/restadmin/download${activity.actPicAndroid}"/><br>
                                </div>
                            </c:if>
                        </td>
                    </tr>
                    
                    <tr>
                        <td style="text-align: right;">活动缩略图 地址Android:</td>
                        <td colspan="2" valign="middle" style="vertical-align: middle;text-align: left;">
                            <div style="float: left;">
                                <input name="file" id="actIconAndroidFile" type="file"/>
                                <input name="actIconAndroid" id="actIconAndroid" type="hidden" value="${activity.actIconAndroid }"/>

                                <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p7"
                                     class="easyui-progressbar"></div>
                            </div>
                            <c:if test="${not empty activity.actIconAndroid}">
                                <div style="float: left;">
                                    <img style="border: 1px solid #CCCCCC;width: 70px;height: 70px;"
                                         src="/restadmin/download${activity.actIconAndroid}"/><br>
                                </div>
                            </c:if>
                        </td>
                    </tr>
                    
                    <tr classType='shoot' >
                        <td style="text-align: right;">素材加载图 地址Android:</td>
                        <td colspan="2" valign="middle" style="vertical-align: middle;text-align: left;">
                            <div style="float: left;">
                                <input name="file" id="loadPicAndroidFile" type="file"/>
                                <input name="loadPicAndroid" id="loadPicAndroid" type="hidden" value="${activity.loadPicAndroid }"/>

                                <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p8"
                                     class="easyui-progressbar"></div>
                            </div>
                            <c:if test="${not empty activity.loadPicAndroid}">
                                <div style="float: left;">
                                    <img style="border: 1px solid #CCCCCC;width: 70px;height: 70px;"
                                         src="/restadmin/download${activity.loadPicAndroid}"/><br>
                                </div>
                            </c:if>
                        </td>
                    </tr>
                    
                    <c:if test="${false }">
                    <tr classType='shoot' >
                        <td style="text-align: right;">素材加载失败图 地址Android:</td>
                        <td colspan="2" valign="middle" style="vertical-align: middle;text-align: left;">
                            <div style="float: left;">
                                <input name="file" id="loadFailPicAndroidFile" type="file"/>
                                <input name="loadFailPicAndroid" id="loadFailPicAndroid" type="hidden" value="${activity.loadFailPicAndroid }"/>

                                <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p9"
                                     class="easyui-progressbar"></div>
                            </div>
                            <c:if test="${not empty activity.loadFailPicAndroid}">
                                <div style="float: left;">
                                    <img style="border: 1px solid #CCCCCC;width: 70px;height: 70px;"
                                         src="/restadmin/download${activity.loadFailPicAndroid}"/><br>
                                </div>
                            </c:if>
                        </td>
                    </tr>
                    </c:if>
                    
                    <tr classType='shoot' >
                        <td style="text-align: right;">拍摄素材包 地址Android:</td>
                        <td colspan="2" valign="middle" style="vertical-align: middle;text-align: left;">
                            <div style="float: left;">
                                <input name="file" id="bagAndroidFile" type="file"/>
                                <input name="bagAndroid" id="bagAndroid" type="hidden" value="${activity.bagAndroid }"/>

                                <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p10"
                                     class="easyui-progressbar"></div>
                            </div>
                            <c:if test="${not empty activity.bagAndroid}">
                                <div style="float: left;">
                                <a href="/restadmin/download${activity.bagAndroid}" >下载</a>
                                </div>
                            </c:if>
                        </td>
                    </tr>
                    
                    <tr>
                        <td style="text-align: right;">活动buttonAndroid:</td>
                        <td colspan="2" valign="middle" style="vertical-align: middle;text-align: left;">
                            <div style="float: left;">
                                <input name="file" id="buttonIconAndroidFile" type="file"/>
                                <input name="buttonIconAndroid" id="buttonIconAndroid" type="hidden" value="${activity.buttonIconAndroid }"/>

                                <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p12"
                                     class="easyui-progressbar"></div>
                            </div>
                            <c:if test="${not empty activity.buttonIconAndroid}">
                                <div style="float: left;">
                                    <img style="border: 1px solid #CCCCCC;width: 70px;height: 70px;"
                                         src="/restadmin/download${activity.buttonIconAndroid}"/><br>
                                </div>
                            </c:if>
                        </td>
                    </tr>
                    
                    
                    
                    
                    
                    
                    

                    <tr>
                        <td colspan="2" align="center">
                            <a href="javascript:void(0)" style="" class="easyui-linkbutton"
                               data-options="iconCls:'icon-ok'"
                               onclick="pageSub()">保存</a>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <a href="javascript:void(0)" style="" class="easyui-linkbutton"
                               data-options="iconCls:'icon-cancel'" onclick="javascript:history.back();">取消</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
    
    <!-- 弹出的选择活动对话框 -->
	<div id="ca_dlg" class="easyui-dialog" style="width:390px;height:490px;padding:2px 5px" closed="true" buttons="#vdlg-buttons">
	    <table id="activityTable" data-options="border:false,toolbar:'#tb_label'">
	    </table>
	    <div id="tb_label">
	        <form id="searchForm1"  >
	            <table>
	                <tr>
	                    <td><label>活动名称：</label></td>
	                    <td>
	                        <input type="text" name="keyword">
	                    </td>
	                    <td>
	                        <a href="javascript:;" onclick="activityListSearch()" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
	                    </td>
	                </tr>
	            </table>
	        </form>
	    </div>
	</div>
	<div id="vdlg-buttons">
	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectActivity();">确定</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
	       onclick="javascript:$('#ca_dlg').dialog('close')">取消</a>
	</div>
	
	<!-- 弹出的选择片头对话框 -->
	<div id="template_dlg" class="easyui-dialog" style="width:390px;height:490px;padding:2px 5px" closed="true" buttons="#t-buttons">
	    <table id="templateTable" data-options="border:false,toolbar:'#tb_template'">
	    </table>
	    <div id="tb_template">
	    	<form id="searchForm2"  >
	            <input type="hidden" id="templateType2" name="type" />
	        </form>
	    </div>
	</div>
	<div id="t-buttons">
	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectTemplate();">确定</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
	       onclick="javascript:$('#template_dlg').dialog('close')">取消</a>
	</div>
</body>
</html>