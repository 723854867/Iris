package com.busap.vcs.data.entity;
/**
 * 消费记录
 */
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "consume_record")
public class ConsumeRecord extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2742126297130684215L;
	
	private Long giftId;		//礼物或道具Id
	
	private Integer number;		//消费数量
	
	private Integer price;		//价格
	
	private Integer diamondCount;//消耗砖石数
	
	private Integer points;		//主播收到点数
	
	private Long roomId;		//发生直播房间
	
	private Long sender;		//赠送者
	
	private Long reciever;		//接收者
	
	private String giftName;	//礼物名称
	
	@Transient
	private String senderName;		//赠送者名称
	
	@Transient
	private String nick;
	
	private Double money;   //用户消耗对应的人民币
	
	private Double inMoney;  //主播收益对应的人民币
	
	private String appVersion; //版本信息
	
	private String platform; //平台信息：ios 或者 android
	
	private String channel; //用户注册渠道
	
	private Integer diamondRemand;//当前余额
	
	
	public ConsumeRecord() {
	}

	public ConsumeRecord(Date createDate,Integer number, Long giftId, Integer diamondCount,
			Integer points, String giftName, String name) {
		this.createDate = createDate;
		this.number = number;
		this.giftId = giftId;
		this.diamondCount = diamondCount;
		this.points = points;
		this.giftName = giftName;
		this.name = name;
	}

	@Transient
	private String name;	//礼物名称

	public Long getGiftId() {
		return giftId;
	}

	public void setGiftId(Long giftId) {
		this.giftId = giftId;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getDiamondCount() {
		return diamondCount;
	}

	public void setDiamondCount(Integer diamondCount) {
		this.diamondCount = diamondCount;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public Long getSender() {
		return sender;
	}

	public void setSender(Long sender) {
		this.sender = sender;
	}

	public Long getReciever() {
		return reciever;
	}

	public void setReciever(Long reciever) {
		this.reciever = reciever;
	}

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Double getInMoney() {
		return inMoney;
	}

	public void setInMoney(Double inMoney) {
		this.inMoney = inMoney;
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

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Integer getDiamondRemand() {
		return diamondRemand;
	}

	public void setDiamondRemand(Integer diamondRemand) {
		this.diamondRemand = diamondRemand;
	}
	
	
}
