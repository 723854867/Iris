package com.busap.vcs.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.busap.vcs.data.mapper.ConsumeRecordDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.ConsumeRecord;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.ConsumeRecordRepository;
import com.busap.vcs.service.ConsumeRecordService;
import com.busap.vcs.service.JedisService;

@Service("consumeRecordService")
public class ConsumeRecordServiceImpl extends BaseServiceImpl<ConsumeRecord, Long> implements ConsumeRecordService {
	
    @Resource(name = "consumeRecordRepository")
    private ConsumeRecordRepository consumeRecordRepository;
     
    
    
    @Autowired
    JedisService jedisService;

	@Resource
	private ConsumeRecordDAO consumeRecordDAO;

    @Resource(name = "consumeRecordRepository")
    @Override
    public void setBaseRepository(BaseRepository<ConsumeRecord, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }

	@Override
	public List<Object[]> getTopPointUserInfo() {
		Long current = System.currentTimeMillis();
		String interval = jedisService.get(BicycleConstants.RECOMMEND_INTERVAL);
		int hour = 24;
		if (interval != null && !"".equals(interval)) {
			hour = Integer.parseInt(interval);
		}
		Date date = new Date(current-1000*60*60*hour);
		return consumeRecordRepository.getTopPointUserInfo(date);
	}  
	
	@Override
	public List<Object[]> findAnchorRankingList(Date date,Integer start,Integer count) {
		if(start==null) {
			start=0;
		}
		if(count==null) {
			count=10;
		}
		return consumeRecordRepository.findAnchorRankingList(date, start, count);
	}
	
	@Override
	public List<Object[]> findAnchorRankingList(Date date) {
		return consumeRecordRepository.findAnchorRankingList(date, 0, 10);
	}
	
	@Override
	public List<Object[]> findAnchorDayRankingList() {
		Long current = System.currentTimeMillis();
		Date date = new Date(current-1000*60*60*24);
		return this.findAnchorRankingList(date, 0, 100);
	}
	
	@Override
	public List<Object[]> findAnchorWeekRankingList() {
		Long current = System.currentTimeMillis();
		Date date = new Date(current-1000*60*60*24*7);
		return this.findAnchorRankingList(date, 0, 100);
	}
	
	@Override
	public List<Object[]> findAnchorMonthRankingList() {
		Calendar c = Calendar.getInstance(); 
        c.add(Calendar.MONTH, -1); 
//      System.out.println("上个月是："+new SimpleDateFormat("yyyy年MM月").format(c.getTime()));
        
        
//		Long current = System.currentTimeMillis();
//		Date date = new Date(current-1000*60*60*24*30);
		return this.findAnchorRankingList(c.getTime(), 0, 100);
	}
	
	@Override
	public List<Object[]> findAnchorAllRankingList() {
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		 
		Date date=null;
		try {
			date = sdf.parse("2016-01-01 00:00:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return consumeRecordRepository.findAnchorAllRankingList(date, 0, 100);
	}
	
	@Override
	public List<Object[]> findRichRankingList(Date date,Integer start,Integer count) {
		if(start==null) {
			start=0;
		}
		if(count==null) {
			count=10;
		}
		return consumeRecordRepository.findRichRankingList(date, start, count);
	}
	
	@Override
	public List<Object[]> findRichRankingList(Date date) {
		return consumeRecordRepository.findRichRankingList(date, 0, 10);
	}
	
	@Override
	public List<Object[]> findRichDayRankingList() {
		Long current = System.currentTimeMillis();
		Date date = new Date(current-1000*60*60*24);
		return this.findRichRankingList(date, 0, 100);
	}
	
	@Override
	public List<Object[]> findRichWeekRankingList() {
		Long current = System.currentTimeMillis();
		Date date = new Date(current-1000*60*60*24*7);
		return this.findRichRankingList(date, 0, 100);
	}
	
	@Override
	public List<Object[]> findRichMonthRankingList() {
		Calendar c = Calendar.getInstance(); 
        c.add(Calendar.MONTH, -1); 
//      System.out.println("上个月是："+new SimpleDateFormat("yyyy年MM月").format(c.getTime()));
        
        
//		Long current = System.currentTimeMillis();
//		Date date = new Date(current-1000*60*60*24*30);
		return this.findRichRankingList(c.getTime(), 0, 100);
	}
	
	@Override
	public List<Object[]> findRichAllRankingList() {
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		 
		Date date=null;
		try {
			date = sdf.parse("2016-01-01 00:00:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return this.findRichRankingList(date, 0, 100);
	}

	/**
	 * 获取主播金豆数
	 * @param params
	 * @return
	 */
	@Override
	public Long selectUserPointInfo(Map<String,Object> params){
		return consumeRecordDAO.selectUserPointInfo(params);
	}

	/*public static void main(String[] args) {
		new ConsumeRecordServiceImpl().findRichAllRankingList();
	}*/
	

}
