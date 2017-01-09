package com.busap.vcs.restadmin.interceptor;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.service.JedisService;
import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.busap.vcs.data.entity.Permission;
import com.busap.vcs.data.entity.Role;
import com.busap.vcs.data.entity.User;
import com.busap.vcs.service.PermissionService;
import com.busap.vcs.service.UserService;
import com.busap.vcs.webcomn.Constants;
import com.busap.vcs.webcomn.U;

public class MenuInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory
			.getLogger(MenuInterceptor.class);
	@Autowired
	private UserService userService;
	
	@Autowired
	private PermissionService permissionService;

	@Autowired
	private JedisService jedisService;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if(U.getAttribute(Constants.SESSION_UID)==null){
			return true;
		}
		
		if(U.getAttribute("menu")!=null){
			return true;
		}
		
		Map<Long, Permission> map = new HashMap<Long, Permission>();//二级菜单里id对应的一级菜单id是什么
		List<Permission> fpList=this.permissionService.findAll(0L);
		for (Permission pm : fpList) {
			List<Permission> subPmList=this.permissionService.findAll(pm.getId());
//			pm.setChildren(subPmList);
			for (Permission permission : subPmList) {
				map.put(permission.getId(), pm);
			}
		}
		
		User u=this.userService.find(U.getUid());
		List<Role> rList=u.getRoles();
		List<Permission> upList=new ArrayList<Permission>();//肯定包含所有的二级菜单id
		for (Role r : rList) {
			upList.addAll(r.getPermissions());
		}
		
		//find first level menu
		List<Permission> firstLeveMenuList=new ArrayList<Permission>();
		for (Permission permission : upList) {
			if(permission.getPid()==0){
				if(!firstLeveMenuList.contains(permission)){
					firstLeveMenuList.add(permission);
				}
			}else{
				Permission tmp=map.get(permission.getId());//取出一级菜单
				if(!firstLeveMenuList.contains(tmp)){
					firstLeveMenuList.add(tmp);//添加到一级菜单
				}else{
					tmp=this.findById(firstLeveMenuList, tmp.getId());//查找出一级菜单
				}
				List<Permission> subList=tmp.getChildren();//取出二级菜单
				//添加到二级菜单列表里
				if(subList==null){
					subList=new ArrayList<Permission>();
					subList.add(permission);
				}else{
					if(!subList.contains(permission)){
						subList.add(permission);
					}
				}
			}
		}
		
		//对二级菜单升序
		for (Permission fp : firstLeveMenuList) {
			List<Permission> subp=fp.getChildren();
			for (Permission p : subp) {
				String value=p.getValue();
				if(StringUtils.isNotBlank(value)){
//					if(value.indexOf("?")==-1){
//						value=value+"?pid="+p.getId();
//					}else{
//						value=value+"&pid="+p.getId();
//					}
					if(value.indexOf("?")==-1){
						value=value+"?menuId="+p.getId();
					}else if(value.indexOf("menuId")==-1){
						value=value+"&menuId="+p.getId();
					}
				}
				p.setValue(value);
			}
			fp.setChildren(this.sort(subp));
		}
		
		//对一级菜单升序
		Set<String> checkGroup = jedisService.getSortedSetFromShardByDesc(BicycleConstants.CHECK_GROUP);
		U.setAttribute("checkGroupName",StringUtils.join(checkGroup.toArray(), ","));
		U.setAttribute("menu", JSONArray.fromObject(this.sort(firstLeveMenuList)));
		return true;
	}
	
	private Permission findById(List<Permission> pList,Long id){
		for (Permission p : pList) {
			if(p.getId().longValue()==id.longValue()){
				return p;
			}
		}
		return null;
	}
	
	private List<Permission> sort(List<Permission> pl){
		Permission[] os=pl.toArray(new Permission[0]);
		Arrays.sort(os);
		return Arrays.asList(os);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
