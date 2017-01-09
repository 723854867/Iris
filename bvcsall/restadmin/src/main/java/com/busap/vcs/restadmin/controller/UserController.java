package com.busap.vcs.restadmin.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.busap.vcs.data.entity.*;
import com.busap.vcs.service.*;
import com.busap.vcs.service.utils.CSVUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.enums.ModuleType;
import com.busap.vcs.data.enums.UserType;
import com.busap.vcs.data.model.ExportWopaiNormalUser;
import com.busap.vcs.data.model.ExportWopaiUser;
import com.busap.vcs.data.model.RuserDisplay;
import com.busap.vcs.data.model.UserDisplay;
import com.busap.vcs.restadmin.utils.CommonUtils;
import com.busap.vcs.restadmin.utils.EnableFunction;
import com.busap.vcs.restadmin.utils.ResultData;
import com.busap.vcs.service.impl.SolrUserService;
import com.busap.vcs.service.kafka.producer.KafkaProducer;
import com.busap.vcs.service.utils.ExcelUtils;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.Page;
import com.busap.vcs.util.page.PagingContextHolder;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;
import org.springframework.web.servlet.ModelAndView;
import scala.util.parsing.combinator.testing.Str;

/**
 * 暂时结合easyui写的增删查改的例子
 * @author meizhiwen
 *
 */
@Controller()
@RequestMapping("user")
public class UserController extends CRUDController<User, Long>{

	@Value("${files.localpath}")
	private String basePath;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Resource(name="jedisService")
	private JedisService jedisService;

    @Resource
    private UserService userService;

	@Resource
	private VideoService videoService;

	@Resource(name="userService")
	private UserService UserService;

	@Resource(name="ruserService")
	private RuserService ruserService;

	@Autowired
	private RoleService roleService;

	@Resource(name = "solrUserService")
	private SolrUserService solrUserService;

	@Autowired
	KafkaProducer kafkaProducer;

	@Resource
	private  AnchorService anchorService;
	
	@Resource(name = "roomService")
	private RoomService roomService;

	@Resource
	private OrganizationService organizationService;
	
	@Resource(name = "consumeRecordService")
	private ConsumeRecordService consumeRecordService;

	@Resource(name="userService")
	@Override
	public void setBaseService(BaseService<User, Long> baseService) {
		this.baseService=baseService;
	}

	@Override
	public RespBody update(User entity) {
		User dbEntity=this.UserService.find(entity.getId());
		dbEntity.setUsername(entity.getUsername());
        dbEntity.setName(entity.getName());
        dbEntity.setPhone(entity.getPhone());
        dbEntity.setEmail(entity.getEmail());

		String[] rids=this.request.getParameterValues("rids[]");
		if(rids==null&&StringUtils.isNotBlank(this.request.getParameter("rids"))){
			rids=new String[]{this.request.getParameter("rids")};
		}

		if(rids==null){
			return this.respBodyWriter.toError("请为用户分配角色");
		}

		List<Long> rList = new ArrayList<Long>();
		for (String lg : rids) {
			rList.add(Long.parseLong(lg));
		}

		List<Role> roles=this.roleService.findAll(rList.toArray(new Long[0]));
		dbEntity.setRoles(roles);
		boolean lock = "1".equals(this.request.getParameter("isLocked")) ? true:false;
		if(lock){
			dbEntity.setIsLocked(lock);
		}else {
			dbEntity.setIsLocked(false);
			dbEntity.setIsEnabled(true);
		}
		this.UserService.update(dbEntity);
		this.operationLogService.save(new OperationLog(log_meduleType, "更新", dbEntity.getId().toString(), dbEntity.getClass().getSimpleName(), U.getUid(), U.getUname(), "更新"+dbEntity.getClass().getSimpleName()));
		return this.respBodyWriter.toSuccess();
	}

    @RequestMapping("deleteUser")
    @ResponseBody
    public RespBody deleteUser(Long id) {
        User dbEntity=this.UserService.find(id);
        dbEntity.setIsLocked(true);
        dbEntity.setIsEnabled(false);
        dbEntity.setLockedDate(new Date());
        this.UserService.update(dbEntity);
        this.operationLogService.save(new OperationLog(log_meduleType, "删除管理员用户", id.toString(), id.getClass().getSimpleName(), U.getUid(), U.getUname(), "更新"+id.getClass().getSimpleName()));
        return this.respBodyWriter.toSuccess();
    }

	@Override
	public RespBody create(User entity) {
		if (!validator(entity, BaseEntity.Save.class)) {
            return respBodyWriter.toError("");
        }
		if(StringUtils.isNotBlank(entity.getPassword())){
			entity.setPassword(DigestUtils.md5Hex(entity.getPassword()));
		}
		String[] rids=this.request.getParameterValues("rids[]");
		if(rids==null&&StringUtils.isNotBlank(this.request.getParameter("rids"))){
			rids=new String[]{this.request.getParameter("rids")};
		}

		if(rids==null){
			return this.respBodyWriter.toError("请为用户分配角色");
		}

		List<Long> rList = new ArrayList<Long>();
		for (String lg : rids) {
			rList.add(Long.parseLong(lg));
		}

		List<Role> roles=this.roleService.findAll(rList.toArray(new Long[0]));
		entity.setRoles(roles);
        baseService.save(entity);
        return respBodyWriter.toSuccess(entity);
	}

