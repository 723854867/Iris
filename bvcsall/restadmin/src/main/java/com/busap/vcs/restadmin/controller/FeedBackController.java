package com.busap.vcs.restadmin.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import com.busap.vcs.data.entity.User;
import com.busap.vcs.data.model.ExportFeedback;
import com.busap.vcs.data.model.FeedbackDisplay;
import com.busap.vcs.restadmin.utils.CommonUtils;
import com.busap.vcs.service.UserService;
import com.busap.vcs.service.utils.Email;
import com.busap.vcs.service.utils.ExcelUtils;
import com.busap.vcs.service.utils.MailUtils;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.PagingContextHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.busap.vcs.data.entity.Feedback;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.FeedbackService;
import com.busap.vcs.webcomn.controller.CRUDController;
import org.springframework.web.servlet.ModelAndView;

@Controller()
@RequestMapping("feedback")
public class FeedBackController extends CRUDController<Feedback, Long> {

    @Resource(name = "feedbackService")
    private FeedbackService feedbackService;

    @Resource
    private UserService userService;

    @Value("${files.localpath}")
    private String basePath;

    @Resource(name = "feedbackService")
    @Override
    public void setBaseService(BaseService<Feedback, Long> baseService) {
        this.baseService = baseService;
    }

    @RequestMapping("feedbacklist")
    public ModelAndView feedbackPage() {
        ModelAndView mav = new ModelAndView();
        Map<String,Object> params = new HashMap<String, Object>();
        List<User> list = userService.queryUserInfo(params);
        mav.addObject("emailList", list);
        mav.setViewName("feedback/list");
        return mav;
    }

/*    @RequestMapping("ajaxsearch")
    @ResponseBody
    public Map ajaxSearch(Integer page, Integer rows,
                          @RequestParam(value = "content",required = false) String content,
                          @RequestParam(value = "dataFrom",required = false) String dataFrom,
                          @RequestParam(value = "startTime",required = false) String startTime,
                          @RequestParam(value = "endTime",required = false) String endTime) {
        if (page == null || page.intValue() < 1) {
            page = 1;
        }

        Page pinfo = feedbackService.findFeedback(page, rows,content,dataFrom,startTime,endTime);

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", pinfo.getTotalElements());//total键 存放总记录数，必须的
        jsonMap.put("rows", pinfo.getContent());//rows键 存放每页记录 list
        return jsonMap;
    }*/

    @RequestMapping("queryFeedBackList")
    @ResponseBody
    @EnablePaging
    public Map<String,Object> queryFeedBackList(@ModelAttribute("queryPage") JQueryPage queryPage,
                                                @RequestParam(value = "content",required = false) String content,
                                                @RequestParam(value = "dataFrom",required = false) String dataFrom,
                                                @RequestParam(value = "startTime",required = false) String startTime,
                                                @RequestParam(value = "endTime",required = false) String endTime){
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("content",content);
        params.put("dataFrom",dataFrom);
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        List<FeedbackDisplay> list = feedbackService.queryFeedbackList(params);
        com.busap.vcs.util.page.Page page = PagingContextHolder.getPage();
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("total",page.getTotalResult());
        map.put("rows",list);
        return map;
    }

    @RequestMapping("sendMail")
    @ResponseBody
    public Map<String, Object> sendMail(@RequestParam(value = "feedbackIds",required = false) String feedbackIds,@RequestParam(value = "emailList",required = false) String emailList, @RequestParam(value = "newEmail",required = false) String newEmail) {
        String[] ids = feedbackIds.split(",");
        String checkEmail = StringUtils.isNotBlank(emailList)?emailList:"";
        String inputEmail = StringUtils.isNotBlank(newEmail) ? newEmail+";" : "";

        Email email = new Email();
        email.setTo(checkEmail+inputEmail);
        email.setSubject("【用户反馈】");
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("ids",ids);
        List<FeedbackDisplay> list = feedbackService.queryFeedbackInIds(ids);
        email.setText(getHtmlMailContent(list));
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            MailUtils.sendMail(email);
            map.put("resultCode", "ok");
            map.put("resultMessage", "邮件发送成功！");
            feedbackService.updateFeedbackInIds(list);
            //String emails = checkEmail+inputEmail;
            //String[] emailArray = emails.split(";");
            //userEmailService.updateUserEmailInEmails(emailArray);
        } catch (MessagingException e) {
            e.printStackTrace();
            map.put("resultCode", "error");
            map.put("resultMessage", "邮件发送失败！");
        }
        return map;
    }

    private String getHtmlMailContent(List<FeedbackDisplay> list){
        StringBuilder html = new StringBuilder();
        String table = "color:#333333;border-width: 1px;border-color: #666666;border-collapse: collapse;";
        String th = "border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #dedede;";
        String td = "border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #ffffff;";
        html.append("<div><table style='"+table+"'><tr><th style='"+th+"'>联系方式</th><th style='"+th+"'>手机号</th><th style='"+th+"'>反馈内容</th><th style='"+th+"'>时间</th><th style='"+th+"'>状态</th><th style='"+th+"'>来源</th></tr>");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (FeedbackDisplay feedback : list){
            String phone = feedback.getPhone()==null?" ":feedback.getPhone();
            html.append("<tr><td style='"+td+ "'>"+feedback.getContact()+"</td><td style='"+td+"'>"+phone+"</td><td style='"+td+"'>"+feedback.getContent()+"</td><td style='"+td+"'>"+sdf.format(feedback.getCreateDateStr())+"</td><td style='"+td+"'>"+feedback.getStatusName()+"</td><td style='"+td+"'>"+feedback.getDataFrom()+"</td></tr>");
        }
        html.append("</div></table>");
        return html.toString();
    }

    @RequestMapping("exportFeedBackToExcel")
    public void exportFeedBackToExcel(@RequestParam(value = "content",required = false) String content,
                                      @RequestParam(value = "dataFrom",required = false) String dataFrom,
                                      @RequestParam(value = "startTime",required = false) String startTime,
                                      @RequestParam(value = "endTime",required = false) String endTime,
                                      HttpServletResponse response)throws IOException {
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("content",content);
        params.put("dataFrom",dataFrom);
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        List<ExportFeedback> list = feedbackService.exportFeedBackList(params);
        ExcelUtils<ExportFeedback> excelUtils = new ExcelUtils<ExportFeedback>();
        String[] headers = {"反馈内容", "反馈时间 ", "用户ID", "联系方式","来源", "状态"};
        response.reset();
        // 设定输出文件头
        response.setHeader("Content-disposition", "attachment; filename=feedback_info.xls");
        // 定义输出类型
        response.setContentType("application/msexcel");
        OutputStream out = response.getOutputStream();
        excelUtils.exportExcel("用户反馈信息",headers,list,out,"yyyy-MM-dd HH:mm:ss");
    }

}
