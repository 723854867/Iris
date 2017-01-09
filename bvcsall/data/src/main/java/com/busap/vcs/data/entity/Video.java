package com.busap.vcs.data.entity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.busap.vcs.data.enums.VideoStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "video")
@NamedNativeQueries({ @NamedNativeQuery(name = "findVideo", query = "select a.id as id,a.name as name,a.tag as tag,a.description as description,a.creator_id as creator_id,b.username as username,a.create_at,a.play_key,a.introduction_mark as introductionMark,a.template_id as templateId,a.is_logo as isLogo,a.play_rate_today as playRateToday from video as a LEFT JOIN video_uploader as b on a.creator_id=b.id  where a.id=?1", resultSetMapping = "findVideo") })
@SqlResultSetMappings({ @SqlResultSetMapping(name = "findVideo", entities = {}, columns = {
		@ColumnResult(name = "username"), 
		@ColumnResult(name = "id"),
		@ColumnResult(name = "name"), 
		@ColumnResult(name = "tag"),
		@ColumnResult(name = "description"),
		@ColumnResult(name = "creator_id"), 
		@ColumnResult(name = "create_at"),
		@ColumnResult(name = "play_key"),
		@ColumnResult(name = "introductionMark"),
		@ColumnResult(name = "templateId"),
		@ColumnResult(name = "isLogo"),
		@ColumnResult(name = "playRateToday")
}) })
public class Video extends BaseEntity {

	private static final long serialVersionUID = -4528470881091940889L;

	private String name;// 默认名称

	private String nameEn;// 英文名称

	private String description;// 扩展描述

	private String tag;// 类似分类信息，搜索用

	private String url;// 指向地址

//	private String useStat;// 视频使用状态，禁止，启用，禁止keep，启用live，默认启用

	private String flowStat = VideoStatus.未审核.getName();// 视频流转状态,ct新建、up上传成功、trans转码成功、check审核通过

	private String failReason;// 审核失败原因

	private String pixel;// 直接存储字符 比如 800*600,1024*800

	private Integer width;

	private Integer height;

	private String playKey;// 视频服务器生产的id

	private String videoPic;// 视频封面图片
	
	private String videoListPic;//视频列表图片

	private Integer orderNum;

	private int praiseCount; // 赞的次数

	private int favoriteCount;// 收藏的次数

	private int evaluationCount;// 评论的次数

	private int playCount;// 播放次数

	private String duration;// 时长

	private Date planPublishTime;// 计划发布时间

	private Long auditorId;

	private Date auditDateTime;

	private Date publishTime;// 发布时间

	private Double longitude = 0.00D; // 标准w84经度

	private Double latitude = 0.00D; // 标准w84维度

	private Double gd_longitude = 0.00D;// 高德经度

	private Double gd_latitude = 0.00D; // 高德纬度

	private String coordinateAddr;// 坐标实际地理名称

	private Long playCountToday = 0L;//观看人数

	private BigDecimal playRateToday = new BigDecimal(0.0000);

	private int isRecommend = 0;// 是否推荐(是：1 | 否：0)，默认为0，被推荐后视频会显示在网站首页

	@Transient
	private List<Long> actIds;

	@Transient
	private Date showDate = null;// 专门用于页面显示的时间
	
	private String videoActivity;//专门用于页面显示参与的活动
	 
	@Transient
	private boolean praise = false;

	@Transient
	private boolean favorite = false;
	
	@Transient
	private int showPlayCount; //用于页面显示的播放次数
 
	
	//是否关注该视频的上传者
	@Transient
	private int attentionAuthor = 0;

//	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
//	@JoinColumn(name = "ruser_id")
//	private Ruser ruser;
	
	private Long ruserId;

	@Transient
	private String img;

	@Transient
	private Date modifyat;// 专门用于页面显示修改时间

	private String introductionMark = "0";// 用于标记该视频只显示在活动的第一个 作为介绍。

	private String isLogo;// 视频封面是否贴标

	private Long templateId;// 存储模板ID
	
	@Transient
	private String uploader;

	@Transient
	private String username;

	@Transient
	private String phone;

	private int playRateState;//是否每天重新计算播放率  需要重新计算：0（默认值）  不需要重新计算：1
	@Transient
	private String activityTitle; //活动title
	
	 @Column(name = "day_hot_value",columnDefinition = "double(32,2)  NULL DEFAULT '0.00'",nullable=true)
	private Double dayHotValue;
	 @Column(name = "week_hot_value",columnDefinition = "double(32,2)  NULL DEFAULT '0.00'",nullable=true)
	private Double weekHotValue;
	 @Column(name = "month_hot_value",columnDefinition = "double(32,2)  NULL DEFAULT '0.00'",nullable=true)
	private Double monthHotValue;

	//视频热度排名
	@Transient
	private int hotRank = 0;
	
