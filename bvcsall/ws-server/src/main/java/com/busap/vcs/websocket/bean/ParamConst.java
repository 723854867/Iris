package com.busap.vcs.websocket.bean;

public class ParamConst {
	//key--未审核视频id in redis 
	public static final String VIDEO_CHECK = "Video_Check";
	
	//key--未处理直播投诉 in redis 
	public static final String LC_CHECK = "LC_CHECK";

	//存放违规(涉黄暴恐)信息key SET
	public static final String IRREGULARITY_IMAGE = "IRREGULARITY_IMAGE";

	//存放违规(涉黄暴恐)信息key Map
	public static final String IRREGULARITY_IMAGE_STREAMID = "IRREGULARITY_IMAGE_STREAMID_";

	public static final String CHECK_GROUP_COUNT = "CHECK_GROUP_COUNT";

	public static final String CHECK_GROUP = "CHECK_GROUP";

	public static final String NEWEST_LIVE_ROOM = "NEWEST_LIVE_ROOM";

	public static final String NEW_LIVE_ROOM = "NEW_LIVE_ROOM";
}
