package com.busap.vcs.restcheck.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.MissingPlaybackInfo;
import com.busap.vcs.data.entity.Room;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.data.enums.VideoStatus;
import com.busap.vcs.data.model.AnchorInfo;
import com.busap.vcs.data.repository.RoomRepository;
import com.busap.vcs.service.*;
import com.busap.vcs.service.utils.Base64;
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
 * Created by busap on 2016/6/28.
 */
@Controller
@RequestMapping("wcs")
public class WangSuController {


    private static final Logger logger = LoggerFactory.getLogger(WangSuController.class);

    @Resource
    private JedisService jedisService;

    @Resource
    private AnchorService anchorService;

    @Resource
    private RoomRepository roomRepository;

    @Resource
    private VideoService videoService;

    @Resource
    private RoomService roomService;

    @Resource
    private RuserService ruserService;

    /**
     * 网宿回调(截图&回放)
     *
     * @return
     */
    @RequestMapping(value = "/receive")
    @ResponseBody
    public void receive(String message_type, HttpServletRequest request) {
        logger.info(message_type + "开始处理时间：" + System.currentTimeMillis());
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
            //kaishi ceshi
            //stream = "eyJwZXJzaXN0ZW50SWQiOiIxMDA1OGRlMTUxOTgxMjJlNDQ3ZDk4YmJkYWJkNTgxNWUxYjIiLCJzdHJlYW1uYW1lIjoiYmxpdmUtNDczMjdlNDJlZDlhMjdlMC5mbHYiLCJvcHMiOiJ3c3JlY29yZC9tM3U4L3NlZ3RpbWUvNS92Y29kZWMvY29weS9hY29kZWMvY29weXxzYXZlYXMvWW14cGRtVXRjbVZqYjNKa09pUW9jM1J5WldGdGJtRnRaU2t0SkNoMGFXMWxjM1JoYlhBcCIsImJ1Y2tldCI6ImJsaXZlLXJlY29yZCIsImJhdGNoX25vdGlmeV9pZCI6bnVsbH0=";
            //stream = "ewogICAgIml0ZW1zIjogWwogICAgICAgIHsKICAgICAgICAgICAgInBlcnNpc3RlbnRJZCI6ICIxMDA1YWVlYTlhZWNjZDI2NDQ2YTkwZjhjY2ExNTc0NTk5aXUiLAogICAgICAgICAgICAic3RyZWFtbmFtZSI6ICJibGl2ZS00NzMyN2U0MmVkOWEyN2UwLmZsdiIsCiAgICAgICAgICAgICJvcHMiOiAid3NyZWNvcmQvbTN1OC9zZWd0aW1lLzE1L3Zjb2RlYy9jb3B5L2Fjb2RlYy9jb3B5fHNhdmVhcy9ZbXhwZG1VdGNtVmpiM0prT2lRb2MzUnlaV0Z0Ym1GdFpTa3RKQ2gwYVcxbGMzUmhiWEFwIiwKICAgICAgICAgICAgImJ1Y2tldCI6ICJibGl2ZS1yZWNvcmQiLAogICAgICAgICAgICAiY29kZSI6IDEsCiAgICAgICAgICAgICJkZXNjIjogImZpbGVPcGVyYXRlQWN0aXZlIiwKICAgICAgICAgICAgImVycm9yIjogbnVsbCwKICAgICAgICAgICAgImtleXMiOiBbCiAgICAgICAgICAgICAgICAiYmxpdmUtcmVjb3JkOmJsaXZlLTQ3MzI3ZTQyZWQ5YTI3ZTAtLTIwMTYwNzE4MTQ0NDExLm0zdTgiCiAgICAgICAgICAgIF0sCiAgICAgICAgICAgICJ1cmxzIjogWwogICAgICAgICAgICAgICAgImh0dHA6Ly93c2x6LndvcGFpdHYuY29tL2JsaXZlLTQ3MzI3ZTQyZWQ5YTI3ZTAtLTIwMTYwNzE4MTQ0NDExLm0zdTgiCiAgICAgICAgICAgIF0sCiAgICAgICAgICAgICJkZXRhaWwiOiBbCiAgICAgICAgICAgICAgICB7CiAgICAgICAgICAgICAgICAgICAgImtleSI6ICJibGl2ZS1yZWNvcmQ6YmxpdmUtNDczMjdlNDJlZDlhMjdlMC0tMjAxNjA3MTgxNDQ0MTEubTN1OCIsCiAgICAgICAgICAgICAgICAgICAgInVybCI6ICJodHRwOi8vd3Nsei53b3BhaXR2LmNvbS9ibGl2ZS00NzMyN2U0MmVkOWEyN2UwLS0yMDE2MDcxODE0NDQxMS5tM3U4IiwKICAgICAgICAgICAgICAgICAgICAiZHVyYXRpb24iOiA0My42MywKICAgICAgICAgICAgICAgICAgICAiaGFzaCI6ICJGdG43VWRRWDZwNEw1X2ZNM2dlWWtwMTNKYVlwIiwKICAgICAgICAgICAgICAgICAgICAiZnNpemUiOiAyNzAsCiAgICAgICAgICAgICAgICAgICAgInN0YXJ0VGltZSI6ICIyMDE2MDcxODE0NDQxMSIsCiAgICAgICAgICAgICAgICAgICAgImVuZFRpbWUiOiAiMjAxNjA3MTgxNDQ2MTgiCiAgICAgICAgICAgICAgICB9CiAgICAgICAgICAgIF0KICAgICAgICB9CiAgICBdLAogICAgImJhdGNoX25vdGlmeV9pZCI6IG51bGwKfQ==";
            //stream = "eyJwZXJzaXN0ZW50SWQiOiIxMDA1OGRlMTUxOTgxMjJlNDQ3ZDk4YmJkYWJkNTgxNWUxYjEiLCJzdHJlYW1uYW1lIjoiYmxpdmUtNDczMjdlNDJlZDlhMjdlMC5mbHYiLCJvcHMiOiJ3c3JlY29yZC9tM3U4L3NlZ3RpbWUvNS92Y29kZWMvY29weS9hY29kZWMvY29weXxzYXZlYXMvWW14cGRtVXRjbVZqYjNKa09pUW9jM1J5WldGdGJtRnRaU2t0SkNoMGFXMWxjM1JoYlhBcCIsImJ1Y2tldCI6ImJsaXZlLXJlY29yZCIsImJhdGNoX25vdGlmeV9pZCI6bnVsbH0=";
            //stream = "ewogICAgIml0ZW1zIjogWwogICAgICAgIHsKICAgICAgICAgICAgInBlcnNpc3RlbnRJZCI6ICIxMDA1OGRlMTUxOTgxMjJlNDQ3ZDk4YmJkYWJkNTgxNWUxYjEiLAogICAgICAgICAgICAic3RyZWFtbmFtZSI6ICJibGl2ZS00NzMyN2U0MmVkOWEyN2UwLmZsdiIsCiAgICAgICAgICAgICJvcHMiOiAid3NyZWNvcmQvbTN1OC9zZWd0aW1lLzE1L3Zjb2RlYy9jb3B5L2Fjb2RlYy9jb3B5fHNhdmVhcy9ZbXhwZG1VdGNtVmpiM0prT2lRb2MzUnlaV0Z0Ym1GdFpTa3RKQ2gwYVcxbGMzUmhiWEFwIiwKICAgICAgICAgICAgImJ1Y2tldCI6ICJibGl2ZS1yZWNvcmQiLAogICAgICAgICAgICAiY29kZSI6IDEsCiAgICAgICAgICAgICJkZXNjIjogImZpbGVPcGVyYXRlQWN0aXZlIiwKICAgICAgICAgICAgImVycm9yIjogbnVsbCwKICAgICAgICAgICAgImtleXMiOiBbCiAgICAgICAgICAgICAgICAiYmxpdmUtcmVjb3JkOmJsaXZlLTQ3MzI3ZTQyZWQ5YTI3ZTAtLTIwMTYwNzE4MTQ0NDExLm0zdTgiCiAgICAgICAgICAgIF0sCiAgICAgICAgICAgICJ1cmxzIjogWwogICAgICAgICAgICAgICAgImh0dHA6Ly93c2x6LndvcGFpdHYuY29tL2JsaXZlLTQ3MzI3ZTQyZWQ5YTI3ZTAtLTIwMTYwNzE4MTQ0NDExLm0zdTgiCiAgICAgICAgICAgIF0sCiAgICAgICAgICAgICJkZXRhaWwiOiBbCiAgICAgICAgICAgICAgICB7CiAgICAgICAgICAgICAgICAgICAgImtleSI6ICJibGl2ZS1yZWNvcmQ6YmxpdmUtNDczMjdlNDJlZDlhMjdlMC0tMjAxNjA3MTgxNDQ0MTEubTN1OCIsCiAgICAgICAgICAgICAgICAgICAgInVybCI6ICJodHRwOi8vd3Nsei53b3BhaXR2LmNvbS9ibGl2ZS00NzMyN2U0MmVkOWEyN2UwLS0yMDE2MDcxODE0NDQxMS5tM3U4IiwKICAgICAgICAgICAgICAgICAgICAiZHVyYXRpb24iOiA0My42MywKICAgICAgICAgICAgICAgICAgICAiaGFzaCI6ICJGdG43VWRRWDZwNEw1X2ZNM2dlWWtwMTNKYVlwIiwKICAgICAgICAgICAgICAgICAgICAiZnNpemUiOiAyNzAsCiAgICAgICAgICAgICAgICAgICAgInN0YXJ0VGltZSI6ICIyMDE2MDcxODE0NDQxMSIsCiAgICAgICAgICAgICAgICAgICAgImVuZFRpbWUiOiAiMjAxNjA3MTgxNDQ2MTgiCiAgICAgICAgICAgICAgICB9CiAgICAgICAgICAgIF0KICAgICAgICB9CiAgICBdLAogICAgImJhdGNoX25vdGlmeV9pZCI6IG51bGwKfQ==";
            if (StringUtils.isNotBlank(stream)) {
                stream = new String(Base64.decode(stream));
                logger.info(message_type + "stream info :" + stream);
                JSONObject json = JSON.parseObject(stream);
                if ("ws_record_start".equals(message_type)) {
                    //开始通知
                    String persistentId = json.getString("persistentId");
                    String streamName = json.getString("streamname");
                    String bucket = json.getString("bucket");
                    if ("blive-img".equals(bucket)) {
                        //截图
                        logger.info("ws_record_start 通知开始截图接口回调 stream info:" + stream);
                    } else if ("blive-record".equals(bucket)) {
                        //点播
                        //获取streamId
                        String streamId = streamName.substring(streamName.indexOf("-") + 1, streamName.indexOf("."));
                        //根据streamId查询主播最新正在直播中的房间信息
                        Map<String, Object> params = new HashMap<String, Object>(1);
                        params.put("streamId", streamId);
                        Long maxRoomId = anchorService.queryMaxRoomIdByStreamId(params);
                        logger.info("maxRoomId==========" + maxRoomId);
                        if (maxRoomId != null) {
                            Room room = roomRepository.findOne(maxRoomId);
                            if (StringUtils.isNotBlank(room.getPersistentId())) {
                                MissingPlaybackInfo missingPlaybackInfo = videoService.selectMissPlaybackByPersistentId(persistentId);
                                if (missingPlaybackInfo == null) {
                                    MissingPlaybackInfo mpi = new MissingPlaybackInfo();
                                    mpi.setPersistentId(persistentId);
                                    mpi.setRoomId(room.getId());
                                    mpi.setStreamId(streamId);
                                    mpi.setCreateDate(new Date());
                                    videoService.insertMissPlayback(mpi);
                                    logger.info("streamId:" + streamId + "，此房间已通知过，重复通知，persistentId:" + persistentId + "，roomId:" + room.getId() + "===" + maxRoomId);
                                }
                            } else {
                                //将persistentId写入room表
                                room.setPersistentId(persistentId);
                                roomRepository.save(room);
                                logger.info("关联room房间信息，maxRoomId:" + maxRoomId + ",persistentId:" + persistentId);
                            }
                        } else {
                            logger.error("主播信息不存在，streamId:" + streamId);
                        }
                    }
                } else {
                    //结束通知
                    JSONArray jsonArray = json.getJSONArray("items");
                    if (!jsonArray.isEmpty()) {
                        JSONObject item = jsonArray.getJSONObject(0);
                        String persistentId = item.getString("persistentId");
                        String bucket = item.getString("bucket");
                        Integer code = item.getInteger("code");
                        String streamName = item.getString("streamname");
                        String streamId = streamName.substring(streamName.indexOf("-") + 1, streamName.indexOf("."));
                        if ("blive-img".equals(bucket)) {
                            //截图
                            JSONArray arrayDetail = item.getJSONArray("detail");
                            JSONObject detail = arrayDetail.getJSONObject(0);
                            String url = detail.getString("url");
                            //解析streamName得到streamId
                            if (StringUtils.isNotBlank(streamName)) {
                                //根据streamId查询主播最新房间信息
                                Map<String, Object> params = new HashMap<String, Object>(1);
                                params.put("streamId", streamId);
                                Long maxRoomId = anchorService.queryMaxRoomIdByStreamId(params);
                                if (maxRoomId != null) {
                                    //解析url得到img url相关信息
                                    String imageUrl = url;
                                    jedisService.setValueToSortedSetInShard(BicycleConstants.ROOM_SCREENSHOT_ID + maxRoomId, System.currentTimeMillis(), imageUrl);
                                } else {
                                    logger.info("主播信息不存在");
                                }
                            } else {
                                logger.error("streamName解析失败，获取streamId失败");
                            }
                        } else if ("blive-record".equals(bucket)) {
                            //点播
                            JSONArray urlsDetail = item.getJSONArray("urls");
                            String url = urlsDetail.getString(0);
                            logger.info("======================" + url);
                            //解析streamName得到streamId
                            if (StringUtils.isNotBlank(persistentId)) {
                                //根据streamId查询主播最新房间信息
                                Map<String, Object> params = new HashMap<String, Object>(1);
                                params.put("persistentId", persistentId);
                                Room room = roomService.queryRoomByPersistentId(params);
                                if (room != null) {
                                    //根据roomId查询video信息，如存在，保存视频步骤，记录相关日志
                                    Video videoInfo = videoService.getDeletePlaybackByRoomId(room.getId());
                                    if (videoInfo != null) {
                                        //保存回放
                                        logger.info("update video status. url:" + url + " vid:" + videoInfo.getId() + " roomId:" + room.getId());
                                        videoInfo.setPlayKey(url);
                                        videoInfo.setFlowStat(VideoStatus.审核通过.getName()); //将回放信息更新为通过状态
                                        videoService.save(videoInfo);
                                    } else {
                                        //missing_playback_info表查询persistentId，如果存在，更新playKey
                                        MissingPlaybackInfo mpi = videoService.selectMissPlaybackByPersistentId(persistentId);
                                        if (mpi != null) {
                                            mpi.setPlaykey(url);
                                            mpi.setModifyDate(new Date());
                                            videoService.updateMissPlaybackByPersistentId(mpi);
                                        } else {
                                            MissingPlaybackInfo missingPlaybackInfo = new MissingPlaybackInfo();
                                            missingPlaybackInfo.setPersistentId(persistentId);
                                            missingPlaybackInfo.setRoomId(room.getId());
                                            missingPlaybackInfo.setPlaykey(url);
                                            missingPlaybackInfo.setStreamId(streamId);
                                            missingPlaybackInfo.setCreateDate(new Date());
                                            videoService.insertMissPlayback(missingPlaybackInfo);
                                        }
                                        logger.info("视频信息不存在，roomId:" + room.getId() + ",url:" + url);
                                    }
                                } else {
                                    //missing_playback_info表查询persistentId，如果存在，更新playKey
                                    MissingPlaybackInfo mpi = videoService.selectMissPlaybackByPersistentId(persistentId);
                                    if (mpi != null) {
                                        mpi.setPlaykey(url);
                                        mpi.setModifyDate(new Date());
                                        videoService.updateMissPlaybackByPersistentId(mpi);
                                    } else {
                                        MissingPlaybackInfo missingPlaybackInfo = new MissingPlaybackInfo();
                                        missingPlaybackInfo.setPersistentId(persistentId);
                                        missingPlaybackInfo.setPlaykey(url);
                                        missingPlaybackInfo.setStreamId(streamId);
                                        missingPlaybackInfo.setCreateDate(new Date());
                                        videoService.insertMissPlayback(missingPlaybackInfo);
                                    }
                                    logger.info("开始通知未获取到相关persistentId，persistentId：" + persistentId);

                                }
                            } else {
                                logger.error("streamName解析失败，获取streamId失败");
                            }
                        }
                    } else {
                        logger.error("======stream信息错误，解析失败" + stream);
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
        logger.info(message_type + "结束处理时间：" + System.currentTimeMillis());
    }

    @RequestMapping(value = "/mosaicLiveVideo")
    @ResponseBody
    public void mosaicLiveVideo(HttpServletRequest request) {

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
            //stream = "eyJpZCI6IjEwMDU4ZjVlNmFmYThhYjE0Y2M1OTZlMjA4OTQ5NTg0ZmNkOCIsImNvZGUiOjMsImlucHV0a2V5IjoiYmxpdmUtYzczMWU2OTRkNmNjMzIyNC0tMjAxNjA4MTkxNDM0MTgubTN1OCIsImlucHV0YnVja2V0IjoiYmxpdmUtcmVjb3JkIiwiaW5wdXRmc2l6ZSI6ODgwOCwiZGVzYyI6Im9wZXJhdGUgW2F2Y29uY2F0L20zdTgvc2VndGltZS8xNS9tb2RlLzEvY29uY2F0b3JkZXIvMS0yLTMvWW14cGRtVXRZemN6TVdVMk9UUmtObU5qTXpJeU5DMHRNakF4TmpBNE1Ua3hOREk1TkRrdWJUTjFPQT09L1lteHBkbVV0WXpjek1XVTJPVFJrTm1Oak16SXlOQzB0TWpBeE5qQTRNVGt4TkRJM01ETXViVE4xT0E9PXxzYXZlYXMvWW14cGRtVXRjbVZqYjNKa09tSnNhWFpsTFRRM016STNaVFF5WldRNVlUSTNaVEF0TWpBeE5pMHdPQzB5TkM1dE0zVTRdIGlzIGZpbmlzaCBcbiIsInNlcGFyYXRlIjoxLCJpdGVtcyI6W3siY21kIjoiYXZjb25jYXQvbTN1OC9zZWd0aW1lLzE1L21vZGUvMS9jb25jYXRvcmRlci8xLTItMy9ZbXhwZG1VdFl6Y3pNV1UyT1RSa05tTmpNekl5TkMwdE1qQXhOakE0TVRreE5ESTVORGt1YlROMU9BPT0vWW14cGRtVXRZemN6TVdVMk9UUmtObU5qTXpJeU5DMHRNakF4TmpBNE1Ua3hOREkzTURNdWJUTjFPQT09fHNhdmVhcy9ZbXhwZG1VdGNtVmpiM0prT21Kc2FYWmxMVFEzTXpJM1pUUXlaV1E1WVRJM1pUQXRNakF4Tmkwd09DMHlOQzV0TTNVNCIsImNvZGUiOiIzIiwiY29zdFRpbWUiOjAsImRlc2MiOiJmaWxlT3BlcmF0ZVN1Y2NlZWQiLCJlcnJvciI6bnVsbCwiZnNpemUiOjc5MTcsImhhc2giOiJGcExxY3R0ZHdhZU1YSFk0eGhzYWt4ZGZfUTRQIiwia2V5IjoiYmxpdmUtcmVjb3JkOmJsaXZlLTQ3MzI3ZTQyZWQ5YTI3ZTAtMjAxNi0wOC0yNC5tM3U4IiwidXJsIjoiaHR0cDovL3dzbHoud29wYWl0di5jb20vYmxpdmUtNDczMjdlNDJlZDlhMjdlMC0yMDE2LTA4LTI0Lm0zdTgiLCJkdXJhdGlvbiI6MTg3OS42OTIsImRldGFpbCI6W3siZnNpemUiOjc5MTcsImhhc2giOiJGcExxY3R0ZHdhZU1YSFk0eGhzYWt4ZGZfUTRQIiwia2V5IjoiYmxpdmUtcmVjb3JkOmJsaXZlLTQ3MzI3ZTQyZWQ5YTI3ZTAtMjAxNi0wOC0yNC5tM3U4IiwidXJsIjoiaHR0cDovL3dzbHoud29wYWl0di5jb20vYmxpdmUtNDczMjdlNDJlZDlhMjdlMC0yMDE2LTA4LTI0Lm0zdTgiLCJkdXJhdGlvbiI6MTg3OS42OTJ9XX1dfQ==";
            if (StringUtils.isNotBlank(stream)) {
                stream = new String(Base64.decode(stream));

                System.out.println("stream:======"+stream);
                JSONObject json = JSON.parseObject(stream);

                //业务处理
                String code = json.getString("code");
                String persistentId = json.getString("id");
                if ("3".equals(code)) {
                    if (StringUtils.isNotBlank(persistentId)) {
                        JSONArray jsonArray = json.getJSONArray("items");
                        if (!jsonArray.isEmpty()) {
                            JSONObject item = jsonArray.getJSONObject(0);
                            String url = item.getString("url");
                            logger.info("url====="+url);
                            Double duration = item.getDouble("duration");
                            //根据streamId查询主播最新房间信息
                            Map<String, Object> params = new HashMap<String, Object>(1);
                            params.put("persistentId", persistentId);
                            Room room = roomService.queryRoomByPersistentId(params);
                            if (room != null) {
                                //根据roomId查询video信息，如存在，保存视频步骤，记录相关日志
                                Video videoInfo = videoService.getDeletePlaybackByRoomId(room.getId());
                                if (videoInfo != null) {
                                    //更新回放信息
                                    logger.info("update video status. url:" + url + " vid:" + videoInfo.getId() + " roomId:" + room.getId());
                                    videoInfo.setPlayKey(url);
                                    videoInfo.setFlowStat(VideoStatus.审核通过.getName()); //将回放信息更新为通过状态
                                    videoInfo.setDuration(String.valueOf(duration * 1000));//时长
                                    videoService.save(videoInfo);
                                } else {
                                    Room roomInfo = roomService.find(room.getId());
                                    //回放信息不存在，进行插入操作
                                    Video video = new Video();

                                    video.setCreatorId(roomInfo.getCreatorId());
                                    video.setCreateDate(roomInfo.getCreateDateStr());
                                    video.setPublishTime(roomInfo.getFinishDateStr());//发布时间改为直播结束时间（毕，小瓶盖）
                                    video.setDataFrom("back");

                                    video.setDuration(String.valueOf(duration * 1000));
                                    video.setVideoPic(roomInfo.getRoomPic());

                                    video.setPlayKey(url);
                                    video.setFlowStat(VideoStatus.审核通过.getName()); //将回放信息更新为通过状态

                                    video.setDescription(roomInfo.getTitle());
                                    video.setType(2);
                                    video.setPlayCountToday(Long.valueOf(roomInfo.getMaxAccessNumber()));//直播回放时,存的最大观看人数
                                    video.setLiveNoticeId(roomInfo.getId());//直播回放时,存的直播房间id
                                    Ruser ruser = ruserService.find(room.getCreatorId());
                                    int liveWeight = ruser.getLiveWeight();
                                    video.setPlaybackWeight(liveWeight + roomInfo.getOnlineNumber());
                                    videoService.save(video);
                                }
                            } else {
                                logger.error(persistentId+"persistentId对应房间信息不存在");
                            }
                        }
                    }else{
                        logger.error(persistentId+"persistentId不存在");
                    }

                }

            } else {
                //stream信息不存在
                logger.error("stream流信息不能为空");
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
