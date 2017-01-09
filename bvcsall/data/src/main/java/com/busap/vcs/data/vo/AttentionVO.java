package com.busap.vcs.data.vo;



public class AttentionVO {

	private Long  id; // 自增主键
	private Long  attentionId; //被关注的用户id    
	private String createDate;//创建时间
	private String name;//被关注的用户昵称
	private String pic;//被关注的用户头像
	private String dataFrom;//数据来源
	private String signature;//签名
	private int isAttention = 0;
	private int vstat;
	private Double dayPopularity;//人气
	private Double monthPopularity;//人气 ,月

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAttentionId() {
		return attentionId;
	}

	public void setAttentionId(Long attentionId) {
		this.attentionId = attentionId;
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

	public int getIsAttention() {
		return isAttention;
	}

	public void setIsAttention(int isAttention) {
		this.isAttention = isAttention;
	}

	public int getVstat() {
		return vstat;
	}

	public void setVstat(int vstat) {
		this.vstat = vstat;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
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