	//add by yinhb 2015-06-01 视频保存成功，向客户端返回服务器解析出来的tag list
	@Transient
	private List<String> tags;
	
	@Transient
	private int isForward = 0; //是否是转发，1：是，0：否
	
	@Transient
	private String forwardEvaluation = ""; //转发评论
	
	@Transient
	private Ruser forwardUser;  //转发人
	
	@Transient
	private Long forwardUserId = 0l; //转发人id
	
	@Transient
	private Date forwardTime;  //转发时间
	
	private int type = 1;//类型，1：视频，2：直播回放，3：普通图片，4：预告图片
	
	private int playbackWeight = 0; //直播回放权重
	
	private Long liveNoticeId;//直播预告id
	
	private int weight = 0; //整体权重（包括视频，回放，普通图片，预告图片），默认是0
	
//	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)     
//  @JoinColumn( referencedColumnName="videoid" )
////	@JoinTable(name="activity_video",
////  			joinColumns = @JoinColumn(name="id"),
////  			inverseJoinColumns = @JoinColumn(name="videoid")
////			)
//
//
//	private ActivityVideo av;
//	
//	public ActivityVideo getAv() {
//		return av;
//	}
//
//	public void setAv(ActivityVideo av) {
//		this.av = av;
//	}
	
//	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)     
//	@JoinTable(name="activity_video",
//  			joinColumns = @JoinColumn(name="videoid"),
//  			inverseJoinColumns = @JoinColumn(name="activityid")
//			)
//	private Activity activity;
//	
//	
//
//	public Activity getActivity() {
//		return activity;
//	}
//
//	public void setActivity(Activity activity) {
//		this.activity = activity;
//	}
	
	@JsonBackReference
	@ManyToMany(cascade = {}, fetch = FetchType.LAZY)     
	@JoinTable(name="activity_video",
  			joinColumns = @JoinColumn(name="videoid"),
  			inverseJoinColumns = @JoinColumn(name="activityid")
			)
	private Set<Activity> activity;
	
	
	@Transient
	private String actives;
	
	//热度指数
	@Column(name = "hot_point",columnDefinition = "int DEFAULT 0",nullable=true)
	private Integer hotPoint;
	
	private String versionNum;// app版本号

//	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)     
//  @JoinColumn( referencedColumnName="videoid" )
////	@JoinTable(name="activity_video",
////  			joinColumns = @JoinColumn(name="id"),
////  			inverseJoinColumns = @JoinColumn(name="videoid")
////			)
//
//
//	private ActivityVideo av;
//	
//	public ActivityVideo getAv() {
//		return av;
//	}
//
//	public void setAv(ActivityVideo av) {
//		this.av = av;
//	}
	
//	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)     
//	@JoinTable(name="activity_video",
//  			joinColumns = @JoinColumn(name="videoid"),
//  			inverseJoinColumns = @JoinColumn(name="activityid")
//			)
//	private Activity activity;
//	
//	
//
//	public Activity getActivity() {
//		return activity;
//	}
//
//	public void setActivity(Activity activity) {
//		this.activity = activity;
//	}
	

	@Column(columnDefinition = "tinyint  DEFAULT 0",nullable=true)
	private Integer mp4Flg=0;// 是否以mp4方式播放 1:mp4 0:m3u8
	
	//真的马甲用户是否可进入排行榜，0或者null不限制，1不能上榜
    @Column(name = "rank_able",columnDefinition = "int  NULL DEFAULT 0",nullable=true)
	private Integer rankAble = 0;
    
    @Transient
	private Integer forwardCount=0;
    
	@Transient
	private ActivityVideo activityVideo;

	public Set<Activity> getActivity() {
		return activity;
	}

	public void setActivity(Set<Activity> activity) {
		this.activity = activity;
	} 
	
	public String getVideoListPic() {
		return videoListPic;
	}

	public void setVideoListPic(String videoListPic) {
		this.videoListPic = videoListPic;
	}

	public Date getShowDate() {
		return showDate;
	}

	public String getVideoActivity() {
		return videoActivity;
	}

	public void setVideoActivity(String videoActivity) {
		this.videoActivity = videoActivity;
	}

	public void setShowDate(Date showDate) {
		this.showDate = showDate;
	}

	public Long getCreateDate() {
		if (showDate == null)
			return publishTime.getTime();
		else
			return showDate.getTime();
	}

	public Date getCreateDateStr() {
		if (showDate == null)
			return publishTime;
		else
			return showDate;
	}

	public String getFormatCreateDateStr() {
		SimpleDateFormat sdf=new SimpleDateFormat("MM-dd HH:mm");
		if (showDate == null){
			return sdf.format(publishTime);
		}else{
			return sdf.format(showDate);
		}
	}
	public Date getModifyat() {
		return modifyat;
	}

