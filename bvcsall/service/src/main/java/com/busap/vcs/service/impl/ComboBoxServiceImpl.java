package com.busap.vcs.service.impl;

import com.busap.vcs.data.mapper.ComboBoxDao;
import com.busap.vcs.data.model.ComboBoxData;
import com.busap.vcs.service.ComboBoxService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huoshanwei on 2015/9/22.
 */
@Service("comboBoxService")
public class ComboBoxServiceImpl implements ComboBoxService {

    @Resource
    private ComboBoxDao comboBoxDao;

    @Override
    public List<ComboBoxData> queryMusicTypeComboBox(String selected){
        List<ComboBoxData> list = comboBoxDao.selectMusicTypeComboBox();
        return comboboxSelected(list,selected);
    }

    @Override
    public List<ComboBoxData> queryUserComboBox(String selected){
        List<ComboBoxData> list = comboBoxDao.selectUserComboBox();
        return comboboxSelected(list, selected);
    }

    @Override
    public List<ComboBoxData> queryComboBoxLabels(String selected){
        List<ComboBoxData> list = comboBoxDao.selectComboBoxLabels();
        return comboboxSelected(list, selected);
    }

    @Override
    public List<ComboBoxData> queryActivityCombobox(String selected){
        List<ComboBoxData> list = comboBoxDao.selectActivityCombobox();
        return comboboxSelected(list, selected);
    }

    @Override
    public List<ComboBoxData> queryPermissionCombobox(String selected){
        List<ComboBoxData> list = comboBoxDao.selectPermissionCombobox();
        return comboboxSelected(list, selected);
    }

    @Override
    public List<ComboBoxData> queryUserComboBoxByType(String selected,Integer type){
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("type",type);
        List<ComboBoxData> list = comboBoxDao.selectUserComboBoxByType(params);
        return comboboxSelected(list, selected);
    }

    @Override
    public List<ComboBoxData> queryOrganizationList(String selected){
        List<ComboBoxData> list = comboBoxDao.selectOrganizationComboBox();
        return comboboxSelected(list, selected);
    }

    @Override
    public List<ComboBoxData> selectSingerTypeComboBox(String selected) {
        List<ComboBoxData> list =  comboBoxDao.selectSingerTypeComboBox();
        return comboboxSelected(list, selected);
    }

    @Override
    public List<ComboBoxData> selectSingerComboBox(String selected) {
        List<ComboBoxData> list =  comboBoxDao.selectSingerComboBox();
        return comboboxSelected(list, selected);
    }

    @Override
    public List<ComboBoxData> selectAlbumComboBox(String selected) {
        List<ComboBoxData> list =  comboBoxDao.selectAlbumComboBox();
        return comboboxSelected(list, selected);
    }

    @Override
    public List<ComboBoxData> selectRegPlatformCombobox(String selected) {
        List<ComboBoxData> list =  comboBoxDao.selectRegPlatformCombobox();
        return comboboxSelected(list, selected);
    }

    /**
     * 设置列表默认选择项
     *
     * @param comboboxDataList 下拉列表
     * @param selected         默认选中
     * @return 下拉列表
     */
    private List<ComboBoxData> comboboxSelected(List<ComboBoxData> comboboxDataList, String selected) {
        List<ComboBoxData> comboboxDatas = new ArrayList<ComboBoxData>();
        ComboBoxData selectedData = new ComboBoxData();
        selectedData.setId("");
        selectedData.setText("请选择");
        comboboxDatas.add(selectedData);
        comboboxDatas.addAll(comboboxDataList);

        // 判断是否有默认选择
        if (StringUtils.isNotBlank(selected)) {
            for (ComboBoxData data : comboboxDatas) {
                // 设置默认选择项
                if (selected.equals(data.getId())) {
                    data.setSelected(true);
                    break;
                }
            }
        } else {
            comboboxDatas.get(0).setSelected(true);
        }
        return comboboxDatas;
    }

}
