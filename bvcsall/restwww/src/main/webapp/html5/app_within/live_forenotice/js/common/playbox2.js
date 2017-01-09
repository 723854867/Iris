define(['../libs/jquery-2.1.4','../common/browse'], function(jquery,browse) {
	var isplay = false;
	var isHas = 0;
	var newurl;
	var timer;
	var playBox = {
		playV: function(url,obj,userInfo){
			if(obj.parent().find('video').length==0){
				$('.videoPic').show();
				$('.canPlay').show();
				$('video').remove();
				obj = obj[0];

				isHas = 1;
				this.media = document.createElement('video');
				
				this.media.setAttribute('x-webkit-airplay','');
				this.media.setAttribute('webkit-playsinline','controls');

				if(browse.versions.ios){
					this.media.style.height =  window.screen.width+'px';
				}else{
					this.media.style.height = obj.parentNode.offsetHeight + 'px';
					//this.media.style.maxHeight = obj.parentNode.offsetHeight + 'px';

				}

				this.oparent = obj.parentNode;
				this.oparent.appendChild(this.media);
				this.media.setAttribute('src',url);
				this.media.style.display='block';


				this.videoPic = this.oparent.getElementsByTagName('img')[0];
				this.videoBtn = this.oparent.getElementsByTagName('em')[0];
				this.videoPic.style.display = 'none';
				this.videoBtn.style.display = 'none';
				

				playBox.addEvent('ended',this.media,function(){
					playBox.playEnd(this);
				});
				playBox.addEvent('pause',this.media,function(){

				});
				
				this.videoBtn.addEventListener('click',function(){
					playBox.media.play();
					//console.log("videobtn click");
					playBox.oparent.getElementsByTagName('em')[0].style.display = 'none';
				})
				this.media.addEventListener('click',function(){
					playBox.playPause()
				})

				this.media.play();
				playBox.timerFn();
				event.preventDefault();
			}
		},
		timerFn:function(){
			var video=this.media;
			video.addEventListener("loadedmetadata", function(){
				var duration = video.duration;
				var i=0;
				 timer = setInterval(function() {
					  var end = playBox.getEnd(video),currentTime = video.currentTime;
				        console.log(parseInt((end/duration)*100)+'%');
				        console.log(end+'<br/>'+currentTime+'<br>'+video.duration);
				        if(browse.versions.isWeiBo){
				        	playBox.addEvent('webkitendfullscreen',video,function(){
				        		video.pause();
				        		$('.canPlay').show()
							  });
				        }else{
				        	 if(end<duration/4) {
								$('video').parent().find('.bgBox').show();
							    $('video').parent().find('.loadingGif').show();video.pause();
							    $('.loadingGif').text(parseInt((end/duration)*100)+'%');
							  }else if(video.ended){
								  clearInterval(timer);
							  }else{
								  $('video').parent().find('.bgBox').hide();
								  $('video').parent().find('.loadingGif').hide();video.play();
							  }
				        }
					}, 1000)
			})
			
		},
		addEvent: function(e,obj,callBack){
			obj.addEventListener(e,callBack)
		},
		removeEvent: function(e,obj,callBack){
			obj.removeEventListerner(e,callBack)
		},
		playEnd: function(self){
			isHas = 0;
			playBox.oparent.getElementsByTagName('em')[0].style.display = 'block';
			playBox.oparent.getElementsByTagName('img')[0].style.display = 'block';
			playBox.oparent.removeChild(playBox.media);
		},
		playPause: function(){
			if(this.media.paused){

				isHas = 2;
				this.media.play();
				this.media.parentNode.getElementsByTagName('em')[0].style.display = 'none';
				playBox.timerFn();
			}else{

				this.media.pause();
				this.media.parentNode.getElementsByTagName('em')[0].style.display = 'block';
				clearInterval(timer);
			}
		},
		getEnd:function(video){
			var end = 0;
			  try {
			    end = video.buffered.end(0) || 0;
			    //end = parseInt(end * 1000 + 1) / 1000;
			  } catch(e) {
			  }
			  return end;
		}
	};
	return playBox;
})