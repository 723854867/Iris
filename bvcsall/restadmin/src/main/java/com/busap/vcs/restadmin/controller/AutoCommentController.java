package com.busap.vcs.restadmin.controller;

import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.data.entity.AutoComment;
import com.busap.vcs.service.AutoCommentService;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.webcomn.controller.CRUDController;
import org.apache.commons.lang.StringUtils;
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
 * 评论管理
 * Created by Knight on 15/11/11.
 */
@Controller()
@RequestMapping("autoComment")
public class AutoCommentController extends CRUDController<AutoComment, Long> {

    @Resource(name = "autoCommentService")
    private AutoCommentService autoCommentService;

    @RequestMapping("commentList")
    public String commentList() throws Exception {

        return "autoComment/commentList";
    }

    @Override
    public void setBaseService(BaseService<AutoComment, Long> baseService) {
        this.baseService = baseService;
    }

    @RequestMapping(value = "commentListJson")
    @ResponseBody
    @EnablePaging
    public Map<String,Object> commentListJson(Integer page, Integer rows) throws Exception{

        if (page == null) {
            page = 1;
        }
        if (rows == null) {
            rows = 20;
        }

        StringBuffer hql = new StringBuffer();
        StringBuffer countHql = new StringBuffer();
        List<ParameterBean> paramList = new ArrayList<ParameterBean>();

        hql.append("FROM AutoComment comment ");
        countHql.append("SELECT COUNT(*) FROM AutoComment comment ");

        List<OrderByBean> orderByList=new ArrayList<OrderByBean>();

        OrderByBean orderByObject1=new OrderByBean("id", 0, "comment");
        orderByList.add(orderByObject1);

        List commentList = autoCommentService.getObjectByJpql(hql, page, rows, "comment", paramList, null, orderByList);
        Long totalCount = autoCommentService.getObjectCountByJpql(countHql, paramList);

        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("total",totalCount);
        map.put("rows",commentList);
        return map;
    }

    @RequestMapping("updateCommentStatus")
    @ResponseBody
    public String updateCommentStatus(
            @RequestParam(value = "commentId", required = false)  Long commentId,
            @RequestParam(value = "actType", required = false)  Integer actType) {

        if (commentId != null && actType != null) {

            AutoComment autoComment = autoCommentService.find(commentId);
            autoComment.setAvailable(actType);
            autoCommentService.update(autoComment);
        }
        return "ok";
    }

    // 新建评论
    @RequestMapping("addComment")
    public String showAddComment() {
        return "autoComment/addComment";
    }

    // 保存新建评论
    @RequestMapping("saveAddComment")
    public String saveComment(String comments, String commentId) {
        if (StringUtils.isNotBlank(commentId) && StringUtils.isNumeric(commentId)) {
            AutoComment autoComment = autoCommentService.find(Long.parseLong(commentId));
            if (autoComment != null) {
                autoComment.setComment(comments);
                autoCommentService.update(autoComment);
            }
        } else {
            String[] commentArray = comments.split("\\r\\n");
            for (String comment : commentArray) {
                if (StringUtils.isNotBlank(comment)) {
                    AutoComment autoComment = new AutoComment();
                    autoComment.setAvailable(1);
                    autoComment.setComment(comment);
                    autoCommentService.save(autoComment);
                }
            }
        }

        return "autoComment/commentList";
    }

    // 跳转到编辑评论
    @RequestMapping("commentEdit")
    public String commentEdit(Long commentId) {
        AutoComment autoComment = autoCommentService.find(commentId);
        this.request.setAttribute("autoComment", autoComment);
        return "autoComment/addComment";
    }

    // 删除评论
    @RequestMapping("deleteComment")
    @ResponseBody
    public String deleteComment(@RequestParam(value = "commentId", required = false)  Long commentId) {
        if (commentId != null) {
            autoCommentService.delete(commentId);
        }
        return "ok";
    }
}
