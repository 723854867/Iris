package com.busap.vcs.data.mapper;


import com.busap.vcs.data.entity.Label;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface LabelDAO {

    /**
     * 获取label信息列表
     * @param params
     * @return Label List
     */
    List<Label> selectLabels(Map<String,Object> params);

    /**
     * 获取label信息列表Count
     * @param
     * @return Integer Count
     */
    Integer selectLabelsCount(Map<String,Object> params);

}
