package com.busap.vcs.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.mapper.PraiseDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Tuple;

import com.busap.vcs.base.Filter;
import com.busap.vcs.data.entity.Praise;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.data.enums.DataFrom;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.PraiseRepository;
import com.busap.vcs.data.repository.VideoRepository;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.PraiseService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.VideoService;

@Service("praiseService")
public class PraiseServiceImpl extends BaseServiceImpl<Praise, Long> implements
PraiseService {
 
	
	@Resource(name = "praiseRepository")
	private PraiseRepository praiseRepository;
	 
	@Resource(name="videoRepository") 
	private VideoRepository videoRepository;
	
	@Resource(name="videoService") 
	private VideoService videoService;
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Resource(name = "ruserService")
	RuserService ruserService;

	@Autowired
	private PraiseDAO praiseDAO;

	@Resource(name = "praiseRepository")
	@Override
	public void setBaseRepository(BaseRepository<Praise, Long> baseRepository) {
		super.setBaseRepository(baseRepository);
	} 
	 
	//获取某人对某一个视频的赞
	public Praise getPraise(Long videoId,Long uid){ 
		List<Filter> ls = new ArrayList<Filter>();
		ls.add(new Filter("videoId", videoId.toString()));
		ls.add(new Filter("creatorId", uid.toString()));
		List<Praise> tmp = praiseRepository.findAll(ls, null);
		if(tmp!=null&&tmp.size()>0)
			return tmp.get(0);
		else
			return null;
	}
	
	public void savePraise(Praise f){
		Praise p = praiseRepository.findByCreatorIdAndVideoId(f.getCreatorId(), f.getVideoId());
		if(p==null){
			this.save(f);
			videoRepository.incPraiseCount(f.getVideoId());
			//赞后重新计算视频热度值
			videoService.updateHotPointByVideoId(f.getVideoId());
			//重新计算当日视频热度
			videoService.excuteVideoDayHotValue(f.getVideoId());
		}
	}

	public void deletePraise(Long videoId,Long creator){ 
		Praise p = praiseRepository.findByCreatorIdAndVideoId(creator, videoId);
		if(p!=null){
			praiseRepository.deleteByCreatorIdAndVideoId(creator,videoId);
			videoRepository.decPraiseCount(videoId);
			//取消赞后重新计算视频热度值
			videoService.updateHotPointByVideoId(videoId);
			//重新计算当日视频热度
			videoService.excuteVideoDayHotValue(videoId);
		}
	}

	@Override
	public void savePraise(Long videoId, String creatorIds[], Long adminId) {
		for(String majia:creatorIds){
			Praise p = new Praise();
			p.setCreatorId(Long.valueOf(majia));
			p.setVideoId(videoId);
			p.setAdminId(adminId);
			p.setCreateDate(new Date());
			p.setDataFrom(DataFrom.移动麦视后台.getName());
			
			this.savePraise(p);
		}
	}

	@Override
	public List<Praise> searchByMajia(Long videoId) {
		return praiseRepository.findByMajia(videoId);
	}

	@Override
	public void deletePraiseByIds(List<Long> ids) {
		try {
			praiseDAO.deletePraise(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void autoPraise() {
		String flag = jedisService.get("auto_praise_switch");
		if (flag != null && "1".equals(flag)){
			String officalVideoIdKey = "offical_video_id";  //官方发布的活动视频id(redis key)
			String officalPraiseCountKey = "offical_praise_count";  //官方发布的视频每批点赞次数(redis key)
			String vipVideoIdKey = "vip_video_id";  //vip发布的活动视频id(redis key)
			String vipPraiseCountKey = "vip_praise_count";  //vip发布的视频每批点赞次数(redis key)
			String specialActivityIdKey = "special_activity_id";  //指定活动id(redis key)
			
			String specialRuserId = "special_user_id"; //官方上传者id
			
			String majiaForVideoKey = "majia_for_video_"; //每个视频被点赞的马甲用户id(set ，redis key)
			
			String isInitKey = "is_init"; //是否是初始化执行 (redis ,key)
			
			String isInit  = jedisService.get(isInitKey);
			
			Date date = null;
			
			if ("1".equals(isInit)) {  //初始化执行，提取五天内的活动视频id
				jedisService.set(isInitKey, "0"); //初始化方法只执行一次
				
				//获得当前时间前五天
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 5);
				date = calendar.getTime();
				
			} else { //定时执行，提取10分钟内的活动视频id
				//获得当前时间前10分钟
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - 20);
				date = calendar.getTime();
			}
			
			//查询活动视频列表
			Long actId = Long.parseLong(jedisService.get(specialActivityIdKey));
			List<Video> vList = videoService.findActVideosAfterAssignedTime(date.getTime(), actId, 500);
			
			Set<String> addSet = jedisService.getSetFromShard("add_id");
			for (String str:addSet) {
				try {
					Video video = videoService.find(Long.parseLong(str));
					if (video != null) {
						vList.add(video);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			jedisService.deleteSetFromShard("add_id");
			
			System.out.println("******AutoPraise*****vList size="+vList.size());
			
			for (Video video:vList) {
				Long uid = video.getCreatorId();
				if (String.valueOf(uid).equals(jedisService.get(specialRuserId))) { //官方用户上传的视频
					System.out.println("******AutoPraise*****add officalVideoId:"+String.valueOf(video.getId())+"   "+video.getCreateDate());
					jedisService.setValueToSortedSetInShard(officalVideoIdKey, video.getCreateDate(), String.valueOf(video.getId())); //将视频id放入到redis
				} else {  //非官方用户上传的视频（仅限v用户）
					int vipStat = ruserService.find(uid).getVipStat(); //获得视频上传者的v状态
					System.out.println("******AutoPraise*****vipStat:"+vipStat);
					if (vipStat > 0) {
						System.out.println("******AutoPraise*****add vipVideoId:"+String.valueOf(video.getId())+"   "+video.getCreateDate());
						jedisService.setValueToSortedSetInShard(vipVideoIdKey, video.getCreateDate(), String.valueOf(video.getId())); //将视频id放入到redis
					}
				}
				
				Set<String> set = jedisService.getSetFromShard(BicycleConstants.MAJIA_UID);  //获得所有的马甲id
				System.out.println("******AutoPraise*****maijia size:"+set.size());
				for (String majiaUid : set) {   //针对每个视频，放一组马甲用户id
				      jedisService.setValueToSetInShard(majiaForVideoKey+String.valueOf(video.getId()), majiaUid);
				}  
			}
			
			//获得当前时间前五天,五天前的视频不在自动点赞
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 5);
			date = calendar.getTime();
			Set<Tuple> officialVideoId = jedisService.getSortedSetByScoreWithScores(officalVideoIdKey, date.getTime(), 2453089822018l);
			System.out.println("******AutoPraise*****officialVideoId size:"+officialVideoId.size());
			Set<Tuple> vipVideoId = jedisService.getSortedSetByScoreWithScores(vipVideoIdKey, date.getTime(), 2453089822018l);
			System.out.println("******AutoPraise*****vipVideoId size:"+vipVideoId.size());
			
			for (Tuple tuple:officialVideoId) {
				try {
					String videoId = tuple.getElement();
					Long createTime = new Double(tuple.getScore()).longValue();
					
					//获得当前时间前10分钟
					calendar = Calendar.getInstance();
					calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - 10);
					date = calendar.getTime();
					
					Long start = createTime;
					if (date.getTime() > createTime) {
						start = date.getTime();
					}
					start = start+1000*60;
					Long end = System.currentTimeMillis();
					
					Long timeInterval = (end-start)<=0?0:(end-start);
					
					int officalPraiseCount = Integer.parseInt(jedisService.get(officalPraiseCountKey));
					System.out.println("******AutoPraise*****officalPraiseCount:"+officalPraiseCount);
					for (int i=0;i<officalPraiseCount;i++) {
						System.out.println("******AutoPraise*****"+majiaForVideoKey+videoId+":"+jedisService.getSetSizeFromShard(majiaForVideoKey+videoId));
						String creatorId = jedisService.getSrandmember(majiaForVideoKey+videoId);  //从该视频对应的马甲用户id中随机取一个id
						if (creatorId == null || "".equals(creatorId)){  //如果马甲用完了 ，不执行点赞
							break;
						}
						Long praiseTime = start+getRandom(0, timeInterval.intValue());  //生成点赞时间
						
						Praise praise = new Praise();
						praise.setVideoId(Long.parseLong(videoId));
						praise.setCreateDate(new Date(praiseTime));
						
						praise.setCreatorId(Long.parseLong(creatorId));
						
						try {
							System.out.println("******AutoPraise******createid="+creatorId+"  videoid="+videoId);
							Praise p = praiseRepository.findByCreatorIdAndVideoId(Long.parseLong(creatorId), Long.parseLong(videoId));
							if(p==null){
								//点赞
								this.save(praise);
								//更新点赞数
								videoRepository.incPraiseCount(Long.parseLong(videoId)); 
								//更新播放数
								videoRepository.incPlayCount(1, Long.parseLong(videoId));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						jedisService.deleteSetItemFromShard(majiaForVideoKey+videoId, creatorId); //该马甲点赞后，从列表中删除
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			for (Tuple tuple:vipVideoId) {
				try {
					String videoId = tuple.getElement();
					Long createTime = new Double(tuple.getScore()).longValue();
					
					//获得当前时间前10分钟
					calendar = Calendar.getInstance();
					calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - 10);
					date = calendar.getTime();
					
					Long start = createTime;
					if (date.getTime() > createTime) {
						start = date.getTime();
					}
					start = start+1000*60;
					Long end = System.currentTimeMillis();
					
					Long timeInterval = (end-start)<=0?0:(end-start);
					
					int vipPraiseCount = Integer.parseInt(jedisService.get(vipPraiseCountKey));
					System.out.println("******AutoPraise*****vipPraiseCount:"+vipPraiseCount);
					for (int i=0;i<vipPraiseCount;i++) {
						System.out.println("******AutoPraise*****"+majiaForVideoKey+videoId+":"+jedisService.getSetSizeFromShard(majiaForVideoKey+videoId));
						String creatorId = jedisService.getSrandmember(majiaForVideoKey+videoId);  //从该视频对应的马甲用户id中随机取一个id
						if (creatorId == null || "".equals(creatorId)){  //如果马甲用完了 ，不执行点赞
							break;
						}
						
						System.out.println(new Date(start)+"************************start**************");
						Long praiseTime = start+getRandom(0, timeInterval.intValue());  //生成点赞时间
						System.out.println(new Date(praiseTime)+"**********************praise***************");
						
						Praise praise = new Praise();
						praise.setVideoId(Long.parseLong(videoId));
						praise.setCreateDate(new Date(praiseTime));
						
						praise.setCreatorId(Long.parseLong(creatorId));
						
						try {
							System.out.println("******AutoPraise******createid="+creatorId+"  videoid="+videoId);
							Praise p = praiseRepository.findByCreatorIdAndVideoId(Long.parseLong(creatorId), Long.parseLong(videoId));
							if(p==null){
								//点赞
								this.save(praise);
								//更新点赞数
								videoRepository.incPraiseCount(Long.parseLong(videoId)); 
								//更新播放数
								videoRepository.incPlayCount(1, Long.parseLong(videoId));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						jedisService.deleteSetItemFromShard(majiaForVideoKey+videoId, creatorId); //该马甲点赞后，从列表中删除
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private int getRandom(int min,int max){
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
	}
	public static void main(String[] args) {
//		System.out.println(new Date(1453168769391l));
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 5);
		System.out.println(calendar.getTime());
	}
}
