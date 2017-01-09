package com.busap.vcs.web;

import com.busap.vcs.data.entity.LotteryRecord;
import com.busap.vcs.data.vo.LotteryRecordVO;
import com.busap.vcs.service.LotteryRecordService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 抽奖controller
 * Created by Knight on 15/12/2.
 */
@Controller
@RequestMapping("/lotteryDraw")
public class LotteryDrawController {

    private Logger logger = LoggerFactory.getLogger(LotteryDrawController.class);

    @Autowired
    private LotteryRecordService lotteryRecordService;

    @Resource(name = "respBodyBuilder")
    private RespBodyBuilder respBodyWriter = new RespBodyBuilder();

    /**
     * 抽奖页面首页
     * @return 用户抽奖相关信息
     */
    @RequestMapping("/index")
    @ResponseBody
    public RespBody indexHandler(Long userId) {
        logger.info("Index handler... userId=" + userId);
        LotteryRecord lotteryRecord = lotteryRecordService.findLotteryRecordByUserId(userId);
        if (lotteryRecord == null) {
            // 首次进页面
            lotteryRecord = lotteryRecordService.createLotteryRecord(userId);
        }
        return respBodyWriter.toSuccess(lotteryRecord);
    }

    @RequestMapping("/inviteFriend")
    @ResponseBody
    public RespBody inviteFriendHandler(Long userId) {
        logger.info("Invite friend handler... userId=" + userId);
        LotteryRecord lotteryRecord = lotteryRecordService.findLotteryRecordByUserId(userId);
        lotteryRecord.setInvited(1);
        boolean bln = lotteryRecordService.updateLotteryRecord(lotteryRecord);
        if (bln) {
            return respBodyWriter.toSuccess();
        } else {
            return respBodyWriter.toError("邀请好友更新数据失败", -1);
        }
    }

    @RequestMapping("/doRaffle")
    @ResponseBody
    public RespBody doRaffleHandler(Long userId) {
        logger.info("Do raffle handler... userId=" + userId);
        LotteryRecord record = lotteryRecordService.doRaffle(userId);
        if (record == null) {
            return respBodyWriter.toError("没有此用户信息", -1);
        }
        return respBodyWriter.toSuccess(record);
    }

    @RequestMapping("/saveUserInfo")
    @ResponseBody
    public RespBody saveUserInformationHandler(Long userId, String realName, String phone, String address) {
        logger.info("Save user information handler... userId=" + userId + " & real name=" + realName
                + " & phone=" + phone + " & address=" + address);
        LotteryRecord lotteryRecord = lotteryRecordService.findLotteryRecordByUserId(userId);
        if (lotteryRecord != null) {
            lotteryRecord.setAddress(address);
            lotteryRecord.setRealName(realName);
            lotteryRecord.setPhone(phone);
            boolean bln = lotteryRecordService.updateLotteryRecord(lotteryRecord);
            if (bln) {
                return respBodyWriter.toSuccess();
            } else {
                return respBodyWriter.toError("保存用户信息失败", -1);
            }
        } else {
            return respBodyWriter.toError("没有此用户信息", -1);
        }
    }
}
