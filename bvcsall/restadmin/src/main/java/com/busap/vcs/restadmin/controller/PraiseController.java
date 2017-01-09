package com.busap.vcs.restadmin.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.data.entity.Praise;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.PraiseService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;

@Controller()
@RequestMapping("praise")
public class PraiseController extends CRUDController<Praise, Long>{

	@Resource(name = "praiseService")
	private PraiseService praiseService;
	
	@Override
	public void setBaseService(BaseService<Praise, Long> baseService) {
		this.baseService = baseService;
	}
	
	@RequestMapping("createPraise")
	@ResponseBody
	public RespBody createPraise(Long videoId,String majia){
		if(videoId != null && StringUtils.isNotBlank(majia)){
			String ids[] = majia.split(",");
			praiseService.savePraise(videoId, ids, U.getUid());
		}
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("delmjPraise")
	@ResponseBody
	public RespBody deletePraise(Long videoId,Long majia){
		praiseService.deletePraise(videoId, majia);
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("mjpraise")
	@ResponseBody
	public List<Praise> searchPraiseByMJ(Long videoId){
		return praiseService.searchByMajia(videoId);
	}

}
