package com.busap.vcs.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Banner;
import com.busap.vcs.data.mapper.BannerDAO;
import com.busap.vcs.data.repository.BannerRepository;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.service.BannerService;
import org.springframework.transaction.annotation.Transactional;

@Service("bannerService")
public class BannerServiceImpl extends BaseServiceImpl<Banner, Long> implements BannerService {
	
    @Resource(name = "bannerRepository")
    private BannerRepository bannerRepository;
    
     
    @Resource(name = "bannerRepository")
    @Override
    public void setBaseRepository(BaseRepository<Banner, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }
    
    @Autowired
	BannerDAO bannerDao;

	@Override
	public List<Banner> findAllBanner(Integer showAble) {
		return bannerDao.findAllBanner(showAble);
	}

    @Override
    public int updateSort(Map<String,Object> params){
        return bannerDao.updateSort(params);
    }

    @Override
    public Banner queryBannerByOrderNum(Map<String,Object> params){
        return bannerDao.selectBannerByOrderNum(params);
    }

    @Override
    @Transactional(readOnly = true,rollbackFor = Exception.class)
    public boolean updateBannerSort(Long id,Integer type) throws Throwable{
        Banner banner = bannerDao.selectByPrimaryKey(id);//当前传入ID
        int res = 0,ret = 0;
        if(type == 1){
            Map<String,Object> maxParams = new HashMap<String, Object>();
            maxParams.put("MAX","MAX");
            maxParams.put("orderNum",banner.getOrderNum());
            Banner maxBanner = bannerDao.selectBannerByOrderNum(maxParams);//置顶
            Map<String,Object> params = new HashMap<String, Object>();
            params.put("id",id);
            params.put("orderNum",maxBanner.getOrderNum()+1);
            int result = bannerDao.updateSort(params);
            if(result>0){
                return true;
            }
        }else if(type == 2){
            Map<String,Object> ascParams = new HashMap<String, Object>();
            ascParams.put("ASC","ASC");
            ascParams.put("orderNum",banner.getOrderNum());
            Banner ascBanner = bannerDao.selectBannerByOrderNum(ascParams);//上移
            Map<String,Object> params = new HashMap<String, Object>();
            params.put("id",id);
            params.put("orderNum",ascBanner.getOrderNum());
            res = bannerDao.updateSort(params);
            Map<String,Object> param = new HashMap<String, Object>();
            param.put("id",ascBanner.getId());
            param.put("orderNum",banner.getOrderNum());
            ret = bannerDao.updateSort(param);
        }else{
            Map<String,Object> descParams = new HashMap<String, Object>();
            descParams.put("DESC","DESC");
            descParams.put("orderNum",banner.getOrderNum());
            Banner descBanner = bannerDao.selectBannerByOrderNum(descParams);//下移
            Map<String,Object> params = new HashMap<String, Object>();
            params.put("id",id);
            params.put("orderNum",descBanner.getOrderNum());
            res = bannerDao.updateSort(params);
            Map<String,Object> param = new HashMap<String, Object>();
            param.put("id",descBanner.getId());
            param.put("orderNum",banner.getOrderNum());
            ret = bannerDao.updateSort(param);
        }
        if(res > 0 && ret > 0){
            return true;
        }else{
            Throwable throwable = new Throwable();
            throwable.printStackTrace();
            throw throwable;
        }
    }

    @Override
    public List<Banner> queryBannerList(Map<String,Object> params){
        return bannerDao.selectBannerList(params);
    }

}
