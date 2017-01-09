package com.busap.vcs.restadmin.controller;

import com.busap.vcs.data.entity.Anchor;
import com.busap.vcs.data.entity.Organization;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.enums.DataFrom;
import com.busap.vcs.data.model.ExportWopaiNormalUser;
import com.busap.vcs.data.model.OrganizationAnchorDisplay;
import com.busap.vcs.data.model.RuserDisplay;
import com.busap.vcs.restadmin.utils.CommonUtils;
import com.busap.vcs.restadmin.utils.ResultData;
import com.busap.vcs.service.*;
import com.busap.vcs.service.utils.CSVUtils;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.Page;
import com.busap.vcs.util.page.PagingContextHolder;
import com.busap.vcs.webcomn.U;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by huoshanwei on 2016/3/22.
 */
@Controller
@RequestMapping("organization")
public class OrganizationController {

    @Resource
    private OrganizationService organizationService;

    @Resource
    private RuserService ruserService;

    @Resource
    private AnchorService anchorService;

    @Resource
    private RoomService roomService;

    @Value("${files.localpath}")
    private String basePath;

    @Resource
    private ConsumeRecordService consumeRecordService;

    //机构查询
    @RequestMapping("forwardOrganizationList")
    public ModelAndView forwardOrganizationList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("organization/query_organization");
        return mav;
    }

    @ResponseBody
    @RequestMapping("queryOrganizationList")
    @EnablePaging
    public Map<String, Object> queryOrganizationList(@ModelAttribute("queryPage") JQueryPage queryPage,
                                                     @RequestParam(value = "key", required = false) Integer key,
                                                     @RequestParam(value = "useKey", required = false) String useKey) {
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("type",key);
        params.put("useKey",useKey);
        List<Organization> list = organizationService.queryAll(params);
        Page page = PagingContextHolder.getPage();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total", page.getTotalResult());
        resultMap.put("rows", list);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("insertOrganization")
    public ResultData insertOrganization(@ModelAttribute("organization") @Valid Organization organization, BindingResult results) {
        ResultData resultData = new ResultData();
        organization.setCreateDate(new Date());
        organization.setDataFrom(DataFrom.移动麦视后台.getName());
        organization.setAnchorCount(0);
        organization.setCreatorId(U.getUid());
        int result = organizationService.insert(organization);
        if (result > 0) {
            resultData.setResultCode("success");
            resultData.setResultMessage("添加成功！");
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("添加失败！");
        }
        return resultData;
    }

    @RequestMapping("updateOrganizationTemplate")
    public ModelAndView updateOrganizationTemplate(Long id) {
        ModelAndView mav = new ModelAndView();
        Organization organization = organizationService.queryOrganization(id);
        mav.addObject("organization", organization);
        mav.setViewName("organization/update_organization");
        return mav;
    }

    @ResponseBody
    @RequestMapping("updateOrganization")
    public ResultData updateOrganization(@ModelAttribute("organization") @Valid Organization organization, BindingResult results) {
        ResultData resultData = new ResultData();
        int result = organizationService.updateByPrimaryKey(organization);
        if (result > 0) {
            resultData.setResultCode("success");
            resultData.setResultMessage("更新成功！");
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("更新失败！");
        }
        return resultData;
    }

    @ResponseBody
    @RequestMapping("deleteOrganization")
    public ResultData deleteOrganization(Long id){
        ResultData resultData = new ResultData();
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("organizationId",id);
        params.put("pageStart",0);
        params.put("pageSize",5);
        List<RuserDisplay> list = ruserService.queryWopaiRusers(params);
        if(list.size() == 0){
            int result = organizationService.deleteByPrimaryKey(id);
            if (result > 0) {
                resultData.setResultCode("success");
                resultData.setResultMessage("删除成功！");
            } else {
                resultData.setResultCode("fail");
                resultData.setResultMessage("删除失败！");
            }
        }else{
            resultData.setResultCode("fail");
            resultData.setResultMessage("删除失败,当前机构下有主播信息！");
        }

        return resultData;
    }

    //主播管理
    @RequestMapping("forwardManageAnchor")
    public ModelAndView forwardManageAnchor(Long organizationId) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("selected","anchor");
        mav.addObject("organizationId",organizationId);
        mav.setViewName("organization/manage_anchor");
        return mav;
    }

    @ResponseBody
    @RequestMapping("queryOrganizationAnchorList")
    @EnablePaging
    public Map<String, Object> queryOrganizationAnchorList(@ModelAttribute("queryPage") JQueryPage queryPage,
                                                           Long organizationId) {
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("organizationId",organizationId);
        List<OrganizationAnchorDisplay> list = anchorService.queryOrganizationAnchorList(params);
        Page page = PagingContextHolder.getPage();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total", page.getTotalResult());
        resultMap.put("rows", list);
        return resultMap;
    }

    @RequestMapping("exportOrganizationAnchor")
    public void exportOrganizationAnchor(Long organizationId,HttpServletResponse response){
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("organizationId",organizationId);
        List<OrganizationAnchorDisplay> list = anchorService.queryOrganizationAnchorList(params);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List cList = new ArrayList<Map>();
        for (OrganizationAnchorDisplay orgAnchor : list) {
            Map row = new LinkedHashMap<String, String>();
            String certificateTypeName = "";
            if(orgAnchor.getCertificateType() == 1){
                certificateTypeName = "身份证";
            }else if(orgAnchor.getCertificateType() == 2){
                certificateTypeName = "护照";
            }else if(orgAnchor.getCertificateType() == 3){
                certificateTypeName = "台胞证";
            }else {
                certificateTypeName = "身份证";
            }

            row.put("1",orgAnchor.getUserId());
            row.put("2",orgAnchor.getName());
            row.put("3",orgAnchor.getRealName());
            row.put("4",certificateTypeName);
            row.put("5",orgAnchor.getCertificateNumber());
            row.put("6",orgAnchor.getBankName());
            row.put("7",orgAnchor.getBranchBankName());
            row.put("8","卡号："+orgAnchor.getBankNumber());
            row.put("9",sdf.format(orgAnchor.getCreateDate()));
            row.put("10",orgAnchor.getPhone());
            row.put("11",orgAnchor.getBasicSalary());
            cList.add(row);
        }
        LinkedHashMap headers = new LinkedHashMap();
        headers.put("1", "用户ID");
        headers.put("2", "用户昵称");
        headers.put("3", "姓名");
        headers.put("4", "证件类型");
        headers.put("5", "证件号码");
        headers.put("6", "开户行");
        headers.put("7", "支行");
        headers.put("8", "银行卡号");
        headers.put("9", "注册时间");
        headers.put("10", "手机号码");
        headers.put("11", "底薪（元）");
        File file = CSVUtils.createCSVFile(cList, headers, basePath+"/exportExcel/", "机构主播信息");
        CommonUtils.download(file.getPath(),response);
    }

    @ResponseBody
    @RequestMapping("updateOrgAnchorTemplate")
    public ModelAndView updateOrgAnchorTemplate(Long id){
        ModelAndView mav = new ModelAndView();
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("userId",id);
        List<OrganizationAnchorDisplay> list = anchorService.queryOrganizationAnchorList(params);
        OrganizationAnchorDisplay orgAnchor = new OrganizationAnchorDisplay();
        for(int i=0;i<list.size();i++){
            orgAnchor.setUserId(list.get(0).getUserId());
            orgAnchor.setId(list.get(0).getId());
            orgAnchor.setBankName(list.get(0).getBankName());
            orgAnchor.setBranchBankName(list.get(0).getBranchBankName());
            orgAnchor.setBankNumber(list.get(0).getBankNumber());
            orgAnchor.setRealName(list.get(0).getRealName());
            orgAnchor.setCertificateType(list.get(0).getCertificateType());
            orgAnchor.setCertificateNumber(list.get(0).getCertificateNumber());
            orgAnchor.setPhone(list.get(0).getPhone());
            orgAnchor.setBasicSalary(list.get(0).getBasicSalary());
        }
        mav.addObject("orgAnchor", orgAnchor);
        mav.setViewName("organization/update_organization_anchor");
        return mav;
    }

    @ResponseBody
    @RequestMapping("updateOrgAnchor")
    public ResultData updateOrgAnchor(Long id, String realName, String bankName,String branchBankName,String phone, String bankNumber, int certificateType, String certificateNumber, BigDecimal basicSalary){
        ResultData resultData = new ResultData();
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("id",id);
        Anchor anchor = anchorService.queryAnchor(id);
        if (anchor != null){
            anchor.setCertificateNumber(certificateNumber);
            anchor.setCertificateType(certificateType);
            anchor.setRealName(realName);
            anchor.setBankName(bankName);
            anchor.setBranchBankName(branchBankName);
            anchor.setBankNumber(bankNumber);
            anchor.setBasicSalary(basicSalary);
            anchor.setPhone(phone);
            int result = anchorService.updateByPrimaryKey(anchor);
            if(result > 0){
                resultData.setResultCode("success");
                resultData.setResultMessage("更新成功！");
            }else{
                resultData.setResultCode("fail");
                resultData.setResultMessage("更新失败！");
            }
        }else{
            resultData.setResultCode("fail");
            resultData.setResultMessage("更新失败，该主播还未开播过！");
        }
        return resultData;
    }

    @ResponseBody
    @RequestMapping("removeAnchorFromOrg")
    public ResultData removeAnchorFromOrg(Long userId){
        ResultData resultData = new ResultData();
        Ruser ruser = ruserService.selectByPrimaryKey(userId);
        Long organizationId = ruser.getOrganizationId();
        if(ruser != null){
            ruser.setOrganizationId(null);
            int result = ruserService.updateRuser(ruser);
            int ret = organizationService.updateAnchorCount(organizationId);
            if(result > 0 && ret>0){
                resultData.setResultCode("success");
                resultData.setResultMessage("移除成功！");
            }else{
                resultData.setResultCode("fail");
                resultData.setResultMessage("移除失败！");
            }
        }else{
            resultData.setResultCode("fail");
            resultData.setResultMessage("移除失败！");
        }
        return resultData;
    }

    @RequestMapping("forwardAnchorLiveData")
    public ModelAndView forwardAnchorLiveData(Long organizationId){
        ModelAndView mav = new ModelAndView();
        mav.addObject("selected","liveData");
        mav.addObject("organizationId",organizationId);
        mav.setViewName("organization/anchor_live_data");
        return mav;
    }

    @ResponseBody
    @RequestMapping("queryOrgAnchorLiveDataList")
    @EnablePaging
    public Map<String, Object> queryOrgAnchorLiveDataList(@ModelAttribute("queryPage") JQueryPage queryPage,
                                                          Long organizationId,
                                                          @RequestParam(value = "userKey", required = false) Integer userKey,
                                                          @RequestParam(value = "userKeyword", required = false) String userKeyword,
                                                          @RequestParam(value = "startDate", required = false) String startDate,
                                                          @RequestParam(value = "endDate", required = false) String endDate) {
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("organizationId",organizationId);
        params.put("userKey",userKey);
        params.put("userKeyword",userKeyword);

        List<OrganizationAnchorDisplay> list = anchorService.queryOrganizationAnchorList(params);
        Page page = PagingContextHolder.getPage();
        Map<String,Object> durationParams = new HashMap<String, Object>();
        for (OrganizationAnchorDisplay orgAnchor : list){
            durationParams.put("userId",orgAnchor.getUserId());
            durationParams.put("startDate",startDate);
            durationParams.put("endDate",endDate);
            Page p = PagingContextHolder.getPage();
            PagingContextHolder.removePage();
            OrganizationAnchorDisplay oa = roomService.queryUserLiveDurationInfo(durationParams);
            Long points = consumeRecordService.selectUserPointInfo(durationParams);
            PagingContextHolder.setPage(p);
            orgAnchor.setDays(oa.getDays());
            orgAnchor.setPointNumber(points);
            orgAnchor.setTotalDuration(oa.getTotalDuration()==null?"0": CommonUtils.formatDuring(Long.parseLong(oa.getTotalDuration()),2));
        }


        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total", page.getTotalResult());
        resultMap.put("rows", list);
        return resultMap;
    }

    @RequestMapping("exportOrgAnchorLiveData")
    public void exportOrgAnchorLiveData(Long organizationId,
                                        @RequestParam(value = "userKey", required = false) Integer userKey,
                                        @RequestParam(value = "userKeyword", required = false) String userKeyword,
                                        @RequestParam(value = "startDate", required = false) String startDate,
                                        @RequestParam(value = "endDate", required = false) String endDate,
                                        HttpServletResponse response){
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("organizationId",organizationId);
        params.put("userKey",userKey);
        params.put("userKeyword",userKeyword);
        List<OrganizationAnchorDisplay> list = anchorService.queryOrganizationAnchorList(params);
        Map<String,Object> durationParams = new HashMap<String, Object>();
        String certificateTypeName = "";
        for (OrganizationAnchorDisplay orgAnchor : list){
            durationParams.put("userId",orgAnchor.getUserId());
            durationParams.put("startDate",startDate);
            durationParams.put("endDate",endDate);
            OrganizationAnchorDisplay oa = roomService.queryUserLiveDurationInfo(durationParams);
            orgAnchor.setDays(oa.getDays());
            Long points = consumeRecordService.selectUserPointInfo(durationParams);
            orgAnchor.setPointNumber(points);
            orgAnchor.setTotalDuration(oa.getTotalDuration()==null?"0": CommonUtils.formatDuring(Long.parseLong(oa.getTotalDuration()),2));
            if(orgAnchor.getCertificateType() == 1){
                certificateTypeName = "身份证";
            }else if(orgAnchor.getCertificateType() == 2){
                certificateTypeName = "护照";
            }else if(orgAnchor.getCertificateType() == 3){
                certificateTypeName = "台胞证";
            }else {
                certificateTypeName = "身份证";
            }
        }
        List cList = new ArrayList<Map>();
        for (OrganizationAnchorDisplay orgAnchor : list) {
            Map row = new LinkedHashMap<String, String>();
            row.put("1",orgAnchor.getRealName());
            row.put("2",certificateTypeName+"/"+orgAnchor.getCertificateNumber());
            row.put("3",orgAnchor.getBankName());
            row.put("4",orgAnchor.getBranchBankName());
            row.put("5","卡号："+orgAnchor.getBankNumber());
            row.put("6",orgAnchor.getBasicSalary());
            row.put("7",orgAnchor.getUserId());
            row.put("8",orgAnchor.getName());
            row.put("9",orgAnchor.getPhone());
            row.put("10",orgAnchor.getDays());
            row.put("11",orgAnchor.getTotalDuration());
            row.put("12",orgAnchor.getPointNumber());
            cList.add(row);
        }
        LinkedHashMap headers = new LinkedHashMap();
        headers.put("1", "真实姓名");
        headers.put("2", "证件类型/号码");
        headers.put("3", "开户行");
        headers.put("4", "支行");
        headers.put("5", "银行卡号");
        headers.put("6", "底薪");
        headers.put("7", "用户ID");
        headers.put("8", "用户昵称");
        headers.put("9", "手机号");
        headers.put("10", "直播天数");
        headers.put("11", "直播时长");
        headers.put("12", "金豆数");
        File file = CSVUtils.createCSVFile(cList, headers, basePath+"/exportExcel/", "机构主播直播数据信息");
        CommonUtils.download(file.getPath(),response);
    }

}
