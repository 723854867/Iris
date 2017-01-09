package com.busap.vcs.restadmin.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.busap.vcs.service.LiveCheckLogService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.busap.vcs.base.WsMessage;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Room;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RoomService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.kafka.producer.WsMessageProducer;
import com.busap.vcs.service.utils.HttpPostUtils;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.webcomn.U;

/**
 * Created by huoshanwei on 2016/1/13.
 */
@Controller
@RequestMapping("/living")
public class LivingController {

/*    private static final String secretId = "5715cd97616106484771b0d7";

    private static final String url = "http://api.open.tuputech.com/v3/recognition/" + secretId;

    //色情taskId
    private static final String sexyTaskId = "54bcfc6c329af61034f7c2fc";

    //暴恐taskId
    private static final String terrorTaskId = "55c03cf238dc1cfb3d80be14";*/

    @Resource(name = "jedisService")
    private JedisService jedisService;

    @Resource(name = "ruserService")
    private RuserService ruserService;

    @Resource(name = "roomService")
    private RoomService roomService;

    @Resource(name="wsMessageProducer")
	private WsMessageProducer wsMessageProducer;
    
    private static final Logger logger = LoggerFactory.getLogger(LivingController.class);

    @Resource
    private LiveCheckLogService liveCheckLogService;

