/**
 * The Patch for jQuery EasyUI 1.3.6
 */
(function($){
	var plugin = $.fn.linkbutton;
	$.fn.linkbutton = function(options, param){
		var result = plugin.call(this, options, param);
		if (typeof options != 'string'){
			this.each(function(){
				$(this).unbind('click.linkbutton').bind('click.linkbutton', function(){
					var opts = $(this).linkbutton('options');
					if (!opts.disabled){
						if (opts.toggle){
							if (opts.selected){
								$(this).linkbutton('unselect');
							} else {
								$(this).linkbutton('select');
							}
						}
						opts.onClick.call(this);
					}
				});
			});
		}
		return result;
	};
	$.fn.linkbutton.methods = plugin.methods;
	$.fn.linkbutton.parseOptions = plugin.parseOptions;
	$.fn.linkbutton.defaults = plugin.defaults;
})(jQuery);

(function($){
	function setMenuSize(target, menu){
		var opts = $.data(target, 'menu').options;
		var style = menu.attr('style') || '';
		menu.css({
			display: 'block',
			left:-10000,
			height: 'auto',
			overflow: 'hidden'
		});
		
		var el = menu[0];
		var width = el.originalWidth || 0;
		if (!width){
			width = 0;
			menu.find('div.menu-text').each(function(){
				if (width < $(this)._outerWidth()){
					width = $(this)._outerWidth();
				}
				$(this).closest('div.menu-item')._outerHeight($(this)._outerHeight()+2);
			});
			width += 40;
		}
		
		width = Math.max(width, opts.minWidth);
		var height = el.originalHeight || menu.outerHeight();
		var lineHeight = Math.max(el.originalHeight, menu.outerHeight()) - 2;
		menu._outerWidth(width)._outerHeight(height);
		menu.children('div.menu-line')._outerHeight(lineHeight);
		
		style += ';width:' + el.style.width + ';height:' + el.style.height;
		
		menu.attr('style', style);
	}
	
	function setVisible(target, itemEl, visible){
		var menu = $(itemEl).parent();
		if (visible){
			$(itemEl).show();
		} else {
			$(itemEl).hide();
		}
		setMenuSize(target, menu);
	}
	
	$.extend($.fn.menu.methods, {
		showItem: function(jq, itemEl){
			return jq.each(function(){
				setVisible(this, itemEl, true);
			});
		},
		hideItem: function(jq, itemEl){
			return jq.each(function(){
				setVisible(this, itemEl, false);
			});
		}
	});
	
	var removeMenuItem = $.fn.menu.methods.removeItem;
	$.fn.menu.methods.removeItem = function(jq, itemEl){
		return jq.each(function(){
			var menu = $(itemEl).parent();
			removeMenuItem.call($.fn.menu.methods, $(this), itemEl);
			setMenuSize(this, menu);
		});
	}

})(jQuery);

(function($){
	$.fn.datagrid.defaults.loader = function(param, success, error){
		var target = this;
		var opts = $(target).datagrid('options');
		if (!opts.url) return false;
		$.ajax({
			type: opts.method,
			url: opts.url,
			data: param,
			dataType: 'json',
			success: function(data){
				var onLoadSuccess = opts.onLoadSuccess;
				opts.onLoadSuccess = function(){};
				success(data);
				opts.onLoadSuccess = onLoadSuccess;
				opts.onLoadSuccess.call(target, data);
			},
			error: function(){
				error.apply(this, arguments);
			}
		});
	};
})(jQuery);

(function($){
	var plugin = $.fn.window;
	$.fn.window = function(options, param){
		var result = plugin.call(this, options, param);
		if (typeof options != 'string'){
			this.each(function(){
				var state = $.data(this, 'window');
				state.window.resizable({
					onStartResize:function(e){
						if (state.pmask){state.pmask.remove();}
						state.pmask = $('<div class="window-proxy-mask"></div>').insertAfter(state.window);
						state.pmask.css({
							zIndex: $.fn.window.defaults.zIndex++,
							left: e.data.left,
							top: e.data.top,
							width: state.window._outerWidth(),
							height: state.window._outerHeight()
						});
						if (state.proxy){state.proxy.remove();}
						state.proxy = $('<div class="window-proxy"></div>').insertAfter(state.window);
						state.proxy.css({
							zIndex: $.fn.window.defaults.zIndex++,
							left: e.data.left,
							top: e.data.top
						});
						state.proxy._outerWidth(e.data.width)._outerHeight(e.data.height);
					}
				});
			});
		}
		return result;
	};
	$.fn.window.methods = plugin.methods;
	$.fn.window.parseOptions = plugin.parseOptions;
	$.fn.window.defaults = plugin.defaults;
})(jQuery);
