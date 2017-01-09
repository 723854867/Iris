package com.busap.vcs.web;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.service.AttentionService;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 用户设置 黑名单管理
 * Created by huoshanwei on 2016/3/3.
 */
@Controller
@RequestMapping("/userSetting")
public class UserSettingController extends CRUDController<Ruser, Long> {

    @Autowired
    protected HttpServletRequest request;

    @Resource
    private JedisService jedisService;

    @Resource
    private AttentionService attentionService;

    @Override
    public void setBaseService(BaseService<Ruser, Long> baseService) {
        this.baseService = baseService;
    }

    /**
     * 加入黑名单
     *
     * @param userId 用户ID
     * @return RespBody
     */
    @RequestMapping("/addBlacklist")
    @ResponseBody
    public RespBody addBlacklist(Long userId) {
        if (userId == null) {
            return respBodyWriter.toError(ResponseCode.CODE_450.toString(), ResponseCode.CODE_450.toCode());
        }
        String uid = this.request.getHeader("uid");
        if(userId.longValue() == Long.valueOf(uid).longValue()){
            //不能关注本人
            return respBodyWriter.toError(ResponseCode.CODE_615.toString(), ResponseCode.CODE_615.toCode());
        }
        if(jedisService.isSetMemberInShard(BicycleConstants.BLACK_LIST_USER_ID + uid, String.valueOf(userId))){
            return respBodyWriter.toError(ResponseCode.CODE_610.toString(), ResponseCode.CODE_610.toCode());
        }
        // 取消关注 加入黑名单后，自动取消关注
        attentionService.deleteAttention(userId, Long.parseLong(uid), "www");
        jedisService.deleteSetItemFromShard(BicycleConstants.ATTENTION_ID_OF+userId, uid);
        jedisService.setValueToSetInShard(BicycleConstants.BLACK_LIST_USER_ID + uid, String.valueOf(userId));
        return respBodyWriter.toSuccess();
    }

    /**
     * 取消加入黑名单
     *
     * @param userId 用户ID
     * @return RespBody
     */
    @RequestMapping("/cancelAddBlacklist")
    @ResponseBody
    public RespBody cancelAddBlacklist(Long userId) {
        if (userId == null) {
            return respBodyWriter.toError(ResponseCode.CODE_450.toString(), ResponseCode.CODE_450.toCode());
        }
        String uid = this.request.getHeader("uid");
        jedisService.deleteSetItemFromShard(BicycleConstants.BLACK_LIST_USER_ID + uid, String.valueOf(userId));
        return respBodyWriter.toSuccess();
    }

    /**
     * 黑名单列表
     *
     * @param
     * @return RespBody
     */
    @RequestMapping("/getBlacklist")
    @ResponseBody
    public RespBody getBlacklist() {
        String uid = this.request.getHeader("uid");
        Set<String> userList = jedisService.getSetFromShard(BicycleConstants.BLACK_LIST_USER_ID + uid);
        List<Map<String, String>> uList = new ArrayList<Map<String, String>>();
        for (String userId : userList) {
            Map<String, String> userMap = new HashMap<String, String>();
            String bUserId = jedisService.getValueFromMap(BicycleConstants.USER_INFO + userId, "id");
            String bName = jedisService.getValueFromMap(BicycleConstants.USER_INFO + userId, "name");
            String bPic = jedisService.getValueFromMap(BicycleConstants.USER_INFO + userId, "pic");
            String vipStat = jedisService.getValueFromMap(BicycleConstants.USER_INFO + userId, "vipStat");
            userMap.put("id", bUserId);
            userMap.put("name", bName);
            userMap.put("pic", bPic);
            userMap.put("vipStat", vipStat);
            uList.add(userMap);
        }
        return respBodyWriter.toSuccess(uList);
    }

}