    /**
     * 直播管理 页面
     * @param
     * @return
     */
    @RequestMapping("forwardLivingList")
    public ModelAndView forwardLivingList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("living/query_living_list");
        mav.addObject("liveNotice",jedisService.get(BicycleConstants.LIVE_NOTICE));
        mav.addObject("loopNotice",jedisService.get("LOOP_LIVE_NOTICE"));
        return mav;
    }

    /**
     * 直播管理 数据请求
     * @param
     * @return
     */
    @RequestMapping("queryLivingList")
    @ResponseBody
    public Map<String, Object> queryLivingList(@ModelAttribute("query") JQueryPage queryPage) {
        Map<String, Object> map = new HashMap<String, Object>();
        Set<String> rooms = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_ORDER, (queryPage.getPage() - 1) * queryPage.getRows(), queryPage.getPage() * queryPage.getRows());
        //Set<String> rooms = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_ORDER, 0, -1);
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (String roomId : rooms) {
            Map<String, String> roomInfo = jedisService.getMapByKey(BicycleConstants.ROOM_ + roomId);
            if(roomInfo != null && roomInfo.size() > 0 && roomInfo.get("id") != null) {
                if (StringUtils.isBlank(roomInfo.get("anchorName"))) {
                    if (StringUtils.isNotBlank(roomInfo.get("creatorId")) && StringUtils.isNotBlank(roomInfo.get("id"))) {
                        Ruser ruser = ruserService.find(Long.parseLong(roomInfo.get("creatorId")));
                        roomInfo.put("anchorName", ruser.getName());
                    }
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (StringUtils.isNotBlank(roomInfo.get("createDate"))) {
                    roomInfo.put("createDate", sdf.format(new Date(Long.parseLong(roomInfo.get("createDate")))));
                }
                String rtmpLiveUrl = roomInfo.get("rtmpLiveUrl");
                if(StringUtils.isNotBlank(rtmpLiveUrl)){
                    if(rtmpLiveUrl.indexOf("ucloud")>=0){
                        //ucloud截图固定地址
                        roomInfo.put("streamSource", "ucloud");
                    }else if(rtmpLiveUrl.indexOf("plls") >=0){
                        //乐视固定地址
                        roomInfo.put("streamSource", "乐视");
                    }else if(rtmpLiveUrl.indexOf("plxy") >=0){
                        //星宇固定地址
                        roomInfo.put("streamSource", "星宇");
                    }else if(rtmpLiveUrl.indexOf("wsflv") >=0){
                        //星宇固定地址
                        roomInfo.put("streamSource", "网宿");
                    }else if(rtmpLiveUrl.indexOf("pili") >=0){
                        //星宇固定地址
                        roomInfo.put("streamSource", "七牛");
                    }else{
                        //其它固定地址
                        roomInfo.put("streamSource", "unknown");
                    }
                }
                if(StringUtils.isNotBlank(roomInfo.get("streamJson"))){
                    JSONObject streamJson = JSON.parseObject(roomInfo.get("streamJson"));
                    String hub = streamJson.getString("hub");
                    String title = streamJson.getString("title");
                    JSONObject publish = streamJson.getJSONObject("hosts").getJSONObject("publish");
                    String rtmp = publish.getString("rtmp");
                    String streamUrl = "rtmp://";
                    if("busappstream".equals(hub)){
                        String publishKey = streamJson.getString("publishKey");
                        streamUrl += rtmp+"/"+hub+"/"+title+"?key="+publishKey;
                    }else {
                        streamUrl += rtmp+"/"+hub+"/"+title;
                    }
                    roomInfo.put("streamUrl",streamUrl);
                }
                list.add(roomInfo);
            }
        }
        Long allRoomTotal = jedisService.getSortedSetSizeFromShard(BicycleConstants.ROOM_ORDER);
        map.put("rows", list);
        map.put("total", allRoomTotal);
        return map;
    }


    /**
     * 直播管理 下线
     * @param roomId
     * @return
     */
    @RequestMapping("offline")
    @ResponseBody
    public String offline(Long roomId,String message,Integer expireMin){
        Room room = roomService.find(roomId);
        if(room != null) {
            jedisService.set(BicycleConstants.LOCK_LIVE_USER + room.getCreatorId(), "" + (System.currentTimeMillis() + expireMin * 60 * 1000));
            jedisService.expire(BicycleConstants.LOCK_LIVE_USER + room.getCreatorId(), expireMin * 60);
            try {
                message = message + ",账户将被冻结" + expireMin + "分钟";
                roomService.offlineRoom(roomId, U.getUid(), message);
                //加入日志
                liveCheckLogService.check(U.getUid(), "下线", message, roomId, room.getCreatorId(),"live");
                return "success";
            } catch (Exception e) {
                e.printStackTrace();
                return "fail";
            }
        }else{
            return "fail";
        }
    }

    /**
     * 直播公告
     * @param content
     * @return
     */
    @RequestMapping("settingLiveNotice")
    @ResponseBody
    public String settingLiveNotice(String content,String loopNotice){
        try {
            jedisService.set(BicycleConstants.LIVE_NOTICE,content);
            jedisService.set("LOOP_LIVE_NOTICE",loopNotice);
            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
    }
    
/*    private WsMessage buildLiveEndMsg(String roomId,String adminId,String content){
		WsMessage m = new WsMessage();
		m.setCode("400");
		m.setChildCode("4003");
		m.setRoomId(roomId);
		m.setSenderId(adminId);
		m.setContent(content);
		m.setTitle("直播终结");
		
		return m;
	}*/

/*    @RequestMapping("forwardIrregularityImage")
    public ModelAndView forwardIrregularityImage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("living/query_irregularity_image");
        return mav;
    }*/

/*    @RequestMapping("queryIrregularityImage")
    @ResponseBody
    public Map<String,Object> queryIrregularityImage(@ModelAttribute("queryPage") JQueryPage queryPage){
        Map<String, Object> map = new HashMap<String, Object>();
        Set<String> irregularityImage = jedisService.getSetFromShard(BicycleConstants.IRREGULARITY_IMAGE);
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (String irregularityImageId : irregularityImage) {
            Map<String, String> irregularityImageInfo = jedisService.getMapByKey(BicycleConstants.IRREGULARITY_IMAGE_STREAMID + irregularityImageId);
            irregularityImageInfo.put("streamId",irregularityImageId);
            if(irregularityImageInfo != null && irregularityImageInfo.size() > 0 && irregularityImageInfo.get("id") != null) {
                list.add(irregularityImageInfo);
            }
        }
        Long total = jedisService.getSetSizeFromShard(BicycleConstants.IRREGULARITY_IMAGE);
        map.put("rows", list);
        map.put("total", total);
        return map;
    }*/

    /**
     * 违规信息 下线
     * @param roomId
     * @param message
     * @param expireMin
     * @param streamId
     * @return
     */
/*    @RequestMapping("offlineIrregularityRoom")
    @ResponseBody
    public String offlineIrregularityRoom(Long roomId,String message,Integer expireMin,String streamId){
        Room room = roomService.find(roomId);
        jedisService.set(BicycleConstants.LOCK_LIVE_USER+room.getCreatorId(), ""+(System.currentTimeMillis()+expireMin*60*1000));
        jedisService.expire(BicycleConstants.LOCK_LIVE_USER+room.getCreatorId(), expireMin*60);
        try {
            message = message + ",账户将被冻结" + expireMin + "分钟";
            WsMessage msg = this.buildLiveEndMsg(roomId+"", U.getUid().toString(), message);//消息内容可以改为页面输入
          //发送到chat
			Set<String> urls = jedisService.getSetFromShard(BicycleConstants.ROOM_SERVERURLS+roomId);
			if(urls != null && !urls.isEmpty()){
				String mess=null;
				try {
					mess = URLEncoder.encode(msg.toString(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Map<String,String> params = new HashMap<String,String>();
				for(String url:urls){
					String fullUrl = "http://"+url+"/ws/message?message="+mess;
					HttpPostUtils.doHttpPost(fullUrl, params);
				}
			}
        	
            wsMessageProducer.send("chat_topic_", msg);
            logger.info("直播管理,发送强制下线消息：{}",msg);
            jedisService.deleteSortedSetItemFromShard(BicycleConstants.ROOM_ORDER, roomId.toString());//默认清除缓存
            jedisService.deleteSetItemFromShard(BicycleConstants.IRREGULARITY_IMAGE,streamId);//删除缓存中违规图片信息
            jedisService.delete(BicycleConstants.IRREGULARITY_IMAGE_STREAMID+streamId);
            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
    }*/

    /**
     * 违规信息 忽略
     * @param streamId
     * @return
     */
/*    @RequestMapping("ignoreIrregularityRoom")
    @ResponseBody
    public String ignoreIrregularityRoom(String streamId){
        try {
            jedisService.deleteSetItemFromShard(BicycleConstants.IRREGULARITY_IMAGE,streamId);//删除缓存中违规图片信息
            jedisService.delete(BicycleConstants.IRREGULARITY_IMAGE_STREAMID+streamId);
            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
    }*/

}
