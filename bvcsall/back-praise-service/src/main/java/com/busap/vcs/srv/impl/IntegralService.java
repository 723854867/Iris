package com.busap.vcs.srv.impl;

import com.busap.vcs.dao.IntegralDao;
import com.busap.vcs.data.entity.Task;
import com.busap.vcs.data.enums.TaskTypeOneEnum;
import com.busap.vcs.data.enums.TaskTypeSecondEnum;
import com.busap.vcs.srv.IIntegralService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 积分service实现
 * Created by Knight on 15/10/27.
 */
@Service("integralService")
public class IntegralService implements IIntegralService {

    @Autowired
    IntegralDao integralDao;

    public void getIntegralFromFanNumber(Long userId) {
        int fansCount = integralDao.getUserFansCount(userId);
        List<Task> taskList = integralDao.selectFansTask();
        for (Task task : taskList) {
            int integralCount = integralDao.selectIntegral(userId, task.getId(), false);
            if (integralCount > 0) {
                /** 判断 是否已经获得该任务积分 **/
                continue;
            }
            if (fansCount >= Integer.parseInt(task.getTaskValue())) {
                int integralNum = task.getIntegral().intValue();
                System.out.println("======> insert Integral...");
                integralDao.insert(userId, Integer.parseInt(task.getTypeTwo()), integralNum, task.getId(), task.getTitle());
            }
        }
    }


    /**
     * 上传视频获取积分
     * @param userId 用户id
     */
    public void getIntegralFromActivity(Long userId, Long videoId) {

        List<Task> taskList = integralDao.selectSpecialVideoTask();
        List<Task> dailyTasks = integralDao.selectDailyVideoTask();
        List<String> activityIds = integralDao.findActivityIdsByVideoId(userId, videoId);
        if (taskList == null || taskList.size() == 0) {
            taskList = new ArrayList<>();
        }
        if (dailyTasks != null && dailyTasks.size() > 0) {
            taskList.addAll(dailyTasks);
        }
        // 根据type查询出任务列表，可能有多个特殊任务
        for (Task task : taskList) {
            if (task != null && activityIds != null && activityIds.size() > 0) {
                // 如果有多个活动 遍历
                for (String actId : activityIds) {
                    Date limitDate = task.getDeadLine(); // 活动限期
                    int integralCount = integralDao.selectIntegral(userId, task.getId(),
                            TaskTypeOneEnum.daily.getValue().equals(Integer.parseInt(task.getTypeOne())));
                    if (integralCount > 0 || !StringUtils.isNumeric(actId)) {
                        /** 判断1 是否已经获得该任务积分 **/
                        continue;
                    }

                    int actCount = 0;
                    if (TaskTypeSecondEnum.specialActivity.getValue().equals(Integer.parseInt(task.getTypeTwo()))
                            && (limitDate == null || limitDate.getTime() > System.currentTimeMillis())
                            && StringUtils.isNotBlank(task.getTaskValue()) && actId.equals(task.getTaskValue())) {
                        /** 判断2 是否在任务限期内
                         *  判断3 任务指定id是否等于视频参与的活动id
                         *  限时任务必须指定一个活动id */
                        actCount = integralDao.countVideoByActivityId(userId, Long.parseLong(actId), false);
                    } else if (TaskTypeSecondEnum.joinActivity.getValue().equals(Integer.parseInt(task.getTypeTwo()))
                            && (task.getTaskValue() == null || task.getTaskValue().equals(actId))) {
                        /** 判断2 是否今天完成
                         *  判断3 任务指定id是否等于视频参与的活动id
                         *  日常任务可不指定活动id */
                        actCount = integralDao.countVideoByActivityId(userId, Long.parseLong(actId), true);
                    } else if (TaskTypeSecondEnum.publishVideo.getValue().equals(Integer.parseInt(task.getTypeTwo()))) {
                        actCount = integralDao.countUserVideoToday(userId);
                    }
                    if ( (task.getNum() == null && actCount > 0)
                            || (StringUtils.isNumeric(task.getNum())
                            && actCount >= Integer.parseInt(task.getNum()))) {
                        int integralNum = task.getIntegral().intValue();
                        integralDao.insert(userId, Integer.parseInt(task.getTypeTwo()), integralNum, task.getId(), task.getTitle());
                    }
                }
            } else if (task != null) {
                if (TaskTypeSecondEnum.publishVideo.getValue().equals(Integer.parseInt(task.getTypeTwo()))) {
                    int integralCount = integralDao.selectIntegral(userId, task.getId(),
                            TaskTypeOneEnum.daily.getValue().equals(Integer.parseInt(task.getTypeOne())));
                    if (integralCount > 0) {
                        /** 判断 是否已经获得该任务积分 **/
                        continue;
                    }
                    int actCount = integralDao.countUserVideoToday(userId);
                    if ((StringUtils.isBlank(task.getNum()) && actCount > 0)
                            || (StringUtils.isNotBlank(task.getNum()) && Integer.parseInt(task.getNum()) <= actCount)) {
                        int integralNum = task.getIntegral().intValue();
                        integralDao.insert(userId, Integer.parseInt(task.getTypeTwo()), integralNum, task.getId(), task.getTitle());
                    }
                }
            }
        }
    }
}
