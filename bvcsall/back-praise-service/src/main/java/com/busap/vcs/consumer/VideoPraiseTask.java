package com.busap.vcs.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import com.busap.vcs.srv.IVideoPraiseService;


/**
 * 视频点赞的执行任务
 * @author wangyongfei
 *
 */
public class VideoPraiseTask implements Job {
	protected static final Logger log = LoggerFactory.getLogger(VideoPraiseTask.class);
	
	//@Value("#{configProperties['max_handle_count']}")
	//private  int maxHandleCount = 4;//最大的处理次数，比如一个视频可以在四个时间点（使用Quarts的Trigger来配置）来处理
	private static BlockingQueue<PraiseTaskItem> workQueue = new LinkedBlockingQueue<PraiseTaskItem>();//需要处理的队列
	private List<PraiseTaskItem> reentryList = new ArrayList<PraiseTaskItem>();//重新入队列表
	private volatile boolean isDone = false;//是否完成
	
	//@Autowired
	//IVideoPraiseService videoPraiseService;
	
	public void execute(){
		/*log.info("==========================================>do praise task...maxHandleCount="+maxHandleCount);
		PraiseTaskItem currentItem = null;
		try {
			while((currentItem = workQueue.poll())!=null){
				//TODO 执行点赞
				//计算当次需要的点赞数
				//随机抽取一个马甲用户进行点赞
				videoPraiseService.doPraise(currentItem.getId());
				int currentCount = currentItem.getCount();
				//如果处理次数尚未达到上限，将任务项计数加1;并重新入队
				if(currentCount < (maxHandleCount-1)){
					currentItem.setCount(currentCount+1);
					reentryList.add(currentItem);
				}
				log.info("=====================>PraiseTaskItem is {}",currentItem.toString());
			}
			log.info("=====================>reentryList is {}",reentryList);
			//将需要重新入队的任务项加入到队列
			for(PraiseTaskItem tastItem : reentryList){
				workQueue.offer(tastItem);
			}
			//清空重新入队列表
			reentryList = new ArrayList<PraiseTaskItem>();
		} catch (Exception e) {
			log.error("VideoPraiseTask : ",e);
		}*/
		
	}
	
	public static void addTaskItem(PraiseTaskItem item){
		try {
			workQueue.offer(item);
		} catch (Exception e) {
		}
	}
	
	
	/**
	 * 赞任务项
	 * @author wangyongfei
	 *
	 */
	public static class PraiseTaskItem{
		private long id;//视频id
		private int count ;//处理计数
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		
		public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append("id=").append(id).append(";");
			sb.append("count=").append(count);
			return sb.toString();
			
		}
		
		
	}


	@Override
	public void execute(JobExecutionContext jec) throws JobExecutionException {
		JobDataMap dataMap = jec.getJobDetail().getJobDataMap();
		ApplicationContext ctx = (ApplicationContext)dataMap.get("applicationContext");
		String maxHandleCount = ctx.getBean("maxHandleCount", String.class);
		int _maxHandleCount = Integer.parseInt(maxHandleCount);
		int min = Integer.parseInt(dataMap.getString("min"));
		int max = Integer.parseInt(dataMap.getString("max"));
		log.info("==========================================>do praise task...maxHandleCount="+maxHandleCount);
		PraiseTaskItem currentItem = null;
		try {
			while((currentItem = workQueue.poll())!=null){
				//TODO 执行点赞
				//计算当次需要的点赞数
				//随机抽取一个马甲用户进行点赞
				IVideoPraiseService videoPraiseService = ctx.getBean(IVideoPraiseService.class);
				videoPraiseService.doPraise(currentItem.getId(),min,max);
				int currentCount = currentItem.getCount();
				//如果处理次数尚未达到上限，将任务项计数加1;并重新入队
				if(currentCount < (_maxHandleCount-1)){
					currentItem.setCount(currentCount+1);
					reentryList.add(currentItem);
				}
				log.info("=====================>PraiseTaskItem is {}",currentItem.toString());
			}
			log.info("=====================>reentryList is {}",reentryList);
			//将需要重新入队的任务项加入到队列
			for(PraiseTaskItem tastItem : reentryList){
				workQueue.offer(tastItem);
			}
			//清空重新入队列表
			reentryList = new ArrayList<PraiseTaskItem>();
		} catch (Exception e) {
			log.error("VideoPraiseTask : ",e);
		}
		
	}
}
