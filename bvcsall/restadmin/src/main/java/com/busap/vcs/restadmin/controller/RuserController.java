package com.busap.vcs.restadmin.controller;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.OperatePlan;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.model.AnchorDetailDisplay;
import com.busap.vcs.data.model.PrizeDetailDisplay;
import com.busap.vcs.restadmin.utils.CommonUtils;
import com.busap.vcs.restadmin.utils.ResultData;
import com.busap.vcs.service.*;
import com.busap.vcs.service.impl.SolrUserService;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.PagingContextHolder;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.Tuple;

import javax.annotation.Resource;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller()
@RequestMapping("ruser")
public class RuserController extends CRUDController<Ruser,Long> {

	@Override
	public void setBaseService(BaseService<Ruser, Long> baseService) {
		this.baseService=baseService;
	}
	@Resource(name="ruserService")
	private RuserService ruserService;
	
	@Resource(name="operatePlanService")
	private OperatePlanService operatePlanService;
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Resource(name = "solrUserService")
	private SolrUserService solrUserService;

	@Resource
	private AnchorService anchorService;

	@Resource
	private LiveCheckLogService liveCheckLogService;

	/**
	 * 修改用户状态，禁言、封号、解除
	 * @param uid
	 * @param stat
	 * @return
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	@RequestMapping("chgstat")
	@ResponseBody
	public RespBody changeStat(Long uid,Integer stat,@RequestParam(value = "expired", required = false) Date expired,@RequestParam(value = "reason", required = false) String reason) throws SolrServerException, IOException{
		Ruser user = ruserService.find(uid);
		if(user!=null) {
			if(stat.intValue() == 2){
				ruserService.offlineLiveRoom(uid, U.getUid());
			}
		
			user.setStat(stat);
			if(expired != null)
				user.setLockedDate(expired);
			user.setModifyDate(new Date());

			ruserService.update(user);

			jedisService.saveAsMap(BicycleConstants.USER_INFO+user.getId(), user);

			String key = "FBU_"+uid.toString();
			if(stat.intValue() == 0){
				if(jedisService.keyExists(key))
					jedisService.delete(key);
				//solrUserService.addUser(uid, user.getName());
				solrUserService.addUser(uid, user.getName(),user.getPhone());

				//解禁把用户从提现黑名单删除
				user.setIsBlacklist(1);
				ruserService.update(user);
			} else {
				jedisService.set(key, stat.toString());
				if(expired != null){
					Long expiredTime = expired.getTime()-System.currentTimeMillis();
					if(expiredTime>0)
						jedisService.expire(key, (int)(expiredTime/1000));
				}
				solrUserService.deleteDocByid(uid);

				//账户被禁用，删除redis中的登陆信息
				if (stat.intValue() == 2 ){
					String userKey = "oltu_"+user.getUsername()+"_token";
					if (StringUtils.isNotBlank(userKey)){
						String access_token = jedisService.get(userKey);
						if (StringUtils.isNotBlank(access_token)){
							jedisService.delete(access_token);
						}
						jedisService.delete(userKey);
					}

					//封号把用户加入提现黑名单
					user.setIsBlacklist(0);
					ruserService.update(user);

					if(jedisService.keyExists(BicycleConstants.TOP_POINT_USER_ID)) {
						//从缓存中清除首页金豆榜的用户id
						jedisService.deleteSortedSetItemFromShard(BicycleConstants.TOP_POINT_USER_ID, String.valueOf(uid));
					}

					if(jedisService.keyExists(BicycleConstants.SEARCH_RECOMMEND_USER_ID)) {
						//从缓存中清除搜索页推荐用户ID
						jedisService.deleteSortedSetItemFromShard(BicycleConstants.SEARCH_RECOMMEND_USER_ID, String.valueOf(uid));
					}

					if(jedisService.keyExists(BicycleConstants.VOICE_RECOMMEND_USER_ID)) {
						//从缓存中清除新歌声专区推荐用户ID
						jedisService.deleteSortedSetItemFromShard(BicycleConstants.VOICE_RECOMMEND_USER_ID, String.valueOf(uid));
					}
				}
			}

			if(stat.intValue() == 2){
				liveCheckLogService.check(U.getUid(), "封号", reason, 0L, uid,"close");
			}

		}
		
		return this.respBodyWriter.toSuccess();
	}
	
	/**
	 * 修改用户vip等级，蓝V、黄V、绿V
	 * @param uid
	 * @param stat
	 * @return
	 */
	@RequestMapping("chgvipstat")
	@ResponseBody
	public RespBody changeVipStat(Long uid,Integer stat){
		Ruser user = ruserService.find(uid);
		if(user.getVipStat().intValue()==1){
			jedisService.deleteSetItemFromShard(BicycleConstants.VIP_OF_BLUE, uid.toString());
		}else if(user.getVipStat().intValue()==2){
			jedisService.deleteSetItemFromShard(BicycleConstants.VIP_OF_YELLOW, uid.toString());
		}else if(user.getVipStat().intValue()==3){
			jedisService.deleteSetItemFromShard(BicycleConstants.VIP_OF_GREEN, uid.toString());
		}
		user.setVipStat(stat);
		user.setModifyDate(new Date());
		
		ruserService.update(user);
		
		jedisService.saveAsMap(BicycleConstants.USER_INFO+user.getId(), user);
		if(user.getType().equals("majia")){
			jedisService.setValueToMap(BicycleConstants.MAJIA + user.getId(), "vstat", String.valueOf(user.getVipStat()));
		}
		if(stat.intValue() == 1){
			jedisService.setValueToSetInShard(BicycleConstants.VIP_OF_BLUE, uid.toString());
		} else if(stat.intValue() == 2){
			jedisService.setValueToSetInShard(BicycleConstants.VIP_OF_YELLOW, uid.toString());
		} else if(stat.intValue() == 3){
			jedisService.setValueToSetInShard(BicycleConstants.VIP_OF_GREEN, uid.toString());
		}

		try {
			if (user.getVipStat() > 0 && stat == 0) {
				// 由vip用户改为普通用户
				Set<String> vipIds = jedisService.getSetFromShard(BicycleConstants.NEW_VIP_AUTO_FOCUS);
				for (String string : vipIds) {
					if (string.startsWith(String.valueOf(uid))) {
						jedisService.deleteSetItemFromShard(BicycleConstants.NEW_VIP_AUTO_FOCUS, string);
					}
				}
			}
			// insert cache
			if (stat == 1 || stat == 2 || stat == 3) {
				jedisService.setValueToSetInShard(BicycleConstants.NEW_VIP_AUTO_FOCUS,
						String.valueOf(uid) + "-" + System.currentTimeMillis());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return this.respBodyWriter.toSuccess();
	}
	
	/**
	 * 修改vip权重
	 * @param uid
	 * @param vipWeight
	 * @return
	 */
	@RequestMapping("chgVipWeight")
	@ResponseBody
	public RespBody chgVipWeight(Long uid,Integer vipWeight){
		Ruser user = ruserService.find(uid);
		user.setVipWeight(vipWeight);
		user.setModifyDate(new Date());
		
		ruserService.update(user);
		
		jedisService.saveAsMap(BicycleConstants.USER_INFO+user.getId(), user);
		
		return this.respBodyWriter.toSuccess();
	}
	/**
	 * 显示注册用户数，大屏独立页面，无需登录
	 * @return
	 */
	@RequestMapping("showregist")
	public String showRegist(){
		return "user/registUserNum";
	}
	
	/**
	 * 查询累计注册用户数
	 * @return
	 */
	@RequestMapping("registcount")
	@ResponseBody
	public Map<String,Object> registNum(){
		Map<String,Object> rmap = new HashMap<String,Object>();
		DecimalFormat def = new DecimalFormat("###,###,###");
		DateFormat daf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		Long count = ruserService.registNum();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,   -4);
		String fiveDays = new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());
		List<Map<String,Object>> dayUsers = ruserService.lastXDayUserNum(fiveDays);
		rmap.put("total", def.format(count));
		rmap.put("date", daf.format(new Date()));
		rmap.put("dayUserNum", dayUsers);
		
		OperatePlan currentWeek = operatePlanService.findCurrentPlan("week");
		OperatePlan currentMonth = operatePlanService.findCurrentPlan("month");
		
		if(currentWeek != null){
			rmap.put("currentWeek", currentWeek);
		}
		
		if(currentMonth != null){
			rmap.put("currentMonth", currentMonth);
		}
		
		return rmap;
	}
	
