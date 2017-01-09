package com.busap.vcs.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.busap.vcs.base.Action;
import com.busap.vcs.base.Message;
import com.busap.vcs.base.Module;
import com.busap.vcs.data.enums.TaskTypeSecondEnum;
import com.busap.vcs.service.IntegralService;
import com.busap.vcs.service.kafka.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.Forward;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.ForwardService;
import com.busap.vcs.service.VideoService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;
import com.busap.vcs.webcomn.controller.CRUDController;
 
@Controller
@RequestMapping("/forward")
public class ForwardController extends CRUDController<Forward, Long> {

    private Logger logger = LoggerFactory.getLogger(ForwardController.class);
	@Resource(name="integralService")
	IntegralService integralService;

	@Resource(name="forwardService")
    ForwardService forwardService; 
    
    @Resource(name = "videoService")
	private VideoService videoService;

	@Autowired
	private KafkaProducer kafkaProducer;

    @Resource(name="forwardService")
    @Override
    public void setBaseService(BaseService<Forward, Long> baseService) {
        this.baseService = baseService;
    }
    
    @Resource(name = "respBodyBuilder")
	private RespBodyBuilder respBodyWriter = new RespBodyBuilder();

    
    //添加转发
    @RequestMapping("/addForward")
    @ResponseBody
    public RespBody addForward(Long videoId,Long authorId,String evaluation,Integer operate,String dataFrom){ 
    	String uid = (String) this.request.getHeader("uid");
		logger.info("uid={},videoId={},authorId={},operate={},evaluation={},addForward", uid,videoId,authorId,evaluation,operate);
		
		if (operate == 1){ //添加转发
			Video forwardVideo = videoService.getVideo(videoId, uid);
			//转发视频不存在，提示用户
			if (forwardVideo == null)
				return this.respBodyWriter.toError(ResponseCode.CODE_345.toString(), ResponseCode.CODE_345.toCode());
			
			//如果已经转发过，提示用户
			if (forwardService.isForward(videoId, Long.parseLong(uid)))
				return this.respBodyWriter.toError(ResponseCode.CODE_344.toString(), ResponseCode.CODE_344.toCode());
			
			//不能转发自己的视频
			if (forwardVideo.getCreatorId().intValue() == Long.parseLong(uid)) {
				return this.respBodyWriter.toError(ResponseCode.CODE_343.toString(), ResponseCode.CODE_343.toCode());
			}
			try {
				integralService.getIntegralFromDaily(Long.parseLong(uid), String.valueOf(videoId), TaskTypeSecondEnum.forwardVideo);
			} catch (Exception e) {
				logger.error("getIntegralFromDaily : forward error! userId=" + uid + " & videoId=" + videoId);
				e.printStackTrace();
			}

	    	Forward f = new Forward();
	    	f.setCreateDate(new Date());
	    	f.setCreatorId(Long.parseLong(uid));
	    	f.setDataFrom(dataFrom);
	    	f.setEvaluation(evaluation);
	    	f.setVideoId(videoId);
	    	f.setAuthorId(authorId);
	    	f.setVideoPic(forwardVideo.getVideoPic());
			sendMsg(uid, videoId, authorId);
	    	f.setVideoPic(forwardVideo.getVideoPic());
	    	return this.create(f); 
		} else { //取消转发
			forwardService.cancelForward(videoId, Long.parseLong(uid));
			return this.respBodyWriter.toSuccess();
		}
    }
    
    //批量删除转发
    @RequestMapping("/multiCancelForward")
    @ResponseBody
    public RespBody multiCancelForward(String videoIds){ 
    	String uid = (String) this.request.getHeader("uid");
		logger.info("uid={},videoIds={}", uid,videoIds);
		
		if (videoIds != null && !"".equals(videoIds)) {
			String[] videoIdArray = videoIds.split(",");
			for(int i=0;i<videoIdArray.length;i++) {
				forwardService.cancelForward(Long.parseLong(videoIdArray[i]), Long.parseLong(uid));
			}
		}
		return this.respBodyWriter.toSuccess();
    }

	/**
	 * 发送消息 -> kafka
	 * @param uid 转发人Id
	 * @param videoId 视频ID
	 * @param creatorId 视频创建者ID
	 */
	private void sendMsg(String uid, Long videoId, Long creatorId) {
		Message msg = new Message();
		msg.setModule(Module.FORWARD);
		msg.setAction(Action.INSERT);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("vid", String.valueOf(videoId));
		map.put("pid", String.valueOf(creatorId));
		msg.setDataMap(map);
		kafkaProducer.send("push-msg-topic", msg);
	}
}

