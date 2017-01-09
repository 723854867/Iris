package com.busap.vcs.service.impl;

import com.busap.vcs.base.IntegralStatus;
import com.busap.vcs.data.entity.LotteryRecord;
import com.busap.vcs.data.entity.LotteryRecordExample;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.enums.TaskTypeSecondEnum;
import com.busap.vcs.data.mapper.LotteryRecordMapper;
import com.busap.vcs.service.IntegralService;
import com.busap.vcs.service.LotteryRecordService;
import com.busap.vcs.service.RuserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 抽奖service实现
 * Created by Knight on 15/12/3.
 */
@Service("lotteryRecordService")
public class LotteryRecordServiceImpl implements LotteryRecordService {

    private static final int integralNum = 30;

    private static final int SEQUENCE = 50000;

    @Autowired
    private LotteryRecordMapper lotteryRecordMapper;

    @Autowired
    private IntegralService integralService;

    @Resource(name="ruserService")
    private RuserService ruserService;

    @Override
    public LotteryRecord createLotteryRecord(Long userId) {
        LotteryRecord record = new LotteryRecord();
        record.setGiftType(0);
        record.setUserId(userId);
        record.setInvited(0);
        record.setRaffleTime(0);
        record.setUpdateTime(new Date());
        record.setAddress("");
        record.setDescription("");
        record.setPhone("");
        record.setRealName("");
        record.setSequence(50000);
        lotteryRecordMapper.insert(record);
        return record;
    }

    @Override
    public LotteryRecord findLotteryRecordByUserId(Long userId) {
        LotteryRecordExample example = new LotteryRecordExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<LotteryRecord> list = lotteryRecordMapper.selectByExample(example);
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public LotteryRecord doRaffle(Long userId) {
        LotteryRecordExample example = new LotteryRecordExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<LotteryRecord> list = lotteryRecordMapper.selectByExample(example);
        if (list.size() == 1) {
            LotteryRecord record = list.get(0);
            int raffleTime = record.getRaffleTime();
            if (raffleTime == 0) {
                // 第一次不中奖
                record.setRaffleTime(1);
                record.setUpdateTime(new Date());
                lotteryRecordMapper.updateByPrimaryKey(record);
                return record;
            } else if (raffleTime == 1 && record.getInvited() == 1) {
                // 第二次中奖
                LotteryRecordExample recordExample = new LotteryRecordExample();
                recordExample.createCriteria().andRaffleTimeGreaterThan(1);
                int sequence = lotteryRecordMapper.countByExample(recordExample);
                record = calculate(record);
                record.setSequence(SEQUENCE + sequence);
                lotteryRecordMapper.updateByPrimaryKey(record);

                return record;
            } else if (raffleTime == 1 && record.getInvited() == 0) {
                // 已摇过一次奖  未邀请好友
                return record;
            } else if (raffleTime == 2) {
                // 已摇过两次奖
                record.setRaffleTime(3);
                lotteryRecordMapper.updateByPrimaryKey(record);
                return record;
            }
        }
        return null;
    }

    @Override
    public boolean updateLotteryRecord(LotteryRecord lotteryRecord) {
        int i = lotteryRecordMapper.updateByPrimaryKey(lotteryRecord);
        return i > 0;
    }

    /**
     * 计算中奖几率
     */
    private LotteryRecord calculate(LotteryRecord record) {
        Ruser ru = ruserService.find(record.getUserId());
        if (ru == null) {
            return null;
        }

        int step = getStepByTime();
        if (step > 0) {
            LotteryRecordExample example = new LotteryRecordExample();
            example.createCriteria().andGiftTypeEqualTo(2);
            int count = lotteryRecordMapper.countByExample(example);
            float f = new Random().nextFloat();
            // 计算中实物奖概率  已中奖人数越多 中奖几率递减
            float odds = 0.2f * ((1000 - count) / 1000f);
            if (f < odds) {
                record.setGiftType(2);
                record.setUpdateTime(new Date());
                record.setRaffleTime(2);
                record.setDescription("抽中苹果");
                return record;
            }
        }
        integralService.insertIntegral(record.getUserId(), integralNum, 0l, TaskTypeSecondEnum.lottery, IntegralStatus.RECEIVE.getStatus(), null);
        ru.setSignSum(ru.getSignSum() + integralNum);
        ruserService.update(ru);

        record.setGiftType(1);
        record.setUpdateTime(new Date());
        record.setRaffleTime(2);
        record.setDescription("抽中积分");
        return record;
    }

    private int getStepByTime() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (hour < 8) {
            return 0;
        } else if (hour >= 8 && hour < 12) {
            return 1;
        } else if (hour >= 12 && hour < 18) {
            return 2;
        } else if (hour >= 18 && hour < 21) {
            return 3;
        } else if (hour >= 21 && hour < 24) {
            return 4;
        }
        return 0;
    }


}
