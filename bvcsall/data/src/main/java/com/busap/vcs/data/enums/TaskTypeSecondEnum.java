package com.busap.vcs.data.enums;


import java.util.ArrayList;
import java.util.List;

/**
 * 任务子类型
 * @author zx
 *
 */
public enum TaskTypeSecondEnum {

	sign(TaskTypeOneEnum.special.getValue(), 51,"签到获取积分"),

	publishVideo(TaskTypeOneEnum.daily.getValue(), 1,"发布一个视频,赠送N积分"),
	
	joinActivity(TaskTypeOneEnum.daily.getValue(), 41,"参与M个活动,赠送N积分"),
	
	attentionYellowVip(TaskTypeOneEnum.daily.getValue(), 21,"关注黄V,赠送N积分"),
	
	attentionBlueVip(TaskTypeOneEnum.daily.getValue(), 22,"关注蓝V,赠送N积分"),
	
	attentionGreenVip(TaskTypeOneEnum.daily.getValue(), 23,"关注绿V,赠送N积分"),
	
	commentVideo(TaskTypeOneEnum.daily.getValue(), 31,"评论一次视频,赠送N积分"),

	praiseVideo(TaskTypeOneEnum.daily.getValue(), 32,"赞一次视频,赠送N积分"),
	
	forwardVideo(TaskTypeOneEnum.daily.getValue(), 33,"转发一次视频,赠送N积分"),
	/** type > 1000 定时任务不会删除 */
	specialActivity(TaskTypeOneEnum.limit.getValue(), 1001,"参加特定活动,赠送N积分"),

	perfectPersonalInfo(TaskTypeOneEnum.once.getValue(), 2001,"完善个人资料,赠送N积分"),

	//经与运营协商，现在只保留这一种类型，后两种屏蔽
	fans(TaskTypeOneEnum.once.getValue(), 2021,"粉丝达到M,赠送N积分"),

	fans100(TaskTypeOneEnum.once.getValue(), 2022,"粉丝达到100,赠送N积分"),
	
	fans200(TaskTypeOneEnum.once.getValue(), 2023,"粉丝达到200,赠送N积分"),

	lottery(TaskTypeOneEnum.special.getValue(), 52, "抽奖活动获取积分");
	
	
//	special(4,"特殊任务");
	
	private Integer parentType;
	
	private Integer value;
	
	private String description;


	private TaskTypeSecondEnum(Integer parentType,Integer value,String description){

		this.parentType=parentType;
		this.value=value;
		this.description=description;

	}


	public Integer getValue() {
		return value;
	}


	public void setValue(Integer value) {
		this.value = value;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Integer getParentType() {
		return parentType;
	}


	public void setParentType(Integer parentType) {
		this.parentType = parentType;
	}


	public static TaskTypeSecondEnum getTaskTypeByValue(int value) {
		for (TaskTypeSecondEnum taskType : TaskTypeSecondEnum.values()) {
			if (taskType.getValue() == value) {
				return taskType;
			}
		}
		return null;
	}

	public static List<Integer> getDailyTaskValues(boolean includeSign) {
		List<Integer> result = new ArrayList<Integer>();
		if (includeSign) {
			result.add(sign.getValue());
		}
		for (TaskTypeSecondEnum taskType : TaskTypeSecondEnum.values()) {
			if (TaskTypeOneEnum.daily.getValue().equals(taskType.getParentType())) {
				result.add(taskType.getValue());
			}
		}
		return result;
	}

	public static List<TaskTypeSecondEnum> getDailyTasks() {
		List<TaskTypeSecondEnum> result = new ArrayList<TaskTypeSecondEnum>();
		for (TaskTypeSecondEnum taskType : TaskTypeSecondEnum.values()) {
			if (TaskTypeOneEnum.daily.getValue().equals(taskType.getParentType())) {
				result.add(taskType);
			}
		}
		return result;
	}
}
