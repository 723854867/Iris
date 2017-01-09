package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.busap.vcs.data.enums.Sex;

import java.math.BigDecimal;

//主播
@Entity
@Table(name = "anchor")
public class Anchor extends BaseEntity {


	private static final long serialVersionUID = 57459831939209872L;

	// 七牛直播流id
	@Column(nullable = true)
	private String qiniuStreamId;
	
	//直播流id(非七牛)
	@Column(nullable = true)
	private String streamId;
	
	//主播点数
	private int pointCount = 0;
	//锁定点数，用于结算中锁定，防止重复结算使用
	private Integer lockPoints = 0;
	//已结算点数
	private Integer settledPoints = 0;
	
	//真实姓名
	@Column(nullable = true)
	private String realName;
	
	//性别
	@Column(nullable = true)
	private String sex=Sex.男.getName();
	
	//手机号
	@Column(nullable = true)
	private String phone;
	
	//认证状态，0：未认证，1：认证通过，-1：认证失败
	@Column(nullable = true,columnDefinition = "int(8) DEFAULT -1")
	private int status = 0;
	
	//qq号码
	@Column(nullable = true)
	private String qq;
	
	//微信号码
	@Column(nullable = true)
	private String wechat;
	
	//银行卡号
	@Column(nullable = true)
	private String bankNumber;
	
	//开户行省
	@Column(nullable = true)
	private String bankProvince;
	
	//开户行名称
	@Column(nullable = true)
	private String bankName;
	
	//开户行市
	@Column(nullable = true)
	private String bankCity;
	
	//开户行地址
	@Column(nullable = true)
	private String bankAddress;
	
	//证件类型，1：身份证，2：护照，3：台胞证
	@Column(nullable = true,columnDefinition = "int(8) DEFAULT 1")
	private int certificateType = 1;
	
	//证件号码
	@Column(nullable = true)
	private String certificateNumber;
	
	//证件照片1
	@Column(nullable = true)
	private String picOne;
	
	//证件照片2
	@Column(nullable = true)
	private String picTwo;
	
	//证件照片3
	@Column(nullable = true)
	private String picThree;
	
	@Column(nullable = true)
	private String rejectReason;
	
	//主播总点数（不受提现影响）
	private Integer totalPointCount = 0;

	//主播底薪
	@Column(name = "basic_salary",columnDefinition = "decimal(10,2) NULL",nullable=true)
	private BigDecimal basicSalary;

	//开户支行名称
	@Column(name = "branch_bank_name",columnDefinition = "varchar(255) NULL",nullable=true)
	private String branchBankName;

	public String getQiniuStreamId() {
		return qiniuStreamId;
	}

	public void setQiniuStreamId(String qiniuStreamId) {
		this.qiniuStreamId = qiniuStreamId;
	}

	public int getPointCount() {
		return pointCount;
	}

	public void setPointCount(int pointCount) {
		this.pointCount = pointCount;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public int getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(int certificateType) {
		this.certificateType = certificateType;
	}

	public String getPicOne() {
		return picOne;
	}

	public void setPicOne(String picOne) {
		this.picOne = picOne;
	}

	public String getPicTwo() {
		return picTwo;
	}

	public void setPicTwo(String picTwo) {
		this.picTwo = picTwo;
	}

	public String getPicThree() {
		return picThree;
	}

	public void setPicThree(String picThree) {
		this.picThree = picThree;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public Integer getLockPoints() {
		return lockPoints;
	}

	public void setLockPoints(Integer lockPoints) {
		this.lockPoints = lockPoints;
	}

	public Integer getSettledPoints() {
		return settledPoints;
	}

	public void setSettledPoints(Integer settledPoints) {
		this.settledPoints = settledPoints;
	}

	public String getStreamId() {
		return streamId;
	}

	public void setStreamId(String streamId) {
		this.streamId = streamId;
	}

	public Integer getTotalPointCount() {
		return totalPointCount;
	}

	public void setTotalPointCount(Integer totalPointCount) {
		this.totalPointCount = totalPointCount;
	}

	public String getBankNumber() {
		return bankNumber;
	}

	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}

	public String getBankProvince() {
		return bankProvince;
	}

	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}

	public String getBankCity() {
		return bankCity;
	}

	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public BigDecimal getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(BigDecimal basicSalary) {
		this.basicSalary = basicSalary;
	}

	public String getBranchBankName() {
		return branchBankName;
	}

	public void setBranchBankName(String branchBankName) {
		this.branchBankName = branchBankName;
	}
}
