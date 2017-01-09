package com.busap.vcs.restadmin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.ehcache.constructs.web.PageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.base.Filter;
import com.busap.vcs.data.entity.LoadConfigUrl;
import com.busap.vcs.data.entity.Sign;
import com.busap.vcs.data.vo.ComplainVO;
import com.busap.vcs.data.vo.SignVO;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.SignService;
import com.busap.vcs.service.UserService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;

@Controller
@RequestMapping("sign")
public class SignController extends CRUDController<Sign,Long>{

		private static final Logger logger = LoggerFactory.getLogger(SignController.class);
		
		@Value("${files.localpath}")
		private String basePath;
		
		@Resource(name = "signService")
		private SignService signService;
		
		@Resource(name = "userService")
		private UserService userService;
		
		@Autowired
	    JedisService jedisService;
		
		@Resource(name="signService")
		@Override
		public void setBaseService(BaseService<Sign,Long> signService) {
			this.baseService=signService;
			
		}
		@RequestMapping("modify")
		public String updateSign(@ModelAttribute Sign sign){
			
			return "sign/list";
		}
	/**
	 * 进入sign页面
	 * @param sign
	 * @return
	 */
		@RequestMapping("loadpage")
		public String loadSignPage(@ModelAttribute Sign sign,HttpServletRequest request){
			SignVO sv=new SignVO();
			int beyongMaxNum = signService.findAllBeyondMaxUser();
			int allNum = signService.findAllSignUser();
			sv.setBeyongMaxNum(beyongMaxNum);
			sv.setLessMaxNum(allNum-beyongMaxNum);
			request.setAttribute("signvo", sv);
			return "sign/list";
		}

		@RequestMapping("signlist")
		@ResponseBody
	    public Map<String,Object> signListPage(Integer page, Integer rows,
											   @RequestParam(value = "startTime",required = false) String startTime,
											   @RequestParam(value = "endTime",required = false) String endTime,
											   @RequestParam(value = "startCount",required = false) Integer startCount,
											   @RequestParam(value = "endCount",required = false) Integer endCount) {
			if(page==null||page<=0){
				page=1;
			}
			if(rows==null||rows<=0){
				rows=20;
			}
			Sort sort=new Sort(Direction.DESC,"date_mark");
			Integer total = signService.findEveryDaySumSignCount(startTime,endTime,startCount,endCount);

			List<SignVO> lsv = signService.findEveryDaySumSign(page,rows,startTime,endTime,startCount,endCount);
			Page<SignVO> pinfo = new PageImpl<SignVO>(lsv,new PageRequest(page, rows, null),total);
	        Map<String, Object> jsonMap = new HashMap<String, Object>();
	        jsonMap.put("total", pinfo.getTotalElements());//total键 存放总记录数，必须的 
	        jsonMap.put("rows", pinfo.getContent());//rows键 存放每页记录 list  
	        return jsonMap;
	    }

		/**
		 * 添加
		 */
		@RequestMapping("loadsumuser")
		public RespBody loadSumUserSign(@ModelAttribute Sign sign,HttpServletRequest request){
			SignVO sv=new SignVO();
			int beyongMaxNum = signService.findAllBeyondMaxUser();
			int allNum = signService.findAllSignUser();
			sv.setBeyongMaxNum(beyongMaxNum);
			sv.setLessMaxNum(allNum-beyongMaxNum);
			return respBodyWriter.toSuccess(sv);
		}
		
		
		@RequestMapping("/getresult")
		@ResponseBody
		public Map getresult(){
			Sort sort=new Sort(Direction.DESC,"date_mark");
	        Page pinfo=(Page)this.listPage(1, 1000, new ArrayList(), sort).getResult();
			Map<String, Object> jsonMap = new HashMap<String, Object>();
	        jsonMap.put("total", pinfo.getTotalElements());//total键 存放总记录数，必须的 
	        jsonMap.put("rows", pinfo.getContent());//rows键 存放每页记录 list  
	        return jsonMap;
		}
		/**
		 * 根据id删除
		 * @param id
		 * @return
		 */
		@RequestMapping("delete")
		@ResponseBody
		public RespBody deleteSign(Long id){
			return this.respBodyWriter.toSuccess();
		}
		
		

}
