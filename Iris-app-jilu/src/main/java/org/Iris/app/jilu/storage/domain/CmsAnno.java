package org.Iris.app.jilu.storage.domain;

import org.Iris.redis.RedisHashBean;
import org.Iris.util.lang.DateUtils;

public class CmsAnno implements RedisHashBean {

	private long annoId;
	private int created;
	private int updated;
	private String title;
	private String content;
	private String author;
	private String source;
	private String img;
	private int ispublished;

	public CmsAnno(String title, String content, String author, String source) {
		this.title = title;
		this.content = content;
		this.author = author;
		this.source = source;
		int time = DateUtils.currentTime();
		this.created = time;
		this.updated = time;
	}

	public CmsAnno(long annoId, int updated, String title, String content, String author, String source) {
		super();
		this.annoId = annoId;
		this.updated = updated;
		this.title = title;
		this.content = content;
		this.author = author;
		this.source = source;
		this.img = img;
	}

	public CmsAnno(long annoId) {
		this.annoId = annoId;
	}

	public CmsAnno() {
		super();
	}

	public int getIspublished() {
		return ispublished;
	}

	public void setIspublished(int ispublished) {
		this.ispublished = ispublished;
	}

	public long getAnnoId() {
		return annoId;
	}

	public void setAnnoId(long annoId) {
		this.annoId = annoId;
	}

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getUpdated() {
		return updated;
	}

	public void setUpdated(int updated) {
		this.updated = updated;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String redisKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
