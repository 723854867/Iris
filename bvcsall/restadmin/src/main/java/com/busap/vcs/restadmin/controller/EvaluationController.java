package com.busap.vcs.restadmin.controller;

/**
 * 评论管理
 * @author dmsong
 * @createdate 2015-1-14
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.data.entity.Evaluation;
import com.busap.vcs.data.repository.EvaluationRepository;
import com.busap.vcs.data.vo.EvaluationVO;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.EvaluationService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;
import com.busap.vcs.webcomn.controller.CRUDForm;

@Controller()
@RequestMapping("evaluation")
public class EvaluationController extends CRUDController<Evaluation, Long>{
	private static final Logger logger = LoggerFactory.getLogger(EvaluationController.class);
	
	@Resource(name = "evaluationService")
	private EvaluationService evaluationService;
	
	@Resource(name = "evaluationRepository")
	private EvaluationRepository evaluationRepository;
	
	@Resource(name = "ruserService")
	private RuserService ruserService;
	
	@Resource(name = "evaluationService")
	@Override
	public void setBaseService(BaseService<Evaluation, Long> baseService) {
		this.baseService = baseService;		
	}
	
	@RequestMapping("index")
	public String list(HttpServletRequest req){
		return "evaluation/list";
	}
	
	@RequestMapping("searchListPage")
	@ResponseBody
	public Map searchListPage(Integer page, Integer rows,
							  @RequestParam(value = "startTime",required = false) String startTime,
							  @RequestParam(value = "endTime",required = false) String endTime,
							  @RequestParam(value = "evaluationPersonType",required = false) Integer evaluationPersonType,
							  @RequestParam(value = "evaluationPerson",required = false) String evaluationPerson,
							  CRUDForm curdForm) {
		  // 从request中获取通用查询条件
    	if(page <= 0 || page == null){
    		page=1;
    	}
		if(rows <= 0 || rows == null){
			rows=50;
		}
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("pageStart", (page-1)*rows);
    	params.put("pageSize", rows);
		params.put("startTime",startTime);
		params.put("endTime",endTime);
		params.put("evaluationPersonType",evaluationPersonType);
		params.put("evaluationPerson",evaluationPerson);
    	Page pinfo = evaluationService.searchEvlauation(page,rows,params);
    	
    	List<EvaluationVO> evas = pinfo.getContent();
    	if(evas !=null && evas.size()>0){
	    	for(EvaluationVO eva:evas){
	    		eva.setContent(eva.getContent().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
	    	}
    	}
        
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", pinfo.getTotalElements());//total键 存放总记录数，必须的 
        jsonMap.put("rows", evas);//rows键 存放每页记录 list  
        return jsonMap;
	}
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	@RequestMapping("remove")
	@ResponseBody
	public String deleteEvaluation(Long id){
		evaluationService.deleteEvaluation(id);
		return "ok";
	}
	
	@RequestMapping("logicremove")
	@ResponseBody
	public String multiDelete(String ids){
		String id[] = ids.split(",");
		List<Long> idList = new ArrayList<Long>();
		if(id.length > 0){
			for(String evaId:id){
				idList.add(Long.parseLong(evaId));
			}
		}
		evaluationService.deleteEvaluation(idList);
		return "ok";
	}
	
	@RequestMapping("saveEvaluation")
	@ResponseBody
	public RespBody saveEvaluation(Long videoId,String content,Long majia){
		Evaluation eva = new Evaluation();
		eva.setVideoId(videoId);
		eva.setContent(content);
		eva.setCreatorId(majia);
		eva.setAdminId(U.getUid());
		eva.setCreateDate(new Date());
		
		evaluationService.saveEvaluation(eva);
		
		return this.respBodyWriter.toSuccess();
	}

}
