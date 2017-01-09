package com.busap.vcs.data.entity;
/**
 * 消费记录
 */
import java.util.Date;



public class ConsumeRecordVo  {

	
	/**
	 * 
	 */
	
	private String nick;	//用户名称
	
	
	
	private String name;	//赠送者
	
	private String giftName;	//礼物名称

//	private Long giftId;		//礼物或道具Id
	
	private Integer number;		//消费数量
	
	private Date createDate;
	
	private Integer points;		//主播收到点数
	
//	private Integer diamondCount;//消耗砖石数
	
	private Double money;//消耗砖石数
	
//	private Integer price;		//价格
//	
//	
//	
//	
//	
//	private Long roomId;		//发生直播房间
//	
//	private Long sender;		//赠送者
//	
//	private Long reciever;		//接收者
	
	
	
	
	public ConsumeRecordVo() {
	}

	public ConsumeRecordVo(Date createDate,Integer number,  Double money,
			Integer points, String giftName, String name,String nick) {
		this.createDate = createDate;
		this.number = number;
//		this.giftId = giftId;
//		this.diamondCount = diamondCount;
		this.points = points;
		this.giftName = giftName;
		this.name = name;
		this.nick = nick;
		this.money = money;
	}

	

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

//	public Long getGiftId() {
//		return giftId;
//	}
//
//	public void setGiftId(Long giftId) {
//		this.giftId = giftId;
//	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

//	public Integer getPrice() {
//		return price;
//	}
//
//	public void setPrice(Integer price) {
//		this.price = price;
//	}

//	public Integer getDiamondCount() {
//		return diamondCount;
//	}
//
//	public void setDiamondCount(Integer diamondCount) {
//		this.diamondCount = diamondCount;
//	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

//	public Long getRoomId() {
//		return roomId;
//	}
//
//	public void setRoomId(Long roomId) {
//		this.roomId = roomId;
//	}
//
//	public Long getSender() {
//		return sender;
//	}
//
//	public void setSender(Long sender) {
//		this.sender = sender;
//	}
//
//	public Long getReciever() {
//		return reciever;
//	}
//
//	public void setReciever(Long reciever) {
//		this.reciever = reciever;
//	}

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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}


	
}
