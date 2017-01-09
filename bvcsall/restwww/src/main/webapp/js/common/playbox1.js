define(['../libs/jquery-2.1.4','../common/browse'], function(jquery,browse) {
	
	var playBox = {
		play: function(url,obj,userInfo){
			//console.log($('#videoTag').length)
			if($('#videoTag').length>0)
			{
				$('#videoTag').siblings('*').show();
				$('#videoTag').remove();//alert(43)
				
			}
			this.media = $('<video id="videoTag" controls src="" style="max-height:300px;" width="100%" x-webkit-airplay="" webkit-playsinline="" autoplay="autoplay"></video>')
			
			//this.media = $('#videoTag');
			
			oparent = obj.closest('.playBox'); // 最大的盒子
			oSpan = obj.parent();

			oparent.append(this.media);
			oSpan.hide();
			//console.log(oparent)
			this.media.attr('src',url);
			this.media.show();
			this.media.play();
			this.media.click(function(){

			})
			playBox.addEvent('ended',this.media,function(){
				playBox.playEnd(oSpan);
			});
			// playBox.addEvent('pause',this.media,function(){
			// 	playBox.playEnd(oSpan)
			// });
		},
		addEvent: function(e,obj,callBack){
			obj.on(e,obj,callBack)
		},
		playEnd: function(obj){
			this.media.hide();
			obj.show();
			obj.show();
			//console.log(this.parentNode);
		},
		playPause: function(obj){
			this.media.hide();
			obj.show();
			obj.show();
			//console.log(this.parentNode);
		}

		
	};
	
	return playBox;

})











