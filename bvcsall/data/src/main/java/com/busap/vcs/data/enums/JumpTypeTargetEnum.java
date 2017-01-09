package com.busap.vcs.data.enums;


/**
 * 任务跳转，目标id枚举
 * @author zx
 *
 */
public enum JumpTypeTargetEnum {
	
	

	publisVideo("task", 1,"发布一个视频,赠送N积分","打开拍摄页"),
	
	joinActivity("task", 41,"参与M个活动,赠送N积分","切换到活动页首页"),
	
	attentionYellowVip("task", 21,"关注黄V,赠送N积分","打开“您可能感兴趣的人”"),
	
	attentionBlueVip("task", 22,"关注蓝V,赠送N积分","打开“您可能感兴趣的人”"),
	
	attentionGreenVip("task", 23,"关注绿V,赠送N积分","打开“您可能感兴趣的人”"),
	
	commentVideo("task", 31,"评论一次视频,赠送N积分","打开首页推荐第一个分类"),
	
	praiseVideo("task", 32,"赞一次视频,赠送N积分","打开首页推荐第一个分类"),
	
	fowardVideo("task", 33,"转发一次视频,赠送N积分","打开首页推荐第一个分类"),
	
	specialActivity("task", 1001,"参加特定活动,赠送N积分","打开XXX活动详情页"),              
	
	perfectPersonalInfo("task", 2001,"完善个人资料,赠送N积分","打开个人资料页"),
	
	fans50("task", 2021,"粉丝达到50,赠送N积分","顶部提示：需要X个粉丝可完成此任务"),
	
	fans100("task", 2022,"粉丝达到100,赠送N积分","顶部提示：需要X个粉丝可完成此任务"),
	
	fans200("task", 2023,"粉丝达到200,赠送N积分","顶部提示：需要X个粉丝可完成此任务"),
	
	sign("task", 51,"签到任务","");
	
	
	private String type;
	
	private Integer targetId;
	
	private String description;
	
	
	private JumpTypeTargetEnum(String type,Integer targetId,String description,String forwardPage){
		
		this.type=type;
		this.targetId=targetId;
		this.description=description;
		
	}



	public Integer getTargetId() {
		return targetId;
	}

	public void setTargetId(Integer targetId) {
		this.targetId = targetId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
