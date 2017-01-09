package com.busap.vcs.web;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.service.DeviceInfoService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;

@Controller
@RequestMapping("/deviceInfo")
public class DeviceInfoController {

	private Logger logger = LoggerFactory.getLogger(DeviceInfoController.class);

	@Autowired
	protected HttpServletRequest request;

	@Resource(name = "respBodyBuilder")
	private RespBodyBuilder respBodyWriter = new RespBodyBuilder();
	
	@Resource(name="jedisService")
	private JedisService jedisService;

	@Resource(name = "deviceInfoService")
	private DeviceInfoService deviceInfoService;

	// 添加设备信息
	@RequestMapping("/addDeviceInfo")
	@ResponseBody
	@Transactional
	public RespBody addDeviceInfo(String uniqueMark,
			@RequestParam(required = false) String platform,
			@RequestParam(required = false) String osVersion,
			@RequestParam(required = false) String operator,
			@RequestParam(required = false) String model,
			@RequestParam(required = false) String factory,
			@RequestParam(required = false) String imei,
			@RequestParam(required = false) String mac,
			@RequestParam(required = false) String iccid,
			@RequestParam(required = false) String meid,
			@RequestParam(required = false) String pseudoUniqeId,
			@RequestParam(required = false) String androidId,
			@RequestParam(required = false) String wlanMac,
			@RequestParam(required = false) String btMac,
			@RequestParam(required = false) String cpu,
			@RequestParam(required = false) String memory,
			@RequestParam(required = false) String screenResolution,
			@RequestParam(required = false) String pixel) {
		logger.info(
				"uniqueMark={},platform={},model={},osVersion={},addDeviceInfo",
				uniqueMark, platform, model, osVersion);

		if (uniqueMark != null && !"".equals(uniqueMark)) { // 如果设备唯一标识为空，不返回我拍唯一标识
			String uuid = deviceInfoService.selectUuidByUniqueMark(uniqueMark);
			if (uuid == null || "".equals(uuid)) { // 如果某设备不存在对应的我拍唯一标识，生成唯一标识并返回
				uuid = UUID.randomUUID().toString();
				int result = deviceInfoService.insert(uuid, uniqueMark, null,
						platform, osVersion, operator, model, factory, imei,
						mac, iccid, meid, pseudoUniqeId, androidId, wlanMac,
						btMac, cpu, memory, screenResolution, pixel);

				if (result > 0) {
					return respBodyWriter.toSuccess(uuid);
				}
			} else { // 如果某设备已经存在对应的我拍唯一标识，直接返回该标识
				return respBodyWriter.toSuccess(uuid);
			}
		}

		return respBodyWriter.toError(ResponseCode.CODE_203.toString(),
				ResponseCode.CODE_203.toCode());

	}

	// 绑定用户id和设备唯一标识
	@RequestMapping("/bindUidAndUuid")
	@ResponseBody
	@Transactional
	public RespBody bindUidAndUuid(String uuid) {
		String uid = this.request.getHeader("uid");
		logger.info("uid={},uuid={},bindUidAndUuid", uid, uuid);
		int result = deviceInfoService
				.bindUidAndUuid(Long.parseLong(uid), uuid);
		if (result > 0) {
			return respBodyWriter.toSuccess(result);
		}
		return respBodyWriter.toError(ResponseCode.CODE_204.toString(),
				ResponseCode.CODE_204.toCode());
	}
	
	// 获得设备配置信息
	@RequestMapping("/getDeviceConfig")
	@ResponseBody
	@Transactional
	public RespBody getDeviceConfig() {
		Map<String,String> map = new HashMap<String,String>();
		map.put("version", jedisService.get(BicycleConstants.DEVICE_CONFIG_VERSION));
		map.put("url", jedisService.get(BicycleConstants.DEVICE_CONFIG_URL));
		return respBodyWriter.toSuccess(map);
	}

}
