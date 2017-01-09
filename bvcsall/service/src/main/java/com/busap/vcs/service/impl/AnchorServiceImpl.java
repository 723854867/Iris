package com.busap.vcs.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.busap.vcs.data.model.AnchorDetailDisplay;
import com.busap.vcs.data.model.AnchorInfo;
import com.busap.vcs.data.model.OrganizationAnchorDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.busap.vcs.base.Filter;
import com.busap.vcs.data.entity.Anchor;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.Settlement;
import com.busap.vcs.data.mapper.AnchorDao;
import com.busap.vcs.data.mapper.RuserDAO;
import com.busap.vcs.data.mapper.SettlementDao;
import com.busap.vcs.data.model.AnchorIncome;
import com.busap.vcs.data.repository.AnchorRepository;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.SettlementRepository;
import com.busap.vcs.service.AnchorService;

@Service("anchorService")
public class AnchorServiceImpl extends BaseServiceImpl<Anchor, Long> implements
		AnchorService {

	private Logger logger = LoggerFactory.getLogger(AnchorServiceImpl.class);

	@Resource(name="anchorRepository")
	private AnchorRepository anchorRepository;

	@Resource(name = "settlementRepository")
	private SettlementRepository settlementRepository;
	
	@Resource
	private AnchorDao anchorDao;

	@Resource
	private SettlementDao settlementDao;

	@Resource
	private RuserDAO ruserDAO;

	@Resource(name="anchorRepository")
	@Override
	public void setBaseRepository(BaseRepository<Anchor, Long> baseRepository) {
		super.setBaseRepository(anchorRepository);
	}

	@Override
	public Anchor getAnchorByUserid(Long userId) {
		List<Filter> ls = new ArrayList<Filter>(1);
		ls.add(new Filter("creatorId", userId));
		List<Anchor> list = anchorRepository.findAll(ls, null);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 主播收益查询
	 */
	@Override
	public List<AnchorIncome> queryAnchorIncome(Map<String,Object> map){
		return anchorDao.selectAnchorIncome(map);
	}

	@Override
	public Anchor queryAnchor(Long id){
		return anchorDao.selectByPrimaryKey(id);
	}

	@Override
	@Transactional(readOnly = false,rollbackFor = Exception.class)
	public void doAnchorSettlement(Anchor anchor,Settlement settlement) throws Throwable {
        anchorRepository.save(anchor);//.updateByPrimaryKey(anchor);
		settlementRepository.save(settlement);//.insert(settlement);
	}

	@Override
	public Anchor getAnchorByPhone(String phone) {
		List<Filter> ls = new ArrayList<Filter>(1);
		ls.add(new Filter("phone", phone));
		List<Anchor> list = anchorRepository.findAll(ls, null);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}


	public int updateAnchorPoint(Integer point, Long userId) {
		return anchorRepository.exchangePoint(point, userId);
	}

	@Override
	public int rollbackAnchorPoint(Integer point, Long userId) {
		return anchorRepository.addPoint(point, userId);
	}

	@Override
	public Anchor getAnchorByQq(String qq) {
		List<Filter> ls = new ArrayList<Filter>(1);
		ls.add(new Filter("qq", qq));
		List<Anchor> list = anchorRepository.findAll(ls, null);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Anchor getAnchorByWechat(String wechat) {
		List<Filter> ls = new ArrayList<Filter>(1);
		ls.add(new Filter("wechat", wechat));
		List<Anchor> list = anchorRepository.findAll(ls, null);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Anchor getAnchorByCertificateNumber(String certificateNumber) {
		List<Filter> ls = new ArrayList<Filter>(1);
		ls.add(new Filter("certificateNumber", certificateNumber));
		List<Anchor> list = anchorRepository.findAll(ls, null);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<Anchor> queryAnchors(Map<String,Object> params){
		return anchorDao.select(params);
	}

	@Override
	public int updateByPrimaryKey(Anchor anchor){
		return anchorDao.updateByPrimaryKey(anchor);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void allowAnchorLiving(Anchor anchor, Ruser ruser) throws Throwable {
		int result = anchorDao.updateByPrimaryKey(anchor);
		int ret = ruserDAO.updateRuser(ruser);
		if (result <= 0 || ret <= 0) {
			Exception exception = new Exception();
			exception.printStackTrace();
			throw exception;
		}
	}

	/**
	 * 实名验证是否通过
	 * @param uid uid
	 * @param isChecked true:通过 false:没通过
	 * @throws Throwable
	 */
	@Override
	public boolean realAuthCheck(Long uid, boolean isChecked, String reason) throws Throwable {
		List<Long> uids = new ArrayList<Long>();
		uids.add(uid);
		List<Ruser> rusers = ruserDAO.getRusers(uids);
		Anchor anchor = getAnchorByUserid(uid);
		if (rusers.size() != 1 || anchor == null) {
			logger.error("userId= " + uid + "用户不存在");
			return false;
		}
		if (isChecked) {
			Ruser ruser = rusers.get(0);
			ruser.setIsAnchor(1);
			int u = ruserDAO.updateRuser(ruser);
			anchor.setStatus(1);
			int a = anchorDao.updateByPrimaryKey(anchor);
			return (a + u) == 2;
		} else {
			Ruser ruser = rusers.get(0);
			ruser.setIsAnchor(-1);
			int u = ruserDAO.updateRuser(ruser);
			anchor.setStatus(-1);
			anchor.setRejectReason(reason);
			int a = anchorDao.updateByPrimaryKey(anchor);
			return (a + u) == 2;
		}
	}


	@Override
	public Long getTotalPoints(Map<String, Object> params) {
		
		return anchorDao.getAnchorTotalPoints(params);
	}

	@Override
	public AnchorDetailDisplay queryAnchorLiveDetail(Map<String, Object> params){
		return anchorDao.selectAnchorLiveDetail(params);
	}

	@Override
	public List<AnchorDetailDisplay> queryAnchorLiveDetailRecord(Map<String, Object> params){
		return anchorDao.selectAnchorLiveDetailRecord(params);
	}

	@Override
	public AnchorInfo queryAnchorByStreamId(Map<String,Object> params){
		return anchorDao.selectAnchorByStreamId(params);
	}

	@Override
	public List<OrganizationAnchorDisplay> queryOrganizationAnchorList(Map<String, Object> params) {
		return anchorDao.selectOrganizationAnchorList(params);
	}

	@Override
	public Long queryMaxRoomIdByStreamId(Map<String, Object> params) {
		return anchorDao.selectMaxRoomIdByStreamId(params);
	}
}
