package com.busap.vcs.data.mapper;

import java.util.List;
import java.util.Map;

public interface ConsumeRecordDAO {

	/**
	 * 计算主播金豆收入总数
	 * @param params{anchorUid,startTime}
	 * @return
	 */
	Long getAnchorTotalRecievePoints(Map<String,Object> params);
	/**
	 * 用户对主播总贡献值
	 * @param params{uid,anchorUid,startTime}
	 * @return
	 */
	Integer getTotalContributionToAnchor(Map<String,Object> params);
	/**
	 * 获取主播贡献历史
	 * @param userId
	 * @return
	 */
	List<Map<String,Object>> getContributionHistory(Long userId);

	/**
	 * 获取主播金豆数
	 * @param params
	 * @return
	 */
	Long selectUserPointInfo(Map<String,Object> params);
}
