package com.busap.vcs.restcheck.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.busap.vcs.base.WsMessage;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Anchor;
import com.busap.vcs.data.entity.Room;
import com.busap.vcs.data.model.AnchorInfo;
import com.busap.vcs.service.AnchorService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RoomService;
import com.busap.vcs.service.kafka.producer.WsMessageProducer;
import com.busap.vcs.service.utils.HttpPostUtils;
import com.busap.vcs.service.utils.TupuSecurityUtil;
import com.busap.vcs.service.utils.TupuUtils;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by busap on 2016/4/22.
 */
@Controller()
@RequestMapping("qiniu")
public class QiniuController {

    //色情taskId
    private static final String sexyTaskId = "54bcfc6c329af61034f7c2fc";

    //暴恐taskId
    private static final String terrorTaskId = "55c03cf238dc1cfb3d80be14";

    //截图存放url地址
    private static final String screenshotImageUrl = "http://pili-static.play.wopaitv.com/";

    private static final Logger logger = LoggerFactory.getLogger(QiniuController.class);

    @Resource
    private WsMessageProducer wsMessageProducer;

    @Resource
    private JedisService jedisService;

    @Resource
    private AnchorService anchorService;

    @Resource
    private RoomService roomService;

    /**
     * 接收七牛推送截图【保存图片信息至redis】
     *
     * @return
     */
    @RequestMapping(value = "receiveQiniuScreenshot")
    public void receiveQiniuScreenshot(HttpServletRequest request){
        InputStream is = null;
        try {
            is = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String stream = sb.toString();
            logger.info("stream:================"+stream);
            if (StringUtils.isNotBlank(stream)) {
                JSONObject json = JSON.parseObject(stream);
                JSONArray jsArr = json.getJSONArray("items");
                JSONObject item = jsArr.getJSONObject(0);
                String imgName = item.getString("key");
                logger.info("stream key:================"+imgName);
                String nameArr[] = imgName.split("/");
                String streamId = nameArr[0];
                //根据streamId查询主播最新房间信息
                Map<String, Object> params = new HashMap<String, Object>(1);
                params.put("qiniuStreamId", streamId);
                AnchorInfo anchorInfo = anchorService.queryAnchorByStreamId(params);
                if(anchorInfo != null && anchorInfo.getRoomId() != null && anchorInfo.getRoomId() > 0) {
                    String imageUrl = screenshotImageUrl + imgName;
                    jedisService.setValueToSortedSetInShard(BicycleConstants.ROOM_SCREENSHOT_ID + anchorInfo.getRoomId(), System.currentTimeMillis(), imageUrl);
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 直播截屏图片回调
     *
     * @return
     */
    @RequestMapping(value = "/liveScreenshotCallback")
    @ResponseBody
    public RespBody liveScreenshotCallback(HttpServletRequest request) {
        InputStream is = null;
        try {
            is = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String stream = sb.toString();
            logger.info("截图回调：" + sb.toString());
            if (StringUtils.isNotBlank(stream)) {
                JSONObject json = JSON.parseObject(stream);
                JSONArray jsArr = json.getJSONArray("items");
                JSONObject item = jsArr.getJSONObject(0);
                String imgName = item.getString("key");
                String nameArr[] = imgName.split("/");
                String streamId = nameArr[0];
                //String streamIdArr[] = streamId.split("\\.");
                //String anchorStreamId = streamIdArr[2];//主播streamId
                logger.info("cut image name:" + imgName + "  streamId:" + streamId);
                //请求图普接口
                String imageUrl = screenshotImageUrl + imgName;
                String result = TupuUtils.sendScreenshotToTupu(imageUrl);
                if (!result.equals("err")) {
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    String resultJson = jsonObject.getString("json");
                    String resultSignature = jsonObject.getString("signature");
                    // 进行验证
                    boolean verify = TupuSecurityUtil.verify(resultSignature, resultJson);
                    if (verify) {
                        logger.info("验证成功，开始处理");
                        //解析结果 进行业务处理
                        JSONObject ret = JSONObject.parseObject(resultJson);
                        if ("0".equals(ret.getString("code"))) {
                            JSONObject sexyObject = ret.getJSONObject(sexyTaskId);
                            if (!sexyObject.isEmpty()) {
                                JSONArray sexyFileList = JSONArray.parseArray(sexyObject.getString("fileList"));
                                //for(int i = 0;i<=sexyFileList.size();i++ ){
                                if (!sexyFileList.isEmpty()) {
                                    //识别分类 0：色情 1：性感 2：正常
                                    String sexyLabel = sexyFileList.getJSONObject(0).getString("label");
                                    Double sexyRate = sexyFileList.getJSONObject(0).getDouble("rate");
                                    if ("0".equals(sexyLabel)) {
                                        //机器检测色情，进行业务处理 1色情
                                        //if (sexyRate.compareTo(95.000) >= 0) {
                                            //直接下线处理

                                        //} else {
                                            saveIrregularityImageToRedis(streamId, imageUrl, String.valueOf(1), sexyRate);
                                        //}
                                    }
                                }
                            }
                            //}
                            JSONObject terrorObject = ret.getJSONObject(terrorTaskId);
                            if (!terrorObject.isEmpty()) {
                                JSONArray terrorFileList = JSONArray.parseArray(terrorObject.getString("fileList"));
                                if (!terrorFileList.isEmpty()) {
                                    String terrorLabel = terrorFileList.getJSONObject(0).getString("label");
                                    Double terrorRate = terrorFileList.getJSONObject(0).getDouble("rate");
                                    //识别分类 0：非暴恐 1：暴恐
                                    if ("1".equals(terrorLabel)) {
                                        //机器检测暴恐，进行业务处理 2暴恐
                                        //if (terrorRate.compareTo(95.000) >= 0) {
                                            //直接下线处理

                                        //} else {
                                            saveIrregularityImageToRedis(streamId, imageUrl, String.valueOf(2), terrorRate);
                                        //}

                                    }
                                }
                            }
                            logger.info("处理成功！！！" + ret);
                        } else {
                            //调用接口出现错误
                            logger.info("调用图普接口出现错误：code码：" + ret.getString("code") + "，提示信息：" + ret.getString("message") + "，json信息：" + resultJson);
                        }
                    } else {
                        logger.info("验证失败：" + resultJson);
                    }
                } else {
                    logger.info("error：" + result);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new RespBody();
    }

    private void saveIrregularityImageToRedis(String streamId, String imageUrl, String type, Double rate) {
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("qiniuStreamId", streamId);
        //params.put("qiniuStreamId","z1.busappstream.95dc0439bce136b7");
        AnchorInfo anchorInfo = anchorService.queryAnchorByStreamId(params);
        if (anchorInfo != null) {
            String streamIdArray[] = streamId.split("\\.");
            String streamIdInfo = streamIdArray[2];
            if (StringUtils.isNotBlank(streamIdInfo)) {
                jedisService.setValueToSetInShard(BicycleConstants.IRREGULARITY_IMAGE, streamIdInfo);
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", String.valueOf(anchorInfo.getId()));
                map.put("name", anchorInfo.getName());
                map.put("username", anchorInfo.getUsername());
                map.put("phone", anchorInfo.getPhone());
                map.put("imageUrl", imageUrl);
                map.put("rate", String.valueOf(rate));
                map.put("type", type);
                map.put("roomId", String.valueOf(anchorInfo.getRoomId()));
                jedisService.setValueToMap(BicycleConstants.IRREGULARITY_IMAGE_STREAMID + streamIdInfo, map);
            }
        } else {
            logger.info("主播不存在");
        }

    }

    private WsMessage buildLiveEndMsg(String roomId,String adminId,String content){
        WsMessage m = new WsMessage();
        m.setCode("400");
        m.setChildCode("4003");
        m.setRoomId(roomId);
        m.setSenderId(adminId);
        m.setContent(content);
        m.setTitle("直播终结");
        return m;
    }

    //下线
    private String offlineIrregularityRoom(String message, Integer expireMin, String streamId) {
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("qiniuStreamId", streamId);
        AnchorInfo anchorInfo = anchorService.queryAnchorByStreamId(params);
        if(anchorInfo != null && anchorInfo.getRoomId() != null && anchorInfo.getRoomId() > 0){
            Room room = roomService.find(Long.valueOf(anchorInfo.getRoomId()));
            jedisService.set(BicycleConstants.LOCK_LIVE_USER + room.getCreatorId(), "" + (System.currentTimeMillis() + expireMin * 60 * 1000));
            jedisService.expire(BicycleConstants.LOCK_LIVE_USER + room.getCreatorId(), expireMin * 60);
            try {
                message = message + ",账户将被冻结" + expireMin + "分钟";
                WsMessage msg = this.buildLiveEndMsg(anchorInfo.getRoomId() + "", U.getUid().toString(), message);//消息内容可以改为页面输入
                wsMessageProducer.send("chat_topic_", msg);
                logger.info("直播管理,发送强制下线消息：{}", msg);
                jedisService.deleteSortedSetItemFromShard(BicycleConstants.ROOM_ORDER, String.valueOf(anchorInfo.getRoomId()));//默认清除缓存
                return "success";
            } catch (Exception e) {
                e.printStackTrace();
                return "fail";
            }
        }else {
            return "fail";
        }
    }
}
