!
function e(t, i, n) {
	function s(o, r) {
		if (!i[o]) {
			if (!t[o]) {
				var _ = "function" == typeof require && require;
				if (!r && _) return _(o, !0);
				if (a) return a(o, !0);
				var p = new Error("Cannot find module '" + o + "'");
				throw p.code = "MODULE_NOT_FOUND", p
			}
			var l = i[o] = {
				exports: {}
			};
			t[o][0].call(l.exports, function(e) {
				var i = t[o][1][e];
				return s(i ? i : e)
			}, l, l.exports, e, t, i, n)
		}
		return i[o].exports
	}
	for (var a = "function" == typeof require && require, o = 0; o < n.length; o++) s(n[o]);
	return s
}({
	1: [function(e, t, i) {
		!
		function(t, i) {
			var n = (e("./tools"), e("./template"), e("./skin_replay")),
				s = e("./skin_liveplay"),
				a = e("./skin_mp4");
			huajiao_video_player = function() {
				function e() {
					t(i.new_video).bind("urlError", function(e) {
						console.log("视频地址有误")
					}), t(i.new_video).bind("backUrlError", function(e) {
						console.log("视频地址有误")
					}), t(i.new_video).bind("connectSuccess", function(e) {
						"replay" == i.vtype ? n.init(i.new_video) : "mp4" == i.vtype ? a.init(i.new_video) : s.init(i.new_video)
					}), t(i.new_video).bind("play", function(e) {
						"replay" == i.vtype ? n.hide_icon() : "mp4" == i.vtype ? a.hide_icon() : (s.hide_icon(), t(".hjPopbox").show())
					}), t(i.new_video).bind("pause", function(e) {
						"replay" == i.vtype ? (n.hide_icon(), n.show_pause()) : "mp4" == i.vtype ? (a.hide_icon(), a.show_pause()) : (s.hide_icon(), s.show_pause())
					}), t(i.new_video).bind("waiting", function(e) {
						"replay" == i.vtype ? (n.hide_icon(), n.show_waiting()) : "mp4" == i.vtype ? (a.hide_icon(), a.show_waiting()) : (s.hide_icon(), s.show_waiting())
					}), t(i.new_video).bind("playing", function(e) {
						"replay" == i.vtype ? (n.set_play(), n.hide_icon()) : "mp4" == i.vtype ? (a.set_play(), a.hide_icon()) : (s.set_play(), s.hide_icon())
					}), t(i.new_video).bind("seeking", function(e) {
						i.is_seek = 1
					}), t(i.new_video).bind("seeked", function(e) {
						setTimeout(function() {
							i.is_seek = 0
						}, 2e3)
					}), t(i.new_video).bind("timeupdate", function(e) {
						i.is_seek || ("replay" == i.vtype ? (n.update_time(), n.set_play()) : "mp4" == i.vtype ? (a.update_time(), a.set_play()) : (s.update_time(), s.set_play())), "replay" == i.vtype && (window.Huajiao && "function" == typeof Huajiao.addReplaypop ? Huajiao.addReplaypop(parseInt(i.new_video.get_attributes("current_time"))) : window.console && console.info("subtitle is not ready"))
					}), t(i.new_video).bind("canplaythrough", function(e) {
						"replay" == i.vtype ? n.set_total_time(i.new_video) : "mp4" == i.vtype ? a.set_total_time(i.new_video) : s.set_total_time(i.new_video)
					}), t(i.new_video).bind("ended", function(e) {
						("replay" == i.vtype || "mp4" == i.vtype) && (window.Huajiao && window.Huajiao.finishPlay && "function" == typeof window.Huajiao.finishPlay ? Huajiao.finishPlay() : window.console && console.info("There is no method finishPlay in namespace of Huajiao"))
					}), t(i.new_video).bind("durationchange", function(e) {
						"replay" == i.vtype ? n.update_total_time(i.new_video) : "mp4" == i.vtype ? a.update_total_time(i.new_video) : s.update_total_time(i.new_video)
					})
				}
				var i = this;
				i._data = {
					bid: "huajiao",
					channel: "live_huajiao",
					sn: "",
					sid: (new Date).getTime() + Math.random(),
					mid: "test",
					vtype: "liveplay",
					controls: !1
				}, i.vtype = null, i.new_video = null, i.is_seek = 0, i.page_msg = null, this.init = function(n, s, a, o) {
					i.vtype = n;
					var r = s.substr(0, 4);
					if ("_LC_" == r && (i._data.channel = "live_huajiao_v2"), "replay" == n ? i._data.vtype = "replay" : "mp4" == n && (i._data.vtype = "mp4"), s ? i._data.sn = s : window.console && console.log("没有视频地址"), o) for (var _ in o) i._data[_] = o[_];
					i.new_video = new H5_video(i._data, a), e(), t(".hjPopbox").hide()
				}, this.play = function() {
					i.new_video.play()
				}
			}
		}(jQuery, window)
	}, {
		"./skin_liveplay": 2,
		"./skin_mp4": 3,
		"./skin_replay": 4,
		"./template": 5,
		"./tools": 6
	}],
	2: [function(e, t, i) {
		t.exports = function() {
			var t, i, n, s, a = e("./tools"),
				o = e("./template"),
				r = $(".video_wrap"),
				_ = function() {
					var e = $(".h5_player_control_btn");
					t.show(), e.addClass("pause"), e.removeClass("play")
				},
				p = function() {
					i.show(), n.hide()
				},
				l = function() {
					var e = $(".h5_player_control_btn");
					e.find(".play").hide(), e.find(".pause").show()
				},
				c = function() {
					n.show()
				},
				d = function() {
					t.hide(), i.hide(), n.hide()
				},
				u = function(e) {
					$(".h5_player_control_btn").on("click", function() {
						s.get_attributes("paused") ? s.play() : s.pause()
					}), n.click(function() {
						s.get_attributes("paused") ? s.play() : s.pause()
					})
				},
				h = function() {
					var e = navigator.userAgent.toLowerCase();
					/iphone|ipad|ipod/.test(e) ? "micromessenger" == e.match(/MicroMessenger/i) ? setTimeout(function() {
						s.get_attributes("current_time") || (d(), n.show(), $(".h5_player_tip_btn").show())
					}, 3e3) : (d(), n.show(), $(".h5_player_tip_btn").show()) : /android/.test(e) && ("micromessenger" == e.match(/MicroMessenger/i) ? $(".h5_player_tip_btn").hide() : (d(), n.show(), $(".h5_player_tip_btn").show()))
				},
				y = function(e) {
					s = e;
					var a = o.liveplay;
					r.append(a), n = $(".h5_player_pause"), t = $(".h5_player_waiting"), i = $(".h5_player_warning"), d(), _(), h(), u()
				},
				v = function() {
					var e = (parseInt(s.get_attributes("duration")), parseInt(s.get_attributes("current_time"))),
						t = a.format_time(e);
					$(".h5_player_control_time .now").html(t)
				},
				f = function() {
					var e = a.format_time(s.get_attributes("duration"));
					e && "NaN:NaN" != e ? $(".h5_player_control_time .total").html(e) : (setTimeout(function() {
						f()
					}, 2e3), $(".h5_player_control_time .total").html("00"))
				},
				w = function() {
					var e = a.format_time(s.get_attributes("duration"));
					e && "NaN:NaN" != e ? $(".h5_player_control_time .total").html(e) : (setTimeout(function() {
						f()
					}, 2e3), $(".h5_player_control_time .total").html("00"))
				};
			return {
				init: y,
				show_pause: c,
				show_waiting: _,
				show_warning: p,
				hide_icon: d,
				set_play: l,
				update_time: v,
				set_total_time: f,
				update_total_time: w
			}
		}()
	}, {
		"./template": 5,
		"./tools": 6
	}],
	3: [function(e, t, i) {
		t.exports = function() {
			var t, i, n, s, a, o = e("./tools"),
				r = e("./template"),
				_ = $(".video_wrap"),
				p = 0,
				l = 0,
				c = 0,
				d = 0,
				u = 0,
				h = 0,
				y = 0,
				v = function(e) {
					$(".process_line").css({
						width: e + 10
					}), $(".process_btn").css({
						left: e
					})
				},
				f = function(e) {
					p = e.touches[0].pageX
				},
				w = function(e) {
					p = e.touches[0].pageX
				},
				m = function(e) {
					var t = $(".process_btn"),
						i = p - parseInt(s.css("left")) - parseInt(t.width()) / 4;
					0 > i ? i = 0 : i > s.width() - t.width() + parseInt(t.width()) / 2 && (i = s.width() - t.width() + parseInt(t.width()) / 2), v(i);
					var n = parseInt(a.get_attributes("duration")),
						r = n * i / parseInt($(".process_bg").width() - t.width() + parseInt(t.width()) / 2);
					a.seek(r), r = o.format_time(r), $(".h5_player_control_time .now").html(r), p = 0
				},
				b = function(e) {
					c = 1
				},
				g = function(e) {
					c = 1;
					var t = $(".process_btn"),
						i = e.touches[0].pageX - parseInt(s.css("left")) - parseInt(t.width()) / 4;
					0 > i ? i = 0 : i > s.width() - t.width() + parseInt(t.width()) / 2 && (i = s.width() - t.width() + parseInt(t.width()) / 2), l = i, $(".h5_player_process_forward_wrap").show(), $(".h5_player_process_forward").show();
					var n = parseInt(a.get_attributes("duration")),
						r = n * i / parseInt($(".process_bg").width());
					$(".h5_player_process_forward .time").html(o.format_time(r)), v(i), q()
				},
				k = function(e) {
					$(".h5_player_process_forward_wrap").hide(), $(".h5_player_process_forward").hide();
					var t = parseInt(a.get_attributes("duration")),
						i = t * l / parseInt($(".process_bg").width());
					a.seek(i), c = 0, a.get_attributes("paused") ? a.play() : a.pause()
				},
				I = function() {
					if (!c) {
						$(".h5_player_control_btn");
						t.show()
					}
				},
				x = function() {
					i.show()
				},
				N = function() {
					if (!c) {
						var e = $(".h5_player_control_btn");
						e.find(".play").hide(), e.find(".pause").show()
					}
				},
				L = function() {
					if (!c) {
						var e = $(".h5_player_control_btn");
						e.find(".play").show(), e.find(".pause").hide(), H(), n.show()
					}
				},
				q = function() {
					t.hide(), i.hide(), n.hide()
				},
				E = function(e) {
					d = e.touches[0].pageX, u = e.touches[0].pageY
				},
				M = function(e) {
					h = e.touches[0].pageX, y = e.touches[0].pageY
				},
				j = function() {
					(Math.abs(Math.abs(y) - Math.abs(u)) <= 5 || 0 == y) && (a.get_attributes("paused") ? (a.play(), N()) : (a.pause(), L())), d = 0, u = 0, h = 0, y = 0
				},
				S = function(e) {
					var t = $(".h5_player"),
						i = $(".h5_player_tip_btn");
					$(".process_btn"), $(".h5_player_process_bar");
					t.bind("touchstart", E), t.bind("touchmove", M), t.bind("touchend", j), i.bind("touchstart", E), i.bind("touchmove", M), i.bind("touchend", j), document.querySelector(".h5_player_control_btn").addEventListener("touchend", function() {
						a.get_attributes("paused") ? a.play() : a.pause()
					}), document.querySelector(".process_btn").addEventListener("touchstart", b), document.querySelector(".process_btn").addEventListener("touchmove", g), document.querySelector(".process_btn").addEventListener("touchend", k), document.querySelector(".h5_player_process_bar").addEventListener("touchstart", f), document.querySelector(".h5_player_process_bar").addEventListener("touchmove", w), document.querySelector(".h5_player_process_bar").addEventListener("touchend", m)
				},
				T = function() {
					var e = parseInt(a.get_attributes("duration")),
						t = parseInt(a.get_attributes("current_time")),
						i = $(".process_btn"),
						n = parseInt($(".process_bg").width() - i.width() + 12) * t / e;
					v(n)
				},
				H = function() {
					var e = navigator.userAgent.toLowerCase(),
						t = navigator.userAgent.match(/MicroMessenger\/([\d\.]+)/i);
					"micromessenger" == e.match(/MicroMessenger/i) && -1 != t.indexOf("6.2.3") ? $(".h5_player_tip_btn").hide() : $(".h5_player_tip_btn").show()
				},
				P = function(e) {
					a = e;
					var s = r.mp4;
					_.append(s), n = $(".h5_player_pause"), t = $(".h5_player_waiting"), i = $(".h5_player_warning"), a.get_attributes("auto_play") ? N() : L(), X(), S()
				},
				X = function() {
					$("body").append(r.svg_template);
					var e = $(window).width(),
						t = $(".h5_player_process_bar");
					t.css({
						width: e - 110,
						left: 20
					}), t.find(".process_bg").css("width", e - 110), t.find(".process_line").css("width", 0), s = $(".h5_player_process_bar")
				},
				C = function() {
					if (c) return !1;
					var e = parseInt(a.get_attributes("duration")),
						t = parseInt(a.get_attributes("current_time")),
						i = o.format_time(t);
					if ($(".h5_player_control_time .now").html(i), T(a), isNaN(e)) return !1;
					var n = e - t;
					$(".h5_player_replay_logo .time").html(o.format_time(n))
				},
				A = function() {
					var e = o.format_time(a.get_attributes("duration"));
					e && "NaN:NaN" != e ? ($(".h5_player_control_time .total").html(e), $(".h5_player_replay_logo .time").html(e)) : (setTimeout(function() {
						A()
					}, 2e3), $(".h5_player_control_time .total").html("00"))
				},
				O = function() {
					var e = o.format_time(a.get_attributes("duration"));
					e && "NaN:NaN" != e ? $(".h5_player_control_time .total").html(e) : (setTimeout(function() {
						A()
					}, 2e3), $(".h5_player_control_time .total").html("00"))
				};
			return {
				init: P,
				show_pause: L,
				show_waiting: I,
				show_warning: x,
				hide_icon: q,
				set_play: N,
				update_time: C,
				set_total_time: A,
				update_total_time: O
			}
		}()
	}, {
		"./template": 5,
		"./tools": 6
	}],
	4: [function(e, t, i) {
		t.exports = function() {
			var t, i, n, s, a, o = e("./tools"),
				r = e("./template"),
				_ = $(".video_wrap"),
				p = 0,
				l = 0,
				c = 0,
				d = function(e) {
					$(".process_line").css({
						width: e + 10
					}), $(".process_btn").css({
						left: e
					})
				},
				u = function(e) {
					p = e.touches[0].pageX
				},
				h = function(e) {
					p = e.touches[0].pageX
				},
				y = function(e) {
					var t = $(".process_btn"),
						i = p - parseInt(s.css("left")) - parseInt(t.width()) / 4;
					0 > i ? i = 0 : i > s.width() - t.width() + parseInt(t.width()) / 2 && (i = s.width() - t.width() + parseInt(t.width()) / 2), d(i);
					var n = parseInt(a.get_attributes("duration")),
						r = n * i / parseInt($(".process_bg").width() - t.width() + parseInt(t.width()) / 2);
					a.seek(r), r = o.format_time(r), $(".h5_player_control_time .now").html(r), p = 0
				},
				v = function(e) {
					e.stopPropagation(), c = 1
				},
				f = function(e) {
					e.stopPropagation(), c = 1;
					var t = $(".process_btn"),
						i = e.touches[0].pageX - parseInt(s.css("left")) - parseInt(t.width()) / 4;
					0 > i ? i = 0 : i > s.width() - t.width() + parseInt(t.width()) / 2 && (i = s.width() - t.width() + parseInt(t.width()) / 2), l = i, $(".h5_player_process_forward_wrap").show(), $(".h5_player_process_forward").show();
					var n = parseInt(a.get_attributes("duration")),
						r = n * i / parseInt($(".process_bg").width());
					$(".h5_player_process_forward .time").html(o.format_time(r)), d(i), I()
				},
				w = function(e) {
					e.stopPropagation(), c = 0, a.get_attributes("paused") ? k() : g(), $(".h5_player_process_forward_wrap").hide(), $(".h5_player_process_forward").hide();
					var t = parseInt(a.get_attributes("duration")),
						i = t * l / parseInt($(".process_bg").width());
					a.seek(i)
				},
				m = function() {
					if (!c) {
						$(".h5_player_control_btn");
						t.show()
					}
				},
				b = function() {
					i.show()
				},
				g = function() {
					if (!c) {
						var e = $(".h5_player_control_btn");
						e.find(".play").hide(), e.find(".pause").show()
					}
				},
				k = function() {
					if (!c) {
						var e = $(".h5_player_control_btn");
						e.find(".play").show(), e.find(".pause").hide(), L(), n.show()
					}
				},
				I = function() {
					t.hide(), i.hide(), n.hide()
				},
				x = function(e) {
					$(".h5_player"), $(".h5_player_tip_btn"), $(".process_btn"), $(".h5_player_process_bar");
					n.click(function() {
						a.get_attributes("paused") ? a.play() : a.pause()
					}), document.querySelector(".h5_player_control_btn").addEventListener("touchend", function() {
						a.get_attributes("paused") ? a.play() : a.pause()
					}), document.querySelector(".process_btn").addEventListener("touchstart", v), document.querySelector(".process_btn").addEventListener("touchmove", f), document.querySelector(".process_btn").addEventListener("touchend", w), document.querySelector(".h5_player_process_bar").addEventListener("touchstart", u), document.querySelector(".h5_player_process_bar").addEventListener("touchmove", h), document.querySelector(".h5_player_process_bar").addEventListener("touchend", y)
				},
				N = function() {
					var e = parseInt(a.get_attributes("duration")),
						t = parseInt(a.get_attributes("current_time")),
						i = $(".process_btn"),
						n = parseInt($(".process_bg").width() - i.width() + 12) * t / e;
					d(n)
				},
				L = function() {
					var e = navigator.userAgent.toLowerCase();
					/iphone|ipad|ipod/.test(e) ? "micromessenger" == e.match(/MicroMessenger/i) ? $(".h5_player_tip_btn").show() : "weibo" == e.match(/weibo/i) ? $(".h5_player_tip_btn").show() : $(".h5_player_tip_btn").hide() : /android/.test(e) && ("micromessenger" == e.match(/MicroMessenger/i) ? $(".h5_player_tip_btn").hide() : $(".h5_player_tip_btn").show())
				},
				q = function(e) {
					a = e;
					var s = r.replay;
					_.append(s), n = $(".h5_player_pause"), t = $(".h5_player_waiting"), i = $(".h5_player_warning"), k(), E(), x()
				},
				E = function() {
					$("body").append(r.svg_template);
					var e = $(window).width(),
						t = $(".h5_player_process_bar");
					t.css({
						width: e - 110,
						left: 20
					}), t.find(".process_bg").css("width", e - 110), t.find(".process_line").css("width", 0), s = $(".h5_player_process_bar")
				},
				M = function() {
					if (c) return !1;
					var e = parseInt(a.get_attributes("duration")),
						t = parseInt(a.get_attributes("current_time")),
						i = o.format_time(t);
					if ($(".h5_player_control_time .now").html(i), N(a), isNaN(e)) return !1;
					var n = e - t;
					$(".h5_player_replay_logo .time").html(o.format_time(n))
				},
				j = function() {
					var e = o.format_time(a.get_attributes("duration"));
					e && "NaN:NaN" != e ? ($(".h5_player_control_time .total").html(e), $(".h5_player_replay_logo .time").html(e)) : (setTimeout(function() {
						j()
					}, 2e3), $(".h5_player_control_time .total").html("00"))
				},
				S = function() {
					var e = o.format_time(a.get_attributes("duration"));
					e && "NaN:NaN" != e ? $(".h5_player_control_time .total").html(e) : (setTimeout(function() {
						j()
					}, 2e3), $(".h5_player_control_time .total").html("00"))
				};
			return {
				init: q,
				show_pause: k,
				show_waiting: m,
				show_warning: b,
				hide_icon: I,
				set_play: g,
				update_time: M,
				set_total_time: j,
				update_total_time: S
			}
		}()
	}, {
		"./template": 5,
		"./tools": 6
	}],
	5: [function(e, t, i) {
		t.exports = function() {
			return {
				svg_template: '<div style="display:none"><svg><symbol id="process_bg"><line x1="0" y1="2" x2="300" y2="2" style="stroke:grey; stroke-width:4; fill-opacity:0.5"/></symbol><symbol id="process"><line x1="0" y1="2" x2="300" y2="2" style="stroke:#1dbbfe;stroke-width:4"/></symbol><symbol id="process_btn"><circle cx="9" cy="9" r="9" style="fill:#fff"/></symbol><symbol id="play"><polyline class="play" points="0,0  14,9  0,18" style="stroke:#fff;fill:#fff;"/></symbol><symbol id="pause"><line class="pause" x1="2" y1="0" x2="2" y2="18" stroke="#fff" stroke-width="5" /><line class="pause" x1="11" y1="0" x2="11" y2="18" stroke="#fff" stroke-width="5" /></symbol><symbol id="control_btn_bg"><circle cx="20" cy="20" r="20" style="fill:#000; fill-opacity:0.5"/></symbol></svg></div>',
				replay: '<div class="h5_player_tip_btn"><div class="h5_player_pause" style="display:none"></div><div class="h5_player_waiting" style="display:none"><img width="80" src="http://static.huajiao.com/huajiao/web/static/module/huajiao_player/skin/img/waiting.png?v=32c89a4" /></div><div class="h5_player_warning" style="display:none"></div></div><div class="h5_player_control_btn"><svg class="control_btn_bg"><use xlink:href="#control_btn_bg"/></svg><svg class="play"><use xlink:href="#play"/></svg><svg class="pause"><use xlink:href="#pause"/></svg></div><div class="h5_player_control_time"><span class="now">00:00:00</span></div><div class="h5_player_process_bar"><svg class="process_bg"><use xlink:href="#process_bg"/></svg><svg class="process_line"><use xlink:href="#process"/></svg><svg class="process_btn"><use xlink:href="#process_btn"/></svg></div><div class="h5_player_process_forward_wrap" style="display:none"></div><div class="h5_player_process_forward" style="display:none"><div class="img forward"></div><div class="time">00:00</div></div>',
				liveplay: '<div class="h5_player_tip_btn"><div class="h5_player_pause" style="display:none"></div><div class="h5_player_waiting" style="display:none"><img width="80" src="http://static.huajiao.com/huajiao/web/static/module/huajiao_player/skin/img/waiting.png?v=32c89a4" /></div><div class="h5_player_warning" style="display:none"></div></div><div class="h5_player_control_bar" style="display:none"><svg class="h5_player_control_btn"><polyline class="play" points="0,0  14,9  0,18"style="stroke:#444844;fill:#444844; display:none"/><line class="pause" x1="2" y1="0" x2="2" y2="18" stroke="#444844" stroke-width="5" /><line class="pause" x1="11" y1="0" x2="11" y2="18" stroke="#444844" stroke-width="5" /></svg><div class="h5_player_control_time"><span class="now">00:00</span></div></div>',
				mp4: '<div class="h5_player_tip_btn"><div class="h5_player_pause" style="display:none"></div><div class="h5_player_waiting" style="display:none"><img width="80" src="http://static.huajiao.com/huajiao/web/static/module/huajiao_player/skin/img/waiting.png?v=32c89a4" /></div><div class="h5_player_warning" style="display:none"></div></div><div class="h5_player_control_btn"><svg class="control_btn_bg"><use xlink:href="#control_btn_bg"/></svg><svg class="play"><use xlink:href="#play"/></svg><svg class="pause"><use xlink:href="#pause"/></svg></div><div class="h5_player_control_time"><span class="now">00:00:00</span></div><div class="h5_player_process_bar"><svg class="process_bg"><use xlink:href="#process_bg"/></svg><svg class="process_line"><use xlink:href="#process"/></svg><svg class="process_btn"><use xlink:href="#process_btn"/></svg></div><div class="h5_player_process_forward_wrap" style="display:none"></div><div class="h5_player_process_forward" style="display:none"><div class="img forward"></div><div class="time">00:00</div></div>'
			}
		}()
	}, {}],
	6: [function(e, t, i) {
		t.exports = function() {
			var e = function(e) {
					var t = "",
						i = parseInt(e),
						n = i % 60,
						s = parseInt(i / 60),
						a = 0;
					return s > 60 && (a = parseInt(s / 60), s = parseInt(s % 60)), 10 > s && (s = "0" + s), 10 > n && (n = "0" + n), 10 > a && (a = "0" + a), t = a + ":" + s + ":" + n
				},
				t = function(e, t) {
					for (var i in e) t = t.replace("{{" + i + "}}", e[i]);
					return t
				};
			return {
				format_time: function(t) {
					return e(t)
				},
				replace: function(e, i) {
					return t(e, i)
				}
			}
		}()
	}, {}]
}, {}, [1]);