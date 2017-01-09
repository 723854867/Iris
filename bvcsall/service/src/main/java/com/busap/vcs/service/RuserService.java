package com.busap.vcs.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.busap.vcs.data.model.ExportWopaiNormalUser;
import com.busap.vcs.data.model.ExportWopaiUser;
import com.busap.vcs.data.model.RuserDisplay;

import org.springframework.data.domain.Page;

import com.busap.vcs.data.entity.Ruser;

public interface RuserService extends BaseService<Ruser, Long> {
	/**
	 * 创建用户
	 * 
	 * @param user
	 */
	public Ruser createUser(Ruser user);

	/**
	 * 修改密码
	 * 
	 * @param userId
	 * @param newPassword
	 */
	public void changePassword(Long userId, String newPassword);

	/**
	 * 根据用户名查找用户
	 * 
	 * @param username
	 * @return
	 */
	public Ruser findByUsername(String username);
	
	public Ruser findByBandPhone(String bandPhone);

	public List<Long> fuzzyQueryByName(String username);

	public List<Long> fuzzyQueryByPhone(String phone);

	public Ruser findByWechatUnionid(String wechatUnionid);
	
	public List findByType(String type);
	public List<Long> findIdsByVipState(int vipstate);
	public List<Long> findAllIds();
	
	public boolean existByName(String name,Long uid);
	public boolean existByName(String name);
	/**
	 * 判断第三方账号是否已经存在
	 * @param thirdUsername
	 * @param thirdFrom
	 * @return
	 */
	public boolean countByThirdName(String thirdUsername,String thirdFrom);
	
	/**
	 * 判断微信unionid是否存在
	 * @param thirdUsername
	 * @param thirdFrom
	 * @return
	 */
	public boolean countByWechatUnionid(String unionid,String thirdFrom);
	/**
	 * 判断用户信息是否完整[目前当email为空时视为资料不完整]
	 * @param username
	 * @return
	 */
	public boolean isUserInfoCompletion(String username);
	
	public void updateLoginDate(String username);
	
	public void insertWechatUnionid(String username,String unionid);
	
	public Map<Long,Ruser> getRusers(List<Long> ids);
	
	/**
	 * 注册用户数
	 * @return
	 */
	public Long registNum();
	//最近X天每天注册用户数
	public List<Map<String,Object>> lastXDayUserNum(String date);
	
	public Page searchNormalUserList(Integer pageNo, Integer pageSize, Map<String, Object> params);

	//根据手机号码或者昵称查询用户信息
	public List<Ruser> findUserByKeyWord(String uid,Date timestamp,String keyWord,int count);

	List<Ruser> findUsersByIds(List<Long> ids);
	
	//根据id从redis中读取用户信息，返回map格式
	Map<String,String> getUserFromRedis(Long id);
	//用户人气日排行榜，执行存储过程，前50条存redis
	public void dayUserPopularityToRedis();
	public List<Ruser> findDayUserPopularity(Long uid,Integer start,Integer count);
	public List<Ruser> findVipUsers();
	public List<Ruser> findRecommondVipUsers(Collection ids,int count);
	//计算当日人气
	public void executeDayUserPopularity(Long uid);
	//人气定时重算
	public void calculatePopularity();

	List<Ruser> selectRusers(Map<String,Object> params);

	List<RuserDisplay> queryWopaiRusers(Map<String,Object> params);

	int selectRusersCount(Map<String,Object> params);

	int deleteRuser(Long id);

	Ruser selectByPrimaryKey(Long id);

	int updateRuser(Ruser ruser);

	int insertRuser(Ruser ruser);

	int batchRankAbleToAllow(String[] ids);

	int batchRankAbleToBan(String[] ids);
	
	List<String> selectAllRegPlatform();

	/**
	 * 根据时间类型type获取用户人气排行榜
	 *
	 * @param type 时间类型 day 24小时 week 周 month 月 year 年
	 * @param uid 用户ID
	 * @param start 开始条数
	 * @param count 总数
	 * @return
	 */
	List<Ruser> queryUserPopularityList(String type,Long uid,Integer start,Integer count);

	/**
	 * 根据时间类型重新计算需要计算的用户人气信息
	 *
	 * @param type 时间类型 day 24小时 week 周 month 月 year 年
	 * @return
	 */
	void reCalculateUserPopularity(String type);

	/**
	 * 查询不同时间范围人气值后写入redis
	 *
	 * @param params type 时间类型 day 24小时 week 周 month 月 year 年
	 * @param params count
	 * @return
	 */
	void queryUserPopularityToRedis(Map<String, Object> params);

	List<ExportWopaiUser> queryWopaiUsers(Map<String,Object> params);
	
	//开启、关闭评论
	public void allowEvaluation(int isAllow,Long uid);

	Long queryRealFansCountById(Long id);
	
	//修改用户上传的非法图片(头像，背景图)
	public void changeInvalidatePic(Long uid,String type);
	
	//审核通过用户上传的图片
	public void verifyPic(Long uid,String type);

	/**
	 * 封号
	 * @param userId 用户ID
	 **/
	void closeAccount(Long userId);

	List<ExportWopaiNormalUser> queryWopaiUserToExcel(Map<String, Object> params);
	
	public void updateBerryCount(Long uid);
	
	public void updateDiamond(Long uid,Integer count);
	public int reduceDiamond(Long uid,Integer count);
	
	/**
	 * 如果用户正在直播，则下线
	 * @param uid
	 */
	public void offlineLiveRoom(Long uid,Long adminId);
	
	public Long findCountByUserIdIfaNotNull(Long userId);

}
