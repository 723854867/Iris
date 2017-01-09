package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "live_setting")
public class LiveSetting extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3706487830032638205L;
	
	private String name;		//设置名称
	
	private Integer majiaCount;	//进入马甲总数  上限
	
	private Integer majiaPeriod;//最小马甲进入时间间隔（秒）
	
	private Integer maxMajiaPeriod;//最大进入时间间隔
	
	private Integer status;		//状态 0 有效，1 失效
	
	private Long typeId;		//自动发言类型
	
	private Integer majiaCountBegin;//自动加数起始马甲数
	
	private Integer maxUserCount;//自动加数最大值
	
	private Integer userCountStep;//自动加数每次增加数量
	
	private Integer userCountPeriod;//自动加数最小间隔时间
	
	private Integer maxUserCountPeriod;//自动加数最大间隔时间
	
	private Integer userCountStat;//自动加数开启状态，0 开启，1关闭
	
	@Transient
	private String typeName;	//自动发言类型名称

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMajiaCount() {
		return majiaCount;
	}

	public void setMajiaCount(Integer majiaCount) {
		this.majiaCount = majiaCount;
	}

	public Integer getMajiaPeriod() {
		return majiaPeriod;
	}

	public void setMajiaPeriod(Integer majiaPeriod) {
		this.majiaPeriod = majiaPeriod;
	}

	public Integer getMaxMajiaPeriod() {
		return maxMajiaPeriod;
	}

	public void setMaxMajiaPeriod(Integer maxMajiaPeriod) {
		this.maxMajiaPeriod = maxMajiaPeriod;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getMajiaCountBegin() {
		return majiaCountBegin;
	}

	public void setMajiaCountBegin(Integer majiaCountBegin) {
		this.majiaCountBegin = majiaCountBegin;
	}

	public Integer getMaxUserCount() {
		return maxUserCount;
	}

	public void setMaxUserCount(Integer maxUserCount) {
		this.maxUserCount = maxUserCount;
	}

	public Integer getUserCountStep() {
		return userCountStep;
	}

	public void setUserCountStep(Integer userCountStep) {
		this.userCountStep = userCountStep;
	}

	public Integer getUserCountPeriod() {
		return userCountPeriod;
	}

	public void setUserCountPeriod(Integer userCountPeriod) {
		this.userCountPeriod = userCountPeriod;
	}

	public Integer getMaxUserCountPeriod() {
		return maxUserCountPeriod;
	}

	public void setMaxUserCountPeriod(Integer maxUserCountPeriod) {
		this.maxUserCountPeriod = maxUserCountPeriod;
	}

	public Integer getUserCountStat() {
		return userCountStat;
	}

	public void setUserCountStat(Integer userCountStat) {
		this.userCountStat = userCountStat;
	}
	
}
