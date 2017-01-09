package com.busap.vcs.constants;


/**
 * 中国新歌声--常量表
 *
 */
public class SingConstants {
	
	//新歌声学员id，set
	public static final String SING_MEMBER = "sing_member";
	
	//付费投票免费次数（针对某个人，某天）
	public static final String EXPENSE_VOTE_FREE_TIMES_ = "expense_vote_free_times_"; //默认1
	
	//付费投票对应的金币数
	public static final String EXPENSE_VOTE_DIAMOND_COUNT = "expense_vote_diamond_count"; //默认6
	
	//带CD时间的投票时间间隔，单位秒
	public static final String CD_VOTE_INTERVAL = "cd_vote_interval";  //默认10秒
	
	//判断用户在CD时间内是否投过票
	public static final String CD_VOTE_FLAG_ = "cd_vote_flag_";
	
	//CD投票系数
	public static final String CD_VOTE_RATIO = "cd_vote_ratio";   //默认10
	
	//付费投票系数
	public static final String EXPENSE_VOTE_RATIO = "expense_vote_ratio"; //默认6
	
	//礼物投票系数
	public static final String GIFT_VOTE_RATIO = "gift_vote_ratio";  //默认1
	
	//新歌声活动id
	public static final String SING_ACTIVITY_ID = "sing_activity_id";
	
	//学员榜的key
	public static final String MEMBER_RANK = "member_rank";
	//网综榜的key
	public static final String NORMAL_RANK = "normal_rank";
	//主播榜的key
	public static final String ANCHOR_RANK = "anchor_rank";
	//贡献榜的key
	public static final String CONTRIBUTE_RANK = "contribute_rank";
	//贡献榜总榜的key
    public static final String CONTRIBUTE_RANK_TOTAL = "contribute_rank_total";
	
	//学员榜的倒计时
	public static final String MEMBER_DATELINE = "member_dateline";
	//网综榜的倒计时
	public static final String NORMAL_DATELINE = "normal_dateline";
	//主播榜的倒计时
	public static final String ANCHOR_DATELINE = "anchor_dateline";
	//贡献榜的倒计时
	public static final String CONTRIBUTE_DATELINE = "contribute_dateline";
	//贡献榜总榜的倒计时
	public static final String CONTRIBUTE_DATELINE_TOTAL = "contribute_dateline_total";
	
	//主播榜的跳转url
	public static final String ANCHOR_RANK_URL = "anchor_rank_url";
	
	//贡献榜的跳转url
	public static final String CONTRIBUTE_RANK_URL = "contribute_rank_url";
	
	//网综人气榜
    public static final String WANGZONG_RANK = "wangzong_rank";
    
    //网综人气榜礼物及学员设置
    public static final String WANGZONG_INFO = "wangzong_info";
	
	

}
