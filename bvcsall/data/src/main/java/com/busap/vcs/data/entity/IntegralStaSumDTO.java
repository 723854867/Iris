package com.busap.vcs.data.entity;



public class IntegralStaSumDTO  {

	
	private Long signSum=0l;
	
	private Long dailySum=0l;
	
	private Long visiteSum=0l;
	
	private String createTime;

	public Long getSignSum() {
		return signSum;
	}

	public void setSignSum(Long signSum) {
		this.signSum = signSum;
	}

	public Long getDailySum() {
		return dailySum;
	}

	public void setDailySum(Long dailySum) {
		this.dailySum = dailySum;
	}

	public Long getVisiteSum() {
		return visiteSum;
	}

	public void setVisiteSum(Long visiteSum) {
		this.visiteSum = visiteSum;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	

}
