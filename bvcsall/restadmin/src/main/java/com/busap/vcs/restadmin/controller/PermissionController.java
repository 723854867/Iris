package com.busap.vcs.restadmin.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.data.entity.Permission;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.PermissionService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;
import com.busap.vcs.webcomn.controller.CRUDForm;

/**
 * 暂时结合easyui写的增删查改的例子
 * @author meizhiwen
 *
 */
@Controller()
@RequestMapping("permission")
public class PermissionController extends CRUDController<Permission, Long>{

	@Override
	public RespBody update(Permission entity) {
		Permission dbEntity=this.permissionService.find(entity.getId());
		dbEntity.setName(entity.getName());
		dbEntity.setPid(entity.getPid());
		dbEntity.setValue(entity.getValue());
		dbEntity.setSort(entity.getSort());
		this.permissionService.update(dbEntity);
		return this.respBodyWriter.toSuccess();
	}

	@Override
	public RespBody delete(Long id){
		List<Permission> fList=this.permissionService.findAll(id);
		if(fList != null && fList.size()>0)
			return this.respBodyWriter.toError("存在子菜单，不能删除！");
		
		this.permissionService.delete(id);
		
		return this.respBodyWriter.toSuccess();
	}

	private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);
	
	@Resource(name="permissionService")
	private PermissionService permissionService;
	
	@Resource(name="permissionService")
	@Override
	public void setBaseService(BaseService<Permission, Long> baseService) {
		this.baseService=baseService;
	}
	
	@RequestMapping("permissionlist")
	public String list(){
		return "permission/permissionList";
	}


//	@Override
//	public RespBody create(Permission entity) {
////		Integer sort=this.permissionService.getMaxSortByPid(entity.getPid());
////		sort=(sort==null?0:sort);
////		entity.setSort(sort+1);
//		this.permissionService.save(entity);
//		return this.respBodyWriter.toSuccess(entity);
//	}

//	@RequestMapping("getMaxSort")
//	@ResponseBody
//	public RespBody getMaxSort(){
//		Integer sort=this.permissionService.getMaxSortByPid(0L);
//		sort=(sort==null?0:sort);
//		return this.respBodyWriter.toSuccess(sort+1);
//	}

	@Override
	public Map listpage(Integer page, Integer rows, CRUDForm curdForm) {
		Map map = new HashMap();
		List<Permission> fList=this.permissionService.findAll(0L);
		for (Permission fw : fList) {
			fw.setChildren(this.permissionService.findAll(fw.getId()));
		}
		map.put("rows", fList);
		map.put("total",this.permissionService.count());
		return map;
	}
	
	@RequestMapping("saveAjax")
	@ResponseBody
	public RespBody save(Permission entity) {
		Permission dbEntity=this.permissionService.find(entity.getId());
		dbEntity.setName(entity.getName());
		dbEntity.setValue(entity.getValue());
		this.permissionService.update(dbEntity);
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("saveMenuId")
	@ResponseBody
	public String saveMenuId(
			HttpServletRequest req,
			String pid,
			String menuId){
		
		req.getSession().setAttribute("pid", pid);
//		req.getSession().setAttribute("pid", 6);
		req.getSession().setAttribute("menuId", menuId);
		
		return "success";
	}
}
