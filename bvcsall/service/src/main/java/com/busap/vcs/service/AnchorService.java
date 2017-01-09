package com.busap.vcs.service;

import com.busap.vcs.data.entity.Anchor;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.Settlement;
import com.busap.vcs.data.model.AnchorDetailDisplay;
import com.busap.vcs.data.model.AnchorIncome;
import com.busap.vcs.data.model.AnchorInfo;
import com.busap.vcs.data.model.OrganizationAnchorDisplay;

import java.util.List;
import java.util.Map;

public interface AnchorService extends BaseService<Anchor, Long> {
	public Anchor getAnchorByUserid(Long userId);
	
	public Anchor getAnchorByPhone(String phone);
	
	public Anchor getAnchorByQq(String qq);

	public Anchor getAnchorByWechat(String wechat);
	
	public Anchor getAnchorByCertificateNumber(String certificateNumber);


	/**
	 * 主播收益查询
	 */
	List<AnchorIncome> queryAnchorIncome(Map<String,Object> map);

	Anchor queryAnchor(Long id);

	void doAnchorSettlement(Anchor anchor,Settlement settlement) throws Throwable;

	List<Anchor> queryAnchors(Map<String,Object> params);

	int updateByPrimaryKey(Anchor anchor);

	void allowAnchorLiving(Anchor anchor,Ruser ruser) throws Throwable;

	boolean realAuthCheck(Long uid, boolean isChecked, String reason) throws Throwable;

	public Long getTotalPoints(Map<String, Object> params);

	AnchorDetailDisplay queryAnchorLiveDetail(Map<String, Object> params);

	List<AnchorDetailDisplay> queryAnchorLiveDetailRecord(Map<String, Object> params);

	public int updateAnchorPoint(Integer point, Long userId);

	public int rollbackAnchorPoint(Integer point, Long userId);

	AnchorInfo queryAnchorByStreamId(Map<String,Object> params);

	/**
	 * 查询某一机构下主播数据
	 * @param params 参数信息
	 */
	List<OrganizationAnchorDisplay> queryOrganizationAnchorList(Map<String,Object> params);

	Long queryMaxRoomIdByStreamId(Map<String,Object> params);

}
