package com.busap.vcs.data.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@Table(name = "activity")
public class Activity extends BaseEntity{  
	 
	private static final long serialVersionUID = -1299070164949884683L;
	
	private String  title;       
	private String  cover; 
	private String  description;  
	private int  order_num;
	
	@Column(name = "tags",columnDefinition = "varchar(1024)  NULL ",nullable=true)
	private String tags;
	
	@Transient
	private Long  videoId;
	
	@Transient
	private int  editFlg;//0:可以编辑   1：不可编辑
	
	
	
//	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.EAGER, mappedBy="activity")
//	Set<ActivityVideo> activityVideoSet=new HashSet<ActivityVideo>();
	
	public Long getVideoId() {
		return videoId;
	}
	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}


	private int  status;
	
	/*发现:0 | 活动:1*/
	private int groupType;
	
	private String playkey;// 宣传视频

	private String bannerPic;// banner图片路径

	private String videoCoverPic;// 宣传视频封面图 

//	private int type;//活动显示类型       0:文字,1:文字+banner图片（+视频+视频图片）,2:文字+视频 +视频图片
	
	private String vips;// vip活动  用户vip类别状态，暂时支持（蓝V 1，黄V 2，绿V 3）（允许多个，分号分隔）
	
	private String rusers;// 个人明星活动，需配置明星用户id（允许多个，分号分隔）

	private String bannerDescription;

	private String source;

	@Column(name = "share_img",columnDefinition = "varchar(500)  NULL ", nullable=true)
	private String shareImg;

	@Column(name = "share_text",columnDefinition = "varchar(500)  NULL ", nullable=true)
	private String shareText;

	public String getShareImg() {
		return shareImg;
	}

	public void setShareImg(String shareImg) {
		this.shareImg = shareImg;
	}

	public String getShareText() {
		return shareText;
	}

	public void setShareText(String shareText) {
		this.shareText = shareText;
	}

	public String getPlaykey() {
		return playkey;
	}
	public void setPlaykey(String playkey) {
		this.playkey = playkey;
	}
	public String getBannerPic() {
		return bannerPic;
	}
	public void setBannerPic(String bannerPic) {
		this.bannerPic = bannerPic;
	}
	public String getVideoCoverPic() {
		return videoCoverPic;
	}
	public void setVideoCoverPic(String videoCoverPic) {
		this.videoCoverPic = videoCoverPic;
	} 
//	public int getType() {
//		return type;
//	}
//	public void setType(int type) {
//		this.type = type;
//	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getOrder_num() {
		return order_num;
	}
	public void setOrder_num(int order_num) {
		this.order_num = order_num;
	}
	public int getGroupType() {
		return groupType;
	}
	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
//	public Set<ActivityVideo> getActivityVideoSet() {
//		return activityVideoSet;
//	}
//	public void setActivityVideoSet(Set<ActivityVideo> activityVideoSet) {
//		this.activityVideoSet = activityVideoSet;
//	}   
	
	
//	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)     
//	@JoinTable(name="activity_video",
//    			joinColumns = @JoinColumn(name="activityid"),
//    			inverseJoinColumns = @JoinColumn(name="videoid")
//			)
//	private Video video;
//	public Video getVideo() {
//		return video;
//	}
//	public void setVideo(Video video) {
//		this.video = video;
//	}
	
	@JsonBackReference
	@ManyToMany(cascade = {},   fetch = FetchType.LAZY)     
	@JoinTable(name="activity_video",
    			joinColumns = @JoinColumn(name="activityid"),
    			inverseJoinColumns = @JoinColumn(name="videoid")
			)
	private Set<Video> video;
	public Set<Video> getVideo() {
		return video;
	}
	public void setVideo(Set<Video> video) {
		this.video = video;
	}
	public int getEditFlg() {
		return editFlg;
	}
	public void setEditFlg(int editFlg) {
		this.editFlg = editFlg;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getVips() {
		return vips;
	}
	public void setVips(String vips) {
		this.vips = vips;
	}
	public String getRusers() {
		return rusers;
	}
	public void setRusers(String rusers) {
		this.rusers = rusers;
	}

	public String getBannerDescription() {
		return bannerDescription;
	}

	public void setBannerDescription(String bannerDescription) {
		this.bannerDescription = bannerDescription;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