	public void setModifyat(Date modifyat) {
		this.modifyat = modifyat;
	}

	public String getVideoPic() {
		return videoPic;
	}

	public void setVideoPic(String videoPic) {
		this.videoPic = videoPic;
	}

	public String getCoordinateAddr() {
		return coordinateAddr;
	}

	public void setCoordinateAddr(String coordinateAddr) {
		this.coordinateAddr = coordinateAddr;
	}

	public int getHotRank() {
		return hotRank;
	}

	public void setHotRank(int hotRank) {
		this.hotRank = hotRank;
	}

	public List<Long> getActIds() {
		return actIds;
	}

	public void setActIds(List<Long> actIds) {
		this.actIds = actIds;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getGd_longitude() {
		return gd_longitude;
	}

	public void setGd_longitude(Double gd_longitude) {
		this.gd_longitude = gd_longitude;
	}

	public Double getGd_latitude() {
		return gd_latitude;
	}

	public void setGd_latitude(Double gd_latitude) {
		this.gd_latitude = gd_latitude;
	}

	public Long getPlayCountToday() {
		return playCountToday;
	}

	public void setPlayCountToday(Long playCountToday) {
		this.playCountToday = playCountToday;
	}

	public BigDecimal getPlayRateToday() {
		return playRateToday;
	}

	public void setPlayRateToday(BigDecimal playRateToday) {
		this.playRateToday = playRateToday;
	}

	public Long getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Long auditorId) {
		this.auditorId = auditorId;
	}

	public Date getAuditDateTime() {
		return auditDateTime;
	}

