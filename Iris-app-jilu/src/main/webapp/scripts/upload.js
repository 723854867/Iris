var policyText = {
    "expiration": "2020-01-01T12:00:00.000Z", //设置该Policy的失效时间，超过这个失效时间之后，就没有办法通过这个policy上传文件了
    "conditions": [
    ["content-length-range", 0, 1048576000] // 设置上传文件的大小限制
    ]
};

accessid= 'LTAIxGJaJ5dToccf';
accesskey= 'SeSwNEp2626vuwvqapar7j6FsT5gg7';
host = 'http://qydj.oss-cn-hangzhou.aliyuncs.com';


var policyBase64 = Base64.encode(JSON.stringify(policyText))
message = policyBase64
var bytes = Crypto.HMAC(Crypto.SHA1, message, accesskey, { asBytes: true }) ;
var signature = Crypto.util.bytesToBase64(bytes);

function set_upload_param(up)
{
	var key = "common/sys/game/"+$("#gameId").val()+"/game.jpg";

    new_multipart_params = {
        'key' : key,
        'policy': policyBase64,
        'OSSAccessKeyId': accessid, 
        'success_action_status' : '200', //让服务端返回200,不然，默认会返回204
        'signature': signature,
    };

    up.setOption({
        'url': host,
        'multipart_params': new_multipart_params
    });

    console.log('reset uploader')
}


var uploader = new plupload.Uploader({
	runtimes : 'html5,flash,silverlight,html4',
	browse_button : 'selectfiles', 
	container: document.getElementById('container'),
	flash_swf_url : 'lib/plupload-2.1.2/js/Moxie.swf',
	silverlight_xap_url : 'lib/plupload-2.1.2/js/Moxie.xap',
    url : host,
	init: {
		PostInit: function() {
			document.getElementById('ossfile').innerHTML = '';
			document.getElementById('postfiles').onclick = function() {
				set_upload_param(uploader);
				uploader.start();
				return false;
			};
		},

		FilesAdded: function(up, files) {
			document.getElementById('ossfile').innerHTML="";//只允许上传一张图片
			plupload.each(files, function(file) {
				document.getElementById('ossfile').innerHTML += '<div id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ')<b></b>'
				+'<div class="progress"><div class="progress-bar progress-bar-striped active" style="width: 0%"></div></div>'
				+'</div>';
			});
		},

		UploadProgress: function(up, file) {
			var d = document.getElementById(file.id);
			d.getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";
            
            var prog = d.getElementsByTagName('div')[0];
			var progBar = prog.getElementsByTagName('div')[0]
			progBar.innerHTML=file.percent+'%';
			progBar.setAttribute('style','width:'+file.percent+'%');
			progBar.setAttribute('aria-valuenow', file.percent);
		},

		FileUploaded: function(up, file, info) {
            //alert(info.status)
            if (info.status >= 200 || info.status < 200)
            {
                document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = 'success';
                
                var gameId= $("#gameId").val();
    			var name = $("#gameName").val();
    			
    			/***图片上传成功以后修改上传图片的状态***/
				$.ajax({
					type: 'POST',
		            url: '/qydj/backstage',
		            data: { 'id': gameId ,'name':name,'gamePhoto':'1', 'action': 'game_edit' },
		            dataType: 'json',
		            beforeSend: function () {
		            	
		            },
		            success: function (json) {
		            	if(json.code=="0"){
		            		window.location.reload();
		            	}
		            	else{
		            		alert(json.desc);
		            	}
		            },
		            error: function () {
		                alert("数据加载失败");
		            }
		        });
            }
            else
            {
                document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = info.response;
            } 
		},

		Error: function(up, err) {
			alert("图片上传失败！");
			//document.getElementById('console').appendChild(document.createTextNode("\nError xml:" + err.response));
		}
	}
});

uploader.init();
