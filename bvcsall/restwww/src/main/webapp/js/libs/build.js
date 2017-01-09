!
function e(t, n, r) {
	function i(_, a) {
		if (!n[_]) {
			if (!t[_]) {
				var d = "function" == typeof require && require;
				if (!a && d) return d(_, !0);
				if (o) return o(_, !0);
				var u = new Error("Cannot find module '" + _ + "'");
				throw u.code = "MODULE_NOT_FOUND", u
			}
			var s = n[_] = {
				exports: {}
			};
			t[_][0].call(s.exports, function(e) {
				var n = t[_][1][e];
				return i(n ? n : e)
			}, s, s.exports, e, t, n, r)
		}
		return n[_].exports
	}
	for (var o = "function" == typeof require && require, _ = 0; _ < r.length; _++) i(r[_]);
	return i
}({
	1: [function(e, t, n) {
		t.exports = function() {
			var e = function(e) {
					var t = arguments[0];
					if ("undefined" == typeof t || null === t) return {};
					if ("object" != typeof t) return t;
					var n = arguments[1],
						r = !0;
					n instanceof Array || (n = t, r = !1);
					var i, o = {};
					for (i in n) attr = r ? n[i] : i, t.hasOwnProperty(attr) && (t[attr] instanceof Array ? o[attr] = cloneArray(t[attr]) : "object" == typeof t[attr] ? o[attr] = cloneOwn(t[attr]) : o[attr] = t[attr]);
					return o
				};
			return {
				clone_obj: e
			}
		}()
	}, {}],
	2: [function(e, t, n) {
		t.exports = function() {
			var t = e("./ajaxTools"),
				n = "http://g.live.360.cn/liveplay",
				r = "http://gtest.live.360.cn/liveplay",
				i = "http://g.live.360.cn/replay",
				o = "http://gtest.live.360.cn/replay",
				_ = 0,
				a = function(e, d, u) {
					var s = "";
					s = "liveplay" == d.vtype ? d.debug ? r : n : d.debug ? o : i;
					var c = {
						channel: d.channel,
						sn: d.sn,
						_rate: d._rate,
						stype: d.stype,
						sid: d.sid,
						ts: d.ts,
						_jsonp: d._jsonp
					};
					t.ajaxJsonp(s, c, function(t) {
						if (t) u(t), e.trigger("connectSuccess");
						else if (e.trigger("reconnect"), 3 > _) {
							_++;
							var n = Math.floor(0 + 2 * Math.random());
							switch (n) {
							case 0:
								setTimeout(function() {
									a(c, u)
								}, 1e3);
								break;
							case 1:
								setTimeout(function() {
									a(c, u)
								}, 3e3);
								break;
							case 2:
								setTimeout(function() {
									a(c, u)
								}, 5e3)
							}
						} else e.trigger("connectError"), u(!1)
					})
				};
			return {
				init: a
			}
		}()
	}, {
		"./ajaxTools": 7
	}],
	3: [function(e, t, n) {
		!
		function(t, n) {
			var r = e("./Connect"),
				i = e("./Video_event"),
				o = e("./Video_get_attributes"),
				_ = e("./ajaxTools");
			clone_data = e("./Clone_obj"), H5_video = function(e, t) {
				this._dom = t, this._user_data = e, this._upload_info_url = "http://qos.live.360.cn/vc.gif", this._event, this._online_timer, this._back_url_timer, this._sdkver = "1.0.0", this._update_info_base_date, this._is_pause = 0, this._skin = e.skin || !1, this.check_video() ? this.init() : console.log("不支持video标签")
			}, H5_video.prototype.init = function() {
				var e = this;
				this._video, this._is_waiting = 0, this._waiting_start_time = 0, this._waiting_end_time = 0, this._waiting_num = 0, this._is_seek = 0, this._total_waiting_time = 0, this._is_first_error = 1, this._mark_time = 0, this._back_url = [], this._update_info_base_date = {
					cid: this._user_data.channel,
					uid: this._user_data.uid || "",
					sdkver: this._sdkver,
					sid: this._user_data.sid || "",
					bid: this._user_data.bid || "",
					pid: this._user_data.pid || "H5",
					ver: "0.0.1",
					rid: this._user_data.sn,
					mid: this._user_data.mid || "",
					tm: (new Date).getTime(),
					r: Math.random(),
					ty: "online"
				};
				var n = {
					channel: this._user_data.channel,
					sdkver: this._sdkver,
					sn: this._user_data.sn,
					_rate: this._user_data._rate || "xd",
					stype: this._user_data.stype || "m3u8",
					vtype: this._user_data.vtype || "",
					debug: this._user_data.debug || !1,
					sid: this._user_data.sid || "",
					ts: (new Date).getTime(),
					_jsonp: this._user_data._jsonp || "_jsonp"
				},
					i = (new Date).getTime();
				r.init(t(this), n, function(t) {
					if (t) {
						e._back_url = t.back, e._update_info_base_date.u = encodeURI(t.main);
						var n = e._user_data.width || "100%",
							r = e._user_data.height || "100%",
							o = e._user_data.controls ? " controls" : "",
							_ = e._user_data.autoplay ? " autoplay" : "",
							a = e._user_data.loop ? 'loop="loop"' : "",
							d = e._user_data.preload ? e._user_data.preload : "auto",
							u = e._user_data.poster || "",
							s = "<video " + o + _ + ' webkit-playsinline="true" x-webkit-airplay="true" width="' + n + '" height="' + r + '" ' + a + ' preload="' + d + '" poster="' + u + '" src="' + t.main + '" ></video>';
						e._dom.html(s), e._video = e._dom.find("video")[0], e._video.duration = 0, e.add_dispatch_event();
						var c = (new Date).getTime(),
							l = c - i;
						e.upload_info_action(0, l), e.upload_info_init(), e._online_timer = setInterval(function() {
							e.upload_info_online(e._upload_info_url, e._update_info_base_date)
						}, 6e4)
					} else {
						alert("cdn 返回链接失败");
						var c = (new Date).getTime(),
							l = c - i;
						e.upload_info_action(e.dom.error.code, l)
					}
					e._is_first_error = 0
				})
			}, H5_video.prototype.use_back_url = function() {
				var e = this;
				clearInterval(e._online_timer), e._back_url.length ? (" " != t.trim(e._back_url[0]) && "" != t.trim(e._back_url[0]) ? (e._video.src = e._back_url[0], e._update_info_base_date.u = encodeURI(e._back_url[0]), e._back_url.splice(0, 1), e._back_url_timer = setInterval(function() {
					e.upload_info_online(e._upload_info_url, e._update_info_base_date)
				}, 6e4)) : (e._back_url.splice(0, 1), e.use_back_url()), t(this).trigger("backUrlError")) : (clearInterval(e._back_url_timer), e._mark_time = 0, t(this).trigger("urlError"))
			}, H5_video.prototype.check_video = function() {
				var e = document.createElement("video");
				return e.canPlayType ? !0 : !1
			}, H5_video.prototype.request_full_screen = function() {
				this._video.requestFullScreen ? (this._video.requestFullScreen(), t(this).trigger("requestFullScreen")) : this._video.mozRequestFullScreen ? (this._video.mozRequestFullScreen(), t(this).trigger("requestFullScreen")) : this._video.webkitRequestFullScreen && (this._video.webkitRequestFullScreen(), t(this).trigger("requestFullScreen"))
			}, H5_video.prototype.cancel_full_screen = function() {
				this._video.exitFullscreen ? (this._video.exitFullscreen(), t(this).trigger("cancelFullScreen")) : this._video.mozCancelFullScreen ? (this._video.mozCancelFullScreen(), t(this).trigger("cancelFullScreen")) : this._video.webkitCancelFullScreen && (this._video.webkitCancelFullScreen(), t(this).trigger("cancelFullScreen"))
			}, H5_video.prototype.add_dispatch_event = function() {
				var e = this;
				e._event = new i.init(e), e._event.add_event()
			}, H5_video.prototype.remove_dispatch_event = function() {
				var e = this;
				e._event.remove_event(), e._event = null
			}, H5_video.prototype.canPlayType = function(e) {
				return this._video.canPlayType(e)
			}, H5_video.prototype.pause = function() {
				this._video.pause()
			}, H5_video.prototype.play = function() {
				this._video.play()
			}, H5_video.prototype.seek = function(e) {
				this._video.currentTime = e
			}, H5_video.prototype.load = function() {
				this._video.load()
			}, H5_video.prototype.add_current_time = function(e) {
				this._video.currentTime += e
			}, H5_video.prototype.reduce_current_time = function(e) {
				this._video.currentTime += e
			}, H5_video.prototype.add_playback_rate = function(e) {
				this._video.playbackRate += e
			}, H5_video.prototype.reduce_playback_rate = function(e) {
				this._video.playbackRate -= e
			}, H5_video.prototype.add_volume = function() {
				this._video.volume += .1
			}, H5_video.prototype.reduce_volume = function() {
				this._video.volume -= .1
			}, H5_video.prototype.muted = function(e) {
				this._video.muted = e
			}, H5_video.prototype.change_sn = function(e) {
				this._user_data.sn = e, this.remove_dispatch_event(), clearInterval(this._online_timer), this._is_first_error = 1, this.init()
			}, H5_video.prototype.get_attributes = function() {
				var e = arguments[0],
					t = arguments[1] || 0;
				switch (e) {
				case "current_time":
					return o.currentTime(this._video);
				case "duration":
					return o.duration(this._video);
				case "paused":
					return o.paused(this._video);
				case "default_playback_rate":
					return o.defaultPlaybackRate(this._video);
				case "playback_rate":
					return o.playbackRate(this._video);
				case "played_start":
					return o.played_start(this._video, t);
				case "played_end":
					return o.played_end(this._video, t);
				case "seekable_start":
					return o.seekable_start(this._video, t);
				case "seekable_end":
					return o.seekable_end(this._video, t);
				case "ended":
					return o.ended(this._video);
				case "auto_play":
					return o.autoPlay(this._video);
				case "loop":
					return o.loop(this._video);
				case "buffered_start":
					return o.buffered_start(this._video, t);
				case "buffered_end":
					return o.buffered_end(this._video, t);
				case "audioTracks_length":
					return o.audioTracks_length(this._video);
				case "audioTracks_get_object_by_id":
					return o.audioTracks_get_object_by_id(this._video, t);
				case "audioTracks_get_object_by_index":
					return o.audioTracks_get_object_by_index(this._video, t);
				case "controller":
					return o.controller(this._video);
				case "current_src":
					return o.currentSrc(this._video);
				case "default_muted":
					return o.defaultMuted(this._video);
				case "error":
					return o.error(this._video);
				case "media_group":
					return o.mediaGroup(this._video);
				case "muted":
					return o.muted(this._video);
				case "network_state":
					return o.networkState(this._video);
				case "ready_state":
					return o.readyState(this._video);
				case "volume":
					return o.volume(this._video);
				case "textTracks_length":
					return o.textTracks_length(this._video);
				case "textTracks_get_object_by_index":
					return o.textTracks_get_object_by_index(this._video, t);
				case "textTracks_get_object_by_id":
					return o.textTracks_get_object_by_id(this._video, t);
				case "videoTracks_get_object_by_index":
					return o.videoTracks_get_object_by_index(this._video, t);
				case "videoTracks_get_object_by_id":
					return o.videoTracks_get_object_by_id(this._video, t)
				}
			}, H5_video.prototype.upload_info_init = function() {
				var e = this;
				_.ajaxGet(e._upload_info_url, e._update_info_base_date, function(e) {
					e ? console.log("调度接口请求成功") : console.log("请求调度接口请求失败")
				})
			}, H5_video.prototype.upload_info_action = function(e, t) {
				var n = this,
					r = clone_data.clone_obj(n._update_info_base_date);
				switch (r.st = 2, r.rt = t, r.ty = "action", r.er = e, r.tm = (new Date).getTime(), r.r = Math.random(), e) {
				case 0:
					r.em = "调度成功";
					break;
				case 1:
					r.em = "用户终止";
					break;
				case 2:
					r.em = "网络错误";
					break;
				case 3:
					r.em = "解码错误";
					break;
				case 4:
					r.em = "URL无效"
				}
				_.ajaxGet(n._upload_info_url, r, function(e) {
					e ? console.log("调度接口上报成功") : console.log("调度接口上报失败")
				})
			}, H5_video.prototype.upload_info_online = function(e, t) {
				var n = clone_data.clone_obj(t);
				n.ty = "online", n.tm = (new Date).getTime(), n.r = Math.random(), _.ajaxGet(e, n, function(e) {
					e ? console.log("在线接口上报成功") : console.log("在线接口上报失败")
				})
			}, H5_video.prototype.upload_info_exception = function(e) {
				var t = this,
					n = clone_data.clone_obj(t._update_info_base_date);
				switch (n.exception = e, n.st = 2, n.ty = "action", n.tm = (new Date).getTime(), n.r = Math.random(), n.er = e, e) {
				case 1:
					n.em = "第一次加载时用户终止";
					break;
				case 2:
					n.em = "第一次加载时网络错误";
					break;
				case 3:
					n.em = "第一次加载时解码错误";
					break;
				case 4:
					n.em = "第一次加载时URL无效"
				}
				_.ajaxGet(t._upload_info_url, n, function(e) {
					e ? console.log("播放过程中的错误接口上报成功") : console.log("播放过程中的错误接口上报失败")
				})
			}, H5_video.prototype.upload_info_buffer = function(e, t) {
				var n = this,
					r = clone_data.clone_obj(n._update_info_base_date);
				switch (r.br = e, r.ty = "buffer", r.bc = n._waiting_num, r.po = n._video.currentTime, r.bt = t, r.tm = (new Date).getTime(), r.r = Math.random(), e) {
				case 1:
					r.em = "第一次加载引起的缓冲结束";
					break;
				case 2:
					r.em = "因为seek引起得缓冲开始";
					break;
				case 4:
					r.em = "因为播放过程中卡引起的缓冲开始"
				}
				_.ajaxGet(n._upload_info_url, r, function(e) {
					e ? console.log("缓冲原因和时间接口上报成功") : console.log("缓冲原因和时间接口上报失败")
				})
			}, H5_video.prototype.upload_info_summary = function(e, t) {
				var n = this,
					r = clone_data.clone_obj(n._update_info_base_date);
				r.ot = e, r.bc = n._waiting_num, r.pt = t, r.ty = "summary", r.tm = (new Date).getTime(), r.r = Math.random(), _.ajaxGet(n._upload_info_url, r, function(e) {
					e ? console.log("加上缓冲一共花去的观看影片的时间接口上报成功") : console.log("加上缓冲一共花去的观看影片的时间接口上报失败")
				})
			}
		}(jQuery, window)
	}, {
		"./Clone_obj": 1,
		"./Connect": 2,
		"./Video_event": 4,
		"./Video_get_attributes": 5,
		"./ajaxTools": 7
	}],
	4: [function(e, t, n) {
		t.exports = function() {
			var t = function(t) {
					function n() {
						$(T._obj).trigger("loadstart"), T._obj._skin && (w.hide_waiting(T._dom), w.show_play(T._dom)), T._obj._is_waiting = 1, T._obj._waiting_start_time = (new Date).getTime()
					}
					function r() {
						$(T._obj).trigger("progress")
					}
					function i() {
						$(T._obj).trigger("suspend")
					}
					function o() {
						$(T._obj).trigger("abort")
					}
					function _(e) {
						$(T._obj).trigger("error"), T._obj._is_first_error || T._obj.upload_info_exception(T._dom.error.code), T._obj._mark_time = T._dom.currentTime, T._obj.use_back_url()
					}
					function a() {
						$(T._obj).trigger("stalled"), T._obj._mark_time = T._dom.currentTime
					}
					function d() {
						T._obj._skin && (w.hide_waiting(T._dom), w.hide_play(T._dom)), $(T._obj).trigger("play")
					}
					function u() {
						T._obj._skin && (w.show_play(T._dom), w.hide_waiting(T._dom)), T._obj._is_pause = 1, $(T._obj).trigger("pause")
					}
					function s() {
						$(T._obj).trigger("loadedmetadata")
					}
					function c() {
						$(T._obj).trigger("loadeddata")
					}
					function l() {
						$(T._obj).trigger("waiting"), T._obj._waiting_num && T._obj._skin && (w.show_waiting(T._dom), w.hide_play(T._dom)), T._obj._waiting_num++, T._obj._waiting_start_time = (new Date).getTime()
					}
					function f() {
						T._obj._skin && (w.hide_waiting(T._dom), w.hide_play(T._dom)), $(T._obj).trigger("playing"), T._obj._waiting_end_time = (new Date).getTime();
						var e = T._obj._waiting_end_time - T._obj._waiting_start_time;
						T._obj._total_waiting_time = T._obj._total_waiting_time + e, T._obj._is_pause ? T._obj._is_pause = 0 : T._obj._is_seek ? 0 != T._obj._waiting_start_time && T._obj.upload_info_buffer(2, e) : 0 == T._obj._waiting_num ? (T._obj.upload_info_buffer(1, e), T._obj._waiting_num++) : 0 != T._obj._waiting_start_time && T._obj.upload_info_buffer(4, e), T._obj._waiting_start_time = 0, T._obj._waiting_end_time = 0, T._obj._is_seek = 0
					}
					function m() {
						$(T._obj).trigger("canplay"), T._obj._mark_time && (T._dom.currentTime = T._obj._mark_time, T._obj._mark_time = 0)
					}
					function v() {
						$(T._obj).trigger("canplaythrough")
					}
					function p() {
						T._obj._is_seek = 1, $(T._obj).trigger("seeking")
					}
					function h() {
						$(T._obj).trigger("seeked")
					}
					function b() {
						$(T._obj).trigger("timeupdate")
					}
					function g() {
						T._obj.upload_info_summary(T._obj._total_waiting_time + T._obj._video.currentTime, T._obj._video.currentTime), T._obj._total_waiting_time = 0, T._obj._waiting_num = 0, clearInterval(T._obj._online_timer), T._obj._is_first_error = 1, T._obj._mark_time = 0, $(T._obj).trigger("ended")
					}
					function y() {
						$(T._obj).trigger("ratechange")
					}
					function k() {
						$(T._obj).trigger("durationchange")
					}
					function j() {
						$(T._obj).trigger("volumechange")
					}
					var w = e("./Video_skin"),
						T = this;
					T._obj = t, T._dom = t._video, T.add_event = function() {
						T._dom.addEventListener("loadstart", n, !1), T._dom.addEventListener("progress", r, !1), T._dom.addEventListener("suspend", i, !1), T._dom.addEventListener("abort", o, !1), T._dom.addEventListener("error", _, !1), T._dom.addEventListener("stalled", a, !1), T._dom.addEventListener("play", d, !1), T._dom.addEventListener("pause", u, !1), T._dom.addEventListener("loadedmetadata", s, !1), T._dom.addEventListener("loadeddata", c, !1), T._dom.addEventListener("waiting", l, !1), T._dom.addEventListener("playing", f, !1), T._dom.addEventListener("canplay", m, !1), T._dom.addEventListener("canplaythrough", v, !1), T._dom.addEventListener("seeking", p, !1), T._dom.addEventListener("seeked", h, !1), T._dom.addEventListener("timeupdate", b, !1), T._dom.addEventListener("ended", g, !1), T._dom.addEventListener("ratechange", y, !1), T._dom.addEventListener("durationchange", k, !1), T._dom.addEventListener("volumechange", j, !1), $("body").delegate(".js_h5_sdk_play_dom", "touchend", function() {
							T._dom.play()
						})
					}, T.remove_event = function() {
						T._dom.removeEventListener("loadstart", n, !1), T._dom.removeEventListener("progress", r, !1), T._dom.removeEventListener("suspend", i, !1), T._dom.removeEventListener("abort", o, !1), T._dom.removeEventListener("error", _, !1), T._dom.removeEventListener("stalled", a, !1), T._dom.removeEventListener("play", d, !1), T._dom.removeEventListener("pause", u, !1), T._dom.removeEventListener("loadedmetadata", s, !1), T._dom.removeEventListener("loadeddata", c, !1), T._dom.removeEventListener("waiting", l, !1), T._dom.removeEventListener("playing", f, !1), T._dom.removeEventListener("canplay", m, !1), T._dom.removeEventListener("canplaythrough", v, !1), T._dom.removeEventListener("seeking", p, !1), T._dom.removeEventListener("seeked", h, !1), T._dom.removeEventListener("timeupdate", b, !1), T._dom.removeEventListener("ended", g, !1), T._dom.removeEventListener("ratechange", y, !1), T._dom.removeEventListener("durationchange", k, !1), T._dom.removeEventListener("volumechange", j, !1), $(".js_h5_sdk_play_dom").unbind()
					}
				};
			return {
				init: t
			}
		}()
	}, {
		"./Video_skin": 6
	}],
	5: [function(e, t, n) {
		t.exports = function() {
			var e = 0,
				t = 0,
				n = function(n) {
					return 2 > e ? e++ : 2 == e && (e++, n.currentTime > 1e3 && (t = n.currentTime)), t ? _currentTime = n.currentTime - t : _currentTime = n.currentTime, _currentTime
				},
				r = function(e) {
					return e.duration
				},
				i = function(e) {
					return e.paused
				},
				o = function(e) {
					return e.defaultPlaybackRate
				},
				_ = function(e) {
					return e.playbackRate
				},
				a = function(e, t) {
					return e.played.start(t)
				},
				d = function(e, t) {
					return e.played.end(t)
				},
				u = function(e, t) {
					return e.seekable.start(t)
				},
				s = function(e, t) {
					return e.seekable.end(t)
				},
				c = function(e) {
					return e.ended
				},
				l = function(e) {
					return e.autoplay
				},
				f = function(e) {
					return e.loop
				},
				m = function(e, t) {
					return e.buffered.start(t)
				},
				v = function(e, t) {
					return e.buffered.end(t)
				},
				p = function(e) {
					return e.audioTracks.length
				},
				h = function(e, t) {
					return e.audioTracks.getTrackById(t)
				},
				b = function(e, t) {
					return e.audioTracks[t]
				},
				g = function(e) {
					return e.controller
				},
				y = function(e) {
					return e.currentSrc
				},
				k = function(e) {
					return e.defaultMuted
				},
				j = function(e) {
					return e.error
				},
				w = function(e) {
					return e.mediaGroup
				},
				T = function(e) {
					return e.muted
				},
				x = function(e) {
					return e.volume
				},
				E = function(e) {
					return e.networkState
				},
				L = function(e) {
					return e.readyState
				},
				$ = function(e) {
					return e.textTracks.length
				},
				H = function(e, t) {
					return e.textTracks[t]
				},
				S = function(e, t) {
					return e.textTracks.getTrackById(t)
				},
				F = function(e, t) {
					return e.videoTracks[t]
				},
				R = function(e, t) {
					return e.videoTracks.getTrackById(t)
				};
			return {
				currentTime: function(e) {
					return n(e)
				},
				duration: function(e) {
					return r(e)
				},
				paused: function(e) {
					return i(e)
				},
				defaultPlaybackRate: function(e) {
					return o(e)
				},
				playbackRate: function(e) {
					return _(e)
				},
				played_start: function(e, t) {
					return a(e, t)
				},
				played_end: function(e, t) {
					return d(e, t)
				},
				seekable_start: function(e, t) {
					return u(e, t)
				},
				seekable_end: function(e, t) {
					return s(e, t)
				},
				ended: function(e) {
					return c(e)
				},
				autoPlay: function(e) {
					return l(e)
				},
				loop: function(e) {
					return f(e)
				},
				buffered_start: function(e, t) {
					return m(e, t)
				},
				buffered_end: function(e, t) {
					return v(e, t)
				},
				audioTracks_length: function(e) {
					return p(e)
				},
				audioTracks_get_object_by_id: function(e, t) {
					return h(e, t)
				},
				audioTracks_get_object_by_index: function(e, t) {
					return b(e, t)
				},
				controller: function(e) {
					return g(e)
				},
				currentSrc: function(e) {
					return y(e)
				},
				defaultMuted: function(e) {
					return k(e)
				},
				error: function(e) {
					return j(e)
				},
				mediaGroup: function(e) {
					return w(e)
				},
				muted: function(e) {
					return T(e)
				},
				networkState: function(e) {
					return E(e)
				},
				readyState: function(e) {
					return L(e)
				},
				volume: function(e) {
					return x(e)
				},
				textTracks_length: function(e) {
					return $(e)
				},
				textTracks_get_object_by_index: function(e, t) {
					return H(e, t)
				},
				textTracks_get_object_by_id: function(e, t) {
					return S(e, t)
				},
				videoTracks_get_object_by_id: function(e, t) {
					return R(e, t)
				},
				videoTracks_get_object_by_index: function(e, t) {
					return F(e, t)
				}
			}
		}()
	}, {}],
	6: [function(e, t, n) {
		t.exports = function() {
			var e = function(e) {
					if ($(".js_h5_sdk_waiting_dom").length) $(".js_h5_sdk_play_dom").hide(), $(".js_h5_sdk_waiting_dom").show();
					else {
						var t = '<a type="button" class="js_h5_sdk_waiting_dom"></a>';
						$(e).after(t)
					}
				},
				t = function(e) {
					$(e).next(".js_h5_sdk_waiting_dom").hide()
				},
				n = function(e) {
					if ($(".js_h5_sdk_play_dom").length) $(".js_h5_sdk_waiting_dom").hide(), $(".js_h5_sdk_play_dom").show();
					else {
						var t = '<a type="button" style="4px solid green" class="js_h5_sdk_play_dom"></a>';
						$(e).after(t)
					}
				},
				r = function(e) {
					$(e).next(".js_h5_sdk_play_dom").hide()
				};
			return {
				show_waiting: e,
				hide_waiting: t,
				show_play: n,
				hide_play: r
			}
		}()
	}, {}],
	7: [function(e, t, n) {
		t.exports = function() {
			var e = function(e, t, n) {
					$.ajax({
						type: "POST",
						url: e,
						cache: !1,
						data: t,
						xhrFields: {
							withCredentials: !0
						},
						success: function(e) {
							n(e)
						},
						error: function() {
							n(!1)
						}
					})
				},
				t = function(e, t, n) {
					$.ajax({
						type: "GET",
						url: e,
						cache: !1,
						data: t,
						xhrFields: {
							withCredentials: !0
						},
						success: function(e) {
							n(e)
						},
						error: function() {
							n(!1)
						}
					})
				},
				n = function(e, t, n) {
					$.ajax({
						type: "GET",
						dataType: "jsonp",
						jsonp: "callbackfn",
						jsonpCallback: t._jsonp,
						url: e,
						cache: !1,
						data: t,
						xhrFields: {
							withCredentials: !0
						},
						success: function(e) {
							n(e)
						},
						error: function() {
							n(!1)
						}
					})
				};
			return {
				ajaxPost: e,
				ajaxGet: t,
				ajaxJsonp: n
			}
		}()
	}, {}]
}, {}, [3]);