package com.busap.vcs.data.mapper;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.vo.AttentionRecordVO;
import com.busap.vcs.data.vo.AttentionVO;
import com.busap.vcs.data.vo.FansVO;

/**
 * @author Administrator
 *
 */
public interface AttentionDAO {

	/**
	 * 添加关注
	 * @param params
	 * @return
	 */
	public int insert(Map<String,Object> params);
	
	public int insert2(Map<String,Object> params);
	
	/**
	 * 取消关注
	 * @param params
	 * @return
	 */
	public int delete(Map<String,Object> params);
	
	/**查询关注列表
	 * @param createId
	 * @return
	 */
	public List<AttentionVO> selectAllAttention(Map<String,Object> params);
	
	public List<AttentionVO> selectAllAttentionNew(Map<String,Object> params);

	public List<AttentionVO> selectAttentionByCreator(Map<String,Object> params);
	/**
	 * 查询粉丝列表
	 * @param attentionId
	 * @return
	 */
	public List<FansVO> selectAllFans(Map<String,Object> params);
	
	/**
	 * 查询关注人id列表
	 * @param creatorId
	 * @return
	 */
	public List<Long> selectAllAttentionId(Long creatorId);
	
	/**
	 * 查询粉丝id列表
	 * @param creatorId
	 * @return
	 */
	public List<Long> selectAllFansId(Long attentionId);
	
	/**
	 * 查询粉丝id列表（去除马甲）
	 * @param creatorId
	 * @return
	 */
	public List<Long> selectAllFansIdWithoutMajia(Long attentionId);
	
	/**
	 * 查询是否关注某人，1：是，0：否
	 * @param uid
	 * @param otherUid
	 * @return
	 */
	public int isAttention(Map<String,Object> params);

	/**
	 * 获得他人关注列表
	 * @param params
	 * @return
	 */
	public List<AttentionVO> selectOtherAttention(Map<String,Object> params);

	/**
	 * 获得他人粉丝列表
	 * @param params
	 * @return
	 */
	public List<FansVO> selectOtherFans(Map<String,Object> params);
	
	/**
	 * 获得粉丝数
	 * @param uid
	 * @return
	 */
	public int getFansCount(Long uid);
	
	/**
	 * 获得关注数
	 * @param uid
	 * @return
	 */
	public int getAttentionCount(Long uid);
	
	/**
	 * 获得所有记录
	 * @return
	 */
	public List<AttentionRecordVO> getAllRecords();

	/**
	 * 根据ID删除
	 * @param idList attentionID
	 */
	public void deleteAttention(List<Long> idList);
}
