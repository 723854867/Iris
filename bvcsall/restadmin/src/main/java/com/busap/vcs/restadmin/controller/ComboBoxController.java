package com.busap.vcs.restadmin.controller;

import com.busap.vcs.data.entity.LiveActivity;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.model.ActivityDisplay;
import com.busap.vcs.data.model.ComboBoxData;
import com.busap.vcs.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by busap on 2015/9/23.
 */
@Controller
@RequestMapping("comboBox")
public class ComboBoxController {

    @Resource
    private ComboBoxService comboBoxService;

    @Resource
    private ActivityService activityService;

    @Resource
    private RuserService ruserService;

    @Resource
    private LiveActivityService liveActivityService;


    @RequestMapping("getMusicTypeComboBox")
    @ResponseBody
    public List<ComboBoxData> getMusicTypeComboBox() {
        return comboBoxService.queryMusicTypeComboBox("");
    }

    @RequestMapping("getUserComboBox")
    @ResponseBody
    public List<ComboBoxData> getUserComboBox() {
        return comboBoxService.queryUserComboBox("");
    }

    @RequestMapping("getActivitiesCombogrid")
    @ResponseBody
    public List<ActivityDisplay> getActivitiesCombogrid() {
        Map<String, Object> params = new HashMap<String, Object>();
        return activityService.queryActivities(params);
    }

    @RequestMapping("queryCombogridUsers")
    @ResponseBody
    public List<Ruser> queryCombogridUsers(String userType) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (!"all".equals(userType)) {
            params.put("type", userType);
        }
        return ruserService.selectRusers(params);
    }

    @RequestMapping("queryComboBoxLabels")
    @ResponseBody
    public List<ComboBoxData> queryComboBoxLabels() {
        return comboBoxService.queryComboBoxLabels("");
    }

    @RequestMapping("getActivityCombobox")
    @ResponseBody
    public List<ComboBoxData> getActivityCombobox() {
        return comboBoxService.queryActivityCombobox("");
    }

    @RequestMapping("getPermissionCombobox")
    @ResponseBody
    public List<ComboBoxData> getPermissionCombobox() {
        return comboBoxService.queryPermissionCombobox("");
    }

    @RequestMapping("getUserComboBoxByType")
    @ResponseBody
    public List<ComboBoxData> getUserComboBoxByType(String selected, Integer type) {
        return comboBoxService.queryUserComboBoxByType(selected, type);
    }

    @RequestMapping("getOrganizationCombobox")
    @ResponseBody
    public List<ComboBoxData> getOrganizationCombobox() {
        return comboBoxService.queryOrganizationList("");
    }

    @RequestMapping("getLiveActivitiesCombogrid")
    @ResponseBody
    public List<LiveActivity> getLiveActivitiesCombogrid() {
        return liveActivityService.findAll();
    }

    @RequestMapping("getSingerTypeCombobox")
    @ResponseBody
    public List<ComboBoxData> getSingerTypeCombobox(@RequestParam(value = "selected", required = false)  String selected) {
        return comboBoxService.selectSingerTypeComboBox(selected);
    }

    @RequestMapping("getSingerCombobox")
    @ResponseBody
    public List<ComboBoxData> getSingerCombobox(@RequestParam(value = "selected", required = false)  String selected) {
        return comboBoxService.selectSingerComboBox(selected);
    }

    @RequestMapping("getAlbumCombobox")
    @ResponseBody
    public List<ComboBoxData> getAlbumCombobox(@RequestParam(value = "selected", required = false)  String selected) {
        return comboBoxService.selectAlbumComboBox(selected);
    }

    @RequestMapping("getRegPlatformCombobox")
    @ResponseBody
    public List<ComboBoxData> getRegPlatformCombobox() {
        List<String> list = ruserService.selectAllRegPlatform();
        List<ComboBoxData> list1 = new ArrayList<ComboBoxData>();
        ComboBoxData cb = new ComboBoxData();
        cb.setId("");
        cb.setText("请选择");
        list1.add(cb);
        for (String name : list){
            ComboBoxData comboBoxData = new ComboBoxData();
            comboBoxData.setId(name);
            comboBoxData.setText(name);
            list1.add(comboBoxData);
        }
        return list1;
    }

}
