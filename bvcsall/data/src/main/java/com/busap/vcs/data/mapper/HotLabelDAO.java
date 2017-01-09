package com.busap.vcs.data.mapper;


import com.busap.vcs.data.entity.HotLabel;

import java.util.List;
import java.util.Map;

public interface HotLabelDAO {

    /**
     * 获取HotLabel信息列表
     * @param
     * @return Label List
     */
    List<HotLabel> selectHotLabels(Map<String, Object> params);

    /**
     * 获取HotLabel信息列表Count
     * @param
     * @return Integer Count
     */
/*    Integer selectHotLabelsCount(Map<String, Object> params);*/

    int insert(HotLabel hotLabel);

}
