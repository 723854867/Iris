package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.Anchor;
import com.busap.vcs.data.model.AnchorDetailDisplay;
import com.busap.vcs.data.model.AnchorIncome;
import com.busap.vcs.data.model.AnchorInfo;
import com.busap.vcs.data.model.OrganizationAnchorDisplay;

import java.util.List;
import java.util.Map;

public interface AnchorDao {

    /**
     * 主播收益查询
     */
    List<AnchorIncome> selectAnchorIncome(Map<String, Object> map);

    Anchor selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Anchor anchor);

    List<Anchor> select(Map<String,Object> params);

	Long getAnchorTotalPoints(Map<String, Object> params);

    AnchorDetailDisplay selectAnchorLiveDetail(Map<String, Object> params);

    List<AnchorDetailDisplay> selectAnchorLiveDetailRecord(Map<String, Object> params);

    AnchorInfo selectAnchorByStreamId(Map<String,Object> params);

    List<OrganizationAnchorDisplay> selectOrganizationAnchorList(Map<String,Object> params);

    Long selectMaxRoomIdByStreamId(Map<String,Object> params);

}