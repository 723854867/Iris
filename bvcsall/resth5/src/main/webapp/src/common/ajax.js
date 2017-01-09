define(['../libs/jquery-2.1.4','../common/aler','../common/info'],
    function(jquery, alert, info) {

    var sendings = {};

    return {
        getAccessToken: function() {
    		var access_token = info.getCookie('access_token');
            return access_token || "";
        },

        getCurrentUid: function() {
    		var uid = info.getCookie('uid');
            return uid || "0";
        },
        
        _onced: false,
        once: function() {
            this._onced = true;
            return this;
        },

        _quieted: false,
        quiet: function() {
            this._quieted = true;
            return this;
        },

        postJSON: function(url, params) {
            var dtd = $.Deferred();

            if (this._onced && sendings[url]) {
                this._onced = false;
                return dtd;
            }

            var quited = this._quieted;

            if (this._quieted) {
                this._quieted = false;
            }

            params || (params = {}); //参数
            params.access_token = this.getAccessToken();
            sendings[url] = true;
            $.ajaxSetup({
            	beforeSend: function (request) {
                	$('#btnMore').html('<img width="30" height="30" style="display:block;margin:5px auto;padding:10px 0;" src="'+ info.getRequestPrefix()+'/img/icons-2x/loading.gif" />');
                	$('#btnMore').removeClass('loadMore');
                	//$('#btnMore').hide();
                },
                complete:function(){
                	$('#btnMore').show();
                	setTimeout(function(){
	            		$('#btnMore').html('加载更多');
	            		$('#btnMore').addClass('loadMore');
	            	},1000)
                }
            	});
            $.post(url, params, 'json').done(
                function(json) {
            	if(json.result==''){
            		$('#btnMore').hide();
            	}else{
            		$('#btnMore').show();
            	}
            	$('#btnMore').show();
                delete sendings[url];
                var message = '系统繁忙，请稍后再试。';
                if (!json || json.code != '200' && (message = json.message)) {
                    if (!quited) {
						alert.alertMsg(message);
                    }
                    dtd.reject(json);
                } else {
                    dtd.resolve(json);
                }
            }).fail(function() {
                if (!quited) {
                    alert.alertMsg('系统繁忙，稍后重试。');

                }
                delete sendings[url];
                dtd.reject({});
            });

            return dtd;
        },
        
        postJSONWithBeforeSend: function(url, params) {
            var dtd = $.Deferred();

            if (this._onced && sendings[url]) {
                this._onced = false;
                return dtd;
            }

            var quited = this._quieted;

            if (this._quieted) {
                this._quieted = false;
            }

            params || (params = {}); //参数
            params.access_token = this.getAccessToken();
            var currentUid = this.getCurrentUid();
            sendings[url] = true;
            $.ajax({
                type: "post",
                data: params,
                dataType: "json",
                url: url,
                beforeSend: function (request) {
                	request.setRequestHeader("uid", currentUid);
                	$('#btnMore').html('<img width="30" style="display:block;margin:0 auto;padding:5px 0;" src="'+ info.getRequestPrefix()+'/img/icons-2x/loading.gif" />');
                	$('#btnMore').removeClass('loadMore');
                	//$('#btnMore').hide();
                }
            }).done(
	            function(json) {
	            	
//	            	if(json.result.length<20){
//	            		$('#btnMore').hide();
//	            	}else{
//	            		$('#btnMore').show();
//	            	}
	            	setTimeout(function(){
	            		$('#btnMore').html('加载更多');
	            		$('#btnMore').addClass('loadMore');
	            		$('#btnMore').show();
	            	},1000)
	                delete sendings[url];
	                var message = '系统繁忙，请稍后再试。';
	                if (!json || json.code != '200' && (message = json.message)) {
	                    if (!quited) {
	                    }
	                    dtd.reject(json);
	                } else {
	                    dtd.resolve(json);
	                }
	            }).fail(function() {
	                if (!quited) {
	                }
	                delete sendings[url];
	                dtd.reject({});
	            });
            return dtd;
        },
        postJSONWithBeforeSendNew: function(url, params) {
            var dtd = $.Deferred();

            if (this._onced && sendings[url]) {
                this._onced = false;
                return dtd;
            }

            var quited = this._quieted;

            if (this._quieted) {
                this._quieted = false;
            }

            params || (params = {}); //参数
            params.access_token = this.getAccessToken();
            var currentUid = this.getCurrentUid();
            sendings[url] = true;
            $.ajax({
                type: "post",
                data: params,
                dataType: "json",
                url: url,
                beforeSend: function (request) {
                	request.setRequestHeader("uid", currentUid);
                }
            }).done(
	            function(json) {
	            	setTimeout(function(){
	            		$('#btnMore').html('加载更多');
	            		$('#btnMore').addClass('loadMore');
	            	},1000)
	                delete sendings[url];
	                var message = '系统繁忙，请稍后再试。';
	                if (!json || json.code != '200' && (message = json.message)) {
	                    if (!quited) {
	                    }
	                    dtd.reject(json);
	                } else {
	                    dtd.resolve(json);
	                }
	            }).fail(function() {
	                if (!quited) {
	                }
	                delete sendings[url];
	                dtd.reject({});
	            });
            return dtd;
        },
        postJSONWithBeforeSendAtten: function(url, params) {
            var dtd = $.Deferred();

            if (this._onced && sendings[url]) {
                this._onced = false;
                return dtd;
            }

            var quited = this._quieted;

            if (this._quieted) {
                this._quieted = false;
            }

            params || (params = {}); //参数
            params.access_token = this.getAccessToken();
            var currentUid = this.getCurrentUid();
            sendings[url] = true;
            $.ajax({
                type: "post",
                data: params,
                dataType: "json",
                url: url,
                beforeSend: function (request) {
                	request.setRequestHeader("uid", currentUid);
                	$('#btnMore').html('<img width="30" style="display:block;margin:0 auto;padding:5px 0;" src="'+ info.getRequestPrefix()+'/img/icons-2x/loading.gif" />');
                	$('#btnMore').removeClass('loadMore');
                	//$('#btnMore').hide();
                }
            }).done(
	            function(json) {
	            	$('#btnMore').show();
	            	if(json.result.length<10){
	            		$('#btnMore').hide();
	            	}else{
	            		$('#btnMore').show();
	            	}
	            	setTimeout(function(){
	            		$('#btnMore').html('加载更多');
	            		$('#btnMore').addClass('loadMore');
	            	},1000)
	                delete sendings[url];
	                var message = '系统繁忙，请稍后再试。';
	                if (!json || json.code != '200' && (message = json.message)) {
	                    if (!quited) {
	                    }
	                    dtd.reject(json);
	                } else {
	                    dtd.resolve(json);
	                }
	            }).fail(function() {
	                if (!quited) {
	                }
	                delete sendings[url];
	                dtd.reject({});
	            });
            return dtd;
        },
        postJSONWithBeforeSendHit: function(url, params) {
            var dtd = $.Deferred();

            if (this._onced && sendings[url]) {
                this._onced = false;
                return dtd;
            }

            var quited = this._quieted;

            if (this._quieted) {
                this._quieted = false;
            }

            params || (params = {}); //参数
            params.access_token = this.getAccessToken();
            var currentUid = this.getCurrentUid();
            sendings[url] = true;
            $.ajax({
                type: "post",
                data: params,
                dataType: "json",
                url: url,
                beforeSend: function (request) {
                	request.setRequestHeader("uid", currentUid);
                	$('#btnMore').html('<img width="30" style="display:block;margin:0 auto;padding:5px 0;" src="'+ info.getRequestPrefix()+'/img/icons-2x/loading.gif" />');
                	$('#btnMore').removeClass('loadMore');
                	$('#btnMore').hide();
                }
            }).done(
	            function(json) {
	            	$('#btnMore').show();
	            	if(json.result.length<5){
	            		$('#btnMore').hide();
	            	}else{
	            		$('#btnMore').show();
	            	}
	            	setTimeout(function(){
	            		$('#btnMore').html('加载更多');
	            		$('#btnMore').addClass('loadMore');
	            	},1000)
	                delete sendings[url];
	                var message = '系统繁忙，请稍后再试。';
	                if (!json || json.code != '200' && (message = json.message)) {
	                    if (!quited) {
	                    }
	                    dtd.reject(json);
	                } else {
	                    dtd.resolve(json);
	                }
	            }).fail(function() {
	                if (!quited) {
	                }
	                delete sendings[url];
	                dtd.reject({});
	            });
            return dtd;
        },
        postJSONWithBeforeSendPraise: function(url, params) {
            var dtd = $.Deferred();

            if (this._onced && sendings[url]) {
                this._onced = false;
                return dtd;
            }

            var quited = this._quieted;

            if (this._quieted) {
                this._quieted = false;
            }

            params || (params = {}); //参数
            params.access_token = this.getAccessToken();
            var currentUid = this.getCurrentUid();
            sendings[url] = true;
            $.ajax({
                type: "post",
                data: params,
                dataType: "json",
                url: url,
                beforeSend: function (request) {
                	request.setRequestHeader("uid", currentUid);
                	$('#btnMore').html('<img width="30" style="display:block;margin:0 auto;padding:5px 0;" src="'+ info.getRequestPrefix()+'/img/icons-2x/loading.gif" />');
                	$('#btnMore').removeClass('loadMore');
                	//$('#btnMore').hide();
                }
            }).done(
	            function(json) {
	            	$('#btnMore').show();
	            	setTimeout(function(){
	            		$('#btnMore').html('加载更多');
	            		$('#btnMore').addClass('loadMore');
	            	},1000)
	                delete sendings[url];
	                var message = '系统繁忙，请稍后再试。';
	                if (!json || json.code != '200' && (message = json.message)) {
	                    if (!quited) {
	                    }
	                    dtd.reject(json);
	                } else {
	                    dtd.resolve(json);
	                }
	            }).fail(function() {
	                if (!quited) {
	                }
	                delete sendings[url];
	                dtd.reject({});
	            });
            return dtd;
        }
    };
});
