package com.busap.vcs.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.data.entity.VarietyHistory;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.VarietyHistoryService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;

@Controller
@RequestMapping("varietyHistory")
public class VarietyHistoryController extends CRUDController<VarietyHistory, Long> {
	@Resource(name = "varietyHistoryService")
	private VarietyHistoryService varietyHistoryService;
	
	
	@Override
	@Resource(name = "varietyHistoryService")
	public void setBaseService(BaseService<VarietyHistory, Long> baseService) {
		this.baseService = varietyHistoryService;
	}
	
	@RequestMapping("findVarietyHistory")
	@ResponseBody
	public RespBody findVarietyHistory(){
		List<VarietyHistory> result = varietyHistoryService.findAllVariety();
		
		return this.respBodyWriter.toSuccess(result);
	}

}
