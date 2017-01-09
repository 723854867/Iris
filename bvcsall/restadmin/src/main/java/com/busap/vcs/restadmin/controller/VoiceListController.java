package com.busap.vcs.restadmin.controller;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.SingConstants;
import com.busap.vcs.data.entity.RuserLiveActivity;
import com.busap.vcs.data.entity.VoiceList;
import com.busap.vcs.data.model.SingVoteDisplay;
import com.busap.vcs.restadmin.utils.ResultData;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RuserLiveActivityService;
import com.busap.vcs.service.SingVoteService;
import com.busap.vcs.service.VoiceListService;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.Page;
import com.busap.vcs.util.page.PagingContextHolder;
import com.busap.vcs.webcomn.U;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by busap on 2016/8/2.
 */
@Controller
@RequestMapping("/voiceList")
public class VoiceListController {

    @Resource
    private VoiceListService voiceListService;

    @Resource
    private JedisService jedisService;

    @Resource
    private SingVoteService singVoteService;

    @Resource
    private RuserLiveActivityService ruserLiveActivityService;

    @RequestMapping("forwardVoiceList")
    public ModelAndView forwardVoiceList(Long id){
        ModelAndView mav = new ModelAndView();
        VoiceList voiceList = voiceListService.queryVoiceList(id);
        mav.addObject("voiceListName",voiceList.getName());
        mav.addObject("voiceListStartTime",voiceList.getStartTime());
        mav.addObject("voiceListEndTime",voiceList.getEndTime());
        mav.setViewName("voiceList/query_voice_list");
        return mav;
    }

