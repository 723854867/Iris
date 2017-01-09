package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.Ruser;

import java.util.List;
import java.util.Map;

/**
 * Created by huoshanwei on 2015/11/30.
 */
public interface CombogridDao {

    List<Ruser> selectUserCombogridByType(Map<String,Object> params);

}
