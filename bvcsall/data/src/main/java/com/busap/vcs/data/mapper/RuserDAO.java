package com.busap.vcs.data.mapper;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.model.ExportWopaiNormalUser;
import com.busap.vcs.data.model.ExportWopaiUser;
import com.busap.vcs.data.model.RuserDisplay;
import org.apache.ibatis.annotations.Param;

import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.vo.InviteFriendVO;

public interface RuserDAO {
	 
	public List<Ruser> getRusers(List<Long> ids);
	
	public void incAllVideoCount(@Param("uid")Long uid);
	
	public void decVideoCountAndAllCount(@Param("uid")Long uid);
	
	public void decAllVideoCount(@Param("uid")Long uid);
	/**
	 * 查询非马甲用户
	 * @param params
	 * @return
	 */
	public List<Ruser> getNormalUser(Map<String,Object> params);
	public List<Ruser> findUserByPhoneOrName(Map<String,Object> params);
	
	public Integer getNormalUserCount(Map<String,Object> params);
	
	/**
	 * 查询用户同学录中的我拍账户
	 * @param usernames
	 * @return
	 */
	public List<InviteFriendVO> getRusersFromContacts(Map<String,Object> params);
	
	/**
	 * 查询用户第三方好友中的我拍账户
	 * @param thirdUsernames
	 * @return
	 */
	public List<InviteFriendVO> getRusersFromThirdPart(Map<String,Object> params);
	//执行存储过程，计算用户当日人气
	public void execDayUserPopularityProc(Map<String,Object> params);
	//当日人气排行榜
	public List<Ruser> findDayUserPopularityTop50(Integer count);
	//最近5天注册用户数
	public List<Map<String,Object>> last5DayUserNum(String date);
	//用户当日上传视频数
	public Integer dayUserVideos(Long uid);
	//用户当日粉丝数
	public Integer dayUserFans(Long uid);
//查找需要重算人气的用户id
	public List<Long> findCalculateRankUsers();

	List<Ruser> selectRusers(Map<String,Object> params);

	List<RuserDisplay> selectWopaiRusers(Map<String,Object> params);

	int selectRusersCount(Map<String,Object> params);

	int deleteRuser(Long id);

	Ruser selectByPrimaryKey(Long id);

	int updateRuser(Ruser ruser);

	int insertRuser(Ruser ruser);

	int batchRankAbleToAllow(String[] ids);

	int batchRankAbleToBan(String[] ids);

	/**
	 * 查询用户一定时间范围内粉丝数
	 *
	 * @param params
	 * @return
	 */
	Integer selectUserFansByTime(Map<String,Object> params);

	/**
	 * 查询用户一定时间范围内视频数
	 *
	 * @param params
	 * @return
	 */
	Integer selectUserVideosByTime(Map<String,Object> params);

	/**
	 * 查找需要重新计算人气排行的用户
	 *
	 * @param list
	 * @return
	 */
	List<Long> selectNeedReCalculateUsers(List<Long> list);

	/**
	 * 查询用户人气列表list
	 *
	 * @param params
	 * @return
	 */
	List<Ruser> selectUserPopularityByTime(Map<String,Object> params);

	int updateUserPopularity(Map<String,Object> params);


	List<Long> selectUserIdByAttentionTime(Map<String,Object> params);

	List<Long> selectUserIdByVideoTime(Map<String,Object> params);

	int updateRuserPopularity(Map<String,Object> params);

	List<ExportWopaiUser> selectWopaiUsers(Map<String,Object> params);

	int updateUserDayPopularityZero(List<Long> list);

	int updateUserWeekPopularityZero(List<Long> list);

	int updateUserMonthPopularityZero(List<Long> list);

	int updateUserYearPopularityZero(List<Long> list);

	Long selectRealFansCountById(Long id);

	List<ExportWopaiNormalUser> selectWopaiUserToExcel(Map<String,Object> params);

}
