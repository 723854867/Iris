package com.busap.vcs.restadmin.controller;

import com.busap.vcs.data.entity.SchoolRegister;
import com.busap.vcs.service.SchoolRegisterService;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.PagingContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 校园大使后台查询controller
 * Created by Knight on 16/4/25.
 */
@Controller()
@RequestMapping("schoolRegister")
public class SchoolRegisterController {

    @Autowired
    private SchoolRegisterService schoolRegisterService;

    @RequestMapping("queryList")
    @ResponseBody
    @EnablePaging
    public Map<String,Object> queryList (Integer page, Integer rows,
                                         @RequestParam(value = "type", required = false) String type,
                                         @RequestParam(value = "param", required = false) String param) {
        if (page == null) {
            page = 1;
        }
        if (rows == null) {
            rows = 20;
        }
        int total = 0;
        List<SchoolRegister> list = new ArrayList<SchoolRegister>();
        if (StringUtils.isBlank(type) || StringUtils.isBlank(param)) {
            list = schoolRegisterService.getRegisterByPage(page, rows);
            total = schoolRegisterService.findAll().size();
        } else if ("name".equals(type)) {
            list = schoolRegisterService.getRegisterByName(param, page, rows);
            total = schoolRegisterService.getRegisterByNameTotal(param);
        } else if ("phone".equals(type)) {
            SchoolRegister schoolRegister = schoolRegisterService.getRegisterInfo(param);
            if (schoolRegister != null) {
                list.add(schoolRegister);
                total = list.size();
            }
        }
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("rows", list);
        return map;
    }

    @RequestMapping("list")
    public String getList() throws Exception {
        return "schoolRegister/list";
    }
}
