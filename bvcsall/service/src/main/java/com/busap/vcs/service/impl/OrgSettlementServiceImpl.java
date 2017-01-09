package com.busap.vcs.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.Anchor;
import com.busap.vcs.data.entity.OrgSettlement;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.repository.AnchorRepository;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.OrgSettlementRepository;
import com.busap.vcs.service.AnchorService;
import com.busap.vcs.service.ExchangeService;
import com.busap.vcs.service.OrgSettlementService;
import com.busap.vcs.service.PingPayService;
import com.busap.vcs.service.RuserService;

@Service("orgSettlementService")
public class OrgSettlementServiceImpl extends BaseServiceImpl<OrgSettlement, Long> implements OrgSettlementService {
	
    @Resource(name = "orgSettlementRepository")
    private OrgSettlementRepository orgSettlementRepository;
    
    @Resource(name = "anchorRepository")
    private AnchorRepository anchorRepository;
    
    @Resource(name = "anchorService")
    private AnchorService anchorService;
    
    @Resource(name="ruserService")
    private RuserService ruserService;
    
    @Autowired
    private ExchangeService exchangeService;
    
    @Autowired
    private PingPayService pingPayService;
     
    
    

    @Resource(name = "orgSettlementRepository")
    @Override
    public void setBaseRepository(BaseRepository<OrgSettlement, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }

    public String doOrgSettlement(Long orgId) {
    	OrgSettlement os=orgSettlementRepository.findOne(orgId);
    	if(os!=null) {
    		Anchor anchor=anchorService.getAnchorByUserid(os.getUserId());
    		Ruser ruser = ruserService.find(os.getUserId());
    		if(anchor!=null&&ruser!=null) {
//    			boolean isForbid = ruser.getIsBlacklist() != null && ruser.getIsBlacklist() == 0;
// 		        if (isForbid) {
// 		            return ResponseCode.CODE_616.toString();
// 		        }
 		        Integer amt=0;
 		        Integer pts=0;
 		        if(os.getPoints()!=null&&anchor.getPointCount()>=os.getPoints()) {
 		        	
 		        	amt=os.getAmount();
 	 		        pts=os.getPoints();
 		        	anchor.setPointCount(anchor.getPointCount()-os.getPoints());
 		        	os.setRealAmount(os.getAmount());
 		        	os.setRealPoints(os.getPoints());
 		        	
 		        	
 		        }else {
 		        	amt=anchor.getPointCount()*4;
 	 		        pts=anchor.getPointCount();
 		        	os.setRealAmount(anchor.getPointCount()*4);
 		        	os.setRealPoints(anchor.getPointCount());
 		        	anchor.setPointCount(0);
 		        	
 		        }
 		       
 		       anchorRepository.save(anchor);
 		       os.setStatus(1);
		       orgSettlementRepository.save(os);
		       pingPayService.saveExchangeRecodeForOrgSettlement(amt, os.getUserId(), pts);
    			
    		}
    	}
    	
    	return "ok";
    	
    }
	

}
