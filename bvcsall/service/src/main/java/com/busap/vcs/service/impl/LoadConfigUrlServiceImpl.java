package com.busap.vcs.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.LoadConfigUrl;
import com.busap.vcs.data.mapper.LoadConfigUrlDAO;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.LoadConfigUrlRepository;
import com.busap.vcs.data.vo.LoadConfigUrlVO;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.LoadConfigUrlService;
@Service("loadConfigUrlService")
public class LoadConfigUrlServiceImpl extends BaseServiceImpl<LoadConfigUrl,Long> implements LoadConfigUrlService  {
	private final String ALL_URL_CONFIG_CLIENT_PF="all";//所有平台通用
    @Resource(name = "loadConfigUrlRepository")
    private LoadConfigUrlRepository loadConfigUrlRepository;
    
    @Resource(name = "loadConfigUrlRepository")
    @Override
    public void setBaseRepository(BaseRepository<LoadConfigUrl, Long> loadConfigUrlRepository) {
        super.setBaseRepository(loadConfigUrlRepository);
    }
    
	@Autowired
    JedisService jedisService;
	
	@Autowired
	LoadConfigUrlDAO loadConfigUrlDao;
	
	@Override
	public List<LoadConfigUrlVO> findAllLoadConfigUrl() {
		return loadConfigUrlDao.findAllLoadConfigUrl();
	}
	
	//获取用户平台所有url配置
	@Override
	public List<LoadConfigUrlVO> findLoadConfigUrlByClientPf(String clientPf) {
		//redis取出所有的配置信息
		jedisService.delete(BicycleConstants.ALL_URL_CONFIG_REDIS_KEY);
		@SuppressWarnings("unchecked")
		List<LoadConfigUrlVO> listLoadConfigUrlVO=(List<LoadConfigUrlVO>)jedisService.getObject(BicycleConstants.ALL_URL_CONFIG_REDIS_KEY);
		List<LoadConfigUrlVO> returnList=new ArrayList<LoadConfigUrlVO>();
		Map<String,List<LoadConfigUrlVO>> mapList=new HashMap<String,List<LoadConfigUrlVO>>();
		if(listLoadConfigUrlVO!=null&&listLoadConfigUrlVO.size()>0){
			for(LoadConfigUrlVO lvuv:listLoadConfigUrlVO){
				//取出该平台和通用的url
				if(lvuv.getClientPf().equals(clientPf)||lvuv.getClientPf().equals(ALL_URL_CONFIG_CLIENT_PF)){
					List<LoadConfigUrlVO> tempList=null;
					if(mapList.containsKey(lvuv.getType())){
						tempList=mapList.get(lvuv.getType());
					}else{
						tempList=new ArrayList<LoadConfigUrlVO>();
					}
					tempList.add(lvuv);
					mapList.put(lvuv.getType(),tempList);
				}
			}
		}else{
			listLoadConfigUrlVO =findAllLoadConfigUrl();
			jedisService.setObject(BicycleConstants.ALL_URL_CONFIG_REDIS_KEY,listLoadConfigUrlVO);
			for(LoadConfigUrlVO lvuv:listLoadConfigUrlVO){
				if(lvuv.getClientPf().equals(clientPf)||lvuv.getClientPf().equals(ALL_URL_CONFIG_CLIENT_PF)){
					List<LoadConfigUrlVO> tempList=null;
					if(mapList.containsKey(lvuv.getType())){
						tempList=mapList.get(lvuv.getType());
					}else{
						tempList=new ArrayList<LoadConfigUrlVO>();
					}
					tempList.add(lvuv);
					mapList.put(lvuv.getType(),tempList);
				}
			}

		}
		if(mapList!=null){
			Collection<List<LoadConfigUrlVO>> setlv =(Collection<List<LoadConfigUrlVO>>)mapList.values();
			for(List<LoadConfigUrlVO> l:setlv){
				Integer sumweight=0;
				Integer sweight=0;
				if(l.size()==1){
					returnList.addAll(l);
				}else{
					//排序，按照权重从小到大
					Collections.sort(l,new Comparator<LoadConfigUrlVO>(){
						@Override  
						public int compare(LoadConfigUrlVO o1, LoadConfigUrlVO o2) {  
							if(o1.getWeight()-o2.getWeight()>0){
								return 1;
							}else{
								return -1;
							}
						}
					});	
					for(LoadConfigUrlVO lvuvo:l){
						sumweight+=lvuvo.getWeight();
					}
					int randnum=(int)(Math.random()*sumweight);
					for(LoadConfigUrlVO lvuvo:l){
						sweight+=lvuvo.getWeight();
						if(randnum<sweight){
							returnList.add(lvuvo);
							break;
						}
					}
				}
			}
		}
		return returnList;
	}

}
