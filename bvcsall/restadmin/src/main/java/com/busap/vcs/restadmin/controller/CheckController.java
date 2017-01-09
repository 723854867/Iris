package com.busap.vcs.restadmin.controller;

import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Anchor;
import com.busap.vcs.data.entity.Room;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.UserRole;
import com.busap.vcs.data.model.UserDisplay;
import com.busap.vcs.restadmin.utils.ResultData;
import com.busap.vcs.service.*;
import com.busap.vcs.util.DateUtils;
import com.busap.vcs.webcomn.RespBodyBuilder;
import com.busap.vcs.webcomn.U;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 直播审核
 * Created by huoshanwei on 2016/5/3.
 */
@Controller
@RequestMapping("check")
public class CheckController {

    @Resource
    private JedisService jedisService;

    @Resource
    private RuserService ruserService;

    @Resource
    private AnchorService anchorService;

    private static final Logger logger = LoggerFactory.getLogger(CheckController.class);

    @Resource
    private LiveCheckLogService liveCheckLogService;

    private static final String baseUcloudUrl = "http://bliveimg.ufile.ucloud.cn/";

    private static final String basePlxyUrl = "http://xyjt.wopaitv.com/blive/";

    private static final String baseUnknownUrl = "";

    @Resource
    private RoomService roomService;

    @Resource
    private UserRoleService userRoleService;
    
    @Resource(name = "respBodyBuilder")
    private RespBodyBuilder respBodyWriter = new RespBodyBuilder();
    
    @Resource
    private UserService userService;