	@RequestMapping("getNormalUsers")
	@ResponseBody
	public Map searchNormalUser(Integer page, Integer rows,@RequestParam(value = "name", required = false) String name,
				@RequestParam(value = "username", required = false) String username,
				@RequestParam(value = "phone", required = false) String phone,
				@RequestParam(value = "id", required = false) Long id){
		
		if(page==0){
    		page=1;
    	}
    	Map<String,Object> params = new HashMap<String,Object>();
        params.put("pageStart", (page-1)*rows);
        params.put("pageSize", rows);
		if(id !=null && id > 0){
			params.put("id", id);
		}
        if(StringUtils.isNotBlank(name)){
        	params.put("name", name);
        }
        if(StringUtils.isNotBlank(username)){
        	params.put("username", username);
        }
        if(StringUtils.isNotBlank(phone)){
        	params.put("phone", phone);
        }
        
        params.put("type", "normal");
        
        Page pinfo = ruserService.searchNormalUserList(page, rows, params);
        
		Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", pinfo.getTotalElements());//total键 存放总记录数，必须的 
        jsonMap.put("rows", pinfo.getContent());//rows键 存放每页记录 list  
        
        return jsonMap;
	}



	/**
	 * 添加推荐用户
	 * @param userId 用户ID
	 * @param weight 排序权重
	 */
	@RequestMapping("insertRecommendUser")
	@ResponseBody
	public ResultData insertRecommendUser(Long userId,Integer type,Double weight){
		ResultData resultData = new ResultData();
		if(type == 1) {
			jedisService.setValueToSortedSetInShard(BicycleConstants.RECOMMEND_USER_ID, weight, String.valueOf(userId));
		} else if(type == 2) {
			jedisService.setValueToSortedSetInShard(BicycleConstants.SEARCH_RECOMMEND_USER_ID, weight, String.valueOf(userId));
		}else {
			jedisService.setValueToSortedSetInShard(BicycleConstants.VOICE_RECOMMEND_USER_ID, weight, String.valueOf(userId));
		}
		resultData.setResultCode("success");
		resultData.setResultMessage("添加成功！");
		return resultData;
	}

