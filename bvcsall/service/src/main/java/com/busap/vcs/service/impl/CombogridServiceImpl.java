package com.busap.vcs.service.impl;

import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.mapper.CombogridDao;
import com.busap.vcs.service.CombogridService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by huoshanwei on 2015/11/30.
 */
@Service("combogridService")
public class CombogridServiceImpl implements CombogridService {

    @Resource
    private CombogridDao combogridDao;

    @Override
    public List<Ruser> queryUserCombogridByType(Map<String,Object> params){
        return combogridDao.selectUserCombogridByType(params);
    }

}