	public void setAuditDateTime(Date auditDateTime) {
		this.auditDateTime = auditDateTime;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
 

//	public Ruser getRuser() {
//		return ruser;
//	}
//
//	public void setRuser(Ruser ruser) {
//		this.ruser = ruser;
//	}
	
	

	public String getImg() {
		return img;
	}

	public Long getRuserId() {
		return ruserId;
	}

	public void setRuserId(Long ruserId) {
		this.ruserId = ruserId;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Date getPlanPublishTime() {
		return planPublishTime;
	}

	public void setPlanPublishTime(Date planPublishTime) {
		this.planPublishTime = planPublishTime;
	}

	public int getPlayCount() {
		return playCount;
	}

	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}

	public boolean isPraise() {
		return praise;
	}

	public void setPraise(boolean praise) {
		this.praise = praise;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public int getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public int getEvaluationCount() {
		return evaluationCount;
	}

	public void setEvaluationCount(int evaluationCount) {
		this.evaluationCount = evaluationCount;
	}

	 

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

//	public String getUseStat() {
//		return useStat;
//	}
//
//	public void setUseStat(String useStat) {
//		this.useStat = useStat;
//	}

	public String getFlowStat() {
		return flowStat;
	}

	public void setFlowStat(String flowStat) {
		this.flowStat = flowStat;
	}

	public String getPixel() {
		return pixel;
	}

	public void setPixel(String pixel) {
		this.pixel = pixel;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getPlayKey() {
		return playKey;
	}

	public void setPlayKey(String playKey) {
		this.playKey = playKey;
	}

	public String getDescription() {
		return description;
	}
	public String getDescriptionReplace() {
		if (description !=null && !"".equals(description))
			return description.replace("\r", "").replace("\n", "").replace("'", "\\'");
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getName() {
		return name;
	}
	public String getNameReplace() {
		if (name != null && !"".equals(name))
			return name.replace("\r", "").replace("\n", "").replace("'", "\\'");
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(int isRecommend) {
		this.isRecommend = isRecommend;
	}

	// @ManyToOne(cascade = { CascadeType.MERGE,CascadeType.REFRESH },optional =
	// false)
	// @JoinColumn(name="creator")
	// public VideoUploader getVideoUploader() {
	// return videoUploader;
	// }

	// public void setVideoUploader(VideoUploader videoUploader) {
	// this.videoUploader = videoUploader;
	// }

	@Override
	public String toString() {
		return "Video{" + "name='" + name + '\'' + ", nameEn='" + nameEn + '\''
				+ ", description='" + description + '\'' + ", tag='" + tag
				+ '\'' + ", url='" + url + '\''
				+ '\'' + ", flowStat='" + flowStat + '\'' + ", failReason='"
				+ failReason + '\'' + ", pixel='" + pixel + '\'' + ", width="
				+ width + ", height=" + height + ", playKey='" + playKey + '\''
				+ ", videoPic='" + videoPic + '\'' + ", orderNum=" + orderNum
				+ ", praiseCount=" + praiseCount + ", favoriteCount="
				+ favoriteCount + ", evaluationCount=" + evaluationCount
				+ ", playCount=" + playCount + ", duration='" + duration + '\''
				+ ", planPublishTime=" + planPublishTime + ", auditorId="
				+ auditorId + ", auditDateTime=" + auditDateTime
				+ ", longitude=" + longitude + ", latitude=" + latitude
				+ ", gd_longitude=" + gd_longitude + ", gd_latitude="
				+ gd_latitude + ", coordinateAddr='" + coordinateAddr + '\''
				+ ", playCountToday=" + playCountToday + ", playRateToday="
				+ playRateToday + ", isRecommend=" + isRecommend + ", actIds="
				+ actIds + ", praise=" + praise
				+ ", favorite=" + favorite + ", img='" + img + '\'' + '}';
	}

	public String getIntroductionMark() {
		return introductionMark;
	}

	public void setIntroductionMark(String introductionMark) {
		this.introductionMark = introductionMark;
	}

	public String getIsLogo() {
		return isLogo;
	}

	public void setIsLogo(String isLogo) {
		this.isLogo = isLogo;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getUploader() {
		return uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public int getPlayRateState() {
		return playRateState;
	}

	public void setPlayRateState(int playRateState) {
		this.playRateState = playRateState;
	}

	public String getActives() {
		return actives;
	}

	public void setActives(String actives) {
		this.actives = actives;
	}

	public int getAttentionAuthor() {
		return attentionAuthor;
	}

	public void setAttentionAuthor(int attentionAuthor) {
		this.attentionAuthor = attentionAuthor;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public Integer getHotPoint() {
		return hotPoint;
	}

	public void setHotPoint(Integer hotPoint) {
		this.hotPoint = hotPoint;
	}

	public String getActivityTitle() {
		return activityTitle;
	}

	public void setActivityTitle(String activityTitle) {
		this.activityTitle = activityTitle;
	}

	public String getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(String versionNum) {
		this.versionNum = versionNum;
	}

	public Integer getMp4Flg() {
		return mp4Flg;
	}

	public void setMp4Flg(Integer mp4Flg) {
		this.mp4Flg = mp4Flg;
	}

	public Double getMonthHotValue() {
		return monthHotValue;
	}

	public void setMonthHotValue(Double monthHotValue) {
		this.monthHotValue = monthHotValue;
	}

	public Double getDayHotValue() {
		return dayHotValue;
	}


	public int getIsForward() {
		return isForward;
	}

	public void setIsForward(int isForward) {
		this.isForward = isForward;
	}

	public Ruser getForwardUser() {
		return forwardUser;
	}

	public void setForwardUser(Ruser forwardUser) {
		this.forwardUser = forwardUser;
	}

	public String getForwardEvaluation() {
		return forwardEvaluation;
	}

	public void setForwardEvaluation(String forwardEvaluation) {
		this.forwardEvaluation = forwardEvaluation;
	}

	public ActivityVideo getActivityVideo() {
		return activityVideo;
	}

	public void setActivityVideo(ActivityVideo activityVideo) {
		this.activityVideo = activityVideo;
	}

	public Long getForwardUserId() {
		return forwardUserId;
	}

	public void setForwardUserId(Long forwardUserId) {
		this.forwardUserId = forwardUserId;
	}

	public Long getForwardTime() {
		if (forwardTime !=null){
			return forwardTime.getTime();
		}
		return 0l;
	}

	public void setForwardTime(Date forwardTime) {
		this.forwardTime = forwardTime;
	}

	public int getShowPlayCount() {
		if (publishTime != null) {
			Long interval = System.currentTimeMillis()-publishTime.getTime();
			if (interval > 30*1000) {
				int random = 0;
				try {
					random = getId().intValue()%10+playCount%10;
				} catch (Exception e) {
					e.printStackTrace();
				}
				showPlayCount = (playCount+1)*21+random;
			}
		}
		return showPlayCount;
	}

	public void setShowPlayCount(int showPlayCount) {
		this.showPlayCount = showPlayCount;
	}

	public Double getWeekHotValue() {
		return weekHotValue;
	}

	public void setWeekHotValue(Double weekHotValue) {
		this.weekHotValue = weekHotValue;
	}

	public Integer getRankAble() {
		return rankAble;
	}

	public void setRankAble(Integer rankAble) {
		this.rankAble = rankAble;
	}

	public void setDayHotValue(Double dayHotValue) {
		this.dayHotValue = dayHotValue;
	}

	public Integer getForwardCount() {
		return forwardCount;
	}

	public void setForwardCount(Integer forwardCount) {
		this.forwardCount = forwardCount;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getLiveNoticeId() {
		return liveNoticeId;
	}

	public void setLiveNoticeId(Long liveNoticeId) {
		this.liveNoticeId = liveNoticeId;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public static void main(String args[]){

	}

	public int getPlaybackWeight() {
		return playbackWeight;
	}

	public void setPlaybackWeight(int playbackWeight) {
		this.playbackWeight = playbackWeight;
	}
	
}
