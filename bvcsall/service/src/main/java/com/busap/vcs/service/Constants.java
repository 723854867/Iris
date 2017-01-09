package com.busap.vcs.service;

/**
 * Created by
 * User: djyin
 * Date: 12/6/13
 * Time: 9:56 AM
 * <p/>
 * 记录一些多个包协同使用的常量
 */
public class Constants extends com.busap.vcs.base.Constants {
	//redis存储是否存在5分钟还未审核的视频Key头
		public static final String VIDEO_CHECK = "Video_Check";

	public static final int continueSign = 0;

	public static final int firstSign = 1;

	public static final int alreadySign = 2;

	public static final Long NORMAL = 1L;

	public static final Long DELETE = 0L;

//	public static enum DailyTaskType {
//		ACTIVITY(1), ATTENTION(2), COMMENT(3), FORWARD(4), PRAISE(5), RELEASED(6);
//
//		private int order;
//
//		DailyTaskType(int order) {
//			this.order = order;
//		}
//
//		public int getOrder() {
//			return order;
//		}
//
//		public void setOrder(int order) {
//			this.order = order;
//		}
//	}

	// 每日任务是否完成，
	public static enum TaskFinish {
		WORKING(0),
		FINISH(1),
		RECEIVE(2);

		private int status;

		TaskFinish(int status) {
			this.status = status;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public static TaskFinish getTaskFinish(int status) {
			for (TaskFinish taskStatus : TaskFinish.values()) {
				if (taskStatus.getStatus() == status) {
					return taskStatus;
				}
			}
			return null;
		}
	}

	/**
	 * 临时读取，备用，之后会改为从后台配置读取
	 */
	public static enum FansCountStep {

		COUNT_STEP1(50, 10),
		COUNT_STEP2(100, 50),
		COUNT_STEP3(200, 100);

		private int count;
		private int integral;

		FansCountStep(int count, int integral) {
			this.count = count;
			this.integral = integral;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public int getIntegral() {
			return integral;
		}

		public void setIntegral(int integral) {
			this.integral = integral;
		}
	}

	public static enum GiveDiamondType {
		SHARE(1, "share"),
		LIVE(2, "live"),
		REGISTER_360(3,"register_360");//360渠道注册，送的金币

		private int type;
		private String channel;

		GiveDiamondType(int type, String channel) {
			this.type = type;
			this.channel = channel;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getChannel() {
			return channel;
		}

		public void setChannel(String channel) {
			this.channel = channel;
		}
	}

}
