package com.busap.vcs.service.impl;

import com.busap.vcs.base.IntegralStatus;
import com.busap.vcs.base.IntegralType;
import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.*;
import com.busap.vcs.data.enums.TaskTypeOneEnum;
import com.busap.vcs.data.enums.TaskTypeSecondEnum;
import com.busap.vcs.data.mapper.GeneralCustomDAO;
import com.busap.vcs.data.mapper.IntegralDAO;
import com.busap.vcs.data.mapper.IntegralMapper;
import com.busap.vcs.data.model.IntegralStaDisplay;
import com.busap.vcs.data.vo.AttentionVO;
import com.busap.vcs.data.vo.IntegralDetailVO;
import com.busap.vcs.data.vo.IntegralVO;
import com.busap.vcs.data.vo.UserTaskVO;
import com.busap.vcs.service.*;
import com.busap.vcs.util.DateFormatUtils;
import com.busap.vcs.util.DateUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

import static com.busap.vcs.service.Constants.*;

/**
 * 积分服务
 * Created by Yangxinyu on 15/9/29.
 */
@Service("integralService")
public class IntegralServiceImpl implements IntegralService {

    @Autowired
    private IntegralMapper integralMapper;

    @Autowired
    private IntegralDAO integralDAO;

    @Autowired
    private GeneralCustomDAO generalCustomDAO;

    @Resource(name = "taskService")
    private TaskService taskService;

    @Resource(name="signUserService")
    private SignUserService signUserService;

    @Resource(name="attentionService")
    private AttentionService attentionService;

    @Resource(name="ruserService")
    private RuserService ruserService;

    @Resource(name="jedisService")
    private JedisService jedisService;

    @Resource(name = "activityVideoService")
    private ActivityVideoService activityVideoService;


    private Logger logger = LoggerFactory.getLogger(IntegralServiceImpl.class);

    /**
     * 添加积分记录
     * @param userId    用户ID
     * @param integralNum   获得积分数量
     * @param sourceId  任务ID
     * @param taskType  积分类型
     * @param status    状态
     * @return          积分对象
     */
    @Transactional
    public Integral insertIntegral(Long userId,
                                   int integralNum,
                                   Long sourceId,
                                   TaskTypeSecondEnum taskType,
                                   Integer status,
                                   String desc) {
        Integral integral = new Integral();
        integral.setUserId(userId);
        integral.setCreateTime(new Date());
        integral.setModifyTime(new Date());
        if (desc != null && StringUtils.isNotBlank(desc)) {
            integral.setDescription(desc);
        } else {
            integral.setDescription(taskType.getDescription());
        }
        integral.setIntegralNum(integralNum);
        integral.setStatus(status);
        integral.setType(taskType.getValue());
        integral.setSourceId(sourceId);
        integral.setCreateDate(new Date());
        integralMapper.insertSelective(integral);
        integral.setId(generalCustomDAO.findLastID());
        logger.info("insert integral" + JSONObject.fromObject(integral).toString());
//        finishDailyTask(userId, taskType, TaskFinish.FINISH);
        return integral;
    }

    /**
     * 签到获得积分
     * @param userId 用户ID
     */
    @Transactional
    public Map<String, Integer> getIntegralFromSign(Long userId) {
        List<Task> taskList = getTaskByTaskType(TaskTypeSecondEnum.sign, false);
        Long signTaskId;
        if (taskList != null && taskList.size() == 1) {
            signTaskId = taskList.get(0).getId();
        } else {
            return  null;
        }

        // 今日签到获得积分
        int signIntegralNum = 5;
        // 每日签到获得的积分上限
        int signMaxIntegralNum = 35;
        Map<String, Integer> map = new HashMap<String, Integer>();

        IntegralExample example = new IntegralExample();
        example.createCriteria().andUserIdEqualTo(userId)
                .andTypeEqualTo(TaskTypeSecondEnum.sign.getValue());
        example.setOrderByClause(" create_time desc ");
        List<Integral> integralList = integralMapper.selectByExample(example);
        if (integralList == null || integralList.size() == 0) {
            // 没有签到记录
            Integral integral = insertIntegral(userId, signIntegralNum, signTaskId, TaskTypeSecondEnum.sign, IntegralStatus.RECEIVE.getStatus(), null);
            map.put("continueSign", firstSign);
            map.put("signNum", signIntegralNum);
            receiveSignIntegral(integral, false);
        } else {
            // Integral表中有签到记录
            Integral integral = integralList.get(0);
            Calendar today = Calendar.getInstance();
            Calendar yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DATE, -1);

            if (DateUtils.compareDate(yesterday.getTime(), integral.getCreateDate())) {
                // 对比昨天签到的日期 昨天已签到
                signIntegralNum = integral.getIntegralNum();
                if (signIntegralNum < signMaxIntegralNum) {
                    signIntegralNum++;
                }
                Integral result = insertIntegral(userId, signIntegralNum, signTaskId, TaskTypeSecondEnum.sign, IntegralStatus.RECEIVE.getStatus(), null);
                map.put("continueSign", continueSign);
                receiveSignIntegral(result, true);
            } else if (DateUtils.compareDate(today.getTime(), integral.getCreateDate())) {
                // 如果当天已经签到，今日签到获得积分 = 积分记录中的值
                map.put("continueSign", alreadySign);
                signIntegralNum = integral.getIntegralNum();
            } else {
                // 昨天没有签到，从第一天开始
                Integral result = insertIntegral(userId, signIntegralNum, signTaskId, TaskTypeSecondEnum.sign, IntegralStatus.RECEIVE.getStatus(), null);
                map.put("continueSign", firstSign);
                receiveSignIntegral(result, false);
            }
            map.put("signNum", signIntegralNum);
        }