	@RequestMapping("userlist")
	public String list(){
		return "user/list";
	}

	@RequestMapping("frontUserlist")
	public String frontUserlist(HttpServletRequest req) throws ParseException {
		List<String> list = ruserService.selectAllRegPlatform();
		String iosChannel = "";
		String h5Channel = "";
		String androidChannel = "";
		for (String regPlatform:list) {
			if (regPlatform !=null && !"".equals(regPlatform)){
				if (regPlatform.startsWith("ios")){
					iosChannel += regPlatform+",";
					continue;
				} else if (regPlatform.startsWith("h5")) {
					h5Channel += regPlatform+",";
					continue;
				} else {
					androidChannel += regPlatform+",";
					continue;
				}
			}
		}
		if (iosChannel.endsWith(",")) {
			iosChannel = iosChannel.substring(0, iosChannel.length()-1);
		}
		if (h5Channel.endsWith(",")) {
			h5Channel = h5Channel.substring(0, h5Channel.length()-1);
		}
		if (androidChannel.endsWith(",")) {
			androidChannel = androidChannel.substring(0, androidChannel.length()-1);
		}
		
		StringBuffer jpqlApp = new StringBuffer();
        List<ParameterBean> paramListApp=new ArrayList<ParameterBean>();
        
        jpqlApp.append(" select  distinct r.appVersion FROM AppVersion r  ");
        
		
		List<OrderByBean> orderByListApp=new ArrayList<OrderByBean>();
        OrderByBean orderByObjectApp=new OrderByBean("createDate",1,"r");
        orderByListApp.add(orderByObjectApp);
        
        
        try {
			List appVersionList=userService.getObjectByJpql(jpqlApp, 0, 1000, "r", paramListApp, null, orderByListApp);
			
			req.setAttribute("appVersionList", appVersionList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		req.setAttribute("iosChannel", iosChannel);
		req.setAttribute("h5Channel", h5Channel);
		req.setAttribute("androidChannel", androidChannel);
		return "user/frontUserList";
	}

	@RequestMapping("queryUserList")
	@ResponseBody
	@EnablePaging
	public Map<String,Object> queryUserList(@ModelAttribute("queryPage") JQueryPage queryPage,
											@RequestParam(value = "username",required = false) String username,
											@RequestParam(value = "phone",required = false) String phone,
											@RequestParam(value = "email",required = false) String email,
											@RequestParam(value = "group",required = false) Integer group,
											@RequestParam(value = "isLocked",required = false) Integer isLocked,
											@RequestParam(value = "isEnabled",required = false) Integer isEnabled){
        Map<String,Object> params = new HashMap<String, Object>();
		params.put("username",username);
		params.put("phone",phone);
		params.put("email",email);
		params.put("group",group);
		params.put("isLocked",isLocked);
		params.put("isEnabled",isEnabled);
        List<UserDisplay> list = userService.queryUsers(params);
		Page page = PagingContextHolder.getPage();
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("total",page.getTotalResult());
        map.put("rows",list);
		return map;
	}

	/**
	 * 获取普通用户信息
	 * @author huoshanwei
	 * @return map ok 成功  error失败
	 */
	@RequestMapping("queryFrontUserList")
	@ResponseBody
	public Map<String,Object> queryFrontUserList(@ModelAttribute("queryPage") JQueryPage queryPage,
												 @RequestParam(value = "sort", required = false)  String sort,
												 @RequestParam(value = "order", required = false)  String order,
												 @RequestParam(value = "user",required = false) Long user,
												 @RequestParam(value = "userKeyword",required = false) String userKeyword,
												 @RequestParam(value = "stat",required = false) Integer stat,
												 @RequestParam(value = "vipStat",required = false) Integer vipStat,
												 @RequestParam(value = "rankAble",required = false) Integer rankAble,
												 @RequestParam(value = "sex",required = false) Integer sex,
												 @RequestParam(value = "addr",required = false) String addr,
												 @RequestParam(value = "startCount",required = false) Integer startCount,
												 @RequestParam(value = "endCount",required = false) Integer endCount,
												 @RequestParam(value = "startTime",required = false) String startTime,
												 @RequestParam(value = "endTime",required = false) String endTime,
												 @RequestParam(value = "regPlatform", required = false)  String regPlatform,
												 @RequestParam(value = "appVersion", required = false)  String appVersion,
												 @RequestParam(value = "regPlatformChannel", required = false)  String regPlatformChannel,
												 @RequestParam(value = "allowPublish",required = false) Integer allowPublish,
												 @RequestParam(value = "isAnchor",required = false) Integer isAnchor,
												 @RequestParam(value = "organizationId",required = false) Long organizationId,
												 @RequestParam(value = "recommendBit1",required = false) Long recommendBit1,
												 @RequestParam(value = "thirdFrom",required = false) String thirdFrom){
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("pageStart",(queryPage.getPage()-1)*queryPage.getRows());
		params.put("pageSize",queryPage.getRows());
		params.put("type",UserType.普通用户.getName());
		params.put("sort",sort);
		params.put("order",order);
		params.put("userKey",user);
		params.put("userKeyword",userKeyword);
		params.put("stat",stat);
		params.put("vipStat",vipStat);
		params.put("rankAble",rankAble);
		params.put("sex",sex);
		params.put("addr",addr);
		params.put("startCount",startCount);
		params.put("endCount",endCount);
		params.put("startTime",startTime);
		params.put("endTime",endTime);
		params.put("regPlatform",regPlatform);
		params.put("appVersion",appVersion);
		params.put("regPlatformChannel",regPlatformChannel);
		params.put("allowPublish",allowPublish);
		params.put("isAnchor",isAnchor);
		params.put("organizationId",organizationId);
		params.put("thirdFrom",thirdFrom);
		params.put("recommendBit",recommendBit1);
		List<RuserDisplay> list = ruserService.queryWopaiRusers(params);
		for (RuserDisplay ruserDisplay : list){
			Long realFansCount = ruserService.queryRealFansCountById(ruserDisplay.getId());
			ruserDisplay.setRealFansCount(realFansCount == null ? 0 : realFansCount);
		}
		int pageTotal = ruserService.selectRusersCount(params);
		map.put("rows",list);
		map.put("total",pageTotal);
		return map;
	}

	@RequestMapping("rolelistAjax")
	@ResponseBody
	public List roleList() {
		List<Role> rlist=this.roleService.findAll();
		return rlist;
	}

	@RequestMapping("updatePasswordAjax")
	@ResponseBody
	public RespBody updatePassword(Long id,String old_password,String new_password,String new_repeat_password){
		User user=this.UserService.find(id);
		if(!user.getPassword().equals(DigestUtils.md5Hex(old_password))){
			return this.respBodyWriter.toError("原密码错误");
		}
		user.setPassword(DigestUtils.md5Hex(new_password));
		this.UserService.update(user);
		this.operationLogService.save(new OperationLog(ModuleType.移动麦视后台.getName(), "修改", id+"", "User", U.getUid(), U.getUname(), "修改密码"));
		return this.respBodyWriter.toSuccess();
	}

	@RequestMapping("updateInfoAjax")
	@ResponseBody
	public RespBody updateInfo(Long id,@RequestParam(value = "phone", required = false) String phone,@RequestParam(value = "email", required = false) String email){
		User user=this.userService.find(id);
		if(StringUtils.isNotBlank(phone)){
			user.setPhone(phone);
		}
		if(StringUtils.isNotBlank(email)){
			user.setEmail(email);
		}
		this.userService.update(user);
		this.operationLogService.save(new OperationLog(ModuleType.移动麦视后台.getName(), "修改", id+"", "User", U.getUid(), U.getUname(), "修改用户信息"));
		return this.respBodyWriter.toSuccess();
	}

	@RequestMapping("roleListByUserAjax")
	@ResponseBody
	public List roleListByUser(Long id){
		User user=this.UserService.find(id);
		List list=user.getRoles();
		return list;
	}

	/**
	 * 删除用户信息
	 * @param id 用户ID
	 * @author huoshanwei
	 * @return map ok 成功  error失败
	 */
	@RequestMapping("deleteRuser")
	@ResponseBody
	public ResultData deleteRuser(Long id){
        ResultData resultData = new ResultData();
//        List<Video> videoList = videoService.queryVideosByCreatorId(id);
//        if(videoList.isEmpty()){
//            Ruser ruser = ruserService.selectByPrimaryKey(id);
//            if(ruser!=null){
//                int result = ruserService.deleteRuser(id);
//                if(result == 1){
//					//if type eq "delete"
//					//删除redis中的马甲信息
//					if (ruser.getType().equals("majia")) {
//						jedisService.deleteSetItemFromShard(BicycleConstants.MAJIA_UID, String.valueOf(ruser.getId()));
//						jedisService.deleteValueFromMap(BicycleConstants.MAJIA + String.valueOf(ruser.getId()),"uid","name","pic","vstat");
//					}
//					resultData.setResultCode("ok");
//                    resultData.setResultMessage("删除成功");
//                }else {
//                    resultData.setResultCode("error");
//                    resultData.setResultMessage("删除失败");
//                }
//            }else {
//                resultData.setResultCode("error");
//                resultData.setResultMessage("用户不存在");
//            }
//        }else{
//            resultData.setResultCode("error");
//            resultData.setResultMessage("删除失败，此用户下存在视频信息，请先删除视频！");
//        }

		return resultData;
	}

	/**
	 * 根据用户ID获取用户信息
	 * @param id 用户ID
	 * @author huoshanwei
	 * @return map ok 成功  error失败
	 */
	@RequestMapping("getRuserStateInfo")
	@ResponseBody
	public Map<String,Object> getRuserStateInfo(Long id){
		Ruser ruser = ruserService.selectByPrimaryKey(id);
		Map<String,Object> map = new HashMap<String, Object>();
		if(ruser != null){
			map.put("resultCode","ok");
			map.put("id",ruser.getId());
			map.put("name",ruser.getName());
			map.put("pic", ruser.getPic());
			map.put("rankAble", ruser.getRankAble());
			map.put("sex", ruser.getSex());
			map.put("signature", ruser.getSignature());
			map.put("state", ruser.getStat());
			map.put("vstate",ruser.getVipStat());
			map.put("vipWeight",ruser.getVipWeight());
			map.put("dayPopularity",ruser.getDayPopularity());
			map.put("allowPublish",ruser.getAllowPublish());
			map.put("liveWeight",ruser.getLiveWeight());
			map.put("isAnchor",ruser.getIsAnchor());
			map.put("organizationId",ruser.getOrganizationId());
			/*map.put("isBlacklist",ruser.getIsBlacklist());*/
			map.put("liveType",ruser.getLiveType());
			map.put("recommendBit",ruser.getRecommendBit());
		}else{
			map.put("resultCode", "error");
		}
		return map;
	}

	/**
	 * 更新用户等级和状态信息
	 * @param id 用户ID
	 * @param vipStat 用户等级
	 * @param -- liveWeight 直播权重
	 * @author huoshanwei
	 * @return map ok 成功  error失败
	 */
	@RequestMapping("doEditStateAndVstate")
	@ResponseBody
	public Map<String,Object> doEditStateAndVstate(Long id,Integer vipStat,Integer vipWeight,Integer liveWeight,Integer allowPublish,
												   @RequestParam(value = "organizationId",required = false) Long organizationId,
												   /*@RequestParam(value = "isBlacklist",required = false) Integer isBlacklist,*/
												   @RequestParam(value = "recommendBit",required = false) Integer recommendBit,
												   @RequestParam(value = "liveType",required = false) Integer liveType){
		Map<String,Object> map = new HashMap<String, Object>();
		Ruser ruser = ruserService.selectByPrimaryKey(id);
		if(ruser == null){
			map.put("resultCode","error");
			map.put("resultMessage","此用户不存在！");
			return map;
		}
		if (ruser.getLiveWeight() != liveWeight) {
			updateLiveRoomWeight(id, liveWeight);
		}

		ruser.setVipStat(vipStat);
		ruser.setVipWeight(vipWeight);
		ruser.setLiveWeight(liveWeight);
		ruser.setAllowPublish(allowPublish);
		Long orgId = ruser.getOrganizationId();
		ruser.setOrganizationId(organizationId);
		/*ruser.setIsBlacklist(isBlacklist);*/
		/*if (organizationId != null) {
			ruser.setIsBlacklist(0);
		} else {
			ruser.setIsBlacklist(isBlacklist);
		}*/
		ruser.setLiveType(liveType);
		ruser.setRecommendBit(recommendBit);
		int result = ruserService.updateRuser(ruser);
		if (result == 1) {
			map.put("resultCode", "ok");
			map.put("resultMessage", "更新用户成功！");
			try {
				//将马甲用户信息写入redis
				setMajiaToRedis(ruser.getId());
				if (ruser.getVipStat().intValue() != vipStat.intValue()) {
					if (ruser.getVipStat().intValue() == 1) {
						jedisService.deleteSetItemFromShard(BicycleConstants.VIP_OF_BLUE, id.toString());
					} else if (ruser.getVipStat().intValue() == 2) {
						jedisService.deleteSetItemFromShard(BicycleConstants.VIP_OF_YELLOW, id.toString());
					} else if (ruser.getVipStat().intValue() == 3) {
						jedisService.deleteSetItemFromShard(BicycleConstants.VIP_OF_GREEN, id.toString());
					}
					//由普通用户改为VIP的
					if (vipStat == 0) {
						// 由vip用户改为普通用户
						logger.info("update user...cancel VIP stat! uid=" + id);
						Set<String> vipIds = jedisService.getSetFromShard(BicycleConstants.NEW_VIP_AUTO_FOCUS);
						for (String string : vipIds) {
							if (string.startsWith(String.valueOf(id))) {
								jedisService.deleteSetItemFromShard(BicycleConstants.NEW_VIP_AUTO_FOCUS, string);
							}
						}
					} else {
						logger.info("update user...set VIP stat! uid=" + id);
						if (vipStat.intValue() == 1) {
							jedisService.setValueToSetInShard(BicycleConstants.VIP_OF_BLUE, id.toString());
						} else if (vipStat.intValue() == 2) {
							jedisService.setValueToSetInShard(BicycleConstants.VIP_OF_YELLOW, id.toString());
						} else if (vipStat.intValue() == 3) {
							jedisService.setValueToSetInShard(BicycleConstants.VIP_OF_GREEN, id.toString());
						}
					}
					// insert cache
					if (vipStat == 1 || vipStat == 2 || vipStat == 3) {
						jedisService.setValueToSetInShard(BicycleConstants.NEW_VIP_AUTO_FOCUS,
								String.valueOf(id) + "-" + System.currentTimeMillis());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			updateAnchorCount(organizationId,orgId,id);//根据新旧orgId更新机构表中主播数量及将机构用户加入提现黑名单
			//将用户手机号同步至anchor表
			if(organizationId != null) {
				Anchor anchor = anchorService.getAnchorByUserid(id);
				//当主播信息不为空且主播手机号为空时
				if (anchor != null && StringUtils.isBlank(anchor.getPhone())) {
					anchor.setPhone(ruser.getBandPhone());
					anchorService.updateByPrimaryKey(anchor);
				}
			}
			//将直播权重写入redis用户信息中
			String key = BicycleConstants.USER_INFO + id;
			jedisService.setValueToMap(key, "liveWeight", String.valueOf(liveWeight));
			jedisService.setValueToMap(key, "vipStat", String.valueOf(vipStat));
			jedisService.setValueToMap(key, "vipWeight", String.valueOf(vipWeight));
			jedisService.setValueToMap(key, "organizationId", String.valueOf(organizationId));
			/*jedisService.setValueToMap(key, "isBlacklist", String.valueOf(isBlacklist));*/
			jedisService.setValueToMap(key, "liveType", String.valueOf(liveType));
			jedisService.setValueToMap(key, "recommendBit", String.valueOf(recommendBit));
			
			if(recommendBit!=null&&recommendBit==0) {
				List<Object[]> list = consumeRecordService.getTopPointUserInfo();
				if (list != null && list.size() >0) {
					try {
						jedisService.delete(BicycleConstants.TOP_POINT_USER_ID);
						for (Object[] array:list){
							if (array[0] != null && array[1] !=null) {
								jedisService.setValueToSortedSetInShard(BicycleConstants.TOP_POINT_USER_ID, Double.parseDouble(String.valueOf(array[1])), String.valueOf(array[0]));
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}else if(recommendBit!=null&&recommendBit==1) {
				if(jedisService.keyExists(BicycleConstants.TOP_POINT_USER_ID)) {
					//从缓存中清除首页金豆榜的用户id
					jedisService.deleteSortedSetItemFromShard(BicycleConstants.TOP_POINT_USER_ID, String.valueOf(id));
				}
				
			}
			
		} else {
			map.put("resultCode", "error");
			map.put("resultMessage", "更新用户失败！");
		}
		return map;
	}

	//更新机构表中主播数量
	private void updateAnchorCount(Long organizationId,Long orgId,Long id){
		if (organizationId != null) {
			//修改机构表中anchor_count字段 TODO 此方法需优化做事务处理
			if (orgId != null) {
				if (orgId.longValue() != organizationId.longValue()) {
					organizationService.updateAnchorCount(organizationId);
					organizationService.updateAnchorCount(orgId);
				}
			} else {
				organizationService.updateAnchorCount(organizationId);
			}

		} else {
			if (orgId != null) {
				organizationService.updateAnchorCount(orgId);
			}

		}
	}

	/**
	 * 直播权重即时生效
	 * @param id
	 * @param liveWeight
	 */
	private void updateLiveRoomWeight(Long id, Integer liveWeight) {
		Room room = roomService.queryLivingRoomByUserId(id);
		if(room != null){
			room.setAdditionalNumber(room.getOnlineNumber()+liveWeight);
			roomService.update(room);
			
			Double score = jedisService.isSortedSetMemberInShard(BicycleConstants.ROOM_ORDER, room.getId().toString());
			if(score != null){
				jedisService.setValueToSortedSetInShard(BicycleConstants.ROOM_ORDER, liveWeight.doubleValue()+room.getOnlineNumber(),room.getId().toString());
				if(jedisService.isKeyExistsInMap(BicycleConstants.ROOM_+room.getId(), "additionalNumber")){
					jedisService.setValueToMap(BicycleConstants.ROOM_+room.getId(), "additionalNumber", liveWeight.toString());
				}
			}
		}
	}

	/**
	 * 更新马甲用户信息
	 * @param ruser ruser对象
	 * @author huoshanwei
	 * @return map ok 成功  error失败
	 */
	@RequestMapping("doUpdateRuser")
	@ResponseBody
	public Map<String,Object> doUpdateRuser(@ModelAttribute("file") MultipartFile file,@ModelAttribute("ruser") Ruser ruser){
		Map<String,Object> map = new HashMap<String, Object>();
		Ruser r = ruserService.selectByPrimaryKey(ruser.getId());
		if(r == null){
			map.put("resultCode","error");
			map.put("resultMessage","此用户不存在！");
			return map;
		}
		if(!file.isEmpty()){
			String relPath = File.separator + "userHeadPic" + File.separator + "majiaHeadPic" + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator;
			String originalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_") + file.getOriginalFilename();
			String relUrl = "";
			try {
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File(basePath + relPath, originalFilename));
				relUrl = relPath+originalFilename;
				logger.info(relUrl);
				ruser.setPic(relUrl);//用户头像存储路径
			} catch (IOException e) {
				logger.error("文件[" + originalFilename + "]上传失败",e);
				e.printStackTrace();
			}
		}
		int result = ruserService.updateRuser(ruser);
		if(result == 1){
			jedisService.saveAsMap(BicycleConstants.USER_INFO + ruser.getId(), ruser);
			//将马甲用户信息更新至redis
			setMajiaToRedis(ruser.getId());
			//新注册用户昵称加入solr引擎索引
        	try {
    			//solrUserService.addUser(user.getId(), user.getName());
    			solrUserService.addUser(ruser.getId(), ruser.getName(),ruser.getPhone());
    		} catch (SolrServerException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

			map.put("resultCode", "ok");
			map.put("resultMessage","更新用户成功！");
		}else{
			map.put("resultCode", "error");
			map.put("resultMessage","更新用户失败！");
		}
		return map;
	}

	@RequestMapping("userInfo")
	public String userInfo(){
		Long id = U.getUid();
		User u = this.userService.find(id);
		this.request.setAttribute("user", u);
		this.request.setAttribute("role", u.getRoles().get(0));
		return "user/userInfo";
	}

	@RequestMapping("batchSettingRankAble")
	@ResponseBody
	public ResultData batchSettingRankAble(String ids,int rankAble){
		ResultData resultData = new ResultData();
		String[] idArray = ids.split(",");
		int result = 0;
		if(rankAble == 0){
			result = ruserService.batchRankAbleToAllow(idArray);
		}else{
			result = ruserService.batchRankAbleToBan(idArray);
		}
		if(result > 0){
			resultData.setResultCode("success");
			resultData.setResultMessage("更新成功！");
		}else{
			resultData.setResultCode("fail");
			resultData.setResultMessage("更新失败！");
		}
		return resultData;
	}

	@EnableFunction("用户管理,我拍用户-导出我拍用户信息")
	@RequestMapping("exportWopaiUsersToExcel")
	public void exportWopaiUsersToExcel(@RequestParam("vipStat") int vipStat,
										HttpServletResponse response) throws IOException {
		if (vipStat != 0) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("vipStat", vipStat);
			List<ExportWopaiUser> list = ruserService.queryWopaiUsers(params);
			ExcelUtils<ExportWopaiUser> excelUtils = new ExcelUtils<ExportWopaiUser>();
			String[] headers = {"用户ID", "用户昵称", "发布视频数", "粉丝", "铁粉" ,"积分", "24小时人气", "周人气", "月人气","注册时间","性别","年龄","所在地"};
			response.reset();
			// 设定输出文件头
			response.setHeader("Content-disposition", "attachment; filename=wopai_vip_user.xls");
			// 定义输出类型
			response.setContentType("application/msexcel");
			OutputStream out = response.getOutputStream();
			excelUtils.exportExcel("BliveVIP用户", headers, list, out, "yyyy-MM-dd HH:mm:ss");
		}
	}

	//将马甲信息更新或添加至redis
	private void setMajiaToRedis(Long id) {
		Ruser ruser = ruserService.selectByPrimaryKey(id);
		if (ruser.getType().equals("majia")) {
			String uidStr = String.valueOf(id);
			//将用户的uid放到set中
			jedisService.setValueToSetInShard(BicycleConstants.MAJIA_UID, uidStr);
			//将用户信息放到map中
			jedisService.setValueToMap(BicycleConstants.MAJIA + uidStr, "uid", uidStr);
			jedisService.setValueToMap(BicycleConstants.MAJIA + uidStr, "name", ruser.getName() == null ? "" : ruser.getName());
			jedisService.setValueToMap(BicycleConstants.MAJIA + uidStr, "pic", ruser.getPic() == null ? "" : ruser.getPic());
			jedisService.setValueToMap(BicycleConstants.MAJIA + uidStr, "vstat", String.valueOf(ruser.getVipStat()));
		}
	}

	/**
	 * 批量导入马甲用户信息
	 * @param ruserFile txt
	 * @author huoshanwei
	 * @return map ok 成功  error失败
	 */
	@RequestMapping("batchImportMajiaUserInfo")
    @ResponseBody
    public Map<String, Object> batchImportMajiaUserInfo(@RequestParam("ruserFile") MultipartFile ruserFile) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (!ruserFile.isEmpty()) {
            String path = File.separator + "sensitiveWord" + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator;
            String originalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmm_") + ruserFile.getOriginalFilename();
            try {
                FileUtils.copyInputStreamToFile(ruserFile.getInputStream(), new File(basePath + path, originalFilename));
                String txtStr = CommonUtils.readTxt(basePath + path + originalFilename, "\n");
                String txt = txtStr.substring(0, txtStr.length() - 1);
                String[] txtArray = txt.split("\n");
                List<String> listArray = Arrays.asList(txtArray);
                int listSize = listArray.size();
                String errorStr = "";
                for (int i = 0; i < listSize; i++) {
                    String[] ruserArray = listArray.get(i).split("\\|@\\|");
                    if(ruserArray.length == 4) {
                        Ruser ruser = new Ruser();
                        ruser.setPhone("13800138000");
                        ruser.setIsEnabled(true);
                        ruser.setIsLocked(false);
                        ruser.setName(ruserArray[0]);
                        ruser.setUsername(String.valueOf(System.currentTimeMillis()));
                        ruser.setType(UserType.马甲.getName());
                        ruser.setPassword("password@_!@#");
                        ruser.setPic("/" + ruserArray[2]);
                        ruser.setSignature(ruserArray[1]);
                        ruser.setSex(ruserArray[3]);
                        int result = ruserService.insertRuser(ruser);
                        if (result > 0) {
                            String uidStr = String.valueOf(ruser.getId());
                            jedisService.saveAsMap(BicycleConstants.USER_INFO + uidStr, ruser);
                            //setMajiaToRedis(ruser.getId());
                            //将用户的uid放到set中
                            jedisService.setValueToSetInShard(BicycleConstants.MAJIA_UID, uidStr);
                            //将用户信息放到map中
                            jedisService.setValueToMap(BicycleConstants.MAJIA + uidStr, "uid", uidStr);
                            jedisService.setValueToMap(BicycleConstants.MAJIA + uidStr, "name", ruser.getName() == null ? "" : ruser.getName());
                            jedisService.setValueToMap(BicycleConstants.MAJIA + uidStr, "pic", ruser.getPic() == null ? "" : ruser.getPic());
                            jedisService.setValueToMap(BicycleConstants.MAJIA + uidStr, "vstat", String.valueOf(0));
                            //将马甲用户昵称等信息加入solr引擎索引
                            try {
                                solrUserService.addUser(ruser.getId(), ruser.getName(), ruser.getPhone());
                            } catch (SolrServerException s) {
                                s.printStackTrace();
                            } catch (IOException io) {
                                io.printStackTrace();
                            }
                        } else {
                            logger.info("第" + (i + 1) + "行数据写入失败！");
                        }
                    } else {
                        errorStr += (i + 1) + ",";
                        logger.info("第" + (i + 1) + "行数据不规则！");
                    }
                }
                map.put("resultCode", "success");
                errorStr = errorStr.substring(0, errorStr.length() - 1);

                String error = "导入成功！";
                if (errorStr.split(",").length > 0) {
                    error = "部分导入成功！失败行数为：第" + errorStr + "行，失败原因：数据不规则。";
                }
                map.put("resultMessage", error);
            } catch (IOException e) {
                map.put("resultCode", "error");
                map.put("resultMessage", "导入异常，请重试！");
                e.printStackTrace();
            }
        } else {
            map.put("resultCode", "error");
            map.put("resultMessage", "文件不存在！");
        }
        return map;
    }

	@RequestMapping("exportWopaiNormalUsersToExcel")
	public void exportWopaiNormalUsersToExcel(@RequestParam(value = "sort", required = false)  String sort,
											  @RequestParam(value = "order", required = false)  String order,
											  @RequestParam(value = "user",required = false) Long user,
											  @RequestParam(value = "userKeyword",required = false) String userKeyword,
											  @RequestParam(value = "stat",required = false) Integer stat,
											  @RequestParam(value = "vipStat",required = false) Integer vipStat,
											  @RequestParam(value = "rankAble",required = false) Integer rankAble,
											  @RequestParam(value = "sex",required = false) Integer sex,
											  @RequestParam(value = "addr",required = false) String addr,
											  @RequestParam(value = "startCount",required = false) Integer startCount,
											  @RequestParam(value = "endCount",required = false) Integer endCount,
											  @RequestParam(value = "startTime",required = false) String startTime,
											  @RequestParam(value = "endTime",required = false) String endTime,
											  @RequestParam(value = "regPlatform", required = false)  String regPlatform,
											  @RequestParam(value = "regPlatformChannel", required = false)  String regPlatformChannel,
											  @RequestParam(value = "allowPublish",required = false) Integer allowPublish,
											  @RequestParam(value = "isAnchor",required = false) Integer isAnchor,
											  @RequestParam(value = "organizationId",required = false) Long organizationId,
											  @RequestParam(value = "thirdFrom",required = false) String thirdFrom,
											  HttpServletResponse response)throws IOException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type",UserType.普通用户.getName());
		params.put("sort",sort);
		params.put("order",order);
		params.put("userKey",user);
		params.put("userKeyword",userKeyword);
		params.put("stat",stat);
		params.put("vipStat",vipStat);
		params.put("rankAble",rankAble);
		params.put("sex",sex);
		params.put("addr",addr);
		params.put("startCount",startCount);
		params.put("endCount",endCount);
		params.put("startTime",startTime);
		params.put("endTime",endTime);
		params.put("regPlatform",regPlatform);
		params.put("regPlatformChannel",regPlatformChannel);
		params.put("allowPublish",allowPublish);
		params.put("isAnchor",isAnchor);
		params.put("organizationId",organizationId);
		params.put("thirdFrom",thirdFrom);
		List<ExportWopaiNormalUser> list = ruserService.queryWopaiUserToExcel(params);
		for (ExportWopaiNormalUser exportWopaiNormalUser : list){
			Long realFansCount = ruserService.queryRealFansCountById(Long.valueOf(exportWopaiNormalUser.getId()));
			exportWopaiNormalUser.setRealFansCount(String.valueOf(realFansCount == null ? 0 : realFansCount));
		}
		/*ExcelUtils<ExportWopaiNormalUser> excelUtils = new ExcelUtils<ExportWopaiNormalUser>();
		//String[] headers = {"用户ID", "用户昵称", "发布视频数", "粉丝", "铁粉" ,"积分", "24小时人气", "周人气", "月人气","注册时间","上传视频时间段","性别","年龄","所在地"};
		String[] headers = {"用户ID", "用户昵称", "发布视频数", "粉丝", "铁粉" ,"积分", "注册时间","性别","年龄","所在地"};
		response.reset();
		// 设定输出文件头
		response.setHeader("Content-disposition", "attachment; filename=wopai_normal_user.xls");
		// 定义输出类型
		response.setContentType("application/msexcel");
		OutputStream out = response.getOutputStream();
		excelUtils.exportExcel("Blive用户", headers, list, out, "yyyy-MM-dd HH:mm:ss");*/
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List cList = new ArrayList<Map>();
		for (ExportWopaiNormalUser exportWopaiNormalUser : list) {
			Map row = new LinkedHashMap<String, String>();
			row.put("1",exportWopaiNormalUser.getId());
			row.put("2",exportWopaiNormalUser.getName());
			row.put("3",exportWopaiNormalUser.getVideoCount());
			row.put("4",exportWopaiNormalUser.getFansCount());
			row.put("5",exportWopaiNormalUser.getRealFansCount());
			row.put("6",exportWopaiNormalUser.getSignSum());
			row.put("7",sdf.format(exportWopaiNormalUser.getCreateDate()));
			row.put("8",exportWopaiNormalUser.getSex());
			row.put("9",exportWopaiNormalUser.getAge());
			row.put("10",exportWopaiNormalUser.getAddress());
			cList.add(row);
		}
		LinkedHashMap headers = new LinkedHashMap();
		headers.put("1", "用户ID");
		headers.put("2", "用户昵称");
		headers.put("3", "发布视频数");
		headers.put("4", "粉丝");
		headers.put("5", "铁粉");
		headers.put("6", "积分");
		headers.put("7", "注册时间");
		headers.put("8", "性别");
		headers.put("9", "年龄");
		headers.put("10", "所在地");
		File file = CSVUtils.createCSVFile(cList, headers, basePath+"/exportExcel/", "Blive用户");
		CommonUtils.download(file.getPath(),response);

	}

	@RequestMapping("withdrawCashBlackList")
	public ModelAndView withdrawCashBlackList(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("user/wc_black_list");
		return mav;
	}

	@RequestMapping("insertWCBlackList")
	@ResponseBody
	public ResultData insertWCBlackList(Long userId){
		ResultData resultData = new ResultData();
		Ruser ruser = ruserService.find(userId);
		if(ruser != null){
			if(ruser.getIsBlacklist() != null){
				if(ruser.getIsBlacklist() == 0){
					resultData.setResultCode("fail");
					resultData.setResultMessage("sorry,此用户已经在提现黑名单中了哦！");
				}else{
					ruser.setIsBlacklist(0);//加入黑名单
					ruserService.save(ruser);
					jedisService.setValueToMap(BicycleConstants.USER_INFO+userId, "isBlacklist", "0");
					resultData.setResultCode("success");
					resultData.setResultMessage("添加成功！");
				}
			}else{
				resultData.setResultCode("fail");
				resultData.setResultMessage("sorry,此用户已经在提现黑名单中了哦！");
			}
		}else{
			resultData.setResultCode("fail");
			resultData.setResultMessage("此用户不存在，请不要淘气哦！");
		}
		return resultData;
	}

	@RequestMapping("queryWCBlackList")
	@ResponseBody
	public Map<String,Object> queryWCBlackList(@ModelAttribute("queryPage") JQueryPage queryPage,
											   @RequestParam(value = "user",required = false) Long user,
											   @RequestParam(value = "userKeyword",required = false) String userKeyword){
		Map<String,Object> resultMap = new HashMap<String, Object>(2);
		Map<String,Object> paramsMap = new HashMap<String, Object>(1);
		if(queryPage.getPage() != null && queryPage.getRows() != null){
			paramsMap.put("isBlacklist",0);
			paramsMap.put("userKey",user);
			paramsMap.put("userKeyword",userKeyword);
			paramsMap.put("pageStart",(queryPage.getPage()-1)*queryPage.getRows());
			paramsMap.put("pageSize",queryPage.getRows());
			paramsMap.put("type",UserType.普通用户.getName());
			List<RuserDisplay> list = ruserService.queryWopaiRusers(paramsMap);
			int pageTotal = ruserService.selectRusersCount(paramsMap);
			resultMap.put("rows",list);
			resultMap.put("total",pageTotal);
		}else {
			resultMap.put("rows","");
			resultMap.put("total",0);
		}

		return resultMap;
	}

	@RequestMapping("removeWCBlackList")
	@ResponseBody
	public ResultData removeWCBlackList(Long userId){
		ResultData resultData = new ResultData();
		Ruser ruser = ruserService.find(userId);
		if (ruser != null){
			if(ruser.getIsBlacklist() != null){
				if(ruser.getIsBlacklist() == 1){
					resultData.setResultCode("fail");
					resultData.setResultMessage("移除失败，此用户不在黑名单中，请不要淘气哦！");
				}else{
					ruser.setIsBlacklist(1);
					ruserService.save(ruser);
					jedisService.setValueToMap(BicycleConstants.USER_INFO+userId, "isBlacklist", "1");
					resultData.setResultCode("success");
					resultData.setResultMessage("移除成功！");
				}
			}else{
				ruser.setIsBlacklist(1);//1未加入黑名单
				ruserService.save(ruser);
				jedisService.setValueToMap(BicycleConstants.USER_INFO+userId, "isBlacklist", "1");
				resultData.setResultCode("success");
				resultData.setResultMessage("移除成功！");
			}
		}else{
			resultData.setResultCode("fail");
			resultData.setResultMessage("移除失败，用户ID不存在，请重新核对后再试！");
		}
		return resultData;
	}

}