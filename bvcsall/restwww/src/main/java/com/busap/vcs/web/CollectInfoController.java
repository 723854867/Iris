package com.busap.vcs.web;

import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.service.CollectInfoService;
import com.busap.vcs.web.live.RoomController;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 信息采集
 * Created by Knight on 16/5/5.
 */
@Controller
@RequestMapping("/collectInfo")
public class CollectInfoController {
	
	private Logger logger = LoggerFactory.getLogger(CollectInfoController.class);
    @Autowired
    protected  HttpServletRequest request;

    @Autowired
    private CollectInfoService collectInfoService;

    @Resource(name = "respBodyBuilder")
    private RespBodyBuilder respBodyWriter = new RespBodyBuilder();

    @RequestMapping("/save")
    @ResponseBody
    public RespBody saveHandler(Long roomId, Long firstDelayTime, Integer blockTimes, String type, String publishHost, String piliHost, String clientType, String errorCode) {
        String uid = this.request.getHeader("uid");
        if (StringUtils.isNotBlank(uid))  {
        	String ip = request.getHeader("x-forwarded-for");
    		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
    			ip = request.getHeader("Proxy-Client-IP");
    		}
    		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
    			ip = request.getHeader("WL-Proxy-Client-IP");
    		}
    		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
    			ip = request.getRemoteAddr();
    		}
    		
    		if (StringUtils.isNotBlank(ip)) {
				String[] ss = ip.split(",");
				if (ss.length > 1) {
					ip = ss[0];
				}
			}
    		
    		logger.info("************************************************************************************"+ip);
    		
            if (collectInfoService.saveInfo(Long.parseLong(uid), roomId, firstDelayTime, blockTimes, type,ip, publishHost, piliHost,clientType,errorCode)) {
                return respBodyWriter.toSuccess();
            }
        }
        return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_312.toCode());
    }
}
