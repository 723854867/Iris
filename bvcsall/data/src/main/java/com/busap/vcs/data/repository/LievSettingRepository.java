package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.LiveSetting;

@Resource(name = "lievSettingRepository")
public interface LievSettingRepository extends BaseRepository<LiveSetting, Long> {

}