    @RequestMapping("forwardCheckPage")
    public ModelAndView forwardCheckPage(String type) {
        String username = U.getUname();
        ModelAndView mav = new ModelAndView();
        String checkGroupAdminId = jedisService.get(BicycleConstants.CHECK_ADMIN_GROUP_ID);
        Set<String> checkGroup = jedisService.getSortedSetFromShardByDesc(BicycleConstants.CHECK_GROUP);
        if (StringUtils.isNotBlank(checkGroupAdminId)) {
            //如果用户权限为超级审核权限，查看所有直播信息
            if (isCheckAdminGroup(Long.valueOf(checkGroupAdminId))) {
                Set<String> roomOrder = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_ORDER);
                //删除set中无效的数据
                roomOrder = removeInvalidInfoFromRoomOrder(roomOrder);
                String roomData = "";
                for(String ro : roomOrder){
                    roomData +=ro+",";
                }
                List<Map<String, String>> result = addRoomInfoToList(roomOrder);
                //按类型排序
                sort(result,type);

                mav.addObject("type",type);
                if (!result.isEmpty()) {
                    mav.addObject("roomOrder",roomData);
                    mav.addObject("roomCount",result.size());
                    mav.addObject("list", result);
                }
                mav.addObject("checkGroup",StringUtils.join(checkGroup.toArray(), ","));
                mav.setViewName("check/query_check_list");
                return mav;
            }
        }
        if (jedisService.isSortedSetMemberInShard(BicycleConstants.CHECK_GROUP, username) != null) {
            //如果用户权限为普通审核员权限，进行动态分配
            Set<String> roomOrder = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_ORDER);
            //删除set中无效的数据
            roomOrder = removeInvalidInfoFromRoomOrder(roomOrder);
            //将当前在线用户数量写入缓存
            jedisService.set(BicycleConstants.CHECK_GROUP_COUNT, String.valueOf(checkGroup.size()));
            //将当前最新直播房间ID写入缓存
            Long maxRoomId = roomService.queryMaxRoomId();
            jedisService.set(BicycleConstants.NEW_LIVE_ROOM, String.valueOf(maxRoomId));
            //取余数
            int remainder = roomOrder.size() % checkGroup.size();
            //取平均数
            int average = roomOrder.size() / checkGroup.size();
            List roomList = new ArrayList(roomOrder);
            List<List<String>> list = new ArrayList<List<String>>();
            List<String> subList = new ArrayList<String>();
            if (remainder == 0) {
                if (checkGroup.size() <= roomOrder.size()) {
                    for (int i = 0; i < checkGroup.size(); i++) {
                        subList = roomList.subList(i * average, average * (i + 1));
                        list.add(i, subList);
                    }
                }
            } else {
                for (int i = 0; i < checkGroup.size(); i++) {
                    if (i <= remainder - 1) {
                        subList = roomList.subList(i * average + i, average * (i + 1) + i + 1);
                    } else if (i > remainder - 1 && i < checkGroup.size() - 1) {
                        subList = roomList.subList(i * average + remainder, i * average + remainder + average);
                    } else if (i == checkGroup.size() - 1) {
                        subList = roomList.subList(i * average + remainder, roomOrder.size());
                    }
                    list.add(i, subList);
                    logger.info(i + "=====================" + subList);
                }
            }
            //取username值在set中的位置
            Long pos = jedisService.zrank(BicycleConstants.CHECK_GROUP, username);
            if (pos != null && !list.isEmpty()) {
                String position = String.valueOf(pos);
                List<String> resultList = list.get(Integer.parseInt(position));
                String roomData = "";
                for(String rl : resultList){
                    roomData += rl+",";
                }
                List<Map<String, String>> result = addRoomInfoToList(resultList);
                //按类型排序
                sort(result,type);
                if (!result.isEmpty()) {
                    mav.addObject("roomOrder",roomData);
                    mav.addObject("roomCount",result.size());
                    mav.addObject("list", result);
                    logger.info("result=========" + result);
                }
            }
            mav.addObject("checkGroup",StringUtils.join(checkGroup.toArray(), ","));
            mav.addObject("type",type);
            mav.setViewName("check/query_check_list");
            return mav;
        }
        mav.addObject("roomCount",0);
        mav.addObject("type",type);
        mav.setViewName("index/welcome");
        return mav;
    }

    private void sort(List<Map<String,String>> result,String type){
        if("new".equals(type)) {
            //按最新排序
            Collections.sort(result, new Comparator<Map<String, String>>() {
                @Override
                public int compare(Map<String, String> o1, Map<String, String> o2) {
                    String createTime1 = o1.get("createDate");
                    if(!StringUtils.isNumeric(createTime1)){
                        return -1;
                    }
                    String createTime2 = o2.get("createDate");
                    if(!StringUtils.isNumeric(createTime2)){
                        return 0;
                    }
                    Long create1 = Long.parseLong(createTime1);
                    Long create2 = Long.parseLong(createTime2);

                    return create2.compareTo(create1);
                }

            });
        }else if("hot".equals(type)){
            Collections.sort(result, new Comparator<Map<String,String>>(){
                @Override
                public int compare(Map<String, String> o1, Map<String, String> o2) {
                    Long id1 = jedisService.zrank(BicycleConstants.ROOM_ORDER, o1.get("id"));
                    if(id1 == null || id1 <= 0){
                        return -1;
                    }
                    Long id2 = jedisService.zrank(BicycleConstants.ROOM_ORDER, o2.get("id"));
                    if(id2 == null || id2 <= 0){
                        return 0;
                    }
                    return id2.compareTo(id1);
                }

            });
        }
    }

    private Set<String> removeInvalidInfoFromRoomOrder(Set<String> roomOrder) {
        List<String> removeList = new ArrayList<String>();
        for (String room : roomOrder) {
            Map<String, String> roomMap = jedisService.getMapByKey(BicycleConstants.ROOM_ + room);
            if (roomMap == null || roomMap.size() == 0 || roomMap.get("id") == null) {
                removeList.add(room);
            }
        }
        roomOrder.removeAll(removeList);
        return roomOrder;
    }

    private List<Map<String, String>> addRoomInfoToList(Set<String> roomOrder) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        for (String roomId : roomOrder) {
            Map<String, String> roomInfo = jedisService.getMapByKey(BicycleConstants.ROOM_ + roomId);

            if (roomInfo != null && roomInfo.size() > 0 && roomInfo.get("id") != null) {
                Boolean shieldStatus = jedisService.ifKeyExists(BicycleConstants.SHIELD_LIVE_USER+roomInfo.get("creatorId"));
                if(shieldStatus){
                    roomInfo.put("shieldStatus", "1");
                }else{
                    roomInfo.put("shieldStatus", "0");
                }
                if (StringUtils.isBlank(roomInfo.get("anchorName"))) {
                    if (StringUtils.isNotBlank(roomInfo.get("creatorId")) && StringUtils.isNotBlank(roomInfo.get("id"))) {
                        Ruser ruser = ruserService.find(Long.parseLong(roomInfo.get("creatorId")));
                        roomInfo.put("anchorName", ruser.getName());
                    }
                }
                Set<String> screenshotInfo = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_SCREENSHOT_ID + roomId, 0, 0);
                if (!screenshotInfo.isEmpty()) {
                    for (String screenshotUrl : screenshotInfo) {
                        roomInfo.put("screenshotUrl", screenshotUrl);
                    }
                    //roomInfo.put("streamSource", "wangsu");
                } else {
                    String rtmpLiveUrl = roomInfo.get("rtmpLiveUrl");
                    Anchor anchor = anchorService.getAnchorByUserid(Long.parseLong(roomInfo.get("creatorId")));
                    if (anchor != null) {
                        if(rtmpLiveUrl.indexOf("ucloud")>=0){
                            //ucloud截图固定地址
                            roomInfo.put("screenshotUrl", baseUcloudUrl + anchor.getStreamId() + ".jpg");
                            roomInfo.put("streamSource", "ucloud");
                        }else if(rtmpLiveUrl.indexOf("plls") >=0){
                            //乐视固定地址
                            roomInfo.put("streamSource", "plls");
                        }else if(rtmpLiveUrl.indexOf("plxy") >=0){
                            //星宇固定地址
                            roomInfo.put("screenshotUrl", basePlxyUrl + anchor.getStreamId() + ".jpg");
                            roomInfo.put("streamSource", "plxy");
                        }else{
                            //其它固定地址
                            roomInfo.put("screenshotUrl", baseUnknownUrl + anchor.getStreamId() + ".jpg");
                            roomInfo.put("streamSource", "unknown");
                        }
                    }
                }
                result.add(roomInfo);
            }
        }
        return result;
    }

    private List<Map<String, String>> addRoomInfoToList(List<String> roomOrder) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        for (String roomId : roomOrder) {
            Map<String, String> roomInfo = jedisService.getMapByKey(BicycleConstants.ROOM_ + roomId);
            if (roomInfo != null && roomInfo.size() > 0 && roomInfo.get("id") != null) {
                Boolean shieldStatus = jedisService.ifKeyExists(BicycleConstants.SHIELD_LIVE_USER+roomInfo.get("creatorId"));
                if(shieldStatus){
                    roomInfo.put("shieldStatus", "1");
                }else{
                    roomInfo.put("shieldStatus", "0");
                }
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
                Set<String> screenshotInfo = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_SCREENSHOT_ID + roomId, 0, 0);
                if (!screenshotInfo.isEmpty()) {
                    for (String screenshotUrl : screenshotInfo) {
                        roomInfo.put("screenshotUrl", screenshotUrl);
                    }
                } else {
                    String rtmpLiveUrl = roomInfo.get("rtmpLiveUrl");
                    Anchor anchor = anchorService.getAnchorByUserid(Long.parseLong(roomInfo.get("creatorId")));
                    if (anchor != null) {
                        if(rtmpLiveUrl.indexOf("ucloud")>=0){
                            //ucloud截图固定地址
                            roomInfo.put("screenshotUrl", baseUcloudUrl + anchor.getStreamId() + ".jpg");
                            roomInfo.put("streamSource", "ucloud");
                        }else if(rtmpLiveUrl.indexOf("plls") >=0){
                            //乐视固定地址
                            roomInfo.put("streamSource", "plls");
                        }else if(rtmpLiveUrl.indexOf("plxy") >=0){
                            //星宇固定地址
                            roomInfo.put("screenshotUrl", basePlxyUrl + anchor.getStreamId() + ".jpg");
                            roomInfo.put("streamSource", "plxy");
                        }else{
                            //其它固定地址
                            roomInfo.put("screenshotUrl", baseUnknownUrl + anchor.getStreamId() + ".jpg");
                            roomInfo.put("streamSource", "unknown");
                        }
                    }
                }
                result.add(roomInfo);
            }
        }
        return result;
    }

    @RequestMapping("getNewScreenshot")
    @ResponseBody
    public ResultData getNewScreenshot(String roomId) {
        ResultData resultData = new ResultData();
        Double score = jedisService.isSortedSetMemberInShard(BicycleConstants.ROOM_ORDER, roomId);
        if (score == null) {
            resultData.setResultCode("601");
            resultData.setResultMessage("房间信息不存在");
            return resultData;
        }
        Map<String, String> resultMap = new HashMap<String, String>();
        if (StringUtils.isNotBlank(roomId)) {
            Set<String> screenshotInfo = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_SCREENSHOT_ID + roomId, 0, 0);
            if (!screenshotInfo.isEmpty()) {
                for (String screenshotUrl : screenshotInfo) {
                    resultMap.put("screenshotUrl", screenshotUrl);
                }
            } else {
                String creatorId = jedisService.getValueFromMap(BicycleConstants.ROOM_ + roomId, "creatorId");
                if (StringUtils.isNotBlank(creatorId)) {
                    Anchor anchor = anchorService.getAnchorByUserid(Long.parseLong(creatorId));
                    String rtmpLiveUrl = jedisService.getValueFromMap(BicycleConstants.ROOM_ + roomId, "rtmpLiveUrl");
                    if (anchor != null) {
                        if(rtmpLiveUrl.indexOf("ucloud")>=0){
                            resultMap.put("screenshotUrl", baseUcloudUrl + anchor.getStreamId() + ".jpg?"+System.currentTimeMillis());
                        }else if(rtmpLiveUrl.indexOf("plxy") >=0){
                            resultMap.put("screenshotUrl", basePlxyUrl + anchor.getStreamId() + ".jpg?"+System.currentTimeMillis());
                        }else{
                            resultMap.put("screenshotUrl", baseUcloudUrl + anchor.getStreamId() + ".jpg?"+System.currentTimeMillis());
                        }
                    }
                }
            }

        }
        String onlineNumber = jedisService.getValueFromMap(BicycleConstants.ROOM_ + roomId, "onlineNumber");
        //long roomCount = jedisService.getSortedSetSizeFromShard(BicycleConstants.ROOM_ORDER);
        //resultMap.put("roomCount", String.valueOf(roomCount));
        resultMap.put("onlineNumber",onlineNumber);
        resultData.setData(resultMap);
        return resultData;
    }



    @RequestMapping("closeAccount")
    @ResponseBody
    public String closeAccount(Long roomId) {
        Room room = roomService.find(roomId);
        if(room != null && room.getCreatorId() != null){
            String message = "直播内容不合法，封号.";
            //进行下线操作
            roomService.offlineRoom(roomId,U.getUid(),message);
            //更新用户信息等
            ruserService.closeAccount(room.getCreatorId());
            //加入日志
            liveCheckLogService.check(U.getUid(), "封号", message, roomId, room.getCreatorId(),"live");
            return "success";
        }
        return "fail";
    }

    @RequestMapping("historyTemplate")
    public ModelAndView historyTemplate(Long roomId) {
        ModelAndView mav = new ModelAndView();
        Set<String> screenshot = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_SCREENSHOT_ID + roomId, 0, -1);
        if (!screenshot.isEmpty()) {
            mav.addObject("screenshot", screenshot);
            mav.setViewName("check/screenshot_history");
        } else {
            mav.addObject("screenshot", "ucloud");
        }
        return mav;
    }

