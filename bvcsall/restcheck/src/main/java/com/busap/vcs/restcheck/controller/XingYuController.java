package com.busap.vcs.restcheck.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.busap.vcs.data.entity.MissingPlaybackInfo;
import com.busap.vcs.data.entity.Room;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.data.enums.VideoStatus;
import com.busap.vcs.data.repository.RoomRepository;
import com.busap.vcs.service.AnchorService;
import com.busap.vcs.service.RoomService;
import com.busap.vcs.service.VideoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by busap on 2016/8/1.
 */
@Controller
@RequestMapping("xy")
public class XingYuController {

    private static final Logger logger = LoggerFactory.getLogger(XingYuController.class);

    @Resource
    private AnchorService anchorService;

    @Resource
    private RoomRepository roomRepository;

    @Resource
    private VideoService videoService;

    @Resource
    private RoomService roomService;

    /**
     * xingyu回放回调
     *
     * @return
     */
    @RequestMapping(value = "/receivePlaybackInfo")
    @ResponseBody
    public void receivePlaybackInfo(HttpServletRequest request) {
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
            logger.info("============="+stream);
            if (StringUtils.isNotBlank(stream)) {
                JSONObject jsonObject = JSON.parseObject(stream);
                String streamId = jsonObject.getString("stream");
                String taskId = jsonObject.getString("task_id");
                String notifyType = jsonObject.getString("notify_type");
                if("start".equals(notifyType)) {
                    //根据streamId查询主播最新正在直播中的房间信息
                    Map<String, Object> params = new HashMap<String, Object>(1);
                    params.put("streamId", streamId);
                    Long maxRoomId = anchorService.queryMaxRoomIdByStreamId(params);
                    logger.info("maxRoomId==========" + maxRoomId);
                    if (maxRoomId != null) {
                        if(StringUtils.isNotBlank(taskId)) {
                            Room room = roomRepository.findOne(maxRoomId);
                            if (StringUtils.isNotBlank(room.getPersistentId())) {
                                MissingPlaybackInfo missingPlaybackInfo = videoService.selectMissPlaybackByPersistentId(taskId);
                                if (missingPlaybackInfo == null) {
                                    MissingPlaybackInfo mpi = new MissingPlaybackInfo();
                                    mpi.setPersistentId(taskId);
                                    mpi.setRoomId(room.getId());
                                    mpi.setStreamId(streamId);
                                    mpi.setCreateDate(new Date());
                                    videoService.insertMissPlayback(mpi);
                                    logger.info("streamId:" + streamId + "，此房间已通知过，重复通知，taskId:" + taskId + "，roomId:" + room.getId() + "===" + maxRoomId);
                                }
                            } else {
                                //将persistentId写入room表
                                room.setPersistentId(taskId);
                                roomRepository.save(room);
                                logger.info("关联room房间信息，maxRoomId:" + maxRoomId + ",taskId:" + taskId);
                            }
                        }else{
                            logger.error("taskId不存在，taskId:" + taskId);
                        }
                    } else {
                        logger.error("主播信息不存在，streamId:" + streamId);
                    }

                }else{
                    String url = jsonObject.getString("vod_url");
                    String duration = jsonObject.getString("vod_duration");
                    //根据streamId查询主播最新房间信息
                    Map<String, Object> params = new HashMap<String, Object>(1);
                    params.put("persistentId", taskId);
                    Room room = roomService.queryRoomByPersistentId(params);
                    if (room != null) {
                        //根据roomId查询video信息，如存在，保存视频步骤，记录相关日志
                        Video videoInfo = videoService.getDeletePlaybackByRoomId(room.getId());
                        if (videoInfo != null) {
                            //保存回放
                            videoInfo.setDuration(String.valueOf(duration));
                            videoInfo.setPlayKey(url);
                            videoInfo.setFlowStat(VideoStatus.审核通过.getName()); //将回放信息更新为通过状态
                            videoService.save(videoInfo);
                        } else {
                            //missing_playback_info表查询taskId，如果存在，更新playKey
                            MissingPlaybackInfo mpi = videoService.selectMissPlaybackByPersistentId(taskId);
                            if(mpi != null){
                                mpi.setPlaykey(url);
                                mpi.setModifyDate(new Date());
                                videoService.updateMissPlaybackByPersistentId(mpi);
                            }else{
                                MissingPlaybackInfo missingPlaybackInfo = new MissingPlaybackInfo();
                                missingPlaybackInfo.setPersistentId(taskId);
                                missingPlaybackInfo.setRoomId(room.getId());
                                missingPlaybackInfo.setPlaykey(url);
                                missingPlaybackInfo.setStreamId(streamId);
                                missingPlaybackInfo.setCreateDate(new Date());
                                videoService.insertMissPlayback(missingPlaybackInfo);
                            }
                            logger.info("视频信息不存在，roomId:"+room.getId()+",url:"+url);
                        }
                    } else {
                        //missing_playback_info表查询persistentId，如果存在，更新playKey
                        MissingPlaybackInfo mpi = videoService.selectMissPlaybackByPersistentId(taskId);
                        if(mpi != null){
                            mpi.setPlaykey(url);
                            mpi.setModifyDate(new Date());
                            videoService.updateMissPlaybackByPersistentId(mpi);
                        }else{
                            MissingPlaybackInfo missingPlaybackInfo = new MissingPlaybackInfo();
                            missingPlaybackInfo.setPersistentId(taskId);
                            missingPlaybackInfo.setPlaykey(url);
                            missingPlaybackInfo.setStreamId(streamId);
                            missingPlaybackInfo.setCreateDate(new Date());
                            videoService.insertMissPlayback(missingPlaybackInfo);
                        }
                        logger.info("开始通知未获取到相关taskId，taskId：" + taskId);
                    }
                }
            } else {
                //stream信息不存在
                logger.error("======stream流信息不能为空");
            }
        } catch (IOException e) {
            //异常处理
            //logger.error();
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

}
