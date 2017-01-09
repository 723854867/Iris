package com.busap.vcs.data.vo;



public class FansVO {
	
	private Long  fansId; //粉丝的用户id    
	private String createDate;//创建时间
	private String name;//粉丝的用户昵称
	private String pic;//粉丝的用户头像
	private String dataFrom;//数据来源
	private int isAttention; //是否关注了该粉丝，1：是，0：否
	private int vstat;
	private Double dayPopularity;//人气
	private Double monthPopularity;//月人气
	
	public Long getFansId() {
		return fansId;
	}
	public void setFansId(Long fansId) {
		this.fansId = fansId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public int getIsAttention() {
		return isAttention;
	}
	public void setIsAttention(int isAttention) {
		this.isAttention = isAttention;
	}
	public String getDataFrom() {
		return dataFrom;
	}
	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public int getVstat() {
		return vstat;
	}
	public void setVstat(int vstat) {
		this.vstat = vstat;
	}
	public Double getDayPopularity() {
		return dayPopularity;
	}
	public void setDayPopularity(Double dayPopularity) {
		this.dayPopularity = dayPopularity;
	}

	public Double getMonthPopularity() {
		return monthPopularity;
	}

	public void setMonthPopularity(Double monthPopularity) {
		this.monthPopularity = monthPopularity;
	}
}
