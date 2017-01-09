package com.busap.vcs.restadmin.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Tuple;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;
import com.busap.vcs.webcomn.controller.CRUDForm;

@Controller()
@RequestMapping("autoAttention")
public class AutoAttentionController{
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Resource(name = "ruserService")
	private RuserService ruserService;
	
	@Resource(name = "respBodyBuilder")
	private RespBodyBuilder respBodyWriter = new RespBodyBuilder();
	
	@RequestMapping("list")
	public String list(){
		return "autoAttention/list";
	}
	
	@RequestMapping("new")
	public String newMess(){
		return "autoAttention/create";
	}
	
	
	@RequestMapping("searchListPage")
	@ResponseBody
	public Map searchListPage(Integer page, Integer rows, CRUDForm curdForm) {
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Set<Tuple> set = jedisService.getSortedSetByScoreWithScores(BicycleConstants.AUTO_ATTENTION_UID,System.currentTimeMillis(), 2453089822018l);
		for (Tuple tuple:set) {
			String uid = tuple.getElement();
			Long score = new Double(tuple.getScore()).longValue();
			Ruser ruser = ruserService.find(Long.parseLong(uid));
			if (ruser != null) {
				Map<String,String> map = new HashMap<String,String>();
				map.put("userId", String.valueOf(ruser.getId()));
				map.put("name", ruser.getName());
				map.put("vipStat", String.valueOf(ruser.getVipStat()));
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				map.put("endTime", format.format(new Date(score)));
				list.add(map);
			}
		}
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", list.size());//total键 存放总记录数，必须的 
        jsonMap.put("rows", list);//rows键 存放每页记录 list  
        return jsonMap;
	}
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	@RequestMapping("remove")
	@ResponseBody
	public RespBody remove(Long userId){
		jedisService.deleteSortedSetItemFromShard(BicycleConstants.AUTO_ATTENTION_UID, String.valueOf(userId));
		jedisService.delete(BicycleConstants.ATTENTION_MAJIA_ID_OF_+userId);
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("add")
	public String add(String targetId,Integer days){
		try {
			Set<String> set = jedisService.getSortedSetByScore(BicycleConstants.AUTO_ATTENTION_UID,System.currentTimeMillis(), 2453089822018l);
			if (set.size() <20){
				Long score = System.currentTimeMillis();
				score = score+days*24*60*60*1000;
				jedisService.setValueToSortedSetInShard(BicycleConstants.AUTO_ATTENTION_UID, Double.parseDouble(String.valueOf(score)), targetId);
				//获得马甲用户id
				Set<String> majiaUidSet = jedisService.getSetFromShard(BicycleConstants.MAJIA_UID);
				//获得关注该用户的马甲id
				Set<String> fansIdSet = jedisService.getSetFromShard(BicycleConstants.MAJIA_FANS_ID_OF_+targetId);
				for (String fansId: fansIdSet) {
					majiaUidSet.remove(fansId);//去除马甲用户中关注过该用户的id
				}
				jedisService.delete(BicycleConstants.ATTENTION_MAJIA_ID_OF_+targetId);
				jedisService.setValueToSetInShard(BicycleConstants.ATTENTION_MAJIA_ID_OF_+targetId, majiaUidSet);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "autoAttention/list";
	}

}
