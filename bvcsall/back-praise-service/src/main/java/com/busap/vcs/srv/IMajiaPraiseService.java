package com.busap.vcs.srv;

import com.busap.vcs.bean.VideoTaskItem;

/**
 * 视频赞业务逻辑
 * @author wangyongfei
 *
 */
public interface IMajiaPraiseService {

	/**
	 * 对指定用户进行关注
	 * @param uid 用户ID
	 */
	public boolean doPraise(long vid,int praiseNumber );
	
}
