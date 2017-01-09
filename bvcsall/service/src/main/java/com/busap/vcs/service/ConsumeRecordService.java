package com.busap.vcs.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.ConsumeRecord;

/**
 * Created by
 * User: zx
 * Date: 28/12/15
 * Time: 11:52 AM
 */
public interface ConsumeRecordService extends BaseService<ConsumeRecord, Long> {

	public List<Object[]> getTopPointUserInfo();

	List<Object[]> findAnchorRankingList(Date date, Integer start, Integer count);

	List<Object[]> findAnchorRankingList(Date date);

	List<Object[]> findAnchorDayRankingList();

	List<Object[]> findAnchorWeekRankingList();

	List<Object[]> findAnchorMonthRankingList();
	
	List<Object[]> findAnchorAllRankingList();
	
	
	List<Object[]> findRichRankingList(Date date, Integer start, Integer count);

	List<Object[]> findRichRankingList(Date date);

	List<Object[]> findRichDayRankingList();

	List<Object[]> findRichWeekRankingList();

	List<Object[]> findRichMonthRankingList();

	List<Object[]> findRichAllRankingList();

	/**
	 * 获取主播金豆数
	 * @param params
	 * @return
	 */
	Long selectUserPointInfo(Map<String,Object> params);
}
