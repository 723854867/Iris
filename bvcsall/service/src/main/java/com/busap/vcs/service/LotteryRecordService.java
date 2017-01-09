package com.busap.vcs.service;

import com.busap.vcs.data.entity.LotteryRecord;

/**
 * 抽奖service
 * Created by Knight on 15/12/3.
 */
public interface LotteryRecordService {

    /**
     * 新建用户抽奖信息
     * @param userId 用户id
     * @return LotteryRecord
     */
    public LotteryRecord createLotteryRecord(Long userId);

    /**
     * 查询中奖记录
     * @param userId 用户id
     * @return LotteryRecord
     */
    public LotteryRecord findLotteryRecordByUserId(Long userId);

    /**
     * 抽奖
     * @param userId 用户id
     * @return LotteryRecord
     */
    public LotteryRecord doRaffle(Long userId);

    /**
     * 更新抽奖信息
     */
    public boolean updateLotteryRecord(LotteryRecord lotteryRecord);

}
