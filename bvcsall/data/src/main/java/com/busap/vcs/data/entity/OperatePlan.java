package com.busap.vcs.data.entity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "operate_plan")
public class OperatePlan extends BaseEntity {

	private static final long serialVersionUID = 8334005543772673477L;
	
	@Column(name = "plan_type",columnDefinition = "varchar(20) NOT NULL ",nullable=false)
	private String planType;	//计划类型
	@Column(name = "target_num",columnDefinition = "int NOT NULL ",nullable=false)
	private Integer targetNum;	//计划目标数
	@Column(name = "start_time",columnDefinition = "timestamp NOT NULL",nullable=true)
	private Date startTime;		//计划开始时间
	@Column(name = "end_time",columnDefinition = "timestamp NOT NULL",nullable=true)
	private Date endTime;		//计划结束时间
	@Column(name = "time_unit",columnDefinition = "varchar(10) NOT NULL",nullable=true)
	private String timeUnit;	//计划时间单位、week/month
	
	@Transient
	private String startDate;	//开始时间，输入参数
	@Transient
	private String endDate;		//开始时间，输入参数
	@Transient
	private Integer actualNum; 	//计划实际完成数
	
	public String getPlanType() {
		return planType;
	}
	public void setPlanType(String planType) {
		this.planType = planType;
	}
	public Integer getTargetNum() {
		return targetNum;
	}
	public void setTargetNum(Integer targetNum) {
		this.targetNum = targetNum;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		this.startDate = df.format(startTime);
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		this.endDate = df.format(endTime);
		this.endTime = endTime;
	}
	public String getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.startTime = df.parse(startDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.endTime = df.parse(endDate+" 23:59:59");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.endDate = endDate;
	}
	public Integer getActualNum() {
		return actualNum;
	}
	public void setActualNum(Integer actualNum) {
		this.actualNum = actualNum;
	}
	
}
