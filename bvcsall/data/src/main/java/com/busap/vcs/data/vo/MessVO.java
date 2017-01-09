package com.busap.vcs.data.vo;

public class MessVO  {
	private Long id;
	private String type;
	private Integer oid;
	private String name;
	private String avatar;
	private String vstat;
	public String getVstat() {
		return vstat;
	}
	public void setVstat(String vstat) {
		this.vstat = vstat;
	}
	private String create_at;
	private Integer uid;
    private Integer videoId;		//点击消息操作，app 打开应用首页、video打开视频详情页、activity打开活动详情页
	private String videoPic;			//操作目标对象id，视频id或者活动id

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVideoPic() {
		return videoPic;
	}
	public void setVideoPic(String videoPic) {
		this.videoPic = videoPic;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getOid() {
		return oid;
	}
	public void setOid(Integer oid) {
		this.oid = oid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getCreate_at() {
		return create_at;
	}
	public void setCreate_at(String create_at) {
		this.create_at = create_at;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getVideoId() {
		return videoId;
	}
	public void setVideoId(Integer videoId) {
		this.videoId = videoId;
	}
}
