package com.busap.vcs.data.entity;

import java.util.Date;

/**
 * 
 * @author klh
 *
 */
public class VideoCheck {
	private Long id;
	private Long uid;
	private Long videoid;
	private Long publishdate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getVideoid() {
		return videoid;
	}

	public void setVideoid(Long videoid) {
		this.videoid = videoid;
	}

	public Long getPublishdate() {
		return publishdate;
	}

	public void setPublishdate(Long publishdate) {
		this.publishdate = publishdate;
	}

}
