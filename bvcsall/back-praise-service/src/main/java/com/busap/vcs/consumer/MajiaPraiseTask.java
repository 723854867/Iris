package com.busap.vcs.consumer;

import java.util.*;

import com.busap.vcs.constants.BicycleConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.bean.VideoTaskItem;
import com.busap.vcs.dao.VideoCheckDao;
import com.busap.vcs.srv.JedisService;
import com.busap.vcs.srv.impl.MajiaPraiseService;

@Service
public class MajiaPraiseTask extends TimerTask{
	private static final Logger logger = LoggerFactory.getLogger(MajiaPraiseTask.class);
	@Autowired
	private MajiaPraiseService majiaPraiseService;
	@Autowired
	VideoCheckDao videoCheckDao;
	@Autowired
	JedisService jedisService;
	@Override
	public void run(){
		try{
			// 要清除的关注视频消息
			List<VideoTaskItem>  deleteList = new ArrayList<VideoTaskItem>();
			logger.debug("自动点赞参数信息--------praiseList.size()-----------------------" + Consumer.praiselist.size());
			for (VideoTaskItem vti : Consumer.praiselist) {
				long now = System.currentTimeMillis();
				if (vti.getPubDate() < (now - (650 * 60 * 1000))) {
					deleteList.add(vti);
				}
//				if (!checkVideoPlayedCount(vti.getVid())) {
//				取消播放数限制
//					continue;
//				}
				if (videoCheckDao.isDelete(vti.getVid())) {
					// 如果视频已经被删除，清除该视频任务
					deleteList.add(vti);
				} else if(this.checkTime(vti.getPcount(), vti.getPubDate()) && vti.getPcount() == 0){
					// 第一批点赞
					majiaPraiseService.doPraise(vti.getVid(),this.getPraiseNumber(vti.getPcount(), String.valueOf(vti.getUid())));
					vti.setPcount(vti.getPcount()+1);
				} else if(this.checkTime(vti.getPcount(), vti.getPubDate()) && vti.getPcount() == 1){
					// 第二批点赞
					majiaPraiseService.doPraise(vti.getVid(),this.getPraiseNumber(vti.getPcount(), String.valueOf(vti.getUid())));
					vti.setPcount(vti.getPcount()+1);
				} else if(this.checkTime(vti.getPcount(), vti.getPubDate()) && vti.getPcount() == 2){
					// 第仨批点赞
					majiaPraiseService.doPraise(vti.getVid(),this.getPraiseNumber(vti.getPcount(), String.valueOf(vti.getUid())));
					vti.setPcount(vti.getPcount()+1);
				} else if(this.checkTime(vti.getPcount(), vti.getPubDate()) && vti.getPcount() == 3){
					// 第四批点赞
					majiaPraiseService.doPraise(vti.getVid(),this.getPraiseNumber(vti.getPcount(), String.valueOf(vti.getUid())));
					vti.setPcount(vti.getPcount()+1);
				} else if(this.checkTime(vti.getPcount(), vti.getPubDate()) && vti.getPcount() == 4){
					// 第五批点赞
					majiaPraiseService.doPraise(vti.getVid(),this.getPraiseNumber(vti.getPcount(), String.valueOf(vti.getUid())));
					vti.setPcount(vti.getPcount()+1);
				} else if(this.checkTime(vti.getPcount(), vti.getPubDate()) && vti.getPcount() == 5){
					// 第六批点赞
					majiaPraiseService.doPraise(vti.getVid(),this.getPraiseNumber(vti.getPcount(), String.valueOf(vti.getUid())));
					vti.setPcount(vti.getPcount()+1);
					deleteList.add(vti);
				}
			}
			logger.debug("自动点赞参数信息--------delete.size()-----------------------" + deleteList.size());
			for (VideoTaskItem vti : deleteList) {
				Consumer.praiselist.remove(vti);
			}
			logger.debug("自动点赞参数信息--------praiseList.size()------------over-----------" + Consumer.praiselist.size());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

//	/**
//	 * 检查这个视频的点赞数是否小于播放数
//	 * @param videoId 视频id
//	 * @return true:点赞 < 播放; false:点赞 > 播放;
//	 */
//	public boolean checkVideoPlayedCount(long videoId) {
//		return videoCheckDao.checkPlayedPraiseCount(videoId);
//	}

	private int getPraiseNumber(int pcount, String uid){
		boolean isBlue = jedisService.isSetMemberInShard(BicycleConstants.VIP_OF_BLUE, uid);
		boolean isYellow = jedisService.isSetMemberInShard(BicycleConstants.VIP_OF_YELLOW, uid);
		if (isBlue || isYellow) {
			// 是VIP
			if(pcount == 0)
				return 10 + new Random().nextInt(20);
			else if(pcount == 1)
				return 30 + new Random().nextInt(30);
			else if(pcount == 2)
				return 40 + new Random().nextInt(40);
			else if(pcount == 3)
				return 30 + new Random().nextInt(30);
			else if(pcount == 4)
				return 20 + new Random().nextInt(20);
			else if(pcount == 5)
				return 10 + new Random().nextInt(20);
			else return 0;
		} else {
			if(pcount == 0)
				return 5 + new Random().nextInt(10);
			else if(pcount == 1)
				return 10 + new Random().nextInt(10);
			else if(pcount == 2)
				return 20 + new Random().nextInt(10);
			else if(pcount == 3)
				return 20 + new Random().nextInt(10);
			else if(pcount == 4)
				return 10 + new Random().nextInt(10);
			else if(pcount == 5)
				return 5 + new Random().nextInt(5);
			else return 0;
		}
	}

	private boolean checkTime(int pcount,long pubDate){
		long start;
		if (pcount == 0) {
			start = pubDate + 2 * 60 * 1000;
		} else if (pcount == 1) {
			start = pubDate + 25 * 60 * 1000;
		} else if (pcount == 2) {
			start = pubDate + 80 * 60 * 1000;
		} else if (pcount == 3) {
			start = pubDate + 150 * 60 * 1000;
		} else if (pcount == 4) {
			start = pubDate + 300 * 60 * 1000;
		} else if (pcount == 5) {
			start = pubDate + 600 * 60 * 1000;
		} else {
			start = 0;
		}
		long end;
		if (pcount == 0) {
			end = pubDate + 15 * 60 * 1000;
		} else if (pcount == 1) {
			end = pubDate + 60 * 60 * 1000;
		} else if (pcount == 2) {
			end = pubDate + 120 * 60 * 1000;
		} else if (pcount == 3) {
			end = pubDate + 280 * 60 * 1000;
		} else if (pcount == 4) {
			end = pubDate + 500 * 60 * 1000;
		} else if (pcount == 5) {
			end = pubDate + 650 * 60 * 1000;
		} else {
			end = 0;
		}
		long now = new Date().getTime();
		return now > start && now < end;
	}

}