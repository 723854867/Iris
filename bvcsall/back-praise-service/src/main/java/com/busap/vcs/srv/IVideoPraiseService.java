package com.busap.vcs.srv;

/**
 * 视频赞业务逻辑
 * @author wangyongfei
 *
 */
public interface IVideoPraiseService {

	/**
	 * 对指定视频进行赞
	 * @param videoId 视频ID
	 */
	public void doPraise(long videoId,int min,int max);
	
}
