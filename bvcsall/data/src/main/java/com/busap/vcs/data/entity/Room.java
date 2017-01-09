package com.busap.vcs.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

//直播房间
@Entity
@Table(name = "room")
public class Room extends BaseEntity {

	private static final long serialVersionUID = 7014000160807465234L;

	// 房间主题
	@Column(nullable = true)
	private String title;

	// 房间封面
	@Column(nullable = true)
	private String roomPic;
	
	@Transient
	private String anchorName; // 主播昵称
	
	@Transient
	private String anchorPic; // 主播头像
	
	@Transient
	private String anchorVstat; // 主播vip等级
	
	@Transient
	private String anchorSignature; // 主播签名

	// 结束时间
	@Column(name = "finish_at", columnDefinition = "datetime NULL", nullable = true)
	private Date finishDate;

	// 直播时长
	@Column(nullable = true)
	private Long duration;// 单位秒

	// 房间状态
	private int status; // 1:直播中，0：已结束

	// 最高访问人数
	@Column(nullable = true)
	private int maxAccessNumber = 0;

	// 实时在线人数
	@Transient
	@Column(nullable = true)
	private int onlineNumber = 0;

	// 赞数
	@Column(nullable = true)
	private int praiseNumber = 0;
	
	// 马甲赞数
	@Column(nullable = true)
	private int mjPraiseNumber = 0;
	
	// 附加数量，用于排序（排序值=additionalNumber + onlineNumber）
	private int additionalNumber;
	
	//直播流json
	@Column(name = "stream_json", length=500, nullable = true)
	private String streamJson;
	
	private String rtmpLiveUrl;
	
	private String hlsLiveUrl;
	
	private Long liveActivityId;//直播活动id
	
	// 每场收入的礼物数
	@Column(nullable = true)
	private Integer giftNumber = 0;
	
	// 每场收入的金豆数
	@Column(nullable = true)
	private Integer pointNumber = 0;
	
	@Column(name = "live_type",columnDefinition = "int(4) NULL DEFAULT 0",nullable=true)
	private Integer liveType; //直播类型 0：手机 1：摄像机
	
	private String localOutIp;// 主播出口ip
	
	private Integer chatCount = 0; 		//发言次数
	
	private Integer anchorChatCount = 0;//主播发言次数
	
	private Integer normalUserCount = 0;//真实观看人数
	
	private String appVersion; //版本信息
	
	private String platform; //平台信息：ios 或者 android
	
	private int uv;
	
	private String channel;//用户注册渠道
	
	private String longitude; //经度
	
	private String  latitude; // 维度
	
	private String area; //地区
	
	@Transient
	private String linkMicPushUrl; //连麦推流地址
	
	@Transient
	private Integer canLinkMic; //是否可以连麦，1：可以，0：不可以
	
	@Transient
	private String streamId;
	

	@Column(name = "persistent_id",columnDefinition = "varchar(100) NULL",nullable=true)
	private String persistentId; //网宿提供，录制任务的唯一标识，回放回调使用

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRoomPic() {
		return roomPic;
	}

	public void setRoomPic(String roomPic) {
		this.roomPic = roomPic;
	}

	public Long getFinishDate() { //返回结束时间的毫秒
		if (finishDate != null)
			return finishDate.getTime();
		else
			return null;
	}
	
	public Date getFinishDateStr() { //返回结束时间的Date类型
        return finishDate;
    }

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getMaxAccessNumber() {
		return maxAccessNumber;
	}

	public void setMaxAccessNumber(int maxAccessNumber) {
		this.maxAccessNumber = maxAccessNumber;
	}

	public int getOnlineNumber() {
		return onlineNumber;
	}

	public void setOnlineNumber(int onlineNumber) {
		this.onlineNumber = onlineNumber;
	}

	public int getPraiseNumber() {
		return praiseNumber;
	}

