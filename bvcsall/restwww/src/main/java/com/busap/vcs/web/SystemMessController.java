package com.busap.vcs.web;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.data.vo.SysmessVO;
import com.busap.vcs.service.SystemMessService;
//import com.busap.vcs.data.mapper.AttentionDAO;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;

@Controller
@RequestMapping("/sysmess")
public class SystemMessController {

	private Logger logger = LoggerFactory.getLogger(SystemMessController.class);

	@Autowired
	protected HttpServletRequest request;

	@Resource(name = "respBodyBuilder")
	private RespBodyBuilder respBodyWriter = new RespBodyBuilder();
	
	@Resource(name="systemMessService")
	private SystemMessService systemMessService;
	
	// 添加或取消关注
	@RequestMapping("/getSystemMess")
	@ResponseBody
	@Transactional
	public RespBody getSystemMess(Long timestamp, int count) {
		String uid =  this.request.getHeader("uid");
		logger.info("uid={}", uid);
		List<SysmessVO> list = systemMessService.searchAvailableSysmessByUid(Long.valueOf(uid), timestamp==null||timestamp.longValue()==0?null:new Date(timestamp), count);
		return respBodyWriter.toSuccess(list);
		
	}
}
