package com.busap.vcs.srv.impl;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.dao.VideoCheckDao;
import com.busap.vcs.srv.IMajiaPraiseService;
import com.busap.vcs.srv.JedisService;


@Service
public class MajiaPraiseService implements  IMajiaPraiseService{
	private static final Logger logger = LoggerFactory.getLogger(MajiaPraiseService.class);
	@Autowired
	VideoCheckDao videoCheckDao;
	@Autowired
	JedisService jedisService;
	@Override
	public boolean doPraise(final long vid , int praiseNumber ) {
		try{
			int count = 0;
			List<String> majiaUids = jedisService.getSetRanomMembers(UserFocusService.KEY_ALL_MAJIA_UID, praiseNumber+10);
			for(final String mid:majiaUids){
				if(!videoCheckDao.isPraise(Long.parseLong(mid), vid)){
					
					logger.debug("-------------doPraise: mid,vid， praiseNumber--"+mid+"--"+vid+"--"+praiseNumber);
					
					final Timer timer = new Timer();
					timer.schedule(new TimerTask() {
						public void run() {
							try {
								videoCheckDao.insertPraise(Long.parseLong(mid), vid,System.currentTimeMillis());
								videoCheckDao.updateVideoPraiseCount(1, vid);
							} catch (Exception e) {
								logger.error("-------------doPraise error: insert praise failed! vid=" + vid + " : " + e.getMessage());
							}
							timer.cancel();
						}
					},this.delay() );
					count ++;
				}
				if(count>=praiseNumber)	break;
			}
		}catch(Exception e ){
			e.printStackTrace();
		}
		return true;
	}
	private long delay(){
		return new Random().nextInt(10*60*1000);
	}
	public static void main(String args[]){
		for(int i=0;i<100;i++)System.out.println(new MajiaPraiseService().delay());
	}
}
