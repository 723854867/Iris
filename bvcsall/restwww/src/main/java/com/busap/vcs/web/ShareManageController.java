package com.busap.vcs.web;

import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.ShareManage;
import com.busap.vcs.service.ShareManageService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Knight on 15/11/17.
 */
@Controller
@RequestMapping("/shareManage")
public class ShareManageController {

    @Resource(name = "respBodyBuilder")
    private RespBodyBuilder respBodyWriter = new RespBodyBuilder();

    @Resource(name = "shareManageService")
    private ShareManageService shareManageService;

    @RequestMapping("/findAll")
    @ResponseBody
    public RespBody findAllHandler() {
        List<ShareManage> shareList = shareManageService.findAll();
        return respBodyWriter.toSuccess(shareList);
    }

    @RequestMapping("/findByType")
    @ResponseBody
    public RespBody findByTypeHandler(Integer shareType) throws Exception {
        if (shareType == null) {
            return respBodyWriter.toError(ResponseCode.CODE_450.toString(), ResponseCode.CODE_450.toCode());
        }
        StringBuffer hql = new StringBuffer();
        hql.append("FROM ShareManage share where share.shareType=").append(shareType);

        List shareList = shareManageService.getObjectByJpql(hql, 1, 10, "share",
                new ArrayList<ParameterBean>(), null, new ArrayList<OrderByBean>());
        if (shareList.size() > 0) {
            return respBodyWriter.toSuccess(shareList.get(0));
        } else {
            return respBodyWriter.toError("没有相关记录", -1);
        }
    }
}
