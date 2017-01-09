// QQ表情插件 //已经不是原版。该版本是经过定制的，为了满足 多textarea并同时hack拖拽排序
(function($){  
	var faceList = {
			"101":"阿尔帕",
			"102":"宾得",
			"103":"富士",
			"104":"华联丁林哈夫",
			"105":"佳能",
			"106":"卡西欧",
			"107":"莱卡",
			"108":"禄莱",
			"109":"尼康",
			"110":"索尼",
			"111":"仙娜",
			"112":"镜头",
			"113":"相机1",
			"114":"相机2",
			"115":"相机3",
			"116":"相机4",
			"1":"OK",
			"2":"爱情",
			"3":"拜拜",
			"4":"鄙视",
			"5":"闭嘴",
			"6":"不高兴",
			"7":"馋",
			"8":"吃惊",
			"9":"呲牙",
			"10":"打哈欠",
			"11":"呆",
			"12":"飞吻哟",
			"13":"奋斗",
			"14":"尴尬",
			"15":"鼓掌",
			"16":"害羞",
			"17":"坏笑",
			"18":"惊恐",
			"19":"开心",
			"20":"可爱",
			"21":"抠鼻子",
			"22":"哭",
			"23":"骷髅",
			"24":"酷",
			"25":"困",
			"26":"雷啊",
			"27":"流汗",
			"28":"卖萌",
			"29":"么么哒",
			"30":"撇嘴",
			"31":"潜水",
			"32":"糗大",
			"33":"色",
			"34":"伤心",
			"35":"生病",
			"36":"生气",
			"37":"衰",
			"38":"偷笑",
			"39":"吐",
			"40":"吐舌",
			"41":"委屈",
			"42":"嘘",
			"43":"要哭了",
			"44":"疑问",
			"45":"阴险",
			"46":"右哼哼",
			"47":"晕了",
			"48":"砸",
			"49":"猪",
			"50":"抓狂",
			"51":"左哼哼",
			"52":"NO",
			"53":"爱你",
			"54":"抱抱",
			"55":"抱拳",
			"56":"彩虹",
			"57":"橙子",
			"58":"蛋糕",
			"59":"刀子",
			"60":"凋谢",
			"61":"发财",
			"62":"福利袋",
			"63":"购物袋",
			"64":"好弱啊",
			"65":"好样的",
			"66":"红唇",
			"67":"话筒",
			"68":"煎饼人",
			"69":"咖啡",
			"70":"蜡烛",
			"71":"篮球",
			"72":"礼物盒",
			"73":"米饭",
			"74":"炮",
			"75":"啤酒",
			"76":"乒乓",
			"77":"苹果",
			"78":"汽车",
			"79":"拳头",
			"80":"闪电",
			"81":"圣诞树",
			"82":"胜利",
			"83":"太差劲",
			"84":"太阳",
			"85":"糖果",
			"86":"握手",
			"87":"西瓜",
			"88":"囍",
			"89":"鲜花",
			"90":"香蕉",
			"91":"翔",
			"92":"心",
			"93":"心碎",
			"94":"信",
			"95":"药",
			"96":"雨伞",
			"97":"月亮",
			"98":"云",
			"99":"炸弹",
			"100":"足球"
	};
	window.global_index = 1
	$.fn.qqFace = function(options){
		var defaults = {
			id : 'facebox',
			path : '/',
			assign : 'content',
			tip : 'face'
		};
		var option = $.extend(defaults, options);
		var assign = $('#'+option.assign);
		var id = option.id;
		var path = option.path;
		var tip = option.tip;
		
		/*if(assign.length<=0){
			alert('缺少表情赋值对象。');
			return false;
		}*/
		
		$(this).click(function(e){
			var index = $(this).attr('data-index')
			var strFace, labFace;
			if($('#txtarea'+index).val() == '150字以内') {
				$('#txtarea'+index).val('')
			}
			if($('#'+id).length<=0){
				strFace = '<div id="'+id+'" style="position:absolute;display:none;z-index:1000;top:40px;left:35px;" class="qqFace">' +
							  '<table border="0" cellspacing="0" cellpadding="0"><tr>';
							  
				for(var i=1; i<=32; i++){
					//labFace = '['+tip+']'+i+'[/face]';
					hz = faceList[i];
					labFace = '[' + hz + ']';
					strFace += '<td><img src="'+path+'Expression_'+i+'@2x.png" id="_imgsrc'+ i +'" onclick="$(\'#'+option.assign+ '\' +\''+ index +'\').setCaret();$(\'#'+option.assign+'\'+\''+ index +'\').insertAtCaret(\'' + labFace + '\');" /></td>';
					if( i % 10 == 0 ) strFace += '</tr><tr>';
				}
				strFace += '</tr></table></div>';
			}
			$(this).parent().append(strFace);
			var offset = $(this).position();
			//var top = offset.top + $(this).outerHeight();
			/*$('#'+id).css('top',top);
			$('#'+id).css('left',offset.left);*/
			$('#' + id).css({top:'-136px,left:54px'});
			$('#'+id).show();
			e.stopPropagation();
		});

		$(document).click(function(){
			$('#'+id).hide();
			$('#'+id).remove();
		});
	};

})(jQuery);

