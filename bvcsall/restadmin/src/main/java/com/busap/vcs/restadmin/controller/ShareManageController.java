package com.busap.vcs.restadmin.controller;

import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.data.entity.ShareManage;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.ShareManageService;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.webcomn.controller.CRUDController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分享文案、图片管理
 * Created by Knight on 15/11/16.
 */
@Controller()
@RequestMapping("shareManage")
public class ShareManageController extends CRUDController<ShareManage, Long> {

    @Resource(name = "shareManageService")
    private ShareManageService shareManageService;

    @Override
    public void setBaseService(BaseService<ShareManage, Long> baseService) {
        this.baseService = baseService;
    }

    /**
     * 查询列表
     * @return 分享列表
     */
    @RequestMapping("shareManageList")
    public String shareManageList() throws Exception {
        return "shareManage/shareManageList";
    }

    @RequestMapping(value = "shareManageListJson")
    @ResponseBody
    @EnablePaging
    public Map<String,Object> shareManageListJson(Integer page, Integer rows) throws Exception{

        if (page == null) {
            page = 1;
        }
        if (rows == null) {
            rows = 20;
        }

        StringBuffer hql = new StringBuffer();
        StringBuffer countHql = new StringBuffer();
        List<ParameterBean> paramList = new ArrayList<ParameterBean>();

        hql.append("FROM ShareManage share ");
        countHql.append("SELECT COUNT(*) FROM ShareManage share ");

        List<OrderByBean> orderByList=new ArrayList<OrderByBean>();

        OrderByBean orderByObject1=new OrderByBean("id", 0, "share");
        orderByList.add(orderByObject1);

        List shareList = shareManageService.getObjectByJpql(hql, page, rows, "share", paramList, null, orderByList);
        Long totalCount = shareManageService.getObjectCountByJpql(countHql, paramList);

        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("total",totalCount);
        map.put("rows",shareList);
        return map;
    }

    /**
     * 新建分享文案
     */
    @RequestMapping("addShareManage")
    public String addShareManage() {
        return "shareManage/addShareManage";
    }

    /**
     * 保存分享文案
     */
    @RequestMapping("saveShareManage")
    public String saveShareManage(String shareId, String shareType, String shareImg, String shareText,String shareTitle) {
        if (StringUtils.isNotBlank(shareId)) {
            ShareManage shareManage = shareManageService.find(Long.parseLong(shareId));
            if (shareManage != null) {
                shareManage.setShareType(shareType);
                shareManage.setShareImg(shareImg);
                shareManage.setShareTitle(shareTitle);
                shareManage.setShareText(shareText);
                shareManageService.update(shareManage);
            } else {
                saveShareManage(shareType, shareImg, shareText,shareTitle);
            }
        } else {
            saveShareManage(shareType, shareImg, shareText,shareTitle);
        }
        return "shareManage/shareManageList";
    }

    private void saveShareManage(String shareType, String shareImg, String shareText,String shareTitle) {
        ShareManage shareManage = new ShareManage();
        shareManage.setShareTitle(shareTitle);
        shareManage.setShareText(shareText);
        shareManage.setShareImg(shareImg);
        shareManage.setShareType(shareType);
        shareManageService.save(shareManage);
    }

    /**
     * 删除分享文案
     * @param shareId id
     */
    @RequestMapping("deleteShareManage")
    @ResponseBody
    public String deleteShareManage(@RequestParam(value = "shareId", required = false)  Long shareId) {
        if (shareId != null) {
            shareManageService.delete(shareId);
        }
        return "ok";
    }

    /**
     * 删除分享文案
     * @param shareId id
     */
    @RequestMapping("editShare")
    public String editShare(@RequestParam(value = "shareId", required = false)  Long shareId) {
        ShareManage shareManage = shareManageService.find(shareId);
        this.request.setAttribute("shareManage", shareManage);
        return "shareManage/addShareManage";
    }
}
