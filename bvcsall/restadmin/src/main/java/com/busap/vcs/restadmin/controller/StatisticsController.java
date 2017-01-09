package com.busap.vcs.restadmin.controller;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.busap.vcs.data.vo.StatisticsVO;
import com.busap.vcs.service.DispatcherService;
import com.busap.vcs.service.bean.ClientInfo;
import com.busap.vcs.service.bean.ResponseEntity;
import com.busap.vcs.util.page.JQueryPage;

@Controller
@RequestMapping("logsta")
public class StatisticsController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(StatisticsController.class);

	@Autowired
	private DispatcherService dispatcherService;

	@Value("${statistics_url}")
	private String url;

	@RequestMapping("newuser")
	public String getNewUser() {
		return "logsta/newusers";
	}

	@RequestMapping("keepleave")
	public String getKeepLeave() {
		return "logsta/keepleaves";
	}

	@RequestMapping("activityusers")
	public String getActivityUser() {
		return "logsta/activityusers";
	}

	@RequestMapping("live")
	public String getLive() {
		return "logsta/live";
	}

	@RequestMapping("recharge")
	public String getRecharge() {
		return "logsta/recharges";
	}

	@RequestMapping("give")
	public String getGive() {
		return "logsta/gives";
	}

	@RequestMapping("userday")
	public String getUserday() {
		return "logsta/userday";
	}

	@RequestMapping("anchorday")
	public String getAnchorday() {
		return "logsta/anchorday";
	}

	@RequestMapping("revenue")
	public String getRevenue() {
		return "logsta/revenues";
	}

	@RequestMapping("rank")
	public String getRank() {
		return "logsta/ranks";
	}

	@RequestMapping("autoLog")
	public String getAutoLog(HttpServletRequest request,
			@RequestParam(value = "tableId", required = true) String tableId) {
		request.setAttribute("tableId", tableId);
		return "logsta/autolog";
	}

	/**
	 * auto table
	 * 
	 * @param tableId
	 * @return
	 */
	@RequestMapping("autoLogList")
	@ResponseBody
	public Object getAutoLogList(
			@RequestParam(value = "tableId", required = true) String tableId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("tableId", tableId);
		ClientInfo clientInfo = new ClientInfo(RequestType.GET.value,
				"auto/dealTable", url);
		clientInfo.setParams(params);
		JSONObject obj = null;
		try {
			ResponseEntity<String> entity = dispatcherService
					.dispatcher(clientInfo);
			obj = JSONObject.parseObject(entity.getResult());
		} catch (URISyntaxException e) {
			LOGGER.error("dispatcher error: ", e);
		}
		LOGGER.info("return : {}", obj);
		return obj;
	}

	/**
	 * auto table data
	 * 
	 * @param tableId
	 * @param body
	 * @return
	 */
	@RequestMapping("getAutoData")
	@ResponseBody
	public Object getAutoData(
			@RequestParam(value = "tableId", required = true) String tableId,
			@RequestParam(value = "body", required = false) String body) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("tableId", tableId);
		if (StringUtils.isNotBlank(body)) {
			JSONObject obj = JSONObject.parseObject(body);
			Set<String> keySet = obj.keySet();
			for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
				String key = it.next();
				params.put(key, obj.get(key).toString());
			}
		}
		ClientInfo clientInfo = new ClientInfo(RequestType.GET.value,
				"auto/getData", url);
		clientInfo.setParams(params);
		JSONObject obj = null;
		try {
			ResponseEntity<String> entity = dispatcherService
					.dispatcher(clientInfo);
			obj = JSONObject.parseObject(entity.getResult());
		} catch (URISyntaxException e) {
			LOGGER.error("dispatcher error: ", e);
		}
		LOGGER.info("return : {}", obj);
		return obj;

	}

	/**
	 * 获取统计信息
	 * 
	 * @param queryPage
	 * @param statisticsVO
	 * @param functionId
	 * @return
	 */
	@RequestMapping("queryStaList")
	@ResponseBody
	public Object queryStaList(
			@ModelAttribute("queryPage") JQueryPage queryPage,
			StatisticsVO statisticsVO,
			@RequestParam(value = "functionId", required = true) int functionId) {
		Map<String, String> params = getParams(queryPage, statisticsVO);
		ClientInfo clientInfo = new ClientInfo(RequestType.GET.value,
				FunctionType.getUrl(functionId)[0], url);
		clientInfo.setParams(params);
		JSONObject obj = null;
		try {
			ResponseEntity<String> entity = dispatcherService
					.dispatcher(clientInfo);
			obj = JSONObject.parseObject(entity.getResult());
		} catch (URISyntaxException e) {
			LOGGER.error("dispatcher error: ", e);
		}
		LOGGER.info("return : {}", obj);
		return obj;
	}

	/**
	 * 下拉框处理
	 * 
	 * @param selected
	 * @param param
	 * @param functionId
	 * @return
	 */
	@RequestMapping("querySelect")
	@ResponseBody
	public Map<String, Object> querySelect(
			@RequestParam(value = "selected", required = false) String selected,
			@RequestParam(value = "param", required = true) String param,
			@RequestParam(value = "functionId", required = true) int functionId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("selected", selected);
		params.put("param", param);
		ClientInfo clientInfo = new ClientInfo(RequestType.GET.value,
				FunctionType.getUrl(functionId)[1], url);
		clientInfo.setParams(params);
		ResponseEntity<String> entity;
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			entity = dispatcherService.dispatcher(clientInfo);
			JSONArray arr = JSONArray.parseArray(entity.getResult());
			map.put("rows", arr);
		} catch (URISyntaxException e) {
			LOGGER.error("dispatcher error: ", e);
		}
		return map;
	}

	private Map<String, String> getParams(JQueryPage queryPage,
			StatisticsVO statisticsVO) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("platform", statisticsVO.getPlatform());
		params.put("channel", statisticsVO.getChannel());
		params.put("startDate", statisticsVO.getStartTime());
		params.put("endDate", statisticsVO.getEndTime());
		params.put("sex", statisticsVO.getSex());
		if (queryPage.getRows() != null) {
			params.put("rows", queryPage.getRows().toString());
		}
		if (queryPage.getPage() != null) {
			params.put("page", queryPage.getPage().toString());
		}
		return params;
	}

	static enum RequestType {
		GET("GET"), POST("POST");

		public String value;

		private RequestType(String value) {
			this.value = value;
		}
	}

	static enum FunctionType {
		NEW_USER(1, new String[] { "nuser/getData", "nuser/querySelect" }), KEEP_LEAVE(
				2,
				new String[] { "keepLeave/getData", "keepLeave/querySelect" }), ACTIVITY(
				3, new String[] { "live/getData", "live/querySelect" }), LIVELY(
				4, new String[] { "lively/getData", "lively/querySelect" }), RECHARGE(
				5, new String[] { "recharge/getData", "recharge/querySelect" }), GIVE(
				6, new String[] { "give/getData", "give/querySelect" }), ANCHORDAY(
				7, new String[] { "live/getAnchorData", "/" }), USERDAY(8,
				new String[] { "keepLeave/getDayData", "/" }), REVENUE(9,
				new String[] { "revenue/getData", "/" }), RANK(10,
				new String[] { "rank/getData", "/" });
		public int code;
		public String[] url;
		public String[] e = new String[] { "321" };

		private static Map<Integer, String[]> values = new HashMap<Integer, String[]>();

		static {
			for (FunctionType func : FunctionType.values()) {
				values.put(func.code, func.url);
			}
		}

		private FunctionType(int code, String[] url) {
			this.code = code;
			this.url = url;
		}

		public static String[] getUrl(int code) {
			return values.get(code);
		}
	}
}