	/**
	 * 查询推荐用户
	 * @param queryPage 分页参数
	 */
	@RequestMapping("queryRecommendUser")
	@ResponseBody
	public Map<String,Object> queryRecommendUser(Integer type,@ModelAttribute("queryPage") JQueryPage queryPage){
		Map<String, Object> map = new HashMap<String, Object>();
		Set<Tuple> userIds = null;
		if (type == 1) {
			userIds = jedisService.zrevrangeWithScores(BicycleConstants.RECOMMEND_USER_ID, Long.valueOf((queryPage.getPage() - 1) * queryPage.getRows()), Long.valueOf(queryPage.getPage() * queryPage.getRows()) - 1);
		} else if(type == 2) {
			userIds = jedisService.zrevrangeWithScores(BicycleConstants.SEARCH_RECOMMEND_USER_ID, Long.valueOf((queryPage.getPage() - 1) * queryPage.getRows()), Long.valueOf(queryPage.getPage() * queryPage.getRows()) - 1);
		} else{
			userIds = jedisService.zrevrangeWithScores(BicycleConstants.VOICE_RECOMMEND_USER_ID, Long.valueOf((queryPage.getPage() - 1) * queryPage.getRows()), Long.valueOf(queryPage.getPage() * queryPage.getRows()) - 1);
		}

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (Tuple user : userIds) {
			Map<String, String> userInfo = jedisService.getMapByKey(BicycleConstants.USER_INFO + user.getElement());
			userInfo.put("weight", String.valueOf(user.getScore()));
			list.add(userInfo);
		}
		Long userTotal = 0L;
		if(type == 1){
			userTotal = jedisService.getSortedSetSizeFromShard(BicycleConstants.RECOMMEND_USER_ID);
		}else if(type == 2){
			userTotal = jedisService.getSortedSetSizeFromShard(BicycleConstants.SEARCH_RECOMMEND_USER_ID);
		}else{
			userTotal = jedisService.getSortedSetSizeFromShard(BicycleConstants.VOICE_RECOMMEND_USER_ID);
		}
		map.put("rows", list);
		map.put("total", userTotal);
		return map;
	}