    @RequestMapping("insertVoiceList")
    @ResponseBody
    public ResultData insertVoiceList(String name,Long type,String startTime,String endTime,Integer personNumber,String url) throws ParseException {
        ResultData resultData = new ResultData();
        VoiceList voiceList = new VoiceList();
        DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = fmt.parse(startTime);
        Date end = fmt.parse(endTime);
        voiceList.setName(name);
        voiceList.setType(type);
        voiceList.setCreateTime(new Date());
        voiceList.setStartTime(start);
        voiceList.setEndTime(end);
        voiceList.setPersonNumber(personNumber);
        voiceList.setUrl(url);
        int result = voiceListService.insert(voiceList);
        if(result > 0){
            resultData.setResultCode("success");
            resultData.setResultMessage("成功");
        }else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("失败");
        }
        return resultData;
    }

    @RequestMapping("insertStudent")
    @ResponseBody
    public ResultData insertStudent(Long id){
        ResultData resultData = new ResultData();
        jedisService.setValueToSetInShard(SingConstants.SING_MEMBER, String.valueOf(id));
        String liveActivityId = jedisService.get(SingConstants.SING_ACTIVITY_ID);
        if (ruserLiveActivityService.isJoin(id, Long.valueOf(liveActivityId)) == 0){ //没参加过的，允许参加活动
            RuserLiveActivity ruserLiveActivity = new RuserLiveActivity();
            ruserLiveActivity.setCreatorId(id);
            ruserLiveActivity.setLiveActivityId(Long.valueOf(liveActivityId));
            ruserLiveActivityService.save(ruserLiveActivity);
        }
        resultData.setResultCode("success");
        resultData.setResultMessage("成功");
        return resultData;
    }

    @RequestMapping("querySingMember")
    @ResponseBody
    public Map<String,Object> querySingMember(@ModelAttribute("query") JQueryPage queryPage){
        Map<String,Object> map = new HashMap<String,Object>();
        Set<String> singMember = jedisService.getSetFromShard(SingConstants.SING_MEMBER);
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (String sm : singMember){
            Map<String, String> userInfo = jedisService.getMapByKey(BicycleConstants.USER_INFO + sm);
            Map<String,String> student = new HashMap<String, String>();
            student.put("id",sm);
            student.put("name",userInfo.get("name"));
            student.put("pic",userInfo.get("pic"));
            student.put("corps",userInfo.get("corps"));
            student.put("createDate",userInfo.get("createDate"));
            Long popularity = singVoteService.getPopularity(Long.valueOf(sm));
            student.put("popularity", String.valueOf(popularity == null?0:popularity));
            list.add(student);
        }
        map.put("rows", list);
        map.put("total", singMember.size());
        return map;
    }

    @RequestMapping("settingCorps")
    @ResponseBody
    public ResultData settingCorps(Long id,Long tutorId){
        ResultData resultData = new ResultData();
        jedisService.setValueToMap(BicycleConstants.USER_INFO +id,"corps", String.valueOf(tutorId));
        resultData.setResultCode("success");
        resultData.setResultMessage("成功");
        return resultData;
    }

    @RequestMapping("forwardVarietyList")
    public ModelAndView forwardVarietyList(Long id){
        ModelAndView mav = new ModelAndView();
        VoiceList voiceList = voiceListService.queryVoiceList(id);
        mav.addObject("selected","variety");
        mav.addObject("voiceListId",id);
        mav.addObject("voiceListName",voiceList.getName());
        mav.addObject("voiceListStartTime",voiceList.getStartTime());
        mav.addObject("voiceListEndTime",voiceList.getEndTime());
        mav.setViewName("voiceList/query_variety_list");
        return mav;
    }

    @RequestMapping("forwardAnchorList")
    public ModelAndView forwardAnchorList(Long id){
        ModelAndView mav = new ModelAndView();
        VoiceList voiceList = voiceListService.queryVoiceList(id);
        mav.addObject("voiceListId",id);
        mav.addObject("voiceListName",voiceList.getName());
        mav.addObject("voiceListStartTime",voiceList.getStartTime());
        mav.addObject("voiceListEndTime",voiceList.getEndTime());
        mav.setViewName("voiceList/query_anchor_list");
        return mav;
    }

    @RequestMapping("forwardContributionList")
    public ModelAndView forwardContributionList(Long id){
        ModelAndView mav = new ModelAndView();
        VoiceList voiceList = voiceListService.queryVoiceList(id);
        mav.addObject("voiceListId",id);
        mav.addObject("voiceListName",voiceList.getName());
        mav.addObject("voiceListStartTime",voiceList.getStartTime());
        mav.addObject("voiceListEndTime",voiceList.getEndTime());
        mav.setViewName("voiceList/query_contribution_list");
        return mav;
    }

    @RequestMapping("forwardList")
    public String forwardList(){
        return "voiceList/query_list";
    }

    @RequestMapping("queryListInfo")
    @ResponseBody
    @EnablePaging
    public Map<String,Object> queryListInfo(@ModelAttribute("query") JQueryPage queryPage){
        Map<String,Object> map = new HashMap<String ,Object>();
        Map<String,Object> params = new HashMap<String, Object>();
        List<VoiceList> list = voiceListService.queryVoiceList(params);
        Page page = PagingContextHolder.getPage();
        map.put("total",page.getTotalResult());
        map.put("rows",list);
        return map;
    }


    @RequestMapping("queryCheatSingVoteInfo")
    @ResponseBody
    @EnablePaging
    public Map<String,Object> queryCheatSingVoteInfo(Long userId,@ModelAttribute("query") JQueryPage queryPage){
        Map<String,Object> map = new HashMap<String ,Object>();
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("type",4);
        params.put("destId",userId);
        List<SingVoteDisplay> list = singVoteService.querySingVote(params);
        Page page = PagingContextHolder.getPage();
        map.put("total",page.getTotalResult());
        map.put("rows",list);
        return map;
    }

    @RequestMapping("insertPopularity")
    @ResponseBody
    public ResultData insertPopularity(Long userId,String createTime,Long popularity) throws ParseException {
        ResultData resultData = new ResultData();
        DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date cTime = fmt.parse(createTime);
        try {
            singVoteService.manualVote(cTime,userId,popularity,U.getUid());
            resultData.setResultCode("success");
            resultData.setResultMessage("成功");
        }catch (Exception e){
            resultData.setResultCode("fail");
            resultData.setResultMessage("失败");
        }
        return resultData;
    }

    @RequestMapping("deleteSingMember")
    @ResponseBody
    public ResultData deleteSingMember(Long userId) throws ParseException {
        ResultData resultData = new ResultData();
        jedisService.deleteSetItemFromShard(SingConstants.SING_MEMBER, String.valueOf(userId));
        jedisService.deleteValueFromMap(BicycleConstants.USER_INFO+userId,"corps");
        resultData.setResultCode("success");
        resultData.setResultMessage("成功");
        return resultData;
    }

    @RequestMapping("queryVoiceListInfoById")
    @ResponseBody
    public Map<String,Object> queryVoiceListInfoById(Long id,@ModelAttribute("query") JQueryPage queryPage){
        VoiceList voiceList = voiceListService.queryVoiceList(id);
        Map<String,Object> map = new HashMap<String ,Object>();
        if(voiceList != null){
            String type = String.valueOf(voiceList.getType());
            List<Map<String,Object>> mapList = singVoteService.getRank(Integer.valueOf(type),voiceList.getPersonNumber(),voiceList.getStartTime(),voiceList.getEndTime(),null);
            for (Map<String,Object> voiceListMap : mapList){
                String name = "";
                if(voiceList.getType() == 4 || voiceList.getType() == 5) {
                    name = jedisService.getValueFromMap(BicycleConstants.USER_INFO + voiceListMap.get("creatorId"), "name");
                }else{
                    name = jedisService.getValueFromMap(BicycleConstants.USER_INFO + voiceListMap.get("destId"), "name");
                }
                voiceListMap.put("name",name);
            }
            map.put("total",mapList.size());
            map.put("rows",mapList);
        }
        return map;
    }

    @RequestMapping("deleteVoiceList")
    @ResponseBody
    public ResultData deleteVoiceList(Long id){
        ResultData resultData = new ResultData();
        VoiceList voiceList = voiceListService.queryVoiceList(id);
        if(voiceList != null){
            voiceList.setState(0L);
            voiceList.setUpdateTime(new Date());
            int result = voiceListService.updateByPrimaryKey(voiceList);
            if(result > 0){
                resultData.setResultCode("success");
                resultData.setResultMessage("成功");
            }else{
                resultData.setResultCode("fail");
                resultData.setResultMessage("失败");
            }
        }else{
            resultData.setResultCode("fail");
            resultData.setResultMessage("此条记录不存在！");
        }

        return resultData;
    }

}
