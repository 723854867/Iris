package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.VoiceList;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by busap on 2016/8/2.
 */
@Repository
public interface VoiceListDao {

    int insert(VoiceList voiceList);

    List<VoiceList> selectVoiceList(Map<String,Object> params);

    VoiceList selectByPrimaryKey(Long id);

    int updateByPrimaryKey(VoiceList voiceList);
}
