package com.busap.vcs.web;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.constants.SingConstants;
import com.busap.vcs.service.*;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 首页推荐
 * Created by huoshanwei on 2016/2/25.
 */
@Controller
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    protected HttpServletRequest request;

    @Resource(name = "respBodyBuilder")
    private RespBodyBuilder respBodyWriter = new RespBodyBuilder();

    @Resource
    private JedisService jedisService;

    @Resource
    private SingVoteService singVoteService;

    /**
     * 获得推荐用户列表
     *
     * @param position 位置  homePage:首页 searchPage:搜索页 voicePage:新歌声专区推荐用户
     * @param page     页码
     * @param rows     行数
     * @return
     */
    @RequestMapping("/getRecommendUser")
    @ResponseBody
    public RespBody getRecommendUser(String position, Integer page, Integer rows) {
        //判断position参数是否为空
        if (StringUtils.isBlank(position) || page == null || rows == null) {
            respBodyWriter.toError(ResponseCode.CODE_450.toString(), ResponseCode.CODE_450.toCode());
        }
        //获取当前用户UID
        String uid = this.request.getHeader("uid");
        List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
        Set<String> userIds = null;
        if (position.equals("homePage")) {
            userIds = getTopPointUserId(); //首页用户推荐，为xx小时内用户的金豆榜
        } else if("voicePage".equals(position)){
            userIds = jedisService.getSortedSetFromShardByDesc(BicycleConstants.VOICE_RECOMMEND_USER_ID);
        }else {
            userIds = jedisService.getSortedSetFromShardByDesc(BicycleConstants.SEARCH_RECOMMEND_USER_ID);
        }

        //首页推荐用户
        if ("homePage".equals(position)) {
            getUserDetailInfo(userList,userIds);
        } else if("voicePage".equals(position)){
            //getUserDetailInfo(userList,userIds);
            Set<String> rooms = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_ORDER);
            Set<String> creatorSet=new HashSet<String>();
            for (String roomId : rooms) {
                Map<String, String> roomInfo = jedisService.getMapByKey(BicycleConstants.ROOM_ + roomId);
                if (StringUtils.isNotBlank(roomInfo.get("creatorId"))) {
                    creatorSet.add(roomInfo.get("creatorId"));
                }
            }
            for (String userId : userIds) {
                Map<String, String> user = jedisService.getMapByKey(BicycleConstants.USER_INFO + userId);
                Map<String, String> u = new HashMap<String, String>();
                u.put("id", user.get("id"));
                u.put("pic", user.get("pic"));
                u.put("name", user.get("name"));
                u.put("isAnchor", user.get("isAnchor"));
                u.put("vipStat", user.get("vipStat"));
                Integer type = jedisService.isSetMemberInShard(SingConstants.SING_MEMBER, userId)?1:3;
                Double popularity = singVoteService.getUserPopularity(Long.valueOf(userId),type);
                u.put("popularity", String.valueOf(popularity == null?0:popularity));
                if(creatorSet.contains(userId)) {
                    u.put("isLive", "1");
                }else {
                    u.put("isLive", "0");
                }
                if(user.get("stat")!=null&&(!user.get("stat").equals("0")||!user.get("stat").equals("1"))) {
                    userList.add(u);
                }
            }
        } else {
            //搜索页推荐用户
            //当前直播中的房间信息
            Set<String> rooms = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_ORDER);
            for (String userId : userIds) {
                Map<String, String> user = jedisService.getMapByKey(BicycleConstants.USER_INFO + userId);
                Map<String, String> u = new HashMap<String, String>();
                u.put("id", user.get("id"));
                u.put("pic", user.get("pic"));
                u.put("name", user.get("name"));
                u.put("isAnchor", user.get("isAnchor"));
                u.put("vipStat", user.get("vipStat"));
                //遍历set 根据房间中的uid和当前推荐用户uid对比 确定用户是否正在直播中 isLive=1 直播中 else 未直播
                for (String roomId : rooms) {
                    Map<String, String> roomInfo = jedisService.getMapByKey(BicycleConstants.ROOM_ + roomId);
                    if (StringUtils.isNotBlank(roomInfo.get("creatorId"))) {
                        if (userId.equals(roomInfo.get("creatorId"))) {
                            u.put("isLive", "1");
                        } else {
                            u.put("isLive", "0");
                        }
                    }
                }
                if(user.get("stat")!=null&&(!user.get("stat").equals("0")||!user.get("stat").equals("1"))) {
                	userList.add(u);
                }
            }
        }
        if (page <= 0) {
            page = 1;
        }
        if (rows <= 0) {
            rows = 20;
        }
        int totalCount = userIds.size();
        int m = totalCount % rows;
        int pageCount = 0;
        if (m > 0) {
            pageCount = totalCount / rows + 1;
        } else {
            pageCount = totalCount / rows;
        }

        if (m == 0) {
            if (totalCount >= rows * (page)) {
                userList = userList.subList((page - 1) * rows, rows * (page));
            } else {
                userList = null;
            }
        } else {
            if (page == pageCount) {
                userList = userList.subList((page - 1) * rows, totalCount);
            } else {
                if (totalCount >= rows * (page)) {
                    userList = userList.subList((page - 1) * rows, rows * (page));
                } else {
                    userList = null;
                }
            }
        }

        return respBodyWriter.toSuccess(userList);
    }

    /**
     * 获取用户详情信息
     * @return
     */
    private void getUserDetailInfo(List<Map<String, String>> userList,Set<String> userIds){
    	Set<String> rooms = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_ORDER);
        Set<String> creatorSet=new HashSet<String>();
        for (String roomId : rooms) {
            Map<String, String> roomInfo = jedisService.getMapByKey(BicycleConstants.ROOM_ + roomId);
            if (StringUtils.isNotBlank(roomInfo.get("creatorId"))) {
            	creatorSet.add(roomInfo.get("creatorId"));
            }
        }
        for (String userId : userIds) {
            Map<String, String> user = jedisService.getMapByKey(BicycleConstants.USER_INFO + userId);
            Map<String, String> u = new HashMap<String, String>();
            u.put("id", user.get("id"));
            u.put("pic", user.get("pic"));
            u.put("name", user.get("name"));
            u.put("isAnchor", user.get("isAnchor"));
            u.put("vipStat", user.get("vipStat"));
            Double points = jedisService.zscore(BicycleConstants.TOP_POINT_USER_ID,userId);
            u.put("points", String.valueOf(points==null?0:points));
            if(creatorSet.contains(userId)) {
            	u.put("isLive", "1");
            }else {
            	u.put("isLive", "0");
            }
            if(user.get("stat")!=null&&(!user.get("stat").equals("0")||!user.get("stat").equals("1"))) {
                userList.add(u);
            }
        }
    }

    /**
     * 获得xx小时内金豆榜用户id
     * @return
     */
    private Set<String> getTopPointUserId() {
    	Set<String> set = jedisService.getSortedSetFromShardByDesc(BicycleConstants.TOP_POINT_USER_ID); //从缓存中取金豆榜的用户id(admin定时任务刷缓存)
    	if (set == null) {
    		set = new HashSet<String>();
    	}
    	return set;
    }

    /**
     * 获取一周内首播新人
     * @param page 页码
     * @param rows 行数
     * @return RespBody 直播列表信息
     */
    @RequestMapping("/getPeriodFirstTimeLiveUser")
    @ResponseBody
    public RespBody getPeriodFirstTimeLiveUser(Integer page, Integer rows){
        if(page == null || page <= 0 || rows == null || rows <= 0){
            return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_312.toCode());
        }
        /*Map<String,Object> params = new HashMap<String, Object>();
        params.put("hours",StringUtils.isBlank(jedisService.get(BicycleConstants.FIRST_LIVE_TIMES))?24:jedisService.get(BicycleConstants.FIRST_LIVE_TIMES));
        params.put("pageStart",(page-1)*rows);
        params.put("pageSize",rows);*/
        List<Map<String, String>> uList = new ArrayList<Map<String, String>>();
        //获取一周内首播的用户ID信息
        //List<String> userIdList = roomService.queryPeriodFirstTimeLiveUser(params);
        List<String> userIdList = (List<String>) jedisService.getObject(BicycleConstants.FIRST_LIVE_USER_INFO);
        int totalCount = userIdList.size();
        int m = totalCount % rows;
        int pageCount = 0;
        if (m > 0) {
            pageCount = totalCount / rows + 1;
        } else {
            pageCount = totalCount / rows;
        }
        if (m == 0) {
            if (totalCount >= rows * (page)) {
                userIdList = userIdList.subList((page - 1) * rows, rows * (page));
            } else {
                userIdList = null;
            }
        } else {
            if (page == pageCount) {
                userIdList = userIdList.subList((page - 1) * rows, totalCount);
            } else {
                if (totalCount >= rows * (page)) {
                    userIdList = userIdList.subList((page - 1) * rows, rows * (page));
                } else {
                    userIdList = null;
                }
            }
        }
        //当前直播中的房间信息
        //Set<String> rooms = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_ORDER);
        Set<String> rooms = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_ORDER);
        Set<String> creatorSet=new HashSet<String>();
        for (String roomId : rooms) {
            Map<String, String> roomInfo = jedisService.getMapByKey(BicycleConstants.ROOM_ + roomId);
            if (StringUtils.isNotBlank(roomInfo.get("creatorId"))) {
            	creatorSet.add(roomInfo.get("creatorId"));
            }
        }
        if (userIdList != null && userIdList.size() > 0) {
            for (String userId : userIdList) {
                Map<String, String> u = jedisService.getMapByKey(BicycleConstants.USER_INFO + userId);
                Map<String, String> user = new HashMap<String, String>();
                user.put("id", u.get("id"));
                user.put("pic", u.get("pic"));
                user.put("name", u.get("name"));
                user.put("isAnchor", u.get("isAnchor"));
                user.put("vipStat", u.get("vipStat"));
                if(creatorSet.contains(userId)) {
                	user.put("isLive", "1");
                }else {
                	user.put("isLive", "0");
                }
                
                uList.add(user);
            }
        }

        return respBodyWriter.toSuccess(uList);

    }

}
