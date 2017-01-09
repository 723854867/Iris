package com.busap.vcs.service;


import com.busap.vcs.base.IntegralStatus;
import com.busap.vcs.data.entity.ActivityVideo;
import com.busap.vcs.data.entity.Integral;
import com.busap.vcs.data.entity.IntegralStaDTO;
import com.busap.vcs.data.enums.TaskTypeSecondEnum;
import com.busap.vcs.data.model.IntegralStaDisplay;
import com.busap.vcs.data.vo.IntegralDetailVO;
import com.busap.vcs.data.vo.IntegralVO;
import com.busap.vcs.data.vo.UserTaskVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 积分service
 * Created by yangxinyu on 15/9/29.
 */
public interface IntegralService {

    public Integral insertIntegral(Long userId, int integralNum, Long sourceId, TaskTypeSecondEnum taskType, Integer status, String desc);

    /**
     * 签到获得积分
     * @param userId 用户ID
     */
    public Map<String, Integer> getIntegralFromSign(Long userId);

    /**
     * 完成每日任务得积分
     * @param userId 用户ID
     * @param videoId 视频ID
     * @param taskType 每日任务类型 参考DailyTaskType
     */
    public void getIntegralFromDaily(Long userId, String videoId, TaskTypeSecondEnum taskType);

    /**
     * 特殊活动获取积分
     * @param userId 用户ID
     * @param activityVideos  视频活动
     */
    public void getIntegralFromActivity(Long userId, List<ActivityVideo> activityVideos);

    public void receiveSignIntegral(Integral integral, boolean isContinuous);

    public IntegralVO receiveIntegral(Long integralId);

    public void getIntegralFromFanNumber(Long userId);

    public void getIntegralFromCompleteInfo(Long userId);

    /**
     * 查询当前用户关注的人是否有vip用户
     * @param userId 用户ID
     * @param stat   VIP类型
     * @return 是否已关注了
     */
    public boolean checkAttentionTaskFinished(Long userId, com.busap.vcs.base.Constants.USER_VIP_STAT stat);

    public void checkAttentedUserIsVIP(Long userId, Long attentionId);

    public Map<TaskTypeSecondEnum, Constants.TaskFinish> getUserDailyTaskStatus(Long userId);

    public List<UserTaskVO> getAllTaskStatusByUserId(Long userId);
    /**
     * 查询用户积分明细列表
     * @param userId 用户ID
     * @return 列表
     */
    public List<IntegralDetailVO> findUserIntegralDetailList(Long userId, Integer page, Integer size);

    public Integer deleteIntegralLogByTime();

    public Integral getIntegral(Long userId, Long sourceId, TaskTypeSecondEnum taskType);

    public List<Integral> findSignIntegralList(Long userId, TaskTypeSecondEnum taskType, IntegralStatus status);

    public Integer getUserIntegralSum(Long userId);
    
    public List<IntegralStaDTO> integralMultiTypeStatistics();

    public List<UserTaskVO> taskVOFilter(List<UserTaskVO> taskVOList);

    List<IntegralStaDisplay> queryIntegralMultiTypeStatistics();
}
