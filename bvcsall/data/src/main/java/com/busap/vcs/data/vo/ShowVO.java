package com.busap.vcs.data.vo;
/**
 * 20150723
 * @author dmsong
 *
 */

public class ShowVO extends BaseVO {
	private String title;		//标题
	private String description;		//描述
	private String pic;		//图片地址
	private Long videoId;	//视频id
	private String refVideoId;		//关联视频id ,多个id用逗号分隔
	private String refUserId;		//关联视频的上传用户id，多个id用逗号分隔
	private String playKey;			//视频文件播放地址参数
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public Long getVideoId() {
		return videoId;
	}
	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}
	public String getRefVideoId() {
		return refVideoId;
	}
	public void setRefVideoId(String refVideoId) {
		this.refVideoId = refVideoId;
	}
	public String getRefUserId() {
		return refUserId;
	}
	public void setRefUserId(String refUserId) {
		this.refUserId = refUserId;
	}
	public String getPlayKey() {
		return playKey;
	}
	public void setPlayKey(String playKey) {
		this.playKey = playKey;
	}
}
