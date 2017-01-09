package com.busap.vcs.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.HotLabel;
import com.busap.vcs.data.entity.RecommandPosition;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.RecommandPositionRepository;
import com.busap.vcs.service.HotLabelService;
import com.busap.vcs.service.RecommandPositionService;
@Service("recommandPositionService")
public class RecommandPositionServiceImpl extends BaseServiceImpl<RecommandPosition, Long> implements RecommandPositionService {

	@Resource(name="recommandPositionRepository")
	private RecommandPositionRepository recommandPositionRepository;
	
	@Resource(name="hotLabelService")
	private HotLabelService hotLabelService;
	
	@Resource(name="recommandPositionRepository")
	@Override
	public void setBaseRepository(
			BaseRepository<RecommandPosition, Long> baseRepository) {
		// TODO Auto-generated method stub
		super.setBaseRepository(baseRepository);
	}

	@Override
	public List<Map<String,Object>> findCurrentPagePositions(Integer page) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<RecommandPosition> psList = recommandPositionRepository.findByPage(page);
		if(psList!=null && psList.size()>0){
			for(RecommandPosition rp:psList){
				try {
					Map<String,Object> psMap = BeanUtils.describe(rp);
					psMap.put("type", -1);
					String operation = rp.getOperation();
					if(StringUtils.isNotBlank(operation)){
						if(operation.equals("label")){
							HotLabel label = hotLabelService.find(rp.getTargetid()==null?0:rp.getTargetid());
							if(label != null){
								psMap.put("labelName", label.getLabelName());
							}
						}
					}
					list.add(psMap);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	@Override
	public Integer findPrePagePositionCount(Integer page) {
		// TODO Auto-generated method stub
		return recommandPositionRepository.getPrePagePositionCount(page);
	}

	
}
