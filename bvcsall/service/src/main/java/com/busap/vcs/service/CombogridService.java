package com.busap.vcs.service;

import com.busap.vcs.data.entity.Ruser;

import java.util.List;
import java.util.Map;

/**
 * Created by huoshanwei on 2015/11/30.
 */
public interface CombogridService {

    List<Ruser> queryUserCombogridByType(Map<String,Object> params);

}
