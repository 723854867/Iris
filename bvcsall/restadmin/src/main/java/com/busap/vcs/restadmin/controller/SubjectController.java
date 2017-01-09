//package com.busap.vcs.restadmin.controller;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.beanutils.BeanUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.busap.vcs.data.entity.BaseEntity;
//import com.busap.vcs.data.entity.Subject;
//import com.busap.vcs.service.BaseService;
//import com.busap.vcs.service.SubjectService;
//import com.busap.vcs.webcomn.RespBody;
//import com.busap.vcs.webcomn.controller.CRUDController;
//
///**
// * 暂时结合easyui写的增删查改的例子
// * @author meizhiwen
// *
// */
//@Controller()
//@RequestMapping("subject")
//public class SubjectController extends CRUDController<Subject, Long>{
//
//	private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);
//	
//	@Resource(name="subjectService")
//	private SubjectService subjectService;
//	
//	@Resource(name="subjectService")
//	@Override
//	public void setBaseService(BaseService<Subject, Long> baseService) {
//		this.baseService=baseService;
//	}
//	
//	@RequestMapping("subjectlist")
//	public String list(){
//		return "subject/list";
//	}
//	
//	@RequestMapping(value = {"/updatepage"}, method = {RequestMethod.POST, RequestMethod.PUT})
//    @ResponseBody
//    public RespBody updatepage(Subject entity,HttpServletRequest req) {
//    	if (!validator(entity, BaseEntity.Update.class)) {
//            return respBodyWriter.toError(entity);
//        }
//    	Subject dbEntity=this.subjectService.find(entity.getId());
//    	
//    	String[] ps=new String[]{"name","useStat"};
//    	try {
//	    	for (String s : ps) {
//				BeanUtils.setProperty(dbEntity, s, BeanUtils.getProperty(entity, s));;
//			}
//    	} catch (Exception e) {
//    		this.logger.error("copy properties error",e);
//    		return this.respBodyWriter.toError(e);
//    	}
//    	
//        baseService.update(dbEntity);
//        return respBodyWriter.toSuccess(dbEntity);
//    }
//}