	public void setPraiseNumber(int praiseNumber) {
		this.praiseNumber = praiseNumber;
	}

	public int getAdditionalNumber() {
		return additionalNumber;
	}

	public void setAdditionalNumber(int additionalNumber) {
		this.additionalNumber = additionalNumber;
	}

	public String getAnchorName() {
		return anchorName;
	}

	public void setAnchorName(String anchorName) {
		this.anchorName = anchorName;
	}

	public String getAnchorPic() {
		return anchorPic;
	}

	public void setAnchorPic(String anchorPic) {
		this.anchorPic = anchorPic;
	}

	public String getAnchorVstat() {
		return anchorVstat;
	}

	public void setAnchorVstat(String anchorVstat) {
		this.anchorVstat = anchorVstat;
	}

	public String getAnchorSignature() {
		return anchorSignature;
	}

	public void setAnchorSignature(String anchorSignature) {
		this.anchorSignature = anchorSignature;
	}

	public int getMjPraiseNumber() {
		return mjPraiseNumber;
	}

	public void setMjPraiseNumber(int mjPraiseNumber) {
		this.mjPraiseNumber = mjPraiseNumber;
	}

	public String getStreamJson() {
		return streamJson;
	}

	public void setStreamJson(String streamJson) {
		this.streamJson = streamJson;
	}

	public String getRtmpLiveUrl() {
		return rtmpLiveUrl;
	}

	public void setRtmpLiveUrl(String rtmpLiveUrl) {
		this.rtmpLiveUrl = rtmpLiveUrl;
	}

	public String getHlsLiveUrl() {
		return hlsLiveUrl;
	}

	public void setHlsLiveUrl(String hlsLiveUrl) {
		this.hlsLiveUrl = hlsLiveUrl;
	}

	public Long getLiveActivityId() {
		return liveActivityId;
	}

	public void setLiveActivityId(Long liveActivityId) {
		this.liveActivityId = liveActivityId;
	}

	public Integer getGiftNumber() {
		return giftNumber;
	}

	public void setGiftNumber(Integer giftNumber) {
		this.giftNumber = giftNumber;
	}

	public Integer getPointNumber() {
		return pointNumber;
	}

	public void setPointNumber(Integer pointNumber) {
		this.pointNumber = pointNumber;
	}

	public Integer getLiveType() {
		return liveType;
	}

	public void setLiveType(Integer liveType) {
		this.liveType = liveType;
	}

	public String getLocalOutIp() {
		return localOutIp;
	}

	public void setLocalOutIp(String localOutIp) {
		this.localOutIp = localOutIp;
	}

	public Integer getChatCount() {
		return chatCount;
	}

	public void setChatCount(Integer chatCount) {
		this.chatCount = chatCount;
	}

	public Integer getAnchorChatCount() {
		return anchorChatCount;
	}

	public void setAnchorChatCount(Integer anchorChatCount) {
		this.anchorChatCount = anchorChatCount;
	}

	public Integer getNormalUserCount() {
		return normalUserCount;
	}

	public void setNormalUserCount(Integer normalUserCount) {
		this.normalUserCount = normalUserCount;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public int getUv() {
		return uv;
	}

	public void setUv(int uv) {
		this.uv = uv;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPersistentId() {
		return persistentId;
	}

	public void setPersistentId(String persistentId) {
		this.persistentId = persistentId;
	}

	public Integer getCanLinkMic() {
		return canLinkMic;
	}

	public void setCanLinkMic(Integer canLinkMic) {
		this.canLinkMic = canLinkMic;
	}

	public String getLinkMicPushUrl() {
		return linkMicPushUrl;
	}

	public void setLinkMicPushUrl(String linkMicPushUrl) {
		this.linkMicPushUrl = linkMicPushUrl;
	}

	public String getStreamId() {
		return streamId;
	}

	public void setStreamId(String streamId) {
		this.streamId = streamId;
	}
	
	
}
