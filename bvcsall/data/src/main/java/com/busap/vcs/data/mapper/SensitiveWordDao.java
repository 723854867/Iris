package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.SensitiveWord;

import java.util.List;
import java.util.Map;

/**
 * Created by huoshanwei on 2015/10/14.
 */
public interface SensitiveWordDao {

    List<SensitiveWord> select(Map<String,Object> params);

/*    Integer selectCount(Map<String,Object> params);*/

    int update(SensitiveWord sensitiveWord);

    int insert(SensitiveWord sensitiveWord);

    int batchInsertSensitiveWord(List<SensitiveWord> list);

    int deleteByPrimaryKey(Long id);

    SensitiveWord selectByPrimaryKey(Long id);

}