	/**
	 * 删除推荐用户
	 * @param type 类型
	 * @param userId 用户ID
	 * @param weight 排序权重
	 */
	@RequestMapping("deleteRecommendUser")
	@ResponseBody
	public ResultData deleteRecommendUser(Integer type, Long userId,Double weight){
		ResultData resultData = new ResultData();
		if(type == 1){
			jedisService.deleteSortedSetItemFromShard(BicycleConstants.RECOMMEND_USER_ID, String.valueOf(weight), String.valueOf(userId));
		} else if(type == 2) {
			jedisService.deleteSortedSetItemFromShard(BicycleConstants.SEARCH_RECOMMEND_USER_ID, String.valueOf(weight), String.valueOf(userId));
		}else {
			jedisService.deleteSortedSetItemFromShard(BicycleConstants.VOICE_RECOMMEND_USER_ID, String.valueOf(weight), String.valueOf(userId));
		}
		resultData.setResultCode("success");
		resultData.setResultMessage("删除成功！");
		return resultData;
	}

	/**
	 * 设置推荐用户页面
	 */
	@RequestMapping("forwardRecommendUserList")
	public String list(){
		return "user/recommendUserList";
	}

	/**
	 * 用户直播详情
	 * @param
	 */
	@RequestMapping("forwardUserLiveDetail")
	public ModelAndView forwardUserLiveDetail(Long id,@RequestParam(value = "startDate", required = false) String startDate,
											  @RequestParam(value = "endDate", required = false) String endDate){
		ModelAndView mav = new ModelAndView();
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("id",id);
		params.put("startDate",startDate);
		params.put("endDate",endDate);
		AnchorDetailDisplay anchorDetail = anchorService.queryAnchorLiveDetail(params);
		anchorDetail.setGiftNumber(anchorDetail.getGiftNumber()==null?0:anchorDetail.getGiftNumber());
		anchorDetail.setPointNumber(anchorDetail.getPointNumber()==null?0:anchorDetail.getPointNumber());
		anchorDetail.setTotalDuration(anchorDetail.getDuration()==null?"0": CommonUtils.formatDuring(anchorDetail.getDuration(),1));
		anchorDetail.setHourDuration(anchorDetail.getDuration()==null?"0": CommonUtils.formatDuring(anchorDetail.getDuration(),2));
		mav.addObject("anchorDetail",anchorDetail);
		mav.addObject("startDate",startDate);
		mav.addObject("endDate",endDate);
		mav.setViewName("user/queryUserLiveDetail");
		return mav;
	}

	@RequestMapping("queryAnchorLiveDetailRecord")
	@ResponseBody
	@EnablePaging
	public Map<String, Object> queryAnchorLiveDetailRecord(Long id,@ModelAttribute("queryPage") JQueryPage queryPage) {
		Map<String,Object> params = new HashMap<String, Object>(1);
		params.put("id",id);
		List<AnchorDetailDisplay> list = anchorService.queryAnchorLiveDetailRecord(params);

		for(AnchorDetailDisplay anchorDetail : list){
			anchorDetail.setGiftNumber(anchorDetail.getGiftNumber()==null?0:anchorDetail.getGiftNumber());
			anchorDetail.setPointNumber(anchorDetail.getPointNumber()==null?0:anchorDetail.getPointNumber());
			anchorDetail.setMaxAccessNumber(anchorDetail.getMaxAccessNumber()==null?0:anchorDetail.getMaxAccessNumber());
		}
		com.busap.vcs.util.page.Page page = PagingContextHolder.getPage();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("total", page.getTotalResult());
		resultMap.put("rows", list);
		return resultMap;
	}

/*	public static void main(String[] args) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。
		String hms = formatter.format(326372658);
		System.out.println(hms);
	}*/
}
