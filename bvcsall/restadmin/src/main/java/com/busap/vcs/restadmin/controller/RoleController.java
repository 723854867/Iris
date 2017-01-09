package com.busap.vcs.restadmin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.User;
import com.busap.vcs.data.entity.UserRole;
import com.busap.vcs.service.*;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.Page;
import com.busap.vcs.util.page.PagingContextHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.base.Filter;
import com.busap.vcs.data.entity.Permission;
import com.busap.vcs.data.entity.Role;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;

/**
 * 暂时结合easyui写的增删查改的例子
 *
 * @author meizhiwen
 */
@Controller()
@RequestMapping("role")
public class RoleController extends CRUDController<Role, Long> {

    @Resource
    private UserRoleService userRoleService;

    @Override
    public RespBody get(Long id) {
        List pList = this.roleService.findPidByRoleId(id);
        return this.respBodyWriter.toSuccess(pList);
    }

    @Override
    public RespBody create(Role entity) {
        String pids = this.request.getParameter("permissionId");
        List<Long> strList = new ArrayList<Long>();

        String[] ps = StringUtils.split(pids, ",");
        for (String pid : ps) {
            strList.add(Long.parseLong(pid));
        }
        List<Permission> pList = this.permissionService.findAll(strList.toArray(new Long[0]));
        entity.setPermissions(pList);
        this.roleService.save(entity);
        return this.respBodyWriter.toSuccess();
    }

    @Autowired
    private PermissionService permissionService;

    @Override
    public RespBody update(Role entity) {
        Role dbEntity = this.roleService.find(entity.getId());
        dbEntity.setName(entity.getName());
        dbEntity.setDescription(entity.getDescription());

        String pids = this.request.getParameter("permissionId");
        List<Long> strList = new ArrayList<Long>();

        String[] ps = StringUtils.split(pids, ",");
        for (String pid : ps) {
            strList.add(Long.parseLong(pid));
        }
        List<Permission> pList = this.permissionService.findAll(strList.toArray(new Long[0]));
        dbEntity.setPermissions(pList);

        this.roleService.update(dbEntity);
        return this.respBodyWriter.toSuccess();
    }

    @Resource(name = "roleService")
    private RoleService roleService;

    @Resource(name = "roleService")
    @Override
    public void setBaseService(BaseService<Role, Long> baseService) {
        this.baseService = baseService;
    }

    @RequestMapping("rolelist")
    public String list() {
        return "role/list";
    }

    @RequestMapping("permissionListAjax")
    @ResponseBody
    public List permissionList() throws Exception {
        List<Filter> fl = new ArrayList<Filter>();
        fl.add(Filter.eq("pid", 0));
        List<Permission> pList = this.permissionService.findAll(0L);
        for (Permission permission : pList) {
            this.setPermiTextByName(permission);
            List<Permission> subList = this.permissionService.findAll(permission.getId());
            for (Permission permission2 : subList) {
                this.setPermiTextByName(permission2);
            }
            permission.setChildren(subList);
        }
        return pList;
    }

    private void setPermiTextByName(Permission pm) {
        pm.setText(pm.getName());
    }

    @RequestMapping("queryRoleList")
    @ResponseBody
    @EnablePaging
    public Map<String, Object> queryRoleList(@ModelAttribute("queryPage") JQueryPage queryPage) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        List<Role> list = roleService.queryRoles(params);
        Page page = PagingContextHolder.getPage();
        map.put("rows", list);
        map.put("total", page.getTotalResult());
        return map;
    }

    @RequestMapping("deleteRole")
    @ResponseBody
    public Map<String,Object> deleteRole(Long id){
        Map<String,Object> map = new HashMap<String, Object>();
        UserRole userRole = new UserRole();
        userRole.setRoles(id);
        List<UserRole> userRoleList = userRoleService.queryUserRoles(userRole);
        if(userRoleList.isEmpty()){
            Role role = new Role();
            role.setId(id);
            role.setStatus(1L);
            int result = roleService.updateRole(role);
            if(result>0){
                map.put("resultCode","ok");
                map.put("resultMessage","删除成功！");
            }else{
                map.put("resultCode","error");
                map.put("resultMessage","删除失败！");
            }
        }else{
            map.put("resultCode","error");
            map.put("resultMessage","删除失败，此管理组下有管理员存在！要删除用户组，必须先删除此管理组下的管理员！");
        }
        return map;
    }

}
