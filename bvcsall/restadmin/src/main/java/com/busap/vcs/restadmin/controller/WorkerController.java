package com.busap.vcs.restadmin.controller;

import com.busap.vcs.restadmin.utils.EnableFunction;
import com.busap.vcs.restadmin.utils.WorkerTrigger;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.SimpleTriggerBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/worker")
public class WorkerController {

    private static final Logger logger = Logger.getLogger(WorkerController.class);

    @Resource
    private Scheduler schedulerFactory;

    @RequestMapping(value = "/forwardWorkers")
    public String forwardWorkers() {
        return "workers";
    }

    @EnableFunction("排行榜定时任务,查看排行榜定时任务")
    @RequestMapping(value = "/queryWorkers")
    @ResponseBody
    public Map<String, Object> queryWorkers() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            List<WorkerTrigger> wtList = getWorkerTriggers();
            logger.info("worker count：" + wtList.size());
            resultMap.put("total", wtList.size());
            resultMap.put("rows", wtList);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @EnableFunction("排行榜定时任务,恢复排行榜定时任务")
    @RequestMapping(value = "resumeWorker")
    @ResponseBody
    public String resumeWorker(WorkerTrigger workerTrigger) {
        try {
            logger.info("resumeWorker 信息" + workerTrigger.getName() + ":" + workerTrigger.getGroup());
            schedulerFactory.resumeTrigger(workerTrigger.getName(), workerTrigger.getGroup());
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "任务激活失败";
        }
        return "任务激活成功";
    }

    @EnableFunction("排行榜定时任务,暂停排行榜定时任务")
    @RequestMapping(value = "pauseWorker")
    @ResponseBody
    public String pauseWorker(WorkerTrigger workerTrigger) {
        try {
            logger.info("pauseWorker 信息" + workerTrigger.getName() + ":" + workerTrigger.getGroup());
            schedulerFactory.pauseTrigger(workerTrigger.getName(), workerTrigger.getGroup());
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "任务暂停失败";
        }
        return "任务暂停成功";
    }

    @EnableFunction("排行榜定时任务,更新排行榜定时任务")
    @RequestMapping(value = "/updateWorker", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String updateWorker(WorkerTrigger workerTrigger) {
        try {
            if (StringUtils.isNotBlank(workerTrigger.getCronExpression())) {
                CronTriggerBean temp = (CronTriggerBean) schedulerFactory.getTrigger(workerTrigger.getName(), workerTrigger.getGroup());
                logger.info("Trigger CronExpression：" + workerTrigger.getCronExpression());
                temp.setCronExpression(workerTrigger.getCronExpression());
                schedulerFactory.rescheduleJob(temp.getName(), temp.getGroup(), temp);
            } else if (workerTrigger.getRepeatInterval() > 0) {
                SimpleTriggerBean temp = (SimpleTriggerBean) schedulerFactory.getTrigger(workerTrigger.getName(), workerTrigger.getGroup());
                logger.info("Trigger RepeatInterval：" + workerTrigger.getRepeatInterval());
                temp.setRepeatInterval(temp.getRepeatInterval());
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "任务更新失败";
        } catch (ParseException e) {
            e.printStackTrace();
            return "任务更新失败";
        }
        return "任务更新成功";
    }


    /**
     * 获取定时任务列表
     *
     * @return
     * @throws Exception
     */
    private List<WorkerTrigger> getWorkerTriggers() throws SchedulerException {
        // 获取定时任务分组信息
        String[] groupNames = schedulerFactory.getTriggerGroupNames();
        List<WorkerTrigger> workerTriggerList = new ArrayList<WorkerTrigger>();
        for (String groupName : groupNames) {
            // 获取每个分给下的trigger名称集合
            String[] triggerNames = schedulerFactory.getTriggerNames(groupName);
            for (String triggerName : triggerNames) {
                // 获取单个trigger
                Trigger trigger = schedulerFactory.getTrigger(triggerName, groupName);
                WorkerTrigger wt = new WorkerTrigger();
                wt.setGroup(trigger.getGroup());
                wt.setName(trigger.getName());
                wt.setPreviousFireTime(trigger.getPreviousFireTime());
                wt.setNextFireTime(trigger.getNextFireTime());
                wt.setStartTime(trigger.getStartTime());
                wt.setEndTime(trigger.getEndTime());
                wt.setPriority(trigger.getPriority());
                wt.setTriggerState(schedulerFactory.getTriggerState(triggerName, groupName));
                if (trigger instanceof CronTriggerBean) {
                    wt.setTriggerType("CronTrigger");
                    CronTriggerBean temp = (CronTriggerBean) schedulerFactory.getTrigger(triggerName, groupName);
                    wt.setCronExpression(temp.getCronExpression());
                }
                if (trigger instanceof SimpleTriggerBean) {
                    wt.setTriggerType("SimpleTrigger");
                    SimpleTriggerBean temp = (SimpleTriggerBean) schedulerFactory.getTrigger(triggerName, groupName);
                    wt.setRepeatInterval(temp.getRepeatInterval());
                }
                workerTriggerList.add(wt);
            }
        }
        return workerTriggerList;
    }
}
