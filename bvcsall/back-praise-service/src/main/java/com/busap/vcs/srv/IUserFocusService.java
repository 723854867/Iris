package com.busap.vcs.srv;

import com.busap.vcs.bean.VideoTaskItem;

/**
 * 视频赞业务逻辑
 * @author wangyongfei
 *
 */
public interface IUserFocusService {

	/**
	 * 对指定用户进行关注
	 * @param vti 消息对象
	 */
	public boolean doFocus(VideoTaskItem vti );

	public boolean checkTodayOverSize(long uid);
}