/*    @RequestMapping("historyTemplate2")
    @ResponseBody
    public Set<String> historyTemplate2(Long roomId) {
        ModelAndView mav = new ModelAndView();
        Set<String> screenshot = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_SCREENSHOT_ID + roomId, 0, -1);
        return screenshot;
    }*/

    @RequestMapping("checkPicList")
    public ModelAndView checkPicList(
            HttpServletRequest req,
            @RequestParam(value = "page", required = false) Long page,
            @RequestParam(value = "rows", required = false) Long rows
    ) {

        ModelAndView mav = new ModelAndView();

        if (rows == null) {
            rows = 50l;
        } else {

        }
        if (page == null) {
            page = 1l;
        } else {
//			page=(page-1)*rows;
        }

        List picList = new ArrayList();
        Set<String> picSet = jedisService.getSortedSetFromShardByDesc(BicycleConstants.TO_BE_VERIFIED_PIC, (page - 1) * rows, page * rows-1);
        for (String pic : picSet) {
            String arr[] = pic.split("\\|");
            String id = arr[0];
            String type = arr[1];
            if(id!=null&&!id.equals("")) {
            	Map map = jedisService.getMapByKey(BicycleConstants.USER_INFO + id);
            	
            	if(map==null||map.get("id")==null||map.get("id").equals("")) {
            		Ruser user = ruserService.find(Long.valueOf(id));
            		if (user != null&&user.getId()!=null&&(user.getUsername()!=null&&!user.getUsername().equals(""))) {
            			String key = BicycleConstants.USER_INFO + user.getId();
            			jedisService.saveAsMap(key, user);
            			map = jedisService.getMapByKey(BicycleConstants.USER_INFO + id);
            		}else {
            			jedisService.deleteSortedSetItemFromShard(BicycleConstants.TO_BE_VERIFIED_PIC, id.toString());
            		}
            		
            	}
                map.put("picType", type);

//        		String bName = jedisService.getValueFromMap(BicycleConstants.USER_INFO + userId);
//        		Ruser ruser = ruserService.find(Long.valueOf(id));
                picList.add(map);
            }
            
        }
        
        Long total=jedisService.getSortedSetSizeFromShard(BicycleConstants.TO_BE_VERIFIED_PIC);
        
        mav.addObject("total", total);

        mav.addObject("picList", picList);

        mav.addObject("page", page);
        mav.addObject("rows", rows);


        mav.setViewName("check/check_pic_list");
        return mav;
    }
    
    @RequestMapping("checkPic")
    public ModelAndView checkPic(
            HttpServletRequest req,
            @RequestParam(value = "page", required = false) Long page,
            @RequestParam(value = "rows", required = false) Long rows,
            @RequestParam(value = "userKey", required = false) String userKey,
            @RequestParam(value = "userKeyword", required = false) String userKeyword,
            @RequestParam(value = "userId", required = false) Long userId
    ) throws Exception {

        ModelAndView mav = new ModelAndView();
        
        if(userKey!=null&&!userKey.equals("")) {
        	if(userKey.equals("1")) {
        		if(userKeyword!=null&&!userKeyword.equals("")) {
        			Map map = jedisService.getMapByKey(BicycleConstants.USER_INFO + userKeyword);
                	mav.addObject("vo", map);
        		}
        	}else if(userKey.equals("2")){
        		if(userKeyword!=null&&!userKeyword.equals("")) {
        			Ruser ruser=ruserService.findByUsername(userKeyword);
            		mav.addObject("vo", ruser);
        		}
        	}else if(userKey.equals("3")){
        		if(userKeyword!=null&&!userKeyword.equals("")) {
        			StringBuffer jpql = new StringBuffer();
        	        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        	        
        	        jpql.append(" FROM Ruser r2   WHERE   1=1  ");
        	        
        	        jpql.append(" AND r2.name like :uValue ");
        			
        			ParameterBean paramBean=new ParameterBean("uValue","%"+userKeyword+"%",null);
        			paramList.add(paramBean);
        	        
        	        
        	        
        	        
        	        
        			
        			List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        	        OrderByBean orderByObject=new OrderByBean("createDate",1,"r2");
        	        orderByList.add(orderByObject);
        	        
        	        
        	        List userPicList=liveCheckLogService.getObjectByJpql(jpql, 1, Long.valueOf(100).intValue(), "r2", paramList, null, orderByList);
        	        
        	        mav.addObject("userPicList", userPicList);
        		}
        	}
        	
        }


        mav.addObject("userKey", userKey);
        mav.addObject("userKeyword", userKeyword);

        mav.setViewName("check/checkPic");
        return mav;
    }

    @RequestMapping("checkPics")
    @ResponseBody
    public ResultData checkPics(HttpServletRequest req,@RequestParam(value = "idType", required = false) String[] idType,
            @RequestParam(value = "checkType", required = false) String checkType) {
        ResultData resultData = new ResultData();
        if (idType != null && idType.length > 0) {
            if (checkType != null && checkType.equals("yes")) {
                for (int i = 0; i < idType.length; i++) {
                    String[] temp = idType[i].split("\\_");
                    String id = temp[0];
                    String tp = temp[1];
                    ruserService.verifyPic(Long.valueOf(id), tp);
                    if(tp!=null&&tp.equals("head")) {
                    	liveCheckLogService.check(U.getUid(), "通过", "个人头像审核通过", null, Long.valueOf(id),"head");
                    }else if(tp!=null&&tp.equals("home")) {
                    	liveCheckLogService.check(U.getUid(), "通过", "个人背景图片审核通过", null, Long.valueOf(id),"home");
                    }
                    

                }
            } else if (checkType != null && checkType.equals("no")) {
                for (int i = 0; i < idType.length; i++) {
                    String[] temp = idType[i].split("\\_");
                    String id = temp[0];
                    String tp = temp[1];
                    ruserService.changeInvalidatePic(Long.valueOf(id), tp);
                    if(tp!=null&&tp.equals("head")) {
                    	liveCheckLogService.check(U.getUid(), "不通过", "个人头像审核不通过", null, Long.valueOf(id),"head");
                    }else if(tp!=null&&tp.equals("home")) {
                    	liveCheckLogService.check(U.getUid(), "不通过", "个人背景图片审核不通过", null, Long.valueOf(id),"home");
                    }
                    

                }
            }

        }

        resultData.setResultCode("success");
        return resultData;
    }


