package com.busap.vcs.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.busap.vcs.data.entity.Anchor;
import com.busap.vcs.data.entity.Settlement;
import com.busap.vcs.data.mapper.SettlementDao;
import com.busap.vcs.data.model.ExportSettlement;
import com.busap.vcs.data.repository.AnchorRepository;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.SettlementRepository;
import com.busap.vcs.service.AnchorService;
import com.busap.vcs.service.SettlementService;

/**
 * Created by busap on 2015/12/30.
 */
@Service("settlementService")
public class SettlementServiceImpl extends BaseServiceImpl<Settlement, Long> implements SettlementService {

	@Resource
    private SettlementDao settlementDao;
	
	@Resource(name = "anchorRepository")
	private AnchorRepository anchorRepository;
	
	@Resource(name = "settlementRepository")
	private SettlementRepository settlementRepository;
	
	@Resource(name = "anchorService")
	private AnchorService anchorService;
	
	@Resource(name = "settlementRepository")
    @Override
	public void setBaseRepository(
			BaseRepository<Settlement, Long> baseRepository) {
		super.setBaseRepository(baseRepository);
	}

    @Override
    public List<Settlement> queryAll(Map<String,Object> params){
        return settlementDao.selectAll(params);
    }

    @Override
    public List<ExportSettlement> queryExportSettlementRecord(Map<String,Object> params){
        return settlementDao.selectExportSettlementRecord(params);
    }

	@Override
	public List<Settlement> queryAll(Long receiver, int page, int size) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("receiver", receiver);
		params.put("start", (page-1)*size);
		params.put("count", size);
		return settlementDao.select(params);
	}

	@Override
	@Transactional(readOnly = false,rollbackFor = Exception.class)
	public void doSettle(Settlement sm) throws Exception {
		
		Anchor anchor = anchorService.getAnchorByUserid(sm.getReciever());
		if(anchor != null && anchor.getLockPoints()>=sm.getPoints()){
			anchor.setLockPoints(anchor.getLockPoints()-sm.getPoints());
			anchor.setSettledPoints(anchor.getSettledPoints()+sm.getPoints());
			
			anchorRepository.save(anchor);
			
			settlementRepository.save(sm);
		}else{
			throw new Exception("结算数据不正确");
		}
	}

	@Override
	public List<Map<String, Object>> querySettlement(Map<String, Object> params) {
		
		return settlementDao.querySettlement(params);
	}

	@Override
	public Long getTotalSettlement(Map<String, Object> params) {
		
		return settlementDao.getTotalSettlement(params);
	}

}