        return map;
    }

    /**
     * 检查关注的用户中是否包含VIP
     * 查询任务列表时读取
     * @param userId 用户ID
     * @param stat   VIP类型
     * @return true:false
     */
    @Transactional
    public boolean checkAttentionTaskFinished(Long userId, USER_VIP_STAT stat) {
        // 取此用户当天关注列表
        List<AttentionVO> attentionList = attentionService.findAttentionByCreator(userId, new Date());

        List<Long> uidList = new ArrayList<Long>();
        for (AttentionVO attentionVO : attentionList) {
            uidList.add(attentionVO.getAttentionId());
        }
        // 查询此用户关注的人的详情
        List<Ruser> ruserList = ruserService.findUsersByIds(uidList);
        for (Ruser user : ruserList) {
            if (user.getVipStat() != null && stat.getStat() == user.getVipStat()) {
                // 判断这些人中如果有对应的vip类型
                TaskTypeSecondEnum taskType;
                if (stat.getStat() == USER_VIP_STAT.BLUE.getStat()) {
                    taskType = TaskTypeSecondEnum.attentionBlueVip;
                } else if (stat.getStat() == USER_VIP_STAT.GREEN.getStat()) {
                    taskType = TaskTypeSecondEnum.attentionGreenVip;
                } else if (stat.getStat() == USER_VIP_STAT.YELLOW.getStat()) {
                    taskType = TaskTypeSecondEnum.attentionYellowVip;
                } else {
                    return false;
                }
                List<Task> taskList = getTaskByTaskType(taskType, true);
                if (taskList != null && taskList.size() > 0) {
                    Task task = taskList.get(0);
                    Integral integral = getIntegral(userId, task.getId(), taskType);
                    if (integral == null) {
                        // 如果当天没有积分记录则添加
                        insertIntegral(userId, 5, task.getId(), taskType, IntegralStatus.CREATE.getStatus(), task.getTitle());
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查关注的人是不是vip用户
     * 在用户执行了关注动作后mi-push.focus方法中调用
     * @param userId        关注人
     * @param attentionId   被关注人
     */
    @Transactional
    public void checkAttentedUserIsVIP(Long userId, Long attentionId) {
        Ruser user = ruserService.find(attentionId);
        if (USER_VIP_STAT.BLUE.getStat() == user.getVipStat()) {
            List<Task> taskList = getTaskByTaskType(TaskTypeSecondEnum.attentionBlueVip, true);
            if (taskList != null && taskList.size() > 0) {
                Task task = taskList.get(0);
                Integral integral = getIntegral(userId, task.getId(), TaskTypeSecondEnum.attentionBlueVip);
                if (integral == null) {
                    insertIntegral(userId, task.getIntegral().intValue(), task.getId(),
                            TaskTypeSecondEnum.attentionBlueVip, IntegralStatus.CREATE.getStatus(), task.getTitle());
                }
            }
        } else if (USER_VIP_STAT.YELLOW.getStat() == user.getVipStat()) {
            List<Task> taskList = getTaskByTaskType(TaskTypeSecondEnum.attentionYellowVip, true);
            if (taskList != null && taskList.size() > 0) {
                Task task = taskList.get(0);
                Integral integral = getIntegral(userId, task.getId(), TaskTypeSecondEnum.attentionYellowVip);
                if (integral == null) {
                    insertIntegral(userId, task.getIntegral().intValue(), task.getId(),
                            TaskTypeSecondEnum.attentionYellowVip, IntegralStatus.CREATE.getStatus(), task.getTitle());
                }
            }

        } else if (USER_VIP_STAT.GREEN.getStat() == user.getVipStat()) {
            List<Task> taskList = getTaskByTaskType(TaskTypeSecondEnum.attentionGreenVip, true);
            if (taskList != null && taskList.size() > 0) {
                Task task = taskList.get(0);
                Integral integral = getIntegral(userId, task.getId(), TaskTypeSecondEnum.attentionGreenVip);
                if (integral == null) {
                    insertIntegral(userId, task.getIntegral().intValue(), task.getId(),
                            TaskTypeSecondEnum.attentionGreenVip, IntegralStatus.CREATE.getStatus(), task.getTitle());
                }
            }
        }
    }

    /**
     * 粉丝到达一定数量获得积分
     * @param userId 用户ID
     */
    @Transactional
    public void getIntegralFromFanNumber(Long userId) {
        Ruser ru = ruserService.find(userId);
        if (ru != null) {
            List<Task> fansTaskList = getTaskByTaskType(TaskTypeSecondEnum.fans, false);
            for (Task task : fansTaskList) {
                if (task != null) {
                    Integral integral = getIntegral(userId, task.getId(), null);
                    if (integral == null && ru.getFansCount() >= Integer.parseInt(task.getTaskValue())) {
                        insertIntegral(userId, task.getIntegral().intValue(), task.getId(),
                                TaskTypeSecondEnum.fans, IntegralStatus.CREATE.getStatus(), task.getTitle());
                    }
                }
            }
        }
    }

    /**
     * 完善个人资料 5积分
     * @param userId 用户ID
     */
    @Transactional
    public void getIntegralFromCompleteInfo(Long userId) {
        List<Task> taskList = getTaskByTaskType(TaskTypeSecondEnum.perfectPersonalInfo, false);
        if (taskList != null && taskList.size() > 0) {
            Task task = taskList.get(0);
            Long sourceId = task.getId();
            Integral integral = getIntegral(userId, sourceId, TaskTypeSecondEnum.perfectPersonalInfo);
            if (integral == null) {
                // 查询是否已有积分记录：已有积分记录则不再添加
                insertIntegral(userId, task.getIntegral().intValue(), sourceId,
                        TaskTypeSecondEnum.perfectPersonalInfo, IntegralStatus.CREATE.getStatus(), task.getTitle());
            }
        }
    }

    /**
     * 完成每日任务得积分
     * @param userId 用户ID
     * @param videoId 指定的视频ID
     * @param taskType 每日任务类型
     */
    @Transactional
    public void getIntegralFromDaily(Long userId, String videoId, TaskTypeSecondEnum taskType) {
        List<Task> taskList = getTaskByTaskType(taskType, true);
        if (taskList != null && taskList.size() > 0) {
            for (Task task : taskList) {
                Long integralNum = task.getIntegral();
                Long taskId = task.getId();
                if (taskId != null && integralNum > 0) {
                    IntegralExample example = new IntegralExample();
                    example.createCriteria().andUserIdEqualTo(userId)
                            .andTypeEqualTo(taskType.getValue())
                            .andSourceIdEqualTo(taskId)
                            .andCreateDateEqualTo(new Date());
                    int count = integralMapper.countByExample(example);
                    if (count == 0 && (task.getTaskValue().equals(videoId) || StringUtils.isBlank(task.getTaskValue()))) {
                        insertIntegral(userId, integralNum.intValue(), taskId, taskType, IntegralStatus.CREATE.getStatus(), task.getTitle());
                    }
                }
            }
        }
    }

    /**
     * 取用户任务状态
     * 若缓存中没有则去查询任务
     * @param userId userId
     * @return Map<任务类型:状态>
     */
    public Map<TaskTypeSecondEnum, TaskFinish> getUserDailyTaskStatus(Long userId) {
        Map<TaskTypeSecondEnum, TaskFinish> taskMap = new HashMap<TaskTypeSecondEnum, TaskFinish>();

        String value = jedisService.get(BicycleConstants.USER_TASK_STATUS + userId);

        if (StringUtils.isNotBlank(value)) {
            char[] chars = value.toCharArray();
            taskMap.put(TaskTypeSecondEnum.attentionBlueVip,  TaskFinish.getTaskFinish(Integer.parseInt(chars[0] + "")));
            taskMap.put(TaskTypeSecondEnum.attentionGreenVip, TaskFinish.getTaskFinish(Integer.parseInt(chars[1] + "")));
            taskMap.put(TaskTypeSecondEnum.attentionYellowVip,   TaskFinish.getTaskFinish(Integer.parseInt(chars[2] + "")));
            taskMap.put(TaskTypeSecondEnum.commentVideo,   TaskFinish.getTaskFinish(Integer.parseInt(chars[3] + "")));
            taskMap.put(TaskTypeSecondEnum.forwardVideo,    TaskFinish.getTaskFinish(Integer.parseInt(chars[4] + "")));
            taskMap.put(TaskTypeSecondEnum.joinActivity,  TaskFinish.getTaskFinish(Integer.parseInt(chars[5] + "")));
            taskMap.put(TaskTypeSecondEnum.praiseVideo,  TaskFinish.getTaskFinish(Integer.parseInt(chars[6] + "")));
            taskMap.put(TaskTypeSecondEnum.publishVideo,  TaskFinish.getTaskFinish(Integer.parseInt(chars[7] + "")));
        } else {
            List<TaskTypeSecondEnum> dailyTasks =
                    Arrays.asList(TaskTypeSecondEnum.attentionBlueVip,
                            TaskTypeSecondEnum.attentionGreenVip,
                            TaskTypeSecondEnum.attentionYellowVip,
                            TaskTypeSecondEnum.commentVideo,
                            TaskTypeSecondEnum.forwardVideo,
                            TaskTypeSecondEnum.joinActivity,
                            TaskTypeSecondEnum.praiseVideo,
                            TaskTypeSecondEnum.publishVideo);

            for (TaskTypeSecondEnum task : dailyTasks) {
                taskMap.put(task, TaskFinish.WORKING);
            }
        }
        return taskMap;
    }

    /**
     * 查询用户所有的任务状态
     * 顺序：限时 > 一次性 > 日常
     * @param userId 用户id
     * @return 任务状态列表: 若TaskFinish=0未完成  key为TaskId, 若TaskFinish=1,2已完成  key为IntegralId
     */
    public List<UserTaskVO> getAllTaskStatusByUserId(Long userId) {
        List<UserTaskVO> taskVOList = new ArrayList<UserTaskVO>();
        // 查询用户限时 & 一次性任务状态
        List<Task> taskList = getTaskByTaskType(null, false);
        List<Long> taskIds = new ArrayList<Long>();
        for (Object object : taskList) {
            Task task = (Task) object;
            taskIds.add(task.getId());
        }

        IntegralExample example  = new IntegralExample();
        example.createCriteria().andUserIdEqualTo(userId).andSourceIdIn(taskIds);
        List<Integral> integralList = integralMapper.selectByExample(example);
        Map<Long, Integral> userIntegrals = new HashMap<Long, Integral>();
        for (Integral integral : integralList) {
            userIntegrals.put(integral.getSourceId(), integral);
        }
        for (Object object : taskList) {
            Task task = (Task) object;
            Integral integral = userIntegrals.get(task.getId());
            UserTaskVO taskVO = new UserTaskVO();
            BeanUtils.copyProperties(task, taskVO);
            if (integral == null) {
                taskVO.setIntegralId(null);
                taskVO.setIsFinished(TaskFinish.WORKING.getStatus());
            } else {
                taskVO.setIntegralId(integral.getId());
                if (IntegralStatus.CREATE.getStatus() == integral.getStatus()) {
                    taskVO.setIsFinished(TaskFinish.FINISH.getStatus());
                } else if (IntegralStatus.RECEIVE.getStatus() == integral.getStatus()) {
                    taskVO.setIsFinished(TaskFinish.RECEIVE.getStatus());
                }
            }
            taskVOList.add(taskVO);
        }

        // 查询用户日常任务状态
        List<Task> dailyTasks = getTaskByTaskTypes(TaskTypeSecondEnum.getDailyTasks(), true);
        example.clear();
        example.createCriteria().andUserIdEqualTo(userId).andCreateDateEqualTo(new Date()).andTypeIn(TaskTypeSecondEnum.getDailyTaskValues(false));
        List<Integral> dailyIntegrals = integralMapper.selectByExample(example);

        for (Task task : dailyTasks) {
            if (task != null) {
                UserTaskVO taskVO = new UserTaskVO();
                BeanUtils.copyProperties(task, taskVO);
                taskVO.setIntegralId(null);
                taskVO.setIsFinished(TaskFinish.WORKING.getStatus());
                for (Integral integral : dailyIntegrals) {
                    if (integral.getType().equals(Integer.parseInt(task.getTypeTwo())) && integral.getSourceId().equals(task.getId())) {
                        taskVO.setIntegralId(integral.getId());
                        if (IntegralStatus.CREATE.getStatus() == integral.getStatus()) {
                            taskVO.setIsFinished(TaskFinish.FINISH.getStatus());
                        } else if (IntegralStatus.RECEIVE.getStatus() == integral.getStatus()) {
                            taskVO.setIsFinished(TaskFinish.RECEIVE.getStatus());
                        }
                    }
                }
                taskVOList.add(taskVO);
            }

        }
        return taskVOList;

    }

    public List<IntegralDetailVO> findUserIntegralDetailList(Long userId, Integer page, Integer size) {
        if (com.busap.vcs.util.BeanUtils.checkParamNotNull(userId, page, size)) {
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("userId", userId);
            params.put("pageStart", (page - 1) * size);
            params.put("pageSize", size);

            return integralDAO.findUserIntegralDetailList(params);
        }
        return null;
    }

    /**
     * 删除积分记录
     * @return 删除数量
     */
    @Transactional
    public Integer deleteIntegralLogByTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date date = calendar.getTime();
        List<Integer> values = TaskTypeSecondEnum.getDailyTaskValues(true);
        IntegralExample example = new IntegralExample();
        example.createCriteria().andCreateDateLessThanOrEqualTo(date).andTypeIn(values);
        return integralMapper.deleteByExample(example);
    }

    /**
     * 保存任务状态
     * @param userId 用户ID
     * @param taskType 任务枚举类型
     * @param taskFinish 任务状态
     */
    private void finishDailyTask(Long userId, TaskTypeSecondEnum taskType, TaskFinish taskFinish) {

        if (userId != null && taskType != null) {
            Map<TaskTypeSecondEnum, TaskFinish> taskMap = getUserDailyTaskStatus(userId);

            taskMap.put(taskType, taskFinish);

            String statusString =
                      String.valueOf(taskMap.get(TaskTypeSecondEnum.attentionBlueVip).getStatus())
                    + String.valueOf(taskMap.get(TaskTypeSecondEnum.attentionGreenVip).getStatus())
                    + String.valueOf(taskMap.get(TaskTypeSecondEnum.attentionYellowVip).getStatus())
                    + String.valueOf(taskMap.get(TaskTypeSecondEnum.commentVideo).getStatus())
                    + String.valueOf(taskMap.get(TaskTypeSecondEnum.forwardVideo).getStatus())
                    + String.valueOf(taskMap.get(TaskTypeSecondEnum.joinActivity).getStatus())
                    + String.valueOf(taskMap.get(TaskTypeSecondEnum.praiseVideo).getStatus())
                    + String.valueOf(taskMap.get(TaskTypeSecondEnum.publishVideo).getStatus());

            jedisService.set(BicycleConstants.USER_TASK_STATUS + userId, statusString);
            // 设为当天最晚时间过期
            jedisService.expireAt(BicycleConstants.USER_TASK_STATUS + userId,
                    DateUtils.parseDate(DateFormatUtils.PATTEN_DEFAULT_FULL, DateUtils.getNowUnixYMDAsStr1()).getTime() / 1000);
        }
    }

    /**
     * 特殊活动获取积分
     * 一次性积分奖励
     * 每发一次视频会调用一次
     * @param userId 用户ID
     * @param activityVideos 活动
     */
    @Transactional
    public void getIntegralFromActivity(Long userId, List<ActivityVideo> activityVideos) {
        List<Task> taskList = getTaskByTaskType(TaskTypeSecondEnum.specialActivity, false);
        List<Task> dailyTasks = getTaskByTaskType(TaskTypeSecondEnum.joinActivity, false);
        taskList.addAll(dailyTasks);
        // 根据type查询出任务列表，可能有多个特殊任务
        for (Task task : taskList) {
            if (task != null) {
                // 如果有多个活动 遍历
                for (ActivityVideo activityVideo : activityVideos) {
                    IntegralExample example = new IntegralExample();
                    example.createCriteria().andUserIdEqualTo(userId).andSourceIdEqualTo(task.getId());

                    Date limitDate = task.getDeadLine(); // 活动限期
                    int count = integralMapper.countByExample(example);
                    logger.info("getIntegralFromActivity count=" + count + " & activityId=" + activityVideo.getActivityid());
                    if (count == 0 && (limitDate == null || limitDate.getTime() > System.currentTimeMillis())
                            && (StringUtils.isBlank(task.getTaskValue())
                            && TaskTypeSecondEnum.joinActivity.getValue().equals(Integer.parseInt(task.getTypeTwo()))
                            || task.getTaskValue().equals(String.valueOf(activityVideo.getActivityid())))) {
                            /** 判断1 是否已经获得该任务积分
                             *  判断2 是否在任务限期内
                             *  判断3 任务指定id是否等于视频参与的活动id
                             *  限时任务必须指定一个活动id , 日常任务可不指定*/
                        if (task.getNum() != null && Long.parseLong(task.getNum()) > 1) {
                            // 如果这个任务要求完成次数大于1
                            Long actCount = activityVideoService.findCountByCreatorIdAndActivityId(userId, activityVideo.getActivityid());
                            // 查询这个任务已经完成的次数
                            if (actCount >= Long.parseLong(task.getNum())) {
                                // 如果已参加的活动次数大于等于任务要求的次数即可
                                int integralNum = task.getIntegral().intValue();
                                String desc = "用户:" + userId + ",发布视频:" + activityVideo.getVideoid()
                                        + ",参与了活动:" + activityVideo.getActivityid();
                                insertIntegral(userId, integralNum, task.getId(), TaskTypeSecondEnum.specialActivity,
                                        IntegralStatus.CREATE.getStatus(), desc);
                            }
                        } else {
                            // 如果这个任务没有要求完成次数
                            int integralNum = task.getIntegral().intValue();
                            String desc = "用户:" + userId + ",发布视频:" + activityVideo.getVideoid()
                                    + ",参与了活动:" + activityVideo.getActivityid();
                            insertIntegral(userId, integralNum, task.getId(), TaskTypeSecondEnum.specialActivity,
                                    IntegralStatus.CREATE.getStatus(), desc);
                        }
                    }
                }
            }
        }
    }

    /**
     * 领取签到积分
     * @param integral 积分记录ID
     */
    @Transactional
    public void receiveSignIntegral(Integral integral, boolean isContinuous) {
        logger.info("receiveSignIntegral integral=" + JSONObject.fromObject(integral).toString() + " isContinuous=" + isContinuous);
        List<SignUser> signUserList = signUserService.findSignUserByuid(String.valueOf(integral.getUserId()));
        Integer sumIntegral = 5;
        if (signUserList != null && signUserList.size() > 0) {
            // 此用户已有积分记录
            SignUser signUser = signUserList.get(0);
            signUser.setContinueSign(isContinuous ? signUser.getContinueSign() + 1 : 1);
            signUser.setSumSignNum(signUser.getSumSignNum() + integral.getIntegralNum());
            signUser.setModifyDate(new Date());
            signUser.setCreatorId(integral.getUserId());
            signUser.setDataFrom(IntegralType.getDescByType(integral.getType()));
            // 更新用户总积分
            logger.info("receiveSignIntegral updateSignUser signUser=" + JSONObject.fromObject(signUser).toString());
            signUserService.update(signUser);
            sumIntegral = integral.getIntegralNum();
        } else {
            // 此用户没有积分记录
            SignUser signUser = new SignUser();
            signUser.setCreateDate(new Date());
            signUser.setContinueSign(1);    // 连续签到第一天
            signUser.setSumSignNum(5);      // 第一次签到积分默认为5
            signUser.setCreatorId(integral.getUserId());
            signUser.setDataFrom(IntegralType.getDescByType(integral.getType()));
            signUserService.save(signUser);
        }
        integral.setStatus(IntegralStatus.RECEIVE.getStatus());
        integral.setModifyTime(new Date());
        integralMapper.updateByPrimaryKeySelective(integral);
        // 更新ruser表
        Ruser ru = ruserService.find(integral.getUserId());
        if (ru != null) {
            int signSum = ru.getSignSum() == null ? 0 : ru.getSignSum();
            int sum = sumIntegral == null ? 0 : sumIntegral;
            ru.setSignSum(signSum + sum);
            ruserService.update(ru);
            // 更新缓存中用户积分
            logger.info("receiveSignIntegral update cache ruser.id=" + ru.getId() + " ruser.signNum=" + ru.getSignSum());
            jedisService.saveAsMap(BicycleConstants.USER_INFO + ru.getId(), ru);
        }
    }

    /**
     * 领取积分
     * @param integralId 积分记录id
     */
    @Transactional
    public IntegralVO receiveIntegral(Long integralId) {
        logger.info("receiveIntegral integralId=" + integralId);
        Integral integral = integralMapper.selectByPrimaryKey(integralId);
        if (integral != null && IntegralStatus.CREATE.getStatus() == integral.getStatus()) {
            if (TaskTypeSecondEnum.getTaskTypeByValue(integral.getType()).getParentType().equals(TaskTypeOneEnum.daily.getValue())
                    && !DateUtils.compareDate(integral.getCreateTime(), new Date())) {
                // 如果是日常任务 且积分记录创建时间不等于今天 则返回   不予领取
                return null;
            }
            // 更新ruser表
            Ruser ru = ruserService.find(integral.getUserId());
            if (ru != null) {
                int integralSum = ru.getSignSum() + integral.getIntegralNum();
                ru.setSignSum(integralSum);
                ruserService.update(ru);
                // 更新缓存中用户积分
                jedisService.saveAsMap(BicycleConstants.USER_INFO + ru.getId(), ru);
                // 更新缓存中用户任务状态
                finishDailyTask(integral.getUserId(), TaskTypeSecondEnum.getTaskTypeByValue(integral.getType()), TaskFinish.RECEIVE);
                IntegralVO integralVO = copyIntegral(integral, integralSum);
                if (integralVO != null) {
                    // 更新积分记录表
                    integral.setStatus(IntegralStatus.RECEIVE.getStatus());
                    integral.setModifyTime(new Date());
                    integralMapper.updateByPrimaryKey(integral);
                }
                return integralVO;
            }
        }
        return null;
    }

    private IntegralVO copyIntegral (Integral integral, Integer integralSum) {
        IntegralVO integralVO = new IntegralVO();
        BeanUtils.copyProperties(integral, integralVO);
        integralVO.setStatus(IntegralStatus.RECEIVE.getStatus());
        integralVO.setModifyTime(new Date());
        integralVO.setIntegralSum(integralSum);
        return integralVO;
    }

    /**
     * 查询积分记录
     * @param userId    用户ID
     * @param sourceId  查询粉丝数达标的任务ID
     * @param taskType  任务类型
     * @return  积分记录
     */
    public Integral getIntegral(Long userId, Long sourceId, TaskTypeSecondEnum taskType) {
        if ((sourceId == null && taskType == null) || userId == null) {
            return null;
        }
        List<Integer> status = new ArrayList<Integer>();
        status.add(IntegralStatus.CREATE.getStatus());
        status.add(IntegralStatus.RECEIVE.getStatus());

        IntegralExample example = new IntegralExample();

        IntegralExample.Criteria criteria = example.createCriteria().andUserIdEqualTo(userId)
                .andStatusIn(status);
        if (sourceId != null) {
            criteria.andSourceIdEqualTo(sourceId);
        }
        if (taskType != null) {
            criteria.andTypeEqualTo(taskType.getValue());
            if (TaskTypeOneEnum.daily.getValue().equals(taskType.getParentType())) {
                // 如果是日常任务  则添加创建日期
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                Date date = calendar.getTime();
                criteria.andCreateDateEqualTo(date);
            }
        }
        example.setOrderByClause(" create_time desc ");
        List<Integral> integralList = integralMapper.selectByExample(example);
        if (integralList.size() > 0) {
            return integralList.get(0);
        } else {
            return null;
        }
    }


    public List<Integral> findSignIntegralList(Long userId, TaskTypeSecondEnum taskType, IntegralStatus status) {
        if (userId == null || taskType == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        IntegralExample example = new IntegralExample();

        IntegralExample.Criteria criteria = example.createCriteria()
                .andUserIdEqualTo(userId).andTypeEqualTo(taskType.getValue())
                .andStatusEqualTo(status.getStatus())
                .andCreateTimeGreaterThanOrEqualTo(calendar.getTime());
        // 查询当月的签到记录
        criteria.andTypeEqualTo(taskType.getValue());
        if (TaskTypeOneEnum.daily.getValue().equals(taskType.getParentType())) {
            // 如果是日常任务  则添加创建日期
            criteria.andCreateDateEqualTo(new Date());
        }
        return integralMapper.selectByExample(example);
    }

    /**
     * 查询用户当前总积分
     * @param userId 用户id
     * @return 积分数量
     */
    public Integer getUserIntegralSum(Long userId) {
        Map<String, String> map = jedisService.getMapByKey(BicycleConstants.USER_INFO + userId);
        // 先查缓存
        if (map != null && map.size() > 0) {
            String sumString = map.get("signSum");
            return Integer.parseInt(sumString);
        } else {
            // 缓存中没有再查表
            Ruser ruser = ruserService.find(userId);
            if (ruser != null) {
                return ruser.getSignSum();
            } else {
                return null;
            }
        }
    }

    /**
     * 任务前置过滤
     * @param taskVOList 任务列表
     * @return 任务列表
     */
    public List<UserTaskVO> taskVOFilter(List<UserTaskVO> taskVOList) {
        Map<Long, UserTaskVO> taskVOMap = new HashMap<Long, UserTaskVO>();
        for (UserTaskVO taskVO : taskVOList) {
            taskVOMap.put(taskVO.getId(), taskVO);
        }
        List<UserTaskVO> resultList = new ArrayList<UserTaskVO>();
        // 如果前置任务没完成  则移除掉当前任务
        for (UserTaskVO taskVO : taskVOList) {
            String desc = taskVO.getDescription();
            if (StringUtils.isNotBlank(desc)) {
                desc = desc.replaceAll("\\r\\n", "");
                taskVO.setDescription(desc);
            }
            if (TaskFinish.RECEIVE.getStatus() == taskVO.getIsFinished()) {
                // 过滤掉已完成任务
                continue;
            }
            if (taskVO.getTypeTwo().equals(String.valueOf(TaskTypeSecondEnum.sign.getValue()))) {
                // 过滤掉签到任务
                continue;
            }
            if (TaskTypeSecondEnum.specialActivity.getValue().equals(Integer.parseInt(taskVO.getTypeTwo()))
                    && taskVO.getDeadLine() != null && taskVO.getDeadLine().before(new Date())) {
                // 过滤掉已过期任务
                continue;
            }
            if (taskVO.getPreviousTaskId() != null && taskVO.getPreviousTaskId() > 0) {
                UserTaskVO previousTask = taskVOMap.get(taskVO.getPreviousTaskId());
                if (previousTask == null || previousTask.getIsFinished() != TaskFinish.WORKING.getStatus()) {
                    resultList.add(taskVO);
                }
            } else {
                resultList.add(taskVO);
            }
        }
        return resultList;
    }

    /**
     * 根据taskType查询任务详情
     * 先查缓存，缓存中没有再查数据库
     *
     * @param taskType 任务类型  null:取所有任务
     * @param includeDaily 是否包含日常任务
     * @return 任务列表
     */
    private List<Task> getTaskByTaskType(TaskTypeSecondEnum taskType, boolean includeDaily) {
        List<Task> taskList = new ArrayList<Task>();
//        String key = taskType == null ?  ALL_TASK_KEY : (TASK_KEY + taskType);
//        Object object = jedisService.getObject(key);
//
//        if (object != null && object instanceof ArrayList<?>) {
//            List objList = (List) object;
//            for (Object obj : objList) {
//                Task task = new Task();
//                if (obj != null) {
//                    BeanUtils.copyProperties(task, obj);
//                }
//                taskList.add(task);
//            }
//        } else {
            StringBuffer hql = new StringBuffer();
            StringBuffer countHql = new StringBuffer();
            hql.append("FROM Task task where task.status=0 ");
            countHql.append("SELECT COUNT(*) FROM Task task where task.status=0  ");
            if (taskType != null) {
                hql.append(" and task.typeTwo= ");
                hql.append(taskType.getValue());
                countHql.append(" and task.typeTwo= ");
                countHql.append(taskType.getValue());
            }
            if (!includeDaily) {
                hql.append(" and task.typeOne!=1 ");
                countHql.append(" and task.typeOne!=1 ");
            }

            List<OrderByBean> orderByList=new ArrayList<OrderByBean>();

            OrderByBean orderByObject1=new OrderByBean("typeOne",0,"task");
            orderByList.add(orderByObject1);

            OrderByBean orderByObject2=new OrderByBean("weight",0,"task");
            orderByList.add(orderByObject2);
            try {
                // 一次查询出全部任务
                logger.info("getTaskByTaskType count sql = " + countHql);
                Long count = taskService.getObjectCountByJpql(countHql, null);
                logger.info("getTaskByTaskType count = " + count + "  select sql = " + hql);
                List objList = taskService.getObjectByJpql(hql, 1, count.intValue(), "task", null, null, orderByList);

                for (Object obj : objList) {
                    Task task = new Task();
                    if (obj != null) {
                        BeanUtils.copyProperties(obj, task);
                    }
                    taskList.add(task);
                }
//                jedisService.saveAsMapByList(key, taskList);
            } catch (Exception e) {
                e.printStackTrace();
            }
//        }
        return taskList;
    }

    private List<Task> getTaskByTaskTypes(List<TaskTypeSecondEnum> taskTypes, boolean includeDaily) {

        List<Task> taskList = new ArrayList<Task>();
        StringBuilder str = new StringBuilder();
        if (taskTypes.size() > 0) {
            for (TaskTypeSecondEnum type : taskTypes) {
                if (type != null) {
                    str.append(",").append(type.getValue());
                }
            }
        }
        String types = str.deleteCharAt(0).toString();
        StringBuffer hql = new StringBuffer();
        hql.append("FROM Task task where task.status=0 ");
        if (StringUtils.isNotBlank(types)) {
            hql.append(" and task.typeTwo in (").append(types).append(")");
        }
        if (!includeDaily) {
            hql.append(" and task.typeOne!=1 ");
        }

        List<OrderByBean> orderByList=new ArrayList<OrderByBean>();

        OrderByBean orderByObject1=new OrderByBean("typeOne",0,"task");
        orderByList.add(orderByObject1);

        OrderByBean orderByObject2=new OrderByBean("weight",0,"task");
        orderByList.add(orderByObject2);
        try {
            // 一次查询出全部任务
            List objList = taskService.getObjectByJpql(hql, 1, 100, "task", null, null, orderByList);
            for (Object obj : objList) {
                Task task = new Task();
                if (obj != null) {
                    BeanUtils.copyProperties(obj, task);
                }
                taskList.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return taskList;
    }
    
    public List<IntegralStaDTO> integralMultiTypeStatistics() {

        return integralDAO.integralMultiTypeStatistics();
    }

    @Override
    public List<IntegralStaDisplay> queryIntegralMultiTypeStatistics(){
        return integralDAO.selectIntegralMultiTypeStatistics();
    }
}
