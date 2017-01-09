define(['../libs/jquery-2.1.4','../common/browse'], function(jquery,browse) {
	var isplay = false;
	var isHas = 0;
	var newurl;
	var playBox = {
		playV: function(url,obj,userInfo){
			
			if(newurl != '' && newurl != url){
				isHas = 0;
				if($('video').length > 0)
				{
					$('video').siblings('*').show();
					$('video').attr('src','');
					$('video').remove();
				}
			}	
			newurl = url;
			obj = obj[0];
			if(isHas == 0)
			{	
				console.log(url)
				isHas = 1;
				this.media = document.createElement('video');
				// this.media.setAttribute('controls','controls');
				this.media.setAttribute('x-webkit-airplay','');
				this.media.setAttribute('webkit-playsinline','controls');
				this.media.style.height =  window.screen.width+'px';
				//this.media.setAttribute('autoplay','true'); //自动播放
				this.media.setAttribute('src',url);
				this.media.style.display='block';
				this.media.play();

				oparent = obj.parentNode;
				
				this.videoPic = oparent.getElementsByTagName('img')[0];
				this.videoBtn = oparent.getElementsByTagName('em')[0];
				this.videoPic.style.display = 'none';
				this.videoBtn.style.display = 'none';
				oparent.appendChild(this.media);

				var self = this;
				playBox.addEvent('ended',this.media,function(){
					playBox.playEnd(this); 
				});
				playBox.addEvent('pause',this.media,function(){
					//playBox.playBtn();
				});

				this.videoBtn.addEventListener('click',function(){
					self.media.play();
					self.media.parentNode.getElementsByTagName('em')[0].style.display = 'none';
				})
				this.media.addEventListener('click',function(){
					playBox.playPause();
				})
			}
		},
		addEvent: function(e,obj,callBack){
			obj.addEventListener(e,callBack)
		},
		removeEvent: function(e,obj,callBack){
			obj.removeEventListerner(e,callBack)
		},
		playEnd: function(self){
			isHas = 0;
			self.parentNode.getElementsByTagName('em')[0].style.display = 'block';
			self.parentNode.getElementsByTagName('img')[0].style.display = 'block';
			self.parentNode.removeChild(self)
		},
		playPause: function(){
			if(self.media.paused){
				//console.log('play')
				isHas = 2;
				self.media.play();
				self.media.parentNode.getElementsByTagName('em')[0].style.display = 'none';
			}else{
				//console.log('stop')
				self.media.pause();
				self.media.parentNode.getElementsByTagName('em')[0].style.display = 'block';
			}
		}
	};
	return playBox;
})









