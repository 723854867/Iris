package com.busap.vcs.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Gift;
import com.busap.vcs.data.entity.LiveActivity;
import com.busap.vcs.data.mapper.GeneralCustomDAO;
import com.busap.vcs.data.mapper.LiveActivityDAO;
import com.busap.vcs.data.model.LiveActivityDisplay;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.LiveActivityRepository;
import com.busap.vcs.service.GiftService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.LiveActivityService;
import com.busap.vcs.service.RuserLiveActivityService;

@Service("liveActivityService")
public class LiveActivityServiceImpl extends BaseServiceImpl<LiveActivity, Long> implements LiveActivityService {
	
    @Resource(name = "liveActivityRepository")
    private LiveActivityRepository liveActivityRepository;
    
    @Resource(name = "ruserLiveActivityService")
   	private RuserLiveActivityService ruserLiveActivityService;

    @Resource(name = "giftService")
    private GiftService giftService;

    @Resource(name = "jedisService")
    private JedisService jedisService;

    @Autowired
    private GeneralCustomDAO generalCustomDAO;

    @Resource(name = "liveActivityRepository")
    @Override
    public void setBaseRepository(BaseRepository<LiveActivity, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }
    
    @Autowired
    LiveActivityDAO liveActivityDao;


	@Override
	public List<LiveActivity> findMyLiveActivity(Long uid, int findAll) {
		if (findAll ==1){
			return liveActivityRepository.findMyLiveActivity(uid);
		} else {
			return liveActivityRepository.findMyAvalibleLiveActivity(uid,new Date());
		}
	}

    public int insert(LiveActivity liveActivity) {
        return liveActivityDao.insert(liveActivity);
    }

    @Override
    public Page queryLiveActivity(Map<String, Object> params) {
        Integer totalCount = liveActivityDao.searchLiveActivityCount(params);
        Integer pageNo = (Integer)params.get("pageNo");
        Integer pageSize = (Integer)params.get("pageSize");
        Integer totalPage = (totalCount/pageSize) + (totalCount%pageSize>0?1:0);
        if(pageNo>totalPage&&totalPage!=0){
            pageNo = totalPage;
            params.put("pageNo", pageNo);
            params.put("pageStart", (pageNo-1)*pageSize);
        }
        List<Map<String,Object>> acts = liveActivityDao.searchLiveActivity(params);

        return new PageImpl(acts,new PageRequest(pageNo-1, pageSize, null),totalCount);
    }

    @Override
    public Integer queryLiveActivityCount(Map<String, Object> params) {
        return liveActivityDao.searchLiveActivityCount(params);
    }

    public List<LiveActivityDisplay> queryLiveActivities(Map<String,Object> params){
        List<LiveActivityDisplay> list = liveActivityDao.selectActivities(params);
        for (LiveActivityDisplay display : list) {
            String giftIds = display.getGiftIds();
            if (giftIds != null && StringUtils.isNotBlank(giftIds)) {
                List<Long> ids = new ArrayList<Long>();
                for (String id : giftIds.split(",")) {
                    if (StringUtils.isNotBlank(id)) {
                        ids.add(Long.parseLong(id));
                    }
                }
                List<Gift> giftList = giftService.selectByIds(ids);
                StringBuilder builder = new StringBuilder();
                for (Gift gift : giftList) {
                    builder.append(",").append(gift.getName());
                }
                display.setGifts(builder.deleteCharAt(0).toString());
            }
            Long create = display.getCreateDate();
            Date start = display.getStartTime();
            Date end = display.getEndTime();
            display.setStartTime(start);
            display.setEndTime(end);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(create);

            String actTime = sdf.format(start) + " - " + sdf.format(end);
            display.setActTime(actTime);
            display.setCreateTime(sdf.format(c.getTime()));
        }
        return list;
    }

    @Override
    @Transactional
    public void save(String[] gifts, Long id, String title, String description, Integer status, Integer type,
                     String cover, int showCountOfTop, String startTime, String endTime, Long uid, Integer orderNum) {
        LiveActivity activity = new LiveActivity();
        activity.setId(id);
        activity.setTitle(title);
        activity.setDescription(description);
        activity.setCover(cover);
        activity.setOrderNum(orderNum);
        activity.setType(type);
        if (gifts != null && gifts.length > 0) {
            StringBuilder builder = new StringBuilder();
            for (String giftId : gifts) {
                builder.append(",").append(giftId);
            }
            String giftIds = builder.deleteCharAt(0).toString();
            activity.setGiftIds(giftIds);
            if(id != null){
                LiveActivity oldActivity = find(id);
                if (oldActivity != null) {
                    String historyGiftIds = oldActivity.getHistoryGiftIds();
                    if (StringUtils.isBlank(historyGiftIds)) {
                        activity.setHistoryGiftIds(removeRepeat(giftIds));
                    } else {
                        activity.setHistoryGiftIds(removeRepeat(historyGiftIds+","+giftIds));
                    }
                }
            }else {
                activity.setHistoryGiftIds(removeRepeat(giftIds));
            }
        } else {
            activity.setGiftIds("");
        }

        activity.setShowCountOfTop(showCountOfTop);
        if (status == null) {
            activity.setStatus(1);
        }
        activity.setCreatorId(uid);
        activity.setCreateDate(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            activity.setStartTime(start);
            activity.setEndTime(end);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        save(activity);
        if (id == null) {
            id = generalCustomDAO.findLastID();
        }
        jedisService.saveAsMap(BicycleConstants.LIVE_ACTIVITY_ + id, activity);
    }
    
    private String removeRepeat(String str) {
    	String result = "";
    	try {
    		if (str != null && str.length()>0) {
        		String[] ss = str.split(",");
        		Set<String> set = new HashSet<String>();
        		for (int i=0;i<ss.length;i++) {
        			set.add(ss[i]);
        		}
        		for (String s : set) {  
        		     result += s+",";
        		} 
        		if (result.endsWith(",")) {
        			result = result.substring(0,result.length()-1);
        		}
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return "".equals(result)?str:result;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        delete(id);
        jedisService.delete(BicycleConstants.LIVE_ACTIVITY_ + id);
    }

	@Override
	public List<LiveActivity> getLiveActivityList() {
		return liveActivityRepository.getLiveActivityList();
	}

    @Override
    public LiveActivity queryLiveActivityById(Long id) {
        return liveActivityDao.selectLiveActivityById(id);
    }
}
