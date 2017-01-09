package com.busap.vcs.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "task")
public class Task extends BaseEntity{  
	 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9222591131406572054L;
	
	
	@Column(name = "description",columnDefinition = "varchar(1024)  NULL ",nullable=true)
	private String  description;// 描述
	
	@Column(name = "title",columnDefinition = "varchar(512)  NULL ",nullable=true)
	private String  title;// 标题
	
	@Column(name = "icon",columnDefinition = "varchar(1024)  NULL ",nullable=true)
	private String  icon; // 图片路径
	
	private String  typeOne;//大类型
	
	private String  typeTwo;//小类型
	
	private String   jumpType;//跳转类型
	
	private String   jumpTargetId;//跳转目标id
	
	private String   taskValue;//任务值
	
	private String   num;//需完成个数
	
	private Long   integral;//任务积分
	
	private Long   previousTaskId;//前置任务编号
	
	private Long   weight;//权重
	
	private Integer status;//状态  0:进行中   1：删除
	
    private Date deadLine;//过期时间
    
    @Transient
    private String deadLineStr;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTypeOne() {
		return typeOne;
	}

	public void setTypeOne(String typeOne) {
		this.typeOne = typeOne;
	}

	public String getTypeTwo() {
		return typeTwo;
	}

	public void setTypeTwo(String typeTwo) {
		this.typeTwo = typeTwo;
	}

	public String getJumpType() {
		return jumpType;
	}

	public void setJumpType(String jumpType) {
		this.jumpType = jumpType;
	}

	public String getJumpTargetId() {
		return jumpTargetId;
	}

	public void setJumpTargetId(String jumpTargetId) {
		this.jumpTargetId = jumpTargetId;
	}

	public String getTaskValue() {
		return taskValue;
	}

	public void setTaskValue(String taskValue) {
		this.taskValue = taskValue;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public Long getIntegral() {
		return integral;
	}

	public void setIntegral(Long integral) {
		this.integral = integral;
	}

	public Long getPreviousTaskId() {
		return previousTaskId;
	}

	public void setPreviousTaskId(Long previousTaskId) {
		this.previousTaskId = previousTaskId;
	}

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(Date deadLine) {
		this.deadLine = deadLine;
	}

	public String getDeadLineStr() {
		return deadLineStr;
	}

	public void setDeadLineStr(String deadLineStr) {
		this.deadLineStr = deadLineStr;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	
	

}