jQuery.extend({ 
unselectContents: function(){ 
	if(window.getSelection) 
		window.getSelection().removeAllRanges(); 
	else if(document.selection) 
		document.selection.empty(); 
	} 
}); 
jQuery.fn.extend({ 
	replace_face : function(str) {
		var fList = {
				"勾引" : 85,
				"恶魔" : 96,
				"吐" : 20,
				"睡" : 9,
				"右哼哼" : 47,
				"蛋糕" : 69,
				"冷饮" : 92,
				"闪电" : 70,
				"篮球" : 59,
				"吓" : 54,
				"调皮" : 13,
				"爱心" : 67,
				"炸弹" : 71,
				"剪刀头" : 93,
				"发呆" : 4,
				"糗大了" : 44,
				"疯了" : 36,
				"发怒" : 12,
				"礼物" : 78,
				"害羞" : 7,
				"骷髅" : 38,
				"两颗心" : 91,
				"太阳" : 77,
				"西瓜" : 57,
				"小鬼" : 94,
				"小黄鸭" : 95,
				"得意" : 5,
				"虚" : 34,
				"小蛋糕" : 98,
				"咒骂" : 32,
				"微笑" : 1,
				"可怜" : 55,
				"爱你" : 88,
				"乒乓" : 60,
				"猪头" : 63,
				"抠鼻" : 42,
				"流泪" : 6,
				"撇嘴" : 2,
				"大足球" : 99,
				"哈气" : 48,
				"难过" : 16,
				"再见" : 40,
				"月亮" : 76,
				"阴险" : 52,
				"拳头" : 86,
				"菜刀" : 56,
				"奋斗" : 31,
				"便便" : 75,
				"闭嘴" : 8,
				"左哼哼" : 46,
				"凋谢" : 65,
				"惊讶" : 15,
				"流汗" : 28,
				"傲慢" : 24,
				"快哭了" : 51,
				"啤酒" : 58,
				"悠闲" : 30,
				"尴尬" : 11,
				"憨笑" : 29,
				"晕" : 35,
				"色" : 3,
				"OK" : 90,
				"瓢虫" : 74,
				"握手" : 82,
				"鄙视" : 49,
				"高跟鞋" : 100,
				"大哭" : 10,
				"足球" : 73,
				"困" : 26,
				"敲打" : 39,
				"胜利" : 83,
				"衰" : 37,
				"惊恐" : 27,
				"亲亲" : 53,
				"饥饿" : 25,
				"偷笑" : 21,
				"差劲" : 87,
				"坏笑" : 45,
				"心碎" : 68,
				"拍手" : 43,
				"玫瑰" : 64,
				"抱拳" : 84,
				"刀" : 72,
				"弱" : 81,
				"委屈" : 50,
				"嘴唇" : 66,
				"呲牙" : 14,
				"擦汗" : 41,
				"拥抱" : 79,
				"愉快" : 22,
				"抓狂" : 19,
				"疑问" : 33,
				"冷汗" : 18,
				"NO" : 89,
				"咖啡" : 61,
				"白眼" : 23,
				"强" : 80,
				"酷" : 17,
				"饭" : 62,
				"星星" :97,
				"赞美" : 101
			};
		if(str) {
			//str = str.replace(/\</g,'&lt;');
			//str = str.replace(/\>/g,'&gt;');
			str = str.replace(/\n/g,'<br/>');
//			debugger
			if(str.indexOf("[face]")>-1){
				str = str.replace(/\[face\]([0-9]*)\[\/face\]/g,"<img style='width:14px;height:14px;margin-top:5px;' src='arclist/face$1.png' />");
			}else{
				str = str.replace(/\[(.*?)\]/g,function($0,$1){
					var num=fList[$1];
					return '<img style="width:18px; height:18px;" src="/restwww/img/arclist/Expression_'+num+'@2x.png"/>';
				});
			}
			$(this).html(str);
		}
		
	},
	selectContents: function(){ 
		$(this).each(function(i){ 
			var node = this; 
			var selection, range, doc, win; 
			if ((doc = node.ownerDocument) && (win = doc.defaultView) && typeof win.getSelection != 'undefined' && (selection = window.getSelection()) && typeof selection.removeAllRanges != 'undefined'){ 
				range = doc.createRange(); 
				range.selectNode(node); 
				if(i == 0){ 
					selection.removeAllRanges(); 
				} 
				selection.addRange(range); 
			} else if (document.body && typeof document.body.createTextRange != 'undefined' && (range = document.body.createTextRange())){ 
				range.moveToElementText(node); 
				range.select(); 
			} 
		}); 
	}, 

	setCaret: function(){ 
		
		if(!(/msie/.test(navigator.userAgent.toLowerCase()))) return; 
		var initSetCaret = function(){ 
			var textObj = $(this).get(0); 
			textObj.caretPos = document.selection.createRange().duplicate(); 
		}; 
		$(this).click(initSetCaret).select(initSetCaret).keyup(initSetCaret); 
		
	}, 

	insertAtCaret: function(textFeildValue){ 
		var textObj = $(this).get(0); 
		
		
		if(document.all && textObj.createTextRange && textObj.caretPos){ 
			var caretPos=textObj.caretPos; 
			caretPos.text = caretPos.text.charAt(caretPos.text.length-1) == '' ? 
			textFeildValue+'' : textFeildValue; 
		} else if(textObj.setSelectionRange){ 
			var rangeStart=textObj.selectionStart; 
			var rangeEnd=textObj.selectionEnd; 
			var tempStr1=textObj.value.substring(0,rangeStart); 
			var tempStr2=textObj.value.substring(rangeEnd); 
			textObj.value=tempStr1+textFeildValue+tempStr2; 
			textObj.focus(); 
			var len=textFeildValue.length; 
			textObj.setSelectionRange(rangeStart+len,rangeStart+len); 
			textObj.blur(); 
		}else{ 
			textObj.value+=textFeildValue; 
			
			
		} 
	} 
});