/*    public static void main(String[] args) {
        List<Integer> list = new ArrayList<Integer>();
        int sizes = 11; //sizes是一个动态变量 测试的时候先写死
        for (int i = 1; i <= sizes; i++)
            list.add(i);

        int count = list.size() / 9;
        int yu = list.size() % 9;
        if(yu == 0) {
            for (int i = 0; i < 9; i++) {
                List<Integer> subList = new ArrayList<Integer>();
                subList = list.subList(i*count, count * (i + 1));
                System.out.println(subList);

            }
        }else{
            for (int i = 0; i < 9; i++) {
                List<Integer> subList = new ArrayList<Integer>();
                List<Integer> subList2 = new ArrayList<Integer>();
                List<Integer> subList3 = new ArrayList<Integer>();

                if(i<=yu-1){
                    subList = list.subList(i * count + i, count * (i + 1) + i + 1);
                    System.out.println(i+"i===subList"+subList);
                }else if(i>yu-1 && i<8){
                    subList = list.subList(i * count + yu, i * count + yu+count);
                    System.out.println(i+"i===subList2"+subList);
                }else if(i == 8){
                    System.out.println(i+"i===subList3"+list.subList(i * count+yu, sizes ));
                }


            }
        }
    }*/

    private Boolean isCheckAdminGroup(Long roleId) {
        UserRole ur = new UserRole();
        ur.setUser(U.getUid());
        ur.setRoles(roleId);
        List<UserRole> userRoleList = userRoleService.queryUserRoles(ur);
        if (!userRoleList.isEmpty()) {
            return true;
        }
        return false;
    }
    
	@RequestMapping("checkLogList")
	public String checkLogList(HttpServletRequest request){
		Map<String,Object> params = new HashMap<String, Object>();
		//params.put("group",5);
        //params.put("isLocked",0);
        //params.put("isEnabled",1);
        List<UserDisplay> list = userService.queryUsers(params);
        request.setAttribute("list", list);
		return "check/checkLogList";
	}
    
  //获取充值信息
    @RequestMapping("findCheckLogList")
    @ResponseBody
    public Map<String,Object> findCheckLogList(
    		@RequestParam(value = "page", required = false)  Long page,
    		@RequestParam(value = "rows", required = false)  Long rows,
    		@RequestParam(value = "type", required = false)  String type,
    		@RequestParam(value = "uType", required = false)  String uType,
    		@RequestParam(value = "uId", required = false)  String uId,
    		@RequestParam(value = "uValue", required = false)  String uValue,
    		@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "operate", required = false) String operate,
    		HttpServletRequest request
    		
    		) throws Exception{ 
        
	    
	    String userId =  request.getHeader("uid");
	    
//	    userId="118790";
	    
     
	    if(page==null) {
			page=1l;
        }
        if(rows==null) {
        	rows=10l;
        }
        
    	StringBuffer jpql = new StringBuffer();
        StringBuffer countJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
        jpql.append("select new Map(lcl.userId as userId ,r2.name as name ,r2.phone as phone ,r1.username as uName,lcl.operate as operate,lcl.type as type,lcl.reason as reason,lcl.createDate as createDate,lcl.roomId as roomId ) FROM LiveCheckLog lcl,User r1,Ruser r2   WHERE   lcl.creatorId=r1.id and  lcl.userId=r2.id  ");
        countJpql.append("SELECT COUNT(*)  FROM LiveCheckLog lcl,User r1,Ruser r2   WHERE   lcl.creatorId=r1.id and  lcl.userId=r2.id    ");
        
        
        if(type!=null&&!type.equals("")) {
			jpql.append(" AND lcl.type = :type ");
			countJpql.append(" AND lcl.type = :type ");
			
			ParameterBean paramBean=new ParameterBean("type",type,null);
			paramList.add(paramBean);
		}else {
			
		}
        
        if(uType!=null&&!uType.equals("")) {
        	
        	if(uValue!=null&&!uValue.equals("")) {
        		if(uType.equals("1")) {//
        			jpql.append(" AND lcl.userId = :uValue ");
        			countJpql.append(" AND lcl.userId = :uValue ");
        			
        			ParameterBean paramBean=new ParameterBean("uValue",Long.valueOf(uValue),null);
        			paramList.add(paramBean);
        		}else if(uType.equals("3")) {//
        			jpql.append(" AND r2.phone = :uValue ");
        			countJpql.append(" AND r2.phone = :uValue ");
        			
        			ParameterBean paramBean=new ParameterBean("uValue",uValue,null);
        			paramList.add(paramBean);
        		}else if(uType.equals("4")) {//
        			jpql.append(" AND r2.name like :uValue ");
        			countJpql.append(" AND r2.name like :uValue ");
        			
        			ParameterBean paramBean=new ParameterBean("uValue","%"+uValue+"%",null);
        			paramList.add(paramBean);
        		}
            	
    			
    		}else {
    			
    		}
        	
		}else {
			
		}
        
        if(uId!=null&&!uId.equals("")) {
			jpql.append(" AND r1.id = :uId ");
			countJpql.append(" AND r1.id = :uId ");
			
			ParameterBean paramBean=new ParameterBean("uId",Long.valueOf(uId),null);
			paramList.add(paramBean);
		}else {
			
		}
        if(operate!=null && !"".equals(operate)){
            jpql.append(" AND lcl.operate = :operate ");
            countJpql.append(" AND lcl.operate = :operate ");

            ParameterBean paramBean=new ParameterBean("operate",operate,null);
            paramList.add(paramBean);
        }else {

        }
        System.out.println("=============="+jpql);
        
        if(startDate!=null&&!"".equals(startDate) || endDate!=null&&!"".equals(endDate)) {
			if((startDate!=null&&!"".equals(startDate)) && (endDate!=null&&!"".equals(endDate))) {
//				startDate+=" 00:00:00";
//				endDate+=" 23:59:59";
				jpql.append(" AND lcl.createDate >= :startDate AND lcl.createDate <=:endDate ");
				countJpql.append(" AND lcl.createDate >= :startDate AND lcl.createDate <=:endDate ");
				
				ParameterBean paramBean = new ParameterBean("startDate",DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",startDate), TemporalType.TIMESTAMP);
				ParameterBean paramBean2 = new ParameterBean("endDate", DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",endDate), TemporalType.TIMESTAMP);
				paramList.add(paramBean);
				paramList.add(paramBean2);
				
				
			}else if(startDate!=null&&!"".equals(startDate)) {
//				startDate+=" 00:00:00";
				jpql.append(" AND lcl.createDate >= :startDate ");
				countJpql.append(" AND lcl.createDate >= :startDate ");
				
				ParameterBean paramBean = new ParameterBean("startDate", DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",startDate), TemporalType.TIMESTAMP);
				paramList.add(paramBean);
				
			}else if(endDate!=null&&!"".equals(endDate)) {
//				endDate+=" 23:59:59";
				jpql.append(" AND lcl.createDate <= :endDate ");
				countJpql.append(" AND lcl.createDate <= :endDate ");
				
				ParameterBean paramBean = new ParameterBean("endDate", DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",endDate), TemporalType.TIMESTAMP);
				paramList.add(paramBean);
				
			}
		}
        
        
		
		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        OrderByBean orderByObject=new OrderByBean("createDate",1,"lcl");
        orderByList.add(orderByObject);
        
        Long totalCount=liveCheckLogService.getObjectCountByJpql(countJpql, paramList);
        
        List liveCheckLogList=liveCheckLogService.getObjectByJpql(jpql, page.intValue(), rows.intValue(), "lcl", paramList, null, orderByList);
        
        
//        List<Long> userIdList=new ArrayList<Long>();
//        
//        for (Object object : consumeRecordList) {
//			
//        	ConsumeRecord lnt=(ConsumeRecord) object;
//        	
//        	userIdList.add(lnt.getCreatorId());
//			
//			
//		}
//        
//        if(userIdList!=null&&userIdList.size()>0) {
//        	List ruserList=ruserService.findUsersByIds(userIdList);
//    		
//    		
//    		List orderUserList=new ArrayList();
//    		for(int i=0;i<consumeRecordList.size();i++) {
//    			ConsumeRecord lnTemp=(ConsumeRecord)consumeRecordList.get(i);
//    			for(int j=0;j<ruserList.size();j++) {
//    				Ruser ruTemp=(Ruser)ruserList.get(j);
//    				if(ruTemp!=null&&lnTemp.getSender().longValue()==ruTemp.getId().longValue()) {
//    					
//    					lnTemp.setSenderName(ruTemp.getName());
//    					break;
//    				}
//    			}
//    			
//    		}
//        }
        
        HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("total",totalCount);
		map.put("rows",liveCheckLogList);
		return map;
    }

    @RequestMapping("shieldLive")
    @ResponseBody
    public ResultData shieldLive(Long roomId,Integer expireTime,String shieldMessage){
        ResultData resultData = new ResultData();
        Room room = roomService.find(roomId);
        if(room != null){
            jedisService.set(BicycleConstants.SHIELD_LIVE_USER + room.getCreatorId(), "" + (System.currentTimeMillis() + expireTime * 60 * 1000));
            jedisService.expire(BicycleConstants.SHIELD_LIVE_USER+room.getCreatorId(),expireTime*60);
            liveCheckLogService.check(U.getUid(), "屏蔽", shieldMessage, roomId, room.getCreatorId(),"shield");
            resultData.setResultCode("success");
            resultData.setResultMessage("屏蔽成功");
        }else{
            resultData.setResultCode("fail");
            resultData.setResultMessage("屏蔽失败，房间信息不存在！");
        }
        return resultData;
    }

    @RequestMapping("checkPageDetail")
    public ModelAndView checkPageDetail(Long roomId){
        ModelAndView modelAndView = new ModelAndView();
        Map<String,String> roomMap = jedisService.getMapByKey(BicycleConstants.ROOM_+roomId);
        Double score = jedisService.isSortedSetMemberInShard(BicycleConstants.ROOM_ORDER, String.valueOf(roomId));
        Map<String,String> map = new HashMap<String, String>();
        Set<String> screenshot = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_SCREENSHOT_ID + roomId, 0, -1);
        if(!roomMap.isEmpty() && StringUtils.isNotBlank(roomMap.get("id")) && StringUtils.isNotBlank(roomMap.get("creatorId")) && score!=null){
            Boolean shieldStatus = jedisService.ifKeyExists(BicycleConstants.SHIELD_LIVE_USER+roomMap.get("creatorId"));
            if(shieldStatus){
                map.put("shieldStatus", "1");
            }else{
                map.put("shieldStatus", "0");
            }
            //直播中
            Map<String,String> userInfo = jedisService.getMapByKey(BicycleConstants.USER_INFO+roomMap.get("creatorId"));
            map.put("userId",userInfo.get("id"));
            map.put("name",userInfo.get("name"));
            map.put("onlineNumber",roomMap.get("onlineNumber"));
            map.put("isLive","1");
            map.put("id",roomMap.get("id"));
            map.put("rtmpLiveUrl",roomMap.get("rtmpLiveUrl"));
        }else{
            //
            map.put("isLive","0");
        }
        modelAndView.addObject("screenshot",screenshot);
        modelAndView.addObject("userInfo",map);
        modelAndView.setViewName("check/query_check_detail");
        return modelAndView;
    }

}